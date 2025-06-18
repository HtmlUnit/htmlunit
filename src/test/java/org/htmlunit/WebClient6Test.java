/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link WebClient} using WebDriverTestCase.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class WebClient6Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirect301() throws Exception {
        redirectGet(301, HttpMethod.GET, "/page2.html");
        redirectPost(301, HttpMethod.GET, "/page2.html", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirectAbsolute301() throws Exception {
        redirectGet(301, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm());
        redirectPost(301, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm(), false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo"})
    public void redirect301WithQuery() throws Exception {
        redirectGet(301, HttpMethod.GET, "/page2.html?test=foo");
        redirectPost(301, HttpMethod.GET, "/page2.html?test=foo", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html#hash"})
    public void redirect301WithHash() throws Exception {
        redirectGet(301, HttpMethod.GET, "/page2.html#hash");
        redirectPost(301, HttpMethod.GET, "/page2.html#hash", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo#hash"})
    public void redirect301WithQueryAndHash() throws Exception {
        redirectGet(301, HttpMethod.GET, "/page2.html?test=foo#hash");
        redirectPost(301, HttpMethod.GET, "/page2.html?test=foo#hash", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?ignorefrom=pwr&", "3",
        "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4"})
    @HtmlUnitNYI(CHROME = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                           "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                                   + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            EDGE = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                    "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                            + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            FF = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                  "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                          + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            FF_ESR = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                      "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                              + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"})
    public void redirect301WithQueryAndHashSpecialChars() throws Exception {
        redirectGet(301, HttpMethod.GET,
                "/page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4");
        redirectPost(301, HttpMethod.GET,
                "/page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4",
                true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo#hash"})
    public void redirectAbsolute301WithQueryAndHash() throws Exception {
        redirectGet(301, HttpMethod.GET, new URL(URL_FIRST,
                "/page2.html?test=foo#hash").toExternalForm());
        redirectPost(301, HttpMethod.GET, new URL(URL_FIRST,
                "/page2.html?test=foo#hash").toExternalForm(), true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?ignorefrom=pwr&", "3",
             "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;"
                     + "ignoresearch_submit=Get%20Resumes&#x26;mne=4"})
    @HtmlUnitNYI(CHROME = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                           "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                           + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            EDGE = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                    "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                    + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            FF = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                  "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                          + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            FF_ESR = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                      "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                      + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"})
    public void redirectAbsolute301WithQueryAndHashSpecialChars() throws Exception {
        redirectGet(301, HttpMethod.GET, new URL(URL_FIRST,
            "/page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4")
                .toExternalForm());
        redirectPost(301, HttpMethod.GET, new URL(URL_FIRST,
            "/page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4")
                    .toExternalForm(), true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirect302() throws Exception {
        redirectGet(302, HttpMethod.GET, "/page2.html");
        redirectPost(302, HttpMethod.GET, "/page2.html", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirectAbsolute302() throws Exception {
        redirectGet(302, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm());
        redirectPost(302, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm(), false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo"})
    public void redirect302WithQuery() throws Exception {
        redirectGet(302, HttpMethod.GET, "/page2.html?test=foo");
        redirectPost(302, HttpMethod.GET, "/page2.html?test=foo", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html#hash"})
    public void redirect302WithHash() throws Exception {
        redirectGet(302, HttpMethod.GET, "/page2.html#hash");
        redirectPost(302, HttpMethod.GET, "/page2.html#hash", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo#hash"})
    public void redirect302WithQueryAndHash() throws Exception {
        redirectGet(302, HttpMethod.GET, "/page2.html?test=foo#hash");
        redirectPost(302, HttpMethod.GET, "/page2.html?test=foo#hash", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirect303() throws Exception {
        redirectGet(303, HttpMethod.GET, "/page2.html");
        redirectPost(303, HttpMethod.GET, "/page2.html", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirectAbsolute303() throws Exception {
        redirectGet(303, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm());
        redirectPost(303, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm(), false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo"})
    public void redirect303WithQuery() throws Exception {
        redirectGet(303, HttpMethod.GET, "/page2.html?test=foo");
        redirectPost(303, HttpMethod.GET, "/page2.html?test=foo", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html#hash"})
    public void redirect303WithHash() throws Exception {
        redirectGet(303, HttpMethod.GET, "/page2.html#hash");
        redirectPost(303, HttpMethod.GET, "/page2.html#hash", false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo#hash"})
    public void redirect303WithQueryAndHash() throws Exception {
        redirectGet(303, HttpMethod.GET, "/page2.html?test=foo#hash");
        redirectPost(303, HttpMethod.GET, "/page2.html?test=foo#hash", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirect307() throws Exception {
        redirectGet(307, HttpMethod.GET, "/page2.html");
        redirectPost(307, HttpMethod.POST, "/page2.html", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo"})
    public void redirect307WithQuery() throws Exception {
        redirectGet(307, HttpMethod.GET, "/page2.html?test=foo");
        redirectPost(307, HttpMethod.POST, "/page2.html?test=foo", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirectAbsolute307() throws Exception {
        redirectGet(307, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm());
        redirectPost(307, HttpMethod.POST, new URL(URL_FIRST, "/page2.html").toExternalForm(), true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html#hash"})
    public void redirect307WithHash() throws Exception {
        redirectGet(307, HttpMethod.GET, "/page2.html#hash");
        redirectPost(307, HttpMethod.POST, "/page2.html#hash", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo#hash"})
    public void redirect307WithQueryAndHash() throws Exception {
        redirectGet(307, HttpMethod.GET, "/page2.html?test=foo#hash");
        redirectPost(307, HttpMethod.POST, "/page2.html?test=foo#hash", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirect308() throws Exception {
        redirectGet(308, HttpMethod.GET, "/page2.html");
        redirectPost(308, HttpMethod.POST, "/page2.html", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo"})
    public void redirect308WithQuery() throws Exception {
        redirectGet(308, HttpMethod.GET, "/page2.html?test=foo");
        redirectPost(308, HttpMethod.POST, "/page2.html?test=foo", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html"})
    public void redirectAbsolute308() throws Exception {
        redirectGet(308, HttpMethod.GET, new URL(URL_FIRST, "/page2.html").toExternalForm());
        redirectPost(308, HttpMethod.POST, new URL(URL_FIRST, "/page2.html").toExternalForm(), true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html", "3", "§§URL§§page2.html#hash"})
    public void redirect308WithHash() throws Exception {
        redirectGet(308, HttpMethod.GET, "/page2.html#hash");
        redirectPost(308, HttpMethod.POST, "/page2.html#hash", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?test=foo", "3", "§§URL§§page2.html?test=foo#hash"})
    public void redirect308WithQueryAndHash() throws Exception {
        redirectGet(308, HttpMethod.GET, "/page2.html?test=foo#hash");
        redirectPost(308, HttpMethod.POST, "/page2.html?test=foo#hash", true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"§§URL§§page2.html?ignorefrom=pwr&", "3",
        "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4"})
    @HtmlUnitNYI(CHROME = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                           "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                                   + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            EDGE = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                    "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                            + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            FF = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                  "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                          + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"},
            FF_ESR = {"§§URL§§page2.html?ignorefrom=pwr&", "3",
                      "§§URL§§page2.html?ignorefrom=pwr&#x26;ignorenai=1&"
                              + "%23x26;ignoresearch_submit=Get%20Resumes&%23x26;mne=4"})
    public void redirect302WithQueryAndHashSpecialChars() throws Exception {
        redirectGet(302, HttpMethod.GET,
                "/page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4");
        redirectPost(302, HttpMethod.GET,
                "/page2.html?ignorefrom=pwr&#x26;ignorenai=1&#x26;ignoresearch_submit=Get%20Resumes&#x26;mne=4",
                true);
    }

    private void redirectGet(final int code, final HttpMethod httpMethod, final String redirectUrl) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><a href='redirect.html'>redirect</a></body></html>";
        final int reqCount = getMockWebConnection().getRequestCount();

        final URL url = new URL(URL_FIRST, "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", redirectUrl));
        getMockWebConnection().setDefaultResponse("", code, "Found", null, headers);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        assertEquals(getExpectedAlerts()[0], getMockWebConnection().getLastWebRequest().getUrl().toString());
        assertEquals(reqCount + Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount());
        assertEquals(httpMethod, getMockWebConnection().getLastWebRequest().getHttpMethod());
        assertEquals(getExpectedAlerts()[2], driver.getCurrentUrl());
    }

    private void redirectPost(final int code, final HttpMethod httpMethod,
            final String redirectUrl, final boolean resendParams) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><form action='redirect.html' method='POST'>\n"
                + "  <input type='hidden' name='param1' value='paramValue'>\n"
                + "  <input type='submit' id='postBtn' value='Submit'>\n"
                + "</form></body></html>";
        final int reqCount = getMockWebConnection().getRequestCount();

        final URL url = new URL(URL_FIRST, "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", redirectUrl));
        getMockWebConnection().setDefaultResponse("", code, "Found", null, headers);

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("postBtn")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        assertEquals(reqCount + Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount());
        assertEquals(httpMethod, getMockWebConnection().getLastWebRequest().getHttpMethod());

        if (resendParams) {
            assertTrue(getMockWebConnection().getLastWebRequest().getRequestParameters().size() > 0);

            final NameValuePair param = getMockWebConnection().getLastWebRequest().getRequestParameters().get(0);
            if ("param1".equals(param.getName())) {
                assertEquals("paramValue", param.getValue());
            }
            else if ("test".equals(param.getName())) {
                assertEquals("foo", param.getValue());
            }
            else if (!param.getName().startsWith("ignore")) {
                Assertions.fail("unexpected param '" + param.getName() + "'");
            }
        }
        else {
            assertEquals(0, getMockWebConnection().getLastWebRequest().getRequestParameters().size());
        }

        assertEquals(getExpectedAlerts()[0], getMockWebConnection().getLastWebRequest().getUrl().toString());
        assertEquals(getExpectedAlerts()[2], driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 2822048: a 302 redirect without Location header.
     * @throws Exception if an error occurs
     */
    @Test
    public void redirect302WithoutLocation() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='page2'>to redirect</a></body></html>";
        getMockWebConnection().setDefaultResponse("", 302, "Found", null);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(URL_FIRST + "page2", driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 3017719: a 302 redirect should change the page url.
     * @throws Exception if an error occurs
     */
    @Test
    public void redirect302ChangePageUrl() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='redirect.html'>redirect</a></body></html>";

        final URL url = new URL(URL_FIRST, "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", "/page2.html"));
        getMockWebConnection().setDefaultResponse("", 302, "Found", null, headers);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(url.toString(), driver.getCurrentUrl());
    }

    /**
     * Regression test for bug 3035155.
     * Bug was fixes in HttpClient 4.1.
     * @throws Exception if an error occurs
     */
    @Test
    public void redirect302UrlsInQuery() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='redirect.html'>redirect</a></body></html>";

        final URL url = new URL(URL_FIRST, "page2.html");
        getMockWebConnection().setResponse(url, html);

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", "/page2.html?param=http%3A//somwhere.org"));
        getMockWebConnection().setDefaultResponse("", 302, "Found", null, headers);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("a")).click();
        assertEquals(url + "?param=http%3A//somwhere.org", driver.getCurrentUrl());
    }
}
