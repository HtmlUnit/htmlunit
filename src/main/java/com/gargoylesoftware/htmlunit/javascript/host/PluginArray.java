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

/**
 * A JavaScript object for a document.navigator.plugins.
 * @version $Revision$
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/PluginArray.html">XUL Planet</a>
 */
public class PluginArray extends SimpleArray {
    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public PluginArray() {
        // nothing
    }

    /**
     * Current implementation does nothing.
     * @param reloadDocuments reload yes / no
     * @see <a href="http://www.xulplanet.com/references/objref/PluginArray.html#method_refresh">XUL Planet</a>
     */
    public void jsxFunction_refresh(final boolean reloadDocuments) {
        // nothing
    }

    /**
     * Gets the name of the plugin.
     * @param element a {@link Plugin}
     * @return the name
     */
    @Override
    protected String getItemName(final Object element) {
        return ((Plugin) element).jsxGet_name();
    }
}
