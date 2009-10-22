/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Process occurrences of \xDD in string literals.
 * <p>This is done by Rhino, but it is needed before passing the source to Rhino.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class StringScriptPreProcessor implements ScriptPreProcessor {

    /**
     * {@inheritDoc}
     */
    public String preProcess(final HtmlPage htmlPage, final String sourceCode,
            final String sourceName, final HtmlElement htmlElement) {

        final StringBuilder sb = new StringBuilder();
        boolean inString = false;
        char stringChar = 0;
        for (int i = 0; i < sourceCode.length(); i++) {
            final char ch = sourceCode.charAt(i);
            switch (ch) {
                case '\'':
                case '"':
                    if (stringChar == 0) {
                        stringChar = ch;
                        inString = true;
                    }
                    else {
                        stringChar = 0;
                        inString = false;
                    }
                    break;

                case '\\':
                    if (inString) {
                        if (i + 3 < sourceCode.length() && sourceCode.charAt(i + 1) == 'x') {
                            final char ch1 = Character.toUpperCase(sourceCode.charAt(i + 2));
                            final char ch2 = Character.toUpperCase(sourceCode.charAt(i + 3));
                            if ((ch1 >= '0' && ch1 <= '9' || ch1 >= 'A' && ch1 <= 'F')
                                    && (ch2 >= '0' && ch2 <= '9' || ch2 >= 'A' && ch2 <= 'F')) {
                                sb.append((char) Integer.parseInt(sourceCode.substring(i + 2, i + 4), 16));
                                i += 3;
                                continue;
                            }
                        }
                        else if (i + 1 < sourceCode.length()) {
                            sb.append(ch).append(sourceCode.charAt(i + 1));
                            i++;
                            continue;
                        }
                    }

                default:
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}
