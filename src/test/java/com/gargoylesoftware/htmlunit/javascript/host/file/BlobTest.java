/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.file.Blob}.
 *
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@RunWith(BrowserRunner.class)
public class BlobTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", MimeType.TEXT_HTML})
    public void properties() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var blob = new Blob(['abc'], {type : 'text/html'});\n"

            + "  log(blob.size);\n"
            + "  log(blob.type);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "Hello HtmlUnit"},
            IE = {"undefined", "TypeError true"})
    public void text() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var blob = new Blob(['Hello HtmlUnit'], {type : 'text/html'});\n"

                + "  log(typeof blob.text);\n"
                + "  try {\n"
                + "    blob.text().then(function(text) { log(text); });\n"
                + "  } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text/plain")
    public void typeTxt() throws Exception {
        type(MimeType.TEXT_PLAIN);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("htmlunit")
    public void typeHtmlUnit() throws Exception {
        type("htmlunit");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void typeEmpty() throws Exception {
        type("");
    }

    private void type(final String type) throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var blob = new Blob(['Hello HtmlUnit'], {type : '" + type + "'});\n"
            + "  log(blob.type);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void typeDefault() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var blob = new Blob(['Hello HtmlUnit']);\n"
            + "  log(blob.type);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "", ""},
            IE = {"0", "", "TypeError true"})
    public void ctorNoArgs() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var blob = new Blob();\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"
                + "    try {\n"
                + "      blob.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "", ""},
            IE = {"0", "", "TypeError true"})
    public void ctorEmpty() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var blob = new Blob([]);\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"
                + "    try {\n"
                + "      blob.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "", "HtmlUnit"},
            IE = {"8", "", "TypeError true"})
    public void ctorString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var blob = new Blob(['HtmlUnit']);\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"
                + "    try {\n"
                + "      blob.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"8", "application/octet-stream", "HtmlUnit"},
            IE = {"8", "application/octet-stream", "TypeError true"})
    public void ctorStringWithOptions() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var blob = new Blob(['Html', 'Unit'], {type: 'application/octet-stream'});\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"
                + "    try {\n"
                + "      blob.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"16", "", "HtmlUnitis great"},
            IE = {"16", "", "TypeError true"})
    public void ctorStrings() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var blob = new Blob(['Html', 'Unit', 'is great']);\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"
                + "    try {\n"
                + "      blob.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "HtmlUnitMMMK"},
            IE = {"12", "", "TypeError true"})
    public void ctorMixed() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nab = new ArrayBuffer(2);\n"
                + "    var nabv = new Uint8Array(nab, 0, 2);\n"
                + "    nabv.set([77, 77], 0);\n"
                + "    var blob = new Blob(['HtmlUnit',"
                                        + "nab, new Int8Array([77,75])]);\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"
                + "    try {\n"
                + "      blob.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"34", "", "HtmlUnitHtmlUnitMMMKMKHtmlUnitMMMK"},
            IE = {"34", "", "TypeError true"})
    public void ctorMixedBlobs() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nab = new ArrayBuffer(2);\n"
                + "    var nabv = new Uint8Array(nab, 0, 2);\n"
                + "    nabv.set([77, 77], 0);\n"
                + "    var blob = new Blob(['HtmlUnit',"
                                        + "nab, new Int8Array([77,75])]);\n"
                + "    blob = new Blob(['HtmlUnit',"
                                    + "blob, new Int8Array([77,75]), blob]);\n"
                + "    log(blob.size);\n"
                + "    log(blob.type);\n"
                + "    try {\n"
                + "      blob.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "Hello HtmlUnit"},
            IE = {"undefined", "TypeError true"})
    public void arrayBuffer() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var blob = new Blob(['Hello HtmlUnit'], {type : 'text/html'});\n"
                + "  log(typeof blob.arrayBuffer);\n"
                + "  try {\n"
                + "    blob.arrayBuffer().then(function(buf) {\n"
                + "      var arr = new Uint8Array(buf);\n"
                + "      log(String.fromCharCode.apply(String, arr));\n"
                + "    })\n"
                + "  } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "function", "3", "", "tml"},
            IE = {"12", "", "function", "3", "", "TypeError true"})
    public void slice() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nab = new ArrayBuffer(2);\n"
                + "    var nabv = new Uint8Array(nab, 0, 2);\n"
                + "    nabv.set([77, 77], 0);\n"
                + "    var blob = new Blob(['HtmlUnit',"
                                        + "nab, new Int8Array([77,75])]);\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"

                + "    log(typeof blob.slice);\n"

                + "    var sliced = blob.slice(1,4);\n"
                + "    log(sliced.size);\n"
                + "    log(sliced.type);\n"

                + "    try {\n"
                + "      sliced.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "12", "", "HtmlUnitMMMK"},
            IE = {"12", "", "12", "", "TypeError true"})
    public void sliceWhole() throws Exception {
        slice("blob.slice();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "9", "", "lUnitMMMK"},
            IE = {"12", "", "9", "", "TypeError true"})
    public void sliceStartOnly() throws Exception {
        slice("blob.slice(3);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "7", "", "nitMMMK"},
            IE = {"12", "", "7", "", "TypeError true"})
    public void sliceStartOnlyNegative() throws Exception {
        slice("blob.slice(-7);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "12", "", "HtmlUnitMMMK"},
            IE = {"12", "", "12", "", "TypeError true"})
    public void sliceStartOnlyNegativeOutside() throws Exception {
        slice("blob.slice(-123);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "3", "", "mlU"},
            IE = {"12", "", "3", "", "TypeError true"})
    public void sliceEndNegative() throws Exception {
        slice("blob.slice(2, -7);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "10", "", "mlUnitMMMK"},
            IE = {"12", "", "10", "", "TypeError true"})
    public void sliceEndOutside() throws Exception {
        slice("blob.slice(2, 1234);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "0", "", ""},
            IE = {"12", "", "0", "", "TypeError true"})
    public void sliceBothOutside() throws Exception {
        slice("blob.slice(123, 1234);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "0", "", ""},
            IE = {"12", "", "0", "", "TypeError true"})
    public void sliceNoIntersection() throws Exception {
        slice("blob.slice(5, 4);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "1", "", "U"},
            IE = {"12", "", "1", "", "TypeError true"})
    public void sliceEmptyIntersection() throws Exception {
        slice("blob.slice(4, 5);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "12", "", "HtmlUnitMMMK"},
            IE = {"12", "", "12", "", "TypeError true"})
    public void sliceWrongStart() throws Exception {
        slice("blob.slice('four');");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "0", "", ""},
            IE = {"12", "", "0", "", "TypeError true"})
    public void sliceWrongEnd() throws Exception {
        slice("blob.slice(1, 'four');");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "1", "type", "t"},
            IE = {"12", "", "1", "tyPE", "TypeError true"})
    @HtmlUnitNYI(IE = {"12", "", "1", "type", "TypeError true"})
    public void sliceContentType() throws Exception {
        slice("blob.slice(1, 2, 'tyPE');");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"12", "", "1", "7", "t"},
            IE = {"12", "", "1", "7", "TypeError true"})
    public void sliceContentTypeNotString() throws Exception {
        slice("blob.slice(1, 2, 7);");
    }

    private void slice(final String slice) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var nab = new ArrayBuffer(2);\n"
                + "    var nabv = new Uint8Array(nab, 0, 2);\n"
                + "    nabv.set([77, 77], 0);\n"
                + "    var blob = new Blob(['HtmlUnit',"
                                        + "nab, new Int8Array([77,75])]);\n"

                + "    log(blob.size);\n"
                + "    log(blob.type);\n"

                + "    var sliced = " + slice + "\n"
                + "    log(sliced.size);\n"
                + "    log(sliced.type);\n"

                + "    try {\n"
                + "      sliced.text().then(function(text) { log(text); });\n"
                + "    } catch(e) { log('TypeError ' + (e instanceof TypeError)); }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body>\n"
                + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
