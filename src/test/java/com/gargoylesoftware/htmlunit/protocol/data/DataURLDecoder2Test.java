/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.protocol.data;

import static com.gargoylesoftware.htmlunit.protocol.data.DataUrlDecoder.decodeDataURL;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.junit.Test;

/**
 * Tests for {@link DataUrlDecoder}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Carsten Steul
 */
public class DataURLDecoder2Test {

    /**
     * @throws Exception if the test fails
     */
    @Test
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

        decoder = decodeDataURL("data:application/octet-stream;base64,a+b/cQ==");
        assertArrayEquals(new byte[]{107, -26, -1, 113}, decoder.getBytes());

        decoder = decodeDataURL("data:text/html;charset=utf-8,%3C%21DOCTYPE%20html%3E%0D%0A%3Cht"
                    + "ml%20lang%3D%22en%22%3E%0D%0A%3Chead%3E%3Ctitle%3EEmbedded%20Window%3C%2F"
                    + "title%3E%3C%2Fhead%3E%0D%0A%3Cbody%3E%3Ch1%3E42%3C%2Fh1%3E%3C%2Fbody%3E%0"
                    + "A%3C%2Fhtml%3E%0A%0D%0A");
        assertEquals("<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head><title>Embedded Window</title></head>\r\n"
                    + "<body><h1>42</h1></body>\n</html>\n\r\n", decoder.getDataAsString());

        decoder = decodeDataURL("data:text/plain;charset=iso-8859-7,%b8%f7%fe");
        assertEquals("\u0388\u03c7\u03ce", decoder.getDataAsString());

        decoder = decodeDataURL("data:,A brief note");
        assertEquals("A brief note", decoder.getDataAsString());

        decoder = decodeDataURL("data:;,A brief note");
        assertEquals("A brief note", decoder.getDataAsString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void urlWithPlus() throws Exception {
        // real browsers are able to show this image
        final DataUrlDecoder decoder = decodeDataURL(
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAA"
                + "BlBMVEUAAAD///+l2Z/dAAAAM0lEQVR4nGP4/5/h/1+G/58ZDrAz3D/McH8yw83NDDeN"
                + "Ge4Ug9C9zwz3gVLMDA/A6P9/AFGGFyjOXZtQAAAAAElFTkSuQmCC");
        try (ByteArrayInputStream in = new ByteArrayInputStream(decoder.getBytes());
                ) {
            ImageIO.read(in);
        }
    }
}
