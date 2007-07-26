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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DomText}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 */
public class DomTextTest extends WebTestCase {

    /**
     * Create an instance
     * @param name Name of the test
     */
    public DomTextTest(final String name) {
        super(name);
    }

    /**
     * Test the clean up of &amp;nbsp; in strings
     * @throws Exception if the test fails
     */
    public void testAsText_nbsp() throws Exception {
        testPlainText("a b&nbsp;c  d &nbsp;e",  "a b c d  e");
        testPlainText("a b&nbsp;c  d &nbsp; e", "a b c d   e");
        
        testPlainText("&nbsp;a&nbsp;", " a ");
        testPlainText("&nbsp; a&nbsp;", "  a ");
        testPlainText("&nbsp;a &nbsp;", " a  ");
    }
    
    /**
     * Test font formats, as per bug #1731042
     *  (http://sourceforge.net/tracker/index.php?func=detail&aid=1731042&group_id=47038&atid=448266)
     *
     * @throws Exception if the test fails
     */
    public void testAsText_fontFormat() throws Exception {
        // specific case reported by rgitzel
        testAsText("a <b>b</b> c",  "a b c");
        testAsText("a <b>b</b>c",   "a bc");
        testAsText("a<b>b</b> c",   "ab c");
        testAsText("a<b>b</b>c",    "abc");

        // italics and teletype should work the same way
        testAsText("a <i>b</i> c",  "a b c");
        testAsText("a <i>b</i>c",   "a bc");
        testAsText("a<i>b</i> c",   "ab c");
        testAsText("a<i>b</i>c",    "abc");

        testAsText("a <tt>b</tt> c",  "a b c");
        testAsText("a <tt>b</tt>c",   "a bc");
        testAsText("a<tt>b</tt> c",   "ab c");
        testAsText("a<tt>b</tt>c",    "abc");

        // suggested by asashour ;-)
        testAsText("a <font>b</font> c",  "a b c");
        testAsText("a<font>b</font> c",   "ab c");
        testAsText("a <font>b</font>c",   "a bc");
        testAsText("a<font>b</font>c",    "abc");

        // I guess 'span' should be just like 'font'
        testAsText("a <span>b</span> c",  "a b c");
        testAsText("a<span>b</span> c",   "ab c");
        testAsText("a <span>b</span>c",   "a bc");
        testAsText("a<span>b</span>c",    "abc");
        
        // try some combinations
        testAsText("a<b><font><i>b</i></font></b>c",    "abc");
        testAsText("a<b><font> <i>b</i></font></b>c",    "a bc");
    }
    
    /**
     * These worked before the changes for bug #1731042, and should afterwards, too
     *
     * @throws Exception if the test fails
     */
    public void testAsText_regression() throws Exception {
        testAsText("a<ul><li>b</ul>c",                     "a b c");
        testAsText("a<p>b<br>c",                           "a b c");
        testAsText("a<table><tr><td>b</td></tr></table>c", "a b c");
        testAsText("a<div>b</div>c",                       "a b c");

        testAsText("a<table><tr><td> b </td></tr>\n<tr><td> b </td></tr></table>c", "a b b c");
    }
    
    /**
     * check the HtmlTable* objects themselves
     *
     * @throws Exception if the test fails
     */
    public void testAsText_table_elements() throws Exception {
        final String html = "<table id='table'><tr id='row'><td id='cell'> b </td></tr>\n</table>";
        final String content = "<html><body><span id='foo'>" + html + "</span></body></html>";

        final HtmlPage page = loadPage(content);
        
        assertEquals("b", page.getHtmlElementById("cell").asText());
        assertEquals("b", page.getHtmlElementById("row").asText());
        assertEquals("b", page.getHtmlElementById("table").asText());
    }
    
    // ====================================================================================
        
    private void testPlainText(final String html, final String expectedText) throws Exception {
        final String content = "<html><body><span id='foo'>" + html + "</span></body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlElement elt = page.getHtmlElementById("foo");
        assertEquals(expectedText, elt.asText());

        final DomNode node = elt.getFirstChild();
        assertEquals(expectedText, node.asText());
    }

    private void testAsText(final String html, final String expectedText) throws Exception {
        final String content = "<html><body><span id='foo'>" + html + "</span></body></html>";

        final HtmlPage page = loadPage(content);
        final HtmlElement elt = page.getHtmlElementById("foo");
        assertEquals(expectedText, elt.asText());
    }
}
