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

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTitleEquals(page, "bar");
        });
        assertEquals("Actual page title 'foo' does not match expected page title 'bar'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleContains() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleContains(page, "o");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTitleContains(page, "a");
        });
        assertEquals("Page title 'foo' does not contain the substring 'a'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleMatches() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleMatches(page, "f..");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTitleMatches(page, "b..");
        });
        assertEquals("Page title 'foo' does not match the regular expression 'b..'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresent(page, "a");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertElementPresent(page, "b");
        });
        assertEquals("The page does not contain an element with ID 'b'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementPresentByXPath() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresentByXPath(page, "html/body/div");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertElementPresentByXPath(page, "ul");
        });
        assertEquals("The page does not contain any elements matching the XPath expression 'ul'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresent(page, "b");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertElementNotPresent(page, "a");
        });
        assertEquals("The page contains an element with ID 'a' but should not.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementNotPresentByXPath() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresentByXPath(page, "ul");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertElementNotPresentByXPath(page, "html/body/div");
        });
        assertEquals("The page contains 1 element(s) matching the XPath "
                        + "expression 'html/body/div' but should not contain any.",
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

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTextPresent(page, "baz");
        });
        assertEquals("The page does not contain the text 'baz'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextPresentInElement() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextPresentInElement(page, "bar", "a");

        AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTextPresentInElement(page, "baz", "a");
        });
        assertEquals("The element with ID 'a' does not contain the text 'baz'.", error.getMessage());

        error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTextPresentInElement(page, "bar", "b");
        });
        assertEquals("Unable to verify that the element with ID 'b' contains the "
                        + "text 'bar' because the specified element does not exist.",
                    error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresent(page, "baz");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTextNotPresent(page, "bar");
        });
        assertEquals("The page contains the text 'bar' but should not.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextNotPresentInElement() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresentInElement(page, "baz", "a");

        AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTextNotPresentInElement(page, "bar", "a");
        });
        assertEquals("The element with ID 'a' contains the text 'bar' but should not.", error.getMessage());

        error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertTextNotPresentInElement(page, "bar", "b");
        });
        assertEquals("Unable to verify that the element with ID 'b' "
                        + "does not contain the text 'bar' because the specified element does not exist.",
                    error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresent(page, "x");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertLinkPresent(page, "z");
        });
        assertEquals("The page does not contain a link with ID 'z'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresent(page, "z");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertLinkNotPresent(page, "x");
        });
        assertEquals("The page contains a link with ID 'x' but should not.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkPresentWithText() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresentWithText(page, "r");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertLinkPresentWithText(page, "x");
        });
        assertEquals("The page does not contain a link with text 'x'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkNotPresentWithText() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresentWithText(page, "x");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertLinkNotPresentWithText(page, "r");
        });
        assertEquals("The page contains a link with text 'r' but should not.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertFormPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormPresent(page, "f");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertFormPresent(page, "x");
        });
        assertEquals("The page does not contain a form named 'x'.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertFormNotPresent() throws Exception {
        final String html = DOCTYPE_HTML + "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormNotPresent(page, "x");

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertFormNotPresent(page, "f");
        });
        assertEquals("The page contains a form named 'f' but should not.", error.getMessage());
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

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertInputPresent(page, "q");
        });
        assertEquals("Unable to find an input element named 'q'.", error.getMessage());
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

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertInputNotPresent(page, "i");
        });
        assertEquals("Found an input element named 'i' when none was expected.", error.getMessage());
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

        AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertInputContainsValue(page, "i", "z");
        });
        assertEquals("The input element named 'i' contains the value 'x', not the expected value 'z'.",
                error.getMessage());

        error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertInputContainsValue(page, "q", "x");
        });
        assertEquals("Unable to find an input element named 'q'.", error.getMessage());
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

        AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertInputDoesNotContainValue(page, "i", "x");
        });
        assertEquals("The input element named 'i' contains the value 'x', not the expected value 'x'.",
                error.getMessage());

        error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertInputDoesNotContainValue(page, "q", "x");
        });
        assertEquals("Unable to find an input element named 'q'.", error.getMessage());
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

        AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertAllTabIndexAttributesSet(page2);
        });
        assertEquals("Illegal value for tab index: ''.", error.getMessage());

        final String html3 = DOCTYPE_HTML + "<html><body><a href='#' tabindex='x'>foo</a></body></html>";
        final HtmlPage page3 = loadPage(html3);

        error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertAllTabIndexAttributesSet(page3);
        });
        assertEquals("Illegal value for tab index: 'x'.", error.getMessage());
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

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertAllAccessKeyAttributesUnique(page2);
        });
        assertEquals("The access key 'k' is not unique.", error.getMessage());
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

        final AssertionError error = assertThrows(AssertionError.class, () -> {
            WebAssert.assertAllIdAttributesUnique(page2);
        });
        assertEquals("The element ID 'k' is not unique.", error.getMessage());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleEqualsNullHandling() throws Exception {
        final String html = DOCTYPE_HTML + "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        assertThrows(NullPointerException.class, () -> {
            WebAssert.assertTitleEquals(null, "title");
        }, "Should throw NullPointerException when page is null");

        assertThrows(NullPointerException.class, () -> {
            WebAssert.assertTitleEquals(page, null);
        }, "Should throw NullPointerException when title is null");
    }
}
