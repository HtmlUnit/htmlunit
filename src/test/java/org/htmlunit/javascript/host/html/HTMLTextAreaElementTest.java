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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Tests for {@link HTMLTextAreaElement}.
 *
 * @author Mike Bowler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
public class HTMLTextAreaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234", "PoohBear"})
    public void getValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      log(document.form1.textarea1.value);\n"
            + "      document.form1.textarea1.value = 'PoohBear';\n"
            + "      log(document.form1.textarea1.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <p>hello world</p>\n"
            + "  <form name='form1' method='post' >\n"
            + "    <textarea name='textarea1' cols='45' rows='4'>1234</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onChange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='textarea1' onchange='alert(this.value)'></textarea>\n"
            + "    <input name='myButton' type='button' onclick='document.form1.textarea1.value=\"from button\"'>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textarea = driver.findElement(By.name("textarea1"));
        textarea.sendKeys("foo");
        driver.findElement(By.name("myButton")).click();

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Verifies that repeatedly setting a textarea's value via JavaScript reuses
     * the existing child text node instead of removing and recreating it on every
     * assignment.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "TEXTAREA"})
    public void setValuePreservesChildTextNodeIdentity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>initial</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    var firstNode = textarea.childNodes[0];\n"

            + "    textarea.value = 'some text';\n"
            + "    log(textarea.childNodes[0] === firstNode);\n"

            + "    textarea.value = 'other text';\n"
            + "    log(textarea.childNodes[0] === firstNode);\n"
            + "    log(document.form1.elements[0].tagName);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that the text node created by the *first* .value assignment
     * on an initially-empty textarea is then reused (not recreated) on the
     * *second* assignment -- i.e. the "no child yet" -> "has child, reuse it"
     * transition works correctly.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setValueReusesNodeCreatedByPreviousAssignment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'first';\n"
            + "    var node = textarea.childNodes[0];\n"
            + "    textarea.value = 'second';\n"
            + "    log(textarea.childNodes[0] === node);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies the "scan forward past non-text siblings" branch of
     * setTextInternal() -- when a comment node precedes the text node, setting
     * .value should locate and update the existing text node in place (via
     * setData()) rather than disturbing the comment or creating a new node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "2"})
    public void setValueWithLeadingCommentReusesTextNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>initial</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.insertBefore(document.createComment('c'), textarea.firstChild);\n"
            + "    var textNode = textarea.childNodes[1];\n"
            + "    textarea.value = 'changed';\n"
            + "    log(textarea.childNodes[1] === textNode);\n"
            + "    log(textarea.childNodes.length);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies the remove-and-recreate fallback path -- when a textarea
     * has children but none of them is a text node (e.g. only a comment), setting
     * .value should remove the non-text child and end up with exactly one text
     * node containing the new value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "created"})
    public void setValueWithNoExistingTextNodeCreatesOne() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.appendChild(document.createComment('c1'));\n"
            + "    textarea.appendChild(document.createComment('c2'));\n"
            + "    textarea.value = 'created';\n"
            + "    log(textarea.childNodes.length);\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Checks whether setting .value when a textarea has
     * MULTIPLE pre-existing text-node children only updates the first one via
     * setData(), while getText()/.value concatenates all text children. If so,
     * the reported value after assignment would incorrectly include leftover
     * content from the second (untouched) text node. This is a separate concern
     * from the node-identity fix and may expose an independent bug; confirm
     * expected behavior against a real browser before trusting the @Alerts value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foobar", "2", "X", "2"})
    public void setValueWithMultipleExistingTextNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.appendChild(document.createTextNode('foo'));\n"
            + "    textarea.appendChild(document.createTextNode('bar'));\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.childNodes.length);\n"

            + "    textarea.value = 'X';\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.childNodes.length);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Case 5: verifies the selection/caret position lands at the end of the new
     * value after a .value assignment that reuses an existing text node -- to
     * confirm the identity fix didn't inadvertently change the selectionStart/
     * selectionEnd behavior at the end of setTextInternal().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5"})
    public void setValueMovesSelectionToEndAfterReuse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>initial</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'hello';\n"
            + "    log(textarea.selectionStart);\n"
            + "    log(textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Case 4a: pins down WHICH of two existing text-node children actually receives
     * the new value, and which gets cleared, when .value is set on a textarea with
     * multiple pre-existing text nodes. Resolves the ambiguity left open by
     * setValueWithMultipleExistingTextNodes() (which only checked the combined
     * .value and childNodes.length, not per-node data or identity/position).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "bar", "true", "true"})
    public void setValueWithMultipleExistingTextNodes_identifiesTargetNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    var n1 = document.createTextNode('foo');\n"
            + "    var n2 = document.createTextNode('bar');\n"
            + "    textarea.appendChild(n1);\n"
            + "    textarea.appendChild(n2);\n"
            + "    textarea.value = 'X';\n"
            + "    log(n1.data);\n"
            + "    log(n2.data);\n"
            + "    log(textarea.childNodes[0] === n1);\n"
            + "    log(textarea.childNodes[1] === n2);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies the "clear all but one" behavior generalizes to three or
     * more pre-existing text nodes, rather than a rule that only happens to look
     * correct with exactly two (e.g. a pairwise first/last swap).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"X", "3", "a", "b", "c"})
    public void setValueWithThreeExistingTextNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    var n1 = document.createTextNode('a');\n"
            + "    var n2 = document.createTextNode('b');\n"
            + "    var n3 = document.createTextNode('c');\n"
            + "    textarea.appendChild(n1);\n"
            + "    textarea.appendChild(n2);\n"
            + "    textarea.appendChild(n3);\n"
            + "    textarea.value = 'X';\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.childNodes.length);\n"
            + "    log(n1.data);\n"
            + "    log(n2.data);\n"
            + "    log(n3.data);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that text nodes separated by a non-text sibling (a comment)
     * are still all cleared-not-removed, and that the comment itself is left
     * untouched and in its original position.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"X", "3", "3", "8", "3", "foo", "c", "bar"})
    public void setValueWithTextNodesInterleavedWithComment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    var n1 = document.createTextNode('foo');\n"
            + "    var n2 = document.createComment('c');\n"
            + "    var n3 = document.createTextNode('bar');\n"
            + "    textarea.appendChild(n1);\n"
            + "    textarea.appendChild(n2);\n"
            + "    textarea.appendChild(n3);\n"
            + "    textarea.value = 'X';\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.childNodes.length);\n"
            + "    log(textarea.childNodes[0].nodeType);\n"
            + "    log(textarea.childNodes[1].nodeType);\n"
            + "    log(textarea.childNodes[2].nodeType);\n"
            + "    log(n1.data);\n"
            + "    log(n2.data);\n"
            + "    log(n3.data);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies behavior when the new value itself is the empty string,
     * with multiple pre-existing text nodes -- checks whether "clear, don't remove"
     * still holds, or whether browsers collapse/remove nodes differently when the
     * net result is empty.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "2", "foo", "bar"})
    public void setValueEmptyStringWithMultipleExistingTextNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    var n1 = document.createTextNode('foo');\n"
            + "    var n2 = document.createTextNode('bar');\n"
            + "    textarea.appendChild(n1);\n"
            + "    textarea.appendChild(n2);\n"
            + "    textarea.value = '';\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.childNodes.length);\n"
            + "    log(n1.data);\n"
            + "    log(n2.data);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies the still-unconfirmed fallback branch -- setting
     * .value when a textarea has children but NONE of them is a text node (only a
     * comment). Determines whether the comment is removed and replaced by a new
     * text node, or kept alongside a newly-appended text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"created", "1"})
    public void setValueWithOnlyNonTextChildCreatesTextNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.appendChild(document.createComment('c'));\n"
            + "    textarea.value = 'created';\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.childNodes.length);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A textarea parsed with text content should report that content as both
     * its .value and its .defaultValue immediately, with no script interaction
     * required.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello world", "hello world"})
    public void initialValueMatchesParsedContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>hello world</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.defaultValue);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Per the HTML parsing algorithm, a single leading newline immediately
     * after the opening tag is stripped from both .value and .defaultValue.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello", "5"})
    public void initialValueStripsLeadingNewline() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>\nhello</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.value.length);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An empty textarea has an empty .value and .defaultValue, and no
     * childNodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "0"})
    public void initialStateOfEmptyTextarea() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    log(textarea.value);\n"
            + "    log(textarea.defaultValue);\n"
            + "    log(textarea.childNodes.length);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Appending a text node to a textarea whose value has never been set via
     * script (dirty flag still false) should update .value to include the new
     * text, since children-changed-steps re-derive the raw value from child
     * content while clean.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foobar")
    public void appendingTextNodeUpdatesValueWhileClean() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.appendChild(document.createTextNode('bar'));\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Removing a child text node from a still-clean textarea should update
     * .value to reflect the remaining content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void removingTextNodeUpdatesValueWhileClean() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foobar</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.removeChild(textarea.firstChild);\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Directly mutating an existing child text node's data (not replacing
     * the node, just its data) while still clean should also update .value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("changed")
    public void mutatingChildTextNodeDataUpdatesValueWhileClean() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.firstChild.data = 'changed';\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Setting .textContent on a still-clean textarea should update .value,
     * per the spec's explicit "textContent IDL attribute changes value" hook.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bar")
    public void settingTextContentUpdatesValueWhileClean() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.textContent = 'bar';\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Setting .innerHTML on a still-clean textarea replaces its children,
     * which should also trigger the same children-changed sync as any other
     * child mutation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bar")
    public void settingInnerHtmlUpdatesValueWhileClean() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.innerHTML = 'bar';\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Per spec discussion, browsers move the text entry cursor to the end of
     * the control when .textContent changes while the dirty flag is false (the
     * same cursor-repositioning behavior as the .value setter). Confirms whether
     * this applies here too.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void settingTextContentMovesCursorToEndWhileClean() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.textContent = 'hello';\n"
            + "    log(textarea.selectionStart);\n"
            + "    log(textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Once .value has been set via script, the dirty flag is true, so a
     * subsequent child-append no longer affects .value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("X")
    public void settingValueThenAppendingChild_appendHasNoEffect() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    textarea.appendChild(document.createTextNode('bar'));\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Same as settingValueThenAppendingChild_appendHasNoEffect,
     * but removing an existing child after dirtying should also
     * have no effect on .value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("X")
    public void settingValueThenRemovingChild_removeHasNoEffect() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    textarea.removeChild(textarea.firstChild);\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Guard applies to .textContent assignment after dirtying -- once
     * .value has been set, setting .textContent should NOT change .value (only
     * the children, decoupled from the raw value).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("X")
    public void settingValueThenSettingTextContent_textContentHasNoEffectOnValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    textarea.textContent = 'ignored';\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The .value setter itself must not touch the DOM child nodes at all --
     * childNodes.length and content should be exactly what they were before the
     * assignment.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "true", "foo"})
    public void settingValueDoesNotModifyChildNodes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    var node = textarea.firstChild;\n"
            + "    textarea.value = 'X';\n"
            + "    log(textarea.childNodes.length);\n"
            + "    log(textarea.firstChild === node);\n"
            + "    log(textarea.firstChild.data);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The .value setter moves the text entry cursor to the end of the
     * control.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5"})
    public void settingValueMovesCursorToEnd() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>initial</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'hello';\n"
            + "    log(textarea.selectionStart);\n"
            + "    log(textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Per spec discussion, browsers should do nothing (not even move the
     * cursor) when .value is set to the SAME value it already holds. Worth
     * confirming since this is a known point of spec/browser divergence.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void settingValueToSameValue_selectionUnchanged() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>hello</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.selectionStart = 1;\n"
            + "    textarea.selectionEnd = 2;\n"
            + "    textarea.value = 'hello';\n"
            + "    log(textarea.selectionStart);\n"
            + "    log(textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * After dirtying via .value, calling the form's reset() should bring
     * .value back to the current child text content and clear the dirty flag,
     * so that a SUBSEQUENT child mutation is once again reflected in .value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "foobar"})
    public void resetClearsDirtyFlag_laterChildMutationAffectsValueAgain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>foo</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    document.form1.reset();\n"
            + "    log(textarea.value);\n"
            + "    textarea.appendChild(document.createTextNode('bar'));\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * If the child nodes were mutated WHILE the dirty flag was
     * true (so children-changed-steps were suppressed and never synced), reset()
     * should still pick up whatever the children currently contain at the moment
     * of reset, NOT the original text present when the page first loaded.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("original-appended")
    public void resetReflectsCurrentChildContentNotOriginalParsedContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>original</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    textarea.appendChild(document.createTextNode('-appended'));\n"
            + "    document.form1.reset();\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Baseline reset case -- dirty then reset with untouched children should
     * simply restore the original parsed text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("original")
    public void resetRestoresOriginalValueWhenChildrenUntouched() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>original</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    document.form1.reset();\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Reset triggered via a &lt;button type=reset&gt; click (real user-style
     * reset path) should behave identically to a scripted form.reset() call.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("original")
    public void resetViaResetButtonClickBehavesSameAsScriptedReset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>original</textarea>\n"
            + "    <input id='resetBtn' type='reset'>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("resetBtn")).click();
        final String value = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.form1.question.value;");
        assertEquals(getExpectedAlerts()[0], value);
    }

    /**
     * The reset() called on a field whose
     * value was NEVER changed. Unlike the current HtmlInput.setValue() fix
     * (conditional on old-value != new-value), HtmlTextArea.reset() as currently
     * written ALWAYS moves the cursor to the end unconditionally -- it does not
     * go through setText()/setTextInternal() at all, so it has no "did the value
     * actually change" guard. This checks whether that unconditional behavior
     * matches real browsers, or whether real browsers also skip the cursor move
     * when nothing actually changed (in which case HtmlTextArea.reset() would
     * need the same kind of conditional guard added).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5", "original", "5", "5"})
    public void resetOnNeverDirtiedTextarea_selectionPosition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<form id='theForm'>\n"
            + "  <textarea id='theTextarea'>original</textarea>\n"
            + "  <input id='resetBtn' type='reset'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("theTextarea"));

        // click into the field and move the caret, WITHOUT ever changing its
        // value (no sendKeys typing, no .value assignment)
        textarea.click();
        textarea.sendKeys(Keys.ARROW_LEFT, Keys.ARROW_LEFT, Keys.ARROW_LEFT);

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final Long selStartBeforeReset = (Long) js.executeScript(
                "return arguments[0].selectionStart;", textarea);
        final Long selEndBeforeReset = (Long) js.executeScript(
                "return arguments[0].selectionEnd;", textarea);

        driver.findElement(By.id("resetBtn")).click();

        final Long selStartAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionStart;", textarea);
        final Long selEndAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionEnd;", textarea);
        final String valueAfterReset = textarea.getDomProperty("value");

        assertEquals(getExpectedAlerts()[0], String.valueOf(selStartBeforeReset));
        assertEquals(getExpectedAlerts()[1], String.valueOf(selEndBeforeReset));
        assertEquals(getExpectedAlerts()[2], valueAfterReset);
        assertEquals(getExpectedAlerts()[3], String.valueOf(selStartAfterReset));
        assertEquals(getExpectedAlerts()[4], String.valueOf(selEndAfterReset));
    }

    /**
     * Same "never touched" scenario, but with
     * an explicit mid-field selection RANGE set beforehand via script, to check
     * whether reset() collapses/repositions a real selection range too, or only
     * affects a collapsed caret position.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"original", "2", "5"})
    public void resetOnNeverDirtiedTextareaWithExplicitSelectionRange_selectionPosition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<form id='theForm'>\n"
            + "  <textarea id='theTextarea'>original</textarea>\n"
            + "  <input id='resetBtn' type='reset'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("theTextarea"));

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setSelectionRange(2, 5);", textarea);

        driver.findElement(By.id("resetBtn")).click();

        final Long selStartAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionStart;", textarea);
        final Long selEndAfterReset = (Long) js.executeScript(
                "return arguments[0].selectionEnd;", textarea);
        final String valueAfterReset = textarea.getDomProperty("value");

        assertEquals(getExpectedAlerts()[0], valueAfterReset);
        assertEquals(getExpectedAlerts()[1], String.valueOf(selStartAfterReset));
        assertEquals(getExpectedAlerts()[2], String.valueOf(selEndAfterReset));
    }

    /**
     * The .defaultValue always reflects the current child text content,
     * regardless of the dirty flag -- even after .value has been dirtied and
     * decoupled from the children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"original", "X"})
    public void defaultValueReflectsChildContentRegardlessOfDirtyFlag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>original</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    log(textarea.defaultValue);\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Setting .defaultValue mutates the child nodes (it's specified in terms
     * of textContent), which in turn triggers children-changed-steps -- so while
     * still CLEAN, setting .defaultValue should also update .value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("newDefault")
    public void settingDefaultValueWhileClean_alsoUpdatesValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>original</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.defaultValue = 'newDefault';\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Same operation as E2, but performed AFTER .value has already been
     * dirtied -- setting .defaultValue should still update the children (so
     * .defaultValue itself changes) but must NOT affect the already-dirtied
     * .value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"newDefault", "X"})
    public void settingDefaultValueWhileDirty_doesNotUpdateValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>original</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    textarea.defaultValue = 'newDefault';\n"
            + "    log(textarea.defaultValue);\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Per spec discussion, textContent/defaultValue/value
     * "should" all agree when a textarea has nested-element children containing
     * text (rather than direct text-node children) -- but real browsers are
     * known to disagree with the spec AND with each other on this exact case.
     * Run this primarily to document actual current behavior across browsers,
     * not to assert a single "correct" answer.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TEXT", "", ""})
    public void nestedElementTextContentInteraction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var t = document.createElement('textarea');\n"
            + "    var span = document.createElement('span');\n"
            + "    span.appendChild(document.createTextNode('TEXT'));\n"
            + "    t.appendChild(span);\n"
            + "    document.body.appendChild(t);\n"
            + "    log(t.textContent);\n"
            + "    log(t.defaultValue);\n"
            + "    log(t.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Even though a same-value assignment leaves the selection
     * untouched, the dirty flag must still flip to true (per spec: "set the
     * element's dirty value flag to true" happens unconditionally, only the
     * cursor move is conditional). Proven indirectly: if the flag were still
     * false, a later child mutation would still be reflected in .value; if the
     * flag correctly flipped true, the later child mutation must be ignored.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void settingValueToSameValue_stillSetsDirtyFlag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>hello</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'hello';\n"
            + "    textarea.appendChild(document.createTextNode(' world'));\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Selection must be preserved across MULTIPLE consecutive same-value
     * assignments, not just the first one -- confirms the comparison is always
     * against the current value at the time of each call, not just a one-time
     * check.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2"})
    public void settingValueToSameValueTwice_selectionPreservedBothTimes() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>hello</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'hello';\n"
            + "    textarea.selectionStart = 1;\n"
            + "    textarea.selectionEnd = 2;\n"
            + "    textarea.value = 'hello';\n"
            + "    log(textarea.selectionStart);\n"
            + "    log(textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The "old value" comparison must be against the CURRENT raw value, not the
     * page's originally-parsed text. After dirtying to 'X', setting .value back
     * to the ORIGINAL text ('hello') is a change relative to the current raw
     * value ('X'), so the cursor MUST move -- even though 'hello' matches what
     * was there at parse time.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"5", "5"})
    public void settingValueBackToOriginalText_cursorMovesBecauseCurrentValueDiffers() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>hello</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = 'X';\n"
            + "    textarea.selectionStart = 0;\n"
            + "    textarea.selectionEnd = 0;\n"
            + "    textarea.value = 'hello';\n"
            + "    log(textarea.selectionStart);\n"
            + "    log(textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Edge case: assigning the empty string to an already-empty value should
     * also be treated as "no change" -- no cursor move, but still dirties the
     * flag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void settingEmptyValueToAlreadyEmptyValue_selectionUnchangedButDirty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'></textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.value = '';\n"
            + "    textarea.appendChild(document.createTextNode('late'));\n"
            + "    log(textarea.value);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Confirms the ordinary changing-value path still moves the cursor to the
     * end -- guards against the new conditional accidentally suppressing the
     * cursor move for genuine changes, not just same-value assignments.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7", "7"})
    public void settingValueToDifferentValue_cursorStillMovesToEnd() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form name='form1'>\n"
            + "    <textarea name='question'>hello</textarea>\n"
            + "  </form>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.form1.question;\n"
            + "    textarea.selectionStart = 0;\n"
            + "    textarea.selectionEnd = 0;\n"
            + "    textarea.value = 'goodbye';\n"
            + "    log(textarea.selectionStart);\n"
            + "    log(textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Confirms that typing into a textarea (real keyboard simulation, not a
     * scripted .value assignment) updates .value but leaves .textContent /
     * childNodes untouched -- the JS-observable version of the typingAndClone()
     * Java-level test, verifying the same value/textContent split holds for real
     * browsers, not just HtmlUnit's internal model.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4711", "", "0"})
    public void typingUpdatesValueButNotTextContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='foo'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("foo"));
        textarea.click();
        textarea.sendKeys("4711");

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String value = (String) js.executeScript("return arguments[0].value;", textarea);
        final String textContent = (String) js.executeScript("return arguments[0].textContent;", textarea);
        final Long childNodeCount = (Long) js.executeScript("return arguments[0].childNodes.length;", textarea);

        assertEquals(getExpectedAlerts()[0], value);
        assertEquals(getExpectedAlerts()[1], textContent);
        assertEquals(getExpectedAlerts()[2], String.valueOf(childNodeCount));
    }

    /**
     * Companion case: same real-keyboard-typing scenario, but starting from a
     * textarea that already has parsed content -- confirms typed characters
     * don't get appended to (or otherwise disturb) the existing child text node,
     * even though .value correctly reflects the combined typed result.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-typed", "seed"})
    public void typingIntoNonEmptyTextareaLeavesOriginalChildTextNodeUnchanged() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='foo'>seed</textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("foo"));
        textarea.click();
        // move caret to end before typing, since click() alone doesn't guarantee position
        textarea.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        textarea.sendKeys("-typed");

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String value = (String) js.executeScript("return arguments[0].value;", textarea);
        final String textContent = (String) js.executeScript("return arguments[0].textContent;", textarea);

        assertEquals(getExpectedAlerts()[0], value);
        assertEquals(getExpectedAlerts()[1], textContent);
    }

    /**
     * After typing (which dirties .value but never touches the DOM
     * children), clicking a reset button should bring .value back in sync with
     * .textContent -- since reset() clears the dirty flag and .textContent was
     * never disturbed by typing in the first place, the two must now be equal.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-typed", "seed", "seed"})
    public void resetAfterTyping_valueMatchesTextContentAgain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='foo'>seed</textarea>\n"
            + "    <input id='resetBtn' type='reset'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("foo"));
        textarea.click();
        textarea.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        textarea.sendKeys("-typed");

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String valueBeforeReset = (String) js.executeScript("return arguments[0].value;", textarea);
        final String textContentBeforeReset =
                (String) js.executeScript("return arguments[0].textContent;", textarea);

        driver.findElement(By.id("resetBtn")).click();

        final String valueAfterReset = (String) js.executeScript("return arguments[0].value;", textarea);
        final String textContentAfterReset =
                (String) js.executeScript("return arguments[0].textContent;", textarea);

        assertEquals(getExpectedAlerts()[0], valueBeforeReset);
        assertEquals(getExpectedAlerts()[1], textContentBeforeReset);
        assertEquals(getExpectedAlerts()[2], valueAfterReset);
        assertEquals(textContentAfterReset, valueAfterReset);
    }

    /**
     * If the DOM children are mutated by script WHILE .value is dirty
     * (typing already happened, so children-changed effects on .value are
     * suppressed), a subsequent reset() must pick up whatever the children
     * CURRENTLY contain -- including that mutation -- not the page's original
     * parsed text. Confirmed here by asserting .value equals the LIVE
     * .textContent (which already reflects the mutation) immediately after the
     * reset button click, using real typing + a real click rather than only
     * scripted reset().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-typed", "seed-appended", "seed-appended"})
    public void resetAfterTypingAndChildMutationWhileDirty_valueMatchesCurrentTextContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='foo'>seed</textarea>\n"
            + "    <input id='resetBtn' type='reset'>\n"
            + "  </form>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("foo"));
        textarea.click();
        textarea.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        textarea.sendKeys("-typed");   // dirties .value; children still just "seed"

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        // mutate the children directly WHILE .value is dirty -- must have no
        // effect on .value yet, but DOES change what reset() should pick up
        js.executeScript("arguments[0].appendChild(document.createTextNode('-appended'));", textarea);

        final String valueBeforeReset = (String) js.executeScript("return arguments[0].value;", textarea);
        final String textContentBeforeReset =
                (String) js.executeScript("return arguments[0].textContent;", textarea);

        driver.findElement(By.id("resetBtn")).click();

        final String valueAfterReset = (String) js.executeScript("return arguments[0].value;", textarea);

        assertEquals(getExpectedAlerts()[0], valueBeforeReset);
        assertEquals(getExpectedAlerts()[1], textContentBeforeReset);
        assertEquals(getExpectedAlerts()[2], valueAfterReset);
    }

    /**
     * After dirtying .value via real typing (so it has diverged from
     * the child text content), a deep clone must preserve BOTH the dirtied value
     * AND its decoupling from textContent -- not silently revert to the (cloned)
     * children's content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-typed", "seed"})
    public void cloneNodeAfterTyping_cloneRetainsDirtiedValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='foo'>seed</textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("foo"));
        textarea.click();
        textarea.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        textarea.sendKeys("-typed");

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String cloneValue = (String) js.executeScript(
                "return arguments[0].cloneNode(true).value;", textarea);
        final String cloneTextContent = (String) js.executeScript(
                "return arguments[0].cloneNode(true).textContent;", textarea);

        assertEquals(getExpectedAlerts()[0], cloneValue);
        assertEquals(getExpectedAlerts()[1], cloneTextContent);
    }

    /**
     * A deep clone's children must be independent of the original's -- mutating
     * the original's children AFTER cloning must not affect the clone's
     * .textContent or (for a still-clean clone) its .value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"changed", "seed"})
    public void cloneNodeDeep_childrenAreIndependentOfOriginal() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='foo'>seed</textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("foo"));

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "var c = arguments[0].cloneNode(true);"
            + "c.id = 'clone';"
            + "document.body.appendChild(c);",
            textarea);

        // mutate the ORIGINAL's children only, after cloning
        js.executeScript(
            "arguments[0].removeChild(arguments[0].firstChild);"
            + "arguments[0].appendChild(document.createTextNode('changed'));",
            textarea);

        final String originalValue = (String) js.executeScript("return arguments[0].value;", textarea);
        final String cloneValue = (String) js.executeScript(
                "return document.getElementById('clone').value;");

        assertEquals(getExpectedAlerts()[0], originalValue);
        assertEquals(getExpectedAlerts()[1], cloneValue);
    }

    /**
     * A shallow clone (deep=false) doesn't copy children at all -- but since the
     * dirtied value lives on the element itself, not in the DOM tree, a shallow
     * clone of an already-dirtied textarea must still preserve the dirtied
     * value, even though its childNodes/textContent end up empty.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-typed", ""})
    public void cloneNodeShallowAfterTyping_stillRetainsDirtiedValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='foo'>seed</textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textarea = driver.findElement(By.id("foo"));
        textarea.click();
        textarea.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        textarea.sendKeys("-typed");

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String cloneValue = (String) js.executeScript(
                "return arguments[0].cloneNode(false).value;", textarea);
        final String cloneTextContent = (String) js.executeScript(
                "return arguments[0].cloneNode(false).textContent;", textarea);

        assertEquals(getExpectedAlerts()[0], cloneValue);
        assertEquals(getExpectedAlerts()[1], cloneTextContent);
    }

    /**
     * After cloning a still-CLEAN textarea and attaching the clone to the
     * document, typing independently into the original and the clone (real
     * keyboard input on each) must not cross-contaminate either one's value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"original-text", "clone-text"})
    public void cloneNodeThenIndependentTyping_noCrossContamination() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='foo'></textarea>\n"
            + "  <script>\n"
            + "    var c = document.getElementById('foo').cloneNode(true);\n"
            + "    c.id = 'clone';\n"
            + "    document.body.appendChild(c);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement original = driver.findElement(By.id("foo"));
        final WebElement clone = driver.findElement(By.id("clone"));

        original.click();
        original.sendKeys("original-text");
        clone.click();
        clone.sendKeys("clone-text");

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String originalValue = (String) js.executeScript("return arguments[0].value;", original);
        final String cloneValue = (String) js.executeScript("return arguments[0].value;", clone);

        assertEquals(getExpectedAlerts()[0], originalValue);
        assertEquals(getExpectedAlerts()[1], cloneValue);
    }

    /**
     * Resetting a clone (via a real click on its OWN form's reset button) must
     * only affect the clone -- the original, sitting in a separate form, must be
     * untouched.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"seed-original-dirtied", "seed"})
    public void cloneNodeThenResetOnCloneOnly_doesNotAffectOriginal() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='foo'>seed</textarea>\n"
            + "    <input id='resetBtn1' type='reset'>\n"
            + "  </form>\n"
            + "  <form id='form2'>\n"
            + "    <input id='resetBtn2' type='reset'>\n"
            + "  </form>\n"
            + "  <script>\n"
            + "    var c = document.getElementById('foo').cloneNode(true);\n"
            + "    c.id = 'clone';\n"
            + "    document.getElementById('form2').appendChild(c);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement original = driver.findElement(By.id("foo"));
        final WebElement clone = driver.findElement(By.id("clone"));

        original.click();
        original.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        original.sendKeys("-original-dirtied");

        clone.click();
        clone.sendKeys(Keys.chord(Keys.CONTROL, Keys.END));
        clone.sendKeys("-clone-dirtied");

        // reset only form2, which contains the clone
        driver.findElement(By.id("resetBtn2")).click();

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final String originalValue = (String) js.executeScript("return arguments[0].value;", original);
        final String cloneValue = (String) js.executeScript("return arguments[0].value;", clone);

        assertEquals(getExpectedAlerts()[0], originalValue);
        assertEquals(getExpectedAlerts()[1], cloneValue);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"11", "0"})
    public void textLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='myTextArea'></textarea>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    textarea.value = 'hello there';\n"
            + "    log(textarea.textLength);\n"
            + "    textarea.value = '';\n"
            + "    log(textarea.textLength);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"0,0", "11,11", "3,11", "3,10", "7,7"})
    public void selection() throws Exception {
        selection(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"0,0", "11,11", "11,11", "11,11", "7,7"})
    public void selection_outOfBounds() throws Exception {
        selection(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"0,0", "11,11", "10,11", "5,5", "7,7"})
    public void selection_reverseOrder() throws Exception {
        selection(10, 5);
    }

    private void selection(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='myTextArea'></textarea>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var textarea = document.getElementById('myTextArea');\n"
            + "    log(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.value = 'Hello there';\n"
            + "    log(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionStart = " + selectionStart + ";\n"
            + "    log(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.selectionEnd = " + selectionEnd + ";\n"
            + "    log(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "    textarea.value = 'nothing';\n"
            + "    log(textarea.selectionStart + ',' + textarea.selectionEnd);\n"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("no")
    public void doScroll() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        if(t.doScroll) {\n"
            + "          log('yes');\n"
            + "          t.doScroll();\n"
            + "          t.doScroll('down');\n"
            + "        } else {\n"
            + "          log('no');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><textarea id='t'>abc</textarea></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that the new line immediately following opening tag is ignored.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello\\nworld\\n")
    public void value_ignoreFirstNewLine() throws Exception {
        value("\nHello\nworld\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\\s\\nHello\\nworld\\n")
    public void value_spaceBeforeFirstNewLine() throws Exception {
        value(" \nHello\nworld\n");
    }

    private void value(final String textAreaBody) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "    function doTest() {\n"
            + "      log(document.form1.textarea1.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "  <form name='form1' method='post' >\n"
            + "    <textarea name='textarea1'>" + textAreaBody + "</textarea>\n"
            + "    </textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"\\sfoo\\s\\n\\sbar\\s", "\\sfoo\\s\\n\\sbar\\s"})
    public void defaultValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "    function test() {\n"
            + "      var t = document.getElementById('textArea');\n"
            + "      log(t.defaultValue);\n"
            + "      log(t.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='textArea'>\n foo \n bar </textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void readOnly() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('textArea');\n"
            + "    log(t.readOnly);\n"
            + "    t.readOnly = false;\n"
            + "    log(t.readOnly);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='textArea' readonly>\n foo \n bar </textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true", ""})
    public void readOnlySet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('textArea');\n"
            + "    log(t.readOnly);\n"
            + "    t.readOnly = true;\n"
            + "    log(t.readOnly);\n"
            + "    log(t.getAttribute('readonly'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form id='form1'>\n"
            + "    <textarea id='textArea'>\n foo \n bar </textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "A", "a", "A", "a8", "8Afoo", "8", "@"})
    public void accessKey() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='a1'>a1</textarea>\n"
            + "  <textarea id='a2' accesskey='A'>a2</textarea>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "    log(a1.accessKey);\n"
            + "    log(a2.accessKey);\n"

            + "    a1.accessKey = 'a';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = 'A';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = 'a8';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = '8Afoo';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = '8';\n"
            + "    log(a1.accessKey);\n"

            + "    a1.accessKey = '@';\n"
            + "    log(a1.accessKey);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"20", "5", "8", "4", "20", "20", "20", "3"})
    public void cols() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setCols(e, value) {\n"
            + "    try {\n"
            + "      e.cols = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <textarea id='a1'>a1</textarea>\n"
            + "  <textarea id='a2' cols='5'>a2</textarea>\n"

            + "  <script>\n"
            + "    var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "    log(a1.cols);\n"
            + "    log(a2.cols);\n"

            + "    setCols(a1, '8');\n"
            + "    log(a1.cols);\n"

            + "    setCols(a1, 4);\n"
            + "    log(a1.cols);\n"

            + "    setCols(a1, 'a');\n"
            + "    log(a1.cols);\n"

            + "    setCols(a1, '');\n"
            + "    log(a1.cols);\n"

            + "    setCols(a1, -1);\n"
            + "    log(a1.cols);\n"

            + "    setCols(a1, 3.4);\n"
            + "    log(a1.cols);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "5", "8", "4", "2", "2", "2", "3"})
    public void rows() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setRows(e, value) {\n"
            + "    try {\n"
            + "      e.rows = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"

            + "<body>\n"
            + "  <textarea id='a1'>a1</textarea>\n"
            + "  <textarea id='a2' rows='5'>a2</textarea>\n"

            + "  <script>\n"
            + "    var a1 = document.getElementById('a1'), a2 = document.getElementById('a2');\n"
            + "    log(a1.rows);\n"
            + "    log(a2.rows);\n"

            + "    setRows(a1, '8');\n"
            + "    log(a1.rows);\n"

            + "    setRows(a1, 4);\n"
            + "    log(a1.rows);\n"

            + "    setRows(a1, 'a');\n"
            + "    log(a1.rows);\n"

            + "    setRows(a1, '');\n"
            + "    log(a1.rows);\n"

            + "    setRows(a1, -1);\n"
            + "    log(a1.rows);\n"

            + "    setRows(a1, 3.4);\n"
            + "    log(a1.rows);\n"
            + "  </script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"9", "9", "2", "7"})
    public void selectionRange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var ta = document.getElementById('myInput');\n"
            + "      ta.setSelectionRange(15, 15);\n"
            + "      log(ta.selectionStart);\n"
            + "      log(ta.selectionEnd);\n"
            + "      ta.setSelectionRange(2, 7);\n"
            + "      log(ta.selectionStart);\n"
            + "      log(ta.selectionEnd);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='myInput'>some test</textarea>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"test", "4", "42", "2", "[object HTMLTextAreaElement]", "28"})
    public void getAttributeAndSetValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'test';\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"

            + "        t.value = 42;\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"

            + "        t.value = document.getElementById('t');\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea id='t'>abc</textarea>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "4", "", "0"})
    public void getAttributeAndSetValueNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 'null';\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"

            + "        t.value = null;\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea id='t'>abc</textarea>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"12", "2", "[object HTMLTextAreaElement]", "28"})
    public void getAttributeAndSetValueOther() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var t = document.getElementById('t');\n"
            + "        t.value = 12;\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"

            + "        t.value = t;\n"
            + "        log(t.value);\n"
            + "        if (t.value != null)\n"
            + "          log(t.value.length);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea id='t'>abc</textarea>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "null", "32", "32", "-1", "ms"})
    public void maxLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.form1.textarea1.maxLength);\n"
            + "      log(document.form1.textarea1.getAttribute('maxLength'));\n"
            + "      log(document.form1.textarea2.maxLength);\n"
            + "      log(document.form1.textarea2.getAttribute('maxLength'));\n"
            + "      log(document.form1.textarea3.maxLength);\n"
            + "      log(document.form1.textarea3.getAttribute('maxLength'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='form1' method='post'>\n"
            + "    <textarea name='textarea1'></textarea>\n"
            + "    <textarea name='textarea2' maxLength='32'></textarea>\n"
            + "    <textarea name='textarea3' maxLength='ms'></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "null", "32", "32", "-1", "ms"})
    public void minLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log(document.form1.textarea1.minLength);\n"
            + "      log(document.form1.textarea1.getAttribute('minLength'));\n"
            + "      log(document.form1.textarea2.minLength);\n"
            + "      log(document.form1.textarea2.getAttribute('minLength'));\n"
            + "      log(document.form1.textarea3.minLength);\n"
            + "      log(document.form1.textarea3.getAttribute('minLength'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='form1' method='post'>\n"
            + "    <textarea name='textarea1'></textarea>\n"
            + "    <textarea name='textarea2' minLength='32'></textarea>\n"
            + "    <textarea name='textarea3' minLength='ms'></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"10", "10", "IndexSizeError/DOMException", "10", "10", "0", "0"})
    public void setMaxLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function setMaxLength(length){\n"
            + "      try {\n"
            + "        document.form1.textarea1.maxLength = length;\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <form name='form1' method='post' >\n"
            + "    <textarea id='textarea1'></textarea>\n"
            + "    <script>\n"
            + "      var a = document.getElementById('textarea1');\n"

            + "      setMaxLength(10);\n"
            + "      log(a.maxLength);\n"
            + "      log(a.getAttribute('maxLength'));\n"

            + "      setMaxLength(-1);\n"
            + "      log(a.maxLength);\n"
            + "      log(a.getAttribute('maxLength'));\n"

            + "      setMaxLength('abc');\n"
            + "      log(a.maxLength);\n"
            + "      log(a.getAttribute('maxLength'));\n"

            + "    </script>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLFormElement]")
    public void form() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <textarea id='a'></textarea>\n"
            + "  </form>"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    log(document.getElementById('a').form);\n"
            + "  </script>"
            + "</body>"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverTextarea() throws Exception {
        shutDownAll();
        mouseOver("<textarea id='tester' onmouseover='dumpEvent(event);'>HtmlUnit</textarea>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("mouse over [tester]")
    public void mouseOverTextareaDisabled() throws Exception {
        shutDownAll();
        mouseOver("<textarea id='tester' onmouseover='dumpEvent(event);' disabled >HtmlUnit</textarea>");
    }

    private void mouseOver(final String element) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "    function dumpEvent(event) {\n"
            + "      // target\n"
            + "      var eTarget;\n"
            + "      if (event.target) {\n"
            + "        eTarget = event.target;\n"
            + "      } else if (event.srcElement) {\n"
            + "        eTarget = event.srcElement;\n"
            + "      }\n"
            + "      // defeat Safari bug\n"
            + "      if (eTarget.nodeType == 3) {\n"
            + "        eTarget = eTarget.parentNode;\n"
            + "      }\n"
            + "      var msg = 'mouse over';\n"
            + "      if (eTarget.name) {\n"
            + "        msg = msg + ' [' + eTarget.name + ']';\n"
            + "      } else {\n"
            + "        msg = msg + ' [' + eTarget.id + ']';\n"
            + "      }\n"
            + "      document.title += msg;\n"
            + "    }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    " + element + "\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.id("tester")));
        actions.perform();

        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit", "</> htmx rocks!"})
    public void innerHtml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>Page Title</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var textarea = document.getElementsByTagName('textarea')[0];\n"
            + "        log(textarea.value);\n"
            + "        textarea.innerHTML = '</> htmx rocks!';\n"
            + "        log(textarea.value);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>"
            + "    <textarea>HtmlUnit</textarea>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"HtmlUnit", "<div>htmx rocks</div>"})
    public void innerHtmlTag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>Page Title</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var textarea = document.getElementsByTagName('textarea')[0];\n"
            + "        log(textarea.value);\n"
            + "        textarea.innerHTML = '<div>htmx rocks</div>';\n"
            + "        log(textarea.value);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea>HtmlUnit</textarea>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "</> htmx rocks!"})
    public void innerHtmlEscaping() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <title>Page Title</title>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var textarea = document.getElementsByTagName('textarea')[0];\n"
            + "        log(textarea.value);\n"
            + "        textarea.innerHTML = '&lt;/> htmx rocks!';\n"
            + "        log(textarea.value);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <textarea></textarea>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i2').willValidate);\n"
                + "      log(document.getElementById('i3').willValidate);\n"
                + "      log(document.getElementById('i4').willValidate);\n"
                + "      log(document.getElementById('i5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <textarea id='i1'>button</textarea>"
                + "    <textarea id='i2' disabled></textarea>"
                + "    <textarea id='i3' hidden></textarea>"
                + "    <textarea id='i4' readonly></textarea>"
                + "    <textarea id='i5' style='display: none'></textarea>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An enabled, editable textarea with no custom validity issue
     * reports checkValidity() true and an empty validationMessage.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", ""})
    public void checkValidityWithoutCustomValidity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    log(t.checkValidity());\n"
            + "    log(t.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t'>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An enabled, editable textarea with a custom validity message set must
     * report checkValidity() false.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "false", "false"})
    public void setCustomValidityOnEditableTextarea_isInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.setCustomValidity('some error');\n"
            + "    log(t.willValidate);\n"
            + "    log(t.validity.customError);\n"
            + "    log(t.validity.valid);\n"
            + "    log(t.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t'>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A DISABLED textarea with a custom validity message must still
     * report checkValidity() true -- disabled bars it from constraint
     * validation entirely, so the custom error must not surface through
     * checkValidity(), even though willValidate is already known to be false
     * for this case.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void setCustomValidityOnDisabledTextarea_notInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.setCustomValidity('some error');\n"
            + "    log(t.willValidate);\n"
            + "    log(t.validity.customError);\n"
            + "    log(t.validity.valid);\n"
            + "    log(t.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t' disabled>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Same as above but for READONLY instead of disabled -- the
     * existing willValidate() test already confirms readonly returns
     * willValidate=false.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "true"})
    public void setCustomValidityOnReadonlyTextarea_notInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.setCustomValidity('some error');\n"
            + "    log(t.willValidate);\n"
            + "    log(t.validity.customError);\n"
            + "    log(t.validity.valid);\n"
            + "    log(t.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t' readonly>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Clearing a previously-set custom validity message (empty string) must
     * restore validity for an editable textarea.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "true"})
    public void clearCustomValidity_restoresValid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.setCustomValidity('some error');\n"
            + "    log(t.validity.valid);\n"
            + "    t.setCustomValidity('');\n"
            + "    log(t.validity.customError);\n"
            + "    log(t.validity.valid);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t'>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The validationMessage should reflect the custom validity message for a
     * validation-participating (editable) textarea, and should be empty for a
     * barred-from-validation (disabled) one, regardless of a custom message
     * being set.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"editable error", ""})
    public void validationMessageReflectsCustomValidityWhereApplicable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var editable = document.getElementById('editable');\n"
            + "    var disabled = document.getElementById('disabled');\n"
            + "    editable.setCustomValidity('editable error');\n"
            + "    disabled.setCustomValidity('disabled error');\n"
            + "    log(editable.validationMessage);\n"
            + "    log(disabled.validationMessage);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='editable'>content</textarea>\n"
            + "    <textarea id='disabled' disabled>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false"})
    public void reportValidityMatchesCheckValidity() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.setCustomValidity('some error');\n"
            + "    log(t.checkValidity());\n"
            + "    log(t.reportValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t'>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A required, empty textarea is genuinely invalid (valueMissing)
     * when editable, but must report checkValidity() true if ALSO disabled.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void requiredEmptyDisabledTextarea_checkValidityTrue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var editable = document.getElementById('editable');\n"
            + "    var disabled = document.getElementById('disabled');\n"
            + "    log(editable.checkValidity());\n"
            + "    log(disabled.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='editable' required></textarea>\n"
            + "    <textarea id='disabled' required disabled></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"invalid fired", "false"})
    public void textareaCheckValidityFiresInvalidEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.addEventListener('invalid', function() { log('invalid fired'); });\n"
            + "    log(t.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t' required></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void textareaCheckValidityDoesNotFireInvalidWhenActuallyValid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.addEventListener('invalid', function() { log('unexpected invalid fired'); });\n"
            + "    log(t.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t' required>content</textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void textareaCheckValidityDoesNotFireInvalidOnDisabledControl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.addEventListener('invalid', function() { log('unexpected invalid fired'); });\n"
            + "    log(t.checkValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t' required disabled></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"invalid fired", "false"})
    public void textareaReportValidityFiresInvalidEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    t.addEventListener('invalid', function() { log('invalid fired'); });\n"
            + "    log(t.reportValidity());\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form>\n"
            + "    <textarea id='t' required></textarea>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
