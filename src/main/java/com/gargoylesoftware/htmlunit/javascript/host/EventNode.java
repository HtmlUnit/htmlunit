/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
     * Sets the onclick event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onclick(final Object handler) {
        setEventHandlerProp("onclick", handler);
    }

    /**
     * Gets the onclick event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onclick() {
        return getEventHandlerProp("onclick");
    }

    /**
     * Sets the ondblclick event handler for this element.
     * @param handler the new handler
     **/
    public void jsxSet_ondblclick(final Object handler) {
        setEventHandlerProp("ondblclick", handler);
    }

    /**
     * Gets the ondblclick event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_ondblclick() {
        return getEventHandlerProp("ondblclick");
    }

    /**
     * Sets the onblur event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onblur(final Object handler) {
        setEventHandlerProp("onblur", handler);
    }

    /**
     * Gets the onblur event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onblur() {
        return getEventHandlerProp("onblur");
    }

    /**
     * Sets the onfocus event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onfocus(final Object handler) {
        setEventHandlerProp("onfocus", handler);
    }

    /**
     * Gets the onfocus event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onfocus() {
        return getEventHandlerProp("onfocus");
    }

    /**
     * Sets the onfocusin event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onfocusin(final Object handler) {
        setEventHandlerProp("onfocusin", handler);
    }

    /**
     * Gets the onfocusin event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onfocusin() {
        return getEventHandlerProp("onfocusin");
    }

    /**
     * Sets the onfocusout event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onfocusout(final Object handler) {
        setEventHandlerProp("onfocusout", handler);
    }

    /**
     * Gets the onfocusout event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onfocusout() {
        return getEventHandlerProp("onfocusout");
    }

    /**
     * Sets the onkeydown event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onkeydown(final Object handler) {
        setEventHandlerProp("onkeydown", handler);
    }

    /**
     * Gets the onkeydown event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onkeydown() {
        return getEventHandlerProp("onkeydown");
    }

    /**
     * Sets the onkeypress event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onkeypress(final Object handler) {
        setEventHandlerProp("onkeypress", handler);
    }

    /**
     * Gets the onkeypress event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onkeypress() {
        return getEventHandlerProp("onkeypress");
    }

    /**
     * Sets the onkeyup event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onkeyup(final Object handler) {
        setEventHandlerProp("onkeyup", handler);
    }

    /**
     * Gets the onkeyup event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onkeyup() {
        return getEventHandlerProp("onkeyup");
    }

    /**
     * Sets the onmousedown event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmousedown(final Object handler) {
        setEventHandlerProp("onmousedown", handler);
    }

    /**
     * Gets the onmousedown event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmousedown() {
        return getEventHandlerProp("onmousedown");
    }

    /**
     * Sets the onmousemove event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmousemove(final Object handler) {
        setEventHandlerProp("onmousemove", handler);
    }

    /**
     * Gets the onmousemove event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmousemove() {
        return getEventHandlerProp("onmousemove");
    }

    /**
     * Sets the onmouseout event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmouseout(final Object handler) {
        setEventHandlerProp("onmouseout", handler);
    }

    /**
     * Gets the onmouseout event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmouseout() {
        return getEventHandlerProp("onmouseout");
    }

    /**
     * Sets the onmouseover event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmouseover(final Object handler) {
        setEventHandlerProp("onmouseover", handler);
    }

    /**
     * Gets the onmouseover event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmouseover() {
        return getEventHandlerProp("onmouseover");
    }

    /**
     * Sets the onmouseup event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onmouseup(final Object handler) {
        setEventHandlerProp("onmouseup", handler);
    }

    /**
     * Gets the onmouseup event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onmouseup() {
        return getEventHandlerProp("onmouseup");
    }

    /**
     * Sets the oncontextmenu event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_oncontextmenu(final Object handler) {
        setEventHandlerProp("oncontextmenu", handler);
    }

    /**
     * Gets the oncontextmenu event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_oncontextmenu() {
        return getEventHandlerProp("oncontextmenu");
    }

    /**
     * Sets the onresize event handler for this element.
     * @param handler the new handler
     */
    public void jsxSet_onresize(final Object handler) {
        setEventHandlerProp("onresize", handler);
    }

    /**
     * Gets the onresize event handler for this element.
     * @return <code>org.mozilla.javascript.Function</code>
     */
    public Object jsxGet_onresize() {
        return getEventHandlerProp("onresize");
    }

    /**
     * Fires a specified event on this element (IE only). See the
     * <a href="http://msdn2.microsoft.com/en-us/library/ms536423.aspx">MSDN documentation</a>
     * for more information.
     * @param cx the JavaScript context
     * @param thisObj the element instance on which this method was invoked
     * @param args contains the event type as a string, and an optional event template
     * @param f the function being invoked
     * @return <tt>true</tt> if the event fired successfully, <tt>false</tt> if it was cancelled
     */
    public static ScriptResult jsxFunction_fireEvent(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function f) {

        final EventNode me = (EventNode) thisObj;

        // Extract the function arguments.
        final String type = (String) args[0];
        final Event template;
        if (args.length > 1) {
            template = (Event) args[1];
        }
        else {
            template = null;
        }

        // Create the event, whose class will depend on the type specified.
        final Event event;
        final String cleanedType = StringUtils.removeStart(type.toLowerCase(), "on");
        if (MouseEvent.isMouseEvent(cleanedType)) {
            event = new MouseEvent();
            event.setPrototype(me.getPrototype(MouseEvent.class));
        }
        else {
            event = new Event();
            event.setPrototype(me.getPrototype(Event.class));
        }
        event.setParentScope(me.getWindow());

        // Initialize the event using the template, if provided.
        if (template != null) {
            event.copyPropertiesFrom(template);
        }

        // These four properties have predefined values, independent of the template.
        event.jsxSet_cancelBubble(false);
        event.jsxSet_returnValue(Boolean.TRUE);
        event.jsxSet_srcElement(me);
        event.setEventType(cleanedType);

        return me.fireEvent(event);
    }

}
