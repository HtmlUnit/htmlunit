/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host.test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Tests for Inputs
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class TextareaTest extends WebTestCase {
    public TextareaTest( final String name ) {
        super(name);
    }


    public void testGetValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title><script>"
            + "function doTest(){\n"
            + "alert(document.form1.textarea1.value )\n"
            + "document.form1.textarea1.value='PoohBear';\n"
            + "alert(document.form1.textarea1.value )\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1' method='post' >"
            + "<textarea name='textarea1' cols='45' rows='4'>1234</textarea>"
            + "</form></body></html>";
         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(htmlContent, collectedAlerts);
         assertEquals("foo", page.getTitleText());

         final List expectedAlerts = Arrays.asList( new String[]{
             "1234", "PoohBear"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }

}
