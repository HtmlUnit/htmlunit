/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Base64;
import java.util.Locale;

import org.apache.commons.io.Charsets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.protocol.data.DataURLConnection;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.StringUtils;

/**
 * A JavaScript object for {@code FileReader}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class FileReader extends EventTarget {

    private static final Log LOG = LogFactory.getLog(FileReader.class);

    /** No data has been loaded yet. */
    @JsxConstant
    public static final int EMPTY = 0;

    /** Data is currently being loaded. */
    @JsxConstant
    public static final int LOADING = 1;

    /** The entire read request has been completed. */
    @JsxConstant
    public static final int DONE = 2;

    private int readyState_ = EMPTY;
    private Object result_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the current state of the reading operation.
     *
     * @return {@value #EMPTY}, {@value #LOADING}, or {@value #DONE}
     */
    @JsxGetter
    public int getReadyState() {
        return readyState_;
    }

    /**
     * Returns the file's contents.
     * @return the file's contents
     */
    @JsxGetter
    public Object getResult() {
        return result_;
    }

    /**
     * Reads the contents of the specified {@link Blob} or {@link File}.
     * @param object the {@link Blob} or {@link File} from which to read
     * @throws IOException if an error occurs
     */
    @JsxFunction
    public void readAsDataURL(final Object object) throws IOException {
        readyState_ = LOADING;

        result_ = DataURLConnection.DATA_PREFIX;

        final byte[] bytes = ((Blob) object).getBytes();
        final String value = new String(Base64.getEncoder().encode(bytes), StandardCharsets.US_ASCII);

        String contentType = ((Blob) object).getType();
        if (StringUtils.isEmptyOrNull(contentType)) {
            contentType = MimeType.APPLICATION_OCTET_STREAM;
        }

        result_ += contentType + ";base64," + value;
        readyState_ = DONE;

        final Event event = new Event(this, Event.TYPE_LOAD);
        fireEvent(event);
    }

    /**
     * Reads the contents of the specified {@link Blob} or {@link File}.
     * @param object the {@link Blob} or {@link File} from which to read
     */
    @JsxFunction
    public void readAsArrayBuffer(final Object object) {
        readyState_ = LOADING;

        if (object instanceof Blob) {
            final byte[] bytes = ((Blob) object).getBytes();

            final NativeArrayBuffer buffer = new NativeArrayBuffer(bytes.length);
            System.arraycopy(bytes, 0, buffer.getBuffer(), 0, bytes.length);
            buffer.setParentScope(getParentScope());
            buffer.setPrototype(ScriptableObject.getClassPrototype(getWindow(), buffer.getClassName()));

            result_ = buffer;
        }

        readyState_ = DONE;

        final Event event = new Event(this, Event.TYPE_LOAD);
        fireEvent(event);
    }

    /**
     * Reads the contents of the specified {@link Blob} or {@link File}.
     * When the read operation is complete, the readyState is changed to DONE,
     * the loaded event is triggered, and the result attribute contains the
     * contents of the file as a text string.
     * @param object the {@link Blob} or {@link File} from which to read
     * @param encoding the encoding
     */
    @JsxFunction
    public void readAsText(final Object object, final Object encoding) {
        readyState_ = LOADING;

        Charset charset = StandardCharsets.UTF_8;
        if (encoding != null && !JavaScriptEngine.isUndefined(encoding)) {
            final String encAsString = JavaScriptEngine.toString(encoding);
            if (StringUtils.isNotBlank(encAsString)) {
                try {
                    charset = Charsets.toCharset(encAsString.trim().toLowerCase(Locale.ROOT));
                }
                catch (final UnsupportedCharsetException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("FileReader readAsText was called with an unsupported encoding '"
                                    + encoding + "'. Using UTF-8 instead.");
                    }
                }
            }
        }

        if (object instanceof Blob) {
            result_ = new String(((Blob) object).getBytes(), charset);
        }

        readyState_ = DONE;

        final Event event = new Event(this, Event.TYPE_LOAD);
        fireEvent(event);
    }

    /**
     * Returns the {@code onload} event handler for this {@link FileReader}.
     * @return the {@code onload} event handler for this {@link FileReader}
     */
    @JsxGetter
    public Function getOnload() {
        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the {@code onload} event handler for this {@link FileReader}.
     * @param onload the {@code onload} event handler for this {@link FileReader}
     */
    @JsxSetter
    public void setOnload(final Object onload) {
        setEventHandler(Event.TYPE_LOAD, onload);
    }

    /**
     * Returns the {@code onerror} event handler for this {@link FileReader}.
     * @return the {@code onerror} event handler for this {@link FileReader}
     */
    @JsxGetter
    public Function getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Sets the {@code onerror} event handler for this {@link FileReader}.
     * @param onerror the {@code onerror} event handler for this {@link FileReader}
     */
    @JsxSetter
    public void setOnerror(final Object onerror) {
        setEventHandler(Event.TYPE_ERROR, onerror);
    }
}
