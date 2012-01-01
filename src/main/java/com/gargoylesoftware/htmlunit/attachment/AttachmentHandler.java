/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.attachment;

import com.gargoylesoftware.htmlunit.Page;

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
 * are not registered with new <tt>WebClient</tt> instances, in order to maintain backwards
 * compatibility with HtmlUnit 2.1 and earlier. This will likely change in the future.</p>
 *
 * @version $Revision$
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @see com.gargoylesoftware.htmlunit.WebClient#setAttachmentHandler(AttachmentHandler)
 * @see com.gargoylesoftware.htmlunit.WebClient#getAttachmentHandler()
 * @see <a href="http://www.ietf.org/rfc/rfc2183.txt">RFC 2183</a>
 */
public interface AttachmentHandler {

    /**
     * Handles the specified attached page.
     * @param page an attached page, which doesn't get loaded inline
     */
    void handleAttachment(final Page page);

}
