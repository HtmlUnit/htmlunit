/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

/**
 * Base class for list-type elements (<tt>ul</tt>, <tt>ol</tt>, <tt>dir</tt>, etc).
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class HTMLListElement extends HTMLElement {

    /**
     * Returns the value of the <tt>compact</tt> attribute.
     * @return the value of the <tt>compact</tt> attribute
     */
    public boolean jsxGet_compact() {
        return getDomNodeOrDie().hasAttribute("compact");
    }

    /**
     * Sets the value of the <tt>compact</tt> attribute.
     * @param compact the value of the <tt>compact</tt> attribute
     */
    public void jsxSet_compact(final Object compact) {
        if (Context.toBoolean(compact)) {
            getDomNodeOrDie().setAttribute("compact", "");
        }
        else {
            getDomNodeOrDie().removeAttribute("compact");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxFunction_getAttribute(final String attributeName, final Integer flags) {
        if ("compact".equals(attributeName) && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_85)) {
            return jsxGet_compact();
        }
        return super.jsxFunction_getAttribute(attributeName, flags);
    }

}
