/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.css;

import java.io.Serializable;

import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.parser.media.MediaQuery;

/**
 * A MediaList.
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class CssMediaList implements Serializable {

    private final MediaListImpl wrappedList_;

    /**
     * Creates a new instance.
     * @param wrappedList the wrapped media list that this host object exposes
     */
    public CssMediaList(final MediaListImpl wrappedList) {
        wrappedList_ = wrappedList;
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     */
    public MediaQuery getMediaQuery(final int index) {
        if (index < 0 || index >= getLength()) {
            return null;
        }
        return wrappedList_.mediaQuery(index);
    }

    /**
     * Returns the number of media in the list.
     * @return the number of media in the list
     */
    public int getLength() {
        return wrappedList_.getLength();
    }

    /**
     * The parsable textual representation of the media list.
     * This is a comma-separated list of media.
     * @return the parsable textual representation.
     */
    public String getMediaText() {
        return wrappedList_.getMediaText();
    }
}
