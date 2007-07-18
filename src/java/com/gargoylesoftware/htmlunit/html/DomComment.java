/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.io.PrintWriter;

/**
 * Wrapper for the DOM node Comment.
 *
 * @version  $Revision$
 * @author Karel Kolman
 */
public class DomComment extends DomCharacterData {

    private static final long serialVersionUID = -7728247579175928593L;

    /** the symbolic node name */
    public static final String NODE_NAME = "#comment";

    /**
     * Create an instance of DomComment
     *
     * @param page The HtmlPage that contains this element.
     * @param data the string data held by this node
     */
    public DomComment(final HtmlPage page, final String data) {
        super(page, data);
    }
    
    /**
     * @return the node type constant, in this case {@link DomNode#COMMENT_NODE}
     */
    public short getNodeType() {
        return DomNode.COMMENT_NODE;
    }

    /**
     * @return the node name, in this case {@link #NODE_NAME}
     */
    public String getNodeName() {
        return NODE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public String asText() {
        return "";
    }

    /**
     * recursively write the XML data for the node tree starting at <code>node</code>
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printXml( final String indent, final PrintWriter printWriter ) {
        printWriter.print(indent);
        printWriter.print("<!--");
        printWriter.print(getData());
        printWriter.print("-->");
        printChildrenAsXml( indent, printWriter );
    }

    /**
     * Gives a simple representation to facilitate debugging
     * @return a simple representation
     */
    public String toString() {
        return asXml();
    }
}
