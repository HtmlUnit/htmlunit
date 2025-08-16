/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TreeWalker}.
 *
 * @author Mike Dirolf
 * @author Marc Guillemot
 * @author Frank Danek
 */
public class TreeWalkerTest extends WebDriverTestCase {
    private static final String CONTENT_START = DOCTYPE_HTML
        + "<html><head><title></title>\n"
        + "<script>\n"
        + LOG_TITLE_FUNCTION
        + "function safeTagName(o) {\n"
        + "  return o ? o.tagName : undefined\n"
        + "}\n"
        + "function alertTreeWalker(tw) {\n"
        + "  log(safeTagName(tw.root));\n"
        + "  log(safeTagName(tw.currentNode));\n"
        + "  log(tw.whatToShow);\n"
        + "  log(tw.expandEntityReferences);\n"
        + "}\n"
        + "function test() {\n"
        + "  try {\n";

    private static final String CONTENT_END = "\n  } catch(e) { logEx(e) }\n"
        + "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        + "</div>\n"
        + "<p id='theP'>for TreeWalker's</p>\n"
        + "</body></html>";

    private void test(final String script) throws Exception {
        final String html = CONTENT_START + script + CONTENT_END;

        loadPageVerifyTitle2(html);
    }

    private static final String CONTENT_START2 =
        "<html><head><title></title>\n"
        + "<script>\n"
        + LOG_TITLE_FUNCTION
        + "function safeTagName(o) {\n"
        + "  return o ? o.tagName : undefined\n"
        + "}\n"
        + "function test() {\n"
        + "  try {\n";

    private static final String CONTENT_END2 = "\n  } catch(e) { logEx(e) }\n"
        + "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        + "</div>\n"
        + "<p id='theP'>for <br/>TreeWalkers<span>something</span>that is <a>important to me</a></p>\n"
        + "<span>something <code>codey</code>goes <pre>  here</pre></span>\n"
        + "</body></html>";

    private void test2(final String script) throws Exception {
        final String html = CONTENT_START2 + script + CONTENT_END2;

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"BODY", "BODY", "1", "undefined"})
    public void getters1() throws Exception {
        final String script = "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, false);"
                            + "alertTreeWalker(tw);";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"A", "A", "4294967295", "undefined"})
    // The spec states it is an unsigned long.
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
    @Alerts({"BODY", "DIV", "1", "undefined"})
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
    @Alerts({"BODY", "SPAN", "1", "undefined"})
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
    @Alerts({"BODY", "P", "1", "undefined"})
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
    @Alerts({"BODY", "SPAN", "1", "undefined"})
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
    @Alerts({"BODY", "BODY", "1", "undefined", "null"})
    public void parentNode() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theDiv');\n"
            + "tw.parentNode();\n"
            + "alertTreeWalker(tw);\n"
            + "log(tw.parentNode());";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"BODY", "DIV", "1", "undefined"})
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
    @Alerts({"BODY", "P", "1", "undefined", "null"})
    public void siblings() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theDiv');\n"
            + "tw.nextSibling();\n"
            + "alertTreeWalker(tw);\n"
            + "log(tw.nextSibling());\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"BODY", "DIV", "1", "undefined", "null"})
    public void siblings2() throws Exception {
        final String script1 =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theP');\n"
            + "tw.previousSibling();\n"
            + "alertTreeWalker(tw);\n"
            + "log(tw.previousSibling());\n";

        test(script1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"BODY", "DIV", "SPAN", "A", "P", "undefined", "P"})
    public void next() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "log(safeTagName(tw.currentNode));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.currentNode));\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"P", "A", "SPAN", "DIV", "BODY", "undefined", "BODY"})
    public void previous() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.getElementById('theP');\n"
            + "log(safeTagName(tw.currentNode));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.currentNode));\n";

        test(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DIV", "SPAN", "A", "undefined", "P", "BODY", "undefined", "SPAN", "undefined",
                "P", "SPAN", "CODE", "PRE", "undefined"})
    public void walking() throws Exception {
        final String script = "var tw = document.createTreeWalker(document.body, 1, null, true);\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.lastChild()));\n"
            + "log(safeTagName(tw.lastChild()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.parentNode()));\n"
            + "log(safeTagName(tw.parentNode()));\n"
            + "log(safeTagName(tw.lastChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.previousSibling()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.nextNode()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TITLE", "SCRIPT", "HEAD", "HTML", "HEAD", "BODY", "undefined"})
    public void walkingOutsideTheRoot() throws Exception {
        final String script =
            "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "tw.currentNode = document.firstChild.firstChild;\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextNode()));\n"
            + "log(safeTagName(tw.parentNode()));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void nullRoot() throws Exception {
        final String script = "try {\n"
            + "var tw = document.createTreeWalker(null, NodeFilter.SHOW_ELEMENT, null, true);\n"
            + "} catch(e) { logEx(e); }\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TITLE", "undefined", "HEAD", "HTML", "HEAD", "BODY", "undefined"})
    public void simpleFilter() throws Exception {
        final String script = "var noScripts = {\n"
            + "  acceptNode: function(node) {\n"
            + "    if (node.tagName == 'SCRIPT')\n"
            + "      return NodeFilter.FILTER_REJECT;\n"
            // using number rather that object field causes Rhino to pass a Double
            + "    return 1; // NodeFilter.FILTER_ACCEPT \n"
            + "  }\n"
            + "}\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "tw.currentNode = document.firstChild.firstChild;\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.parentNode()));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TITLE", "undefined", "HEAD", "HTML", "HEAD", "BODY", "undefined"})
    public void simpleFilter_asAFunction() throws Exception {
        final String script = "var noScripts = function(node) {\n"
            + "  if (node.tagName == 'SCRIPT')\n"
            + "    return NodeFilter.FILTER_REJECT;\n"
            + "  return 1;\n"
            + "}\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "tw.currentNode = document.firstChild.firstChild;\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.parentNode()));\n"
            + "log(safeTagName(tw.previousNode()));\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void emptyFilter() throws Exception {
        final String script = "try {\n"
            + "var tw = document.createTreeWalker(null, NodeFilter.SHOW_ELEMENT, {}, true);\n"
            + "} catch(e) { logEx(e); }\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"P", "undefined"})
    public void secondFilterReject() throws Exception {
        final String script = ""
            + "var noScripts = {\n"
            + "  acceptNode: function(node) {\n"
            + "    if (node.tagName == 'SPAN' || node.tagName == 'DIV') {\n"
            + "      return NodeFilter.FILTER_REJECT;\n"
            + "    }\n"
            + "    return NodeFilter.FILTER_ACCEPT;\n"
            + "  }\n"
            + "}\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"A", "P", "CODE", "PRE", "undefined"})
    public void secondFilterSkip() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
            + "node.tagName == 'DIV') return NodeFilter.FILTER_SKIP;"
            + "return NodeFilter.FILTER_ACCEPT}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "log(safeTagName(tw.firstChild()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.nextSibling()));\n"
            + "log(safeTagName(tw.nextSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"P", "undefined"})
    public void secondFilterRejectReverse() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
            + "node.tagName == 'DIV') return NodeFilter.FILTER_REJECT;"
            + "return NodeFilter.FILTER_ACCEPT}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "log(safeTagName(tw.lastChild()));\n"
            + "log(safeTagName(tw.previousSibling()));\n";

        test2(script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"PRE", "CODE", "P", "A", "undefined"})
    public void secondFilterSkipReverse() throws Exception {
        final String script = "var noScripts = {acceptNode: function(node) {if (node.tagName == 'SPAN' ||"
            + "node.tagName == 'DIV') return NodeFilter.FILTER_SKIP; return NodeFilter.FILTER_ACCEPT}};\n"
            + "var tw = document.createTreeWalker(document.body, NodeFilter.SHOW_ELEMENT, noScripts, true);\n"
            + "log(safeTagName(tw.lastChild()));\n"
            + "log(safeTagName(tw.previousSibling()));\n"
            + "log(safeTagName(tw.previousSibling()));\n"
            + "log(safeTagName(tw.previousSibling()));\n"
            + "log(safeTagName(tw.previousSibling()));";

        test2(script);
    }
}
