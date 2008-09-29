/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.ScriptResult;

/**
 * A node which supports all of the <tt>onXXX</tt> event handlers and other event-related functions.
 *
 * Basically contains any event-related features that both elements and the document support.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class EventNode extends Node {

    /** Serial version UID. */
    private static final long serialVersionUID = 4894810197917509182L;

    /**
     * Sets the <tt>onclick</tt> event handler for this element.
     * @param handler the <tt>onclick</tt> event handler for this element
     */
    public void jsxSet_onclick(final Object handler) {
        setEventHandlerProp("onclick", handler);
    }

    /**
     * Returns the <tt>onclick</tt> event handler for this element.
     * @return the <tt>onclick</tt> event handler for this element
     */
    public Object jsxGet_onclick() {
        return getEventHandlerProp("onclick");
    }

    /**
     * Sets the <tt>ondblclick</tt> event handler for this element.
     * @param handler the <tt>ondblclick</tt> event handler for this element
     */
    public void jsxSet_ondblclick(final Object handler) {
        setEventHandlerProp("ondblclick", handler);
    }

    /**
     * Returns the <tt>ondblclick</tt> event handler for this element.
     * @return the <tt>ondblclick</tt> event handler for this element
     */
    public Object jsxGet_ondblclick() {
        return getEventHandlerProp("ondblclick");
    }

    /**
     * Sets the <tt>onblur</tt> event handler for this element.
     * @param handler the <tt>onblur</tt> event handler for this element
     */
    public void jsxSet_onblur(final Object handler) {
        setEventHandlerProp("onblur", handler);
    }

    /**
     * Returns the <tt>onblur</tt> event handler for this element.
     * @return the <tt>onblur</tt> event handler for this element
     */
    public Object jsxGet_onblur() {
        return getEventHandlerProp("onblur");
    }

    /**
     * Sets the <tt>onfocus</tt> event handler for this element.
     * @param handler the <tt>onfocus</tt> event handler for this element
     */
    public void jsxSet_onfocus(final Object handler) {
        setEventHandlerProp("onfocus", handler);
    }

    /**
     * Returns the <tt>onfocus</tt> event handler for this element.
     * @return the <tt>onfocus</tt> event handler for this element
     */
    public Object jsxGet_onfocus() {
        return getEventHandlerProp("onfocus");
    }

    /**
     * Sets the <tt>onfocusin</tt> event handler for this element.
     * @param handler the <tt>onfocusin</tt> event handler for this element
     */
    public void jsxSet_onfocusin(final Object handler) {
        setEventHandlerProp("onfocusin", handler);
    }

    /**
     * Returns the <tt>onfocusin</tt> event handler for this element.
     * @return the <tt>onfocusin</tt> event handler for this element
     */
    public Object jsxGet_onfocusin() {
        return getEventHandlerProp("onfocusin");
    }

    /**
     * Sets the <tt>onfocusout</tt> event handler for this element.
     * @param handler the <tt>onfocusout</tt> event handler for this element
     */
    public void jsxSet_onfocusout(final Object handler) {
        setEventHandlerProp("onfocusout", handler);
    }

    /**
     * Returns the <tt>onfocusout</tt> event handler for this element.
     * @return the <tt>onfocusout</tt> event handler for this element
     */
    public Object jsxGet_onfocusout() {
        return getEventHandlerProp("onfocusout");
    }

    /**
     * Sets the <tt>onkeydown</tt> event handler for this element.
     * @param handler the <tt>onkeydown</tt> event handler for this element
     */
    public void jsxSet_onkeydown(final Object handler) {
        setEventHandlerProp("onkeydown", handler);
    }

    /**
     * Returns the <tt>onkeydown</tt> event handler for this element.
     * @return the <tt>onkeydown</tt> event handler for this element
     */
    public Object jsxGet_onkeydown() {
        return getEventHandlerProp("onkeydown");
    }

    /**
     * Sets the <tt>onkeypress</tt> event handler for this element.
     * @param handler the <tt>onkeypress</tt> event handler for this element
     */
    public void jsxSet_onkeypress(final Object handler) {
        setEventHandlerProp("onkeypress", handler);
    }

    /**
     * Returns the <tt>onkeypress</tt> event handler for this element.
     * @return the <tt>onkeypress</tt> event handler for this element
     */
    public Object jsxGet_onkeypress() {
        return getEventHandlerProp("onkeypress");
    }

    /**
     * Sets the <tt>onkeyup</tt> event handler for this element.
     * @param handler the <tt>onkeyup</tt> event handler for this element
     */
    public void jsxSet_onkeyup(final Object handler) {
        setEventHandlerProp("onkeyup", handler);
    }

    /**
     * Returns the <tt>onkeyup</tt> event handler for this element.
     * @return the <tt>onkeyup</tt> event handler for this element
     */
    public Object jsxGet_onkeyup() {
        return getEventHandlerProp("onkeyup");
    }

    /**
     * Sets the <tt>onmousedown</tt> event handler for this element.
     * @param handler the <tt>onmousedown</tt> event handler for this element
     */
    public void jsxSet_onmousedown(final Object handler) {
        setEventHandlerProp("onmousedown", handler);
    }

    /**
     * Returns the <tt>onmousedown</tt> event handler for this element.
     * @return the <tt>onmousedown</tt> event handler for this element
     */
    public Object jsxGet_onmousedown() {
        return getEventHandlerProp("onmousedown");
    }

    /**
     * Sets the <tt>onmousemove</tt> event handler for this element.
     * @param handler the <tt>onmousemove</tt> event handler for this element
     */
    public void jsxSet_onmousemove(final Object handler) {
        setEventHandlerProp("onmousemove", handler);
    }

    /**
     * Returns the <tt>onmousemove</tt> event handler for this element.
     * @return the <tt>onmousemove</tt> event handler for this element
     */
    public Object jsxGet_onmousemove() {
        return getEventHandlerProp("onmousemove");
    }

    /**
     * Sets the <tt>onmouseout</tt> event handler for this element.
     * @param handler the <tt>onmouseout</tt> event handler for this element
     */
    public void jsxSet_onmouseout(final Object handler) {
        setEventHandlerProp("onmouseout", handler);
    }

    /**
     * Returns the <tt>onmouseout</tt> event handler for this element.
     * @return the <tt>onmouseout</tt> event handler for this element
     */
    public Object jsxGet_onmouseout() {
        return getEventHandlerProp("onmouseout");
    }

    /**
     * Sets the <tt>onmouseover</tt> event handler for this element.
     * @param handler the <tt>onmouseover</tt> event handler for this element
     */
    public void jsxSet_onmouseover(final Object handler) {
        setEventHandlerProp("onmouseover", handler);
    }

    /**
     * Returns the <tt>onmouseover</tt> event handler for this element.
     * @return the <tt>onmouseover</tt> event handler for this element
     */
    public Object jsxGet_onmouseover() {
        return getEventHandlerProp("onmouseover");
    }

    /**
     * Sets the <tt>onmouseup</tt> event handler for this element.
     * @param handler the <tt>onmouseup</tt> event handler for this element
     */
    public void jsxSet_onmouseup(final Object handler) {
        setEventHandlerProp("onmouseup", handler);
    }

    /**
     * Returns the <tt>onmouseup</tt> event handler for this element.
     * @return the <tt>onmouseup</tt> event handler for this element
     */
    public Object jsxGet_onmouseup() {
        return getEventHandlerProp("onmouseup");
    }

    /**
     * Sets the <tt>oncontextmenu</tt> event handler for this element.
     * @param handler the <tt>oncontextmenu</tt> event handler for this element
     */
    public void jsxSet_oncontextmenu(final Object handler) {
        setEventHandlerProp("oncontextmenu", handler);
    }

    /**
     * Returns the <tt>oncontextmenu</tt> event handler for this element.
     * @return the <tt>oncontextmenu</tt> event handler for this element
     */
    public Object jsxGet_oncontextmenu() {
        return getEventHandlerProp("oncontextmenu");
    }

    /**
     * Sets the <tt>onresize</tt> event handler for this element.
     * @param handler the <tt>onresize</tt> event handler for this element
     */
    public void jsxSet_onresize(final Object handler) {
        setEventHandlerProp("onresize", handler);
    }

    /**
     * Returns the <tt>onresize</tt> event handler for this element.
     * @return the <tt>onresize</tt> event handler for this element
     */
    public Object jsxGet_onresize() {
        return getEventHandlerProp("onresize");
    }

    /**
     * Sets the <tt>onpropertychange</tt> event handler for this element.
     * @param handler the <tt>onpropertychange</tt> event handler for this element
     */
    public void jsxSet_onpropertychange(final Object handler) {
        setEventHandlerProp("onpropertychange", handler);
    }

    /**
     * Returns the <tt>onpropertychange</tt> event handler for this element.
     * @return the <tt>onpropertychange</tt> event handler for this element
     */
    public Object jsxGet_onpropertychange() {
        return getEventHandlerProp("onpropertychange");
    }

    /**
     * Fires a specified event on this element (IE only). See the
     * <a href="http://msdn.microsoft.com/en-us/library/ms536423.aspx">MSDN documentation</a>
     * for more information.
     * @param cx the JavaScript context
     * @param thisObj the element instance on which this method was invoked
     * @param args contains the event type as a string, and an optional event template
     * @param f the function being invoked
     * @return <tt>true</tt> if the event fired successfully, <tt>false</tt> if it was canceled
     */
    public static ScriptResult jsxFunction_fireEvent(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function f) {

        final String type = (String) args[0];

        final EventNode me = (EventNode) thisObj;

        // Extract the function arguments.
        final Event event;
        if (args.length > 1) {
            event = (Event) args[1];
        }
        else {
            event = new MouseEvent();
        }

        // Create the event, whose class will depend on the type specified.
        final String cleanedType = StringUtils.removeStart(type.toLowerCase(), "on");
        if (MouseEvent.isMouseEvent(cleanedType)) {
            event.setPrototype(me.getPrototype(MouseEvent.class));
        }
        else {
            event.setPrototype(me.getPrototype(Event.class));
        }
        event.setParentScope(me.getWindow());

        // These four properties have predefined values, independent of the template.
        event.jsxSet_cancelBubble(false);
        event.jsxSet_returnValue(Boolean.TRUE);
        event.jsxSet_srcElement(me);
        event.setEventType(cleanedType);

        return me.fireEvent(event);
    }

}
