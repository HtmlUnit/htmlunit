/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.NativeArray;

/**
 * A javascript object representing a Form.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Form extends HTMLElement {

    private NativeArray jsElements_;
    private Map overridingProperties_ = Collections.synchronizedMap(new HashMap(89));

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

        getHtmlElementOrDie().setScriptObject(this);

        final List allJsElements = new ArrayList();

        final int attributes = READONLY;
        final List elementList = getHtmlForm().getHtmlElementsByTagNames(
            Arrays.asList( new String[]{
                "input", "button", "option", "select", "textarea"
            } ) );

        final List radioButtons = new ArrayList();

        final Iterator iterator = elementList.iterator();
        while( iterator.hasNext() ) {
            final HtmlElement htmlElement = (HtmlElement)iterator.next();
            final String name = htmlElement.getAttributeValue("name");
            if( name.length() != 0 ) {
                if( htmlElement instanceof HtmlRadioButtonInput ) {
                    radioButtons.add(htmlElement);
                }
                else {
                    final String className = getClassNameForFormElement(htmlElement);
                    final SimpleScriptable jsElement = makeJavaScriptObject( className );
                    jsElement.setHtmlElement(htmlElement);

                    allJsElements.add(jsElement);
                    overridingProperties_.put( name, jsElement );
//                    put( name, this, jsElement );
                }
            }
        }

        final Scriptable array[] = new Scriptable[allJsElements.size()];
        allJsElements.toArray(array);
        jsElements_ = new NativeArray(array);
        final Iterator scriptableIterator = allJsElements.iterator();
        while( scriptableIterator.hasNext() ) {
            final SimpleScriptable scriptable = (SimpleScriptable)scriptableIterator.next();
            final String name = scriptable.getHtmlElementOrDie().getAttributeValue("name");
            jsElements_.defineProperty(name, scriptable, attributes);
        }

        // If the element is a radio button then set the value of the property to an array
        // of radio buttons with the specified name
        while( radioButtons.isEmpty() == false ) {
            final List collectedRadioButtons = new ArrayList(radioButtons.size());
            final Iterator radioButtonIterator = radioButtons.iterator();
            final HtmlRadioButtonInput firstRadioButton
                = (HtmlRadioButtonInput)radioButtonIterator.next();
            final String radioButtonName = firstRadioButton.getNameAttribute();

            Radio radioButton = (Radio)makeJavaScriptObject("Radio");
            radioButton.setHtmlElement(firstRadioButton);
            collectedRadioButtons.add(radioButton);
            radioButtonIterator.remove();

            while( radioButtonIterator.hasNext() ) {
                HtmlRadioButtonInput eachRadioButton
                    = (HtmlRadioButtonInput)radioButtonIterator.next();
                if( eachRadioButton.getNameAttribute().equals(radioButtonName) ) {
                    radioButton = (Radio)makeJavaScriptObject("Radio");
                    radioButton.setHtmlElement(eachRadioButton);

                    collectedRadioButtons.add(radioButton);
                    radioButtonIterator.remove();
                }
            }
            if( collectedRadioButtons.size() == 1 ) {
                defineProperty(radioButtonName, radioButton, attributes);
                jsElements_.defineProperty(radioButtonName, radioButton, attributes);
            }
            else {
                final Radio radioArray[] = new Radio[collectedRadioButtons.size()];
                defineProperty(radioButtonName, collectedRadioButtons.toArray(radioArray), attributes);
                jsElements_.defineProperty(radioButtonName, collectedRadioButtons.toArray(radioArray), attributes);
            }
        }
    }


    private String getClassNameForType( final String type ) {
        String className = "Input";
        if( type.equals("radio") ) {
            className = "Radio";
        }

        return className;
    }


    private static String getClassNameForFormElement( final HtmlElement htmlElement ) {
        final String tagName = htmlElement.getTagName();

        String type = null;
        if( tagName.equals("input") ) {
            type = htmlElement.getAttributeValue("type").toLowerCase();
            if( type.equals("file") ) {
                type = "FileUpload";
            }

            if( type.length() == 0 ) {
                type = "text";
            }
        }
        else if( tagName.equals("button") ) {
            type = htmlElement.getAttributeValue("type").toLowerCase();
            if( type.length() == 0 ) {
                type = "button";
            }
        }
        else if( tagName.equals("select") ) {
            type = "Select";
        }
        else if( tagName.equals("option") ) {
            type = "option";
        }
        else if( tagName.equals("textarea") ) {
            type = "textarea";
        }

        if( type == null ) {
            throw new IllegalStateException("Unexpected htmlElement ["+htmlElement+"]");
        }

        if( type.length() == 0 ) {
            throw new IllegalStateException("Empty type string for element ["+tagName+"]");
        }

        // Uppercase the first letter and return
        return type.substring(0,1).toUpperCase()+type.substring(1);
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
    public Scriptable jsGet_elements() {
        return jsElements_;
    }


    /**
     * Return the value of the javascript attribute "length".
     * @return The value of this attribute.
     */
    public long jsGet_length() {
        return jsElements_.jsGet_length();
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
         final SimpleScriptable property = (SimpleScriptable)overridingProperties_.get(name);
         if( property == null ) {
             return super.get(name, start);
         }
         else {
             return property;
         }
     }
}

