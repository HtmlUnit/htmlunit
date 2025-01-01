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
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
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
    @Alerts({"Float32Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Float32Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Float32Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Float32Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Float32Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringFloat32Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Float32Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Float64Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Float64Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Float64Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Float64Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Float64Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringFloat64Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Float64Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Int8Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Int8Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Int8Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Int8Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Int8Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringInt8Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Int8Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Int16Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Int16Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Int16Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Int16Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Int16Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringInt16Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Int16Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Int32Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Int32Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Int32Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Int32Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Int32Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringInt32Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Int32Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Uint8Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Uint8Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Uint8Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Uint8Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Uint8Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringUint8Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Uint8Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Uint16Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Uint16Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Uint16Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Uint16Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Uint16Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringUint16Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Uint16Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Uint32Array", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Uint32Array", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Uint32Array", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Uint32Array", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Uint32Array", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringUint32Array() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Uint32Array(1)"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Uint8ClampedArray", "false", "true",
             "undefined", "false", "true",
             "undefined", "true", "true",
             "Symbol(Symbol.iterator),Symbol(Symbol.toStringTag)"})
    @HtmlUnitNYI(CHROME = {"Uint8ClampedArray", "false", "false",
                           "undefined", "false", "false",
                           "undefined", "false", "false", ""},
            EDGE = {"Uint8ClampedArray", "false", "false",
                    "undefined", "false", "false",
                    "undefined", "false", "false", ""},
            FF = {"Uint8ClampedArray", "false", "false",
                  "undefined", "false", "false",
                  "undefined", "false", "false", ""},
            FF_ESR = {"Uint8ClampedArray", "false", "false",
                      "undefined", "false", "false",
                      "undefined", "false", "false", ""})
    public void toStringUint8ClampedArray() throws Exception {
        loadPageVerifyTitle2(toStringTagTest("new Uint8ClampedArray(1)"));
    }

    private static String toStringTagTest(final String init) {
        return
            "<html></head>\n"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function sortFunction(s1, s2) {\n"
            + "    var s1lc = s1.toLowerCase();\n"
            + "    var s2lc =  s2.toLowerCase();\n"
            + "    if (s1lc > s2lc) { return 1; }\n"
            + "    if (s1lc < s2lc) { return -1; }\n"
            + "    return s1 > s2 ? 1 : -1;\n"
            + "  }\n"

            + "  var typedArray = " + init + ";\n"
            + "  log((typedArray)[Symbol.toStringTag]);\n"
            + "  log(typedArray.hasOwnProperty(Symbol.toStringTag));\n"
            + "  log(Symbol.toStringTag in typedArray);\n"

            + "  var typedArrayProto = typedArray.__proto__;"
            + "  log((typedArrayProto)[Symbol.toStringTag]);\n"
            + "  log(typedArrayProto.hasOwnProperty(Symbol.toStringTag));\n"
            + "  log(Symbol.toStringTag in typedArrayProto);\n"

            + "  var typedArrayProtoProto = typedArray.__proto__.__proto__;"
            + "  log((typedArrayProtoProto)[Symbol.toStringTag]);\n"
            + "  log(typedArrayProtoProto.hasOwnProperty(Symbol.toStringTag));\n"
            + "  log(Symbol.toStringTag in typedArrayProtoProto);\n"

            + "  var syms = Object.getOwnPropertySymbols(typedArrayProtoProto);\n"
            + "  var symStrs = [];"
            + "  for (i = 0; i < syms.length; i++) {\n"
            + "    symStrs.push(syms[i].toString());\n"
            + "  }\n"
            + "  symStrs.sort(sortFunction);\n"
            + "  log(symStrs);"
            + "</script>\n"
            + "</body></html>";
    }
}
