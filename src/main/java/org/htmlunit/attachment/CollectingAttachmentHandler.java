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
package org.htmlunit.attachment;

import java.util.ArrayList;
import java.util.List;

import org.htmlunit.Page;
import org.htmlunit.WebAssert;

/**
 * An {@link AttachmentHandler} implementation which creates an {@link Attachment} for
 * each attached page, collecting all created attachments into a list.
 *
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class CollectingAttachmentHandler implements AttachmentHandler {

    private final List<Attachment> collectedAttachments_;

    /**
     * Creates a new instance.
     */
    public CollectingAttachmentHandler() {
        this(new ArrayList<>());
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
    @Override
    public void handleAttachment(final Page page, final String attachmentFilename) {
        collectedAttachments_.add(new Attachment(page, attachmentFilename));
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
