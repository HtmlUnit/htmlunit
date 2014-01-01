/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.javascript.host.MediaList;

/**
 * A JavaScript object for a {@link org.w3c.dom.css.CSSMediaRule}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@JsxClass
public class CSSMediaRule extends CSSRule {

    private MediaList media_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    @Deprecated
    public CSSMediaRule() {
        // Empty.
    }

    /**
     * Creates a new instance.
     * @param stylesheet the Stylesheet of this rule.
     * @param rule the wrapped rule
     */
    protected CSSMediaRule(final CSSStyleSheet stylesheet, final org.w3c.dom.css.CSSMediaRule rule) {
        super(stylesheet, rule);
    }

    /**
     * Returns the media types that the imported CSS style sheet applies to.
     * @return the media types that the imported CSS style sheet applies to
     */
    @JsxGetter
    public MediaList getMedia() {
        if (media_ == null) {
            final CSSStyleSheet parent = getParentStyleSheet();
            final org.w3c.dom.stylesheets.MediaList ml = getMediaRule().getMedia();
            media_ = new MediaList(parent, ml);
        }
        return media_;
    }

    /**
     * Returns the wrapped rule, as an media rule.
     * @return the wrapped rule, as an media rule
     */
    private org.w3c.dom.css.CSSMediaRule getMediaRule() {
        return (org.w3c.dom.css.CSSMediaRule) getRule();
    }
}
