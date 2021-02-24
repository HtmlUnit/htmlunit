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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link TextDecoder}.
 *
 * @author Ronald Brill
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/TextDecoder">TextDecoder() - Web APIs | MDN</a>
 */
@RunWith(BrowserRunner.class)
public class TextDecoderTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"utf-8", "utf-8", "utf-8", "utf-8"},
            IE = "no TextDecoder")
    public void encoding() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextDecoder === 'undefined') {\n"
            + "        alert('no TextDecoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextDecoder();\n"
            + "      alert(enc.encoding);\n"

            + "      enc = new TextDecoder(undefined);\n"
            + "      alert(enc.encoding);\n"

            + "      enc = new TextDecoder('utf-8');\n"
            + "      alert(enc.encoding);\n"

            + "      enc = new TextDecoder('utf8');\n"
            + "      alert(enc.encoding);\n"

            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "utf-8",
            IE = "no TextDecoder")
    public void encoding_utf8() throws Exception {
        encoding("unicode-1-1-utf-8");
        encoding("utf-8");
        encoding("utf8");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "ibm866",
            IE = "no TextDecoder")
    public void encoding_ibm866() throws Exception {
        encoding("866");
        encoding("cp866");
        encoding("csibm866");
        encoding("ibm866");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-2",
            IE = "no TextDecoder")
    public void encoding_iso_8859_2() throws Exception {
        encoding("csisolatin2");
        encoding("iso-8859-2");
        encoding("iso-ir-101");
        encoding("iso8859-2");
        encoding("iso88592");
        encoding("iso_8859-2");
        encoding("iso_8859-2:1987");
        encoding("l2");
        encoding("latin2");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-3",
            IE = "no TextDecoder")
    public void encoding_iso_8859_3() throws Exception {
        encoding("csisolatin3");
        encoding("iso-8859-3");
        encoding("iso-ir-109");
        encoding("iso8859-3");
        encoding("iso88593");
        encoding("iso_8859-3");
        encoding("iso_8859-3:1988");
        encoding("l3");
        encoding("latin3");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-4",
            IE = "no TextDecoder")
    public void encoding_iso_8859_4() throws Exception {
        encoding("csisolatin4");
        encoding("iso-8859-4");
        encoding("iso-ir-110");
        encoding("iso8859-4");
        encoding("iso88594");
        encoding("iso_8859-4");
        encoding("iso_8859-4:1988");
        encoding("l4");
        encoding("latin4");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-5",
            IE = "no TextDecoder")
    public void encoding_iso_8859_5() throws Exception {
        encoding("csisolatincyrillic");
        encoding("cyrillic");
        encoding("iso-8859-5");
        encoding("iso-ir-144");
        encoding("iso88595");
        encoding("iso_8859-5");
        encoding("iso_8859-5:1988");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-6",
            IE = "no TextDecoder")
    public void encoding_iso_8859_6() throws Exception {
        encoding("arabic");
        encoding("asmo-708");
        encoding("csiso88596e");
        encoding("csiso88596i");
        encoding("csisolatinarabic");
        encoding("ecma-114");
        encoding("iso-8859-6");
        encoding("iso-8859-6-e");
        encoding("iso-8859-6-i");
        encoding("iso-ir-127");
        encoding("iso8859-6");
        encoding("iso88596");
        encoding("iso_8859-6");
        encoding("iso_8859-6:1987");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-7",
            IE = "no TextDecoder")
    public void encoding_iso_8859_7() throws Exception {
        encoding("csisolatingreek");
        encoding("ecma-118");
        encoding("elot_928");
        encoding("greek");
        encoding("greek8");
        encoding("iso-8859-7");
        encoding("iso-ir-126");
        encoding("iso8859-7");
        encoding("iso88597");
        encoding("iso_8859-7");
        encoding("iso_8859-7:1987");
        encoding("sun_eu_greek");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-8",
            IE = "no TextDecoder")
    public void encoding_iso_8859_8() throws Exception {
        encoding("csiso88598e");
        encoding("csisolatinhebrew");
        encoding("hebrew");
        encoding("iso-8859-8");
        encoding("iso-8859-8-e");
        encoding("iso-ir-138");
        encoding("iso8859-8");
        encoding("iso88598");
        encoding("iso_8859-8");
        encoding("iso_8859-8:1988");
        encoding("visual");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-8-i",
            IE = "no TextDecoder")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void encoding_iso_8859_8i() throws Exception {
        encoding("csiso88598i");
        encoding("iso-8859-8-i");
        encoding("logical");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-10",
            IE = "no TextDecoder")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void encoding_iso_8859_10() throws Exception {
        encoding("csisolatin6");
        encoding("iso-8859-10");
        encoding("iso-ir-157");
        encoding("iso8859-10");
        encoding("iso885910");
        encoding("l6");
        encoding("latin6");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-13",
            IE = "no TextDecoder")
    public void encoding_iso_8859_13() throws Exception {
        encoding("iso-8859-13");
        encoding("iso8859-13");
        encoding("iso885913");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-14",
            IE = "no TextDecoder")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void encoding_iso_8859_14() throws Exception {
        encoding("iso-8859-14");
        encoding("iso8859-14");
        encoding("iso885914");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-15",
            IE = "no TextDecoder")
    public void encoding_iso_8859_15() throws Exception {
        encoding("csisolatin9");
        encoding("iso-8859-15");
        encoding("iso8859-15");
        encoding("iso885915");
        encoding("l9");
        // encoding("latin9");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "no TextDecoder")
    public void encoding_iso_8859_15_ex() throws Exception {
        encoding("latin9");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-8859-16",
            IE = "no TextDecoder")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void encoding_iso_8859_16() throws Exception {
        encoding("iso-8859-16");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "koi8-r",
            IE = "no TextDecoder")
    public void encoding_koi8_r() throws Exception {
        encoding("cskoi8r");
        encoding("koi");
        encoding("koi8");
        encoding("koi8-r");
        encoding("koi8_r");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "koi8-u",
            IE = "no TextDecoder")
    public void encoding_koi8_u() throws Exception {
        encoding("koi8-u");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "macintosh",
            IE = "no TextDecoder")
    public void encoding_macintosh() throws Exception {
        encoding("csmacintosh");
        encoding("mac");
        encoding("macintosh");
        encoding("x-mac-roman");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-874",
            IE = "no TextDecoder")
    public void encoding_windows_874() throws Exception {
        encoding("dos-874");
        encoding("iso-8859-11");
        encoding("iso8859-11");
        encoding("iso885911");
        encoding("tis-620");
        encoding("windows-874");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1250",
            IE = "no TextDecoder")
    public void encoding_windows_1250() throws Exception {
        encoding("cp1250");
        encoding("windows-1250");
        encoding("x-cp1250");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1251",
            IE = "no TextDecoder")
    public void encoding_windows_1251() throws Exception {
        encoding("cp1251");
        encoding("windows-1251");
        encoding("x-cp1251");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1252",
            IE = "no TextDecoder")
    public void encoding_windows_1252() throws Exception {
        encoding("ansi_x3.4-1968");
        encoding("ascii");
        encoding("cp1252");
        encoding("cp819");
        encoding("csisolatin1");
        encoding("ibm819");
        encoding("iso-8859-1");
        encoding("iso-ir-100");
        encoding("iso8859-1");
        encoding("iso88591");
        encoding("iso_8859-1");
        encoding("iso_8859-1:1987");
        encoding("l1");
        encoding("latin1");
        encoding("us-ascii");
        encoding("windows-1252");
        encoding("x-cp1252");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1253",
            IE = "no TextDecoder")
    public void encoding_windows_1253() throws Exception {
        encoding("cp1253");
        encoding("windows-1253");
        encoding("x-cp1253");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1254",
            IE = "no TextDecoder")
    public void encoding_windows_1254() throws Exception {
        encoding("cp1254");
        encoding("csisolatin5");
        encoding("iso-8859-9");
        encoding("iso-ir-148");
        encoding("iso8859-9");
        encoding("iso88599");
        encoding("iso_8859-9");
        encoding("iso_8859-9:1989");
        encoding("l5");
        encoding("latin5");
        encoding("windows-1254");
        encoding("x-cp1254");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1255",
            IE = "no TextDecoder")
    public void encoding_windows_1255() throws Exception {
        encoding("cp1255");
        encoding("windows-1255");
        encoding("x-cp1255");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1256",
            IE = "no TextDecoder")
    public void encoding_windows_1256() throws Exception {
        encoding("cp1256");
        encoding("windows-1256");
        encoding("x-cp1256");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1257",
            IE = "no TextDecoder")
    public void encoding_windows_1257() throws Exception {
        encoding("cp1257");
        encoding("windows-1257");
        encoding("x-cp1257");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "windows-1258",
            IE = "no TextDecoder")
    public void encoding_windows_1258() throws Exception {
        encoding("cp1258");
        encoding("windows-1258");
        encoding("x-cp1258");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "x-mac-cyrillic",
            IE = "no TextDecoder")
    public void encoding_x_mac_cyrillic() throws Exception {
        encoding("x-mac-cyrillic");
        encoding("x-mac-ukrainian");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "gbk",
            IE = "no TextDecoder")
    public void encoding_gbk() throws Exception {
        encoding("chinese");
        encoding("csgb2312");
        encoding("csiso58gb231280");
        encoding("gb2312");
        encoding("gb_2312");
        encoding("gb_2312-80");
        encoding("gbk");
        encoding("iso-ir-58");
        encoding("x-gbk");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "gb18030",
            IE = "no TextDecoder")
    public void encoding_gb18030() throws Exception {
        encoding("gb18030");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "no TextDecoder")
    public void encoding_hz_gb_2312() throws Exception {
        encoding("hz-gb-2312");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "big5",
            IE = "no TextDecoder")
    public void encoding_big5() throws Exception {
        encoding("big5");
        encoding("big5-hkscs");
        encoding("cn-big5");
        encoding("csbig5");
        encoding("x-x-big5");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "euc-jp",
            IE = "no TextDecoder")
    public void encoding_euc_jp() throws Exception {
        encoding("cseucpkdfmtjapanese");
        encoding("euc-jp");
        encoding("x-euc-jp");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "iso-2022-jp",
            IE = "no TextDecoder")
    public void encoding_iso_2022_jp() throws Exception {
        encoding("csiso2022jp");
        encoding("iso-2022-jp");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "shift_jis",
            IE = "no TextDecoder")
    public void encoding_shift_jis() throws Exception {
        encoding("csshiftjis");
        encoding("ms_kanji");
        encoding("shift-jis");
        encoding("shift_jis");
        encoding("sjis");
        encoding("windows-31j");
        encoding("x-sjis");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "euc-kr",
            IE = "no TextDecoder")
    public void encoding_euc_kr() throws Exception {
        encoding("cseuckr");
        encoding("csksc56011987");
        encoding("euc-kr");
        encoding("iso-ir-149");
        encoding("korean");
        encoding("ks_c_5601-1987");
        encoding("ks_c_5601-1989");
        encoding("ksc5601");
        encoding("ksc_5601");
        encoding("windows-949");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "no TextDecoder")
    public void encoding_iso_2022_kr() throws Exception {
        encoding("csiso2022kr");
        encoding("iso-2022-kr");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "utf-16be",
            IE = "no TextDecoder")
    public void encoding_utf_16be() throws Exception {
        encoding("utf-16be");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "utf-16le",
            IE = "no TextDecoder")
    public void encoding_utf_16le() throws Exception {
        encoding("utf-16");
        encoding("utf-16le");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "x-user-defined",
            IE = "no TextDecoder")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void encoding_x_user_defined() throws Exception {
        encoding("x-user-defined");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "no TextDecoder")
    public void encoding_replacement() throws Exception {
        encoding("iso-2022-cn");
        encoding("iso-2022-cn-ext");
    }

    private void encoding(final String encoding) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextDecoder === 'undefined') {\n"
            + "        alert('no TextDecoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      try {\n"
            + "        enc = new TextDecoder('" + encoding + "');\n"
            + "        alert(enc.encoding);\n"
            + "      } catch(e) { alert('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"0", "8", "72", "116"},
            IE = "no TextEncoder")
    public void encode() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextEncoder === 'undefined') {\n"
            + "        alert('no TextEncoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      var encoded = enc.encode('');\n"
            + "      alert(encoded.length);\n"

            + "      encoded = enc.encode('HtmlUnit');\n"
            + "      alert(encoded.length);\n"
            + "      alert(encoded[0]);\n"
            + "      alert(encoded[encoded.length - 1]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "HtmlUnit",
            IE = "no TextDecoder")
    public void decode() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextDecoder === 'undefined') {\n"
            + "        alert('no TextDecoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      var encoded = enc.encode('HtmlUnit');\n"

            + "      var dec = new TextDecoder('utf-8');\n"
            + "      var decoded = dec.decode(encoded);\n"
            + "      alert(decoded);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"", "exception"},
            IE = "no TextDecoder")
    public void decode2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextDecoder === 'undefined') {\n"
            + "        alert('no TextDecoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var dec = new TextDecoder('utf-8');\n"
            + "      try {\n"
            + "        alert(dec.decode(undefined));\n"
            + "      } catch(e) { alert('exception'); }\n"

            + "      try {\n"
            + "        alert(dec.decode(null));\n"
            + "      } catch(e) { alert('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
