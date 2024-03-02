/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code ClientRectList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(className = "DOMRectList", value = {CHROME, EDGE, FF, FF_ESR})
public class ClientRectList extends HtmlUnitScriptable {

    private List<ClientRect> clientRects_;

    /**
     * Creates an instance.
     */
    public ClientRectList() {
        clientRects_ = new ArrayList<>();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
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
