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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLAreaElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Lai Quang Duong
 */
public class HTMLAreaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "A", "a", "A", "a8", "8Afoo", "8", "@"})
    public void readWriteAccessKey() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><map><area id='a1'/><area id='a2' accesskey='A'/></map><script>\n"
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
    @Alerts({"", "function HTMLAreaElement() { [native code] }"})
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "  var elem = document.getElementById('a1');\n"
            + "    try {\n"
            + "      log(elem);\n"
            + "      log(HTMLAreaElement);\n"
            + "    } catch(e) { logEx(e); }\n"
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
            FF_ESR = {"[object HTMLButtonElement]", "", "§§URL§§", "http://srv/htmlunit.org"})
    public void focus() throws Exception {
        final String html = DOCTYPE_HTML
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body><map><area id='a1'/><area id='a2' rel='alternate help'/></map><script>\n"
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
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"https:", "example.com", "8443", "/app/index.html", "?q=test", "#section",
             "example.com:8443", "https://example.com:8443", "user", "pass"})
    public void urlProperties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var a = document.getElementById('a1');\n"
            + "    log(a.protocol);\n"
            + "    log(a.hostname);\n"
            + "    log(a.port);\n"
            + "    log(a.pathname);\n"
            + "    log(a.search);\n"
            + "    log(a.hash);\n"
            + "    log(a.host);\n"
            + "    log(a.origin);\n"
            + "    log(a.username);\n"
            + "    log(a.password);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <map><area id='a1'"
            + " href='https://user:pass@example.com:8443/app/index.html?q=test#section'"
            + " shape='rect' coords='0,0,1,1'/></map>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", ":", "", "", "", "", "", ""})
    public void noHrefAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var a = document.getElementById('a1');\n"
            + "    log(a.href);\n"
            + "    log(a.protocol);\n"
            + "    log(a.hostname);\n"
            + "    log(a.port);\n"
            + "    log(a.pathname);\n"
            + "    log(a.search);\n"
            + "    log(a.hash);\n"
            + "    log(a.origin);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <map><area id='a1' shape='rect' coords='0,0,1,1'/></map>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "example.com",
             "", "example.com", "https://example.com"})
    public void defaultPortStripping() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            // http:80
            + "    var a = document.getElementById('a1');\n"
            + "    log(a.port);\n"
            + "    log(a.host);\n"
            // https:443
            + "    var b = document.getElementById('a2');\n"
            + "    log(b.port);\n"
            + "    log(b.host);\n"
            + "    log(b.origin);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <map>\n"
            + "    <area id='a1' href='http://example.com:80/path' shape='rect' coords='0,0,1,1'/>\n"
            + "    <area id='a2' href='https://example.com:443/path' shape='rect' coords='0,0,1,1'/>\n"
            + "  </map>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http:", "http://user:pass@example.com:8080/path/to/page?query=value#fragment",
             "9000", "example.com:9000",
             "", "example.com",
             "other.com",
             "new.com:3000", "3000",
             "/new/path",
             "?new=search", "?no=prefix",
             "#section", "#prefixed",
             "newuser", "secret",
             "https://final.com/done"})
    public void urlSetters() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var a = document.getElementById('a1');\n"
            // setProtocol
            + "    a.protocol = 'http';\n"
            + "    log(a.protocol);\n"
            + "    log(a.href);\n"
            // setPort non-default
            + "    a.port = '9000';\n"
            + "    log(a.port);\n"
            + "    log(a.host);\n"
            // setPort default (http:80) - should strip
            + "    a.port = '80';\n"
            + "    log(a.port);\n"
            + "    log(a.host);\n"
            // setHostname
            + "    a.hostname = 'other.com';\n"
            + "    log(a.hostname);\n"
            // setHost with port
            + "    a.host = 'new.com:3000';\n"
            + "    log(a.host);\n"
            + "    log(a.port);\n"
            // setPathname
            + "    a.pathname = '/new/path';\n"
            + "    log(a.pathname);\n"
            // setSearch with and without ?
            + "    a.search = '?new=search';\n"
            + "    log(a.search);\n"
            + "    a.search = 'no=prefix';\n"
            + "    log(a.search);\n"
            // setHash with and without #
            + "    a.hash = 'section';\n"
            + "    log(a.hash);\n"
            + "    a.hash = '#prefixed';\n"
            + "    log(a.hash);\n"
            // setUsername
            + "    a.username = 'newuser';\n"
            + "    log(a.username);\n"
            // setPassword
            + "    a.password = 'secret';\n"
            + "    log(a.password);\n"
            // setHref
            + "    a.href = 'https://final.com/done';\n"
            + "    log(a.href);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <map><area id='a1'"
            + " href='https://user:pass@example.com:8080/path/to/page?query=value#fragment'"
            + " shape='rect' coords='0,0,1,1'/></map>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "alternate", "help"})
    public void relList() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><map><area id='a1'/><area id='a2' rel='alternate help'/></map><script>\n"
            + LOG_TITLE_FUNCTION
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"

            + "try {\n"
            + "  log(a1.relList.length);\n"
            + "  log(a2.relList.length);\n"

            + "  for (var i = 0; i < a2.relList.length; i++) {\n"
            + "    log(a2.relList[i]);\n"
            + "  }\n"
            + "} catch(e) { logEx(e); }\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
