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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.protocol.HTTP;
import org.apache.james.mime4j.field.ContentTypeField;
import org.apache.james.mime4j.field.FieldName;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Header;
import org.apache.james.mime4j.message.MessageWriter;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.CharsetUtil;

/**
 * A modified version of {@link org.apache.http.entity.mime.HttpMultipart}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@NotThreadSafe // parent is @NotThreadSafe
class HtmlUnitMultipart extends Multipart {

    private static ByteArrayBuffer encode(final Charset charset, final String string) {
        final ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        final ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
        bab.append(encoded.array(), encoded.position(), encoded.remaining());
        return bab;
    }

    private static void writeBytes(final ByteArrayBuffer b, final OutputStream out) throws IOException {
        out.write(b.buffer(), 0, b.length());
    }

    private static void writeBytes(final ByteSequence b, final OutputStream out) throws IOException {
        if (b instanceof ByteArrayBuffer) {
            writeBytes((ByteArrayBuffer) b, out);
        }
        else {
            out.write(b.toByteArray());
        }
    }

    private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
    private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");

    private HttpMultipartMode mode_;

    HtmlUnitMultipart(final String subType) {
        super(subType);
        this.mode_ = HttpMultipartMode.STRICT;
    }

    HttpMultipartMode getMode() {
        return this.mode_;
    }

    /**
     * Sets the mode.
     * @param mode the mode
     */
    public void setMode(final HttpMultipartMode mode) {
        this.mode_ = mode;
    }

    /**
     * Returns the charset.
     * @return the charset
     */
    protected Charset getCharset() {
        final Entity e = getParent();
        final ContentTypeField cField = (ContentTypeField) e.getHeader().getField(
                FieldName.CONTENT_TYPE);
        Charset charset = null;

        switch (this.mode_) {
            case STRICT:
                charset = MIME.DEFAULT_CHARSET;
                break;
            case BROWSER_COMPATIBLE:
                if (cField.getCharset() != null) {
                    charset = CharsetUtil.getCharset(cField.getCharset());
                }
                else {
                    charset = CharsetUtil.getCharset(HTTP.DEFAULT_CONTENT_CHARSET);
                }
                break;
            default:
        }
        return charset;
    }

    /**
     * Returns the boundary
     * @return the boundary
     */
    protected String getBoundary() {
        final Entity e = getParent();
        final ContentTypeField cField = (ContentTypeField) e.getHeader().getField(
                FieldName.CONTENT_TYPE);
        return cField.getBoundary();
    }

    private void doWriteTo(
            final HttpMultipartMode mode,
            final OutputStream out,
            final boolean writeContent) throws IOException {

        final List<BodyPart> bodyParts = getBodyParts();
        final Charset charset = getCharset();

        final ByteArrayBuffer boundary = encode(charset, getBoundary());

        switch (mode) {
            case STRICT:
                final String preamble = getPreamble();
                if (preamble != null && preamble.length() != 0) {
                    final ByteArrayBuffer b = encode(charset, preamble);
                    writeBytes(b, out);
                    writeBytes(CR_LF, out);
                }

                for (int i = 0; i < bodyParts.size(); i++) {
                    writeBytes(TWO_DASHES, out);
                    writeBytes(boundary, out);
                    writeBytes(CR_LF, out);

                    final BodyPart part = bodyParts.get(i);
                    final Header header = part.getHeader();

                    final List<Field> fields = header.getFields();
                    for (final Field field : fields) {
                        writeBytes(field.getRaw(), out);
                        writeBytes(CR_LF, out);
                    }
                    writeBytes(CR_LF, out);
                    if (writeContent) {
                        MessageWriter.DEFAULT.writeBody(part.getBody(), out);
                    }
                    writeBytes(CR_LF, out);
                }
                writeBytes(TWO_DASHES, out);
                writeBytes(boundary, out);
                writeBytes(TWO_DASHES, out);
                writeBytes(CR_LF, out);
                final String epilogue = getEpilogue();
                if (epilogue != null && epilogue.length() != 0) {
                    final ByteArrayBuffer b = encode(charset, epilogue);
                    writeBytes(b, out);
                    writeBytes(CR_LF, out);
                }
                break;
            case BROWSER_COMPATIBLE:
                for (int i = 0; i < bodyParts.size(); i++) {
                    writeBytes(TWO_DASHES, out);
                    writeBytes(boundary, out);
                    writeBytes(CR_LF, out);
                    final BodyPart part = bodyParts.get(i);

                    final Field cd = part.getHeader().getField(MIME.CONTENT_DISPOSITION);

                    final StringBuilder s = new StringBuilder();
                    s.append(cd.getName());
                    s.append(": ");
                    s.append(cd.getBody());
                    writeBytes(encode(charset, s.toString()), out);
                    writeBytes(CR_LF, out);
                    if (part instanceof FormBodyPart) {
                        final Field contentField = part.getHeader().getField(MIME.CONTENT_TYPE);
                        s.setLength(0);
                        s.append(contentField.getName());
                        s.append(": ");
                        s.append(contentField.getBody());
                        writeBytes(encode(charset, s.toString()), out);
                        writeBytes(CR_LF, out);
                        writeBytes(CR_LF, out);
                    }
                    else {
                        writeBytes(CR_LF, out);
                    }
                    if (writeContent) {
                        MessageWriter.DEFAULT.writeBody(part.getBody(), out);
                    }
                    writeBytes(CR_LF, out);
                }

                writeBytes(TWO_DASHES, out);
                writeBytes(boundary, out);
                writeBytes(TWO_DASHES, out);
                writeBytes(CR_LF, out);
                break;
            default:
        }
    }

    /**
     * Writes out the content in the multipart/form encoding. This method
     * produces slightly different formatting depending on its compatibility
     * mode.
     *
     * @see #getMode()
     */
    public void writeTo(final OutputStream out) throws IOException {
        doWriteTo(this.mode_, out, true);
    }

    /**
     * Determines the total length of the multipart content (content length of
     * individual parts plus that of extra elements required to delimit the parts
     * from one another). If any of the @{link BodyPart}s contained in this object
     * is of a streaming entity of unknown length the total length is also unknown.
     * <p/>
     * This method buffers only a small amount of data in order to determine the
     * total length of the entire entity. The content of individual parts is not
     * buffered.
     *
     * @return total length of the multipart entity if known, <code>-1</code>
     *   otherwise.
     */
    public long getTotalLength() {
        final List<?> bodyParts = getBodyParts();

        long contentLen = 0;
        for (int i = 0; i < bodyParts.size(); i++) {
            final BodyPart part = (BodyPart) bodyParts.get(i);
            final Body body = part.getBody();
            if (body instanceof ContentBody) {
                final long len = ((ContentBody) body).getContentLength();
                if (len >= 0) {
                    contentLen += len;
                }
                else {
                    return -1;
                }
            }
            else {
                return -1;
            }
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            doWriteTo(this.mode_, out, false);
            final byte[] extra = out.toByteArray();
            return contentLen + extra.length;
        }
        catch (final IOException ex) {
            // Should never happen
            return -1;
        }
    }

}
