/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.apache.xml.utils.PrefixResolver;

/**
 * A special {@link PrefixResolver} for {@link NativeFunction}s.
 *
 * @version $Revision$
 * @author Chuck Dumont
 */
public class NativeFunctionPrefixResolver implements PrefixResolver {

    private NativeFunction resolverFn_;
    private Scriptable scope_;

    /**
     * Constructor.
     *
     * @param resolverFn the {@link NativeFunction} this resolver is for
     * @param scope the scope
     */
    public NativeFunctionPrefixResolver(final NativeFunction resolverFn, final Scriptable scope) {
        resolverFn_ = resolverFn;
        scope_ = scope;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseIdentifier() {
        final Object result = Context.call(null, resolverFn_, scope_, null, new Object[]{});
        return result != null ? result.toString() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceForPrefix(final String prefix) {
        final Object result = Context.call(null, resolverFn_, scope_, null, new Object[]{prefix});
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
