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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.Context;

/**
 * The JavaScript object "HTMLDivElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
public class HTMLDivElement extends HTMLElement {

    private static final long serialVersionUID = 7038120861175708271L;

    /**
     * Creates an instance.
     */
    public HTMLDivElement() {
        // Empty.
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    public String jsxGet_align() {
        String align = getDomNodeOrDie().getAttribute("align");
        if (align == NOT_FOUND) {
            align = "";
        }
        return align;
    }

    /**
     * Returns the value of the "align" property.
     * @param align the value
     */
    public void jsxSet_align(String align) {
        align = align.toLowerCase();
        if (getBrowserVersion().isFirefox()
                || align.equals("center") || align.equals("justify")
                || align.equals("left") || align.equals("right")) {
            getDomNodeOrDie().setAttribute("align", align);
        }
        else {
            Context.throwAsScriptRuntimeEx(new Exception("Could not get the align property. Invalid argument."));
        }
    }
}
