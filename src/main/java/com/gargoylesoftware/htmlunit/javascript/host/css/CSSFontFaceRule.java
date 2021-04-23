/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_FONTFACERULE_CSSTEXT_CHROME_STYLE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_FONTFACERULE_CSSTEXT_IE_STYLE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.css.dom.CSSFontFaceRuleImpl;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

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

    private static final Pattern REPLACEMENT_1 = Pattern.compile("font-family: ([^;]*);");
    private static final Pattern REPLACEMENT_2 = Pattern.compile("src: url\\(([^;]*)\\);");

    /**
     * Creates a new instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public CSSFontFaceRule() {
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
    public short getType() {
        return FONT_FACE_RULE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCssText() {
        String cssText = super.getCssText();
        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(CSS_FONTFACERULE_CSSTEXT_IE_STYLE)) {
            cssText = StringUtils.replace(cssText, "{", "{\n\t");
            cssText = StringUtils.replace(cssText, "}", ";\n}\n");
            cssText = StringUtils.replace(cssText, "; ", ";\n\t");
        }
        else if (browserVersion.hasFeature(CSS_FONTFACERULE_CSSTEXT_CHROME_STYLE)) {
            cssText = StringUtils.replace(cssText, "{", "{ ");
            cssText = StringUtils.replace(cssText, "}", "; }");
            cssText = StringUtils.replace(cssText, "; ", "; ");
            cssText = REPLACEMENT_1.matcher(cssText).replaceFirst("font-family: $1;");
            cssText = REPLACEMENT_2.matcher(cssText).replaceFirst("src: url(\"$1\");");
        }
        else {
            cssText = StringUtils.replace(cssText, "{", "{\n  ");
            cssText = StringUtils.replace(cssText, "}", ";\n}");
            cssText = StringUtils.replace(cssText, "; ", ";\n  ");
            cssText = REPLACEMENT_1.matcher(cssText).replaceFirst("font-family: $1;");
            cssText = REPLACEMENT_2.matcher(cssText).replaceFirst("src: url(\"$1\");");
        }
        return cssText;
    }
}
