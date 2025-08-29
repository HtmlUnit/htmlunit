/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for NativeArrayBuffer.
 *
 * @author Ronald Brill
 */
public class NativeArrayBufferTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "8", "2", "4",
             "4", "2", "undefined",
             "8", "2", "0"})
    public void transfer() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            // Create an ArrayBuffer and write a few bytes\
            + "let buffer = new ArrayBuffer(8);\n"
            + "let view = new Uint8Array(buffer);\n"
            + "view[1] = 2;\n"
            + "view[7] = 4;\n"

            // Copy the buffer to the same size
            + "let buffer2 = buffer.transfer();\n"
            + "log(buffer.detached);\n"
            + "log(buffer2.byteLength);\n"
            + "let view2 = new Uint8Array(buffer2);\n"
            + "log(view2[1]);\n"
            + "log(view2[7]);\n"

            // Copy the buffer to a smaller size
            + "let buffer3 = buffer2.transfer(4);\n"
            + "log(buffer3.byteLength);\n"
            + "let view3 = new Uint8Array(buffer3);\n"
            + "log(view3[1]);\n"
            + "log(view3[7]);\n"

            // Copy the buffer to a larger size
            + "let buffer4 = buffer3.transfer(8);\n"
            + "log(buffer4.byteLength);\n"
            + "let view4 = new Uint8Array(buffer4);\n"
            + "log(view4[1]);\n"
            + "log(view4[7]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"8", "2", "4"})
    public void transferToFixedLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "let buffer = new ArrayBuffer(8, { maxByteLength: 16 });\n"
            + "let view = new Uint8Array(buffer);\n"
            + "view[1] = 2;\n"
            + "view[7] = 4;\n"

            + "let buffer2 = buffer.transferToFixedLength();\n"
            + "log(buffer2.byteLength);\n"

            + "let view2 = new Uint8Array(buffer2);\n"
            + "log(view2[1]);\n"
            + "log(view2[7]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
