/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.PrintWriter;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectionDelegate;
import com.gargoylesoftware.htmlunit.html.impl.SimpleSelectionDelegate;
import com.gargoylesoftware.htmlunit.util.StringUtils;
import com.gargoylesoftware.htmlunit.util.XMLStringUtils;

/**
 * Representation of a text node in the HTML DOM.
 *
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Rodney Gitzel
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Philip Graf
 * @author Ronald Brill
 */
public class DomText extends DomCharacterData implements Text {

    private SelectionDelegate selectionDelegate_;
    private DoTypeProcessor doTypeProcessor_;

    /** The symbolic node name. */
    public static final String NODE_NAME = "#text";

    /**
     * Creates an instance of DomText.
     *
     * @param page the Page that contains this element
     * @param data the string data held by this node
     */
    public DomText(final SgmlPage page, final String data) {
        super(page, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomText splitText(final int offset) {
        if (offset < 0 || offset > getLength()) {
            throw new IllegalArgumentException("offset: " + offset + " data.length: " + getLength());
        }

        // split text into two separate nodes
        final DomText newText = createSplitTextNode(offset);
        setData(getData().substring(0, offset));

        // insert new text node
        if (getParentNode() != null) {
            getParentNode().insertBefore(newText, getNextSibling());
        }
        return newText;
    }

    /**
     * Creates a new text node split from another text node. This method allows
     * the derived type of the new text node to match the original node type.
     *
     * @param offset the character position at which to split the DomText node
     * @return the newly created Text node
     */
    protected DomText createSplitTextNode(final int offset) {
        return new DomText(getPage(), getData().substring(offset));
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean isElementContentWhitespace() {
        throw new UnsupportedOperationException("DomText.isElementContentWhitespace is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWholeText() {
        // I couldn't find a way to have a nearby EntityReference node (either sibling or parent)
        // if this is found, have a look at xerces TextImpl.
        return getNodeValue();
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Text replaceWholeText(final String content) throws DOMException {
        throw new UnsupportedOperationException("DomText.replaceWholeText is not yet implemented.");
    }

    /**
     * @return the node type constant, in this case {@link org.w3c.dom.Node#TEXT_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.TEXT_NODE;
    }

    /**
     * @return the node name, in this case {@link #NODE_NAME}
     */
    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    /**
     * Recursively writes the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        String data = getData();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(data)) {
            printWriter.print(indent);
            if (!(getParentNode() instanceof HtmlStyle) || !data.startsWith("<!--") || !data.endsWith("-->")) {
                data = XMLStringUtils.escapeXmlChars(data);
            }
            printWriter.print(data);
            printWriter.print("\r\n");
        }
        printChildrenAsXml(indent, printWriter);
    }

    /**
     * Gives a simple representation to facilitate debugging.
     * @return a simple representation
     */
    @Override
    public String toString() {
        return asNormalizedText();
    }

    /**
     * Performs the effective type action, called after the keyPress event and before the keyUp event.
     * @param c the character you with to simulate typing
     * @param htmlElement the element in which typing occurs
     * @param lastType is this the last character to type
     */
    protected void doType(final char c, final HtmlElement htmlElement, final boolean lastType) {
        initDoTypeProcessor();
        doTypeProcessor_.doType(getData(), selectionDelegate_, c, htmlElement, lastType);
    }

    /**
     * Performs the effective type action, called after the keyPress event and before the keyUp event.
     *
     * @param keyCode the key code wish to simulate typing
     * @param htmlElement the element in which typing occurs
     * @param lastType is this the last character to type
     */
    protected void doType(final int keyCode, final HtmlElement htmlElement, final boolean lastType) {
        initDoTypeProcessor();
        doTypeProcessor_.doType(getData(), selectionDelegate_, keyCode, htmlElement, lastType);
    }

    private void initDoTypeProcessor() {
        if (selectionDelegate_ == null) {
            selectionDelegate_ = new SimpleSelectionDelegate();
            doTypeProcessor_ = new DoTypeProcessor(this);
        }
    }

    /**
     * Indicates if the provided character can by "typed" in the element.
     * @param c the character
     * @return {@code true} if it is accepted
     */
    protected boolean acceptChar(final char c) {
        // This range is this is private use area
        // see http://www.unicode.org/charts/PDF/UE000.pdf
        return (c < '\uE000' || c > '\uF8FF') && (c == ' ' || !Character.isWhitespace(c));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final DomText newnode = (DomText) super.cloneNode(deep);
        selectionDelegate_ = new SimpleSelectionDelegate();
        doTypeProcessor_ = new DoTypeProcessor(this);

        return newnode;
    }

    /**
     * Moves the selection to the end.
     */
    public void moveSelectionToEnd() {
        initDoTypeProcessor();
        selectionDelegate_.setSelectionStart(getData().length());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        // Empty.
    }
}
