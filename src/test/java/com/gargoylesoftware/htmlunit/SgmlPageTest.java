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
package com.gargoylesoftware.htmlunit;

import static org.apache.http.client.utils.DateUtils.formatDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.Html;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link SgmlPage}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public final class SgmlPageTest extends WebServerTestCase {

    /**
     * Do not cleanup WebResponse between pages if it is cached.
     * @throws Exception if the test fails
     */
    @Test
    public void onlyCacheToCleanUpWebResponse() throws Exception {
        try (WebClient webClient = getWebClient()) {
            webClient.getOptions().setMaxInMemory(3);

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Expires", formatDate(DateUtils.addHours(new Date(), 1))));
            getMockWebConnection().setDefaultResponse("something", 200, "Ok", MimeType.TEXT_HTML, headers);
            startWebServer(getMockWebConnection());

            webClient.getPage(URL_FIRST);
            webClient.getPage(URL_FIRST);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagNameAsterisk() throws Exception {
        final String html
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final DomNodeList<DomElement> elements = page.getElementsByTagName("*");

        assertEquals(9, elements.getLength());
        validateDomNodeList(elements);

        final HtmlDivision newDiv = new HtmlDivision(HtmlDivision.TAG_NAME, page, null);
        page.getBody().appendChild(newDiv);
        assertEquals(10, elements.getLength());
        validateDomNodeList(elements);
    }

    private <E extends DomNode> void validateDomNodeList(final DomNodeList<E> nodes) {
        assertEquals(nodes.getLength(), nodes.size());
        final Iterator<E> nodesIterator = nodes.iterator();
        for (int i = 0; i < nodes.getLength(); i++) {
            assertEquals(nodes.item(i), nodes.get(i));
            assertEquals(nodes.item(i), nodesIterator.next());
            assertEquals(i, nodes.indexOf(nodes.item(i)));
        }
        assertEquals(false, nodesIterator.hasNext());
        final ListIterator<E> nodesListIterator = nodes.listIterator();
        assertEquals(nodes.item(0), nodesListIterator.next());
        assertEquals(nodes.item(1), nodesListIterator.next());
        assertEquals(nodes.item(1), nodesListIterator.previous());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagNameNSAsterisk() throws Exception {
        final String html
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final DomNodeList<DomElement> elements = page.getElementsByTagNameNS(Html.XHTML_NAMESPACE, "*");

        assertEquals(9, elements.getLength());
        validateDomNodeList(elements);

        final HtmlDivision newDiv = new HtmlDivision(HtmlDivision.TAG_NAME, page, null);
        page.getBody().appendChild(newDiv);
        assertEquals(10, elements.getLength());
        validateDomNodeList(elements);
    }

}
