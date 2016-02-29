/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.SACMediaList;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.steadystate.css.dom.MediaListImpl;

/**
 * A JavaScript object for {@code MediaQueryList}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class MediaQueryList extends EventTarget {

    private String media_;

    /**
     * Default constructor.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public MediaQueryList() {
    }

    /**
     * Constructor.
     *
     * @param mediaQueryString the media query string
     */
    public MediaQueryList(final String mediaQueryString) {
        media_ = mediaQueryString;
    }

    /**
     * Returns the {@code media} property.
     * @return the {@code media} property
     */
    @JsxGetter
    public String getMedia() {
        return media_;
    }

    /**
     * Returns whether the document currently matches the media query list or not.
     * @return whether the document currently matches the media query list or not
     */
    @JsxGetter
    public boolean getMatches() {
        final ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
        final SACMediaList mediaList = CSSStyleSheet.parseMedia(errorHandler, media_);
        return CSSStyleSheet.isActive(this, new MediaListImpl(mediaList));
    }

    /**
     * Adds the {@code listener} event handler for this element.
     * @param listener the {@code listener} event handler for this element
     */
    @JsxFunction
    public void addListener(final Object listener) {
        // dummy impl for the moment
    }

    /**
     * Removes the {@code listener} event handler for this element.
     * @param listener the {@code listener} event handler to be removed
     */
    @JsxFunction
    public void removeListener(final Object listener) {
        // dummy impl for the moment
    }
}
