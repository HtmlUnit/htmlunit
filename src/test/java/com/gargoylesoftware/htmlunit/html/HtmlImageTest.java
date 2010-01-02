/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlImageTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testIsMapClick() throws Exception {
        testIsMapClick("img1", false, "?0,0", "?25,30");
        testIsMapClick("img2", false, "", "");
        testIsMapClick("img3", true, "", "");
        testIsMapClick("img3", true, "", "");
    }

    private void testIsMapClick(final String imgId, final boolean samePage,
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
        Assert.assertEquals("same page after click", samePage, (page == page2));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClick, page2.getWebResponse().getRequestSettings().getUrl());
        }

        final Page page3 = img.click(25, 30);
        Assert.assertEquals("same page after click(25, 30)", samePage, (page == page3));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClickXY, page3.getWebResponse().getRequestSettings().getUrl());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testUseMapClick() throws Exception {
        testUseMapClick(0, 0, "/");
        testUseMapClick(10, 10, "a.html");
        testUseMapClick(20, 10, "a.html");
        testUseMapClick(29, 10, "b.html");
        testUseMapClick(50, 50, "/");
    }

    /**
     * @throws Exception if the test fails
     */
    private void testUseMapClick(final int x, final int y, final String urlSuffix) throws Exception {
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
        final URL url = page2.getWebResponse().getRequestSettings().getUrl();
        assertTrue(url.toExternalForm(), url.toExternalForm().endsWith(urlSuffix));
    }

    /**
     * Tests circle radius of percentage value.
     * @throws Exception if the test fails
     */
    @Test
    public void testUseMapClick_CircleRadiusPercentage() throws Exception {
        if (notYetImplemented()) {
            return;
        }

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
}
