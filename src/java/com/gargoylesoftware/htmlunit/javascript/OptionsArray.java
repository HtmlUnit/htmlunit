/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.host.Option;
import org.mozilla.javascript.Scriptable;

/**
 * This is the array returned by the "options" property of Select.
 *
 * @version  $Revision$
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 */
public class OptionsArray extends SimpleScriptable {
    private static final long serialVersionUID = -4790255174217201235L;
	private HtmlSelect htmlSelect_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public OptionsArray() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }


    /**
     * Initialize this object
     * @param select The HtmlSelect that this object will retrive elements from.
     */
    public void initialize( final HtmlSelect select ) {
        Assert.notNull("select", select);
        htmlSelect_ = select;
    }


    /**
     * <p>Return the object at the specified index.</p>
     *
     * @param index The index
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get( final int index, final Scriptable start ) {
        Object object = null;
        try {
            object = getScriptableFor( htmlSelect_.getOption( index ) );
        }
        catch( final IndexOutOfBoundsException e ) {
            object = NOT_FOUND;
        }
        return object;
    }

    /**
     * <p>Return the object at the specified index.</p>
     *
     * @param index The index
     * @return The object or NOT_FOUND
     */
    public Object jsFunction_item(final int index) {
        return get(index, null);
    }


    /**
     * Set the index property
     * @param index The index
     * @param start The scriptable object that was originally invoked for this property
     * @param newValue The new value
     */
    public void put( final int index, final Scriptable start,
        Object newValue ) {
        if ( newValue == null ) {
            // Remove the indexed option.
            htmlSelect_.removeOption( index );
        }
        else {
            final Option option = (Option) newValue;
            HtmlOption htmlOption = (HtmlOption) option.getHtmlElementOrNull();
            if ( htmlOption == null ) {
                initJavaScriptObject( option );
                htmlOption = new HtmlOption(htmlSelect_.getPage(), null);
                option.setDomNode( htmlOption );
                // BUG: Set the text and value.
            }
            if ( index >= jsGet_length() ) {
                // Add a new option at the end.
                htmlSelect_.appendOption( htmlOption );
            }
            else {
                // Replace the indexed option.
                htmlSelect_.replaceOption( index, htmlOption );
            }
        }
    }


   /**
    * <p>Return the number of elements in this array</p>
    *
    * @return The number of elements in the array
    */
    public int jsGet_length() {
        return htmlSelect_.getOptionSize();
    }


    /**
     * Remove options by reducing the "length" property
     * @param newLength The new length property value
     */
    public void jsSet_length( final int newLength ) {
        htmlSelect_.setOptionSize( newLength );
    }
}
