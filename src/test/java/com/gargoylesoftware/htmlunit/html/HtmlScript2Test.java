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
package com.gargoylesoftware.htmlunit.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.util.Map;

import org.apache.commons.io.ByteOrderMark;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

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
            = "<html><head><title>Page A</title>\n"
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
    @Alerts({"created", "hello", "replaced"})
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
     * See https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types#JavaScript_types.
     * @exception Exception If the test fails
     */
    @Test
    @Alerts({"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G"})
    public void typeValues() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <script type='application/javascript'>alert('1');</script>\n"
            + "  <script type='application/ecmascript'>alert('2');</script>\n"
            + "  <script type='application/x-ecmascript'>alert('3');</script>\n"
            + "  <script type='application/x-javascript'>alert('4');</script>\n"
            + "  <script type='text/javascript'>alert('5');</script>\n"
            + "  <script type='text/ecmascript'>alert('6');</script>\n"
            + "  <script type='text/javascript1.0'>alert('7');</script>\n"
            + "  <script type='text/javascript1.1'>alert('8');</script>\n"
            + "  <script type='text/javascript1.2'>alert('9');</script>\n"
            + "  <script type='text/javascript1.3'>alert('A');</script>\n"
            + "  <script type='text/javascript1.4'>alert('B');</script>\n"
            + "  <script type='text/javascript1.5'>alert('C');</script>\n"
            + "  <script type='text/jscript'>alert('D');</script>\n"
            + "  <script type='text/livescript'>alert('E');</script>\n"
            + "  <script type='text/x-ecmascript'>alert('F');</script>\n"
            + "  <script type='text/x-javascript'>alert('G');</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts({"1", "5", "7"})
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
     * See bug #469.
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
    @Alerts({"deferred", "start", "dcl listener added", "end", "dcLoaded", "onload"})
    public void deferInline() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script defer>alert('deferred')</script>\n"
            + "  <script>alert('start')</script>"
            + "  <script>\n"
            + "    document.addEventListener('DOMContentLoaded', function(event) { alert('dcLoaded') });\n"
            + "    alert('dcl listener added')</script>"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='alert(\"onload\")'>\n"
            + "</body>\n"
            + "<script>alert('end')</script>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"start", "dcl listener added", "end", "deferred-1", "deferred-2", "deferred-3", "dcLoaded", "onload"})
    public void deferExternal() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script defer src='script1.js'></script>\n"
            + "  <script>alert('start')</script>"
            + "  <script>\n"
            + "    document.addEventListener('DOMContentLoaded', function(event) { alert('dcLoaded') });\n"
            + "    alert('dcl listener added')</script>"
            + "  </script>\n"
            + "  <script defer src='script2.js'></script>\n"
            + "</head>\n"
            + "<body onload='alert(\"onload\")'>\n"
            + "  <div id='abc'>Test</div>\n"
            + "</body>\n"
            + "<script defer src='script3.js'></script>\n"
            + "<script>alert('end')</script>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "script1.js"), "alert('deferred-1');");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script2.js"), "alert('deferred-2');");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script3.js"), "alert('deferred-3');");
        loadPageWithAlerts2(html);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"dcl listener added", "head-end", "end",
                        "deferred-2", "deferred-1", "deferred-3", "dcLoaded", "onload"},
            CHROME = {"dcl listener added", "head-end", "end",
                        "deferred-1", "deferred-3", "dcLoaded", "deferred-2", "onload"},
            EDGE = {"dcl listener added", "head-end", "end",
                        "deferred-1", "deferred-3", "dcLoaded", "deferred-2", "onload"},
            IE = {"dcl listener added", "head-end", "deferred-2", "end",
                        "deferred-1", "deferred-3", "dcLoaded", "onload"})
    @HtmlUnitNYI(CHROME = {"dcl listener added", "head-end", "end",
                        "deferred-1", "deferred-2", "deferred-3", "dcLoaded", "onload"},
            EDGE = {"dcl listener added", "head-end", "end",
                        "deferred-1", "deferred-2", "deferred-3", "dcLoaded", "onload"},
            FF = {"dcl listener added", "head-end", "end",
                        "deferred-1", "deferred-2", "deferred-3", "dcLoaded", "onload"},
            FF78 = {"dcl listener added", "head-end", "end",
                        "deferred-1", "deferred-2", "deferred-3", "dcLoaded", "onload"},
            IE = {"dcl listener added", "head-end", "end",
                        "deferred-1", "deferred-2", "deferred-3", "dcLoaded", "onload"})
    public void deferDynamicExternal() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    document.addEventListener('DOMContentLoaded', function(event) { alert('dcLoaded') });\n"
            + "    alert('dcl listener added')</script>"
            + "  </script>\n"
            + "  <script defer src='script1.js'></script>\n"
            + "  <script>\n"
            + "    head = document.getElementsByTagName('head')[0];\n"

            + "    script = document.createElement('script');\n"
            + "    script.setAttribute('defer', 'defer');\n"
            + "    script.setAttribute('src', 'script2.js');\n"
            + "    head.appendChild(script);\n"
            + "  </script>\n"
            + "  <script defer src='script3.js'></script>\n"
            + "  <script>alert('head-end')</script>\n"
            + "</head>\n"
            + "<body onload='alert(\"onload\")'>\n"
            + "  <div id='abc'>Test</div>\n"
            + "</body>\n"
            + "<script>alert('end')</script>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "script1.js"), "alert('deferred-1');");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script2.js"), "alert('deferred-2');");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script3.js"), "alert('deferred-3');");

        getMockWebConnection().setResponse(new URL(URL_FIRST, "script1.js"), "alert('deferred-1');");
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for replaceChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
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
    @Alerts({"1", "2"})
    public void executesMultipleTextNodes() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + "  var script = document.createElement('script');\n"
            + "  try {\n"
            + "    script.appendChild(document.createTextNode('alert(\"1\");'));\n"
            + "    script.appendChild(document.createTextNode('alert(\"2\");'));\n"
            + "  } catch(e) {\n"
            + "    script.text = 'alert(\"1\");alert(\"2\");';\n"
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
            + "  var script = document.createElement('script');\n"
            + "  try {\n"
            + "  script.appendChild(document.createTextNode('var x=1;'));\n;\n"
            + "  script.appendChild(document.createTextNode('x=2;'));\n;\n"
            + "  } catch(e) {\n"
            + "    script.text = 'var x=1;x=2;';\n;\n"
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
            + "    var script = document.createElement('script');\n"
            + "    script.appendChild(document.createTextNode('alert(\"1\");'));\n"
            + "    script.appendChild(document.createTextNode('alert(\"2\");'));\n"
            + "    script.text = 'alert(\"3\");';\n"
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
    @Alerts({"1", "2", "3"})
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

        getMockWebConnection().setResponse(new URL(URL_FIRST, "script2.js"), "alert(2);");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script3.js"), "alert(3);");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script4.js"), "alert(4);");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script5.js"), "alert(5);");

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"s-x", "z"},
            IE = {"s-x", "x", "z"})
    public void addEventListener_load() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s1 = document.createElement('script');\n"
            + "    s1.text = 'alert(\"s-x\")';\n"
            + "    s1.addEventListener('load', function() {alert('x')}, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "    \n"
            + "    var s2 = document.createElement('script');\n"
            + "    s2.src = '//:';\n"
            + "    s2.addEventListener('load', function() {alert('y')}, false);\n"
            + "    document.body.insertBefore(s2, document.body.firstChild);\n"
            + "    \n"
            + "    var s3 = document.createElement('script');\n"
            + "    s3.src = 'script.js';\n"
            + "    s3.addEventListener('load', function() {alert('z')}, false);\n"
            + "    document.body.insertBefore(s3, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse("", MimeType.APPLICATION_JAVASCRIPT);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "load",
            IE = "error")
    public void addEventListener_NoContent() throws Exception {
        // use always a different url to avoid caching effects
        final URL scriptUrl = new URL(URL_SECOND, "" + System.currentTimeMillis() + ".js");

        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s1 = document.createElement('script');\n"
            + "    s1.src = '" + scriptUrl + "';\n"
            + "    s1.addEventListener('load', function() {alert('load')}, false);\n"
            + "    s1.addEventListener('error', function() {alert('error')}, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(scriptUrl, (String) null, HttpStatus.SC_NO_CONTENT, "No Content",
                                                MimeType.APPLICATION_JAVASCRIPT, null);
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug #1267.
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
     * ignored silently (bug #455).
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
    @Alerts({"loaded", "§§URL§§abcd"})
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u0623\u0647\u0644\u0627\u064b\u0623\u0647\u0644\u0627"
            + "\u064b\u0623\u0647\u0644\u0627\u064b\u0623\u0647\u0644\u0627\u064b")
    public void incorrectCharset() throws Exception {
        final String html
            = "<html><head>\n"
            + "  <script src='" + URL_SECOND + "' charset='" + ISO_8859_1 + "'></script>\n"
            + "</head>\n"
            + "<body></body>\n"
            + "</html>";

        final String script = new String(ByteOrderMark.UTF_8.getBytes())
                + "alert('" + "\u0623\u0647\u0644\u0627\u064b\u0623\u0647\u0644\u0627"
                            + "\u064b\u0623\u0647\u0644\u0627\u064b\u0623\u0647\u0644\u0627\u064b" + "');";
        getMockWebConnection().setResponse(URL_SECOND, script, MimeType.APPLICATION_JAVASCRIPT, UTF_8);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoad() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.js"), "");
        onLoadOnError("src='simple.js' type='text/javascript'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"onLoad", "body onLoad"},
            IE = "body onLoad")
    public void onLoadTypeWhitespace() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.js"), "");
        onLoadOnError("src='simple.js' type='\t  text/javascript     '");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onError", "body onLoad"})
    public void onError() throws Exception {
        onLoadOnError("src='unknown.js' type='text/javascript'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onError", "body onLoad"})
    public void onLoadOnErrorWithoutType() throws Exception {
        onLoadOnError("src='unknown.js'");
    }

    private void onLoadOnError(final String attribs) throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script " + attribs
                        + " onload='alert(\"onLoad\")' onerror='alert(\"onError\")'></script>\n"
                + "</head>\n"
                + "<body onload='alert(\"body onLoad\")'>\n"
                + "</body>\n"
                + "</html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"from script", "onLoad [object Event]"})
    public void onLoadDynamic() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.js"), "log('from script');");
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var dynScript = document.createElement('script');\n"
                + "      dynScript.type = 'text/javascript';\n"
                + "      dynScript.onload = function (e) { log(\"onLoad \" + e) };\n"
                + "      document.head.appendChild(dynScript);\n"
                + "      dynScript.src = 'simple.js';"
                + "    }\n"

                + "    function log(x) {\n"
                + "      document.getElementById('log').value += x + '\\n';\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body>\n"
                + "  <textarea id='log' cols='80' rows='40'></textarea>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLScriptElement]",
            IE = "undefined")
    public void currentScriptInline() throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script id='tester'>\n"
                + "    alert(document.currentScript);\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "undefined")
    public void currentScriptFunction() throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script id='tester'>\n"
                + "    function test() {\n"
                + "      alert(document.currentScript);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLScriptElement]",
            IE = "undefined")
    public void currentScriptExternal() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.js"), "alert(document.currentScript);");
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script id='tester' src='simple.js' type='text/javascript'></script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 "
            + "21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39")
    public void scriptExecutionOrder() throws Exception {
        final StringBuilder html = new StringBuilder();
        html.append("<html>\n<head>\n");
        int i = 0;
        for ( ; i < 20; i++) {
            html.append("  <script type='text/javascript'>document.title += ' ")
                .append(Integer.toString(i))
                .append("'</script>\n");
        }
        html.append("</head>\n<body>\n");
        for ( ; i < 40; i++) {
            html.append("  <script type='text/javascript'>document.title += ' ")
                .append(Integer.toString(i))
                .append("'</script>\n");
        }
        html.append("</body>\n</html>");

        final WebDriver driver = loadPage2(html.toString());
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Tests the 'Referer' HTTP header.
     * @throws Exception on test failure
     */
    @Test
    @Alerts("§§URL§§index.html?test")
    public void refererHeader() throws Exception {
        final String firstContent
            = "<html><head><title>Page A</title></head>\n"
            + "<body><script src='" + URL_SECOND + "'/></body>\n"
            + "</html>";

        final String secondContent = "var i = 7;";

        expandExpectedAlertsVariables(URL_FIRST);

        final URL indexUrl = new URL(URL_FIRST.toString() + "index.html");

        getMockWebConnection().setResponse(indexUrl, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        loadPage2(firstContent, new URL(URL_FIRST.toString() + "index.html?test#ref"));

        assertEquals(2, getMockWebConnection().getRequestCount());

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));
    }

}
