/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code Intl.v8BreakIterator}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(value = IE, minVersion = 11), @WebBrowser(FF), @WebBrowser(CHROME) })
public class V8BreakIterator extends SimpleScriptable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return getClassName();
    }

    /**
     * Returns the index of the first break and moves pointer to it.
     * @return the index of the first break
     */
    @JsxFunction
    public int first() {
        return -1;
    }

    /**
     * Returns the index of the next break and moves pointer to it.
     * @return the index of the next break
     */
    @JsxFunction
    public int next() {
        return -1;
    }

    /**
     * Returns the index of the current break.
     * @return the index of the current break
     */
    @JsxFunction
    public int current() {
        return -1;
    }

    /**
     * Assigns text to be segmented to the iterator.
     * @param text the text
     */
    @JsxFunction
    public void adoptText(final String text) {
    }

    /**
     * Returns the type of the break.
     * @return {@code none}, {@code number}, {@code letter}, {@code kana}, {@code ideo} or {@code unknown}
     */
    @JsxFunction
    public String breakType() {
        return "none";
    }

}
