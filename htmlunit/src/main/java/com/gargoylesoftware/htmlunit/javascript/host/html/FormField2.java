/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;

/**
 * Base class for all JavaScript object corresponding to form fields.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class FormField2 extends HTMLElement2 {

    /**
     * Returns the value of the JavaScript attribute {@code value}.
     *
     * @return the value of this attribute
     */
    @Getter
    public String getValue() {
        return getDomNodeOrDie().getAttribute("value");
    }

    /**
     * Sets the value of the JavaScript attribute {@code value}.
     *
     * @param newValue the new value
     */
    @Setter
    public void setValue(final Object newValue) {
        getDomNodeOrDie().setAttribute("value", newValue.toString());
    }

    /**
     * Returns the value of the JavaScript attribute {@code name}.
     *
     * @return the value of this attribute
     */
    @Getter
    public String getName() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the value of the JavaScript attribute {@code name}.
     *
     * @param newName the new name
     */
    @Setter
    public void setName(final String newName) {
        getDomNodeOrDie().setAttribute("name", newName);
    }

}
