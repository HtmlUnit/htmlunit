/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * A JavaScript object for {@code XPathResult}.
 *
 * @author Ahmed Ashour
 * @author Chuck Dumont
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF_ESR})
public class XPathResult extends HtmlUnitScriptable {

    /**
     * This code does not represent a specific type.
     * An evaluation of an XPath expression will never produce this type. If this type is requested,
     * then the evaluation returns whatever type naturally results from evaluation of the expression.
     */
    @JsxConstant
    public static final int ANY_TYPE = 0;

    /**
     * The result is a number.
     */
    @JsxConstant
    public static final int NUMBER_TYPE = 1;

    /**
     * The result is a string.
     */
    @JsxConstant
    public static final int STRING_TYPE = 2;

    /**
     * The result is a boolean.
     */
    @JsxConstant
    public static final int BOOLEAN_TYPE = 3;

    /**
     * The result is a node set that will be accessed iteratively, which may not produce nodes in a particular order.
     * This is the default type returned if the result is a node set and {@link #ANY_TYPE} is requested.
     */
    @JsxConstant
    public static final int UNORDERED_NODE_ITERATOR_TYPE = 4;

    /**
     * The result is a node set that will be accessed iteratively, which will produce document-ordered nodes.
     */
    @JsxConstant
    public static final int ORDERED_NODE_ITERATOR_TYPE = 5;

    /**
     * The result is a node set that will be accessed as a snapshot list of nodes
     * that may not be in a particular order.
     */
    @JsxConstant
    public static final int UNORDERED_NODE_SNAPSHOT_TYPE = 6;

    /**
     * The result is a node set that will be accessed as a snapshot list of nodes
     * that will be in original document order.
     */
    @JsxConstant
    public static final int ORDERED_NODE_SNAPSHOT_TYPE = 7;

    /**
     * The result is a node set and will be accessed as a single node, which may be null if the node set is empty.
     * If there is more than one node in the actual result,
     * the single node returned might not be the first in document order.
     */
    @JsxConstant
    public static final int ANY_UNORDERED_NODE_TYPE = 8;

    /**
     * The result is a node set and will be accessed as a single node, which may be null if the node set is empty.
     * If there are more than one node in the actual result,
     * the single node returned will be the first in document order.
     */
    @JsxConstant
    public static final int FIRST_ORDERED_NODE_TYPE = 9;

    private List<?> result_;
    private int resultType_;

    /**
     * The index of the next result.
     */
    private int iteratorIndex_;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public XPathResult() {
    }

    /**
     * @param result the evaluation result
     * @param type If a specific type is specified, then the result will be returned as the corresponding type
     */
    void init(final List<?> result, final int type) {
        result_ = result;
        resultType_ = type;

        if (type == ANY_TYPE) {
            resultType_ = UNORDERED_NODE_ITERATOR_TYPE;

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
        }

        iteratorIndex_ = 0;
    }

    /**
     * The code representing the type of this result, as defined by the type constants.
     * @return the code representing the type of this result
     */
    @JsxGetter
    public int getResultType() {
        return resultType_;
    }

    /**
     * The number of nodes in the result snapshot.
     * @return the number of nodes in the result snapshot
     */
    @JsxGetter
    public int getSnapshotLength() {
        if (resultType_ != UNORDERED_NODE_SNAPSHOT_TYPE && resultType_ != ORDERED_NODE_SNAPSHOT_TYPE) {
            throw Context.reportRuntimeError("Cannot get snapshotLength for type: " + resultType_);
        }
        return result_.size();
    }

    /**
     * The value of this single node result, which may be null.
     * @return the value of this single node result, which may be null
     */
    @JsxGetter
    public Node getSingleNodeValue() {
        if (resultType_ != ANY_UNORDERED_NODE_TYPE && resultType_ != FIRST_ORDERED_NODE_TYPE) {
            throw Context.reportRuntimeError("Cannot get singleNodeValue for type: " + resultType_);
        }
        if (!result_.isEmpty()) {
            return ((DomNode) result_.get(0)).getScriptableObject();
        }
        return null;
    }

    /**
     * Iterates and returns the next node from the node set or {@code null} if there are no more nodes.
     * @return the next node
     */
    @JsxFunction
    public Node iterateNext() {
        if (resultType_ != UNORDERED_NODE_ITERATOR_TYPE && resultType_ != ORDERED_NODE_ITERATOR_TYPE) {
            throw Context.reportRuntimeError("Cannot get iterateNext for type: " + resultType_);
        }
        if (iteratorIndex_ < result_.size()) {
            return ((DomNode) result_.get(iteratorIndex_++)).getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the index<sup>th</sup> item in the snapshot collection.
     * If index is greater than or equal to the number of nodes in the list, this method returns null.
     * @param index Index into the snapshot collection
     * @return the node at the index<sup>th</sup> position in the NodeList, or null if that is not a valid index
     */
    @JsxFunction
    public Node snapshotItem(final int index) {
        if (resultType_ != UNORDERED_NODE_SNAPSHOT_TYPE && resultType_ != ORDERED_NODE_SNAPSHOT_TYPE) {
            throw Context.reportRuntimeError("Cannot get snapshotLength for type: " + resultType_);
        }
        if (index >= 0 && index < result_.size()) {
            return ((DomNode) result_.get(index)).getScriptableObject();
        }
        return null;
    }

    /**
     * Returns the value of this number result.
     * @return the value of this number result
     */
    @JsxGetter
    public double getNumberValue() {
        if (resultType_ != NUMBER_TYPE) {
            throw Context.reportRuntimeError("Cannot get numberValue for type: " + resultType_);
        }

        if (result_.size() == 1) {
            final Object o = result_.get(0);
            if (o instanceof Number) {
                return ((Double) o).doubleValue();
            }
            if (o instanceof Boolean) {
                return ((Boolean) o).booleanValue() ? 1 : 0;
            }
        }

        final String asString = asString();
        double answer;
        try {
            answer = Double.parseDouble(asString);
        }
        catch (final NumberFormatException e) {
            answer = Double.NaN;
        }
        return answer;
    }

    /**
     * Returns the value of this boolean result.
     * @return the value of this boolean result
     */
    @JsxGetter
    public boolean getBooleanValue() {
        if (resultType_ != BOOLEAN_TYPE) {
            throw Context.reportRuntimeError("Cannot get booleanValue for type: " + resultType_);
        }

        if (result_.size() == 1) {
            final Object o = result_.get(0);
            if (o instanceof Number) {
                final double d = ((Double) o).doubleValue();
                if (Double.isNaN(d) || Double.isInfinite(d)) {
                    return true;
                }

                return 0.0 != d;
            }
            if (o instanceof String) {
                return !((String) o).isEmpty();
            }
            if (o instanceof Boolean) {
                return ((Boolean) o).booleanValue();
            }
        }

        return result_.size() > 0;
    }

    /**
     * Returns the value of this string result.
     * @return the value of this string result
     */
    @JsxGetter
    public String getStringValue() {
        if (resultType_ != STRING_TYPE) {
            throw Context.reportRuntimeError("Cannot get stringValue for type: " + resultType_);
        }
        return asString();
    }

    private String asString() {
        if (result_.size() < 1) {
            return "";
        }

        final Object resultObj = result_.get(0);
        if (resultObj instanceof DomAttr) {
            return ((DomAttr) resultObj).getValue();
        }
        if (resultObj instanceof DomNode) {
            return ((DomNode) resultObj).asNormalizedText();
        }
        return resultObj.toString();
    }
}
