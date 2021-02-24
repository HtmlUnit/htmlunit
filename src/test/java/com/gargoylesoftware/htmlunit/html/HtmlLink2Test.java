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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlLink}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlLink2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLLinkElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<link id='myId' href='file1.css'>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlLink.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleText() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head>\n"
            + "  <link id='tester' href='file1.css'>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().getVisibleText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <link id='l' href='file1.css'>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final boolean displayed = driver.findElement(By.id("l")).isDisplayed();
        assertFalse(displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoad() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        onLoadOnError("rel='stylesheet' href='simple.css'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoadRelCase() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        onLoadOnError("rel='sTYLeSheet' href='simple.css'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoadMediaScreen() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        onLoadOnError("rel='stylesheet' href='simple.css' media='screen'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoadMediaPrint() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        onLoadOnError("rel='stylesheet' href='simple.css' media='print'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoadMediaQueryMatch() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        onLoadOnError("rel='stylesheet' href='simple.css' media='(min-width: 100px)'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoadMediaQueryNotMatch() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        onLoadOnError("rel='stylesheet' href='simple.css' media='(max-width: 10px)'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"onLoad", "body onLoad"})
    public void onLoadRelWhitespace() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        onLoadOnError("rel='\t stylesheet     ' href='simple.css'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"onError", "body onLoad"},
            IE = {"onLoad", "body onLoad"})
    @NotYetImplemented(IE)
    public void onError() throws Exception {
        onLoadOnError("rel='stylesheet' href='unknown.css'");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("body onLoad")
    public void onLoadOnErrorWithoutRel() throws Exception {
        onLoadOnError("href='unknown.css'");
    }

    private void onLoadOnError(final String attribs) throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <link " + attribs
                        + " onload='alert(\"onLoad\")' onerror='alert(\"onError\")'>\n"
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
    @Alerts(DEFAULT = {"onLoad1", "onLoadJs1", "onLoad2", "body onLoad;"},
            IE = {"onLoadJs1", "body onLoad", "onLoad1", "onLoad2;"})
    @NotYetImplemented(IE)
    public void onLoadOrder() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple1.css"), "");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple2.css"), "");
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple1.js"), "var x=1;");

        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function log(x) {\n"
                + "      document.title += x + ';';\n"
                + "    }\n"
                + "  </script>\n"
                + "  <link rel='stylesheet' href='simple1.css' onload='log(\"onLoad1\")'>\n"
                + "  <script type='text/javascript' src='simple1.js' onload='log(\"onLoadJs1\")'></script>\n"
                + "  <link rel='stylesheet' href='simple2.css' onload='log(\"onLoad2\")'>\n"
                + "</head>\n"
                + "<body onload='log(\"body onLoad\")'>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, String.join(";", getExpectedAlerts()));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("onLoad [object Event]")
    public void onLoadDynamic() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var dynLink = document.createElement('link');\n"
                + "      dynLink.rel = 'stylesheet';\n"
                + "      dynLink.type = 'text/css';\n"
                + "      dynLink.href = 'simple.css';"
                + "      dynLink.onload = function (e) { log(\"onLoad \" + e) };\n"
                + "      dynLink.onerror = function (e) { log(\"onError \" + e) };\n"
                + "      document.head.appendChild(dynLink);\n"
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
    @Alerts(DEFAULT = "onError [object Event]",
            IE = "onLoad [object Event]")
    @NotYetImplemented(IE)
    public void onLoadDynamicUnknown() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"), "");
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var dynLink = document.createElement('link');\n"
                + "      dynLink.rel = 'stylesheet';\n"
                + "      dynLink.type = 'text/css';\n"
                + "      dynLink.href = 'unknown.css';"
                + "      dynLink.onload = function (e) { log(\"onLoad \" + e) };\n"
                + "      dynLink.onerror = function (e) { log(\"onError \" + e) };\n"
                + "      document.head.appendChild(dynLink);\n"
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
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);
        final String text = driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
        assertEquals(String.join("\n", getExpectedAlerts()), text);
    }

    /**
     * Test for issue #2011.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§sample?string=a&int=1", "/sample?string=a&int=1"})
    public void testEntityRefWithoutSemicolon() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <a id='target' href='/sample?string=a&int=1'>/sample?string=a&int=1</a>\n"
            + "</body>\n"
            + "</html>";

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);

        final WebElement element = driver.findElement(By.id("target"));

        assertEquals(getExpectedAlerts()[0], element.getAttribute("href"));
        assertEquals(getExpectedAlerts()[1], element.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§sample?string=a&iexcl=1", "/sample?string=a\u00A1=1"})
    public void testEntityRefWithoutSemicolonReplaceInAttrib() throws Exception {
        final String html
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <a id='target' href='/sample?string=a&iexcl=1'>/sample?string=a&iexcl=1</a>\n"
            + "</body>\n"
            + "</html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);

        final WebElement element = driver.findElement(By.id("target"));

        assertEquals(getExpectedAlerts()[0], element.getAttribute("href"));
        assertEquals(getExpectedAlerts()[1], element.getText());
    }

    /**
     * This is a WebDriver test because we need an OnFile
     * cache entry.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testReloadAfterRemovalFromCache() throws Exception {
        final WebDriver driver = getWebDriver();

        int maxInMemory = 0;
        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = getWebWindowOf((HtmlUnitDriver) driver).getWebClient();
            maxInMemory = webClient.getOptions().getMaxInMemory();
        }

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Expires", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date(
                System.currentTimeMillis()
                    + (12 * DateUtils.MILLIS_PER_MINUTE)))));
        final String bigContent = ".someRed { color: red; }" + StringUtils.repeat(' ', maxInMemory);

        getMockWebConnection().setResponse(new URL(URL_FIRST, "simple.css"),
                bigContent,  200, "OK", MimeType.TEXT_CSS, headers);

        final String html = "<html><head>\n"
            + "<link id='myId' href='simple.css'></link>\n"
            + "</head><body>\n"
            + "</body></html>";

        loadPage2(html);

        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = getWebWindowOf((HtmlUnitDriver) driver).getWebClient();

            final HtmlPage page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
            final HtmlLink link = page.getFirstByXPath("//link");

            assertTrue(webClient.getCache().getSize() == 0);
            WebResponse respCss = link.getWebResponse(true);
            assertEquals(bigContent, respCss.getContentAsString());

            assertTrue(webClient.getCache().getSize() > 0);

            webClient.getCache().clear();

            respCss = link.getWebResponse(true);
            // assertTrue(getWebClient().getCache().getSize() > 1);
            assertEquals(bigContent, respCss.getContentAsString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void polymerImportCheck() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert('import' in document.createElement('link'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§index.html?test")
    public void getResponse_referer() throws Exception {
        final String html = "<html><head>\n"
            + "<link rel='stylesheet' href='" + URL_SECOND + "'></link>\n"
            + "</head><body>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);

        final URL indexUrl = new URL(URL_FIRST.toString() + "index.html");

        getMockWebConnection().setResponse(indexUrl, html);
        getMockWebConnection().setResponse(URL_SECOND, "");

        loadPage2(html, new URL(URL_FIRST.toString() + "index.html?test#ref"));

        assertEquals(2, getMockWebConnection().getRequestCount());

        final Map<String, String> lastAdditionalHeaders = getMockWebConnection().getLastAdditionalHeaders();
        assertEquals(getExpectedAlerts()[0], lastAdditionalHeaders.get(HttpHeader.REFERER));
    }
}
