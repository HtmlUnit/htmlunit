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

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for a {@link org.w3c.dom.css.CSSCharsetRule}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@JsxClass
public class CSSCharsetRule extends CSSRule {

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSCharsetRule() {
        // Empty.
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSCharsetRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSCharsetRule rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the encoding of the charset rule.
     * @return the encoding of the charset rule
     */
    @JsxGetter
    public String get_encoding() {
        return getCharsetRule().getEncoding();
    }

    /**
     * Sets the encoding of the charset rule.
     * @param encoding the encoding of the charset rule
     */
    @JsxSetter
    public void setEncoding(final String encoding) {
        getCharsetRule().setEncoding(encoding);
    }

    /**
     * Returns the wrapped rule, as a charset rule.
     * @return the wrapped rule, as a charset rule
     */
    private org.w3c.dom.css.CSSCharsetRule getCharsetRule() {
        return (org.w3c.dom.css.CSSCharsetRule) getRule();
    }

}
