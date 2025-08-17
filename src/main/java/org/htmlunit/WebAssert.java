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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;

/**
 * Utility class which contains standard assertions for HTML pages.
 *
 * <p>This class provides a collection of static assertion methods for testing
 * HTML page content, structure, and behavior. All assertion methods throw
 * {@link AssertionError} when the expected condition is not met.</p>
 *
 * <p>Common use cases include:</p>
 * <ul>
 *   <li>Verifying page titles and content</li>
 *   <li>Checking for presence/absence of elements</li>
 *   <li>Validating form inputs and links</li>
 *   <li>Ensuring accessibility attributes are properly set</li>
 * </ul>
 *
 * @author Daniel Gredler
 * @author Mike Bowler
 * @author Ahmed Ashour
 * @author Ronald Broöö
 */
public final class WebAssert {

    /**
     * Private to prevent instantiation.
     */
    private WebAssert() {
        // Empty.
    }

    /**
     * Verifies that the specified page's title equals the specified expected title.
     *
     * @param page the page to check
     * @param title the expected title
     * @throws AssertionError if the page title does not match the expected title
     * @throws NullPointerException if page or title is null
     */
    public static void assertTitleEquals(final HtmlPage page, final String title) {
        final String s = page.getTitleText();
        if (!title.equals(s)) {
            final String msg = "Actual page title '" + s + "' does not match expected page title '" + title + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page's title contains the specified substring.
     *
     * @param page the page to check
     * @param titlePortion the substring which the page title is expected to contain
     * @throws AssertionError if the page title does not contain the substring
     * @throws NullPointerException if page or titlePortion is null
     */
    public static void assertTitleContains(final HtmlPage page, final String titlePortion) {
        final String s = page.getTitleText();
        if (!s.contains(titlePortion)) {
            final String msg = "Page title '" + s + "' does not contain the substring '" + titlePortion + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page's title matches the specified regular expression.
     *
     * @param page the page to check
     * @param regex the regular expression that the page title is expected to match
     * @throws AssertionError if the page title does not match the regular expression
     * @throws NullPointerException if page or regex is null
     */
    public static void assertTitleMatches(final HtmlPage page, final String regex) {
        final String s = page.getTitleText();
        if (!s.matches(regex)) {
            final String msg = "Page title '" + s + "' does not match the regular expression '" + regex + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page contains an element with the specified ID.
     *
     * @param page the page to check
     * @param id the ID of an element expected in the page
     * @throws AssertionError if no element with the specified ID is found
     * @throws NullPointerException if page or id is null
     */
    public static void assertElementPresent(final HtmlPage page, final String id) {
        try {
            page.getHtmlElementById(id);
        }
        catch (final ElementNotFoundException e) {
            final String msg = "The page does not contain an element with ID '" + id + "'.";
            throw new AssertionError(msg, e);
        }
    }

    /**
     * Verifies that the specified page contains an element matching the specified XPath expression.
     *
     * <p><b>Example usage:</b></p>
     * <pre>{@code
     * WebAssert.assertElementPresentByXPath(page, "//div[@class='error']");
     * WebAssert.assertElementPresentByXPath(page, "//input[@type='submit' and @value='Login']");
     * }</pre>
     *
     * @param page the page to check
     * @param xpath the XPath expression which is expected to match an element in the page
     * @throws AssertionError if no elements match the XPath expression
     * @throws NullPointerException if page or xpath is null
     */
    public static void assertElementPresentByXPath(final HtmlPage page, final String xpath) {
        final List<?> elements = page.getByXPath(xpath);
        if (elements.isEmpty()) {
            final String msg = "The page does not contain any elements matching the XPath expression '" + xpath
                            + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page does not contain an element with the specified ID.
     *
     * @param page the page to check
     * @param id the ID of an element which expected to not exist on the page
     * @throws AssertionError if an element with the specified ID is found
     * @throws NullPointerException if page or id is null
     */
    public static void assertElementNotPresent(final HtmlPage page, final String id) {
        try {
            page.getHtmlElementById(id);
        }
        catch (final ElementNotFoundException e) {
            return;
        }
        final String msg = "The page contains an element with ID '" + id + "' but should not.";
        throw new AssertionError(msg);
    }

    /**
     * Verifies that the specified page does not contain an element matching the specified XPath
     * expression.
     *
     * @param page the page to check
     * @param xpath the XPath expression which is expected to not match an element in the page
     * @throws AssertionError if any elements match the XPath expression
     */
    public static void assertElementNotPresentByXPath(final HtmlPage page, final String xpath) {
        final List<?> elements = page.getByXPath(xpath);
        if (!elements.isEmpty()) {
            final String msg = "The page contains " + elements.size()
                                    + " element(s) matching the XPath expression '"
                                    + xpath + "' but should not contain any.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page contains the specified text.
     *
     * @param page the page to check
     * @param text the text to check for
     * @throws AssertionError if the page does not contain the specified text
     * @throws NullPointerException if page or text is null
     */
    public static void assertTextPresent(final HtmlPage page, final String text) {
        if (!page.asNormalizedText().contains(text)) {
            final String msg = "The page does not contain the text '" + text + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the element on the specified page which matches the specified ID contains the
     * specified text.
     *
     * @param page the page to check
     * @param text the text to check for
     * @param id the ID of the element which is expected to contain the specified text
     * @throws AssertionError if the element does not contain the specified text
     * @throws ElementNotFoundException if no element with the specified ID exists
     * @throws NullPointerException if any parameter is null
     */
    public static void assertTextPresentInElement(final HtmlPage page, final String text, final String id) {
        try {
            final HtmlElement element = page.getHtmlElementById(id);
            if (!element.asNormalizedText().contains(text)) {
                final String msg = "The element with ID '" + id + "' does not contain the text '" + text + "'.";
                throw new AssertionError(msg);
            }
        }
        catch (final ElementNotFoundException e) {
            final String msg = "Unable to verify that the element with ID '" + id + "' contains the text '" + text
                            + "' because the specified element does not exist.";
            throw new AssertionError(msg, e);
        }
    }

    /**
     * Verifies that the specified page does not contain the specified text.
     *
     * @param page the page to check
     * @param text the text to check for
     * @throws AssertionError if the page contains the specified text
     * @throws NullPointerException if page or text is null
     */
    public static void assertTextNotPresent(final HtmlPage page, final String text) {
        if (page.asNormalizedText().contains(text)) {
            final String msg = "The page contains the text '" + text + "' but should not.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the element on the specified page which matches the specified ID does not
     * contain the specified text.
     *
     * @param page the page to check
     * @param text the text to check for
     * @param id the ID of the element which is expected to not contain the specified text
     */
    public static void assertTextNotPresentInElement(final HtmlPage page, final String text, final String id) {
        try {
            final HtmlElement element = page.getHtmlElementById(id);
            if (element.asNormalizedText().contains(text)) {
                final String msg = "The element with ID '" + id + "' contains the text '" + text + "' but should not.";
                throw new AssertionError(msg);
            }
        }
        catch (final ElementNotFoundException e) {
            final String msg = "Unable to verify that the element with ID '" + id + "' does not contain the text '"
                            + text + "' because the specified element does not exist.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page contains a link with the specified ID.
     *
     * @param page the page to check
     * @param id the ID of the link which the page is expected to contain
     * @throws AssertionError if no link with the specified ID is found
     * @see #assertLinkNotPresent(HtmlPage, String)
     * @see #assertLinkPresentWithText(HtmlPage, String)
     */
    public static void assertLinkPresent(final HtmlPage page, final String id) {
        try {
            page.getDocumentElement().getOneHtmlElementByAttribute("a", DomElement.ID_ATTRIBUTE, id);
        }
        catch (final ElementNotFoundException e) {
            final String msg = "The page does not contain a link with ID '" + id + "'.";
            throw new AssertionError(msg, e);
        }
    }

    /**
     * Verifies that the specified page does not contain a link with the specified ID.
     *
     * @param page the page to check
     * @param id the ID of the link which the page is expected to not contain
     * @throws AssertionError if a link with the specified ID is found
     * @see #assertLinkPresent(HtmlPage, String)
     * @see #assertLinkNotPresentWithText(HtmlPage, String)
     */
    public static void assertLinkNotPresent(final HtmlPage page, final String id) {
        try {
            page.getDocumentElement().getOneHtmlElementByAttribute("a", DomElement.ID_ATTRIBUTE, id);
            // Not expected.
            final String msg = "The page contains a link with ID '" + id + "' but should not.";
            throw new AssertionError(msg);
        }
        catch (final ElementNotFoundException expected) {
        }
    }

    /**
     * Verifies that the specified page contains a link with the specified text. The specified text
     * may be a substring of the entire text contained by the link.
     *
     * @param page the page to check
     * @param text the text which a link in the specified page is expected to contain
     */
    public static void assertLinkPresentWithText(final HtmlPage page, final String text) {
        boolean found = false;
        for (final HtmlAnchor a : page.getAnchors()) {
            if (a.asNormalizedText().contains(text)) {
                found = true;
                break;
            }
        }
        if (!found) {
            final String msg = "The page does not contain a link with text '" + text + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page does not contain a link with the specified text. The
     * specified text may be a substring of the entire text contained by the link.
     *
     * @param page the page to check
     * @param text the text which a link in the specified page is not expected to contain
     */
    public static void assertLinkNotPresentWithText(final HtmlPage page, final String text) {
        boolean found = false;
        for (final HtmlAnchor a : page.getAnchors()) {
            if (a.asNormalizedText().contains(text)) {
                found = true;
                break;
            }
        }
        if (found) {
            final String msg = "The page contains a link with text '" + text + "' but should not.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page contains a form with the specified name.
     *
     * @param page the page to check
     * @param name the expected name of a form on the page
     * @throws AssertionError if no form with the specified name is found
     * @see #assertFormNotPresent(HtmlPage, String)
     */
    public static void assertFormPresent(final HtmlPage page, final String name) {
        try {
            page.getFormByName(name);
        }
        catch (final ElementNotFoundException e) {
            final String msg = "The page does not contain a form named '" + name + "'.";
            throw new AssertionError(msg, e);
        }
    }

    /**
     * Verifies that the specified page does not contain a form with the specified name.
     *
     * @param page the page to check
     * @param name the name of a form which should not exist on the page
     * @throws AssertionError if a form with the specified name is found
     * @see #assertFormPresent(HtmlPage, String)
     */
    public static void assertFormNotPresent(final HtmlPage page, final String name) {
        try {
            page.getFormByName(name);
        }
        catch (final ElementNotFoundException e) {
            return;
        }
        final String msg = "The page contains a form named '" + name + "' but should not.";
        throw new AssertionError(msg);
    }

    /**
     * Verifies that the specified page contains an input element with the specified name.
     *
     * @param page the page to check
     * @param name the name of the input element to look for
     * @throws AssertionError if no input element with the specified name is found
     * @see #assertInputNotPresent(HtmlPage, String)
     * @see #assertInputContainsValue(HtmlPage, String, String)
     */
    public static void assertInputPresent(final HtmlPage page, final String name) {
        final String xpath = "//input[@name='" + name + "']";
        final List<?> list = page.getByXPath(xpath);
        if (list.isEmpty()) {
            throw new AssertionError("Unable to find an input element named '" + name + "'.");
        }
    }

    /**
     * Verifies that the specified page does not contain an input element with the specified name.
     *
     * @param page the page to check
     * @param name the name of the input element to look for
     * @throws AssertionError if an input element with the specified name is found
     * @throws NullPointerException if page or name is null
     */
    public static void assertInputNotPresent(final HtmlPage page, final String name) {
        final String xpath = "//input[@name='" + name + "']";
        final List<?> list = page.getByXPath(xpath);
        if (!list.isEmpty()) {
            throw new AssertionError("Found an input element named '" + name + "' when none was expected.");
        }
    }

    /**
     * Verifies that the input element with the specified name on the specified page contains the
     * specified value.
     *
     * @param page the page to check
     * @param name the name of the input element to check
     * @param value the value to check for
     */
    public static void assertInputContainsValue(final HtmlPage page, final String name, final String value) {
        final String xpath = "//input[@name='" + name + "']";
        final List<?> list = page.getByXPath(xpath);
        if (list.isEmpty()) {
            throw new AssertionError("Unable to find an input element named '" + name + "'.");
        }
        final HtmlInput input = (HtmlInput) list.get(0);
        final String s = input.getValue();
        if (!s.equals(value)) {
            throw new AssertionError("The input element named '" + name + "' contains the value '" + s
                            + "', not the expected value '" + value + "'.");
        }
    }

    /**
     * Verifies that the input element with the specified name on the specified page does not
     * contain the specified value.
     *
     * @param page the page to check
     * @param name the name of the input element to check
     * @param value the value to check for
     */
    public static void assertInputDoesNotContainValue(final HtmlPage page, final String name, final String value) {
        final String xpath = "//input[@name='" + name + "']";
        final List<?> list = page.getByXPath(xpath);
        if (list.isEmpty()) {
            throw new AssertionError("Unable to find an input element named '" + name + "'.");
        }
        final HtmlInput input = (HtmlInput) list.get(0);
        final String s = input.getValue();
        if (s.equals(value)) {
            throw new AssertionError("The input element named '" + name + "' contains the value '" + s
                            + "', not the expected value '" + value + "'.");
        }
    }

    /**
     * <p>Many HTML elements are "tabbable" and can have a <code>tabindex</code> attribute
     * that determines the order in which the components are navigated when
     * pressing the tab key. To ensure good usability for keyboard navigation,
     * all tabbable elements should have the <code>tabindex</code> attribute set.</p>
     *
     * <p>This method verifies that all tabbable elements have a valid value set for
     * the <code>tabindex</code> attribute. Valid values are positive integers,
     * 0 (for default tab order), or -1 (to exclude from tab order).</p>
     *
     * <p>The following elements are checked: a, area, button, input, object, select, textarea</p>
     *
     * @param page the page to check
     * @throws AssertionError if any tabbable element has an invalid or missing tabindex attribute
     */
    public static void assertAllTabIndexAttributesSet(final HtmlPage page) {
        final List<String> tags =
            Arrays.asList("a", "area", "button", "input", "object", "select", "textarea");

        for (final String tag : tags) {
            for (final HtmlElement element : page.getDocumentElement().getStaticElementsByTagName(tag)) {
                final Short tabIndex = element.getTabIndex();
                if (tabIndex == null || HtmlElement.TAB_INDEX_OUT_OF_BOUNDS.equals(tabIndex)) {
                    final String s = element.getAttributeDirect("tabindex");
                    throw new AssertionError("Illegal value for tab index: '" + s + "'.");
                }
            }
        }
    }

    /**
     * Many HTML components can have an <code>accesskey</code> attribute which defines a hot key for
     * keyboard navigation. This method verifies that all the <code>accesskey</code> attributes on the
     * specified page are unique.
     *
     * <p>Duplicate access keys can confuse users and make keyboard navigation unpredictable.</p>
     *
     * @param page the page to check
     * @throws AssertionError if any access key is used more than once on the page
     */
    public static void assertAllAccessKeyAttributesUnique(final HtmlPage page) {
        final List<String> list = new ArrayList<>();
        for (final HtmlElement element : page.getHtmlElementDescendants()) {
            final String key = element.getAttributeDirect("accesskey");
            if (key != null && !key.isEmpty()) {
                if (list.contains(key)) {
                    throw new AssertionError("The access key '" + key + "' is not unique.");
                }
                list.add(key);
            }
        }
    }

    /**
     * Verifies that all element IDs in the specified page are unique.
     *
     * @param page the page to check
     * @throws AssertionError if any element ID is used more than once on the page
     * @throws NullPointerException if page is null
     */
    public static void assertAllIdAttributesUnique(final HtmlPage page) {
        final List<String> list = new ArrayList<>();
        for (final HtmlElement element : page.getHtmlElementDescendants()) {
            final String id = element.getId();
            if (id != null && !id.isEmpty()) {
                if (list.contains(id)) {
                    throw new AssertionError("The element ID '" + id + "' is not unique.");
                }
                list.add(id);
            }
        }
    }

    /**
     * Assert that the specified parameter is not null. Throw a NullPointerException
     * if a null is found.
     *
     * @param description the description to pass into the NullPointerException
     * @param object the object to check for null
     * @throws NullPointerException if the object is null
     */
    public static void notNull(final String description, final Object object) {
        if (object == null) {
            throw new NullPointerException(description);
        }
    }
}
