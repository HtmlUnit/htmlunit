/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple base class for {@link WebResponse}.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class WebResponseImpl implements WebResponse, Serializable {

    private static final long serialVersionUID = 2842434739251092348L;
    private static final Pattern patternEncoding_ = Pattern.compile("[a-zA-Z0-9][\\w:\\.-]*");

    private final transient Log log_ = LogFactory.getLog(WebResponseImpl.class);
    private long loadTime_;
    private WebResponseData responseData_;
    private WebRequestSettings requestSettings_;

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param url               Where this response came from
     * @param requestMethod     the method used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponseImpl(final WebResponseData responseData, final URL url,
            final HttpMethod requestMethod, final long loadTime) {
        this(responseData, new WebRequestSettings(url, requestMethod), loadTime);
    }

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param charset           Charset used if not returned in the response
     * @param requestSettings   the request settings used to get this response
     * @param loadTime          How long the response took to be sent
     * @deprecated As of 2.6, please use @link {@link #WebResponseImpl(WebResponseData, WebRequestSettings, long)}
     */
    @Deprecated
    public WebResponseImpl(final WebResponseData responseData, final String charset,
            final WebRequestSettings requestSettings, final long loadTime) {
        this(responseData, requestSettings, loadTime);
    }

    /**
     * Constructs with all data.
     *
     * @param responseData      Data that was send back
     * @param requestSettings   the request settings used to get this response
     * @param loadTime          How long the response took to be sent
     */
    public WebResponseImpl(final WebResponseData responseData,
            final WebRequestSettings requestSettings, final long loadTime) {
        responseData_ = responseData;
        requestSettings_ = requestSettings;
        loadTime_ = loadTime;
    }

    /**
     * {@inheritDoc}
     */
    public WebRequestSettings getRequestSettings() {
        return requestSettings_;
    }

    /**
     * {@inheritDoc}
     */
    public HttpMethod getRequestMethod() {
        return getRequestSettings().getHttpMethod();
    }

    /**
     * {@inheritDoc}
     */
    public URL getRequestUrl() {
        return getRequestSettings().getUrl();
    }

    /**
     * {@inheritDoc}
     */
    public List<NameValuePair> getResponseHeaders() {
        return responseData_.getResponseHeaders();
    }

    /**
     * {@inheritDoc}
     */
    public String getResponseHeaderValue(final String headerName) {
        for (final NameValuePair pair : responseData_.getResponseHeaders()) {
            if (pair.getName().equalsIgnoreCase(headerName)) {
                return pair.getValue();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getStatusCode() {
        return responseData_.getStatusCode();
    }

    /**
     * {@inheritDoc}
     */
    public String getStatusMessage() {
        return responseData_.getStatusMessage();
    }

    /**
     * {@inheritDoc}
     */
    public String getContentType() {
        final String contentTypeHeader = getResponseHeaderValue("content-type");
        if (contentTypeHeader == null) {
            // Not technically legal but some servers don't return a content-type
            return "";
        }
        final int index = contentTypeHeader.indexOf(';');
        if (index == -1) {
            return contentTypeHeader;
        }
        return contentTypeHeader.substring(0, index);
    }

    /**
     * {@inheritDoc}
     * If no charset is specified in headers, then try to guess it from the content.
     * @see <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Wikipedia - Byte Order Mark</a>
     * @return the charset, {@link TextUtil#DEFAULT_CHARSET} if it can't be determined
     * @deprecated As of 2.6, please use @link {@link #getContentCharset()}
     */
    @Deprecated
    public String getContentCharSet() {
        return getContentCharset();
    }

    /**
     * {@inheritDoc}
     * @see <a href="http://en.wikipedia.org/wiki/Byte_Order_Mark">Wikipedia - Byte Order Mark</a>
     */
    public String getContentCharsetOrNull() {
        final String contentTypeHeader = getResponseHeaderValue("content-type");
        String charset = StringUtils.substringAfter(contentTypeHeader, "charset=");
        if (StringUtils.isEmpty(charset)) {
            log_.debug("No charset specified in header, trying to guess it from content");
            final byte[] body = responseData_.getBody();
            final byte[] markerUTF8 = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
            final byte[] markerUTF16BE = {(byte) 0xfe, (byte) 0xff};
            final byte[] markerUTF16LE = {(byte) 0xff, (byte) 0xfe};
            if (body != null && ArrayUtils.isEquals(markerUTF8, ArrayUtils.subarray(body, 0, 3))) {
                log_.debug("UTF-8 marker found");
                charset = "UTF-8";
            }
            else if (body != null && ArrayUtils.isEquals(markerUTF16BE, ArrayUtils.subarray(body, 0, 2))) {
                log_.debug("UTF-16BE marker found");
                charset = "UTF-16BE";
            }
            else if (body != null && ArrayUtils.isEquals(markerUTF16LE, ArrayUtils.subarray(body, 0, 2))) {
                log_.debug("UTF-16LE marker found");
                charset = "UTF-16LE";
            }
            else {
                final String xmlEncoding = getXMLEncoding(body);
                if (xmlEncoding != null) {
                    log_.debug("XML encoding found: " + xmlEncoding);
                    charset = xmlEncoding;
                }
                else {
                    log_.debug("No charset guessed");
                    charset = null;
                }
            }
        }
        else {
            final Matcher matcher = patternEncoding_.matcher(charset);
            if (!matcher.find()) {
                charset = TextUtil.DEFAULT_CHARSET;
            }
            else {
                // TODO: notify incorrectness if !charset.equals(matcher.group())
                charset = matcher.group();
                try {
                    if (!Charset.isSupported(charset)) {
                        log_.info("Unsupported charset: " + charset
                            + ". Using default one: " + TextUtil.DEFAULT_CHARSET);
                        charset = TextUtil.DEFAULT_CHARSET;
                    }
                }
                catch (final IllegalCharsetNameException e) {
                    log_.info("Illegal charset: " + charset + ". Using default one: " + TextUtil.DEFAULT_CHARSET);
                    charset = TextUtil.DEFAULT_CHARSET;
                }
            }
        }
        return charset;
    }

    /**
     * {@inheritDoc}
     */
    public String getContentCharset() {
        String charset = getContentCharsetOrNull();
        if (charset == null) {
            charset = getRequestSettings().getCharset();
        }
        if (charset == null) {
            charset = TextUtil.DEFAULT_CHARSET;
        }
        return charset;
    }

    /**
     * {@inheritDoc}
     */
    public String getContentAsString() {
        return getContentAsString(getContentCharset());
    }

    /**
     * {@inheritDoc}
     */
    public String getContentAsString(final String encoding) {
        final byte[] body = responseData_.getBody();
        if (body != null) {
            try {
                return new String(body, encoding);
            }
            catch (final UnsupportedEncodingException e) {
                log_.warn("Attempted to use unsupported encoding '" + encoding + "'; using default system encoding.");
                return new String(body);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getContentAsStream() throws IOException {
        final byte[] body = responseData_.getBody();
        if (body != null) {
            return new ByteArrayInputStream(body);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getContentAsBytes() {
        return responseData_.getBody();
    }

    /**
     * {@inheritDoc}
     */
    public long getLoadTime() {
        return loadTime_;
    }

    /**
     * Searches for XML declaration and returns the <tt>encoding</tt> if found, otherwise returns <code>null</code>.
     */
    private String getXMLEncoding(final byte[] body) {
        String encoding = null;
        final byte[] declarationPrefix = "<?xml ".getBytes();
        if (ArrayUtils.isEquals(declarationPrefix, ArrayUtils.subarray(body, 0, declarationPrefix.length))) {
            final int index = ArrayUtils.indexOf(body, (byte) '?', 2);
            if (index + 1 < body.length && body[index + 1] == '>') {
                final String declaration = new String(body, 0, index + 2);
                int start = declaration.indexOf("encoding");
                if (start != -1) {
                    start += 8;
                    char delimiter;

                outer:
                    while (true) {
                        switch (declaration.charAt(start)) {
                            case '"':
                            case '\'':
                                delimiter = declaration.charAt(start);
                                start = start + 1;
                                break outer;

                            default:
                                start++;
                        }
                    }
                    final int end = declaration.indexOf(delimiter, start);
                    encoding = declaration.substring(start, end);
                }
            }
        }
        return encoding;
    }

}
