/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Element;

/**
 * A specialized creator that knows how to create input objects
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
class HtmlInputElementCreator extends HtmlElementCreator {

    /**
     * Create an HtmlElement for the specified xmlElement, contained in the specified page.
     *
     * @param page The page that this element will belong to.
     * @param xmlElement The xml element that this HtmlElement corresponds to.
     * @return The new HtmlElement.
     */
    HtmlElement create( final HtmlPage page, final Element xmlElement ) {
        if( xmlElement.getTagName().equals("input") == false ) {
            throw new IllegalArgumentException("tagName is not 'input': "+xmlElement.getTagName());
        }

        final String type = xmlElement.getAttribute("type").toLowerCase();

        if( type.length() == 0 ) {
            // This is really an illegal value but the common browsers seem to
            // treat it as a "text" input so we will as well.
            return new HtmlTextInput( page, xmlElement );
        }
        else if( type.equals("submit") ) {
            return new HtmlSubmitInput( page, xmlElement );
        }
        else if( type.equals("checkbox")) {
            return new HtmlCheckBoxInput( page, xmlElement );
        }
        else if( type.equals("radio")) {
            return new HtmlRadioButtonInput( page, xmlElement );
        }
        else if( type.equals("text")) {
            return new HtmlTextInput( page, xmlElement );
        }
        else if( type.equals("hidden")) {
            return new HtmlHiddenInput( page, xmlElement );
        }
        else if( type.equals("password")) {
            return new HtmlPasswordInput( page, xmlElement );
        }
        else if( type.equals("image")) {
            return new HtmlImageInput( page, xmlElement );
        }
        else if( type.equals("reset")) {
            return new HtmlResetInput( page, xmlElement );
        }
        else if( type.equals("button")) {
            return new HtmlButtonInput( page, xmlElement );
        }
        else if( type.equals("file")) {
            return new HtmlFileInput( page, xmlElement );
        }
        else {
            throw new IllegalStateException("Unexpected type ["+type+"]");
        }
    }
}

