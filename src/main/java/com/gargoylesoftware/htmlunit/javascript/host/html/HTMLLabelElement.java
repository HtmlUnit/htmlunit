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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.FormChild;

/**
 * A JavaScript object for a Label.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlLabel.class)
public class HTMLLabelElement extends FormChild {

    /**
     * Retrieves the object to which the given label object is assigned.
     * @return the identifier of the element to which the label element is assigned
     */
    @JsxGetter
    public String getHtmlFor() {
        return ((HtmlLabel) getDomNodeOrDie()).getForAttribute();
    }

    /**
     * Sets or retrieves the object to which the given label object is assigned.
     * @param id Specifies the identifier of the element to which the label element is assigned
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms533872.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setHtmlFor(final String id) {
        ((HtmlLabel) getDomNodeOrDie()).setAttribute("for", id);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter(@WebBrowser(FF))
    public String getAccessKey() {
        return super.getAccessKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter(@WebBrowser(FF))
    public void setAccessKey(final String accessKey) {
        super.setAccessKey(accessKey);
    }
}
