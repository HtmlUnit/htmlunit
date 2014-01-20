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
package com.gargoylesoftware.htmlunit.javascript.host.activex;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMAttribute.<br>
 * Represents an attribute of the IXMLDOMElement. Valid and default values for the attribute are defined in a
 * document type definition (DTD) or schema.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762738.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Sudhan Moghe
 * @author Frank Danek
 */
@JsxClass(domClass = DomAttr.class, browsers = @WebBrowser(IE))
public class XMLDOMAttribute extends XMLDOMNode {

    private XMLDOMText textNode_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMAttribute() { }

    /**
     * Returns a node list containing the child nodes.
     * @return a node list containing the child nodes
     */
    @Override
    public XMLDOMNodeList getChildNodes() {
        initTextNode();

        return super.getChildNodes();
    }

    /**
     * Returns the first child of the attribute.
     * @return the first child of the attribute
     */
    @Override
    public XMLDOMNode getFirstChild() {
        return getLastChild();
    }

    /**
     * Returns the last child attribute.
     * @return the last child attribute
     */
    @Override
    public XMLDOMNode getLastChild() {
        initTextNode();

        return textNode_;
    }

    private void initTextNode() {
        if (textNode_ == null) {
            final String value = getValue();
            if (!org.apache.commons.lang3.StringUtils.isEmpty(value)) {
                final DomText text = new DomText(getDomNodeOrDie().getPage(), value);
                getDomNodeOrDie().appendChild(text);
                textNode_ = (XMLDOMText) text.getScriptObject();
            }
        }
    }

    /**
     * Returns the attribute name.
     * @return the attribute name
     */
    @JsxGetter
    public String getName() {
        return getDomNodeOrDie().getName();
    }

    /**
     * Returns the text associated with the attribute.
     * @return the text associated with the attribute
     */
    @Override
    public String getNodeValue() {
        return getValue();
    }

    /**
     * Sets the text associated with the attribute.
     * @param value the new text associated with the attribute
     */
    @Override
    public void setNodeValue(final String value) {
        setValue(value);
    }

    /**
     * Returns the parent node.
     * @return <code>null</code>
     */
    @Override
    public Node getParentNode() {
        return null;
    }

    /**
     * Indicates whether the attribute is explicitly specified or derived from a default value in
     * the document type definition (DTD) or schema.
     * @return <code>true</code> if this attribute has been explicitly specified
     */
    @JsxGetter
    public boolean getSpecified() {
        return getDomNodeOrDie().getSpecified();
    }

    /**
     * Returns a string representing the value of the attribute with entities expanded.
     * @return the value of this attribute
     */
    @Override
    public Object getText() {
        return getValue();
    }

    /**
     * Sets the text content of the attribute.
     * @param value the text content of the attribute
     */
    @Override
    public void setText(final Object value) {
        setValue(value == null ? null : Context.toString(value));
    }

    /**
     * Returns the attribute value.
     * @return the attribute value
     */
    @JsxGetter
    public String getValue() {
        return getDomNodeOrDie().getValue();
    }

    /**
     * Sets the attribute value.
     * @param value the new attribute value
     */
    @JsxSetter
    public void setValue(final String value) {
        getDomNodeOrDie().setValue(value);

        resetTextNode();
    }

    private void resetTextNode() {
        if (textNode_ != null) {
            getDomNodeOrDie().removeChild(textNode_.getDomNodeOrNull());
            textNode_ = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXml() {
        final StringBuilder sb = new StringBuilder(getName());
        sb.append('=').append('"');
        sb.append(StringUtils.escapeXmlAttributeValue(getValue()));
        sb.append('"');
        return sb.toString();
    }

    /**
     * Detaches this attribute from the parent HTML element after caching the attribute value.
     */
    public void detachFromParent() {
        final DomAttr domNode = getDomNodeOrDie();
        final DomElement parent = (DomElement) domNode.getParentNode();
        if (parent != null) {
            domNode.setValue(parent.getAttribute(getName()));
        }
        domNode.remove();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomAttr getDomNodeOrDie() throws IllegalStateException {
        return super.getDomNodeOrDie();
    }
}
