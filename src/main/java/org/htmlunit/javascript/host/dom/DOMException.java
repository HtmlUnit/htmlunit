/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.corejs.javascript.Undefined;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * Exception for DOM manipulations.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-17189187">
 * DOM-Level-2-Core</a>
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DOMException extends HtmlUnitScriptable {
    /** If the specified range of text does not fit into a DOMString. */
    @JsxConstant
    public static final int DOMSTRING_SIZE_ERR = org.w3c.dom.DOMException.DOMSTRING_SIZE_ERR;
    /** If any node is inserted somewhere it doesn't belong. */
    @JsxConstant
    public static final int HIERARCHY_REQUEST_ERR = org.w3c.dom.DOMException.HIERARCHY_REQUEST_ERR;
    /** If index or size is negative, or greater than the allowed value. */
    @JsxConstant
    public static final int INDEX_SIZE_ERR = org.w3c.dom.DOMException.INDEX_SIZE_ERR;
    /** If an attempt is made to add an attribute that is already in use elsewhere. */
    @JsxConstant
    public static final int INUSE_ATTRIBUTE_ERR = org.w3c.dom.DOMException.INUSE_ATTRIBUTE_ERR;
    /** If a parameter or an operation is not supported by the underlying object. */
    @JsxConstant
    public static final int INVALID_ACCESS_ERR = org.w3c.dom.DOMException.INVALID_ACCESS_ERR;
    /** If an invalid or illegal character is specified, such as in a name. */
    @JsxConstant
    public static final int INVALID_CHARACTER_ERR = org.w3c.dom.DOMException.INVALID_CHARACTER_ERR;
    /** If an attempt is made to modify the type of the underlying object. */
    @JsxConstant
    public static final int INVALID_MODIFICATION_ERR = org.w3c.dom.DOMException.INVALID_MODIFICATION_ERR;
    /** If an attempt is made to use an object that is not, or is no longer, usable. */
    @JsxConstant
    public static final int INVALID_STATE_ERR = org.w3c.dom.DOMException.INVALID_STATE_ERR;
    /** If an attempt is made to create or change an object in a way which is incorrect with regard to namespaces. */
    @JsxConstant
    public static final int NAMESPACE_ERR = org.w3c.dom.DOMException.NAMESPACE_ERR;
    /** If data is specified for a node which does not support data. */
    @JsxConstant
    public static final int NO_DATA_ALLOWED_ERR = org.w3c.dom.DOMException.NO_DATA_ALLOWED_ERR;
    /** If an attempt is made to modify an object where modifications are not allowed. */
    @JsxConstant
    public static final int NO_MODIFICATION_ALLOWED_ERR = org.w3c.dom.DOMException.NO_MODIFICATION_ALLOWED_ERR;
    /** If an attempt is made to reference a node in a context where it does not exist. */
    @JsxConstant
    public static final int NOT_FOUND_ERR = org.w3c.dom.DOMException.NOT_FOUND_ERR;
    /** If the implementation does not support the requested type of object or operation. */
    @JsxConstant
    public static final int NOT_SUPPORTED_ERR = org.w3c.dom.DOMException.NOT_SUPPORTED_ERR;
    /** If an invalid or illegal string is specified. */
    @JsxConstant
    public static final int SYNTAX_ERR = org.w3c.dom.DOMException.SYNTAX_ERR;
    /** If a node is used in a different document than the one that created it (that doesn't support it). */
    @JsxConstant
    public static final int WRONG_DOCUMENT_ERR = org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR;
    /** If a call to a method would make the {@code Node} invalid with respect to "partial validity". */
    @JsxConstant
    public static final int VALIDATION_ERR = org.w3c.dom.DOMException.VALIDATION_ERR;
    /** If the type of an object is incompatible with the expected type of the parameter. */
    @JsxConstant
    public static final int TYPE_MISMATCH_ERR = org.w3c.dom.DOMException.TYPE_MISMATCH_ERR;
    /** Security error. */
    @JsxConstant
    public static final int SECURITY_ERR = 18;
    /** Network error. */
    @JsxConstant
    public static final int NETWORK_ERR = 19;
    /** Abort error. */
    @JsxConstant
    public static final int ABORT_ERR = 20;
    /** URL mismatch error. */
    @JsxConstant
    public static final int URL_MISMATCH_ERR = 21;
    /** Quota exceeded error. */
    @JsxConstant
    public static final int QUOTA_EXCEEDED_ERR = 22;
    /** Timeout error. */
    @JsxConstant
    public static final int TIMEOUT_ERR = 23;
    /** Invalid node type error. */
    @JsxConstant
    public static final int INVALID_NODE_TYPE_ERR = 24;
    /** Data clone error. */
    @JsxConstant
    public static final int DATA_CLONE_ERR = 25;

    private final int code_;
    private final String message_;
    private int lineNumber_;
    private String fileName_;

    /**
     * Default constructor used to build the prototype.
     */
    public DOMException() {
        code_ = -1;
        message_ = null;
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param errorCode the error code
     */
    public DOMException(final String message, final int errorCode) {
        code_ = errorCode;
        message_ = message;
    }

    /**
     * Gets the exception code.
     * @return the exception code
     */
    @JsxGetter
    public Object getCode() {
        if (code_ == -1) {
            return Undefined.instance;
        }
        return code_;
    }

    /**
     * Gets the exception message.
     * @return the exception message
     */
    @JsxGetter
    public Object getMessage() {
        if (message_ == null) {
            return Undefined.instance;
        }
        return message_;
    }

    /**
     * Gets the line at which the exception occurred.
     * @return the line of the exception
     */
    @JsxGetter({FF, FF_ESR})
    public Object getLineNumber() {
        if (lineNumber_ == -1) {
            return Undefined.instance;
        }
        return lineNumber_;
    }

    /**
     * Gets the name of the in which the exception occurred.
     * @return the name of the source file
     */
    @JsxGetter({FF, FF_ESR})
    public Object getFilename() {
        if (fileName_ == null) {
            return Undefined.instance;
        }
        return fileName_;
    }

    /**
     * Sets the location in JavaScript source where this exception occurred.
     * @param fileName the name of the source file
     * @param lineNumber the line number
     */
    public void setLocation(final String fileName, final int lineNumber) {
        fileName_ = fileName;
        lineNumber_ = lineNumber;
    }
}
