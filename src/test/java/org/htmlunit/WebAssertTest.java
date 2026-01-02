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
package org.htmlunit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebAssert}.
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class WebAssertTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleEquals() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleEquals(page, "foo");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertTitleEquals(page, "bar"));
        assertEquals("Page title 'foo' does not match expected title 'bar'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleContains() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleContains(page, "o");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertTitleContains(page, "a"));
        assertEquals("Page title 'foo' does not contain the expected substring 'a'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleMatches() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleMatches(page, "f..");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertTitleMatches(page, "b.."));
        assertEquals("Page title 'foo' does not match the expected regular expression 'b..'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresent(page, "a");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertElementPresent(page, "b"));
        assertEquals("Expected element with ID 'b' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementPresentByXPath() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresentByXPath(page, "html/body/div");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertElementPresentByXPath(page, "ul"));
        assertEquals("No elements found matching the XPath expression 'ul'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresent(page, "b");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertElementNotPresent(page, "a"));
        assertEquals("Found unexpected element with ID 'a' on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementNotPresentByXPath() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresentByXPath(page, "ul");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertElementNotPresentByXPath(page, "html/body/div"));
        assertEquals("Found 1 unexpected element(s) matching the XPath expression 'html/body/div'.",
                error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextPresent(page, "bar");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertTextPresent(page, "baz"));
        assertEquals("Expected text 'baz' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextPresentInElement() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextPresentInElement(page, "bar", "a");

        AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertTextPresentInElement(page, "baz", "a"));
        assertEquals("Element with ID 'a' does not contain the expected text 'baz'.", error.getMessage());

        error = assertThrows(AssertionError.class, () -> WebAssert.assertTextPresentInElement(page, "bar", "b"));
        assertEquals("Cannot verify text content: element with ID 'b' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresent(page, "baz");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertTextNotPresent(page, "bar"));
        assertEquals("Found unexpected text 'bar' on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextNotPresentInElement() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresentInElement(page, "baz", "a");

        AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertTextNotPresentInElement(page, "bar", "a"));
        assertEquals("Element with ID 'a' contains unexpected text 'bar'.", error.getMessage());

        error = assertThrows(AssertionError.class, () -> WebAssert.assertTextNotPresentInElement(page, "bar", "b"));
        assertEquals("Cannot verify text content: element with ID 'b' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresent(page, "x");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertLinkPresent(page, "z"));
        assertEquals("Expected link with ID 'z' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresent(page, "z");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertLinkNotPresent(page, "x"));
        assertEquals("Found unexpected link with ID 'x' on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkPresentWithText() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresentWithText(page, "r");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertLinkPresentWithText(page, "x"));
        assertEquals("Expected link containing text 'x' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkNotPresentWithText() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresentWithText(page, "x");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertLinkNotPresentWithText(page, "r"));
        assertEquals("Found unexpected link containing text 'r' on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertFormPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormPresent(page, "f");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertFormPresent(page, "x"));
        assertEquals("Expected form with name 'x' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertFormNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormNotPresent(page, "x");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertFormNotPresent(page, "f"));
        assertEquals("Found unexpected form with name 'f' on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputPresent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputPresent(page, "i");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertInputPresent(page, "q"));
        assertEquals("Expected input element with name 'q' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputNotPresent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputNotPresent(page, "q");

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertInputNotPresent(page, "i"));
        assertEquals("Found unexpected input element with name 'i' on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputContainsValue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputContainsValue(page, "i", "x");

        AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertInputContainsValue(page, "i", "z"));
        assertEquals("Input element 'i' has value 'x' but expected 'z'.", error.getMessage());

        error = assertThrows(AssertionError.class, () -> WebAssert.assertInputContainsValue(page, "q", "x"));
        assertEquals("Expected input element with name 'q' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputDoesNotContainValue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputDoesNotContainValue(page, "i", "z");

        AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertInputDoesNotContainValue(page, "i", "x"));
        assertEquals("Input element 'i' has unexpected value 'x'.", error.getMessage());

        error = assertThrows(AssertionError.class, () -> WebAssert.assertInputDoesNotContainValue(page, "q", "x"));
        assertEquals("Expected input element with name 'q' was not found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertAllTabIndexAttributesSet() throws Exception {
        final String html1 = DOCTYPE_HTML + "<html><body><a href='#' tabindex='1'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllTabIndexAttributesSet(page1);

        final String html2 = DOCTYPE_HTML + "<html><body><a href='#'>foo</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertAllTabIndexAttributesSet(page2));
        assertEquals("Invalid tabindex value '' found on element.", error.getMessage());

        final String html3 = DOCTYPE_HTML + "<html><body><a href='#' tabindex='x'>foo</a></body></html>";
        final HtmlPage page3 = loadPage(html3);

        error = assertThrows(AssertionError.class, () -> WebAssert.assertAllTabIndexAttributesSet(page3));
        assertEquals("Invalid tabindex value 'x' found on element.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertAllAccessKeyAttributesUnique() throws Exception {
        final String html1 = DOCTYPE_HTML + "<html><body><a accesskey='k'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllAccessKeyAttributesUnique(page1);

        final String html2 = DOCTYPE_HTML
                + "<html><body><a accesskey='k'>foo</a><a accesskey='k'>bar</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertAllAccessKeyAttributesUnique(page2));
        assertEquals("Duplicate access key 'k' found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertAllIdAttributesUnique() throws Exception {
        final String html1 = DOCTYPE_HTML + "<html><body><a id='k'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllIdAttributesUnique(page1);

        final String html2 = DOCTYPE_HTML + "<html><body><a id='k'>foo</a><a id='k'>bar</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        final AssertionError error = assertThrows(AssertionError.class, () -> WebAssert.assertAllIdAttributesUnique(page2));
        assertEquals("Duplicate element ID 'k' found on the page.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleEqualsNullHandling() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        assertThrows(NullPointerException.class, () -> WebAssert.assertTitleEquals(null, "title"), "Should throw NullPointerException when page is null");

        assertThrows(NullPointerException.class, () -> WebAssert.assertTitleEquals(page, null), "Should throw NullPointerException when title is null");
    }
}
