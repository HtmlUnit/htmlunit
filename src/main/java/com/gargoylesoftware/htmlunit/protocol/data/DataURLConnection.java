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
package com.gargoylesoftware.htmlunit.protocol.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A URLConnection for supporting data URLs.
 * @see <a href="http://www.ietf.org/rfc/rfc2397.txt">RFC2397</a>
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DataURLConnection extends URLConnection {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(DataURLConnection.class);

    /** The JavaScript "URL" prefix. */
    public static final String DATA_PREFIX = "data:";

    /** The JavaScript code. */
    private final byte[] content_;

    /**
     * Creates an instance.
     * @param url the data URL
     */
    public DataURLConnection(final URL url) {
        super(url);

        byte[] data = null;
        try {
            data = DataUrlDecoder.decode(url).getBytes();
        }
        catch (final UnsupportedEncodingException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Exception decoding " + url, e);
            }
        }
        catch (final DecoderException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Exception decoding " + url, e);
            }
        }
        content_ = data;
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
        return new ByteArrayInputStream(content_);
    }

}
