/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HTMLTitleElement}.
 *
 * @author Sudhan Moghe
 * @author Ronald Brill
 */
public class HTMLTitleElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Page Title", "New Title"})
    public void text() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>Page Title</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        log(title.text);\n"
            + "        title.text = 'New Title';\n"
            + "        log(title.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "New Title"})
    public void textCreateElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.createElement('title');\n"
            + "        log(title.text);\n"
            + "        title.text = 'New Title';\n"
            + "        log(title.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Page Title", "</> htmx rocks!", "</> htmx rocks!"})
    public void innerHtml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>Page Title</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        log(title.text);\n"
            + "        title.innerHTML = '</> htmx rocks!';\n"
            + "        log(title.text);\n"
            + "        log(window.document.title);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Page Title", "<div>htmx rocks</div>", "<div>htmx rocks</div>"})
    public void innerHtmlTag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>Page Title</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        log(title.text);\n"
            + "        title.innerHTML = '<div>htmx rocks</div>';\n"
            + "        log(title.text);\n"
            + "        log(window.document.title);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "</> htmx rocks!", "</> htmx rocks!"})
    public void innerHtmlEscaping() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title></title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        log(title.text);\n"
            + "        title.innerHTML = '&lt;/> htmx rocks!';\n"
            + "        log(title.text);\n"
            + "        log(window.document.title);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * The getter returns the text of all text node children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ab", "2"})
    public void textMultipleTextNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * Element children are skipped by text, but not by textContent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ab", "aELEMb", "3"})
    public void textIgnoresElementChildren() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        var s = document.createElement('span');\n"
            + "        s.appendChild(document.createTextNode('ELEM'));\n"
            + "        title.appendChild(s);\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        log(title.text);\n"
            + "        log(title.textContent);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * The getter does not stop at the first child if this is not a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"b"})
    public void textFirstChildIsElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title></title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        var s = document.createElement('span');\n"
            + "        s.appendChild(document.createTextNode('ELEM'));\n"
            + "        title.appendChild(s);\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        log(title.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * No text node children at all.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "ELEM"})
    public void textOnlyElementChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title></title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        var s = document.createElement('span');\n"
            + "        s.appendChild(document.createTextNode('ELEM'));\n"
            + "        title.appendChild(s);\n"
            + "        log(title.text);\n"
            + "        log(title.textContent);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * Comments are skipped.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ab", "3"})
    public void textSkipsComment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createComment('c'));\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * An empty text node in front does not hide the following ones.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x", "2"})
    public void textEmptyTextNodeFirst() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title></title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createTextNode(''));\n"
            + "        title.appendChild(document.createTextNode('x'));\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * splitText creates a second text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc", "2"})
    public void textAfterSplitText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>abc</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.firstChild.splitText(1);\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * A new first child is part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"xa"})
    public void textAfterInsertBefore() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.insertBefore(document.createTextNode('x'), title.firstChild);\n"
            + "        log(title.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * The text follows the remaining children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"b"})
    public void textAfterRemoveFirstChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        title.removeChild(title.firstChild);\n"
            + "        log(title.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * text is the raw value, only document.title strips and collapses whitespace.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"__a___b_", "a b"})
    public void textIsNotNormalized() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>  a   b </title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        log(title.text.replace(/ /g, '_'));\n"
            + "        log(document.title);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * The setter replaces all children, not only the first one.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"new", "new", "1"})
    public void textSetReplacesAllChildren() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        var s = document.createElement('span');\n"
            + "        s.appendChild(document.createTextNode('ELEM'));\n"
            + "        title.appendChild(s);\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        title.text = 'new';\n"
            + "        log(title.text);\n"
            + "        log(title.textContent);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * The setter also works if the first child is an element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x", "1", "3"})
    public void textSetFirstChildIsElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title></title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        var s = document.createElement('span');\n"
            + "        s.appendChild(document.createTextNode('ELEM'));\n"
            + "        title.appendChild(s);\n"
            + "        title.text = 'x';\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "        log(title.firstChild.nodeType);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * An empty string removes all children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0"})
    public void textSetEmptyString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        title.text = '';\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * The value is converted to a string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "undefined", "42"})
    public void textSetCoercion() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.text = null;\n"
            + "        log(title.text);\n"
            + "        title.text = undefined;\n"
            + "        log(title.text);\n"
            + "        title.text = 42;\n"
            + "        log(title.text);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * Markup in the value is text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b>x</b> &amp;", "1", "3"})
    public void textSetMarkupIsText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.text = '<b>x</b> &amp;';\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "        log(title.firstChild.nodeType);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * Setting again does not add children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"b2", "1"})
    public void textSetTwice() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.text = 'a1';\n"
            + "        title.text = 'b2';\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * The setter on a title that never had a child.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "3"})
    public void textSetCreatedElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.createElement('title');\n"
            + "        title.text = 'x';\n"
            + "        log(title.childNodes.length);\n"
            + "        log(title.firstChild.nodeType);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * textContent replaces all children, text reads the result.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"tc", "1"})
    public void textContentSetThenText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        title.textContent = 'tc';\n"
            + "        log(title.text);\n"
            + "        log(title.childNodes.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * document.title uses all text node children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ab"})
    public void documentTitleMultipleTextNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        log(document.title);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * document.title skips element children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ab"})
    public void documentTitleIgnoresElementChildren() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        var s = document.createElement('span');\n"
            + "        s.appendChild(document.createTextNode('ELEM'));\n"
            + "        title.appendChild(s);\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        log(document.title);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }


    /**
     * document.title follows the text setter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ab", "new"})
    public void documentTitleAfterTextSetter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>a</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var title = document.getElementsByTagName('title')[0];\n"
            + "        title.appendChild(document.createTextNode('b'));\n"
            + "        log(document.title);\n"
            + "        title.text = 'new';\n"
            + "        log(document.title);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }
}