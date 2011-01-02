/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * A JavaScript object for a MediaList.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class MediaList extends SimpleScriptable {

    private final org.w3c.dom.stylesheets.MediaList wrappedList_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public MediaList() {
        wrappedList_ = null;
    }

    /**
     * Creates a new instance.
     * @param parent the parent style
     * @param wrappedList the wrapped media list that this host object exposes
     */
    public MediaList(final CSSStyleSheet parent, final org.w3c.dom.stylesheets.MediaList wrappedList) {
        wrappedList_ = wrappedList;
        setParentScope(parent);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Returns the number of media in the list.
     * @return the number of media in the list
     */
    public int jsxGet_length() {
        return wrappedList_.getLength();
    }

}
