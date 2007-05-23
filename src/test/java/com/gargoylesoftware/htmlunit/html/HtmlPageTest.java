/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.AssertionFailedError;

import org.apache.commons.httpclient.Cookie;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.ImmediateRefreshHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for HtmlPage.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlPageTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlPageTest(final String name) {
        super(name);
    }

    /**
     * @exception  Exception If the test fails
     */
    public void testConstructor() throws Exception {
        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<p>hello world</p>"
            + "<form id='form1' action='/formSubmit' method='post'>"
            + "<input type='text' NAME='textInput1' value='textInput1'/>"
            + "<input type='text' name='textInput2' value='textInput2'/>"
            + "<input type='hidden' name='hidden1' value='hidden1'/>"
            + "<input type='submit' name='submitInput1' value='push me'/>"
            + "</form>"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        assertEquals("foo", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetInputByName() throws Exception {
        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<p>hello world</p>"
            + "<form id='form1' action='/formSubmit' method='post'>"
            + "<input type='text' NAME='textInput1' value='textInput1'/>"
            + "<input type='text' name='textInput2' value='textInput2'/>"
            + "<input type='hidden' name='hidden1' value='hidden1'/>"
            + "<input type='submit' name='submitInput1' value='push me'/>"
            + "</form>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final HtmlInput input = form.getInputByName("textInput1");
        assertEquals("name", "textInput1", input.getNameAttribute());

        assertEquals("value", "textInput1", input.getValueAttribute());
        assertEquals("type", "text", input.getTypeAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testFormSubmit() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<p>hello world</p>"
            + "<form id='form1' action='/formSubmit' method='PoSt'>"
            + "<input type='text' NAME='textInput1' value='textInput1'/>"
            + "<input type='text' name='textInput2' value='textInput2'/>"
            + "<input type='hidden' name='hidden1' value='hidden1'/>"
            + "<input type='submit' name='submitInput1' value='push me'/>"
            + "</form>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final HtmlInput textInput = form.getInputByName("textInput1");
        textInput.setValueAttribute("foo");

        final HtmlSubmitInput button = (HtmlSubmitInput) form.getInputByName("submitInput1");
        final HtmlPage secondPage = (HtmlPage) button.click();

        final List expectedParameters = new ArrayList();
        expectedParameters.add(new KeyValuePair("textInput1", "foo"));
        expectedParameters.add(new KeyValuePair("textInput2", "textInput2"));
        expectedParameters.add(new KeyValuePair("hidden1", "hidden1"));
        expectedParameters.add(new KeyValuePair("submitInput1", "push me"));

        final URL expectedUrl = new URL("http://www.gargoylesoftware.com/formSubmit");
        final URL actualUrl = secondPage.getWebResponse().getUrl();
        assertEquals("url", expectedUrl, actualUrl);
        assertEquals("method", SubmitMethod.POST, webConnection.getLastMethod());
        assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
        assertNotNull(secondPage);
    }

    /**
     *  Test getHtmlElement() for all elements that can be loaded
     *
     * @throws Exception if the test fails
     */
    public void testGetHtmlElement() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "    <p>hello world</p>"
            + "    <form id='form1' id='form1' action='/formSubmit' method='post'>"
            + "    <input type='text' NAME='textInput1' value='textInput1'/>"
            + "    <button type='submit' name='button1'>foobar</button>"
            + "    <select name='select1'>"
            + "        <option value='option1'>Option1</option>"
            + "    </select>"
            + "    <textarea name='textArea1'>foobar</textarea>"
            + "    </form>"
            + "    <a href='http://www.foo.com' name='anchor1'>foo.com</a>"
            + "    <table id='table1'>"
            + "        <tr>"
            + "            <th id='header1'>Header</th>"
            + "            <td id='data1'>Data</td>"
            + "        </tr>"
            + "    </table>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        assertSame("form1", form, page.getHtmlElementById("form1")); //huh??

        final HtmlInput input = form.getInputByName("textInput1");
        assertSame("input1", input, form.getInputByName("textInput1")); //??

        final HtmlButton button = (HtmlButton) form.getButtonsByName("button1").get(0);
        assertSame("button1", button, form.getButtonsByName("button1").get(0));

        final HtmlSelect select = (HtmlSelect) form.getSelectsByName("select1").get(0);
        assertSame("select1", select, form.getSelectsByName("select1").get(0));

        final HtmlOption option = select.getOptionByValue("option1");
        assertSame("option1", option, select.getOptionByValue("option1"));

        final HtmlTable table = (HtmlTable) page.getHtmlElementById("table1");
        assertSame("table1", table, page.getHtmlElementById("table1"));

        final HtmlAnchor anchor = page.getAnchorByName("anchor1");
        assertSame("anchor1", anchor, page.getAnchorByName("anchor1"));
        assertSame("anchor3", anchor, page.getAnchorByHref("http://www.foo.com"));
        assertSame("anchor4", anchor, page.getFirstAnchorByText("foo.com"));

        final HtmlTableRow tableRow = table.getRow(0);
        assertSame("tableRow1", tableRow, table.getRow(0));

        final HtmlTableHeaderCell tableHeaderCell = (HtmlTableHeaderCell) tableRow.getCell(0);
        assertSame("tableHeaderCell1", tableHeaderCell, tableRow.getCell(0));
        assertSame("tableHeaderCell2", tableHeaderCell, page.getHtmlElementById("header1"));

        final HtmlTableDataCell tableDataCell = (HtmlTableDataCell) tableRow.getCell(1);
        assertSame("tableDataCell1", tableDataCell, tableRow.getCell(1));
        assertSame("tableDataCell2", tableDataCell, page.getHtmlElementById("data1"));

        final HtmlTextArea textArea = (HtmlTextArea) form.getTextAreasByName("textArea1").get(0);
        assertSame("textArea1", textArea, form.getTextAreasByName("textArea1").get(0));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetTabbableElements_None() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<p>hello world</p>"
            + "<table><tr><td>foo</td></tr></table>"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        assertEquals(Collections.EMPTY_LIST, page.getTabbableElements());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetTabbableElements_OneEnabled_OneDisabled() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<form><p>hello world</p>"
            + "<input name='foo' type='submit' disabled='disabled' id='foo'/>"
            + "<input name='bar' type='submit' id='bar'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List expectedElements = new ArrayList();
        expectedElements.add(page.getHtmlElementById("bar"));

        assertEquals(expectedElements, page.getTabbableElements());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetTabbableElements() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' tabindex='1'>foo</a>"
            + "<a id='b'>foo</a>"
            + "<form>"
            + "<a id='c' tabindex='3'>foo</a>"
            + "<a id='d' tabindex='2'>foo</a>"
            + "<a id='e' tabindex='0'>foo</a>"
            + "</form>"
            + "<a id='f' tabindex='3'>foo</a>"
            + "<a id='g' tabindex='1'>foo</a>"
            + "<a id='q' tabindex='-1'>foo</a>"
            + "<form><p>hello world</p>"
            + "<input name='foo' type='submit' disabled='disabled' id='foo'/>"
            + "<input name='bar' type='submit' id='bar'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List expectedElements = Arrays.asList(new Object[] {page.getHtmlElementById("a"),
                page.getHtmlElementById("g"), page.getHtmlElementById("d"),
                page.getHtmlElementById("c"), page.getHtmlElementById("f"),
                page.getHtmlElementById("e"), page.getHtmlElementById("b"),
                page.getHtmlElementById("bar")});

        assertEquals(expectedElements, page.getTabbableElements());

        final List expectedIds = Arrays
                .asList(new String[] {"a", "g", "d", "c", "f", "e", "b", "bar"});
        assertEquals(expectedIds, page.getTabbableElementIds());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetHtmlElementByAccessKey() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' accesskey='a'>foo</a>"
            + "<a id='b'>foo</a>"
            + "<form>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form>"
            + "<form><p>hello world</p>"
            + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
            + "<input name='bar' type='submit' id='bar'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        assertEquals(page.getHtmlElementById("a"), page.getHtmlElementByAccessKey('A'));
        assertEquals(page.getHtmlElementById("c"), page.getHtmlElementByAccessKey('c'));
        assertNull(page.getHtmlElementByAccessKey('z'));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetHtmlElementsByAccessKey() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head><body>"
            + "<a id='a' accesskey='a'>foo</a>"
            + "<a id='b' accesskey='a'>foo</a>"
            + "<form>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final List expectedElements = Arrays.asList(new Object[] {page.getHtmlElementById("a"),
                page.getHtmlElementById("b")});
        final List collectedElements = page.getHtmlElementsByAccessKey('a');
        assertEquals(expectedElements, collectedElements);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssertAllIdAttributesUnique() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' accesskey='a'>foo</a>"
            + "<a id='b'>foo</a>"
            + "<form>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form>"
            + "<form><p>hello world</p>"
            + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
            + "<input name='bar' type='submit' id='bar'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        page.assertAllIdAttributesUnique();
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssertAllIdAttributesUnique_Duplicates() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='dupeID' accesskey='a'>foo</a>"
            + "<a id='b'>foo</a>"
            + "<form>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form>"
            + "<form><p>hello world</p>"
            + "<input name='foo' type='submit' disabled='disabled' id='dupeID' accesskey='f'/>"
            + "<input name='bar' type='submit' id='bar'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        try {
            page.assertAllIdAttributesUnique();
            fail("Expected AssertionFailedError");
        }
        catch (final AssertionFailedError e) {
            assertTrue("dupeID", e.getMessage().indexOf("dupeID") != -1);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssertAllAccessKeyAttributesUnique() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' accesskey='a'>foo</a>"
            + "<a id='b'>foo</a>"
            + "<form>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form>"
            + "<form><p>hello world</p>"
            + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
            + "<input name='bar' type='submit' id='bar'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        page.assertAllAccessKeyAttributesUnique();
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssertAllAccessKeyAttributesUnique_Duplicates() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' accesskey='a'>foo</a>"
            + "<a id='b'>foo</a>"
            + "<form>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form>"
            + "<form><p>hello world</p>"
            + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
            + "<input name='bar' type='submit' id='bar' accesskey='a'/>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        try {
            page.assertAllAccessKeyAttributesUnique();
            fail("Expected AssertionFailedError");
        }
        catch (final AssertionFailedError e) {
            //pass
        }

    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssertAllTabIndexAttributesSet() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' tabindex='1'>foo</a>"
            + "<form>"
            + "<a id='c' tabindex='3'>foo</a>"
            + "<a id='d' tabindex='2'>foo</a>"
            + "<a id='e' tabindex='0'>foo</a>"
            + "</form>"
            + "<a id='f' tabindex='3'>foo</a>"
            + "<a id='g' tabindex='1'>foo</a>"
            + "<a id='q' tabindex='5'>foo</a>"
            + "<form><p>hello world</p>"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        page.assertAllTabIndexAttributesSet();
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssertAllTabIndexAttributesSet_SomeMissing() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' tabindex='1'>foo</a>"
            + "<form>"
            + "<a id='c' tabindex='3'>foo</a>"
            + "<a id='d' tabindex='2'>foo</a>"
            + "<a id='e' tabindex='0'>foo</a>"
            + "</form>"
            + "<a id='f' tabindex='3'>foo</a>"
            + "<a id='g'>foo</a>"
            + "<a id='q' tabindex='1'>foo</a>"
            + "<form><p>hello world</p>"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        try {
            page.assertAllTabIndexAttributesSet();
            fail("Expected AssertionFailedError");
        }
        catch (final AssertionFailedError e) {
            //pass
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAssertAllTabIndexAttributesSet_BadValue() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<a id='a' tabindex='1'>foo</a>"
            + "<form>"
            + "<a id='c' tabindex='3'>foo</a>"
            + "<a id='d' tabindex='2'>foo</a>"
            + "<a id='e' tabindex='0'>foo</a>"
            + "</form>"
            + "<a id='f' tabindex='3'>foo</a>"
            + "<a id='g' tabindex='1'>foo</a>"
            + "<a id='q' tabindex='300000'>foo</a>"
            + "<form><p>hello world</p>"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        try {
            page.assertAllTabIndexAttributesSet();
            fail("Expected AssertionFailedError");
        }
        catch (final AssertionFailedError e) {
            //pass
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetFullQualifiedUrl_NoBaseSpecified() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + "<table><tr><td><input type='text' id='foo'/></td></tr></table>"
            + "</form></body></html>";
        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        final String urlString = URL_GARGOYLE.toExternalForm();
        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);

        assertEquals(urlString, page.getFullyQualifiedUrl(""));
        assertEquals(urlString + "foo", page.getFullyQualifiedUrl("foo"));
        assertEquals("http://foo.com/bar", page.getFullyQualifiedUrl("http://foo.com/bar"));
        assertEquals("mailto:me@foo.com", page.getFullyQualifiedUrl("mailto:me@foo.com"));

        assertEquals(urlString + "foo", page.getFullyQualifiedUrl("foo"));
        assertEquals(urlString + "bbb", page.getFullyQualifiedUrl("aaa/../bbb"));
        assertEquals(urlString + "c/d", page.getFullyQualifiedUrl("c/./d"));

        final HtmlPage secondPage = (HtmlPage) client
                .getPage(new URL(urlString + "foo/bar?a=b&c=d"));
        assertEquals(urlString + "foo/bar?a=b&c=d", secondPage.getFullyQualifiedUrl(""));
        assertEquals(urlString + "foo/one", secondPage.getFullyQualifiedUrl("one"));
        assertEquals(urlString + "two", secondPage.getFullyQualifiedUrl("/two"));
        assertEquals(urlString + "foo/two?a=b", secondPage.getFullyQualifiedUrl("two?a=b"));

        final HtmlPage thirdPage = (HtmlPage) client.getPage(new URL(
                "http://foo.com/dog/cat/one.html"));
        assertEquals("http://foo.com/dog/cat/one.html", thirdPage.getFullyQualifiedUrl(""));
        assertEquals("http://foo.com/dog/cat/two.html", thirdPage.getFullyQualifiedUrl("two.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetFullQualifiedUrl_WithBase() throws Exception {
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
        final String htmlContent = "<html><head><title>foo</title>"
            + "<base href='" + baseUrl + "'>"
            + "</head><body>"
            + "<form id='form1'>"
            + "<table><tr><td><input type='text' id='foo'/></td></tr></table>"
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
    public void testGetFullQualifiedUrl_WithInvalidBase() throws Exception {
        final String htmlContent = "<html><head><base href='---****://=='/></head></html>";
        final HtmlPage page = loadPage(htmlContent);

        // invalid base url should be ignored
        assertEquals("http://somewhere.com/", page.getFullyQualifiedUrl("http://somewhere.com/"));
        assertEquals(page.getWebResponse().getUrl().toExternalForm() + "foo.html",
                page.getFullyQualifiedUrl("foo.html"));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler_BodyStatement() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "</head><body onLoad='alert(\"foo\")'>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[] {"foo"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * If the onload handler contains two statements then only the first would execute
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler_TwoBodyStatements() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "</head><body onLoad='alert(\"foo\");alert(\"bar\")'>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[] {"foo", "bar"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 713646
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler_BodyName() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "<script type='text/javascript'>"
            + "window.onload=function(){alert('foo')}</script>"
            + "</head><body></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[] {"foo"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 713646
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler_BodyName_NotAFunction() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head>"
            + "<body onLoad='foo=4711'>"
            + "<a name='alert' href='javascript:alert(foo)'/>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        page.getAnchorByName("alert").click();

        final List expectedAlerts = Arrays.asList(new String[] {"4711"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for window.onload property
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler_ScriptName() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "<script type='text/javascript'>\n"
            + "load=function(){alert('foo')};\n"
            + "onload=load\n"
            + "</script></head><body></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[] {"foo"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for window.onload property
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler_ScriptNameRead() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "<script type='text/javascript'>\n"
            + "load=function(){};\n"
            + "onload=load;\n"
            + "alert(onload);\n"
            + "</script></head><body></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[] {"\nfunction () {\n}\n"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testEmbeddedMetaTag_Regression() throws Exception {

        final String htmlContent = "<html><head><title>foo</title>"
            + "</head><body>"
            + "<table><tr><td>\n"
            + "<meta name=vs_targetSchema content=\"http://schemas.microsoft.com/intellisense/ie5\">"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</td></tr></table>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();

        // This used to blow up on page load
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Collections.EMPTY_LIST;
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetPageEncoding() throws Exception {

        final String htmlContent = "<html><head>"
            + "<title>foo</title>"
            + "<meta http-equiv='Content-Type' content='text/html ;charset=Shift_JIS'>"
            + "</head><body>"
            + "<table><tr><td>\n"
            + "<meta name=vs_targetSchema content=\"http://schemas.microsoft.com/intellisense/ie5\">"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</td></tr></table>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        assertEquals("Shift_JIS", page.getPageEncoding());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetForms() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<form name='one'>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form>"
            + "<form name='two'>"
            + "<a id='c' accesskey='c'>foo</a>"
            + "</form>"
            + "<input name='foo' type='submit' disabled='disabled' id='foo' accesskey='f'/>"
            + "<input name='bar' type='submit' id='bar'/>"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final List expectedForms = Arrays.asList(new HtmlForm[] {page.getFormByName("one"),
                page.getFormByName("two")});
        assertEquals(expectedForms, page.getForms());
    }

    /**
     * Test auto-refresh from a meta tag.
     * @throws Exception if the test fails
     */
    public void testRefresh_MetaTag_DefaultRefreshHandler() throws Exception {

        final String firstContent = "<html><head><title>first</title>"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"3;URL=http://second\">"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);

        assertEquals("second", page.getTitleText());
    }

    /**
     * Test auto-refresh from a meta tag with no URL.
     * @throws Exception if the test fails
     */
    public void testRefresh_MetaTag_NoUrl() throws Exception {

        final String firstContent = "<html><head><title>first</title>"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"1\">"
            + "</head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedItems = new ArrayList();
        client.setRefreshHandler(new LoggingRefreshHandler(collectedItems));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);

        // avoid using equals() on URL because it takes to much time (due to ip resolution)
        assertEquals("first", collectedItems.get(0));
        assertEquals(URL_FIRST, (URL) collectedItems.get(1));
        assertEquals(new Integer(1), collectedItems.get(2));
    }

    /**
     * Ensures that if a page is supposed to refresh itself every certain amount of
     * time, and the ImmediateRefreshHandler is being used, an OOME is avoided by
     * not performing the refresh.
     * @throws Exception if the test fails
     */
    public void testRefresh_ImmediateRefresh_AvoidOOME() throws Exception {

        final String firstContent = "<html><head><title>first</title>"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"1\">"
            + "</head><body></body></html>";

        final WebClient client = new WebClient();
        assertInstanceOf(client.getRefreshHandler(), ImmediateRefreshHandler.class);
        try {
            loadPage(firstContent);
            fail("should have thrown");
        }
        catch (final RuntimeException e){
            assertTrue(e.getMessage().indexOf("could have caused an OutOfMemoryError") > -1);
        }
        Thread.sleep(1000);
    }

    /**
     * Test auto-refresh from a meta tag with url quoted.
     * @throws Exception if the test fails
     */
    public void testRefresh_MetaTagQuoted() throws Exception {
        final String firstContent = "<html><head><title>first</title>"
            + "<META HTTP-EQUIV='Refresh' CONTENT='0;URL=\"http://second\"'>"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);

        assertEquals("second", page.getTitleText());
    }

    /**
     * Test auto-refresh from a meta tag with url partly quoted.
     * @throws Exception if the test fails
     */
    public void testRefresh_MetaTagPartlyQuoted() throws Exception {
        final String firstContent = "<html><head><title>first</title>"
            + "<META HTTP-EQUIV='Refresh' CONTENT=\"0;URL='http://second\">"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);

        assertEquals("second", page.getTitleText());
    }

    /**
     * Test auto-refresh from a meta tag inside noscript.
     * @throws Exception if the test fails
     */
    public void testRefresh_MetaTagNoScript() throws Exception {

        final String firstContent = "<html><head><title>first</title>"
            + "<noscript>"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;URL=http://second\">"
            + "</noscript>"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("first", page.getTitleText());

        client.setJavaScriptEnabled(false);
        page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("second", page.getTitleText());
    }

    /**
     * Test auto-refresh from a meta tag with a refresh handler that doesn't refresh.
     * @throws Exception if the test fails
     */
    public void testRefresh_MetaTag_CustomRefreshHandler() throws Exception {

        final String firstContent = "<html><head><title>first</title>"
            + "<META HTTP-EQUIV=\"Refresh\" CONTENT=\"3;URL=http://second\">"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List collectedItems = new ArrayList();
        client.setRefreshHandler(new LoggingRefreshHandler(collectedItems));

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);

        assertEquals("first", page.getTitleText());

        // avoid using equals() on URL because it takes to much time (due to ip resolution)
        assertEquals("first", collectedItems.get(0));
        assertEquals(URL_SECOND, ((URL) collectedItems.get(1)));
        assertEquals(new Integer(3), collectedItems.get(2));
    }

    /**
     * Test auto-refresh from a response header.
     * @throws Exception if the test fails
     */
    public void testRefresh_HttpResponseHeader() throws Exception {

        final String firstContent = "<html><head><title>first</title>"
            + "</head><body></body></html>";
        final String secondContent = "<html><head><title>second</title></head><body></body></html>";

        final WebClient client = new WebClient();

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent, 200, "OK", "text/html", Collections
                .singletonList(new KeyValuePair("Refresh", "3;URL=http://second")));
        webConnection.setResponse(URL_SECOND, secondContent);
        client.setWebConnection(webConnection);

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);

        assertEquals("second", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testDocumentElement() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlElement root = page.getDocumentElement();

        assertNotNull(root);
        assertEquals("html", root.getTagName());
        assertSame(page, root.getParentNode());
    }

    /**
     * @throws Exception if the test fails
     */
    public void testDocumentNodeType() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlElement root = page.getDocumentElement();

        assertEquals(DomNode.DOCUMENT_NODE, page.getNodeType());
        assertEquals(DomNode.ELEMENT_NODE, root.getNodeType());
        assertEquals("#document", page.getNodeName());
    }

    /**
     * 
     * @throws Exception if the test fails
     */
    public void testDeregisterFrameWithoutSrc() throws Exception {

        final String htmlContent = "<html>"
            + "<head><title>foo</title></head>"
            + "<body>"
            + "<iframe />"
            + "<a href='about:blank'>link</a>"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlAnchor link = (HtmlAnchor) page.getAnchors().get(0);
        link.click();
    }

    /**
     * Test that a return statement in onload doesn't throw any exception
     * @throws Exception if the test fails
     */
    public void testOnLoadReturn() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head>\n"
            + "<body onload='return true'>\n"
            + "</body></html>";

        loadPage(htmlContent);
    }

    /**
     * Test that wrong formed html code is parsed like browsers do
     * @throws Exception if the test fails
     */
    public void testWrongHtml_TagBeforeHtml() throws Exception {
        if (notYetImplemented()) {
            return;
        }

        final String htmlContent = "<div>"
            + "<html>"
            + "<head><title>foo</title>"
            + "<script>"
            + "var toto = 12345;"
            + "</script>"
            + "</head>"
            + "<body onload='alert(toto)'>"
            + "blabla"
            + "</body>"
            + "</html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final List expectedAlerts = Arrays.asList(new String[] {"12345"});
        createTestPageForRealBrowserIfNeeded(htmlContent, expectedAlerts);

        assertEquals("foo", page.getTitleText());

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @exception  Exception If the test fails
     */
    public void testAsXml() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head>"
            + "<body><p>helloworld</p></body>"
            + "</html>";

        final HtmlPage page = loadPage(htmlContent);
        assertEquals(htmlContent, page.asXml().replaceAll("\\s", ""));
    }

    /**
     * @exception  Exception If the test fails
     */
    public void testAsXml2() throws Exception {
        final String htmlContent = "<html><head><title>foo</title>"
            + "<script>var test = 15 < 16;</script></head>"
            + "</head>"
            + "<body onload='test=(1 > 2) && (45 < 78)'><p>helloworld &amp;amp; helloall</p></body>"
            + "</html>";

        final HtmlPage page = loadPage(htmlContent);
        assertNotNull("xml document could not be parsed", page.asXml());
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        builder.parse(TextUtil.toInputStream(page.asXml()));
    }

    /**
     * Regression test for bug 1204637
     * @exception  Exception If the test fails
     */
    public void testIsJavascript() throws Exception {
        assertTrue(HtmlPage.isJavaScript("text/javascript", null));
        assertTrue(HtmlPage.isJavaScript("text/JavaScript", null));
    }

    /**
     * Regression test for bug 1233519
     * @exception  Exception If the test fails
     */
    public void testGetHtmlElementByIdAfterRemove() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body>"
            + "<div id='div1'>"
            + "<div id='div2'>"
            + "</div>"
            + "</div>"
            + "</body>"
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
     * @exception  Exception If the test fails
     */
    public void testGetHtmlElementById_idTwice() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head>\n"
            + "<body>"
            + "<div id='id1'>foo</div>"
            + "<span id='id1'>bla</span>"
            + "</body>"
            + "</html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlElement elt1 = page.getHtmlElementById("id1");
        assertEquals("div", elt1.getNodeName());
        elt1.remove();
        assertEquals("span", page.getHtmlElementById("id1").getNodeName());
    }

    /**
     * Test the set-cookie meta tag
     * @throws Exception if the test fails
     */
    public void testSetCookieMetaTag() throws Exception {

        final String content = "<html><head><title>first</title>"
            + "<meta http-equiv='set-cookie' content='webm=none; path=/;'>"
            + "</head><body>"
            + "<script>alert(document.cookie)</script>"
            + "</body></html>";

        final String[] expectedAlerts = {"webm=none"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
        
        final Cookie[] cookies = page.getWebClient().getWebConnection().getState().getCookies();
        assertEquals(1, cookies.length);
        final Cookie cookie = cookies[0];
        assertEquals(page.getWebResponse().getUrl().getHost(), cookie.getDomain());
        assertEquals("webm", cookie.getName());
        assertEquals("none", cookie.getValue());
        assertEquals("/", cookie.getPath());
    }
    
    /**
     * Regression test for bug 1658273
     * @throws Exception if the test fails
     */
    public void testOnLoadHandler_idChange() throws Exception {
        final String content = "<html><head><title>foo</title>"
            + "<div id='id1' class='cl1'><div id='id2' class='cl2'></div></div>'"
            + "<script type='text/javascript'>"
            + "document.getElementById('id1').id = 'id3';"
            + "alert(document.getElementById('id2').className);"
            + "alert(document.getElementById('id3').className);"
            + "</script>"
            + "</head><body></body></html>";
        
        final String[] expectedAlerts = {"cl2", "cl1"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }    

    /**
     * Test for bug 1714767
     * @throws Exception if the test fails
     */
    public void testNoSlashURL() throws Exception {
        testNoSlashURL( "http:/second" );
        testNoSlashURL( "http:second" );
    }

    private void testNoSlashURL( final String url ) throws Exception {
        final String firstContent
            = "<html><body>"
            + "<iframe id='myIFrame' src='" + url + "'></iframe>"
            + "</body></html>";
    
        final String secondContent
            = "<html><body></body></html>";
        final WebClient client = new WebClient(BrowserVersion.MOZILLA_1_0);

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setResponse(URL_SECOND, secondContent);

        client.setWebConnection( webConnection );
    
        final HtmlPage firstPage = ( HtmlPage )client.getPage(URL_FIRST);
        final HtmlInlineFrame iframe = (HtmlInlineFrame)firstPage.getHtmlElementById( "myIFrame" );
        
        assertEquals( URL_SECOND, iframe.getEnclosedPage().getWebResponse().getUrl() );
    }

    /**
     * @throws Exception failure
     */
    public void testMetaTagWithEmptyURL() throws Exception {
        final WebClient client = new WebClient();
        client.setRefreshHandler(new ImmediateRefreshHandler());
        
        // connection will return a page with <meta ... refresh> for the first call
        // and the same page without it for the other calls
        final MockWebConnection webConnection = new MockWebConnection(client) {
            private int nbCalls_ = 0;
            public WebResponse getResponse(final WebRequestSettings settings) throws IOException {
                String content = "<html><head>";
                if (nbCalls_ == 0) {
                    content += "<meta http-equiv='refresh' content='1; URL='>";
                }
                content += "</head><body></body></html>";
                ++nbCalls_;
                return new StringWebResponse(content, settings.getURL()) {
                    public SubmitMethod getRequestMethod() {
                        return settings.getSubmitMethod();
                    }
                };
            }
        };
        client.setWebConnection( webConnection );
        
        final WebRequestSettings settings = new WebRequestSettings(URL_GARGOYLE);
        settings.setSubmitMethod(SubmitMethod.POST);
        client.getPage(settings);
    }

}
