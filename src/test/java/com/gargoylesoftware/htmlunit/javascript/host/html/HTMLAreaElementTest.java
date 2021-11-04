/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLAreaElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLAreaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "A", "a", "A", "a8", "8Afoo", "8", "@"})
    public void readWriteAccessKey() throws Exception {
        final String html
            = "<html><body><map><area id='a1'/><area id='a2' accesskey='A'/></map><script>\n"
            + LOG_TITLE_FUNCTION
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "log(a1.accessKey);\n"
            + "log(a2.accessKey);\n"
            + "a1.accessKey = 'a';\n"
            + "a2.accessKey = 'A';\n"
            + "log(a1.accessKey);\n"
            + "log(a2.accessKey);\n"
            + "a1.accessKey = 'a8';\n"
            + "a2.accessKey = '8Afoo';\n"
            + "log(a1.accessKey);\n"
            + "log(a2.accessKey);\n"
            + "a1.accessKey = '8';\n"
            + "a2.accessKey = '@';\n"
            + "log(a1.accessKey);\n"
            + "log(a2.accessKey);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "function HTMLAreaElement() { [native code] }"},
            IE = {"", "[object HTMLAreaElement]"})
    public void type() throws Exception {
        final String html = ""
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "  var elem = document.getElementById('a1');\n"
            + "    try {\n"
            + "      log(elem);\n"
            + "      log(HTMLAreaElement);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <map><area id='a1'/><area id='a2' accesskey='A'/></map>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLButtonElement]", "[object HTMLButtonElement]",
                       "§§URL§§", "http://srv/htmlunit.org"},
            FF = {"[object HTMLButtonElement]", "", "§§URL§§", "http://srv/htmlunit.org"},
            FF78 = {"[object HTMLButtonElement]", "", "§§URL§§", "http://srv/htmlunit.org"})
    public void focus() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var testNode = document.getElementById('myButton');\n"
            + "      testNode.focus();\n"
            + "      log(document.activeElement);\n"

            + "      testNode = document.getElementById('a1');\n"
            + "      testNode.focus();\n"
            + "      log(document.activeElement);\n"

            + "      testNode = document.getElementById('a2');\n"
            + "      testNode.focus();\n"
            + "      log(document.activeElement);\n"

            + "      testNode = document.getElementById('a3');\n"
            + "      testNode.focus();\n"
            + "      log(document.activeElement);\n"
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

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a1 clicked", "a2 clicked"})
    public void click() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.getElementById('a1').click();\n"
            + "    document.getElementById('a2').click();\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <img usemap='#dot'"
                        + " src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAA"
                        + "HElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg=='>\n"
            + "  <map name='dot'>\n"
            + "    <area id='a1' shape='rect' coords='0,0,1,1' onclick='log(\"a1 clicked\");'/>\n"
            + "    <area id='a2' shape='rect' coords='0 0 2 2' onclick='log(\"a2 clicked\");'/>\n"
            + "  <map>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "alternate help", "prefetch", "prefetch", "not supported", "notsupported"})
    public void readWriteRel() throws Exception {
        final String html
            = "<html><body><map><area id='a1'/><area id='a2' rel='alternate help'/></map><script>\n"
            + LOG_TITLE_FUNCTION
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"

            + "log(a1.rel);\n"
            + "log(a2.rel);\n"

            + "a1.rel = 'prefetch';\n"
            + "a2.rel = 'prefetch';\n"
            + "log(a1.rel);\n"
            + "log(a2.rel);\n"

            + "a1.rel = 'not supported';\n"
            + "a2.rel = 'notsupported';\n"
            + "log(a1.rel);\n"
            + "log(a2.rel);\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "2", "alternate", "help"},
            IE = "exception")
    public void relList() throws Exception {
        final String html
            = "<html><body><map><area id='a1'/><area id='a2' rel='alternate help'/></map><script>\n"
            + LOG_TITLE_FUNCTION
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"

            + "try {\n"
            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  for (var i = 0; i < a2.relList.length; i++) {\n"
            + "    log(a2.relList[i]);\n"
            + "  }\n"
            + "} catch(e) { log('exception'); }\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
