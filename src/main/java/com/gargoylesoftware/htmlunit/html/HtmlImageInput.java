/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class HtmlImageInput extends HtmlInput {

    // For click with x, y position.
    private boolean wasPositionSpecified_;
    private int xPosition_;
    private int yPosition_;

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlImageInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitKeyValuePairs() {
        final String name = getNameAttribute();
        final String prefix;
        // a clicked image without name sends parameter x and y
        if (StringUtils.isEmpty(name)) {
            prefix = "";
        }
        else {
            prefix = name + ".";
        }

        if (wasPositionSpecified_) {
            final NameValuePair valueX = new NameValuePair(prefix + 'x', Integer.toString(xPosition_));
            final NameValuePair valueY = new NameValuePair(prefix + 'y', Integer.toString(yPosition_));
            if (prefix.length() > 0 && getPage().getWebClient().getBrowserVersion()
                    .hasFeature(BrowserVersionFeatures.HTMLIMAGE_NAME_VALUE_PARAMS)
                    && getValueAttribute().length() > 0) {
                return new NameValuePair[] {valueX, valueY,
                    new NameValuePair(getNameAttribute(), getValueAttribute()) };
            }
            return new NameValuePair[] {valueX, valueY};
        }
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getValueAttribute())};
    }

    /**
     * Submit the form that contains this input. Only a couple of the inputs
     * support this method so it is made protected here. Those subclasses
     * that wish to expose it will override and make it public.
     *
     * @return the Page that is the result of submitting this page to the server
     * @exception IOException If an io error occurs
     */
    @Override
    @SuppressWarnings("unchecked")
    public Page click() throws IOException {
        return click(0, 0);
    }

    /**
     * This method will be called if there either wasn't an onclick handler or there was
     * but the result of that handler was true. This is the default behavior of clicking
     * the element. The default implementation returns the current page - subclasses
     * requiring different behavior (like {@link HtmlSubmitInput}) will override this
     * method.
     *
     * @throws IOException if an IO error occurred
     */
    @Override
    protected void doClickAction() throws IOException {
        final HtmlForm form = getEnclosingForm();
        if (form != null) {
            form.submit(this);
            return;
        }
        super.doClickAction();
    }

    /**
     * Simulate clicking this input with a pointing device. The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param <P> the page type
     * @param x the x coordinate of the pointing device at the time of clicking
     * @param y the y coordinate of the pointing device at the time of clicking
     * @return the page that is loaded after the click has taken place
     * @exception IOException If an io error occurs
     * @exception ElementNotFoundException If a particular XML element could not be found in the DOM model
     */
    @Override
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final int x, final int y) throws IOException, ElementNotFoundException {
        wasPositionSpecified_ = true;
        xPosition_ = x;
        yPosition_ = y;
        return (P) super.click();
    }

    /**
     * {@inheritDoc} Also sets the value to the new default value, just like IE.
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        super.setDefaultValue(defaultValue);
        setValueAttribute(defaultValue);
    }
}
