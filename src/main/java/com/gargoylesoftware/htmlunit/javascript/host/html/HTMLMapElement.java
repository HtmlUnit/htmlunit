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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

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
    @JsxConstructor({CHROME, EDGE, FF, FF78})
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
            areas_ = new HTMLCollection(map, false) {
                @Override
                protected List<DomNode> computeElements() {
                    final List<DomNode> list = new ArrayList<>();
                    for (final DomNode node : map.getChildElements()) {
                        if (node instanceof HtmlArea) {
                            list.add(node);
                        }
                    }
                    return list;
                }
            };
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
        return getDomNodeOrDie().getAttributeDirect("name");
    }

    /**
     * Sets the {@code name} attribute.
     * @param name the {@code name} attribute
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

}
