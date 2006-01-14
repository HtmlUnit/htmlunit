/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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

import java.util.Map;

/**
 * A thin wrapper around attributes. Attributes are stored as {@link java.util.Map.Entry} in {@link
 * com.gargoylesoftware.htmlunit.html.HtmlElement}, but the xpath engine expects attributes to be in a {@link
 * com.gargoylesoftware.htmlunit.html.DomNode}.
 *
 * @version $Revision$
 * @author Denis N. Antonioli
 */
public class HtmlAttr extends DomNode implements Map.Entry {
    private final Map.Entry wrappedMappedEntry_;

    /**
     * Instantiate a new wrapper.
     *
     * @param htmlElement The parent element.
     * @param mapEntry The wrapped Map.Entry.
     */
    public HtmlAttr(final HtmlElement htmlElement, final Map.Entry mapEntry) {
        super(htmlElement.getPage());
        wrappedMappedEntry_ = mapEntry;
        setParentNode(htmlElement);
    }

    /**
     * @return the node type
     */
    public short getNodeType() {
        return DomNode.ATTRIBUTE_NODE;
    }

    /**
     * @return The same value as returned by {@link #getKey()}.
     */
    public String getNodeName() {
        return (String) getKey();
    }

    /**
     * @return The same value as returned by {@link #getValue()}.
     */
    public String getNodeValue() {
        return (String) getValue();
    }

    /**
     * @return The key of wrapped map entry.
     */
    public Object getKey() {
        return wrappedMappedEntry_.getKey();
    }

    /**
     * @return The value of wrapped map entry.
     */
    public Object getValue() {
        return wrappedMappedEntry_.getValue();
    }


    /**
     * Delegate to the wrapped map entry.
     * @param value new value to be stored in this entry.
     * @return old value corresponding to the entry.
     */
    public Object setValue(final Object value) {
        return wrappedMappedEntry_.setValue(value);
    }
}
