/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import org.htmlunit.cssparser.dom.CSSFontFaceRuleImpl;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for a {@link org.w3c.dom.css.CSSFontFaceRule}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 * @author Ahmed Ashour
 */
@JsxClass
public class CSSFontFaceRule extends CSSRule {

    /**
     * Creates a new instance.
     */
    public CSSFontFaceRule() {
        super();
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor
    @Override
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSFontFaceRule(final CSSStyleSheet stylesheet, final CSSFontFaceRuleImpl rule) {
        super(stylesheet, rule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getType() {
        return FONT_FACE_RULE;
    }
}
