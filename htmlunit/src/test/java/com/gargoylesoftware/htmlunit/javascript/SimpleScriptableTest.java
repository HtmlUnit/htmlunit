/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.ClassUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
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
 * @author Sudhan Moghe
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 */
@RunWith(BrowserRunner.class)
public class SimpleScriptableTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("past focus")
    public void callInheritedFunction() throws Exception {
        final String html
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

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("foo", page.getTitleText());
        Assert.assertSame("focus not changed to textfield1",
                     page.getFormByName("form1").getInputByName("textfield1"),
                     page.getFocusedElement());
    }

    /**
     * Test.
     */
    @Test
    public void htmlJavaScriptMapping_AllJavaScriptClassesArePresent() {
        final JavaScriptConfiguration jsConfiguration = JavaScriptConfiguration.getInstance(getBrowserVersion());
        final Map<Class<? extends HtmlElement>, Class<? extends SimpleScriptable>> map
            = jsConfiguration.getHtmlJavaScriptMapping();
        String directoryName = "../../../src/main/java/com/gargoylesoftware/htmlunit/javascript/host";
        final Set<String> names = getFileNames(directoryName.replace('/', File.separatorChar));
        directoryName = "../../../src/main/java/com/gargoylesoftware/htmlunit/javascript/host/html";
        names.addAll(getFileNames(directoryName.replace('/', File.separatorChar)));

        // Now pull out those names that we know don't have HTML equivalents
        names.remove("ActiveXObject");
        names.remove("ActiveXObjectImpl");
        names.remove("BoxObject");
        names.remove("ClientRect");
        names.remove("ClipboardData");
        names.remove("ComputedCSSStyleDeclaration");
        names.remove("CSSImportRule");
        names.remove("CSSRule");
        names.remove("CSSRuleList");
        names.remove("CSSStyleDeclaration");
        names.remove("CSSStyleRule");
        names.remove("Document");
        names.remove("DocumentProxy");
        names.remove("DOMException");
        names.remove("DOMImplementation");
        names.remove("DOMParser");
        names.remove("Enumerator");
        names.remove("Event");
        names.remove("EventNode");
        names.remove("EventHandler");
        names.remove("EventListenersContainer");
        names.remove("External");
        names.remove("FormChild");
        names.remove("FormField");
        names.remove("History");
        names.remove("HashChangeEvent");
        names.remove("HTMLCollection");
        names.remove("HTMLCollectionTags");
        names.remove("HTMLDocument");
        names.remove("HTMLListElement");
        names.remove("HTMLOptionsCollection");
        names.remove("HTMLTableComponent");
        names.remove("KeyboardEvent");
        names.remove("JavaScriptBackgroundJob");
        names.remove("Location");
        names.remove("MediaList");
        names.remove("MessageEvent");
        names.remove("MimeType");
        names.remove("MimeTypeArray");
        names.remove("MouseEvent");
        names.remove("MutationEvent");
        names.remove("Namespace");
        names.remove("NamespaceCollection");
        names.remove("Navigator");
        names.remove("Node");
        names.remove("NodeFilter");
        names.remove("OfflineResourceList");
        names.remove("Plugin");
        names.remove("PluginArray");
        names.remove("Popup");
        names.remove("Range");
        names.remove("RowContainer");
        names.remove("Screen");
        names.remove("ScoperFunctionObject");
        names.remove("Selection");
        names.remove("SimpleArray");
        names.remove("StaticNodeList");
        names.remove("Storage");
        names.remove("StorageImpl");
        names.remove("StorageList");
        names.remove("StringCustom");
        names.remove("StyleSheetList");
        names.remove("TextRange");
        names.remove("TreeWalker");
        names.remove("UIEvent");
        names.remove("WebSocket");
        names.remove("Window");
        names.remove("WindowProxy");
        names.remove("XMLDocument");
        names.remove("XMLDOMParseError");
        names.remove("XMLHttpRequest");
        names.remove("XMLSerializer");
        names.remove("XPathNSResolver");
        names.remove("XPathResult");
        names.remove("XSLTProcessor");
        names.remove("XSLTemplate");
        names.remove("XMLAttr");

        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML5_TAGS)) {
            names.remove("HTMLAudioElement");
            names.remove("HTMLSourceElement");
            names.remove("HTMLVideoElement");
        }
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.CANVAS)) {
            names.remove("HTMLCanvasElement");
        }

        final Collection<String> hostClassNames = new ArrayList<String>();
        for (final Class<? extends SimpleScriptable> clazz : map.values()) {
            hostClassNames.add(ClassUtils.getShortClassName(clazz));
        }
        assertEquals(new TreeSet<String>(names).toString(), new TreeSet<String>(hostClassNames).toString());
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
    @NotYetImplemented(Browser.IE)
    @Alerts("exception")
    public void setNonWritableProperty() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "     document.body = 123456;\n"
            + "    } catch (e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Works since Rhino 1.7.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Object]")
    public void arguments_toString() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(arguments);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void stringWithExclamationMark() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var x = '<!>';\n"
            + "    alert(x.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test the host class names match the Firefox (w3c names).
     * @see <a
     *   href="http://java.sun.com/j2se/1.5.0/docs/guide/plugin/dom/org/w3c/dom/html/package-summary.html">DOM API</a>
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    public void hostClassNames() throws Exception {
        testHostClassNames("HTMLAnchorElement");
    }

    private void testHostClassNames(final String className) throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(" + className + ");\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts;
        if (getBrowserVersion().isFirefox() && getBrowserVersion().getBrowserVersionNumeric() < 3) {
            expectedAlerts = new String[] {'[' + className + ']'};
        }
        else {
            expectedAlerts = new String[] {"[object " + className + ']'};
        }
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getBrowserVersion(), content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Blocked by Rhino bug 419090 (https://bugzilla.mozilla.org/show_bug.cgi?id=419090).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "x1", "x2", "x3", "x4", "x5" })
    public void arrayedMap() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
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
        loadPage(getBrowserVersion(), html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * This is related to HtmlUnitContextFactory.hasFeature(Context.FEATURE_PARENT_PROTO_PROPERTIES).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "false", FF = "true")
    public void parentProtoFeature() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    alert(document.createElement('div').__proto__ != undefined);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for https://sourceforge.net/tracker/index.php?func=detail&aid=1933943&group_id=47038&atid=448266.
     * See also http://groups.google.com/group/mozilla.dev.tech.js-engine.rhino/browse_thread/thread/1f1c24f58f662c58.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void passFunctionAsParameter() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "  function run(fun) {\n"
            + "    fun('alert(1)');\n"
            + "  }\n"
            + "\n"
            + "  function test() {\n"
            + "    run(eval);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test JavaScript: 'new Date().getTimezoneOffset()' compared to java.text.SimpleDateFormat.format().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void dateGetTimezoneOffset() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var offset = Math.abs(new Date().getTimezoneOffset());\n"
            + "    var timezone = '' + (offset/60);\n"
            + "    if (timezone.length == 1)\n"
            + "      timezone = '0' + timezone;\n"
            + "    alert(timezone);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String timeZone = new SimpleDateFormat("Z").format(Calendar.getInstance().getTime());
        final String hour = timeZone.substring(1, 3);
        String strMinutes = timeZone.substring(3, 5);
        final int minutes = Integer.parseInt(strMinutes);
        final StringBuilder sb = new StringBuilder();
        if (minutes != 0) {
            sb.append(hour.substring(1));
            strMinutes = String.valueOf((double) minutes / 60);
            strMinutes = strMinutes.substring(1);
            sb.append(strMinutes);
        }
        else {
            sb.append(hour);
        }
        final String[] expectedAlerts = {sb.toString()};
        final List<String> collectedAlerts = new ArrayList<String>();
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "function", "function" })
    public void callee() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var fun = arguments.callee.toString();\n"
            + "  alert(fun.indexOf('test()') != -1);\n"
            + "  alert(typeof arguments.callee);\n"
            + "  alert(typeof arguments.callee.caller);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
