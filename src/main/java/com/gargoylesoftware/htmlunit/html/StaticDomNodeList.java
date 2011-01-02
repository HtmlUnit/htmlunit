/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.AbstractList;
import java.util.List;

import org.w3c.dom.Node;

/**
 * An implementation of DomNodeList that is static.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class StaticDomNodeList extends AbstractList<DomNode> implements DomNodeList<DomNode> {

    private List<DomNode> elements_;

    public StaticDomNodeList(final List<DomNode> elements) {
        elements_ = elements;
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        return elements_.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return getLength();
    }

    /**
     * {@inheritDoc}
     */
    public Node item(final int index) {
        return get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode get(final int index) {
        return elements_.get(index);
    }

}
