/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Marker/accessor interface implemented by the two elements the HTML spec defines as
 * "hyperlink elements" - {@link HtmlAnchor} ({@code a}) and {@link HtmlArea} ({@code area}).
 * Both share the same click/navigation-related attribute set ({@code href}, {@code target},
 * {@code rel}, {@code ping}, {@code download}); {@link HyperlinkElementSupport} operates on
 * this interface to avoid duplicating that logic across the two implementations.
 *
 * @see HyperlinkElementSupport
 *
 * @author Ronald Brill
 */
public interface HyperlinkElement {

    /**
     * Returns the value of the {@code href} attribute, or an empty string if not defined.
     *
     * @return the value of the {@code href} attribute, or an empty string if not defined
     */
    String getHrefAttribute();

    /**
     * Returns the value of the {@code target} attribute, or an empty string if not defined.
     *
     * @return the value of the {@code target} attribute, or an empty string if not defined
     */
    String getTargetAttribute();

    /**
     * Returns the value of the {@code rel} attribute, or an empty string if not defined.
     *
     * @return the value of the {@code rel} attribute, or an empty string if not defined
     */
    String getRelAttribute();

    /**
     * Returns the value of the {@code ping} attribute, or an empty string if not defined.
     *
     * @return the value of the {@code ping} attribute, or an empty string if not defined
     */
    String getPingAttribute();

    /**
     * Returns the value of the {@code download} attribute, or an empty string if not defined.
     *
     * @return the value of the {@code download} attribute, or an empty string if not defined
     */
    String getDownloadAttribute();
}
