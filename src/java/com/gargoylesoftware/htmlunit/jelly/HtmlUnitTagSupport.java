/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.jelly;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;

/**
 * Abstract superclass for all the HtmlUnit jelly tags.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public abstract class HtmlUnitTagSupport extends TagSupport {
    /** The var attribute which is the name of the variable to put the result into */
    private String var_;
    private String page_;


    /**
     * Create an instance
     */
    public HtmlUnitTagSupport() {
    }


    /**
     * Callback from Jelly to set the value of the page attribute.
     * @param page The new value.
     */
    public void setPage( final String page ) {
        page_ = page;
    }


    /**
     * Return the page specified in the script
     * @return The page.
     * @throws JellyTagException If a problem occurs.
     */
    protected final Page getPage() throws JellyTagException {
        if( page_ == null ) {
            throw new JellyTagException("page is a mandatory attribute");
        }

        final Object object = getContext().getVariable(page_);
        try {
            return (Page)object;
        }
        catch( final ClassCastException e ) {
            throw new JellyTagException("Expected page object in variable ["+page_+"] but found ["+object+"]");
        }
    }


    /**
     * Return the HtmlPage specified in the script.  If the page is not an instance of HtmlPage then
     * throw a JellyTagException
     * @return the HtmlPage
     * @throws JellyTagException If a problem occurs.
     */
    protected final HtmlPage getHtmlPage() throws JellyTagException {
        final Page page = getPage();
        if( page instanceof HtmlPage ) {
            return (HtmlPage)page;
        }
        else {
            throw new JellyTagException("Page isn't an instance of HtmlPage: "+page.getClass().getName());
        }
    }


    /**
     * Callback from Jelly to set the value of the var attribute.
     * @param var The new value.
     */
    public void setVar( final String var ) {
        var_ = var;
    }


    /**
     * Return the value of the "var" attribute or throw an exception if it hasn't been set.
     * @return The value of var
     * @throws JellyTagException If var hasn't been set.
     */
    public String getVarOrDie() throws JellyTagException {
        if( var_ == null ) {
            throw new JellyTagException("var is a mandatory attribute");
        }
        return var_;
    }
}
