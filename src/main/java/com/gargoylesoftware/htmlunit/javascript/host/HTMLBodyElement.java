/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

/**
 * The JavaScript object "HTMLBodyElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HTMLBodyElement extends HTMLElement {

    private static final long serialVersionUID = -915040139319661419L;

    /**
     * Creates a new instance.
     */
    public HTMLBodyElement() {
        // Empty.
    }

    /**
     * Creates the event handler from the attribute value. This has to be done no matter
     * which browser is simulated to handle ill formed html code with may body (may be generated) elements.
     * @param attributeName the attribute name
     * @param value the value
     */
    public void createEventHandlerFromAttribute(final String attributeName, final String value) {
        // when many body tags are found while parsing, attributes of
        // different tags are added and should create an event handler when needed
        if (attributeName.toLowerCase().startsWith("on")) {
            createEventHandler(attributeName, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setDefaults(final ComputedCSSStyleDeclaration style) {
        if (getBrowserVersion().isIE()) {
            style.setDefaultLocalStyleAttribute("margin", "15px 10px");
            style.setDefaultLocalStyleAttribute("padding", "0px");
        }
        else {
            style.setDefaultLocalStyleAttribute("margin-left", "8px");
            style.setDefaultLocalStyleAttribute("margin-right", "8px");
            style.setDefaultLocalStyleAttribute("margin-top", "8px");
            style.setDefaultLocalStyleAttribute("margin-bottom", "8px");
        }
    }

}
