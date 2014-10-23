/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
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
    @Alerts({ "", "", "",  "§§URL§§test.css", "stylesheet", "stylesheet1" })
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
    @Alerts(DEFAULT = { "attachEvent not available", "href" }, IE8 = "onclick")
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
    @Alerts("Second") // in fact not alerts here, but it makes config easier
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
    @Alerts({ "§§URL§§testsite1.html", "testsite1.html", "§§URL§§testsite2.html", "testsite2.html",
        "13", "testanchor", "mailto:" })
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
    public void testHrefTrimmed() throws Exception {
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
        javascriptTarget("", 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inner")
    public void javascriptTargetEmpty() throws Exception {
        javascriptTarget("target=''", 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts()
    public void javascriptTargetWhitespace() throws Exception {
        javascriptTarget("target='  '", 1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inner")
    public void javascriptTargetSelf() throws Exception {
        javascriptTarget("target='_self'", 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts()
    public void javascriptTargetBlank() throws Exception {
        javascriptTarget("target='_blank'", 1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("main")
    public void javascriptTargetTop() throws Exception {
        javascriptTarget("target='_top'", 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("main")
    public void javascriptTargetParent() throws Exception {
        javascriptTarget("target='_parent'", 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts()
    public void javascriptTargetUnknown() throws Exception {
        javascriptTarget("target='unknown'", 1);
    }

    private void javascriptTarget(final String target, final int newWindows) throws Exception {
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

        System.out.println(html);
        final WebDriver driver = loadPage2(html);
        System.out.println(driver.getPageSource());
        driver.findElement(By.tagName("a")).click();
        assertEquals(1, getMockWebConnection().getRequestCount());
    }
}
