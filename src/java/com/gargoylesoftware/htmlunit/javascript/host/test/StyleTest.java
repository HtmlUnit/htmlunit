/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host.test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StyleTest extends WebTestCase {
    public StyleTest( final String name ) {
        super(name);
    }


    public void testStyle_OneCssAttribute() throws Exception {
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

        final String firstContent
             = "<html><head><title>First</title><script>\n"
             + "function doTest() {\n"
             + "    var style = document.getElementById('div1').style;\n"
             + "    alert(style.color);\n"
             + "    style.color = 'pink';\n"
             + "    alert(style.color);\n"
             + "}\n</script></head>"
             + "<body onload='doTest()'><div id='div1' style='color: black'>foo</div></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = (HtmlPage)client.getPage(new URL("http://first"));

        final List expectedAlerts = Arrays.asList( new String[]{"black", "pink"} );
        assertEquals( expectedAlerts, collectedAlerts );

        assertEquals("color: pink; ", page.getHtmlElementById("div1").getAttributeValue("style") );
    }


    public void testStyle_MultipleCssAttributes() throws Exception {
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

        final String firstContent
             = "<html><head><title>First</title><script>\n"
             + "function doTest() {\n"
             + "    var style = document.getElementById('div1').style;\n"
             + "    alert(style.color);\n"
             + "    style.color = 'pink';\n"
             + "    alert(style.color);\n"
             + "}\n</script></head>"
             + "<body onload='doTest()'><div id='div1' style='color: black;background:blue;foo:bar'>foo</div></body></html>";

        webConnection.setResponse(
            new URL("http://first"), firstContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = (HtmlPage)client.getPage(new URL("http://first"));

        final List expectedAlerts = Arrays.asList( new String[]{"black", "pink"} );
        assertEquals( expectedAlerts, collectedAlerts );

        assertEquals(
            "background: blue; color: pink; foo: bar; ",
            page.getHtmlElementById("div1").getAttributeValue("style") );
    }
}
