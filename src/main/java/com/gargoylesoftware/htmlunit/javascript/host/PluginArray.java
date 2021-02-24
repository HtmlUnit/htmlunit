/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;

/**
 * A JavaScript object for {@code PluginArray}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="http://www.xulplanet.com/references/objref/PluginArray.html">XUL Planet</a>
 */
@JsxClass
public class PluginArray extends SimpleArray {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public PluginArray() {
    }

    /**
     * Current implementation does nothing.
     * @param reloadDocuments reload yes / no
     * @see <a href="http://www.xulplanet.com/references/objref/PluginArray.html#method_refresh">XUL Planet</a>
     */
    @JsxFunction
    public void refresh(final boolean reloadDocuments) {
        // nothing
    }

    /**
     * Gets the name of the plugin.
     * @param element a {@link Plugin}
     * @return the name
     */
    @Override
    protected String getItemName(final Object element) {
        return ((Plugin) element).getName();
    }
}
