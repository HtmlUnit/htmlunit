/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  An abstract wrapper for html elements
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public abstract class HtmlElement {
    /** Constant meaning that the specified attribute was not defined */
    public static final String ATTRIBUTE_NOT_DEFINED = new String("");

    /** Constant meaning that the specified attribute was found but its value was empty */
    public static final String ATTRIBUTE_VALUE_EMPTY = new String("");

    private final Element element_;
    private final HtmlPage htmlPage_;

    /**
     * This is the javascript object corresponding to this html element.  It is
     * declared as Object so that we don't have a dependancy on the rhino jar
     * file.<p>
     *
     * It may be null if there isn't a corresponding javascript object.
     */
    private Object scriptObject_;


    /**
     *  Create an instance
     *
     * @param  element The page that contains this element
     * @param  htmlPage The xml element that represents this html element
     */
    protected HtmlElement( final HtmlPage htmlPage, final Element element ) {
        assertNotNull( "element", element );
        element_ = element;

        if( htmlPage == null && this instanceof HtmlPage ) {
            htmlPage_ = ( HtmlPage )this;
        }
        else {
            assertNotNull( "htmlPage", htmlPage );
            htmlPage_ = htmlPage;
        }
    }


    /**
     *  Return the XML node that corresponds to this component.
     *
     * @return  The node
     */
    public final Element getElement() {
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

        assertNotNull( "elementName", elementName );
        assertNotNull( "attributeName", attributeName );
        assertNotNull( "attributeValue", attributeValue );

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
        assertNotNull("element", element);

        final Attr attribute = (Attr)element.getAttributeNode(attributeName.toUpperCase());
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
        assertNotNull("element", element);
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
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public String asText()
        throws ElementNotFoundException {
        return getChildrenAsText();
    }


    /**
     *  Return a text string that represents all the child elements as they
     *  would be visible in a web browser
     *
     * @return  See above
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     * @see  #asText()
     */
    protected final String getChildrenAsText()
        throws ElementNotFoundException {

        final StringBuffer buffer = new StringBuffer();
        final NodeList nodeList = getElement().getChildNodes();
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
                buffer.append( ( ( CharacterData )node ).getData() );
            }
            else {
                throw new RuntimeException(
                    "Not implemented yet: getChildrenAsText() for other kinds of nodes" );
            }
        }

        return buffer.toString();
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
    protected Iterator getXmlChildElements() {
        final List list = new ArrayList();
        collectXmlChildElementsInto( list, getElement() );
        return Collections.unmodifiableList( list ).iterator();
    }


    /**
     * Return an iterator that will recursively iterate over every child element
     * below this one.
     * @return The iterator.
     */
    public Iterator getAllHtmlChildElements() {
        final List list = new ArrayList();
        collectXmlChildElementsInto( list, getElement() );

        final HtmlPage page = getPage();

        final int listSize = list.size();
        for( int i=0; i<listSize; i++ ) {
            list.set(i, page.getHtmlElement( (Element)list.get(i) ) );
        }

        return Collections.unmodifiableList( list ).iterator();
    }


    /**
     * Used by {@link #getXmlChildElements} to gather all the child nodes in a list.
     */
    private void collectXmlChildElementsInto( final List list, final Element currentElement ) {
        list.add( currentElement );

        final NodeList nodeList = currentElement.getChildNodes();
        final int nodeListLength = nodeList.getLength();

        for( int i = 0; i < nodeListLength; i++ ) {
            final Node node = nodeList.item( i );
            if( node instanceof Element ) {
                collectXmlChildElementsInto( list, ( Element )node );
            }
        }
    }


    /**
     *  Throw an exception. This is a convenience during development only - it
     *  will likely be removed in the future.
     */
    protected final void notImplemented() {
        throw new RuntimeException( "Not implemented yet" );
    }


    /**
     *  Throw a NullPointerException if the specified object is null.
     *
     * @param  description the description that will be passed into the
     *      NullPointerException
     * @param  object The object to check
     */
    protected final void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException( description );
        }
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

        assertNotNull( "elementName", elementName );
        assertNotNull( "attributeName", attributeName );
        assertNotNull( "attributeValue", attributeValue );

        final List list = getHtmlElementsByAttribute( elementName, attributeName, attributeValue );
        final int listSize = list.size();
        if( listSize == 0 ) {
            throw new ElementNotFoundException( elementName, attributeName, attributeValue );
        }
        else if( listSize > 1 ) {
            throw new IllegalStateException( "Found more than one matching element" );
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

        assertNotNull( "id", id );
        assertNotEmpty( "id", id );

        final Iterator iterator = getXmlChildElements();
        while( iterator.hasNext() ) {
            final Element xmlElement = ( Element )iterator.next();
            final String idValue = getAttributeValue( xmlElement, "id" );
            if( id.equals( idValue ) ) {
                return getPage().getHtmlElement( xmlElement );
            }
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

        assertNotNull( "elementName", elementName );
        assertNotNull( "attributeName", attributeName );
        assertNotNull( "attributeValue", attributeValue );

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
        assertNotNull( "acceptableTagNames", acceptableTagNames );

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
}

