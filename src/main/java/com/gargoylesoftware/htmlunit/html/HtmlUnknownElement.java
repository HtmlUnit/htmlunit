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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_DIALOG_NONE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_RP_DISPLAY_NONE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.CSS_RUBY_DISPLAY_INLINE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.MULTICOL_BLOCK;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.SLOT_CONTENTS;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * An element that is returned for an HTML tag that is not supported by this framework.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
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
     * {@inheritDoc}
     */
    @Override
    protected boolean isTrimmedText() {
        return false;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this frame as created by javascript. This is needed to handle
     * some special IE behavior.
     */
    public void markAsCreatedByJavascript() {
        createdByJavascript_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if this frame was created by javascript. This is needed to handle
     * some special IE behavior.
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
                if (hasFeature(CSS_RUBY_DISPLAY_INLINE)) {
                    return DisplayStyle.INLINE;
                }
                return DisplayStyle.RUBY;
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
            case HtmlMultiColumn.TAG_NAME:
                if (hasFeature(MULTICOL_BLOCK)) {
                    return DisplayStyle.BLOCK;
                }
                break;
            case HtmlDialog.TAG_NAME:
                if (hasFeature(CSS_DIALOG_NONE)) {
                    return DisplayStyle.NONE;
                }
                break;
            case HtmlSlot.TAG_NAME:
                if (getPage().getWebClient().getBrowserVersion().hasFeature(SLOT_CONTENTS)) {
                    return DisplayStyle.CONTENTS;
                }
                break;
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
