/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link Plugin}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class PluginTest extends WebDriverTestCase {

    @Test
    @Alerts({"5",
        "PDF Viewer", "Portable Document Format", "internal-pdf-viewer", "undefined",
        "Chrome PDF Viewer", "Portable Document Format", "internal-pdf-viewer", "undefined",
        "Chromium PDF Viewer", "Portable Document Format", "internal-pdf-viewer", "undefined",
        "Microsoft Edge PDF Viewer", "Portable Document Format", "internal-pdf-viewer", "undefined",
        "WebKit built-in PDF", "Portable Document Format", "internal-pdf-viewer", "undefined"})
    public void plugins() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function doTest() {\n"
                + "      log(window.navigator.plugins.length);\n"
                + "      for (var i = 0; i < window.navigator.plugins.length; i++) {\n"
                + "        let pl = window.navigator.plugins[i];\n"
                + "        log(pl.name);\n"
                + "        log(pl.description);\n"
                + "        log(pl.filename);\n"
                + "        log(pl.version);\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Chromium PDF Viewer")
    public void index() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(navigator.plugins[2].name);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Microsoft Edge PDF Viewer")
    public void item() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(navigator.plugins.item(3).name);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Chromium PDF Viewer")
    public void namedItem() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(navigator.plugins.namedItem('Chromium PDF Viewer').name);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void in() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log('PDF Viewer' in navigator.plugins);\n"
            + "  log('pdf' in navigator.plugins);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "[object MimeType]", "application/pdf", "[object MimeType]", "text/pdf", "undefined"})
    public void pluginIndex() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let pl = navigator.plugins.item(4);\n"

            + "  log(pl.length);\n"
            + "  log(pl[0]);\n"
            + "  log(pl[0].type);\n"

            + "  log(pl[1]);\n"
            + "  log(pl[1].type);\n"

            + "  log(pl[2]);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "[object MimeType]", "application/pdf", "[object MimeType]", "text/pdf", "null"})
    public void pluginItem() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let pl = navigator.plugins.item(4);\n"

            + "  log(pl.length);\n"
            + "  log(pl.item(0));\n"
            + "  log(pl.item(0).type);\n"

            + "  log(pl.item(1));\n"
            + "  log(pl.item(1).type);\n"

            + "  log(pl.item(2));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object MimeType]", "application/pdf", "[object MimeType]", "application/pdf", "null"})
    public void pluginNamedItem() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let pl = navigator.plugins.item(4);\n"

            + "  log(pl.namedItem('application/pdf'));\n"
            + "  log(pl.namedItem('application/pdf').type);\n"

            + "  log(pl.namedItem('text/pdf'));\n"
            + "  log(pl.namedItem('application/pdf').type);\n"

            + "  log(pl.namedItem('unknown'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "PDF Viewer", "Chrome PDF Viewer", "Chromium PDF Viewer",
             "Microsoft Edge PDF Viewer", "WebKit built-in PDF"})
    public void iterator() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var plugs = navigator.plugins;\n"
            + "  log(plugs.length);\n"

            + "  let iter = plugs[Symbol.iterator]();\n"
            + "  for (const plug of iter) {\n"
            + "    log(plug.name);\n"
            + "  }"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "application/pdf", "text/pdf"})
    public void pluginIterator() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let pl = navigator.plugins.item(4);\n"
            + "  log(pl.length);\n"

            + "  let iter = pl[Symbol.iterator]();\n"
            + "  for (const plug of iter) {\n"
            + "    log(plug.type);\n"
            + "  }"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }
}
