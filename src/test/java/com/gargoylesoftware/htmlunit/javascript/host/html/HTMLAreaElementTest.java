/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLAreaElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLAreaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
    public void readWriteAccessKey() throws Exception {
        final String html
            = "<html><body><map><area id='a1'/><area id='a2' accesskey='A'/></map><script>\n"
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a';\n"
            + "a2.accessKey = 'A';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = 'a8';\n"
            + "a2.accessKey = '8Afoo';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "a1.accessKey = '8';\n"
            + "a2.accessKey = '@';\n"
            + "alert(a1.accessKey);\n"
            + "alert(a2.accessKey);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "", "[object HTMLAreaElement]" },
            CHROME = { "", "function HTMLAreaElement() { [native code] }" },
            FF = { "", "function HTMLAreaElement() {\n    [native code]\n}" })
    public void type() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "  var elem = document.getElementById('a1');\n"
            + "    try {\n"
            + "      alert(elem);\n"
            + "      alert(HTMLAreaElement);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <map><area id='a1'/><area id='a2' accesskey='A'/></map>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLButtonElement]", "[object HTMLButtonElement]",
                "http://localhost:12345/", "http://srv/htmlunit.org"})
    @BuggyWebDriver(FF)
    public void focus() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <title>Test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var testNode = document.getElementById('myButton');\n"
            + "      testNode.focus();\n"
            + "      alert(document.activeElement);\n"

            + "      testNode = document.getElementById('a1');\n"
            + "      testNode.focus();\n"
            + "      alert(document.activeElement);\n"

            + "      testNode = document.getElementById('a2');\n"
            + "      testNode.focus();\n"
            + "      alert(document.activeElement);\n"

            + "      testNode = document.getElementById('a3');\n"
            + "      testNode.focus();\n"
            + "      alert(document.activeElement);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <button id='myButton'>Press</button>\n"
            + "  <img usemap='#dot'"
                    + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                    + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
            + "  <map name='dot'>\n"
            + "    <area id='a1' shape='rect' coords='0,0,1,1'/>\n"
            + "    <area id='a2' href='' shape='rect' coords='0,0,1,1'/>\n"
            + "    <area id='a3' href='http://srv/htmlunit.org' shape='rect' coords='0,0,1,1'/>\n"
            + "  <map>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
