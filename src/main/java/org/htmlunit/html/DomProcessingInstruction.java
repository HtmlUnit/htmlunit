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
package org.htmlunit.html;

import java.io.PrintWriter;

import org.htmlunit.SgmlPage;
import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;

/**
 * Wrapper for the DOM node ProcessingInstruction.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DomProcessingInstruction extends DomNode implements ProcessingInstruction {

    private final String target_;
    private String data_;

    /**
     * Creates a new instance.
     *
     * @param page the Page that contains this element
     * @param target the target
     * @param data the data
     */
    public DomProcessingInstruction(final SgmlPage page, final String target, final String data) {
        super(page);
        target_ = target;
        setData(data);
    }

    /**
     * {@inheritDoc}
     * @return the node type constant, in this case {@link org.w3c.dom.Node#PROCESSING_INSTRUCTION_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return target_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTarget() {
        return getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getData() {
        return getNodeValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(final String data) throws DOMException {
        setNodeValue(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String value) {
        data_ = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeValue() {
        return data_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextContent(final String textContent) {
        setNodeValue(textContent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean printXml(final String indent, final boolean tagBefore, final PrintWriter printWriter) {
        printWriter.print("<?");
        printWriter.print(getTarget());
        printWriter.print(" ");
        printWriter.print(getData());
        printWriter.print("?>");
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        // Empty.
    }
}
