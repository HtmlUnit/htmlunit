/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ImmediateRefreshHandler;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.OnbeforeunloadHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElementTest.HtmlAttributeChangeListenerTestImpl;
import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.TextUtils;

/**
 * Tests for {@link HtmlPage}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlPageTest extends SimpleWebTestCase {

    /** The doctype prefix for standards mode. */
    public static final String STANDARDS_MODE_PREFIX_
        = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmit() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' action='/formSubmit' method='PoSt'>\n"
            + "<input type='text' NAME='textInput1' value='textInput1'/>\n"
            + "<input type='text' name='textInput2' value='textInput2'/>\n"
            + "<input type='hidden' name='hidden1' value='hidden1'/>\n"
            + "<input type='submit' name='submitInput1' value='push me'/>\n"
            + "</form>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlInput textInput = form.getInputByName("textInput1");
        textInput.setValue("foo");

        final HtmlSubmitInput button = form.getInputByName("submitInput1");
        final HtmlPage secondPage = button.click();

        final List<NameValuePair> expectedParameters = new ArrayList<>();
        expectedParameters.add(new NameValuePair("textInput1", "foo"));
        expectedParameters.add(new NameValuePair("textInput2", "textInput2"));
        expectedParameters.add(new NameValuePair("hidden1", "hidden1"));
        expectedParameters.add(new NameValuePair("submitInput1", "push me"));

        final URL expectedUrl = new URL(URL_FIRST, "formSubmit");
        final URL actualUrl = secondPage.getUrl();
        assertEquals("url", expectedUrl, actualUrl);
        assertSame("method", HttpMethod.POST, webConnection.getLastMethod());
        assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
        assertNotNull(secondPage);
    }

    /**
     * Tests getHtmlElement() for all elements that can be loaded.
     * @throws Exception if the test fails
     */
    @Test
    public void getHtmlElement() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <p>hello world</p>\n"
            + "  <form id='form1' id='form1' action='/formSubmit' method='post'>\n"
            + "  <input type='text' NAME='textInput1' value='textInput1'/>\n"
            + "  <button type='submit' name='button1'>foobar</button>\n"
            + "  <select name='select1'>\n"
            + "    <option value='option1'>Option1</option>\n"
            + "  </select>\n"
            + "  <textarea name='textArea1'>foobar</textarea>\n"
            + "  </form>\n"
            + "  <a href='http://www.foo.com' name='anchor1'>foo.com</a>\n"
            + "  <table id='table1'>\n"
            + "    <tr>\n"
            + "      <th id='header1'>Header</th>\n"
            + "      <td id='data1'>Data</td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = page.getHtmlElementById("form1");
        assertSame("form1", form, page.getHtmlElementById("form1")); //huh??

        final HtmlInput input = form.getInputByName("textInput1");
        assertSame("input1", input, form.getInputByName("textInput1")); //??

        final HtmlButton button = form.getButtonByName("button1");
        assertSame("button1", button, form.getButtonByName("button1"));

        final HtmlSelect select = form.getSelectsByName("select1").get(0);
        assertSame("select1", select, form.getSelectsByName("select1").get(0));

        final HtmlOption option = select.getOptionByValue("option1");
        assertSame("option1", option, select.getOptionByValue("option1"));

        final HtmlTable table = page.getHtmlElementById("table1");
        assertSame("table1", table, page.getHtmlElementById("table1"));

        final HtmlAnchor anchor = page.getAnchorByName("anchor1");
        assertSame("anchor1", anchor, page.getAnchorByName("anchor1"));
        assertSame("anchor3", anchor, page.getAnchorByHref("http://www.foo.com"));
        assertSame("anchor4", anchor, page.getAnchorByText("foo.com"));

        final HtmlTableRow tableRow = table.getRow(0);
        assertSame("tableRow1", tableRow, table.getRow(0));

        final HtmlTableHeaderCell tableHeaderCell = (HtmlTableHeaderCell) tableRow.getCell(0);
        assertSame("tableHeaderCell1", tableHeaderCell, tableRow.getCell(0));
        assertSame("tableHeaderCell2", tableHeaderCell, page.getHtmlElementById("header1"));

        final HtmlTableDataCell tableDataCell = (HtmlTableDataCell) tableRow.getCell(1);
        assertSame("tableDataCell1", tableDataCell, tableRow.getCell(1));
        assertSame("tableDataCell2", tableDataCell, page.getHtmlElementById("data1"));

        final HtmlTextArea textArea = form.getTextAreaByName("textArea1");
        assertSame("textArea1", textArea, form.getTextAreaByName("textArea1"));
    }

    /**
     * Tests getHtmlElement() for all elements that can be loaded.
     * @throws Exception if the test fails
     */
    @Test
    public void getAnchorByText() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <a href='http://www.foo.com' id='anchor1'>anchor text</a>\n"
            + "  <a href='http://www.foo.com' id='anchor2'><span>anchor text inside span</span></a>\n"
            + "  <a href='http://www.foo.com' id='anchor3'>"
                + "<svg><rect x='1' y='11' width='8' height='8'/></svg>"
                + "<span>complex</span>"
                + "</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        assertSame("anchor1", page.getElementById("anchor1"), page.getAnchorByText("anchor text"));
        assertSame("anchor2", page.getElementById("anchor2"), page.getAnchorByText("anchor text inside span"));
        assertSame("anchor3", page.getElementById("anchor3"), page.getAnchorByText("complex"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getTabbableElements_None() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<table><tr><td>foo</td></tr></table>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        assertEquals(Collections.EMPTY_LIST, page.getTabbableElements());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getTabbableElements_OneEnabled_OneDisabled() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<form><p>hello world</p>\n"
            + "<input name='foo' type='submit' disabled='disabled' id='foo'/>\n"
            + "<input name='bar' type='submit' id='bar'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List<HtmlElement> expectedElements = new ArrayList<>();
        expectedElements.add(page.getHtmlElementById("bar"));

        assertEquals(expectedElements, page.getTabbableElements());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getTabbableElements() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<a id='a' tabindex='1'>foo</a>\n"
            + "<a id='b'>foo</a>\n"
            + "<form>\n"
            + "<a id='c' tabindex='3'>foo</a>\n"
            + "<a id='d' tabindex='2'>foo</a>\n"
            + "<a id='e' tabindex='0'>foo</a>\n"
            + "</form>\n"
            + "<a id='f' tabindex='3'>foo</a>\n"
            + "<a id='g' tabindex='1'>foo</a>\n"
            + "<a id='q' tabindex='-1'>foo</a>\n"
            + "<form><p>hello world</p>\n"
            + "<input name='foo' type='submit' disabled='disabled' id='foo'/>\n"
            + "<input name='bar' type='submit' id='bar'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List<HtmlElement> expectedElements = Arrays.asList(new HtmlElement[] {page.getHtmlElementById("a"),
                page.getHtmlElementById("g"), page.getHtmlElementById("d"),
                page.getHtmlElementById("c"), page.getHtmlElementById("f"),
                page.getHtmlElementById("e"), page.getHtmlElementById("b"),
                page.getHtmlElementById("bar")});

        assertEquals(expectedElements, page.getTabbableElements());

        final String[] expectedIds = {"a", "g", "d", "c", "f", "e", "b", "bar"};
        assertEquals(expectedIds, page.getTabbableElementIds());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getHtmlElementByAccessKey() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<a id='a' accesskey='a'>foo</a>\n"
            + "<a id='b'>foo</a>\n"
            + "<form>\n"
            + "<a id='c' accesskey='c'>foo</a>\n"
            + "</form>\n"
            + "<form><p>hello world</p>\n"
            + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>\n"
            + "<input name='bar' type='submit' id='bar'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        assertEquals(page.getHtmlElementById("a"), page.getHtmlElementByAccessKey('A'));
        assertEquals(page.getHtmlElementById("c"), page.getHtmlElementByAccessKey('c'));
        assertNull(page.getHtmlElementByAccessKey('z'));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getHtmlElementsByAccessKey() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head><body>\n"
            + "<a id='a' accesskey='a'>foo</a>\n"
            + "<a id='b' accesskey='a'>foo</a>\n"
            + "<form>\n"
            + "<a id='c' accesskey='c'>foo</a>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List<HtmlElement> expectedElements = Arrays.asList(new HtmlElement[] {page.getHtmlElementById("a"),
                page.getHtmlElementById("b")});
        final List<HtmlElement> collectedElements = page.getHtmlElementsByAccessKey('a');
        assertEquals(expectedElements, collectedElements);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getFullQualifiedUrl_NoBaseSpecified() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<table><tr><td><input type='text' id='foo'/></td></tr></table>\n"
            + "</form></body></html>";
        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final String urlString = URL_FIRST.toExternalForm();
        final HtmlPage page = client.getPage(URL_FIRST);

        assertEquals(urlString, page.getFullyQualifiedUrl(""));
        assertEquals(urlString + "foo", page.getFullyQualifiedUrl("foo"));
        assertEquals("http://foo.com/bar", page.getFullyQualifiedUrl("http://foo.com/bar"));
        assertEquals("mailto:me@foo.com", page.getFullyQualifiedUrl("mailto:me@foo.com"));

        assertEquals(urlString + "foo", page.getFullyQualifiedUrl("foo"));
        assertEquals(urlString + "bbb", page.getFullyQualifiedUrl("aaa/../bbb"));
        assertEquals(urlString + "c/d", page.getFullyQualifiedUrl("c/./d"));

        final HtmlPage secondPage = client.getPage(urlString + "foo/bar?a=b&c=d");
        assertEquals(urlString + "foo/bar?a=b&c=d", secondPage.getFullyQualifiedUrl(""));
        assertEquals(urlString + "foo/one", secondPage.getFullyQualifiedUrl("one"));
        assertEquals(urlString + "two", secondPage.getFullyQualifiedUrl("/two"));
        assertEquals(urlString + "foo/two?a=b", secondPage.getFullyQualifiedUrl("two?a=b"));

        final HtmlPage thirdPage = client.getPage("http://foo.com/dog/cat/one.html");
        assertEquals("http://foo.com/dog/cat/one.html", thirdPage.getFullyQualifiedUrl(""));
        assertEquals("http://foo.com/dog/cat/two.html", thirdPage.getFullyQualifiedUrl("two.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getFullQualifiedUrl_WithBase() throws Exception {
        testGetFullQualifiedUrl_WithBase("http", "");
        testGetFullQualifiedUrl_WithBase("http", ":8080");
        testGetFullQualifiedUrl_WithBase("https", "");
        testGetFullQualifiedUrl_WithBase("https", ":2005");
    }

    /**
     * @throws Exception if the test fails
     */
    private void testGetFullQualifiedUrl_WithBase(final String baseProtocol, final String basePortPart)
        throws Exception {

        final String baseUrl = baseProtocol + "://second" + basePortPart;
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "<base href='" + baseUrl + "'>\n"
            + "</head><body>\n"
            + "<form id='form1'>\n"
            + "<table><tr><td><input type='text' id='foo'/></td></tr></table>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        assertEquals(baseUrl, page.getFullyQualifiedUrl(""));
        assertEquals(baseUrl + "/foo", page.getFullyQualifiedUrl("foo"));
        assertEquals(baseUrl + "/foo.js", page.getFullyQualifiedUrl("/foo.js"));
        assertEquals("http://foo.com/bar", page.getFullyQualifiedUrl("http://foo.com/bar"));
        assertEquals("mailto:me@foo.com", page.getFullyQualifiedUrl("mailto:me@foo.com"));

        assertEquals(baseUrl + "/bbb", page.getFullyQualifiedUrl("aaa/../bbb"));
        assertEquals(baseUrl + "/c/d", page.getFullyQualifiedUrl("c/./d"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = MalformedURLException.class)
    public void getFullQualifiedUrl_invalid() throws Exception {
        final String htmlContent = "<html><body></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        page.getFullyQualifiedUrl("http://");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testBase_Multiple() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<base href='" + URL_SECOND + "'>\n"
            + "<base href='" + URL_THIRD + "'>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a href='somepage.html'>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();
        final List<String> collectedIncorrectness = new ArrayList<>();
        final IncorrectnessListener listener = new IncorrectnessListener() {
            @Override
            public void notify(final String message, final Object origin) {
                collectedIncorrectness.add(message);
            }
        };
        webClient.setIncorrectnessListener(listener);

        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);
        webConnection.setDefaultResponse(html);
        final HtmlPage page = webClient.getPage(URL_FIRST);
        page.getAnchors().get(0).click();

        final String[] expectedIncorrectness = {"Multiple 'base' detected, only the first is used.",
            "Multiple 'base' detected, only the first is used."};
        assertEquals(expectedIncorrectness, collectedIncorrectness);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testBase_InsideBody() throws Exception {
        final String html = "<html><body>\n"
            + "  <base href='" + URL_SECOND + "'>\n"
            + "  <a href='somepage.html'>link</a>\n"
            + "</body></html>";

        final WebClient webClient = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);
        webConnection.setDefaultResponse(html);
        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getAnchors().get(0);
        final HtmlPage secondPage = anchor.click();

        assertEquals(URL_SECOND + "somepage.html", secondPage.getUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler_BodyStatement() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "</head><body onLoad='alert(\"foo\")'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * If the onload handler contains two statements then only the first would execute.
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler_TwoBodyStatements() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "</head><body onLoad='alert(\"foo\");alert(\"bar\")'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"foo", "bar"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug #69.
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler_BodyName() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "<script type='text/javascript'>\n"
            + "  window.onload = function() {alert('foo')}</script>\n"
            + "</head><body></body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug #69.
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler_BodyName_NotAFunction() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head>\n"
            + "<body onLoad='foo=4711'>\n"
            + "<a name='alert' href='javascript:alert(foo)'/>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        page.getAnchorByName("alert").click();

        final String[] expectedAlerts = {"4711"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for window.onload property.
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadHandler_ScriptName() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "<script type='text/javascript'>\n"
            + "load=function() {alert('foo')};\n"
            + "onload=load\n"
            + "</script></head><body></body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final String[] expectedAlerts = {"foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void embeddedMetaTag_Regression() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<table><tr><td>\n"
            + "<meta name=vs_targetSchema content=\"http://schemas.microsoft.com/intellisense/ie5\">\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</td></tr></table>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();

        // This used to blow up on page load
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List<?> expectedAlerts = Collections.EMPTY_LIST;
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Verifies that an empty charset in a content-type meta tag is ignored. See bug #752.
     * @throws Exception if an error occurs
     */
    @Test
    public void getPageEncoding_EmptyCharset() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='Content-Type' content='text/html; charset='>\n"
            + "</head><body>abc</body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals(ISO_8859_1, page.getCharset());
    }

    /**
     * Bug #914.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void getPageEncoding_HeaderHasPrecedenceOverMetaTag() throws Exception {
        final String html = "<html><head><meta content='text/html; charset=iso-8859-1' http-equiv='Content-Type'/>\n"
            + "</head><body></body></html>";
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html, "text/html; charset=UTF-8");
        final WebClient client = getWebClient();
        client.setWebConnection(conn);
        final HtmlPage page = client.getPage(URL_FIRST);
        assertSame(UTF_8, page.getCharset());
        assertEquals(page.getWebResponse().getContentCharset(), page.getCharset());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getForms() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<form name='one'>\n"
            + "<a id='c' accesskey='c'>foo</a>\n"
            + "</form>\n"
            + "<form name='two'>\n"
            + "<a id='c' accesskey='c'>foo</a>\n"
            + "</form>\n"
            + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>\n"
            + "<input name='bar' type='submit' id='bar'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final List<HtmlForm> expectedForms = Arrays.asList(new HtmlForm[] {page.getFormByName("one"),
                page.getFormByName("two")});
        assertEquals(expectedForms, page.getForms());
    }

    /**
     * Test auto-refresh from a meta tag.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTag_DefaultRefreshHandler() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"2;URL=§§URL§§\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTag_caseSensitivity() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"2;Url=§§URL§§\">");
    }

    /**
     * Regression test for bug #954.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTag_spaceSeparator() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"2 Url=§§URL§§\">");
        testRefresh_MetaTag("<META HTTP-EQUIV=\"Refresh\" CONTENT=\"2\nUrl=§§URL§§\">");
    }

    /**
     * Test auto-refresh from a meta tag with no URL.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTag_NoUrl() throws Exception {
        final String firstContent = "<html><head><title>first</title>\n"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"1\">\n"
            + "</head><body></body></html>";

        final WebClient client = getWebClient();
        final List<Object> collectedItems = new ArrayList<>();
        client.setRefreshHandler(new LoggingRefreshHandler(collectedItems));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);

        // avoid using equals() on URL because it takes to much time (due to ip resolution)
        assertEquals("first", collectedItems.get(0));
        assertEquals(URL_FIRST, (URL) collectedItems.get(1));
        assertEquals(Integer.valueOf(1), collectedItems.get(2));
    }

    /**
     * Ensures that if a page is supposed to refresh itself every certain amount of
     * time, and the ImmediateRefreshHandler is being used, an OOME is avoided by
     * not performing the refresh.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_ImmediateRefresh_AvoidOOME() throws Exception {
        final String firstContent = "<html><head><title>first</title>\n"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"1\">\n"
            + "</head><body></body></html>";

        final WebClient client = getWebClient();
        assertTrue(ImmediateRefreshHandler.class.isInstance(client.getRefreshHandler()));
        try {
            loadPage(firstContent);
            fail("should have thrown");
        }
        catch (final RuntimeException e) {
            assertTrue(e.getMessage().indexOf("could have caused an OutOfMemoryError") > -1);
        }
        Thread.sleep(1000);
    }

    /**
     * Test auto-refresh from a meta tag with URL quoted.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTagQuoted() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT='0;URL=\"§§URL§§\"'>");
    }

    /**
     * Test auto-refresh from a meta tag with URL partly quoted.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTagPartlyQuoted() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT=\"0;URL='§§URL§§\">");
    }

    private void testRefresh_MetaTag(final String metaTag) throws Exception {
        final String firstContent = "<html><head><title>first</title>\n"
            + metaTag.replace("§§URL§§", URL_SECOND.toString()) + "\n"
            + "<META HTTP-EQUIV='Refresh' CONTENT=\"0;URL='" + URL_SECOND + "\">\n"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        assertEquals("second", page.getTitleText());
    }

    /**
     * Test auto-refresh from a meta tag inside noscript.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTagNoScript() throws Exception {
        final String firstContent = "<html><head><title>first</title>\n"
            + "<noscript>\n"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;URL=" + URL_SECOND + "\">\n"
            + "</noscript>\n"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        client.getOptions().setJavaScriptEnabled(false);
        page = client.getPage(URL_FIRST);
        assertEquals("second", page.getTitleText());
    }

    /**
     * Test auto-refresh from a meta tag with a refresh handler that doesn't refresh.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTag_CustomRefreshHandler() throws Exception {
        final String firstContent = "<html><head><title>first</title>\n"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"3;URL=" + URL_SECOND + "\">\n"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = getWebClient();
        final List<Object> collectedItems = new ArrayList<>();
        client.setRefreshHandler(new LoggingRefreshHandler(collectedItems));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        assertEquals("first", page.getTitleText());

        // avoid using equals() on URL because it takes to much time (due to ip resolution)
        assertEquals("first", collectedItems.get(0));
        assertEquals(URL_SECOND, (URL) collectedItems.get(1));
        assertEquals(Integer.valueOf(3), collectedItems.get(2));
    }

    /**
     * Test that whitespace before and after ';' is permitted.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_MetaTag_Whitespace() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT='0  ;  URL=§§URL§§'>");
    }

    /**
     * Test that the refresh time can be a double ("3.4", for example), not just an integer.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void refresh_MetaTag_Double() throws Exception {
        testRefresh_MetaTag("<META HTTP-EQUIV='Refresh' CONTENT='1.2  ;  URL=§§URL§§'>");
    }

    /**
     * Test auto-refresh from a response header.
     * @throws Exception if the test fails
     */
    @Test
    public void refresh_HttpResponseHeader() throws Exception {
        final String firstContent = "<html><head><title>first</title>\n"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", MimeType.TEXT_HTML, Collections
                .singletonList(new NameValuePair("Refresh", "2;URL=" + URL_SECOND)));
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        assertEquals("second", page.getTitleText());
    }

    /**
     * Test that the parent of the DOM Document (HtmlPage) is null.
     * @throws Exception if the test fails
     */
    @Test
    public void documentParentIsNull() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        assertNotNull(page);
        assertNull(page.getParentNode());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void documentElement() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final DomElement root = page.getDocumentElement();

        assertNotNull(root);
        assertEquals("html", root.getTagName());
        assertSame(page, root.getParentNode());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void documentNodeType() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final DomElement root = page.getDocumentElement();

        assertEquals(org.w3c.dom.Node.DOCUMENT_NODE, page.getNodeType());
        assertEquals(org.w3c.dom.Node.ELEMENT_NODE, root.getNodeType());
        assertEquals("#document", page.getNodeName());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void deregisterFrameWithoutSrc() throws Exception {
        final String htmlContent = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<iframe></iframe>\n"
            + "<a href='about:blank'>link</a>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor link = page.getAnchors().get(0);
        link.click();
    }

    /**
     * Tests that a return statement in onload doesn't throw any exception.
     * @throws Exception if the test fails
     */
    @Test
    public void onLoadReturn() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head>\n"
            + "<body onload='return true'>\n"
            + "</body></html>";

        loadPage(htmlContent);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head>"
            + "<body><p>helloworld</p></body>"
            + "</html>";

        final HtmlPage page = loadPage(htmlContent);
        String xml = page.asXml();
        final String prefix = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
        assertTrue(xml.startsWith(prefix));
        xml = xml.substring(prefix.length());
        assertEquals(htmlContent, xml.replaceAll("\\s", ""));
    }

    /**
     * Tests that the generated XML is valid as HTML code too.
     * @exception Exception If the test fails
     */
    @Test
    public void asXmlValidHtmlOutput() throws Exception {
        final String html =
            "<html><head><title>foo</title>"
            + "<script src='script.js'></script></head>"
            + "<body><div></div><iframe src='about:blank'></iframe></body>"
            + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        webConnection.setResponse(new URL(URL_FIRST, "script.js"), "", "text/javascript");
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        String xml = page.asXml();
        final String prefix = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
        assertTrue(xml.startsWith(prefix));
        xml = xml.substring(prefix.length());
        assertEquals(html, xml
                .replaceAll("[\\n\\r]", "")
                .replaceAll("\\s\\s+", "")
                .replaceAll("\"", "'"));
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void asXml2() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "<script>var test = 15 < 16;</script></head>\n"
            + "</head>\n"
            + "<body onload='test=(1 > 2) && (45 < 78)'><p>helloworld &amp;amp; helloall</p></body>\n"
            + "</html>";

        final HtmlPage page = loadPage(htmlContent);
        assertNotNull("xml document could not be parsed", page.asXml());
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        builder.parse(TextUtils.toInputStream(page.asXml()));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml_unicode() throws Exception {
        final String unicodeString = "\u064A\u0627 \u0644\u064A\u064A\u0644";
        final String html = "<html>\n"
            + "<head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head>\n"
            + "<body><span id='foo'>" + unicodeString + "</span></body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse(TextUtils.stringToByteArray(html, UTF_8), 200, "OK", MimeType.TEXT_HTML);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final String xml = page.asXml();
        assertTrue(xml.contains("<?xml "));
        assertTrue(xml.contains(unicodeString));
    }

    /**
     * @exception Exception if the test fails
     */
    @Test
    public void getElementsById() throws Exception {
        final String html = "<html><body><div id='a'>foo</div><div id='b'/><div id='b'/></body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals(1, page.getElementsById("a").size());
        assertEquals(2, page.getElementsById("b").size());
        assertEquals(0, page.getElementsById("c").size());

        final DomElement a = page.getElementsById("a").get(0);
        a.remove();
        assertEquals(0, page.getElementsById("a").size());

        final DomElement b1 = page.getElementsById("b").get(0);
        b1.appendChild(a);
        assertEquals(1, page.getElementsById("a").size());
    }

    /**
     * @exception Exception if the test fails
     */
    @Test
    public void getElementsByName() throws Exception {
        final String html = "<html><body><div name='a'>foo</div><div name='b'/><div name='b'/></body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals(1, page.getElementsByName("a").size());
        assertEquals(2, page.getElementsByName("b").size());
        assertEquals(0, page.getElementsByName("c").size());

        final DomElement a = page.getElementsByName("a").get(0);
        a.remove();
        assertEquals(0, page.getElementsByName("a").size());

        final DomElement b1 = page.getElementsByName("b").get(0);
        b1.appendChild(a);
        assertEquals(1, page.getElementsByName("a").size());
    }

    /**
     * @exception Exception if the test fails
     */
    @Test
    public void getElementByName() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='a' name='a'>foo</div>\n"
            + "<div id='b1' name='b'>bar</div>\n"
            + "<div id='b2' name='b'>baz</div></body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals(page.getElementById("a"), page.getElementByName("a"));
        assertEquals(page.getElementById("b1"), page.getElementByName("b"));

        page.getElementByName("b").remove();
        assertEquals(page.getElementById("b2"), page.getElementByName("b"));

        boolean thrown = false;
        try {
            page.getElementByName("c");
        }
        catch (final ElementNotFoundException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    /**
     * @exception Exception if the test fails
     */
    @Test
    public void getHtmlElementsByIdAndOrName() throws Exception {
        final String html = "<html><body><div name='a' id='a'>foo</div><div name='b' id='c'>bar</div>"
                            + "<div name='b' id='d'>bar</div></body></html>";
        final HtmlPage page = loadPage(html);
        assertEquals(1, page.getElementsByIdAndOrName("a").size());
        assertEquals(2, page.getElementsByIdAndOrName("b").size());
        assertEquals(1, page.getElementsByIdAndOrName("c").size());
        assertEquals(1, page.getElementsByIdAndOrName("d").size());

        final DomElement a = page.getElementsByIdAndOrName("a").get(0);
        a.remove();
        assertEquals(0, page.getElementsByIdAndOrName("a").size());

        final DomElement b1 = page.getElementsByIdAndOrName("b").get(0);
        b1.appendChild(a);
        assertEquals(1, page.getElementsByIdAndOrName("a").size());
    }

    /**
     * Regression test for bug #287.
     * @exception Exception If the test fails
     */
    @Test
    public void getHtmlElementByIdAfterRemove() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<div id='div1'>\n"
            + "<div id='div2'>\n"
            + "</div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement div1 = page.getHtmlElementById("div1");
        page.getHtmlElementById("div2"); // would throw if not found
        div1.remove();
        try {
            page.getHtmlElementById("div1"); // throws if not found
            fail("div1 should have been removed");
        }
        catch (final ElementNotFoundException e) {
            // nothing
        }

        try {
            page.getHtmlElementById("div2"); // throws if not found
            fail("div2 should have been removed");
        }
        catch (final ElementNotFoundException e) {
            // nothing
        }
    }

    /**
     * Test getHtmlElementById() when 2 elements have the same id and the first one
     * is removed.
     * @exception Exception If the test fails
     */
    @Test
    public void getHtmlElementById_idTwice() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body>\n"
            + "<div id='id1'>foo</div>\n"
            + "<span id='id1'>bla</span>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement elt1 = page.getHtmlElementById("id1");
        assertEquals("div", elt1.getNodeName());
        elt1.remove();
        assertEquals("span", page.getHtmlElementById("id1").getNodeName());
    }

    /**
     * Test the "set-cookie" meta tag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("webm=none")
    public void setCookieMetaTag() throws Exception {
        final String content = "<html><head>\n"
            + "<meta http-equiv='set-cookie' content='webm=none; path=/;'>\n"
            + "</head><body>\n"
            + "<script>document.title = document.cookie</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        assertEquals(getExpectedAlerts()[0], page.getTitleText());

        final Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies();
        assertEquals(1, cookies.size());
        final Cookie cookie = cookies.iterator().next();
        assertEquals(page.getUrl().getHost(), cookie.getDomain());
        assertEquals("webm", cookie.getName());
        assertEquals("none", cookie.getValue());
        assertEquals("/", cookie.getPath());
    }

    /**
     * Test for bug #474.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(IE) // in fact IE seems to perform other requests
    public void noSlashURL() throws Exception {
        testNoSlashURL("http:/second");
        testNoSlashURL("http:second");
    }

    private void testNoSlashURL(final String url) throws Exception {
        final String firstContent
            = "<html><body>\n"
            + "<iframe id='myIFrame' src='" + url + "'></iframe>\n"
            + "</body></html>";

        final String secondContent = "<html><body></body></html>";
        final WebClient client = getWebClient();

        final URL secondURL = new URL("http://second/");
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(secondURL, secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlInlineFrame iframe = firstPage.getHtmlElementById("myIFrame");

        assertEquals(secondURL, iframe.getEnclosedPage().getUrl());
    }

    /**
     * @throws Exception failure
     */
    @Test
    public void metaTagWithEmptyURL() throws Exception {
        final WebClient client = getWebClient();
        client.setRefreshHandler(new ImmediateRefreshHandler());

        // connection will return a page with <meta ... refresh> for the first call
        // and the same page without it for the other calls
        final MockWebConnection webConnection = new MockWebConnection() {
            private int nbCalls_ = 0;
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                String content = "<html><head>\n";
                if (nbCalls_ == 0) {
                    content += "<meta http-equiv='refresh' content='1; URL='>\n";
                }
                content += "</head><body></body></html>";
                nbCalls_++;

                final StringWebResponse response = new StringWebResponse(content, request.getUrl());
                response.getWebRequest().setHttpMethod(request.getHttpMethod());
                return response;
            }
        };
        client.setWebConnection(webConnection);

        final WebRequest request = new WebRequest(URL_FIRST);
        request.setHttpMethod(HttpMethod.POST);
        client.getPage(request);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void serialization() throws Exception {
        // The document.all and form.elements calls are important because they trigger the creation
        // of HTMLCollections, which have caused serialization problems in the past (see bug #606).

        final String content =
              "<html><body>\n"
            + "<div id='myId'>Hello there!</div>\n"
            + "<script>\n"
            + "  var x = document.all;\n"
            + "  window.onload = function() {alert('foo')};\n"

            // this tests 3103703
            // we don't store the jobs are pending at the moment of serialization
            + "  var aktiv = window.setInterval('foo()', 1000);\n"
            + "  var i = 0;\n"
            + "  function foo() {\n"
            + "    i = i + 1;\n"
            + "    if (i >= 10)\n"
            + "      window.clearInterval(aktiv);\n"
            + "  }\n"
            + "</script>\n"
            + "<form name='f' id='f'></form>\n"
            + "<script>var y = document.getElementById('f').elements;</script>\n"
            + "</body></html>";

        // waiting for the alerts creates some more js objects associated with the page
        // this tests 3103703
        final List<String> expectedAlerts = new LinkedList<>();
        expectedAlerts.add("foo");

        final HtmlPage page1 = loadPage(content, expectedAlerts);
        final byte[] bytes = SerializationUtils.serialize(page1);

        final HtmlPage page2 = (HtmlPage) SerializationUtils.deserialize(bytes);

        final Iterator<HtmlElement> iterator1 = page1.getHtmlElementDescendants().iterator();
        final Iterator<HtmlElement> iterator2 = page2.getHtmlElementDescendants().iterator();
        while (iterator1.hasNext()) {
            assertTrue(iterator2.hasNext());
            final HtmlElement element1 = iterator1.next();
            final HtmlElement element2 = iterator2.next();
            assertEquals(element1.getNodeName(), element2.getNodeName());
        }
        assertFalse(iterator2.hasNext());
        assertEquals("Hello there!", page2.getHtmlElementById("myId").getFirstChild().getNodeValue());
    }

    /**
     * Verifies that a cloned HtmlPage has its own "idMap_".
     * @throws Exception if the test fails
     */
    @Test
    public void clonedPageHasOwnIdMap() throws Exception {
        final String content = "<html><head><title>foo</title>"
            + "<body>"
            + "<div id='id1' class='cl1'><div id='id2' class='cl2'></div></div>"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlElement id1 = (HtmlElement) page.getDocumentElement().getLastChild().getLastChild();
        assertEquals("id1", id1.getId());
        assertSame(id1, page.getHtmlElementById("id1"));
        final HtmlPage clone = page.cloneNode(true);
        assertSame(id1, page.getHtmlElementById("id1"));
        final HtmlElement id1clone = (HtmlElement) clone.getDocumentElement().getLastChild().getLastChild();
        assertNotSame(id1, id1clone);
        assertEquals("id1", id1clone.getId());
        assertSame(id1clone, clone.getHtmlElementById("id1"));
        assertNotSame(id1clone, page.getHtmlElementById("id1"));
        assertNotSame(id1, clone.getHtmlElementById("id1"));

        page.getHtmlElementById("id2").remove();
        try {
            page.getHtmlElementById("id2");
            fail("should have thrown ElementNotFoundException");
        }
        catch (final ElementNotFoundException enfe) {
            // expected
        }
        assertNotNull(clone.getHtmlElementById("id2"));
    }

    /**
     * Verifies that a cloned HtmlPage has its own "documentElement".
     * @throws Exception if the test fails
     */
    @Test
    public void clonedPageHasOwnDocumentElement() throws Exception {
        final String content = "<html><head><title>foo</title>\n"
            + "<body>\n"
            + "<div id='id1' class='cl1'><div id='id2' class='cl2'></div></div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlPage clone = page.cloneNode(true);
        assertTrue(page != clone);
        final DomElement doc = page.getDocumentElement();
        final DomElement docclone = clone.getDocumentElement();
        assertTrue(doc != docclone);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlAttributeChangeListener_AddAttribute() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.setAttribute('title', 'myTitle');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues = {"attributeAdded: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        page.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlAttributeChangeListener_ReplaceAttribute() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.setAttribute('title', p1.getAttribute('title') + 'a');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues = {"attributeReplaced: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        page.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlAttributeChangeListener_RemoveAttribute() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.removeAttribute('title');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues = {"attributeRemoved: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        page.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlAttributeChangeListener_RemoveListener() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function clickMe() {\n"
            + "    var p1 = document.getElementById('p1');\n"
            + "    p1.setAttribute('title', p1.getAttribute('title') + 'a');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "<input id='myButton' type='button' onclick='clickMe()'>\n"
            + "</body></html>";

        final String[] expectedValues = {"attributeReplaced: p,title,myTitle"};
        final HtmlPage page = loadPage(htmlContent);
        final HtmlAttributeChangeListenerTestImpl listenerImpl = new HtmlAttributeChangeListenerTestImpl();
        page.addHtmlAttributeChangeListener(listenerImpl);
        final HtmlButtonInput myButton = page.getHtmlElementById("myButton");

        myButton.click();
        page.removeHtmlAttributeChangeListener(listenerImpl);
        myButton.click();
        assertEquals(expectedValues, listenerImpl.getCollectedValues());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void htmlAttributeChangeListener_ListenerRegistersNewListener() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<p id='p1' title='myTitle'></p>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final List<String> collector = new ArrayList<>();
        final HtmlAttributeChangeListener listener2 = new HtmlAttributeChangeListenerTestImpl() {
            @Override
            public void attributeReplaced(final HtmlAttributeChangeEvent event) {
                collector.add("in listener 2");
            }
        };
        final HtmlAttributeChangeListener listener1 = new HtmlAttributeChangeListenerTestImpl() {
            @Override
            public void attributeReplaced(final HtmlAttributeChangeEvent event) {
                collector.add("in listener 1");
                page.addHtmlAttributeChangeListener(listener2);
            }
        };

        page.addHtmlAttributeChangeListener(listener1);

        final HtmlElement p = page.getHtmlElementById("p1");
        p.setAttribute("title", "new title");

        final String[] expectedValues = {"in listener 1"};
        assertEquals(expectedValues, collector);
        collector.clear();

        p.setAttribute("title", "new new title");
        final String[] expectedValues2 = {"in listener 1", "in listener 2"};
        assertEquals(expectedValues2, collector);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void caseInsensitiveRegexReplacement() throws Exception {
        final String html = "<html><body><script>\n"
            + "var r = /^([#.]?)([a-z0-9\\*_-]*)/i;\n"
            + "var s = '#userAgent';\n"
            + "s = s.replace(r, '');\n"
            + "alert(s.length);\n"
            + "</script></body></html>";
        final String[] expectedAlerts = {"0"};
        final List<String> actualAlerts = new ArrayList<>();
        loadPage(html, actualAlerts);
        assertEquals(expectedAlerts, actualAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void regexReplacementWithFunction() throws Exception {
        final String html = "<html><body><script>\n"
            + "var r = /-([a-z])/ig;\n"
            + "var s = 'font-size';\n"
            + "s = s.replace(r, function(z,b){return b.toUpperCase();});\n"
            + "alert(s);\n"
            + "</script></body></html>";
        final String[] expectedAlerts = {"fontSize"};
        final List<String> collectedAlerts = new ArrayList<>();
        loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void title_EmptyXmlTagExpanded() throws Exception {
        final String content = "<html><head><title/></head>\n"
            + "<body>Hello World!</body></html>";
        final HtmlPage page = loadPage(content);
        assertTrue(page.asXml().indexOf("</title>") != -1);
    }

    /**
     * Tests getElementById() of child element after appendChild(), removeChild(), then appendChild()
     * of the parent element.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_AfterAppendRemoveAppendChild() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var table = document.createElement('table');\n"
            + "    var tr = document.createElement('tr');\n"
            + "    tr.id = 'myTR';\n"
            + "    table.appendChild(tr);\n"
            + "    document.body.appendChild(table);\n"
            + "    document.body.removeChild(table);\n"
            + "    document.body.appendChild(table);\n"
            + "    alert(document.getElementById('myTR'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        loadPage(content, collectedAlerts);
        assertFalse("null".equals(collectedAlerts.get(0)));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementById_AfterAppendingToNewlyCreatedElement() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var table = document.createElement('table');\n"
            + "    var tr = document.createElement('tr');\n"
            + "    tr.id = 'myTR';\n"
            + "    table.appendChild(tr);\n"
            + "    alert(document.getElementById('myTR'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        loadPage(content, collectedAlerts);
        assertTrue("null".equals(collectedAlerts.get(0)));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onunLoadHandler() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>\n"
            + "</head><body onunload='alert(\"foo\");alert(\"bar\")'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"foo", "bar"};
        final List<String> collectedAlerts = new ArrayList<>();
        final WebClient client = getWebClient();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, htmlContent);
        client.setWebConnection(conn);

        client.getPage(URL_FIRST);
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onbeforeunloadHandler_ok() throws Exception {
        testOnbeforeunloadHandler(true, "second");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onbeforeunloadHandler_cancel() throws Exception {
        testOnbeforeunloadHandler(false, "first");
    }

    /**
     * @param browserVersion the browser version to use
     * @param handlerOk whether <tt>OnbeforeunloadHandler.handleEvent</tt> will return {@code true} of {@code false}
     * @param expectedPageTitle the expected title of the page after clicking
     */
    private void testOnbeforeunloadHandler(final boolean handlerOk, final String expectedPageTitle) throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedConfirms = new ArrayList<>();

        webClient.setOnbeforeunloadHandler(new OnbeforeunloadHandler() {
            @Override
            public boolean handleEvent(final Page page, final String message) {
                collectedConfirms.add(message);
                return handlerOk;
            }
        });

        final String expectedMessage = "Any string value here forces a dialog box to appear before closing the window.";
        final String firstContent = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "  function closeIt(event) {\n"
            + "    event.returnValue = '" + expectedMessage + "';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onbeforeunload='closeIt(event)'>\n"
            + "  <a href='" + URL_SECOND + "'>Second page</a>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>second</title>\n"
            + "</head><body>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        final HtmlAnchor anchor = page.getAnchors().get(0);
        final HtmlPage secondPage = anchor.click();

        assertEquals(new String[] {expectedMessage}, collectedConfirms);
        assertEquals(expectedPageTitle, secondPage.getTitleText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void srcJavaScript() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<script id='ie_ready' src='javascript:void(0)'></script>\n"
            + "</body></html>";

        loadPage(htmlContent);
    }

    /**
     * Regression test for asNormalizedText() which would blow up.
     *
     * @exception Exception If the test fails
     */
    @Test
    public void asNormalizedText() throws Exception {
        final String htmlContent
            = "<html><head><title>test</title></head>\n"
            + "<body><table>\n"
            + "<tr><form><td>a</td></form></tr>\n"
            + "</table></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        page.asNormalizedText();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(firstContent);

        NodeList inputs = page.getElementsByTagName("input");
        assertEquals(1, inputs.getLength());
        assertEquals("button", inputs.item(0).getAttributes().getNamedItem("type").getNodeValue());

        final NodeList divs = page.getElementsByTagName("div");
        assertEquals(3, divs.getLength());

        final HtmlDivision newDiv = new HtmlDivision(HtmlDivision.TAG_NAME, page, null);
        page.getBody().appendChild(newDiv);
        assertEquals(4, divs.getLength());

        // case sensitive
        inputs = page.getElementsByTagName("inPUT");
        assertEquals(1, inputs.getLength());

        // empty
        inputs = page.getElementsByTagName("");
        assertEquals(0, inputs.getLength());

        // null
        inputs = page.getElementsByTagName(null);
        assertEquals(0, inputs.getLength());
    }

    /**
     * HtmlPage.getReadyState() should give the same information than the document element.
     * @see <a href="http://sourceforge.net/p/htmlunit/bugs/402/">402</a>
     * @exception Exception If the test fails
     */
    @Test
    public void readyState() throws Exception {
        final String htmlContent
            = "<html><head><title>test</title></head>\n"
            + "<body><table>\n"
            + "<tr><form><td>a</td></form></tr>\n"
            + "</table></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        assertEquals(DomNode.READY_STATE_COMPLETE, page.getReadyState());
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void cloneNode() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        page.getByXPath("//p");
        final HtmlPage clonedPage = page.cloneNode(true);
        clonedPage.getByXPath("//p");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void cloneHtmlPageWithFrame() throws Exception {
        final String html =
                "<html>\n"
                + "<head></head><body>\n"
                + "<div id='content'>"
                + "  <iframe name='content' src='frame1.html'></iframe>"
                + "</div>"
                + "</body></html>";

        final String frameContent =
                "<html>\n"
                + "<head></head>\n"
                + "<body>"
                + "  <p>frame1</p>"
                + "</body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setResponse(new URL("http://example/index.html"), html);
        webConnection.setResponse(new URL("http://example/frame1.html"), frameContent);

        final WebClient webClient = getWebClientWithMockWebConnection();

        final HtmlPage page = webClient.getPage("http://example/index.html");

        // check frame on page
        final List<FrameWindow> frames = page.getFrames();
        assertEquals(1, frames.size());
        assertEquals("frame1", ((HtmlPage) frames.get(0).getEnclosedPage()).asNormalizedText());

        // clone page with deep false
        HtmlPage clonedPage = page.cloneNode(false);

        assertEquals(1, page.getFrames().size());
        assertEquals(1, clonedPage.getFrames().size());

        // clone page with deep true
        clonedPage = page.cloneNode(true);

        // must be equals 1
        assertEquals(1, page.getFrames().size());
        assertEquals(1, clonedPage.getFrames().size());

        // clone page with deep true
        page.executeJavaScript("document.cloneNode(true)");

        // must be equals 1
        assertEquals(1, page.getFrames().size());
        assertEquals(1, clonedPage.getFrames().size());
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void addAutoCloseable() throws Exception {
        final String html = "";
        final HtmlPage page = loadPage(html);
        page.addAutoCloseable(new AutoCloseable() {
            @Override
            public void close() throws Exception {
                page.addAutoCloseable(new WebSocket());
            }
        });
        page.cleanUp();
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void addAutoCloseableNull() throws Exception {
        final String html = "";
        final HtmlPage page = loadPage(html);
        page.addAutoCloseable(null);
        page.cleanUp();
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void getBaseUrl() throws Exception {
        final String html =
                "<html>\n"
                + "<head></head>\n"
                + "<body>body</body>\n"
                + "</html>";

        HtmlPage page = loadPage(getBrowserVersion(), html, null, URL_FIRST);
        assertEquals(URL_FIRST.toExternalForm(), page.getBaseURL().toExternalForm());

        // see also com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocumentTest.baseURI_noBaseTag()
        String path = "details/abc";
        page = loadPage(getBrowserVersion(), html, null, new URL(URL_FIRST.toString() + path));
        assertEquals(URL_FIRST.toExternalForm() + path, page.getBaseURL().toExternalForm());

        path = "details/abc?x=y&z=z";
        page = loadPage(getBrowserVersion(), html, null, new URL(URL_FIRST.toString() + path));
        assertEquals(URL_FIRST.toExternalForm() + path, page.getBaseURL().toExternalForm());

        path = "details/abc;jsessionid=42?x=y&z=z";
        page = loadPage(getBrowserVersion(), html, null, new URL(URL_FIRST.toString() + path));
        assertEquals(URL_FIRST.toExternalForm() + path, page.getBaseURL().toExternalForm());
    }
}
