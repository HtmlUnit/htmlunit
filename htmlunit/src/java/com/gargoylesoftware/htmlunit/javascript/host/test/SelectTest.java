/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
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
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class SelectTest extends WebTestCase {
    public SelectTest( final String name ) {
        super(name);
    }


    public void testGetSelectedIndex() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    alert(document.form1.select1.length);\n"
                 + "    alert(document.form1.select1.selectedIndex);\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1'>One</option>"
                 + "        <option name='option2' selected>Two</option>"
                 + "        <option name='option3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

         final List expectedAlerts = Arrays.asList( new String[]{
             "3", "1"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testGetOptions() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "function doTest() {\n"
                 + "    var options = document.form1.select1.options;\n"
                 + "    for( i=0; i<options.length; i++ ) {\n"
                 + "        alert(options[i].value);\n"
                 + "    }\n"
                 + "}</script></head><body onload='doTest()'>"
                 + "<p>hello world</p>"
                 + "<form name='form1'>"
                 + "    <select name='select1'>"
                 + "        <option name='option1'>One</option>"
                 + "        <option name='option2' selected>Two</option>"
                 + "        <option name='option3'>Three</option>"
                 + "    </select>"
                 + "</form>"
                 + "</body></html>";

         final List collectedAlerts = new ArrayList();
         final HtmlPage page = loadPage(content, collectedAlerts);

         final List expectedAlerts = Arrays.asList( new String[]{
             "One", "Two", "Three"
         } );

         assertEquals( expectedAlerts, collectedAlerts );
    }
}
