/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.HtmlForm;

/**
 * The JavaScript object "HTMLLegendElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLLegendElement extends HTMLElement {

    private static final long serialVersionUID = -6536306776315347201L;

    /**
     * Creates an instance.
     */
    public HTMLLegendElement() {
        // Empty.
    }

    /**
     * Returns the value of the <tt>form</tt> property.
     * @return the value of the <tt>form</tt> property
     */
    public HTMLFormElement jsxGet_form() {
        final HtmlForm form = getDomNodeOrDie().getEnclosingForm();
        if (form == null) {
            return null;
        }
        return (HTMLFormElement) getScriptableFor(form);
    }

}
