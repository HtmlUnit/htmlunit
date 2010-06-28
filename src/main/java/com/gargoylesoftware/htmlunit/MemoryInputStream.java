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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

/**
 * An {@link InputStream} wrapper which stores initial part in memory, then uses the underlying InputStream
 * for big content.
 *
 * The main usage is for {@link BinaryPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class MemoryInputStream extends FilterInputStream {

    private byte[] data_;
    private int index_;
    private boolean isBinary_;

    /**
     * Creates a <code>MemoryInputStream</code>.
     *
     * @param in the underlying input stream
     * @param maxSize the maximum size allocated in memory
     */
    protected MemoryInputStream(final InputStream in, final int maxSize) throws IOException {
        super(in);
        if (in != null) {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(in, out, maxSize);
            data_ = out.toByteArray();
            for (int i = data_.length - 1; i >= 0; i--) {
                final int b = data_[i] & 0xFF;
                if (b < ' ' && b != '\r' && b != '\n' && b != '\t') {
                    isBinary_ = true;
                    break;
                }
            }
        }
    }

    /**
     * @return whether all data is read or still more remains
     */
    static boolean copy(final InputStream input, final OutputStream output, final int max) throws IOException {
        final byte[] buffer = new byte[1024 * 4];
        int count = 0;
        int n = 0;
        int toRead = Math.min(buffer.length, max - count);
        while ((n = input.read(buffer, 0, toRead)) != -1) {
            output.write(buffer, 0, n);
            count += n;
            if (count >= max) {
                return false;
            }
            toRead = Math.min(toRead, max - count);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        if (data_ == null) {
            return -1;
        }
        if (index_ < data_.length) {
            return data_[index_++] & 0xFF;
        }
        return super.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(final byte b[], final int off, final int len) throws IOException {
        if (data_ == null) {
            return -1;
        }
        if (b == null) {
            throw new NullPointerException();
        }
        else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        else if (len == 0) {
            return 0;
        }
        if (index_ < data_.length) {
            int c = read();
            if (c == -1) {
                return -1;
            }
            b[off] = (byte) c;

            int i = 1;
            try {
                for ( ; i < len; i++) {
                    c = read();
                    if (c == -1) {
                        break;
                    }
                    b[off + i] = (byte) c;
                }
            }
            catch (final IOException e) {
                //empty
            }
            return i;
        }
        return super.read(b, off, len);
    }

    public boolean isBinary() {
        return isBinary_;
    }

    byte[] getData() {
        return data_;
    }
}
