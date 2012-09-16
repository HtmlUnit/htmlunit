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

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement;

/**
 * A JavaScript object for Enumerator.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @see <a href="http://msdn.microsoft.com/en-us/library/6ch9zb09.aspx">MSDN Documentation</a>
 */
public class Enumerator extends SimpleScriptable {

    private int index_;

    private HTMLCollection collection_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Enumerator() {
        // Empty.
    }

    /**
     * JavaScript constructor.
     * @param o the object to enumerate over
     */
    public void jsConstructor(final Object o) {
        if (o instanceof HTMLCollection) {
            collection_ = (HTMLCollection) o;
        }
        else if (o instanceof HTMLFormElement) {
            collection_ = ((HTMLFormElement) o).jsxGet_elements();
        }
        else {
            throw new IllegalArgumentException(String.valueOf(o));
        }
    }

    /**
     * Returns whether the enumerator is at the end of the collection or not.
     * @return whether the enumerator is at the end of the collection or not
     */
    @JsxFunction
    public boolean jsxFunction_atEnd() {
        return index_ >= collection_.getLength();
    }

    /**
     * Returns the current item in the collection.
     * @return the current item in the collection
     */
    @JsxFunction
    public Object jsxFunction_item() {
        if (!jsxFunction_atEnd()) {
            SimpleScriptable scriptable = (SimpleScriptable) collection_.get(index_, collection_);
            scriptable = scriptable.clone();
            scriptable.setCaseSensitive(false);
            return scriptable;
        }
        return Undefined.instance;
    }

    /**
     * Resets the current item in the collection to the first item.
     */
    @JsxFunction
    public void jsxFunction_moveFirst() {
        index_ = 0;
    }

    /**
     * Moves the current item to the next item in the collection.
     */
    @JsxFunction
    public void jsxFunction_moveNext() {
        index_++;
    }
}
