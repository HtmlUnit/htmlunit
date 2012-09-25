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

import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for a {@link org.w3c.dom.css.CSSFontFaceRule}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@JsxClass
public class CSSFontFaceRule extends CSSRule {

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSFontFaceRule() {
        // Empty.
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSFontFaceRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSRule rule) {
        super(stylesheet, rule);
    }

    @Override
    @JsxGetter(@WebBrowser(BrowserName.FF))
    public short getType() {
        return FONT_FACE_RULE;
    }

    @Override
    @JsxGetter(@WebBrowser(BrowserName.FF))
    public String getCssText() {
        String cssText = super.getCssText();
        cssText = cssText.replace("{ ", "{\n  ");
        cssText = cssText.replace("; }", ";\n}");
        cssText = cssText.replace("; ", ";\n  ");
        cssText = cssText.replaceFirst("font-family: ([^;]*);", "font-family: \"$1\";");
        cssText = cssText.replaceFirst("src: ([^;]*);", "src: url(\"$1\");");
        return cssText;
    }
}
