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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.html.DomDocumentFragment;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.html.HTMLCollection;

/**
 * A JavaScript object for {@code DocumentFragment}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 *
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-core.html#ID-B63ED1A3">
 *     W3C Dom Level 1</a>
 */
@JsxClass(domClass = DomDocumentFragment.class)
public class DocumentFragment extends Node {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();

        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        final DomDocumentFragment fragment = new DomDocumentFragment(page);
        setDomNode(fragment);
    }

    /**
     * Inserts a set of Node objects or string objects after the last child
     * of the document fragment. String objects are inserted as equivalent Text nodes.
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void append(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.append(context, thisObj, args, function);
    }

    /**
     * Inserts a set of Node objects or string objects before the first child
     * of the document fragment. String objects are inserted as equivalent Text nodes.
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void prepend(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.prepend(context, thisObj, args, function);
    }

    /**
     * Replaces the existing children of a DocumentFragment with a specified
     * new set of children. These can be string or Node objects.
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction
    public static void replaceChildren(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.replaceChildren(context, thisObj, args, function);
    }

    /**
     * Moves a given Node inside the invoking node as a direct child, before a given reference node.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     */
    @JsxFunction({CHROME, EDGE, FF})
    public static void moveBefore(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        Node.moveBefore(context, scope, thisObj, args, function);
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
            throw JavaScriptEngine.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
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
                return node.getScriptableObject();
            }
            return null;
        }
        catch (final CSSException e) {
            throw JavaScriptEngine.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
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
    @JsxGetter
    public int getChildElementCount() {
        return super.getChildElementCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Element getFirstElementChild() {
        return super.getFirstElementChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public Element getLastElementChild() {
        return super.getLastElementChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * Returns the element with the specified ID, or {@code null} if that element could not be found.
     * @param id the ID to search for
     * @return the element, or {@code null} if it could not be found
     */
    @JsxFunction
    public HtmlUnitScriptable getElementById(final Object id) {
        if (id == null || JavaScriptEngine.isUndefined(id)) {
            return null;
        }
        final String idString = JavaScriptEngine.toString(id);
        if (idString == null || idString.length() == 0) {
            return null;
        }
        for (final DomNode child : getDomNodeOrDie().getChildren()) {
            final Element elem = child.getScriptableObject();
            if (idString.equals(elem.getId())) {
                return elem;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getRootNode() {
        return this;
    }
}
