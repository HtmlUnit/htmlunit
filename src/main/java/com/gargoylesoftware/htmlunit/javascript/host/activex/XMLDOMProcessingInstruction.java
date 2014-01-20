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

import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for MSXML's (ActiveX) XMLDOMProcessingInstruction.<br>
 * Represents a processing instruction, which XML defines to keep processor-specific information in the text of the
 * document.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms765480.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(domClass = DomProcessingInstruction.class, browsers = @WebBrowser(IE))
public final class XMLDOMProcessingInstruction extends XMLDOMNode {

    private static final String XML_DECLARATION_TARGET = "xml";

    private boolean attributesComputed_;
    private XMLDOMNamedNodeMap attributes_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLDOMProcessingInstruction() {
    }

    /**
     * Returns the list of attributes for this element.
     * @return the list of attributes for this element
     */
    @Override
    public Object getAttributes() {
        if (!attributesComputed_) {
            final DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
            if (XML_DECLARATION_TARGET.equalsIgnoreCase(domProcessingInstruction.getTarget())) {
                attributes_ = new XMLDOMNamedNodeMap(getDomNodeOrDie());
            }
            attributesComputed_ = true;
        }
        return attributes_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBaseName() {
        return getTarget();
    }

    /**
     * Returns the content of the processing instruction, excluding the target.
     * @return the content of the processing instruction, excluding the target
     */
    @JsxGetter
    public String getData() {
        final DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
        return domProcessingInstruction.getData();
    }

    /**
     * Sets the content of the processing instruction, excluding the target.
     * @param data the content of the processing instruction, excluding the target
     */
    @JsxSetter
    public void setData(final String data) {
        if (data == null || "null".equals(data)) {
            throw Context.reportRuntimeError("Type mismatch.");
        }

        final DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
        if (XML_DECLARATION_TARGET.equalsIgnoreCase(domProcessingInstruction.getTarget())) {
            throw Context.reportRuntimeError("This operation cannot be performed with a node of type XMLDECL.");
        }
        domProcessingInstruction.setData(data);
    }

    /**
     * Attempting to set the value of document fragments generates an error.
     * @param newValue the new value to set
     */
    @Override
    public void setNodeValue(final String newValue) {
        final DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
        if (XML_DECLARATION_TARGET.equalsIgnoreCase(domProcessingInstruction.getTarget())) {
            throw Context.reportRuntimeError("This operation cannot be performed with a node of type XMLDECL.");
        }

        super.setNodeValue(newValue);
    }

    /**
     * Returns the target for the processing instruction.
     * @return the target for the processing instruction
     */
    @JsxGetter
    public String getTarget() {
        final DomProcessingInstruction domProcessingInstruction = getDomNodeOrDie();
        return domProcessingInstruction.getTarget();
    }

    /**
     * Sets the text contained in the node.
     * @param newText the text contained in the node
     */
    @Override
    public void setText(final Object newText) {
        setData(newText == null ? null : Context.toString(newText));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomProcessingInstruction getDomNodeOrDie() {
        return (DomProcessingInstruction) super.getDomNodeOrDie();
    }
}
