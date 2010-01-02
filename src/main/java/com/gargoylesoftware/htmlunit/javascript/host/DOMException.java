/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Exception for DOM manipulations.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-17189187">
 * DOM-Level-2-Core</a>
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DOMException extends SimpleScriptable {
    /** Serial version UID. */
    private static final long serialVersionUID = 5758391763325220491L;
    /** If the specified range of text does not fit into a DOMString. */
    public static final short DOMSTRING_SIZE_ERR = org.w3c.dom.DOMException.DOMSTRING_SIZE_ERR;
    /** If any node is inserted somewhere it doesn't belong. */
    public static final short HIERARCHY_REQUEST_ERR = org.w3c.dom.DOMException.HIERARCHY_REQUEST_ERR;
    /** If index or size is negative, or greater than the allowed value. */
    public static final short INDEX_SIZE_ERR = org.w3c.dom.DOMException.INDEX_SIZE_ERR;
    /** If an attempt is made to add an attribute that is already in use elsewhere. */
    public static final short INUSE_ATTRIBUTE_ERR = org.w3c.dom.DOMException.INUSE_ATTRIBUTE_ERR;
    /** If a parameter or an operation is not supported by the underlying object. */
    public static final short INVALID_ACCESS_ERR = org.w3c.dom.DOMException.INVALID_ACCESS_ERR;
    /** If an invalid or illegal character is specified, such as in a name. */
    public static final short INVALID_CHARACTER_ERR = org.w3c.dom.DOMException.INVALID_CHARACTER_ERR;
    /** If an attempt is made to modify the type of the underlying object. */
    public static final short INVALID_MODIFICATION_ERR = org.w3c.dom.DOMException.INVALID_MODIFICATION_ERR;
    /** If an attempt is made to use an object that is not, or is no longer, usable. */
    public static final short INVALID_STATE_ERR = org.w3c.dom.DOMException.INVALID_STATE_ERR;
    /** If an attempt is made to create or change an object in a way which is incorrect with regard to namespaces. */
    public static final short NAMESPACE_ERR = org.w3c.dom.DOMException.NAMESPACE_ERR;
    /** If data is specified for a node which does not support data. */
    public static final short NO_DATA_ALLOWED_ERR = org.w3c.dom.DOMException.NO_DATA_ALLOWED_ERR;
    /** If an attempt is made to modify an object where modifications are not allowed. */
    public static final short NO_MODIFICATION_ALLOWED_ERR = org.w3c.dom.DOMException.NO_MODIFICATION_ALLOWED_ERR;
    /** If an attempt is made to reference a node in a context where it does not exist. */
    public static final short NOT_FOUND_ERR = org.w3c.dom.DOMException.NOT_FOUND_ERR;
    /** If the implementation does not support the requested type of object or operation. */
    public static final short NOT_SUPPORTED_ERR = org.w3c.dom.DOMException.NOT_SUPPORTED_ERR;
    /** If an invalid or illegal string is specified. */
    public static final short SYNTAX_ERR = org.w3c.dom.DOMException.SYNTAX_ERR;
    /** If a node is used in a different document than the one that created it (that doesn't support it). */
    public static final short WRONG_DOCUMENT_ERR = org.w3c.dom.DOMException.WRONG_DOCUMENT_ERR;
}
