/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlImageTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isMapClick() throws Exception {
        isMapClick("img1", Boolean.FALSE, "?0,0", "?25,30");
        isMapClick("img2", Boolean.FALSE, "", "");
        isMapClick("img3", Boolean.TRUE, "", "");
        isMapClick("img3", Boolean.TRUE, "", "");
    }

    private void isMapClick(final String imgId, final Boolean samePage,
            final String urlSuffixClick, final String urlSuffixClickXY) throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<a href='http://server/foo'>\n"
            + "<img id='img1' src='foo.png' ismap>\n"
            + "<img id='img2' src='foo.png'>\n"
            + "</a>\n"
            + "<img id='img3' src='foo.png' ismap>\n"
            + "<img id='img4' src='foo.png'>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlImage img = page.getHtmlElementById(imgId);

        final Page page2 = img.click();
        Assert.assertEquals("same page after click", samePage, Boolean.valueOf(page == page2));
        if (!samePage.booleanValue()) {
            assertEquals("http://server/foo" + urlSuffixClick, page2.getWebResponse().getWebRequest().getUrl());
        }

        final Page page3 = img.click(25, 30);
        Assert.assertEquals("same page after click(25, 30)", samePage, Boolean.valueOf(page == page3));
        if (!samePage.booleanValue()) {
            assertEquals("http://server/foo" + urlSuffixClickXY, page3.getWebResponse().getWebRequest().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void useMapClick() throws Exception {
        useMapClick(0, 0, "/");
        useMapClick(10, 10, "a.html");
        useMapClick(20, 10, "a.html");
        useMapClick(29, 10, "b.html");
        useMapClick(50, 50, "/");
    }

    /**
     * @throws Exception if the test fails
     */
    private void useMapClick(final int x, final int y, final String urlSuffix) throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<img id='myImg' src='foo.png' usemap='#map1'>\n"
            + "<map name='map1'>\n"
            + "<area href='a.html' shape='rect' coords='5,5,20,20'>\n"
            + "<area href='b.html' shape='circle' coords='25,10,10'>\n"
            + "</map>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlImage img = page.getHtmlElementById("myImg");

        final Page page2 = img.click(x, y);
        final URL url = page2.getWebResponse().getWebRequest().getUrl();
        assertTrue(url.toExternalForm(), url.toExternalForm().endsWith(urlSuffix));
    }

    /**
     * Tests circle radius of percentage value.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void useMapClick_CircleRadiusPercentage() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<img id='myImg' src='foo.png' usemap='#map1'>\n"
            + "<map name='map1'>\n"
            + "<area href='a.html' shape='rect' coords='5,5,20,20'>\n"
            + "<area href='b.html' shape='circle' coords='25,10,10%'>\n"
            + "</map>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final HtmlImage img = page.getHtmlElementById("myImg");
        img.click(0, 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<img id='img1' src='foo.png'>"
            + "<img id='img2' name='testName' src='foo.png' alt='young'>"
            + "<img id='img3' src='foo.png' width='11' height='17px' >"
            + "<img id='img4' src='foo.png' width='11em' height='17%' >"
            + "</body></html>";
        final HtmlPage page = loadPage(content);

        HtmlImage img = page.getHtmlElementById("img1");
        String expected = "<img id=\"img1\" src=\"foo.png\"/>";
        assertEquals(expected, img.asXml().trim());

        img = page.getHtmlElementById("img2");
        expected = "<img id=\"img2\" name=\"testName\" src=\"foo.png\" alt=\"young\"/>";
        assertEquals(expected, img.asXml().trim());

        img = page.getHtmlElementById("img3");
        expected = "<img id=\"img3\" src=\"foo.png\" width=\"11\" height=\"17px\"/>";
        assertEquals(expected, img.asXml().trim());

        img = page.getHtmlElementById("img4");
        expected = "<img id=\"img4\" src=\"foo.png\" width=\"11em\" height=\"17%\"/>";
        assertEquals(expected, img.asXml().trim());
    }
}
