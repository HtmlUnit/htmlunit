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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * Base class for all JavaScript object corresponding to form fields.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
@JsxClass(isJSObject = false)
public class FormField extends FormChild {

    /**
     * Sets the associated DOM node and sets the enclosing form as parent scope of the current element.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#setDomNode(DomNode)
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        final HtmlForm form = ((HtmlElement) domNode).getEnclosingForm();
        if (form != null) {
            setParentScope(getScriptableFor(form));
        }
    }

    /**
     * Returns the value of the JavaScript attribute "value".
     *
     * @return the value of this attribute
     */
    @JsxGetter
    public String getValue() {
        return getDomNodeOrDie().getAttribute("value");
    }

    /**
     * Sets the value of the JavaScript attribute "value".
     *
     * @param newValue  the new value
     */
    @JsxSetter
    public void setValue(final String newValue) {
        getDomNodeOrDie().setAttribute("value", newValue);
    }

    /**
     * Returns the value of the JavaScript attribute "name".
     *
     * @return the value of this attribute
     */
    @JsxGetter
    public String getName() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the value of the JavaScript attribute "name".
     *
     * @param newName  the new name
     */
    @JsxSetter
    public void setName(final String newName) {
        getDomNodeOrDie().setAttribute("name", newName);
    }

    /**
     * Returns the value of the JavaScript attribute "type".
     *
     * @return the value of this attribute
     */
    @JsxGetter
    public String getType() {
        return getDomNodeOrDie().getAttribute("type");
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public boolean getDisabled() {
        return super.getDisabled();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter
    public void setDisabled(final boolean disabled) {
        super.setDisabled(disabled);
    }
}
