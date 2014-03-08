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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Node;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMElement.<br>
 * Represents the element object.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms760248.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = DomElement.class, browsers = @WebBrowser(IE))
public class XMLDOMElement extends XMLDOMNode {

    private XMLDOMNamedNodeMap attributes_;
    private Map<String, XMLDOMNodeList> elementsByTagName_; // for performance and for equality (==)

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMElement() {
    }

    /**
     * Returns the list of attributes for this element.
     * @return the list of attributes for this element
     */
    @Override
    public Object getAttributes() {
        if (attributes_ == null) {
            attributes_ = createAttributesObject();
        }
        return attributes_;
    }

    /**
     * Creates the JS object for the property attributes. This object will the be cached.
     * @return the JS object
     */
    protected XMLDOMNamedNodeMap createAttributesObject() {
        return new XMLDOMNamedNodeMap(getDomNodeOrDie());
    }

    /**
     * Attempting to set the value of elements generates an error.
     * @param newValue the new value to set
     */
    @Override
    public void setNodeValue(final String newValue) {
        if (newValue == null || "null".equals(newValue)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        throw Context.reportRuntimeError("This operation cannot be performed with a node of type ELEMENT.");
    }

    /**
     * Returns the element name.
     * @return the element name
     */
    @JsxGetter
    public String getTagName() {
        return getNodeName();
    }

    /**
     * Returns a string that represents the element content. This will also include the text content from all child
     * elements, concatenated in document order.
     * @return a string that represents the element content
     */
    @Override
    public String getText() {
        final StringBuilder buffer = new StringBuilder();
        toText(getDomNodeOrDie(), buffer);
        if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) == '\n') {
            return buffer.substring(0, buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * Replaces all children of this element with the supplied value.
     * @param value the new value for the contents of this node
     */
    @Override
    public void setText(final Object value) {
        if (value == null || "null".equals(value)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        super.setText(value);
    }

    private void toText(final DomNode node, final StringBuilder buffer) {
        switch (node.getNodeType()) {
            case Node.DOCUMENT_TYPE_NODE:
            case Node.NOTATION_NODE:
                return;
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
                buffer.append(node.getNodeValue());
                break;
            default:
        }
        boolean lastWasElement = false;
        for (final DomNode child : node.getChildren()) {
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    lastWasElement = true;
                    toText(child, buffer);
                    break;

                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    if (StringUtils.isBlank(child.getNodeValue())) {
                        if (lastWasElement) {
                            buffer.append(' ');
                        }
                        lastWasElement = false;
                        break;
                    }
                    lastWasElement = false;
                    buffer.append(child.getNodeValue());
                    break;
                default:
                    lastWasElement = false;
            }
        }
    }

    /**
     * Returns the value of the attribute.
     * @param name the name of the attribute to return
     * @return the value of the specified attribute, <code>null</code> if the named attribute does not have a
     *     specified value
     */
    @JsxFunction
    public Object getAttribute(final String name) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (StringUtils.isEmpty(name)) {
            throw Context.reportRuntimeError("The empty string '' is not a valid name.");
        }

        final String value = getDomNodeOrDie().getAttribute(name);
        if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
            return null;
        }
        return value;
    }

    /**
     * Returns the attribute node.
     * @param name the name of the attribute to return
     * @return the attribute node with the supplied name, <code>null</code> if the named attribute cannot be found
     *     on this element
     */
    @JsxFunction
    public Object getAttributeNode(final String name) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (StringUtils.isEmpty(name)) {
            throw Context.reportRuntimeError("The empty string '' is not a valid name.");
        }

        final Map<String, DomAttr> attributes = getDomNodeOrDie().getAttributesMap();
        for (final DomAttr attr : attributes.values()) {
            if (attr.getName().equals(name)) {
                return attr.getScriptObject();
            }
        }
        return null;
    }

    /**
     * Removes the named attribute.
     * @param name the name of the attribute to be removed
     */
    @JsxFunction
    public void removeAttribute(final String name) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (StringUtils.isEmpty(name)) {
            throw Context.reportRuntimeError("The empty string '' is not a valid name.");
        }

        getDomNodeOrDie().removeAttribute(name);
        delete(name);
    }

    /**
     * Removes the specified attribute from this element.
     * @param att the attribute to be removed from this element
     * @return the removed attribute
     */
    @JsxFunction
    public XMLDOMAttribute removeAttributeNode(final XMLDOMAttribute att) {
        if (att == null) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final String name = att.getName();

        final XMLDOMNamedNodeMap nodes = (XMLDOMNamedNodeMap) getAttributes();
        final XMLDOMAttribute removedAtt = (XMLDOMAttribute) nodes.getNamedItemWithoutSyntheticClassAttr(name);
        if (removedAtt != null) {
            removedAtt.detachFromParent();
        }
        removeAttribute(name);

        return removedAtt;
    }

    /**
     * Sets the value of the named attribute.
     *
     * @param name the name of the attribute; if the attribute with that name already exists, its value is changed
     * @param value the value for the named attribute
     */
    @JsxFunction
    public void setAttribute(final String name, final String value) {
        if (name == null || "null".equals(name)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }
        if (StringUtils.isEmpty(name)) {
            throw Context.reportRuntimeError("The empty string '' is not a valid name.");
        }
        if (value == null || "null".equals(value)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        getDomNodeOrDie().setAttribute(name, value);
    }

    /**
     * Sets or updates the supplied attribute node on this element.
     * @param newAtt the attribute node to be associated with this element
     * @return the replaced attribute node, if any, <code>null</code> otherwise
     */
    @JsxFunction
    public XMLDOMAttribute setAttributeNode(final XMLDOMAttribute newAtt) {
        if (newAtt == null) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final String name = newAtt.getBaseName();

        final XMLDOMNamedNodeMap nodes = (XMLDOMNamedNodeMap) getAttributes();
        final XMLDOMAttribute replacedAtt = (XMLDOMAttribute) nodes.getNamedItemWithoutSyntheticClassAttr(name);
        if (replacedAtt != null) {
            replacedAtt.detachFromParent();
        }

        final DomAttr newDomAttr = newAtt.getDomNodeOrDie();
        getDomNodeOrDie().setAttributeNode(newDomAttr);
        return replacedAtt;
    }

    /**
     * Returns a list of all descendant elements that match the supplied name.
     * @param tagName the name of the element to find; the tagName value '*' matches all descendant elements of this
     *     element
     * @return a list containing all elements that match the supplied name
     */
    @JsxFunction
    public XMLDOMNodeList getElementsByTagName(final String tagName) {
        if (tagName == null || "null".equals(tagName)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final String tagNameTrimmed = tagName.trim();

        if (elementsByTagName_ == null) {
            elementsByTagName_ = new HashMap<String, XMLDOMNodeList>();
        }

        XMLDOMNodeList collection = elementsByTagName_.get(tagNameTrimmed);
        if (collection != null) {
            return collection;
        }

        final DomNode node = getDomNodeOrDie();
        final String description = "XMLDOMElement.getElementsByTagName('" + tagNameTrimmed + "')";
        if ("*".equals(tagNameTrimmed)) {
            collection = new XMLDOMNodeList(node, false, description) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        else if ("".equals(tagNameTrimmed)) {
            collection = new XMLDOMNodeList(node, false, description) {
                @Override
                protected List<Object> computeElements() {
                    final List<Object> response = new ArrayList<Object>();
                    final DomNode domNode = getDomNodeOrNull();
                    if (domNode == null) {
                        return response;
                    }
                    for (final DomNode node : getCandidates()) {
                        if (node instanceof DomText) {
                            final DomText domText = (DomText) node;
                            if (!StringUtils.isBlank(domText.getWholeText())) {
                                response.add(node);
                            }
                        }
                        else {
                            response.add(node);
                        }
                    }
                    return response;
                }
            };
        }
        else {
            collection = new XMLDOMNodeList(node, false, description) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return tagNameTrimmed.equals(node.getNodeName());
                }
            };
        }

        elementsByTagName_.put(tagName, collection);

        return collection;
    }

    /**
     * Normalizes all descendant elements by combining two or more adjacent text nodes into one unified text node.
     */
    @JsxFunction
    public void normalize() {
        final DomElement domElement = getDomNodeOrDie();

        domElement.normalize();
        // normalize all descendants
        normalize(domElement);
    }

    private void normalize(final DomElement domElement) {
        for (DomNode domNode : domElement.getChildren()) {
            if (domNode instanceof DomElement) {
                domNode.normalize();
                normalize((DomElement) domNode);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomElement getDomNodeOrDie() {
        return (DomElement) super.getDomNodeOrDie();
    }
}
