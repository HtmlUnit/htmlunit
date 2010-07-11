/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;

/**
 * A collection of {@link Namespace}s.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class NamespaceCollection extends SimpleScriptable implements Function {

    /** Serial version UID. */
    private static final long serialVersionUID = 3414897723813218653L;

    /** The HTML document to which this namespace collection belongs. */
    private final HTMLDocument doc_;

    /** The namespaces contained by this namespace collection. */
    private final List<Namespace> namespaces_;

    /** Default constructor required by Rhino. */
    public NamespaceCollection() {
        doc_ = null;
        namespaces_ = new ArrayList<Namespace>();
    }

    /**
     * Creates a new namespace collection for the specified page.
     * @param doc the HTML document to which this namespace collection belongs
     */
    public NamespaceCollection(final HTMLDocument doc) {
        doc_ = doc;
        namespaces_ = new ArrayList<Namespace>();

        setParentScope(doc);
        setPrototype(getPrototype(getClass()));

        final Map<String, String> namespacesMap = doc_.getHtmlPage().getNamespaces();
        for (final String key : namespacesMap.keySet()) {
            if (key.length() != 0) {
                namespaces_.add(new Namespace(doc_, key, namespacesMap.get(key)));
            }
        }
    }

    /**
     * Creates a new namespace and adds it to the collection.
     * @param namespace the name of the namespace to add
     * @param urn the URN of the namespace to add
     * @param url the URL of the namespace to add (optional)
     * @return the newly created namespace
     */
    public final Namespace jsxFunction_add(final String namespace, final String urn, final String url) {
        // TODO: should we add the namespace to the HtmlUnit DOM?
        final Namespace n = new Namespace(doc_, namespace, urn);
        namespaces_.add(n);
        return n;
    }

    /**
     * Returns the length of this namespace collection.
     * @return the length of this namespace collection
     */
    public final int jsxGet_length() {
        return namespaces_.size();
    }

    /**
     * Returns the namespace at the specified index.
     * @param index the index of the namespace (either the numeric index, or the name of the namespace)
     * @return the namespace at the specified index
     */
    public final Object jsxFunction_item(final Object index) {
        if (index instanceof Number) {
            final Number n = (Number) index;
            final int i = n.intValue();
            return get(i, this);
        }
        final String key = String.valueOf(index);
        return get(key, this);
    }

    /** {@inheritDoc} */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (index >= 0 && index < namespaces_.size()) {
            return namespaces_.get(index);
        }
        return super.get(index, start);
    }

    /** {@inheritDoc} */
    @Override
    public Object get(final String name, final Scriptable start) {
        for (final Namespace n : namespaces_) {
            if (StringUtils.equals(n.jsxGet_name(), name)) {
                return n;
            }
        }
        return super.get(name, start);
    }

    /** {@inheritDoc} */
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        if (args.length != 1) {
            return NOT_FOUND;
        }
        return jsxFunction_item(args[0]);
    }

    /** {@inheritDoc} */
    public Scriptable construct(final Context cx, final Scriptable scope, final Object[] args) {
        return null;
    }

}
