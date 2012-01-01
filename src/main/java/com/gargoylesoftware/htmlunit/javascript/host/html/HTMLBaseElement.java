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
package com.gargoylesoftware.htmlunit.javascript.host.html;

/**
 * The JavaScript object "HTMLBaseElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLBaseElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLBaseElement() {
        // Empty.
    }

    /**
     * Returns the value of the "href" property.
     * @return the value of the "href" property
     */
    public String jsxGet_href() {
        return getDomNodeOrDie().getAttribute("href");
    }

    /**
     * Sets the value of the "href" property.
     * @param href the value of the "href" property
     */
    public void jsxSet_href(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of the "target" property.
     * @return the value of the "target" property
     */
    public String jsxGet_target() {
        return getDomNodeOrDie().getAttribute("target");
    }

    /**
     * Sets the value of the "target" property.
     * @param target the value of the "target" property
     */
    public void jsxSet_target(final String target) {
        getDomNodeOrDie().setAttribute("target", target);
    }

}
