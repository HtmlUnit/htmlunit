/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class SimpleScriptableTest extends WebTestCase {
    public SimpleScriptableTest( final String name ) {
        super(name);
    }


    public void testCallInheritedFunction() throws Exception {
        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

        final String content
             = "<html><head><title>foo</title><script>"
             + "function doTest() {\n"
             + "    document.form1.textfield1.focus();\n"
             + "    alert('past focus');\n"
             + "}\n"
             + "</script></head><body onload='doTest()'>"
             + "<p>hello world</p>"
             + "<form name='form1'>"
             + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
             + "</form>"
             + "</body></html>";

        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final List expectedAlerts = Collections.singletonList("past focus");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());

        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testHtmlJavaScriptMapping_AllJavaScriptClassesArePresent() {
        final Map map = SimpleScriptable.getHtmlJavaScriptMapping();
        final String directoryName = "com/gargoylesoftware/htmlunit/javascript/host";
        final Set names = getFileNames(directoryName.replace('/', File.separatorChar));

        // Now pull out those names that we know don't have html equivilents
        names.remove("Document");
        names.remove("History");
        names.remove("Input"); // <- this one is abstract
        names.remove("Location");
        names.remove("Navigator");
        names.remove("Screen");
        names.remove("Style");

        assertEquals( new TreeSet(names), new TreeSet(map.values()) );
    }


    private Set getFileNames( final String directoryName ) {
        File directory = new File("."+File.separatorChar+directoryName);
        if( directory.exists() == false ) {
            directory = new File("./src/java/".replace('/', File.separatorChar)+directoryName );
        }
        assertTrue("directory exists", directory.exists() );
        assertTrue("is a directory", directory.isDirectory() );

        final String fileNames[] = directory.list();
        final Set collection = new HashSet();

        for( int i=0; i<fileNames.length; i++ ) {
            final String name = fileNames[i];
            if( name.endsWith(".java") ) {
                collection.add( name.substring(0, name.length()-5) );
            }
        }
        return collection;
    }
}
