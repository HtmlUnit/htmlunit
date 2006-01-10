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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowImpl;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.IElementFactory;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.ElementArray;

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
 * @author Hans Donner
 * @author Bruce Faulkner
 */
public class HTMLElement extends NodeImpl {
    private static final long serialVersionUID = -6864034414262085851L;
    private static final int BEHAVIOR_ID_UNKNOWN = -1;
    private static final int BEHAVIOR_ID_CLIENT_CAPS = 0;
    private static final int BEHAVIOR_ID_HOMEPAGE = 1;
    private static final int BEHAVIOR_ID_DOWNLOAD = 2;
    private static final String BEHAVIOR_CLIENT_CAPS = "#default#clientCaps";
    private static final String BEHAVIOR_HOMEPAGE = "#default#homePage";
    private static final String BEHAVIOR_DOWNLOAD = "#default#download";
    static final String POSITION_BEFORE_BEGIN = "beforeBegin";
    static final String POSITION_AFTER_BEGIN = "afterBegin";
    static final String POSITION_BEFORE_END = "beforeEnd";
    static final String POSITION_AFTER_END = "afterEnd";

    private final Set behaviors_ = new HashSet();
    private int scrollLeft_ = 0;
    private int scrollTop_ = 0;

    /**
     * The tag names of the objects for which outerHTML is readonly 
     */
    private static final List OUTER_HTML_READONLY =
        Arrays.asList(new String[] {
            "caption", "col", "colgroup", "frameset", "html",
            "tbody", "td", "tfoot", "th", "thead", "tr"});

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
    public Object jsxGet_style() {
        return style_;
    }


    /**
     * Set the DOM node that corresponds to this javascript object
     * @param domNode The DOM node
     */
    public void setDomNode( final DomNode domNode ) {
        super.setDomNode(domNode);

        style_ = new Style(this);
    }


    /**
     * Return the element ID.
     * @return The ID of this element.
     */
    public String jsxGet_id() {
        return getHtmlElementOrDie().getId();
    }


    /**
     * Set the identifier this element.
     *
     * @param newId The new identifier of this element.
     */
    public void jsxSet_id( final String newId ) {
        getHtmlElementOrDie().setId( newId );
    }


    /**
     * Return true if this element is disabled.
     * @return True if this element is disabled.
     */
    public boolean jsxGet_disabled() {
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsxSet_disabled( final boolean disabled ) {
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
    public String jsxGet_tagName() {
        return getHtmlElementOrDie().getTagName().toUpperCase();
    }


    /**
     * Return the value of the named attribute.
     * @param name The name of the variable
     * @param start The scriptable to get the variable from.
     * @return The attribute value
     */
    public Object get( final String name, final Scriptable start ) {
        Object result = super.get( name, start );
        if ( result == NOT_FOUND ) {
            final HtmlElement htmlElement = getHtmlElementOrNull();
            // can name be an attribute of current element?
            // first approximation: attribute are all lowercase
            // this should be improved because it's wrong. For instance: tabIndex, hideFocus, acceptCharset
            if ( htmlElement != null && name.toLowerCase().equals(name)) {
                final String value = htmlElement.getAttributeValue(name);
                if (HtmlElement.ATTRIBUTE_NOT_DEFINED != value) {
                    getLog().debug("Found attribute for evalution of property \"" + name
                            + "\" for of " + start);
                    result = value;
                }
            }
        }

        return result;
    }

    /**
     * Gets the specified property.
     * @param attributeName attribute name.
     * @return The value of the specified attribute, <code>null</code> if the attribute is not defined
     */
    public String jsxFunction_getAttribute(final String attributeName) {
        final String value = getHtmlElementOrDie().getAttributeValue(attributeName);
        if (value == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            return null;
        }
        else {
            return value;
        }
    }

    /**
     * Set an attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-F68F082">
     * the DOM reference</a>
     * 
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    public void jsxFunction_setAttribute(final String name, final String value) {
        getHtmlElementOrDie().setAttributeValue(name, value);
    }

    /**
     * Remove an attribute.
     * 
     * @param name Name of the attribute to remove
     */
    public void jsxFunction_removeAttribute(final String name) {
        getHtmlElementOrDie().removeAttribute(name);
    }

    /**
     * Gets the attribute node for the specified attribute.
     * @param attributeName the name of the attribute to retrieve
     * @return the attribute node for the specified attribute.
     */
    public Object jsxFunction_getAttributeNode(final String attributeName) {
        final Attribute att = (Attribute) makeJavaScriptObject(Attribute.JS_OBJECT_NAME);
        att.init(attributeName, getHtmlElementOrDie());
        return att;
    }

    /**
     * Sets the attribute node for the specified attribute.
     * @param newAtt the attribute to set.
     * @return the replaced attribute node, if any.
     */
    public Attribute jsxFunction_setAttributeNode(final Attribute newAtt) {
        final String name = newAtt.jsxGet_name();
        final String value = newAtt.jsxGet_value();
        final Attribute replacedAtt = (Attribute) jsxFunction_getAttributeNode(name);
        replacedAtt.detachFromParent();
        getHtmlElementOrDie().setAttributeValue(name, value);
        return replacedAtt;
    }

    /**
     * Return all the elements with the specified tag name
     * @param tagName The name to search for
     * @return the list of elements
     */
    public Object jsxFunction_getElementsByTagName( final String tagName ) {
        final HtmlElement element = (HtmlElement) getDomNodeOrDie();
        final ElementArray collection = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);
        try {
            final String xpath = "//" + tagName.toLowerCase();
            collection.init(element, new HtmlUnitXPath(xpath, HtmlUnitXPath.buildSubtreeNavigator(element)));
        }
        catch (final JaxenException e) {
            throw Context.reportRuntimeError("Failed to initialize collection getElementsByTagName("
                    + tagName + "): " + e.getMessage());
        }

        return collection;
    }

    /**
     * Return the class defined for this element
     * @return the class name
     */
    public Object jsxGet_className() {
        return getHtmlElementOrDie().getAttributeValue("class");
    }

    /**
     * Set the class attribute for this element.
     * @param className - the new class name
     */
    public void jsxSet_className(final String className) {
        getHtmlElementOrDie().setAttributeValue("class", className);
    }

    /**
     * Get the innerHTML attribute
     * @return the contents of this node as html
     */
    public String jsxGet_innerHTML() {
        final StringBuffer buf = new StringBuffer();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie());

        return buf.toString();
    }

    /**
     * Get the innerText attribute
     * @return the contents of this node as text
     */
    public String jsxGet_innerText() {
        final StringBuffer buf = new StringBuffer();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie(), false);

        return buf.toString();
    }

    /**
     * Gets the outerHTML of the node.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/outerhtml.asp">
     * MSDN documentation</a>
     * @return the contents of this node as html 
     * (note: the formatting isn't currently exactly the same as IE)
     */
    public String jsxGet_outerHTML() {
        final StringBuffer buf = new StringBuffer();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printNode(buf, getDomNodeOrDie());

        return buf.toString();
    }

    private void printChildren(final StringBuffer buffer, final DomNode node) {
        printChildren(buffer, node, true);
    }

    private void printChildren(final StringBuffer buffer, final DomNode node, final boolean asInnerHTML) {
        for (final Iterator iter = node.getChildIterator(); iter.hasNext();) {
            printNode(buffer, (DomNode) iter.next(), asInnerHTML);
        }
    }

    private void printNode(final StringBuffer buffer, final DomNode node) {
        printNode(buffer, node, true);
    }

    private void printNode(
            final StringBuffer buffer, final DomNode node,
            final boolean asInnerHTML) {
        if (node instanceof DomCharacterData) {
            buffer.append(node.getNodeValue().replaceAll("  ", " ")); // remove white space sequences
        }
        else if (asInnerHTML) {
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

            printChildren(buffer, node, asInnerHTML);
            if (htmlElt.getFirstChild() != null) {
                buffer.append("</");
                buffer.append(htmlElt.getTagName());
                buffer.append(">");
            }
        }
        else {
            final HtmlElement htmlElement = (HtmlElement) node;
            if (htmlElement.getTagName().equals("p")) {
                buffer.append("\r\n"); // \r\n because it's to implement something IE specific
            }

            if (!htmlElement.getTagName().equals("script")) {
                printChildren(buffer, node, asInnerHTML);
            }

        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value - the new value for the contents of this node
     */
    public void jsxSet_innerHTML(final String value) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();

        for (final Iterator iter = parseHtmlSnippet(value).iterator(); iter.hasNext();) {
            final DomNode child = (DomNode) iter.next();
            domNode.appendChild(child);
        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value - the new value for the contents of this node
     */
    public void jsxSet_innerText(final String value) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();

        final DomNode node = new DomText(getDomNodeOrDie().getPage(), value);
        domNode.appendChild(node);
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * Sets the outerHTML of the node.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/outerhtml.asp">
     * MSDN documentation</a>
     * @param value - the new value for replacing this node
     */
    public void jsxSet_outerHTML(final String value) {
        final DomNode domNode = getDomNodeOrDie();

        if (OUTER_HTML_READONLY.contains(domNode.getNodeName())) {
            throw Context.reportRuntimeError("outerHTML is read-only for tag " + domNode.getNodeName());
        }

        for (final Iterator iter = parseHtmlSnippet(value).iterator(); iter.hasNext();) {
            final DomNode child = (DomNode) iter.next();
            domNode.insertBefore(child);
        }
        domNode.remove();
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
            final boolean jsEnabled = webClient.isJavaScriptEnabled();
            // disable js while interpreting the html snippet: it only needs to be interpreted
            // when integrated in the real page
            webClient.setJavaScriptEnabled(false);

            final WebResponse webResp = new StringWebResponse("<html><body>" + htmlSnippet + "</body></html>",
                    getDomNodeOrDie().getPage().getWebResponse().getUrl());
            try {
                final WebWindow pseudoWindow = new WebWindowImpl(webClient) {
                    public WebWindow getParentWindow() {
                        return null;
                    }
                    public WebWindow getTopWindow() {
                        return null;
                    }
                };
                final HtmlPage pseudoPage = HTMLParser.parse(webResp, pseudoWindow);
                final HtmlBody body = (HtmlBody) pseudoPage.getDocumentElement().getFirstChild();

                final Collection nodes = new ArrayList();
                for (final Iterator iter = body.getChildIterator(); iter.hasNext();) {
                    final DomNode child = (DomNode) iter.next();
                    nodes.add(copy(child, getHtmlElementOrDie().getPage()));
                }
                return nodes;
            }
            catch (final Exception e) {
                getLog().error("Unexpected exception occured while parsing html snippet", e);
                throw Context.reportRuntimeError("Unexpected exception occured while parsing html snippet: "
                        + e.getMessage());
            }
            finally {
                // set javascript enabled back to original state
                webClient.setJavaScriptEnabled(jsEnabled);
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
     * 
     * @param node The node to copy.
     * @param page The page containing the node.
     * @return a node with the same properties but bound to the page. 
     */
    private DomNode copy(final DomNode node, final HtmlPage page) {
        final DomNode copy;
        if (node instanceof DomText) {
            copy = new DomText(page, node.getNodeValue());
        }
        else {
            final HtmlElement htmlElt = (HtmlElement) node;
            final IElementFactory factory = HTMLParser.getFactory(htmlElt.getNodeName());
            copy = factory.createElement(page, node.getNodeName(), readAttributes(htmlElt));
            for (final Iterator iter = node.getChildIterator(); iter.hasNext();) {
                final DomNode child = (DomNode) iter.next();
                copy.appendChild(copy(child, page));
            }
        }

        return copy;
    }

    /**
     * Gets the attributes of the element in the form of a {@link org.xml.sax.Attributes} 
     * @param element the element to read the attributes from
     * @return the attributes
     */
    protected AttributesImpl readAttributes(final HtmlElement element) {
        final AttributesImpl attributes = new AttributesImpl();
        for (final Iterator iter = element.getAttributeEntriesIterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String name = (String) entry.getKey();
            final String value = (String) entry.getValue();
            attributes.addAttribute(null, name, name, null, value);
        }

        return attributes;
    }

    /**
     * Inserts the given HTML text into the element at the location.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/insertadjacenthtml.asp">
     * MSDN documentation</a>
     * @param where specifies where to insert the HTML text, using one of the following value:
     *         beforeBegin, afterBegin, beforeEnd, afterEnd
     * @param text the HTML text to insert
     */
    public void jsxFunction_insertAdjacentHTML(final String where, final String text) {
        final DomNode currentNode = getDomNodeOrDie();
        final DomNode node;
        final boolean append;

        // compute the where and how the new nodes should be added
        if (POSITION_AFTER_BEGIN.equalsIgnoreCase(where)) {
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
        else if (POSITION_BEFORE_BEGIN.equalsIgnoreCase(where)) {
            // new nodes should be inserted before current node
            node = currentNode;
            append = false;
        }
        else if (POSITION_BEFORE_END.equalsIgnoreCase(where)) {
            // new nodes should appended to the children of current node
            node = currentNode;
            append = true;
        }
        else if (POSITION_AFTER_END.equalsIgnoreCase(where)) {
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
     *   <li>#default#clientCaps</li>
     *   <li>#default#homePage</li>
     *   <li>#default#download</li>
     * </ul>
     * @param behavior the URL of the behavior to add, or a default behavior name
     * @return an identifier that can be user later to detach the behavior from the element
     */
    public int jsxFunction_addBehavior(final String behavior) {
        // if behavior already defined, then nothing to do
        if (behaviors_.contains(behavior)) {
            return 0;
        }

        if (BEHAVIOR_CLIENT_CAPS.equalsIgnoreCase(behavior)) {
            final Class c = getClass();
            defineProperty("availHeight", c, 0);
            defineProperty("availWidth", c, 0);
            defineProperty("bufferDepth", c, 0);
            defineProperty("colorDepth", c, 0);
            defineProperty("connectionType", c, 0);
            defineProperty("cookieEnabled", c, 0);
            defineProperty("cpuClass", c, 0);
            defineProperty("height", c, 0);
            defineProperty("javaEnabled", c, 0);
            defineProperty("platform", c, 0);
            defineProperty("systemLanguage", c, 0);
            defineProperty("userLanguage", c, 0);
            defineProperty("width", c, 0);
            defineFunctionProperties(new String[] {"addComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"clearComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"compareVersions"}, c, 0);
            defineFunctionProperties(new String[] {"doComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"getComponentVersion"}, c, 0);
            defineFunctionProperties(new String[] {"isComponentInstalled"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_CLIENT_CAPS;
        }
        else if (BEHAVIOR_HOMEPAGE.equalsIgnoreCase(behavior)) {
            final Class c = getClass();
            defineFunctionProperties(new String[] {"isHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"setHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"navigateHomePage"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_HOMEPAGE;
        }
        else if (BEHAVIOR_DOWNLOAD.equalsIgnoreCase(behavior)) {
            final Class c = getClass();
            defineFunctionProperties(new String[] {"startDownload"}, c, 0);
            behaviors_.add(BEHAVIOR_DOWNLOAD);
            return BEHAVIOR_ID_DOWNLOAD;
        }
        else {
            getLog().warn("Unimplemented behavior: " + behavior);
            return BEHAVIOR_ID_UNKNOWN;
        }
    }

    /**
     * Removes the behavior corresponding to the specified identifier from this element.
     * @param id the identifier for the behavior to remove
     */
    public void jsxFunction_removeBehavior(final int id) {
        switch (id) {
            case BEHAVIOR_ID_CLIENT_CAPS:
                delete("availHeight");
                delete("availWidth");
                delete("bufferDepth");
                delete("colorDepth");
                delete("connectionType");
                delete("cookieEnabled");
                delete("cpuClass");
                delete("height");
                delete("javaEnabled");
                delete("platform");
                delete("systemLanguage");
                delete("userLanguage");
                delete("width");
                delete("addComponentRequest");
                delete("clearComponentRequest");
                delete("compareVersions");
                delete("doComponentRequest");
                delete("getComponentVersion");
                delete("isComponentInstalled");
                behaviors_.remove(BEHAVIOR_CLIENT_CAPS);
                break;
            case BEHAVIOR_ID_HOMEPAGE:
                delete("isHomePage");
                delete("setHomePage");
                delete("navigateHomePage");
                behaviors_.remove(BEHAVIOR_HOMEPAGE);
                break;
            case BEHAVIOR_ID_DOWNLOAD:
                delete("startDownload");
                behaviors_.remove(BEHAVIOR_DOWNLOAD);
                break;
            default:
                getLog().warn("Unexpected behavior id: " + id + ". Ignoring.");
        }
    }

    //----------------------- START #default#clientCaps BEHAVIOR -----------------------

    /**
     * Returns the screen's available height. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's available height.
     */
    public int getAvailHeight() {
        return getWindow().jsxGet_screen().jsxGet_availHeight();
    }

    /**
     * Returns the screen's available width. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's available width.
     */
    public int getAvailWidth() {
        return getWindow().jsxGet_screen().jsxGet_availWidth();
    }

    /**
     * Returns the screen's buffer depth. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's buffer depth.
     */
    public int getBufferDepth() {
        return getWindow().jsxGet_screen().jsxGet_bufferDepth();
    }

    /**
     * Returns the screen's color depth. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's color depth.
     */
    public int getColorDepth() {
        return getWindow().jsxGet_screen().jsxGet_colorDepth();
    }

    /**
     * Returns the connection type being used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the connection type being used.
     * Current implementation always return "modem"
     */
    public String getConnectionType() {
        return "modem";
    }

    /**
     * Returns <tt>true</tt> if cookies are enabled. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return whether or not cookies are enabled.
     */
    public boolean getCookieEnabled() {
        return getWindow().jsxGet_navigator().jsxGet_cookieEnabled();
    }

    /**
     * Returns the type of CPU used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the type of CPU used.
     */
    public String getCpuClass() {
        return getWindow().jsxGet_navigator().jsxGet_cpuClass();
    }

    /**
     * Returns the screen's height. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's height.
     */
    public int getHeight() {
        return getWindow().jsxGet_screen().jsxGet_height();
    }

    /**
     * Returns <tt>true</tt> if Java is enabled. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return whether or not Java is enabled.
     */
    public boolean getJavaEnabled() {
        return getWindow().jsxGet_navigator().jsxFunction_javaEnabled();
    }

    /**
     * Returns the platform used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the platform used.
     */
    public String getPlatform() {
        return getWindow().jsxGet_navigator().jsxGet_platform();
    }

    /**
     * Returns the system language. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the system language.
     */
    public String getSystemLanguage() {
        return getWindow().jsxGet_navigator().jsxGet_systemLanguage();
    }

    /**
     * Returns the user language. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the user language.
     */
    public String getUserLanguage() {
        return getWindow().jsxGet_navigator().jsxGet_userLanguage();
    }

    /**
     * Returns the screen's width. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's width.
     */
    public int getWidth() {
        return getWindow().jsxGet_screen().jsxGet_width();
    }

    /**
     * Adds the specified component to the queue of components to be installed. Note
     * that no components ever get installed, and this call is always ignored. Part of
     * the <tt>#default#clientCaps</tt> default IE behavior implementation.
     * @param id the identifier for the component to install
     * @param idType the type of identifier specified
     * @param minVersion the minimum version of the component to install
     */
    public void addComponentRequest(final String id, final String idType, final String minVersion) {
        getLog().debug("Call to addComponentRequest(" + id + ", " + idType + ", " + minVersion + ") ignored.");
    }

    /**
     * Clears the component install queue of all component requests. Note that no components
     * ever get installed, and this call is always ignored. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     */
    public void clearComponentRequest() {
        getLog().debug("Call to clearComponentRequest() ignored.");
    }

    /**
     * Compares the two specified version numbers. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @param v1 the first of the two version numbers to compare
     * @param v2 the second of the two version numbers to compare
     * @return -1 if v1 < v2, 0 if v1 = v2, and 1 if v1 > v2
     */
    public int compareVersions(final String v1, final String v2) {
        final int i = v1.compareTo(v2);
        if(i == 0)     { return 0;  }
        else if(i < 0) { return -1; }
        else           { return 1;  }
    }

    /**
     * Downloads all the components queued via {@link #addComponentRequest(String, String, String)}.
     * @return <tt>true</tt> if the components are downloaded successfully.
     * Current implementation always return <code>false</code>
     */
    public boolean doComponentRequest() {
        return false;
    }

    /**
     * Returns the version of the specified component.
     * @param id the identifier for the component whose version is to be returned
     * @param idType the type of identifier specified
     * @return the version of the specified component.
     * Current implementation always return "1.0"
     */
    public String getComponentVersion(final String id, final String idType) {
        return "1.0";
    }

    /**
     * Returns <tt>true</tt> if the specified component is installed.
     * @param id the identifier for the component to check for
     * @param idType the type of id specified
     * @param minVersion the minimum version to check for
     * @return <tt>true</tt> if the specified component is installed.
     */
    public boolean isComponentInstalled(final String id, final String idType, final String minVersion) {
        return false;
    }

    //----------------------- START #default#download BEHAVIOR -----------------------
    /**
     * Implementation of the IE behavior #default#download
     * @param uri The URI of the download source
     * @param callback the mehtod which should be called when the download is finished
     * @see <a href="http://msdn.microsoft.com/workshop/author/behaviors/reference/methods/startdownload.asp">
     * MSDN documentation</a>
     * @throws MalformedURLException If the url cannot be created
     */
    public void startDownload(final String uri, final Function callback) throws MalformedURLException {
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        final URL url = page.getFullyQualifiedUrl(uri);
        if (!page.getWebResponse().getUrl().getHost().equals(url.getHost())) {
            throw Context.reportRuntimeError("Not authorized url: " + url);
        }
        final Thread t = new DownloadBehaviorDownloader(url, callback);
        getLog().debug("Starting download thread for " + url);
        t.start();
    }

    /**
     * A helper class for the IE behavior #default#download
     * This represents a download action. The download is handeled
     * asynchronously, when the download is finished, the method specified
     * by callback is called with one argument - the content of the response as string.
     * @see #startDownload(String, Function)
     * @author Stefan Anzinger <stefan@anzinger.net>
     */
    private class DownloadBehaviorDownloader extends Thread {
        private final  URL url_;
        private final Function callback_;
        private Context context_;

        /**
         * @param url The URL to download
         * @param callback The function to callback
         */
        public DownloadBehaviorDownloader(final URL url, final Function callback) {
            super("Downloader for behavior #default#download '" + url + "'");
            url_ = url;
            callback_ = callback;
        }

        /**
         * Does the download and calls the callback method
         */
        public void run() {
            context_ = Context.enter();
            try {
                final WebClient wc = getWindow().getWebWindow().getWebClient();
                final String content = wc.getPage(url_).getWebResponse().getContentAsString();
                String message = "Downloaded content: ";
                if(content.length() > 512) {
                    message += content.substring(512) + " ...";
                }
                else {
                    message += content;
                }
                getLog().debug(message);
                final Scriptable scope = callback_.getParentScope();
                final Object[] args = new Object[] { content };
                callback_.call( context_, scope, scope, args);
            }
            catch(final Exception e) {
                getLog().error("Behavior #default#download: Cannot download " + url_, e);
            }
            finally {
                Context.exit();
            }
        }
    }
    //----------------------- END #default#download BEHAVIOR -----------------------

    //----------------------- START #default#homePage BEHAVIOR -----------------------

    /**
     * Returns <tt>true</tt> if the specified URL is the web client's current
     * homepage and the document calling the method is on the same domain as the
     * user's homepage. Part of the <tt>#default#homePage</tt> default IE behavior
     * implementation.
     * @param url the URL to check
     * @return <tt>true</tt> if the specified URL is the current homepage
     */
    public boolean isHomePage(final String url) {
        try {
            final URL newUrl = new URL(url);
            final URL currentUrl = getDomNodeOrDie().getPage().getWebResponse().getUrl();
            final String home = getDomNodeOrDie().getPage().getWebClient().getHomePage();
            final boolean sameDomains = newUrl.getHost().equalsIgnoreCase(currentUrl.getHost());
            final boolean isHomePage = (home != null && home.equals(url));
            return (sameDomains && isHomePage);
        }
        catch(final MalformedURLException e) {
            return false;
        }
    }

    /**
     * Sets the web client's current homepage. Part of the <tt>#default#homePage</tt>
     * default IE behavior implementation.
     * @param url the new homepage URL
     */
    public void setHomePage(final String url) {
        getDomNodeOrDie().getPage().getWebClient().setHomePage(url);
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

    //----------------------- END #default#homePage BEHAVIOR -----------------------

    /**
     * Set the onclick event handler for this element.
     * @param onclick the new handler
     */
    public void jsxSet_onclick(final Function onclick) {
        getHtmlElementOrDie().setEventHandler("onclick", onclick);
    }

    /**
     * Get the onclick event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onclick() {
        return getHtmlElementOrDie().getEventHandler("onclick");
    }

    /**
     * Get the children of the current node.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/collections/children.asp">
     * MSDN documentation</a>
     * @return the child at the given position
     */
    public Object jsxGet_children() {
        final DomNode element = getDomNodeOrDie();
        final ElementArray children = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);

        try {
            final XPath xpath = new HtmlUnitXPath("./*", HtmlUnitXPath.buildSubtreeNavigator(element));
            children.init(element, xpath);
        }
        catch (final JaxenException e) {
            // should never occur
            throw Context.reportRuntimeError("Failed initializing children: " + e.getMessage());
        }
        return children;
    }

     /**
     * Set the ondblclick event handler for this element.
     * @param ondblclick the new handler     */
    public void jsxSet_ondblclick(final Function ondblclick) {
        getHtmlElementOrDie().setEventHandler("ondblclick", ondblclick);
    }

    /**
     * Get the ondblclick event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_ondblclick() {
        return getHtmlElementOrDie().getEventHandler("ondblclick");
    }

    /**
     * Set the onblur event handler for this element.
     * @param onblur the new handler
     */
    public void jsxSet_onblur(final Function onblur) {
        getHtmlElementOrDie().setEventHandler("onblur", onblur);
    }

    /**
     * Get the onblur event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onblur() {
        return getHtmlElementOrDie().getEventHandler("onblur");
    }

    /**
     * Set the onfocus event handler for this element.
     * @param onfocus the new handler
     */
    public void jsxSet_onfocus(final Function onfocus) {
        getHtmlElementOrDie().setEventHandler("onfocus", onfocus);
    }

    /**
     * Get the onfocus event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onfocus() {
        return getHtmlElementOrDie().getEventHandler("onfocus");
    }

    /**
     * Set the onkeydown event handler for this element.
     * @param onkeydown the new handler
     */
    public void jsxSet_onkeydown(final Function onkeydown) {
        getHtmlElementOrDie().setEventHandler("onkeydown", onkeydown);
    }

    /**
     * Get the onkeydown event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onkeydown() {
        return getHtmlElementOrDie().getEventHandler("onkeydown");
    }

    /**
     * Set the onkeypress event handler for this element.
     * @param onkeypress the new handler
     */
    public void jsxSet_onkeypress(final Function onkeypress) {
        getHtmlElementOrDie().setEventHandler("onkeypress", onkeypress);
    }

    /**
     * Get the onkeypress event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onkeypress() {
        return getHtmlElementOrDie().getEventHandler("onkeypress");
    }

    /**
     * Set the onkeyup event handler for this element.
     * @param onkeyup the new handler
     */
    public void jsxSet_onkeyup(final Function onkeyup) {
        getHtmlElementOrDie().setEventHandler("onkeyup", onkeyup);
    }

    /**
     * Get the onkeyup event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onkeyup() {
        return getHtmlElementOrDie().getEventHandler("onkeyup");
    }

    /**
     * Set the onmousedown event handler for this element.
     * @param onmousedown the new handler
     */
    public void jsxSet_onmousedown(final Function onmousedown) {
        getHtmlElementOrDie().setEventHandler("onmousedown", onmousedown);
    }

    /**
     * Get the onmousedown event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onmousedown() {
        return getHtmlElementOrDie().getEventHandler("onmousedown");
    }

    /**
     * Set the onmousemove event handler for this element.
     * @param onmousemove the new handler
     */
    public void jsxSet_onmousemove(final Function onmousemove) {
        getHtmlElementOrDie().setEventHandler("onmousemove", onmousemove);
    }

    /**
     * Get the onmousemove event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onmousemove() {
        return getHtmlElementOrDie().getEventHandler("onmousemove");
    }

    /**
     * Set the onmouseout event handler for this element.
     * @param onmouseout the new handler
     */
    public void jsxSet_onmouseout(final Function onmouseout) {
        getHtmlElementOrDie().setEventHandler("onmouseout", onmouseout);
    }

    /**
     * Get the onmouseout event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onmouseout() {
        return getHtmlElementOrDie().getEventHandler("onmouseout");
    }

    /**
     * Set the onmouseover event handler for this element.
     * @param onmouseover the new handler
     */
    public void jsxSet_onmouseover(final Function onmouseover) {
        getHtmlElementOrDie().setEventHandler("onmouseover", onmouseover);
    }

    /**
     * Get the onmouseover event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onmouseover() {
        return getHtmlElementOrDie().getEventHandler("onmouseover");
    }

    /**
     * Set the onmouseup event handler for this element.
     * @param onmouseup the new handler
     */
    public void jsxSet_onmouseup(final Function onmouseup) {
        getHtmlElementOrDie().setEventHandler("onmouseup", onmouseup);
    }

    /**
     * Get the onmouseup event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onmouseup() {
        return getHtmlElementOrDie().getEventHandler("onmouseup");
    }

    /**
     * Set the onresize event handler for this element.
     * @param onresize the new handler
     */
    public void jsxSet_onresize(final Function onresize) {
        getHtmlElementOrDie().setEventHandler("onresize", onresize);
    }

    /**
     * Get the onresize event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Function jsxGet_onresize() {
        return getHtmlElementOrDie().getEventHandler("onresize");
    }

    /**
     * Get the offsetHeight for this element.
     * @return a dummy value
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/offsetwidth.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_offsetHeight() {
        return 1;
    }

    /**
     * Get the offsetWidth for this element.
     * @return a dummy value
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/offsetWidth.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_offsetWidth() {
        return 1;
    }

    /**
     * Get the offsetLeft for this element.
     * @return a dummy value
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/offsetLeft.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_offsetLeft() {
        return 1;
    }

    /**
     * Get the offsetTop for this element.
     * @return a dummy value
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/offsetTop.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_offsetTop() {
        return 1;
    }

    /**
     * Get the offsetParent for this element
     * @return the offsetParent for this element
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/offsetparent.asp">
     * MSDN documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_el_ref20.html">Gecko DOM reference</a>
     */
    public Object jsxGet_offsetParent() {
        DomNode currentElement = getHtmlElementOrDie();
        Object offsetParent = null;
        while (currentElement != null) {
            final DomNode parentNode = currentElement.getParentNode();
            // According to the Microsoft and Mozilla documentation, and from experimentation
            // in the IE and Firefox browsers, the offsetParent is the container
            // (<td>, <table>, <body>) nearest to the node
            if ((parentNode instanceof HtmlTableDataCell) ||
                (parentNode instanceof HtmlTable) ||
                (parentNode instanceof HtmlBody)) {
                offsetParent = parentNode.getScriptObject();
                break;
            }
            currentElement = currentElement.getParentNode();
        }

        return offsetParent;
    }

    /**
     * Just for debug purposes.
     * {@inheritDoc}
     */
    public String toString() {
        return "HTMLElement for " + getHtmlElementOrNull();
    }


    /**
     * Get the scrollTop for this element.
     * @return a dummy value (default is 0)
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollTop.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollTop() {
        return scrollTop_;
    }

    /**
     * Set the scrollTop for this element.
     * @param scroll the new value
     */
    public void jsxSet_scrollTop(final int scroll) {
        scrollTop_ = scroll;
    }

    /**
     * Get the scrollLeft for this element.
     * @return a dummy value (default is 0)
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollLeft.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollLeft() {
        return scrollLeft_;
    }

    /**
     * Set the scrollLeft for this element.
     * @param scroll the new value
     */
    public void jsxSet_scrollLeft(final int scroll) {
        scrollLeft_ = scroll;
    }

    /**
     * Get the scrollHeight for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollHeight.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollHeight() {
        return 10;
    }

    /**
     * Get the scrollWidth for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/scrollWidth.asp">
     * MSDN documentation</a>
     */
    public int jsxGet_scrollWidth() {
        return 10;
    }
}
