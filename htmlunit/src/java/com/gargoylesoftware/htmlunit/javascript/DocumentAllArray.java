/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.mozilla.javascript.Scriptable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

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
     * Return the object at the specified index.
     *
     * @param index The index
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get( final int index, final Scriptable start ) {
        getLog().debug("Not implemented yet: document.all["+index+"]");
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
}
