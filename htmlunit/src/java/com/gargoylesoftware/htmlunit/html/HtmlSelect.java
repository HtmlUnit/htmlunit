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
import com.gargoylesoftware.htmlunit.ScriptResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *  Wrapper for the html element "select"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 */
public class HtmlSelect
         extends HtmlElement
         implements SubmittableElement {

    private String[] fakeSelectedValues_;


    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element The xml element that corresponds to this html element
     */
    HtmlSelect( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Return a List containing all of the currently selected options
     *
     * @return  See above
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public List getSelectedOptions() {
        final List allOptions = getAllOptions();
        final List selectedOptions = new ArrayList( allOptions.size() );

        final Iterator iterator = allOptions.iterator();
        while( iterator.hasNext() ) {
            final HtmlOption option = ( HtmlOption )iterator.next();

            if( option.isSelected() ) {
                selectedOptions.add( option );
            }
        }

        return Collections.unmodifiableList( selectedOptions );
    }


    /**
     *  Return a List containing all the options
     *
     * @return  See above
     */
    public List getAllOptions() {

        final NodeList nodeList = getElement().getElementsByTagName( "option" );
        final int nodeCount = nodeList.getLength();
        final List allOptions = new ArrayList( nodeCount );
        final HtmlPage page = getPage();

        int i;
        for( i = 0; i < nodeCount; i++ ) {
            allOptions.add( page.getHtmlElement( ( Element )nodeList.item( i ) ) );
        }

        return Collections.unmodifiableList( allOptions );
    }


    /**
     *  Set the "selected" state of the specified option. If this "select" is
     *  single select then calling this will deselect all other options <p>
     *
     *  Only options that are actually in the document may be selected. If you
     *  need to select an option that really isn't there (ie testing error
     *  cases) then use {@link #fakeSelectedAttribute(String)} or {@link
     *  #fakeSelectedAttribute(String[])} instead.
     *
     * @param  isSelected true if the option is to become selected
     * @param  optionValue The value of the option that is to change
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     * @return  The page that occupies this window after this change is made.  It
     * may be the same window or it may be a freshly loaded one.
     */
    public Page setSelectedAttribute( final String optionValue, final boolean isSelected ) {

        try {
            getOptionByValue( optionValue );
        }
        catch( final ElementNotFoundException e ) {
            throw new IllegalArgumentException(
                "optionValue was not contained in the document: " + optionValue );
        }

        if( isMultipleSelectEnabled() ) {
            final List elements = getXmlElementsByAttribute( "option", "value", optionValue );
            final Iterator iterator = elements.iterator();
            while( iterator.hasNext() ) {
                setSelected( ( Element )iterator.next(), isSelected );
            }
        }
        else {
            final Iterator iterator = getAllOptions().iterator();
            while( iterator.hasNext() ) {
                final HtmlOption option = ( HtmlOption )iterator.next();
                setSelected(
                    option.getElement(),
                    option.getValueAttribute().equals( optionValue ) && isSelected );
            }
        }

        final HtmlPage page = getPage();
        final String onChange = getOnChangeAttribute();

        if( onChange.length() != 0 && page.getWebClient().isJavaScriptEnabled() ) {
            final ScriptResult scriptResult
                = page.executeJavaScriptIfPossible( onChange, "onChange handler", true, this );
            return scriptResult.getNewPage();
        }
        return page;
    }


    /**
     *  Set the selected value to be something that was not originally contained
     *  in the document.
     *
     * @param  optionValue The value of the new "selected" option
     */
    public void fakeSelectedAttribute( final String optionValue ) {
        assertNotNull( "optionValue", optionValue );
        fakeSelectedAttribute( new String[]{optionValue} );
    }


    /**
     *  Set the selected values to be something that were not originally
     *  contained in the document.
     *
     * @param  optionValues The values of the new "selected" options
     */
    public void fakeSelectedAttribute( final String optionValues[] ) {
        assertNotNull( "optionValues", optionValues );
        fakeSelectedValues_ = optionValues;
    }


    private void setSelected( final Element element, final boolean isSelected ) {
        if( isSelected ) {
            element.setAttribute( "selected", "selected" );
        }
        else {
            element.removeAttribute( "selected" );
        }
    }


    /**
     *  Return an array of KeyValuePairs that are the values that will be sent
     *  back to the server whenever the current form is submitted.<p>
     *
     *  THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     *  BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.
     *
     * @return  See above
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public KeyValuePair[] getSubmitKeyValuePairs() {
        final String name = getNameAttribute();
        final KeyValuePair[] pairs;

        if( fakeSelectedValues_ == null ) {
            final List selectedOptions = getSelectedOptions();
            final int optionCount = selectedOptions.size();

            pairs = new KeyValuePair[optionCount];

            for( int i = 0; i < optionCount; i++ ) {
                final HtmlOption option = ( HtmlOption )selectedOptions.get( i );
                pairs[i] = new KeyValuePair( name, option.getValueAttribute() );
            }
        }
        else {
            pairs = new KeyValuePair[fakeSelectedValues_.length];
            for( int i = 0; i < pairs.length; i++ ) {
                pairs[i] = new KeyValuePair( name, fakeSelectedValues_[i] );
            }
        }
        return pairs;
    }


    /**
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        final Iterator iterator = getAllOptions().iterator();
        while( iterator.hasNext() ) {
            final HtmlOption option = (HtmlOption)iterator.next();
            option.reset();
        }
    }


    /**
     *  Return true if this select is using "multiple select"
     *
     * @return  See above
     */
    public boolean isMultipleSelectEnabled() {
        return getAttributeValue( "multiple" ) != ATTRIBUTE_NOT_DEFINED;
    }


    /**
     *  Return the HtmlOption object that corresponds to the specified value
     *
     * @param  value The value to search by
     * @return  See above
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public HtmlOption getOptionByValue( final String value )
        throws ElementNotFoundException {
        assertNotNull("value", value);

        return ( HtmlOption )getOneHtmlElementByAttribute( "option", "value", value );
    }


    /**
     *  Return a text representation of this element that represents what would
     *  be visible to the user if this page was shown in a web browser. For
     *  example, a select element would return the currently selected value as
     *  text
     *
     * @return  The element as text
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public String asText() {

        final List options;
        if( isMultipleSelectEnabled() ) {
            options = getAllOptions();
        }
        else {
            options = getSelectedOptions();
        }

        boolean isFirstTimeThrough = true;
        final StringBuffer buffer = new StringBuffer();

        final Iterator iterator = options.iterator();
        while( iterator.hasNext() ) {
            if( isFirstTimeThrough == true ) {
                isFirstTimeThrough = false;
            }
            else {
                buffer.append( "\n" );
            }

            final HtmlOption currentOption = ( HtmlOption )iterator.next();
            buffer.append( currentOption.asText() );
        }

        return buffer.toString();
    }


    /**
     *  Return the value of the attribute "id". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "id" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getIdAttribute() {
        return getAttributeValue( "id" );
    }


    /**
     *  Return the value of the attribute "class". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "class" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getClassAttribute() {
        return getAttributeValue( "class" );
    }


    /**
     *  Return the value of the attribute "style". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "style" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getStyleAttribute() {
        return getAttributeValue( "style" );
    }


    /**
     *  Return the value of the attribute "title". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "title" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getTitleAttribute() {
        return getAttributeValue( "title" );
    }


    /**
     *  Return the value of the attribute "lang". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "lang" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getLangAttribute() {
        return getAttributeValue( "lang" );
    }


    /**
     *  Return the value of the attribute "xml:lang". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "xml:lang" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue( "xml:lang" );
    }


    /**
     *  Return the value of the attribute "dir". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "dir" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue( "dir" );
    }


    /**
     *  Return the value of the attribute "onclick". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onclick" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnClickAttribute() {
        return getAttributeValue( "onclick" );
    }


    /**
     *  Return the value of the attribute "ondblclick". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "ondblclick" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnDblClickAttribute() {
        return getAttributeValue( "ondblclick" );
    }


    /**
     *  Return the value of the attribute "onmousedown". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onmousedown" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnMouseDownAttribute() {
        return getAttributeValue( "onmousedown" );
    }


    /**
     *  Return the value of the attribute "onmouseup". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onmouseup" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnMouseUpAttribute() {
        return getAttributeValue( "onmouseup" );
    }


    /**
     *  Return the value of the attribute "onmouseover". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onmouseover" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnMouseOverAttribute() {
        return getAttributeValue( "onmouseover" );
    }


    /**
     *  Return the value of the attribute "onmousemove". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onmousemove" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnMouseMoveAttribute() {
        return getAttributeValue( "onmousemove" );
    }


    /**
     *  Return the value of the attribute "onmouseout". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onmouseout" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnMouseOutAttribute() {
        return getAttributeValue( "onmouseout" );
    }


    /**
     *  Return the value of the attribute "onkeypress". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onkeypress" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnKeyPressAttribute() {
        return getAttributeValue( "onkeypress" );
    }


    /**
     *  Return the value of the attribute "onkeydown". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onkeydown" or an empty string if
     *      that attribute isn't defined.
     */
    public final String getOnKeyDownAttribute() {
        return getAttributeValue( "onkeydown" );
    }


    /**
     *  Return the value of the attribute "onkeyup". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onkeyup" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnKeyUpAttribute() {
        return getAttributeValue( "onkeyup" );
    }


    /**
     *  Return the value of the attribute "name". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "name" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue( "name" );
    }


    /**
     *  Return the value of the attribute "size". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "size" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getSizeAttribute() {
        return getAttributeValue( "size" );
    }


    /**
     *  Return the value of the attribute "multiple". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "multiple" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getMultipleAttribute() {
        return getAttributeValue( "multiple" );
    }


    /**
     *  Return the value of the attribute "disabled". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "disabled" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getDisabledAttribute() {
        return getAttributeValue( "disabled" );
    }


    /**
     *  Return the value of the attribute "tabindex". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "tabindex" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue( "tabindex" );
    }


    /**
     *  Return the value of the attribute "onfocus". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onfocus" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue( "onfocus" );
    }


    /**
     *  Return the value of the attribute "onblur". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onblur" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue( "onblur" );
    }


    /**
     *  Return the value of the attribute "onchange". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onchange" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnChangeAttribute() {
        return getAttributeValue( "onchange" );
    }
}

