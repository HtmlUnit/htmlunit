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
package com.gargoylesoftware.htmlunit.html;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;

/**
 *  An abstract wrapper for html elements
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Mike Gallaher
 */
public abstract class HtmlElement extends DomNode {

    /** Constant meaning that the specified attribute was not defined */
    public static final String ATTRIBUTE_NOT_DEFINED = new String("");

    /** Constant meaning that the specified attribute was found but its value was empty */
    public static final String ATTRIBUTE_VALUE_EMPTY = new String("");

    /** the map holding the attribute values by name */
    private Map attributes_;

    /** the map holding event handlers */
    private Map eventHandlers_;
    
    /**
     * Counter to provide unique function names when defining event handlers 
     */
    private static int EventHandlerWrapperFunctionCounter_ = 0;

    /**
     *  Create an instance
     *
     * @param htmlPage The page that contains this element
     * @param attributes a map ready initialized with the attributes for this element, or
     * <code>null</code>. The map will be stored as is, not copied.
     */
    protected HtmlElement(final HtmlPage htmlPage, final Map attributes) {

        super(htmlPage);
        eventHandlers_ = Collections.EMPTY_MAP;
        if(attributes != null) {
            attributes_ = attributes;
            attributesToEventHandlers();
        }
        else {
            attributes_ = Collections.EMPTY_MAP;
        }
    }

    /**
     * Overrides DomNode.cloneNode so clone gets its own Map of attributes.
     * 
     * @see DomNode#cloneNode(boolean)
     */
    public DomNode cloneNode(boolean deep) {
        final HtmlElement newnode = (HtmlElement) super.cloneNode(deep);

        newnode.attributes_ = new HashMap();
      
        for (Iterator it = attributes_.keySet().iterator(); it.hasNext();) {
            final Object key = it.next();

            if ("id".equals(key)) {
                continue;
            }

            newnode.setAttributeValue((String) key, (String) attributes_.get(key));
        }

        newnode.setId(ATTRIBUTE_VALUE_EMPTY);

        return newnode;
    }
    
    /**
     *  Return the value of the specified attribute or an empty string.  If the
     * result is an empty string then it will be either {@link #ATTRIBUTE_NOT_DEFINED}
     * if the attribute wasn't specified or {@link #ATTRIBUTE_VALUE_EMPTY} if the
     * attribute was specified but it was empty.
     *
     * @param  attributeName the name of the attribute
     * @return  The value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED}
     * or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    public final String getAttributeValue( final String attributeName ) {

        String value = (String)attributes_.get(attributeName.toLowerCase());

        //return value != null ? value : ATTRIBUTE_NOT_DEFINED;
        if(value != null) {
            return value;
        }
        else {
            return ATTRIBUTE_NOT_DEFINED;
        }
    }

    /**
     * Set the value of the specified attribute.
     *
     * @param attributeName the name of the attribute
     * @param attributeValue The value of the attribute
     */
    public final void setAttributeValue(String attributeName, String attributeValue) {

        if(attributes_ == Collections.EMPTY_MAP) {
            attributes_ = new HashMap();
        }
        if(attributeValue.length() == 0) {
            attributeValue = ATTRIBUTE_VALUE_EMPTY;
        }
        boolean isId = attributeName.equalsIgnoreCase("id");
        if (isId) {
            getPage().removeIdElement(this);
        }
        attributes_.put(attributeName.toLowerCase(), attributeValue);
        if (isId) {
            getPage().addIdElement(this);
        }
    }

    /**
     * remove an attribute from this element
     * @param attributeName the attribute attributeName
     */
    public final void removeAttribute(String attributeName) {
        if (attributeName.equalsIgnoreCase("id")) {
            getPage().removeIdElement(this);
        }
        attributes_.remove(attributeName.toLowerCase());
    }

    /**
     * Return true if the specified attribute has been defined.  This is neccessary
     * in order to distinguish between an attribute that is set to an empty string
     * and one that was not defined at all.
     *
     * @param attributeName The attribute to check
     * @return true if the attribute is defined
     */
    public boolean isAttributeDefined( final String attributeName ) {
        return attributes_.get(attributeName.toLowerCase()) != null;
    }

    /**
     * @return an iterator over the {@link java.util.Map.Entry} objects representing the
     * attributes of this element. Each entry holds a string key and a string value
     */
    public Iterator getAttributeEntriesIterator() {
        return attributes_.entrySet().iterator();
    }

    /**
     *  Return a Function to be executed when a given event occurs.
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @return A rhino javascript executable Function, or null if no event
     * handler has been defined
     */
    public final Function getEventHandler(final String eventName) {
       return (Function) eventHandlers_.get(eventName);
    }
    
    /**
     * Register a Function as an event handler.
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @param eventHandler A rhino javascript executable Function
     */
    public final void setEventHandler(final String eventName, final Function eventHandler) {
       if (eventHandlers_ == Collections.EMPTY_MAP) {
          eventHandlers_ = new HashMap();
       }
       eventHandlers_.put(eventName, eventHandler);
    }
    
    /**
     * Register a snippet of javascript code as an event handler.  The javascript code will
     * be wrapped inside a unique function declaration which provides one argument named
     * "event"
     * @param eventName Name of event such as "onclick" or "onblur", etc.
     * @param jsSnippet executable javascript code
     */
    public final void setEventHandler(final String eventName, final String jsSnippet) {
       
      BaseFunction function = new BaseFunction() {
        private static final long serialVersionUID = 3257850965406068787L;
        
        private String eventHandlerWrapperName_;
          public Object call(final Context cx, final Scriptable scope,
            final Scriptable thisObj, final Object[] args)
            throws JavaScriptException {
    
            if (eventHandlerWrapperName_ == null) {
                final String functionDeclaration = wrapSnippet();
                cx.evaluateString(scope, functionDeclaration, HtmlElement.class.getName(), 1, null);
            }

            final StringBuffer expression = new StringBuffer(eventHandlerWrapperName_);

            if (args.length==1) {
                ScriptableObject.putProperty(scope, "event", args[0]);
                expression.append("(event)");
            }
            else {
                expression.append("()");
            }

            final Object result = cx.evaluateString(scope, expression.toString(), "sourceName", 1, null);

            return result;
        }

        private String wrapSnippet() {
            eventHandlerWrapperName_ = "gargoyleEventHandlerWrapper" + EventHandlerWrapperFunctionCounter_++;

            final StringBuffer buffer = new StringBuffer();

            buffer.append("function ");
            buffer.append(eventHandlerWrapperName_);
            buffer.append("(event) {");
            buffer.append(jsSnippet);
            buffer.append("}");

            return buffer.toString();
        }
      };
       
      setEventHandler(eventName, function);
    }

    /**
     * Return the tag name of this element.  The tag name is the actual html name.  For example
     * the tag name for HtmlAnchor is "a" and the tag name for HtmlTable is "table".
     * This tag name will always be in lowercase, no matter what case was used in the original
     * document.
     *
     * @return the tag name of this element.
     */
    public abstract String getTagName();

    /** @return the node type */
    public short getNodeType() {
        return DomNode.ELEMENT_NODE;
    }

    /**
     * @return The same value as returned by {@link #getTagName()},
     */
    public String getNodeName() {
        return getTagName();
    }

    /**
     * @return the identifier of this element.
     */
    public final String getId() {
        return getAttributeValue("id");
    }


    /**
     * Set the identifier this element.
     *
     * @param newId The new identifier of this element.
     */
    public final void setId(final String newId) {
        setAttributeValue("id", newId);
    }


    /**
     *  Return the form that enclosed this element or null if this element is
     *  not within a form.
     *
     * @return  See above
     */
    public HtmlForm getEnclosingForm() {

        DomNode currentNode = getParentNode();
        while(currentNode != null) {
            if(currentNode instanceof HtmlForm) {
                return (HtmlForm)currentNode;
            }
            currentNode = currentNode.getParentNode();
        }
        return null;
    }


    /**
     *  Return the form that enclosed this element or throw an exception if this element is
     *  not within a form.
     *
     * @return  See above
     * @throws IllegalStateException If the element is not within a form.
     */
    public HtmlForm getEnclosingFormOrDie() throws IllegalStateException {
        final HtmlForm form = getEnclosingForm();
        if( form == null ) {
            throw new IllegalStateException("Element is not comtained within a form: "+this);
        }

        return form;
    }

    /**
     * recursively write the XML data for the node tree starting at <code>node</code>
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    protected void printXml( String indent, PrintWriter printWriter ) {

        final boolean hasChildren = (getFirstChild() != null);
        printWriter.print(indent+"<"+getTagName().toLowerCase());
        Map attributeMap = attributes_;
        for( Iterator it=attributeMap.keySet().iterator(); it.hasNext(); ) {
            printWriter.print(" ");
            String name = (String)it.next();
            printWriter.print(name);
            printWriter.print("=\"");
            printWriter.print(attributeMap.get(name));
            printWriter.print("\"");
        }

        if( ! hasChildren ) {
            printWriter.print("/");
        }
        printWriter.println(">");
        printChildrenAsXml( indent, printWriter );
        if( hasChildren ) {
            printWriter.println(indent+"</"+getTagName().toLowerCase()+">");
        }
    }


    /**
     *  Return a string representation of this object
     *
     * @return  See above
     */
    public String toString() {
        final StringBuffer buffer = new StringBuffer();

        final String className = getClass().getName();
        final int index = className.lastIndexOf( '.' );
        if( index == -1 ) {
            buffer.append( className );
        }
        else {
            buffer.append( className.substring( index + 1 ) );
        }

        buffer.append( "[<" );
        buffer.append(getTagName());

        for(Iterator iterator=attributes_.keySet().iterator(); iterator.hasNext(); ) {
            buffer.append( ' ' );
            String name = (String)iterator.next();
            buffer.append(name);
            buffer.append( "=\"" );
            buffer.append(attributes_.get(name));
            buffer.append( "\"" );
        }
        buffer.append( ">]" );

        return buffer.toString();
    }


    /**
     *  Throw an exception. This is a convenience during development only - it
     *  will likely be removed in the future.
     */
    protected final void notImplemented() {
        throw new RuntimeException( "Not implemented yet" );
    }


    /**
     *  Assert that the specified string is not empty. Throw an exception if it
     *  is.
     *
     * @param  description The description to pass into the exception if this
     *      string is empty
     * @param  string The string to check
     * @throws  IllegalArgumentException If the string is empty
     */
    protected final void assertNotEmpty( final String description, final String string )
        throws IllegalArgumentException {

        if( string.length() == 0 ) {
            throw new IllegalArgumentException( "String may not be empty: " + description );
        }
    }


    /**
     *  Search by the specified criteria and return the first HtmlElement that
     *  is found
     *
     * @param  elementName The name of the element
     * @param  attributeName The name of the attribute
     * @param  attributeValue The value of the attribute
     * @return  The HtmlElement
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public final HtmlElement getOneHtmlElementByAttribute(
            final String elementName,
            final String attributeName,
            final String attributeValue )
        throws
            ElementNotFoundException {

        Assert.notNull( "elementName", elementName );
        Assert.notNull( "attributeName", attributeName );
        Assert.notNull( "attributeValue", attributeValue );

        final List list = getHtmlElementsByAttribute( elementName, attributeName, attributeValue );
        final int listSize = list.size();
        if( listSize == 0 ) {
            throw new ElementNotFoundException( elementName, attributeName, attributeValue );
        }

        return ( HtmlElement )list.get( 0 );
    }


    /**
     *  Return the html element with the specified id. If more than one element
     *  has this id (not allowed by the html spec) then return the first one.
     *
     * @param  id The id value to search by
     * @return  The html element found
     * @exception  ElementNotFoundException If no element was found that matches
     *      the id
     */
    public HtmlElement getHtmlElementById( final String id )
        throws ElementNotFoundException {

        return getPage().getHtmlElementById(id);
    }


    /**
     *  Return true if there is a element with the specified id. This method
     * is intended for situations where it is enough to know whether a specific
     * element is present in the document.<p>
     *
     * Implementation note: This method calls getHtmlElementById() internally
     * so writing code like the following would be extremely inefficient.
     * <pre>
     * if( hasHtmlElementWithId(id) ) {
     *     HtmlElement element = getHtmlElementWithId(id)
     *     ...
     * }
     * </pre>
     *
     * @param  id The id to search by
     * @return  true if an element was found with the specified id.
     */
    public boolean hasHtmlElementWithId( final String id ) {
        try {
            getHtmlElementById(id);
            return true;
        }
        catch( final ElementNotFoundException e ) {
            return false;
        }
    }


    /**
     *  Search by the specified criteria and return all the HtmlElement that
     *  are found
     *
     * @param  elementName The name of the element
     * @param  attributeName The name of the attribute
     * @param  attributeValue The value of the attribute
     * @return  A list of HtmlElements
     */
    public final List getHtmlElementsByAttribute(
            final String elementName,
            final String attributeName,
            final String attributeValue ) {

        List list = new ArrayList();
        DescendantElementsIterator iterator = new DescendantElementsIterator();

        while(iterator.hasNext()) {
            HtmlElement next = iterator.nextElement();
            if(next.getTagName().equals(elementName)) {
                String attValue = next.getAttributeValue(attributeName);
                if(attValue != null && attValue.equals(attributeValue)) {
                    list.add(next);
                }
            }
        }
        return list;
    }

    /**
     *  Given a list of tag names, return the html elements that correspond to
     *  any matching element
     *
     * @param  acceptableTagNames The list of tag names to search by.
     * @return The list of tag names
     */
    public final List getHtmlElementsByTagNames( final List acceptableTagNames ) {

        List list = new ArrayList();
        DescendantElementsIterator iterator = new DescendantElementsIterator();

        while(iterator.hasNext()) {
            HtmlElement next = iterator.nextElement();
            if(acceptableTagNames.contains(next.getTagName())) {
                list.add(next);
            }
        }
        return list;
    }

    /**
     *  Given a list of tag names, return the html elements that correspond to
     *  any matching element
     *
     * @param  tagName the tag name to match
     * @return The list of tag names
     */
    public final List getHtmlElementsByTagName( final String tagName ) {

        List list = new ArrayList();
        DescendantElementsIterator iterator = new DescendantElementsIterator();

        while(iterator.hasNext()) {
            HtmlElement next = iterator.nextElement();
            if(tagName.equals(next.getTagName())) {
                list.add(next);
            }
        }
        return list;
    }

    /**
     *  Create a URL object from the specified href
     *
     * @param  href The href
     * @return  A URL
     * @exception  MalformedURLException If a URL cannot be created from this
     *      href
     * @deprecated Use {@link HtmlPage#getFullyQualifiedUrl(String)} instead.
     */
    public final URL makeUrlFromHref( String href )
        throws MalformedURLException {

            return getPage().getFullyQualifiedUrl(href);
    }

    /**
     * @return an iterator over the HtmlElement children of this object, i.e. excluding the
     * non-element nodes
     */
    public final ChildElementsIterator getChildElementsIterator() {
        return new ChildElementsIterator();
    }

    /**
    * Convert javascript snippets defined in the attribute map to executable event handlers.
    * Should be called only on construction.
    */
    private void attributesToEventHandlers() {
        for (final Iterator iter=attributes_.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String eventName = (String) entry.getKey();

            if (eventName.startsWith("on")) {
                setEventHandler(eventName, (String) entry.getValue());
            }
        }

    }

   /**
     * an iterator over the HtmlElement children
     */
    protected class ChildElementsIterator implements Iterator {

        private HtmlElement nextElement_;

        /** constructor */
        public ChildElementsIterator() {
            if(getFirstChild() != null) {
                if(getFirstChild() instanceof HtmlElement) {
                    nextElement_ = (HtmlElement)getFirstChild();
                }
                else {
                    setNextElement(getFirstChild());
                }
            }
        }

        /** @return is there a next one ? */
        public boolean hasNext() {
            return nextElement_ != null;
        }

        /** @return the next one */
        public Object next() {
            return nextElement();
        }

        /** remove the current one */
        public void remove() {
            if(nextElement_ == null) {
                throw new IllegalStateException();
            }
            if(nextElement_.getPreviousSibling() != null) {
                nextElement_.getPreviousSibling().remove();
            }
        }

        /** @return the next element */
        public HtmlElement nextElement() {
            if(nextElement_ != null) {
                HtmlElement result = nextElement_;
                setNextElement(nextElement_);
                return result;
            }
            else {
                throw new NoSuchElementException();
            }
        }

        private void setNextElement(DomNode node) {
            DomNode next = node.getNextSibling();
            while(next != null && !(next instanceof HtmlElement)) {
                next = next.getNextSibling();
            }
            nextElement_ = (HtmlElement)next;
        }
    }
}
