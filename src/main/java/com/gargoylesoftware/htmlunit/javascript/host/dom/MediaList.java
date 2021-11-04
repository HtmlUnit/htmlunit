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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_MEDIA_LIST_ALL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_MEDIA_LIST_EMPTY_STRING;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.parser.media.MediaQuery;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * A JavaScript object for {@code MediaList}.
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Frank Danek
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/MediaList">MDN doc</a>
 */
// FIXME according to MDN this is part of the CSSOM -> move to .host.css!
@JsxClass
public class MediaList extends SimpleScriptable {

    private final MediaListImpl wrappedList_;

    /**
     * Creates a new instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public MediaList() {
        wrappedList_ = null;
    }

    /**
     * Creates a new instance.
     * @param parent the parent style
     * @param wrappedList the wrapped media list that this host object exposes
     */
    public MediaList(final CSSStyleSheet parent, final MediaListImpl wrappedList) {
        wrappedList_ = wrappedList;
        setParentScope(parent);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     */
    @JsxFunction
    public String item(final int index) {
        if (index < 0 || index >= getLength()) {
            return null;
        }
        final MediaQuery mq = wrappedList_.mediaQuery(index);
        return mq.toString();
    }

    /**
     * Returns the number of media in the list.
     * @return the number of media in the list
     */
    @JsxGetter
    public int getLength() {
        return wrappedList_.getLength();
    }

    /**
     * The parsable textual representation of the media list.
     * This is a comma-separated list of media.
     * @return the parsable textual representation.
     */
    @JsxGetter
    public String getMediaText() {
        return wrappedList_.getMediaText();
    }

    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (getPrototype() != null && wrappedList_ != null) {
            if (wrappedList_.getLength() == 0) {
                final BrowserVersion browserVersion = getBrowserVersion();
                if (browserVersion.hasFeature(JS_MEDIA_LIST_EMPTY_STRING)) {
                    return "";
                }
                if (browserVersion.hasFeature(JS_MEDIA_LIST_ALL)) {
                    return "all";
                }
            }
            return wrappedList_.getMediaText();
        }
        return super.getDefaultValue(hint);
    }
}
