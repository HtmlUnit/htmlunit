/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MimeType}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class MimeTypeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "pdf", "Portable Document Format", "application/pdf",
             "pdf", "Portable Document Format", "text/pdf"})
    public void mimeType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var mimeTypes = navigator.mimeTypes;\n"
            + "  log(mimeTypes.length);\n"

            + "  log(mimeTypes.item(0).suffixes);\n"
            + "  log(mimeTypes.item(0).description);\n"
            + "  log(mimeTypes.item(0).type);\n"

            + "  log(mimeTypes.item(1).suffixes);\n"
            + "  log(mimeTypes.item(1).description);\n"
            + "  log(mimeTypes.item(1).type);\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log('application/pdf' in navigator.mimeTypes);\n"
            + "  log('pdf' in navigator.mimeTypes);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests default configuration of Pdf plugin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object MimeType]", "pdf", "PDF Viewer", "true", "true"})
    public void pdfMimeType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var mimeTypePdf = navigator.mimeTypes['application/pdf'];\n"
            + "  log(mimeTypePdf);\n"
            + "  if (mimeTypePdf) {\n"
            + "    log(mimeTypePdf.suffixes);\n"
            + "    var pluginPdf = mimeTypePdf.enabledPlugin;\n"
            + "    log(pluginPdf.name);\n"
            + "    log(pluginPdf == navigator.plugins[pluginPdf.name]);\n"
            + "    log(pluginPdf == navigator.plugins.namedItem(pluginPdf.name));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests default configuration of Pdf plugin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object MimeType]", "pdf", "PDF Viewer", "true", "true"})
    public void textPdfMimeType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var mimeTypePdf = navigator.mimeTypes['text/pdf'];\n"
            + "  log(mimeTypePdf);\n"
            + "  if (mimeTypePdf) {\n"
            + "    log(mimeTypePdf.suffixes);\n"
            + "    var pluginPdf = mimeTypePdf.enabledPlugin;\n"
            + "    log(pluginPdf.name);\n"
            + "    log(pluginPdf == navigator.plugins[pluginPdf.name]);\n"
            + "    log(pluginPdf == navigator.plugins.namedItem(pluginPdf.name));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests default configuration of Flash plugin for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void flashMimeType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var mimeTypeFlash = navigator.mimeTypes['application/x-shockwave-flash'];\n"
            + "  log(mimeTypeFlash);\n"
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
    public void iterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var mimeTypes = navigator.mimeTypes;\n"
            + "  log(mimeTypes.length);\n"

            + "  let iter = mimeTypes[Symbol.iterator]();\n"
            + "  for (const mime of iter) {\n"
            + "    log(mime.type);\n"
            + "  }"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }
}
