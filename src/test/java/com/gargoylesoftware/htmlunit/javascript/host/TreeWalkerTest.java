/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link TreeWalker}.
 *
 * @version $Revision$
 * @author <a href="mailto:mike@10gen.com">Mike Dirolf</a>
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class TreeWalkerTest extends WebDriverTestCase {
    private static final String contentStart = "<html><head><title>TreeWalker Test</title>\n"
        + "<script>\n"
        + "function safeTagName(o) {\n"
        + "  return o ? o.tagName : undefined\n"
        + "}\n"
        + "function alertTreeWalker(tw) {\n"
        + "  alert(safeTagName(tw.root));\n"
        + "  alert(safeTagName(tw.currentNode));\n"
        + "  alert(tw.whatToShow);\n"
        + "  alert(tw.expandEntityReferences);\n"
        + "}\n"
        + "function test() {\n"
        + "  try {\n";
    private static final String contentEnd = "\n  } catch(e) { alert('exception') };\n"
        + "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a  id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        + "</div>\n"
        + "<p id='theP'>for TreeWalker's</p>\n"
        + "</body></html>";

    private void test(final String script) throws Exception {
        final String html = contentStart + script + contentEnd;

        loadPageWithAlerts2(html);
    }

    private static final String contentStart2 = "<html><head><title>TreeWalker Test</title>\n"
        + "<script>\n"
        + "function safeTagName(o) {\n"
        + "  return o ? o.tagName : undefined\n"
        + "}\n"
        + "function test() {\n"
        + "  try {\n";
    private static final String contentEnd2 = "\n  } catch(e) { alert('exception') };\n"
        + "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a  id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        + "</div>\n"
        + "<p id='theP'>for <br/>TreeWalkers<span>something</span>that is <a>important to me</a></p>\n"
        + "<span>something <code>codey</code>goes <pre>  here</pre></span>"
        + "</body></html>";

    private void test2(final String script) throws Exception {
        final String html = contentStart2 + script + contentEnd2;

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "BODY", "1", "false" },
            IE8 = "exception")
    public void getters1() throws Exception {
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, false);"
                            + "alertTreeWalker(tw);";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "A", "A", "4294967295", "true" },
            FF17 = { "A", "A", "4294967295", "false" },
            IE = "exception",
            IE10 = { "A", "A", "-1", "true" })
    public void getters2() throws Exception {
        final String script = "var theA = document.getElementById('theA');\n"
            + "var tw = document.createTreeWalker(theA, NodeFilter.SHOW_ALL, null, true);\n"
            + "alertTreeWalker(tw);\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "DIV", "1", "true" },
            FF17 = { "BODY", "DIV", "1", "false" },
            IE8 = "exception")
    public void firstChild() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.firstChild();\n"
            + "alertTreeWalker(tw);\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "SPAN", "1", "true" },
            FF17 = { "BODY", "SPAN", "1", "false" },
            IE8 = "exception")
    public void firstChild2() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theDiv');\n"
            + "tw.firstChild();\n"
            + "alertTreeWalker(tw);\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "P", "1", "true" },
            FF17 = { "BODY", "P", "1", "false" },
            IE8 = "exception")
    public void lastChild() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.lastChild();\n"
            + "alertTreeWalker(tw);\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "SPAN", "1", "true" },
            FF17 = { "BODY", "SPAN", "1", "false" },
            IE8 = "exception")
    public void lastChild2() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theDiv');\n"
            + "tw.lastChild();\n"
            + "alertTreeWalker(tw);\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "BODY", "1", "true", "null" },
            FF17 = { "BODY", "BODY", "1", "false", "null" },
            IE8 = "exception")
    public void parentNode() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theDiv');\n"
            + "tw.parentNode();\n"
            + "alertTreeWalker(tw);\n"
            + "alert(tw.parentNode());";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "DIV", "1", "true" },
            FF17 = { "BODY", "DIV", "1", "false" },
            IE8 = "exception")
    public void parentNode2() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theSpan');\n"
            + "tw.parentNode();\n"
            + "alertTreeWalker(tw);";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "P", "1", "true", "null" },
            FF17 = { "BODY", "P", "1", "false", "null" },
            IE8 = "exception")
    public void siblings() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theDiv');\n"
            + "tw.nextSibling();\n"
            + "alertTreeWalker(tw);\n"
            + "alert(tw.nextSibling());\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "DIV", "1", "true", "null" },
            FF17 = { "BODY", "DIV", "1", "false", "null" },
            IE8 = "exception")
    public void siblings2() throws Exception {
        final String script1 =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theP');\n"
            + "tw.previousSibling();\n"
            + "alertTreeWalker(tw);\n"
            + "alert(tw.previousSibling());\n";

        test(script1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "BODY", "DIV", "SPAN", "A", "P", "undefined", "P" },
            IE8 = "exception")
    public void next() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "alert(safeTagName(tw.currentNode));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.currentNode));\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "P", "A", "SPAN", "DIV", "BODY", "undefined", "BODY" },
            IE8 = "exception")
    public void previous() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theP');\n"
            + "alert(safeTagName(tw.currentNode));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.currentNode));\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "DIV", "SPAN", "A", "undefined", "P", "BODY", "undefined", "SPAN", "undefined",
            "P", "SPAN", "CODE", "PRE", "undefined" },
            IE8 = "exception")
    public void walking() throws Exception {
        final String script = "var tw = document.createTreeWalker(document.body, 1, null, true);\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.lastChild()));\n"
            + "alert(safeTagName(tw.lastChild()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.parentNode()));\n"
            + "alert(safeTagName(tw.parentNode()));\n"
            + "alert(safeTagName(tw.lastChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.nextNode()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "TITLE", "SCRIPT", "HEAD", "HTML", "HEAD", "BODY", "undefined" },
            IE8 = "exception")
    public void walkingOutsideTheRoot() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.firstChild.firstChild;\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextNode()));\n"
            + "alert(safeTagName(tw.parentNode()));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void nullRoot() throws Exception {
        final String script = "try {\n"
            + "var tw = document.createTreeWalker(null, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "} catch(e) { alert('exception'); }\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "TITLE", "undefined", "HEAD", "HTML", "HEAD", "BODY", "undefined" },
            IE = "exception")
    public void simpleFilter() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {"
            + "if (node.tagName == 'SCRIPT') return NodeFilter.FILTER_REJECT;"
            // using number rather that object field causes Rhino to pass a Double
            + "return 1; // NodeFilter.FILTER_ACCEPT \n}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "tw.currentNode = document.firstChild.firstChild;\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.parentNode()));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "TITLE", "undefined", "HEAD", "HTML", "HEAD", "BODY", "undefined" },
            IE8 = "exception")
    public void simpleFilter_asAFunction() throws Exception {
        final String script = "var noScripts = function(node) {"
            + "if (node.tagName == 'SCRIPT') return NodeFilter.FILTER_REJECT;"
            + "return 1;};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "tw.currentNode = document.firstChild.firstChild;\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.parentNode()));\n"
            + "alert(safeTagName(tw.previousNode()));\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void emptyFilter() throws Exception {
        final String script = "try {\n"
            + "var tw = document.createTreeWalker(null, NodeFilter.SHOW_ELEMENT, {}, true);\n"
            + "} catch(e) { alert('exception'); }\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "P", "undefined" },
            IE = "exception")
    public void secondFilterReject() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
            + "node.tagName == 'DIV') return NodeFilter.FILTER_REJECT;"
            + "return NodeFilter.FILTER_ACCEPT}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "A", "P", "CODE", "PRE", "undefined" },
            IE = "exception")
    public void secondFilterSkip() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
            + "node.tagName == 'DIV') return NodeFilter.FILTER_SKIP;"
            + "return NodeFilter.FILTER_ACCEPT}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "alert(safeTagName(tw.firstChild()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n"
            + "alert(safeTagName(tw.nextSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "P", "undefined" },
            IE = "exception")
    public void secondFilterRejectReverse() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
            + "node.tagName == 'DIV') return NodeFilter.FILTER_REJECT;"
            + "return NodeFilter.FILTER_ACCEPT}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "alert(safeTagName(tw.lastChild()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "PRE", "CODE", "P", "A", "undefined" },
            IE = "exception")
    public void secondFilterSkipReverse() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
            + "node.tagName == 'DIV') return NodeFilter.FILTER_SKIP; return NodeFilter.FILTER_ACCEPT}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "alert(safeTagName(tw.lastChild()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n"
            + "alert(safeTagName(tw.previousSibling()));\n"
            + "alert(safeTagName(tw.previousSibling()));";

        test2(script);
    }
}
