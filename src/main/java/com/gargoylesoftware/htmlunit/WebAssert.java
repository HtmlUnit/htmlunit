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
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Utility class which contains standard assertions for HTML pages.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
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
     */
    public static void assertTitleEquals(final HtmlPage page, final String title) {
        final String s = page.getTitleText();
        if (!s.equals(title)) {
            final String msg = "Actual page title '" + s + "' does not match expected page title '" + title + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page's title contains the specified substring.
     *
     * @param page the page to check
     * @param titlePortion the substring which the page title is expected to contain
     */
    public static void assertTitleContains(final HtmlPage page, final String titlePortion) {
        final String s = page.getTitleText();
        if (s.indexOf(titlePortion) == -1) {
            final String msg = "Page title '" + s + "' does not contain the substring '" + titlePortion + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page's title matches the specified regular expression.
     *
     * @param page the page to check
     * @param regex the regular expression that the page title is expected to match
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
     * @param id the expected ID of an element in the page
     */
    public static void assertElementPresent(final HtmlPage page, final String id) {
        try {
            page.getHtmlElementById(id);
        }
        catch (final ElementNotFoundException e) {
            final String msg = "The page does not contain an element with ID '" + id + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page contains an element matching the specified XPath expression.
     *
     * @param page the page to check
     * @param xpath the XPath expression which is expected to match an element in the page
     */
    public static void assertElementPresentByXPath(final HtmlPage page, final String xpath) {
        final List< ? > elements = page.getByXPath(xpath);
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
     */
    public static void assertElementNotPresent(final HtmlPage page, final String id) {
        try {
            page.getHtmlElementById(id);
        }
        catch (final ElementNotFoundException e) {
            return;
        }
        final String msg = "The page contains an element with ID '" + id + "'.";
        throw new AssertionError(msg);
    }

    /**
     * Verifies that the specified page does not contain an element matching the specified XPath
     * expression.
     *
     * @param page the page to check
     * @param xpath the XPath expression which is expected to not match an element in the page
     */
    public static void assertElementNotPresentByXPath(final HtmlPage page, final String xpath) {
        final List< ? > elements = page.getByXPath(xpath);
        if (!elements.isEmpty()) {
            final String msg = "The page does not contain any elements matching the XPath expression '" + xpath
                            + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page contains the specified text.
     *
     * @param page the page to check
     * @param text the text to check for
     */
    public static void assertTextPresent(final HtmlPage page, final String text) {
        if (page.asText().indexOf(text) == -1) {
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
     */
    public static void assertTextPresentInElement(final HtmlPage page, final String text, final String id) {
        try {
            final HtmlElement element = page.getHtmlElementById(id);
            if (element.asText().indexOf(text) == -1) {
                final String msg = "The element with ID '" + id + "' does not contain the text '" + text + "'.";
                throw new AssertionError(msg);
            }
        }
        catch (final ElementNotFoundException e) {
            final String msg = "Unable to verify that the element with ID '" + id + "' contains the text '" + text
                            + "' because the specified element does not exist.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page does not contain the specified text.
     *
     * @param page the page to check
     * @param text the text to check for
     */
    public static void assertTextNotPresent(final HtmlPage page, final String text) {
        if (page.asText().contains(text)) {
            final String msg = "The page contains the text '" + text + "'.";
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
            if (element.asText().contains(text)) {
                final String msg = "The element with ID '" + id + "' contains the text '" + text + "'.";
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
     */
    public static void assertLinkPresent(final HtmlPage page, final String id) {
        try {
            page.getDocumentHtmlElement().getOneHtmlElementByAttribute("a", "id", id);
        }
        catch (final ElementNotFoundException e) {
            final String msg = "The page does not contain a link with ID '" + id + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page does not contain a link with the specified ID.
     *
     * @param page the page to check
     * @param id the ID of the link which the page is expected to not contain
     */
    public static void assertLinkNotPresent(final HtmlPage page, final String id) {
        try {
            page.getDocumentHtmlElement().getOneHtmlElementByAttribute("a", "id", id);
            // Not expected.
            final String msg = "The page contains a link with ID '" + id + "'.";
            throw new AssertionError(msg);
        }
        catch (final ElementNotFoundException e) {
            // Expected.
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
            if (a.asText().contains(text)) {
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
            if (a.asText().contains(text)) {
                found = true;
                break;
            }
        }
        if (found) {
            final String msg = "The page contains a link with text '" + text + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page contains a form with the specified name.
     *
     * @param page the page to check
     * @param name the expected name of a form on the page
     */
    public static void assertFormPresent(final HtmlPage page, final String name) {
        try {
            page.getFormByName(name);
        }
        catch (final ElementNotFoundException e) {
            final String msg = "The page does not contain a form named '" + name + "'.";
            throw new AssertionError(msg);
        }
    }

    /**
     * Verifies that the specified page does not contain a form with the specified name.
     *
     * @param page the page to check
     * @param name the name of a form which should not exist on the page
     */
    public static void assertFormNotPresent(final HtmlPage page, final String name) {
        try {
            page.getFormByName(name);
        }
        catch (final ElementNotFoundException e) {
            return;
        }
        final String msg = "The page contains a form named '" + name + "'.";
        throw new AssertionError(msg);
    }

    /**
     * Verifies that the specified page contains an input element with the specified name.
     *
     * @param page the page to check
     * @param name the name of the input element to look for
     */
    public static void assertInputPresent(final HtmlPage page, final String name) {
        final String xpath = "//input[@name='" + name + "']";
        final List< ? > list = page.getByXPath(xpath);
        if (list.isEmpty()) {
            throw new AssertionError("Unable to find an input element named '" + name + "'.");
        }
    }

    /**
     * Verifies that the specified page does not contain an input element with the specified name.
     *
     * @param page the page to check
     * @param name the name of the input element to look for
     */
    public static void assertInputNotPresent(final HtmlPage page, final String name) {
        final String xpath = "//input[@name='" + name + "']";
        final List< ? > list = page.getByXPath(xpath);
        if (!list.isEmpty()) {
            throw new AssertionError("Unable to find an input element named '" + name + "'.");
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
        final List< ? > list = page.getByXPath(xpath);
        if (list.isEmpty()) {
            throw new AssertionError("Unable to find an input element named '" + name + "'.");
        }
        final HtmlInput input = (HtmlInput) list.get(0);
        final String s = input.getValueAttribute();
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
        final List< ? > list = page.getByXPath(xpath);
        if (list.isEmpty()) {
            throw new AssertionError("Unable to find an input element named '" + name + "'.");
        }
        final HtmlInput input = (HtmlInput) list.get(0);
        final String s = input.getValueAttribute();
        if (s.equals(value)) {
            throw new AssertionError("The input element named '" + name + "' contains the value '" + s
                            + "', not the expected value '" + value + "'.");
        }
    }

    /**
     * <p>Many HTML elements are "tabbable" and can have a <tt>tabindex</tt> attribute
     * that determines the order in which the components are navigated when
     * pressing the tab key. To ensure good usability for keyboard navigation,
     * all tabbable elements should have the <tt>tabindex</tt> attribute set.</p>
     *
     * <p>This method verifies that all tabbable elements have a valid value set for
     * the <tt>tabindex</tt> attribute.</p>
     *
     * @param page the page to check
     */
    public static void assertAllTabIndexAttributesSet(final HtmlPage page) {
        final List<String> tags =
            Arrays.asList(new String[] {"a", "area", "button", "input", "object", "select", "textarea"});
        for (final HtmlElement element : page.getDocumentHtmlElement().getHtmlElementsByTagNames(tags)) {
            final Short tabIndex = element.getTabIndex();
            if (tabIndex == null || tabIndex == HtmlElement.TAB_INDEX_OUT_OF_BOUNDS) {
                final String s = element.getAttributeValue("tabindex");
                throw new AssertionError("Illegal value for tab index: '" + s + "'.");
            }
        }
    }

    /**
     * Many HTML components can have an <tt>accesskey</tt> attribute which defines a hot key for
     * keyboard navigation. This method verifies that all the <tt>accesskey</tt> attributes on the
     * specified page are unique.
     *
     * @param page the page to check
     */
    public static void assertAllAccessKeyAttributesUnique(final HtmlPage page) {
        final List<String> list = new ArrayList<String>();
        for (final HtmlElement element : page.getAllHtmlChildElements()) {
            final String key = element.getAttributeValue("accesskey");
            if (key != null && key.length() != 0) {
                if (list.contains(key)) {
                    throw new AssertionError("The access key '" + key + "' is not unique.");
                }
                else {
                    list.add(key);
                }
            }
        }
    }

    /**
     * Verifies that all element IDs in the specified page are unique.
     *
     * @param page the page to check
     */
    public static void assertAllIdAttributesUnique(final HtmlPage page) {
        final List<String> list = new ArrayList<String>();
        for (final HtmlElement element : page.getAllHtmlChildElements()) {
            final String id = element.getAttributeValue("id");
            if (id != null && id.length() != 0) {
                if (list.contains(id)) {
                    throw new AssertionError("The element ID '" + id + "' is not unique.");
                }
                else {
                    list.add(id);
                }
            }
        }
    }

    /**
     * Assert that the specified parameter is not null. Throw a NullPointerException
     * if a null is found.
     * @param description The description to pass into the NullPointerException
     * @param object The object to check for null.
     */
    public static void notNull(final String description, final Object object) {
        if (object == null) {
            throw new NullPointerException(description);
        }
    }
}
