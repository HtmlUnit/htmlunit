/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
     * @throws Exception if the test fails
     */
    @Test
    public void callInheritedFunction() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    document.form1.textfield1.focus();\n"
            + "    alert('past focus');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo'/>\n"
            + "</form>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        final List<String> expectedAlerts = Collections.singletonList("past focus");
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        assertEquals("foo", page.getTitleText());
        Assert.assertEquals("focus not changed to textfield1",
                     page.getFormByName("form1").getInputByName("textfield1"),
                     page.getElementWithFocus());
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     */
    @Test
    public void htmlJavaScriptMapping_AllJavaScriptClassesArePresent() {
        final Map<Class < ? extends HtmlElement>, Class < ? extends SimpleScriptable>> map =
            JavaScriptConfiguration.getHtmlJavaScriptMapping();
        final String directoryName = "../../../src/main/java/com/gargoylesoftware/htmlunit/javascript/host";
        final Set<String> names = getFileNames(directoryName.replace('/', File.separatorChar));

        // Now pull out those names that we know don't have html equivalents
        names.remove("ActiveXObject");
        names.remove("BoxObject");
        names.remove("ComputedCSSStyleDeclaration");
        names.remove("CSSStyleDeclaration");
        names.remove("Document");
        names.remove("DOMImplementation");
        names.remove("DOMParser");
        names.remove("Event");
        names.remove("EventNode");
        names.remove("EventHandler");
        names.remove("EventListenersContainer");
        names.remove("FormField");
        names.remove("History");
        names.remove("HTMLCollection");
        names.remove("HTMLCollectionTags");
        names.remove("HTMLOptionsCollection");
        names.remove("JavaScriptBackgroundJob");
        names.remove("Location");
        names.remove("MimeType");
        names.remove("MimeTypeArray");
        names.remove("MouseEvent");
        names.remove("Navigator");
        names.remove("Node");
        names.remove("Plugin");
        names.remove("PluginArray");
        names.remove("Popup");
        names.remove("Range");
        names.remove("RowContainer");
        names.remove("Screen");
        names.remove("ScoperFunctionObject");
        names.remove("Selection");
        names.remove("SimpleArray");
        names.remove("Stylesheet");
        names.remove("StyleSheetList");
        names.remove("TextRange");
        names.remove("TextRectangle");
        names.remove("UIEvent");
        names.remove("Window");
        names.remove("XMLDocument");
        names.remove("XMLDOMParseError");
        names.remove("XMLHttpRequest");
        names.remove("XMLSerializer");
        names.remove("XPathNSResolver");
        names.remove("XPathResult");
        names.remove("XSLTProcessor");
        names.remove("XSLTemplate");

        final Collection<String> hostClassNames = new ArrayList<String>();
        for (final Class< ? extends SimpleScriptable> clazz : map.values()) {
            hostClassNames.add(ClassUtils.getShortClassName(clazz));
        }
        assertEquals(new TreeSet<String>(names), new TreeSet<String>(hostClassNames));
    }

    private Set<String> getFileNames(final String directoryName) {
        File directory = new File("." + File.separatorChar + directoryName);
        if (!directory.exists()) {
            directory = new File("./src/main/java/".replace('/', File.separatorChar) + directoryName);
        }
        assertTrue("directory exists", directory.exists());
        assertTrue("is a directory", directory.isDirectory());

        final Set<String> collection = new HashSet<String>();

        for (final String name : directory.list()) {
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
    @Test
    public void setNonWritableProperty() throws Exception {
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
     * Works since Rhino 1.7
     * @throws Exception if the test fails
     */
    @Test
    public void arguments_toString() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(arguments);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object Object]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stringWithExclamationMark() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var x = '<!>';\n"
            + "    alert(x.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"3"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
    /**
     * Test the host class names match the Firefox (w3c names).
     * @see <a
     *   href="http://java.sun.com/j2se/1.5.0/docs/guide/plugin/dom/org/w3c/dom/html/package-summary.html">DOM API</a>
     * @throws Exception if the test fails.
     */
    @Test
    public void hostClassNames() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        testHostClassNames("HTMLAnchorElement");
    }

    private void testHostClassNames(final String className) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(" + className + ");\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {'[' + className + ']'};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * https://bugzilla.mozilla.org/show_bug.cgi?id=419090
     *
     * @throws Exception if the test fails
     */
    @Test
    public void arrayedMap() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var map = {};\n"
            + "    map['x1'] = 'y1';\n"
            + "    map['x2'] = 'y2';\n"
            + "    map['x3'] = 'y3';\n"
            + "    map['x4'] = 'y4';\n"
            + "    map['x5'] = 'y5';\n"
            + "    for (var i in map) {\n"
            + "      alert(i);\n"
            + "    }"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"x1", "x2", "x3", "x4", "x5"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void isParentOf() throws Exception {
        isParentOf("Node", "Element", true);
        isParentOf("Document", "XMLDocument", true);
        isParentOf("Node", "XPathResult", false);
        isParentOf("Element", "HTMLElement", true);
        isParentOf("HTMLElement", "HTMLHtmlElement", true);
        isParentOf("CSSStyleDeclaration", "ComputedCSSStyleDeclaration", true);

        //although Image != HTMLImageElement, they seem to be synonyms!!!
        isParentOf("Image", "HTMLImageElement", true);
        isParentOf("HTMLImageElement", "Image", true);
    }

    private void isParentOf(final String object1, final String object2, final boolean status) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(isParentOf(" + object1 + ", " + object2 + "));\n"
            + "  }\n"
            + "  /**\n"
            + "   * Returns true if o1 prototype is parent/grandparent of o2 prototype\n"
            + "   */\n"
            + "  function isParentOf(o1, o2) {\n"
            + "    o1.prototype.myCustomFunction = function() {};\n"
            + "    return o2.prototype.myCustomFunction != undefined;\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {Boolean.toString(status)};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void windowPropertyToString() throws Exception {
        final String content = "<html id='myId'><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "    alert(HTMLHtmlElement);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLHtmlElement]", "[HTMLHtmlElement]"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * This is related to HtmlUnitContextFactory.hasFeature(Context.FEATURE_PARENT_PROTO_PROPERTIES).
     * @throws Exception if the test fails
     */
    @Test
    public void parentProtoFeature() throws Exception {
        parentProtoFeature(BrowserVersion.INTERNET_EXPLORER_7_0, new String[] {"false"});
        parentProtoFeature(BrowserVersion.FIREFOX_2, new String[] {"true"});
    }

    private void parentProtoFeature(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    alert(document.createElement('div').__proto__ != undefined);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
