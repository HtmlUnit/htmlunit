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
import org.w3c.dom.CDATASection;

/**
 * Representation of a CDATA node in the HTML DOM.
 *
 * @author Marc Guillemot
 * @author David K. Taylor
 * @author Ronald Brill
 */
public class DomCDataSection extends DomText implements CDATASection {

    /**
     * Creates a new instance.
     *
     * @param page the Page that contains this element
     * @param data the string data held by this node
     */
    public DomCDataSection(final SgmlPage page, final String data) {
        super(page, data);
    }

    /**
     * @return the node type constant, in this case {@link org.w3c.dom.Node#CDATA_SECTION_NODE}
     */
    @Override
    public short getNodeType() {
        return CDATA_SECTION_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return "#cdata-section";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean printXml(final String indent, final boolean tagBefore, final PrintWriter printWriter) {
        printWriter.print("<![CDATA[");
        printWriter.print(getData());
        printWriter.print("]]>");
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DomText createSplitTextNode(final int offset) {
        return new DomCDataSection(getPage(), getData().substring(offset));
    }
}
