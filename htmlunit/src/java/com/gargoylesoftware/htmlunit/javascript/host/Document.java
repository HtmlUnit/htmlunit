/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.DocumentAllArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

/**
 * A javascript object for a Document
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class Document extends HTMLElement {
    private NativeArray allForms_;
    private DocumentAllArray allArray_;


    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Document() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }


    /**
     * Initialize this object
     */
    public void initialize() {
        final List jsForms = new ArrayList();

        final List formElements
            = getHtmlPage().getHtmlElementsByTagNames( Collections.singletonList("form") );
        final Iterator iterator = formElements.iterator();
        while( iterator.hasNext() ) {
            final HtmlForm htmlForm = (HtmlForm)iterator.next();
            final String formName = htmlForm.getAttributeValue("name");
            if( formName.length() != 0 ) {
                final Form jsForm = (Form)makeJavaScriptObject("Form");
                jsForm.setHtmlElement( htmlForm );
                jsForm.initialize();
                jsForms.add(jsForm);
                defineProperty(formName, jsForm, READONLY);
            }
        }

        final int attributes = READONLY;
        final Form[] array = new Form[jsForms.size()];
        jsForms.toArray(array);
        allForms_ = new NativeArray(array);
        for( int i=0; i<array.length; i++ ) {
            final String name = array[i].getHtmlElementOrDie().getAttributeValue("name");
            allForms_.defineProperty(name, array[i], attributes);
        }

        allArray_ = (DocumentAllArray)makeJavaScriptObject("DocumentAllArray");
        allArray_.initialize( (HtmlPage)getHtmlElementOrDie() );
    }


    /**
     * Return the html page that this document is modeling..
     * @return The page.
     */
    public HtmlPage getHtmlPage() {
        return (HtmlPage)getHtmlElementOrDie();
    }


    /**
     * Return the value of the javascript attribute "forms".
     * @return The value of this attribute.
     */
    public NativeArray jsGet_forms() {
        return allForms_;
    }


    /**
     * javascript function "write".
     * @param text The text to write
     */
    public static Object jsFunction_write(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final String content = getStringArg(0, args, "");
        return null;
    }


    /**
     * javascript function "writeln".
     * @param text The text to write
     */
    public static Object jsFunction_writeln(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final String content = getStringArg(0, args, "");
        return jsFunction_write(context, scriptable, new Object[]{ content+"\n" }, function);
    }


    /**
     * Return the cookie attribute.  Currently hardcoded to return an empty string
     * @return The cookie attribute
     */
    public String jsGet_cookie() {
        getLog().debug("Document.cookie not supported: returning empty string");
        return "";
    }


    /**
     * Return the value of the "location" property.
     * @return The value of the "location" property
     */
    public Location jsGet_location() {
        final WebWindow webWindow = ((HtmlPage)getHtmlElementOrDie()).getEnclosingWindow();
        return ((Window)webWindow.getScriptObject()).jsGet_location();
    }


    /**
     * Return the value of the "images" property.
     * @return The value of the "images" property
     */
    public NativeArray jsGet_images() {
        getLog().debug("Not implemented yet: document.images");
        return new NativeArray(new Image[0]);
    }


    /**
     * Return the value of the "referrer" property.
     * @return The value of the "referrer" property
     */
    public String jsGet_referrer() {
        getLog().debug("Not implemented yet: document.referrer");
        return "";
    }


    /**
     * Return the value of the "URL" property.
     * @return The value of the "URL" property
     */
    public String jsGet_URL() {
        getLog().debug("Not implemented yet: document.URL");
        return "";
    }


    /**
     * Return the value of the "all" property.
     * @return The value of the "all" property
     */
    public DocumentAllArray jsGet_all() {
        return allArray_;
    }


    /**
     * Close an output stream
     */
    public void jsFunction_close() {
        getLog().debug("Not implemented yet: document.close()");
    }


    /**
     * Return the element with the specified id or NOT_FOUND of that element could not be found
     * @param id The id to search for
     * @return the element or NOT_FOUND
     */
    public Object jsFunction_getElementById( final String id ) {
        Object result = NOT_FOUND;
        try {
            final HtmlElement htmlElement = getHtmlElementOrDie().getPage().getHtmlElementById(id);
            final Object jsElement = getScriptableFor(htmlElement);

            if( jsElement == NOT_FOUND ) {
                getLog().debug("getElementById("+id
                    +") cannot return a result as there isn't a javascript object for the html element "
                    + htmlElement.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch( final ElementNotFoundException e ) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }

}

