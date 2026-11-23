/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.html;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the access to local resources (file URLs) by subresources (scripts, images, stylesheets
 * and workers). Like in the browsers, only pages from a file are allowed to load local files.
 * <p>
 * The 'http' tests load the page from the test server and refer to local files (blocked in the
 * browsers); the 'file' tests load the page from a local file and refer to other local files.
 * </p>
 * <p>
 * The local files are written to a temporary directory:
 * </p>
 * <ul>
 * <li>local.js defines the variable 'localScript'</li>
 * <li>local.css sets the color of the element with the id 'd' to red</li>
 * <li>local.png is an image (1x1 pixels)</li>
 * <li>worker.js posts the message 'worker loaded'</li>
 * <li>imported.js defines the variable 'imported' (for importScripts())</li>
 * </ul>
 *
 * @see LocalResourceAccessTest
 * @author Ronald Brill
 */
public class LocalResourceAccess2Test extends WebDriverTestCase {

    private static final Duration WAIT_TIME = Duration.ofSeconds(2);

    private File dir_;
    private String dirUrl_;

    private void createFiles() throws Exception {
        dir_ = Files.createTempDirectory("htmlunit-local").toFile().getCanonicalFile();
        dirUrl_ = dir_.toPath().toUri().toString();

        writeFile("local.js", "var localScript = 'loaded';");
        writeFile("local.css", "#d { color: rgb(255, 0, 0); }");
        writeFile("worker.js", "postMessage('worker loaded');");
        writeFile("imported.js", "var imported = 'yes';");

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-png.img")) {
            FileUtils.copyInputStreamToFile(in, new File(dir_, "local.png"));
        }
    }

    private String writeFile(final String name, final String content) throws Exception {
        final File file = new File(dir_, name);
        FileUtils.writeStringToFile(file, content, UTF_8);
        return file.toPath().toUri().toString();
    }

    /**
     * Deletes the temporary directory.
     */
    @AfterEach
    public void deleteFiles() {
        if (dir_ != null) {
            FileUtils.deleteQuietly(dir_);
            dir_ = null;
        }
    }

    /**
     * A script from a local file in a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error",
                      "undefined"})
    public void http_script() throws Exception {
        createFiles();
        httpTest("<script src='" + dirUrl_ + "local.js' onload=\"log('load')\" onerror=\"log('error')\"></script>\n",
                "log(typeof localScript);");
    }

    /**
     * An image from a local file in a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("error")
    public void http_image() throws Exception {
        createFiles();
        httpTest("<img src='" + dirUrl_ + "local.png' onload=\"log('load ' + this.naturalWidth)\" "
                + "onerror=\"log('error')\">\n", "");
    }

    /**
     * A stylesheet from a local file in a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error",
                      "rgb(0, 0, 0)"})
    public void http_stylesheet() throws Exception {
        createFiles();
        httpTest("<link rel='stylesheet' href='" + dirUrl_ + "local.css' "
                + "onload=\"log('load')\" onerror=\"log('error')\">\n",
                "log(getComputedStyle(document.getElementById('d')).color);");
    }

    /**
     * A stylesheet from a local file imported by a stylesheet of a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(0, 0, 0)")
    public void http_cssImport() throws Exception {
        createFiles();
        httpTest("<style>@import url('" + dirUrl_ + "local.css');</style>\n",
                "log(getComputedStyle(document.getElementById('d')).color);");
    }

    /**
     * A worker from a local file in a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "SecurityError/DOMException",
            FF = {"created",
                  "error"},
            FF_ESR = {"created",
                      "error"})
    public void http_worker() throws Exception {
        createFiles();
        httpTest("", "try {\n"
                + "  var w = new Worker('" + dirUrl_ + "worker.js');\n"
                + "  w.onmessage = function(e) { log(e.data); };\n"
                + "  w.onerror = function(e) { log('error'); };\n"
                + "  log('created');\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * A worker loaded from the server imports a script from a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "NetworkError",
            FF = "SecurityError",
            FF_ESR = "SecurityError")
    public void http_importScripts() throws Exception {
        createFiles();
        final String workerJs = "try {\n"
                + "  importScripts('" + dirUrl_ + "imported.js');\n"
                + "  postMessage('imported ' + imported);\n"
                + "} catch(e) { postMessage(e.name); }";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.TEXT_JAVASCRIPT);

        httpTest("", "var w = new Worker('worker.js');\n"
                + "w.onmessage = function(e) { log(e.data); };");
    }

    /**
     * A script from a local file in a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load",
                      "string"})
    public void file_script() throws Exception {
        fileTest("<script src='local.js' onload=\"log('load')\" onerror=\"log('error')\"></script>\n",
                "log(typeof localScript);");
    }

    /**
     * An image from a local file in a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load 1")
    public void file_image() throws Exception {
        fileTest("<img src='local.png' onload=\"log('load ' + this.naturalWidth)\" onerror=\"log('error')\">\n",
                "");
    }

    /**
     * A stylesheet from a local file in a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load",
                      "rgb(255, 0, 0)"})
    public void file_stylesheet() throws Exception {
        fileTest("<link rel='stylesheet' href='local.css' onload=\"log('load')\" onerror=\"log('error')\">\n",
                "log(getComputedStyle(document.getElementById('d')).color);");
    }

    /**
     * A stylesheet from a local file imported by a stylesheet of a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void file_cssImport() throws Exception {
        fileTest("<style>@import url('local.css');</style>\n",
                "log(getComputedStyle(document.getElementById('d')).color);");
    }

    /**
     * A worker from a local file in a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "SecurityError/DOMException",
            FF = {"created",
                  "worker loaded"},
            FF_ESR = {"created",
                      "worker loaded"})
    public void file_worker() throws Exception {
        fileTest("", "try {\n"
                + "  var w = new Worker('worker.js');\n"
                + "  w.onmessage = function(e) { log(e.data); };\n"
                + "  w.onerror = function(e) { log('error'); };\n"
                + "  log('created');\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * Loads a page from the server; the html is placed in the body, the script runs when the page
     * is loaded. Verifies the log.
     */
    private void httpTest(final String bodyHtml, final String onloadScript) throws Exception {
        final WebDriver driver = loadPage2(page(bodyHtml, onloadScript));
        verifyWindowName2(WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Loads a page from a local file (in the directory of the local files); the html is placed in the body,
     * the script runs when the page is loaded. Verifies the log.
     */
    private void fileTest(final String bodyHtml, final String onloadScript) throws Exception {
        createFiles();
        final String topUrl = writeFile("top.html", page(bodyHtml, onloadScript));

        final WebDriver driver = getWebDriver();
        try {
            driver.get(topUrl);
            verifyWindowName2(WAIT_TIME, driver, getExpectedAlerts());
        }
        finally {
            shutDownAll();
        }
    }

    private static String page(final String bodyHtml, final String onloadScript) {
        return DOCTYPE_HTML
                + "<html><head><title>top</title>\n"
                + "<script>\n"
                + LOG_WINDOW_NAME_FUNCTION
                + "function test() {\n"
                + onloadScript + "\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "<div id='d'>text</div>\n"
                + bodyHtml
                + "</body></html>";
    }
}
