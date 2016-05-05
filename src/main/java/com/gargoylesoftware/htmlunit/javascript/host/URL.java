/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.file.File;

/**
 * A JavaScript object for {@code URL}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class URL extends SimpleScriptable {

    /**
     * Creates an instance.
     */
    @JsxConstructor({@WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE)})
    public URL() {
    }

    /**
     * The URL.createObjectURL() static method creates a DOMString containing an URL
     * representing the object given in parameter.
     * The URL lifetime is tied to the document in the window on which it was created.
     * The new object URL represents the specified File object or Blob object.
     *
     * @param fileOrBlob Is a File object or a Blob object to create a object URL for.
     * @return the url
     */
    @JsxStaticFunction
    public static String createObjectURL(final Object fileOrBlob) {
        if (fileOrBlob instanceof File) {
            final File file = (File) fileOrBlob;
            return file.getFile().toURI().normalize().toString();
        }

        return null;
    }

    /**
     * @param objectURL String representing the object URL that was
     *          created by calling URL.createObjectURL().
     */
    @JsxStaticFunction
    public static void revokeObjectURL(final Object objectURL) {
    }
}
