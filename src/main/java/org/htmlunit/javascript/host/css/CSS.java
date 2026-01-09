/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.css;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.util.StringUtils;

/**
 * A JavaScript object for {@code CSS}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class CSS extends HtmlUnitScriptable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if ("prototype".equals(name)) {
            return NOT_FOUND;
        }
        return super.get(name, start);
    }

    /**
     * @return a Boolean value indicating if the browser supports a given CSS feature, or not
     */
    @JsxStaticFunction
    public static boolean supports() {
        // for the moment we support everything :-)
        return true;
    }

    /**
     * @param ident the string to be escaped
     * @return a string containing the escaped string passed as parameter,
     *     mostly for use as part of a CSS selector
     */
    @JsxStaticFunction
    public static String escape(final String ident) {
        if (StringUtils.isEmptyOrNull(ident)) {
            return ident;
        }

        final int length = ident.length();
        final StringBuilder escaped = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final char c = ident.charAt(i);

            // If the character is NULL (U+0000), then the REPLACEMENT CHARACTER (U+FFFD).
            if (c == '\u0000') {
                escaped.append('\ufffd');
            }

            // If the character is in the range [\1-\1f] (U+0001 to U+001F) or is U+007F,
            // then the character escaped as code point.
            else if (('\u0001' <= c && c <= '\u001f') || c == '\u007f') {
                escaped.append('\\').append(Integer.toHexString(c)).append(' ');
            }

            // If the character is the first character and is in the range [0-9] (U+0030 to U+0039),
            // then the character escaped as code point.
            else if (i == 0 && ('\u0030' <= c && c <= '\u0039')) {
                escaped.append('\\').append(Integer.toHexString(c)).append(' ');
            }

            // If the character is the second character and is in the range [0-9] (U+0030 to U+0039)
            // and the first character is a "-" (U+002D), then the character escaped as code point.
            else if (i == 1 && ('\u0030' <= c && c <= '\u0039') && ident.charAt(0) == '-') {
                escaped.append('\\').append(Integer.toHexString(c)).append(' ');
            }

            // If the character is the first character and is a "-" (U+002D),
            // and there is no second character, then the escaped character.
            else if (i == 0 && c == '-' && length == 1) {
                escaped.append('\\').append(c);
            }

            // If the character is not handled by one of the above rules
            // and is greater than or equal to U+0080, is "-" (U+002D) or "_" (U+005F),
            // or is in one of the ranges [0-9] (U+0030 to U+0039),
            // [A-Z] (U+0041 to U+005A), or [a-z] (U+0061 to U+007A),
            // then the character itself.
            else if (c >= '\u0080'
                        || c == '\u002d'
                        || c == '\u005f'
                        || ('\u0030' <= c && c <= '\u0039')
                        || ('\u0041' <= c && c <= '\u005A')
                        || ('\u0061' <= c && c <= '\u007A')) {
                escaped.append(c);
            }

            // Otherwise, the escaped character
            else {
                escaped.append('\\').append(c);
            }
        }

        return escaped.toString();
    }
}
