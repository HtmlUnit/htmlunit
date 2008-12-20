/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link TreeWalker}.
 *
 * @version $Revision$
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 */
public class TreeWalkerTest extends WebTestCase {
    private static final String contentStart = "<html><head><title>TreeWalker Test</title>\n"
        + "<script>\n"
        + "function safeTagName(o)\n"
        + "{\n"
        + "  return o ? o.tagName : undefined\n"
        + "}\n"
        + "function alertTreeWalker(tw)\n"
        + "{\n"
        + "  alert(safeTagName(tw.root));\n"
        + "  alert(safeTagName(tw.currentNode));\n"
        + "  alert(tw.whatToShow);\n"
        + "  alert(tw.expandEntityReferences);\n"
        + "}\n"
        + "function test() {\n";
    private static final String contentEnd = "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a  id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        +  "</div>\n"
        + "<p id='theP'>for TreeWalker's</p>\n"
        + "</body></html>";

    private void test(final String script, final String[] expectedAlerts) throws Exception {
        final String content = contentStart + script + contentEnd;

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    private static final String contentStart2 = "<html><head><title>TreeWalker Test</title>\n"
        + "<script>\n"
        + "function safeTagName(o)\n"
        + "{\n"
        + "  return o ? o.tagName : undefined\n"
        + "}\n"
        + "function test() {\n";
    private static final String contentEnd2 = "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a  id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        +  "</div>\n"
        + "<p id='theP'>for <br/>TreeWalkers<span>something</span>that is <a>important to me</a></p>\n"
        + "<span>something <code>codey</code>goes <pre>  here</pre></span>"
        + "</body></html>";

    private void test2(final String script, final String[] expectedAlerts) throws Exception {
        final String content = contentStart2 + script + contentEnd2;

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getters1() throws Exception {
        final String[] expectedAlerts = {"BODY", "BODY", Integer.toString(NodeFilter.SHOW_ELEMENT), "false"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, false);"
                            + "alertTreeWalker(tw);";

        test(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getters2() throws Exception {
        final String[] expectedAlerts = {"A", "A", "" + 0xFFFFFFFF, "true"};
        final String script = "var tw = document.createTreeWalker(document.getElementById('theA'), NodeFilter.SHOW_ALL,"
                                            + "null, true); alertTreeWalker(tw);";

        test(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void firstChild() throws Exception {
        final String[] expectedAlerts = {"BODY", "DIV", Integer.toString(NodeFilter.SHOW_ELEMENT), "true"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "tw.firstChild(); alertTreeWalker(tw);";

        test(script, expectedAlerts);

        final String[] expectedAlerts1 = {"BODY", "SPAN", Integer.toString(NodeFilter.SHOW_ELEMENT), "true"};
        final String script1 = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                        + "tw.currentNode = document.getElementById('theDiv'); tw.firstChild(); alertTreeWalker(tw);";

        test(script1, expectedAlerts1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lastChild() throws Exception {
        final String[] expectedAlerts = {"BODY", "P", Integer.toString(NodeFilter.SHOW_ELEMENT), "true"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "tw.lastChild(); alertTreeWalker(tw);";

        test(script, expectedAlerts);

        final String[] expectedAlerts1 = {"BODY", "SPAN", Integer.toString(NodeFilter.SHOW_ELEMENT), "true"};
        final String script1 = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                        + "tw.currentNode = document.getElementById('theDiv'); tw.lastChild(); alertTreeWalker(tw);";

        test(script1, expectedAlerts1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void parentNode() throws Exception {
        final String[] expectedAlerts = {"BODY", "BODY", Integer.toString(NodeFilter.SHOW_ELEMENT), "true", "null"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "tw.currentNode = document.getElementById('theDiv'); tw.parentNode();"
                            + "alertTreeWalker(tw); alert(tw.parentNode());";

        test(script, expectedAlerts);

        final String[] expectedAlerts1 = {"BODY", "DIV", Integer.toString(NodeFilter.SHOW_ELEMENT), "true"};
        final String script1 = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                        + "tw.currentNode = document.getElementById('theSpan'); tw.parentNode(); alertTreeWalker(tw);";

        test(script1, expectedAlerts1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void siblings() throws Exception {
        final String[] expectedAlerts = {"BODY", "P", Integer.toString(NodeFilter.SHOW_ELEMENT), "true", "null"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "tw.currentNode = document.getElementById('theDiv'); tw.nextSibling();"
                            + "alertTreeWalker(tw); alert(tw.nextSibling());";

        test(script, expectedAlerts);

        final String[] expectedAlerts1 = {"BODY", "DIV", Integer.toString(NodeFilter.SHOW_ELEMENT), "true", "null"};
        final String script1 = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "tw.currentNode = document.getElementById('theP'); tw.previousSibling();"
                            + "alertTreeWalker(tw); alert(tw.previousSibling());";

        test(script1, expectedAlerts1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void next() throws Exception {
        final String[] expectedAlerts = {"BODY", "DIV", "SPAN", "A", "P", "undefined", "P"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "alert(safeTagName(tw.currentNode)); alert(safeTagName(tw.nextNode()));"
                            + "alert(safeTagName(tw.nextNode())); alert(safeTagName(tw.nextNode()));"
                            + "alert(safeTagName(tw.nextNode())); alert(safeTagName(tw.nextNode()));"
                            + "alert(safeTagName(tw.currentNode));";

        test(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void previous() throws Exception {
        final String[] expectedAlerts = {"P", "A", "SPAN", "DIV", "BODY", "undefined", "BODY"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "tw.currentNode = document.getElementById('theP'); alert(safeTagName(tw.currentNode));"
                            + "alert(safeTagName(tw.previousNode())); alert(safeTagName(tw.previousNode()));"
                            + "alert(safeTagName(tw.previousNode())); alert(safeTagName(tw.previousNode()));"
                            + "alert(safeTagName(tw.previousNode())); alert(safeTagName(tw.currentNode));";

        test(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void walking() throws Exception {
        final String[] expectedAlerts = {"DIV", "SPAN", "A", "undefined", "P", "BODY", "undefined", "SPAN", "undefined",
                                         "P", "SPAN", "CODE", "PRE", "undefined"};
        final String script = "var tw = document.createTreeWalker(document.body, 1, null, true);"
                            + "alert(safeTagName(tw.firstChild())); alert(safeTagName(tw.firstChild()));"
                            + "alert(safeTagName(tw.lastChild())); alert(safeTagName(tw.lastChild()));"
                            + "alert(safeTagName(tw.nextNode())); alert(safeTagName(tw.parentNode()));"
                            + "alert(safeTagName(tw.parentNode())); alert(safeTagName(tw.lastChild()));"
                            + "alert(safeTagName(tw.nextSibling()));  alert(safeTagName(tw.previousSibling()));"
                            + "alert(safeTagName(tw.nextSibling())); alert(safeTagName(tw.nextNode()));"
                            + "alert(safeTagName(tw.nextNode())); alert(safeTagName(tw.nextNode()));";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void walkingOutsideTheRoot() throws Exception {
        final String[] expectedAlerts = {"TITLE", "SCRIPT", "HEAD", "HTML", "HEAD", "BODY", "undefined"};
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);"
                            + "tw.currentNode = document.firstChild.firstChild; alert(safeTagName(tw.firstChild()));"
                            + "alert(safeTagName(tw.nextNode())); alert(safeTagName(tw.parentNode()));"
                            + "alert(safeTagName(tw.previousNode())); alert(safeTagName(tw.firstChild()));"
                            + "alert(safeTagName(tw.nextSibling())); alert(safeTagName(tw.previousSibling()));";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = ScriptException.class)
    public void nullRoot() throws Exception {
        final String[] expectedAlerts = {};
        final String script = "var tw = document.createTreeWalker(null, NodeFilter.SHOW_ELEMENT, null, true);";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void simpleFilter() throws Exception {
        final String[] expectedAlerts = {"TITLE", "undefined", "HEAD", "HTML", "HEAD", "BODY", "undefined"};
        final String script = "var noScripts = {acceptNode: function(node) {"
            + "if (node.tagName == 'SCRIPT') return NodeFilter.FILTER_REJECT;"
            // using number rather that object field causes Rhino to pass a Double
            + "return 1; // NodeFilter.FILTER_ACCEPT \n}};"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts,"
            + "true); tw.currentNode = document.firstChild.firstChild;"
            + "alert(safeTagName(tw.firstChild())); alert(safeTagName(tw.nextSibling()));"
            + "alert(safeTagName(tw.parentNode())); alert(safeTagName(tw.previousNode()));"
            + "alert(safeTagName(tw.firstChild())); alert(safeTagName(tw.nextSibling()));"
            + "alert(safeTagName(tw.previousSibling()));";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = ScriptException.class)
    public void emptyFilter() throws Exception {
        final String[] expectedAlerts = {"TITLE", "undefined", "HEAD", "HTML", "HEAD", "BODY", "undefined"};
        final String script = "var noScripts = {};"
                            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts,"
                                            + "true); tw.currentNode = document.firstChild.firstChild;"
                            + "alert(safeTagName(tw.firstChild())); alert(safeTagName(tw.nextSibling()));"
                            + "alert(safeTagName(tw.parentNode())); alert(safeTagName(tw.previousNode()));"
                            + "alert(safeTagName(tw.firstChild())); alert(safeTagName(tw.nextSibling()));"
                            + "alert(safeTagName(tw.previousSibling()));";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void secondFilterReject() throws Exception {
        final String[] expectedAlerts = {"P", "undefined"};
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
                                    + "node.tagName == 'DIV') return NodeFilter.FILTER_REJECT;"
                                + "return NodeFilter.FILTER_ACCEPT}};"
                            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts,"
                            + "true); alert(safeTagName(tw.firstChild())); alert(safeTagName(tw.nextSibling()));";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void secondFilterSkip() throws Exception {
        final String[] expectedAlerts = {"A", "P", "CODE", "PRE", "undefined"};
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
                                    + "node.tagName == 'DIV') return NodeFilter.FILTER_SKIP;"
                                + "return NodeFilter.FILTER_ACCEPT}};"
                            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts,"
                                + "true); alert(safeTagName(tw.firstChild())); alert(safeTagName(tw.nextSibling()));"
                            + "alert(safeTagName(tw.nextSibling())); alert(safeTagName(tw.nextSibling()));"
                            + "alert(safeTagName(tw.nextSibling()));";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void secondFilterRejectReverse() throws Exception {
        final String[] expectedAlerts = {"P", "undefined"};
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
                                    + "node.tagName == 'DIV') return NodeFilter.FILTER_REJECT;"
                                + "return NodeFilter.FILTER_ACCEPT}};"
                            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts,"
                            + "true); alert(safeTagName(tw.lastChild())); alert(safeTagName(tw.previousSibling()));";

        test2(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void secondFilterSkipReverse() throws Exception {
        final String[] expectedAlerts = {"PRE", "CODE", "P", "A", "undefined"};
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
                            + "node.tagName == 'DIV') return NodeFilter.FILTER_SKIP; return NodeFilter.FILTER_ACCEPT}};"
                            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts,"
                            + "true); alert(safeTagName(tw.lastChild())); alert(safeTagName(tw.previousSibling()));"
                            + "alert(safeTagName(tw.previousSibling())); alert(safeTagName(tw.previousSibling()));"
                            + "alert(safeTagName(tw.previousSibling()));";

        test2(script, expectedAlerts);
    }
}
