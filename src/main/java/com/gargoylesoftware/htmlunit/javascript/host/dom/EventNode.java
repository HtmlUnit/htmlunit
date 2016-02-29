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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;

import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * A node which supports all of the <tt>onXXX</tt> event handlers and other event-related functions.
 *
 * Basically contains any event-related features that both elements and the document support.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(isJSObject = false, isDefinedInStandardsMode = false)
public class EventNode extends Node {

    /**
     * Sets the {@code onclick} event handler for this element.
     * @param handler the {@code onclick} event handler for this element
     */
    @JsxSetter
    public void setOnclick(final Object handler) {
        setEventHandlerProp("onclick", handler);
    }

    /**
     * Returns the {@code onclick} event handler for this element.
     * @return the {@code onclick} event handler for this element
     */
    @JsxGetter
    public Object getOnclick() {
        return getEventHandlerProp("onclick");
    }

    /**
     * Sets the {@code ondblclick} event handler for this element.
     * @param handler the {@code ondblclick} event handler for this element
     */
    @JsxSetter
    public void setOndblclick(final Object handler) {
        setEventHandlerProp("ondblclick", handler);
    }

    /**
     * Returns the {@code ondblclick} event handler for this element.
     * @return the {@code ondblclick} event handler for this element
     */
    @JsxGetter
    public Object getOndblclick() {
        return getEventHandlerProp("ondblclick");
    }

    /**
     * Sets the {@code onblur} event handler for this element.
     * @param handler the {@code onblur} event handler for this element
     */
    @JsxSetter
    public void setOnblur(final Object handler) {
        setEventHandlerProp("onblur", handler);
    }

    /**
     * Returns the {@code onblur} event handler for this element.
     * @return the {@code onblur} event handler for this element
     */
    @JsxGetter
    public Object getOnblur() {
        return getEventHandlerProp("onblur");
    }

    /**
     * Sets the {@code onfocus} event handler for this element.
     * @param handler the {@code onfocus} event handler for this element
     */
    @JsxSetter
    public void setOnfocus(final Object handler) {
        setEventHandlerProp("onfocus", handler);
    }

    /**
     * Returns the {@code onfocus} event handler for this element.
     * @return the {@code onfocus} event handler for this element
     */
    @JsxGetter
    public Object getOnfocus() {
        return getEventHandlerProp("onfocus");
    }

    /**
     * Sets the {@code onfocusin} event handler for this element.
     * @param handler the {@code onfocusin} event handler for this element
     */
    @JsxSetter(@WebBrowser(IE))
    public void setOnfocusin(final Object handler) {
        setEventHandlerProp("onfocusin", handler);
    }

    /**
     * Returns the {@code onfocusin} event handler for this element.
     * @return the {@code onfocusin} event handler for this element
     */
    @JsxGetter(@WebBrowser(IE))
    public Object getOnfocusin() {
        return getEventHandlerProp("onfocusin");
    }

    /**
     * Sets the {@code onfocusout} event handler for this element.
     * @param handler the {@code onfocusout} event handler for this element
     */
    @JsxSetter(@WebBrowser(IE))
    public void setOnfocusout(final Object handler) {
        setEventHandlerProp("onfocusout", handler);
    }

    /**
     * Returns the {@code onfocusout} event handler for this element.
     * @return the {@code onfocusout} event handler for this element
     */
    @JsxGetter(@WebBrowser(IE))
    public Object getOnfocusout() {
        return getEventHandlerProp("onfocusout");
    }

    /**
     * Sets the {@code onkeydown} event handler for this element.
     * @param handler the {@code onkeydown} event handler for this element
     */
    @JsxSetter
    public void setOnkeydown(final Object handler) {
        setEventHandlerProp("onkeydown", handler);
    }

    /**
     * Returns the {@code onkeydown} event handler for this element.
     * @return the {@code onkeydown} event handler for this element
     */
    @JsxGetter
    public Object getOnkeydown() {
        return getEventHandlerProp("onkeydown");
    }

    /**
     * Sets the {@code onkeypress} event handler for this element.
     * @param handler the {@code onkeypress} event handler for this element
     */
    @JsxSetter
    public void setOnkeypress(final Object handler) {
        setEventHandlerProp("onkeypress", handler);
    }

    /**
     * Returns the {@code onkeypress} event handler for this element.
     * @return the {@code onkeypress} event handler for this element
     */
    @JsxGetter
    public Object getOnkeypress() {
        return getEventHandlerProp("onkeypress");
    }

    /**
     * Sets the {@code onkeyup} event handler for this element.
     * @param handler the {@code onkeyup} event handler for this element
     */
    @JsxSetter
    public void setOnkeyup(final Object handler) {
        setEventHandlerProp("onkeyup", handler);
    }

    /**
     * Returns the {@code onkeyup} event handler for this element.
     * @return the {@code onkeyup} event handler for this element
     */
    @JsxGetter
    public Object getOnkeyup() {
        return getEventHandlerProp("onkeyup");
    }

    /**
     * Sets the {@code onmousedown} event handler for this element.
     * @param handler the {@code onmousedown} event handler for this element
     */
    @JsxSetter
    public void setOnmousedown(final Object handler) {
        setEventHandlerProp("onmousedown", handler);
    }

    /**
     * Returns the {@code onmousedown} event handler for this element.
     * @return the {@code onmousedown} event handler for this element
     */
    @JsxGetter
    public Object getOnmousedown() {
        return getEventHandlerProp("onmousedown");
    }

    /**
     * Sets the {@code onmousemove} event handler for this element.
     * @param handler the {@code onmousemove} event handler for this element
     */
    @JsxSetter
    public void setOnmousemove(final Object handler) {
        setEventHandlerProp("onmousemove", handler);
    }

    /**
     * Returns the {@code onmousemove} event handler for this element.
     * @return the {@code onmousemove} event handler for this element
     */
    @JsxGetter
    public Object getOnmousemove() {
        return getEventHandlerProp("onmousemove");
    }

    /**
     * Sets the {@code onmouseout} event handler for this element.
     * @param handler the {@code onmouseout} event handler for this element
     */
    @JsxSetter
    public void setOnmouseout(final Object handler) {
        setEventHandlerProp("onmouseout", handler);
    }

    /**
     * Returns the {@code onmouseout} event handler for this element.
     * @return the {@code onmouseout} event handler for this element
     */
    @JsxGetter
    public Object getOnmouseout() {
        return getEventHandlerProp("onmouseout");
    }

    /**
     * Sets the {@code onmouseover} event handler for this element.
     * @param handler the {@code onmouseover} event handler for this element
     */
    @JsxSetter
    public void setOnmouseover(final Object handler) {
        setEventHandlerProp("onmouseover", handler);
    }

    /**
     * Returns the {@code onmouseover} event handler for this element.
     * @return the {@code onmouseover} event handler for this element
     */
    @JsxGetter
    public Object getOnmouseover() {
        return getEventHandlerProp("onmouseover");
    }

    /**
     * Sets the {@code onmouseup} event handler for this element.
     * @param handler the {@code onmouseup} event handler for this element
     */
    @JsxSetter
    public void setOnmouseup(final Object handler) {
        setEventHandlerProp("onmouseup", handler);
    }

    /**
     * Returns the {@code onmouseup} event handler for this element.
     * @return the {@code onmouseup} event handler for this element
     */
    @JsxGetter
    public Object getOnmouseup() {
        return getEventHandlerProp("onmouseup");
    }

    /**
     * Sets the {@code oncontextmenu} event handler for this element.
     * @param handler the {@code oncontextmenu} event handler for this element
     */
    @JsxSetter
    public void setOncontextmenu(final Object handler) {
        setEventHandlerProp("oncontextmenu", handler);
    }

    /**
     * Returns the {@code oncontextmenu} event handler for this element.
     * @return the {@code oncontextmenu} event handler for this element
     */
    @JsxGetter
    public Object getOncontextmenu() {
        return getEventHandlerProp("oncontextmenu");
    }

    /**
     * Sets the {@code onresize} event handler for this element.
     * @param handler the {@code onresize} event handler for this element
     */
    @JsxSetter
    public void setOnresize(final Object handler) {
        setEventHandlerProp("onresize", handler);
    }

    /**
     * Returns the {@code onresize} event handler for this element.
     * @return the {@code onresize} event handler for this element
     */
    @JsxGetter
    public Object getOnresize() {
        return getEventHandlerProp("onresize");
    }

    /**
     * Sets the {@code onpropertychange} event handler for this element.
     * @param handler the {@code onpropertychange} event handler for this element
     */
    @JsxSetter(@WebBrowser(IE))
    public void setOnpropertychange(final Object handler) {
        setEventHandlerProp("onpropertychange", handler);
    }

    /**
     * Returns the {@code onpropertychange} event handler for this element.
     * @return the {@code onpropertychange} event handler for this element
     */
    @JsxGetter(@WebBrowser(IE))
    public Object getOnpropertychange() {
        return getEventHandlerProp("onpropertychange");
    }

    /**
     * Sets the {@code onerror} event handler for this element.
     * @param handler the {@code onerror} event handler for this element
     */
    @JsxSetter
    public void setOnerror(final Object handler) {
        setEventHandlerProp("onerror", handler);
    }

    /**
     * Returns the {@code onerror} event handler for this element.
     * @return the {@code onerror} event handler for this element
     */
    @JsxGetter
    public Object getOnerror() {
        return getEventHandlerProp("onerror");
    }

    /**
     * Returns the {@code oninput} event handler for this element.
     * @return the {@code oninput} event handler for this element
     */
    @JsxGetter
    public Function getOninput() {
        return getEventHandler("oninput");
    }

    /**
     * Sets the {@code oninput} event handler for this element.
     * @param onchange the {@code oninput} event handler for this element
     */
    @JsxSetter
    public void setOninput(final Object onchange) {
        setEventHandlerProp("oninput", onchange);
    }

    /**
     * Fires a specified event on this element (IE only). See the
     * <a href="http://msdn.microsoft.com/en-us/library/ms536423.aspx">MSDN documentation</a>
     * for more information.
     * @param type specifies the name of the event to fire.
     * @param event specifies the event object from which to obtain event object properties.
     * @return {@code true} if the event fired successfully, {@code false} if it was canceled
     */
    public boolean fireEvent(final String type, Event event) {
        if (event == null) {
            event = new MouseEvent();
        }

        // Create the event, whose class will depend on the type specified.
        final String cleanedType = StringUtils.removeStart(type.toLowerCase(Locale.ROOT), "on");
        if (MouseEvent.isMouseEvent(cleanedType)) {
            event.setPrototype(getPrototype(MouseEvent.class));
        }
        else {
            event.setPrototype(getPrototype(Event.class));
        }
        event.setParentScope(getWindow());

        // These four properties have predefined values, independent of the template.
        event.setCancelBubble(false);
        event.setReturnValue(Boolean.TRUE);
        event.setSrcElement(this);
        event.setEventType(cleanedType);

        fireEvent(event);
        return ((Boolean) event.getReturnValue()).booleanValue();
    }

}
