/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.Iterator;

import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A JavaScript object for {@code DocumentFragment}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 *
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-core.html#ID-B63ED1A3">
 * W3C Dom Level 1</a>
 */
@JsxClass(domClass = DomDocumentFragment.class)
public class DocumentFragment extends Node {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public DocumentFragment() {
    }

    /**
     * Retrieves all element nodes from descendants of the starting element node that match any selector
     * within the supplied selector strings.
     * The NodeList object returned by the querySelectorAll() method must be static, not live.
     * @param selectors the selectors
     * @return the static node list
     */
    @JsxFunction
    public NodeList querySelectorAll(final String selectors) {
        try {
            return NodeList.staticNodeList(this, getDomNodeOrDie().querySelectorAll(selectors));
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
                    + selectors + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * Returns the first element within the document that matches the specified group of selectors.
     * @param selectors the selectors
     * @return null if no matches are found; otherwise, it returns the first matching element
     */
    @JsxFunction
    public Node querySelector(final String selectors) {
        try {
            final DomNode node = getDomNodeOrDie().querySelector(selectors);
            if (node != null) {
                return (Node) node.getScriptableObject();
            }
            return null;
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
                    + selectors + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if (String.class.equals(hint) || hint == null) {
            return "[object " + getClassName() + "]";
        }
        return super.getDefaultValue(hint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public int getChildElementCount() {
        int counter = 0;
        final Iterator<DomNode> iterator = getDomNodeOrDie().getChildren().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Element getFirstElementChild() {
        for (final DomNode child : getDomNodeOrDie().getChildren()) {
            return (Element) child.getScriptableObject();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Element getLastElementChild() {
        DomNode lastChild = null;
        for (final DomNode child : getDomNodeOrDie().getChildren()) {
            lastChild = child;
        }

        if (lastChild != null) {
            return (Element) lastChild.getScriptableObject();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * Returns the element with the specified ID, or {@code null} if that element could not be found.
     * @param id the ID to search for
     * @return the element, or {@code null} if it could not be found
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public Object getElementById(final Object id) {
        if (id == null || Undefined.isUndefined(id)) {
            return null;
        }
        final String idString = ScriptRuntime.toString(id);
        if (idString == null || idString.length() == 0) {
            return null;
        }
        for (final DomNode child : getDomNodeOrDie().getChildren()) {
            final Element elem = (Element) child.getScriptableObject();
            if (idString.equals(elem.getId())) {
                return elem;
            }
        }
        return null;
    }
}
