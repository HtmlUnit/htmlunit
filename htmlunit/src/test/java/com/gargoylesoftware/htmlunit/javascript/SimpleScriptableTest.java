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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.FakeWebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Tests for SimpleScriptable.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  <a href="mailto:BarnabyCourt@users.sourceforge.net">Barnaby Court</a>
 * @author  David K. Taylor
 */
public class SimpleScriptableTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public SimpleScriptableTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
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


    /**
     */
    public void testHtmlJavaScriptMapping_AllJavaScriptClassesArePresent() {
        final Map map = SimpleScriptable.getHtmlJavaScriptMapping();
        final String directoryName = "../../src/java/com/gargoylesoftware/htmlunit/javascript/host";
        final Set names = getFileNames(directoryName.replace('/', File.separatorChar));

        // Now pull out those names that we know don't have html equivilents
        names.remove("CharacterDataImpl");
        names.remove("Document");
        names.remove("History");
        names.remove("Location");
        names.remove("Navigator");
        names.remove("NodeImpl");
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
