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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.Assert;
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

    private static final long serialVersionUID = -1860993922147246513L;
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
        Assert.notNull("action", action);
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
        Assert.notNull("method", method);
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
        Assert.notNull("target", target);
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
        Assert.notNull("encoding", encoding);
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

         if( formElementsArray_ == null ) {
             initialize();
         }

         final Object result = formElementsArray_.get( name, start );
         if( result != NOT_FOUND ) {
             return result;
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

