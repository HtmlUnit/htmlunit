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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests that when DOM events such as "onclick" have access
 * to an {@link Event} object with context information.
 * 
 * @version  $Revision$
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 */
public class EventTest extends WebTestCase {

    /**
     *  Create an instance
     *
     * @param  name Name of the test
     */
    public EventTest( final String name ) {
        super( name );
    }

    /**
     *  Verify the "this" object refers to the Element being clicked when an
     * event handler is invoked.
     * @throws Exception if the test fails
     */
    public void testThisDefined() throws Exception {
        final List expectedAlerts = Collections.singletonList("clickId");

        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.getAttribute('id')); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>\n";
        onClickPageTest(content, expectedAlerts);
    }

    /**
     *  Verify setting a previously undefined/non-existant property on an Element
     * is accessible from inside an event handler
     * @throws Exception if the test fails
     */
    public void testSetPropOnThisDefined() throws Exception {
        final List expectedAlerts = Collections.singletonList("foo");

        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.madeUpProperty); }\n"
            + "document.getElementById('clickId').onclick = handler;\n"
            + "document.getElementById('clickId').madeUpProperty = 'foo';\n"
            + "</script>\n"
            + "</body></html>\n";
        onClickPageTest(content, expectedAlerts);
    }

    /**
     * Verify that javascript snippets have a variable named 'event' available to them.
     * @throws Exception if the test fails
     */
    public void testEventArgDefinedByWrapper() throws Exception {
        final List expectedAlerts = Collections.singletonList("defined");

        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId' onclick=\"alert(event ? 'defined' : 'undefined')\"/>\n"
            + "</body></html>\n";
        onClickPageTest(BrowserVersion.MOZILLA_1_0, content, expectedAlerts);
        onClickPageTest(BrowserVersion.INTERNET_EXPLORER_6_0, content, expectedAlerts);
    }

    /**
     *  Verify that when event handler is invoked an argument is passed in. 
     * @throws Exception if the test fails
     */
    public void testEventArgDefined() throws Exception {
        final List expectedAlerts = Collections.singletonList("defined");
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(event ? 'defined' : 'undefined'); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>\n";
        onClickPageTest(BrowserVersion.MOZILLA_1_0, content, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testEventTargetSameAsThis() throws Exception {
        final List expectedAlerts = Collections.singletonList("pass");
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.target == this ? 'pass' : event.target + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>\n";
        onClickPageTest(BrowserVersion.NETSCAPE_6_2_3, content, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testEventSrcElementSameAsThis() throws Exception {
        final List expectedAlerts = Collections.singletonList("pass");
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "event = event ? event : window.event;\n"
            + "alert(event.srcElement == this ? 'pass' : event.srcElement + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>\n";
        onClickPageTest(BrowserVersion.INTERNET_EXPLORER_6_0, content, expectedAlerts);
    }

    /**
     *  event.currentTarget == this inside javascript event handler 
     * @throws Exception if the test fails
     */
    public void testEventCurrentTargetSameAsThis() throws Exception {
        final List expectedAlerts = Collections.singletonList("pass");
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.currentTarget == this ? 'pass' : event.currentTarget + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>\n";
        onClickPageTest(BrowserVersion.NETSCAPE_6_2_3, content, expectedAlerts);
    }

    /**
     * Tests that event fires on key press.
     * @throws Exception if the test fails
     */
    public void testEventOnKeyDown () throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<button type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(_e) {\n"
            + "  var e = _e ? _e : window.event;"
            + "if (e.keyCode == 65) {\n"
            + "    alert('pass');"
            + "} else {"
            + "    alert('fail:' + e.keyCode);"
            + "}"
            + "}\n"
            + "document.getElementById('clickId').onkeydown = handler;"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>\n";

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(content, collectedAlerts );
        final ClickableElement element = (ClickableElement) page.getHtmlElementById( "clickId" );
        element.keyDown(65); // A
        element.keyDown(66); // B
        element.click();
        final String[] expectedAlerts = {"pass", "fail:66", "fail:undefined"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    private void onClickPageTest(final String content, final List expectedAlerts) throws Exception, IOException {
        onClickPageTest( BrowserVersion.getDefault(), content, expectedAlerts );
    }

    private void onClickPageTest( final BrowserVersion version, final String content, final List expectedAlerts )
        throws Exception, IOException {

        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage( version, content, collectedAlerts );
        final ClickableElement clickable = (ClickableElement) page.getHtmlElementById( "clickId" );
        clickable.click();
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test that this refers to the element on which the event applies
     * @throws Exception if the test fails
     */
    public void testEventScope() throws Exception {
        final List expectedAlerts = Collections.singletonList("frame1");
        final String content
            = "<html><head></head>"
            + "<body>"
            + "<button name='button1' id='button1' onclick='alert(this.name)'>1</button>"
            + "<iframe src='about:blank' name='frame1' id='frame1'></iframe>"
            + "<script>"
            + "document.getElementById('frame1').onload = document.getElementById('button1').onclick;"
            + "</script>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }


    /**
     * Test event transmission to event handler
     * @throws Exception if the test fails
     */
    public void testEventTransmission() throws Exception {
        final String content =
            "<html><body><span id='clickMe'>foo</span>\n"
            + "<script>\n"
            + "function handler(e) {\n"
            + "  alert(e == null);\n"
            + "  alert(window.event == null);\n"
            + "  var theEvent = (e != null) ? e : window.event;\n"
            + "  var target = theEvent.target ? theEvent.target : theEvent.srcElement;\n"
            + "  alert(target.tagName);\n"
            + "}"
            + "document.getElementById('clickMe').onclick = handler;"
            + "</script></body></html>";

        final List collectedAlerts = new ArrayList();
        HtmlPage page = loadPage(BrowserVersion.MOZILLA_1_0, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("clickMe")).click();

        String[] expectedAlerts = { "false", "true", "SPAN" };
        assertEquals( expectedAlerts, collectedAlerts );

        collectedAlerts.clear();
        page = loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
        ((ClickableElement) page.getHtmlElementById("clickMe")).click();

        expectedAlerts = new String[] { "true", "false", "SPAN" };
        assertEquals( expectedAlerts, collectedAlerts );

    }



    /**
     * Test that the event property of the window is available
     * @throws Exception if the test fails
     */
    public void testIEWindowEvent() throws Exception {
        final String content =
            "<html><head>"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.event == null);\n"
            + "  alert(event == null);\n"
            + "}"
            + "</script>"
            + "</head><body onload='test()'></body></html>";

        final List collectedAlerts = new ArrayList();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);

        final String[] expectedAlerts = { "false", "false"};
        assertEquals( expectedAlerts, collectedAlerts );
    }

    /**
     * Test that the event handler is correctly parsed even if it contains comments.
     * It seems that it is correctly parsed and stored in non public field 
     * org.apache.xerces.util.XMLAttributesImpl#nonNormalizedValue
     * but that getValue(i) returns a normalized value. Furthermore access seems not possible as
     * we just see an org.apache.xerces.parsers.AbstractSAXParser.AttributesProxy
     * @throws Exception if the test fails
     */
    public void testCommentInEventHandlerDeclaration() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String content
            = "<html><head></head>"
            + "<body onload='alert(1);\n"
            + "// a comment within the onload declaration\n"
            + "alert(2)'>"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = { "1", "2" };
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
