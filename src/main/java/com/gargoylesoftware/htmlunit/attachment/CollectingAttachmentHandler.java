/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;

/**
 * An {@link AttachmentHandler} implementation which creates an {@link Attachment} for
 * each attached page, collecting all created attachments into a list.
 *
 * @version $Revision$
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 */
public class CollectingAttachmentHandler implements AttachmentHandler {

    private final List<Attachment> collectedAttachments_;

    /**
     * Creates a new instance.
     */
    public CollectingAttachmentHandler() {
        this(new ArrayList<Attachment>());
    }

    /**
     * Creates a new instance which collects attachments into the specified list.
     * @param list the list to store attachments in
     */
    public CollectingAttachmentHandler(final List<Attachment> list) {
        WebAssert.notNull("list", list);
        collectedAttachments_ = list;
    }

    /**
     * {@inheritDoc}
     */
    public void handleAttachment(final Page page) {
        collectedAttachments_.add(new Attachment(page));
    }

    /**
     * Returns the list of attachments collected by this attachment handler. The returned
     * list is modifiable, so that attachments can be removed after being processed.
     * @return the list of attachments collected by this attachment handler
     */
    public List<Attachment> getCollectedAttachments() {
        return collectedAttachments_;
    }

}
