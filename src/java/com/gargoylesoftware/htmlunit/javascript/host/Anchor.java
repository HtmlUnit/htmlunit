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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.MalformedURLException;

import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;


/**
 * The javascript object that represents an anchor
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
 * @author  David D. Kilzer
 * @author  Marc Guillemot
 */
public class Anchor extends FocusableHostElement {

    private static final long serialVersionUID = -816365374422492967L;

    /**
     * Create an instance.
     */
    public Anchor() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }
 
    /**
     * Set the href property.
     * @param href href attribute value.
     */
    public void jsSet_href( final String href ) {
        getHtmlElementOrDie().setAttributeValue( "href", href );
    }

    /**
     * Return the value of the href property.
     * @return The href property.
     */
    public String jsGet_href() {
        return getHtmlElementOrDie().getAttributeValue( "href" );
    }

    /**
     * Calls for instance for implicit conversion to string
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    public Object getDefaultValue(final Class hint) {
        final HtmlAnchor link = (HtmlAnchor) getHtmlElementOrDie();
        final String href = link.getHrefAttribute();
        
        final String response;
        if (href == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            response = ""; // for example for named anchors
        }
        else {
            final int indexAnchor = href.indexOf('#');
            final String beforeAnchor;
            final String anchorPart; 
            if (indexAnchor == -1) {
                beforeAnchor = href;
                anchorPart = "";
            }
            else {
                beforeAnchor = href.substring(0, indexAnchor);
                anchorPart = href.substring(indexAnchor);
            }

            try {
                response = link.getPage().getFullyQualifiedUrl(beforeAnchor).toExternalForm()
                + anchorPart;
            }
            catch (final MalformedURLException e) {
                throw Context.reportRuntimeError("Problem reading url: " + e);
            }
        }
        
        return response;
    }
    
}
