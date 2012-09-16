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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.javascript.annotations.JsxGetter;

/**
 * The JavaScript object "HTMLMapElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLMapElement extends HTMLElement {
    private HTMLCollection areas_;

    /**
     * Creates an instance.
     */
    public HTMLMapElement() {
        // Empty.
    }

    /**
     * Returns the value of the JavaScript attribute "areas".
     * @return the value of this attribute
     */
    @JsxGetter
    public HTMLCollection jsxGet_areas() {
        if (areas_ == null) {
            final HtmlMap map = (HtmlMap) getDomNodeOrDie();
            areas_ = new HTMLCollection(map, false, "HTMLMapElement.areas") {
                @Override
                protected List<Object> computeElements() {
                    final List<Object> list = new ArrayList<Object>();
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
}
