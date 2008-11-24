/*
 * CommentTest.java 20.11.2008
 * 
 * Copyright (c) 2008 1&1 Internet AG. All rights reserved.
 * 
 * $Id$
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;


/**
 * Tests for {@link CDATASection}.
 *
 * @version $Revision$
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CDATASectionTest extends WebTestCase {
    
    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "null", FF = "[object Comment]")
    public void simpleScriptable() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  alert(document.body.firstChild);\n"
            + "}\n"
            + "</script></head><body onload='test()'><![CDATA[Jeep]]></body></html>";
        loadPageWithAlerts(html);
    }

}
