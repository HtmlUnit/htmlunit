/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;
import java.net.URL;
import java.util.Collections;

/**
 *  Tests for HtmlOption
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlOptionTest extends WebTestCase {
    public HtmlOptionTest( final String name ) {
        super(name);
    }


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

        final FakeWebConnection webConnection = new FakeWebConnection( client );
        webConnection.setContent( htmlContent );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first" ), SubmitMethod.POST, Collections.EMPTY_LIST );

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
}
