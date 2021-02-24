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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Unit tests for {@link HTMLAnchorElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLAnchorElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", "", "§§URL§§test.css", "stylesheet", "stylesheet1"})
    public void attributes() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var a = document.createElement('a');\n"
            + "        alert(a.href);\n"
            + "        alert(a.rel);\n"
            + "        alert(a.rev);\n"
            + "        a.href = 'test.css';\n"
            + "        a.rel  = 'stylesheet';\n"
            + "        a.rev  = 'stylesheet1';\n"
            + "        alert(a.href);\n"
            + "        alert(a.rel);\n"
            + "        alert(a.rev);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"attachEvent not available", "href"})
    public void javaScriptPreventDefaultIE() throws Exception {
        final String html
            = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = document.getElementById('link');\n"
            + "    if (!a.attachEvent) { alert('attachEvent not available'); return }\n"
            + "    a.attachEvent('onclick', handler);\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    event.returnValue = false;\n"
            + "    alert('onclick');\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "  <a id='link' href='javascript: alert(\"href\");'>link</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifyAlerts(driver, getExpectedAlerts()[0]);

        driver.findElement(By.id("link")).click();
        verifyAlerts(driver, getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("onclick")
    public void javaScriptPreventDefault() throws Exception {
        final String html
            = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = document.getElementById('link');\n"
            + "    a.addEventListener('click', handler);\n"
            + "  }\n"
            + "  function handler(event) {\n"
            + "    event.preventDefault();\n"
            + "    alert('onclick');\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload='test()'>\n"
            + "<a id='link' href='javascript: alert(\"href\");'>link</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("link")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "§§URL§§foo.html", "javascript:void(0)", "§§URL§§#", "mailto:"})
    public void defaultConversionToString() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function test() {\n"
            + "  alert(document.getElementById('myAnchor'));\n"
            + "  for (var i = 0; i < document.links.length; i++)\n"
            + "  {\n"
            + "    alert(document.links[i]);\n"
            + "  }\n"
            + "}</script></head>\n"
            + "<body onload='test()'>\n"
            + "<a name='start' id='myAnchor'/>\n"
            + "<a href='foo.html'>foo</a>\n"
            + "<a href='javascript:void(0)'>void</a>\n"
            + "<a href='#'>#</a>\n"
            + "<a href='mailto:'>mail</a>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Second")
    public void javaScriptAnchorClick() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function delegateClick() {\n"
            + "  try {\n"
            + "    document.getElementById(\"link1\").click();\n"
            + "  } catch(e) {}\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<a id='link1' href='#' onclick='document.form1.submit()'>link 1</a>\n"
            + "<form name='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "<input type=button id='button1' value='Test' onclick='delegateClick()'>\n"
            + "<input name='testText'>\n"
            + "</form>\n"
            + "</body></html>";

        final String secondHtml
            = "<html>\n"
            + "<head><title>Second</title></head>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("button1")).click();

        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§testsite1.html", "testsite1.html", "§§URL§§testsite2.html",
                "testsite2.html", "13", "testanchor", "mailto:"})
    public void getAttribute_and_href() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function doTest(anchorElement) {\n"
            + "    alert(anchorElement.href);\n"
            + "    alert(anchorElement.getAttribute('href'));\n"
            + "    anchorElement.href = 'testsite2.html';\n"
            + "    alert(anchorElement.href);\n"
            + "    alert(anchorElement.getAttribute('href'));\n"
            + "    alert(anchorElement.getAttribute('id'));\n"
            + "    alert(anchorElement.getAttribute('name'));\n"
            + "    var link2 = document.getElementById('link2');\n"
            + "    alert(link2.href);\n"
            + "  }\n</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a href='testsite1.html' id='13' name='testanchor' onClick='doTest(this);return false'>bla</a>\n"
            + "  <a href='mailto:' id='link2'>mail</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.name("testanchor")).click();

        expandExpectedAlertsVariables(URL_FIRST);

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§#test",
        "§§URL§§#", "§§URL§§"})
    public void getDefaultValue() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('absolute'));\n"
            + "    alert(document.getElementById('relative'));\n"
            + "    alert(document.getElementById('hash'));\n"
            + "    alert(document.getElementById('hashOnly'));\n"
            + "    alert(document.getElementById('empty'));\n"
            + "  }\n</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='http://htmlunit.sourceforge.net/' id='absolute'>bla</a>\n"
            + "  <a href='test' id='relative'>bla</a>\n"
            + "  <a href='#test' id='hash'>bla</a>\n"
            + "  <a href='#' id='hashOnly'>bla</a>\n"
            + "  <a href='' id='empty'>bla</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        expandExpectedAlertsVariables(URL_FIRST);

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§#test",
        "§§URL§§#", "§§URL§§"})
    public void getDefaultValueWithHash() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('absolute'));\n"
            + "    alert(document.getElementById('relative'));\n"
            + "    alert(document.getElementById('hash'));\n"
            + "    alert(document.getElementById('hashOnly'));\n"
            + "    alert(document.getElementById('empty'));\n"
            + "  }\n</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='http://htmlunit.sourceforge.net/' id='absolute'>bla</a>\n"
            + "  <a href='test' id='relative'>bla</a>\n"
            + "  <a href='#test' id='hash'>bla</a>\n"
            + "  <a href='#' id='hashOnly'>bla</a>\n"
            + "  <a href='' id='empty'>bla</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html, UrlUtils.getUrlWithNewRef(URL_FIRST, "ref"));

        expandExpectedAlertsVariables(URL_FIRST);

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§index.html#test",
                        "§§URL§§index.html#", "§§URL§§index.html"},
            IE = {"http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§index.html#test",
                        "§§URL§§index.html#", "§§URL§§"})
    public void getDefaultValueWithHashAndFileName() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('absolute'));\n"
            + "    alert(document.getElementById('relative'));\n"
            + "    alert(document.getElementById('hash'));\n"
            + "    alert(document.getElementById('hashOnly'));\n"
            + "    alert(document.getElementById('empty'));\n"
            + "  }\n</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='http://htmlunit.sourceforge.net/' id='absolute'>bla</a>\n"
            + "  <a href='test' id='relative'>bla</a>\n"
            + "  <a href='#test' id='hash'>bla</a>\n"
            + "  <a href='#' id='hashOnly'>bla</a>\n"
            + "  <a href='' id='empty'>bla</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        final WebDriver driver = loadPage2(html, UrlUtils.getUrlWithNewPath(URL_FIRST, "/index.html"));

        expandExpectedAlertsVariables(URL_FIRST);

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "not defined"})
    public void onclickToString() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for (var i = 0; i < document.links.length; i++) {\n"
            + "      var onclick = document.links[i].onclick;\n"
            + "      alert(onclick ? (onclick.toString().indexOf('alert(') != -1) : 'not defined');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a href='foo.html' onClick='alert(\"on click\")'>a1</a>\n"
            + "  <a href='foo2.html'>a2</a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "A", "a", "A", "a8", "8Afoo", "8", "@"})
    public void readWriteAccessKey() throws Exception {
        final String html
            = "<html>\n"
                + "<body>\n"
                + "  <a id='a1' href='#'></a><a id='a2' href='#' accesskey='A'></a>\n"
                + "<script>\n"
                + "  var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
                + "  alert(a1.accessKey);\n"
                + "  alert(a2.accessKey);\n"
                + "  a1.accessKey = 'a';\n"
                + "  a2.accessKey = 'A';\n"
                + "  alert(a1.accessKey);\n"
                + "  alert(a2.accessKey);\n"
                + "  a1.accessKey = 'a8';\n"
                + "  a2.accessKey = '8Afoo';\n"
                + "  alert(a1.accessKey);\n"
                + "  alert(a2.accessKey);\n"
                + "  a1.accessKey = '8';\n"
                + "  a2.accessKey = '@';\n"
                + "  alert(a1.accessKey);\n"
                + "  alert(a2.accessKey);\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that anchor href attributes are trimmed of whitespace (bug 1658064),
     * just like they are in IE and Firefox.
     * Verifies that href of anchor without href is empty string.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"9", "9", "true", "false"})
    public void hrefTrimmed() throws Exception {
        final String html = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('a').href.length);\n"
            + "    alert(document.getElementById('b').href.length);\n"
            + "    alert(document.getElementById('c').href === '');\n"
            + "    alert(document.getElementById('d').href === '');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a href=' http://a/ ' id='a'>a</a> "
            + "  <a href='  http://b/    ' id='b'>b</a>\n"
            + "  <a name='myAnchor' id='c'>c</a>\n"
            + "  <a href='' id='d'>d</a>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"# inner", "main"})
    public void javascriptTargetNone() throws Exception {
        javascriptTarget("", 0, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"# inner", "main"})
    public void javascriptTargetEmpty() throws Exception {
        javascriptTarget("target=''", 0, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "inner", "# "})
    @BuggyWebDriver({"1", "Please run manually", ""})
    public void javascriptTargetWhitespace() throws Exception {
        final String[] alerts = getExpectedAlerts();
        javascriptTarget("target='  '",
                            Integer.parseInt(alerts[0]),
                            alerts[1], alerts[2]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"# inner", "main"})
    public void javascriptTargetSelf() throws Exception {
        javascriptTarget("target='_self'", 0, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "inner", "# "})
    @BuggyWebDriver({"1", "Please run manually", ""})
    public void javascriptTargetBlank() throws Exception {
        final String[] alerts = getExpectedAlerts();
        javascriptTarget("target='_blank'",
                            Integer.parseInt(alerts[0]),
                            alerts[1], alerts[2]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inner", "# main"})
    public void javascriptTargetTop() throws Exception {
        javascriptTarget("target='_top'", 0, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inner", "# main"})
    public void javascriptTargetParent() throws Exception {
        javascriptTarget("target='_parent'", 0, getExpectedAlerts()[0], getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "inner", "# "})
    @BuggyWebDriver({"1", "Please run manually", ""})
    public void javascriptTargetUnknown() throws Exception {
        final String[] alerts = getExpectedAlerts();
        javascriptTarget("target='unknown'",
                            Integer.parseInt(alerts[0]),
                            alerts[1], alerts[2]);
    }

    private void javascriptTarget(final String target, final int newWindows,
                    final String frameAlerts, final String windowAlerts) throws Exception {
        assertTrue(newWindows < 2);

        // real browsers (selenium) are getting confused by this scenario
        // and as result the stuck in the code that tries to get the body
        if (newWindows > 0 && useRealBrowser()) {
            assertEquals(frameAlerts, "Please run manually");
            return;
        }

        final String html
            = "<html>\n"
            + "<head><title>main</title></head>\n"
            + "<body title='main'>\n"
            + "  <iframe id='testFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String secondHtml
            = "<html>\n"
            + "<head><title>inner</title></head>\n"
            + "<body title='inner'>\n"
            + "  <a id='tester' " + target
                + " href='javascript: try { document.body.setAttribute(\"title\", \"# \" + document.title); } "
                + "catch(e) { alert(e); }'>no href</a>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        final WebDriver driver = loadPage2(html);

        final String firstWindow = driver.getWindowHandle();

        driver.switchTo().frame("testFrame");
        assertEquals(1, driver.getWindowHandles().size());
        driver.findElement(By.id("tester")).click();

        String titleVal = driver.findElement(By.tagName("body")).getAttribute("title");
        assertEquals(frameAlerts, titleVal);

        // we have to switch back to the outer content
        // otherwise selenium gets confused
        driver.switchTo().defaultContent();

        final Set<String> windows = driver.getWindowHandles();
        assertEquals(1 + newWindows, windows.size());

        if (newWindows > 0) {
            windows.remove(firstWindow);
            driver.switchTo().window(windows.iterator().next());
        }

        titleVal = driver.findElement(By.tagName("body")).getAttribute("title");
        assertEquals(windowAlerts, titleVal);
    }

    /**
     * Regression test for
     * <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1689798&group_id=47038">448</a>.
     * In href, "this" should be the window and not the link.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void thisInJavascriptHref() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <a href='javascript:alert(this === window)'>link 1</a>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();

        assertEquals(1, getMockWebConnection().getRequestCount());
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"§§URL§§second/", "object", "[object HTMLAnchorElement]"},
            CHROME = {"§§URL§§second/", "object", "function HTMLAnchorElement() { [native code] }"},
            EDGE = {"§§URL§§second/", "object", "function HTMLAnchorElement() { [native code] }"},
            FF = {"§§URL§§second/", "object", "function HTMLAnchorElement() {\n    [native code]\n}"},
            FF78 = {"§§URL§§second/", "object", "function HTMLAnchorElement() {\n    [native code]\n}"})
    public void typeof() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.links[0]);\n"
            + "      alert(typeof document.links[0]);\n"
            + "      alert(HTMLAnchorElement);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a id='link' href='" + URL_SECOND + "'>link</a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "text/html", "TExT/hTMl", " text/html ", "application/pdf", "unknown"})
    public void getType() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alertType('idWithout');\n"
            + "    alertType('idEmpty');\n"
            + "    alertType('idText');\n"
            + "    alertType('idCase');\n"
            + "    alertType('idWhitespace');\n"
            + "    alertType('idPdf');\n"
            + "    alertType('idUnknown');\n"
            + "  }\n"
            + "  function alertType(id) {\n"
            + "    var anchor = document.getElementById(id);\n"
            + "    alert(anchor.type);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a id='idWithout' href='" + URL_SECOND + "'>link</a>\n"
            + "  <a id='idEmpty' href='" + URL_SECOND + "' type=''>link</a>\n"
            + "  <a id='idText' href='" + URL_SECOND + "' type='text/html'>link</a>\n"
            + "  <a id='idCase' href='" + URL_SECOND + "' type='TExT/hTMl'>link</a>\n"
            + "  <a id='idWhitespace' href='" + URL_SECOND + "' type=' text/html '>link</a>\n"
            + "  <a id='idPdf' href='" + URL_SECOND + "' type='application/pdf'>link</a>\n"
            + "  <a id='idUnknown' href='" + URL_SECOND + "' type='unknown'>link</a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/html", "", " TExT/hTMl  ", "unknown", "application/pdf"})
    public void setType() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var anchor = document.getElementById('id');\n"
            + "    alert(anchor.type);\n"

            + "    anchor.type = '';\n"
            + "    alert(anchor.type);\n"

            + "    anchor.type = ' TExT/hTMl  ';\n"
            + "    alert(anchor.type);\n"

            + "    anchor.type = 'unknown';\n"
            + "    alert(anchor.type);\n"

            + "    anchor.type = 'application/pdf';\n"
            + "    alert(anchor.type);\n"

            + "  }\n"
            + "  function alertType(id) {\n"
            + "    var anchor = document.getElementById(id);\n"
            + "    alert(anchor.type);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <a id='id' href='" + URL_SECOND + "' type='text/html'>link</a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"http:||||||/", "https:||||||/", "mailto:||||||foo@foo.com", "tel:||||||123456",
                        "foo:||||||blabla", "file:||||||/p://", "file:||||||/p:/", "file:||||||/p:/TeMp"},
            CHROME = {":||||||", ":||||||", "mailto:||||||foo@foo.com", "tel:||||||123456",
                        "foo:||||||blabla", "file:||||||/P://", "file:||||||/P:/", "file:||||||/P:/TeMp"},
            EDGE = {":||||||", ":||||||", "mailto:||||||foo@foo.com", "tel:||||||123456",
                        "foo:||||||blabla", "file:||||||/P://", "file:||||||/P:/", "file:||||||/P:/TeMp"},
            FF = {"http:||||||", "http:||||||", "mailto:||||||", "tel:||||||",
                    "foo:||||||", "p:||||||", "p:||||||", "p:||||||"},
            FF78 = {"http:||||||", "http:||||||", "mailto:||||||", "tel:||||||",
                    "foo:||||||", "p:||||||", "p:||||||", "p:||||||"})
    public void propertiesNonStandardHref() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <a href='http://'>http://</a>\n"
            + "  <a href='https://'>https://</a>\n"
            + "  <a href='mailto:foo@foo.com'>foo@foo.com</a>\n"
            + "  <a href='tel:123456'>tel:123456</a>\n"
            + "  <a href='foo:blabla'>foo:blabla</a>\n"
            + "  <a href='p://'>p://</a>\n"
            + "  <a href='p:/'>p:/</a>\n"
            + "  <a href='p:/TeMp'>p:/TeMp</a>\n"

            + "  <script>\n"
            + "  var links = document.getElementsByTagName('a');\n"
            + "  for (var i = 0; i < links.length; i++) {\n"
            + "    var link = links[i];\n"
            + "    var props = [link.protocol, link.host, link.hostname, \n"
            + "           link.search, link.hash, link.port, link.pathname];\n"
            + "    alert(props.join('|'));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "hi"})
    public void charset() throws Exception {
        attribute("charset", "hi");
    }

    private void attribute(final String attribute, final String value) throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var a = document.createElement('a');\n"
            + "        alert(a." + attribute + ");\n"
            + "        a." + attribute + " = '" + value + "';\n"
            + "        alert(a." + attribute + ");\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "0,0"},
            IE = {"", "0,0,0,0"})
    @NotYetImplemented(IE)
    public void coords() throws Exception {
        attribute("coords", "0,0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "en"})
    public void hreflang() throws Exception {
        attribute("hreflang", "en");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", ""},
            IE = {"undefined", "something"})
    public void origin() throws Exception {
        attribute(HttpHeader.ORIGIN_LC, "something");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "§§URL§§", "§§URL§§", "§§URL§§", "http://www.htmlunit.org",
                        "http://www.htmlunit.org:1234", "https://www.htmlunit.org:1234"},
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined",
                    "undefined", "undefined"})
    public void originAttrib() throws Exception {
        expandExpectedAlertsVariables(new URL("http://localhost:" + PORT));

        final String html =
                "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + "      function test() {\n"
                + "        for(i=0; i<7; i++) {\n"
                + "          var a = document.getElementById('a'+i);\n"
                + "          alert(a.origin);\n"
                + "        }\n"
                + "      }\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body onload='test()'>\n"
                + "    <a id='a0'>a0</a>\n"
                + "    <a id='a1' href=''>a1</a>\n"
                + "    <a id='a2' href='  \t '>a2</a>\n"
                + "    <a id='a3' href='relative.html'>a3</a>\n"
                + "    <a id='a4' href='http://www.htmlunit.org/index.html'>a4</a>\n"
                + "    <a id='a5' href='http://www.htmlunit.org:1234/index.html'>a5</a>\n"
                + "    <a id='a6' href='https://www.htmlunit.org:1234/index.html'>a6</a>\n"
                + "  </body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"-null", "-", "-  \t ", "no-referrer-no-referrer",
                        "origin-origin", "unsafe-url-unsafe-url", "-unknown"},
            IE = {"undefined-null", "undefined-", "undefined-  \t ",
                        "undefined-no-referrer", "undefined-origin",
                        "undefined-unsafe-url", "undefined-unknown"})
    public void referrerPolicy() throws Exception {
        final String html =
                "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + "      function test() {\n"
                + "        for(i=0; i<7; i++) {\n"
                + "          var a = document.getElementById('a'+i);\n"
                + "          alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"
                + "        }\n"
                + "      }\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body onload='test()'>\n"
                + "    <a id='a0'>a0</a>\n"
                + "    <a id='a1' referrerPolicy=''>a1</a>\n"
                + "    <a id='a2' referrerPolicy='  \t '>a2</a>\n"
                + "    <a id='a3' referrerPolicy='no-referrer'>a3</a>\n"
                + "    <a id='a4' referrerPolicy='origin'>a4</a>\n"
                + "    <a id='a5' referrerPolicy='unsafe-url'>a5</a>\n"
                + "    <a id='a6' referrerPolicy='unknown'>a6</a>\n"
                + "  </body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"origin-origin", "-unknown", "no-referrer-no-referrer",
                        "-", "no-referrer-NO-reFerrer", "origin-origin", "- ", "-unknown"},
            IE = {"undefined-origin", "unknown-origin", "no-referrer-origin",
                        "-origin", "NO-reFerrer-origin", "NO-reFerrer-origin",
                        "NO-reFerrer- ", "NO-reFerrer-unknown"})
    public void setReferrerPolicy() throws Exception {
        final String html =
                "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + "      function test() {\n"
                + "        var a = document.getElementById('tester');\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"

                + "        a.referrerPolicy = 'unknown';\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"

                + "        a.referrerPolicy = 'no-referrer';\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"

                + "        a.referrerPolicy = '';\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"

                + "        a.referrerPolicy = 'NO-reFerrer';\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"

                + "        a.setAttribute('referrerPolicy', 'origin');\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"

                + "        a.setAttribute('referrerPolicy', ' ');\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"

                + "        a.setAttribute('referrerPolicy', 'unknown');\n"
                + "        alert(a.referrerPolicy + '-' + a.getAttribute('referrerPolicy'));\n"
                + "      }\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body onload='test()'>\n"
                + "    <a id='tester' referrerPolicy='origin'>a4</a>\n"
                + "  </body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLButtonElement]", "[object HTMLButtonElement]",
                "§§URL§§", "http://srv/htmlunit.org"})
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

            + "      testNode = document.getElementById('myA');\n"
            + "      testNode.focus();\n"
            + "      alert(document.activeElement);\n"

            + "      testNode = document.getElementById('myHrefEmpty');\n"
            + "      testNode.focus();\n"
            + "      alert(document.activeElement);\n"

            + "      testNode = document.getElementById('myHref');\n"
            + "      testNode.focus();\n"
            + "      alert(document.activeElement);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <button id='myButton'>Press</button>\n"
            + "  <a id='myA'>anchor</a>\n"
            + "  <a id='myHrefEmpty' href=''>anchor</a>\n"
            + "  <a id='myHref' href='http://srv/htmlunit.org'>anchor</a>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {},
            CHROME = "PING",
            EDGE = "PING")
    public void ping() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <a href='" + URL_SECOND + "' ping='test2?h'>clickMe</a>\n"
            + "</body></html>";

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/test2", PingServlet.class);

        PingServlet.HEADERS_.clear();
        PingServlet.BODY_ = null;

        getMockWebConnection().setResponse(URL_SECOND, "something");
        final WebDriver driver = loadPage2(html, servlets);
        driver.findElement(By.linkText("clickMe")).click();

        final String[] expectedAlerts = getExpectedAlerts();
        final String firstString;
        final String secondString;
        final String body;
        if (expectedAlerts.length != 0) {
            firstString = URL_FIRST.toString();
            secondString = URL_SECOND.toString();
            body = PingServlet.BODY_;
        }
        else {
            firstString = null;
            secondString = null;
            body = null;
        }
        assertEquals(firstString, PingServlet.HEADERS_.get(HttpHeader.PING_FROM));
        assertEquals(secondString, PingServlet.HEADERS_.get(HttpHeader.PING_TO));
        assertEquals(body, PingServlet.BODY_);
    }

    /**
     * Servlet for {@link #ping()}.
     */
    public static class PingServlet extends HttpServlet {

        private static Map<String, String> HEADERS_ = new HashMap<>();
        private static String BODY_;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding(UTF_8.name());
            response.setContentType(MimeType.TEXT_HTML);

            for (final Enumeration<String> en = request.getHeaderNames(); en.hasMoreElements();) {
                final String key = en.nextElement();
                HEADERS_.put(key, request.getHeader(key));
            }

            final BufferedReader reader = request.getReader();
            final StringWriter stringSriter = new StringWriter();
            IOUtils.copy(reader, stringSriter);
            BODY_ = stringSriter.toString();
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "alternate help", "prefetch", "prefetch", "not supported", "notsupported"})
    public void readWriteRel() throws Exception {
        final String html
            = "<html><body><a id='a1'>a1</a><a id='a2' rel='alternate help'>a2</a><script>\n"
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"

            + "alert(a1.rel);\n"
            + "alert(a2.rel);\n"

            + "a1.rel = 'prefetch';\n"
            + "a2.rel = 'prefetch';\n"
            + "alert(a1.rel);\n"
            + "alert(a2.rel);\n"

            + "a1.rel = 'not supported';\n"
            + "a2.rel = 'notsupported';\n"
            + "alert(a1.rel);\n"
            + "alert(a2.rel);\n"

            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "2", "alternate", "help"},
            IE = "exception")
    public void relList() throws Exception {
        final String html
            = "<html><body><a id='a1'>a1</a><a id='a2' rel='alternate help'>a2</a><script>\n"
            + "var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"

            + "try {\n"
            + "  alert(a1.relList.length);\n"
            + "  alert(a2.relList.length);\n"

            + "  for (var i = 0; i < a2.relList.length; i++) {\n"
            + "    alert(a2.relList[i]);\n"
            + "  }\n"
            + "} catch(e) { alert('exception'); }\n"

            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "user", "user", "",
                "", "",
                "Tester", "https://Tester:password@developer.mozilla.org/",
                "Tester", "https://Tester@developer.mozilla.org/",
                "Tester", "https://Tester@developer.mozilla.org/"},
            IE = {"undefined", "undefined", "undefined", "undefined"})
    @HtmlUnitNYI(CHROME = {"", "user", "user", "",
                "", "",
                "Tester", "https://Tester:password@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org"},
            EDGE = {"", "user", "user", "",
                "", "",
                "Tester", "https://Tester:password@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org"},
            FF78 = {"", "user", "user", "",
                "", "",
                "Tester", "https://Tester:password@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org"},
            FF = {"", "user", "user", "",
                "", "",
                "Tester", "https://Tester:password@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org",
                "Tester", "https://Tester@developer.mozilla.org"})
    public void readWriteUsername() throws Exception {
        final String html
            = "<html><body><a id='a1'>a1</a>"
                    + "<a id='a2' href='https://user:password@developer.mozilla.org'>a2</a>"
                    + "<a id='a3' href='https://user@developer.mozilla.org'>a3</a>"
                    + "<a id='a4' href='https://developer.mozilla.org'>a3</a>"
                    + "<script>\n"
            + "var a1 = document.getElementById('a1'),"
                + "a2 = document.getElementById('a2'),"
                + "a3 = document.getElementById('a3'),"
                + "a4 = document.getElementById('a4');\n"

            + "alert(a1.username);\n"
            + "alert(a2.username);\n"
            + "alert(a3.username);\n"
            + "alert(a4.username);\n"

            + "if (a1.username != undefined) {\n"

            + "a1.username = 'Tester';\n"
            + "a2.username = 'Tester';\n"
            + "a3.username = 'Tester';\n"
            + "a4.username = 'Tester';\n"

            + "alert(a1.username);\n"
            + "alert(a1.href);\n"
            + "alert(a2.username);\n"
            + "alert(a2.href);\n"
            + "alert(a3.username);\n"
            + "alert(a3.href);\n"
            + "alert(a4.username);\n"
            + "alert(a4.href);\n"

            + "}\n"

            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "password", "password", "",
                "", "",
                "Tester", "https://user:Tester@developer.mozilla.org/",
                "Tester", "https://:Tester@developer.mozilla.org/",
                "Tester", "https://:Tester@developer.mozilla.org/"},
            IE = {"undefined", "undefined", "undefined", "undefined"})
    @HtmlUnitNYI(CHROME = {"", "password", "password", "",
                "", "",
                "Tester", "https://user:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org"},
            EDGE = {"", "password", "password", "",
                "", "",
                "Tester", "https://user:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org"},
            FF78 = {"", "password", "password", "",
                "", "",
                "Tester", "https://user:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org"},
            FF = {"", "password", "password", "",
                "", "",
                "Tester", "https://user:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org",
                "Tester", "https://:Tester@developer.mozilla.org"})
    public void readWritePassword() throws Exception {
        final String html
            = "<html><body><a id='a1'>a1</a>"
                    + "<a id='a2' href='https://user:password@developer.mozilla.org'>a2</a>"
                    + "<a id='a3' href='https://:password@developer.mozilla.org'>a3</a>"
                    + "<a id='a4' href='https://developer.mozilla.org'>a3</a>"
                    + "<script>\n"
            + "var a1 = document.getElementById('a1'),"
                + "a2 = document.getElementById('a2'),"
                + "a3 = document.getElementById('a3'),"
                + "a4 = document.getElementById('a4');\n"

            + "alert(a1.password);\n"
            + "alert(a2.password);\n"
            + "alert(a3.password);\n"
            + "alert(a4.password);\n"

            + "if (a1.password != undefined) {\n"

            + "a1.password = 'Tester';\n"
            + "a2.password = 'Tester';\n"
            + "a3.password = 'Tester';\n"
            + "a4.password = 'Tester';\n"

            + "alert(a1.password);\n"
            + "alert(a1.href);\n"
            + "alert(a2.password);\n"
            + "alert(a2.href);\n"
            + "alert(a3.password);\n"
            + "alert(a3.href);\n"
            + "alert(a4.password);\n"
            + "alert(a4.href);\n"

            + "}\n"

            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
