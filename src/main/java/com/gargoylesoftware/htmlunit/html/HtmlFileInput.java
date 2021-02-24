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
package com.gargoylesoftware.htmlunit.html;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "input".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlFileInput extends HtmlInput implements LabelableElement {

    private String contentType_;
    private byte[] data_;
    private File[] files_ = new File[0];

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlFileInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        final DomAttr valueAttrib = attributes.get("value");
        if (valueAttrib != null) {
            setDefaultValue(valueAttrib.getNodeValue(), false);
        }
    }

    /**
     * Returns the in-memory data assigned to this file input element, if any.
     * @return {@code null} if {@link #setData(byte[])} hasn't be used
     */
    public final byte[] getData() {
        return data_;
    }

    /**
     * <p>Assigns in-memory data to this file input element. During submission, instead
     * of loading data from a file, the data is read from in-memory byte array.</p>
     *
     * <p>NOTE: Only use this method if you wish to upload in-memory data; if you instead
     * wish to upload the contents of an actual file, use {@link #setValueAttribute(String)},
     * passing in the path to the file.</p>
     *
     * @param data the in-memory data assigned to this file input element
     */
    public final void setData(final byte[] data) {
        data_ = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitNameValuePairs() {
        if (files_ == null || files_.length == 0) {
            return new NameValuePair[] {new KeyDataPair(getNameAttribute(), null, null, null, (Charset) null)};
        }

        final List<NameValuePair> list = new ArrayList<>();
        for (final File file : files_) {
            String contentType;
            if (contentType_ == null) {
                contentType = getPage().getWebClient().getBrowserVersion().getUploadMimeType(file);
                if (StringUtils.isEmpty(contentType)) {
                    contentType = MimeType.APPLICATION_OCTET_STREAM;
                }
            }
            else {
                contentType = contentType_;
            }
            final Charset charset = getPage().getCharset();
            final KeyDataPair keyDataPair = new KeyDataPair(getNameAttribute(), file, null, contentType, charset);
            keyDataPair.setData(data_);
            list.add(keyDataPair);
        }
        return list.toArray(new NameValuePair[list.size()]);
    }

    /**
     * Sets the content type value that should be sent together with the uploaded file.
     * If content type is not explicitly set, HtmlUnit will try to guess it from the file content.
     * @param contentType the content type ({@code null} resets it)
     */
    public void setContentType(final String contentType) {
        contentType_ = contentType;
    }

    /**
     * Gets the content type that should be sent together with the uploaded file.
     * @return the content type, or {@code null} if this has not been explicitly set
     * and should be guessed from file content
     */
    public String getContentType() {
        return contentType_;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated as of version 2.48.0; use asNormalizedText() instead
     */
    @Deprecated
    @Override
    public String asText() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAttribute(final String newValue) {
        setFiles(new File(newValue));
    }

    /**
     * Used to specify {@code multiple} files to upload.
     *
     * We may follow WebDriver solution, once made,
     * see https://code.google.com/p/selenium/issues/detail?id=2239
     * @param files the list of files to upload
     */
    public void setFiles(final File... files) {
        if (files.length > 1 && getAttributeDirect("multiple") == ATTRIBUTE_NOT_DEFINED) {
            throw new IllegalStateException("HtmlFileInput is not 'multiple'.");
        }
        for (int i = 0; i < files.length; i++) {
            files[i] = normalizeFile(files[i]);
        }
        files_ = files;
        fireEvent(Event.TYPE_CHANGE);
    }

    /**
     * To tolerate {@code file://}
     */
    private static File normalizeFile(final File file) {
        File f = null;
        String path = file.getPath().replace('\\', '/');
        if (path.startsWith("file:/")) {
            if (path.startsWith("file://") && !path.startsWith("file:///")) {
                path = "file:///" + path.substring(7);
            }
            try {
                f = new File(new URI(path));
            }
            catch (final URISyntaxException e) {
                // nothing here
            }
        }
        if (f == null) {
            f = new File(path);
        }
        return f;
    }

    /**
     * Returns the files.
     * @return the array of {@link File}s
     */
    public File[] getFiles() {
        return files_;
    }

    /**
     * Returns whether this element satisfies all form validation constraints set.
     * @return whether this element satisfies all form validation constraints set
     */
    @Override
    public boolean isValid() {
        return !isRequiredSupported() || getAttributeDirect("required") == ATTRIBUTE_NOT_DEFINED
                || files_.length > 0;
    }
}
