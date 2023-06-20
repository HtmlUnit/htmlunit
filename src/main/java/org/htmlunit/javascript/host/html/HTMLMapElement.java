/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlArea;
import org.htmlunit.html.HtmlMap;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLMapElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlMap.class)
public class HTMLMapElement extends HTMLElement {

    private HTMLCollection areas_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public HTMLMapElement() {
    }

    /**
     * Returns the value of the JavaScript attribute {@code areas}.
     * @return the value of this attribute
     */
    @JsxGetter
    public HTMLCollection getAreas() {
        if (areas_ == null) {
            final HtmlMap map = (HtmlMap) getDomNodeOrDie();
            areas_ = new HTMLCollection(map, false);
            areas_.setElementsSupplier(
                    (Supplier<List<DomNode>> & Serializable)
                    () -> {
                        final List<DomNode> list = new ArrayList<>();
                        for (final DomNode node : map.getChildElements()) {
                            if (node instanceof HtmlArea) {
                                list.add(node);
                            }
                        }
                        return list;
                    });
        }
        return areas_;
    }

    /**
     * Returns the {@code name} attribute.
     * @return the {@code name} attribute
     */
    @JsxGetter
    @Override
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.NAME_ATTRIBUTE);
    }

    /**
     * Sets the {@code name} attribute.
     * @param name the {@code name} attribute
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute(DomElement.NAME_ATTRIBUTE, name);
    }

}
