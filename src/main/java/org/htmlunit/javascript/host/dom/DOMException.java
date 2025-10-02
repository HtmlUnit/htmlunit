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
package org.htmlunit.javascript.host.dom;

import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.Arrays;
import java.util.List;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * Exception for DOM manipulations.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-17189187">
 *     DOM-Level-2-Core</a>
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

    /** If the type of object is incompatible with the expected type of the parameter. */
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

    private static final List<String> COMMON_ERROR_NAMES = Arrays.asList(
        "IndexSizeError",
        null,
        "HierarchyRequestError",
        "WrongDocumentError",
        "InvalidCharacterError",
        null,
        "NoModificationAllowedError",
        "NotFoundError",
        "NotSupportedError",
        "InUseAttributeError",
        "InvalidStateError",
        "SyntaxError",
        "InvalidModificationError",
        "NamespaceError",
        "InvalidAccessError",
        null,
        "TypeMismatchError",
        "SecurityError",
        "NetworkError",
        "AbortError",
        "URLMismatchError",
        "QuotaExceededError",
        "TimeoutError",
        "InvalidNodeTypeError",
        "DataCloneError");

    private int code_;
    private String name_;
    private String message_;
    private int lineNumber_;
    private String fileName_;

    /**
     * Default constructor used to build the prototype.
     */
    public DOMException() {
    }

    /**
     * JavaScript constructor.
     * @param message a description of the exception. If not present, the empty string '' is used
     * @param error If the specified name is a standard error name,
     *        then getting the code property of the DOMException object will return the
     *        code number corresponding to the specified name.
     *        If not present, the string 'Error' is used.
     */
    @JsxConstructor
    public void jsConstructor(final String message, final Object error) {
        message_ = message;

        if (error == null) {
            code_ = 0;
            name_ = null;
            return;
        }

        if (JavaScriptEngine.isUndefined(error)) {
            code_ = 0;
            name_ = "Error";
            return;
        }

        name_ = JavaScriptEngine.toString(error);

        final int idx = COMMON_ERROR_NAMES.indexOf(name_);
        if (idx != -1) {
            code_ = idx + 1;
            return;
        }

        code_ = 0;
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param error the error code (on of the constants from the class)
     */
    public DOMException(final String message, final int error) {
        message_ = message;
        code_ = error;
        name_ = COMMON_ERROR_NAMES.get(error - 1);
    }

    /**
     * Gets the exception code.
     * @return the exception code
     */
    @JsxGetter
    public Object getCode() {
        if (code_ == -1) {
            return JavaScriptEngine.UNDEFINED;
        }
        return code_;
    }

    /**
     * Gets the exception name.
     * @return the exception name
     */
    @JsxGetter
    public String getName() {
        return name_;
    }

    /**
     * Gets the exception message.
     * @return the exception message
     */
    @JsxGetter
    public Object getMessage() {
        if (message_ == null) {
            return JavaScriptEngine.UNDEFINED;
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
            return JavaScriptEngine.UNDEFINED;
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
            return JavaScriptEngine.UNDEFINED;
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
