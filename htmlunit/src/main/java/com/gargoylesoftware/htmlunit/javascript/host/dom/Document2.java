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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_CREATE_ELEMENT_STRICT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_DESIGN_MODE_INHERIT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_SELECTION_RANGE_COUNT;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.utils.PrefixResolver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRp;
import com.gargoylesoftware.htmlunit.html.HtmlRt;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.Location2;
import com.gargoylesoftware.htmlunit.javascript.host.ScriptFunctionPrefixResolver;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

/**
 * A JavaScript object for {@code Document}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Michael Ottati
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Ahmed Ashour
 * @author Rob Di Marco
 * @author Ronald Brill
 * @author Chuck Dumont
 * @author Frank Danek
 * @author Madis Pärn
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms531073.aspx">MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">W3C Dom Level 1</a>
 */
@ScriptClass
public class Document2 extends EventNode2 {

    private static final Log LOG = LogFactory.getLog(Document.class);
    private static final Pattern TAG_NAME_PATTERN = Pattern.compile("\\w+");

    private Window2 window_;
    private DOMImplementation implementation_;
    private String designMode_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static Document2 constructor(final boolean newObj, final Object self) {
        final Document2 host = new Document2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Sets the Window JavaScript object that encloses this document.
     * @param window the Window JavaScript object that encloses this document
     */
    public void setWindow(final Window2 window) {
        window_ = window;
    }

    @Override
    public Window2 getWindow() {
        return window_;
    }

    /**
     * Returns the page that this document is modeling.
     * @return the page that this document is modeling
     */
    public SgmlPage getPage() {
        return (SgmlPage) getDomNodeOrDie();
    }

    /**
     * Creates a new element with the given tag name.
     *
     * @param tagName the tag name
     * @return the new HTML element, or {@code undefined} if the tag is not supported
     */
    @Function
    public Object createElement(String tagName) {
        Object result = ScriptRuntime.UNDEFINED;
        try {
            final BrowserVersion browserVersion = getBrowserVersion();

            if (browserVersion.hasFeature(JS_DOCUMENT_CREATE_ELEMENT_STRICT)
                  && (tagName.contains("<") || tagName.contains(">"))) {
                LOG.info("createElement: Provided string '"
                            + tagName + "' contains an invalid character; '<' and '>' are not allowed");
                throw new RuntimeException("String contains an invalid character");
            }
            else if (tagName.startsWith("<") && tagName.endsWith(">")) {
                tagName = tagName.substring(1, tagName.length() - 1);

                final Matcher matcher = TAG_NAME_PATTERN.matcher(tagName);
                if (!matcher.matches()) {
                    LOG.info("createElement: Provided string '" + tagName + "' contains an invalid character");
                    throw new RuntimeException("String contains an invalid character");
                }
            }

            final SgmlPage page = getPage();
            final org.w3c.dom.Node element = page.createElement(tagName);

            if (element instanceof BaseFrameElement) {
                ((BaseFrameElement) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlInput) {
                ((HtmlInput) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlImage) {
                ((HtmlImage) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlRp) {
                ((HtmlRp) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlRt) {
                ((HtmlRt) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlScript) {
                ((HtmlScript) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlUnknownElement) {
                ((HtmlUnknownElement) element).markAsCreatedByJavascript();
            }
            final Object jsElement = getScriptableFor(element);

            if (jsElement == ScriptRuntime.UNDEFINED) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("createElement(" + tagName
                        + ") cannot return a result as there isn't a JavaScript object for the element "
                        + element.getClass().getName());
                }
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to undefined
        }
        return result;
    }

    /**
     * Returns the value of the {@code location} property.
     * @return the value of the {@code location} property
     */
    @Getter
    public Location2 getLocation() {
        return Window2.getLocation(window_);
    }

    /**
     * Sets the {@code location} property. This will cause a reload of the window.
     * @param newLocation the URL of the new content
     * @throws IOException when location loading fails
     */
    @Setter
    public void setLocation(final String newLocation) throws IOException {
        Window2.getLocation(window_).setHref(newLocation);
    }

    /**
     * Creates a new HTML attribute with the specified name.
     *
     * @param attributeName the name of the attribute to create
     * @return an attribute with the specified name
     */
    @Function
    public Attr2 createAttribute(final String attributeName) {
        return (Attr2) getPage().createAttribute(attributeName).getScriptObject2();
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    @Function
    public HTMLCollection2 getElementsByTagName(final String tagName) {
        final HTMLCollection2 collection;
        if ("*".equals(tagName)) {
            collection = new HTMLCollection2(getDomNodeOrDie(), false) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        else {
            collection = new HTMLCollection2(getDomNodeOrDie(), false) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return tagName.equalsIgnoreCase(node.getNodeName());
                }
            };
        }

        return collection;
    }

    /**
     * Create a new DOM text node with the given data.
     *
     * @param newData the string value for the text node
     * @return the new text node or NOT_FOUND if there is an error
     */
    @Function
    public Object createTextNode(final String newData) {
        Object result = null;
        try {
            final DomNode domNode = new DomText(getDomNodeOrDie().getPage(), newData);
            final Object jsElement = getScriptableFor(domNode);

            if (jsElement == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("createTextNode(" + newData
                            + ") cannot return a result as there isn't a JavaScript object for the DOM node "
                            + domNode.getClass().getName());
                }
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }

    /**
     * Does nothing special anymore.
     *
     * @param type the type of events to capture
     * @see Window2#captureEvents(Object, String)
     */
    @Function
    public void captureEvents(final String type) {
        // Empty.
    }

    /**
     * Gets the JavaScript property {@code documentElement} for the document.
     * @return the root node for the document
     */
    @Getter
    public Element2 getDocumentElement() {
        final Object documentElement = getPage().getDocumentElement();
        if (documentElement == null) {
            // for instance with an XML document with parsing error
            return null;
        }
        return (Element2) getScriptableFor(documentElement);
    }

    /**
     * Creates a new Comment.
     * @param comment the comment text
     * @return the new Comment
     */
    @Function
    public Object createComment(final String comment) {
        final DomNode domNode = new DomComment(getDomNodeOrDie().getPage(), comment);
        return getScriptableFor(domNode);
    }

    /**
     * Creates a new document fragment.
     * @return a newly created document fragment
     */
    @Function
    public Object createDocumentFragment() {
        final DomDocumentFragment fragment = getDomNodeOrDie().getPage().createDocumentFragment();
        final DocumentFragment2 node = DocumentFragment2.constructor(true, Global.instance());
        node.setDomNode(fragment);
        return getScriptableFor(fragment);
    }

    /**
     * Creates a new HTML element with the given tag name, and name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @return the new HTML element, or NOT_FOUND if the tag is not supported
     */
    @Function
    public Object createElementNS(final String namespaceURI, final String qualifiedName) {
        final org.w3c.dom.Element element;
        if ("http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul".equals(namespaceURI)) {
            throw new RuntimeException("XUL not available");
        }

        if (HTMLParser.XHTML_NAMESPACE.equals(namespaceURI)
                || HTMLParser.SVG_NAMESPACE.equals(namespaceURI)) {
            element = getPage().createElementNS(namespaceURI, qualifiedName);
        }
        else {
            element = new DomElement(namespaceURI, qualifiedName, getPage(), null);
        }
        return getScriptableFor(element);
    }

    /**
     * Gets the window in which this document is contained.
     * @return the window
     */
    @Getter
    public Global getDefaultView() {
        return getWindow().getGlobal();
    }

    /**
     * Returns a value which indicates whether or not the document can be edited.
     * @return a value which indicates whether or not the document can be edited
     */
    @Getter
    public String getDesignMode() {
        if (designMode_ == null) {
            if (getBrowserVersion().hasFeature(JS_DOCUMENT_DESIGN_MODE_INHERIT)) {
                designMode_ = "inherit";
            }
            else {
                designMode_ = "off";
            }
        }
        return designMode_;
    }

    /**
     * Sets a value which indicates whether or not the document can be edited.
     * @param mode a value which indicates whether or not the document can be edited
     */
    @Setter
    public void setDesignMode(final String mode) {
        final BrowserVersion browserVersion = getBrowserVersion();
        final boolean inherit = browserVersion.hasFeature(JS_DOCUMENT_DESIGN_MODE_INHERIT);
        if (inherit) {
            if (!"on".equalsIgnoreCase(mode) && !"off".equalsIgnoreCase(mode) && !"inherit".equalsIgnoreCase(mode)) {
                throw new RuntimeException("Invalid document.designMode value '" + mode + "'.");
            }

            if ("on".equalsIgnoreCase(mode)) {
                designMode_ = "on";
            }
            else if ("off".equalsIgnoreCase(mode)) {
                designMode_ = "off";
            }
            else if ("inherit".equalsIgnoreCase(mode)) {
                designMode_ = "inherit";
            }
        }
        else {
            if ("on".equalsIgnoreCase(mode)) {
                designMode_ = "on";
                final SgmlPage page = getPage();
                if (page != null && page.isHtmlPage()
                        && getBrowserVersion().hasFeature(JS_DOCUMENT_SELECTION_RANGE_COUNT)) {
                    final HtmlPage htmlPage = (HtmlPage) page;
                    final DomNode child = htmlPage.getBody().getFirstChild();
                    final DomNode rangeNode = child == null ? htmlPage.getBody() : child;
                    htmlPage.setSelectionRange(new SimpleRange(rangeNode, 0));
                }
            }
            else if ("off".equalsIgnoreCase(mode)) {
                designMode_ = "off";
            }
        }
    }

    /**
     * Evaluates an XPath expression string and returns a result of the specified type if possible.
     * @param expression the XPath expression string to be parsed and evaluated
     * @param contextNode the context node for the evaluation of this XPath expression
     * @param resolver the resolver permits translation of all prefixes, including the XML namespace prefix,
     *        within the XPath expression into appropriate namespace URIs.
     * @param type If a specific type is specified, then the result will be returned as the corresponding type
     * @param result the result object which may be reused and returned by this method
     * @return the result of the evaluation of the XPath expression
     */
    @Function({CHROME, FF})
    public XPathResult2 evaluate(final String expression, final Node2 contextNode,
            final Object resolver, final int type, final Object result) {
        XPathResult2 xPathResult = (XPathResult2) result;
        if (xPathResult == null) {
            xPathResult = XPathResult2.constructor(true, getWindow().getGlobal());
        }

        PrefixResolver prefixResolver = null;
        if (resolver instanceof ScriptFunction) {
            prefixResolver = new ScriptFunctionPrefixResolver((ScriptFunction) resolver, getWindow().getGlobal());
        }
        else if (resolver instanceof PrefixResolver) {
            prefixResolver = (PrefixResolver) resolver;
        }
        xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression, prefixResolver), type);
        return xPathResult;
    }

    /**
     * Returns a list of elements with the given tag name belonging to the given namespace.
     * @param namespaceURI the namespace URI of elements to look for
     * @param localName is either the local name of elements to look for or the special value "*",
     *                  which matches all elements.
     * @return a live NodeList of found elements in the order they appear in the tree
     */
    @Function
    public Object getElementsByTagNameNS(final Object namespaceURI, final String localName) {
        final HTMLCollection2 collection = new HTMLCollection2(getDomNodeOrDie(), false) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return localName.equals(node.getLocalName());
            }
        };

        return collection;
    }

    /**
     * Returns the value of the {@code images} property.
     * @return the value of the {@code images} property
     */
    @Getter
    public Object getImages() {
        return new HTMLCollection2(getDomNodeOrDie(), false) {
            @Override
            protected boolean isMatching(final DomNode node) {
                return node instanceof HtmlImage;
            }
        };
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Document2.class,
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
            super("Document",
                    staticHandle("constructor", Document2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("Document");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("Document");
        }
    }
}
