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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DEFINITION_COMMON_CLASS_NAME;

import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;

/**
 * The JavaScript object "HTMLDefinitionDescriptionElement".
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlDefinitionDescription.class)
public class HTMLDefinitionDescriptionElement extends HTMLElement {

    /**
     * Returns the value of the "cite" property.
     * @return the value of the "cite" property
     */
    public String getCite() {
        final String cite = getDomNodeOrDie().getAttribute("cite");
        return cite;
    }

    /**
     * Returns the value of the "cite" property.
     * @param cite the value
     */
    public void setCite(final String cite) {
        getDomNodeOrDie().setAttribute("cite", cite);
    }

    /**
     * Returns the value of the "dateTime" property.
     * @return the value of the "dateTime" property
     */
    public String getDateTime() {
        final String dateTime = getDomNodeOrDie().getAttribute("datetime");
        return dateTime;
    }

    /**
     * Returns the value of the "dateTime" property.
     * @param dateTime the value
     */
    public void setDateTime(final String dateTime) {
        getDomNodeOrDie().setAttribute("datetime", dateTime);
    }

    /**
     * Returns whether the end tag is forbidden or not.
     * @see <a href="http://www.w3.org/TR/html4/index/elements.html">HTML 4 specs</a>
     * @return whether the end tag is forbidden or not
     */
    protected boolean isEndTagForbidden() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null && getBrowserVersion().hasFeature(JS_DEFINITION_COMMON_CLASS_NAME)) {
            return "HTMLElement";
        }
        return "HTMLDDElement";
    }
}
