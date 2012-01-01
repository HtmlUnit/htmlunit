/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.protocol.data;

import static com.gargoylesoftware.htmlunit.protocol.data.DataUrlDecoder.decodeDataURL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

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
     * @throws Exception if something goes wrong
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
