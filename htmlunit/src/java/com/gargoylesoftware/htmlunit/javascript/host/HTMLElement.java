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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.IElementFactory;

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
    private static final String POSITION_BEFORE_BEGIN = "beforeBegin";
    private static final String POSITION_AFTER_BEGIN = "afterBegin";
    private static final String POSITION_BEFORE_END = "beforeEnd";
    private static final String POSITION_AFTER_END = "afterEnd";

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
     * @param value - the new value for the contents of this node
     */
    public void jsSet_innerHTML(final String value) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();

        for (final Iterator iter = parseHtmlSnippet(value).iterator(); iter.hasNext();) {
            final DomNode child = (DomNode) iter.next();
            domNode.appendChild(child);
        }
    }

    /**
     * Parses the html code
     * @param htmlSnippet the html code extract to parse
     * @return collection of {@link DomNode}: the parsed nodes
     */
    private Collection parseHtmlSnippet(final String htmlSnippet) {
        if (htmlSnippet.indexOf('<') >= 0) {
            // build a pseudo WebResponse
            final WebClient webClient = getDomNodeOrDie().getPage().getWebClient();
            final WebResponse webResp = new StringWebResponse("<html><body>" + htmlSnippet + "</body></html>");
            try {
                final WebWindow pseudoWindow = new WebWindow() {
                    public Page getEnclosedPage() {
                        return null;
                    }
                    public String getName() {
                        return null;
                    }
                    public WebWindow getParentWindow() {
                        return null;
                    }
                    public Object getScriptObject() {
                        return null;
                    }
                    public WebWindow getTopWindow() {
                        return null;
                    }
                    public WebClient getWebClient() {
                        return webClient;
                    }
                    public void setEnclosedPage(final Page page) {
                    }
                    public void setScriptObject(final Object scriptObject) {
                    }
                };
                final HtmlPage pseudoPage = HTMLParser.parse(webResp, pseudoWindow);
                final HtmlBody body = (HtmlBody) pseudoPage.getDocumentElement().getFirstChild();
                
                Collection nodes = new ArrayList();
                for (Iterator iter = body.getChildIterator(); iter.hasNext();) {
                    DomNode child = (DomNode) iter.next();
                    nodes.add(copy(child, getHtmlElementOrDie().getPage()));
                }
                return nodes;
            }
            catch (final IOException e) {
                // should not occur
                getLog().warn("Unexpected exception occured while setting innerHTML");
                return Collections.EMPTY_LIST;
            }
            catch (final Exception e) {
                return Collections.EMPTY_LIST;
            }
        }
        else {
            // just text, keep it simple
            final DomNode node = new DomText(getDomNodeOrDie().getPage(), htmlSnippet);
            return Collections.singleton(node);
        }
    }

    /**
     * Copies the node to make it available to the page.
     * All this stuff just to change the htmlPage_ property on all nodes!
     * @param _child
     * @param _page
     * @return a node with the same properties but bound to the page. 
     */
    private DomNode copy(final DomNode node, final HtmlPage page) {
        final DomNode copy;
        if (node instanceof DomText) {
            copy = new DomText(page, node.getNodeValue());
        }
        else {
            final HtmlElement htmlElt = (HtmlElement) node;
            IElementFactory factory = HTMLParser.getFactory(htmlElt.getNodeName());
            final AttributesImpl attributes = new AttributesImpl();
            for (Iterator iter = htmlElt.getAttributeEntriesIterator(); iter.hasNext();) {
                final Map.Entry entry = (Map.Entry) iter.next();
                attributes.addAttribute(null, (String) entry.getKey(), null, null, (String) entry.getValue());
            }
            copy = factory.createElement(page, node.getNodeName(), attributes);
            for (Iterator iter = node.getChildIterator(); iter.hasNext();) {
                final DomNode child = (DomNode) iter.next();
                copy.appendChild(copy(child, page));
            }
        }
        
        return copy;
    }


    /**
     * Inserts the given HTML text into the element at the location.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/insertadjacenthtml.asp">
     * MSDN documentation</a>
     * @param where specifies where to insert the HTML text, using one of the following value:
     *         beforeBegin, afterBegin, beforeEnd, afterEnd
     * @param text the HTML text to insert
     */
    public void jsFunction_insertAdjacentHTML(final String where, final String text) {
        final DomNode currentNode = getDomNodeOrDie();
        final DomNode node;
        final boolean append;
        
        // compute the where and how the new nodes should be added
        if (POSITION_AFTER_BEGIN.equals(where)) {
            if (currentNode.getFirstChild() == null) {
                // new nodes should appended to the children of current node
                node = currentNode;
                append = true;
            }
            else {
                // new nodes should be inserted before first child
                node = currentNode.getFirstChild();
                append = false;
            }
        }
        else if (POSITION_BEFORE_BEGIN.equals(where)) {
            // new nodes should be inserted before current node
            node = currentNode;
            append = false;
        }
        else if (POSITION_BEFORE_END.equals(where)) {
            // new nodes should appended to the children of current node
            node = currentNode;
            append = true;
        }
        else if (POSITION_AFTER_END.equals(where)) {
            if (currentNode.getNextSibling() == null) {
                // new nodes should appended to the children of parent node
                node = currentNode.getParentNode();
                append = true;
            }
            else {
                // new nodes should be inserted before current node's next sibling
                node = currentNode.getNextSibling();
                append = false;
            }
        }
        else {
            throw Context.reportRuntimeError("Illegal position value: \"" + where + "\"");
        }

        // add the new nodes
        for (final Iterator iter = parseHtmlSnippet(text).iterator(); iter.hasNext();) {
            final DomNode child = (DomNode) iter.next();
            if (append) {
                node.appendChild(child);
            }
            else {
                node.insertBefore(child);
            }
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
