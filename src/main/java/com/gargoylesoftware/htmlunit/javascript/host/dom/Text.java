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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code Text}.
 *
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Chuck Dumont
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = DomText.class)
public class Text extends CharacterData {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public Text() {
    }

    /**
     * Split a Text node in two.
     * @param offset the character position at which to split the Text node
     * @return the Text node that was split from this node
     */
    @JsxFunction
    public Object splitText(final int offset) {
        final DomText domText = (DomText) getDomNodeOrDie();
        return getScriptableFor(domText.splitText(offset));
    }

    /**
     * Returns wholeText value.
     * @return wholeText value
     */
    @JsxGetter
    public String getWholeText() {
        return ((DomText) getDomNodeOrDie()).getWholeText();
    }
}
