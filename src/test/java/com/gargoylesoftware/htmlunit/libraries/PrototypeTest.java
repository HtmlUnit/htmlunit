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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with the <a href="http://prototype.conio.net/">Prototype JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class PrototypeTest extends WebTestCase {

    /**
     * @param name The name of the test.
     */
    public PrototypeTest( final String name ) {
        super( name );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testAjax() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        test( "ajax.html", 3, 11 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testArray() throws Exception {
        test( "array.html", 12, 49 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testBase() throws Exception {
        test( "base.html", 4, 48 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testDom() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        test( "dom.html", 25, 254 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testElementMixins() throws Exception {
        test( "element_mixins.html", 4, 7 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testEnumerable() throws Exception {
        test( "enumerable.html", 23, 67 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testForm() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        test( "form.html", 4, 21 );
    }

    /**
     * Blocked by Rhino bug 370279
     * https://bugzilla.mozilla.org/show_bug.cgi?id=370279
     * @throws Exception If test fails.
     */
    public void testHash() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        test( "hash.html", 5, 19 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testPosition() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        test( "position.html", 5, 28 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testRange() throws Exception {
        test( "range.html", 6, 21 );
    }

    /**
     * @throws Exception If test fails.
     */
    public void testSelector() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        test( "selector.html", 18, 46 );
    }

    /**
     * Blocked by Rhino bug 369860
     * https://bugzilla.mozilla.org/show_bug.cgi?id=369860
     * @throws Exception If test fails.
     */
    public void testString() throws Exception {
        if( notYetImplemented() ) {
            return;
        }
        test( "string.html", 19, 76 );
    }

    private void test( final String filename, final int tests, final int assertions ) throws Exception {

        final WebClient client = new WebClient( BrowserVersion.INTERNET_EXPLORER_6_0 );
        final URL url = getClass().getClassLoader().getResource( "prototype/1.5.0-rc1/test/unit/" + filename );
        assertNotNull(url);

        final HtmlPage page = (HtmlPage) client.getPage( url );
        page.getEnclosingWindow().getThreadManager().joinAll( 10000 );

        final String summary = page.getHtmlElementById( "logsummary" ).asText();
        final String expected = tests + " tests, " + assertions + " assertions, 0 failures, 0 errors";
        assertEquals( expected, summary );
    }

}
