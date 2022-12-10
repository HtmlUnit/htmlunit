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
package com.gargoylesoftware.htmlunit.platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Singleton to handle JDK specific stuff.
 * This is required to support at least the differences with android.
 *
 * @author Ronald Brill
 */
public final class Platform {

    private static final Log LOG = LogFactory.getLog(Platform.class);

    private static XmlUtilsHelperAPI HelperXerces_;
    private static XmlUtilsHelperAPI HelperSunXerces_;

    static {
        try {
            HelperSunXerces_ = (XmlUtilsHelperAPI)
                    Class.forName("com.gargoylesoftware.htmlunit.platform.util.XmlUtilsSunXercesHelper").newInstance();
        }
        catch (final Exception e) {
            // ignore
        }

        try {
            HelperXerces_ = (XmlUtilsHelperAPI)
                    Class.forName("com.gargoylesoftware.htmlunit.platform.util.XmlUtilsXercesHelper").newInstance();
        }
        catch (final Exception e2) {
            // ignore
        }
    }

    /**
     * Forward the call to the correct helper.
     *
     * @param namedNodeMap the node map
     * @param attributesOrderMap the order map
     * @param element the node
     * @param requiredIndex the required index
     * @return the index or requiredIndex
     */
    public static int getIndex(final NamedNodeMap namedNodeMap, final Map<Integer, List<String>> attributesOrderMap,
            final Node element, final int requiredIndex) {
        if (HelperXerces_ != null) {
            final int result = HelperXerces_.getIndex(namedNodeMap, attributesOrderMap, element, requiredIndex);
            if (result != -1) {
                return result;
            }
        }
        if (HelperSunXerces_ != null) {
            final int result = HelperSunXerces_.getIndex(namedNodeMap, attributesOrderMap, element, requiredIndex);
            if (result != -1) {
                return result;
            }
        }

        return requiredIndex;
    }

    /**
     * Returns internal Xerces details about all elements in the specified document.
     * The id of the returned {@link Map} is the {@code nodeIndex} of an element, and the list
     * is the array of ordered attributes names.
     * @param document the document
     * @return the map of an element index with its ordered attribute names
     */
    public static Map<Integer, List<String>> getAttributesOrderMap(final Document document) {
        if (HelperXerces_ != null) {
            final Map<Integer, List<String>> result = HelperXerces_.getAttributesOrderMap(document);
            if (result != null) {
                return result;
            }
        }
        if (HelperSunXerces_ != null) {
            final Map<Integer, List<String>> result = HelperSunXerces_.getAttributesOrderMap(document);
            if (result != null) {
                return result;
            }
        }

        return new HashMap<Integer, List<String>>();
    }

    private Platform() {
    }
}
