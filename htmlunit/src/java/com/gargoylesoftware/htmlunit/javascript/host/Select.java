/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import java.util.List;

/**
 * The javascript object that represents a select
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Select extends Input {

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
     * Return the type of this input.
     * @return The type
     */
    public String jsGet_type() {
        final String type;
        if( getHtmlElementOrDie().getAttributeValue("multiple").length() == 0 ) {
            type = "select-one";
        }
        else {
            type = "select-multiple";
        }
        return type;
    }


    /**
     * Return the value of the "options" property
     * @return The options property
     */
    public Option[] jsGet_options() {
        final HtmlSelect htmlSelect = (HtmlSelect)getHtmlElementOrDie();
        final List allOptions = htmlSelect.getAllOptions();
        final Option optionArray[] = new Option[allOptions.size()];

        for( int i=0; i<optionArray.length; i++ ) {
            final HtmlOption htmlOption = (HtmlOption)allOptions.get(i);
            Option jsOption = (Option)htmlOption.getScriptObject();
            if( jsOption == null ) {
                jsOption = (Option)makeJavaScriptObject("Option");
                jsOption.setHtmlElement(htmlOption);
                htmlOption.setScriptObject(jsOption);
            }
            optionArray[i] = jsOption;
        }
        return optionArray;
    }


    /**
     * Return the value of the "selectedIndex" property
     * @return The selectedIndex property
     */
    public int jsGet_selectedIndex() {
        final HtmlSelect htmlSelect = (HtmlSelect)getHtmlElementOrDie();
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
        getLog().debug("select.selectedIndex not implemented yet");
    }


    /**
     * Return the value of the "length" property
     * @return The length property
     */
    public int jsGet_length() {
        final HtmlSelect htmlSelect = (HtmlSelect)getHtmlElementOrDie();
        return htmlSelect.getAllOptions().size();
    }
}

