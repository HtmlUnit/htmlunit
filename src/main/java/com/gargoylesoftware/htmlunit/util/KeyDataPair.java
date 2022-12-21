/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * A holder for a key/value pair that represents a file to upload.
 *
 * @author Brad Clarke
 * @author David D. Kilzer
 * @author Mike Bowler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Michael Lueck
 */
public class KeyDataPair extends NameValuePair {

    private final File fileObject_;
    private final String fileName_;
    private final String mimeType_;
    private transient Charset charset_;
    private byte[] data_;

    /**
     * Creates an instance.
     *
     * @param key the key
     * @param file the file
     * @param fileName the name of the file
     * @param mimeType the MIME type
     * @param charset the charset encoding
     */
    public KeyDataPair(final String key, final File file, final String fileName,
            final String mimeType, final String charset) {
        this(key, file, fileName, mimeType, Charset.forName(charset));
    }

    /**
     * Creates an instance.
     *
     * @param key the key
     * @param file the file
     * @param fileName the name of the file
     * @param mimeType the MIME type
     * @param charset the charset encoding
     */
    public KeyDataPair(final String key, final File file, final String fileName,
            final String mimeType, final Charset charset) {

        super(key, (file == null) ? "" : file.getName());

        if (file != null && file.exists()) {
            fileObject_ = file;
        }
        else {
            fileObject_ = null;
        }
        fileName_ = fileName;

        mimeType_ = mimeType;
        charset_ = charset;
    }

    /**
     * private copy constructor
     *
     * @param name will passed as name to the super constructor
     * @param value will be passed as value to the super constructor
     * @param file the file, may be null
     * @param fileName, the filename, may be null
     * @param mimeType, the mimetype, may be null
     * @param charset, the charset, may be null
     */
    private KeyDataPair(final String name, final String value, final File file,
              final String fileName, final String mimeType, final Charset charset,
              final byte[] data) {
        super(name, value);

        fileObject_ = file;
        fileName_ = fileName;

        mimeType_ = mimeType;
        charset_ = charset;

        data_ = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        // this is overwritten to make FindBugs happy
        // and to make it clear, that we really want to have
        // the same equals semantic like our parent class
        return super.equals(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // this is overwritten to make FindBugs happy
        // and to make it clear, that we really want to have
        // the same hashCode like our parent class
        return super.hashCode();
    }

    /**
     * @return the {@link File} object if the file exists, else {@code null}
     */
    public File getFile() {
        return fileObject_;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName_;
    }

    /**
     * Gets the charset encoding for this file upload.
     * @return the charset
     */
    public Charset getCharset() {
        return charset_;
    }

    /**
     * Gets the MIME type for this file upload.
     * @return the MIME type
     */
    public String getMimeType() {
        return mimeType_;
    }

    /**
     * Gets in-memory data assigned to file value.
     * @return {@code null} if the file content should be used.
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

    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(charset_ == null ? null : charset_.name());
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        final String charsetName = (String) ois.readObject();
        if (charsetName != null) {
            charset_ = Charset.forName(charsetName);
        }
    }

    /**
     * {@inheritDoc}
     *
     * specialization of inherited method which will copy all fields
     * and make sure that the value in the base class is not null, by calling
     * the constructor with the current value
     */
    @Override
    public KeyDataPair normalized() {
        return new KeyDataPair(
            this.getName(),
            this.getValue(),
            this.fileObject_,
            this.fileName_,
            this.mimeType_,
            this.charset_,
            this.data_);
    }
}
