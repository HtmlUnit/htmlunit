/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility holding information about association between MIME type and file extensions.
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public final class MimeType {

    /** "text/javascript". */
    public static final String TEXT_JAVASCRIPT = "text/javascript";
    /** "application/octet-stream". */
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    /** "application/json". */
    public static final String APPLICATION_JSON = "application/json";
    /** application/xhtml+xml. */
    public static final String APPLICATION_XHTML = "application/xhtml+xml";
    /** application/xml. */
    public static final String APPLICATION_XML = "application/xml";

    /** "text/css". */
    public static final String TEXT_CSS = "text/css";
    /** "text/html". */
    public static final String TEXT_HTML = "text/html";
    /** "text/xml". */
    public static final String TEXT_XML = "text/xml";
    /** "text/plain". */
    public static final String TEXT_PLAIN = "text/plain";

    /** "image/gif". */
    public static final String IMAGE_GIF = "image/gif";
    /** "image/jpeg". */
    public static final String IMAGE_JPEG = "image/jpeg";
    /** "image/png". */
    public static final String IMAGE_PNG = "image/png";

    private static final Map<String, String> type2extension = buildMap();

    /**
     * See <a href="https://www.rfc-editor.org/rfc/rfc9239.html#name-iana-considerations">
     * https://www.rfc-editor.org/rfc/rfc9239.html#name-iana-considerations</a>.
     *
     * @param mimeType the type to check
     * @return true if the mime type is obsolete
     */
    public static boolean isJavascriptMimeType(final String mimeType) {
        if (mimeType == null) {
            return false;
        }
        final String mimeTypeLC = StringUtils.toRootLowerCaseWithCache(mimeType);

        return TEXT_JAVASCRIPT.equals(mimeTypeLC)
                || "application/javascript".equals(mimeTypeLC)
                || "application/x-ecmascript".equals(mimeTypeLC)
                || "application/x-javascript".equals(mimeTypeLC)
                || "text/ecmascript".equals(mimeTypeLC)
                || "application/ecmascript".equals(mimeTypeLC)
                || "text/javascript1.0".equals(mimeTypeLC)
                || "text/javascript1.1".equals(mimeTypeLC)
                || "text/javascript1.2".equals(mimeTypeLC)
                || "text/javascript1.3".equals(mimeTypeLC)
                || "text/javascript1.4".equals(mimeTypeLC)
                || "text/javascript1.5".equals(mimeTypeLC)
                || "text/jscript".equals(mimeTypeLC)
                || "text/livescript".equals(mimeTypeLC)
                || "text/x-ecmascript".equals(mimeTypeLC)
                || "text/x-javascript".equals(mimeTypeLC);
    }

    /**
     * See <a href="https://mimesniff.spec.whatwg.org/#javascript-mime-type">
     * https://mimesniff.spec.whatwg.org/#javascript-mime-type</a>.
     *
     * @param mimeType the type to check
     * @return true if the mime type is for js
     */
    public static boolean isObsoleteJavascriptMimeType(final String mimeType) {
        if (mimeType == null) {
            return false;
        }
        final String mimeTypeLC = StringUtils.toRootLowerCaseWithCache(mimeType);

        return "application/javascript".equals(mimeTypeLC)
                || "application/ecmascript".equals(mimeTypeLC)
                || "application/x-ecmascript".equals(mimeTypeLC)
                || "application/x-javascript".equals(mimeTypeLC)
                || "text/ecmascript".equals(mimeTypeLC)
                || "text/javascript1.0".equals(mimeTypeLC)
                || "text/javascript1.1".equals(mimeTypeLC)
                || "text/javascript1.2".equals(mimeTypeLC)
                || "text/javascript1.3".equals(mimeTypeLC)
                || "text/javascript1.4".equals(mimeTypeLC)
                || "text/javascript1.5".equals(mimeTypeLC)
                || "text/jscript".equals(mimeTypeLC)
                || "text/livescript".equals(mimeTypeLC)
                || "text/x-ecmascript".equals(mimeTypeLC)
                || "text/x-javascript".equals(mimeTypeLC);
    }

    private static Map<String, String> buildMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("application/pdf", "pdf");
        map.put("application/x-javascript", "js");
        map.put("image/gif", "gif");
        map.put("image/jpg", "jpeg");
        map.put("image/jpeg", "jpeg");
        map.put("image/png", "png");
        map.put("image/svg+xml", "svg");
        map.put(TEXT_CSS, "css");
        map.put(MimeType.TEXT_HTML, "html");
        map.put(TEXT_PLAIN, "txt");
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
     * @return {@code null} if none is known
     */
    public static String getFileExtension(final String contentType) {
        final String value = type2extension.get(StringUtils.toRootLowerCaseWithCache(contentType));
        if (value == null) {
            return "unknown";
        }

        return value;
    }
}
