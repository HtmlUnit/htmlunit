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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;

/**
 * The javascript object for form input elements (html tag &lt;input ...&gt;).
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 */
public class Input extends FormField {

    private static final long serialVersionUID = 3712016051364495710L;


    /**
     *  Create an instance.
     */
    public Input() {
    }


    /**
     *  Javascript constructor. This must be declared in every javascript file
     *  because the rhino engine won't walk up the hierarchy looking for
     *  constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Sets the value of the attribute "type".
     * Note: this replace the DOM node with a new one.
     * @param newType the new type to set
     */
    public void jsSet_type(final String newType) {
        final HtmlInput input = getHtmlInputOrDie();
        if (!input.getTypeAttribute().equalsIgnoreCase(newType)) {
            final AttributesImpl attributes = readAttributes(input);
            final int index = attributes.getIndex("type");
            attributes.setValue(index, newType);

            final HtmlInput newInput = (HtmlInput) InputElementFactory.instance
                .createElement(input.getPage(), "input", attributes);
            input.replace(newInput);
            input.setScriptObject(null);
            setDomNode(newInput, true);
        }
    }

    /**
     *  Set the checked property. Although this property is defined in Input it
     *  doesn't make any sense for input's other than checkbox and radio. This
     *  implementation does nothing. The implementations in Checkbox and Radio
     *  actually do the work.
     *
     *@param  checked  True if this input should have the "checked" attribute
     *      set
     */
    public void jsSet_checked( final boolean checked ) {
        final HtmlInput input = getHtmlInputOrDie();
        final String type = input.getTypeAttribute().toLowerCase();
        if (type.equals("checkbox") || type.equals("radio")){
            input.setChecked(checked);
        }
        else {
            getLog().debug( "Input.jsSet_checked(" + checked
                + ") was called for class " + getClass().getName() );
        }
    }
    
    /**
     * Commodity for <code>(HtmlInput) getHtmlElementOrDie()</code>
     * @return the bound html input
     */
    protected HtmlInput getHtmlInputOrDie() {
        return (HtmlInput) getHtmlElementOrDie();
    }


    /**
     *  Return the value of the checked property. Although this property is
     *  defined in Input it doesn't make any sense for input's other than
     *  checkbox and radio. This implementation does nothing. The
     *  implementations in Checkbox and Radio actually do the work.
     *
     *@return    The checked property.
     */
    public boolean jsGet_checked() {
        final HtmlInput input = getHtmlInputOrDie();
        final String type = input.getTypeAttribute().toLowerCase();
        if (type.equals("checkbox") || type.equals("radio")){
            return input.isChecked();
        }
        else {
            getLog().warn( "Input.jsGet_checked() was called for class " + getClass().getName() );
            return false;
        }
    }

}

