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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMNode.<br>
 * Extends the core node with support for data types, namespaces, document type definitions (DTDs), and schemas.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms765513.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Chris Erskine
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(browsers = @WebBrowser(IE))
public class XMLDOMNode extends MSXMLScriptable {

    /** "Live" child nodes collection; has to be a member to have equality (==) working. */
    private XMLDOMNodeList childNodes_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMNode() {
    }

    /**
     * Returns the list of attributes for this node.
     * @return the list of attributes for this node
     */
    @JsxGetter
    public Object getAttributes() {
        return null;
    }

    /**
     * Returns the base name for the name qualified with the namespace.
     * @return the base name for the name qualified with the namespace
     */
    @JsxGetter
    public String getBaseName() {
        final DomNode domNode = getDomNodeOrDie();
        final String baseName = domNode.getLocalName();
        if (baseName == null) {
            return "";
        }
        return baseName;
    }

    /**
     * Returns a node list containing the child nodes.
     * @return a node list containing the child nodes
     */
    @JsxGetter
    public XMLDOMNodeList getChildNodes() {
        if (childNodes_ == null) {
            final DomNode domNode = getDomNodeOrDie();
            final boolean isXmlPage = domNode.getOwnerDocument() instanceof XmlPage;
            final Boolean xmlSpaceDefault = isXMLSpaceDefault(domNode);
            final boolean skipEmptyTextNode = isXmlPage && !Boolean.FALSE.equals(xmlSpaceDefault);

            childNodes_ = new XMLDOMNodeList(domNode, false, "XMLDOMNode.childNodes") {
                @Override
                protected List<DomNode> computeElements() {
                    final List<DomNode> response = new ArrayList<DomNode>();
                    for (final DomNode child : domNode.getChildren()) {
                        //IE: XmlPage ignores all empty text nodes
                        if (skipEmptyTextNode && child instanceof DomText && !(child instanceof DomCDataSection)
                            && StringUtils.isBlank(((DomText) child).getNodeValue())) { //and 'xml:space' is 'default'
                            continue;
                        }
                        response.add(child);
                    }

                    return response;
                }
            };
        }
        return childNodes_;
    }

    /**
     * Recursively checks whether "xml:space" attribute is set to "default".
     * @param node node to start checking from
     * @return {@link Boolean#TRUE} if "default" is set, {@link Boolean#FALSE} for other value,
     *         or null if nothing is set.
     */
    private static Boolean isXMLSpaceDefault(DomNode node) {
        for ( ; node instanceof DomElement; node = node.getParentNode()) {
            final String value = ((DomElement) node).getAttribute("xml:space");
            if (!value.isEmpty()) {
                if ("default".equals(value)) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        }
        return null;
    }

    /**
     * Returns the data type for this node.
     * @return the data type for this node
     */
    @JsxGetter
    public String getDataType() {
        return null;
    }

    /**
     * Returns the definition of the node in the document type definition (DTD) or schema.
     * @return the definition of the node in the document type definition (DTD) or schema
     */
    @JsxGetter
    public String getDefinition() {
        return null;
    }

    /**
     * Returns the first child of this node.
     * @return the first child of this node
     */
    @JsxGetter
    public XMLDOMNode getFirstChild() {
        final DomNode domNode = getDomNodeOrDie();
        return getJavaScriptNode(domNode.getFirstChild());
    }

    /**
     * Returns the last child node.
     * @return the last child node
     */
    @JsxGetter
    public XMLDOMNode getLastChild() {
        final DomNode domNode = getDomNodeOrDie();
        return getJavaScriptNode(domNode.getLastChild());
    }

    /**
     * Returns the Uniform Resource Identifier (URI) for the namespace.
     * @return the Uniform Resource Identifier (URI) for the namespace
     */
    @JsxGetter
    public String getNamespaceURI() {
        final DomNode domNode = getDomNodeOrDie();
        final String namespaceURI = domNode.getNamespaceURI();
        if (namespaceURI == null) {
            return "";
        }
        return namespaceURI;
    }

    /**
     * Returns the next sibling of this node in the parent's child list.
     * @return the next sibling of this node in the parent's child list
     */
    @JsxGetter
    public XMLDOMNode getNextSibling() {
        final DomNode domNode = getDomNodeOrDie();
        return getJavaScriptNode(domNode.getNextSibling());
    }

    /**
     * Returns the qualified name for attribute, document type, element, entity, or notation nodes. Returns a fixed
     * string for all other node types.
     * @return the qualified name
     */
    @JsxGetter
    public String getNodeName() {
        final DomNode domNode = getDomNodeOrDie();
        return domNode.getNodeName();
    }

    /**
     * Returns the XML Document Object Model (DOM) node type, which determines valid values and whether the node can
     * have child nodes.
     * @return the XML Document Object Model (DOM) node type
     */
    @JsxGetter
    public short getNodeType() {
        final DomNode domNode = getDomNodeOrDie();
        return domNode.getNodeType();
    }

    /**
     * Returns the text associated with the node.
     * @return the text associated with the node
     */
    @JsxGetter
    public String getNodeValue() {
        final DomNode domNode = getDomNodeOrDie();
        return domNode.getNodeValue();
    }

    /**
     * Sets the text associated with the node.
     * @param value the text associated with the node
     */
    @JsxSetter
    public void setNodeValue(final String value) {
        if (value == null || "null".equals(value)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final DomNode domNode = getDomNodeOrDie();
        domNode.setNodeValue(value);
    }

    /**
     * Returns the root of the document that contains the node.
     * @return the root of the document that contains the node
     */
    @JsxGetter
    public Object getOwnerDocument() {
        final DomNode domNode = getDomNodeOrDie();
        final Object document = domNode.getOwnerDocument();
        if (document == null) {
            return null;
        }
        return ((SgmlPage) document).getScriptObject();
    }

    /**
     * Returns the parent node.
     * @return the parent node
     */
    @JsxGetter
    public Object getParentNode() {
        final DomNode domNode = getDomNodeOrDie();
        return getJavaScriptNode(domNode.getParentNode());
    }

    /**
     * Returns the namespace prefix.
     * @return the namespace prefix
     */
    @JsxGetter
    public String getPrefix() {
        final DomNode domNode = getDomNodeOrDie();
        final String prefix = domNode.getPrefix();
        if (prefix == null || domNode.getHtmlPageOrNull() != null) {
            return "";
        }
        return prefix;
    }

    /**
     * Returns the previous sibling of the node in the parent's child list.
     * @return the previous sibling of the node in the parent's child list
     */
    @JsxGetter
    public XMLDOMNode getPreviousSibling() {
        final DomNode domNode = getDomNodeOrDie();
        return getJavaScriptNode(domNode.getPreviousSibling());
    }

    /**
     * Returns the text content of the node or the concatenated text representing the node and its descendants.
     * @return the text content of the node and its descendants
     */
    @JsxGetter
    public Object getText() {
        final DomNode domNode = getDomNodeOrDie();
        return domNode.getTextContent();
    }

    /**
     * Replace all child elements of this element by the supplied text.
     * @param text the new text of this node
     */
    @JsxSetter
    public void setText(final Object text) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.setTextContent(text == null ? null : Context.toString(text));
    }

    /**
     * Returns the XML representation of the node and all its descendants.
     * @return the XML representation of the node and all its descendants
     */
    @JsxGetter
    public Object getXml() {
        final DomNode domNode = getDomNodeOrDie();
        String xml;
        if (this instanceof XMLDOMElement) {
            final boolean preserveWhiteSpace =
                    ((XMLDOMDocument) getOwnerDocument()).isPreserveWhiteSpaceDuringLoad();

            final XMLSerializer serializer = new XMLSerializer(preserveWhiteSpace);
            xml = serializer.serializeToString(this);
        }
        else {
            xml = domNode.asXml();
        }

        if (xml.endsWith("\r\n")) {
            xml = xml.substring(0, xml.length() - 2);
        }
        return xml;
    }

    /**
     * Appends a new child as the last child of the node.
     * @param newChild the new child node to be appended at the end of the list of children belonging to this node
     * @return the new child node successfully appended to the list
     */
    @JsxFunction
    public Object appendChild(final Object newChild) {
        if (newChild == null || "null".equals(newChild)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        Object appendedChild = null;
        if (newChild instanceof XMLDOMNode) {
            final XMLDOMNode childNode = (XMLDOMNode) newChild;

            // Get XML node for the DOM node passed in
            final DomNode childDomNode = childNode.getDomNodeOrDie();

            // Get the parent XML node that the child should be added to.
            final DomNode parentNode = getDomNodeOrDie();

            // Append the child to the parent node
            parentNode.appendChild(childDomNode);
            appendedChild = newChild;

            // if the parentNode has null parentNode in IE,
            // create a DocumentFragment to be the parentNode's parentNode.
            if (!(parentNode instanceof SgmlPage)
                    && !(this instanceof XMLDOMDocumentFragment) && parentNode.getParentNode() == null) {
                final DomDocumentFragment fragment = parentNode.getPage().createDomDocumentFragment();
                fragment.appendChild(parentNode);
            }
        }
        return appendedChild;
    }

    /**
     * Clones a new node.
     * @param deep flag that indicates whether to recursively clone all nodes that are descendants of this node;
     *     if <code>true</code>, creates a clone of the complete tree below this node,
     *     if <code>false</code>, clones this node and its attributes only
     * @return the newly created clone node
     */
    @JsxFunction
    public Object cloneNode(final boolean deep) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode clonedNode = domNode.cloneNode(deep);

        final XMLDOMNode jsClonedNode = getJavaScriptNode(clonedNode);
        return jsClonedNode;
    }

    /**
     * Provides a fast way to determine whether a node has children.
     * @return boolean <code>true</code> if this node has children
     */
    @JsxFunction
    public boolean hasChildNodes() {
        final DomNode domNode = getDomNodeOrDie();
        return domNode.getChildren().iterator().hasNext();
    }

    /**
     * Inserts a child node to the left of the specified node, or at the end of the list.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return on success, returns the child node that was inserted
     */
    @JsxFunction
    public static Object insertBefore(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        return ((XMLDOMNode) thisObj).insertBeforeImpl(args);
    }

    /**
     * Inserts a child node to the left of the specified node, or at the end of the list.
     * @param args the arguments
     * <ul>
     *   <li>args[0]=<b>newChild</b> the new node to be inserted
     *   <li>args[1]=<b>refChild</b> the reference node; the <code>newChild</code> parameter is inserted to the left
     *   of the <code>refChild</code> parameter; if <code>null</code>, the <code>newChild</code> parameter is inserted
     *   at the end of the child list
     * </ul>
     * @return on success, returns the child node that was inserted
     */
    protected Object insertBeforeImpl(final Object[] args) {
        final Object newChildObject = args[0];
        final Object refChildObject;
        if (args.length > 1) {
            refChildObject = args[1];
        }
        else {
            refChildObject = Undefined.instance;
        }
        Object appendedChild = null;

        if (newChildObject instanceof XMLDOMNode) {
            final XMLDOMNode newChild = (XMLDOMNode) newChildObject;
            final DomNode newChildNode = newChild.getDomNodeOrDie();

            if (newChildNode instanceof DomDocumentFragment) {
                final DomDocumentFragment fragment = (DomDocumentFragment) newChildNode;
                for (final DomNode child : fragment.getChildren()) {
                    insertBeforeImpl(new Object[] {child.getScriptObject(), refChildObject});
                }
                return newChildObject;
            }
            final DomNode refChildNode;
            // IE accepts non standard calls with only one arg
            if (refChildObject == Undefined.instance) {
                if (args.length > 1) {
                    throw Context.reportRuntimeError("Invalid argument.");
                }
                refChildNode = null;
            }
            else if (refChildObject != null) {
                refChildNode = ((XMLDOMNode) refChildObject).getDomNodeOrDie();
            }
            else {
                refChildNode = null;
            }

            final DomNode domNode = getDomNodeOrDie();
            // Append the child to the parent node
            if (refChildNode != null) {
                refChildNode.insertBefore(newChildNode);
                appendedChild = newChildObject;
            }
            else {
                domNode.appendChild(newChildNode);
                appendedChild = newChildObject;
            }

            // if parentNode is null in IE, create a DocumentFragment to be the parentNode
            if (domNode.getParentNode() == null) {
                final DomDocumentFragment fragment = domNode.getPage().createDomDocumentFragment();
                fragment.appendChild(domNode);
            }
        }
        return appendedChild;
    }

    /**
     * Removes the specified child node from the list of children and returns it.
     * @param childNode the child node to be removed from the list of children of this node
     * @return the removed child node
     */
    @JsxFunction
    public Object removeChild(final Object childNode) {
        Object removedChild = null;

        if (childNode instanceof XMLDOMNode) {
            // Get XML node for the DOM node passed in
            final DomNode childDomNode = ((XMLDOMNode) childNode).getDomNodeOrDie();

            // Remove the child from the parent node
            childDomNode.remove();
            removedChild = childNode;
        }
        return removedChild;
    }

    /**
     * Replaces the specified old child node with the supplied new child node.
     * @param newChild the new child that is to replace the old child; if <code>null</code>,
     * <code>oldChild</code> is removed without a replacement
     * @param oldChild the old child that is to be replaced by the new child
     * @return the old child that is replaced
     */
    @JsxFunction
    public Object replaceChild(final Object newChild, final Object oldChild) {
        Object removedChild = null;

        if (newChild instanceof XMLDOMDocumentFragment) {
            final XMLDOMDocumentFragment fragment = (XMLDOMDocumentFragment) newChild;
            XMLDOMNode firstNode = null;
            final XMLDOMNode refChildObject = ((XMLDOMNode) oldChild).getNextSibling();
            for (final DomNode node : fragment.getDomNodeOrDie().getChildren()) {
                if (firstNode == null) {
                    replaceChild(node.getScriptObject(), oldChild);
                    firstNode = (XMLDOMNode) node.getScriptObject();
                }
                else {
                    insertBeforeImpl(new Object[] {node.getScriptObject(), refChildObject});
                }
            }
            if (firstNode == null) {
                removeChild(oldChild);
            }
            removedChild = oldChild;
        }
        else if (newChild instanceof XMLDOMNode && oldChild instanceof XMLDOMNode) {
            final XMLDOMNode newChildNode = (XMLDOMNode) newChild;

            // Get XML nodes for the DOM nodes passed in
            final DomNode newChildDomNode = newChildNode.getDomNodeOrDie();
            final DomNode oldChildDomNode = ((XMLDOMNode) oldChild).getDomNodeOrDie();

            // Replace the old child with the new child.
            oldChildDomNode.replace(newChildDomNode);
            removedChild = oldChild;
        }

        return removedChild;
    }

    /**
     * Applies the specified pattern-matching operation to this node's context and returns the list of matching nodes.
     * @param expression a string specifying an XPath expression
     * @return the collection of nodes selected by applying the given pattern-matching operation;
     *     if no nodes are selected, returns an empty collection
     */
    @JsxFunction
    public XMLDOMSelection selectNodes(final String expression) {
        final DomNode domNode = getDomNodeOrDie();
        final boolean attributeChangeSensitive = expression.contains("@");
        final XMLDOMSelection collection = new XMLDOMSelection(domNode, attributeChangeSensitive,
                "XMLDOMNode.selectNodes('" + expression + "')") {
            @Override
            protected List<DomNode> computeElements() {
                @SuppressWarnings("unchecked")
                final List<DomNode> nodes = (List<DomNode>) domNode.getByXPath(expression);
                return new ArrayList<DomNode>(nodes);
            }
        };
        return collection;
    }

    /**
     * Applies the specified pattern-matching operation to this node's context and returns the first matching node.
     * @param expression a string specifying an XPath expression
     * @return the first node that matches the given pattern-matching operation;
     *     if no nodes match the expression, returns <code>null</code>
     */
    @JsxFunction
    public Object selectSingleNode(final String expression) {
        final XMLDOMNodeList collection = selectNodes(expression);
        if (collection.getLength() > 0) {
            return collection.get(0, collection);
        }
        return null;
    }

    /**
     * Gets the JavaScript node for a given {@link DomNode}.
     * @param domNode the {@link DomNode}
     * @return the JavaScript node or null if the {@link DomNode} was null
     */
    protected XMLDOMNode getJavaScriptNode(final DomNode domNode) {
        if (domNode == null) {
            return null;
        }
        return (XMLDOMNode) getScriptableFor(domNode);
    }
}
