/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for the various TypedArray's.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NativeTypedArrayTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Float32Array")
    public void toStringFloat32Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Float32Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Float64Array")
    public void toStringFloat64Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Float64Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Int8Array")
    public void toStringInt8Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Int8Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Int16Array")
    public void toStringInt16Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Int16Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Int32Array")
    public void toStringInt32Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Int32Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Uint8Array")
    public void toStringUint8Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Uint8Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Uint16Array")
    public void toStringUint16Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Uint16Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Uint32Array")
    public void toStringUint32Array() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Uint32Array(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Uint8ClampedArray")
    public void toStringUint8ClampedArray() throws Exception {
        final String html
            = "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((new Uint8ClampedArray(1))[Symbol.toStringTag]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
