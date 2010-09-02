/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement;

/**
 * Base class for elements which have a parent form.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class FormChild extends HTMLElement {

    /**
     * Creates an instance.
     */
    public FormChild() {
        // Empty.
    }

    /**
     * Returns the value of the JavaScript <tt>form</tt> attribute.
     *
     * @return the value of the JavaScript <tt>form</tt> attribute
     */
    public HTMLFormElement jsxGet_form() {
        final HtmlForm form = getDomNodeOrDie().getEnclosingForm();
        if (form == null) {
            return null;
        }
        return (HTMLFormElement) getScriptableFor(form);
    }

}
