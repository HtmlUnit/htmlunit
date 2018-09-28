/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FILEREADER_CONTENT_TYPE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FILEREADER_EMPTY_NULL;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;

/**
 * A JavaScript object for {@code FileReader}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class FileReader extends EventTarget {

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
        final java.io.File file = ((File) object).getFile();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            FileUtils.copyFile(file, bos);

            final byte[] bytes = bos.toByteArray();
            final String value = new String(new Base64().encode(bytes));
            final BrowserVersion browserVersion = getBrowserVersion();

            result_ = "data:";
            final boolean includeConentType = browserVersion.hasFeature(JS_FILEREADER_CONTENT_TYPE);
            if (!value.isEmpty() || includeConentType) {
                String contentType;
                if (value.isEmpty()) {
                    contentType = URLConnection.guessContentTypeFromName(file.getName());
                }
                else {
                    contentType = Files.probeContentType(file.toPath());
                }

                if (contentType == null) {
                    if (includeConentType) {
                        contentType = "application/octet-stream";
                    }
                    else {
                        contentType = "";
                    }
                }
                result_ += contentType + ";base64," + value;
            }
            if (value.isEmpty() && getBrowserVersion().hasFeature(JS_FILEREADER_EMPTY_NULL)) {
                result_ = "null";
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
