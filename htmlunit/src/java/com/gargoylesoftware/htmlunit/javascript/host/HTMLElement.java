/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import org.w3c.dom.Element;

/**
 * The javascript object "HTMLElement" which is the base class for all html
 * objects.  This will typically wrap an instance of {@link HtmlElement}.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HTMLElement extends SimpleScriptable {
    private Style style_;

     /**
      * Create an instance.
      */
     public HTMLElement() {
     }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the style object for this element.
     *
     * @return The style object
     */
    public Object jsGet_style() {
        getLog().debug("HTMLElement.jsGet_Style() style=["+style_+"]");
        return style_;
    }


     /**
      * Set the html element that corresponds to this javascript object
      * @param htmlElement The html element
      */
     public void setHtmlElement( final HtmlElement htmlElement ) {
         super.setHtmlElement(htmlElement);

         style_ = (Style)makeJavaScriptObject("Style");
         style_.initialize(this);
     }


    /**
     * Return true if this element is disabled.
     * @return True if this element is disabled.
     */
    public boolean jsGet_disabled() {
        getLog().warn(
            "Getting the disabled attribute for non-submittable elements"
            + " is not allowed according to the HTML specification.  Be aware"
            + " that you are using a non-portable feature");
        return getHtmlElementOrDie().isAttributeDefined("disabled");
    }


    /**
     * Set whether or not to disable this element
     * @param disabled True if this is to be disabled.
     */
    public void jsSet_disabled( final boolean disabled ) {
        getLog().warn(
            "Setting the disabled attribute for non-submittable elements"
            + " is not allowed according to the HTML specification.  Be aware"
            + " that you are using a non-portable feature");
        final Element xmlElement = getHtmlElementOrDie().getElement();
        if( disabled ) {
            xmlElement.setAttribute("disabled", "disabled");
        }
        else {
            xmlElement.removeAttribute("disabled");
        }
    }


    /**
     * Return the tag name of this element.
     * @return The tag name
     */
    public String jsGet_tagName() {
        return getHtmlElementOrDie().getTagName().toUpperCase();
    }
}

