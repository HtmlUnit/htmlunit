/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_30;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_31;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_32;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DOCUMENT_CREATE_ELEMENT_EXTENDED_SYNTAX;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XUL_SUPPORT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;

/**
 * A JavaScript object for a Document.
 *
 * @version $Revision$
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
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms531073.aspx">MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">W3C Dom Level 1</a>
 */
@JsxClass
public class Document extends EventNode {

    private static final Log LOG = LogFactory.getLog(Document.class);
    private static final Pattern TAG_NAME_PATTERN = Pattern.compile("\\w+");

    private Window window_;
    private DOMImplementation implementation_;
    private String designMode_;

    /**
     * Sets the Window JavaScript object that encloses this document.
     * @param window the Window JavaScript object that encloses this document
     */
    public void setWindow(final Window window) {
        window_ = window;
    }

    /**
     * Returns the value of the "location" property.
     * @return the value of the "location" property
     */
    @JsxGetter
    public Location getLocation() {
        return window_.getLocation();
    }

    /**
     * Sets the value of the "location" property. The location's default property is "href",
     * so setting "document.location='http://www.sf.net'" is equivalent to setting
     * "document.location.href='http://www.sf.net'".
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms535866.aspx">MSDN documentation</a>
     * @param location the location to navigate to
     * @throws IOException when location loading fails
     */
    @JsxSetter
    public void setLocation(final String location) throws IOException {
        window_.setLocation(location);
    }

    /**
     * Returns the value of the "referrer" property.
     * @return the value of the "referrer" property
     */
    @JsxGetter
    public String getReferrer() {
        final String referrer = getPage().getWebResponse().getWebRequest().getAdditionalHeaders().get("Referer");
        if (referrer == null) {
            return "";
        }
        return referrer;
    }

    /**
     * Gets the JavaScript property "documentElement" for the document.
     * @return the root node for the document
     */
    @JsxGetter
    public Element getDocumentElement() {
        final Object documentElement = getPage().getDocumentElement();
        if (documentElement == null) {
            // for instance with an XML document with parsing error
            return null;
        }
        return (Element) getScriptableFor(documentElement);
    }

    /**
     * Gets the JavaScript property "doctype" for the document.
     * @return the DocumentType of the document
     */
    @JsxGetter
    public SimpleScriptable getDoctype() {
        final Object documentType = getPage().getDoctype();
        if (documentType == null) {
            return null;
        }
        return getScriptableFor(documentType);
    }

    /**
     * Returns a value which indicates whether or not the document can be edited.
     * @return a value which indicates whether or not the document can be edited
     */
    @JsxGetter
    public String getDesignMode() {
        if (designMode_ == null) {
            if (getBrowserVersion().hasFeature(GENERATED_30)) {
                if (getWindow().getWebWindow() instanceof FrameWindow) {
                    designMode_ = "Inherit";
                }
                else {
                    designMode_ = "Off";
                }
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
    @JsxSetter
    public void setDesignMode(final String mode) {
        final boolean ie = getBrowserVersion().hasFeature(GENERATED_31);
        if (ie) {
            if (!"on".equalsIgnoreCase(mode) && !"off".equalsIgnoreCase(mode) && !"inherit".equalsIgnoreCase(mode)) {
                throw Context.reportRuntimeError("Invalid document.designMode value '" + mode + "'.");
            }
            else if (!(getWindow().getWebWindow() instanceof FrameWindow)) {
                // IE ignores designMode changes for documents that aren't in frames.
                return;
            }
            else if ("on".equalsIgnoreCase(mode)) {
                designMode_ = "On";
            }
            else if ("off".equalsIgnoreCase(mode)) {
                designMode_ = "Off";
            }
            else if ("inherit".equalsIgnoreCase(mode)) {
                designMode_ = "Inherit";
            }
        }
        else {
            if ("on".equalsIgnoreCase(mode)) {
                designMode_ = "on";
                final SgmlPage page = getPage();
                if (page instanceof HtmlPage) {
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
     * Returns the page that this document is modeling.
     * @return the page that this document is modeling
     */
    protected SgmlPage getPage() {
        return (SgmlPage) getDomNodeOrDie();
    }

    /**
     * Gets the window in which this document is contained.
     * @return the window
     */
    @JsxGetter(@WebBrowser(FF))
    public Object getDefaultView() {
        return getWindow();
    }

    /**
     * Creates a new document fragment.
     * @return a newly created document fragment
     */
    @JsxFunction
    public Object createDocumentFragment() {
        final DomDocumentFragment fragment = getDomNodeOrDie().getPage().createDomDocumentFragment();
        final DocumentFragment node = new DocumentFragment();
        node.setParentScope(getParentScope());
        node.setPrototype(getPrototype(node.getClass()));
        node.setDomNode(fragment);
        return getScriptableFor(fragment);
    }

    /**
     * Creates a new HTML attribute with the specified name.
     *
     * @param attributeName the name of the attribute to create
     * @return an attribute with the specified name
     */
    @JsxFunction
    public Attr createAttribute(final String attributeName) {
        return (Attr) getPage().createAttribute(attributeName).getScriptObject();
    }

    /**
     * Returns the {@link BoxObject} for the specific element.
     *
     * @param element target for BoxObject
     * @return the BoxObject
     */
    public BoxObject getBoxObjectFor(final HTMLElement element) {
        return element.getBoxObject();
    }

    /**
     * Imports a node from another document to this document.
     * The source node is not altered or removed from the original document;
     * this method creates a new copy of the source node.
     *
     * @param importedNode the node to import
     * @param deep Whether to recursively import the subtree under the specified node; or not
     * @return the imported node that belongs to this Document
     */
    @JsxFunction(@WebBrowser(FF))
    public Object importNode(final Node importedNode, final boolean deep) {
        DomNode domNode = importedNode.getDomNodeOrDie();
        domNode = domNode.cloneNode(deep);
        domNode.processImportNode(this);
        for (final DomNode childNode : domNode.getDescendants()) {
            childNode.processImportNode(this);
        }
        return domNode.getScriptObject();
    }

    /**
     * Returns the implementation object of the current document.
     * @return implementation-specific object
     */
    @JsxGetter
    public DOMImplementation getImplementation() {
        if (implementation_ == null) {
            implementation_ = new DOMImplementation();
            implementation_.setParentScope(getWindow());
            implementation_.setPrototype(getPrototype(implementation_.getClass()));
        }
        return implementation_;
    }

    /**
     * Does nothing special anymore... just like FF.
     * @param type the type of events to capture
     * @see Window#captureEvents(String)
     */
    @JsxFunction(@WebBrowser(FF))
    public void captureEvents(final String type) {
        // Empty.
    }

    /**
     * Adapts any DOM node to resolve namespaces so that an XPath expression can be easily
     * evaluated relative to the context of the node where it appeared within the document.
     * @param nodeResolver the node to be used as a context for namespace resolution
     * @return an XPathNSResolver which resolves namespaces with respect to the definitions
     *         in scope for a specified node
     */
    @JsxFunction(@WebBrowser(FF))
    public XPathNSResolver createNSResolver(final Node nodeResolver) {
        final XPathNSResolver resolver = new XPathNSResolver();
        resolver.setElement(nodeResolver);
        resolver.setParentScope(getWindow());
        resolver.setPrototype(getPrototype(resolver.getClass()));
        return resolver;
    }

    /**
     * Create a new DOM text node with the given data.
     *
     * @param newData the string value for the text node
     * @return the new text node or NOT_FOUND if there is an error
     */
    @JsxFunction
    public Object createTextNode(final String newData) {
        Object result = NOT_FOUND;
        try {
            final DomNode domNode = new DomText(getDomNodeOrDie().getPage(), newData);
            final Object jsElement = getScriptableFor(domNode);

            if (jsElement == NOT_FOUND) {
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
     * Creates a new Comment.
     * @param comment the comment text
     * @return the new Comment
     */
    @JsxFunction
    public Object createComment(final String comment) {
        final DomNode domNode = new DomComment(getDomNodeOrDie().getPage(), comment);
        return getScriptableFor(domNode);
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
    @JsxFunction(@WebBrowser(FF))
    public XPathResult evaluate(final String expression, final Node contextNode,
            final Object resolver, final int type, final Object result) {
        XPathResult xPathResult = (XPathResult) result;
        if (xPathResult == null) {
            xPathResult = new XPathResult();
            xPathResult.setParentScope(getParentScope());
            xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
        }
        xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression), type);
        return xPathResult;
    }

    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName the tag name
     * @return the new HTML element, or NOT_FOUND if the tag is not supported
     */
    @JsxFunction
    public Object createElement(String tagName) {
        Object result = NOT_FOUND;
        try {
            final BrowserVersion browserVersion = getBrowserVersion();

            // FF supports document.createElement('div') or supports document.createElement('<div>')
            // but not document.createElement('<div name="test">')
            // IE supports also document.createElement('<div name="test">')
            if (!browserVersion.hasFeature(JS_DOCUMENT_CREATE_ELEMENT_EXTENDED_SYNTAX)
                  && tagName.startsWith("<") && tagName.endsWith(">")) {
                tagName = tagName.substring(1, tagName.length() - 1);

                final Matcher matcher = TAG_NAME_PATTERN.matcher(tagName);
                if (!matcher.matches()) {
                    LOG.info("createElement: String contains an invalid character: ");
                    throw Context.reportRuntimeError("String contains an invalid character");
                }
            }

            final SgmlPage page = getPage();
            final org.w3c.dom.Element element = page.createElement(tagName);
            if (element instanceof BaseFrameElement) {
                ((BaseFrameElement) element).markAsCreatedByJavascript();
            }
            final Object jsElement = getScriptableFor(element);

            if (jsElement == NOT_FOUND) {
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
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }

    /**
     * Creates a new HTML element with the given tag name, and name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @return the new HTML element, or NOT_FOUND if the tag is not supported
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public Object createElementNS(final String namespaceURI, final String qualifiedName) {
        final org.w3c.dom.Element element;
        final BrowserVersion browserVersion = getBrowserVersion();
        if (browserVersion.hasFeature(XUL_SUPPORT)
                && "http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul".equals(namespaceURI)) {
            // simple hack, no need to implement the XUL objects (at least in a first time)
            element = new HtmlDivision(namespaceURI, qualifiedName, getPage(), null);
        }
        else if (HTMLParser.XHTML_NAMESPACE.equals(namespaceURI)) {
            element = getPage().createElementNS(namespaceURI, qualifiedName);
        }
        else {
            element = new DomElement(namespaceURI, qualifiedName, getPage(), null);
        }
        return getScriptableFor(element);
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    @JsxFunction
    public HTMLCollection getElementsByTagName(final String tagName) {
        final String description = "Document.getElementsByTagName('" + tagName + "')";

        final HTMLCollection collection;
        if ("*".equals(tagName)) {
            collection = new HTMLCollection(getDomNodeOrDie(), false, description) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        else {
            final boolean useLocalName = getBrowserVersion().hasFeature(GENERATED_32);
            final String tagNameLC = tagName.toLowerCase();

            collection = new HTMLCollection(getDomNodeOrDie(), false, description) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    if (useLocalName) {
                        return tagNameLC.equalsIgnoreCase(node.getLocalName());
                    }
                    return tagNameLC.equalsIgnoreCase(node.getNodeName());
                }
            };
        }

        return collection;
    }

    /**
     * Returns a list of elements with the given tag name belonging to the given namespace.
     * @param namespaceURI the namespace URI of elements to look for
     * @param localName is either the local name of elements to look for or the special value "*",
     *                  which matches all elements.
     * @return a live NodeList of found elements in the order they appear in the tree
     */
    @JsxFunction(@WebBrowser(FF))
    public Object getElementsByTagNameNS(final Object namespaceURI, final String localName) {
        final String description = "Document.getElementsByTagNameNS('" + namespaceURI + "', '" + localName + "')";

        final String prefix;
        if (namespaceURI != null && !"*".equals(namespaceURI)) {
            prefix = XmlUtil.lookupPrefix(getPage().getDocumentElement(), Context.toString(namespaceURI));
        }
        else {
            prefix = null;
        }

        final HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), false, description) {
            @Override
            protected boolean isMatching(final DomNode node) {
                if (!localName.equals(node.getLocalName())) {
                    return false;
                }
                if (prefix == null) {
                    return true;
                }
                return true;
            }
        };

        return collection;
    }
}
