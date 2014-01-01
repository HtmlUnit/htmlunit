/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BUTTON_SET_TYPE_THROWS_EXCEPTION;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.io.IOException;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.FormField;

/**
 * The JavaScript object that represents a {@link HtmlButton} (&lt;button type=...&gt;).
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlButton.class)
public class HTMLButtonElement extends FormField {

    /**
     * Sets the value of the attribute "type".
     * <p>Note that there is no GUI change in the shape of the button,
     * so we don't treat it like {@link HTMLInputElement#setType(String)}.</p>
     * @param newType the new type to set
     */
    @JsxSetter
    public void setType(final String newType) {
        if (getBrowserVersion().hasFeature(JS_BUTTON_SET_TYPE_THROWS_EXCEPTION)) {
            throw Context.reportRuntimeError("Object doesn't support this action");
        }
        getDomNodeOrDie().setAttribute("type", newType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public String getType() {
        return ((HtmlButton) getDomNodeOrDie()).getTypeAttribute();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public String getAccessKey() {
        return super.getAccessKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter
    public void setAccessKey(final String accessKey) {
        super.setAccessKey(accessKey);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxFunction(@WebBrowser(FF))
    public void click() throws IOException {
        super.click();
    }
}
