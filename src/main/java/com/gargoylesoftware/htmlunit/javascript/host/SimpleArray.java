/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A javascript object for simple array allowing access per key and index (like {@link MimeTypeArray}).
 *
 * @version $Revision$
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeTypeArray.html">XUL Planet</a>
 */
public class SimpleArray extends SimpleScriptable implements ScriptableWithFallbackGetter {
    private static final long serialVersionUID = 8025124211062703153L;
    private final List elements_ = new ArrayList();

    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public SimpleArray() {
        // nothing
    }

    /**
     * Return the item at the given index
     * @param index the index
     * @return the item at the given position.
     */
    public Object jsxFunction_item(final int index) {
        return elements_.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        final Object response = jsxFunction_namedItem(name);
        if (response != null) {
            return response;
        }
        else {
            return Context.getUndefinedValue();
        }
    }

    /**
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the
     * index is invalid.
     * {@inheritDoc}
     */
    public final Object get(final int index, final Scriptable start) {
        final SimpleArray array = (SimpleArray) start;
        final List elements = array.elements_;

        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        }
        else {
            return null;
        }
    }

    /**
     * Return the item at the given index
     * @param name the item name
     * @return the item with the given name.
     */
    public Object jsxFunction_namedItem(final String name) {
        for (final Iterator iterator = elements_.iterator(); iterator.hasNext();) {
            final Object element = iterator.next();
            if (name.equals(getItemName(element))) {
                return element;
            }
        }
        return null;
    }

    /**
     * Gets the name of the element.
     * Should be abstract but current implementation of prototype configuration doesn't allow it.
     * @param element the array's element
     * @return the element's name
     */
    protected String getItemName(final Object element) {
        return null;
    }

    /**
     * Get the array size.
     * @return the number elements.
     */
    public int jsxGet_length() {
        return elements_.size();
    }

    /**
     * Adds an element
     * @param element the element to add
     */
    void add(final Object element) {
        elements_.add(element);
    }
}
