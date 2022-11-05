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

import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;

/**
 * A special {@link NodeList} to implement {@code .labels} property.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 */
class LabelsNodeList extends NodeList {

    /**
     * Creates an instance.
     *
     * @param domeNode the {@link DomNode}
     */
    LabelsNodeList(final DomElement domeNode) {
        super(domeNode, false);
    }

    /**
     * This is overridden instead of {@link #computeElements()} in order to prevent caching at all.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public List<DomNode> getElements() {
        final List<DomNode> response = new ArrayList<>();
        final DomElement domElement = (DomElement) getDomNodeOrDie();
        for (DomNode parent = domElement.getParentNode(); parent != null; parent = parent.getParentNode()) {
            if (parent instanceof HtmlLabel) {
                response.add(parent);
            }
        }
        final String id = domElement.getId();
        if (ATTRIBUTE_NOT_DEFINED != id) {
            for (final DomElement label : domElement.getHtmlPageOrNull().getElementsByTagName("label")) {
                if (id.equals(label.getAttributeDirect("for"))) {
                    response.add(label);
                }
            }
        }

        return response;
    }
}
