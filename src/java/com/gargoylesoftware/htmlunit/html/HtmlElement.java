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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  An abstract wrapper for html elements
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 */
public abstract class HtmlElement {
    /** Constant meaning that the specified attribute was not defined */
    public static final String ATTRIBUTE_NOT_DEFINED = new String("");

    /** Constant meaning that the specified attribute was found but its value was empty */
    public static final String ATTRIBUTE_VALUE_EMPTY = new String("");

    private Element element_;
    private final HtmlPage htmlPage_;

    // We do lazy initialization on this since the vast majority of HtmlElement instances
    // won't need it.
    private PropertyChangeSupport propertyChangeSupport_ = null;

    /**
     * This is the javascript object corresponding to this html element.  It is
     * declared as Object so that we don't have a dependancy on the rhino jar
     * file.<p>
     *
     * It may be null if there isn't a corresponding javascript object.
     */
    private Object scriptObject_;

    /** The name of the "element" property.  Used when watching property change events. */
    public static final String PROPERTY_ELEMENT = "element";

    /**
     *  Create an instance
     *
     * @param  element The page that contains this element
     * @param  htmlPage The xml element that represents this html element
     */
    protected HtmlElement( final HtmlPage htmlPage, final Element element ) {
        if( this instanceof HtmlPage == false ) {
            Assert.notNull("element", element);
        }
        element_ = element;

        if( htmlPage == null && this instanceof HtmlPage ) {
            htmlPage_ = ( HtmlPage )this;
        }
        else {
            Assert.notNull( "htmlPage", htmlPage );
            htmlPage_ = htmlPage;
        }
    }


    /**
     * Set the xml element that maps to this html element.
     * @param element The xml element.
     */
    protected final void setElement( final Element element ) {
        Assert.notNull("element", element);

        if( element_ == null ) {
            final Object oldValue = element_;
            element_ = element;
            firePropertyChange(PROPERTY_ELEMENT, oldValue, element);
        }
        else {
            throw new IllegalStateException("element_ has already been set");
        }
    }


    /**
     *  Return the XML node that corresponds to this component.
     *
     * @return  The node
     */
    public Element getElement() {
        return element_;
    }


    /**
     *  Search for a specific xml element by element name, attribute name and
     *  attribute value. Return a list of matching elements.
     *
     * @param  elementName The element name
     * @param  attributeName The attribute name
     * @param  attributeValue The attribute value
     * @return  See above
     */
    protected final List getXmlElementsByAttribute(
            final String elementName,
            final String attributeName,
            final String attributeValue ) {

        Assert.notNull( "elementName", elementName );
        Assert.notNull( "attributeName", attributeName );
        Assert.notNull( "attributeValue", attributeValue );

        final List collectedElements = new ArrayList();

        final Iterator iterator = getXmlChildElements();
        while( iterator.hasNext() ) {
            final Element element = ( Element )iterator.next();

            if( getTagName(element).equals( elementName )
                 && getAttributeValue(element, attributeName ).equals( attributeValue ) ) {
                collectedElements.add( element );
            }
        }

        return collectedElements;
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
        return getAttributeValue( getElement(), attributeName );
    }


    /**
     *  Return the value of the specified attribute or an empty string.  If the
     * result is an empty string then it will be either {@link #ATTRIBUTE_NOT_DEFINED}
     * if the attribute wasn't specified or {@link #ATTRIBUTE_VALUE_EMPTY} if the
     * attribute was specified but it was empty.
     *
     * @param element The element from which we are getting the attribute.
     * @param attributeName the name of the attribute
     * @return  The value of the attribute or {@link #ATTRIBUTE_NOT_DEFINED}
     * or {@link #ATTRIBUTE_VALUE_EMPTY}
     */
    public final String getAttributeValue( final Element element, final String attributeName ) {
        Assert.notNull("element", element);

        final Attr attribute = element.getAttributeNode(attributeName.toUpperCase());
        if( attribute == null ) {
            return ATTRIBUTE_NOT_DEFINED;
        }

        final String attributeValue = attribute.getValue();
        if( attributeValue == null || attributeValue.length() == 0 ) {
            return ATTRIBUTE_VALUE_EMPTY;
        }
        else {
            return attributeValue;
        }
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
        return getAttributeValue(attributeName) != ATTRIBUTE_NOT_DEFINED;
    }


    /**
     * Return the tag name of this element.  The tag name is the actual html name.  For example
     * the tag name for HtmlAnchor is "a" and the tag name for HtmlTable is "table".
     * This tag name will always be in lowercase, no matter what case was used in the original
     * document.
     *
     * @return the tag name of this element.
     */
    public final String getTagName() {
        return getTagName(getElement());
    }


    /**
     * Return the tag name of the specified element.  The tag name is the actual html name.
     * For example the tag name for HtmlAnchor is "a" and the tag name for HtmlTable is "table".
     * This tag name will always be in lowercase, no matter what case was used in the original
     * document.
     *
     * @param element The element for which we are getting the tag name
     * @return the tag name of the specified element.
     */
    public final String getTagName( final Element element ) {
        Assert.notNull("element", element);
        return element.getTagName().toLowerCase();
    }


    /**
     *  Return the form that enclosed this element or null if this element is
     *  not within a form.
     *
     * @return  See above
     */
    public HtmlForm getEnclosingForm() {
        Node currentNode = getElement().getParentNode();
        while( currentNode != null ) {
            if( currentNode instanceof Element ) {
                final Element element = ( Element )currentNode;
                if( getTagName(element).equals( "form" ) ) {
                    return ( HtmlForm )getPage().getHtmlElement( element );
                }
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
     *  Return a string representation of this object
     *
     * @return  See above
     */
    public String toString() {
        final Element element = getElement();
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
        buffer.append( getTagName(element) );

        final NamedNodeMap attributeMap = element.getAttributes();
        final int attributeCount = attributeMap.getLength();

        int i;
        for( i = 0; i < attributeCount; i++ ) {
            buffer.append( ' ' );
            final Attr attribute = ( Attr )attributeMap.item( i );
            buffer.append( attribute.getName() );
            buffer.append( "=\"" );
            buffer.append( attribute.getValue() );
            buffer.append( "\"" );
        }
        buffer.append( ">]" );

        return buffer.toString();
    }


    /**
     *  Return a text representation of this element that represents what would
     *  be visible to the user if this page was shown in a web browser. For
     *  example, a select element would return the currently selected value as
     *  text
     *
     * @return  The element as text
     */
    public String asText() {
        String text = getChildrenAsText();

        // Translate non-breaking spaces to regular spaces.
        text = text.replace((char)160,' ');

        // Remove extra whitespace
        text = reduceWhitespace(text);
        return text;
    }


    /**
     *  Return a text string that represents all the child elements as they
     *  would be visible in a web browser
     *
     * @return  See above
     * @see  #asText()
     */
    protected final String getChildrenAsText() {

        final StringBuffer buffer = new StringBuffer();
        final NodeList nodeList = getElement().getChildNodes();
        if( nodeList == null ) {
            return "";
        }
        final int nodeCount = nodeList.getLength();
        final HtmlPage page = getPage();

        for( int i = 0; i < nodeCount; i++ ) {
            final Node node = nodeList.item( i );
            if( node instanceof Element ) {
                final Element xmlElement = ( Element )node;
                final HtmlElement htmlElement = page.getHtmlElement( xmlElement );
                buffer.append( htmlElement.asText() );
            }
            else if( node instanceof Comment ) {
                // Discard comments
            }
            else if( node instanceof CharacterData ) {
                buffer.append(( ( CharacterData )node ).getData());
            }
            else {
                throw new RuntimeException(
                    "Not implemented yet: getChildrenAsText() for other kinds of nodes" );
            }
        }

        return buffer.toString();
    }


    /**
     * Removes extra whitespace from a string similar to what a browser does
     * when it displays text.
     * @param text The text to clean up.
     * @return The cleaned up text.
     */
    private static String reduceWhitespace( final String text ) {
        final StringBuffer buffer = new StringBuffer( text.length() );
        final int length = text.length();
        boolean whitespace = false;
        for( int i = 0; i < length; ++i) {
            char ch = text.charAt(i);
            if( whitespace ) {
                if( Character.isWhitespace(ch) == false ) {
                    buffer.append(ch);
                    whitespace = false;
                }
            }
            else {
                if( Character.isWhitespace(ch) ) {
                    whitespace = true;
                    buffer.append(' ');
                }
                else {
                    buffer.append(ch);
                }
            }
        }
        return buffer.toString().trim();
    }


    /**
     *  Return the HtmlPage that contains this element
     *
     * @return  See above
     */
    public HtmlPage getPage() {
        return htmlPage_;
    }


    /**
     *  Return an iterator that will iterate recursively across all the child
     *  elements starting from the current element
     *
     * @return  See above
     */
    public Iterator getXmlChildElements() {
        final Element rootElement = getElement();
        if( rootElement == null ) {
            throw new IllegalStateException(
                "The xml element hasn't been set for this object.");
        }
        return new Iterator() {
            private Element nextElement = getFirstChildElement(rootElement);
            public boolean hasNext() {
                return nextElement != null;
            }
            public Object next() {
                final Element result = nextElement;
                moveToNext();
                return result;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
            private void moveToNext() {
                //System.out.println("moveToNext() entering nextElement_="+nextElement_);
                Element next = getFirstChildElement(nextElement);
                if( next == null ) {
                    next = getNextSibling(nextElement);
                }
                if( next == null ) {
                    next = getNextElementUpwards(nextElement);
                }
                if( next == rootElement ) {
                    next = null;
                }
                nextElement = next;
                //System.out.println("moveToNext() leaving nextElement_="+nextElement_);

            }
            private Element getNextElementUpwards( final Element startingElement ) {
                Assert.notNull("startingElement", startingElement);

                if( startingElement == rootElement ) {
                    return startingElement;
                }

                final Element parent = (Element)startingElement.getParentNode();
                if( parent == rootElement ) {
                    return parent;
                }

                Node next = parent.getNextSibling();
                while( next != null && next instanceof Element == false ) {
                    next = next.getNextSibling();
                }

                if( next == null ) {
                    return getNextElementUpwards(parent);
                }
                else {
                    return (Element)next;
                }
            }
            private Element getFirstChildElement( final Element parent ) {
                Node node = parent.getFirstChild();
                while( node != null && node instanceof Element == false ) {
                    node = node.getNextSibling();
                }
                return (Element)node;
            }
            private Element getNextSibling( final Element parent ) {
                Node node = parent.getNextSibling();
                while( node != null && node instanceof Element == false ) {
                    node = node.getNextSibling();
                }
                return (Element)node;
            }
        };
        /*
        final List list = new ArrayList();
        collectXmlChildElementsInto( list, getElement() );
        return Collections.unmodifiableList( list ).iterator();
        */
    }


    /**
     * Return an iterator that will recursively iterate over every child element
     * below this one.
     * @return The iterator.
     */
    public Iterator getAllHtmlChildElements() {
        final Iterator xmlIterator = getXmlChildElements();
        final HtmlPage page = getPage();

        return new Iterator() {
            public boolean hasNext() {
                return xmlIterator.hasNext();
            }
            public Object next() {
                final Element xmlElement = (Element)xmlIterator.next();
                return page.getHtmlElement(xmlElement);
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
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
        else if( listSize > 1 ) {
            throw new IllegalStateException( "Found more than one matching element: elementName=["
                +elementName+"] attributeName=["+attributeName+"] attributeValue=["+attributeValue+"]");
        }

        return ( HtmlElement )list.get( 0 );
    }


    /**
     *  Return the html element with the specified id. If more than one element
     *  has this id (not allowed by the html spec) then return the first one.
     *
     * @param  id The id to search by
     * @return  The html element
     * @exception  ElementNotFoundException If no element was found that matches
     *      the id
     */
    public HtmlElement getHtmlElementById( final String id )
        throws ElementNotFoundException {

        Assert.notNull( "id", id );
        assertNotEmpty( "id", id );

        final Document xmlDocument = getElement().getOwnerDocument();
        final Element xmlElement = xmlDocument.getElementById(id);
        if( xmlElement != null ) {
            return getPage().getHtmlElement( xmlElement );
        }

        throw new ElementNotFoundException( "*", "id", id );
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

        Assert.notNull( "elementName", elementName );
        Assert.notNull( "attributeName", attributeName );
        Assert.notNull( "attributeValue", attributeValue );

        final List xmlElements = getXmlElementsByAttribute(
                elementName, attributeName, attributeValue );
        final List htmlElements = new ArrayList( xmlElements.size() );
        final HtmlPage page = getPage();

        final Iterator iterator = xmlElements.iterator();
        while( iterator.hasNext() ) {
            htmlElements.add( page.getHtmlElement( ( Element )iterator.next() ) );
        }
        return htmlElements;
    }


    /**
     *  Given a list of tag names, return the html elements that correspond to
     *  any matching element
     *
     * @param  acceptableTagNames The list of tag names to search by.
     * @return The list of tag names
     */
    public final List getHtmlElementsByTagNames( final List acceptableTagNames ) {
        Assert.notNull( "acceptableTagNames", acceptableTagNames );

        final HtmlPage page = getPage();
        final List collectedElements = new ArrayList();

        final Iterator xmlIterator = getXmlChildElements();
        while( xmlIterator.hasNext() ) {
            final Element xmlElement = ( Element )xmlIterator.next();
            if( acceptableTagNames.contains( getTagName(xmlElement) ) ) {
                collectedElements.add( page.getHtmlElement( xmlElement ) );
            }
        }

        return collectedElements;
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
     *  Return a list of child HtmlElement's of this element
     *
     * @return  A list of HtmlElement objects
     */
    public final List getChildElements() {
        final NodeList childNodes = getElement().getChildNodes();
        final int childCount = childNodes.getLength();
        final HtmlPage page = getPage();
        final List result = new ArrayList();

        for( int i = 0; i < childCount; i++ ) {
            final Node node = childNodes.item( i );
            if( node instanceof Element ) {
                result.add( page.getHtmlElement( ( Element )node ) );
            }
        }

        return result;
    }


    /**
     * Return the parent element of this element
     * @return the parent element of this element
     */
    public HtmlElement getParent() {
        return getPage().getHtmlElement((Element)getElement().getParentNode());
    }


    /**
     * Return the next sibling element in the document
     * @return The next sibling element in the document.
     */
    public HtmlElement getNextSibling() {
        return getPage().getHtmlElement((Element)getElement().getNextSibling());
    }


    /**
     * Return the previous sibling element in the document
     * @return The previous sibling element in the document.
     */
    public HtmlElement getPreviousSibling() {
        return getPage().getHtmlElement((Element)getElement().getPreviousSibling());
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Set the javascript object that corresponds to this element.  This is not guarenteed
     * to be set even if there is a javascript object for this html element.
     * @param scriptObject The javascript object.
     */
    public void setScriptObject( final Object scriptObject ) {
        scriptObject_ = scriptObject;
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Return the javascript object that corresponds to this element.
     * @return The javascript object that corresponds to this element.
     */
    public Object getScriptObject() {
        return scriptObject_;
    }


    /**
     * Return the log object for this element.
     * @return The log object for this element.
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }


    /**
     * Return a string representation of the xml document from this element and all
     * it's children (recursively).
     *
     * @return The xml string.
     */
    public String asXml() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        printXml(getElement(), "", printWriter);
        printWriter.close();
        return stringWriter.toString();
    }


    private void printXml( final Node node, final String indent, final PrintWriter printWriter ) {
        final boolean hasChildren = (node.getFirstChild() != null);
        if( node instanceof Element ) {
            final Element element = (Element)node;
            printWriter.print(indent+"<"+element.getTagName().toLowerCase());
            final NamedNodeMap attributeMap = element.getAttributes();
            final int attributeCount = attributeMap.getLength();
            for( int i=0; i<attributeCount; i++ ) {
                printWriter.print(" ");
                final Attr attribute = (Attr)attributeMap.item(i);
                printWriter.print(attribute.getName().toLowerCase());
                printWriter.print("=\"");
                printWriter.print(attribute.getValue());
                printWriter.print("\"");
            }

            if( hasChildren == false ) {
                printWriter.print("/");
            }
            printWriter.println(">");
        }
        else if( node instanceof CharacterData ) {
            printWriter.print(indent);
            printWriter.println( ((CharacterData)node).getData());
        }
        else {
            printWriter.println(indent+node);
        }
        Node child = node.getFirstChild();
        while (child != null) {
            printXml(child, indent+"  ", printWriter);
            child = child.getNextSibling();
        }
        if( hasChildren && node instanceof Element ) {
            printWriter.println(indent+"</"+((Element)node).getTagName().toLowerCase()+">");
        }
    }


    /**
     * Add a property change listener to this element.
     * @param listener The new listener.
     */
    public final synchronized void addPropertyChangeListener(
        final PropertyChangeListener listener ) {

        Assert.notNull("listener", listener);
        if( propertyChangeSupport_ == null ) {
            propertyChangeSupport_ = new PropertyChangeSupport(this);
        }
        propertyChangeSupport_.addPropertyChangeListener(listener);
    }


    /**
     * Remove a property change listener from this element.
     * @param listener The istener.
     */
    public final synchronized void removePropertyChangeListener(
        final PropertyChangeListener listener ) {

        Assert.notNull("listener", listener);
        if( propertyChangeSupport_ != null ) {
            propertyChangeSupport_.removePropertyChangeListener(listener);
        }
    }


    /**
     * Fire a property change event
     * @param propertyName The name of the property.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected final synchronized void firePropertyChange(
        final String propertyName, final Object oldValue, final Object newValue ) {

        if( propertyChangeSupport_ != null ) {
            propertyChangeSupport_.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
}

