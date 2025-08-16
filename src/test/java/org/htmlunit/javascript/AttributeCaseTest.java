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
package org.htmlunit.javascript;

import java.util.Locale;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlElement} attributes.
 *
 * @author David D. Kilzer
 * @author Mike Bowler
 * @author Ronald Brill
 */
public class AttributeCaseTest extends SimpleWebTestCase {

    private static final String ATTRIBUTE_NAME = "randomAttribute";
    private static final String ATTRIBUTE_VALUE = "someValue";
    private static final String ATTRIBUTE_VALUE_NEW = "newValue";

    private HtmlElement element_;
    private HtmlPage page_;

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeLowerCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertEquals(page_.asXml(), ATTRIBUTE_VALUE,
                element_.getAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeMixedCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertEquals(page_.asXml(), ATTRIBUTE_VALUE, element_.getAttribute(ATTRIBUTE_NAME));
    }

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void getAttributeUpperCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertEquals(page_.asXml(), ATTRIBUTE_VALUE,
                element_.getAttribute(ATTRIBUTE_NAME.toUpperCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeLowerCase() throws Exception {
        setupSetAttributeTest(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT), ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeMixedCase() throws Exception {
        setupSetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void setAttributeUpperCase() throws Exception {
        setupSetAttributeTest(ATTRIBUTE_NAME.toUpperCase(Locale.ROOT), ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeLowerCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT));
        assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeMixedCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME);
        assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void removeAttributeUpperCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME.toUpperCase(Locale.ROOT));
        assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with a lower case name.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeLowerCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME.toLowerCase(Locale.ROOT)));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with a mixed case name.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeMixedCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with an upper case name.
     * @throws Exception if the test fails
     */
    @Test
    public void hasAttributeUpperCase() throws Exception {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME.toUpperCase(Locale.ROOT)));
    }

    private void setupAttributeTest(final String content, final String elementId) throws Exception {
        page_ = loadPage(content);
        element_ = page_.getHtmlElementById(elementId);
    }

    private void setupGetAttributeTest(final String attributeName, final String attributeValue) throws Exception {
        final String elementId = "p-id";
        final String content = DOCTYPE_HTML
                          + "<html><head><title>AttributeCaseTest</title></head><body>\n"
                          + "<p id=\"" + elementId + "\" " + attributeName + "=\"" + attributeValue + "\">\n"
                          + "</body></html>";

        setupAttributeTest(content, elementId);
    }

    private void setupSetAttributeTest(
            final String attributeName, final String attributeValue,
            final String newAttributeValue)
        throws Exception {

        final String elementId = "p-id";
        final String content = DOCTYPE_HTML
             + "<html><head><title>AttributeCaseTest</title></head><body>\n"
             + "<p id=\"" + elementId + "\" " + attributeName + "=\"" + attributeValue + "\">\n"
             + "<script language=\"javascript\" type=\"text/javascript\">\n<!--\n"
             + "  document.getElementById(\"" + elementId + "\").setAttribute(\"" + attributeName + "\", \""
             + newAttributeValue + "\");\n"
             + "\n// -->\n</script>\n"
             + "</body></html>";

        setupAttributeTest(content, elementId);
    }
}
