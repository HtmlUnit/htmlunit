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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Element;

/**
 * A JavaScript object for a CSSValue.
 *
 * @see org.w3c.dom.css.CSSValue
 * @version $Revision$
 * @author Marc Guillemot
 */
@JsxClass(browsers = @WebBrowser(FF))
public class CSSValue extends SimpleScriptable {

    /**
     * The value is inherited and the <code>cssText</code> contains "inherit".
     */
    @JsxConstant
    public static final short CSS_INHERIT = org.w3c.dom.css.CSSValue.CSS_INHERIT;

    /**
     * The value is a primitive value and an instance of the
     * <code>CSSPrimitiveValue</code> interface can be obtained by using
     * binding-specific casting methods on this instance of the
     * <code>CSSValue</code> interface.
     */
    @JsxConstant
    public static final short CSS_PRIMITIVE_VALUE = org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

    /**
     * The value is a <code>CSSValue</code> list and an instance of the
     * <code>CSSValueList</code> interface can be obtained by using
     * binding-specific casting methods on this instance of the
     * <code>CSSValue</code> interface.
     */
    @JsxConstant
    public static final short CSS_VALUE_LIST = org.w3c.dom.css.CSSValue.CSS_VALUE_LIST;

    /**
     * The value is a custom value.
     */
    @JsxConstant
    public static final short CSS_CUSTOM = org.w3c.dom.css.CSSValue.CSS_CUSTOM;

    /**
     * The wrapped CSS value.
     */
    private org.w3c.dom.css.CSSValue wrappedCssValue_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor to instantiate prototype.
     */
    public CSSValue() {
        // Empty.
    }

    /**
     * Creates an instance and sets its parent scope to the one of the provided element.
     * @param element the element to which this style is bound
     */
    CSSValue(final Element element, final org.w3c.dom.css.CSSValue cssValue) {
        setParentScope(element.getParentScope());
        setPrototype(getPrototype(getClass()));
        setDomNode(element.getDomNodeOrNull(), false);
        wrappedCssValue_ = cssValue;
    }

    /**
     * A string representation of the current value.
     * @return the string representation
     */
    @JsxGetter
    public String jsxGet_cssText() {
        return wrappedCssValue_.getCssText();
    }
}
