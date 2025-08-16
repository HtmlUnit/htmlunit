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
package org.htmlunit.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlButton}.
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Brad Clarke
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlButtonTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buttonClick_onClick() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "  <button type='button' name='button' id='button' "
            + "onClick='alert(\"foo\")'>Push me</button>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void submitClick_onClick() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "  <button type='submit' name='button' id='button' "
            + "onClick='alert(\"foo\")'>Push me</button>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        final String[] expectedAlerts = {"foo", "bar"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertNotSame(page, secondPage);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buttonTypeSubmit() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post' onSubmit='alert(\"bar\")' onReset='alert(\"reset\")'>\n"
            + "  <button type='submit' name='button' id='button' value='foo'"
            + "    >Push me</button>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlButton button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();

        final String[] expectedAlerts = {"bar"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertNotSame(page, secondPage);
        final List<NameValuePair> expectedParameters = Arrays.asList(new NameValuePair[]{
            new NameValuePair("button", "foo")
        });
        final List<NameValuePair> collectedParameters = getMockConnection(secondPage).getLastParameters();

        assertEquals(expectedParameters, collectedParameters);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void defaultButtonTypeNotSet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form id='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "  <button name='button' id='button' value='pushme'>PushMe</button>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlButton button = page.getHtmlElementById("button");
        assertEquals(getExpectedAlerts()[0], button.getTypeAttribute());
        assertEquals(getExpectedAlerts()[0], button.getAttribute("type"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Submit")
    public void defaultButtonTypeSubmit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form id='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "  <button name='button' id='button' type='Submit' value='pushme'>PushMe</button>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlButton button = page.getHtmlElementById("button");
        assertEquals(getExpectedAlerts()[0], button.getTypeAttribute());
        assertEquals(getExpectedAlerts()[0], button.getAttribute("type"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("invalid")
    public void defaultButtonTypeInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form id='form1' action='" + URL_SECOND + "' method='post'>\n"
            + "  <button name='button' id='button' type='invalid' value='pushme'>PushMe</button>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlButton button = page.getHtmlElementById("button");
        assertEquals(getExpectedAlerts()[0], button.getTypeAttribute());
        assertEquals(getExpectedAlerts()[0], button.getAttribute("type"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><title>foo</title>\n"
            + "<button></button>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);

        final String xml = page.asXml();
        assertTrue("Node not expanded in: " + xml, xml.contains("</button>"));
    }
}
