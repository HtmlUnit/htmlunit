/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "form"
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlForm extends HtmlElement {

    private static final Collection SUBMITTABLE_ELEMENT_NAMES
             = Arrays.asList( new String[]{"input", "button", "select", "textarea", "isindex"} );

    private KeyValuePair fakeSelectedRadioButton_ = null;


    /**
     *  Create an instance
     *
     * @param  htmlPage The page that contains this element
     * @param  element The xml element that represents this form in the DOM
     * @throws  IOException If an io error occurs
     */
    HtmlForm( final HtmlPage htmlPage, final Element element )
        throws IOException {
        super( htmlPage, element );
    }


    /**
     *  Submit this form to the appropriate server as if a submit button had
     *  been pressed
     *
     * @param  buttonName The name of a submit input element or a button element
     *      which will be sent back up with the response
     * @return  A new Page that reflects the results of this submission
     * @exception  IOException If an IO error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page submit( final String buttonName )
        throws IOException, ElementNotFoundException {

        HtmlElement htmlElement;
        try {
            htmlElement = getOneHtmlElementByAttribute( "input", "name", buttonName );
        }
        catch( final ElementNotFoundException e ) {
            htmlElement = getOneHtmlElementByAttribute( "button", "name", buttonName );
        }
        return submit( ( SubmittableElement )htmlElement );
    }


    /**
     *  Submit this form to the appropriate server as if it had been submitted
     *  by javascript - ie no submit buttons were pressed.
     *
     * @return  A new Page that reflects the results of this submission
     * @exception  IOException If an IO error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page submit()
        throws IOException, ElementNotFoundException {
        return submit( ( SubmittableElement )null );
    }


    /**
     *  Submit this form to the appropriate server
     *
     * @param  submitElement The element that caused the submit to occur
     * @return  A new Page that reflects the results of this submission
     * @exception  IOException If an IO error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    Page submit( final SubmittableElement submitElement )
        throws
            IOException,
            ElementNotFoundException {

        final String action = getActionAttribute();
        final HtmlPage htmlPage = getPage();
        if( htmlPage.getWebClient().isJavaScriptEnabled() ) {
            final String onSubmit = getOnSubmitAttribute();
            if( onSubmit.length() != 0 ) {
                final ScriptResult scriptResult
                    = htmlPage.executeJavaScriptIfPossible( onSubmit, "onSubmit", true );
                if( scriptResult.getJavaScriptResult().equals(Boolean.FALSE) ) {
                    return scriptResult.getNewPage();
                }
            }

            if( action.toLowerCase().startsWith("javascript:") ) {
                return htmlPage.executeJavascriptIfPossible( action, "Form action" );
            }
        }
        else {
            if( action.toLowerCase().startsWith("javascript:") ) {
                // The action is javascript but javascript isn't enabled.  Return
                // the current page.
                return htmlPage;
            }
        }

        final Collection submittableElements = getAllSubmittableElements();

        final List parameterList = new ArrayList( submittableElements.size() );
        final Iterator iterator = submittableElements.iterator();
        while( iterator.hasNext() ) {
            final SubmittableElement element = ( SubmittableElement )iterator.next();
            final KeyValuePair[] pairs = element.getSubmitKeyValuePairs();

            for( int i = 0; i < pairs.length; i++ ) {
                parameterList.add( pairs[i] );
            }
        }
        if( submitElement != null ) {
            final KeyValuePair[] pairs = submitElement.getSubmitKeyValuePairs();
            for( int i = 0; i < pairs.length; i++ ) {
                parameterList.add( pairs[i] );
            }
        }

        if( fakeSelectedRadioButton_ != null ) {
            adjustParameterListToAccountForFakeSelectedRadioButton( parameterList );
        }

        final URL url;
        try {
            url = htmlPage.getFullyQualifiedUrl( action );
        }
        catch( final MalformedURLException e ) {
            throw new IllegalArgumentException( "Not a valid url: " + action );
        }

        final SubmitMethod method = SubmitMethod.getInstance( getAttributeValue( "method" ) );
        return getPage().getWebClient().getPage( url, method, parameterList );
    }


    /**
     * Reset this form to its initial values.
     * @return The page that is loaded at the end of calling this method.  Typically this
     * will be the same page that had been loaded previously but since javascript might
     * have run, this isn't guarenteed.
     */
    public Page reset() {
        final HtmlPage htmlPage = getPage();
        if( htmlPage.getWebClient().isJavaScriptEnabled() ) {
            final String onReset = getOnResetAttribute();
            if( onReset.length() != 0 ) {
                final ScriptResult scriptResult
                    = htmlPage.executeJavaScriptIfPossible( onReset, "onReset", true );
                if( scriptResult.getJavaScriptResult().equals(Boolean.FALSE) ) {
                    return scriptResult.getNewPage();
                }
            }
        }

        final Iterator elementIterator = getAllHtmlChildElements();
        while( elementIterator.hasNext() ) {
            final Object next = elementIterator.next();
            if( next instanceof SubmittableElement ) {
                ((SubmittableElement)next).reset();
            }
        }

        return htmlPage;
    }


    /**
     *  Return a collection of elements that represent all the "submittable"
     *  elements in this form
     *
     * @return  See above
     */
    public Collection getAllSubmittableElements() {

        final List submittableElements = new ArrayList();
        final HtmlPage page = getPage();

        final Iterator iterator = getXmlChildElements();
        while( iterator.hasNext() ) {
            final Element element = ( Element )iterator.next();
            if( isSubmittable( element ) ) {
                submittableElements.add( page.getHtmlElement( element ) );
            }
        }

        return submittableElements;
    }


    private boolean isSubmittable( final Element element ) {
        final String tagName = getTagName(element);
        if( SUBMITTABLE_ELEMENT_NAMES.contains( tagName.toLowerCase() ) == false ) {
            return false;
        }

        final Attr disabled = (Attr)getElement().getAttributeNode("disabled");
        if( disabled != null ) {
            return false;
        }

        if( tagName.equals( "input" ) ) {
            final String type = getAttributeValue(element, "type" );
            if( type.equals( "radio" ) || type.equals( "checkbox" ) ) {
                final Attr checked = (Attr)element.getAttributeNode("checked");
                return checked != null;
            }
            if( type.equals("submit") || type.equals("image") ){
                // The one submit button that was clicked can be submitted but no other ones
                return false;
            }
        }

        return true;
    }


    /**
     *  Return the input tags that have the specified name
     *
     * @param  name The name of the input
     * @return  A list of HtmlInputs
     */
    public List getAllInputsByName( final String name ) {
        return getHtmlElementsByAttribute( "input", "name", name );
    }


    /**
     *  Return the first input with the specified name
     *
     * @param  name The name of the input
     * @return  The input
     */
    public final HtmlInput getInputByName( final String name ) {
        final List inputs = getHtmlElementsByAttribute( "input", "name", name );
        if( inputs.size() == 0 ) {
            throw new ElementNotFoundException( "input", "name", name );
        }
        else {
            return ( HtmlInput )inputs.get( 0 );
        }
    }


    /**
     *  Return the "radio" type input fields that match the specified name and
     *  value
     *
     * @param  name The name of the HtmlInput
     * @param  value The value of the HtmlInput
     * @return  See above
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public HtmlRadioButtonInput getRadioButtonInput( final String name, final String value )
        throws
            ElementNotFoundException {

        final Iterator iterator = getRadioButtonsByName( name ).iterator();
        while( iterator.hasNext() ) {
            final HtmlRadioButtonInput input = ( HtmlRadioButtonInput )iterator.next();
            if( input.getValueAttribute().equals( value ) ) {
                return input;
            }
        }

        throw new ElementNotFoundException( "input", "value", value );
    }


    /**
     *  Return all the HtmlSelect that match the specified name
     *
     * @param  name The name
     * @return  See above
     */
    public List getSelectsByName( final String name ) {
        return getHtmlElementsByAttribute( "select", "name", name );
    }


    /**
     * Find the first select element with the specified name
     * @param name The name of the select element
     * @return The first select.
     */
    public HtmlSelect getSelectByName( final String name ) {
        final List list = getSelectsByName( name );
        if( list.isEmpty() ) {
            throw new ElementNotFoundException( "select", "name", name );
        }
        else {
            return ( HtmlSelect )list.get( 0 );
        }
    }


    /**
     *  Return all the HtmlButtons that match the specified name
     *
     * @param  name The name
     * @return  See above
     * @exception  ElementNotFoundException If no matching buttons were found
     */
    public List getButtonsByName( final String name )
        throws ElementNotFoundException {
        return getHtmlElementsByAttribute( "button", "name", name );
    }


    /**
     *  Return all the HtmlTextAreas that match the specified name
     *
     * @param  name The name
     * @return  See above
     */
    public List getTextAreasByName( final String name ) {
        return getHtmlElementsByAttribute( "textarea", "name", name );
    }


    /**
     *  Return a list of HtmlInputs that are of type radio and match the
     *  specified name
     *
     * @param  name The name
     * @return  See above
     */
    public List getRadioButtonsByName( final String name ) {

        assertNotNull( "name", name );

        final List results = new ArrayList();
        final HtmlPage page = getPage();

        final Iterator iterator = getXmlChildElements();
        while( iterator.hasNext() ) {
            final Element element = ( Element )iterator.next();
            if( getTagName(element).equals( "input" )
                     && getAttributeValue(element, "type").equals( "radio" )
                     && getAttributeValue(element, "name").equals( name ) ) {
                results.add( page.getHtmlElement( element ) );
            }
        }

        return results;
    }


    /**
     *  Select the specified radio button in the form. <p />
     *
     *  Only a radio button that is actually contained in the form can be
     *  selected. If you need to be able to select a button that really isn't
     *  there (ie during testing of error cases) then use {@link
     *  #fakeCheckedRadioButton(String,String)} instead
     *
     * @param  name The name of the radio buttons
     * @param  value The value to match
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public void setCheckedRadioButton(
            final String name,
            final String value )
        throws
            ElementNotFoundException {

        final HtmlInput inputToSelect = getRadioButtonInput( name, value );

        final Iterator iterator = getRadioButtonsByName( name ).iterator();
        if( iterator.hasNext() == false ) {
            throw new ElementNotFoundException("input", name, value);
        }

        while( iterator.hasNext() ) {
            final HtmlInput input = ( HtmlInput )iterator.next();

            if( input == inputToSelect ) {
                input.getElement().setAttribute("checked", "checked");
            }
            else {
                input.getElement().removeAttribute("checked");
            }
        }
    }


    /**
     *  Set the "selected radio buttion" to a value that doesn't actually exist
     *  in the page. This is useful primarily for testing error cases.
     *
     * @param  name The name of the radio buttons
     * @param  value The value to match
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public final void fakeCheckedRadioButton(
            final String name,
            final String value )
        throws
            ElementNotFoundException {

        fakeSelectedRadioButton_ = new KeyValuePair( name, value );
    }


    private void adjustParameterListToAccountForFakeSelectedRadioButton( final List list ) {
        final String fakeRadioButtonName = fakeSelectedRadioButton_.getKey();

        // Remove any pairs that match the name of the radio button
        final Iterator iterator = list.iterator();
        while( iterator.hasNext() ) {
            final KeyValuePair pair = ( KeyValuePair )iterator.next();
            if( pair.getKey().equals( fakeRadioButtonName ) ) {
                iterator.remove();
            }
        }

        // Now add this one back in
        list.add( fakeSelectedRadioButton_ );
    }


    /**
     * Return the first checked radio button with the specified name.  If none of
     * the radio buttons by that name are checked then return null.
     */
    public HtmlRadioButtonInput getCheckedRadioButton( final String name ) {
        assertNotNull("name", name);
        final Iterator iterator = getRadioButtonsByName(name).iterator();
        while( iterator.hasNext() ) {
            final HtmlRadioButtonInput input = (HtmlRadioButtonInput)iterator.next();
            if( input.isChecked() ) {
                return input;
            }
        }
        return null;
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
     *  Return the value of the attribute "action". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "action" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getActionAttribute() {
        return getAttributeValue( "action" );
    }


    /**
     *  Set the value of the attribute "action". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @param action  The value of the attribute "action"
     */
    public final void setActionAttribute( final String action ) {
        getElement().setAttribute( "action", action );
    }


    /**
     *  Return the value of the attribute "method". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "method" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getMethodAttribute() {
        return getAttributeValue( "method" );
    }


    /**
     *  Set the value of the attribute "method". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @param method  The value of the attribute "method" or an empty string if that
     *      attribute isn't defined.
     */
    public final void setMethodAttribute( final String method ) {
        assertNotNull("method", method);
        getElement().setAttribute( "method", method );
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
     *  Return the value of the attribute "enctype". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.<p>
     *
     *  Enctype is the encoding type used when submitting a form back to the server
     *
     * @return  The value of the attribute "enctype" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getEnctypeAttribute() {
        return getAttributeValue( "enctype" );
    }


    /**
     *  Set the value of the attribute "enctype". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.<p>
     *
     *  Enctype is the encoding type used when submitting a form back to the server
     *
     * @param encoding The value of the attribute "enctype" or an empty string if that
     *      attribute isn't defined.
     */
    public final void setEnctypeAttribute( final String encoding ) {
        assertNotNull("encoding", encoding);
        getElement().setAttribute( "enctype", encoding );
    }


    /**
     *  Return the value of the attribute "onsubmit". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onsubmit" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnSubmitAttribute() {
        return getAttributeValue( "onsubmit" );
    }


    /**
     *  Return the value of the attribute "onreset". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "onreset" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getOnResetAttribute() {
        return getAttributeValue( "onreset" );
    }


    /**
     *  Return the value of the attribute "accept". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "accept" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getAcceptAttribute() {
        return getAttributeValue( "accept" );
    }


    /**
     *  Return the value of the attribute "accept-charset". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "accept-charset" or an empty string
     *      if that attribute isn't defined.
     */
    public final String getAcceptCharsetAttribute() {
        return getAttributeValue( "accept-charset" );
    }


    /**
     *  Return the value of the attribute "target". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "target" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getTargetAttribute() {
        return getAttributeValue( "target" );
    }


    /**
     *  Set the value of the attribute "target". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @param target  The value of the attribute "target" or an empty string if that
     *      attribute isn't defined.
     */
    public final void setTargetAttribute( final String target ) {
        assertNotNull("target", target);
        getElement().setAttribute( "target", target );
    }
}

