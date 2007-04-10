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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.host.Option;
import com.gargoylesoftware.htmlunit.javascript.host.HTMLSelectElement;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * This is the array returned by the "options" property of Select.
 *
 * @version  $Revision$
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Bruce Faulkner
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
        final Object response;
        if (index < 0) {
            throw Context.reportRuntimeError("Index or size is negative");
        }
        else if (index >= htmlSelect_.getOptionSize()) {
            response = Context.getUndefinedValue();
        }
        else {
            response = getScriptableFor(htmlSelect_.getOption(index));
        }

        return response;
    }


    /**
     * Returns the object with the specified name. If the specified object
     * is not found and we are emulating IE, this method delegates the call
     * to the parent select.
     * 
     * @param name The name of the object to return.
     * @param start The object from which to get the named object.
     * @return The object corresponding to the specified name or <tt>NOT_FOUND</tt>.
     */
    public Object get( final String name, final Scriptable start ) {
        Object object = super.get( name, start );
        if( object == NOT_FOUND ) {
            // First, check the prototype chain for the OptionsArray to see if the
            // name exists elsewhere on the chain before looking to the Select object;
            // this must be done in case a property or method with the same name
            // is found on the Select object (e.g. "add") but which has different parameters.
            // Doing a search on the prototoype before a search on the Select will avoid a
            // "TypeError: Method "add" called on incompatible object."
            final Scriptable prototype = this.getPrototype();
            if (prototype instanceof OptionsArray) {
                object = prototype.get(name, start);
                if (object != NOT_FOUND) {
                    return object;
                }
            }
            else {
                return NOT_FOUND;
            }

            if( getWindow().getWebWindow().getWebClient().getBrowserVersion().isIE() ) {
                // If the name was NOT_FOUND on the prototype, then just drop through
                // to search on the Select for IE only
                final HTMLSelectElement select = (HTMLSelectElement) htmlSelect_.getScriptObject();
                object = select.get( name, start );
            }
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
    public void put(
            final int index, final Scriptable start, final Object newValue) {
        if ( newValue == null ) {
            // Remove the indexed option.
            htmlSelect_.removeOption( index );
        }
        else {
            final Option option = (Option) newValue;
            final HtmlOption htmlOption = (HtmlOption) option.getHtmlElementOrNull();
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
     * Change the number of options: removes options if the new lenght
     * is less than the current one else add new empty options to reach the
     * new length.
     * @param newLength The new length property value
     */
    public void jsSet_length( final int newLength ) {
        final int currentLength = htmlSelect_.getOptionSize();
        if (currentLength > newLength) {
            htmlSelect_.setOptionSize( newLength );
        }
        else {
            for (int i=currentLength; i<newLength; ++i) {
                htmlSelect_.appendOption(new HtmlOption(htmlSelect_.getPage(), null));
            }
        }
    }

    /**
     * Add a new item to the option collection
     * 
     * <p> 
     * <b><i>Implementation Note:</i></b> The specification for the JavaScript add() method 
     * actually calls for the optional newIndex parameter to be an integer. However, the 
     * newIndex parameter is specified as an Object here rather than an int because of the 
     * way Rhino and HtmlUnit process optional parameters for the JavaScript method calls. 
     * If the newIndex parameter were specified as an int, then the Undefined value for an 
     * integer is specified as NaN (Not A Number, which is a Double value), but Rhino 
     * translates this value into 0 (perhaps correctly?) when converting NaN into an int. 
     * As a result, when the newIndex parameter is not specified, it is impossible to make 
     * a distinction between a caller of the form add(someObject) and add (someObject, 0). 
     * Since the behavior of these two call forms is different, the newIndex parameter is 
     * specified as an Object. If the newIndex parameter is not specified by the actual 
     * JavaScript code being run, then newIndex is of type org.mozilla.javascript.Undefined. 
     * If the newIndex parameter is specified, then it should be of type java.lang.Number and 
     * can be converted into an integer value.
     * </p>
     * <p>
     * This method will call the {@link #put} method for actually adding the element to the
     * collection.
     * </p>
     * <p>
     * According to 
     * <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/add.asp">the
     * Microsoft DHTML reference page for the JavaScript add() method of the options collection</a>, 
     * the index parameter is specified as follows:
     * <dl>
     * <dt></dt>
     * <dd>
     * <i>Optional. Integer that specifies the index position in the collection where the element is 
     * placed. If no value is given, the method places the element at the end of the collection.</i>
     * </dl>
     * </p>
     * 
     * @param newOptionObject The DomNode to insert in the collection
     * @param newIndex An optional parameter which specifies the index position in the 
     * collection where the element is placed. If no value is given, the method places 
     * the element at the end of the collection.
     * 
     * @see #put   
     */
    public void jsFunction_add(final Object newOptionObject, final Object newIndex)
    {
        // If newIndex is undefined, then the item will be appended to the end of
        // the list
        int index = jsGet_length();

        // If newIndex was specified, then use it
        if (newIndex instanceof Number) {
            index = ((Number) newIndex).intValue();
        }

        // The put method either appends or replaces an object in the list,
        // depending on the value of index
        put(index, null, newOptionObject);
    }

}
