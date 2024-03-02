/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.event;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.DomNode;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Window;

/**
 * JavaScript object representing a UI event. For general information on which properties and functions should be
 * supported, see <a href="http://www.w3.org/TR/DOM-Level-3-Events/events.html#Events-UIEvent">DOM Level 3 Events</a>.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class UIEvent extends Event {

    /** Constant. */
    @JsxConstant({FF, FF_ESR})
    public static final int SCROLL_PAGE_DOWN = 0x8000;

    /** Constant. */
    @JsxConstant({FF, FF_ESR})
    public static final int SCROLL_PAGE_UP = 0xFFFF8000;

    /** Specifies some detail information about the event. */
    private long detail_;

    /** Specifies some view information about the event. */
    private Object view_;
    private static final Object NO_VIEW = new Object();

    /**
     * Creates a new UI event instance.
     */
    public UIEvent() {
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @JsxConstructor
    @Override
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        view_ = NO_VIEW;
        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            final Object view = details.get("view", details);
            if (view instanceof Window) {
                view_ = view;
            }
            else if (view != Scriptable.NOT_FOUND) {
                throw JavaScriptEngine.typeError("View must be a window.");
            }
        }
    }

    /**
     * Creates a new UI event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public UIEvent(final DomNode domNode, final String type) {
        super(domNode, type);
    }

    /**
     * Creates a new event instance.
     * @param target the event target
     * @param type the event type
     */
    public UIEvent(final EventTarget target, final String type) {
        super(target, type);
    }

    /**
     * Returns some detail information about the event, depending on the event type. For mouse events,
     * the detail property indicates how many times the mouse has been clicked in the same location for
     * this event.
     *
     * @return some detail information about the event, depending on the event type
     */
    @JsxGetter
    public long getDetail() {
        return detail_;
    }

    /**
     * Sets the detail information for this event.
     *
     * @param detail the detail information for this event
     */
    protected void setDetail(final long detail) {
        detail_ = detail;
    }

    /**
     * Returns the view from which the event was generated. In browsers, this is the originating window.
     *
     * @return the view from which the event was generated
     */
    @JsxGetter
    public Object getView() {
        if (view_ == NO_VIEW) {
            return null;
        }
        if (view_ != null) {
            return view_;
        }
        return getWindow();
    }

    /**
     * Implementation of the DOM Level 3 Event method for initializing the UI event.
     *
     * @param type the event type
     * @param bubbles can the event bubble
     * @param cancelable can the event be canceled
     * @param view the view to use for this event
     * @param detail the detail to set for the event
     */
    @JsxFunction({CHROME, EDGE, FF, FF_ESR, IE})
    public void initUIEvent(
            final String type,
            final boolean bubbles,
            final boolean cancelable,
            final Object view,
            final int detail) {
        initEvent(type, bubbles, cancelable);
        // Ignore the view parameter; we always use the window.
        setDetail(detail);
    }

    /**
     * @return a number that indicates which button was pressed on the mouse,
     * or the numeric keyCode or the character code (charCode) of the key pressed on the keyboard
     */
    @JsxGetter
    public int getWhich() {
        return 0;
    }
}
