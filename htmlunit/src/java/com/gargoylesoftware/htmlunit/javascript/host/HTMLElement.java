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

import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

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
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsSet_disabled( final boolean disabled ) {
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
     * @param name The name of the variable
     * @param start The scriptable to get the variable from.
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
     * Get the JavaScript property "nodeType" for the current node.
     * @return The node type
     */
    public short jsGet_nodeType() {
        return getHtmlElementOrDie().getNodeType();
    }


    /**
     * Get the JavaScript property "nodeName" for the current node.
     * @return The node name
     */
    public String jsGet_nodeName() {
        return getHtmlElementOrDie().getNodeName();
    }


    /**
     * Get the JavaScript property "nodeValue" for the current node.
     * @return The node value
     */
    public String jsGet_nodeValue() {
        return getHtmlElementOrDie().getNodeValue();
    }


    /**
     * Set the JavaScript property "nodeValue" for the current node.
     * @param newValue The new node value
     */
    public void jsSet_nodeValue( String newValue ) {
        getHtmlElementOrDie().getNode().setNodeValue( newValue );
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
            final Node childXmlNode = childHtmlElement.getNode();

            // Get the parent XML node that the child should be added to.
            final HtmlElement parentElement = this.getHtmlElementOrDie();
            final Node parentXmlNode = parentElement.getNode();

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
     * Duplicate an XML node
     * @param deep If true, recursively clone all descendents.  Otherwise,
     * just clone this node.
     * @return The newly cloned node.
     */
    public Object jsFunction_cloneNode(final boolean deep) {
        final HtmlElement htmlElement = getHtmlElementOrDie();
        final Node clonedXmlNode = htmlElement.getNode().cloneNode( deep );
        return getJavaScriptElement(htmlElement.getPage().getHtmlElement(clonedXmlNode));
    }


    /**
     * Add an HTML element as a child to this element before the referenced
     * element.  If the referenced element is null, append to the end.
     * @param newChildObject The element to add to this element
     * @param refChildObject The element before which to add the new child
     * @return The newly added child element.
     */
    public Object jsFunction_insertBefore(final Object newChildObject,
        final Object refChildObject) {
        final Object appendedChild;
        if (newChildObject instanceof HTMLElement &&
            refChildObject instanceof HTMLElement) {
            // Get XML elements for the HTML elements passed in
            final HtmlElement newChildHtmlElement = ((HTMLElement) newChildObject).getHtmlElementOrDie();
            final Node newChildXmlNode = newChildHtmlElement.getNode();
            Node refChildXmlNode = null;
            if (refChildObject != null) {
                final HtmlElement refChildHtmlElement = ((HTMLElement) refChildObject).getHtmlElementOrDie();
                refChildXmlNode = refChildHtmlElement.getNode();
            }

            // Get the parent XML element that the child should be added to.
            final HtmlElement parentElement = this.getHtmlElementOrDie();
            final Node parentXmlNode = parentElement.getNode();

            // Append the child to the parent element
            if ( parentXmlNode.insertBefore(newChildXmlNode,
                refChildXmlNode) == null ) {
                appendedChild = null;
            }
            else {
                appendedChild = newChildObject;
            }
        }
        else {
            appendedChild = null;
        }
        return appendedChild;
    }


    /**
     * Remove an HTML element from this element
     * @param childObject The element to remove from this element
     * @return The removed child element.
     */
    public Object jsFunction_removeChild(final Object childObject) {
        final Object removedChild;
        if (childObject instanceof HTMLElement) {
            // Get XML element for the HTML element passed in
            final HtmlElement childHtmlElement = ((HTMLElement) childObject).getHtmlElementOrDie();
            final Node childXmlNode = childHtmlElement.getNode();

            // Get the parent XML element that the child should be added to.
            final HtmlElement parentElement = this.getHtmlElementOrDie();
            final Node parentXmlNode = parentElement.getNode();

            // Remove the child from the parent element
            if ( parentXmlNode.removeChild(childXmlNode) == null ) {
                removedChild = null;
            }
            else {
                removedChild = childObject;
                parentElement.getPage().removeHtmlElement(childXmlNode);
            }
        }
        else {
            removedChild = null;
        }
        return removedChild;
    }


    /**
     * Replace a child HTML element with another HTML element.
     * @param newChildObject The element to add as a child of this element
     * @param oldChildObject The element to remove as a child of this element
     * @return The removed child element.
     */
    public Object jsFunction_replaceChild(final Object newChildObject,
        final Object oldChildObject) {
        final Object removedChild;
        if (newChildObject instanceof HTMLElement &&
            oldChildObject instanceof HTMLElement) {
            // Get XML elements for the HTML elements passed in
            final HtmlElement newChildHtmlElement = ((HTMLElement) newChildObject).getHtmlElementOrDie();
            final Node newChildXmlNode = newChildHtmlElement.getNode();
            Node oldChildXmlNode = null;
            if (oldChildObject != null) {
                final HtmlElement oldChildHtmlElement = ((HTMLElement) oldChildObject).getHtmlElementOrDie();
                oldChildXmlNode = oldChildHtmlElement.getNode();
            }

            // Get the parent XML element that the child should be added to.
            final HtmlElement parentElement = this.getHtmlElementOrDie();
            final Node parentXmlNode = parentElement.getNode();

            // Replace the old child with the new child.
            if ( parentXmlNode.replaceChild(newChildXmlNode,
                oldChildXmlNode) == null ) {
                removedChild = null;
            }
            else {
                removedChild = oldChildObject;
            }
        }
        else {
            removedChild = null;
        }
        return removedChild;
    }


    /**
     * Get the JavaScript property "parentNode" for the node that
     * contains the current node.
     * @return The parent node
     */
    public Object jsGet_parentNode() {
        return getJavaScriptElement( getHtmlElementOrDie().getParentNode() );
    }


    /**
     * Get the JavaScript property "nextSibling" for the node that
     * contains the current node.
     * @return The next sibling node or null if the current node has
     * no next sibling.
     */
    public Object jsGet_nextSibling() {
        return getJavaScriptElement( getHtmlElementOrDie().getNextSibling() );
    }


    /**
     * Get the JavaScript property "previousSibling" for the node that
     * contains the current node.
     * @return The previous sibling node or null if the current node has
     * no previous sibling.
     */
    public Object jsGet_previousSibling() {
        return getJavaScriptElement( getHtmlElementOrDie().getPreviousSibling() );
    }


    /**
     * Get the JavaScript property "firstChild" for the node that
     * contains the current node.
     * @return The first child node or null if the current node has
     * no children.
     */
    public Object jsGet_firstChild() {
        return getJavaScriptElement( getHtmlElementOrDie().getFirstChild() );
    }


    /**
     * Get the JavaScript property "lastChild" for the node that
     * contains the current node.
     * @return The last child node or null if the current node has
     * no children.
     */
    public Object jsGet_lastChild() {
        return getJavaScriptElement( getHtmlElementOrDie().getLastChild() );
    }


    /**
     * Get the JavaScript element for a given HtmlElement
     * @param htmlElement The HtmlElement
     * @return The JavaScript element or null if the HtmlElement was null.
     */
    protected Object getJavaScriptElement( HtmlElement htmlElement ) {
        if ( htmlElement == null ) {
            return null;
        }
        return getScriptableFor( htmlElement );
    }
}
