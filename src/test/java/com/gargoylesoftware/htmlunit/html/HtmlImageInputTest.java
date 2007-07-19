/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for HtmlImageInput
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlImageInputTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param name The name of the test
     */
    public HtmlImageInputTest( final String name ) {
        super( name );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_NoPosition() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='post'>"
            + "<input type='image' name='aButton' value='foo'/>"
            + "<input type='image' name='button' value='foo'/>"
            + "<input type='image' name='anotherButton' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);
        
        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlImageInput imageInput = (HtmlImageInput)form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage)imageInput.click();
        assertNotNull(secondPage);

        final List expectedPairs = Arrays.asList( new Object[]{
            new KeyValuePair("button.x", "0"),
            new KeyValuePair("button.y", "0")
        });

        assertEquals(
            expectedPairs,
            webConnection.getLastParameters() );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testClick_WithPosition() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='post'>"
            + "<input type='image' name='aButton' value='foo'/>"
            + "<input type='image' name='button' value='foo'/>"
            + "<input type='image' name='anotherButton' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = ( HtmlForm )page.getHtmlElementById( "form1" );

        final HtmlImageInput imageInput = (HtmlImageInput)form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage)imageInput.click(100,200);
        assertNotNull(secondPage);

        final List expectedPairs = Arrays.asList( new Object[]{
            new KeyValuePair("button.x", "100"),
            new KeyValuePair("button.y", "200")
        });

        assertEquals(
            expectedPairs,
            webConnection.getLastParameters() );
    }

    /**
     * If an image button without name is clicked, it should send only "x" and "y" parameters
     * Regression test for bug 1118877
     * @throws Exception if the test fails
     */
    public void testNoNameClick_WithPosition() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1' method='post'>"
            + "<input type='image' value='foo'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final HtmlImageInput imageInput = (HtmlImageInput) form.getInputByValue("foo");
        final HtmlPage secondPage = (HtmlPage) imageInput.click(100, 200);
        assertNotNull(secondPage);

        final List expectedPairs = Arrays.asList( new Object[]{
            new KeyValuePair("x", "100"),
            new KeyValuePair("y", "200")
        });

        assertEquals(
            expectedPairs,
            webConnection.getLastParameters() );
    }

    /**
     * @throws Exception If the test fails
     */
    public void testOutsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='image' src='test.png' onclick='alert(1)'>\n"
            + "</body></html>\n";

        final String[] expectedAlerts = {"1"};
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlImageInput input = (HtmlImageInput)page.getHtmlElementById( "myInput" );
        input.click();
        
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
