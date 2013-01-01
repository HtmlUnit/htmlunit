/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlMarquee;
import com.gargoylesoftware.htmlunit.html.HtmlNoEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object "HTMLDivElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@JsxClass(domClasses = { HtmlDivision.class, HtmlMarquee.class, HtmlNoEmbed.class, HtmlNoFrames.class,
        HtmlNoScript.class })
public class HTMLDivElement extends HTMLElement {

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

}
