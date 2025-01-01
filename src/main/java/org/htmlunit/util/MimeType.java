/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.util.Locale;

import org.htmlunit.cyberneko.util.FastHashMap;

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

    private static final FastHashMap<String, String> TYPE2EXTENSION = buildMap();

    /**
     * A map to avoid lowercase conversion and a check check if this is one of
     * our mimetype we know. The value is not used.
     */
    private static final FastHashMap<String, Boolean> LOOKUP_MAP = new FastHashMap<>(2 * 16 + 1, 0.7f);

    static {
        LOOKUP_MAP.put("application/javascript", true);
        LOOKUP_MAP.put("application/x-ecmascript", true);
        LOOKUP_MAP.put("application/x-javascript", true);
        LOOKUP_MAP.put("text/ecmascript", true);
        LOOKUP_MAP.put("application/ecmascript", true);
        LOOKUP_MAP.put("text/javascript1.0", true);
        LOOKUP_MAP.put("text/javascript1.1", true);
        LOOKUP_MAP.put("text/javascript1.2", true);
        LOOKUP_MAP.put("text/javascript1.3", true);
        LOOKUP_MAP.put("text/javascript1.4", true);
        LOOKUP_MAP.put("text/javascript1.5", true);
        LOOKUP_MAP.put("text/jscript", true);
        LOOKUP_MAP.put("text/livescript", true);
        LOOKUP_MAP.put("text/x-ecmascript", true);
        LOOKUP_MAP.put("text/x-javascript", true);

        // have uppercase ready too, keys() is safe for
        // concurrent modification
        for (final String k : LOOKUP_MAP.keys()) {
            LOOKUP_MAP.put(k.toUpperCase(Locale.ROOT), true);
        }
    }

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
        final String mimeTypeLC = StringUtils.toRootLowerCase(mimeType);

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

        // go a cheap route first
        if (LOOKUP_MAP.get(mimeType) != null) {
            return true;
        }

        // this is our fallback in case we have not found the usual casing
        // our target is ASCII, we can lowercase the normal way because
        // matching some languages with strange rules does not matter, cheaper!
        final String mimeTypeLC = mimeType.toLowerCase(Locale.ROOT);

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

    private static FastHashMap<String, String> buildMap() {
        final FastHashMap<String, String> map = new FastHashMap<>(2 * 11 + 1, 0.7f);
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

        // have uppercase ready too, keys() is safe for
        // concurrent modification
        for (final String k : map.keys()) {
            map.put(k.toUpperCase(Locale.ROOT), map.get(k));
        }
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
        if (contentType == null) {
            return "unknown";
        }

        String value = TYPE2EXTENSION.get(contentType);
        if (value == null) {
            // fallback
            final String uppercased = contentType.toLowerCase(Locale.ROOT);
            value = TYPE2EXTENSION.get(uppercased);
        }

        return value == null ? "unknown" : value;
    }
}
