/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import java.nio.charset.Charset;

/**
 * An element which can handle scripts.
 *
 * @author Ahmed Ashour
 */
public interface ScriptElement {

    /**
     * Returns if executed.
     * @return if executed
     */
    boolean isExecuted();

    /**
     * Sets if executed.
     * @param executed if executed
     */
    void setExecuted(boolean executed);

    /**
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src}
     * or an empty string if that attribute isn't defined.
     */
    String getSrcAttribute();

    /**
     * Returns the value of the attribute {@code charset}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code charset}
     * or an empty string if that attribute isn't defined.
     */
    String getCharsetAttribute();

    /**
     * Returns the {@link Charset}.
     *
     * @return the {@link Charset}
     * @deprecated as of 2.27, not used
     */
    @Deprecated
    Charset getCharset();

}
