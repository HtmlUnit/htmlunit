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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jaxen.JaxenException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;

import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.HTMLCollection;
import com.gargoylesoftware.htmlunit.xml.XmlAttr;
import com.gargoylesoftware.htmlunit.xml.XmlElement;

/**
 * A JavaScript object for XMLElement.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XMLElement extends NodeImpl {

    /**
     * Applies the specified xpath expression to this node's context and returns the generated list of matching nodes.
     * @param expression A string specifying an XPath expression.
     * @return list of the found elements.
     */
    public Object jsxFunction_selectNodes(final String expression) {
        final HTMLCollection collection = new HTMLCollection(this);
        try {
            collection.init(getDomNodeOrDie(), new HtmlUnitXPath(expression));
        }
        catch (final JaxenException e) {
            throw Context.reportRuntimeError("Failed to initialize collection 'selectNodes': " + e.getMessage());
        }
        return collection;
    }

    /**
     * Return the tag name of this element.
     * @return The tag name.
     */
    public String jsxGet_tagName() {
        return ((XmlElement) getDomNodeOrDie()).getTagName();
    }
    
    /**
     * Returns the attributes of this XML element.
     * @return the attributes of this XML element.
     */
    public Object jsxGet_attributes() {
        final Map attributes = ((XmlElement) getDomNodeOrDie()).getAttributes();
        final List list = new ArrayList();
        for (final Iterator values = attributes.values().iterator(); values.hasNext();) {
            final XmlAttr attr = (XmlAttr) values.next();
            list.add(attr.getScriptObject());
        }
        return new NativeArray(list.toArray());
    }
}
