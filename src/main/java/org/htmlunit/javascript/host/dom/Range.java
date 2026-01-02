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

import java.util.HashSet;

import org.apache.commons.logging.LogFactory;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.impl.SimpleRange;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.DOMRect;
import org.htmlunit.javascript.host.DOMRectList;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.html.HTMLElement;

/**
 * The JavaScript object that represents a Range.
 *
 * @see <a href="http://www.xulplanet.com/references/objref/Range.html">XULPlanet</a>
 * @see <a href="http://www.w3.org/TR/DOM-Level-2-Traversal-Range/ranges.html">DOM-Level-2-Traversal-Range</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author James Phillpotts
 * @author Ronald Brill
 */
@JsxClass
public class Range extends AbstractRange {

    /** Comparison mode for compareBoundaryPoints. */
    @JsxConstant
    public static final int START_TO_START = 0;

    /** Comparison mode for compareBoundaryPoints. */
    @JsxConstant
    public static final int START_TO_END = 1;

    /** Comparison mode for compareBoundaryPoints. */
    @JsxConstant
    public static final int END_TO_END = 2;

    /** Comparison mode for compareBoundaryPoints. */
    @JsxConstant
    public static final int END_TO_START = 3;

    /**
     * Creates an instance.
     */
    public Range() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Creates a new instance.
     * @param document the HTML document creating the range
     */
    public Range(final Document document) {
        super(document, document, 0, 0);
    }

    Range(final SimpleRange simpleRange) {
        super(simpleRange.getStartContainer().getScriptableObject(),
                simpleRange.getEndContainer().getScriptableObject(),
                simpleRange.getStartOffset(),
                simpleRange.getEndOffset());
    }

    /**
     * Sets the attributes describing the start of a Range.
     * @param refNode the reference node
     * @param offset the offset value within the node
     */
    @JsxFunction
    public void setStart(final Node refNode, final int offset) {
        if (refNode == null) {
            throw JavaScriptEngine.reportRuntimeError("It is illegal to call Range.setStart() with a null node.");
        }
        internSetStartContainer(refNode);
        internSetStartOffset(offset);
    }

    /**
     * Sets the start of the range to be after the node.
     * @param refNode the reference node
     */
    @JsxFunction
    public void setStartAfter(final Node refNode) {
        if (refNode == null) {
            throw JavaScriptEngine.reportRuntimeError("It is illegal to call Range.setStartAfter() with a null node.");
        }
        internSetStartContainer(refNode.getParent());
        internSetStartOffset(getPositionInContainer(refNode) + 1);
    }

    /**
     * Sets the start of the range to be before the node.
     * @param refNode the reference node
     */
    @JsxFunction
    public void setStartBefore(final Node refNode) {
        if (refNode == null) {
            throw JavaScriptEngine.reportRuntimeError("It is illegal to call Range.setStartBefore() with a null node.");
        }
        internSetStartContainer(refNode.getParent());
        internSetStartOffset(getPositionInContainer(refNode));
    }

    private static int getPositionInContainer(final Node refNode) {
        int i = 0;
        Node node = refNode;
        while (node.getPreviousSibling() != null) {
            node = node.getPreviousSibling();
            i++;
        }
        return i;
    }

    /**
     * Sets the attributes describing the end of a Range.
     * @param refNode the reference node
     * @param offset the offset value within the node
     */
    @JsxFunction
    public void setEnd(final Node refNode, final int offset) {
        if (refNode == null) {
            throw JavaScriptEngine.reportRuntimeError("It is illegal to call Range.setEnd() with a null node.");
        }
        internSetEndContainer(refNode);
        internSetEndOffset(offset);
    }

    /**
     * Sets the end of the range to be after the node.
     * @param refNode the reference node
     */
    @JsxFunction
    public void setEndAfter(final Node refNode) {
        if (refNode == null) {
            throw JavaScriptEngine.reportRuntimeError("It is illegal to call Range.setEndAfter() with a null node.");
        }
        internSetEndContainer(refNode.getParent());
        internSetEndOffset(getPositionInContainer(refNode) + 1);
    }

    /**
     * Sets the end of the range to be before the node.
     * @param refNode the reference node
     */
    @JsxFunction
    public void setEndBefore(final Node refNode) {
        if (refNode == null) {
            throw JavaScriptEngine.reportRuntimeError("It is illegal to call Range.setEndBefore() with a null node.");
        }
        internSetStartContainer(refNode.getParent());
        internSetStartOffset(getPositionInContainer(refNode));
    }

    /**
     * Select the contents within a node.
     * @param refNode Node to select from
     */
    @JsxFunction
    public void selectNodeContents(final Node refNode) {
        internSetStartContainer(refNode);
        internSetStartOffset(0);
        internSetEndContainer(refNode);
        internSetEndOffset(refNode.getChildNodes().getLength());
    }

    /**
     * Selects a node and its contents.
     * @param refNode the node to select
     */
    @JsxFunction
    public void selectNode(final Node refNode) {
        setStartBefore(refNode);
        setEndAfter(refNode);
    }

    /**
     * Collapse a Range onto one of its boundaries.
     * @param toStart if {@code true}, collapses the Range onto its start; else collapses it onto its end
     */
    @JsxFunction
    public void collapse(final boolean toStart) {
        if (toStart) {
            internSetEndContainer(internGetStartContainer());
            internSetEndOffset(internGetStartOffset());
        }
        else {
            internSetStartContainer(internGetEndContainer());
            internSetStartOffset(internGetEndOffset());
        }
    }

    /**
     * Returns the deepest common ancestor container of the Range's two boundary points.
     * @return the deepest common ancestor container of the Range's two boundary points
     */
    @JsxGetter
    public Object getCommonAncestorContainer() {
        final HashSet<Node> startAncestors = new HashSet<>();
        Node ancestor = internGetStartContainer();
        while (ancestor != null) {
            startAncestors.add(ancestor);
            ancestor = ancestor.getParent();
        }

        ancestor = internGetEndContainer();
        while (ancestor != null) {
            if (startAncestors.contains(ancestor)) {
                return ancestor;
            }
            ancestor = ancestor.getParent();
        }

        return JavaScriptEngine.UNDEFINED;
    }

    /**
     * Parses an HTML snippet.
     * @param valueAsString text that contains text and tags to be converted to a document fragment
     * @return a document fragment
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/range.createContextualFragment">Mozilla
     *     documentation</a>
     */
    @JsxFunction
    public HtmlUnitScriptable createContextualFragment(final String valueAsString) {
        final SgmlPage page = internGetStartContainer().getDomNodeOrDie().getPage();
        final DomDocumentFragment fragment = new DomDocumentFragment(page);
        try {
            final WebClient webClient = page.getWebClient();
            webClient.getPageCreator().getHtmlParser()
                    .parseFragment(webClient, fragment,
                            internGetStartContainer().getDomNodeOrDie(), valueAsString, false);
        }
        catch (final Exception e) {
            LogFactory.getLog(Range.class).error("Unexpected exception occurred in createContextualFragment", e);
            throw JavaScriptEngine.reportRuntimeError("Unexpected exception occurred in createContextualFragment: "
                    + e.getMessage());
        }

        return fragment.getScriptableObject();
    }

    /**
     * Moves this range's contents from the document tree into a document fragment.
     * @return the new document fragment containing the range contents
     */
    @JsxFunction
    public HtmlUnitScriptable extractContents() {
        try {
            return getSimpleRange().extractContents().getScriptableObject();
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Compares the boundary points of two Ranges.
     * @param how a constant describing the comparison method
     * @param sourceRange the Range to compare boundary points with this range
     * @return -1, 0, or 1, indicating whether the corresponding boundary-point of range is respectively before,
     *         equal to, or after the corresponding boundary-point of sourceRange.
     */
    @JsxFunction
    public int compareBoundaryPoints(final int how, final Range sourceRange) {
        final Node nodeForThis;
        final int offsetForThis;
        final int containingMoficator;
        if (START_TO_START == how || END_TO_START == how) {
            nodeForThis = internGetStartContainer();
            offsetForThis = internGetStartOffset();
            containingMoficator = 1;
        }
        else {
            nodeForThis = internGetEndContainer();
            offsetForThis = internGetEndOffset();
            containingMoficator = -1;
        }

        final Node nodeForOther;
        final int offsetForOther;
        if (START_TO_END == how || START_TO_START == how) {
            nodeForOther = sourceRange.internGetStartContainer();
            offsetForOther = sourceRange.internGetStartOffset();
        }
        else {
            nodeForOther = sourceRange.internGetEndContainer();
            offsetForOther = sourceRange.internGetEndOffset();
        }

        if (nodeForThis == nodeForOther) {
            if (offsetForThis < offsetForOther) {
                return -1;
            }
            else if (offsetForThis > offsetForOther) {
                return 1;
            }
            return 0;
        }

        final byte nodeComparision = (byte) nodeForThis.compareDocumentPosition(nodeForOther);
        if ((nodeComparision & Node.DOCUMENT_POSITION_CONTAINED_BY) != 0) {
            return -1 * containingMoficator;
        }
        else if ((nodeComparision & Node.DOCUMENT_POSITION_PRECEDING) != 0) {
            return -1;
        }

        // TODO: handle other cases
        return 1;
    }

    /**
     * Returns a clone of the range in a document fragment.
     * @return a clone
     */
    @JsxFunction
    public HtmlUnitScriptable cloneContents() {
        try {
            return getSimpleRange().cloneContents().getScriptableObject();
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Deletes the contents of the range.
     */
    @JsxFunction
    public void deleteContents() {
        try {
            getSimpleRange().deleteContents();
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Inserts a new node at the beginning of the range. If the range begins at an offset, the node is split.
     * @param newNode The node to insert
     * @see <a href="https://developer.mozilla.org/en/DOM/range">https://developer.mozilla.org/en/DOM/range</a>
     */
    @JsxFunction
    public void insertNode(final Node newNode) {
        try {
            getSimpleRange().insertNode(newNode.getDomNodeOrDie());
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Surrounds the contents of the range in a new node.
     * @param newNode The node to surround the range in
     */
    @JsxFunction
    public void surroundContents(final Node newNode) {
        try {
            getSimpleRange().surroundContents(newNode.getDomNodeOrDie());
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Returns a clone of the range.
     * @return a clone of the range
     */
    @JsxFunction
    public Range cloneRange() {
        try {
            return new Range(getSimpleRange().cloneRange());
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Releases Range from use to improve performance.
     */
    @JsxFunction
    public void detach() {
        // Java garbage collection should take care of this for us
    }

    /**
     * Returns the text of the Range.
     * @return the text
     */
    @JsxFunction(functionName = "toString")
    public String jsToString() {
        try {
            return getSimpleRange().toString();
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Retrieves a collection of rectangles that describes the layout of the contents of an object
     * or range within the client. Each rectangle describes a single line.
     * @return a collection of rectangles that describes the layout of the contents
     */
    @JsxFunction
    public DOMRectList getClientRects() {
        final Window w = getWindow();
        final DOMRectList rectList = new DOMRectList();
        rectList.setParentScope(w);
        rectList.setPrototype(getPrototype(rectList.getClass()));

        try {
            // simple impl for now
            for (final DomNode node : getSimpleRange().containedNodes()) {
                final HtmlUnitScriptable scriptable = node.getScriptableObject();
                if (scriptable instanceof HTMLElement) {
                    final DOMRect rect = new DOMRect(0, 0, 1, 1);
                    rect.setParentScope(w);
                    rect.setPrototype(getPrototype(rect.getClass()));
                    rectList.add(rect);
                }
            }

            return rectList;
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }

    /**
     * Returns an object that bounds the contents of the range.
     * this a rectangle enclosing the union of the bounding rectangles for all the elements in the range.
     * @return an object the bounds the contents of the range
     */
    @JsxFunction
    public DOMRect getBoundingClientRect() {
        final DOMRect rect = new DOMRect();
        rect.setParentScope(getWindow());
        rect.setPrototype(getPrototype(rect.getClass()));

        try {
            // simple impl for now
            for (final DomNode node : getSimpleRange().containedNodes()) {
                final HtmlUnitScriptable scriptable = node.getScriptableObject();
                if (scriptable instanceof HTMLElement element) {
                    final DOMRect childRect = element.getBoundingClientRect();
                    rect.setY(Math.min(rect.getX(), childRect.getX()));
                    rect.setY(Math.min(rect.getY(), childRect.getY()));
                    rect.setWidth(Math.max(rect.getWidth(), childRect.getWidth()));
                    rect.setHeight(Math.max(rect.getHeight(), childRect.getHeight()));
                }
            }

            return rect;
        }
        catch (final IllegalStateException e) {
            throw JavaScriptEngine.reportRuntimeError(e.getMessage());
        }
    }
}
