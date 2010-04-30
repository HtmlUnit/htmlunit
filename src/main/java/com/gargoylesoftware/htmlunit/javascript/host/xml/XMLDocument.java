/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for XMLDocument.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 */
public class XMLDocument extends Document {

    private static final long serialVersionUID = 1225601711396578064L;
    private static final Log LOG = LogFactory.getLog(XMLDocument.class);

    private boolean async_ = true;
    private boolean preserveWhiteSpace_;
    private XMLDOMParseError parseError_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public XMLDocument() {
        this(null);
    }

    /**
     * Creates a new instance, with associated XmlPage.
     * @param enclosingWindow the window
     */
    public XMLDocument(final WebWindow enclosingWindow) {
        if (enclosingWindow != null) {
            try {
                final XmlPage page = new XmlPage((WebResponse) null, enclosingWindow);
                setDomNode(page);
            }
            catch (final IOException e) {
                throw Context.reportRuntimeError("IOException: " + e);
            }
        }
    }

    /**
     * Sets the <tt>async</tt> attribute.
     * @param async Whether or not to send the request to the server asynchronously
     */
    public void jsxSet_async(final boolean async) {
        this.async_ = async;
    }

    /**
     * Returns Whether or not to send the request to the server asynchronously.
     * @return the <tt>async</tt> attribute
     */
    public boolean jsxGet_async() {
        return async_;
    }

    /**
     * Loads an XML document from the specified location.
     *
     * @param xmlSource a string containing a URL that specifies the location of the XML file
     * @return true if the load succeeded; false if the load failed
     */
    public boolean jsxFunction_load(final String xmlSource) {
        if (async_) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("XMLDocument.load(): 'async' is true, currently treated as false.");
            }
        }
        try {
            final HtmlPage htmlPage = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
            final WebRequest request = new WebRequest(htmlPage.getFullyQualifiedUrl(xmlSource));
            final WebResponse webResponse = getWindow().getWebWindow().getWebClient().loadWebResponse(request);
            final XmlPage page = new XmlPage(webResponse, getWindow().getWebWindow(), false);
            setDomNode(page);
            return true;
        }
        catch (final IOException e) {
            final XMLDOMParseError parseError = jsxGet_parseError();
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
     *
     * @param strXML A string containing the XML string to load into this XML document object
     *        This string can contain an entire XML document or a well-formed fragment.
     * @return true if the load succeeded; false if the load failed
     */
    public boolean jsxFunction_loadXML(final String strXML) {
        try {
            final List<NameValuePair> emptyList = Collections.emptyList();
            final WebResponseData data = new WebResponseData(strXML.getBytes(), HttpStatus.SC_OK, null, emptyList);
            final WebResponse webResponse = new WebResponse(data, (URL) null, (HttpMethod) null, 0);
            final XmlPage page = new XmlPage(webResponse, getWindow().getWebWindow());
            setDomNode(page);
            return true;
        }
        catch (final IOException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error parsing XML\n" + strXML, e);
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleScriptable makeScriptableFor(final DomNode domNode) {
        final SimpleScriptable scriptable;

        // TODO: cleanup, getScriptObject() should be used!!!
        if (domNode instanceof DomElement && !(domNode instanceof HtmlElement)) {
            scriptable = new Element();
        }
        else if (domNode instanceof DomAttr) {
            final Attr attribute;
            if (getPage().getWebClient().getBrowserVersion().isIE()) {
                attribute = new XMLAttr();
            }
            else {
                attribute = new Attr();
            }
            scriptable = attribute;
        }
        else {
            return super.makeScriptableFor(domNode);
        }

        scriptable.setPrototype(getPrototype(scriptable.getClass()));
        scriptable.setParentScope(getParentScope());
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

    /**
     * Gets the JavaScript property "parseError" for the document.
     * @return the ParserError object for the document
     */
    public XMLDOMParseError jsxGet_parseError() {
        if (parseError_ == null) {
            parseError_ = new XMLDOMParseError();
            parseError_.setPrototype(getPrototype(parseError_.getClass()));
            parseError_.setParentScope(getParentScope());
        }
        return parseError_;
    }

    /**
     * Contains the XML representation of the node and all its descendants.
     * @return an XML representation of this node and all its descendants
     */
    @Override
    public String jsxGet_xml() {
        final XMLSerializer seralizer = new XMLSerializer();
        seralizer.setParentScope(getWindow());
        seralizer.setPrototype(getPrototype(seralizer.getClass()));
        return seralizer.jsxFunction_serializeToString(jsxGet_documentElement());
    }

    /**
     * Gets the current white space handling.
     * @return the current white space handling
     */
    public boolean jsxGet_preserveWhiteSpace() {
        return preserveWhiteSpace_;
    }

    /**
     * Specifies the white space handling.
     * @param preserveWhiteSpace white space handling
     */
    public void jsxSet_preserveWhiteSpace(final boolean preserveWhiteSpace) {
        this.preserveWhiteSpace_ = preserveWhiteSpace;
    }

    /**
     * This method is used to set
     * <a href="http://msdn2.microsoft.com/en-us/library/ms766391.aspx">second-level properties</a>
     * on the DOM object.
     *
     * @param name the name of the property to be set
     * @param value the value of the specified property
     */
    public void jsxFunction_setProperty(final String name, final String value) {
        //empty implementation
    }

    /**
     * Applies the specified XPath expression to this node's context and returns the generated list of matching nodes.
     * @param expression a string specifying an XPath expression
     * @return list of the found elements
     */
    public HTMLCollection jsxFunction_selectNodes(final String expression) {
        final HTMLCollection collection = new HTMLCollection(this);
        collection.init(getDomNodeOrDie(), expression);
        return collection;
    }

    /**
     * Applies the specified pattern-matching operation to this node's context and returns the first matching node.
     * @param expression a string specifying an XPath expression
     * @return the first node that matches the given pattern-matching operation
     *         If no nodes match the expression, returns a null value.
     */
    public Object jsxFunction_selectSingleNode(final String expression) {
        final HTMLCollection collection = jsxFunction_selectNodes(expression);
        if (collection.jsxGet_length() > 0) {
            return collection.get(0, collection);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLCollection jsxFunction_getElementsByTagName(final String tagName) {
        final HTMLCollection collection = new HTMLCollection(this);
        collection.init(this.<DomNode>getDomNodeOrDie().getFirstChild(), "//*[local-name()='" + tagName + "']");
        return collection;
    }

    /**
     * Returns the element with the specified ID, as long as it is an HTML element; <tt>null</tt> otherwise.
     * @param id the ID to search for
     * @return the element with the specified ID, as long as it is an HTML element; <tt>null</tt> otherwise
     */
    public Object jsxFunction_getElementById(final String id) {
        final XmlPage xmlPage = (XmlPage) getDomNodeOrDie();
        final Object domElement = xmlPage.getFirstByXPath("//*[@id = \"" + id + "\"]");
        if (domElement == null) {
            return null;
        }

        if (domElement instanceof HtmlElement) {
            return ((HtmlElement) domElement).getScriptObject();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("getElementById(" + id + "): no HTML DOM node found with this ID");
        }
        return null;
    }

    /**
     * Since we are not processing DTD, this method always returns null.
     * @param id the ID to search for
     * @return null
     */
    public Object jsxFunction_nodeFromID(final String id) {
        return null;
    }

    /**
     * Creates a new ProcessingInstruction.
     * @param target the target
     * @param data the data
     * @return the new ProcessingInstruction
     */
    public Object jsxFunction_createProcessingInstruction(final String target, final String data) {
        final DomNode node = ((XmlPage) getPage()).createProcessingInstruction(target, data);
        return getScriptableFor(node);
    }

    /**
     * Creates a new createCDATASection.
     * @param data the data
     * @return the new CDATASection
     */
    public Object jsxFunction_createCDATASection(final String data) {
        final DomCDataSection node = ((XmlPage) getPage()).createCDATASection(data);
        return getScriptableFor(node);
    }

    /**
     * Creates a node using the supplied type, name, and namespace.
     * @param type a value that uniquely identifies the node type
     * @param name the value for the new node's nodeName property
     * @param namespaceURI A string defining the namespace URI.
     *        If specified, the node is created in the context of the namespaceURI parameter
     *        with the prefix specified on the node name.
     *        If the name parameter does not have a prefix, this is treated as the default namespace.
     * @return the newly created node
     */
    public Object jsxFunction_createNode(final Object type, final String name, final Object namespaceURI) {
        switch((short) Context.toNumber(type)) {
            case Node.ELEMENT_NODE:
                return jsxFunction_createElementNS((String) namespaceURI, name);
            case Node.ATTRIBUTE_NODE:
                return jsxFunction_createAttribute(name);

            default:
                throw Context.reportRuntimeError("xmlDoc.createNode(): Unsupported type "
                        + (short) Context.toNumber(type));
        }
    }
}
