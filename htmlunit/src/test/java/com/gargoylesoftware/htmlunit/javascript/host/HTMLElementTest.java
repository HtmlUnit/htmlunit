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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for NodeImpl
 * 
 * @author yourgod
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @version $Revision$
 */
public class HTMLElementTest extends WebTestCase {

    /**
     * @param name The name of the test case
     */
    public HTMLElementTest(String name) {
        super(name);
    }

    /**
     * @throws Exception on test failure
     */
    public void test_getAttribute() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById(\'myNode\');\n" +
                "       alert(myNode.title);\n" +
                "       alert(myNode.getAttribute('title'));\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload=\'doTest()\'>\n" +
                "<p id=\'myNode\' title=\'a\'>\n" +
                "</p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[]{
            "a", "a"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception on test failure
     */
    public void test_setAttribute() throws Exception {
        final String content = "<html>\n" + 
                "<head>\n" + 
                "    <title>test</title>\n" + 
                "    <script>\n" + 
                "    function doTest(){\n" + 
                "       var myNode = document.getElementById(\'myNode\');\n" + 
                "       alert(myNode.title);\n" + 
                "       myNode.setAttribute(\'title\', \'b\');\n" + 
                "       alert(myNode.title);\n" + 
                "   }\n" + 
                "    </script>\n" + 
                "</head>\n" + 
                "<body onload=\'doTest()\'>\n" + 
                "<p id=\'myNode\' title=\'a\'>\n" + 
                "</p>\n" + 
                "</body>\n" + 
                "</html>\n" + 
                "";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);
        assertEquals("test", page.getTitleText());

        final List expectedAlerts = Arrays.asList(new String[]{
            "a", "b"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Test for getElementsByTagName
     * @throws Exception if the test fails
     */
    public void testGetElementsByTagName() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "var a1 = document.getElementsByTagName('td');\n"
            + "alert('all = ' + a1.length);\n"
            + "var firstRow = document.getElementById('r1');\n"
            + "var rowOnly = firstRow.getElementsByTagName('td');\n"
            + "alert('row = ' + rowOnly.length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<table>\n"
            + "<tr id='r1'><td>1</td><td>2</td></tr>\n"
            + "<tr id='r2'><td>3</td><td>4</td></tr>\n"
            + "</table>\n"
            + "</body></html>\n";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "all = 4", "row = 2"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * Test getting the class for the element
     * @throws Exception if the test fails
     */
    public void testGetClassName() throws Exception {
        final String content
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>\n";
          
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "the class is x"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }
    /**
     * Test getting the class for the element
     * @throws Exception if the test fails
     */
    public void testSetClassName() throws Exception {
        final String content
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    ele.className = 'z';"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>\n";
          
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "the class is z"
        });

        assertEquals(expectedAlerts, collectedAlerts);
    }
    
    /**
     * 
     * @throws Exception if the test fails
     */
    public void testGetSetInnerHTMLSimple() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById('myNode');\n" +
                "       alert('Old = ' + myNode.innerHTML);\n" +
                "       myNode.innerHTML = 'New  cell value';\n" +
                "       alert('New = ' + myNode.innerHTML);\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>\n" +
                "<p id='myNode'><b>Old innerHTML</b></p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "Old = <b>Old innerHTML</b>",
            "New = New cell value"
        });
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * This test will have to change when jsSet_innerHTML changes to
     * support setting complex values with embedded html.
     * 
     * @throws Exception if the test fails
     */
    public void testGetSetInnerHTMLComplex() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>test</title>\n" +
                "    <script>\n" +
                "    function doTest(){\n" +
                "       var myNode = document.getElementById('myNode');\n" +
                "       alert('Old = ' + myNode.innerHTML);\n" +
                "       myNode.innerHTML = '<i id=\"newElt\">New cell value</i>';\n" +
                "       alert('New = ' + myNode.innerHTML);\n" +
                "   }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>\n" +
                "<p id='myNode'><b>Old innerHTML</b></p>\n" +
                "</body>\n" +
                "</html>\n" +
                "";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "Old = <b>Old innerHTML</b>",
            "New = <i id=\"newElt\">New cell value</i>"
        });
        assertEquals(expectedAlerts, collectedAlerts);

        final HtmlElement elt = page.getHtmlElementById("newElt");
        assertEquals("New cell value", elt.asText());
    }

    /**
     * Test the <tt>#default#homePage</tt> default IE behavior.
     * 
     * @throws Exception if the test fails
     */
    public void testAddBehaviorDefaultHomePage() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>Test</title>\n" +
                "    <script>\n" +
                "    function doTest() {\n" +
                "       var body = document.body;\n" +
                "       body.addBehavior('#default#homePage');\n" +
                "       var url = '" + URL_SECOND.toExternalForm() + "';\n" +
                "       alert('isHomePage = ' + body.isHomePage(url));\n" +
                "       body.setHomePage(url);\n" +
                "       alert('isHomePage = ' + body.isHomePage(url));\n" +
                "       body.navigateHomePage();\n" +
                "    }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>Test</body>\n" +
                "</html>";
        final String content2 = "<html></html>";
        
        final WebClient client = new WebClient();
        final List collectedAlerts = new ArrayList();
        client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setResponse(URL_FIRST, content);
        webConnection.setResponse(URL_SECOND, content2);
        client.setWebConnection( webConnection );

        final HtmlPage page = (HtmlPage) client.getPage(URL_FIRST);

        final List expectedAlerts = Arrays.asList(new String[]{
            "isHomePage = false",
            "isHomePage = true"
        });
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals(URL_SECOND.toExternalForm(), page.getWebResponse().getUrl().toExternalForm());
    }

    /**
     * Test the removal of behaviors.
     * 
     * @throws Exception if the test fails
     */
    public void testRemoveBehavior() throws Exception {
        final String content = "<html>\n" +
                "<head>\n" +
                "    <title>Test</title>\n" +
                "    <script>\n" +
                "    function doTest() {\n" +
                "       var body = document.body;\n" +
                "       alert('body.isHomePage = ' + body.isHomePage);\n" +
                "       var id = body.addBehavior('#default#homePage');\n" +
                "       alert('body.isHomePage = ' + body.isHomePage('not the home page'));\n" +
                "       body.removeBehavior(id);\n" +
                "       alert('body.isHomePage = ' + body.isHomePage);\n" +
                "    }\n" +
                "    </script>\n" +
                "</head>\n" +
                "<body onload='doTest()'>Test</body>\n" +
                "</html>";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        final List expectedAlerts = Arrays.asList(new String[]{
            "body.isHomePage = undefined",
            "body.isHomePage = false",
            "body.isHomePage = undefined"
        });
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
