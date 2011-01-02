/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.PrintWriter;

import org.w3c.dom.Comment;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the DOM node Comment.
 *
 * @version $Revision$
 * @author Karel Kolman
 * @author Ahmed Ashour
 */
public class DomComment extends DomCharacterData implements Comment {

    /** The symbolic node name. */
    public static final String NODE_NAME = "#comment";

    /**
     * Creates an instance of DomComment.
     *
     * @param page the Page that contains this element
     * @param data the string data held by this node
     */
    public DomComment(final SgmlPage page, final String data) {
        super(page, data);
    }

    /**
     * {@inheritDoc}
     * @return the node type constant, in this case {@link org.w3c.dom.Node#COMMENT_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.COMMENT_NODE;
    }

    /**
     * @return the node name, in this case {@link #NODE_NAME}
     */
    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    /**
     * Recursively write the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.print(indent);
        printWriter.print("<!--");
        printWriter.print(getData());
        printWriter.print("-->");
        printChildrenAsXml(indent, printWriter);
    }

    /**
     * Returns a simple string representation to facilitate debugging.
     * @return a simple string representation
     */
    @Override
    public String toString() {
        return asXml();
    }
}
