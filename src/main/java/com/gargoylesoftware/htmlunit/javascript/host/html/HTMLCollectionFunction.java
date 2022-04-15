/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCOLLECTION_NULL_IF_NOT_FOUND;

import com.gargoylesoftware.htmlunit.html.DomNode;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A special HTMLCollection that implements the Function interface.
 *
 * @author Ronald Brill
 */
public abstract class HTMLCollectionFunction extends HTMLCollection implements Function {

    public HTMLCollectionFunction() {
    }

    /**
     * Creates an instance.
     * @param domNode parent scope
     * @param attributeChangeSensitive indicates if the content of the collection may change when an attribute
     * of a descendant node of parentScope changes (attribute added, modified or removed)
     */
    public HTMLCollectionFunction(final DomNode domNode, final boolean attributeChangeSensitive) {
        super(domNode, attributeChangeSensitive);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (args.length == 0) {
            throw Context.reportRuntimeError("Zero arguments; need an index or a key.");
        }
        final Object object = getIt(args[0]);
        if (object == NOT_FOUND) {
            if (getBrowserVersion().hasFeature(HTMLCOLLECTION_NULL_IF_NOT_FOUND)) {
                return null;
            }
            return Undefined.instance;
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        return null;
    }
}
