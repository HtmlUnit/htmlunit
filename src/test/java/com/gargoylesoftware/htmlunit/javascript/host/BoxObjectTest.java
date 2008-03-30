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
 * Unit tests for {@link BoxObject}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class BoxObjectTest extends WebTestCase {

    /**
     * Tests box object attributes relating to HTML elements: firstChild, lastChild, previousSibling, etc.
     * @throws Exception if an error occurs
     */
    @Test
    public void testElementAttributes() throws Exception {
        final String html =
              "    <html>\n"
            + "        <body onload='test()'>\n"
            + "            <span id='foo'>foo</span><div id='d'><span id='a'>a</span><span id='b'>b</span></div><span id='bar'>bar</span>\n"
            + "            <script>\n"
            + "                function test() {\n"
            + "                    \n"
            + "                    var div = document.getElementById('d');\n"
            + "                    var spanFoo = document.getElementById('foo');\n"
            + "                    var spanA = document.getElementById('a');\n"
            + "                    var spanB = document.getElementById('b');\n"
            + "                    var spanBar = document.getElementById('bar');\n"
            + "                    \n"
            + "                    var box = document.getBoxObjectFor(div);\n"
            + "                    alert(box.element == div);\n"
            + "                    alert(box.firstChild == spanA);\n"
            + "                    alert(box.lastChild == spanB);\n"
            + "                    alert(box.previousSibling == spanFoo);\n"
            + "                    alert(box.nextSibling == spanBar);\n"
            + "                }\n"
            + "            </script>\n"
            + "        </body>\n"
            + "    </html>";
        final String[] expected = {"true", "true", "true", "true", "true"};
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        assertEquals(expected, actual);
    }

    /**
     * Tests box object attributes relating to position and size: x, y, screenX, screenY, etc.
     * @throws Exception if an error occurs
     */
    @Test
    public void testPositionAndSizeAttributes() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        final String html =
              "    <html>\n"
            + "        <body onload='test()'>\n"
            + "            <style>#d { position:absolute; left:50px; top:100px; width:500px; height:400px; border:3px; padding: 5px; margin: 23px; }</style>\n"
            + "            <div id='d'>daniel</div>\n"
            + "            <script>\n"
            + "                function test() {\n"
            + "                    var div = document.getElementById('d');\n"
            + "                    var box = document.getBoxObjectFor(div);\n"
            + "                    alert(box.x + '-' + box.y);\n"
            + "                    alert(box.screenX + '-' + box.screenY);\n"
            + "                    alert(box.width + '-' + box.height);\n"
            + "                }\n"
            + "            </script>\n"
            + "        </body>\n"
            + "    </html>";
        final String[] expected = {"73-123", "73-244", "510-410"};
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        assertEquals(expected, actual);
    }

}
