/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.htmlunit.html.DefaultElementFactory.OrderedFastHashMapWithLowercaseKeys;
import org.htmlunit.util.OrderedFastHashMap;
import org.htmlunit.util.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * The {@link NamedNodeMap} to store the node attributes.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
class NamedAttrNodeMapImpl implements Map<String, DomAttr>, NamedNodeMap, Serializable {
    private final OrderedFastHashMap<String, DomAttr> map_;
    private final DomElement domNode_;
    private final boolean caseSensitive_;

    NamedAttrNodeMapImpl(final DomElement domNode, final boolean caseSensitive) {
        super();
        if (domNode == null) {
            throw new IllegalArgumentException("Provided domNode can't be null.");
        }
        domNode_ = domNode;
        caseSensitive_ = caseSensitive;
        map_ = new OrderedFastHashMap<>(0);
    }

    NamedAttrNodeMapImpl(final DomElement domNode, final boolean caseSensitive,
            final Map<String, DomAttr> attributes) {
        super();
        if (domNode == null) {
            throw new IllegalArgumentException("Provided domNode can't be null.");
        }
        domNode_ = domNode;
        caseSensitive_ = caseSensitive;

        if (attributes instanceof OrderedFastHashMapWithLowercaseKeys) {
            // no need to rework the map at all, we are case sensitive, so
            // we keep all attributes, and we got the right map from outside too
            map_ = (OrderedFastHashMap) attributes;
        }
        else if (caseSensitive && attributes instanceof OrderedFastHashMap map) {
            // no need to rework the map at all, we are case sensitive, so
            // we keep all attributes, and we got the right map from outside too
            map_ = map;
        }
        else {
            // this is more expensive but atypical, so we don't have to care that much
            map_ = new OrderedFastHashMap<>(attributes.size());
            // this will create a new map with all case lowercased and
            putAll(attributes);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr getNamedItem(final String name) {
        return get(name);
    }

    private String fixName(final String name) {
        if (caseSensitive_) {
            return name;
        }
        return StringUtils.toRootLowerCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNamedItemNS(final String namespaceURI, final String localName) {
        if (domNode_ == null) {
            return null;
        }
        return get(domNode_.getQualifiedName(namespaceURI, fixName(localName)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node item(final int index) {
        if (index < 0 || index >= map_.size()) {
            return null;
        }
        return map_.getValue(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node removeNamedItem(final String name) throws DOMException {
        return remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node removeNamedItemNS(final String namespaceURI, final String localName) {
        if (domNode_ == null) {
            return null;
        }
        return remove(domNode_.getQualifiedName(namespaceURI, fixName(localName)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr setNamedItem(final Node node) {
        return put(node.getLocalName(), (DomAttr) node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node setNamedItemNS(final Node node) throws DOMException {
        return put(node.getNodeName(), (DomAttr) node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr put(final String key, final DomAttr value) {
        final String name = fixName(key);
        return map_.put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr remove(final Object key) {
        if (key instanceof String string) {
            return map_.remove(fixName(string));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        map_.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(final Map<? extends String, ? extends DomAttr> t) {
        // add one after the other to save the positions
        for (final Map.Entry<? extends String, ? extends DomAttr> entry : t.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final Object key) {
        if (key instanceof String string) {
            final String name = fixName(string);
            return map_.containsKey(name);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomAttr get(final Object key) {
        if (key instanceof String string) {
            return map_.get(fixName(string));
        }
        return null;
    }

    /**
     * Fast access.
     * @param key the key
     */
    protected DomAttr getDirect(final String key) {
        return map_.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(final Object value) {
        return map_.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Map.Entry<String, DomAttr>> entrySet() {
        return map_.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return map_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keySet() {
        return map_.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return map_.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DomAttr> values() {
        return map_.values();
    }
}
