/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Element;

/**
 * This is the array returned by the all property of Document.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class DocumentAllArray extends SimpleScriptable {
    private HtmlPage htmlPage_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public DocumentAllArray() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }


    /**
     * Initialize this object
     * @param page The HtmlPage that this object will retrive elements from.
     */
    public void initialize( final HtmlPage page ) {
        assertNotNull("page", page);
        htmlPage_ = page;
    }


    /**
     * <p>Return the object at the specified index.</p>
     *
     * <p>TODO: This implementation is particularly inefficient but without a way
     * to detect if an element has been inserted or removed, it isn't safe to
     * cache the array/<p>
     *
     * @param index The index
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get( final int index, final Scriptable start ) {
        final HtmlPage htmlPage = ((DocumentAllArray)start).htmlPage_;
        if( index == 0 ) {
            return getScriptableFor( htmlPage );
        }

        int elementCount = 1;

        final Iterator iterator = htmlPage.getXmlChildElements();
        while( iterator.hasNext() ) {
            final Element element = (Element)iterator.next();
            if( elementCount == index ) {
                return getScriptableFor( htmlPage.getHtmlElement(element) );
            }
            elementCount++;
        }

        return NOT_FOUND;
    }


    /**
     * Return the object at the specified id/name or NOT_FOUND.  First we search
     * for an element with this id, if that fails then we try the same string as
     * a name.  If more than one element has the same name then an array of
     * elements is returned.
     *
     * @param name The name.
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get( final String name, final Scriptable start ) {
        final Object result = super.get(name, start);
        if( result != NOT_FOUND ) {
            return result;
        }

        final HtmlPage htmlPage = ((DocumentAllArray)start).htmlPage_;
        try {
            final HtmlElement htmlElement = htmlPage.getHtmlElementById(name);
            return getScriptableFor(htmlElement);
        }
        catch( final ElementNotFoundException e ) {
            // Fall through and try to search by name
        }

        final List collectedElements = new ArrayList();
        final Iterator iterator = htmlPage.getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement htmlElement = (HtmlElement)iterator.next();
            if( htmlElement.getAttributeValue("name").equals(name) ) {
                collectedElements.add( htmlElement );
            }
        }

        switch( collectedElements.size() ) {
            case 0:  return NOT_FOUND;
            case 1:  return getScriptableFor((HtmlElement)collectedElements.get(0));

            default:
                final Object array[] = collectedElements.toArray();
                for( int i=0; i<array.length; i++ ) {
                    array[i] = getScriptableFor((HtmlElement)array[i]);
                }
                return array;
        }
    }


   /**
    * <p>Return the number of elements in this array</p>
    *
    * <p>TODO: This implementation is particularly inefficient but without a way
    * to detect if an element has been inserted or removed, it isn't safe to
    * cache the array/<p>
    *
    * @return The number of elements in the array
    */
    public Object jsGet_length() {

        final DocumentAllArray array = this;
        int elementCount = 1; // First one is <html>

        final HtmlPage htmlPage = array.htmlPage_;
        final Iterator iterator = htmlPage.getXmlChildElements();
        while( iterator.hasNext() ) {
            elementCount++;
            iterator.next();
        }

        return new Integer(elementCount);
    }


    /**
     * The javascript function document.all.tags()
     * @param context The javascript context
     * @param scriptable The javascript object that this function is being executed on.
     * @param args The arguments passed to this function.
     * @param function The actual function object
     * @return The matching objects
     */
    public static Object jsFunction_tags(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final DocumentAllArray array = (DocumentAllArray)scriptable;
        final String tagName = getStringArg(0, args, "");
        final List htmlElements = array.htmlPage_.getHtmlElementsByTagNames( Collections.singletonList(tagName) );
        final ListIterator iterator = htmlElements.listIterator();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            iterator.set( array.getScriptableFor(element) );
        }
        return new NativeArray( htmlElements.toArray() );
    }
}
