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
package com.gargoylesoftware.htmlunit.protocol.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.DecoderException;

/**
 * A URLConnection for supporting data URLs.
 * @see <a href="http://www.ietf.org/rfc/rfc2397.txt">RFC2397</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class DataURLConnection extends URLConnection {

    /** The "URL" prefix 'data:'. */
    public static final String DATA_PREFIX = "data:";

    private final DataUrlDecoder dataUrlDecoder_;

    /**
     * Creates an instance.
     * @param url the data URL
     * @throws UnsupportedEncodingException in case the encoding is not supported
     * @throws DecoderException in all other cases
     */
    public DataURLConnection(final URL url) throws UnsupportedEncodingException, DecoderException {
        super(url);
        dataUrlDecoder_ = DataUrlDecoder.decode(url);
    }

    /**
     * This method does nothing in this implementation but is required to be implemented.
     */
    @Override
    public void connect() {
        // Empty.
    }

    /**
     * Returns the input stream - in this case the content of the URL.
     * @return the input stream
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(dataUrlDecoder_.getBytes());
    }

    /**
     * Gets the charset information specified in the data URL.
     * @return "US-ASCII" if the URL didn't contain any charset information
     */
    public String getCharset() {
        return dataUrlDecoder_.getCharset();
    }

    /**
     * Gets the media type information contained in the data URL.
     * @return "text/plain" if the URL didn't contain any media type information
     */
    public String getMediaType() {
        return dataUrlDecoder_.getMediaType();
    }
}
