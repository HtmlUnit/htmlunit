/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests that have to do with when the scripting engine is called.  Javascript
 * specific tests will be found elsewhere.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ScriptEngineTest extends WebTestCase {
    public ScriptEngineTest( final String name ) {
        super(name);
    }


    /**
     * Test that code in script tags is executed on page load.  Try different combinations
     * of the script tag except for the case where a remote javascript page is loaded.  That
     * one will be tested seperately.
     */
    public void testScriptTags_AllLocalContent() throws Exception {
        final String content
                 = "<html>"
                 + "<head><title>foo</title>"
                 + "<script>One</script>" // no language specifed - assume javascript
                 + "<script language='javascript'>Two</script>"
                 + "<script type='text/javascript'>Three</script>"
                 + "<script type='text/perl'>Four</script>" // type is unsupported language
                 + "</head>"
                 + "<body>"
                 + "<p>hello world</p>"
                 + "</body></html>";
        final List collectedScripts = new ArrayList();
        loadPageAndCollectScripts(content, collectedScripts);

        // The last expected is the dummy stub that is needed to initialize the javascript engine
        final List expectedScripts = Arrays.asList( new String[]{
            "One", "Two", "Three", "" } );

        assertEquals( expectedScripts, collectedScripts );
    }


    private HtmlPage loadPageAndCollectScripts(
            final String html, final List collectedScripts )
            throws Exception {

        final WebClient client = new WebClient();
        client.setScriptEngine( new ScriptEngine(client) {
            public Object execute(
                    final HtmlPage htmlPage, final String sourceCode,
                    final String sourceName, final HtmlElement htmlElement ) {
                collectedScripts.add( sourceCode );
                return null;
            }
        } );

        final FakeWebConnection webConnection = new FakeWebConnection( client );

        webConnection.setContent( html );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        return page;
    }
}
