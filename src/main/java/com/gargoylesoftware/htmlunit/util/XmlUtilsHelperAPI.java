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
package com.gargoylesoftware.htmlunit.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * API of the helper.
 *
 * @author Ronald Brill
 */
public interface XmlUtilsHelperAPI {

    /**
     * Returns internal Xerces details about all elements in the specified document.
     * The id of the returned {@link Map} is the {@code nodeIndex} of an element, and the list
     * is the array of ordered attributes names.
     * @param document the document
     * @return the map of an element index with its ordered attribute names
     */
    Map<Integer, List<String>> getAttributesOrderMap(Document document);

    /**
     * Helper.
     *
     * @param namedNodeMap
     * @param attributesOrderMap
     * @param element
     * @param requiredIndex
     * @return the index
     */
    int getIndex(NamedNodeMap namedNodeMap, Map<Integer, List<String>> attributesOrderMap,
            Node element, int requiredIndex);

    @SuppressWarnings("unchecked")
    default <T> T getPrivate(final Object object, final String fieldName) {
        try {
            final Field f = object.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(object);
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
