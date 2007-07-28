/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Style}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class StyleTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public StyleTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyle_OneCssAttribute() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var node = document.getElementById('div1');\n"
            + "    var style = node.style;\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
            + "    alert(node.getAttribute('style'));\n"
            + "}\n</script></head>"
            + "<body onload='doTest()'><div id='div1' style='color: black'>foo</div></body></html>";

        final String[] expectedAlerts = {"black", "pink", "color: pink;"};
        createTestPageForRealBrowserIfNeeded(firstContent, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(firstContent, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);

        assertEquals("color: pink;", page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyle_MultipleCssAttributes() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var style = document.getElementById('div1').style;\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
            + "}\n</script></head>"
            + "<body onload='doTest()'>"
            + "<div id='div1' style='color: black;background:blue;foo:bar'>foo</div></body></html>";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(firstContent, collectedAlerts);

        final String[] expectedAlerts = {"black", "pink"};
        assertEquals(expectedAlerts, collectedAlerts);

        assertEquals(
            "background: blue; color: pink; foo: bar;",
            page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyle_OneUndefinedCssAttribute() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var style = document.getElementById('div1').style;\n"
            + "    alert(document.getElementById('nonexistingid'));\n"
            + "    alert(style.color);\n"
            + "    style.color = 'pink';\n"
            + "    alert(style.color);\n"
            + "}\n</script></head>"
            + "<body onload='doTest()'><div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {"null", "", "pink"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);

        assertEquals("color: pink;", page.getHtmlElementById("div1").getAttributeValue("style"));
    }

    /**
     * Even if javascript isn't really executed according to the browser version used,
     * it may have some side effects if configuration is incorrect.
     * @throws Exception if the test fails
     */
    public void testMozillaStyle() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.visibility);\n"
            + "    oDiv.style.visibility = 'hidden';\n"
            + "    alert(oDiv.style.visibility);\n"
            + "    alert(oDiv.style.behavior);\n"
            + "}\n</script></head>"
            + "<body onload='doTest()'>"
            + "<div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {"", "hidden", "undefined"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testIEStyle() throws Exception {
        final String content
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv = document.getElementById('div1');\n"
            + "    alert(oDiv.style.behavior);\n"
            + "}\n"
            + "</script></head>"
            + "<body onload='doTest()'>"
            + "<div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {""};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Checks that the scopes are correctly set on the style element (wasn't working in CVS snapshot 2005.01.23)
     * @throws Exception if the test fails
     */
    public void testOnclickAccessStyle() throws Exception {
        final String content = "<html><head><title>Color Change Page</title>"
             + "<script>"
             + "function test(obj)"
             + "{"
             + "   obj.style.backgroundColor = 'yellow';"
             + "}"
             + "</script>"
             + "</head>"
             + "<body>"
             + "<span id='red' onclick='test(this)'>foo</span>"
             + "</body></html>";

        final HtmlPage page = loadPage(content);
        ((ClickableElement) page.getHtmlElementById("red")).click();
    }

    /**
     * @throws Exception if the test fails
     */
    public void testAccessProperties() throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
                + "function doTest() {\n"
                + "    var oDiv = document.getElementById('div1');\n"
                + "    alert(typeof oDiv.style.visibility);\n"
                + "    alert(typeof oDiv.style.color);\n"
                + "    alert(typeof oDiv.style.foo);\n"
                + "}\n</script></head>"
                + "<body onload='doTest()'>"
                + "<div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {"string", "string", "undefined"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 1592299
     * @throws Exception if the test fails
     */
    public void testSetStylePropertyNonString() throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
                + "function doTest() {\n"
                + "    var oDiv1 = document.getElementById('div1');\n"
                + "    oDiv1.style.pixelLeft = 123;\n"
                + "    alert(oDiv1.style.pixelLeft);\n"
                + "}\n</script></head>"
                + "<body onload='doTest()'>"
                + "<div id='div1'>foo</div></body></html>";

        final String[] expectedAlerts = {"123"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testGetPropertyValue() throws Exception {
        testGetPropertyValue(BrowserVersion.FIREFOX_2);
        try {
            testGetPropertyValue(BrowserVersion.INTERNET_EXPLORER_7_0);
            fail("getPropertyValue is not supported in IE");
        }
        catch (final Exception e) {
            //expected
        }
    }

    private void testGetPropertyValue(final BrowserVersion browserVersion) throws Exception {
        final String content = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var oDiv1 = document.getElementById('div1');\n"
            + "    alert(oDiv1.style.getPropertyValue('background'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='div1' style='background: blue'>foo</div></body></html>";

        final String[] expectedAlerts = {"blue"};
        final List collectedAlerts = new ArrayList();
        loadPage(browserVersion, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyleFilter_IE() throws Exception {
        testStyleFilter(BrowserVersion.INTERNET_EXPLORER_6_0, new String[] {"", "alpha(opacity=50)"});
    }

    /**
     * @throws Exception if the test fails
     */
    public void testStyleFilter_FF() throws Exception {
        testStyleFilter(BrowserVersion.FIREFOX_2, new String[] {"undefined", "undefined"});
    }

    private void testStyleFilter(final BrowserVersion browserVersion, final String[] expected) throws Exception {
        final String html = "<html><body onload='test()'><script>\r\n"
            + "   function test(){\r\n"
            + "      var div1 = document.getElementById('div1');\r\n"
            + "      alert(div1.style.filter);\r\n"
            + "      var div2 = document.getElementById('div2');\r\n"
            + "      alert(div2.style.filter);\r\n"
            + "   }\r\n"
            + "</script>\r\n"
            + "<div id='div1'>foo</div>\r\n"
            + "<div id='div2' style='filter:alpha(opacity=50)'>bar</div>\r\n"
            + "</body></html>\r\n";
        final List actual = new ArrayList();
        loadPage(browserVersion, html, actual);
        assertEquals(expected, actual);
    }

}
