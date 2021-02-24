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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;

/**
 * A JavaScript object for {@code TextDecoder}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF78})
public class TextDecoder extends SimpleScriptable {
    private static java.util.Map<String, Charset> ENCODINGS_;
    private static java.util.Map<String, String> ENCODING_NAMES_;
    private String encoding_ = StandardCharsets.UTF_8.name();

    static {
        ENCODINGS_ = new HashMap<>();
        ENCODING_NAMES_ = new HashMap<>();

        Charset charset = StandardCharsets.UTF_8;
        ENCODINGS_.put("utf-8", charset);
        ENCODINGS_.put("utf8", charset);
        ENCODINGS_.put("unicode-1-1-utf-8", charset);

        charset = Charset.forName("big5");
        ENCODINGS_.put("big5", charset);
        ENCODINGS_.put("big5-hkscs", charset);
        ENCODINGS_.put("cn-big5", charset);
        ENCODINGS_.put("csbig5", charset);
        ENCODINGS_.put("x-x-big5", charset);

        charset = Charset.forName("euc-jp");
        ENCODINGS_.put("cseucpkdfmtjapanese", charset);
        ENCODINGS_.put("euc-jp", charset);
        ENCODINGS_.put("x-euc-jp", charset);

        charset = Charset.forName("euc-kr");
        ENCODINGS_.put("cseuckr", charset);
        ENCODINGS_.put("csksc56011987", charset);
        ENCODINGS_.put("euc-kr", charset);
        ENCODINGS_.put("iso-ir-149", charset);
        ENCODINGS_.put("korean", charset);
        ENCODINGS_.put("ks_c_5601-1987", charset);
        ENCODINGS_.put("ks_c_5601-1989", charset);
        ENCODINGS_.put("ksc5601", charset);
        ENCODINGS_.put("ksc_5601", charset);
        ENCODINGS_.put("windows-949", charset);

        charset = Charset.forName("gb18030");
        ENCODINGS_.put("gb18030", charset);

        charset = Charset.forName("gbk");
        ENCODINGS_.put("chinese", charset);
        ENCODINGS_.put("csgb2312", charset);
        ENCODINGS_.put("csiso58gb231280", charset);
        ENCODINGS_.put("gb2312", charset);
        ENCODINGS_.put("gb_2312", charset);
        ENCODINGS_.put("gb_2312-80", charset);
        ENCODINGS_.put("gbk", charset);
        ENCODINGS_.put("iso-ir-58", charset);
        ENCODINGS_.put("x-gbk", charset);

        charset = Charset.forName("ibm866");
        ENCODINGS_.put("866", charset);
        ENCODINGS_.put("cp866", charset);
        ENCODINGS_.put("csibm866", charset);
        ENCODINGS_.put("ibm866", charset);

        charset = Charset.forName("iso-2022-jp");
        ENCODINGS_.put("csiso2022jp", charset);
        ENCODINGS_.put("iso-2022-jp", charset);

        charset = Charset.forName("iso-8859-2");
        ENCODINGS_.put("csisolatin2", charset);
        ENCODINGS_.put("iso-8859-2", charset);
        ENCODINGS_.put("iso-ir-101", charset);
        ENCODINGS_.put("iso8859-2", charset);
        ENCODINGS_.put("iso88592", charset);
        ENCODINGS_.put("iso_8859-2", charset);
        ENCODINGS_.put("iso_8859-2:1987", charset);
        ENCODINGS_.put("l2", charset);
        ENCODINGS_.put("latin2", charset);

        charset = Charset.forName("iso-8859-3");
        ENCODINGS_.put("csisolatin3", charset);
        ENCODINGS_.put("iso-8859-3", charset);
        ENCODINGS_.put("iso-ir-109", charset);
        ENCODINGS_.put("iso8859-3", charset);
        ENCODINGS_.put("iso88593", charset);
        ENCODINGS_.put("iso_8859-3", charset);
        ENCODINGS_.put("iso_8859-3:1988", charset);
        ENCODINGS_.put("l3", charset);
        ENCODINGS_.put("latin3", charset);

        charset = Charset.forName("iso-8859-4");
        ENCODINGS_.put("csisolatin4", charset);
        ENCODINGS_.put("iso-8859-4", charset);
        ENCODINGS_.put("iso-ir-110", charset);
        ENCODINGS_.put("iso8859-4", charset);
        ENCODINGS_.put("iso88594", charset);
        ENCODINGS_.put("iso_8859-4", charset);
        ENCODINGS_.put("iso_8859-4:1988", charset);
        ENCODINGS_.put("l4", charset);
        ENCODINGS_.put("latin4", charset);

        charset = Charset.forName("iso-8859-5");
        ENCODINGS_.put("csisolatincyrillic", charset);
        ENCODINGS_.put("cyrillic", charset);
        ENCODINGS_.put("iso-8859-5", charset);
        ENCODINGS_.put("iso-ir-144", charset);
        ENCODINGS_.put("iso88595", charset);
        ENCODINGS_.put("iso_8859-5", charset);
        ENCODINGS_.put("iso_8859-5:1988", charset);

        charset = Charset.forName("iso-8859-6");
        ENCODINGS_.put("arabic", charset);
        ENCODINGS_.put("asmo-708", charset);
        ENCODINGS_.put("csiso88596e", charset);
        ENCODINGS_.put("csiso88596i", charset);
        ENCODINGS_.put("csisolatinarabic", charset);
        ENCODINGS_.put("ecma-114", charset);
        ENCODINGS_.put("iso-8859-6", charset);
        ENCODINGS_.put("iso-8859-6-e", charset);
        ENCODINGS_.put("iso-8859-6-i", charset);
        ENCODINGS_.put("iso-ir-127", charset);
        ENCODINGS_.put("iso8859-6", charset);
        ENCODINGS_.put("iso88596", charset);
        ENCODINGS_.put("iso_8859-6", charset);
        ENCODINGS_.put("iso_8859-6:1987", charset);

        charset = Charset.forName("iso-8859-7");
        ENCODINGS_.put("csisolatingreek", charset);
        ENCODINGS_.put("ecma-118", charset);
        ENCODINGS_.put("elot_928", charset);
        ENCODINGS_.put("greek", charset);
        ENCODINGS_.put("greek8", charset);
        ENCODINGS_.put("iso-8859-7", charset);
        ENCODINGS_.put("iso-ir-126", charset);
        ENCODINGS_.put("iso8859-7", charset);
        ENCODINGS_.put("iso88597", charset);
        ENCODINGS_.put("iso_8859-7", charset);
        ENCODINGS_.put("iso_8859-7:1987", charset);
        ENCODINGS_.put("sun_eu_greek", charset);

        charset = Charset.forName("iso-8859-8");
        ENCODINGS_.put("csiso88598e", charset);
        ENCODINGS_.put("csisolatinhebrew", charset);
        ENCODINGS_.put("hebrew", charset);
        ENCODINGS_.put("iso-8859-8", charset);
        ENCODINGS_.put("iso-8859-8-e", charset);
        ENCODINGS_.put("iso-ir-138", charset);
        ENCODINGS_.put("iso8859-8", charset);
        ENCODINGS_.put("iso88598", charset);
        ENCODINGS_.put("iso_8859-8", charset);
        ENCODINGS_.put("iso_8859-8:1988", charset);
        ENCODINGS_.put("visual", charset);

//        charset = Charset.forName("iso-8859-8-i");
//        ENCODINGS_.put("csiso88598i", charset);
//        ENCODINGS_.put("iso-8859-8-i", charset);
//        ENCODINGS_.put("logical", charset);

//      charset = Charset.forName("iso-8859-10");
//      ENCODINGS_.put("csisolatin6", charset);
//      ENCODINGS_.put("iso-8859-10", charset);
//      ENCODINGS_.put("iso-ir-157", charset);
//      ENCODINGS_.put("iso8859-10", charset);
//      ENCODINGS_.put("iso885910", charset);
//      ENCODINGS_.put("l6", charset);
//      ENCODINGS_.put("latin6", charset);

        charset = Charset.forName("iso-8859-13");
        ENCODINGS_.put("iso-8859-13", charset);
        ENCODINGS_.put("iso8859-13", charset);
        ENCODINGS_.put("iso885913", charset);

//        charset = Charset.forName("iso-8859-14");
//        ENCODINGS_.put("iso-8859-14", charset);
//        ENCODINGS_.put("iso8859-14", charset);
//        ENCODINGS_.put("iso885914", charset);

        charset = Charset.forName("iso-8859-15");
        ENCODINGS_.put("csisolatin9", charset);
        ENCODINGS_.put("iso-8859-15", charset);
        ENCODINGS_.put("iso8859-15", charset);
        ENCODINGS_.put("iso885915", charset);
        ENCODINGS_.put("l9", charset);
        // ENCODINGS_.put("latin9", charset);

//        charset = Charset.forName("iso-8859-16");
//        ENCODINGS_.put("iso-8859-16", charset);

        charset = Charset.forName("koi8-r");
        ENCODINGS_.put("cskoi8r", charset);
        ENCODINGS_.put("koi", charset);
        ENCODINGS_.put("koi8", charset);
        ENCODINGS_.put("koi8-r", charset);
        ENCODINGS_.put("koi8_r", charset);

        charset = Charset.forName("koi8-u");
        ENCODINGS_.put("koi8-u", charset);

        charset = Charset.forName("shift_jis");
        ENCODINGS_.put("csshiftjis", charset);
        ENCODINGS_.put("ms_kanji", charset);
        ENCODINGS_.put("shift-jis", charset);
        ENCODINGS_.put("shift_jis", charset);
        ENCODINGS_.put("sjis", charset);
        ENCODINGS_.put("windows-31j", charset);
        ENCODINGS_.put("x-sjis", charset);

        charset = Charset.forName("utf-16be");
        ENCODINGS_.put("utf-16be", charset);

        charset = Charset.forName("utf-16le");
        ENCODINGS_.put("utf-16", charset);
        ENCODINGS_.put("utf-16le", charset);

        charset = Charset.forName("windows-1250");
        ENCODINGS_.put("cp1250", charset);
        ENCODINGS_.put("windows-1250", charset);
        ENCODINGS_.put("x-cp1250", charset);

        charset = Charset.forName("windows-1251");
        ENCODINGS_.put("cp1251", charset);
        ENCODINGS_.put("windows-1251", charset);
        ENCODINGS_.put("x-cp1251", charset);

        charset = Charset.forName("windows-1252");
        ENCODINGS_.put("ansi_x3.4-1968", charset);
        ENCODINGS_.put("ascii", charset);
        ENCODINGS_.put("cp1252", charset);
        ENCODINGS_.put("cp819", charset);
        ENCODINGS_.put("csisolatin1", charset);
        ENCODINGS_.put("ibm819", charset);
        ENCODINGS_.put("iso-8859-1", charset);
        ENCODINGS_.put("iso-ir-100", charset);
        ENCODINGS_.put("iso8859-1", charset);
        ENCODINGS_.put("iso88591", charset);
        ENCODINGS_.put("iso_8859-1", charset);
        ENCODINGS_.put("iso_8859-1:1987", charset);
        ENCODINGS_.put("l1", charset);
        ENCODINGS_.put("latin1", charset);
        ENCODINGS_.put("us-ascii", charset);
        ENCODINGS_.put("windows-1252", charset);
        ENCODINGS_.put("x-cp1252", charset);

        charset = Charset.forName("windows-1253");
        ENCODINGS_.put("cp1253", charset);
        ENCODINGS_.put("windows-1253", charset);
        ENCODINGS_.put("x-cp1253", charset);

        charset = Charset.forName("windows-1254");
        ENCODINGS_.put("cp1254", charset);
        ENCODINGS_.put("csisolatin5", charset);
        ENCODINGS_.put("iso-8859-9", charset);
        ENCODINGS_.put("iso-ir-148", charset);
        ENCODINGS_.put("iso8859-9", charset);
        ENCODINGS_.put("iso88599", charset);
        ENCODINGS_.put("iso_8859-9", charset);
        ENCODINGS_.put("iso_8859-9:1989", charset);
        ENCODINGS_.put("l5", charset);
        ENCODINGS_.put("latin5", charset);
        ENCODINGS_.put("windows-1254", charset);
        ENCODINGS_.put("x-cp1254", charset);

        charset = Charset.forName("windows-1255");
        ENCODINGS_.put("cp1255", charset);
        ENCODINGS_.put("windows-1255", charset);
        ENCODINGS_.put("x-cp1255", charset);

        charset = Charset.forName("windows-1256");
        ENCODINGS_.put("cp1256", charset);
        ENCODINGS_.put("windows-1256", charset);
        ENCODINGS_.put("x-cp1256", charset);

        charset = Charset.forName("windows-1257");
        ENCODINGS_.put("cp1257", charset);
        ENCODINGS_.put("windows-1257", charset);
        ENCODINGS_.put("x-cp1257", charset);

        charset = Charset.forName("windows-1258");
        ENCODINGS_.put("cp1258", charset);
        ENCODINGS_.put("windows-1258", charset);
        ENCODINGS_.put("x-cp1258", charset);

        charset = Charset.forName("x-windows-874");
        ENCODING_NAMES_.put("x-windows-874", "windows-874");
        ENCODINGS_.put("dos-874", charset);
        ENCODINGS_.put("iso-8859-11", charset);
        ENCODINGS_.put("iso8859-11", charset);
        ENCODINGS_.put("iso885911", charset);
        ENCODINGS_.put("tis-620", charset);
        ENCODINGS_.put("windows-874", charset);

        charset = Charset.forName("x-MacCyrillic");
        ENCODING_NAMES_.put("x-MacCyrillic", "x-mac-cyrillic");
        ENCODINGS_.put("x-mac-cyrillic", charset);
        ENCODINGS_.put("x-mac-ukrainian", charset);

        charset = Charset.forName("x-MacRoman");
        ENCODING_NAMES_.put("x-MacRoman", "macintosh");
        ENCODINGS_.put("csmacintosh", charset);
        ENCODINGS_.put("mac", charset);
        ENCODINGS_.put("macintosh", charset);
        ENCODINGS_.put("x-mac-roman", charset);

//        charset = Charset.forName("x-user-defined");
//        ENCODINGS_.put("x-user-defined", charset);
    }

    /**
     * Ctor.
     */
    public TextDecoder() {
    }

    /**
     * Creates an instance.
     * @param encoding the encoding
     */
    @JsxConstructor
    public TextDecoder(final Object encoding) {
        if (Undefined.isUndefined(encoding)) {
            return;
        }

        final String enc = Context.toString(encoding);
        final Charset charset = ENCODINGS_.get(enc);
        if (charset != null) {
            encoding_ = charset.name();
            return;
        }
        throw ScriptRuntime.typeError("Argument 1 '" + enc + "' is not a supported encoding");
    }

    /**
     * @return always "utf-8"
     */
    @JsxGetter
    public String getEncoding() {
        final String name = ENCODING_NAMES_.get(encoding_);
        if (name != null) {
            return name;
        }
        return encoding_.toLowerCase(Locale.ROOT);
    }

    /**
     * @param buffer to be decoded
     * @return returns the decoded string
     */
    @JsxFunction
    public String decode(final Object buffer) {
        if (Undefined.isUndefined(buffer)) {
            return "";
        }

        NativeArrayBuffer arrayBuffer = null;
        if (buffer instanceof NativeArrayBuffer) {
            arrayBuffer = (NativeArrayBuffer) buffer;
        }
        else if (buffer instanceof NativeArrayBufferView) {
            arrayBuffer = ((NativeArrayBufferView) buffer).getBuffer();
        }

        if (arrayBuffer != null) {
            return new String(arrayBuffer.getBuffer(), Charset.forName(encoding_));
        }

        throw ScriptRuntime.typeError("Argument 1 of TextDecoder.decode could not be"
                                + " converted to any of: ArrayBufferView, ArrayBuffer.");
    }
}
