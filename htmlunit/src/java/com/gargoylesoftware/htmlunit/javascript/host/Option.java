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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlOption;

/**
 * The javascript object that represents an option.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 */
public class Option extends HTMLElement {
    private static final long serialVersionUID = 947015932373556314L;
    private String text_;
    private String value_;
    private boolean selected_;

    /**
     * Create an instance.
     */
    public Option() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     * @param newText The text
     * @param newValue The value
     */
    public void jsConstructor( final String newText, final String newValue ) {
        if ( newText != null && ! newText.equals( "undefined" ) ) {
            text_ = newText;
        }
        if ( newValue != null && ! newValue.equals( "undefined" ) ) {
            value_ = newValue;
        }
    }

     /**
      * Set the DOM node that corresponds to this javascript object
      * @param domNode The DOM node
      */
    public void setDomNode( final DomNode domNode ) {
        super.setDomNode( domNode );
        if ( value_ != null ) {
            jsxSet_value( value_ );
            jsxSet_text( text_ );
            jsxSet_selected(selected_);
        }
    }

    /**
     * Return the value of the "value" property
     * @return The value property
     */
    public String jsxGet_value() {
        final HtmlOption htmlOption = getHtmlOption();
        if (htmlOption == null) {
            return value_;
        }
        
        return ((HtmlOption)getHtmlElementOrDie()).getValueAttribute();
    }

    /**
     * Set the value of the "value" property
     * @param newValue The value property
     */
    public void jsxSet_value( final String newValue ) {
        final HtmlOption htmlOption = getHtmlOption();
        if (htmlOption == null) {
            value_ = newValue;
        }
        else {
            htmlOption.setValueAttribute( newValue );
        }
    }


    /**
     * Return the value of the "text" property
     * @return The text property
     */
    public String jsxGet_text() {
        final HtmlOption htmlOption = getHtmlOption();
        if (htmlOption == null) {
            return text_;
        }
        if ( htmlOption.isAttributeDefined( "label" ) ) {
            return htmlOption.getLabelAttribute();
        }
        return htmlOption.asText();
    }

    private HtmlOption getHtmlOption() {
        return (HtmlOption) getHtmlElementOrNull();
    }

    /**
     * Set the value of the "text" property
     * @param newText The text property
     */
    public void jsxSet_text( final String newText ) {
        final HtmlOption htmlOption = getHtmlOption();
        if (htmlOption == null) {
            text_ = newText;
        }
        else {
            htmlOption.setLabelAttribute( newText );
        }
    }

    /**
     * Return the value of the "selected" property
     * @return The text property
     */
    public boolean jsxGet_selected() {
        final HtmlOption htmlOption = getHtmlOption();
        if (htmlOption == null) {
            return selected_;
        }
        return htmlOption.isSelected();
    }


    /**
     * Set the value of the "selected" property
     * @param selected The new selected property
     */
    public void jsxSet_selected( final boolean selected ) {
        final HtmlOption htmlOption = getHtmlOption();
        if (htmlOption == null) {
            selected_ = selected;
        }
        else {
            htmlOption.setSelected( selected );
        }
    }
}
