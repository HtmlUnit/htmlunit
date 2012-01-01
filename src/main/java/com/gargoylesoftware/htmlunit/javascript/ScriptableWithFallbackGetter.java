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
package com.gargoylesoftware.htmlunit.javascript;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Provides a clean way to specify a fallback property getter when the "normal way" failed.
 * Most properties are "cleanly" defined but some host objects like Document or Window are
 * able to return a value that has not been configured as a property (ex: the DOM node whose
 * ID or name matches the property name).
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public interface ScriptableWithFallbackGetter extends Scriptable {
    /**
     * Fallback called when no configured property is found with the given name
     * on the {@link Scriptable} object.
     * @param name the name of the requested property
     * @return the object value, {@link Scriptable#NOT_FOUND} if nothing is found
     */
    Object getWithFallback(final String name);
}
