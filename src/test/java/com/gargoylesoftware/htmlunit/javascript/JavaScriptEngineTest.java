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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for the {@link JavaScriptEngine}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Darrell DeBoer
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class JavaScriptEngineTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setJavascriptEnabled_false() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "document.form1.textfield1='blue'"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        getWebClientWithMockWebConnection().getOptions().setJavaScriptEnabled(false);

        final HtmlPage page = loadPageWithAlerts(html);

        final HtmlTextInput textInput = page.getHtmlElementById("textfield1");
        assertEquals("foo", textInput.getValueAttribute());
    }

    /**
     * Regression test for bug https://sf.net/tracker/?func=detail&atid=448266&aid=1609944&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void onloadJavascriptFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function onload() {alert('foo');}"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Tries to set the value of a text input field.
     * @throws Exception if the test fails
     */
    @Test
    public void setInputValue() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    document.form1.textfield1.value='blue'"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";
        final List<String> collectedAlerts = null;
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);

        final HtmlTextInput textInput = page.getHtmlElementById("textfield1");
        assertEquals("blue", textInput.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void alert() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "alert('foo')\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Checks that a dynamically compiled function works in the scope of its birth.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void scopeOfNewFunction() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "var f = new Function('alert(\"foo\")');\n"
            + "f();\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void scopeOfNestedNewFunction() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "var foo = 'foo';\n"
            + "var f1 = new Function('f = new Function(\"alert(foo)\"); f()');\n"
            + "f1();\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Checks that a dynamically compiled function works in the scope of its birth and not the other window.
     * @throws Exception if the test fails
     */
    @Test
    public void scopeOfNewFunctionCalledFormOtherWindow() throws Exception {
        final String firstContent
            = "<html><head>\n"
            + "<script>\n"
            + "var foo = 'foo';\n"
            + "var test = new Function('alert(foo);');\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <iframe src='page2.html'/>\n"
            + "</body>\n"
            + "</html>";

        final String secondContent = "<html><head><script>\n"
            + "var foo = 'foo2';\n"
            + "parent.test();\n"
            + "var f = parent.test;\n"
            + "f();\n"
            + "</script></head></html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(secondContent);
        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection(webConnection);

        final String[] expectedAlerts = {"foo", "foo", "foo"};

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * If a reference has been hold on a page and the page is not
     * anymore the one contained in "its" window, JavaScript should not be executed.
     * Up to HtmlUnit-2.10 it could work a little bit without any guarantee but this
     * has been removed to fix a problem where background JS tasks were still executed
     * once a page has been unloaded
     * (see {@link com.gargoylesoftware.htmlunit.javascript.host.WindowConcurrencyTest#cleanSetTimeout}).
     * @throws Exception if the test fails
     */
    @Test
    public void scopeInInactivePage() throws Exception {
        final String firstContent
            = "<html><head>\n"
            + "<script>\n"
            + "var foo = 'foo';\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <a href='page2.html'>to page 2</a>\n"
            + "  <div id='testdiv' onclick='alert(foo)'>foo</div>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse("<html></html>");
        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection(webConnection);

        final String[] expectedAlerts = {"foo"};

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement div = page.getHtmlElementById("testdiv");

        div.click();
        assertEquals(expectedAlerts, collectedAlerts);
        collectedAlerts.clear();

        page.getAnchors().get(0).click();

        // ignore response, and click in the page again that is not "active" anymore
        div.click();
        assertEquals(Collections.emptyList(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("got here")
    public void externalScript() throws Exception {
        final String html
            = "<html><head><title>foo</title><script src='/foo.js' id='script1'/>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        final String jsContent = "alert('got here');\n";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "foo.js"), jsContent,
                "text/javascript");

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlScript htmlScript = page.getHtmlElementById("script1");
        assertNotNull(htmlScript);
    }

    /**
     * An odd case, if two external scripts are referenced and the &lt;script&gt; element
     * of the first contain a comment which contain an apostrophe, then the second script
     * is ignored.
     * https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1990198&group_id=47038
     * This works fine in IE6 and FF 2.0.  Remove the apostrophe from "shouldn't" to make it work here.
     * @throws Exception if the test fails
     */
    @Test
    public void externalScriptWithApostrophesInComment() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "<script src='/foo.js' id='script1'><!-- this shouldn't be a problem --></script>\n"
            + "<script src='/foo2.js' id='script2'><!-- this shouldn't be a problem --></script>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";

        webConnection.setResponse(URL_FIRST, htmlContent);
        webConnection.setResponse(new URL(URL_FIRST, "foo.js"),  "alert('got here');", "text/javascript");
        webConnection.setResponse(new URL(URL_FIRST, "foo2.js"), "alert('got here 2');", "text/javascript");
        client.setWebConnection(webConnection);

        final String[] expectedAlerts = {"got here", "got here 2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);

        assertNotNull(page.getHtmlElementById("script1"));
        assertNotNull(page.getHtmlElementById("script2"));
    }

    /**
     * Test that the URL of the page containing the script is contained in the exception's message.
     * @throws Exception if the test fails
     */
    @Test
    public void scriptErrorContainsPageUrl() throws Exception {
        // embedded script
        final String content1
            = "<html><head><script>a.foo</script>\n"
            + "</head><body>\n"
            + "</body></html>";

        try {
            loadPageWithAlerts(content1);
        }
        catch (final Exception e) {
            assertTrue(e.getMessage().indexOf(getDefaultUrl().toString()) > -1);
        }

        // external script
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String content2
            = "<html><head><title>foo</title><script src='/foo.js'/>\n"
            + "</head><body>\n"
            + "</body></html>";

        final String jsContent = "a.foo = 213;\n";

        webConnection.setResponse(getDefaultUrl(), content2);
        final URL urlScript = new URL(getDefaultUrl(), "foo.js");
        webConnection.setResponse(urlScript, jsContent, "text/javascript");
        client.setWebConnection(webConnection);

        try {
            client.getPage(getDefaultUrl());
        }
        catch (final Exception e) {
            assertTrue(e.getMessage(), e.getMessage().indexOf(urlScript.toString()) > -1);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void externalScriptEncoding() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        /*
         * this page has meta element , and script tag has no charset attribute
         */
        final String htmlContent
            = "<html><head>\n"
            + "<meta http-equiv='content-type' content='text/html; charset=Shift_JIS'>\n"
            + "<title>foo</title>\n"
            + "<script src='/foo.js' id='script1'/>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        /*
         * this page has no meta element , and script tag has charset attribute
         */
        final String htmlContent2
            = "<html><head>\n"
            + "<title>foo</title>\n"
            + "<script src='/foo2.js' charset='Shift_JIS' id='script2'/>\n"
            + "</head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        /*
         * the corresponding SJIS char of '\u8868' has '\' in second byte.
         * if encoding is misspecificated,
         * this cause 'unterminated string reteral error'
         */
        final String jsContent = "alert('\u8868');\n";

        webConnection.setResponse(getDefaultUrl(), htmlContent);

        webConnection.setResponse(
            new URL(getDefaultUrl(), "hidden"),
            htmlContent2);

        webConnection.setResponse(
            new URL(getDefaultUrl(), "foo.js"),
        // make SJIS bytes as responsebody
            new String(jsContent.getBytes("SJIS"), "8859_1"), "text/javascript");

        /*
         * foo2.js is same with foo.js
         */
        webConnection.setResponse(
            new URL(getDefaultUrl(), "foo2.js"),
            // make SJIS bytes as responsebody
            new String(jsContent.getBytes("SJIS"), "8859_1"),
            "text/javascript");

        client.setWebConnection(webConnection);

        final String[] expectedAlerts = {"\u8868"};
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        /*
         * detect encoding from meta tag
         */
        final HtmlPage page = client.getPage(getDefaultUrl());
        final HtmlScript htmlScript = page.getHtmlElementById("script1");

        assertNotNull(htmlScript);
        assertEquals(expectedAlerts, collectedAlerts);

        /*
         * detect encoding from charset attribute of script tag
         */
        collectedAlerts.clear();
        final HtmlPage page2 = client.getPage(new URL(getDefaultUrl(), "hidden"));
        final HtmlScript htmlScript2 = page2.getHtmlElementById("script2");

        assertNotNull(htmlScript2);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("gZip")
    public void externalScriptGZipEncoded() throws Exception {
        final MockWebConnection webConnection = getMockWebConnection();

        final String jsContent = "function doTest() { alert('gZip'); }";
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final GZIPOutputStream gzipper = new GZIPOutputStream(bytes);
        gzipper.write(jsContent.getBytes("ASCII"));
        gzipper.close();

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        webConnection.setResponse(new URL(getDefaultUrl(), "foo.js"),
                bytes.toByteArray(), 200, "OK", "text/javascript", headers);

        final String htmlContent
            = "<html><head>\n"
            + "<title>foo</title>\n"
            + "<script src='/foo.js'></script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(htmlContent);
    }

    /**
     * Test for a javascript which points to an empty gzip encoded file (bug 3566999).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("done")
    public void externalScriptEmptyGZipEncoded() throws Exception {
        final MockWebConnection webConnection = getMockWebConnection();

        final String jsContent = "";
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(jsContent.getBytes("ASCII"));
        bytes.close();

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Length", "0"));
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        webConnection.setResponse(new URL(getDefaultUrl(), "foo.js"),
                bytes.toByteArray(), 200, "OK", "text/javascript", headers);

        final String htmlContent
            = "<html><head>\n"
            + "<title>foo</title>\n"
            + "<script src='/foo.js'></script>\n"
            + "</head><body onload='alert(\"done\");'>\n"
            + "</body></html>";

        loadPageWithAlerts(htmlContent);
    }

    /**
     * Test for a javascript which points to a broken gzip encoded file (bug 3563712).
     * @throws Exception if an error occurs
     */
    @Test
    public void externalScriptBrokenGZipEncoded() throws Exception {
        final MockWebConnection webConnection = getMockWebConnection();

        final String jsContent = "function doTest() { alert('gZip'); }";
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(jsContent.getBytes("ASCII"));
        bytes.close();

        final List<NameValuePair> headers = new ArrayList<NameValuePair>();
        headers.add(new NameValuePair("Content-Encoding", "gzip"));
        webConnection.setResponse(new URL(getDefaultUrl(), "foo.js"),
                bytes.toByteArray(), 200, "OK", "text/javascript", headers);

        final String htmlContent
            = "<html><head>\n"
            + "<title>foo</title>\n"
            + "<script src='/foo.js'></script>\n"
            + "</head><body onload='doTest();alert(\"done\");'>\n"
            + "</body></html>";

        try {
            loadPageWithAlerts(htmlContent);
            Assert.fail("ScriptException expected");
        }
        catch (final ScriptException e) {
            // expected
        }
    }

    /**
     * Sets value on input expects a string. If you pass in a value that isn't a string
     * this used to blow up.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void setValuesThatAreNotStrings() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    document.form1.textfield1.value = 1;\n"
            + "    alert(document.form1.textfield1.value)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void referencingVariablesFromOneScriptToAnother_Regression() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String htmlContent
            = "<html><head><title>foo</title><script src='./test.js'></script>\n"
            + "<script>var testLocalVariable = new Array();</script>\n"
            + "</head><body onload='testNestedMethod();' >\n"
            + "<form name='form1' method='POST' action='../foo' >\n"
            + "    <input type='submit' value='Login' name='loginButton'>\n"
            + "</form></body></html>";

        final String jsContent
            = "function testNestedMethod() {\n"
            + "    if (testLocalVariable == null)\n"
            + "        testLocalVariable = 'foo';\n"
            + "} ";

        webConnection.setResponse(URL_FIRST, htmlContent);
        webConnection.setResponse(new URL(URL_FIRST, "test.js"), jsContent, "text/javascript");
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void javaScriptUrl() throws Exception {
        final String htmlContent
            = "<html><head><script language='javascript'>\n"
            + "var f1 = '<html><head><title>frame1</title></head><body><h1>frame1</h1></body></html>';\n"
            + "var f2 = '<html><head><title>frame2</title></head><body><h1>frame2</h1></body></html>';\n"
            + "</script></head>\n"
            + "<frameset border='0' frameborder='0' framespacing='0' rows='100,*'>\n"
            + "    <frame id='frame1' src='javascript:parent.f1'/>\n"
            + "    <frame id='frame2' src='javascript:parent.f2'/>\n"
            + "</frameset></html>";

        final List<String> emptyList = Collections.emptyList();
        createTestPageForRealBrowserIfNeeded(htmlContent, emptyList);
        final HtmlPage page = loadPage(getBrowserVersion(), htmlContent, null);

        final HtmlPage page1 = (HtmlPage) ((HtmlFrame) page.getHtmlElementById("frame1")).getEnclosedPage();
        final HtmlPage page2 = (HtmlPage) ((HtmlFrame) page.getHtmlElementById("frame2")).getEnclosedPage();

        assertNotNull("page1", page1);
        assertNotNull("page2", page2);

        assertEquals("frame1", page1.getTitleText());
        assertEquals("frame2", page2.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void javaScriptWrappedInHtmlComments() throws Exception {
        final String html
            = "<html><head><title>foo</title><script language='javascript'><!--\n"
            + "function doTest() {\n"
            + "  alert('foo');\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void javaScriptWrappedInHtmlComments2() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script><!--\n"
            + " alert('1')\n"
            + "--></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void javaScriptWrappedInHtmlComments_commentOnOpeningLine() throws Exception {
        final String html
            = "<html><head><title>foo</title><script language='javascript'><!-- Some comment here\n"
            + "function doTest() {\n"
            + " alert('1')\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 1714762.
     * @throws Exception if the test fails
     */
    @Test
    public void javaScriptWrappedInHtmlComments_commentNotClosed() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script language='javascript'><!-- alert(1);</script>\n"
            + "<script language='javascript'><!-- </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void javaScriptWrappedInHtmlComments_allOnOneLine() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script>var test;</script>\n"
            + "    <!-- var test should be undefined since it's on first line -->\n"
            + "    <!-- but there should be no index out of bounds exception  -->\n"
            + "    <script> <!-- test = 'abc'; // --> </script>\n"
            + "  </head>\n"
            + "  <body onload='alert(test)'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("test")
    public void eventHandlerWithComment() throws Exception {
        final String html = "<html><body onLoad='alert(\"test\"); // xxx'></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * When using the syntax this.something in an onclick handler, "this" must represent
     * the object being clicked, not the window. Regression test.
     * @throws Exception if the test fails
     */
    @Test
    public void thisDotInOnClick() throws Exception {
        final String htmlContent
            = "<html><head><title>First</title><script>function foo(message){alert(message);}</script><body>\n"
             + "<form name='form1'><input type='submit' name='button1' onClick='foo(this.name)'></form>\n"
             + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), htmlContent, collectedAlerts);
        assertEquals("First", page.getTitleText());

        ((HtmlSubmitInput) page.getFormByName("form1").getInputByName("button1")).click();

        final String[] expectedAlerts = {"button1"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void functionDefinedInExternalFile_CalledFromInlineScript() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String htmlContent
            = "<html><head><title>foo</title><script src='./test.js'></script>\n"
            + "</head><body>\n"
            + "    <script>externalMethod()</script>\n"
            + "</body></html>";

        final String jsContent
            = "function externalMethod() {\n"
            + "    alert('Got to external method');\n"
            + "} ";

        webConnection.setResponse(
            new URL("http://first/index.html"),
            htmlContent);
        webConnection.setResponse(
            new URL("http://first/test.js"),
            jsContent, "text/javascript");
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = client.getPage("http://first/index.html");
        assertEquals("foo", page.getTitleText());
        assertEquals(new String[] {"Got to external method"}, collectedAlerts);
    }

    /**
     * Regression test for https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1552746&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    public void externalScriptWithNewLineBeforeClosingScriptTag() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final String htmlContent
            = "<html><head><title>foo</title>\n"
            + "</head><body>\n"
            + "<script src='test.js'>\n</script>\n" // \n between opening and closing tag is important
            + "</body></html>";

        final String jsContent
            = "function externalMethod() {\n"
            + "    alert('Got to external method');\n"
            + "}\n"
            + "externalMethod();\n";

        webConnection.setResponse(URL_FIRST, htmlContent);
        webConnection.setDefaultResponse(jsContent, 200, "OK", "text/javascript");
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        assertEquals(new String[] {"Got to external method"}, collectedAlerts);
    }

    /**
     * Test case for bug 707134. Currently I am unable to reproduce the problem.
     * @throws Exception if the test fails
     */
    @Test
    public void functionDefinedInSameFile() throws Exception {
        final String htmlContent
            = "<html><head><title>First</title><script>\n"
            + "function showFoo(foo) {\n"
            + "    alert('Foo is: |' + foo + '|');\n"
            + "}\n"
            + "</script>\n"
            + "</head><body><form name='form1'>\n"
            + "<input name='text1' type='text'>\n"
            + "<input name='button1' type='button' onclick='showFoo(document.form1.text1.value);'>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();

        final HtmlPage page = loadPage(getBrowserVersion(), htmlContent, collectedAlerts);
        assertEquals("First", page.getTitleText());

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextInput textInput = form.getInputByName("text1");
        textInput.setValueAttribute("flintstone");

        final HtmlButtonInput button = form.getInputByName("button1");
        assertEquals(Collections.EMPTY_LIST, collectedAlerts);

        button.click();

        assertEquals(new String[] {"Foo is: |flintstone|"}, collectedAlerts);
    }

    /**
     * Test that the JavaScript engine gets called correctly for variable access.
     * @throws Exception if the test fails
     */
    @Test
    public void javaScriptEngineCallsForVariableAccess() throws Exception {
        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String content
            = "<html><head><title>foo</title><script>\n"
            + "myDate = 'foo';\n"
            + "function doUnqualifiedVariableAccess() {\n"
            + "    alert('unqualified: ' + myDate);\n"
            + "}\n"
            + "function doQualifiedVariableAccess() {\n"
            + "    alert('qualified: ' + window.myDate);\n"
            + "}\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<a id='unqualified' onclick='doUnqualifiedVariableAccess();'>unqualified</a>\n"
            + "<a id='qualified' onclick='doQualifiedVariableAccess();'>qualified</a>\n"
            + "</body></html>";

        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);
        final CountingJavaScriptEngine countingJavaScriptEngine = new CountingJavaScriptEngine(client);
        client.setJavaScriptEngine(countingJavaScriptEngine);

        final HtmlPage page = client.getPage(getDefaultUrl());

        assertEquals(1, countingJavaScriptEngine.getExecutionCount());
        assertEquals(0, countingJavaScriptEngine.getCallCount());

        ((HtmlAnchor) page.getHtmlElementById("unqualified")).click();
        assertEquals(1, countingJavaScriptEngine.getExecutionCount());
        assertEquals(1, countingJavaScriptEngine.getCallCount());

        ((HtmlAnchor) page.getHtmlElementById("qualified")).click();
        assertEquals(1, countingJavaScriptEngine.getExecutionCount());
        assertEquals(2, countingJavaScriptEngine.getCallCount());

        final String[] expectedAlerts = {"unqualified: foo", "qualified: foo"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests ActiveX related exceptions that do not require a map.
     * @throws Exception if the test fails
     */
    @Test
    public void activeXObjectNoMap() throws Exception {
        try {
            loadPage(getBrowserVersion(), getJavaScriptContent("new ActiveXObject()"), null);
            fail("An exception should be thrown for zero argument constructor.");
        }
        catch (final ScriptException e) {
            // Success
        }

        try {
            loadPage(getBrowserVersion(), getJavaScriptContent("new ActiveXObject(1, '2', '3')"), null);
            fail("An exception should be thrown for a three argument constructor.");
        }
        catch (final ScriptException e) {
            // Success
        }

        try {
            loadPage(getBrowserVersion(), getJavaScriptContent("new ActiveXObject(a)"), null);
            fail("An exception should be thrown for an undefined parameter in the constructor.");
        }
        catch (final ScriptException e) {
            // Success
        }

        try {
            loadPage(getBrowserVersion(), getJavaScriptContent("new ActiveXObject(10)"), null);
            fail("An exception should be thrown for an integer parameter in the constructor.");
        }
        catch (final ScriptException e) {
            // Success
        }

        try {
            loadPage(getBrowserVersion(), getJavaScriptContent("new ActiveXObject('UnknownObject')"), null);
            fail("An exception should be thrown for a null map.");
        }
        catch (final ScriptException e) {
            // Success
        }
    }

    /**
     * Test that Java objects placed in the active x map can be instantiated and used within
     * JavaScript using the IE specific ActiveXObject constructor.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    public void activeXObjectWithMap() throws Exception {
        final Map<String, String> activexToJavaMapping = new HashMap<String, String>();
        activexToJavaMapping.put(
                "MockActiveXObject",
                "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        activexToJavaMapping.put(
                "FakeObject",
                "com.gargoylesoftware.htmlunit.javascript.NoSuchObject");
        activexToJavaMapping.put("BadObject", null);

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        client.setWebConnection(webConnection);
        client.setActiveXObjectMap(activexToJavaMapping);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webConnection.setDefaultResponse(getJavaScriptContent("new ActiveXObject('UnknownObject')"));
        try {
            client.getPage("http://www.yahoo.com");
            fail("An exception should be thrown for non existent object in the map.");
        }
        catch (final ScriptException e) {
            // Success
        }

        webConnection.setDefaultResponse(getJavaScriptContent("new ActiveXObject('BadObject', 'server')"));
        try {
            client.getPage("http://www.yahoo.com");
            fail("An exception should be thrown for an invalid object in the map.");
        }
        catch (final ScriptException e) {
            // Success
        }

        // Test for a non existent class in the map
        webConnection.setDefaultResponse(getJavaScriptContent("new ActiveXObject('FakeObject')"));
        try {
            client.getPage("http://www.yahoo.com");
            fail("An exception should be thrown for a non existent object in the map.");
        }
        catch (final ScriptException e) {
            // Success
        }

        Assert.assertEquals("should no alerts yet", Collections.EMPTY_LIST, collectedAlerts);

        // Try a valid object in the map
        webConnection.setDefaultResponse(getJavaScriptContent(
                "var t = new ActiveXObject('MockActiveXObject'); alert(t.MESSAGE);\n"));
        client.getPage("http://www.yahoo.com");
        assertEquals("The active x object did not bind to the object.",
                new String[] {MockActiveXObject.MESSAGE}, collectedAlerts);

        collectedAlerts.clear();

        webConnection.setDefaultResponse(getJavaScriptContent(
                "var t = new ActiveXObject('MockActiveXObject', 'server'); alert(t.GetMessage());\n"));
        client.getPage("http://www.yahoo.com");
        assertEquals("The active x object did not bind to the object.",
                new String[] {new MockActiveXObject().GetMessage()}, collectedAlerts);
    }

    private String getJavaScriptContent(final String javascript) {
        return "<html><head><title>foo</title><script>\n"
             + javascript
             + "</script></head><body>\n"
             + "<p>hello world</p>\n"
             + "<form name='form1'>\n"
             + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
             + "    <input type='text' name='textfield2' id='textfield2'/>\n"
             + "</form>\n"
             + "</body></html>";
    }

    /**
     * Check that wrong JavaScript just causes its context to fail but not the whole page.
     * @throws Exception if something goes wrong
     */
    @Test
    public void scriptErrorIsolated() throws Exception {
        final String content
            = "<html>\n"
            + "<head>\n"
            + "<script>alert(1);</script>\n"
            + "<script>alert(2</script>\n"
            + "<script>alert(3);</script>\n"
            + "</head>\n"
            + "<body onload='alert(4);notExisting()'>\n"
            + "<button onclick='alert(5)'>click me</button>\n"
            + "</body>\n"
            + "</html>";

        final String[] expectedAlerts = {"1", "3", "4"};

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // first test with script exceptions thrown
        try {
            client.getPage(URL_FIRST);
            fail("Should have thrown a script error");
        }
        catch (final Exception e) {
            // nothing
        }
        assertEquals(new String[] {"1"}, collectedAlerts);
        collectedAlerts.clear();

        // and with script exception not thrown
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that prototype changes are made in the right scope.
     * Problem reported by Bruce Faulnker in the dev mailing list.
     * This is due to a Rhino bug:
     * https://bugzilla.mozilla.org/show_bug.cgi?id=374918
     * @throws Exception if something goes wrong
     */
    @Test
    public void prototypeScope() throws Exception {
        prototypeScope("String", "'some string'");
        prototypeScope("Number", "9");
        prototypeScope("Date", "new Date()");
        prototypeScope("Function", "function(){}");
        prototypeScope("Array", "[]");
    }

    private void prototypeScope(final String name, final String value) throws Exception {
        final String content1
            = "<html><head>\n"
            + "<script>\n"
            + "window.open('second.html', 'secondWindow');\n"
            + "</script>\n"
            + "</head><body></body></html>";

        final String content2
            = "<html><head>\n"
            + "<script>\n"
            + "alert('in page 2');\n"
            + name + ".prototype.foo = function() {\n"
            + "   alert('in foo');\n"
            + "};\n"
            + "var x = " + value + ";\n"
            + "x.foo();\n"
            + "</script>\n"
            + "</head><body></body></html>";

        final String[] expectedAlerts = {"in page 2", "in foo"};

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(content2);
        webConnection.setResponse(URL_FIRST, content1);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void timeout() throws Exception {
        final WebClient client = getWebClient();
        final long timeout = 2000;
        final long oldTimeout = client.getJavaScriptTimeout();
        client.setJavaScriptTimeout(timeout);

        try {
            client.getOptions().setThrowExceptionOnScriptError(false);

            final String content = "<html><body><script>while(1) {}</script></body></html>";
            final MockWebConnection webConnection = new MockWebConnection();
            webConnection.setDefaultResponse(content);
            client.setWebConnection(webConnection);

            final Exception[] exceptions = {null};
            final Thread runner = new Thread() {
                @Override
                public void run() {
                    try {
                        client.getPage(URL_FIRST);
                    }
                    catch (final Exception e) {
                        exceptions[0] = e;
                    }
                }
            };

            runner.start();

            runner.join(timeout * 2);
            if (runner.isAlive()) {
                runner.interrupt();
                fail("Script was still running after timeout");
            }
            assertNull(exceptions[0]);
        }
        finally {
            client.setJavaScriptTimeout(oldTimeout);
        }
    }

    private static final class CountingJavaScriptEngine extends JavaScriptEngine {
        private int scriptExecutionCount_ = 0;
        private int scriptCallCount_ = 0;
        private int scriptCompileCount_ = 0;
        private int scriptExecuteScriptCount_ = 0;

        /**
         * Creates an instance.
         * @param client the WebClient
         */
        protected CountingJavaScriptEngine(final WebClient client) {
            super(client);
        }

        /** {@inheritDoc} */
        @Override
        public Object execute(
                final HtmlPage htmlPage, final String sourceCode,
                final String sourceName, final int startLine) {
            scriptExecutionCount_++;
            return super.execute(htmlPage, sourceCode, sourceName, startLine);
        }

        /** {@inheritDoc} */
        @Override
        public Object execute(final HtmlPage htmlPage, final Script script) {
            scriptExecuteScriptCount_++;
            return super.execute(htmlPage, script);
        }

        /** {@inheritDoc} */
        @Override
        public Script compile(final HtmlPage htmlPage, final String sourceCode,
                final String sourceName, final int startLine) {
            scriptCompileCount_++;
            return super.compile(htmlPage, sourceCode, sourceName, startLine);
        }

        /** {@inheritDoc} */
        @Override
        public Object callFunction(
                final HtmlPage htmlPage, final Function javaScriptFunction,
                final Scriptable thisObject, final Object[] args,
                final DomNode htmlElementScope) {
            scriptCallCount_++;
            return super.callFunction(htmlPage, javaScriptFunction, thisObject, args, htmlElementScope);
        }

        /** @return the number of times that this engine has called functions */
        public int getCallCount() {
            return scriptCallCount_;
        }

        /** @return the number of times that this engine has executed code */
        public int getExecutionCount() {
            return scriptExecutionCount_;
        }

        /** @return the number of times that this engine has compiled code */
        public int getCompileCount() {
            return scriptCompileCount_;
        }

        /** @return the number of times that this engine has executed a compiled script */
        public int getExecuteScriptCount() {
            return scriptExecuteScriptCount_;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "2")
    public void commentNoDoubleSlash() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script><!-- alert(1);\n"
            + " alert(2);\n"
            + "alert(3) --></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        boolean exceptionThrown = false;
        try {
            loadPageWithAlerts(html);
        }
        catch (final ScriptException e) {
            exceptionThrown = true;
            assertEquals(4, e.getFailingLineNumber());
        }

        assertEquals(getBrowserVersion().isFirefox(), exceptionThrown);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "3" })
    public void comment() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script><!-- alert(1);\n"
            + " alert(2);\n"
            + "alert(3)//--></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "rstlne-rstlne-rstlne", "rstlno-rstlne-rstlne",
            "rstlna-rstlne-rstlne", "rstlne-rstlne-rstlne",
            "rstlni-rstlni-rstlni", "rstlna-rstlna-rstlna" })
    public void regExpSupport() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script id='a'>\n"
            + "       var s = new String('rstlne-rstlne-rstlne');\n"
            + "       alert(s);\n"
            + "       s = s.replace('e', 'o');\n"
            + "       alert(s);\n"
            + "       s = s.replace(/o/, 'a');\n"
            + "       alert(s);\n"
            + "       s = s.replace(new RegExp('a'), 'e');\n"
            + "       alert(s);\n"
            + "       s = s.replace(new RegExp('e', 'g'), 'i');\n"
            + "       alert(s);\n"
            + "       s = s.replace(/i/g, 'a');\n"
            + "       alert(s);\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>abc</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test ECMA reserved keywords... that are accepted by "normal" browsers
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void ecmaReservedKeywords() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "var o = {float: 123};"
            + "alert(o.float);"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test that compiled script are cached.
     * @throws Exception if the test fails
     */
    @Test
    public void compiledScriptCached() throws Exception {
        final String content1
            = "<html><head><title>foo</title>\n"
            + "<script src='script.js'></script>\n"
            + "</head><body>\n"
            + "<a href='page2.html'>to page 2</a>\n"
            + "</body></html>";
        final String content2
            = "<html><head><title>page 2</title>\n"
            + "<script src='script.js'></script>\n"
            + "</head><body>\n"
            + "</body></html>";
        final String script = "alert(document.title)";

        final WebClient client = getWebClient();
        final MockWebConnection connection = new MockWebConnection();
        client.setWebConnection(connection);
        connection.setResponse(URL_FIRST, content1);
        connection.setResponse(new URL(URL_FIRST, "page2.html"), content2);

        final List<NameValuePair> headersAllowingCache = new ArrayList<NameValuePair>();
        headersAllowingCache.add(new NameValuePair("Last-Modified", "Sun, 15 Jul 2007 20:46:27 GMT"));
        connection.setResponse(new URL(URL_FIRST, "script.js"), script,
                200, "ok", "text/javascript", headersAllowingCache);

        final CountingJavaScriptEngine countingJavaScriptEngine = new CountingJavaScriptEngine(client);
        client.setJavaScriptEngine(countingJavaScriptEngine);

        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        final HtmlPage page1 = client.getPage(URL_FIRST);
        assertEquals(new String[] {"foo"}, collectedAlerts);
        assertEquals(1, countingJavaScriptEngine.getExecuteScriptCount());
        assertEquals(1, countingJavaScriptEngine.getCompileCount());

        collectedAlerts.clear();
        page1.getAnchors().get(0).click();
        assertEquals(new String[] {"page 2"}, collectedAlerts);
        assertEquals(2, countingJavaScriptEngine.getExecuteScriptCount());
        assertEquals(1, countingJavaScriptEngine.getCompileCount());
    }

    /**
     * Test that code in script tags is executed on page load. Try different combinations
     * of the script tag except for the case where a remote JavaScript page is loaded. That
     * one will be tested separately.
     * @throws Exception if something goes wrong
     */
    @Test
    public void scriptTags_AllLocalContent() throws Exception {
        final String content
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>One</script>\n" // no language specified - assume JavaScript
            + "<script language='javascript'>Two</script>\n"
            + "<script type='text/javascript'>Three</script>\n"
            + "<script type='text/perl'>Four</script>\n" // type is unsupported language
            + "</head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "</body></html>";
        final List<String> collectedScripts = new ArrayList<String>();
        loadPageAndCollectScripts(content, collectedScripts);

        // NO MORE: The last expected is the dummy stub that is needed to initialize the JavaScript engine
        final String[] expectedScripts = {"One", "Two", "Three"};

        assertEquals(expectedScripts, collectedScripts);
    }

    private HtmlPage loadPageAndCollectScripts(final String html, final List<String> collectedScripts)
        throws Exception {

        final WebClient client = getWebClient();
        client.setJavaScriptEngine(new JavaScriptEngine(client) {
            @Override
            public Object execute(final HtmlPage htmlPage, final String sourceCode,
                    final String sourceName, final int startLine) {
                collectedScripts.add(sourceCode);
                return null;
            }
            @Override
            public Object callFunction(
                    final HtmlPage htmlPage, final Function javaScriptFunction,
                    final Scriptable thisObject, final Object [] args,
                    final DomNode htmlElement) {
                return null;
            }
            @Override
            public boolean isScriptRunning() {
                return false;
            }
        });

        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setDefaultResponse(html);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(new WebRequest(getDefaultUrl(), HttpMethod.POST));
        return page;
    }

    /**
     * Verifies that we're not using a global context factory, so that we can cleanly run multiple
     * WebClient instances concurrently within a single JVM. See bug 2089599.
     */
    @Test
    public void noGlobalContextFactoryUsed() {
        final WebClient client1 = getWebClient();
        final WebClient client2 = createNewWebClient();

        final ContextFactory cf1 = client1.getJavaScriptEngine().getContextFactory();
        final ContextFactory cf2 = client2.getJavaScriptEngine().getContextFactory();

        assertFalse(cf1 == cf2);
        assertFalse(cf1 == ContextFactory.getGlobal());
        assertFalse(cf2 == ContextFactory.getGlobal());
    }

    /**
     * Configure subclass of {@link JavaScriptEngine} that collects background JS expressions.
     * @throws Exception if the test fails
     */
    @Test
    public void catchBackgroundJSErrors() throws Exception {
        Locale.setDefault(Locale.US); // Rhino has localized error message... for instance for French
        final WebClient webClient = getWebClient();
        final List<ScriptException> jsExceptions = new ArrayList<ScriptException>();
        final JavaScriptEngine myEngine = new JavaScriptEngine(webClient) {
            @Override
            protected void handleJavaScriptException(final ScriptException scriptException,
                    final boolean triggerOnError) {
                jsExceptions.add(scriptException);
                super.handleJavaScriptException(scriptException, triggerOnError);
            }
        };
        webClient.setJavaScriptEngine(myEngine);

        final String html = "<html>\n"
            + "<head><title>Test page</title><\n"
            + "<script>\n"
            + "function myFunction() {\n"
            + "  document.title = 'New title';\n"
            + "  notExisting(); // will throw\n"
            + "}\n"
            + "window.setTimeout(myFunction, 5)\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(URL_FIRST);
        webClient.waitForBackgroundJavaScript(10000);
        assertEquals("New title", page.getTitleText());

        assertEquals(1, jsExceptions.size());
        final ScriptException exception = jsExceptions.get(0);
        assertTrue("Message: " + exception.getMessage(),
            exception.getMessage().contains("\"notExisting\" is not defined"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "found")
    public void enumerateMethods() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    for (var x in document) {\n"
            + "      if (x == 'getElementById')\n"
            + "        alert('found');"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Unit tests for bug 2531218 reported by Rhino as
     * <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=477604">Bug 477604 -
     * Array.concat causes ArrayIndexOutOfBoundException with non dense array</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void array_concat() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var a = [1, 2, 3];\n"
            + "    for (var i=10; i<20; ++i)\n"
            + "      a[i] = 't' + i;\n"
            + "    var b = [1, 2, 3];\n"
            + "    b.concat(a);\n"
            + "    alert(b.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * String value of native functions starts with \n on IE.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(IE)
    @Alerts(FF = { "0", "false", "0" },
            IE = { "1", "true", "1" })
    public void nativeFunction_toStringValue() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(String(window.alert).indexOf('function'));\n"
            + "    alert(String(window.alert).charAt(0) == '\\n');\n"
            + "    alert(String(document.getElementById).indexOf('function'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void function_toStringValue() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function f() {}\n"
            + "  function test() {\n"
            + "    alert(String(f).indexOf('function'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "1", "2" })
    public void function_object_method() throws Exception {
        if (getBrowserVersion().isIE()) {
            final String html = "<html><head><title>foo</title><script>\n"
                    + "  try {\n"
                    + "    alert('1');\n"
                    + "    function document.onclick() {\n"
                    + "      alert('hi');\n"
                    + "    }\n"
                    + "    alert('2');\n"
                    + "  } catch(e) {alert(e)}\n"
                    + "</script></head><body>\n"
                    + "  <div id='myDiv'>Hello there</div>\n"
                    + "</body></html>";

            loadPageWithAlerts(html);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("that's it")
    public void quoteAsUnicodeInString() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "alert('that\\x27s it');\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("error")
    public void recursion() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function recurse(c) {\n"
            + "    try {\n"
            + "      recurse(c++);\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='recurse(1)'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Ensures that the JS executor thread is a daemon thread.
     * @throws Exception if the test fails
     */
    @Test
    public void daemonExecutorThread() throws Exception {
        final String html = "<html><body><script>\n"
            + "function f() { alert('foo'); }\n"
            + "setTimeout(f, 5);\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(getBrowserVersion(), html, collectedAlerts);

        Thread.sleep(20);
        final List<Thread> jsThreads = getJavaScriptThreads();
        assertEquals(1, jsThreads.size());
        final Thread jsThread = jsThreads.get(0);
        assertEquals("JS executor for " + page.getWebClient(), jsThread.getName());
        assertTrue(jsThread.isDaemon());
    }
}
