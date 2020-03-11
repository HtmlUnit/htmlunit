/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FILE_SHORT_DATE_FORMAT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF60;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF68;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code File}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class File extends Blob {
    private static final String LAST_MODIFIED_DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)";
    private static final String LAST_MODIFIED_DATE_FORMAT_FF = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

    private java.io.File file_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF, FF68, FF60})
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
        final TimeZone timezone = browser.getSystemTimezone();

        if (browser.hasFeature(JS_FILE_SHORT_DATE_FORMAT)) {
            final FastDateFormat format = FastDateFormat.getInstance(LAST_MODIFIED_DATE_FORMAT_FF, timezone, locale);
            return format.format(date);
        }

        final FastDateFormat format = FastDateFormat.getInstance(LAST_MODIFIED_DATE_FORMAT, timezone, locale);
        return format.format(date);
    }

    /**
     * Returns the {@code lastModified} property.
     * @return the {@code lastModified} property
     */
    @JsxGetter({CHROME, FF, FF68, FF60})
    public long getLastModified() {
        return file_.lastModified();
    }

    /**
     * Returns the {@code webkitRelativePath} property.
     * @return the {@code webkitRelativePath} property
     */
    @JsxGetter({CHROME, FF, FF68, FF60})
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
        return getBrowserVersion().getUploadMimeType(file_);
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
    @JsxFunction(IE)
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
