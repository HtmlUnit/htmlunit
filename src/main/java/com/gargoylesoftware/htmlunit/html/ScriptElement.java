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
     * Returns {@code true} if this script is deferred.
     * @return {@code true} if this script is deferred
     */
    boolean isDeferred();

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
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this script as created by javascript.
     * Spec: The following scripts will not execute: scripts in XMLHttpRequest's responseXML documents,
     * scripts in DOMParser-created documents, scripts in documents created by XSLTProcessor's
     * transformToDocument feature, and scripts that are first inserted by a script into a Document
     * that was created using the createDocument() API
     */
    void markAsCreatedByJavascript();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if this frame was created by javascript. This is needed to handle
     * some special IE behavior.
     * @return true or false
     */
    boolean wasCreatedByJavascript();
}
