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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ClassUtils;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

/**
 * Tests for {@link SimpleScriptable}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:BarnabyCourt@users.sourceforge.net">Barnaby Court</a>
 * @author David K. Taylor
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class SimpleScriptableTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public SimpleScriptableTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testCallInheritedFunction() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

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

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        final List expectedAlerts = Collections.singletonList("past focus");
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        assertEquals("foo", page.getTitleText());
        assertEquals("focus not changed to textfield1",
                     page.getFormByName("form1").getInputByName("textfield1"),
                     page.getElementWithFocus());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     */
    public void testHtmlJavaScriptMapping_AllJavaScriptClassesArePresent() {
        final Map map = JavaScriptConfiguration.getHtmlJavaScriptMapping();
        final String directoryName = "../../src/java/com/gargoylesoftware/htmlunit/javascript/host";
        final Set names = getFileNames(directoryName.replace('/', File.separatorChar));

        // Now pull out those names that we know don't have html equivalents
        names.remove("Document");
        names.remove("Event");
        names.remove("MouseEvent");
        names.remove("EventHandler");
        names.remove("History");
        names.remove("Location");
        names.remove("Navigator");
        names.remove("NodeImpl");
        names.remove("Screen");
        names.remove("Style");
        names.remove("Stylesheet");
        names.remove("ActiveXObject");
        names.remove("XMLHttpRequest");
        names.remove("Window");
        names.remove("Attribute");
        names.remove("ScoperFunctionObject");
        names.remove("RowContainer");
        names.remove("FormField");
        names.remove("JavaScriptBackgroundJob");
        names.remove("Popup");
        names.remove("EventListenersContainer");
        names.remove("DOMImplementation");
        names.remove("TextRectangle");

        final Transformer class2ShortName = new Transformer() {
            public Object transform(final Object obj) {
                return ClassUtils.getShortClassName((Class) obj);
            }
        };
        final Collection hostClassNames = new ArrayList(map.values());
        CollectionUtils.transform(hostClassNames, class2ShortName);
        assertEquals(new TreeSet(names),  new TreeSet(hostClassNames));
    }

    private Set getFileNames(final String directoryName) {
        File directory = new File("." + File.separatorChar + directoryName);
        if (!directory.exists()) {
            directory = new File("./src/java/".replace('/', File.separatorChar) + directoryName);
        }
        assertTrue("directory exists", directory.exists());
        assertTrue("is a directory", directory.isDirectory());

        final String fileNames[] = directory.list();
        final Set collection = new HashSet();

        for (int i = 0; i < fileNames.length; i++) {
            final String name = fileNames[i];
            if (name.endsWith(".java")) {
                collection.add(name.substring(0, name.length() - 5));
            }
        }
        return collection;
    }

    /**
     * This test fails on IE and FF but not by HtmlUnit because according to Ecma standard,
     * attempts to set read only properties should be silently ignored.
     * Furthermore document.body = document.body will work on FF but not on IE
     * @throws Exception if the test fails
     */
    public void testSetNonWritableProperty() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String content
            = "<html><head><title>foo</title></head><body onload='document.body=123456'>"
            + "</body></html>";

        try {
            loadPage(content);
            fail("Exception should have been thrown");
        }
        catch (final ScriptException e) {
            // it's ok
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testArguments_toString() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(arguments);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object Object]"};
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
