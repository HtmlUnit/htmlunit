/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

/**
 * Constants of various features of each {@link BrowserVersion}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public enum BrowserVersionFeatures {

    /** */
    HTMLOPTION_PREVENT_DISABLED,

    /** */
    BLUR_BEFORE_ONCHANGE,

    /**
     * Indicates if href property for a &lt;link rel="stylesheet" type="text/css" href="..." /&gt;
     * is the fully qualified url.
     */
    STYLESHEET_HREF_EXPANDURL,

    /** Indicates the the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is "" */
    STYLESHEET_HREF_STYLE_EMPTY,

    /** Indicates the the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is null */
    STYLESHEET_HREF_STYLE_NULL;
}
