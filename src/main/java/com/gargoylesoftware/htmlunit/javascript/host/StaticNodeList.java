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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for a static NodeList.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass
public class StaticNodeList extends SimpleScriptable {

    private final List<Node> elements_;

    /**
     * Default constructor.
     */
    public StaticNodeList() {
        elements_ = new ArrayList<Node>();
    }

    /**
     * Constructor.
     * @param elements the elements
     * @param parentScope the parent scope
     */
    public StaticNodeList(final List<Node> elements, final ScriptableObject parentScope) {
        elements_ = elements;
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
    }

    @Override
    public Object get(final int index, final Scriptable start) {
        final StaticNodeList staticNodeList = (StaticNodeList) start;
        final Object result = staticNodeList.item(index);
        if (null == result) {
            return NOT_FOUND;
        }
        return result;
    }

    /**
     * Returns the item or items corresponding to the specified index or key.
     * @param index the index or key corresponding to the element or elements to return
     * @return the element or elements corresponding to the specified index or key
     */
    @JsxFunction
    public Node item(final int index) {
        if (index < 0 || index >= get_length()) {
            return null;
        }
        return elements_.get(index);
    }

    /**
     * Returns the length of this element array.
     * @return the length of this element array
     */
    @JsxGetter
    public int get_length() {
        return elements_.size();
    }

}
