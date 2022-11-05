/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

/**
 * A marker interface for those classes that can be disabled.
 *
 * @author David D. Kilzer
 * @author Ronald Brill
 */
public interface DisabledElement {

    /** The "disabled" attribute name. */
    String ATTRIBUTE_DISABLED = "disabled";

    /**
     * Returns {@code true} if the disabled attribute is set for this element.
     * @return {@code true} if the disabled attribute is set for this element
     */
    boolean isDisabled();

    /**
     * Returns the value of the attribute {@code disabled}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code disabled} or an empty string if that attribute isn't defined
     */
    String getDisabledAttribute();

}
