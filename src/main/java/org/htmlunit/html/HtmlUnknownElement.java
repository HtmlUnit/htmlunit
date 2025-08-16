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
package org.htmlunit.html;

import static org.htmlunit.BrowserVersionFeatures.CSS_RP_DISPLAY_NONE;
import static org.htmlunit.BrowserVersionFeatures.CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS;

import java.util.Map;

import org.htmlunit.SgmlPage;

/**
 * An element that is returned for an HTML tag that is not supported by this framework.
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Christian Sell
 * @author Ahmed Ashour
 * @author Rodney Gitzel
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlUnknownElement extends HtmlElement {

    private boolean createdByJavascript_;

    /**
     * Creates an instance.
     *
     * @param page the page that contains this element
     * @param tagName the HTML tag represented by this object
     * @param attributes the initial attributes
     */
    HtmlUnknownElement(final SgmlPage page, final String tagName, final Map<String, DomAttr> attributes) {
        super(tagName, page, attributes);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this frame as created by javascript.
     */
    public void markAsCreatedByJavascript() {
        createdByJavascript_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if this frame was created by javascript.
     * @return true or false
     */
    public boolean wasCreatedByJavascript() {
        return createdByJavascript_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        switch (getTagName()) {
            case HtmlRuby.TAG_NAME:
                return DisplayStyle.RUBY;
            case HtmlRb.TAG_NAME:
                if (!hasFeature(CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS)
                        && wasCreatedByJavascript() && getParentNode() == null) {
                    return DisplayStyle.BLOCK;
                }
                return DisplayStyle.RUBY_BASE;
            case HtmlRp.TAG_NAME:
                if (hasFeature(CSS_RP_DISPLAY_NONE)) {
                    return DisplayStyle.NONE;
                }
                if (wasCreatedByJavascript() && getParentNode() == null) {
                    return DisplayStyle.BLOCK;
                }
                break;
            case HtmlRt.TAG_NAME:
                if (!hasFeature(CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS)
                        && wasCreatedByJavascript() && getParentNode() == null) {
                    return DisplayStyle.BLOCK;
                }
                return DisplayStyle.RUBY_TEXT;
            case HtmlRtc.TAG_NAME:
                if (!hasFeature(CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS)
                        && wasCreatedByJavascript() && getParentNode() == null) {
                    return DisplayStyle.BLOCK;
                }
                return DisplayStyle.RUBY_TEXT_CONTAINER;
            case HtmlDialog.TAG_NAME:
                return DisplayStyle.NONE;
            case HtmlSlot.TAG_NAME:
                return DisplayStyle.CONTENTS;
            default:
        }
        return DisplayStyle.INLINE;
    }

    /**
     * {@inheritDoc}
     * @return {@code true} to make generated XML readable as HTML.
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }
}
