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

import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.OptionsArray;

/**
 * The javascript object that represents a select
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author Marc Guillemot
 */
public class Select extends FormField {

    private static final long serialVersionUID = 4332789476842114628L;
    private OptionsArray optionsArray_;

    /**
     * Create an instance.
     */
    public Select() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Initialize the object.
     *
     */
    public void initialize() {

        final HtmlSelect htmlSelect = getHtmlSelect();
        htmlSelect.setScriptObject(this);
        if( optionsArray_ == null ) {
            optionsArray_ = (OptionsArray)makeJavaScriptObject("OptionsArray");
            optionsArray_.initialize( htmlSelect );
        }
    }

    /**
     * Remove option at the specified index
     * @param index The index of the item to remove
     */
    public void jsFunction_remove(final int index) {
        put(index, null, null);
    }

    /**
     * Add a new item to the list (optionally) before the specified item
     * @param newOptionObject The DomNode to insert
     * @param beforeOptionObject The DomNode to insert the previous element before (null if at end)
     */
    public void jsFunction_add(final Object newOptionObject, final Object beforeOptionObject) {

        final HtmlSelect select = getHtmlSelect();

        final Option option = (Option) newOptionObject;
        final Option beforeOption = (Option) beforeOptionObject;

        HtmlOption htmlOption = (HtmlOption) option.getHtmlElementOrNull();
        if ( htmlOption == null ) {
            initJavaScriptObject( option );
            htmlOption = new HtmlOption(select.getPage(), null);
            option.setDomNode( htmlOption );
        }

        if (beforeOption == null) {
            select.appendChild(htmlOption);
        }
        else {
            final DomNode before = beforeOption.getDomNodeOrDie();
            before.insertBefore(htmlOption);
        }
    }

    /**
     * Return the type of this input.
     * @return The type
     */
    public String jsGet_type() {
        final String type;
        if (getHtmlSelect().isMultipleSelectEnabled()) {
            type = "select-multiple";
        }
        else {
            type = "select-one";
        }
        return type;
    }


    /**
     * Return the value of the "options" property
     * @return The options property
     */
    public OptionsArray jsGet_options() {

        if( optionsArray_ == null ) {
            initialize();
        }
        return optionsArray_;
    }


    /**
     * Return the value of the "selectedIndex" property
     * @return The selectedIndex property
     */
    public int jsGet_selectedIndex() {
        final HtmlSelect htmlSelect = getHtmlSelect();
        final List selectedOptions = htmlSelect.getSelectedOptions();
        if( selectedOptions.isEmpty() ) {
            return -1;
        }
        else {
            final List allOptions = htmlSelect.getAllOptions();
            return allOptions.indexOf(selectedOptions.get(0));
        }
    }


    /**
     * Set the value of the "selectedIndex" property
     * @param index The new value
     */
    public void jsSet_selectedIndex( final int index ) {
        final HtmlSelect htmlSelect = getHtmlSelect();
        
        final Iterator iter = htmlSelect.getSelectedOptions().iterator();
        while (iter.hasNext()){
            final HtmlOption itemToUnSelect = (HtmlOption) iter.next();
            htmlSelect.setSelectedAttribute(itemToUnSelect, false);
        }
        if (index < 0){
            htmlSelect.fakeSelectedAttribute("");
            return;
        }
        
        final List allOptions = htmlSelect.getAllOptions();
         
        final HtmlOption itemToSelect = (HtmlOption) allOptions.get(index);
        htmlSelect.setSelectedAttribute(itemToSelect, true);
    }
    
    /**
     * Return the actual value of the selected Option
     * @return The value
     */
    public String jsGet_value() {
        final int selectedIndex = jsGet_selectedIndex();
        final Option selectedOption = (Option) jsGet_options().jsFunction_item(selectedIndex);
        return selectedOption.jsGet_value();
    }

    /**
     * Return the value of the "length" property
     * @return The length property
     */
    public int jsGet_length() {
        if( optionsArray_ == null ) {
            initialize();
        }
        return optionsArray_.jsGet_length();
    }


    /**
     * Remove options by reducing the "length" property
     * @param newLength The new length property value
     */
    public void jsSet_length( final int newLength ) {
        if( optionsArray_ == null ) {
            initialize();
        }
        optionsArray_.jsSet_length( newLength );
    }

    /**
     * Return the specified indexed property
     * @param index The index of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get( final int index, final Scriptable start ) {
        if( optionsArray_ == null ) {
            initialize();
        }
        return optionsArray_.get( index, start );
    }


    /**
     * Set the index property
     * @param index The index
     * @param start The scriptable object that was originally invoked for this property
     * @param newValue The new value
     */
    public void put( final int index, final Scriptable start, final Object newValue ) {
        if( optionsArray_ == null ) {
            initialize();
        }
        optionsArray_.put( index, start, newValue );
    }


    /**
     * Return the HTML select object.
     * @return The HTML select object.
     */
    private HtmlSelect getHtmlSelect() {
        return (HtmlSelect)getHtmlElementOrDie();
    }

    
    /**
     * Selects the option with the specified value
     * @param newValue The value of the option to select
     */
    public void jsSet_value(final String newValue) {
        getHtmlSelect().setSelectedAttribute(newValue, true);
    }
}

