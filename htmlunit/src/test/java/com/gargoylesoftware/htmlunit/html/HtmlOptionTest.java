/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import java.util.Collections;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for HtmlOption
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlOptionTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public HtmlOptionTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testSelect() throws Exception {

        final String htmlContent
                 = "<html><head><title>foo</title></head><body>"
                 + "<form id='form1'><select name='select1' id='select1'>"
                 + "<option value='option1' id='option1'>Option1</option>"
                 + "<option value='option2' id='option2' selected='selected'>Option2</option>"
                 + "<option value='option3' id='option3'>Option3</option>"
                 + "</select>"
                 + "<input type='submit' name='button' value='foo'/>"
                 + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_FIRST, SubmitMethod.POST, Collections.EMPTY_LIST );

        final HtmlOption option1 = ( HtmlOption )page.getHtmlElementById( "option1" );
        final HtmlOption option2 = ( HtmlOption )page.getHtmlElementById( "option2" );
        final HtmlOption option3 = ( HtmlOption )page.getHtmlElementById( "option3" );

        assertFalse( option1.isSelected() );
        assertTrue ( option2.isSelected() );
        assertFalse( option3.isSelected() );

        option3.setSelected(true);

        assertFalse( option1.isSelected() );
        assertFalse( option2.isSelected() );
        assertTrue ( option3.isSelected() );

        option3.setSelected(false);

        assertFalse( option1.isSelected() );
        assertFalse( option2.isSelected() );
        assertFalse( option3.isSelected() );
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testGetValue() throws Exception {

        final String htmlContent
        = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'><select name='select1' id='select1'>"
            + "<option value='option1' id='option1'>Option1</option>"
            + "<option id='option2' selected>Number Two</option>"
            + "</select>"
            + "<input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(URL_FIRST);

        final HtmlOption option1 = ( HtmlOption )page.getHtmlElementById( "option1" );
        final HtmlOption option2 = ( HtmlOption )page.getHtmlElementById( "option2" );
        
        assertEquals("option1", option1.getValue());
        assertEquals("Number Two", option2.getValue());

    }    
    /**
     * @throws Exception if the test fails
     */
    public void testGetValue_ContentsIsValue() throws Exception {

        final String htmlContent
        = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>" 
            + "<select name='select1' id='select1'>"
            + "     <option id='option1'>Option1</option>"
            + "     <option id='option2' selected>Number Two</option>"
            + "</select>"
            + "<input type='submit' name='button' value='foo'/>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(URL_FIRST);

        final HtmlOption option1 = ( HtmlOption )page.getHtmlElementById( "option1" );
        final HtmlOption option2 = ( HtmlOption )page.getHtmlElementById( "option2" );
        
        assertEquals("Option1", option1.getValue());
        assertEquals("Number Two", option2.getValue());

    }        
}
