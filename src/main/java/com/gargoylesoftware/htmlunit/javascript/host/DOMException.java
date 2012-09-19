/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * Exception for DOM manipulations.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-17189187">
 * DOM-Level-2-Core</a>
 * @version $Revision$
 * @author Marc Guillemot
 */
@JsxClass(browsers = @WebBrowser(FF))
public class DOMException extends SimpleScriptable {
    /** If the specified range of text does not fit into a DOMString. */
    @JsxConstant
    public static final short DOMSTRING_SIZE_ERR = org.w3c.dom.DOMException.DOMSTRING_SIZE_ERR;
    /** If any node is inserted somewhere it doesn't belong. */
    @JsxConstant
    public static final short HIERARCHY_REQUEST_ERR = org.w3c.dom.DOMException.HIERARCHY_REQUEST_ERR;
    /** If index or size is negative, or greater than the allowed value. */
    @JsxConstant
    public static final short INDEX_SIZE_ERR = org.w3c.dom.DOMException.INDEX_SIZE_ERR;
    /** If an attempt is made to add an attribute that is already in use elsewhere. */
    @JsxConstant
    public static final short INUSE_ATTRIBUTE_ERR = org.w3c.dom.DOMException.INUSE_ATTRIBUTE_ERR;
    /** If a parameter or an operation is not supported by the underlying object. */
    @JsxConstant
    public static final short INVALID_ACCESS_ERR = org.w3c.dom.DOMException.INVALID_ACCESS_ERR;
    /** If an invalid or illegal character is specified, such as in a name. */
    @JsxConstant
    public static final short INVALID_CHARACTER_ERR = org.w3c.dom.DOMException.INVALID_CHARACTER_ERR;
    /** If an attempt is made to modify the type of the underlying object. */
    @JsxConstant
    public static final short INVALID_MODIFICATION_ERR = org.w3c.dom.DOMException.INVALID_MODIFICATION_ERR;
    /** If an attempt is made to use an object that is not, or is no longer, usable. */
    @JsxConstant
    public static final short INVALID_STATE_ERR = org.w3c.dom.DOMException.INVALID_STATE_ERR;
    /** If an attempt is made to create or change an object in a way which is incorrect with regard to namespaces. */
    @JsxConstant
    public static final short NAMESPACE_ERR = org.w3c.dom.DOMException.NAMESPACE_ERR;
    /** If data is specified for a node which does not support data. */
    @JsxConstant
    public static final short NO_DATA_ALLOWED_ERR = org.w3c.dom.DOMException.NO_DATA_ALLOWED_ERR;
    /** If an attempt is made to modify an object where modifications are not allowed. */
    @JsxConstant
    public static final short NO_MODIFICATION_ALLOWED_ERR = org.w3c.dom.DOMException.NO_MODIFICATION_ALLOWED_ERR;
    /** If an attempt is made to reference a node in a context where it does not exist. */
    @JsxConstant
    public static final short NOT_FOUND_ERR = org.w3c.dom.DOMException.NOT_FOUND_ERR;
    /** If the implementation does not support the requested type of object or operation. */
    @JsxConstant
    public static final short NOT_SUPPORTED_ERR = org.w3c.dom.DOMException.NOT_SUPPORTED_ERR;
    /** If an invalid or illegal string is specified. */
    @JsxConstant
    public static final short SYNTAX_ERR = org.w3c.dom.DOMException.SYNTAX_ERR;
    /** If a node is used in a different document than the one that created it (that doesn't support it). */
    @JsxConstant
    public static final short WRONG_DOCUMENT_ERR = org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR;

    private final short code_;
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
     * Constructor.
     * @param message the exception message
     * @param errorCode the error code
     */
    public DOMException(final String message, final short errorCode) {
        code_ = errorCode;
        message_ = message;
    }

    /**
     * Gets the exception code.
     * @return the exception code
     */
    @JsxGetter
    public Object jsxGet_code() {
        if (code_ == -1) {
            return Context.getUndefinedValue();
        }
        return code_;
    }

    /**
     * Gets the exception message.
     * @return the exception message
     */
    @JsxGetter
    public Object jsxGet_message() {
        if (message_ == null) {
            return Context.getUndefinedValue();
        }
        return message_;
    }

    /**
     * Gets the line at which the exception occurred.
     * @return the line of the exception
     */
    @JsxGetter
    public Object jsxGet_lineNumber() {
        if (lineNumber_ == -1) {
            return Context.getUndefinedValue();
        }
        return lineNumber_;
    }

    /**
     * Gets the name of the in  which the exception occurred.
     * @return the name of the source file
     */
    @JsxGetter
    public Object jsxGet_filename() {
        if (fileName_ == null) {
            return Context.getUndefinedValue();
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
