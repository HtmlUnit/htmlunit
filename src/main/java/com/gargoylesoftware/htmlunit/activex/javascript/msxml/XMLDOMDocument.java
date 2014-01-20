/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMDocument.<br>
 * Represents the top level of the XML source. Includes members for retrieving and creating all other XML objects.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms756987.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Chuck Dumont
 * @author Frank Danek
 */
@JsxClass(browsers = @WebBrowser(IE))
public class XMLDOMDocument extends XMLDOMNode {

    private static final Log LOG = LogFactory.getLog(XMLDOMDocument.class);

    private boolean async_ = true;
    private XMLDOMImplementation implementation_;
    private boolean preserveWhiteSpace_;
    private boolean preserveWhiteSpaceDuringLoad_ = true;
    private XMLDOMParseError parseError_;
    private Map<String, String> properties_ = new HashMap<String, String>();
    private String url_ = "";

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMDocument() {
        this(null);
    }

    /**
     * Creates a new instance, with associated {@link XmlPage}.
     * @param enclosingWindow the window
     */
    public XMLDOMDocument(final WebWindow enclosingWindow) {
        if (enclosingWindow != null) {
            try {
                final XmlPage page = new XmlPage((WebResponse) null, enclosingWindow, true, false);
                setDomNode(page);
            }
            catch (final IOException e) {
                throw Context.reportRuntimeError("IOException: " + e);
            }
        }
    }

    /**
     * Returns if asynchronous download is permitted.
     * @return if asynchronous download is permitted
     */
    @JsxGetter
    public boolean getAsync() {
        return async_;
    }

    /**
     * Sets if asynchronous download is permitted.
     * @param async if asynchronous download is permitted
     */
    @JsxSetter
    public void setAsync(final boolean async) {
        async_ = async;
    }

    /**
     * Returns the document type node that specifies the DTD for this document.
     * @return the document type node that specifies the DTD for this document
     */
    @JsxGetter
    public XMLDOMDocumentType getDoctype() {
        final Object documentType = getPage().getDoctype();
        if (documentType == null) {
            return null;
        }
        return (XMLDOMDocumentType) getScriptableFor(documentType);
    }

    /**
     * Returns the root element of the document.
     * @return the root element of the document
     */
    @JsxGetter
    public XMLDOMElement getDocumentElement() {
        final Object documentElement = getPage().getDocumentElement();
        if (documentElement == null) {
            // for instance with an XML document with parsing error
            return null;
        }
        return (XMLDOMElement) getScriptableFor(documentElement);
    }

    /**
     * Sets the root element of the document.
     * @param element the root element of the document
     */
    @JsxSetter
    public void setDocumentElement(final XMLDOMElement element) {
        if (element == null) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final XMLDOMElement documentElement = getDocumentElement();
        if (documentElement != null) {
            documentElement.getDomNodeOrDie().remove();
        }

        appendChild(element);
    }

    /**
     * Returns the implementation object for the document.
     * @return the implementation object for the document
     */
    @JsxGetter
    public XMLDOMImplementation getImplementation() {
        if (implementation_ == null) {
            implementation_ = new XMLDOMImplementation();
            implementation_.setParentScope(getWindow());
            implementation_.setPrototype(getPrototype(implementation_.getClass()));
        }
        return implementation_;
    }

    /**
     * Attempting to set the value of documents generates an error.
     * @param value the new value to set
     */
    @Override
    public void setNodeValue(final String value) {
        if (value == null || "null".equals(value)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        throw Context.reportRuntimeError("This operation cannot be performed with a node of type DOCUMENT.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getOwnerDocument() {
        return null;
    }

    /**
     * Returns a parse error object that contains information about the last parsing error.
     * @return a parse error object
     */
    @JsxGetter
    public XMLDOMParseError getParseError() {
        if (parseError_ == null) {
            parseError_ = new XMLDOMParseError();
            parseError_.setParentScope(getParentScope());
            parseError_.setPrototype(getPrototype(parseError_.getClass()));
        }
        return parseError_;
    }

    /**
     * Returns the default white space handling.
     * @return the default white space handling
     */
    @JsxGetter
    public boolean getPreserveWhiteSpace() {
        return preserveWhiteSpace_;
    }

    /**
     * Set the default white space handling.
     * @param preserveWhiteSpace the default white space handling
     */
    @JsxSetter
    public void setPreserveWhiteSpace(final boolean preserveWhiteSpace) {
        preserveWhiteSpace_ = preserveWhiteSpace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getText() {
        final XMLDOMElement element = getDocumentElement();
        if (element == null) {
            return "";
        }
        return element.getText();
    }

    /**
     * Attempting to set the text of documents generates an error.
     * @param text the new text of this node
     */
    @Override
    public void setText(final Object text) {
        if (text == null || "null".equals(text)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        throw Context.reportRuntimeError("This operation cannot be performed with a node of type DOCUMENT.");
    }

    /**
     * Returns the URL for the last loaded XML document.
     * @return the URL for the last loaded XML document
     */
    @JsxGetter
    public String getUrl() {
        return url_;
    }

    /**
     * Returns the XML representation of the node and all its descendants.
     * @return an XML representation of this node and all its descendants
     */
    @Override
    @JsxGetter
    public String getXml() {
        final XMLSerializer seralizer = new XMLSerializer(preserveWhiteSpaceDuringLoad_);
        return seralizer.serializeToString(getDocumentElement());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object appendChild(final Object newChild) {
        if (newChild == null || "null".equals(newChild) || !(newChild instanceof XMLDOMNode)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (newChild instanceof XMLDOMCDATASection) {
            throw Context.reportRuntimeError("This operation cannot be performed with a node of type CDATA.");
        }
        if (newChild instanceof XMLDOMText) {
            throw Context.reportRuntimeError("This operation cannot be performed with a node of type TEXT.");
        }
        if (newChild instanceof XMLDOMElement && getDocumentElement() != null) {
            throw Context.reportRuntimeError("Only one top level element is allowed in an XML document.");
        }
        if (newChild instanceof XMLDOMDocumentFragment) {
            boolean elementFound = false;
            XMLDOMNode child = ((XMLDOMDocumentFragment) newChild).getFirstChild();
            while (child != null) {
                if (child instanceof XMLDOMCDATASection) {
                    throw Context.reportRuntimeError("This operation cannot be performed with a node of type CDATA.");
                }
                if (child instanceof XMLDOMText) {
                    throw Context.reportRuntimeError("This operation cannot be performed with a node of type TEXT.");
                }
                if (child instanceof XMLDOMElement) {
                    if (elementFound) {
                        throw Context.reportRuntimeError("Only one top level element is allowed in an XML document.");
                    }
                    elementFound = true;
                }
                child = child.getNextSibling();
            }
        }

        return super.appendChild(newChild);
    }

    /**
     * Creates a new attribute with the specified name.
     *
     * @param name the name of the new attribute object
     * @return the new attribute object
     */
    @JsxFunction
    public Object createAttribute(final String name) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (StringUtils.isBlank(name) || name.indexOf('<') >= 0 || name.indexOf('>') >= 0) {
            throw Context.reportRuntimeError("To create a node of type ATTR a valid name must be given.");
        }

        final DomAttr domAttr = getPage().createAttribute(name);
        return getScriptableFor(domAttr);
    }

    /**
     * Creates a CDATA section node that contains the supplied data.
     * @param data the value to be supplied to the new CDATA section object's <code>nodeValue</code> property
     * @return the new CDATA section object
     */
    @JsxFunction
    public Object createCDATASection(final String data) {
        final DomCDataSection domCDATASection = ((XmlPage) getPage()).createCDATASection(data);
        return getScriptableFor(domCDATASection);
    }

    /**
     * Creates a comment node that contains the supplied data.
     * @param data the value to be supplied to the new comment object's <code>nodeValue</code> property
     * @return the new comment object
     */
    @JsxFunction
    public Object createComment(final String data) {
        final DomComment domComment = new DomComment(getPage(), data);
        return getScriptableFor(domComment);
    }

    /**
     * Creates an empty document fragment object.
     * @return the new document fragment object
     */
    @JsxFunction
    public Object createDocumentFragment() {
        final DomDocumentFragment domDocumentFragment = getPage().createDomDocumentFragment();
        return getScriptableFor(domDocumentFragment);
    }

    /**
     * Creates an element node using the specified name.
     * @param tagName the name for the new element node
     * @return the new element object or <code>NOT_FOUND</code> if the tag is not supported
     */
    @JsxFunction
    public Object createElement(final String tagName) {
        if (tagName == null || "null".equals(tagName)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (StringUtils.isBlank(tagName) || tagName.indexOf('<') >= 0 || tagName.indexOf('>') >= 0) {
            throw Context.reportRuntimeError("To create a node of type ELEMENT a valid name must be given.");
        }

        Object result = NOT_FOUND;
        try {
            final DomElement domElement = (DomElement) getPage().createElement(tagName);
            final Object jsElement = getScriptableFor(domElement);

            if (jsElement == NOT_FOUND) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("createElement(" + tagName
                        + ") cannot return a result as there isn't a JavaScript object for the element "
                        + domElement.getClass().getName());
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
     * Creates a node using the supplied type, name, and namespace.
     * @param type a value that uniquely identifies the node type
     * @param name the value for the new node's <code>nodeName</code> property
     * @param namespaceURI the namespace URI.
     *        If specified, the node is created in the context of the namespaceURI parameter
     *        with the prefix specified on the node name.
     *        If the name parameter does not have a prefix, this is treated as the default namespace.
     * @return the newly created node
     */
    @JsxFunction
    public Object createNode(final Object type, final String name, final Object namespaceURI) {
        switch((short) Context.toNumber(type)) {
            case Node.ELEMENT_NODE:
                return createElementNS((String) namespaceURI, name);
            case Node.ATTRIBUTE_NODE:
                return createAttribute(name);

            default:
                throw Context.reportRuntimeError("xmlDoc.createNode(): Unsupported type "
                        + (short) Context.toNumber(type));
        }
    }

    /**
     * Creates a new HTML element with the given tag name, and name.
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @return the new element or NOT_FOUND if the tag is not supported
     */
    private Object createElementNS(final String namespaceURI, final String qualifiedName) {
        final org.w3c.dom.Element element;
        if ("http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul".equals(namespaceURI)) {
            throw Context.reportRuntimeError("XUL not available");
        }
        else if (HTMLParser.XHTML_NAMESPACE.equals(namespaceURI)
                || HTMLParser.SVG_NAMESPACE.equals(namespaceURI)) {
            element = getPage().createElementNS(namespaceURI, qualifiedName);
        }
        else {
            element = new DomElement(namespaceURI, qualifiedName, getPage(), null);
        }
        return getScriptableFor(element);
    }

    /**
     * Creates a processing instruction node that contains the supplied target and data.
     * @param target the target part of the processing instruction
     * @param data the rest of the processing instruction preceding the closing ?&gt; characters
     * @return the new processing instruction object
     */
    @JsxFunction
    public Object createProcessingInstruction(final String target, final String data) {
        final DomProcessingInstruction domProcessingInstruction =
                ((XmlPage) getPage()).createProcessingInstruction(target, data);
        return getScriptableFor(domProcessingInstruction);
    }

    /**
     * Creates a text node that contains the supplied data.
     * @param data the value to be supplied to the new text object's <code>nodeValue</code> property
     * @return the new text object or <code>NOT_FOUND</code> if there is an error
     */
    @JsxFunction
    public Object createTextNode(final String data) {
        Object result = NOT_FOUND;
        try {
            final DomText domText = new DomText(getPage(), data);
            final Object jsElement = getScriptableFor(domText);

            if (jsElement == NOT_FOUND) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("createTextNode(" + data
                            + ") cannot return a result as there isn't a JavaScript object for the DOM node "
                            + domText.getClass().getName());
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
     * Returns a collection of elements that have the specified name.
     * @param tagName the element name to find; the <code>tagName</code> value '*' returns all elements in the document
     * @return a collection of elements that match the specified name
     */
    @JsxFunction
    public XMLDOMNodeList getElementsByTagName(final String tagName) {
        final DomNode firstChild = getDomNodeOrDie().getFirstChild();
        if (firstChild == null) {
            return XMLDOMNodeList.emptyCollection(this);
        }

        final XMLDOMNodeList collection = new XMLDOMNodeList(getDomNodeOrDie(), false,
                "XMLDOMDocument.getElementsByTagName") {
            @Override
            protected boolean isMatching(final DomNode node) {
                return node.getNodeName().equals(tagName);
            }
        };

        return collection;
    }

    /**
     * Retrieves the value of one of the second-level properties that are set either by default or using the
     * {@link #setProperty(String, String)} method.
     * @param name the name of the property
     * @return the property value
     */
    @JsxFunction
    public String getProperty(final String name) {
        return properties_.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object insertBeforeImpl(final Object[] args) {
        final Object newChild = args[0];
        if (newChild == null || "null".equals(newChild) || !(newChild instanceof XMLDOMNode)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (newChild instanceof XMLDOMCDATASection) {
            throw Context.reportRuntimeError("This operation cannot be performed with a node of type CDATA.");
        }
        if (newChild instanceof XMLDOMText) {
            throw Context.reportRuntimeError("This operation cannot be performed with a node of type TEXT.");
        }
        if (newChild instanceof XMLDOMElement && getDocumentElement() != null) {
            throw Context.reportRuntimeError("Only one top level element is allowed in an XML document.");
        }
        if (newChild instanceof XMLDOMDocumentFragment) {
            boolean elementFound = false;
            XMLDOMNode child = ((XMLDOMDocumentFragment) newChild).getFirstChild();
            while (child != null) {
                if (child instanceof XMLDOMCDATASection) {
                    throw Context.reportRuntimeError("This operation cannot be performed with a node of type CDATA.");
                }
                if (child instanceof XMLDOMText) {
                    throw Context.reportRuntimeError("This operation cannot be performed with a node of type TEXT.");
                }
                if (child instanceof XMLDOMElement) {
                    if (elementFound) {
                        throw Context.reportRuntimeError("Only one top level element is allowed in an XML document.");
                    }
                    elementFound = true;
                }
                child = child.getNextSibling();
            }
        }
        if (args.length != 2) {
            throw Context.reportRuntimeError("Wrong number of arguments or invalid property assignment.");
        }

        return super.insertBeforeImpl(args);
    }

    /**
     * Loads an XML document from the specified location.
     * @param xmlSource a URL that specifies the location of the XML file
     * @return <code>true</code> if the load succeeded; <code>false</code> if the load failed
     */
    @JsxFunction
    public boolean load(final String xmlSource) {
        if (async_) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("XMLDOMDocument.load(): 'async' is true, currently treated as false.");
            }
        }
        try {
            final HtmlPage htmlPage = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
            final URL fullyQualifiedURL = htmlPage.getFullyQualifiedUrl(xmlSource);
            final WebRequest request = new WebRequest(fullyQualifiedURL);
            final WebResponse webResponse = getWindow().getWebWindow().getWebClient().loadWebResponse(request);
            final XmlPage page = new XmlPage(webResponse, getWindow().getWebWindow(), false, false);
            setDomNode(page);

            preserveWhiteSpaceDuringLoad_ = preserveWhiteSpace_;
            url_ = fullyQualifiedURL.toExternalForm();
            return true;
        }
        catch (final IOException e) {
            final XMLDOMParseError parseError = getParseError();
            parseError.setErrorCode(-1);
            parseError.setFilepos(1);
            parseError.setLine(1);
            parseError.setLinepos(1);
            parseError.setReason(e.getMessage());
            parseError.setSrcText("xml");
            parseError.setUrl(xmlSource);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error parsing XML from '" + xmlSource + "'", e);
            }
            return false;
        }
    }

    /**
     * Loads an XML document using the supplied string.
     * @param strXML the XML string to load into this XML document object;
     *     this string can contain an entire XML document or a well-formed fragment
     * @return <code>true</code> if the load succeeded; <code>false</code> if the load failed
     */
    @JsxFunction
    public boolean loadXML(final String strXML) {
        try {
            final WebWindow webWindow = getWindow().getWebWindow();

            final WebResponse webResponse = new StringWebResponse(strXML, webWindow.getEnclosedPage().getUrl());
            final XmlPage page = new XmlPage(webResponse, webWindow, false, false);
            setDomNode(page);

            preserveWhiteSpaceDuringLoad_ = preserveWhiteSpace_;
            url_ = "";
            return true;
        }
        catch (final IOException e) {
            final XMLDOMParseError parseError = getParseError();
            parseError.setErrorCode(-1);
            parseError.setFilepos(1);
            parseError.setLine(1);
            parseError.setLinepos(1);
            parseError.setReason(e.getMessage());
            parseError.setSrcText("xml");
            parseError.setUrl("");
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error parsing XML\n" + strXML, e);
            }
            return false;
        }
    }

    /**
     * Returns the node that matches the ID attribute.
     * @param id the value of the ID to match
     * @return since we are not processing DTD, this method always returns <code>null</code>
     */
    @JsxFunction
    public Object nodeFromID(final String id) {
        return null;
    }

    /**
     * This method is used to set second-level properties on the DOM object.
     * @param name the name of the property to be set
     * @param value the value of the specified property
     */
    @JsxFunction
    public void setProperty(final String name, final String value) {
        properties_.put(name, value);
    }

    /**
     * @return the preserveWhiteSpaceDuringLoad
     */
    public boolean isPreserveWhiteSpaceDuringLoad() {
        return preserveWhiteSpaceDuringLoad_;
    }

    /**
     * @return the page that this document is modeling
     */
    protected SgmlPage getPage() {
        return (SgmlPage) getDomNodeOrDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MSXMLScriptable makeScriptableFor(final DomNode domNode) {
        final MSXMLScriptable scriptable;

        if (domNode instanceof DomElement && !(domNode instanceof HtmlElement)) {
            scriptable = new XMLDOMElement();
        }
        else if (domNode instanceof DomAttr) {
            scriptable = new XMLDOMAttribute();
        }
        else {
            return (MSXMLScriptable) super.makeScriptableFor(domNode);
        }

        scriptable.setParentScope(this);
        scriptable.setPrototype(getPrototype(scriptable.getClass()));
        scriptable.setDomNode(domNode);

        return scriptable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initParentScope(final DomNode domNode, final SimpleScriptable scriptable) {
        scriptable.setParentScope(getParentScope());
    }
}
