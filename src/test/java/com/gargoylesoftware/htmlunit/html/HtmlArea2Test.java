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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlArea}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlArea2Test extends SimpleWebTestCase {

    private WebClient createWebClient(final String onClick) throws IOException {
        final MockWebConnection webConnection = new MockWebConnection();

        final URL urlImage = new URL(URL_FIRST, "img.jpg");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            webConnection.setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String firstContent
            = "<html><head><title>first</title></head><body>\n"
            + "<img src='" + urlImage + "' width='145' height='126' usemap='#planetmap'>\n"
            + "<map id='planetmap' name='planetmap'>\n"
            + "<area shape='rect' onClick=\"" + onClick + "\" coords='0,0,82,126' id='second' "
            + "href='" + URL_SECOND + "'>\n"
            + "<area shape='circle' coords='90,58,3' id='third' href='" + URL_THIRD + "'>\n"
            + "</map></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";
        final String thirdContent = "<html><head><title>third</title></head><body></body></html>";

        final WebClient client = getWebClient();

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webConnection.setResponse(URL_THIRD, thirdContent);

        client.setWebConnection(webConnection);

        return client;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click() throws Exception {
        final WebClient client = createWebClient("");

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlArea area = page.getHtmlElementById("third");

        // Test that the correct value is being passed back up to the server
        final HtmlPage thirdPage = area.click();
        assertEquals("third", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_onclickReturnsFalse() throws Exception {
        final WebClient client = createWebClient("alert('foo');return false;");
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlArea area = page.getHtmlElementById("second");

        final HtmlPage thirdPage = area.click();
        assertEquals(new String[] {"foo"}, collectedAlerts);
        assertEquals("first", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_onclickReturnsTrue() throws Exception {
        final WebClient client = createWebClient("alert('foo');return true;");
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlArea area = page.getHtmlElementById("second");

        final HtmlPage thirdPage = area.click();
        assertEquals(new String[] {"foo"}, collectedAlerts);
        assertEquals("second", thirdPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click_javascriptUrl_javascriptDisabled() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body><map>\n"
            + "<area href='javascript:alert(\"clicked\")' id='a2' coords='0,0,10,10'/>\n"
            + "</map></body></html>";
        final WebClient client = getWebClient();
        client.getOptions().setJavaScriptEnabled(false);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlArea area = page.getHtmlElementById("a2");

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        final HtmlPage secondPage = area.click();

        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        assertSame(page, secondPage);
    }

}
