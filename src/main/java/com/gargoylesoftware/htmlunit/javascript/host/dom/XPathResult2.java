/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Attribute;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Where;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * A JavaScript object for {@code XPathResult}.
 *
 * @author Ahmed Ashour
 * @author Chuck Dumont
 * @author Ronald Brill
 */
@ScriptClass
public class XPathResult2 extends SimpleScriptObject {

    /**
     * This code does not represent a specific type.
     * An evaluation of an XPath expression will never produce this type. If this type is requested,
     * then the evaluation returns whatever type naturally results from evaluation of the expression.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int ANY_TYPE = 0;

    /**
     * The result is a number.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int NUMBER_TYPE = 1;

    /**
     * The result is a string.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int STRING_TYPE = 2;

    /**
     * The result is a boolean.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int BOOLEAN_TYPE = 3;

    /**
     * The result is a node set that will be accessed iteratively, which may not produce nodes in a particular order.
     * This is the default type returned if the result is a node set and {@link #ANY_TYPE} is requested.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int UNORDERED_NODE_ITERATOR_TYPE = 4;

    /**
     * The result is a node set that will be accessed iteratively, which will produce document-ordered nodes.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int ORDERED_NODE_ITERATOR_TYPE = 5;

    /**
     * The result is a node set that will be accessed as a snapshot list of nodes
     * that may not be in a particular order.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int UNORDERED_NODE_SNAPSHOT_TYPE = 6;

    /**
     * The result is a node set that will be accessed as a snapshot list of nodes
     * that will be in original document order.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int ORDERED_NODE_SNAPSHOT_TYPE = 7;

    /**
     * The result is a node set and will be accessed as a single node, which may be null if the node set is empty.
     * If there is more than one node in the actual result,
     * the single node returned might not be the first in document order.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int ANY_UNORDERED_NODE_TYPE = 8;

    /**
     * The result is a node set and will be accessed as a single node, which may be null if the node set is empty.
     * If there are more than one node in the actual result,
     * the single node returned will be the first in document order.
     */
    @com.gargoylesoftware.js.nashorn.internal.objects.annotations.Property
        (attributes = Attribute.NOT_WRITABLE | Attribute.NOT_CONFIGURABLE, where = Where.CONSTRUCTOR)
    public static final int FIRST_ORDERED_NODE_TYPE = 9;

    private List<?> result_;
    private int resultType_;

    /**
     * The index of the next result.
     */
    private int iteratorIndex_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static XPathResult2 constructor(final boolean newObj, final Object self) {
        final XPathResult2 host = new XPathResult2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * @param result the evaluation result
     * @param type If a specific type is specified, then the result will be returned as the corresponding type
     */
    void init(final List<?> result, final int type) {
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
     * Iterates and returns the next node from the node set or {@code null} if there are no more nodes.
     * @return the next node
     */
    @Function
    public Node2 iterateNext() {
        if (resultType_ != UNORDERED_NODE_ITERATOR_TYPE && resultType_ != ORDERED_NODE_ITERATOR_TYPE) {
            throw new RuntimeException("Cannot get iterateNext for type: " + resultType_);
        }
        if (iteratorIndex_ < result_.size()) {
            return (Node2) ((DomNode) result_.get(iteratorIndex_++)).getScriptObject2();
        }
        return null;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(XPathResult2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("XPathResult",
                    staticHandle("constructor", XPathResult2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("XPathResult");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("XPathResult");
        }

        /**
         * @return {@link XPathResult2#ANY_TYPE}
         */
        public int G$ANY_TYPE() {
            return ANY_TYPE;
        }

        /**
         * @return {@link XPathResult2#NUMBER_TYPE}
         */
        public int G$NUMBER_TYPE() {
            return NUMBER_TYPE;
        }

        /**
         * @return {@link XPathResult2#STRING_TYPE}
         */
        public int G$STRING_TYPE() {
            return STRING_TYPE;
        }

        /**
         * @return {@link XPathResult2#BOOLEAN_TYPE}
         */
        public int G$BOOLEAN_TYPE() {
            return BOOLEAN_TYPE;
        }

        /**
         * @return {@link XPathResult2#UNORDERED_NODE_ITERATOR_TYPE}
         */
        public int G$UNORDERED_NODE_ITERATOR_TYPE() {
            return UNORDERED_NODE_ITERATOR_TYPE;
        }

        /**
         * @return {@link XPathResult2#ORDERED_NODE_ITERATOR_TYPE}
         */
        public int G$ORDERED_NODE_ITERATOR_TYPE() {
            return ORDERED_NODE_ITERATOR_TYPE;
        }

        /**
         * @return {@link XPathResult2#UNORDERED_NODE_SNAPSHOT_TYPE}
         */
        public int G$UNORDERED_NODE_SNAPSHOT_TYPE() {
            return UNORDERED_NODE_SNAPSHOT_TYPE;
        }

        /**
         * @return {@link XPathResult2#ORDERED_NODE_SNAPSHOT_TYPE}
         */
        public int G$ORDERED_NODE_SNAPSHOT_TYPE() {
            return ORDERED_NODE_SNAPSHOT_TYPE;
        }

        /**
         * @return {@link XPathResult2#ANY_UNORDERED_NODE_TYPE}
         */
        public int G$ANY_UNORDERED_NODE_TYPE() {
            return ANY_UNORDERED_NODE_TYPE;
        }

        /**
         * @return {@link XPathResult2#FIRST_ORDERED_NODE_TYPE}
         */
        public int G$FIRST_ORDERED_NODE_TYPE() {
            return FIRST_ORDERED_NODE_TYPE;
        }
    }
}
