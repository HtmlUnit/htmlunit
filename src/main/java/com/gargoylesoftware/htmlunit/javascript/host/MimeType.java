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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for a document.navigator.mimeTypes elements.
 *
 * @version $Revision$
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeType.html">XUL Planet</a>
 */
public final class MimeType extends SimpleScriptable {
    private String description_;
    private String suffixes_;
    private String type_;
    private Plugin enabledPlugin_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public MimeType() {
        // nothing
    }

    /**
     * Constructor initializing fields.
     * @param type the mime type
     * @param description the type description
     * @param suffixes the file suffixes
     * @param plugin the associated plugin
     */
    public MimeType(final String type, final String description, final String suffixes, final Plugin plugin) {
        type_ = type;
        description_ = description;
        suffixes_ = suffixes;
        enabledPlugin_ = plugin;
    }

    /**
     * Returns the mime type's description.
     * @return the description
     */
    @JsxGetter
    public String jsxGet_description() {
        return description_;
    }

    /**
     * Returns the mime type's suffixes.
     * @return the suffixes
     */
    @JsxGetter
    public String jsxGet_suffixes() {
        return suffixes_;
    }

    /**
     * Returns the mime type's suffixes.
     * @return the suffixes
     */
    @JsxGetter
    public String jsxGet_type() {
        return type_;
    }

    /**
     * Returns the mime type's associated plugin.
     * @return the plugin
     */
    @JsxGetter
    public Object jsxGet_enabledPlugin() {
        return enabledPlugin_;
    }
}
