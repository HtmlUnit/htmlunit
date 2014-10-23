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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLAnchorElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLAnchorElementTest extends SimpleWebTestCase {
    private static final URL URL_GARGOYLE;

    static {
        try {
            URL_GARGOYLE = new URL("http://www.gargoylesoftware.com/");
        }
        catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onClickAnchorHref() throws Exception {
        final String html
            = "<html><body>\n"
            + "<a href='#' onclick='document.form1.submit()'>link 1</a>\n"
            + "<form name='form1' action='foo.html' method='post'>\n"
            + "<input name='testText'>\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final HtmlPage page1 = loadPageWithAlerts(html);
        final Page page2 = page1.getAnchorByHref("#").click();

        assertEquals(getDefaultUrl() + "foo.html", page2.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWriteAnchorTarget() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].target += 'K';\">\n"
            + "<a href='#' target='O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page1 = loadPage(html);
        final HtmlAnchor link = page1.getAnchors().get(0);
        assertEquals("OK", link.getTargetAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWriteAnchorSearch() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].search += '&p2=2';\">\n"
            + "<a href='foo.html?p1=1' target='O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page1 = loadPage(getBrowserVersion(), html, null, URL_GARGOYLE);
        final HtmlAnchor link = page1.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/foo.html?p1=1&p2=2", link.getHrefAttribute());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void readAnchorHash() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <a id='a' href='http://blah.com/abc.html#arg'>foo</a>\n"
            + "  <script>alert(document.getElementById('a').hash);</script>\n"
            + "</body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(getBrowserVersion(), html, actual);
        final String[] expected = {"#arg"};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWriteAnchorHash() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].hash += 'K';\">\n"
            + "  <a href='foo.html#O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals(getDefaultUrl() + "foo.html#OK", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWriteAnchorPort() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].port += '80';\n"
            + "    document.links[1].port += '80'; \">\n"
            + "  <a href='foo.html#O'>link 1</a>\n"
            + "  <a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null, URL_GARGOYLE);
        HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com:80/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(1);
        assertEquals("http://www.gargoylesoftware.com:8080/foo.html#O", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWritePathname() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].pathname = '/bar' + document.links[0].pathname;\">\n"
            + "  <a href='foo.html#B'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null, URL_GARGOYLE);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.com/bar/foo.html#B", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWriteProtocol() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].protocol = document.links[0].protocol.substring(0,4) + 's:';\">\n"
            + "  <a href='foo.html#B'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null, URL_GARGOYLE);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("https://www.gargoylesoftware.com/foo.html#B", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWriteAnchorHost() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].host += 'motion:8080';\n"
            + "    document.links[1].host += 'motion';\n"
            + "    document.links[2].host += '80';\n"
            + "    document.links[3].host = 'www.gargoylesoftware.com'; \">\n"
            + "  <a href='foo.html#O'>link 0</a>\n"
            + "  <a href='foo.html#O'>link 1</a>\n"
            + "  <a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 2</a>\n"
            + "  <a href='http://www.gargoylesoftware.com:80/foo.html#O'>link 3</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null, URL_GARGOYLE);
        HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.commotion:8080/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(1);
        assertEquals("http://www.gargoylesoftware.commotion/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(2);
        assertEquals("http://www.gargoylesoftware.com:8080/foo.html#O", link.getHrefAttribute());
        link = page.getAnchors().get(3);
        assertEquals("http://www.gargoylesoftware.com/foo.html#O", link.getHrefAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readWriteAnchorHostname() throws Exception {
        final String html
            = "<html>\n"
            + "<body onload=\"document.links[0].hostname += 'motion';\">\n"
            + "  <a href='foo.html#O'>link 1</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null, URL_GARGOYLE);
        final HtmlAnchor link = page.getAnchors().get(0);
        assertEquals("http://www.gargoylesoftware.commotion/foo.html#O", link.getHrefAttribute());
    }
}
