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
package com.gargoylesoftware.htmlunit.javascript;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

/**
 * An array of elements. Used for the element arrays returned by <tt>document.all</tt>,
 * <tt>document.all.tags('x')</tt>, <tt>document.forms</tt>, <tt>window.frames</tt>, etc.
 * Note that this class must not be used for collections that can be modified, for example
 * <tt>map.areas</tt> and <tt>select.options</tt>.
 * 
 * @version $Revision$
 * @author Daniel Gredler
 */
public class ElementArray extends SimpleScriptable implements Function {

    private HtmlElement[] elements_;

    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public ElementArray() {
        elements_ = new HtmlElement[0];
    }

    /**
     * Sets the elements in this element array to the specified elements.
     * The specified list must contain <tt>HtmlElement</tt> instances.
     * @param list A list of the elements that the element array is to contain.
     */
    public final void setElements( List list ) {
        HtmlElement[] elements = new HtmlElement[ list.size() ];
        elements = (HtmlElement[]) list.toArray( elements );
        elements_ = elements;
    }

    /**
     * Javascript constructor. This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
        // Empty.
    }

    /**
     * @see Function#call(Context, Scriptable, Scriptable, Object[])
     */
    public final Object call( final Context context, final Scriptable scope, Scriptable thisObj, Object[] args)
    throws JavaScriptException {
        if( args.length == 0 ) {
            throw Context.reportRuntimeError( "Zero arguments; need an index or a key." );
        }
        return get( args[0] );
    }

    /**
     * @see Function#construct(Context, Scriptable, Object[])
     */
    public final Scriptable construct(Context arg0, Scriptable arg1, Object[] arg2)
    throws JavaScriptException {
        return null;
    }

    /**
     * Private helper that retrieves the item or items corresponding to the specified
     * index or key.
     * @param o The index or key corresponding to the element or elements to return.
     * @return The element or elements corresponding to the specified index or key.
     */
    private Object get( Object o ) {
        if( o instanceof Number ) {
            final Number n = (Number) o;
            final int i = n.intValue();
            return get( i, this );
        }
        else {
            final String key = String.valueOf( o );
            return get( key, this );
        }
    }

    /**
     * Returns the element at the specified index, or <tt>NOT_FOUND</tt> if the
     * index is invalid.
     * @see ScriptableObject#get(int, Scriptable)
     */
    public final Object get( final int index, final Scriptable start ) {
        final ElementArray array = (ElementArray) start;
        if( index >= 0 && index < array.elements_.length ) {
            final HtmlElement element = array.elements_[ index ];
            return getScriptableFor( element );
        }
        else {
            return NOT_FOUND;
        }
    }

    /**
     * Returns the element or elements that match the specified key. If it is the name
     * of a property, the property value is returned. If it is the id of an element in
     * the array, that element is returned. Finally, if it is the name of an element or
     * elements in the array, then all those elements are returned. Otherwise,
     * <tt>NOT_FOUND</tt> is returned.
     * @see ScriptableObject#get(String, Scriptable)
     */
    public final Object get( final String name, final Scriptable start ) {
        // If the name of a property was specified, return the property value.
        final Object result = super.get(name, start);
        if( result != NOT_FOUND ) {
            return result;
        }
        // See if there is an element in the element array with the specified id.
        for( int i = 0; i < elements_.length; i++ ) {
            final HtmlElement element = elements_[ i ];
            final String id = element.getId();
            if( id != null && id.equals(name) ) {
                return getScriptableFor( element );
            }
        }
        // See if there are any elements in the element array with the specified name.
        List matches = new ArrayList();
        for( int i = 0; i < elements_.length; i++ ) {
            final HtmlElement element = elements_[ i ];
            final String nameAtt = element.getAttributeValue( "name" );
            if( nameAtt != null && nameAtt.equals(name) ) {
                matches.add( element );
            }
        }
        if( matches.size() > 1 ) {
            final ElementArray array = (ElementArray) makeJavaScriptObject("ElementArray");
            array.setElements( matches );
            return array;
        }
        else if( matches.size() == 1 ) {
            final HtmlElement element = (HtmlElement) matches.get( 0 );
            return getScriptableFor( element );
        }
        // Nothing was found.
        return NOT_FOUND;
    }

    /**
     * Returns the length of this element array.
     * @return The length of this element array.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/length.asp">MSDN doc</a>
     */
    public final int jsGet_length() {
        return elements_.length;
    }

    /**
     * Retrieves the item or items corresponding to the specified index or key.
     * @param index The index or key corresponding to the element or elements to return.
     * @return The element or elements corresponding to the specified index or key.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/item.asp">MSDN doc</a>
     */
    public final Object jsFunction_item( final Object index ) {
        return get( index );
    }

    /**
     * Returns all the elements in this element array that have the specified tag name, or
     * <tt>NOT_FOUND</tt> if there are no elements with the specified tag name.
     * @param tagName The name of the tag of the elements to return.
     * @return All the elements in this element array that have the specified tag name.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/tags.asp">MSDN doc</a>
     */
    public final Object jsFunction_tags( final String tagName ) {
        final List matches = new ArrayList();
        for( int i = 0; i < elements_.length; i++ ) {
            final HtmlElement element = elements_[ i ];
            final String tag = element.getTagName();
            if( tag != null && tag.equals(tagName) ) {
                matches.add( element );
            }
        }
        if( ! matches.isEmpty() ) {
            final ElementArray array = (ElementArray) makeJavaScriptObject("ElementArray");
            array.setElements( matches );
            return array;
        }
        return NOT_FOUND;
    }

}
