/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
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
        
        final HtmlImage img = (HtmlImage) page.getHtmlElementById(imgId);
        
        final Page page2 = img.click();
        Assert.assertEquals("same page after click", samePage, (page == page2));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClick, page2.getWebResponse().getUrl());
        }

        final Page page3 = img.click(25, 30);
        Assert.assertEquals("same page after click(25, 30)", samePage, (page == page3));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClickXY, page3.getWebResponse().getUrl());
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
        
        final HtmlImage img = (HtmlImage) page.getHtmlElementById("myImg");
        
        final Page page2 = img.click(x, y);
        final URL url = page2.getWebResponse().getUrl();
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
        
        final HtmlImage img = (HtmlImage) page.getHtmlElementById("myImg");
        
        img.click(0, 0);
    }

    /**
     * @throws Exception if the test fails.
     */
    @Test
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLImageElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertTrue(HtmlImage.class.isInstance(page.getHtmlElementById("myId")));
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
