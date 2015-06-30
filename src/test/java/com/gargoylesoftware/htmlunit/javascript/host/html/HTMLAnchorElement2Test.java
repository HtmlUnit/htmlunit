/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Unit tests for {@link HTMLAnchorElement}.
 *
 * @version $Revision$
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
    @Alerts(DEFAULT = { "", "", "", "§§URL§§test.css", "stylesheet", "stylesheet1" },
            IE8 = { "", "", "", "test.css", "stylesheet", "stylesheet1" })
    @NotYetImplemented(IE8)
    public void attributes() throws Exception {
        final String html =
              "<html>\n"
            + "    <body onload='test()'>\n"
            + "        <script>\n"
            + "            function test() {\n"
            + "                var a = document.createElement('a');\n"
            + "                alert(a.href);\n"
            + "                alert(a.rel);\n"
            + "                alert(a.rev);\n"
            + "                a.href = 'test.css';\n"
            + "                a.rel  = 'stylesheet';\n"
            + "                a.rev  = 'stylesheet1';\n"
            + "                alert(a.href);\n"
            + "                alert(a.rel);\n"
            + "                alert(a.rev);\n"
            + "            }\n"
            + "        </script>\n"
            + "    </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "attachEvent not available", "href" },
            IE8 = "onclick")
    public void javaScriptPreventDefaultIE() throws Exception {
        final String html
            = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = document.getElementById('link');\n"
            + "    if (!a.attachEvent) { alert('attachEvent not available'); return };\n"
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
        driver.findElement(By.id("link")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "onclick",
            IE8 = { "addEventListener not available", "href" })
    public void javaScriptPreventDefault() throws Exception {
        final String html
            = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = document.getElementById('link');\n"
            + "    if (!a.addEventListener) { alert('addEventListener not available'); return };\n"
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
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "§§URL§§foo.html", "javascript:void(0)", "§§URL§§#", "mailto:" })
    public void defaultConversionToString() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title><script>\n"
            + "function test() {\n"
            + "  alert(document.getElementById('myAnchor'));\n"
            + "  for (var i=0; i<document.links.length; ++i)\n"
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
            + "  try {"
            + "    document.getElementById(\"link1\").click();\n"
            + "  } catch(e) {}\n"
            + "}"
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

        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "§§URL§§testsite1.html", "testsite1.html", "§§URL§§testsite2.html",
                "testsite2.html", "13", "testanchor", "mailto:" },
            IE8 = { "§§URL§§testsite1.html", "§§URL§§testsite1.html", "testsite2.html",
                "testsite2.html", "13", "testanchor", "mailto:" })
    @NotYetImplemented(IE8)
    public void getAttribute_and_href() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function doTest(anchorElement) {\n"
            + "    alert(anchorElement.href);\n"
            + "    alert(anchorElement.getAttribute('href'));\n"
            + "    anchorElement.href='testsite2.html';\n"
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
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§#test",
        "§§URL§§#", "§§URL§§" })
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
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§#test",
        "§§URL§§#", "§§URL§§" })
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
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§index.html#test",
                        "§§URL§§index.html#", "§§URL§§index.html" },
            IE = { "http://htmlunit.sourceforge.net/", "§§URL§§test", "§§URL§§index.html#test",
                        "§§URL§§index.html#", "§§URL§§" })
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
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "not defined" })
    public void onclickToString() throws Exception {
        final String html
            = "<html><head><title>AnchorTest</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for (var i=0; i<document.links.length; ++i) {\n"
            + "        var onclick = document.links[i].onclick;\n"
            + "        alert(onclick ? (onclick.toString().indexOf('alert(') != -1) : 'not defined');\n"
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
    @Alerts({ "", "A", "a", "A", "a8", "8Afoo", "8", "@" })
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
    @Alerts({ "9", "9", "true", "false" })
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
    @Alerts("inner")
    public void javascriptTargetNone() throws Exception {
        javascriptTarget("", 0, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inner")
    public void javascriptTargetEmpty() throws Exception {
        javascriptTarget("target=''", 0, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = { "0", "inner" })
    @NotYetImplemented(CHROME)
    public void javascriptTargetWhitespace() throws Exception {
        final String[] alerts = getExpectedAlerts();
        javascriptTarget("target='  '",
                            Integer.parseInt(alerts[0]),
                            Arrays.copyOfRange(alerts, 1, alerts.length));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inner")
    public void javascriptTargetSelf() throws Exception {
        javascriptTarget("target='_self'", 0, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = { "0", "inner" })
    @NotYetImplemented(CHROME)
    public void javascriptTargetBlank() throws Exception {
        final String[] alerts = getExpectedAlerts();
        javascriptTarget("target='_blank'",
                            Integer.parseInt(alerts[0]),
                            Arrays.copyOfRange(alerts, 1, alerts.length));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "main",
            CHROME = "inner")
    @NotYetImplemented(CHROME)
    public void javascriptTargetTop() throws Exception {
        javascriptTarget("target='_top'", 0, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "main",
            CHROME = "inner")
    @NotYetImplemented(CHROME)
    public void javascriptTargetParent() throws Exception {
        javascriptTarget("target='_parent'", 0, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = { "0", "inner" })
    @NotYetImplemented(CHROME)
    public void javascriptTargetUnknown() throws Exception {
        final String[] alerts = getExpectedAlerts();
        javascriptTarget("target='unknown'",
                            Integer.parseInt(alerts[0]),
                            Arrays.copyOfRange(alerts, 1, alerts.length));
    }

    private void javascriptTarget(final String target, final int newWindows,
                    final String[] expectedAlerts) throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>main</title></head>\n"
            + "<body>\n"
            + "  <iframe id='testFrame' src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String secondHtml
            = "<html>\n"
            + "<head><title>inner</title></head>\n"
            + "<body>\n"
            + "  <a id='tester' " + target
                + " href='javascript: try { alert(document.title); } catch(e) { alert(e); }'>no href</a>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        setExpectedAlerts(expectedAlerts);
        final WebDriver driver = loadPage2(html);

        driver.switchTo().frame("testFrame");
        assertEquals(1, driver.getWindowHandles().size());
        driver.findElement(By.id("tester")).click();
        assertEquals(1 + newWindows, driver.getWindowHandles().size());

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Regression test for https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1689798&group_id=47038.
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
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "§§URL§§second/", "HTMLAnchorElement", "[object HTMLAnchorElement]" },
            IE8 = { "§§URL§§second/", "object", "exception" },
            CHROME = { "§§URL§§second/", "object", "function HTMLAnchorElement() { [native code] }" },
            FF = { "§§URL§§second/", "object", "function HTMLAnchorElement() {\n    [native code]\n}" })
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
    @Alerts({ "", "", "text/html", "TExT/hTMl", " text/html ", "application/pdf", "unknown" })
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
    @Alerts({ "text/html", "", " TExT/hTMl  ", "unknown", "application/pdf" })
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
    @Alerts(DEFAULT = { "http:||||||/", "https:||||||/", "mailto:||||||", "tel:||||||",
                        "foo:||||||", "p:||||||", "p:||||||", "p:||||||" },
            IE8 = { "http:||||||", "https:||||||", "mailto:||||||foo@foo.com", "tel:||||||123456",
                        "foo:||||||blabla", "file:||||||p://", "file:||||||p:/", "file:||||||p:/TeMp" },
            IE11 = { "http:||||||/", "https:||||||/", "mailto:||||||foo@foo.com", "tel:||||||123456",
                        "foo:||||||blabla", "file:||||||/p://", "file:||||||/p:/", "file:||||||/p:/TeMp" },
            CHROME = { ":||||||", ":||||||", "mailto:||||||foo@foo.com", "tel:||||||123456",
                        "foo:||||||blabla", "file:||||||/P://", "file:||||||/P:/", "file:||||||/P:/TeMp" })
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
            + "  for (var i=0; i<links.length; ++i) {\n"
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
    @Alerts(", hi")
    public void charset() throws Exception {
        attribute("charset", "hi");
    }

    private void attribute(final String attribute, final String value) throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "         var a = document.createElement('a');\n"
            + "         alert(a." + attribute + ");\n"
            + "         a." + attribute + " = '" + value + "';\n"
            + "         alert(a." + attribute + ");\n"
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
    @Alerts(", 0,0")
    public void coords() throws Exception {
        attribute("coords", "0,0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(", en")
    public void hreflang() throws Exception {
        attribute("hreflang", "en");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(", ")
    @NotYetImplemented
    public void origin() throws Exception {
        attribute("origin", "something");
    }
}
