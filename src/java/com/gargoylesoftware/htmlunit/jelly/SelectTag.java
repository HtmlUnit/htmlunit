/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.TagSupport;

import org.jaxen.JaxenException;
import org.jaxen.VariableContext;
import org.jaxen.UnresolvableException;

import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;

/**
 * A tag which evaluates an XPath expression against a {@link com.gargoylesoftware.htmlunit.html.DomNode}
 * (e.g. a HtmlPage) and stores the result in a script context variable.<p>
 * <em>Note that the expression must be self-contained, i.e. it must provide the evaluation
 * context by referencing a script variable. See the example below</em>
 *
 * <p>Example:<br><center>
 * <code>
 *   &lt;htmlunit:select var="title" xpath="$page/head/title"/&gt;
 * </code>
 *
 * @author Christian Sell
 * @version $Revision$
 */
public class SelectTag extends TagSupport implements VariableContext {

    /** The variable name to export. */
    private String var_;

    /** The XPath expression to evaluate. */
    private String xpath_;

    /** constructor. or what? */
    public SelectTag() {
    }

    /**
     * Process the tag
     * @param output The xml output
     * @throws MissingAttributeException if attribute is missing
     * @throws JellyTagException If a problem occurs
     */
    public void doTag(final XMLOutput output) throws MissingAttributeException, JellyTagException {
        if (var_ == null) {
            throw new MissingAttributeException( "var" );
        }
        if (xpath_ == null) {
            throw new MissingAttributeException( "xpath" );
        }

        Object value = null;
        try {
            final HtmlUnitXPath xpath = new HtmlUnitXPath(xpath_);
            xpath.setVariableContext(this);
            value = xpath.evaluate(null);
        }
        catch (final JaxenException e) {
            throw new JellyTagException(e);
        }

        context.setVariable(var_, value);
    }

    /**
     * @param var the variable name to define for this expression
     */
    public void setVar(String var) {
        this.var_ = var;
    }

    /**
     * @param xpath the XPath expression to evaluate.
     */
    public void setXpath(String xpath) {
        this.xpath_ = xpath;
    }

    /**
     * fetch the variable during evaluation of an XPath expression
     * @param namespaceURI namespace URI
     * @param prefix prefix
     * @param localName local name
     * @return the variable value
     * @throws UnresolvableException variable not defined
     */
    public Object getVariableValue(final String namespaceURI, final String prefix, final String localName)
            throws UnresolvableException {

        final Object var = context.getVariable(localName);
        if(var == null) {
            throw new UnresolvableException(localName);
        }
        return var;
    }
}
