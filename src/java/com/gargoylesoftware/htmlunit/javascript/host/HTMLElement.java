/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The javascript object "HTMLElement" which is the base class for all html
 * objects.  This will typically wrap an instance of {@link HtmlElement}.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
public class HTMLElement extends NodeImpl {
    private static final long serialVersionUID = -6864034414262085851L;
    private static final String BEHAVIOR_HOMEPAGE = "#default#homePage";
    private static final int BEHAVIOR_ID_UNKNOWN = -1;
    private static final int BEHAVIOR_ID_HOMEPAGE = 0;
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
        return style_;
    }


     /**
      * Set the DOM node that corresponds to this javascript object
      * @param domNode The DOM node
      */
     public void setDomNode( final DomNode domNode ) {
         super.setDomNode(domNode);

         style_ = (Style)makeJavaScriptObject("Style");
         style_.initialize(this);
     }


    /**
     * Return the element ID.
     * @return The ID of this element.
     */
    public String jsGet_id() {
        return getHtmlElementOrDie().getId();
    }


    /**
     * Set the identifier this element.
     *
     * @param newId The new identifier of this element.
     */
    public void jsSet_id( final String newId ) {
        getHtmlElementOrDie().setId( newId );
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
        final HtmlElement element = getHtmlElementOrDie();
        if( disabled ) {
            element.setAttributeValue("disabled", "disabled");
        }
        else {
            element.removeAttribute("disabled");
        }
    }


    /**
     * Return the tag name of this element
     * @return The tag name in uppercase
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
            // can name be an attribute of current element?
            // first approximation: attribute are all lowercase
            // this should be improved because it's wrong. For instance: tabIndex, hideFocus, acceptCharset
            if ( htmlElement != null && name.toLowerCase().equals(name)) {
                final String value = htmlElement.getAttributeValue(name);
                if (HtmlElement.ATTRIBUTE_NOT_DEFINED != value) {
                    result = value;
                }
            }
        }
        return result;
    }


    /**
     * Gets the specified property.
     * @param attibuteName attribute name.
     * @return The value of the specified attribute
     */
    public String jsFunction_getAttribute(String attibuteName) {
        return getHtmlElementOrDie().getAttributeValue(attibuteName);
    }


    /**
     * Set an attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-F68F082">
     * the DOM reference</a>
     * 
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    public void jsFunction_setAttribute(final String name, final String value) {
        getHtmlElementOrDie().setAttributeValue(name, value);
    }
    
    /**
     * Return all the elements with the specified tag name
     * @param tagName The name to search for
     * @return the list of elements
     */
    public Object jsFunction_getElementsByTagName( final String tagName ) {
        final HtmlElement element = (HtmlElement)getDomNodeOrDie();
        final List list = element.getHtmlElementsByTagNames(
            Collections.singletonList(tagName.toLowerCase()));
        final ListIterator iterator = list.listIterator();
        while(iterator.hasNext()) {
            final HtmlElement htmlElement = (HtmlElement)iterator.next();
            iterator.set( getScriptableFor(htmlElement) );
        }

        return new NativeArray( list.toArray() );
    }
  
    /**
     * Return the class defined for this element
     * @return the class name
     */
    public Object jsGet_className() {
        return getHtmlElementOrDie().getAttributeValue("class");
    }

    /**
     * Set the class attribute for this element.
     * @param className - the new class name
     */
    public void jsSet_className(final String className) {
        getHtmlElementOrDie().setAttributeValue("class", className);
    }
    
    /**
     * Get the innerHTML attribute
     * @return the contents of this node as html
     */
    public String jsGet_innerHTML() {
        StringBuffer buf = new StringBuffer();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie());

        return buf.toString();
    }

    private void printChildren(final StringBuffer buffer, final DomNode node) {
        for (final Iterator iter = node.getChildIterator(); iter.hasNext();) {
            printNode(buffer, (DomNode) iter.next());
        }
    }
    private void printNode(final StringBuffer buffer, final DomNode node) {
        if (node instanceof DomCharacterData) {
            buffer.append(node.getNodeValue().replaceAll("  ", " ")); // remove white space sequences
        }
        else {
            final HtmlElement htmlElt = (HtmlElement) node;
            buffer.append("<");
            buffer.append(htmlElt.getTagName());
            
            // the attributes
            for (final Iterator iterator=htmlElt.getAttributeEntriesIterator(); iterator.hasNext(); ) {
                buffer.append(' ' );
                final Map.Entry entry = (Map.Entry) iterator.next();
                buffer.append(entry.getKey());
                buffer.append( "=\"" );
                buffer.append(entry.getValue());
                buffer.append( "\"" );
            }
            if (htmlElt.getFirstChild() == null) {
                buffer.append("/");
            }
            buffer.append(">");
            
            printChildren(buffer, node);
            if (htmlElt.getFirstChild() != null) {
                buffer.append("</");
                buffer.append(htmlElt.getTagName());
                buffer.append(">");
            }
        }
    }    

    /**
     * Replace all children elements of this element with the supplied value.
     * <b>Currently does not support html as the replacement value.</b>
     * @param value - the new value for the contents of this node
     */
    public void jsSet_innerHTML(final String value) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();

        if (value.indexOf('<') >= 0) {
            // build a pseudo WebResponse
            final WebResponse webResp = new StringWebResponse("<html><body>" + value + "</body></html>");
            try {
                final HtmlPage pseudoPage = HTMLParser.parse(webResp, getDomNodeOrDie().getPage().getEnclosingWindow());
                final HtmlBody body = (HtmlBody) pseudoPage.getDocumentElement().getFirstChild();
                for (final Iterator iter = body.getChildIterator(); iter.hasNext();)
                {
                    final HtmlElement child = (HtmlElement) iter.next();
                    domNode.appendChild(child);
                }
            }
            catch (final IOException e) {
                // should not occur
                getLog().warn("Unexpected exception occured while setting innerHTML");
            }
        }
        else {
            // just text, keep it simple
            domNode.appendChild(new DomText(domNode.getPage(), value));
        }
    }

    /**
     * Adds the specified behavior to this HTML element. Currently only supports
     * the following default IE behaviors:
     * <ul>
     *   <li>#default#homePage</li>
     * </ul>
     * @param behavior the URL of the behavior to add, or a default behavior name
     * @return an identifier that can be user later to detach the behavior from the element
     */
    public int jsFunction_addBehavior(final String behavior) {
        if (BEHAVIOR_HOMEPAGE.equals(behavior)) {
            Class c = this.getClass();
            defineFunctionProperties(new String[] {"isHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"setHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"navigateHomePage"}, c, 0);
            return BEHAVIOR_ID_HOMEPAGE;
        }
        
        getLog().warn("Unimplemented behavior: " + behavior);

        return BEHAVIOR_ID_UNKNOWN;
    }

    /**
     * Removes the behavior corresponding to the specified identifier from this element.
     * @param id the identifier for the behavior to remove
     */
    public void jsFunction_removeBehavior(int id) {
        switch (id) {
            case BEHAVIOR_ID_HOMEPAGE:
                this.delete("isHomePage");
                this.delete("setHomePage");
                this.delete("navigateHomePage");
                break;
            default:
                getLog().warn("Unexpected behavior id: " + id + ". Ignoring.");
        }
    }

    /**
     * Returns <tt>true</tt> if the specified URL is the web client's current
     * homepage. Part of the <tt>#default#homePage</tt> default IE behavior
     * implementation.
     * @param url the URL to check
     * @return <tt>true</tt> if the specified URL is the current homepage
     */
    public boolean isHomePage(final String url) {
        final String home = getDomNodeOrDie().getPage().getWebClient().getHomePage();
        return (home != null && home.equals(url));
    }

    /**
     * Sets the web client's current homepage. Part of the <tt>#default#homePage</tt>
     * default IE behavior implementation.
     * @param url the new homepage URL
     */
    public void setHomePage(final String url) {
        this.getDomNodeOrDie().getPage().getWebClient().setHomePage(url);
    }

    /**
     * Causes the web client to navigate to the current home page. Part of the
     * <tt>#default#homePage</tt> default IE behavior implementation.
     * @throws IOException if loading home page fails
     */
    public void navigateHomePage() throws IOException {
        final WebClient webClient = getDomNodeOrDie().getPage().getWebClient(); 
        webClient.getPage(new URL(webClient.getHomePage()));
    }
    
    /**
     * Set the onclick event handler for this element.
     * @param onclick the new handler
     */
    public void jsSet_onclick(final Function onclick) {
        getHtmlElementOrDie().setEventHandler("onclick", onclick);
    }
    
    /**
     * Get the onclick event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsGet_onclick() {
        return getHtmlElementOrDie().getEventHandler("onclick");
    }
}
