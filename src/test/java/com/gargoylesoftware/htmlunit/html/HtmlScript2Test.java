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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlScript}, but as WebDriverTestCase.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Daniel Wagner-Hall
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlScript2Test extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("myValue")
    public void insertBefore() throws Exception {
        final String html
            = "<html><head><title>Page A</title>"
            + "<script>\n"
            + "  function test() {\n"
            + "    var script = document.createElement('script');\n"
            + "    script.text = \"foo = 'myValue';\";\n"
            + "    document.body.insertBefore(script, document.body.firstChild);\n"
            + "    alert(foo);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "created", "hello", "replaced" })
    public void addedFromDocumentFragment() throws Exception {
        final String html = "<html><body>\n"
            + "<span id='A'></span>\n"
            + "<script>\n"
            + "var text = '<script>alert(\"hello\");</sc' + 'ript>';\n"
            + "var element = document.getElementById('A');\n"
            + "try {\n"
            + "  var range = element.ownerDocument.createRange();\n"
            + "  range.selectNode(element);\n"
            + "  var fragment = range.createContextualFragment(text);\n"
            + "  alert('created');\n"
            + "  element.parentNode.replaceChild(fragment, element);\n"
            + "  alert('replaced');\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLScriptElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <script id='myId'></script>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        assertEquals("script", driver.findElement(By.id("myId")).getTagName());
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts("Hello")
    public void type_case_sensitivity() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script type='text/JavaScript'>\n"
            + "    alert('Hello');\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts({ "1", "5", "7" })
    public void type_language() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script>\n"
            + "    alert('1');\n"
            + "  </script>\n"
            + "  <script language='anything'>\n"
            + "    alert('2');\n"
            + "  </script>\n"
            + "  <script type='anything'>\n"
            + "    alert('3');\n"
            + "  </script>\n"
            + "  <script language='anything' type='anything'>\n"
            + "    alert('4');\n"
            + "  </script>\n"
            + "  <script language='anything' type='text/javascript'>\n"
            + "    alert('5');\n"
            + "  </script>\n"
            + "  <script language='javascript' type='anything'>\n"
            + "    alert('6');\n"
            + "  </script>\n"
            + "  <script language='javascript'>\n"
            + "    alert('7');\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that a script element is not run when it is cloned.
     * See bug 1707788.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("a")
    public void scriptIsNotRunWhenCloned() throws Exception {
        final String html = "<html><body onload='document.body.cloneNode(true)'>\n"
            + "<script>alert('a')</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "deferred", "normal", "onload" })
    public void defer() throws Exception {
        final String html = "<html><head>\n"
            + "<script defer>alert('deferred')</script>\n"
            + "<script>alert('normal')</script>\n"
            + "</head>\n"
            + "<body onload='alert(\"onload\")'>test</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for replaceChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "false" })
    public void appendChild_newIdAndScriptAddedInOnce() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + "  var div1 = document.createElement('div');\n"
            + "  div1.id = 'div1';\n"
            + "  var script = document.createElement('script');\n"
            + "  script.text = 'alert(document.getElementById(\"div1\") == null)';\n"
            + "  div1.appendChild(script);\n"
            + "  document.body.appendChild(div1);\n"
            + "  alert(document.getElementById('div1') == null);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "2" })
    public void executesMultipleTextNodes() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + "  var script = document.createElement('script');"
            + "  try {"
            + "    script.appendChild(document.createTextNode('alert(\"1\");'));\n;"
            + "    script.appendChild(document.createTextNode('alert(\"2\");'));\n;"
            + "  } catch(e) {\n"
            + "    script.text = 'alert(\"1\");alert(\"2\");';\n;"
            + "  }\n"
            + "  document.body.appendChild(script);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("var x=1;x=2;")
    public void getTextMultipleTextNodes() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + "  var script = document.createElement('script');"
            + "  try {\n"
            + "  script.appendChild(document.createTextNode('var x=1;'));\n;"
            + "  script.appendChild(document.createTextNode('x=2;'));\n;"
            + "  } catch(e) {\n"
            + "    script.text = 'var x=1;x=2;';\n;"
            + "  }\n"
            + "  document.body.appendChild(script);\n"
            + "  alert(script.text);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void setTextMultipleTextNodes() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + "  try {\n"
            + "    var script = document.createElement('script');"
            + "    script.appendChild(document.createTextNode('alert(\"1\");'));\n;"
            + "    script.appendChild(document.createTextNode('alert(\"2\");'));\n;"
            + "    script.text = 'alert(\"3\");';\n;"
            + "    document.body.appendChild(script);\n"
            + "  } catch (e) {alert('exception');}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that setting a script's <tt>src</tt> attribute behaves correctly.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "2", "3" })
    public void settingSrcAttribute() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <title>Test</title>\n"
            + "    <script id='a'></script>\n"
            + "    <script id='b'>alert('1');</script>\n"
            + "    <script id='c' src='script2.js'></script>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        document.getElementById('a').src = 'script3.js';\n"
            + "        document.getElementById('b').src = 'script4.js';\n"
            + "        document.getElementById('c').src = 'script5.js';\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "      test\n"
            + "  </body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(html);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script2.js"), "alert(2);");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script3.js"), "alert(3);");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script4.js"), "alert(4);");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script5.js"), "alert(5);");

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "s-x", "z" },
            IE = { "s-x", "x", "z" })
    public void addEventListener_load() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s1 = document.createElement('script');\n"
            + "    s1.text = 'alert(\"s-x\")';\n"
            + "    s1.addEventListener('load', function(){alert('x')}, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "    \n"
            + "    var s2 = document.createElement('script');\n"
            + "    s2.src = '//:';\n"
            + "    s2.addEventListener('load', function(){alert('y')}, false);\n"
            + "    document.body.insertBefore(s2, document.body.firstChild);\n"
            + "    \n"
            + "    var s3 = document.createElement('script');\n"
            + "    s3.src = 'script.js';\n"
            + "    s3.addEventListener('load', function(){alert('z')}, false);\n"
            + "    document.body.insertBefore(s3, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("", JAVASCRIPT_MIME_TYPE);
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 3236689.
     * @throws Exception if an error occurs
     */
    @Test
    public void badSrcUrl() throws Exception {
        final String html = "<html><head>\n"
                + "<script src='http://'>alert(1)</script>\n"
                + "</head><body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that the weird script src attribute used by the jQuery JavaScript library is
     * ignored silently (bug 1695279).
     * @throws Exception if the test fails
     */
    @Test
    public void invalidJQuerySrcAttribute() throws Exception {
        loadPage2("<html><body><script src='//:'></script></body></html>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "loaded", "§§URL§§abcd" })
    public void lineBreaksInUrl() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <script id='myScript' src='" + URL_SECOND + "a\rb\nc\r\nd'></script>\n"
            + "</head>\n"
            + "<body onload='alert(document.getElementById(\"myScript\").src);'>Test</body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_SECOND, "abcd"), "alert('loaded')");
        expandExpectedAlertsVariables(URL_SECOND);
        loadPageWithAlerts2(html);
    }
}
