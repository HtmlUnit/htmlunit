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
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.NameValuePair;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.DefaultPageCreator}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DefaultPageCreatorTest extends WebTestCase {


    /**
     * Create an instance
     * @param name The name of the test
     */
    public DefaultPageCreatorTest( final String name ) {
        super(name);
    }

    /**
     * Test for {@link DefaultPageCreator#determinePageType(String)}
     */
    public void testDeterminePageType() {
        final DefaultPageCreator creator = new DefaultPageCreator();

        assertEquals("html", creator.determinePageType("application/vnd.wap.xhtml+xml"));
        assertEquals("html", creator.determinePageType("application/xhtml+xml"));
        assertEquals("html", creator.determinePageType("text/html"));
        assertEquals("html", creator.determinePageType("text/xhtml"));

        assertEquals("javascript", creator.determinePageType("text/javascript"));
        assertEquals("javascript", creator.determinePageType("application/x-javascript"));

        assertEquals("xml", creator.determinePageType("text/xml"));
        assertEquals("xml", creator.determinePageType("application/xml"));
        assertEquals("xml", creator.determinePageType("text/vnd.wap.wml"));
        assertEquals("xml", creator.determinePageType("application/vnd.mozilla.xul+xml"));
        assertEquals("xml", creator.determinePageType("application/rdf+xml"));
        assertEquals("xml", creator.determinePageType("image/svg+xml"));

        assertEquals("text", creator.determinePageType("text/plain"));
        assertEquals("text", creator.determinePageType("text/csv"));
        assertEquals("text", creator.determinePageType("text/css"));

        assertEquals("unknown", creator.determinePageType("application/pdf"));
        assertEquals("unknown", creator.determinePageType("application/x-shockwave-flash"));
    }
    
    /**
     * Test Attachment Page
     * @throws Exception If the test fails.
     */
    public void testAttachmentPage() throws Exception {
        final String content = "<html>But is it really?</html>";
        final WebClient client = new WebClient();
        final MockWebConnection conn = new MockWebConnection(client);
        client.setWebConnection(conn);

        final List headers = new ArrayList();
        headers.add(new NameValuePair("Content-Disposition","attachment"));
        conn.setResponse(URL_FIRST,content,200,"OK","text/html",headers);
        final Page result = client.getPage(URL_FIRST);
        assertInstanceOf(result,AttachmentPage.class);
        HttpWebConnectionTest.assertEquals(
            ((AttachmentPage)result).getInputStream(),
            new ByteArrayInputStream(content.getBytes())
        );
    }
    
    /**
     * Test Attachment Page Callback. This demonstrates the case where the 
     * handler is the only way to obtain a copy of the attachment.
     * @throws Exception If the test fails.
     */
    public void testAttachmentHandler() throws Exception {
        final String content1 = "<html><body>\n"
            + "<form method='POST' name='form' action='" + URL_SECOND + "'>\n"
            + "<input type='submit' value='ok'>\n"
            + "</form>\n"
            + "<a href='#' onclick='document.form.submit()'>click me</a>\n"
            + "</body></html>";
        final String content2="download file contents";
        
        final WebClient client = new WebClient();
        final List attachments = new ArrayList();
        client.setAttachmentHandler(new AttachmentHandler() {
            public void handleAttachment(final AttachmentPage page) {
                attachments.add(page);
            }
        });
        final MockWebConnection conn = new MockWebConnection(client);
        conn.setResponse(URL_FIRST,content1);
        final List headers = new ArrayList();
        headers.add(new NameValuePair("Content-Disposition","attachment"));
        conn.setResponse(URL_SECOND,content2,200,"OK","text/html",headers);
        client.setWebConnection(conn);
        final HtmlPage result = (HtmlPage)client.getPage(URL_FIRST);
        final HtmlAnchor anchor = (HtmlAnchor) result.getAnchors().get(0);
        final Page clickResult = anchor.click(); // returns same page
        assertSame(result,clickResult);
        // attachment was downloaded and placed in attachments
        assertEquals(1, attachments.size());
        assertInstanceOf(attachments.get(0),AttachmentPage.class);
        final AttachmentPage attachment = (AttachmentPage) attachments.get(0);
        HttpWebConnectionTest.assertEquals(
            attachment.getInputStream(),
            new ByteArrayInputStream(content2.getBytes())
        );
    }

    /**
     * Test AttachmentPage's getFilename method.
     * @throws Exception If the test fails.
     */
    public void testAttachmentFilename() throws Exception {
        final String content = "<html>But is it really?</html>";
        final WebClient client = new WebClient();
        final MockWebConnection conn = new MockWebConnection(client);
        client.setWebConnection(conn);

        final List headers1 = new ArrayList();
        headers1.add(new NameValuePair("Content-Disposition","attachment;filename=\"hello.html\""));
        conn.setResponse(URL_FIRST,content,200,"OK","text/html",headers1);
        final AttachmentPage result = (AttachmentPage) client.getPage(URL_FIRST);
        assertEquals(result.getSuggestedFilename(),"hello.html");
        
        final List headers2 = new ArrayList();
        headers2.add(new NameValuePair("Content-Disposition","attachment;filename=hello.html;something=else"));
        conn.setResponse(URL_SECOND,content,200,"OK","text/html",headers2);
        final AttachmentPage result2 = (AttachmentPage) client.getPage(URL_SECOND);
        assertEquals(result2.getSuggestedFilename(),"hello.html");

        final List headers3 = new ArrayList();
        headers3.add(new NameValuePair("Content-Disposition","attachment"));
        conn.setResponse(URL_THIRD,content,200,"OK","text/html",headers3);
        final AttachmentPage result3 = (AttachmentPage) client.getPage(URL_THIRD);
        assertNull(result3.getSuggestedFilename());
    }
}
