/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Wrapper for content downloaded from a remote server.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public interface DownloadedContent extends Serializable {

    /**
     * Implementation keeping content in memory.
     */
    class InMemory implements DownloadedContent {
        private final byte[] bytes_;

        InMemory(final byte[] byteArray) {
            if (byteArray == null) {
                bytes_ = ArrayUtils.EMPTY_BYTE_ARRAY;
            }
            else {
                bytes_ = byteArray;
            }
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes_);
        }

        @Override
        public void cleanUp() {
            // nothing to do
        }

        @Override
        public boolean isEmpty() {
            return length() == 0;
        }

        @Override
        public long length() {
            return bytes_.length;
        }
    }

    /**
     * Implementation keeping content on the file system.
     */
    class OnFile implements DownloadedContent {
        private final File file_;
        private boolean temporary_;

        /**
         * @param file the file
         * @param temporary if true, the file will be deleted when cleanUp() is called.
         */
        OnFile(final File file, final boolean temporary) {
            file_ = file;
            temporary_ = temporary;
        }

        @Override
        public InputStream getInputStream() throws FileNotFoundException {
            return new FileInputStream(file_);
        }

        @Override
        public void cleanUp() {
            if (temporary_) {
                FileUtils.deleteQuietly(file_);
            }
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            cleanUp();
        }

        @Override
        public long length() {
            if (file_ == null) {
                return 0;
            }
            return file_.length();
        }
    }

    /**
     * Returns a new {@link InputStream} allowing to read the downloaded content.
     * @return the InputStream
     * @throws IOException in case of problem accessing the content
     */
    InputStream getInputStream() throws IOException;

    /**
     * Clean up resources associated to this content.
     */
    void cleanUp();

    /**
     * Returns true if the content is empty.
     * @return true or false
     */
    boolean isEmpty();

    /**
     * Returns the number of bytes.
     * @return the length
     */
    long length();
}
