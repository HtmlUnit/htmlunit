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
package com.gargoylesoftware.htmlunit.libraries;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;

/**
 * Tests for compatibility with the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision: 1 $
 * @author Daniel Gredler
 */
public class JQueryTest extends WebTestCase {

    /**
     * Creates an instance.
     *
     * @param name The name of the test.
     */
    public JQueryTest(final String name) {
        super(name);
    }

    /**
     * Runs the jQuery unit tests while simulating IE6. Note that the number of failing unit tests,
     * as well as the total number of unit tests run, vary according to the browser being used to
     * run the tests.
     *
     * @throws Exception If an error occurs.
     */
    public void testJQueryWithIE6() throws Exception {

        if (notYetImplemented()) {
            return;
        }

        final URL url = getClass().getClassLoader().getResource("jquery/1.1.2/test/index.html");
        assertNotNull(url);

        final WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_6_0);
        final HtmlPage page = (HtmlPage) client.getPage(url);
        page.getEnclosingWindow().getThreadManager().joinAll(60000);

        final HtmlElement doc = page.getDocumentElement();
        final HtmlParagraph p = (HtmlParagraph) doc.getHtmlElementsByAttribute("p", "class", "result").get(0);
        final String status1 = p.getFirstChild().asText();
        final String status2 = p.getFirstChild().getNextSibling().getNextSibling().asText();
        getLog().info("Ran the jQuery unit tests. " + status1 + " " + status2);

        final List failedGroups = doc.getHtmlElementsByAttribute("li", "class", "fail");
        for (final Iterator i = failedGroups.iterator(); i.hasNext();) {
            final DomNode group = (DomNode) i.next();
            getLog().info("jQuery failure: " + group.asText());
        }

        assertTrue(status1.startsWith("Tests completed"));
        assertEquals("9 tests of 350 failed.", status2);
    }

}
