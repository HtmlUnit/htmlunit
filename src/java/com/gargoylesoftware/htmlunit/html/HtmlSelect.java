/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;
import com.gargoylesoftware.htmlunit.Page;

/**
 * Wrapper for the HTML element "select".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlSelect extends FocusableElement implements DisabledElement, SubmittableElement {

    private static final long serialVersionUID = 7893240015923163203L;

    /** the HTML tag represented by this element */
    public static final String TAG_NAME = "select";

    private String[] fakeSelectedValues_;

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     * @deprecated You should not directly construct HtmlSelect.
     */
    //TODO: to be removed, deprecated in 23 June 2007
    public HtmlSelect(final HtmlPage page, final Map attributes) {
        this(null, TAG_NAME, page, attributes);
    }

    /**
     *  Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     */
    HtmlSelect(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Return a List containing all of the currently selected options. The following special
     * conditions can occur if the element is in single select mode:
     * <ul>
     * <li>if multiple options are erroneously selected, the last one is returned</li>
     * <li>if no options are selected, the first one is returned</li>
     * </ul>
     *
     * @return  See above
     */
    public List getSelectedOptions() {
        List result;

        if(isMultipleSelectEnabled()) {
            result = new ArrayList();

            final DescendantElementsIterator iterator = new DescendantElementsIterator();
            while(iterator.hasNext()) {
                final HtmlElement element = iterator.nextElement();
                if(element instanceof HtmlOption && ((HtmlOption)element).isSelected()) {
                    result.add(element);
                }
            }
        }
        else {
            result = new ArrayList(1);

            HtmlOption firstOption = null;
            HtmlOption lastOption = null;

            final DescendantElementsIterator iterator = new DescendantElementsIterator();
            while(iterator.hasNext()) {
                final HtmlElement element = iterator.nextElement();
                if (element instanceof HtmlOption) {
                    final HtmlOption option = (HtmlOption) element;
                    if(firstOption == null) {
                        firstOption = option; //remember in case we need it
                    }
                    if(option.isSelected()) {
                        lastOption = option;
                    }
                }
            }
            if(lastOption != null) {
                result.add(lastOption);
            }
            else if (firstOption != null) {
                int theSize;
                try {
                    theSize = Integer.parseInt(getSizeAttribute());
                }
                catch (final NumberFormatException e) {
                    // Different browsers have different (and odd) tolerances for invalid
                    // size attributes so we'll just assume anything invalid is "1"
                    theSize = 1;
                }
                if (theSize <= 1) {
                    result.add(firstOption);
                }
            }
        }

        return Collections.unmodifiableList( result );
    }

    /**
     *  Return a List containing all the options
     *
     * @return  See above
     */
    public List getOptions() {
        final List elementList = getHtmlElementsByTagName( "option" );
        return Collections.unmodifiableList( elementList );
    }

    /**
     *  Return the indexed option.
     *
     * @param index The index
     * @return The option specified by the index
     */
    public HtmlOption getOption( final int index ) {

        final List elementList = getHtmlElementsByTagName( "option" );
        return (HtmlOption) elementList.get( index );
    }

    /**
     * Return the number of options
     * @return The number of options
     */
    public int getOptionSize() {

        final List elementList = getHtmlElementsByTagName( "option" );
        return elementList.size();
    }

    /**
     * Remove options by reducing the "length" property.  This has no
     * effect if the length is set to the same or greater.
     * @param newLength The new length property value
     */
    public void setOptionSize( final int newLength ) {
        final List elementList = getHtmlElementsByTagName( "option" );

        for(int i=elementList.size()-1; i >= newLength; i--) {
            ((HtmlElement)elementList.get(i)).remove();
        }
    }

    /**
     * Remove an option at the given index.
     * @param index The index of the option to remove
     */
    public void removeOption( final int index ) {
        final ChildElementsIterator iterator = new ChildElementsIterator();
        for(int i=0; iterator.hasNext(); i++) {
            final HtmlElement element = iterator.nextElement();
            if(i == index) {
                element.remove();
                return;
            }
        }
    }

    /**
     * Replace an option at the given index with a new option.
     * @param index The index of the option to remove
     * @param newOption The new option to replace to indexed option
     */
    public void replaceOption( final int index, final HtmlOption newOption ) {

        final ChildElementsIterator iterator = new ChildElementsIterator();
        for (int i=0; iterator.hasNext(); i++) {
            final HtmlElement element = iterator.nextElement();
            if (i == index) {
                element.replace(newOption);
                return;
            }
        }

        if (newOption.isSelected()) {
            setSelectedAttribute(newOption, true);
        }
    }

    /**
     * Add a new option at the end.
     * @param newOption The new option to add
     */
    public void appendOption( final HtmlOption newOption ) {
        appendChild( newOption ) ;
    }

    /**
     * {@inheritDoc}
     * @see DomNode#appendChild(DomNode)
     */
    public DomNode appendChild(final DomNode node) {
        final DomNode response = super.appendChild(node);
        if (node instanceof HtmlOption) {
            final HtmlOption option = (HtmlOption) node;
            if (option.isSelected()) {
                setSelectedAttribute(option, true);
            }
        }
        return response;
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
     * @return  The page that occupies this window after this change is made.  It
     * may be the same window or it may be a freshly loaded one.
     */
    public Page setSelectedAttribute( final String optionValue, final boolean isSelected ) {
        try {
            return setSelectedAttribute( getOptionByValue(optionValue), isSelected );
        }
        catch( final ElementNotFoundException e ) {
            throw new IllegalArgumentException("No option found with value: " + optionValue);
        }
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
     * @param  selectedOption The value of the option that is to change
     * @return  The page that occupies this window after this change is made.  It
     * may be the same window or it may be a freshly loaded one.
     */
    public Page setSelectedAttribute( final HtmlOption selectedOption, final boolean isSelected ) {
        final boolean triggerHandler  = (selectedOption.isSelected() != isSelected);

        fakeSelectedValues_ = null;

        // caution the HtmlOption may have been created from js and therefore the select now need
        // to "know" that it is selected 
        if (isMultipleSelectEnabled()) {
            selectedOption.setSelectedInternal(isSelected);
        }
        else {
            final Iterator iterator = getOptions().iterator();
            while( iterator.hasNext() ) {
                final HtmlOption option = ( HtmlOption )iterator.next();
                option.setSelectedInternal(option == selectedOption && isSelected);
            }
        }

        if (triggerHandler) {
            return HtmlInput.executeOnChangeHandlerIfAppropriate(this);
        }
        else {
            // nothing to do
            return getPage();
        }
    }

    /**
     *  Set the selected value to be something that was not originally contained
     *  in the document.
     *
     * @param  optionValue The value of the new "selected" option
     */
    public void fakeSelectedAttribute( final String optionValue ) {
        Assert.notNull( "optionValue", optionValue );
        fakeSelectedAttribute( new String[]{optionValue} );
    }

    /**
     *  Set the selected values to be something that were not originally
     *  contained in the document.
     *
     * @param  optionValues The values of the new "selected" options
     */
    public void fakeSelectedAttribute( final String optionValues[] ) {
        Assert.notNull( "optionValues", optionValues );
        fakeSelectedValues_ = optionValues;
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
        final String name = getNameAttribute();
        final KeyValuePair[] pairs;

        if (ArrayUtils.isEmpty(fakeSelectedValues_)) {
            final List selectedOptions = getSelectedOptions();
            final int optionCount = selectedOptions.size();

            pairs = new KeyValuePair[optionCount];

            for( int i = 0; i < optionCount; i++ ) {
                final HtmlOption option = ( HtmlOption )selectedOptions.get( i );
                pairs[i] = new KeyValuePair( name, option.getValueAttribute() );
            }
        }
        else {
            final List pairsList = new ArrayList();
            for( int i = 0; i < fakeSelectedValues_.length; i++ ) {
                if (fakeSelectedValues_[i].length() > 0) {
                    pairsList.add(new KeyValuePair( name, fakeSelectedValues_[i] ));
                }
            }
            pairs = (KeyValuePair[]) pairsList.toArray(new KeyValuePair[pairsList.size()]);
        }
        return pairs;
    }

    /**
     * Indicates if this select is submittable
     * @return <code>false</code> if not
     */
    boolean isValidForSubmission() {
        return getOptionSize() > 0 || (fakeSelectedValues_ != null && fakeSelectedValues_.length > 0);
    }

    /**
     * Return the value of this element to what it was at the time the page was loaded.
     */
    public void reset() {
        final Iterator iterator = getOptions().iterator();
        while( iterator.hasNext() ) {
            final HtmlOption option = (HtmlOption)iterator.next();
            option.reset();
        }
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultValue(String)
     */
    public void setDefaultValue( final String defaultValue ) {
        setSelectedAttribute( defaultValue, true );
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultValue(String)
     */
    public String getDefaultValue() {
        final List options = getSelectedOptions();
        if( options.size() > 0 ) {
            return ( (HtmlOption) options.get( 0 ) ).getValueAttribute();
        }
        else {
            return "";
        }
    }

    /**
     * {@inheritDoc} This implementation is empty; only checkboxes and radio buttons
     * really care what the default checked value is.
     * @see SubmittableElement#setDefaultChecked(boolean)
     * @see HtmlRadioButtonInput#setDefaultChecked(boolean)
     * @see HtmlCheckBoxInput#setDefaultChecked(boolean)
     */
    public void setDefaultChecked( final boolean defaultChecked ) {
        // Empty.
    }

    /**
     * {@inheritDoc} This implementation returns <tt>false</tt>; only checkboxes and
     * radio buttons really care what the default checked value is.
     * @see SubmittableElement#isDefaultChecked()
     * @see HtmlRadioButtonInput#isDefaultChecked()
     * @see HtmlCheckBoxInput#isDefaultChecked()
     */
    public boolean isDefaultChecked() {
        return false;
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
        Assert.notNull("value", value);

        return ( HtmlOption )getOneHtmlElementByAttribute( "option", "value", value );
    }

    /**
     *  Returns a text representation of this element that represents what would
     *  be visible to the user if this page was shown in a web browser. If the user
     *  can only select one option at a time, this method returns the selected option.
     *  If the user can select multiple options, this method returns all options.
     *
     * @return The element as text.
     */
    public String asText() {

        final List options;
        if(isMultipleSelectEnabled()) {
            options = getOptions();
        }
        else {
            options = getSelectedOptions();
        }

        final StringBuffer buffer = new StringBuffer();
        for (final Iterator i = options.iterator(); i.hasNext();) {
            final HtmlOption currentOption = (HtmlOption) i.next();
            if (currentOption != null) {
                buffer.append(currentOption.asText());
            }
            if (i.hasNext()) {
                buffer.append("\n");
            }
        }

        return buffer.toString();
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
     * Return true if the disabled attribute is set for this element.
     *
     * @return Return true if this element is disabled.
     */
    public final boolean isDisabled() {
        return isAttributeDefined( "disabled" );
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
