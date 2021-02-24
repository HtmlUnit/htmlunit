/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.attachment;

import java.io.Serializable;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * <p>A handler for attachments, which represent pages received from the server which contain
 * {@code Content-Disposition=attachment} headers. Normally pages are loaded inline: clicking on
 * a link, for example, loads the linked page in the current window. Attached pages are different
 * in that they are intended to be loaded outside of this flow: clicking on a link prompts the
 * user to either save the linked page, or open it outside of the current window, but does not
 * load the page in the current window.</p>
 *
 * <p>HtmlUnit complies with the semantics described above when an <tt>AttachmentHandler</tt> has
 * been registered with the {@link com.gargoylesoftware.htmlunit.WebClient} via
 * {@link com.gargoylesoftware.htmlunit.WebClient#setAttachmentHandler(AttachmentHandler)}. When
 * no attachment handler has been registered with the <tt>WebClient</tt>, the semantics described
 * above to not apply, and attachments are loaded inline. By default, <tt>AttachmentHandler</tt>s
 * are not registered with new <tt>WebClient</tt> instances.</p>
 *
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Alex Gorbatovsky
 * @see com.gargoylesoftware.htmlunit.WebClient#setAttachmentHandler(AttachmentHandler)
 * @see com.gargoylesoftware.htmlunit.WebClient#getAttachmentHandler()
 * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
 */
public interface AttachmentHandler extends Serializable {

    /**
     * Handles the specified attached page.
     * This method will only be called if {@link #handleAttachment(WebResponse)}
     * has retund false for the response..
     * @param page an attached page, which doesn't get loaded inline
     */
    void handleAttachment(Page page);

    /**
     * Process the specified attachment. If this method returns false,
     * the client will open a new window with a page created from this
     * response as content.
     * Overwrite this method (and return true) if you do not need to create
     * a new window for the response.
     * @param response the response to process
     * @return {@code true} if the specified response represents is handled by this method
     * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
     */
    default boolean handleAttachment(final WebResponse response) {
        return false;
    }

    /**
     * Returns {@code true} if the specified response represents an attachment.
     * @param response the response to check
     * @return {@code true} if the specified response represents an attachment, {@code false} otherwise
     * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
     */
    default boolean isAttachment(final WebResponse response) {
        final String disp = response.getResponseHeaderValue(HttpHeader.CONTENT_DISPOSITION);
        if (disp == null) {
            return false;
        }
        return disp.toLowerCase(Locale.ROOT).startsWith("attachment");
    }
}
