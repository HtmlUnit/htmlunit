/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.css.CssStyleSheet;
import org.htmlunit.html.HtmlStyle;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * The JavaScript object {@code HTMLStyleElement}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLStyleElement">MDN Documentation</a>
 */
@JsxClass(domClass = HtmlStyle.class)
public class HTMLStyleElement extends HTMLElement {

    private CSSStyleSheet sheet_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Gets the associated sheet.
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLStyleElement/sheet">MDN Documentation</a>
     * @return the sheet
     */
    @JsxGetter
    public CSSStyleSheet getSheet() {
        if (sheet_ != null) {
            return sheet_;
        }

        final HtmlStyle style = (HtmlStyle) getDomNodeOrDie();
        sheet_ = new CSSStyleSheet(this, getTopLevelScope(getParentScope()), style.getSheet());

        return sheet_;
    }

    /**
     * Returns the value of the {@code type} property.
     * @return the value of the {@code type} property
     */
    @JsxGetter
    public String getType() {
        final HtmlStyle style = (HtmlStyle) getDomNodeOrDie();
        return style.getTypeAttribute();
    }

    /**
     * Sets the value of the {@code type} property.
     * @param type the {@code type} property value
     */
    @JsxSetter
    public void setType(final String type) {
        final HtmlStyle style = (HtmlStyle) getDomNodeOrDie();
        style.setTypeAttribute(type);
    }

    /**
     * Returns the value of the {@code media} property.
     * @return the value of the {@code media} property
     */
    @JsxGetter
    public String getMedia() {
        final HtmlStyle style = (HtmlStyle) getDomNodeOrDie();
        return style.getAttributeDirect("media");
    }

    /**
     * Sets the value of the {@code media} property.
     * @param media the {@code media} property value
     */
    @JsxSetter
    public void setMedia(final String media) {
        final HtmlStyle style = (HtmlStyle) getDomNodeOrDie();
        style.setAttribute("media", media);
    }

    /**
     * Returns the {@code disabled} property.
     * @return the {@code disabled} property
     */
    @Override
    @JsxGetter
    public boolean isDisabled() {
        return !getSheet().getCssStyleSheet().isEnabled();
    }

    /**
     * Sets the {@code disabled} property.
     * @param disabled the {@code disabled} property value
     */
    @Override
    @JsxSetter
    public void setDisabled(final boolean disabled) {
        final CssStyleSheet sheet = getSheet().getCssStyleSheet();
        final boolean modified = disabled == sheet.isEnabled();
        sheet.setEnabled(!disabled);

        if (modified) {
            getDomNodeOrDie().getPage().clearComputedStyles();
        }
    }
}
