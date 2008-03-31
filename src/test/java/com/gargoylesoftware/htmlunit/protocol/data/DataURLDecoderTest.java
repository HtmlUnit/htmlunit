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
package com.gargoylesoftware.htmlunit.protocol.data;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

import static com.gargoylesoftware.htmlunit.protocol.data.DataUrlDecoder.decodeDataURL;

/**
 * Tests for {@link DataUrlDecoder}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class DataURLDecoderTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.NONE)
    public void testDecodeDataURL() throws Exception {

        DataUrlDecoder decoder = decodeDataURL("data:text/javascript,d1%20%3D%20'one'%3B");
        assertEquals("d1 = 'one';", decoder.getDataAsString());
        assertEquals("text/javascript", decoder.getMediaType());
        assertEquals("US-ASCII", decoder.getCharset());

        decoder = decodeDataURL("data:text/javascript;base64,ZDIgPSAndHdvJzs%3D");
        assertEquals("d2 = 'two';", decoder.getDataAsString());

        decoder = decodeDataURL(
            "data:text/javascript;base64,%5a%44%4d%67%50%53%41%6e%64%47%68%79%5a%57%55%6e%4f%77%3D%3D");
        assertEquals("d3 = 'three';", decoder.getDataAsString());

        decoder = decodeDataURL("data:text/javascript;base64,%20ZD%20Qg%0D%0APS%20An%20Zm91cic%0D%0A%207%20");
        assertEquals("d4 = 'four';", decoder.getDataAsString());

        decoder = decodeDataURL("data:text/javascript,d5%20%3D%20'five%5Cu0027s'%3B");
        assertEquals("d5 = 'five\\u0027s';", decoder.getDataAsString());
    }

    /**
     * @throws Exception If something goes wrong.
     */
    @Test
    @Alerts(FF = { "one", "two", "three", "four", "five's" },
            IE = { "undefined", "undefined", "undefined", "undefined", "undefined" })
    public void testDataProtocol() throws Exception {
        final String html = "<html><head><title>foo</title>"
            + "<script>"
            + "var d1, d2, d3, d4, d5;\n"
            + "</script>\n"
            + "<script src=\"data:text/javascript,d1%20%3D%20'one'%3B\"></script>\n"
            + "<script src=\"data:text/javascript;base64,ZDIgPSAndHdvJzs%3D\"></script>\n"
            + "<script src=\""
            + "data:text/javascript;base64,%5a%44%4d%67%50%53%41%6e%64%47%68%79%5a%57%55%6e%4f%77%3D%3D\"></script>\n"
            + "<script src=\"data:text/javascript;base64,%20ZD%20Qg%0D%0APS%20An%20Zm91cic%0D%0A%207%20\"></script>\n"
            + "<script src=\"data:text/javascript,d5%20%3D%20'five%5Cu0027s'%3B\"></script>\n"
            + "<script>\n"
            + "function test() {\n"
            + "alert(d1)\n"
            + "alert(d2)\n"
            + "alert(d3)\n"
            + "alert(d4)\n"
            + "alert(d5)\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageWithAlerts(html);
    }
}
