/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 *  Wrapper for the html element "form"
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author  Brad Clarke
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 */
public class HtmlForm extends ClickableElement {

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "form";

    private static final Collection SUBMITTABLE_ELEMENT_NAMES
             = Arrays.asList( new String[]{"input", "button", "select", "textarea", "isindex"} );

    private KeyValuePair fakeSelectedRadioButton_ = null;

    /**
     *  Create an instance
     *
     * @param  htmlPage The page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlForm( final HtmlPage htmlPage, final Map attributes) {
        super(htmlPage, attributes);
    }

    /**
     * @return the HTML tag name
     */
    public String getTagName() {
        return TAG_NAME;
    }

    /**
     *  Submit this form to the appropriate server as if a submit button had
     *  been pressed
     *
     * @param  buttonName The name of a submit input element or a button element
     *      which will be sent back up with the response
     * @return  A new Page that reflects the results of this submission
     * @throws IOException If an IO error occurs
     * @throws ElementNotFoundException If a button with the specified name cannot be found.
     */
    public Page submit( final String buttonName ) throws IOException, ElementNotFoundException {

        final List inputList = getHtmlElementsByAttribute("input", "name", buttonName);
        final Iterator iterator = inputList.iterator();
        while( iterator.hasNext() ) {
            final HtmlInput input = (HtmlInput)iterator.next();
            if( input.getTypeAttribute().equals("submit")) {
                return submit( input );
            }
        }

        final HtmlButton button = (HtmlButton)getOneHtmlElementByAttribute( "button", "name", buttonName );
        return submit( button );
    }


    /**
     *  Submit this form to the appropriate server as if it had been submitted
     *  by javascript - ie no submit buttons were pressed.  Note that because we
     *  are simulating a javascript submit, the onsubmit handler will not get
     *  executed.
     *
     * @return  A new Page that reflects the results of this submission
     * @exception  IOException If an IO error occurs
     */
    public Page submit() throws IOException {
        return submit( ( SubmittableElement )null );
    }


    /**
     *  Submit this form to the appropriate server.  If submitElement is null then
     *  treat this as if it was called by javascript.  In this case, the onsubmit
     *  handler will not get executed.
     *
     * @param  submitElement The element that caused the submit to occur
     * @return  A new Page that reflects the results of this submission
     * @exception  IOException If an IO error occurs
     */
    Page submit( final SubmittableElement submitElement ) throws IOException {

        final String action = getActionAttribute();
        final HtmlPage htmlPage = getPage();
        if( submitElement != null ) {
            if( htmlPage.getWebClient().isJavaScriptEnabled() ) {
                final String onSubmit = getOnSubmitAttribute();
                if( onSubmit.length() != 0 ) {
                    final ScriptResult scriptResult
                        = htmlPage.executeJavaScriptIfPossible( onSubmit, "onSubmit", true, this );
                    if( scriptResult.getJavaScriptResult().equals(Boolean.FALSE) ) {
                        return scriptResult.getNewPage();
                    }
                }
    
                if( TextUtil.startsWithIgnoreCase(action, "javascript:") ) {
                    return htmlPage.executeJavaScriptIfPossible( action, "Form action", false, this ).getNewPage();
                }
            }
            else {
                if( TextUtil.startsWithIgnoreCase(action, "javascript:") ) {
                    // The action is javascript but javascript isn't enabled.  Return
                    // the current page.
                    return htmlPage;
                }
            }
        }

        final URL url;
        try {
            url = htmlPage.getFullyQualifiedUrl( action );
        }
        catch( final MalformedURLException e ) {
            throw new IllegalArgumentException( "Not a valid url: " + action );
        }
        final WebRequestSettings params = new WebRequestSettings(url)
            .setRequestParameters(getParameterListForSubmit(submitElement))
            .setEncodingType(FormEncodingType.getInstance( getEnctypeAttribute() ))
            .setSubmitMethod(SubmitMethod.getInstance( getAttributeValue( "method" ) ));

        final WebWindow webWindow = htmlPage.getEnclosingWindow();
        return htmlPage.getWebClient().getPage(
                webWindow,
                htmlPage.getResolvedTarget(getTargetAttribute()),
                params);
    }


    /**
     * Return a list of {@link KeyValuePair}s that represent the data that will be
     * sent to the server on a form submit.  This is primarily intended to aid
     * debugging.
     *
     * @param submitElement The element that would have been pressed to submit the
     * form or null if the form was submitted by javascript.
     * @return The list of {@link KeyValuePair}s.
     */
    public final List getParameterListForSubmit( final SubmittableElement submitElement ) {
        final Collection submittableElements = getAllSubmittableElements(submitElement);

        final List parameterList = new ArrayList( submittableElements.size() );
        final Iterator iterator = submittableElements.iterator();
        while( iterator.hasNext() ) {
            final SubmittableElement element = ( SubmittableElement )iterator.next();
            final KeyValuePair[] pairs = element.getSubmitKeyValuePairs();

            for( int i = 0; i < pairs.length; i++ ) {
                parameterList.add( pairs[i] );
            }
        }

        if( fakeSelectedRadioButton_ != null ) {
            adjustParameterListToAccountForFakeSelectedRadioButton( parameterList );
        }
        return parameterList;
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
                    = htmlPage.executeJavaScriptIfPossible( onReset, "onReset", true, this );
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
     * @param submitElement The element that would have been pressed to submit the
     * form or null if the form was submitted by javascript.
     * @return  See above
     */
    public Collection getAllSubmittableElements(final SubmittableElement submitElement) {

        final List submittableElements = new ArrayList();

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = ( HtmlElement )iterator.next();
            if( isSubmittable(element, submitElement) ) {
                submittableElements.add(element);
            }
        }

        return submittableElements;
    }

    private boolean isValidForSubmission(final HtmlElement element, final SubmittableElement submitElement){
        final String tagName = element.getTagName();
        if (!SUBMITTABLE_ELEMENT_NAMES.contains(tagName.toLowerCase())) {
            return false;
        }        
        if(element.isAttributeDefined("disabled")) {
            return false;
        }
        // clicked input type="image" is submittted even if it hasn't a name
        if (element == submitElement && element instanceof HtmlImageInput) {
            return true;
        }

        if (!tagName.equals("isindex") && !element.isAttributeDefined("name")){
            return false;
        }
        
        if( ! tagName.equals( "isindex" ) && element.getAttributeValue("name").equals("") ) {
            return false;
        }        
        
        if( tagName.equals( "input" ) ) {
            final String type = element.getAttributeValue("type").toLowerCase();
            if( type.equals( "radio" ) || type.equals( "checkbox" ) ) {
                return element.isAttributeDefined("checked");
            }
        }
        if ( tagName.equals("select") ) {
            //an empty select list has no value, and is therefore not submittable
            if (((HtmlSelect) element).getOptionSize() < 1) {
                return false;
            }
        }          
        return true;
    }

    /**
     * 
     * @param submitElement The element that would have been pressed to submit the
     * form or null if the form was submitted by javascript.
     */
    private boolean isSubmittable(final HtmlElement element, final SubmittableElement submitElement) {
        final String tagName = element.getTagName();
        if (!isValidForSubmission(element, submitElement)){
            return false;
        }

        // The one submit button that was clicked can be submitted but no other ones
        if (element == submitElement) {
            return true;
        }
        if( tagName.equals( "input" ) ) {
            final HtmlInput input = (HtmlInput)element;
            final String type = input.getTypeAttribute().toLowerCase();
            if( type.equals("submit") || type.equals("image") ){
                return false;
            }
        }
        if ( tagName.equals("button") ) {
            final HtmlButton button = (HtmlButton)element;
            final String type = button.getTypeAttribute().toLowerCase();
            if( type.equals("submit") ){
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
     * @throws ElementNotFoundException If no inputs could be found with the specified name.
     */
    public final HtmlInput getInputByName( final String name ) throws ElementNotFoundException {
        final List inputs = getHtmlElementsByAttribute( "input", "name", name );
        if( inputs.size() == 0 ) {
            throw new ElementNotFoundException( "input", "name", name );
        }
        else {
            return ( HtmlInput )inputs.get( 0 );
        }
    }


    /**
     *  Return the "radio" type input field that matches the specified name and value
     *
     * @param  name The name of the HtmlInput
     * @param  value The value of the HtmlInput
     * @return  See above
     * @exception  ElementNotFoundException If the field could not be found
     */
    public HtmlRadioButtonInput getRadioButtonInput( final String name, final String value )
        throws
            ElementNotFoundException {

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = iterator.nextElement();

            if( element instanceof HtmlRadioButtonInput
                    && element.getAttributeValue("name").equals( name ) ) {

                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)element;
                if( input.getValueAttribute().equals( value ) ) {
                    return input;
                }
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
     * @throws ElementNotFoundException If the select cannot be found.
     */
    public HtmlSelect getSelectByName( final String name ) throws ElementNotFoundException {
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

        Assert.notNull( "name", name );

        final List results = new ArrayList();

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = ( HtmlElement )iterator.next();
            if( element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals( name ) ) {
                results.add(element);
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
     * @exception  ElementNotFoundException If the specified element could not be found
     */
    public void setCheckedRadioButton(
            final String name,
            final String value )
        throws
            ElementNotFoundException {

        //we could do this with one iterator, but that would set the state of the other
        //radios also in the case where the specified one is not found
        final HtmlInput inputToSelect = getRadioButtonInput( name, value );

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = iterator.nextElement();
            if( element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals( name ) ) {

                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)element;
                if( input == inputToSelect ) {
                    input.setAttributeValue("checked", "checked");
                }
                else {
                    input.removeAttribute("checked");
                }
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
     *
     * @param name The name of the radio button
     * @return The first checked radio button.
     */
    public HtmlRadioButtonInput getCheckedRadioButton( final String name ) {

        Assert.notNull("name", name);

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = iterator.nextElement();
            if( element instanceof HtmlRadioButtonInput
                     && element.getAttributeValue("name").equals( name ) ) {

                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)element;
                if( input.isChecked() ) {
                    return input;
                }
            }
        }
        return null;
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
        setAttributeValue( "action", action );
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
        setAttributeValue( "method", method );
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
        setAttributeValue( "enctype", encoding );
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
        setAttributeValue( "target", target );
    }

    /**
     * Return the first input with the specified value.
     * @param value The value
     * @return The first input with the specified value.
     * @throws ElementNotFoundException If no elements can be found with the specified value.
     */
    public HtmlInput getInputByValue( final String value ) throws ElementNotFoundException {
        return (HtmlInput)getOneHtmlElementByAttribute("input", "value", value);
    }

    /**
     * Return all the inputs with the specified value.
     * @param value The value
     * @return all the inputs with the specified value.
     */
    public List getInputsByValue( final String value ) {
        return getHtmlElementsByAttribute("input", "value", value);
    }
}
