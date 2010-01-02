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
package com.gargoylesoftware.htmlunit.util;

import java.io.File;

/**
 * A holder for a key/value pair that represents a file to upload.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author David D. Kilzer
 * @author Mike Bowler
 */
public class KeyDataPair extends NameValuePair {

    private static final long serialVersionUID = -1129314696176851675L;

    private final File fileObject_;
    private final String contentType_;
    private final String charset_;
    private byte[] data_;

    /**
     * Creates an instance.
     *
     * @param key the key
     * @param file the file
     * @param contentType the content type
     * @param charset the charset encoding
     */
    public KeyDataPair(final String key, final File file, final String contentType,
            final String charset) {

        super(key, file.getName());

        if (file.exists()) {
            fileObject_ = file;
        }
        else {
            fileObject_ = null;
        }

        contentType_ = contentType;
        charset_ = charset;
    }

    /**
     * @return the {@link File} object if the file exists, else <tt>null</tt>
     */
    public File getFile() {
        return fileObject_;
    }

    /**
     * Gets the charset encoding for this file upload.
     * @return the charset
     */
    public String getCharset() {
        return charset_;
    }

    /**
     * Gets the content type for this file upload.
     * @return the content type
     */
    public String getContentType() {
        return contentType_;
    }

    /**
     * Gets in-memory data assigned to file value.
     * @return <code>null</code> if the file content should be used.
     */
    public byte[] getData() {
        return data_;
    }

    /**
     * Sets file value data. If nothing is set, the file content will be used.
     * @param data byte array with file data.
     */
    public void setData(final byte[] data) {
        data_ = data;
    }
}
