/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision: 1129 $
 * @author Marc Guillemot
 */
public class HtmlImageTest extends WebTestCase {
    /**
     * Create an instance
     *
     * @param name The name of the test
     */
    public HtmlImageTest(final String name) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testIsMapClick() throws Exception {
        testIsMapClick("img1", false, "?0,0", "?25,30");
        testIsMapClick("img2", false, "", "");
        testIsMapClick("img3", true, "", "");
        testIsMapClick("img3", true, "", "");
    }

    private void testIsMapClick(final String imgId, final boolean samePage, 
            final String urlSuffixClick, final String urlSuffixClickXY) throws Exception {

        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<a href='http://server/foo'>"
            + "<img id='img1' src='foo.png' ismap>"
            + "<img id='img2' src='foo.png'>"
            + "</a>"
            + "<img id='img3' src='foo.png' ismap>"
            + "<img id='img4' src='foo.png'>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        
        final HtmlImage img = (HtmlImage) page.getHtmlElementById(imgId);
        
        final Page page2 = img.click();
        assertEquals("same page after click", samePage, (page == page2));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClick, page2.getWebResponse().getUrl());
        }

        final Page page3 = img.click(25, 30);
        assertEquals("same page after click(25, 30)", samePage, (page == page3));
        if (!samePage) {
            assertEquals("http://server/foo" + urlSuffixClickXY, page3.getWebResponse().getUrl());
        }
    }

}
