/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.KeyValuePair;
import org.w3c.dom.Element;

/**
 * Wrapper for the html element "isindex".  Note that this element has been
 * deprecated in the HTML spec.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlIsIndex
    extends HtmlElement
    implements SubmittableElement {

    private String value_ = "";

    /**
     * Create an instance of HtmlIsIndex
     *
     * @param page The HtmlPage that contains this element.
     * @param xmlElement The actual html element that we are wrapping.
     */
    HtmlIsIndex( final HtmlPage page, final Element xmlElement ) {
        super(page, xmlElement);
    }


    /**
     * Set the value that will be returned during submission of a form.
     *
     * @param newValue The value.
     */
    public void setValue( final String newValue ) {
        assertNotNull("newValue", newValue);
        value_ = newValue;
    }


    /**
     * Return the value that would be send during submission of a form.
     *
     * @return The value
     */
    public String getValue() {
        return value_;
    }


    /**
     *  Return an array of KeyValuePairs that are the values that will be sent
     *  back to the server whenever the current form is submitted.<p>
     *
     *  THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     *  BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.
     *
     * @return  See above
     */
    public KeyValuePair[] getSubmitKeyValuePairs() {
        return new KeyValuePair[]{new KeyValuePair( getPromptAttribute(), getValue() )};
    }


    /**
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        value_ = "";
    }


    /**
     * Return the value of the attribute "id".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "id"
     * or an empty string if that attribute isn't defined.
     */
    public final String getIdAttribute() {
        return getAttributeValue("id");
    }


    /**
     * Return the value of the attribute "class".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "class"
     * or an empty string if that attribute isn't defined.
     */
    public final String getClassAttribute() {
        return getAttributeValue("class");
    }


    /**
     * Return the value of the attribute "style".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "style"
     * or an empty string if that attribute isn't defined.
     */
    public final String getStyleAttribute() {
        return getAttributeValue("style");
    }


    /**
     * Return the value of the attribute "title".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "title"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTitleAttribute() {
        return getAttributeValue("title");
    }


    /**
     * Return the value of the attribute "lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLangAttribute() {
        return getAttributeValue("lang");
    }


    /**
     * Return the value of the attribute "xml:lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "xml:lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue("xml:lang");
    }


    /**
     * Return the value of the attribute "dir".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "dir"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue("dir");
    }


    /**
     * Return the value of the attribute "prompt".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "prompt"
     * or an empty string if that attribute isn't defined.
     */
    public final String getPromptAttribute() {
        return getAttributeValue("prompt");
    }
}
