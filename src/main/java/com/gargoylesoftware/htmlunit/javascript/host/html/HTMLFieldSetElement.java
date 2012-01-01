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

import com.gargoylesoftware.htmlunit.javascript.host.FormChild;

/**
 * The JavaScript object "HTMLFieldSetElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLFieldSetElement extends FormChild {

    /**
     * Creates an instance.
     */
    public HTMLFieldSetElement() {
        // Empty.
    }

    /**
     * Returns the value of the <tt>align</tt> property.
     * @return the value of the <tt>align</tt> property
     */
    public String jsxGet_align() {
        return getAlign(false);
    }

    /**
     * Sets the value of the <tt>align</tt> property.
     * @param align the value of the <tt>align</tt> property
     */
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getWithFallback(final String name) {
        if ("align".equals(name)) {
            return NOT_FOUND;
        }
        return super.getWithFallback(name);
    }

}
