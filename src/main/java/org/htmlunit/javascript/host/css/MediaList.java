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

import org.htmlunit.css.CssMediaList;
import org.htmlunit.cssparser.parser.media.MediaQuery;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code MediaList}.
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Frank Danek
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/MediaList">MDN doc</a>
 */
@JsxClass
public class MediaList extends HtmlUnitScriptable {

    private final CssMediaList cssMediaList_;

    /**
     * Creates a new instance.
     */
    public MediaList() {
        super();
        cssMediaList_ = null;
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates a new instance.
     * @param parent the parent style
     * @param cssMediaList the css media list that this host object exposes
     */
    public MediaList(final CSSStyleSheet parent, final CssMediaList cssMediaList) {
        super();
        cssMediaList_ = cssMediaList;
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
        if (cssMediaList_ == null || index < 0 || index >= getLength()) {
            return null;
        }

        final MediaQuery mq = cssMediaList_.getMediaQuery(index);
        return mq.toString();
    }

    /**
     * Returns the number of media in the list.
     * @return the number of media in the list
     */
    @JsxGetter
    public int getLength() {
        if (cssMediaList_ == null) {
            return 0;
        }

        return cssMediaList_.getLength();
    }

    /**
     * The parsable textual representation of the media list.
     * This is a comma-separated list of media.
     * @return the parsable textual representation.
     */
    @JsxGetter
    public String getMediaText() {
        if (cssMediaList_ == null) {
            return null;
        }

        return cssMediaList_.getMediaText();
    }

    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (getPrototype() != null && cssMediaList_ != null) {
            if (cssMediaList_.getLength() == 0) {
                return "";
            }
            return cssMediaList_.getMediaText();
        }

        return super.getDefaultValue(hint);
    }
}
