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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebAssert}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class WebAssertTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleEquals() throws Exception {
        final String html = "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleEquals(page, "foo");

        boolean caught = false;
        try {
            WebAssert.assertTitleEquals(page, "bar");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleContains() throws Exception {
        final String html = "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleContains(page, "o");

        boolean caught = false;
        try {
            WebAssert.assertTitleContains(page, "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTitleMatches() throws Exception {
        final String html = "<html><head><title>foo</title></head><body>bar</body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTitleMatches(page, "f..");

        boolean caught = false;
        try {
            WebAssert.assertTitleMatches(page, "b..");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementPresent() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresent(page, "a");

        boolean caught = false;
        try {
            WebAssert.assertElementPresent(page, "b");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementPresentByXPath() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementPresentByXPath(page, "html/body/div");

        boolean caught = false;
        try {
            WebAssert.assertElementPresentByXPath(page, "ul");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementNotPresent() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresent(page, "b");

        boolean caught = false;
        try {
            WebAssert.assertElementNotPresent(page, "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertElementNotPresentByXPath() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertElementNotPresentByXPath(page, "ul");

        boolean caught = false;
        try {
            WebAssert.assertElementNotPresentByXPath(page, "html/body/div");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextPresent() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextPresent(page, "bar");

        boolean caught = false;
        try {
            WebAssert.assertTextPresent(page, "baz");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextPresentInElement() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextPresentInElement(page, "bar", "a");

        boolean caught = false;
        try {
            WebAssert.assertTextPresentInElement(page, "baz", "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertTextPresentInElement(page, "bar", "b");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextNotPresent() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresent(page, "baz");

        boolean caught = false;
        try {
            WebAssert.assertTextNotPresent(page, "bar");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertTextNotPresentInElement() throws Exception {
        final String html = "<html><body><div id='a'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertTextNotPresentInElement(page, "baz", "a");

        boolean caught = false;
        try {
            WebAssert.assertTextNotPresentInElement(page, "bar", "a");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertTextNotPresentInElement(page, "bar", "b");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkPresent() throws Exception {
        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresent(page, "x");

        boolean caught = false;
        try {
            WebAssert.assertLinkPresent(page, "z");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkNotPresent() throws Exception {
        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresent(page, "z");

        boolean caught = false;
        try {
            WebAssert.assertLinkNotPresent(page, "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkPresentWithText() throws Exception {
        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkPresentWithText(page, "r");

        boolean caught = false;
        try {
            WebAssert.assertLinkPresentWithText(page, "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertLinkNotPresentWithText() throws Exception {
        final String html = "<html><body><a href='foo.html' id='x'>bar</a></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertLinkNotPresentWithText(page, "x");

        boolean caught = false;
        try {
            WebAssert.assertLinkNotPresentWithText(page, "r");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertFormPresent() throws Exception {
        final String html = "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormPresent(page, "f");

        boolean caught = false;
        try {
            WebAssert.assertFormPresent(page, "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertFormNotPresent() throws Exception {
        final String html = "<html><body><form name='f'>bar</form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertFormNotPresent(page, "x");

        boolean caught = false;
        try {
            WebAssert.assertFormNotPresent(page, "f");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputPresent() throws Exception {
        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputPresent(page, "i");

        boolean caught = false;
        try {
            WebAssert.assertInputPresent(page, "q");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputNotPresent() throws Exception {
        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputNotPresent(page, "q");

        boolean caught = false;
        try {
            WebAssert.assertInputNotPresent(page, "i");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputContainsValue() throws Exception {
        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputContainsValue(page, "i", "x");

        boolean caught = false;
        try {
            WebAssert.assertInputContainsValue(page, "i", "z");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertInputContainsValue(page, "q", "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertInputDoesNotContainValue() throws Exception {
        final String html = "<html><body><form name='f'><input name='i' value='x'/></form></body></html>";
        final HtmlPage page = loadPage(html);

        WebAssert.assertInputDoesNotContainValue(page, "i", "z");

        boolean caught = false;
        try {
            WebAssert.assertInputDoesNotContainValue(page, "i", "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
            WebAssert.assertInputDoesNotContainValue(page, "q", "x");
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertAllTabIndexAttributesSet() throws Exception {
        final String html1 = "<html><body><a href='#' tabindex='1'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllTabIndexAttributesSet(page1);

        final String html2 = "<html><body><a href='#'>foo</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        boolean caught = false;
        try {
            WebAssert.assertAllTabIndexAttributesSet(page2);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);

        final String html3 = "<html><body><a href='#' tabindex='x'>foo</a></body></html>";
        final HtmlPage page3 = loadPage(html3);

        caught = false;
        try {
            WebAssert.assertAllTabIndexAttributesSet(page3);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertAllAccessKeyAttributesUnique() throws Exception {
        final String html1 = "<html><body><a accesskey='k'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllAccessKeyAttributesUnique(page1);

        final String html2 = "<html><body><a accesskey='k'>foo</a><a accesskey='k'>bar</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        boolean caught = false;
        try {
            WebAssert.assertAllAccessKeyAttributesUnique(page2);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void assertAllIdAttributesUnique() throws Exception {
        final String html1 = "<html><body><a id='k'>foo</a></body></html>";
        final HtmlPage page1 = loadPage(html1);

        WebAssert.assertAllIdAttributesUnique(page1);

        final String html2 = "<html><body><a id='k'>foo</a><a id='k'>bar</a></body></html>";
        final HtmlPage page2 = loadPage(html2);

        boolean caught = false;
        try {
            WebAssert.assertAllIdAttributesUnique(page2);
        }
        catch (final AssertionError e) {
            caught = true;
        }
        assertTrue(caught);
    }
}
