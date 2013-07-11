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

import com.gargoylesoftware.htmlunit.html.HtmlProgress;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object "HTMLProgressElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass(domClasses = HtmlProgress.class, browsers = @WebBrowser(value = FF, minVersion = 10))
public class HTMLProgressElement extends HTMLElement {

    /**
     * The getter for the "value" property.
     * @return the value
     */
    @JsxGetter
    public double getValue() {
        return getAttributeAsDouble("value", 0);
    }

    /**
     * The getter for the "max" property.
     * @return the value
     */
    @JsxGetter
    public double getMax() {
        return getAttributeAsDouble("max", 1);
    }

    private double getAttributeAsDouble(final String attributeName, final double defaultValue) {
        try {
            return Double.parseDouble(getDomNodeOrDie().getAttribute(attributeName));
        }
        catch (final NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * {@inheritDoc}
    */
    @Override
    public String getDefaultStyleDisplay() {
        return "inline-block";
    }
}
