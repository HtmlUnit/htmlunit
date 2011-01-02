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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * An element that can have it's values sent to the server during a form submit.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public interface SubmittableElement {

    /**
     * <p>Returns an array of {@link NameValuePair}s that are the values that will be sent
     * back to the server whenever this element's containing form is submitted.</p>
     *
     * <p>THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     * BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.</p>
     *
     * @return the values that will be sent back to the server whenever this element's
     *         containing form is submitted
     */
    NameValuePair[] getSubmitKeyValuePairs();

    /**
     * Returns the value of this element to the default value or checked state (usually what it was at
     * the time the page was loaded, unless it has been modified via JavaScript).
     */
    void reset();

    /**
     * Sets the default value to use when this element gets reset, if applicable.
     * @param defaultValue the default value to use when this element gets reset, if applicable
     */
    void setDefaultValue(final String defaultValue);

    /**
     * Returns the default value to use when this element gets reset, if applicable.
     * @return the default value to use when this element gets reset, if applicable
     */
    String getDefaultValue();

    /**
     * Sets the default checked state to use when this element gets reset, if applicable.
     * @param defaultChecked the default checked state to use when this element gets reset, if applicable
     */
    void setDefaultChecked(final boolean defaultChecked);

    /**
     * Returns the default checked state to use when this element gets reset, if applicable.
     * @return the default checked state to use when this element gets reset, if applicable
     */
    boolean isDefaultChecked();

}
