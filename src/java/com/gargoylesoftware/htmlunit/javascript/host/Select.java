/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import java.util.List;

/**
 * The javascript object that represents a select
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Select extends Input {

    /**
     * Create an instance.
     */
    public Select() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the type of this input.
     * @return The type
     */
    public String jsGet_type() {
        final String type;
        if( getHtmlElementOrDie().getAttributeValue("multiple").length() == 0 ) {
            type = "select-one";
        }
        else {
            type = "select-multiple";
        }
        return type;
    }


    /**
     * Return the value of the "options" property
     * @return The options property
     */
    public Option[] jsGet_options() {
        final HtmlSelect htmlSelect = (HtmlSelect)getHtmlElementOrDie();
        final List allOptions = htmlSelect.getAllOptions();
        final Option optionArray[] = new Option[allOptions.size()];

        for( int i=0; i<optionArray.length; i++ ) {
            final HtmlOption htmlOption = (HtmlOption)allOptions.get(i);
            Option jsOption = (Option)htmlOption.getScriptObject();
            if( jsOption == null ) {
                jsOption = (Option)makeJavaScriptObject("Option");
                jsOption.setHtmlElement(htmlOption);
                htmlOption.setScriptObject(jsOption);
            }
            optionArray[i] = jsOption;
        }
        return optionArray;
    }


    /**
     * Return the value of the "selectedIndex" property
     * @return The selectedIndex property
     */
    public int jsGet_selectedIndex() {
        final HtmlSelect htmlSelect = (HtmlSelect)getHtmlElementOrDie();
        final List selectedOptions = htmlSelect.getSelectedOptions();
        if( selectedOptions.isEmpty() ) {
            return -1;
        }
        else {
            final List allOptions = htmlSelect.getAllOptions();
            return allOptions.indexOf(selectedOptions.get(0));
        }
    }


    /**
     * Set the value of the "selectedIndex" property
     * @param index The new value
     */
    public void jsSet_selectedIndex( final int index ) {
        getLog().debug("select.selectedIndex not implemented yet");
    }


    /**
     * Return the value of the "length" property
     * @return The length property
     */
    public int jsGet_length() {
        final HtmlSelect htmlSelect = (HtmlSelect)getHtmlElementOrDie();
        return htmlSelect.getAllOptions().size();
    }
}

