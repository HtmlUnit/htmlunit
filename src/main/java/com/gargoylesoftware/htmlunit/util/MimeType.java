/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility holding information about association between MIME type and file extensions.
 * @version $Revision$
 * @author Marc Guillemot
 */
public final class MimeType {
    private static final Map<String, String> type2extension = buildMap();

    private static Map<String, String> buildMap() {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("application/pdf", "pdf");
        map.put("application/x-javascript", "js");
        map.put("image/gif", "gif");
        map.put("image/jpg", "jpeg");
        map.put("image/jpeg", "jpeg");
        map.put("image/png", "png");
        map.put("image/svg+xml", "svg");
        map.put("text/css", "css");
        map.put("text/html", "html");
        map.put("text/plain", "txt");
        map.put("image/x-icon", "ico");
        return map;
    }

    /**
     * Disallow instantiation of this class.
     */
    private MimeType() {
        // Empty.
    }

    /**
     * Gets the preferred file extension for a content type.
     * @param contentType the mime type
     * @return <code>null</code> if none is known
     */
    public static String getFileExtension(final String contentType) {
        final String value = type2extension.get(contentType.toLowerCase());
        if (value == null) {
            return "unknown";
        }

        return value;
    }
}
