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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FILEREADER_CONTENT_TYPE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FILEREADER_EMPTY_NULL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.util.MimeType;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;

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
    public static final short EMPTY = 0;

    /** Data is currently being loaded. */
    @JsxConstant
    public static final short LOADING = 1;

    /** The entire read request has been completed. */
    @JsxConstant
    public static final short DONE = 2;

    private int readyState_ = EMPTY;
    private Object result_;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public FileReader() {
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

        result_ = "data:";

        final byte[] bytes = ((Blob) object).getBytes();
        final String value = new String(new Base64().encode(bytes), StandardCharsets.US_ASCII);
        final BrowserVersion browserVersion = getBrowserVersion();

        String contentType = ((Blob) object).getType();
        if (StringUtils.isEmpty(contentType) && !browserVersion.hasFeature(JS_FILEREADER_EMPTY_NULL)) {
            contentType = MimeType.APPLICATION_OCTET_STREAM;
        }

        if (object instanceof File) {
            final java.io.File file = ((File) object).getFile();
            if (value.isEmpty()) {
                contentType = URLConnection.guessContentTypeFromName(file.getName());
            }
            else {
                contentType = Files.probeContentType(file.toPath());
                // this is a bit weak, on linux we get different results
                // e.g. 'application/octet-stream' for a file with random bits
                // instead of null on windows
            }
        }

        if (browserVersion.hasFeature(JS_FILEREADER_EMPTY_NULL)) {
            if (value.isEmpty()) {
                result_ = "null";
            }
            else {
                if (contentType != null) {
                    result_ += contentType;
                }
                result_ += ";base64," + value;
            }
        }
        else {
            final boolean includeConentType = browserVersion.hasFeature(JS_FILEREADER_CONTENT_TYPE);
            if (!value.isEmpty() || includeConentType) {
                if (contentType == null) {
                    contentType = MimeType.APPLICATION_OCTET_STREAM;
                }
                result_ += contentType + ";base64," + value;
            }
        }
        readyState_ = DONE;

        final Event event = new Event(this, Event.TYPE_LOAD);
        fireEvent(event);
    }

    /**
     * Reads the contents of the specified {@link Blob} or {@link File}.
     * @param object the {@link Blob} or {@link File} from which to read
     * @throws IOException if an error occurs
     */
    @JsxFunction
    public void readAsArrayBuffer(final Object object) throws IOException {
        readyState_ = LOADING;
        final java.io.File file = ((File) object).getFile();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            FileUtils.copyFile(file, bos);

            final byte[] bytes = bos.toByteArray();

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
     * @throws IOException if an error occurs
     */
    @JsxFunction
    public void readAsText(final Object object, final Object encoding) throws IOException {
        readyState_ = LOADING;
        final java.io.File file = ((File) object).getFile();
        Charset charset = StandardCharsets.UTF_8;
        if (encoding != null && !Undefined.isUndefined(encoding)) {
            final String encAsString = Context.toString(encoding);
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

        try {
            result_ = FileUtils.readFileToString(file, charset);
        }
        catch (final IOException e) {
            LOG.warn("FileReader readAsText can't read the file.", e);
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
