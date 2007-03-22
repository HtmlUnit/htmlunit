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
package com.gargoylesoftware.htmlunit.javascript;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;


/**
 * Tests for the Javascript engine
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author Darrell DeBoer
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author David K. Taylor
 */
public class JavaScriptEngineTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public JavaScriptEngineTest( final String name ) {
        super(name);
    }


    /**
     * @throws Exception if the test fails
     */
    public void testSetJavascriptEnabled_false() throws Exception {
        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String content
            = "<html><head><title>foo</title><script>"
            + "document.form1.textfield1='blue'"
            + "</script></head><body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</body></html>";

        webConnection.setDefaultResponse( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);

        final HtmlTextInput textInput = (HtmlTextInput)page.getHtmlElementById("textfield1");
        assertEquals("foo", textInput.getValueAttribute());
    }

    /**
     * Regression test for bug
     * https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1609944&group_id=47038
     * @throws Exception if the test fails
     */
    public void testOnloadJavascriptFunction() throws Exception {
    	if (notYetImplemented()) {
    		return;
    	}
    	
        final String content
            = "<html><head><title>foo</title><script>"
            + "function onload() {alert('foo');}"
            + "</script></head><body>"
            + "</body></html>";

        final String[] expectedAlerts = { "foo" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }
    
    /**
     * Try to set the value of a text input field.
     * @throws Exception if the test fails
     */
    public void testSetInputValue() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest(){\n"
            + "    document.form1.textfield1.value='blue'"
            + "}\n"
            + "</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</body></html>";
        final List collectedAlerts = null;
        final HtmlPage page = loadPage(content, collectedAlerts);

        final HtmlTextInput textInput = (HtmlTextInput)page.getHtmlElementById("textfield1");
        assertEquals("blue", textInput.getValueAttribute());
    }


    /**
     * @throws Exception if the test fails
     */
    public void testAlert() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "alert('foo')"
            + "</script></head><body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "foo"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Checks that a dynamically compiled function works in the scope of its birth
     * @throws Exception if the test fails
     */
    public void testScopeOfNewFunction() throws Exception {
        final String content
            = "<html><head><script>"
            + "var f = new Function('alert(\"foo\")');"
            + "f();"
            + "</script></head><body>"
            + "</body></html>";
        final List expectedAlerts = Arrays.asList( new String[]{"foo"});
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testScopeOfNestedNewFunction() throws Exception {
        final List expectedAlerts = Collections.singletonList("foo");
        final String content
            = "<html><head>"
            + "<script>\n"
            + "var foo = 'foo';\n"
            + "var f1 = new Function('f = new Function(\"alert(foo)\"); f()');\n"
            + "f1();\n"
            + "</script>"
            + "</head>"
            + "<body>\n"
            + "</body></html>\n";
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Checks that a dynamically compiled function works in the scope of its birth
     * and not the other window
     * @throws Exception if the test fails
     */
    public void testScopeOfNewFunctionCalledFormOtherWindow() throws Exception {
        final String firstContent
            = "<html><head>"
            + "<script>"
            + "var foo = 'foo';"
            + "var test = new Function('alert(foo);');"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "  <iframe src='page2.html'/>"
            + "</body>"
            + "</html>";

        final String secondContent = "<html><head><script>"
            + "var foo = 'foo2';"
            + "parent.test();"
            + "var f = parent.test;"
            + "f();"
            + "</script></head></html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( secondContent );
        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection( webConnection );

        final List expectedAlerts = Arrays.asList( new String[]{"foo", "foo", "foo"});

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );
        client.getPage(URL_FIRST);

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * If a reference has been hold on a page and the page is not
     * anymore the one contained in "its" window, javascript execution should
     * work... a bit
     * @throws Exception if the test fails
     */
    public void testScopeInInactivePage() throws Exception {
        final String firstContent
            = "<html><head>"
            + "<script>"
            + "var foo = 'foo';"
            + "</script>"
            + "</head>"
            + "<body>"
            + "  <a href='page2.html'>to page 2</a>"
            + "  <div id='testdiv' onclick='alert(foo)'>foo</div>"
            + "</body>"
            + "</html>";

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse("<html></html>");
        webConnection.setResponse(URL_FIRST, firstContent);
        client.setWebConnection( webConnection );

        final List expectedAlerts = Arrays.asList( new String[]{"foo"});

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );
        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        final ClickableElement div = ((ClickableElement) page.getHtmlElementById("testdiv"));

        ((HtmlAnchor) page.getAnchors().get(0)).click();
        // ignore response, and click in the page again
        div.click();

        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * @throws Exception if the test fails
     */
    public void testExternalScript() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String htmlContent
            = "<html><head><title>foo</title><script src='/foo.js' id='script1'/>"
            + "</head><body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</body></html>";

        final String jsContent = "alert('got here');";

        webConnection.setResponse(URL_GARGOYLE, htmlContent);
        webConnection.setResponse(new URL("http://www.gargoylesoftware.com/foo.js"), jsContent,
                "text/javascript");
        client.setWebConnection( webConnection );

        final List expectedAlerts = Collections.singletonList("got here");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        final HtmlScript htmlScript = (HtmlScript)page.getHtmlElementById("script1");
        assertNotNull(htmlScript);
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test that the url of the page containing the script is contained in the exception's message
     * @throws Exception if the test fails
     */
    public void testScriptErrorContainsPageUrl() throws Exception {

        // embedded script
        final String content1
            = "<html><head><script>a.foo</script>"
            + "</head><body>"
            + "</body></html>";

        try {
            loadPage(content1);
        }
        catch (final Exception e) {
            assertTrue(e.getMessage().indexOf(URL_GARGOYLE.toString()) > -1);
        }

        // external script
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String content2
            = "<html><head><title>foo</title><script src='/foo.js'/>"
            + "</head><body>"
            + "</body></html>";

        final String jsContent = "a.foo = 213;";

        webConnection.setResponse(URL_GARGOYLE, content2);
        final URL urlScript = new URL("http://www.gargoylesoftware.com/foo.js");
        webConnection.setResponse(urlScript, jsContent, "text/javascript");
        client.setWebConnection( webConnection );

        try {
            client.getPage(URL_GARGOYLE);
        }
        catch (final Exception e) {
            assertTrue(e.getMessage().indexOf(urlScript.toString()) > -1);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testExternalScriptEncoding() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        /*
     * this page has meta element , and script tag has no charset attribute
     */
        final String htmlContent
            = "<html><head>"
            + "<meta http-equiv='content-type' content='text/html; charset=Shift_JIS'>"
            + "<title>foo</title>"
            + "<script src='/foo.js' id='script1'/>"
            + "</head><body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</body></html>";

    /*
     * this page has no meta element , and script tag has charset attribute
     */
        final String htmlContent2
            = "<html><head>"
            + "<title>foo</title>"
            + "<script src='/foo2.js' charset='Shift_JIS' id='script2'/>"
            + "</head><body>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</body></html>";

        /*
         * the corresponding SJIS char of '\u8868' has '\' in second byte.
         * if encoding is misspecificated,
         * this cause 'unterminated string reteral error'
         */
        final String jsContent = "alert('\u8868');";

        webConnection.setResponse(URL_GARGOYLE, htmlContent);

        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/hidden"),
            htmlContent2);

        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/foo.js"),
        // make SJIS bytes as responsebody
            new String(jsContent.getBytes("SJIS"),"8859_1"), "text/javascript");

    /*
     * foo2.js is same with foo.js
     */
        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/foo2.js"),
        // make SJIS bytes as responsebody
            new String(jsContent.getBytes("SJIS"),"8859_1"),
            "text/javascript");

        client.setWebConnection( webConnection );

        final List expectedAlerts = Collections.singletonList("\u8868");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

    /*
     * detect encoding from meta tag
     */
        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);
        final HtmlScript htmlScript =
                     (HtmlScript)page.getHtmlElementById("script1");

        assertNotNull(htmlScript);
        assertEquals( expectedAlerts, collectedAlerts );

    /*
     * detect encoding from charset attribute of script tag
     */
        collectedAlerts.clear();
        final HtmlPage page2 = ( HtmlPage )client.getPage(
             new URL( "http://www.gargoylesoftware.com/hidden"));
        final HtmlScript htmlScript2 =
                     (HtmlScript)page2.getHtmlElementById("script2");

        assertNotNull(htmlScript2);
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Set value on input expects a string.  If you pass in a value that isn't a string
     * this used to blow up.
     * @throws Exception if the test fails
     */
    public void testSetValuesThatAreNotStrings() throws Exception {
        final String content
            = "<html><head><title>foo</title><script>"
            + "function doTest() {\n"
            + "    document.form1.textfield1.value=1;"
            + "    alert(document.form1.textfield1.value)"
            + "}\n"
            + "</script></head><body onload='doTest()'>"
            + "<p>hello world</p>"
            + "<form name='form1'>"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
            + "    <input type='text' name='textfield2' id='textfield2'/>"
            + "</form>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("foo", page.getTitleText());

        final List expectedAlerts = Arrays.asList( new String[]{
            "1"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testReferencingVariablesFromOneScriptToAnother_Regression() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String htmlContent
            = "<html><head><title>foo</title><script src='./test.js'></script>"
            + "<script>var testLocalVariable = new Array();</script>"
            + "</head><body onload='testNestedMethod();' >"
            + "<form name='form1' method='POST' action='../foo' >"
            + "    <input type='submit' value='Login' name='loginButton'>"
            + "</form></body></html>";

        final String jsContent
            = "function testNestedMethod() {\n"
            + "    if (testLocalVariable == null)\n"
            + "        testLocalVariable = 'foo';\n"
            + "} ";

        webConnection.setResponse(URL_FIRST, htmlContent);
        webConnection.setResponse(new URL("http://first/test.js"), jsContent, "text/javascript");
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);
        assertEquals("foo", page.getTitleText());

    }


    /**
     * @throws Exception if the test fails
     */
    public void testJavaScriptUrl() throws Exception {

        final String htmlContent
            = "<html><head><script language='javascript'>"
            + "var f1 = '<html><head><title>frame1</title></head><body><h1>frame1</h1></body></html>';\n"
            + "var f2 = '<html><head><title>frame2</title></head><body><h1>frame2</h1></body></html>';\n"
            + "</script></head>\n"
            + "<frameset border='0' frameborder='0' framespacing='0' rows='100,*'>"
            + "    <frame id='frame1' src='javascript:parent.f1'/>"
            + "    <frame id='frame2' src='javascript:parent.f2'/>"
            + "</frameset></html>";

        createTestPageForRealBrowserIfNeeded(htmlContent, Collections.EMPTY_LIST);
        final HtmlPage page = loadPage(htmlContent);

        final HtmlPage page1 = (HtmlPage)((HtmlFrame)page.getHtmlElementById("frame1")).getEnclosedPage();
        final HtmlPage page2 = (HtmlPage)((HtmlFrame)page.getHtmlElementById("frame2")).getEnclosedPage();

        assertNotNull("page1", page1);
        assertNotNull("page2", page2);

        assertEquals( "frame1", page1.getTitleText() );
        assertEquals( "frame2", page2.getTitleText() );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testJavaScriptWrappedInHtmlComments() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title><script language='javascript'><!--\n"
            + "function doTest() {\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        assertEquals("foo", page.getTitleText());
    }


    /**
     * @throws Exception if the test fails
     */
    public void testJavaScriptWrappedInHtmlComments_commentOnOpeningLine() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title><script language='javascript'><!-- Some comment here\n"
            + "function doTest() {\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        assertEquals("foo", page.getTitleText());
    }


    /**
     * @throws Exception If the test fails.
     */
    public void testJavaScriptWrappedInHtmlComments_allOnOneLine() throws Exception {
        final String content
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
            + "</html>\n";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        final List expectedAlerts = Arrays.asList( new String[]{"undefined"} );
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testEventHandlerWithComment() throws Exception {
        final String content = "<html><body onLoad='alert(\"test\"); // xxx'></body></html>";
        final String[] expectedAlerts = { "test" };
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * When using the syntax this.something in an onclick handler, "this" must represent
     * the object being clicked, not the window.  Regression test.
     * @throws Exception if the test fails
     */
    public void testThisDotInOnClick() throws Exception {

        final String htmlContent
            = "<html><head><title>First</title><script>function foo(message){alert(message);}</script><body>"
             + "<form name='form1'><input type='submit' name='button1' onClick='foo(this.name)'></form>"
             + "</body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("First", page.getTitleText());

        ((HtmlSubmitInput)page.getFormByName("form1").getInputByName("button1")).click();

        final List expectedAlerts = Collections.singletonList("button1");
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * @throws Exception if the test fails
     */
    public void testFunctionDefinedInExternalFile_CalledFromInlineScript() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String htmlContent
            = "<html><head><title>foo</title><script src='./test.js'></script>"
            + "</head><body>"
            + "    <script>externalMethod()</script>"
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
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = (HtmlPage) client.getPage(new URL("http://first/index.html"));
        assertEquals("foo", page.getTitleText());
        assertEquals( Collections.singletonList("Got to external method"), collectedAlerts );
    }

    /**
     * Regression test for https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1552746&group_id=47038
     * @throws Exception if the test fails
     */
    public void testExternalScriptWithNewLineBeforeClosingScriptTag() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String htmlContent
            = "<html><head><title>foo</title>"
            + "</head><body>\n"
            + "<script src='test.js'>\n</script>" // \n between opening and closing tag is important
            + "</body></html>";

        final String jsContent
            = "function externalMethod() {\n"
            + "    alert('Got to external method');\n"
            + "} \n"
            + "externalMethod();";

        webConnection.setResponse(URL_FIRST, htmlContent);
        webConnection.setDefaultResponse(jsContent, 200, "OK", "text/javascript");
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        client.getPage(URL_FIRST);
        assertEquals( Collections.singletonList("Got to external method"), collectedAlerts );
    }

    /**
     * Test case for bug 707134.  Currently I am unable to reproduce the problem.
     * @throws Exception if the test fails
     */
    public void testFunctionDefinedInSameFile() throws Exception {
        final String htmlContent
            = "<html><head><title>First</title><script>"
            + "function showFoo( foo ) {\n"
            + "    alert( 'Foo is: |' + foo + '|' );\n"
            + "}\n"
            + "</script>"
            + "</head><body><form name='form1'>"
            + "<input name='text1' type='text'>\n"
            + "<input name='button1' type='button' onclick='showFoo( document.form1.text1.value);'>\n"
            + "</form></body></html>";

        final List collectedAlerts = new ArrayList();

        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        assertEquals("First", page.getTitleText());

        final HtmlForm form = page.getFormByName("form1");
        final HtmlTextInput textInput = (HtmlTextInput)form.getInputByName("text1");
        textInput.setValueAttribute("flintstone");

        final HtmlButtonInput button = (HtmlButtonInput)form.getInputByName("button1");
        assertEquals( Collections.EMPTY_LIST, collectedAlerts );

        button.click();

        assertEquals( Collections.singletonList("Foo is: |flintstone|"), collectedAlerts );
    }



    /**
     * Test that the javascript engine gets called correctly for variable access.
     * @throws Exception If the test fails
     */
    public void testJavaScriptEngineCallsForVariableAccess() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String content
            = "<html><head><title>foo</title><script>"
            + "myDate = 'foo';"
            + "function doUnqualifiedVariableAccess() {\n"
            + "    alert('unqualified: ' + myDate);\n"
            + "}\n"
            + "function doQualifiedVariableAccess() {\n"
            + "    alert('qualified: ' + window.myDate);\n"
            + "}\n"
            + "</script></head><body>"
            + "<p>hello world</p>"
            + "<a id='unqualified' onclick='doUnqualifiedVariableAccess();'>unqualified</a>"
            + "<a id='qualified' onclick='doQualifiedVariableAccess();'>qualified</a>"
            + "</body></html>";

        webConnection.setDefaultResponse( content );
        client.setWebConnection( webConnection );
        final CountingJavaScriptEngine countingJavaScriptEngine = new CountingJavaScriptEngine(
                client.getScriptEngine());
        client.setScriptEngine(countingJavaScriptEngine);

        final HtmlPage page = (HtmlPage) client.getPage(URL_GARGOYLE);

        assertEquals(1, countingJavaScriptEngine.getExecutionCount());
        assertEquals(0, countingJavaScriptEngine.getCallCount());

        ((HtmlAnchor) page.getHtmlElementById("unqualified")).click();
        assertEquals(1, countingJavaScriptEngine.getExecutionCount());
        assertEquals(1, countingJavaScriptEngine.getCallCount());

        ((HtmlAnchor) page.getHtmlElementById("qualified")).click();
        assertEquals(1, countingJavaScriptEngine.getExecutionCount());
        assertEquals(2, countingJavaScriptEngine.getCallCount());

        final List expectedAlerts = Arrays.asList(new String[]{"unqualified: foo", "qualified: foo"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * ActiveX related exceptions that do not require a map
     * @throws Exception If the test fails
     */
    public void testActiveXObjectNoMap() throws Exception {

        try {
            loadPage(getJavaScriptContent( "new ActiveXObject()" ));
            fail( "An exception should be thrown for zero argument constructor." );
        }
        catch( final ScriptException e ) {
            // Success
        }

        try {
            loadPage(getJavaScriptContent( "new ActiveXObject(1, '2', '3')" ));
            fail( "An exception should be thrown for a three argument constructor." );
        }
        catch( final ScriptException e ) {
            // Success
        }

        try {
            loadPage(getJavaScriptContent( "new ActiveXObject(a)" ));
            fail( "An exception should be thrown for an undefined parameter in the constructor." );
        }
        catch( final ScriptException e ) {
            // Success
        }

        try {
            loadPage(getJavaScriptContent( "new ActiveXObject(10)" ));
            fail( "An exception should be thrown for an integer parameter in the constructor." );
        }
        catch( final ScriptException e ) {
            // Success
        }

        try {
            loadPage(getJavaScriptContent( "new ActiveXObject('UnknownObject')" ));
            fail( "An exception should be thrown for a null map." );
        }
        catch( final ScriptException e ) {
            // Success
        }
    }

    /**
     * Test that Java objects placed in the active x map can be instantiated and used within
     * javascript using the IE specific ActiveXObject constructor.
     * @throws Exception If the test fails
     */
    public void testActiveXObjectWithMap() throws Exception {
        final Map activexToJavaMapping = new HashMap();
        activexToJavaMapping.put(
                "MockActiveXObject",
                "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        activexToJavaMapping.put(
                "FakeObject",
                "com.gargoylesoftware.htmlunit.javascript.NoSuchObject");
        activexToJavaMapping.put("BadObject", new Object());

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        client.setWebConnection(webConnection);
        client.setActiveXObjectMap(activexToJavaMapping);
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject('UnknownObject')" ) );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for non existent object in the map." );
        }
        catch( final ScriptException e ) {
            // Success
        }

        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject('BadObject', 'server')" ) );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for an invalid object in the map." );
        }
        catch( final ScriptException e ) {
            // Success
        }

        // Test for a non existent class in the map
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject('FakeObject')" ) );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for a non existent object in the map." );
        }
        catch( final ScriptException e ) {
            // Success
        }

        assertEquals("should no alerts yet", Collections.EMPTY_LIST, collectedAlerts);

        // Try a valid object in the map
        webConnection.setDefaultResponse( getJavaScriptContent(
                "var t = new ActiveXObject('MockActiveXObject'); alert(t.MESSAGE);" ) );
        client.getPage( new URL( "http://www.yahoo.com" ) );
        assertEquals(
                "The active x object did not bind to the object.",
                Collections.singletonList(MockActiveXObject.MESSAGE),
                collectedAlerts);

        collectedAlerts.clear();

        webConnection.setDefaultResponse( getJavaScriptContent(
                "var t = new ActiveXObject('MockActiveXObject', 'server'); alert(t.GetMessage());" ) );
        client.getPage( new URL( "http://www.yahoo.com" ) );
        assertEquals(
                "The active x object did not bind to the object.",
                Collections.singletonList(new MockActiveXObject().GetMessage()),
                collectedAlerts);
    }

    private String getJavaScriptContent( final String javascript ) {
        return "<html><head><title>foo</title><script>"
             + javascript
             + "</script></head><body>"
             + "<p>hello world</p>"
             + "<form name='form1'>"
             + "    <input type='text' name='textfield1' id='textfield1' value='foo' />"
             + "    <input type='text' name='textfield2' id='textfield2'/>"
             + "</form>"
             + "</body></html>";
    }

    /**
     * Check that wrong javascript just causes its context to fail but not the whole page.
     * @throws Exception If something goes wrong.
     */
    public void testScriptErrorIsolated() throws Exception {

        final String content
            = "<html>"
            + "<head>"
            + "<script>alert(1);</script>"
            + "<script>alert(2</script>"
            + "<script>alert(3);</script>"
            + "</head>"
            + "<body onload='alert(4);notExisting()'>"
            + "<button onclick='alert(5)'>click me</button>"
            + "</body>"
            + "</html>";

        final List expectedAlerts = Arrays.asList( new String[]{ "1", "3", "4" } );
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        // first test with script exceptions thrown
        try {
            client.getPage(URL_FIRST);
            fail("Should have thrown a script error");
        }
        catch (final Exception e) {
            // nothing
        }
        assertEquals(Collections.singletonList("1"), collectedAlerts);
        collectedAlerts.clear();

        // and with script exception not thrown
        client.setThrowExceptionOnScriptError(false);
        client.getPage(URL_FIRST);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for 1680026
     * https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1680026&group_id=47038
     * @throws Exception If something goes wrong.
     */
    public void testIEConditionalCompilation() throws Exception 
    {
        if (notYetImplemented()) {
            return;
        }

        final String[] alertsIE = {"testing @cc_on"};
        testIEConditionalCompilation(BrowserVersion.INTERNET_EXPLORER_6_0, alertsIE);
        final String[] alertsFF = {};
        testIEConditionalCompilation(BrowserVersion.MOZILLA_1_0, alertsFF);
    }

    /**
     * Check that wrong javascript just causes its context to fail but not the whole page.
     * @throws Exception If something goes wrong.
     */
    private void testIEConditionalCompilation(final BrowserVersion browser, final String[] expectedAlerts)
        throws Exception {

        final String content
            = "<html>"
            + "<head>"
            + "<script>"
            + "function test()"
            + "{"
            + "/*@cc_on"
            + "  alert('testing @cc_on');"
            + "@*/"
            + "}"
            + "</script>"
            + "</head>"
            + "<body onload='test()'>"
            + "</body>"
            + "</html>";

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(browser, content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that prototype changes are made in the right scope.
     * Problem reported by Bruce Faulnker in the dev mailing list.
     * This is due to a Rhino bug:
     * https://bugzilla.mozilla.org/show_bug.cgi?id=374918
     * @throws Exception If something goes wrong.
     */
    public void testPrototypeScope() throws Exception {

        if (notYetImplemented()) {
            return;
        }

        final String content1
            = "<html><head>"
            + "<script>"
            + "window.open('second.html', 'secondWindow');"
            + "</script>"
            + "</head><body></body></html>";

        final String content2
            = "<html><head>"
            + "<script>\n"
            + "alert('in page 2');\n"
            + "String.prototype.foo = function()\n"
            + "{\n"
            + "   alert('in foo');\n"
            + "};\n"
            + "var testString = 'some string';\n"
            + "testString.foo();\n"
            + "</script>"
            + "</head><body></body></html>";

        final String[] expectedAlerts = { "in page 2", "in foo" };

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(content2);
        webConnection.setResponse(URL_FIRST, content1);
        client.setWebConnection(webConnection);

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    public void testTimeout() throws Exception {
      
        final long timeout = 2000;
        JavaScriptEngine.setTimeout(timeout);
        final WebClient client = new WebClient();
        client.setThrowExceptionOnScriptError(false);

        final String content = "<html><body><script>while(1) {}</script></body></html>";
        
        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setDefaultResponse(content);
        client.setWebConnection(webConnection);

        final Exception[] exceptions = { null }; 
        final Thread runner = new Thread() {
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

        // Wait longer just to be sure we don't conclude too early
        runner.join(timeout * 2);
        if (runner.isAlive()) {
            runner.interrupt();
            fail("Script was still running after timeout");
        }
        assertNull(exceptions[0]);
    }

    private static final class CountingJavaScriptEngine extends ScriptEngine {
        private ScriptEngine delegate_;
        private int scriptExecutionCount_ = 0;
        private int scriptCallCount_ = 0;

        /**
         * Create an instance
         * @param delegate The ScriptEngine that we're wrapping.
         */
        protected CountingJavaScriptEngine(final ScriptEngine delegate) {
            super(delegate.getWebClient());
            delegate_ = delegate;
        }

        /**
         * Initialize with a given window.
         * @param window The window.
         */
        public void initialize(final WebWindow window) {
            delegate_.initialize(window);
        }

        /** @inheritDoc ScriptEngine#execute(HtmlPage,String,String,HtmlElement) */
        public Object execute(
                final HtmlPage htmlPage, final String sourceCode,
                final String sourceName, final HtmlElement htmlElement) {
            scriptExecutionCount_++;
            return delegate_.execute(htmlPage, sourceCode, sourceName, htmlElement);
        }

        /** @inheritDoc ScriptEngine#callFunction(HtmlPage,Object,Object,Object[],HtmlElement) */
        public Object callFunction(
                final HtmlPage htmlPage, final Object javaScriptFunction,
                final Object thisObject, final Object[] args,
                final HtmlElement htmlElementScope) {
            scriptCallCount_++;
            return delegate_.callFunction(htmlPage, javaScriptFunction, thisObject, args, htmlElementScope);
        }

        /** @return The number of times that this engine has called functions */
        public int getCallCount() {
            return scriptCallCount_;
        }

        /** @return The number of times that this engine has executed code */
        public int getExecutionCount() {
            return scriptExecutionCount_;
        }

        /** @return true if the script is running */
        public boolean isScriptRunning() {
            return false;
        }
    }

}

