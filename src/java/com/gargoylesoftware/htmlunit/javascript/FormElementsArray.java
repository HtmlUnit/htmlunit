/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

/**
 * This is the array returned by the "elements" property of Form.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class FormElementsArray extends SimpleScriptable {
    private HtmlForm htmlForm_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public FormElementsArray() {
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
    public void initialize( final HtmlForm form ) {
        Assert.notNull("form", form);
        htmlForm_ = form;
    }


    /**
     * <p>Return the object at the specified index.</p>
     *
     * <p>TODO: This implementation is particularly inefficient but without a way
     * to detect if an element has been inserted or removed, it isn't safe to
     * cache the array.<p>
     *
     * @param index The index
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get( final int index, final Scriptable start ) {
        final HtmlForm htmlForm = ((FormElementsArray)start).htmlForm_;
        final List formElements = getHtmlElementsInForm(htmlForm);
        try {
            return getScriptableFor( (HtmlElement)formElements.get(index) );
        }
        catch( final IndexOutOfBoundsException e ) {
            return NOT_FOUND;
        }
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
         final HtmlForm htmlForm = htmlForm_;
         if(htmlForm == null ) {
             // The object hasn't fully been initialized yet so just pass through to the superclass
             return super.get(name, start);
         }

        final List elementList = getHtmlElementsInForm(htmlForm);
        HtmlElement htmlElement;
        final Iterator iterator = elementList.iterator();
        while( iterator.hasNext() ) {
            htmlElement = (HtmlElement)iterator.next();
            if( htmlElement.getAttributeValue("name").equals(name) ) {
                if( htmlElement instanceof HtmlRadioButtonInput ) {
                    final List collectedRadioButtons = new ArrayList(elementList.size());
                    collectedRadioButtons.add(getScriptableFor(htmlElement));
                    while( iterator.hasNext() ) {
                        htmlElement = (HtmlElement)iterator.next();
                        if( htmlElement instanceof HtmlRadioButtonInput
                            && ((HtmlRadioButtonInput)htmlElement).getNameAttribute().equals(name) ) {
                            collectedRadioButtons.add(getScriptableFor(htmlElement));
                        }
                    }

                    switch( collectedRadioButtons.size() ) {
                        case 0:  return NOT_FOUND;
                        case 1:  return collectedRadioButtons.get(0);
                        default: return new NativeArray( collectedRadioButtons.toArray() );
                    }
                }
                else {
                    return getScriptableFor(htmlElement);
                }
            }
        }

        return super.get(name, start);
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
    public int jsGet_length() {
        return getHtmlElementsInForm(htmlForm_).size();
    }


    private List getHtmlElementsInForm( final HtmlForm htmlForm ) {
        Assert.notNull("htmlForm", htmlForm);

        return htmlForm.getHtmlElementsByTagNames(
            Arrays.asList( new String[]{
                "input", "button", /*"option",*/ "select", "textarea"
            } ) );
    }
}
