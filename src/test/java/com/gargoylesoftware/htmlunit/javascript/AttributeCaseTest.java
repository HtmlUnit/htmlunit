/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HtmlElement} attributes.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class AttributeCaseTest extends WebTestCase {

    private static final String ATTRIBUTE_NAME = "randomAttribute";
    private static final String ATTRIBUTE_VALUE = "someValue";
    private static final String ATTRIBUTE_VALUE_NEW = "newValue";

    private HtmlElement element_;
    private HtmlPage page_;

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with a lower case name.
     * @throws IOException if the test fails
     */
    @Test
    public void getAttributeLowerCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE, element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with a mixed case name.
     * @throws IOException if the test fails
     */
    @Test
    public void getAttributeMixedCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE, element_.getAttribute(ATTRIBUTE_NAME));
    }

    /**
     * Tests {@link HtmlElement#getAttribute(String)} with an upper case name.
     * @throws IOException if the test fails
     */
    @Test
    public void getAttributeUpperCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE, element_.getAttribute(ATTRIBUTE_NAME.toUpperCase()));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with a lower case name.
     * @throws IOException if the test fails
     */
    @Test
    public void setAttributeLowerCase() throws IOException {
        setupSetAttributeTest(ATTRIBUTE_NAME.toLowerCase(), ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with a mixed case name.
     * @throws IOException if the test fails
     */
    @Test
    public void setAttributeMixedCase() throws IOException {
        setupSetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#setAttribute(String,String)} with an upper case name.
     * @throws IOException if the test fails
     */
    @Test
    public void setAttributeUpperCase() throws IOException {
        setupSetAttributeTest(ATTRIBUTE_NAME.toUpperCase(), ATTRIBUTE_VALUE, ATTRIBUTE_VALUE_NEW);
        Assert.assertEquals(page_.asXml(), ATTRIBUTE_VALUE_NEW,
            element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with a lower case name.
     * @throws IOException if the test fails
     */
    @Test
    public void removeAttributeLowerCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME.toLowerCase());
        Assert.assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with a mixed case name.
     * @throws IOException if the test fails
     */
    @Test
    public void removeAttributeMixedCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME);
        Assert.assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#removeAttribute(String)} with an upper case name.
     * @throws IOException if the test fails
     */
    @Test
    public void removeAttributeUpperCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        element_.removeAttribute(ATTRIBUTE_NAME.toUpperCase());
        Assert.assertEquals(page_.asXml(), "", element_.getAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with a lower case name.
     * @throws IOException if the test fails
     */
    @Test
    public void hasAttributeLowerCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME.toLowerCase()));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with a mixed case name.
     * @throws IOException if the test fails
     */
    @Test
    public void hasAttributeMixedCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME));
    }

    /**
     * Tests {@link HtmlElement#hasAttribute(String)} with an upper case name.
     * @throws IOException if the test fails
     */
    @Test
    public void hasAttributeUpperCase() throws IOException {
        setupGetAttributeTest(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
        assertTrue(page_.asXml(), element_.hasAttribute(ATTRIBUTE_NAME.toUpperCase()));
    }

    private void setupAttributeTest(final String content, final String elementId) throws IOException {
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        page_ = client.getPage(URL_GARGOYLE);
        element_ = page_.getHtmlElementById(elementId);
    }

    private void setupGetAttributeTest(final String attributeName, final String attributeValue) throws IOException {
        final String elementId = "p-id";
        final String content = "<html><head><title>AttributeCaseTest</title></head><body>\n"
                          + "<p id=\"" + elementId + "\" " + attributeName + "=\"" + attributeValue + "\">\n"
                          + "</body></html>";

        setupAttributeTest(content, elementId);
    }

    private void setupSetAttributeTest(
            final String attributeName, final String attributeValue,
            final String newAttributeValue)
        throws IOException {

        final String elementId = "p-id";
        final String content
            = "<html><head><title>AttributeCaseTest</title></head><body>\n"
             + "<p id=\"" + elementId + "\" " + attributeName + "=\"" + attributeValue + "\">\n"
             + "<script language=\"javascript\" type=\"text/javascript\">\n<!--\n"
             + "  document.getElementById(\"" + elementId + "\").setAttribute(\"" + attributeName + "\", \""
             + newAttributeValue + "\");\n"
             + "\n// -->\n</script>\n"
             + "</body></html>";

        setupAttributeTest(content, elementId);
    }
}
