/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.List;

import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for XPathResult.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XPathResult extends SimpleScriptable {

    private static final long serialVersionUID = 261617209266957657L;

    /**
     * This code does not represent a specific type.
     * An evaluation of an XPath expression will never produce this type. If this type is requested,
     * then the evaluation returns whatever type naturally results from evaluation of the expression.
     */
    public static final int ANY_TYPE = 0;

    /**
     * The result is a number.
     */
    public static final int NUMBER_TYPE = 1;

    /**
     * The result is a string.
     */
    public static final int STRING_TYPE = 2;

    /**
     * The result is a boolean.
     */
    public static final int BOOLEAN_TYPE = 3;

    /**
     * The result is a node set that will be accessed iteratively, which may not produce nodes in a particular order.
     * This is the default type returned if the result is a node set and {@link #ANY_TYPE} is requested.
     */
    public static final int UNORDERED_NODE_ITERATOR_TYPE = 4;

    /**
     * The result is a node set that will be accessed iteratively, which will produce document-ordered nodes.
     */
    public static final int ORDERED_NODE_ITERATOR_TYPE = 5;
    
    /**
     * The result is a node set that will be accessed as a snapshot list of nodes
     * that may not be in a particular order.
     */
    public static final int UNORDERED_NODE_SNAPSHOT_TYPE = 6;

    /**
     * The result is a node set that will be accessed as a snapshot list of nodes
     * that will be in original document order.
     */
    public static final int ORDERED_NODE_SNAPSHOT_TYPE = 7;

    /**
     * The result is a node set and will be accessed as a single node, which may be null if the node set is empty.
     * If there is more than one node in the actual result,
     * the single node returned might not be the first in document order.
     */
    public static final int ANY_UNORDERED_NODE_TYPE = 8;

    /**
     * The result is a node set and will be accessed as a single node, which may be null if the node set is empty.
     * If there are more than one node in the actual result,
     * the single node returned will be the first in document order.
     */
    public static final int FIRST_ORDERED_NODE_TYPE = 9;

    private List result_;
    private int resultType_;

    /**
     * @param result the evaluation result.
     * @param type If a specific type is specified, then the result will be returned as the corresponding type.
     */
    void init(final List result, final int type) {
        result_ = result;
        resultType_ = -1;
        if (result_.size() == 1) {
            final Object o = result_.get(0);
            if (o instanceof Number) {
                resultType_ = NUMBER_TYPE;
            }
            else if (o instanceof String) {
                resultType_ = STRING_TYPE;
            }
            else if (o instanceof Boolean) {
                resultType_ = BOOLEAN_TYPE;
            }
        }
        
        if (resultType_ == -1) {
            if (type != ANY_TYPE) {
                resultType_ = type;
            }
            else {
                resultType_ = UNORDERED_NODE_ITERATOR_TYPE;
            }
        }
    }
    
    /**
     * The code representing the type of this result, as defined by the type constants.
     * @return The code representing the type of this result.
     */
    public int jsxGet_resultType() {
        return resultType_;
    }
    
    /**
     * The number of nodes in the result snapshot.
     * @return The number of nodes in the result snapshot.
     */
    public int jsxGet_snapshotLength() {
        if (resultType_ != UNORDERED_NODE_SNAPSHOT_TYPE && resultType_ != ORDERED_NODE_SNAPSHOT_TYPE) {
            throw Context.reportRuntimeError("Can not get snapshotLength for type: " + resultType_);
        }
        return result_.size();
    }
    
    /**
     * The value of this single node result, which may be null.
     * @return The value of this single node result, which may be null.
     */
    public NodeImpl jsxGet_singleNodeValue() {
        if (resultType_ != ANY_UNORDERED_NODE_TYPE && resultType_ != FIRST_ORDERED_NODE_TYPE) {
            throw Context.reportRuntimeError("Can not get singleNodeValue for type: " + resultType_);
        }
        if (!result_.isEmpty()) {
            return (NodeImpl) ((DomNode) result_.get(0)).getScriptObject();
        }
        else {
            return null;
        }
    }

    /**
     * Returns the index<sup>th</sup> item in the snapshot collection.
     * If index is greater than or equal to the number of nodes in the list, this method returns null.
     * @param index Index into the snapshot collection.
     * @return The node at the index<sup>th</sup> position in the NodeList, or null if that is not a valid index.
     */
    public NodeImpl jsxFunction_snapshotItem(final int index) {
        if (resultType_ != UNORDERED_NODE_SNAPSHOT_TYPE && resultType_ != ORDERED_NODE_SNAPSHOT_TYPE) {
            throw Context.reportRuntimeError("Can not get snapshotLength for type: " + resultType_);
        }
        if (index >= 0 && index < result_.size()) {
            return (NodeImpl) ((DomNode) result_.get(index)).getScriptObject();
        }
        else {
            return null;
        }
    }
}
