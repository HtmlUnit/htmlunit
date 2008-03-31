/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link HTMLBodyElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class HTMLBodyElementTest extends WebTestCase {

    /**
     * Tests the default body padding and margins.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDefaultPaddingAndMargins() throws Exception {
        testDefaultPaddingAndMargins(BrowserVersion.FIREFOX_2, ",0px,0px,0px,0px", ",,,,", ",8px,8px,8px,8px", ",,,,");
        testDefaultPaddingAndMargins(BrowserVersion.INTERNET_EXPLORER_7_0, "0px,0px,0px,0px,0px", ",,,,", "15px 10px,10px,10px,15px,15px", ",,,,");
        testDefaultPaddingAndMargins(BrowserVersion.INTERNET_EXPLORER_6_0, "0px,0px,0px,0px,0px", ",,,,", "15px 10px,10px,10px,15px,15px", ",,,,");
    }

    private void testDefaultPaddingAndMargins(final BrowserVersion version, final String... expected) throws Exception {
        final String html =
            "    <html>\n"
            + "        <head>\n"
            + "            <script>\n"
            + "                function test() {\n"
            + "                    var b = document.getElementById('body');\n"
            + "                    var s = b.currentStyle ? b.currentStyle : getComputedStyle(b, null);\n"
            + "                    alert(s.padding + ',' + s.paddingLeft + ',' + s.paddingRight + ',' + s.paddingTop + ',' + s.paddingBottom);\n"
            + "                    alert(b.style.padding + ',' + b.style.paddingLeft + ',' + b.style.paddingRight + ',' + b.style.paddingTop + ',' + b.style.paddingBottom);\n"
            + "                    alert(s.margin + ',' + s.marginLeft + ',' + s.marginRight + ',' + s.marginTop + ',' + s.marginBottom);\n"
            + "                    alert(b.style.margin + ',' + b.style.marginLeft + ',' + b.style.marginRight + ',' + b.style.marginTop + ',' + b.style.marginBottom);\n"
            + "                }\n"
            + "            </script>\n"
            + "        </head>\n"
            + "        <body id='body' onload='test()'>blah</body>\n"
            + "    </html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(version, html, actual);
        assertEquals(expected, actual);
    }

}
