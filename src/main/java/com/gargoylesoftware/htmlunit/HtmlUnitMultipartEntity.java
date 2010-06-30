/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.james.mime4j.field.Fields;
import org.apache.james.mime4j.message.Message;

/**
 * A modified version of {@link org.apache.http.entity.mime.MultipartEntity}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@ThreadSafe
class HtmlUnitMultipartEntity implements HttpEntity {

    /**
     * The pool of ASCII chars to be used for generating a multipart boundary.
     */
    private static final char[] MULTIPART_CHARS_ =
        "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        .toCharArray();

    private final Message message_;
    private final HtmlUnitMultipart multipart_;
    private final Header contentType_;

    private long length_;
    private volatile boolean dirty_; // used to decide whether to recalculate length

    HtmlUnitMultipartEntity(
            HttpMultipartMode mode,
            final String boundary,
            final Charset charset) {
        super();
        this.multipart_ = new HtmlUnitMultipart("form-data");
        this.contentType_ = new BasicHeader(
                HTTP.CONTENT_TYPE,
                generateContentType(boundary, charset));
        this.dirty_ = true;

        this.message_ = new Message();
        final org.apache.james.mime4j.message.Header header =
            new org.apache.james.mime4j.message.Header();
        this.message_.setHeader(header);
        this.multipart_.setParent(message_);
        if (mode == null) {
            mode = HttpMultipartMode.STRICT;
        }
        this.multipart_.setMode(mode);
        this.message_.getHeader().addField(Fields.contentType(this.contentType_.getValue()));
    }

    HtmlUnitMultipartEntity(final HttpMultipartMode mode) {
        this(mode, null, null);
    }

    HtmlUnitMultipartEntity() {
        this(HttpMultipartMode.STRICT, null, null);
    }

    /**
     * Generate content type
     * @param boundary the boundary
     * @param charset the charset
     * @return the content type
     */
    protected String generateContentType(
            final String boundary,
            final Charset charset) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("multipart/form-data; boundary=");
        if (boundary != null) {
            buffer.append(boundary);
        }
        else {
            final Random rand = new Random();
            final int count = rand.nextInt(11) + 30; // a random size from 30 to 40
            for (int i = 0; i < count; i++) {
                buffer.append(MULTIPART_CHARS_[rand.nextInt(MULTIPART_CHARS_.length)]);
            }
        }
        if (charset != null) {
            buffer.append("; charset=");
            buffer.append(charset.name());
        }
        return buffer.toString();
    }

    /**
     * Adds a part.
     * @param name the name
     * @param contentBody the body
     */
    public void addPart(final String name, final ContentBody contentBody) {
        this.multipart_.addBodyPart(new FormBodyPart(name, contentBody));
        this.dirty_ = true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isRepeatable() {
        final List<?> parts = this.multipart_.getBodyParts();
        for (final Iterator<?> it = parts.iterator(); it.hasNext();) {
            final FormBodyPart part = (FormBodyPart) it.next();
            final ContentBody body = (ContentBody) part.getBody();
            if (body.getContentLength() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isChunked() {
        return !isRepeatable();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStreaming() {
        return !isRepeatable();
    }

    /**
     * {@inheritDoc}
     */
    public long getContentLength() {
        if (this.dirty_) {
            this.length_ = this.multipart_.getTotalLength();
            this.dirty_ = false;
        }
        return this.length_;
    }

    /**
     * {@inheritDoc}
     */
    public Header getContentType() {
        return this.contentType_;
    }

    /**
     * {@inheritDoc}
     */
    public Header getContentEncoding() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void consumeContent()
        throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException(
            "Multipart form entity does not implement #getContent()");
    }

    /**
     * {@inheritDoc}
     */
    public void writeTo(final OutputStream outstream) throws IOException {
        this.multipart_.writeTo(outstream);
    }

}
