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
package org.htmlunit.attachment;

import org.htmlunit.HttpHeader;
import org.htmlunit.Page;
import org.htmlunit.WebResponse;
import org.htmlunit.http.ContentDisposition;
import org.htmlunit.http.ContentDispositionParser;

/**
 * An attachment represents a page received from the server which contains a
 * {@code Content-Disposition=attachment} header.
 *
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class Attachment {

    /** The attached page. */
    private final Page page_;

    /** The file name of this attachment. */
    private final String attachmentFilename_;

    /**
     * Creates a new attachment for the specified page.
     * @param page the attached page
     */
    public Attachment(final Page page) {
        this(page, null);
    }

    /**
     * Creates a new attachment for the specified page.
     * @param page the attached page
     * @param attachmentFilename the file name of this attachment
     */
    public Attachment(final Page page, final String attachmentFilename) {
        page_ = page;
        attachmentFilename_ = attachmentFilename;
    }

    /**
     * Returns the attached page.
     * @return the attached page
     */
    public Page getPage() {
        return page_;
    }

    /**
     * Returns the file name for this attachment, the way a real browser would suggest it in
     * its file save dialog (or use it for an automatic download).
     *
     * <p>If a file name was given explicitly when this attachment was created (for example because
     * the download was triggered by an anchor with a {@code download} attribute, or the name was
     * already derived from the response by the {@link org.htmlunit.WebClient}), this name is
     * returned as it is. Otherwise the name is taken from the {@code Content-Disposition} header
     * of the response of the attached page; see {@link #getSuggestedFilename(String)} for the details.</p>
     *
     * <p>Like a browser, this method does <em>not</em> use the URL of the request as fallback.
     * If you need a name in this case, you have to build one on your own (for example from the
     * URL, as the {@link DownloadingAttachmentHandler} does).</p>
     *
     * @return the attachment's suggested filename, or {@code null} if none was suggested
     */
    public String getSuggestedFilename() {
        if (attachmentFilename_ != null) {
            return attachmentFilename_;
        }

        final WebResponse response = page_.getWebResponse();
        final String disp = response.getResponseHeaderValue(HttpHeader.CONTENT_DISPOSITION);
        return getSuggestedFilename(disp);
    }

    /**
     * Returns the file name suggested by the given {@code Content-Disposition} header, or
     * {@code null} if the header does not contain a usable file name.
     *
     * <p>This method does what the real browsers do when they choose the name of a downloaded
     * file, so the result is a name that is safe to use for saving the file, and not the plain value
     * sent by the server:</p>
     * <ul>
     * <li>The header is parsed by the {@link ContentDispositionParser} (RFC 6266, RFC 2183).
     * Parameter names are case-insensitive, the value can be a token or a quoted string
     * (with escapes), and a {@code ;} inside a quoted string does not end the value.</li>
     * <li>The extended parameter {@code filename*} (RFC 8187, e.g. {@code filename*=UTF-8''gr%C3%B6%C3%9Fe.txt})
     * wins over {@code filename}, whatever the order in the header is. Supported charsets are UTF-8 and
     * ISO-8859-1; if the extended value is not usable, the plain {@code filename} is used.</li>
     * <li>Characters that are not allowed in file names on at least one platform
     * ({@code \ / : * ? " < > |} and control characters) are replaced by an underscore. This also
     * makes sure a path in the header can never be used to leave the download directory:
     * {@code ../../evil.txt} becomes {@code _.._evil.txt}.</li>
     * <li>Leading dots and white space (no hidden files) and trailing dots and white space
     * are removed.</li>
     * <li>If nothing is left, for example because the header has no file name or an empty one,
     * the result is {@code null}. A browser would use the last part of the URL in this case, this has to
     * be done by the caller.</li>
     * </ul>
     *
     * <p>Not all browsers behave exactly the same in some corner cases. If the extended value uses an
     * unknown charset or an invalid percent encoding, this method behaves like Chrome and Edge:
     * an unknown charset is ignored and an invalid percent encoding is kept as it is.
     * Firefox uses the extended value even with an unknown charset, but ignores it if the percent
     * encoding is invalid.</p>
     *
     * @param contentDispositionHeader the value of the {@code Content-Disposition} header, can be {@code null}
     * @return the sanitized file name, or {@code null} if none was suggested
     */
    public static String getSuggestedFilename(final String contentDispositionHeader) {
        final ContentDisposition contentDisposition = ContentDispositionParser.parse(contentDispositionHeader);
        if (contentDisposition == null) {
            return null;
        }

        // a filename* that is not usable falls back to filename
        String filename = sanitizeFilename(contentDisposition.getExtendedParameter("filename"));
        if (filename == null) {
            filename = sanitizeFilename(contentDisposition.getParameter("filename"));
        }
        return filename;
    }

    /**
     * Makes the name usable as file name: characters that are not allowed are replaced by '_',
     * leading dots and white space and trailing dots and white space are removed.
     *
     * @param name the name to sanitize
     * @return the sanitized name or {@code null} if nothing is left
     */
    private static String sanitizeFilename(final String name) {
        if (name == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (c < ' ' || c == 127 || "\\/:*?\"<>|".indexOf(c) >= 0) {
                sb.append('_');
            }
            else {
                sb.append(c);
            }
        }

        int start = 0;
        int end = sb.length();
        while (start < end && (sb.charAt(start) == '.' || Character.isWhitespace(sb.charAt(start)))) {
            start++;
        }
        while (end > start && (sb.charAt(end - 1) == '.' || Character.isWhitespace(sb.charAt(end - 1)))) {
            end--;
        }
        return start == end ? null : sb.substring(start, end);
    }
}
