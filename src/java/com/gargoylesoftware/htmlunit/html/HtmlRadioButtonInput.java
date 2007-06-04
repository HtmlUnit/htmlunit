/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
import java.util.Map;

import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Mike Bresnahan
 * @author Daniel Gredler
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
public class HtmlRadioButtonInput extends HtmlInput {

    private boolean defaultCheckedState_;

    /**
     * Create an instance
     * If no value is specified, it is set to "on" as browsers do (eg IE6 and Mozilla 1.7) 
     * even if spec says that it is not allowed 
     * (<a href="http://www.w3.org/TR/REC-html40/interact/forms.html#adef-value-INPUT">W3C</a>).
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     */
    public HtmlRadioButtonInput( final HtmlPage page, final Map attributes ) {
        super(page, attributes);

        // default value for both IE6 and Mozilla 1.7 even if spec says it is unspecified
        if (getAttributeValue("value") == ATTRIBUTE_NOT_DEFINED) {
            setAttributeValue("value", "on");
        }

        defaultCheckedState_ = isAttributeDefined("checked");
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    public void reset() {
        if( defaultCheckedState_ ) {
            setAttributeValue("checked", "checked");
        }
        else {
            removeAttribute("checked");
        }
    }

    /**
     *  Set the "checked" attribute
     *
     * @param  isChecked true if this element is to be selected
     * @return The page that occupies this window after setting checked status.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page setChecked( final boolean isChecked ) {
        final HtmlForm form = getEnclosingForm();
        final boolean changed = isChecked() != isChecked;

        if( isChecked ) {
            form.setCheckedRadioButton(this);
        }
        else {
            removeAttribute( "checked" );
        }

        final Function onchangeHandler = getEventHandler("onchange");
        Page page = getPage();

        if (changed && onchangeHandler != null && ((HtmlPage)page).getWebClient().isJavaScriptEnabled()) {
            final Event event = new Event(this, Event.TYPE_CHANGE);
            page = getPage().runEventHandler(onchangeHandler, event).getNewPage();
        }
        return page;
    }

    /**
     * A radio button does not have a textual representation,
     * but we invent one for it because it is useful for testing.
     * @return "checked" or "unchecked" according to the radio state
     */
    public String asText() {
        if (isChecked()) {
            return "checked";
        }
        else {
            return "unchecked";
        }
    }

    /**
     * Override of default clickAction that makes this radio button the selected
     * one when it is clicked
     *
     * @param defaultPage The default page to return if the action does not
     * load a new page.
     * @return The page that is currently loaded after execution of this method
     * @throws IOException If an IO error occured
     */
    protected Page doClickAction(final Page defaultPage) throws IOException {
        setChecked(true);
        return defaultPage;
    }

    /**
     * {@inheritDoc} Also sets the value to the new default value.
     * @see SubmittableElement#setDefaultValue(String)
     */
    public void setDefaultValue( final String defaultValue ) {
        super.setDefaultValue( defaultValue );
        setValueAttribute( defaultValue );
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultChecked(boolean)
     */
    public void setDefaultChecked( final boolean defaultChecked ) {
        defaultCheckedState_ = defaultChecked;
        if( getPage().getWebClient().getBrowserVersion().isNetscape() ) {
            setChecked( defaultChecked );
        }
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#isDefaultChecked()
     */
    public boolean isDefaultChecked() {
        return defaultCheckedState_;
    }

    /**
     * {@inheritDoc}
     * @see com.gargoylesoftware.htmlunit.html.ClickableElement#isStateUpdateFirst()
     */
    protected boolean isStateUpdateFirst() {
        return true;
    }
}

