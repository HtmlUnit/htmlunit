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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
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
    private static final long serialVersionUID = 1515527212503251703L;
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
     * @param form The HtmlForm that this object will retrive elements from.
     */
    public void initialize( final HtmlForm form ) {
        Assert.notNull("form", form);
        htmlForm_ = form;
    }


    /**
     * <p>Return the object at the specified index.</p>
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
                if( htmlElement instanceof HtmlInput ) {
                    final List collectedRadioButtons = new ArrayList(elementList.size());
                    collectedRadioButtons.add(getScriptableFor(htmlElement));
                    while( iterator.hasNext() ) {
                        htmlElement = (HtmlElement)iterator.next();
                        if( htmlElement instanceof HtmlInput
                            && ((HtmlInput)htmlElement).getNameAttribute().equals(name) ) {
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
