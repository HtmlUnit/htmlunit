/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import java.io.IOException;
import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "button"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlButton
         extends HtmlElement
         implements SubmittableElement {

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element The xml element that corresponds to this html element
     */
    HtmlButton( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Set the content of the "value" attribute
     *
     * @param  newValue The new content
     */
    public void setValueAttribute( final String newValue ) {
        assertNotNull( "newValue", newValue );
        getElement().setAttribute( "value", newValue );
    }


    /**
     *  Submit the form.
     *
     * @deprecated Use {@link #click()} instead
     * @return  The Page that wraps the response from the server
     * @exception  IllegalStateException If the type of this input isn't
     *      "submit"
     * @exception  IOException If an IO problem occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page submit()
        throws
            IllegalStateException,
            IOException,
            ElementNotFoundException {

        return click();
    }


    /**
     *  Submit the form.
     *
     * @return  The Page that wraps the response from the server
     * @exception  IllegalStateException If the type of this input isn't
     *      "submit"
     * @exception  IOException If an IO problem occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page click()
        throws
            IllegalStateException,
            IOException {

        if( isDisabled() ) {
            return getPage();
        }

        final String onClick = getOnClickAttribute();
        final HtmlPage page = getPage();
        if( onClick.length() == 0 || page.getWebClient().isJavaScriptEnabled() == false ) {
            return getEnclosingFormOrDie().submit();
        }
        else {
            return page.executeJavaScriptIfPossible(onClick, "HtmlButton onClick handler", true, this).getNewPage();
        }
    }


    /**
     * Return true if the disabled attribute is set for this element.
     * @return Return true if this is disabled.
     */
    public final boolean isDisabled() {
        return isAttributeDefined("disabled");
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
        return new KeyValuePair[]{new KeyValuePair( getNameAttribute(), getValueAttribute() )};
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
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        getLog().debug("reset() not implemented for this element");
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
     * Return the value of the attribute "onclick".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onclick"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnClickAttribute() {
        return getAttributeValue("onclick");
    }


    /**
     * Return the value of the attribute "ondblclick".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "ondblclick"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnDblClickAttribute() {
        return getAttributeValue("ondblclick");
    }


    /**
     * Return the value of the attribute "onmousedown".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmousedown"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseDownAttribute() {
        return getAttributeValue("onmousedown");
    }


    /**
     * Return the value of the attribute "onmouseup".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseup"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseUpAttribute() {
        return getAttributeValue("onmouseup");
    }


    /**
     * Return the value of the attribute "onmouseover".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseover"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseOverAttribute() {
        return getAttributeValue("onmouseover");
    }


    /**
     * Return the value of the attribute "onmousemove".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmousemove"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseMoveAttribute() {
        return getAttributeValue("onmousemove");
    }


    /**
     * Return the value of the attribute "onmouseout".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseout"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseOutAttribute() {
        return getAttributeValue("onmouseout");
    }


    /**
     * Return the value of the attribute "onkeypress".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeypress"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyPressAttribute() {
        return getAttributeValue("onkeypress");
    }


    /**
     * Return the value of the attribute "onkeydown".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeydown"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyDownAttribute() {
        return getAttributeValue("onkeydown");
    }


    /**
     * Return the value of the attribute "onkeyup".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeyup"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyUpAttribute() {
        return getAttributeValue("onkeyup");
    }


    /**
     * Return the value of the attribute "name".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }


    /**
     * Return the value of the attribute "value".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "value"
     * or an empty string if that attribute isn't defined.
     */
    public final String getValueAttribute() {
        return getAttributeValue("value");
    }


    /**
     * Return the value of the attribute "type".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "type"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeValue("type");
    }


    /**
     * Return the value of the attribute "disabled".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "disabled"
     * or an empty string if that attribute isn't defined.
     */
    public final String getDisabledAttribute() {
        return getAttributeValue("disabled");
    }


    /**
     * Return the value of the attribute "tabindex".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "tabindex"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }


    /**
     * Return the value of the attribute "accesskey".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "accesskey"
     * or an empty string if that attribute isn't defined.
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }


    /**
     * Return the value of the attribute "onfocus".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onfocus"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }


    /**
     * Return the value of the attribute "onblur".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onblur"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }
}
