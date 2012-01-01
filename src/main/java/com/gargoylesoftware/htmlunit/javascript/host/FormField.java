/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Function;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;

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
    public String jsxGet_value() {
        return getDomNodeOrDie().getAttribute("value");
    }

    /**
     * Sets the value of the JavaScript attribute "value".
     *
     * @param newValue  the new value
     */
    public void jsxSet_value(final String newValue) {
        getDomNodeOrDie().setAttribute("value", newValue);
    }

    /**
     * Returns the value of the JavaScript attribute "name".
     *
     * @return the value of this attribute
     */
    public String jsxGet_name() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the value of the JavaScript attribute "name".
     *
     * @param newName  the new name
     */
    public void jsxSet_name(final String newName) {
        getDomNodeOrDie().setAttribute("name", newName);
    }

    /**
     * Returns the value of the JavaScript attribute "type".
     *
     * @return the value of this attribute
     */
    public String jsxGet_type() {
        return getDomNodeOrDie().getAttribute("type");
    }

    /**
     * Sets the <tt>onchange</tt> event handler for this element.
     * @param onchange the <tt>onchange</tt> event handler for this element
     */
    public void jsxSet_onchange(final Object onchange) {
        setEventHandlerProp("onchange", onchange);
    }

    /**
     * Returns the <tt>onchange</tt> event handler for this element.
     * @return the <tt>onchange</tt> event handler for this element
     */
    public Function jsxGet_onchange() {
        return getEventHandler("onchange");
    }
}
