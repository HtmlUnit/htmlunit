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
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * Tests for the access to local resources (file URLs). Like in the browsers, only pages from a file
 * are allowed to load a local file into a frame or a window; about:blank and about:srcdoc documents
 * are judged by the origin of their creator, data: documents have an opaque origin.
 * <p>
 * The 'http' tests load the page from the test server and try to navigate to a local file
 * (blocked in the browsers); the 'file' tests load the page from a local file and navigate to
 * another local file (allowed in the browsers).
 * </p>
 * <p>
 * Every local file used is written to a temporary directory; the local target file has the
 * title 'local' and the text 'local file'.
 * </p>
 *
 * @author Ronald Brill
 */
public class LocalResourceAccessTest extends WebDriverTestCase {

    private static final Duration WAIT_TIME = Duration.ofSeconds(2);

    private static final String TARGET_HTML = DOCTYPE_HTML
            + "<html><head><title>local</title></head><body>local file</body></html>";

    private static final String FRAME_HTML = DOCTYPE_HTML
            + "<html><head><title>frame</title></head><body>frame</body></html>";

    private File dir_;
    private String dirUrl_;
    private String targetUrl_;

    private void createTargetFile() throws Exception {
        dir_ = Files.createTempDirectory("htmlunit-local").toFile().getCanonicalFile();
        dirUrl_ = dir_.toPath().toUri().toString();
        targetUrl_ = writeFile("target.html", TARGET_HTML);
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
     * Location.assign() of a frame to a local file from a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_frameLocationAssign() throws Exception {
        httpFrameTest("frames.f.location.assign('" + targetUrl() + "');");
    }

    /**
     * Location.replace() of a frame to a local file from a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_frameLocationReplace() throws Exception {
        httpFrameTest("frames.f.location.replace('" + targetUrl() + "');");
    }

    /**
     * Location.href of a frame set to a local file from a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_frameLocationHref() throws Exception {
        httpFrameTest("frames.f.location.href = '" + targetUrl() + "';");
    }

    /**
     * Location.protocol of a frame set to 'file:'; the URL standard ignores this
     * if the URL has a port.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame",
             "http:"})
    public void http_frameLocationProtocol() throws Exception {
        httpFrameTest("try {\n"
                + "  frames.f.location.protocol = 'file:';\n"
                + "  log(frames.f.location.protocol);\n"
                + "} catch(e) { logEx(e); }\n");
    }

    /**
     * Method window.open() into an existing frame.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"top",
                       "frame: frame"},
            FF = {"top",
                  "frame: frame",
                  "Error/Error"},
            FF_ESR = {"top",
                      "frame: frame",
                      "Error/Error"})
    public void http_windowOpenIntoFrame() throws Exception {
        httpFrameTest("try {\n"
                + "  window.open('" + targetUrl() + "', 'f');\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * A link targeting a frame.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_linkTarget() throws Exception {
        httpFrameTest("<a id='a' href='" + targetUrl() + "' target='f'>link</a>",
                "document.getElementById('a').click();");
    }

    /**
     * An area element targeting a frame.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_areaTarget() throws Exception {
        httpFrameTest("<map name='m'><area id='a' shape='rect' coords='0,0,10,10' href='" + targetUrl()
                + "' target='f'></map>",
                "document.getElementById('a').click();");
    }

    /**
     * A link resolved against a base element with a file URL.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_baseHref() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title>\n"
                + "<base href='" + dirUrl_ + "'>\n"
                + "</head>\n"
                + "<body onload=\"document.getElementById('a').click();\">\n"
                + "<a id='a' href='target.html' target='f'>link</a>\n"
                + "<iframe name='f' src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, FRAME_HTML);

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * Submitting a form (submit button) targeting a frame.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_formTarget() throws Exception {
        httpFrameTest("<form id='form' action='" + targetUrl() + "' method='get' target='f'>"
                + "<input type='submit' id='s'></form>",
                "document.getElementById('s').click();");
    }

    /**
     * Submitting a form using the formaction attribute of the submit button.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_formAction() throws Exception {
        httpFrameTest("<form id='form' action='" + URL_THIRD + "' method='get' target='f'>"
                + "<button type='submit' id='s' formaction='" + targetUrl() + "'>go</button></form>",
                "document.getElementById('s').click();");
    }

    /**
     * Submitting a form using form.submit().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_formSubmit() throws Exception {
        httpFrameTest("<form id='form' action='" + targetUrl() + "' method='get' target='f'></form>",
                "document.getElementById('form').submit();");
    }

    /**
     * Submitting a form using form.requestSubmit().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_formRequestSubmit() throws Exception {
        httpFrameTest("<form id='form' action='" + targetUrl() + "' method='get' target='f'></form>",
                "document.getElementById('form').requestSubmit();");
    }

    /**
     * The frame sets top.location to a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_topLocationFromFrame() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>";
        final String frame = DOCTYPE_HTML
                + "<html><head><title>frame</title></head>\n"
                + "<body onload=\"top.location = '" + targetUrl_ + "';\">frame</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, frame);

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * Meta refresh of a frame to a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_frameMetaRefresh() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>";
        final String frame = DOCTYPE_HTML
                + "<html><head><title>frame</title>\n"
                + "<meta http-equiv='refresh' content='0;url=" + targetUrl_ + "'>\n"
                + "</head><body>frame</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, frame);

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * Refresh header of a frame to a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: frame"})
    public void http_frameRefreshHeader() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>";
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Refresh", "0;url=" + targetUrl_));
        getMockWebConnection().setResponse(URL_SECOND, FRAME_HTML, 200, "OK", "text/html", headers);

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * The server redirects the frame to a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: "})
    public void http_frameRedirect() throws Exception {
        createTargetFile();
        final URL redirectUrl = new URL(URL_SECOND, "redirect");
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' src='" + redirectUrl + "'></iframe>\n"
                + "</body></html>";
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", targetUrl_));
        getMockWebConnection().setResponse(redirectUrl, "", 302, "Found", "text/html", headers);

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * A srcdoc frame of a page loaded from the server sets its location to a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: srcdoc"})
    @HtmlUnitNYI(CHROME = {"top",
                           "frame: "},
                 EDGE = {"top",
                         "frame: "},
                 FF = {"top",
                       "frame: "},
                 FF_ESR = {"top",
                           "frame: "})
    public void http_srcdocFrameLocation() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' srcdoc=\"srcdoc<script>location.href = '" + targetUrl_ + "';</script>\">"
                + "</iframe>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * A link inside a srcdoc frame of a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: srcdoc"})
    public void http_srcdocFrameLink() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' srcdoc=\"<a id='a' href='" + targetUrl_ + "'>srcdoc</a>"
                + "<script>document.getElementById('a').click();</script>\"></iframe>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * A data: frame (opaque origin) sets its location to a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: data"})
    @HtmlUnitNYI(CHROME = {"top",
                           "frame: "},
                 EDGE = {"top",
                         "frame: "},
                 FF = {"top",
                       "frame: "},
                 FF_ESR = {"top",
                           "frame: "})
    public void http_dataFrameLocation() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' src=\"data:text/html,data<script>location.href = '" + targetUrl_
                + "';</script>\"></iframe>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
    }

    /**
     * Method window.open() of a local file from a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("windows: 1")
    public void http_windowOpen() throws Exception {
        httpPopupTest("window.open('" + targetUrl() + "');");
    }

    /**
     * The return value of window.open() of a local file from a page loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"null: true",
                       "windows: 1"},
            FF = {"Error",
                  "windows: 1"},
            FF_ESR = {"Error",
                      "windows: 1"})
    public void http_windowOpenReturnValue() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var w = window.open('" + targetUrl_ + "');\n"
                + "    document.title = 'null: ' + (w === null);\n"
                + "  } catch(e) { document.title = e.name; }\n"
                + "}\n"
                + "</script></head>\n"
                + "<body>\n"
                + "<button id='go' onclick='test()'>go</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("go")).click();
        verifyResults(driver, d -> List.of(d.getTitle(), "windows: " + d.getWindowHandles().size()));
    }

    /**
     * The return value of window.open() of a local file into an existing frame from a page
     * loaded from the server.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"top",
                       "frame: frame",
                       "false",
                       "true"},
            FF = {"top",
                  "frame: frame",
                  "Error/Error"},
            FF_ESR = {"top",
                      "frame: frame",
                      "Error/Error"})
    public void http_windowOpenIntoFrameReturnValue() throws Exception {
        httpFrameTest("try {\n"
                + "  var w = window.open('" + targetUrl() + "', 'f');\n"
                + "  log(w === null);\n"
                + "  log(w === frames.f);\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * A link with target _blank to a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("windows: 1")
    public void http_linkTargetBlank() throws Exception {
        createTargetFile();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<a id='go' href='" + targetUrl_ + "' target='_blank'>link</a>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String mainWindow = driver.getWindowHandle();
        driver.findElement(By.id("go")).click();
        verifyResults(driver, d -> popupResult(d, mainWindow));
    }

    /**
     * A link inside an about:blank popup opened by a page loaded from the server;
     * the popup has the origin of its opener.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows: 2",
             "popup: §§URL§§",
             "popup: popup"})
    @HtmlUnitNYI(CHROME = {"windows: 2",
                           "popup: about:blank",
                           "popup: popup"},
                 EDGE = {"windows: 2",
                         "popup: about:blank",
                         "popup: popup"},
                 FF = {"windows: 2",
                       "popup: about:blank",
                       "popup: popup"},
                 FF_ESR = {"windows: 2",
                           "popup: about:blank",
                           "popup: popup"})
    public void http_aboutBlankPopupLink() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        httpPopupTest("var w = window.open('');\n"
                + "w.document.write(\"<a id='a' href='" + targetUrl() + "'>popup</a>\");\n"
                + "w.document.close();\n"
                + "w.document.getElementById('a').click();");
    }

    /**
     * A script inside an about:blank popup opened by a page loaded from the server sets
     * the location of the popup.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows: 2",
             "popup: §§URL§§",
             "popup: popup"})
    @HtmlUnitNYI(CHROME = {"windows: 2",
                           "popup: about:blank",
                           "popup: "},
                 EDGE = {"windows: 2",
                         "popup: about:blank",
                         "popup: "},
                 FF = {"windows: 2",
                       "popup: about:blank",
                       "popup: "},
                 FF_ESR = {"windows: 2",
                           "popup: about:blank",
                           "popup: "})
    public void http_aboutBlankPopupScript() throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);
        httpPopupTest("var w = window.open('');\n"
                + "w.document.write(\"popup<script>location.href = '" + targetUrl()
                + "';<\\/script>\");\n"
                + "w.document.close();");
    }

    /**
     * A frame of a page loaded from a file loads a local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_frameSrc() throws Exception {
        fileFrameTest("<iframe name='f' src='target.html'></iframe>", "", "");
    }

    /**
     * Location.href of a frame set to a local file from a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_frameLocationHref() throws Exception {
        fileFrameTest("frames.f.location.href = 'target.html';");
    }

    /**
     * Location.assign() of a frame to a local file from a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"top",
                       "frame: frame"},
            FF = {"top",
                  "frame: local file"},
            FF_ESR = {"top",
                      "frame: local file"})
    @HtmlUnitNYI(CHROME = {"top",
                           "frame: local file"},
                 EDGE = {"top",
                         "frame: local file"})
    public void file_frameLocationAssign() throws Exception {
        fileFrameTest("frames.f.location.assign('target.html');");
    }

    /**
     * Location.replace() of a frame to a local file from a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_frameLocationReplace() throws Exception {
        fileFrameTest("frames.f.location.replace('target.html');");
    }

    /**
     * Method window.open() into an existing frame from a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_windowOpenIntoFrame() throws Exception {
        fileFrameTest("window.open('target.html', 'f');");
    }

    /**
     * A link targeting a frame from a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_linkTarget() throws Exception {
        fileFrameTest("<iframe name='f' src='frame.html'></iframe>\n"
                + "<a id='a' href='target.html' target='f'>link</a>",
                "document.getElementById('a').click();", FRAME_HTML);
    }

    /**
     * Submitting a form targeting a frame from a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_formTarget() throws Exception {
        fileFrameTest("<iframe name='f' src='frame.html'></iframe>\n"
                + "<form action='target.html' method='get' target='f'><input type='submit' id='s'></form>",
                "document.getElementById('s').click();", FRAME_HTML);
    }

    /**
     * Meta refresh of a frame loaded from a file to another local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_frameMetaRefresh() throws Exception {
        fileFrameTest("<iframe name='f' src='frame.html'></iframe>", "",
                DOCTYPE_HTML
                + "<html><head><title>frame</title>\n"
                + "<meta http-equiv='refresh' content='0;url=target.html'>\n"
                + "</head><body>frame</body></html>");
    }

    /**
     * The frame of a page loaded from a file sets top.location to another local file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"local",
             "file:target.html"})
    public void file_topLocationFromFrame() throws Exception {
        createTargetFile();
        writeFile("frame.html", DOCTYPE_HTML
                + "<html><head><title>frame</title></head>\n"
                + "<body onload=\"top.location = 'target.html';\">frame</body></html>");
        final String topUrl = writeFile("top.html", DOCTYPE_HTML
                + "<html><head><title>top</title></head>\n"
                + "<body>\n"
                + "<iframe name='f' src='frame.html'></iframe>\n"
                + "</body></html>");

        final WebDriver driver = getWebDriver();
        try {
            driver.get(topUrl);
            verifyResults(driver, d -> List.of(d.getTitle(), normalizeUrl(d.getCurrentUrl())));
        }
        finally {
            shutDownAll();
        }
    }

    /**
     * A srcdoc frame of a page loaded from a file sets its location to a local file;
     * the srcdoc document has the origin of its parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top",
             "frame: local file"})
    public void file_srcdocFrameLocation() throws Exception {
        createTargetFile();
        fileFrameTest("<iframe name='f' srcdoc=\"srcdoc<script>location.href = '" + targetUrl_
                + "';</script>\"></iframe>", "", "");
    }

    /**
     * Method window.open() of a local file from a page loaded from a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows: 2",
             "popup: file:target.html",
             "popup: local file"})
    public void file_windowOpen() throws Exception {
        filePopupTest("window.open('target.html');");
    }

    /**
     * A script inside an about:blank popup opened by a page loaded from a file sets the
     * location of the popup to a local file; the popup has the origin of its opener.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows: 2",
             "popup: file:target.html",
             "popup: local file"})
    public void file_aboutBlankPopupScript() throws Exception {
        createTargetFile();
        filePopupTest("var w = window.open('');\n"
                + "w.document.write(\"popup<script>location.href = '" + targetUrl_ + "';<\\/script>\");\n"
                + "w.document.close();");
    }

    /**
     * A link inside an about:blank popup opened by a page loaded from a file;
     * the popup has the origin of its opener.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"windows: 2",
             "popup: file:target.html",
             "popup: local file"})
    public void file_aboutBlankPopupLink() throws Exception {
        createTargetFile();
        filePopupTest("var w = window.open('');\n"
                + "w.document.write(\"<a id='a' href='" + targetUrl_ + "'>popup</a>\");\n"
                + "w.document.close();\n"
                + "w.document.getElementById('a').click();");
    }

    /**
     * Creates the target file (if not done yet) and returns its URL.
     */
    private String targetUrl() throws Exception {
        if (dir_ == null) {
            createTargetFile();
        }
        return targetUrl_;
    }

    /**
     * Loads a page from the server with a frame 'f' (loaded from the server) and runs the script
     * when the page is loaded. Verifies the title of the page, the text of the frame and the logged entries.
     */
    private void httpFrameTest(final String script) throws Exception {
        httpFrameTest("", script);
    }

    private void httpFrameTest(final String body, final String script) throws Exception {
        targetUrl();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title>\n"
                + "<script>\n"
                + LOG_WINDOW_NAME_FUNCTION
                + "function test() {\n"
                + script + "\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + body + "\n"
                + "<iframe name='f' src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, FRAME_HTML);

        final WebDriver driver = loadPage2(html);
        verifyResults(driver, d -> {
            final List<String> results = new ArrayList<>();
            results.add(d.getTitle());
            results.add(frameText(d, "f"));
            results.addAll(getWindowName2Entries(d));
            return results;
        });
    }

    /**
     * Loads a page from the server; a click on a button runs the script (popups need a user gesture).
     * Verifies the number of windows and the URL and the text of the popup.
     */
    private void httpPopupTest(final String script) throws Exception {
        targetUrl();
        final String html = DOCTYPE_HTML
                + "<html><head><title>top</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + script + "\n"
                + "}\n"
                + "</script></head>\n"
                + "<body>\n"
                + "<button id='go' onclick='test()'>go</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String mainWindow = driver.getWindowHandle();
        driver.findElement(By.id("go")).click();
        verifyResults(driver, d -> popupResult(d, mainWindow));
    }

    /**
     * Loads a page from a file with a frame 'f' (loaded from a file) and runs the script
     * when the page is loaded. Verifies the title of the page and the text of the frame.
     */
    private void fileFrameTest(final String script) throws Exception {
        fileFrameTest("<iframe name='f' src='frame.html'></iframe>", script, FRAME_HTML);
    }

    private void fileFrameTest(final String body, final String script, final String frameHtml)
            throws Exception {
        if (dir_ == null) {
            createTargetFile();
        }
        if (!frameHtml.isEmpty()) {
            writeFile("frame.html", frameHtml);
        }
        final String topUrl = writeFile("top.html", DOCTYPE_HTML
                + "<html><head><title>top</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + script + "\n"
                + "}\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + body + "\n"
                + "</body></html>");

        final WebDriver driver = getWebDriver();
        try {
            driver.get(topUrl);
            verifyResults(driver, d -> List.of(d.getTitle(), frameText(d, "f")));
        }
        finally {
            shutDownAll();
        }
    }

    /**
     * Loads a page from a file; a click on a button runs the script (popups need a user gesture).
     * Verifies the number of windows and the URL and the text of the popup.
     */
    private void filePopupTest(final String script) throws Exception {
        if (dir_ == null) {
            createTargetFile();
        }
        final String topUrl = writeFile("top.html", DOCTYPE_HTML
                + "<html><head><title>top</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + script + "\n"
                + "}\n"
                + "</script></head>\n"
                + "<body>\n"
                + "<button id='go' onclick='test()'>go</button>\n"
                + "</body></html>");

        final WebDriver driver = getWebDriver();
        try {
            driver.get(topUrl);
            final String mainWindow = driver.getWindowHandle();
            driver.findElement(By.id("go")).click();
            verifyResults(driver, d -> popupResult(d, mainWindow));
        }
        finally {
            shutDownAll();
        }
    }

    /**
     * Polls the results until they match the expected alerts or the wait time is over.
     */
    private void verifyResults(final WebDriver driver, final Function<WebDriver, List<String>> results)
            throws Exception {
        verify(String.join("\n", getExpectedAlerts()), () -> String.join("\n", results.apply(driver)), WAIT_TIME);
    }

    /**
     * Returns the text of the body of the frame with the given name.
     */
    private static String frameText(final WebDriver driver, final String frameName) {
        try {
            driver.switchTo().frame(frameName);
            return "frame: " + driver.findElement(By.tagName("body")).getText();
        }
        catch (final WebDriverException e) {
            return "frame: " + e.getClass().getSimpleName();
        }
        finally {
            driver.switchTo().defaultContent();
        }
    }

    /**
     * Returns the number of windows and the URL and the text of the popup.
     */
    private List<String> popupResult(final WebDriver driver, final String mainWindow) {
        final List<String> result = new ArrayList<>();
        result.add("windows: " + driver.getWindowHandles().size());
        for (final String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                try {
                    driver.switchTo().window(handle);
                    result.add("popup: " + normalizeUrl(driver.getCurrentUrl()));
                    result.add("popup: " + driver.findElement(By.tagName("body")).getText());
                }
                catch (final WebDriverException e) {
                    result.add("popup: " + e.getClass().getSimpleName());
                }
            }
        }
        driver.switchTo().window(mainWindow);
        return result;
    }

    /**
     * Replaces the URL of a local file by 'file:' and the file name, the URLs of the local files differ
     * from machine to machine (and from browser to browser, e.g. 'file:/C:/...' and 'file:///C:/...').
     */
    private static String normalizeUrl(final String url) {
        if (url != null && url.startsWith("file:")) {
            return "file:" + url.substring(url.lastIndexOf('/') + 1);
        }
        return url;
    }
}
