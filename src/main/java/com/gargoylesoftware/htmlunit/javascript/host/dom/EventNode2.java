/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;

/**
 * A node which supports all of the <tt>onXXX</tt> event handlers and other event-related functions.
 *
 * Basically contains any event-related features that both elements and the document support.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class EventNode2 extends Node2 {

    /**
     * Sets the {@code onclick} event handler for this element.
     * @param handler the {@code onclick} event handler for this element
     */
    @Setter
    public void setOnclick(final Object handler) {
        setEventHandler("click", handler);
    }

    /**
     * Returns the {@code onclick} event handler for this element.
     * @return the {@code onclick} event handler for this element
     */
    @Getter
    public Object getOnclick() {
        return getEventHandlerProp("onclick");
    }

    /**
     * Sets the {@code ondblclick} event handler for this element.
     * @param handler the {@code ondblclick} event handler for this element
     */
    @Setter
    public void setOndblclick(final Object handler) {
        setEventHandler("dblclick", handler);
    }

    /**
     * Returns the {@code ondblclick} event handler for this element.
     * @return the {@code ondblclick} event handler for this element
     */
    @Getter
    public Object getOndblclick() {
        return getEventHandlerProp("ondblclick");
    }

    /**
     * Sets the {@code onblur} event handler for this element.
     * @param handler the {@code onblur} event handler for this element
     */
    @Setter
    public void setOnblur(final Object handler) {
        setEventHandler("blur", handler);
    }

    /**
     * Returns the {@code onblur} event handler for this element.
     * @return the {@code onblur} event handler for this element
     */
    @Getter
    public Object getOnblur() {
        return getEventHandlerProp("onblur");
    }

    /**
     * Sets the {@code onfocus} event handler for this element.
     * @param handler the {@code onfocus} event handler for this element
     */
    @Setter
    public void setOnfocus(final Object handler) {
        setEventHandler("focus", handler);
    }

    /**
     * Returns the {@code onfocus} event handler for this element.
     * @return the {@code onfocus} event handler for this element
     */
    @Getter
    public Object getOnfocus() {
        return getEventHandlerProp("onfocus");
    }

    /**
     * Sets the {@code onfocusin} event handler for this element.
     * @param handler the {@code onfocusin} event handler for this element
     */
    @Setter(IE)
    public void setOnfocusin(final Object handler) {
        setEventHandler("focusin", handler);
    }

    /**
     * Returns the {@code onfocusin} event handler for this element.
     * @return the {@code onfocusin} event handler for this element
     */
    @Getter(IE)
    public Object getOnfocusin() {
        return getEventHandlerProp("onfocusin");
    }

    /**
     * Sets the {@code onfocusout} event handler for this element.
     * @param handler the {@code onfocusout} event handler for this element
     */
    @Setter(IE)
    public void setOnfocusout(final Object handler) {
        setEventHandler("focusout", handler);
    }

    /**
     * Returns the {@code onfocusout} event handler for this element.
     * @return the {@code onfocusout} event handler for this element
     */
    @Getter(IE)
    public Object getOnfocusout() {
        return getEventHandlerProp("onfocusout");
    }

    /**
     * Sets the {@code onkeydown} event handler for this element.
     * @param handler the {@code onkeydown} event handler for this element
     */
    @Setter
    public void setOnkeydown(final Object handler) {
        setEventHandler("keydown", handler);
    }

    /**
     * Returns the {@code onkeydown} event handler for this element.
     * @return the {@code onkeydown} event handler for this element
     */
    @Getter
    public Object getOnkeydown() {
        return getEventHandlerProp("onkeydown");
    }

    /**
     * Sets the {@code onkeypress} event handler for this element.
     * @param handler the {@code onkeypress} event handler for this element
     */
    @Setter
    public void setOnkeypress(final Object handler) {
        setEventHandler("keypress", handler);
    }

    /**
     * Returns the {@code onkeypress} event handler for this element.
     * @return the {@code onkeypress} event handler for this element
     */
    @Getter
    public Object getOnkeypress() {
        return getEventHandlerProp("onkeypress");
    }

    /**
     * Sets the {@code onkeyup} event handler for this element.
     * @param handler the {@code onkeyup} event handler for this element
     */
    @Setter
    public void setOnkeyup(final Object handler) {
        setEventHandler("keyup", handler);
    }

    /**
     * Returns the {@code onkeyup} event handler for this element.
     * @return the {@code onkeyup} event handler for this element
     */
    @Getter
    public Object getOnkeyup() {
        return getEventHandlerProp("onkeyup");
    }

    /**
     * Sets the {@code onmousedown} event handler for this element.
     * @param handler the {@code onmousedown} event handler for this element
     */
    @Setter
    public void setOnmousedown(final Object handler) {
        setEventHandler("mousedown", handler);
    }

    /**
     * Returns the {@code onmousedown} event handler for this element.
     * @return the {@code onmousedown} event handler for this element
     */
    @Getter
    public Object getOnmousedown() {
        return getEventHandlerProp("onmousedown");
    }

    /**
     * Sets the {@code onmousemove} event handler for this element.
     * @param handler the {@code onmousemove} event handler for this element
     */
    @Setter
    public void setOnmousemove(final Object handler) {
        setEventHandler("mousemove", handler);
    }

    /**
     * Returns the {@code onmousemove} event handler for this element.
     * @return the {@code onmousemove} event handler for this element
     */
    @Getter
    public Object getOnmousemove() {
        return getEventHandlerProp("onmousemove");
    }

    /**
     * Sets the {@code onmouseout} event handler for this element.
     * @param handler the {@code onmouseout} event handler for this element
     */
    @Setter
    public void setOnmouseout(final Object handler) {
        setEventHandler("mouseout", handler);
    }

    /**
     * Returns the {@code onmouseout} event handler for this element.
     * @return the {@code onmouseout} event handler for this element
     */
    @Getter
    public Object getOnmouseout() {
        return getEventHandlerProp("onmouseout");
    }

    /**
     * Sets the {@code onmouseover} event handler for this element.
     * @param handler the {@code onmouseover} event handler for this element
     */
    @Setter
    public void setOnmouseover(final Object handler) {
        setEventHandler("mouseover", handler);
    }

    /**
     * Returns the {@code onmouseover} event handler for this element.
     * @return the {@code onmouseover} event handler for this element
     */
    @Getter
    public Object getOnmouseover() {
        return getEventHandlerProp("onmouseover");
    }

    /**
     * Sets the {@code onmouseup} event handler for this element.
     * @param handler the {@code onmouseup} event handler for this element
     */
    @Setter
    public void setOnmouseup(final Object handler) {
        setEventHandler("mouseup", handler);
    }

    /**
     * Returns the {@code onmouseup} event handler for this element.
     * @return the {@code onmouseup} event handler for this element
     */
    @Getter
    public Object getOnmouseup() {
        return getEventHandlerProp("onmouseup");
    }

    /**
     * Sets the {@code oncontextmenu} event handler for this element.
     * @param handler the {@code oncontextmenu} event handler for this element
     */
    @Setter
    public void setOncontextmenu(final Object handler) {
        setEventHandler("contextmenu", handler);
    }

    /**
     * Returns the {@code oncontextmenu} event handler for this element.
     * @return the {@code oncontextmenu} event handler for this element
     */
    @Getter
    public Object getOncontextmenu() {
        return getEventHandlerProp("oncontextmenu");
    }

    /**
     * Sets the {@code onresize} event handler for this element.
     * @param handler the {@code onresize} event handler for this element
     */
    @Setter
    public void setOnresize(final Object handler) {
        setEventHandler("resize", handler);
    }

    /**
     * Returns the {@code onresize} event handler for this element.
     * @return the {@code onresize} event handler for this element
     */
    @Getter
    public Object getOnresize() {
        return getEventHandlerProp("onresize");
    }

    /**
     * Sets the {@code onpropertychange} event handler for this element.
     * @param handler the {@code onpropertychange} event handler for this element
     */
    @Setter(IE)
    public void setOnpropertychange(final Object handler) {
        setEventHandler("propertychange", handler);
    }

    /**
     * Returns the {@code onpropertychange} event handler for this element.
     * @return the {@code onpropertychange} event handler for this element
     */
    @Getter(IE)
    public Object getOnpropertychange() {
        return getEventHandlerProp("onpropertychange");
    }

    /**
     * Sets the {@code onerror} event handler for this element.
     * @param handler the {@code onerror} event handler for this element
     */
    @Setter
    public void setOnerror(final Object handler) {
        setEventHandler("error", handler);
    }

    /**
     * Returns the {@code onerror} event handler for this element.
     * @return the {@code onerror} event handler for this element
     */
    @Getter
    public Object getOnerror() {
        return getEventHandlerProp("onerror");
    }

    /**
     * Sets the {@code oninput} event handler for this element.
     * @param onchange the {@code oninput} event handler for this element
     */
    @Setter
    public void setOninput(final Object onchange) {
        setEventHandler("input", onchange);
    }

    /**
     * Returns the {@code oninput} event handler for this element.
     * @return the {@code oninput} event handler for this element
     */
    @Getter
    public Object getOninput() {
        return getEventHandler("input");
    }
}
