/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.mozilla.javascript.Scriptable;

/**
 * The javascript object "HTMLElement" which is the base class for all html
 * objects.  This will typically wrap an instance of {@link HtmlElement}.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 */
public class HTMLElement extends SimpleScriptable {
    private Style style_;

     /**
      * Create an instance.
      */
     public HTMLElement() {
     }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the style object for this element.
     *
     * @return The style object
     */
    public Object jsGet_style() {
        getLog().debug("HTMLElement.jsGet_Style() style=["+style_+"]");
        return style_;
    }


     /**
      * Set the html element that corresponds to this javascript object
      * @param htmlElement The html element
      */
     public void setHtmlElement( final HtmlElement htmlElement ) {
         super.setHtmlElement(htmlElement);

         style_ = (Style)makeJavaScriptObject("Style");
         style_.initialize(this);
     }


    /**
     * Return true if this element is disabled.
     * @return True if this element is disabled.
     */
    public boolean jsGet_disabled() {
        getLog().warn(
            "Getting the disabled attribute for non-submittable elements"
            + " is not allowed according to the HTML specification.  Be aware"
            + " that you are using a non-portable feature");
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsSet_disabled( final boolean disabled ) {
        getLog().warn(
            "Setting the disabled attribute for non-submittable elements"
            + " is not allowed according to the HTML specification.  Be aware"
            + " that you are using a non-portable feature");
        final Element xmlElement = getHtmlElementOrDie().getElement();
        if( disabled ) {
            xmlElement.setAttribute("disabled", "disabled");
        }
        else {
            xmlElement.removeAttribute("disabled");
        }
    }


    /**
     * Return the tag name of this element.
     * @return The tag name
     */
    public String jsGet_tagName() {
        return getHtmlElementOrDie().getTagName().toUpperCase();
    }


    /**
     * Return the value of the named attribute.
     * @return The attribute value
     */
    public Object get( String name, Scriptable start ) {
        Object result = super.get( name, start );
        if ( result == NOT_FOUND ) {
            final HtmlElement htmlElement = getHtmlElementOrNull();
            if( htmlElement != null) {
                final String value = htmlElement.getAttributeValue(name);
                if( value.length() != 0 ) {
                    result = value;
                }
            }
        }
        return result;
    }


    /**
     * Add an HTML element to the element
     * @param childObject The element to add to this element
     * @return The newly added child element.
     */
    public Object jsFunction_appendChild(final Object childObject) {
        final Object appendedChild;
        if (childObject instanceof HTMLElement) {
            // Get XML element for the HTML element passed in
            final HtmlElement childHtmlElement = ((HTMLElement) childObject).getHtmlElementOrDie();
            final Element childXmlNode = childHtmlElement.getElement();
            
            // Get the parent XML element that the child should be added to.
            final HtmlElement parentElement = this.getHtmlElementOrDie();
            final Element parentXmlNode = parentElement.getElement();
            
            // Append the child to the parent element
            if ( parentXmlNode.appendChild(childXmlNode) == null ) {
                appendedChild = null;
            } 
            else {
                appendedChild = childObject;
            }
        } 
        else {
            appendedChild = null;
        }
        return appendedChild;
    }


    /**
     * Get the JavaScript property "parentNode" for the node that
     * contains the current node.
     * @return The parent node
     */
    public Object jsGet_parentNode() {
        final HtmlElement htmlElement = getHtmlElementOrDie();
        final Element xmlElement = htmlElement.getElement();
        final Node parentXmlNode = xmlElement.getParentNode();
        if ( parentXmlNode == null ) {
            return null;
        }
        if ( ( parentXmlNode instanceof Element ) == false ) {
            getLog().warn( "Parent XML node is not an Element.  Only Elements are currently supported.  Parent class: " 
                + parentXmlNode.getClass() );
            return null;
        }
        final Element parentXmlElement = (Element) parentXmlNode;
        final HtmlElement parentHtmlElement =
            htmlElement.getPage().getHtmlElement( parentXmlElement );
        final Object jsParentElement = getScriptableFor( parentHtmlElement );
        return jsParentElement;
    }
}
