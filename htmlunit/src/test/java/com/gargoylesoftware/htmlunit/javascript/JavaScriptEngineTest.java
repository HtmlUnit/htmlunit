/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
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
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Noboru Sinohara
 * @author  Darrell DeBoer
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
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

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final HtmlTextInput textInput = (HtmlTextInput)page.getHtmlElementById("textfield1");
        assertEquals("foo", textInput.getValueAttribute());
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

        webConnection.setResponse(
            URL_GARGOYLE,
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/foo.js"),
            jsContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final List expectedAlerts = Collections.singletonList("got here");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlScript htmlScript = (HtmlScript)page.getHtmlElementById("script1");
        assertNotNull(htmlScript);
        assertEquals( expectedAlerts, collectedAlerts );
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

        webConnection.setResponse(
            URL_GARGOYLE,
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );

        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/hidden"),
            htmlContent2, 200, "OK", "text/html", Collections.EMPTY_LIST );

        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/foo.js"),
        // make SJIS bytes as responsebody
            new String(jsContent.getBytes("SJIS"),"8859_1"),
        200, "OK", "text/javascript", Collections.EMPTY_LIST );

    /*
     * foo2.js is same with foo.js
     */
        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/foo2.js"),
        // make SJIS bytes as responsebody
            new String(jsContent.getBytes("SJIS"),"8859_1"),
        200, "OK", "text/javascript", Collections.EMPTY_LIST );

        client.setWebConnection( webConnection );

        final List expectedAlerts = Collections.singletonList("\u8868");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

    /*
     * detect encoding from meta tag
     */
        final HtmlPage page = ( HtmlPage )client.getPage(
             URL_GARGOYLE,
             SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlScript htmlScript =
                     (HtmlScript)page.getHtmlElementById("script1");

    assertNotNull(htmlScript);
        assertEquals( expectedAlerts, collectedAlerts );

    /*
     * detect encoding from charset attribute of script tag
     */
    collectedAlerts.clear();
        final HtmlPage page2 = ( HtmlPage )client.getPage(
             new URL( "http://www.gargoylesoftware.com/hidden" ),
             SubmitMethod.POST, Collections.EMPTY_LIST );
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

        webConnection.setResponse(
            new URL("http://first/index.html"),
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://first/test.js"),
            jsContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first/index.html" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());

    }


    /**
     * @throws Exception if the test fails
     */
    public void testJavaScriptUrl() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String htmlContent
             = "<html><head><script language='javascript'>"
             + "var f1 = '<html><head><title>frame1</title></head><body><h1>frame1</h1></body></html>';\n"
             + "var f2 = '<html><head><title>frame2</title></head><body><h1>frame2</h1></body></html>';\n"
             + "</script></head>\n"
             + "<frameset border='0' frameborder='0' framespacing='0' rows='100,*'>"
             + "    <frame id='frame1' src='javascript:parent.f1'/>"
             + "    <frame id='frame2' src='javascript:parent.f2'/>"
             + "</frameset></html>";

        webConnection.setResponse(
            new URL("http://first/index.html"),
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first/index.html" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

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

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String htmlContent
             = "<html><head><title>foo</title><script language='javascript'><!--\n"
             + "function doTest() {\n"
             + "}\n"
             + "-->\n</script></head>\n"
             + "<body onload='doTest()'></body></html>";

        webConnection.setResponse(
            new URL("http://first/index.html"),
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first/index.html" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());

    }


    /**
     * @throws Exception if the test fails
     */
    public void testJavaScriptWrappedInHtmlComments_commentOnOpeningLine() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final String htmlContent
             = "<html><head><title>foo</title><script language='javascript'><!-- Some comment here\n"
             + "function doTest() {\n"
             + "}\n"
             + "-->\n</script></head>\n"
             + "<body onload='doTest()'></body></html>";

        webConnection.setResponse(
            new URL("http://first/index.html"),
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first/index.html" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());
    }


    /**
     * When using the syntax this.something in an onclick handler, "this" must represent
     * the object being clicked, not the window.  Regression test.
     * @throws Exception if the test fails
     */
    public void testThisDotInOnClick() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final String htmlContent
            = "<html><head><title>First</title><script>function foo(message){alert(message);}</script><body>"
             + "<form name='form1'><input type='submit' name='button1' onClick='foo(this.name)'></form>"
             + "</body></html>";

        webConnection.setResponse(
            new URL("http://first/index.html"),
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first/index.html" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
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
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://first/test.js"),
            jsContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first/index.html" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        assertEquals("foo", page.getTitleText());
        assertEquals( Collections.singletonList("Got to external method"), collectedAlerts );
    }


    /**
     * Test case for bug 707134.  Currently I am unable to reproduce the problem.
     * @throws Exception if the test fails
     */
    public void testFunctionDefinedInSameFile() throws Exception {

        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );

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

        webConnection.setResponse(
            new URL("http://first/index.html"),
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://first/index.html" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
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
     * Test that the file JavaScriptConfiguration.xml is valid.
     * @throws Exception If the test fails
     */
    public void testConfigurationFileAgainstSchema() throws Exception {
        final XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        final String directory = "src/java/com/gargoylesoftware/htmlunit/javascript/";
        parser.setFeature("http://xml.org/sax/features/validation", true);
        parser.setFeature("http://apache.org/xml/features/validation/schema", true);
        parser.setEntityResolver( new EntityResolver() {
            public InputSource resolveEntity (String publicId, String systemId) throws IOException {
                return createInputSourceForFile(directory+"JavaScriptConfiguration.xsd");
            }
        });
        parser.setErrorHandler( new ErrorHandler() {
            public void warning(SAXParseException exception) throws SAXException {
                throw exception;
            }
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
        });

        parser.parse( createInputSourceForFile(directory+"JavaScriptConfiguration.xml") );

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

        final HtmlPage page = ( HtmlPage )client.getPage(
                URL_GARGOYLE,
                SubmitMethod.POST, Collections.EMPTY_LIST );

        assertEquals(1, countingJavaScriptEngine.getExecutionCount());

        ((HtmlAnchor) page.getHtmlElementById("unqualified")).click();
        assertEquals(3, countingJavaScriptEngine.getExecutionCount());

        ((HtmlAnchor) page.getHtmlElementById("qualified")).click();
        assertEquals(5, countingJavaScriptEngine.getExecutionCount());

        final List expectedAlerts = Arrays.asList(new String[]{"unqualified: foo", "qualified: foo"});
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test that Java objects placed in the active x map can be instantiated and used within
     * javascript using the IE specific ActiveXObject constructor.
     * @throws Exception If the test fails
     */
    public void testActiveXObject() throws Exception {
        final WebClient client = new WebClient();
        final MockWebConnection webConnection = new MockWebConnection( client );
        final Map map = new HashMap();
        map.put("MockActiveXObject", "com.gargoylesoftware.htmlunit.javascript.MockActiveXObject");
        map.put("FakeObject", "com.gargoylesoftware.htmlunit.javascript.NoSuchObject");
        map.put("BadObject", new Object());

        // Test for 0 arguments in the constructor
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject()" ) );
        client.setWebConnection( webConnection );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for zero argument constructor." );
        } catch( ScriptException e ) {
            // Success
        }

        // Test for 3 arguments in the constructor
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject(1, '2', '3')" ) );
        client.setWebConnection( webConnection );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for a three argument constructor." );
        } catch( ScriptException e ) {
             // Success
        }

        // Test for an undefined argument in the constructor
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject(a)" ) );
        client.setWebConnection( webConnection );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for an undefined parameter in the constructor." );
        } catch( ScriptException e ) {
            // Success
        }

        // Test for an integer in the constructor
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject(10)" ) );
        client.setWebConnection( webConnection );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for an integer parameter in the constructor." );
        } catch( ScriptException e ) {
            // Success
        }

        // Test for a null map
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject('UnknownObject')" ) );
        client.setWebConnection( webConnection );
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for a null map." );
        } catch( ScriptException e ) {
            // Success
        }

        // Test for an non existent object in the map
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject('UnknownObject')" ) );
        client.setWebConnection( webConnection );
        client.setActiveXObjectMap(map);
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for non existent object in the map." );
        } catch( ScriptException e ) {
            // Success
        }

        // Test for an invalid object in the map
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject('BadObject', 'server')" ) );
        client.setWebConnection( webConnection );
        client.setActiveXObjectMap(map);
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for an invalid object in the map." );
        } catch( ScriptException e ) {
            // Success
        }

        // Test for a non existent class in the map
        webConnection.setDefaultResponse( getJavaScriptContent( "new ActiveXObject('FakeObject')" ) );
        client.setWebConnection( webConnection );
        client.setActiveXObjectMap(map);
        try {
            client.getPage( new URL( "http://www.yahoo.com" ) );
            fail( "An exception should be thrown for a non existent object in the map." );
        } catch( ScriptException e ) {
            // Success
        }

        // Try a valid object in the map
        webConnection.setDefaultResponse( getJavaScriptContent(
                "var t = new ActiveXObject('MockActiveXObject'); alert(t.Message);" ) );
        client.setWebConnection( webConnection );
        client.setActiveXObjectMap(map);
        client.setAlertHandler( new AlertHandler() {
            public void handleAlert( final Page page, final String message ) {
                if( !message.equals( new MockActiveXObject().Message ) ) {
                    fail( "The active x object did not bind to the object." );
                }
            }

        });
        client.getPage( new URL( "http://www.yahoo.com" ) );

        // Try a valid object in the map
        webConnection.setDefaultResponse( getJavaScriptContent(
                "var t = new ActiveXObject('MockActiveXObject', 'server'); alert(t.GetMessage());" ) );
        client.setWebConnection( webConnection );
        client.setActiveXObjectMap(map);
        client.setAlertHandler( new AlertHandler() {
            public void handleAlert( final Page page, final String message ) {
                if( !message.equals( new MockActiveXObject().GetMessage() ) ) {
                    fail( "The active x object did not bind to the object." );
                }
            }

        });
        client.getPage( new URL( "http://www.yahoo.com" ) );
    }

    private String getJavaScriptContent( String javascript ) {
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
    
    private InputSource createInputSourceForFile( final String fileName ) throws FileNotFoundException {
        return new InputSource( getFileAsStream(fileName) );
    }

    private static final class CountingJavaScriptEngine extends ScriptEngine {
        private ScriptEngine delegate_;
        private int scriptExecutionCount_ = 0;

        protected CountingJavaScriptEngine(ScriptEngine delegate) {
            super(delegate.getWebClient());
            delegate_ = delegate;
        }

        /**
         * Initialize with a given page.
         * @param page The page.
         */
        public void initialize(HtmlPage page) {
            delegate_.initialize(page);
        }

        /** @inheritDoc ScriptEngine#execute(HtmlPage,String,String,HtmlElement) */
        public Object execute(HtmlPage htmlPage, String sourceCode, String sourceName, HtmlElement htmlElement) {
            scriptExecutionCount_++;
            return delegate_.execute(htmlPage, sourceCode, sourceName, htmlElement);
        }

        /** @inheritDoc ScriptEngine#callFunction(HtmlPage,Object,Object,Object[],HtmlElement) */
        public Object callFunction(HtmlPage htmlPage, Object javaScriptFunction, Object thisObject,
                Object[] args, HtmlElement htmlElementScope) {
            return delegate_.callFunction(htmlPage, javaScriptFunction, thisObject, args, htmlElementScope);
        }

        /** @inheritDoc ScriptEngine#toString(HtmlPage,Object) */
        public String toString(HtmlPage htmlPage, Object javaScriptObject) {
            return delegate_.toString(htmlPage, javaScriptObject);
        }

        /** @return The number of times that this engine has executed code */
        public int getExecutionCount() {
            return scriptExecutionCount_;
        }
    }
}

