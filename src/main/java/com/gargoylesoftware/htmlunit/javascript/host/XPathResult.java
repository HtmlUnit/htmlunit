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

import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for XPathResult.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XPathResult extends SimpleScriptable {

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

    private List<? extends Object> result_;
    private int resultType_;

    /**
     * The index of the next result.
     */
    private int iteratorIndex_;

    /**
     * @param result the evaluation result
     * @param type If a specific type is specified, then the result will be returned as the corresponding type
     */
    void init(final List<? extends Object> result, final int type) {
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
        iteratorIndex_ = 0;
    }

    /**
     * The code representing the type of this result, as defined by the type constants.
     * @return the code representing the type of this result
     */
    public int jsxGet_resultType() {
        return resultType_;
    }

    /**
     * The number of nodes in the result snapshot.
     * @return the number of nodes in the result snapshot
     */
    public int jsxGet_snapshotLength() {
        if (resultType_ != UNORDERED_NODE_SNAPSHOT_TYPE && resultType_ != ORDERED_NODE_SNAPSHOT_TYPE) {
            throw Context.reportRuntimeError("Cannot get snapshotLength for type: " + resultType_);
        }
        return result_.size();
    }

    /**
     * The value of this single node result, which may be null.
     * @return the value of this single node result, which may be null
     */
    public Node jsxGet_singleNodeValue() {
        if (resultType_ != ANY_UNORDERED_NODE_TYPE && resultType_ != FIRST_ORDERED_NODE_TYPE) {
            throw Context.reportRuntimeError("Cannot get singleNodeValue for type: " + resultType_);
        }
        if (!result_.isEmpty()) {
            return (Node) ((DomNode) result_.get(0)).getScriptObject();
        }
        return null;
    }

    /**
     * Iterates and returns the next node from the node set or <code>null</code> if there are no more nodes.
     * @return the next node
     */
    public Node jsxFunction_iterateNext() {
        if (resultType_ != UNORDERED_NODE_ITERATOR_TYPE && resultType_ != ORDERED_NODE_ITERATOR_TYPE) {
            throw Context.reportRuntimeError("Cannot get iterateNext for type: " + resultType_);
        }
        if (iteratorIndex_ < result_.size()) {
            return (Node) ((DomNode) result_.get(iteratorIndex_++)).getScriptObject();
        }
        return null;
    }

    /**
     * Returns the index<sup>th</sup> item in the snapshot collection.
     * If index is greater than or equal to the number of nodes in the list, this method returns null.
     * @param index Index into the snapshot collection
     * @return the node at the index<sup>th</sup> position in the NodeList, or null if that is not a valid index
     */
    public Node jsxFunction_snapshotItem(final int index) {
        if (resultType_ != UNORDERED_NODE_SNAPSHOT_TYPE && resultType_ != ORDERED_NODE_SNAPSHOT_TYPE) {
            throw Context.reportRuntimeError("Cannot get snapshotLength for type: " + resultType_);
        }
        if (index >= 0 && index < result_.size()) {
            return (Node) ((DomNode) result_.get(index)).getScriptObject();
        }
        return null;
    }

    /**
     * Returns the value of this number result.
     * @return the value of this number result
     */
    public double jsxGet_numberValue() {
        if (resultType_ != NUMBER_TYPE) {
            throw Context.reportRuntimeError("Cannot get numberValue for type: " + resultType_);
        }
        return ((Number) result_.get(0)).doubleValue();
    }

    /**
     * Returns the value of this boolean result.
     * @return the value of this boolean result
     */
    public boolean jsxGet_booleanValue() {
        if (resultType_ != BOOLEAN_TYPE) {
            throw Context.reportRuntimeError("Cannot get booleanValue for type: " + resultType_);
        }
        return ((Boolean) result_.get(0)).booleanValue();
    }

    /**
     * Returns the value of this string result.
     * @return the value of this string result
     */
    public String jsxGet_stringValue() {
        if (resultType_ != STRING_TYPE) {
            throw Context.reportRuntimeError("Cannot get stringValue for type: " + resultType_);
        }
        return (String) result_.get(0);
    }
}
