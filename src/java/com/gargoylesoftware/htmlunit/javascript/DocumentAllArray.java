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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
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

/**
 * This is the array returned by the all property of Document.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
public class DocumentAllArray extends SimpleScriptable {
    private static final long serialVersionUID = 898458356155974768L;
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
        Assert.notNull("page", page);
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

        int elementCount = 0;

        final Iterator iterator = htmlPage.getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if( elementCount == index ) {
                return getScriptableFor( element );
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
            final HtmlElement htmlElement = htmlPage.getDocumentElement().getHtmlElementById(name);
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
        int elementCount = 0;

        final HtmlPage htmlPage = array.htmlPage_;
        final Iterator iterator = htmlPage.getAllHtmlChildElements();
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
        final List htmlElements = array.htmlPage_.getDocumentElement().getHtmlElementsByTagNames(
            Collections.singletonList(tagName) );
        final ListIterator iterator = htmlElements.listIterator();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            iterator.set( array.getScriptableFor(element) );
        }
        return new NativeArray( htmlElements.toArray() );
    }
}
