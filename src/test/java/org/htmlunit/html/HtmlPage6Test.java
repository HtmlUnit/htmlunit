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
package org.htmlunit.html;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link HtmlPage}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Joerg Werner
 */
public class HtmlPage6Test extends WebDriverTestCase {

    /**
     * Test auto-refresh from a meta tag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_DefaultRefreshHandler() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;URL=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; URL=§§URL§§\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_caseSensitivity() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; Url=§§URL§§\">");
    }

    /**
     * Regression test for bug #954.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_spaceSeparator() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0 Url=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0\nUrl=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0\tUrl=§§URL§§\">");
    }

    /**
     * Regression test for bug #954.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_commaSeparator() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0,Url=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0, Url=§§URL§§\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "1"})
    public void refresh_MetaTag_noSeparator() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0Url=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0abc=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0abc§§URL§§\">");
    }

    /**
     * Test for bug #1002.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_no_url_label() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; §§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0 §§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0\n§§URL§§\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"wrong", "2", "§§URL§§abcd=§§URL§§second/"})
    @HtmlUnitNYI(CHROME = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"},
            EDGE = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"},
            FF = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"},
            FF_ESR = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"})
    public void refresh_MetaTag_wrong_url_label() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;abcd=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0; abcd=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0 abcd=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0\nabcd=§§URL§§\">");
    }

    /**
     * Test auto-refresh from a meta tag with no URL.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "3", "§§URL§§"})
    public void refresh_MetaTag_NoUrl() throws Exception {
        // currently an endless loop
        // todo testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"1\">");
        // todo testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"1;\">");
        // todo testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"1 \">");
    }

    /**
     * Test auto-refresh from a meta tag with URL quoted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_Quoted() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT='0;URL=\"§§URL§§\"'>");
    }

    /**
     * Test auto-refresh from a meta tag with URL partly quoted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_PartlyQuoted() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT=\"0;URL='§§URL§§\">");
    }

    /**
     * Test that whitespace before and after ';' is permitted.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_Whitespace() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT='0  ;  URL=§§URL§§'>");
    }

    /**
     * Test that the refresh time can be a double ("3.4", for example), not just an integer.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_MetaTag_Double() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT='1.2  ;  URL=§§URL§§'>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"wrong", "2", "§§URL§§2;URL=§§URL§§second/"})
    @HtmlUnitNYI(CHROME = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"},
            EDGE = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"},
            FF = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"},
            FF_ESR = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"})
    public void refresh_MetaTag_DoubleComma() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT='1,2;URL=§§URL§§'>");
    }

    private void testRefresh_MetaTag(final String metaTag) throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html><head><title>first§</title>\n"
            + metaTag.replace("§§URL§§", URL_SECOND.toString()) + "\n"
            + "</head><body></body></html>";

        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>second§</title></head><body></body></html>";
        final String wrongContent = DOCTYPE_HTML
                + "<html><head><title>wrong§</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setDefaultResponse(wrongContent);

        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();
        for (int i = 0; i < expectedAlerts.length; i++) {
            expectedAlerts[i] = expectedAlerts[i]
                                .replaceAll("§§URL1§§", URL_FIRST.toExternalForm().replace("http://", "http:/"));
        }

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = loadPage2(firstContent);
        if (useRealBrowser()) {
            Thread.sleep(DEFAULT_WAIT_TIME.multipliedBy(3).toMillis());
        }
        verifyTitle2(DEFAULT_WAIT_TIME, driver, expectedAlerts[0]);
        assertEquals(Integer.parseInt(expectedAlerts[1]), getMockWebConnection().getRequestCount() - count);
        if (expectedAlerts.length > 2) {
            assertEquals(expectedAlerts[2], getMockWebConnection().getLastWebRequest().getUrl().toString());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_DefaultRefreshHandler() throws Exception {
        testRefresh_HttpResponseHeader("0;URL=§§URL§§");
        testRefresh_HttpResponseHeader("0; URL=§§URL§§");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_caseSensitivity() throws Exception {
        testRefresh_HttpResponseHeader("0; Url=§§URL§§");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_spaceSeparator() throws Exception {
        testRefresh_HttpResponseHeader("0 Url=§§URL§§");
        testRefresh_HttpResponseHeader("0\nUrl=§§URL§§");
        testRefresh_HttpResponseHeader("0\tUrl=§§URL§§");
    }

    /**
     * Regression test for bug #954.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_commaSeparator() throws Exception {
        testRefresh_HttpResponseHeader("0,Url=§§URL§§");
        testRefresh_HttpResponseHeader("0, Url=§§URL§§");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "1"})
    public void refresh_HttpResponseHeader_noSeparator() throws Exception {
        testRefresh_HttpResponseHeader("0Url=§§URL§§");
        testRefresh_HttpResponseHeader("0§§URL§§");
        testRefresh_HttpResponseHeader("0abc=§§URL§§");
        testRefresh_HttpResponseHeader("0abc§§URL§§");
    }

    /**
     * Test for bug #1002.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_no_url_label() throws Exception {
        testRefresh_HttpResponseHeader("0;§§URL§§");
        testRefresh_HttpResponseHeader("0; §§URL§§");
        testRefresh_HttpResponseHeader("0 §§URL§§");
        testRefresh_HttpResponseHeader("0\n§§URL§§");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"wrong", "2", "§§URL§§abcd=§§URL§§second/"})
    @HtmlUnitNYI(CHROME = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"},
            EDGE = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"},
            FF = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"},
            FF_ESR = {"wrong", "2", "§§URL§§abcd=§§URL1§§second/"})
    public void refresh_HttpResponseHeader_wrong_url_label() throws Exception {
        testRefresh_HttpResponseHeader("0;abcd=§§URL§§");
        testRefresh_HttpResponseHeader("0; abcd=§§URL§§");
        testRefresh_HttpResponseHeader("0 abcd=§§URL§§");
        testRefresh_HttpResponseHeader("0\nabcd=§§URL§§");
    }

    /**
     * Test auto-refresh from a meta tag with no URL.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"first", "3", "§§URL§§"})
    public void refresh_HttpResponseHeader_NoUrl() throws Exception {
        // currently an endless loop
        // todo testRefresh_HttpResponseHeader("1");
        // todo testRefresh_HttpResponseHeader("1;");
        // todo testRefresh_HttpResponseHeader("1 ");
    }

    /**
     * Test auto-refresh from a meta tag with URL quoted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_Quoted() throws Exception {
        testRefresh_HttpResponseHeader("0;URL=\"§§URL§§\"");
    }

    /**
     * Test auto-refresh from a meta tag with URL partly quoted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_PartlyQuoted() throws Exception {
        testRefresh_HttpResponseHeader("0;URL='§§URL§§");
    }

    /**
     * Test that whitespace before and after ';' is permitted.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_Whitespace() throws Exception {
        testRefresh_HttpResponseHeader("0  ;  URL=§§URL§§");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"second", "2", "§§URL§§second/"})
    public void refresh_HttpResponseHeader_Double() throws Exception {
        testRefresh_HttpResponseHeader("1.2  ;  URL=§§URL§§");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"wrong", "2", "§§URL§§2;URL=§§URL§§second/"})
    @HtmlUnitNYI(CHROME = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"},
            EDGE = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"},
            FF = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"},
            FF_ESR = {"wrong", "2", "§§URL§§2;URL=§§URL1§§second/"})
    public void refresh_HttpResponseHeader_DoubleComma() throws Exception {
        testRefresh_HttpResponseHeader("1,2;URL=§§URL§§");
    }

    private void testRefresh_HttpResponseHeader(final String refreshHeader) throws Exception {
        final String firstContent = DOCTYPE_HTML
                + "<html><head><title>first§</title>\n"
                + "</head><body></body></html>";
        final String secondContent = DOCTYPE_HTML
                + "<html><head><title>second§</title></head><body></body></html>";
        final String wrongContent = DOCTYPE_HTML
                + "<html><head><title>wrong§</title></head><body></body></html>";

        final String header = refreshHeader.replace("§§URL§§", URL_SECOND.toString());
        getMockWebConnection().setResponse(URL_FIRST, firstContent, 200, "OK", MimeType.TEXT_HTML, Collections
                .singletonList(new NameValuePair("Refresh", header)));
        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setDefaultResponse(wrongContent);

        expandExpectedAlertsVariables(URL_FIRST);
        final String[] expectedAlerts = getExpectedAlerts();
        for (int i = 0; i < expectedAlerts.length; i++) {
            expectedAlerts[i] = expectedAlerts[i]
                                .replaceAll("§§URL1§§", URL_FIRST.toExternalForm().replace("http://", "http:/"));
        }

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = loadPage2(URL_FIRST, StandardCharsets.ISO_8859_1);
        if (useRealBrowser()) {
            Thread.sleep(DEFAULT_WAIT_TIME.multipliedBy(3).toMillis());
        }
        verifyTitle2(DEFAULT_WAIT_TIME, driver, expectedAlerts[0]);
        assertEquals(Integer.parseInt(expectedAlerts[1]), getMockWebConnection().getRequestCount() - count);
        if (expectedAlerts.length > 2) {
            assertEquals(expectedAlerts[2], getMockWebConnection().getLastWebRequest().getUrl().toString());
        }
    }
}
