/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.css;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Unit tests for {@link StyleSheetList}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class StyleSheetListTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void length() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <link href='style1.css'></link>\n"
            + "    <link href='style2.css' rel='stylesheet'></link>\n"
            + "    <link href='style3.css' type='text/css'></link>\n"
            + "    <link href='style4.css' rel='stylesheet' type='text/css'></link>\n"
            + "    <style>div.x { color: red; }</style>\n"
            + "  </head>\n"
            + "  <body onload='alert(document.styleSheets.length)'>\n"
            + "    <style>div.y { color: green; }</style>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "rgb(255, 0, 0)", "rgb(255, 0, 0)" }, IE = "exception")
    public void getComputedStyle_Link() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='" + URL_SECOND + "'/>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var div = document.getElementById('myDiv');\n"
            + "        try {\n"
            + "          alert(window.getComputedStyle(div, null).color);\n"
            + "          var div2 = document.getElementById('myDiv2');\n"
            + "          alert(window.getComputedStyle(div2, null).color);\n"
            + "        } catch(e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <div id='myDiv'></div>\n"
            + "    <div id='myDiv2'></div>\n"
            + "  </body>\n"
            + "</html>";

        final String css = "div {color:red}";

        getMockWebConnection().setDefaultResponse(css, "text/css");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "0", "undefined", "undefined", "exception for -2" },
            IE = { "0", "exception for 0", "exception for 46", "exception for -2" })
    public void arrayIndexOutOfBoundAccess() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.styleSheets.length);\n"

            + "        try {\n"
            + "          alert(document.styleSheets[0]);\n"
            + "        }\n"
            + "        catch (e) {\n"
            + "          alert('exception for 0');\n"
            + "        }\n"

            + "        try {\n"
            + "          alert(document.styleSheets[46]);\n"
            + "        }\n"
            + "        catch (e) {\n"
            + "          alert('exception for 46');\n"
            + "        }\n"

            + "        try {\n"
            + "          alert(document.styleSheets[-2]);\n"
            + "        }\n"
            + "        catch (e) {\n"
            + "          alert('exception for -2');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for a stylesheet link which points to a non-existent file (bug 2070940).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"1", "[object CSSStyleSheet]", "[object CSSStyleSheet]" }, IE = { "1", "[object]", "[object]" })
    public void nonExistentStylesheet() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.styleSheets.length);\n"
            + "        alert(document.styleSheets.item(0));\n"
            + "        alert(document.styleSheets[0]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("Not Found", 404, "Not Found", "text/html");
        loadPageWithAlerts2(html);
    }

    /**
     * Test for a stylesheet link which points to a broken gzip encoded file (bug 3566999).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"1", "[object CSSStyleSheet]", "[object CSSStyleSheet]" }, IE = { "1", "[object]", "[object]" })
    public void emptyGZipEncodedStylesheet() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.styleSheets.length);\n"
            + "        alert(document.styleSheets.item(0));\n"
            + "        alert(document.styleSheets[0]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        final String css = "";

        getMockWebConnection().setDefaultResponse(css, "text/css");
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Length", "0"));
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        getMockWebConnection().setDefaultResponse(css, 200, "OK", "text/css", headers);

        loadPageWithAlerts2(html);
    }

    /**
     * Test for a stylesheet link which points to a broken gzip encoded file (bug 3498578).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"1", "[object CSSStyleSheet]", "[object CSSStyleSheet]" }, IE = { "1", "[object]", "[object]" })
    public void brokenGZipEncodedStylesheet() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <link rel='stylesheet' type='text/css' href='foo.css'/>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.styleSheets.length);\n"
            + "        alert(document.styleSheets.item(0));\n"
            + "        alert(document.styleSheets[0]);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>abc</body>\n"
            + "</html>";

        final String css = "div {color:red}";

        getMockWebConnection().setDefaultResponse(css, "text/css");
        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        getMockWebConnection().setDefaultResponse(css, 200, "OK", "text/css", headers);

        loadPageWithAlerts2(html);
    }
}
