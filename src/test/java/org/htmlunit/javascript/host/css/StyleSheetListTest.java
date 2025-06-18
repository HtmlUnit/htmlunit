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
package org.htmlunit.javascript.host.css;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link StyleSheetList}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
public class StyleSheetListTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void length() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link href='style1.css'></link>\n"
            + "    <link href='style2.css' rel='stylesheet'></link>\n"
            + "    <link href='style3.css' type='text/css'></link>\n"
            + "    <link href='style4.css' rel='stylesheet' type='text/css'></link>\n"
            + "    <style>div.x { color: red; }</style>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "    </script></head>\n"
            + "  </head>\n"
            + "  <body onload='log(document.styleSheets.length)'>\n"
            + "    <style>div.y { color: green; }</style>\n"
            + "  </body>\n"
            + "</html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"rgb(255, 0, 0)", "rgb(255, 0, 0)"})
    public void getComputedStyle_Link() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='" + URL_SECOND + "'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var div = document.getElementById('myDiv');\n"
            + "        try {\n"
            + "          log(window.getComputedStyle(div, null).color);\n"
            + "          var div2 = document.getElementById('myDiv2');\n"
            + "          log(window.getComputedStyle(div2, null).color);\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='myDiv'></div>\n"
            + "    <div id='myDiv2'></div>\n"
            + "  </body>\n"
            + "</html>";

        final String css = "div {color:red}";

        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "undefined", "undefined", "undefined"})
    public void arrayIndexOutOfBoundAccess() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        log(document.styleSheets.length);\n"

            + "        try {\n"
            + "          log(document.styleSheets[0]);\n"
            + "        } catch(e) { logEx(e); }\n"

            + "        try {\n"
            + "          log(document.styleSheets[46]);\n"
            + "        } catch(e) { logEx(e); }\n"

            + "        try {\n"
            + "          log(document.styleSheets[-2]);\n"
            + "        } catch(e) { logEx(e); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for a stylesheet link which points to a non-existent file (bug #685).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "[object CSSStyleSheet]", "[object CSSStyleSheet]"})
    public void nonExistentStylesheet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        log(document.styleSheets.length);\n"
            + "        log(document.styleSheets.item(0));\n"
            + "        log(document.styleSheets[0]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("Not Found", 404, "Not Found", MimeType.TEXT_HTML);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for a stylesheet link which points to a broken gzip encoded file (Bug #1434).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "[object CSSStyleSheet]", "[object CSSStyleSheet]"})
    public void emptyGZipEncodedStylesheet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        log(document.styleSheets.length);\n"
            + "        log(document.styleSheets.item(0));\n"
            + "        log(document.styleSheets[0]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        final String css = "";

        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_LENGTH, "0"));
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        getMockWebConnection().setDefaultResponse(css, 200, "OK", MimeType.TEXT_CSS, headers);

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for a stylesheet link which points to a broken gzip encoded file (Bug #1389).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "[object CSSStyleSheet]", "[object CSSStyleSheet]"})
    public void brokenGZipEncodedStylesheet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        log(document.styleSheets.length);\n"
            + "        log(document.styleSheets.item(0));\n"
            + "        log(document.styleSheets[0]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        final String css = "div {color:red}";

        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        getMockWebConnection().setDefaultResponse(css, 200, "OK", MimeType.TEXT_CSS, headers);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "1"})
    @HtmlUnitNYI(CHROME = {"1", "2"},
            EDGE = {"1", "2"},
            FF = {"1", "2"},
            FF_ESR = {"1", "2"})
    public void dynamicAddedStyleSheet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='" + URL_SECOND + "'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        log(document.styleSheets.length);\n"

            + "        var linkTag = document.createElement ('link');\n"
            + "        linkTag.href = 'new.css';\n"
            + "        linkTag.rel = 'stylesheet';\n"
            + "        var head = document.getElementsByTagName ('head')[0];\n"
            + "        head.appendChild (linkTag);\n"

            + "        log(document.styleSheets.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='myDiv'></div>\n"
            + "    <div id='myDiv2'></div>\n"
            + "  </body>\n"
            + "</html>";

        final String css = "div {color:red}";
        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "false", "true", "false", "false"})
    public void in() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var sheets = document.styleSheets;\n"
            + "        log(sheets.length);\n"
            + "        log(-1 in sheets);\n"
            + "        log(0 in sheets);\n"
            + "        log(1 in sheets);\n"
            + "        log(42 in sheets);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        final String css = "div {color:red}";
        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "undefined", "[object CSSStyleSheet]", "undefined", "undefined"})
    public void index() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var sheets = document.styleSheets;\n"
            + "        log(sheets.length);\n"
            + "        log(sheets[-1]);\n"
            + "        log(sheets[0]);\n"
            + "        log(sheets[1]);\n"
            + "        log(sheets[42]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        final String css = "div {color:red}";
        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "null", "[object CSSStyleSheet]", "null", "null"})
    public void item() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var sheets = document.styleSheets;\n"
            + "        log(sheets.length);\n"
            + "        log(sheets.item(-1));\n"
            + "        log(sheets.item(0));\n"
            + "        log(sheets.item(1));\n"
            + "        log(sheets.item(42));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        final String css = "div {color:red}";
        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "false"})
    public void equivalentValues() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "  <head>\n"
              + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
              + "    <script>\n"
              + LOG_TITLE_FUNCTION
              + "      function test() {\n"
              + "        var sheets = document.styleSheets;\n"
              + "        log(sheets == document.styleSheets);\n"
              + "        log(sheets == null);\n"
              + "        log(null == sheets);\n"
              + "      }\n"
              + "    </script>\n"
              + "  </head>\n"
              + "  <body onload='test()'>abc</body>\n"
              + "</html>";

        final String css = "div {color:red}";
        getMockWebConnection().setDefaultResponse(css, MimeType.TEXT_CSS);

        loadPageVerifyTitle2(html);
    }
}
