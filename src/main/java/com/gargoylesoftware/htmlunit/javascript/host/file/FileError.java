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

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMError;

/**
 * A JavaScript object for {@code FileError}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(browsers = @WebBrowser(CHROME))
public class FileError extends DOMError {

    /** A required file or directory could not be found at the time an operation was processed. */
    @JsxConstant
    public static final short NOT_FOUND_ERR = 1;

    /** Access to the files were denied. */
    @JsxConstant
    public static final short SECURITY_ERR = 2;

    /** Abort Error. */
    @JsxConstant
    public static final short ABORT_ERR = 3;

    /** The file or directory cannot be read. */
    @JsxConstant
    public static final short NOT_READABLE_ERR = 4;

    /** The URL is malformed. */
    @JsxConstant
    public static final short ENCODING_ERR = 5;

    /** The state of the underlying file system prevents any writing to a file or a directory. */
    @JsxConstant
    public static final short NO_MODIFICATION_ALLOWED_ERR = 6;

    /** The operation cannot be performed on the current state of the interface object. */
    @JsxConstant
    public static final short INVALID_STATE_ERR = 7;

    /** Syntax Error. */
    @JsxConstant
    public static final short SYNTAX_ERR = 8;

    /** The modification requested is not allowed. */
    @JsxConstant
    public static final short INVALID_MODIFICATION_ERR = 9;

    /** Either there's not enough remaining storage space or the storage quota was reached
     * and the user declined to give more space to the database. */
    @JsxConstant
    public static final short QUOTA_EXCEEDED_ERR = 10;

    /** The app looked up an entry, but the entry found is of the wrong type. */
    @JsxConstant
    public static final short TYPE_MISMATCH_ERR = 11;

    /** The file or directory with the same path already exists. */
    @JsxConstant
    public static final short PATH_EXISTS_ERR = 12;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public FileError() {
    }

}
