/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import org.apache.xml.utils.PrefixResolver;

import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

/**
 * A special {@link PrefixResolver} for {@link ScriptFunction}s.
 *
 * @author Chuck Dumont
 * @author Ahmed Ashour
 */
public class ScriptFunctionPrefixResolver implements PrefixResolver {

    private ScriptFunction resolverFn_;
    private Global global_;

    /**
     * Constructor.
     *
     * @param resolverFn the {@link ScriptFunction} this resolver is for
     * @param global the global
     */
    public ScriptFunctionPrefixResolver(final ScriptFunction resolverFn, final Global global) {
        resolverFn_ = resolverFn;
        global_ = global;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseIdentifier() {
        return apply();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceForPrefix(final String prefix) {
        return apply(prefix);
    }

    private String apply(final String... parameters) {
        final Object result = ScriptRuntime.apply(resolverFn_, global_, (Object[]) parameters);
        return result != null ? result.toString() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceForPrefix(final String prefix, final org.w3c.dom.Node node) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handlesNullPrefixes() {
        return false;
    }
}
