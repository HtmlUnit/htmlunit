/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.javascript.FormElementsArray;
import java.io.IOException;
import org.mozilla.javascript.Scriptable;

/**
 * A javascript object representing a Form.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Form extends HTMLElement {

//    private NativeArray jsElements_;
//    private Map overridingProperties_ = Collections.synchronizedMap(new HashMap(89));
    private FormElementsArray formElementsArray_;

    /**
     * Create an instance.  A default constructor is required for all javascript objects.
     */
    public Form() { }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }


    /**
     * Initialize the object.
     *
     */
    public void initialize() {

        final HtmlForm htmlForm = getHtmlForm();
        htmlForm.setScriptObject(this);
        if( formElementsArray_ == null ) {
            formElementsArray_ = (FormElementsArray)makeJavaScriptObject("FormElementsArray");
            formElementsArray_.initialize( htmlForm );
        }
    }


    /**
     * Return the value of the javascript attribute "name".
     * @return The value of this attribute.
     */
    public String jsGet_name() {
        return getHtmlForm().getNameAttribute();
    }


    /**
     * Return the value of the javascript attribute "elements".
     * @return The value of this attribute.
     */
    public FormElementsArray jsGet_elements() {
        return formElementsArray_;
    }


    /**
     * Return the value of the javascript attribute "length".
     * @return The value of this attribute.
     */
    public int jsGet_length() {
        return jsGet_elements().jsGet_length();
    }


    /**
     * Return the value of the javascript attribute "action".
     * @return The value of this attribute.
     */
    public String jsGet_action() {
        return getHtmlForm().getActionAttribute();
    }


    /**
     * Set the value of the javascript attribute "action".
     * @param action The new value.
     */
    public void jsSet_action( final String action ) {
        assertNotNull("action", action);
        getHtmlForm().setActionAttribute(action);
    }


    /**
     * Return the value of the javascript attribute "method".
     * @return The value of this attribute.
     */
    public String jsGet_method() {
        return getHtmlForm().getMethodAttribute();
    }


    /**
     * Set the value of the javascript attribute "method".
     * @param method The new value.
     */
    public void jsSet_method( final String method ) {
        assertNotNull("method", method);
        getHtmlForm().setMethodAttribute(method);
    }


    /**
     * Return the value of the javascript attribute "target".
     * @return The value of this attribute.
     */
    public String jsGet_target() {
        return getHtmlForm().getTargetAttribute();
    }


    /**
     * Set the value of the javascript attribute "target".
     * @param target The new value.
     */
    public void jsSet_target( final String target ) {
        assertNotNull("target", target);
        getHtmlForm().setTargetAttribute(target);
    }


    /**
     * Return the value of the javascript attribute "encoding".
     * @return The value of this attribute.
     */
    public String jsGet_encoding() {
        return getHtmlForm().getEnctypeAttribute();
    }


    /**
     * Set the value of the javascript attribute "encoding".
     * @param encoding The new value.
     */
    public void jsSet_encoding( final String encoding ) {
        assertNotNull("encoding", encoding);
        getHtmlForm().setEnctypeAttribute(encoding);
    }


    private HtmlForm getHtmlForm() {
        return (HtmlForm)getHtmlElementOrDie();
    }


    private HtmlForm getHtmlFormOrNull() {
        return (HtmlForm)getHtmlElementOrNull();
    }


    /**
     * Submit the form.
     *
     * @throws IOException if an io error occurs
     */
    public void jsFunction_submit() throws IOException {
        getHtmlForm().submit();
    }


    /**
     * Reset this form
     */
    public void jsFunction_reset() {
        getHtmlForm().reset();
    }


     /**
      * Return the specified property or NOT_FOUND if it could not be found.
      * @param name The name of the property
      * @param start The scriptable object that was originally queried for this property
      * @return The property.
      */
     public Object get( final String name, final Scriptable start ) {
         final HtmlForm htmlForm = getHtmlFormOrNull();
         if(htmlForm == null ) {
             // The object hasn't fully been initialized yet so just pass through to the superclass
             return super.get(name, start);
         }

         if( formElementsArray_ != null ) {
             final Object result = formElementsArray_.get( name, start );
             if( result != NOT_FOUND ) {
                 return result;
             }
         }
         return super.get(name, start);
     }


     /**
      * Return the specified indexed property
      * @param index The index of the property
      * @param start The scriptable object that was originally queried for this property
      * @return The property.
      */
     public Object get( final int index, final Scriptable start ) {
         return jsGet_elements().get(index, start);
     }
}

