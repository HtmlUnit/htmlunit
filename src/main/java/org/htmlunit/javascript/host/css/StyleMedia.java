/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.htmlunit.css.dom.MediaListImpl;
import org.htmlunit.css.parser.CSSErrorHandler;

import org.htmlunit.WebWindow;
import org.htmlunit.css.CssStyleSheet;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code StyleMedia}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(isJSObject = false, value = {CHROME, EDGE})
@JsxClass(IE)
public class StyleMedia extends HtmlUnitScriptable {

    /**
     * Default constructor.
     */
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
        final WebWindow webWindow = getWindow().getWebWindow();
        final CSSErrorHandler errorHandler = webWindow.getWebClient().getCssErrorHandler();
        final MediaListImpl mediaList = CssStyleSheet.parseMedia(errorHandler, media);
        return CssStyleSheet.isActive(mediaList, webWindow);
    }

}
