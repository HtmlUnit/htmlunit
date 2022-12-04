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
package com.gargoylesoftware.htmlunit.platform.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DeferredNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.util.XmlUtilsHelperAPI;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Special processing if the Xerces parser is in use.
 *
 * @author Ronald Brill
 */
public final class XmlUtilsXercesHelper implements XmlUtilsHelperAPI {

    // private static final Log LOG = LogFactory.getLog(XmlUtilsXerces.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, List<String>> getAttributesOrderMap(final Document document) {
        if (document instanceof DeferredDocumentImpl) {
            final Map<Integer, List<String>> map = new HashMap<>();
            final DeferredDocumentImpl deferredDocument = (DeferredDocumentImpl) document;
            final int fNodeCount = getPrivate(deferredDocument, "fNodeCount");
            for (int i = 0; i < fNodeCount; i++) {
                final int type = deferredDocument.getNodeType(i, false);
                if (type == Node.ELEMENT_NODE) {
                    int attrIndex = deferredDocument.getNodeExtra(i, false);
                    final List<String> attributes = new ArrayList<>();
                    map.put(i, attributes);
                    while (attrIndex != -1) {
                        attributes.add(deferredDocument.getNodeName(attrIndex, false));
                        attrIndex = deferredDocument.getPrevSibling(attrIndex, false);
                    }
                }
            }
            return map;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndex(final NamedNodeMap namedNodeMap, final Map<Integer, List<String>> attributesOrderMap,
            final Node element, final int requiredIndex) {
        if (attributesOrderMap != null && element instanceof DeferredNode) {
            final int elementIndex = ((DeferredNode) element).getNodeIndex();
            final List<String> attributesOrderList = attributesOrderMap.get(elementIndex);
            if (attributesOrderList != null) {
                final String attributeName = attributesOrderList.get(requiredIndex);
                for (int i = 0; i < namedNodeMap.getLength(); i++) {
                    if (namedNodeMap.item(i).getNodeName().equals(attributeName)) {
                        return i;
                    }
                }
            }
            return requiredIndex;
        }
        return -1;
    }
}
