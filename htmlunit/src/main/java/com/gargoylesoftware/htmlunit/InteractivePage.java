/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_FOCUS_FOCUS_IN_BLUR_OUT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_FOCUS_IN_FOCUS_OUT_BLUR;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.PAGE_SELECTION_RANGE_FROM_SELECTABLE_TEXT_INPUT;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.w3c.dom.ranges.Range;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

/**
 * An interactive SGML page, which is able to handle JavaScript events.
 *
 * @author Ahmed Ashour
 */
public abstract class InteractivePage extends SgmlPage {

    private DomElement elementWithFocus_;
    private List<Range> selectionRanges_ = new ArrayList<>(3);

    /**
     * Creates an instance of {@code InteractiveSgmlPage}.
     *
     * @param webResponse the web response that was used to create this page
     * @param webWindow the window that this page is being loaded into
     */
    public InteractivePage(final WebResponse webResponse, final WebWindow webWindow) {
        super(webResponse, webWindow);
    }

    /**
     * Moves the focus to the specified element. This will trigger any relevant JavaScript
     * event handlers.
     *
     * @param newElement the element that will receive the focus, use {@code null} to remove focus from any element
     * @return true if the specified element now has the focus
     * @see #getFocusedElement()
     */
    public boolean setFocusedElement(final DomElement newElement) {
        return setFocusedElement(newElement, false);
    }

    /**
     * Moves the focus to the specified element. This will trigger any relevant JavaScript
     * event handlers.
     *
     * @param newElement the element that will receive the focus, use {@code null} to remove focus from any element
     * @param windowActivated - whether the enclosing window got focus resulting in specified element getting focus
     * @return true if the specified element now has the focus
     * @see #getFocusedElement()
     */
    public boolean setFocusedElement(final DomElement newElement, final boolean windowActivated) {
        if (elementWithFocus_ == newElement && !windowActivated) {
            // nothing to do
            return true;
        }

        final DomElement oldFocusedElement = elementWithFocus_;
        elementWithFocus_ = null;

        if (!windowActivated) {
            if (hasFeature(EVENT_FOCUS_IN_FOCUS_OUT_BLUR)) {
                if (oldFocusedElement != null) {
                    oldFocusedElement.fireEvent(Event.TYPE_FOCUS_OUT);
                }

                if (newElement != null) {
                    newElement.fireEvent(Event.TYPE_FOCUS_IN);
                }
            }

            if (oldFocusedElement != null) {
                oldFocusedElement.removeFocus();
                oldFocusedElement.fireEvent(Event.TYPE_BLUR);
            }
        }

        elementWithFocus_ = newElement;

        if (elementWithFocus_ instanceof SelectableTextInput
                && hasFeature(PAGE_SELECTION_RANGE_FROM_SELECTABLE_TEXT_INPUT)) {
            final SelectableTextInput sti = (SelectableTextInput) elementWithFocus_;
            setSelectionRange(new SimpleRange(sti, sti.getSelectionStart(), sti, sti.getSelectionEnd()));
        }

        if (elementWithFocus_ != null) {
            elementWithFocus_.focus();
            elementWithFocus_.fireEvent(Event.TYPE_FOCUS);
        }

        if (hasFeature(EVENT_FOCUS_FOCUS_IN_BLUR_OUT)) {
            if (oldFocusedElement != null) {
                oldFocusedElement.fireEvent(Event.TYPE_FOCUS_OUT);
            }

            if (newElement != null) {
                newElement.fireEvent(Event.TYPE_FOCUS_IN);
            }
        }

        // If a page reload happened as a result of the focus change then obviously this
        // element will not have the focus because its page has gone away.
        return this == getEnclosingWindow().getEnclosedPage();
    }

    /**
     * Returns the element with the focus or null if no element has the focus.
     * @return the element with focus or null
     * @see #setFocusedElement(DomElement)
     */
    public DomElement getFocusedElement() {
        return elementWithFocus_;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * Sets the element with focus.
     * @param elementWithFocus the element with focus
     */
    public void setElementWithFocus(final DomElement elementWithFocus) {
        elementWithFocus_ = elementWithFocus;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * <p>Returns the page's current selection ranges. Note that some browsers, like IE, only allow
     * a single selection at a time.</p>
     *
     * @return the page's current selection ranges
     */
    public List<Range> getSelectionRanges() {
        return selectionRanges_;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * <p>Makes the specified selection range the *only* selection range on this page.</p>
     *
     * @param selectionRange the selection range
     */
    public void setSelectionRange(final Range selectionRange) {
        selectionRanges_.clear();
        selectionRanges_.add(selectionRange);
    }

    /**
     * {@inheritDoc}
     * Override cloneNode to add cloned elements to the clone, not to the original.
     */
    @Override
    public InteractivePage cloneNode(final boolean deep) {
        // we need the ScriptObject clone before cloning the kids.
        final InteractivePage result = (InteractivePage) super.cloneNode(false);
        final SimpleScriptable jsObjClone = ((SimpleScriptable) getScriptableObject()).clone();
        jsObjClone.setDomNode(result);

        // if deep, clone the kids too, and re initialize parts of the clone
        if (deep) {
            result.selectionRanges_ = new ArrayList<>(3);

        }
        return result;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Execute a Function in the given context.
     *
     * @param function the JavaScript Function to call
     * @param thisObject the "this" object to be used during invocation
     * @param args the arguments to pass into the call
     * @param htmlElementScope the HTML element for which this script is being executed
     * This element will be the context during the JavaScript execution. If null,
     * the context will default to the page.
     * @return a ScriptResult which will contain both the current page (which may be different than
     * the previous page and a JavaScript result object.
     */
    public ScriptResult executeJavaScriptFunctionIfPossible(final Function function, final Scriptable thisObject,
            final Object[] args, final DomNode htmlElementScope) {

        if (!getWebClient().getOptions().isJavaScriptEnabled()) {
            return new ScriptResult(null, this);
        }

        final JavaScriptEngine engine = getWebClient().getJavaScriptEngine();
        final Object result = engine.callFunction(this, function, thisObject, args, htmlElementScope);

        return new ScriptResult(result, getWebClient().getCurrentWindow().getEnclosedPage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InteractivePage clone() {
        final InteractivePage result = (InteractivePage) super.clone();
        result.elementWithFocus_ = null;
        return result;
    }
}
