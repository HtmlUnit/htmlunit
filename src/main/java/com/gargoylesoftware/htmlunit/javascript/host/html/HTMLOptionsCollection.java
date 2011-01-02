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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * This is the array returned by the "options" property of Select.
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
public class HTMLOptionsCollection extends SimpleScriptable implements ScriptableWithFallbackGetter {

    private HtmlSelect htmlSelect_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public HTMLOptionsCollection() {
        // Empty.
    }

    /**
     * Creates an instance.
     * @param parentScope parent scope
     */
    public HTMLOptionsCollection(final SimpleScriptable parentScope) {
        setParentScope(parentScope);
        setPrototype(getPrototype(getClass()));
    }

    /**
     * Initializes this object.
     * @param select the HtmlSelect that this object will retrieve elements from
     */
    public void initialize(final HtmlSelect select) {
        WebAssert.notNull("select", select);
        htmlSelect_ = select;
    }

    /**
     * Returns the object at the specified index.
     *
     * @param index the index
     * @param start the object that get is being called on
     * @return the object or NOT_FOUND
     */
    @Override
    public Object get(final int index, final Scriptable start) {
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
     * <p>If IE is emulated, and this class does not have the specified property, and the owning
     * select *does* have the specified property, this method delegates the call to the parent
     * select element.</p>
     *
     * <p>See {@link #getWithFallback(String)} for the corresponding getter behavior.</p>
     *
     * @param name {@inheritDoc}
     * @param start {@inheritDoc}
     * @param value {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (htmlSelect_ == null) {
            // This object hasn't been initialized; it's probably being used as a prototype.
            // Just pretend we didn't even see this invocation and let Rhino handle it.
            super.put(name, start, value);
            return;
        }

        final HTMLSelectElement parent = (HTMLSelectElement) htmlSelect_.getScriptObject();

        if (!has(name, start) && ScriptableObject.hasProperty(parent, name)) {
            ScriptableObject.putProperty(parent, name, value);
        }
        else {
            super.put(name, start, value);
        }
    }

    /**
     * <p>This method delegates the call to the parent select element.</p>
     *
     * <p>See {@link #put(String, Scriptable, Object)} for the corresponding setter behavior.</p>
     *
     * @param name {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_87) && "childNodes".equals(name)) {
            return NOT_FOUND;
        }
        // If the name was NOT_FOUND on the prototype, then just drop through
        // to search on the select element for IE only AND FF.
        final HTMLSelectElement select = (HTMLSelectElement) htmlSelect_.getScriptObject();
        return ScriptableObject.getProperty(select, name);
    }

    /**
     * Returns the object at the specified index.
     *
     * @param index the index
     * @return the object or NOT_FOUND
     */
    public Object jsxFunction_item(final int index) {
        return get(index, null);
    }

    /**
     * Sets the index property.
     * @param index the index
     * @param start the scriptable object that was originally invoked for this property
     * @param newValue the new value
     */
    @Override
    public void put(final int index, final Scriptable start, final Object newValue) {
        if (newValue == null) {
            // Remove the indexed option.
            htmlSelect_.removeOption(index);
        }
        else {
            final HTMLOptionElement option = (HTMLOptionElement) newValue;
            final HtmlOption htmlOption = option.getDomNodeOrNull();
            if (index >= jsxGet_length()) {
                // Add a new option at the end.
                htmlSelect_.appendOption(htmlOption);
            }
            else {
                // Replace the indexed option.
                htmlSelect_.replaceOption(index, htmlOption);
            }
        }
        if (jsxGet_length() == 1 && !htmlSelect_.isMultipleSelectEnabled()) {
            ((HTMLSelectElement) htmlSelect_.getScriptObject()).jsxSet_selectedIndex(0);
        }
    }

   /**
    * Returns the number of elements in this array.
    *
    * @return the number of elements in the array
    */
    public int jsxGet_length() {
        return htmlSelect_.getOptionSize();
    }

    /**
     * Changes the number of options: removes options if the new length
     * is less than the current one else add new empty options to reach the
     * new length.
     * @param newLength the new length property value
     */
    public void jsxSet_length(final int newLength) {
        final int currentLength = htmlSelect_.getOptionSize();
        if (currentLength > newLength) {
            htmlSelect_.setOptionSize(newLength);
        }
        else {
            for (int i = currentLength; i < newLength; i++) {
                final HtmlOption option = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                        htmlSelect_.getPage(), HtmlOption.TAG_NAME, null);
                htmlSelect_.appendOption(option);
                if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_88)) {
                    option.appendChild(new DomText(option.getPage(), ""));
                }
            }
        }
    }

    /**
     * Adds a new item to the option collection.
     *
     * <p><b><i>Implementation Note:</i></b> The specification for the JavaScript add() method
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
     * JavaScript code being run, then newIndex is of type net.sourceforge.htmlunit.corejs.javascript.Undefined.
     * If the newIndex parameter is specified, then it should be of type java.lang.Number and
     * can be converted into an integer value.</p>
     *
     * <p>This method will call the {@link #put(int, Scriptable, Object)} method for actually
     * adding the element to the collection.</p>
     *
     * <p>According to <a href="http://msdn.microsoft.com/en-us/library/ms535921.aspx">the
     * Microsoft DHTML reference page for the JavaScript add() method of the options collection</a>,
     * the index parameter is specified as follows:
     * <dl>
     * <i>Optional. Integer that specifies the index position in the collection where the element is
     * placed. If no value is given, the method places the element at the end of the collection.</i>
     * </dl>
     * </p>
     *
     * @param newOptionObject the DomNode to insert in the collection
     * @param newIndex An optional parameter which specifies the index position in the
     * collection where the element is placed. If no value is given, the method places
     * the element at the end of the collection.
     *
     * @see #put(int, Scriptable, Object)
     */
    public void jsxFunction_add(final Object newOptionObject, final Object newIndex) {
        // If newIndex is undefined, then the item will be appended to the end of
        // the list
        int index = jsxGet_length();

        // If newIndex was specified, then use it
        if (newIndex instanceof Number) {
            index = ((Number) newIndex).intValue();
        }

        // The put method either appends or replaces an object in the list,
        // depending on the value of index
        put(index, null, newOptionObject);
    }

    /**
     * Removes the option at the specified index.
     * @param index the option index
     */
    public void jsxFunction_remove(final int index) {
        if (index < 0) {
            Context.reportRuntimeError("Invalid index: " + index);
        }

        if (index < jsxGet_length()) {
            htmlSelect_.removeOption(index);
        }
    }
}
