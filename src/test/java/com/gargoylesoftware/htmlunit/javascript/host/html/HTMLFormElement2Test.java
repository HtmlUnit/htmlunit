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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * Tests for {@link HTMLFormElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLFormElement2Test extends WebTestCase {

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
    public void formSubmitWithJavascript() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='javascript:alert(\"javaScript\")'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"javaScript"};

        final HtmlPage page1 = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.form1.submit()").getNewPage();

        assertEquals(page1, page2);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmitWithJavascriptLeadingWhitespace() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='  javascript:alert(\"javaScript\")'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"javaScript"};

        final HtmlPage page1 = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.form1.submit()").getNewPage();

        assertEquals(page1, page2);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmitWithJavascriptMixedCase() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1' method='get' action='javaSCript:alert(\"javaScript\")'>\n"
            + "    <input type='button' name='button1' />\n"
            + "    <input type='button' name='button2' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"javaScript"};

        final HtmlPage page1 = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlPage page2 = (HtmlPage) page1.executeJavaScript("document.form1.submit()").getNewPage();

        assertEquals(page1, page2);
        assertEquals(expectedAlerts, collectedAlerts);
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

        assertEquals(URL_THIRD.toExternalForm(), page2.getWebResponse().getWebRequest().getUrl());
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
        assertEquals(URL_SECOND + "?button1=", secondPage.getWebResponse().getWebRequest().getUrl());
    }

    /**
    * @throws Exception if the test fails
    */
    @Test
    public void lostFunction() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + " function onSubmit() { alert('hi!'); return false; }\n"
            + "</script></head><body>\n"
            + "<form onsubmit='return onSubmit();'>\n"
            + " <input type='submit' id='clickMe' />\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = page.getHtmlElementById("clickMe");
        button.click();
        final String[] expectedAlerts = {"hi!"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
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

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlSubmitInput button = page.getHtmlElementById("clickMe");
        button.click();
        final String[] expectedAlerts = {"hi!"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that the event object is correctly made available.
     * Regression test for https://sf.net/tracker/index.php?func=detail&aid=1648014&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "srcElement null: true", "srcElement==form: false", "target null: false", "target==form: true" },
        IE = { "srcElement null: false", "srcElement==form: true", "target null: true", "target==form: false" })
    public void onSubmitEvent() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = getMockWebConnection();

        final String html = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "function test(_event) {\n"
            + "  var oEvent = _event ? _event : window.event;\n"
            + "  alert('srcElement null: ' + (oEvent.srcElement == null));\n"
            + "  alert('srcElement==form: ' + (oEvent.srcElement == document.forms[0]));\n"
            + "  alert('target null: ' + (oEvent.target == null));\n"
            + "  alert('target==form: ' + (oEvent.target == document.forms[0]));\n"
            + "}\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form name='formPage1' action='about:blank' onsubmit='test(event)'>\n"
            + "<input type='submit' id='theButton'>\n"
            + "</form>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        page.getHtmlElementById("theButton").click();

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * In action "this" should be the window and not the form.
     * @throws Exception if the test fails
     */
    @Test
    public void thisInJavascriptAction() throws Exception {
        final String content
            = "<html>\n"
            + "<body>\n"
            + "<form action='javascript:alert(this == window)'>\n"
            + "<input type='submit' id='theButton'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final String[] expectedAlerts = {"true"};
        final HtmlPage page1 = loadPage(getBrowserVersion(), content, collectedAlerts);
        final Page page2 = page1.getHtmlElementById("theButton").click();

        assertEquals(expectedAlerts, collectedAlerts);
        assertSame(page1, page2);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void fileInput_fireOnChange() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<form>\n"
            + "  <input type='file' name='myFile' id='myFile' onchange='alert(this.value)'/>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        final String[] expectedAlerts = {"dummy.txt"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);
        final HtmlFileInput fileInput = page.getHtmlElementById("myFile");
        fileInput.focus();
        fileInput.setAttribute("value", "dummy.txt");
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);
        // remove focus to trigger onchange
        page.setFocusedElement(null);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "prepare frame", "submit form", "submitted ok" })
    public void submitWithTargetOnIFrameAndOnload_script() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' name='form1' method='get' action='" + URL_SECOND + "'>\n"
            + "  <input type='button' name='button1' />\n"
            + "</form>\n"
            + "<script>\n"
            + "  // Prepare the iframe for the target\n"
            + "  alert('prepare frame');\n"
            + "  var div = document.createElement('div');\n"
            + "  div.style.display = 'none';\n"
            + "  div.innerHTML = \"<iframe name='frame' id='frame'></iframe>\";\n"
            + "  document.body.appendChild(div);\n"
            + "  // Get the form and set the target\n"
            + "  var form = document.getElementById('form1');\n"
            + "  form.target = 'frame';\n"
            + "  // Finally submit the form with a delay to make sure that the onload of the iframe\n"
            + "  // is called for the submit and not for the page creation\n"
            + "  var t = setTimeout(function() {\n"
            + "    clearTimeout(t);\n"
            + "    var iframe = document.getElementById('frame');\n"
            + "    iframe.onload = function() {\n"
            + "      alert('submitted ' + iframe.contentWindow.document.body.getAttribute('id'));\n"
            + "    };\n"
            + "    alert('submit form');\n"
            + "    form.submit();\n"
            + "  }, 1000);\n"
            + "</script></body></html>";
        final String html2
            = "<?xml version='1.0'?>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'><body id='ok'><span id='result'>OK</span></body></html>";
        getMockWebConnection().setDefaultResponse(html2);
        loadPageWithAlerts(html, getDefaultUrl(), 5000);
    }

    /**
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called. This is a Firefox-specific test.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "submit form", "submitted ok" })
    public void submitWithTargetOnIFrameAndOnload_bubbling_FF() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' name='form1' method='get' action='" + URL_SECOND + "' target='frame'>\n"
            + "  <input type='button' name='button1' />\n"
            + "</form>\n"
            + "<div style='display:none;'><iframe name='frame' id='frame'></iframe></div>\n"
            + "<script>\n"
            + "  // Get the form and set the target\n"
            + "  var form = document.getElementById('form1');\n"
            + "  var iframe = document.getElementById('frame');\n"
            + "  // Finally submit the form with a delay to make sure that the onload of the iframe\n"
            + "  // is called for the submit and not for the page creation\n"
            + "  var t = setTimeout(function() {\n"
            + "    clearTimeout(t);\n"
            + "    iframe.addEventListener('load', function() {\n"
            + "      alert('submitted ' + iframe.contentWindow.document.body.getAttribute('id'));\n"
            + "    }, true);\n"
            + "    alert('submit form');\n"
            + "    form.submit();\n"
            + "  }, 1000);\n"
            + "</script>\n"
            + "</body></html>";
        final String html2
            = "<?xml version='1.0'?>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'><body id='ok'><span id='result'>OK</span></body></html>";
        getMockWebConnection().setDefaultResponse(html2);
        loadPageWithAlerts(html, getDefaultUrl(), 5000);
    }

    /**
     * This test is used to check that when a form having a target is submitted
     * and if the target is an iframe and the iframe has an onload event, then
     * the onload event is called. This is an IE-specific test.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "submit form", "submitted ok" })
    public void submitWithTargetOnIFrameAndOnload_attached_IE() throws Exception {
        final String html
            = "<html><head><title>first</title></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' name='form1' method='get' action='" + URL_SECOND + "' target='frame'>\n"
            + "    <input type='button' name='button1' />\n"
            + "</form>\n"
            + "<div style='display:none;'><iframe name='frame' id='frame'></iframe></div>\n"
            + "<script>\n"
            + "  // Get the form and set the target\n"
            + "  var form = document.getElementById('form1');\n"
            + "  var iframe = document.getElementById('frame');\n"
            + "  // Finally submit the form with a delay to make sure that the onload of the iframe\n"
            + "  // is called for the submit and not for the page creation\n"
            + "  var t = setTimeout(function() {\n"
            + "    clearTimeout(t);\n"
            + "    iframe.attachEvent('onload', function() {\n"
            + "      alert('submitted ' + iframe.contentWindow.document.body.getAttribute('id'));\n"
            + "    });\n"
            + "    alert('submit form');\n"
            + "    form.submit();\n"
            + "  }, 1000);\n"
            + "</script>\n"
            + "</body></html>";
        final String html2
            = "<?xml version='1.0'?>\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml'><body id='ok'><span id='result'>OK</span></html>";
        getMockWebConnection().setDefaultResponse(html2);
        loadPageWithAlerts(html, getDefaultUrl(), 5000);
    }

}
