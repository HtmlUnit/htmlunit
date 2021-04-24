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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENTRECTLIST_THROWS_IF_ITEM_NOT_FOUND;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code ClientRectList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(className = "DOMRectList", value = {CHROME, EDGE, FF, FF78})
@JsxClass(IE)
public class ClientRectList extends SimpleScriptable {

    private final List<ClientRect> clientRects_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public ClientRectList() {
        clientRects_ = new ArrayList<>();
    }

    /**
     * Returns the length property.
     * @return the length
     */
    @JsxGetter
    public int getLength() {
        return clientRects_.size();
    }

    /**
     * Returns the element at the specified index, or {@link #NOT_FOUND} if the index is invalid.
     * {@inheritDoc}
     */
    @Override
    public final Object get(final int index, final Scriptable start) {
        if (index >= 0 && index < clientRects_.size()) {
            return clientRects_.get(index);
        }
        return NOT_FOUND;
    }

    /**
     * Returns the item at the specified index.
     * @param index the index
     * @return the found item
     */
    @JsxFunction
    public ClientRect item(final int index) {
        if (index >= 0 && index < clientRects_.size()) {
            return clientRects_.get(index);
        }
        if (getBrowserVersion().hasFeature(JS_CLIENTRECTLIST_THROWS_IF_ITEM_NOT_FOUND)) {
            throw Context.reportRuntimeError("Invalid index '" + index + "'");
        }
        return null;
    }

    /**
     * Add a rect.
     * @param clientRect the rect to add
     */
    public void add(final ClientRect clientRect) {
        clientRects_.add(clientRect);
    }
}
