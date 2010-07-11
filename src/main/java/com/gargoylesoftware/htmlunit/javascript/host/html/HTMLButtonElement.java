/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.javascript.host.FormField;

/**
 * The JavaScript object that represents a {@link HtmlButton} (&lt;button type=...&gt;).
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLButtonElement extends FormField {

    private static final long serialVersionUID = 7075194404850135839L;

    /**
     * Creates an instance.
     */
    public HTMLButtonElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Sets the value of the attribute "type".
     * <p>Note that there is no GUI change in the shape of the button,
     * so we don't treat it like {@link HTMLInputElement#jsxSet_type(String)}.</p>
     * @param newType the new type to set
     */
    public void jsxSet_type(final String newType) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_44)) {
            throw Context.reportRuntimeError("Object doesn't support this action");
        }
        getDomNodeOrDie().setAttribute("type", newType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_type() {
        return ((HtmlButton) getDomNodeOrDie()).getTypeAttribute();
    }
}
