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
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.SACMediaList;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.steadystate.css.dom.MediaListImpl;

/**
 * A JavaScript object for {@code StyleMedia}.
 *
 * @author Ahmed Ashour
 */
@JsxClasses({
        @JsxClass(isJSObject = false, isDefinedInStandardsMode = false, browsers = @WebBrowser(CHROME)),
        @JsxClass(browsers = { @WebBrowser(IE), @WebBrowser(EDGE) })
    })
public class StyleMedia extends SimpleScriptable {

    /**
     * Default constructor.
     */
    @JsxConstructor(@WebBrowser(EDGE))
    public StyleMedia() {
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return "screen";
    }

    /**
     * Returns whether the specified media is supported by the object that displays the document object.
     * @param media the media query
     * @return whether the specified media is supported or not
     */
    @JsxFunction
    public boolean matchMedium(final String media) {
        final ErrorHandler errorHandler = getWindow().getWebWindow().getWebClient().getCssErrorHandler();
        final SACMediaList mediaList = CSSStyleSheet.parseMedia(errorHandler, media);
        return CSSStyleSheet.isActive(this, new MediaListImpl(mediaList));
    }

}
