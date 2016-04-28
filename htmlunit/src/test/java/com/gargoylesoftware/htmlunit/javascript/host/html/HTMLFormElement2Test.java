/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * Tests for {@link HTMLFormElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLFormElement2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmit() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);
        final HtmlPage page = loadPageWithAlerts(html);

        final HtmlPage secondPage =
            (HtmlPage) page.executeJavaScript("document.form1.submit()").getNewPage();
        assertEquals("second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("javaScript")
    public void formSubmitWithJavascript() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='javascript:alert(\"javaScript\")'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();

        final HtmlPage page1 = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.form1.submit()").getNewPage();

        assertEquals(page1, page2);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("javaScript")
    public void formSubmitWithJavascriptLeadingWhitespace() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='  javascript:alert(\"javaScript\")'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();

        final HtmlPage page1 = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.form1.submit()").getNewPage();

        assertEquals(page1, page2);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("javaScript")
    public void formSubmitWithJavascriptMixedCase() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='javaSCript:alert(\"javaScript\")'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();

        final HtmlPage page1 = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.form1.submit()").getNewPage();

        assertEquals(page1, page2);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onSubmitChangesAction() throws Exception {
        final String html
            = "<html><body>\n"
            + "<form name='form1' action='" + URL_SECOND + "' onsubmit='this.action=\"" + URL_THIRD + "\"' "
            + "method='post'>\n"
            + "    <input type='submit' id='button1' />\n"
            + "</form>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("<html></html>");

        final HtmlPage page = loadPageWithAlerts(html);
        final Page page2 = page.getHtmlElementById("button1").click();

        assertEquals(URL_THIRD.toExternalForm(), page2.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmit_target() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "' target='MyNewWindow'>\n"
            + "    <input type='button' name='button1' />\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final HtmlPage page = loadPageWithAlerts(html);

        final HtmlPage secondPage
            = (HtmlPage) page.executeJavaScript("document.form1.submit()").getNewPage();
        assertEquals("second", secondPage.getTitleText());
        assertEquals("MyNewWindow", secondPage.getEnclosingWindow().getName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmitDoesntCallOnSubmit() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "' onsubmit=\"this.action = 'foo.html'\">\n"
            + "    <input type='submit' />\n"
            + "</form>\n"
            + "<a href='javascript:document.form1.submit()' id='link1'>Click me</a>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlAnchor link = page.getHtmlElementById("link1");
        final HtmlPage page2 = link.click();
        assertEquals("second", page2.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmit_MultipleButtons() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "'>\n"
            + "    <button type='submit' name='button1' id='button1'/>\n"
            + "    <button type='submit' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent
            = "<html><head><title>second</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(secondContent);

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("first", page.getTitleText());

        final HtmlButton button = page.getHtmlElementById("button1");
        final HtmlPage secondPage = button.click();
        assertEquals("second", secondPage.getTitleText());
        assertEquals(URL_SECOND + "?button1=", secondPage.getUrl());
    }

    /**
    * @throws Exception if the test fails
    */
    @Test
    @Alerts("hi!")
    public void lostFunction() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + " function onSubmit() { alert('hi!'); return false; }\n"
            + "</script></head><body>\n"
            + "<form onsubmit='return onSubmit();'>\n"
            + " <input type='submit' id='clickMe' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = page.getHtmlElementById("clickMe");
        button.click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hi!")
    public void assignedOnsubmit() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + " function onSubmit() { alert('hi!'); return false; }\n"
            + " function init() { document.myForm.onsubmit = onSubmit; }\n"
            + " window.onload = init;\n"
            + "</script></head><body>\n"
            + "<form name='myForm'>\n"
            + " <input type='submit' id='clickMe' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = page.getHtmlElementById("clickMe");
        button.click();
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * In action "this" should be the window and not the form.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void thisInJavascriptAction() throws Exception {
        final String content
            = "<html>\n"
            + "<body>\n"
            + "<form action='javascript:alert(this == window)'>\n"
            + "<input type='submit' id='theButton'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page1 = loadPage(getBrowserVersion(), content, collectedAlerts);
        final Page page2 = page1.getHtmlElementById("theButton").click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
        assertSame(page1, page2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("dummy.txt")
    public void fileInput_fireOnChange() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<form>\n"
            + "  <input type='file' name='myFile' id='myFile' onchange='alert(this.value)'/>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlFileInput fileInput = page.getHtmlElementById("myFile");
        fileInput.focus();
        fileInput.setAttribute("value", "dummy.txt");
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        // remove focus to trigger onchange
        page.setFocusedElement(null);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
