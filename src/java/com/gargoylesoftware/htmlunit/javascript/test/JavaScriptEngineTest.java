/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.test;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.test.FakeWebConnection;
import com.gargoylesoftware.htmlunit.test.WebTestCase;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class JavaScriptEngineTest extends WebTestCase {
    public JavaScriptEngineTest( final String name ) {
        super(name);
    }


    public void testSetJavascriptEnabled_false() throws Exception {
        final WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);
        final FakeWebConnection webConnection = new FakeWebConnection( client );

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

        webConnection.setContent( content );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );

        final HtmlTextInput textInput = (HtmlTextInput)page.getHtmlElementById("textfield1");
        assertEquals("foo", textInput.getValueAttribute());
    }

    /**
     * Try to set the value of a text input field.
     */
    public void testSetInputValue() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "document.form1.textfield1.value='blue'"
                 + "</script></head><body>"
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


    public void testExternalScript() throws Exception {

        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

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
            new URL("http://www.gargoylesoftware.com"),
            htmlContent, 200, "OK", "text/html", Collections.EMPTY_LIST );
        webConnection.setResponse(
            new URL("http://www.gargoylesoftware.com/foo.js"),
            jsContent, 200, "OK", "text/javascript", Collections.EMPTY_LIST );
        client.setWebConnection( webConnection );

        final List expectedAlerts = Collections.singletonList("got here");
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        final HtmlScript htmlScript = (HtmlScript)page.getHtmlElementById("script1");
        assertNotNull(htmlScript);
        assertEquals( expectedAlerts, collectedAlerts );
    }


    /**
     * Set value on input expects a string.  If you pass in a value that isn't a string
     * this used to blow up.
     */
    public void testSetValuesThatAreNotStrings() throws Exception {
        final String content
                 = "<html><head><title>foo</title><script>"
                 + "document.form1.textfield1.value=1;"
                 + "alert(document.form1.textfield1.value)"
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
            "1"
        });
        assertEquals( expectedAlerts, collectedAlerts );
    }


    public void testReferencingVariablesFromOneScriptToAnother_Regression() throws Exception {

        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

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


    public void testJavaScriptUrl() throws Exception {

        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

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


    public void testJavaScriptWrappedInHtmlComments() throws Exception {

        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

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


    public void testJavaScriptWrappedInHtmlComments_commentOnOpeningLine() throws Exception {

        final WebClient client = new WebClient();
        final FakeWebConnection webConnection = new FakeWebConnection( client );

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
}
