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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for a document.navigator.plugins.
 * @version $Revision$
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeTypeArray.html">XUL Planet</a>
 */
@JsxClass(browsers = @WebBrowser(FF), extend = "SimpleArray")
public class Plugin extends SimpleArray {
    private String description_;
    private String filename_;
    private String name_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Plugin() {
        // nothing
    }

    /**
     * C'tor initializing fields.
     * @param name the plugin name
     * @param description the plugin description
     * @param filename the plugin filename
     */
    public Plugin(final String name, final String description, final String filename) {
        name_ = name;
        description_ = description;
        filename_ = filename;
    }

    /**
     * Gets the name of the mime type.
     * @param element a {@link MimeType}
     * @return the name
     */
    @Override
    protected String getItemName(final Object element) {
        return ((MimeType) element).jsxGet_type();
    }

    /**
     * Gets the plugin's description.
     * @return the description
     */
    @JsxGetter
    public String jsxGet_description() {
        return description_;
    }

    /**
     * Gets the plugin's file name.
     * @return the file name
     */
    @JsxGetter
    public String jsxGet_filename() {
        return filename_;
    }

    /**
     * Gets the plugin's name.
     * @return the name
     */
    @JsxGetter
    public String jsxGet_name() {
        return name_;
    }
}
