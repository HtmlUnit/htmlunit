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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code File}.
 *
 * @author Ahmed Ashour
 */
@JsxClass
public class File extends Blob {
    private static final String LAST_MODIFIED_DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)";
    private static final String LAST_MODIFIED_DATE_FORMAT_FF = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

    private java.io.File file_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38), @WebBrowser(EDGE) })
    public File() {
    }

    File(final String pathname) {
        file_ = new java.io.File(pathname);
    }

    /**
     * Returns the {@code name} property.
     * @return the {@code name} property
     */
    @JsxGetter
    public String getName() {
        return file_.getName();
    }

    /**
     * Returns the {@code lastModifiedDate} property.
     * @return the {@code lastModifiedDate} property
     */
    @JsxGetter
    public String getLastModifiedDate() {
        final Date date = new Date(getLastModified());
        final BrowserVersion browser = getBrowserVersion();
        final Locale locale = new Locale(browser.getSystemLanguage());

        if (browser.hasFeature(BrowserVersionFeatures.JS_FILE_SHORT_DATE_FORMAT)) {
            final FastDateFormat format = FastDateFormat.getInstance(LAST_MODIFIED_DATE_FORMAT_FF, locale);
            return format.format(date);
        }

        final FastDateFormat format = FastDateFormat.getInstance(LAST_MODIFIED_DATE_FORMAT, locale);
        return format.format(date);
    }

    /**
     * Returns the {@code lastModified} property.
     * @return the {@code lastModified} property
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38) })
    public long getLastModified() {
        return file_.lastModified();
    }

    /**
     * Returns the {@code webkitRelativePath} property.
     * @return the {@code webkitRelativePath} property
     */
    @JsxGetter(@WebBrowser(CHROME))
    public String getWebkitRelativePath() {
        return "";
    }

    /**
     * Returns the {@code size} property.
     * @return the {@code size} property
     */
    @JsxGetter
    public long getSize() {
        return file_.length();
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        try {
            return Files.probeContentType(file_.toPath());
        }
        catch (final IOException e) {
            return "";
        }
    }

    /**
     * Slices the file.
     */
    @JsxFunction
    public void slice() {
    }

    /**
     * Closes the file.
     */
    @JsxFunction(@WebBrowser(IE))
    public void msClose() {
    }

    /**
     * Returns the underlying file.
     * @return the underlying file
     */
    public java.io.File getFile() {
        return file_;
    }
}
