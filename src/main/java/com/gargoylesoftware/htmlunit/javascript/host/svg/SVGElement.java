/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.svg.SvgElement;

import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * A JavaScript object for {@code SVGElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = SvgElement.class)
public class SVGElement extends Element {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public SVGElement() {
    }

    /**
     * Returns the bounding box, in current user space, of the geometry of all contained graphics elements.
     * @return the bounding box
     */
    protected SVGRect getBBox() {
        final SVGRect rect = new SVGRect();
        rect.setParentScope(getParentScope());
        rect.setPrototype(getPrototype(rect.getClass()));
        return rect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public CSSStyleDeclaration getStyle() {
        return super.getStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setStyle(final String style) {
        super.setStyle(style);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(IE)
    public String getId() {
        return super.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(IE)
    public void setId(final String newId) {
        super.setId(newId);
    }

    /**
     * Returns the {@code onfocus} event handler.
     * @return the {@code onfocus} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnfocus() {
        return getEventHandler(Event.TYPE_FOCUS);
    }

    /**
     * Sets the {@code onfocus} event handler.
     * @param focus the {@code onfocus} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnfocus(final Object focus) {
        setEventHandler(Event.TYPE_FOCUS, focus);
    }

    /**
     * Returns the {@code ondragend} event handler.
     * @return the {@code ondragend} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndragend() {
        return getEventHandler(Event.TYPE_DRAGEND);
    }

    /**
     * Sets the {@code ondragend} event handler.
     * @param dragend the {@code ondragend} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndragend(final Object dragend) {
        setEventHandler(Event.TYPE_DRAGEND, dragend);
    }

    /**
     * Returns the {@code oninvalid} event handler.
     * @return the {@code oninvalid} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOninvalid() {
        return getEventHandler(Event.TYPE_INVALID);
    }

    /**
     * Sets the {@code oninvalid} event handler.
     * @param invalid the {@code oninvalid} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOninvalid(final Object invalid) {
        setEventHandler(Event.TYPE_INVALID, invalid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointercancel() {
        return getEventHandler(Event.TYPE_POINTERCANCEL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointercancel(final Object pointercancel) {
        setEventHandler(Event.TYPE_POINTERCANCEL, pointercancel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerout() {
        return getEventHandler(Event.TYPE_POINTEROUT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerout(final Object pointerout) {
        setEventHandler(Event.TYPE_POINTEROUT, pointerout);
    }

    /**
     * Returns the {@code onratechange} event handler.
     * @return the {@code onratechange} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnratechange() {
        return getEventHandler(Event.TYPE_RATECHANGE);
    }

    /**
     * Sets the {@code onratechange} event handler.
     * @param ratechange the {@code onratechange} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnratechange(final Object ratechange) {
        setEventHandler(Event.TYPE_RATECHANGE, ratechange);
    }

    /**
     * Returns the {@code onresize} event handler.
     * @return the {@code onresize} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnresize() {
        return getEventHandler(Event.TYPE_RESIZE);
    }

    /**
     * Sets the {@code onresize} event handler.
     * @param resize the {@code onresize} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnresize(final Object resize) {
        setEventHandler(Event.TYPE_RESIZE, resize);
    }

    /**
     * Returns the {@code oncanplaythrough} event handler.
     * @return the {@code oncanplaythrough} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOncanplaythrough() {
        return getEventHandler(Event.TYPE_CANPLAYTHROUGH);
    }

    /**
     * Sets the {@code oncanplaythrough} event handler.
     * @param canplaythrough the {@code oncanplaythrough} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOncanplaythrough(final Object canplaythrough) {
        setEventHandler(Event.TYPE_CANPLAYTHROUGH, canplaythrough);
    }

    /**
     * Returns the {@code oncancel} event handler.
     * @return the {@code oncancel} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncancel() {
        return getEventHandler(Event.TYPE_CANCEL);
    }

    /**
     * Sets the {@code oncancel} event handler.
     * @param cancel the {@code oncancel} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncancel(final Object cancel) {
        setEventHandler(Event.TYPE_CANCEL, cancel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerenter() {
        return getEventHandler(Event.TYPE_POINTERENTER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerenter(final Object pointerenter) {
        setEventHandler(Event.TYPE_POINTERENTER, pointerenter);
    }

    /**
     * Returns the {@code onselect} event handler.
     * @return the {@code onselect} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnselect() {
        return getEventHandler(Event.TYPE_SELECT);
    }

    /**
     * Sets the {@code onselect} event handler.
     * @param select the {@code onselect} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnselect(final Object select) {
        setEventHandler(Event.TYPE_SELECT, select);
    }

    /**
     * Returns the {@code onauxclick} event handler.
     * @return the {@code onauxclick} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnauxclick() {
        return getEventHandler(Event.TYPE_AUXCLICK);
    }

    /**
     * Sets the {@code onauxclick} event handler.
     * @param auxclick the {@code onauxclick} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnauxclick(final Object auxclick) {
        setEventHandler(Event.TYPE_AUXCLICK, auxclick);
    }

    /**
     * Returns the {@code onpause} event handler.
     * @return the {@code onpause} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpause() {
        return getEventHandler(Event.TYPE_PAUSE);
    }

    /**
     * Sets the {@code onpause} event handler.
     * @param pause the {@code onpause} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpause(final Object pause) {
        setEventHandler(Event.TYPE_PAUSE, pause);
    }

    /**
     * Returns the {@code onloadstart} event handler.
     * @return the {@code onloadstart} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnloadstart() {
        return getEventHandler(Event.TYPE_LOAD_START);
    }

    /**
     * Sets the {@code onloadstart} event handler.
     * @param loadstart the {@code onloadstart} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnloadstart(final Object loadstart) {
        setEventHandler(Event.TYPE_LOAD_START, loadstart);
    }

    /**
     * Returns the {@code onprogress} event handler.
     * @return the {@code onprogress} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnprogress() {
        return getEventHandler(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the {@code onprogress} event handler.
     * @param progress the {@code onprogress} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnprogress(final Object progress) {
        setEventHandler(Event.TYPE_PROGRESS, progress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerup() {
        return getEventHandler(Event.TYPE_POINTERUP);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerup(final Object pointerup) {
        setEventHandler(Event.TYPE_POINTERUP, pointerup);
    }

    /**
     * Returns the {@code onscroll} event handler.
     * @return the {@code onscroll} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnscroll() {
        return getEventHandler(Event.TYPE_SCROLL);
    }

    /**
     * Sets the {@code onscroll} event handler.
     * @param scroll the {@code onscroll} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnscroll(final Object scroll) {
        setEventHandler(Event.TYPE_SCROLL, scroll);
    }

    /**
     * Returns the {@code onkeydown} event handler.
     * @return the {@code onkeydown} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnkeydown() {
        return getEventHandler(Event.TYPE_KEY_DOWN);
    }

    /**
     * Sets the {@code onkeydown} event handler.
     * @param keydown the {@code onkeydown} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnkeydown(final Object keydown) {
        setEventHandler(Event.TYPE_KEY_DOWN, keydown);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerleave() {
        return getEventHandler(Event.TYPE_POINTERLEAVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerleave(final Object pointerleave) {
        setEventHandler(Event.TYPE_POINTERLEAVE, pointerleave);
    }

    /**
     * Returns the {@code onclick} event handler.
     * @return the {@code onclick} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnclick() {
        return getEventHandler(MouseEvent.TYPE_CLICK);
    }

    /**
     * Sets the {@code onclick} event handler.
     * @param click the {@code onclick} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnclick(final Object click) {
        setEventHandler(MouseEvent.TYPE_CLICK, click);
    }

    /**
     * Returns the {@code onkeyup} event handler.
     * @return the {@code onkeyup} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnkeyup() {
        return getEventHandler(Event.TYPE_KEY_UP);
    }

    /**
     * Sets the {@code onkeyup} event handler.
     * @param keyup the {@code onkeyup} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnkeyup(final Object keyup) {
        setEventHandler(Event.TYPE_KEY_UP, keyup);
    }

    /**
     * Returns the {@code onchange} event handler.
     * @return the {@code onchange} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnchange() {
        return getEventHandler(Event.TYPE_CHANGE);
    }

    /**
     * Sets the {@code onchange} event handler.
     * @param change the {@code onchange} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnchange(final Object change) {
        setEventHandler(Event.TYPE_CHANGE, change);
    }

    /**
     * Returns the {@code onreset} event handler.
     * @return the {@code onreset} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnreset() {
        return getEventHandler(Event.TYPE_RESET);
    }

    /**
     * Sets the {@code onreset} event handler.
     * @param reset the {@code onreset} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnreset(final Object reset) {
        setEventHandler(Event.TYPE_RESET, reset);
    }

    /**
     * Returns the {@code onkeypress} event handler.
     * @return the {@code onkeypress} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnkeypress() {
        return getEventHandler(Event.TYPE_KEY_PRESS);
    }

    /**
     * Sets the {@code onkeypress} event handler.
     * @param keypress the {@code onkeypress} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnkeypress(final Object keypress) {
        setEventHandler(Event.TYPE_KEY_PRESS, keypress);
    }

    /**
     * Returns the {@code ontimeupdate} event handler.
     * @return the {@code ontimeupdate} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOntimeupdate() {
        return getEventHandler(Event.TYPE_TIMEUPDATE);
    }

    /**
     * Sets the {@code ontimeupdate} event handler.
     * @param timeupdate the {@code ontimeupdate} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOntimeupdate(final Object timeupdate) {
        setEventHandler(Event.TYPE_TIMEUPDATE, timeupdate);
    }

    /**
     * Returns the {@code ondblclick} event handler.
     * @return the {@code ondblclick} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndblclick() {
        return getEventHandler(MouseEvent.TYPE_DBL_CLICK);
    }

    /**
     * Sets the {@code ondblclick} event handler.
     * @param dblclick the {@code ondblclick} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndblclick(final Object dblclick) {
        setEventHandler(MouseEvent.TYPE_DBL_CLICK, dblclick);
    }

    /**
     * Returns the {@code ondrag} event handler.
     * @return the {@code ondrag} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndrag() {
        return getEventHandler(Event.TYPE_DRAG);
    }

    /**
     * Sets the {@code ondrag} event handler.
     * @param drag the {@code ondrag} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndrag(final Object drag) {
        setEventHandler(Event.TYPE_DRAG, drag);
    }

    /**
     * Returns the {@code onseeked} event handler.
     * @return the {@code onseeked} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnseeked() {
        return getEventHandler(Event.TYPE_SEEKED);
    }

    /**
     * Sets the {@code onseeked} event handler.
     * @param seeked the {@code onseeked} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnseeked(final Object seeked) {
        setEventHandler(Event.TYPE_SEEKED, seeked);
    }

    /**
     * Returns the {@code onabort} event handler.
     * @return the {@code onabort} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnabort() {
        return getEventHandler(Event.TYPE_ABORT);
    }

    /**
     * Sets the {@code onabort} event handler.
     * @param abort the {@code onabort} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnabort(final Object abort) {
        setEventHandler(Event.TYPE_ABORT, abort);
    }

    /**
     * Returns the {@code onloadedmetadata} event handler.
     * @return the {@code onloadedmetadata} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnloadedmetadata() {
        return getEventHandler(Event.TYPE_LOADEDMETADATA);
    }

    /**
     * Sets the {@code onloadedmetadata} event handler.
     * @param loadedmetadata the {@code onloadedmetadata} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnloadedmetadata(final Object loadedmetadata) {
        setEventHandler(Event.TYPE_LOADEDMETADATA, loadedmetadata);
    }

    /**
     * Returns the {@code ontoggle} event handler.
     * @return the {@code ontoggle} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOntoggle() {
        return getEventHandler(Event.TYPE_TOGGLE);
    }

    /**
     * Sets the {@code ontoggle} event handler.
     * @param toggle the {@code ontoggle} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOntoggle(final Object toggle) {
        setEventHandler(Event.TYPE_TOGGLE, toggle);
    }

    /**
     * Returns the {@code onplay} event handler.
     * @return the {@code onplay} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnplay() {
        return getEventHandler(Event.TYPE_PLAY);
    }

    /**
     * Sets the {@code onplay} event handler.
     * @param play the {@code onplay} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnplay(final Object play) {
        setEventHandler(Event.TYPE_PLAY, play);
    }

    /**
     * Returns the {@code oncontextmenu} event handler.
     * @return the {@code oncontextmenu} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOncontextmenu() {
        return getEventHandler(MouseEvent.TYPE_CONTEXT_MENU);
    }

    /**
     * Sets the {@code oncontextmenu} event handler.
     * @param contextmenu the {@code oncontextmenu} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOncontextmenu(final Object contextmenu) {
        setEventHandler(MouseEvent.TYPE_CONTEXT_MENU, contextmenu);
    }

    /**
     * Returns the {@code onmousemove} event handler.
     * @return the {@code onmousemove} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmousemove() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_MOVE);
    }

    /**
     * Sets the {@code onmousemove} event handler.
     * @param mousemove the {@code onmousemove} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnmousemove(final Object mousemove) {
        setEventHandler(MouseEvent.TYPE_MOUSE_MOVE, mousemove);
    }

    /**
     * Returns the {@code onerror} event handler.
     * @return the {@code onerror} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Sets the {@code onerror} event handler.
     * @param error the {@code onerror} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnerror(final Object error) {
        setEventHandler(Event.TYPE_ERROR, error);
    }

    /**
     * Returns the {@code onmouseup} event handler.
     * @return the {@code onmouseup} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmouseup() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_UP);
    }

    /**
     * Sets the {@code onmouseup} event handler.
     * @param mouseup the {@code onmouseup} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnmouseup(final Object mouseup) {
        setEventHandler(MouseEvent.TYPE_MOUSE_UP, mouseup);
    }

    /**
     * Returns the {@code ondragover} event handler.
     * @return the {@code ondragover} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndragover() {
        return getEventHandler(Event.TYPE_DRAGOVER);
    }

    /**
     * Sets the {@code ondragover} event handler.
     * @param dragover the {@code ondragover} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndragover(final Object dragover) {
        setEventHandler(Event.TYPE_DRAGOVER, dragover);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointermove() {
        return getEventHandler(Event.TYPE_POINTERMOVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointermove(final Object pointermove) {
        setEventHandler(Event.TYPE_POINTERMOVE, pointermove);
    }

    /**
     * Returns the {@code oncut} event handler.
     * @return the {@code oncut} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOncut() {
        return getEventHandler(Event.TYPE_CUT);
    }

    /**
     * Sets the {@code oncut} event handler.
     * @param cut the {@code oncut} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOncut(final Object cut) {
        setEventHandler(Event.TYPE_CUT, cut);
    }

    /**
     * Returns the {@code onmouseover} event handler.
     * @return the {@code onmouseover} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmouseover() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OVER);
    }

    /**
     * Sets the {@code onmouseover} event handler.
     * @param mouseover the {@code onmouseover} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnmouseover(final Object mouseover) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OVER, mouseover);
    }

    /**
     * Returns the {@code oninput} event handler.
     * @return the {@code oninput} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOninput() {
        return getEventHandler(Event.TYPE_INPUT);
    }

    /**
     * Sets the {@code oninput} event handler.
     * @param input the {@code oninput} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOninput(final Object input) {
        setEventHandler(Event.TYPE_INPUT, input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnlostpointercapture() {
        return getEventHandler(Event.TYPE_LOSTPOINTERCAPTURE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnlostpointercapture(final Object lostpointercapture) {
        setEventHandler(Event.TYPE_LOSTPOINTERCAPTURE, lostpointercapture);
    }

    /**
     * Returns the {@code onstalled} event handler.
     * @return the {@code onstalled} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnstalled() {
        return getEventHandler(Event.TYPE_STALLED);
    }

    /**
     * Sets the {@code onstalled} event handler.
     * @param stalled the {@code onstalled} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnstalled(final Object stalled) {
        setEventHandler(Event.TYPE_STALLED, stalled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerover() {
        return getEventHandler(Event.TYPE_POINTEROVER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerover(final Object pointerover) {
        setEventHandler(Event.TYPE_POINTEROVER, pointerover);
    }

    /**
     * Returns the {@code onclose} event handler.
     * @return the {@code onclose} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnclose() {
        return getEventHandler(Event.TYPE_CLOSE);
    }

    /**
     * Sets the {@code onclose} event handler.
     * @param close the {@code onclose} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnclose(final Object close) {
        setEventHandler(Event.TYPE_CLOSE, close);
    }

    /**
     * Returns the {@code ondragenter} event handler.
     * @return the {@code ondragenter} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndragenter() {
        return getEventHandler(Event.TYPE_DRAGENTER);
    }

    /**
     * Sets the {@code ondragenter} event handler.
     * @param dragenter the {@code ondragenter} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndragenter(final Object dragenter) {
        setEventHandler(Event.TYPE_DRAGENTER, dragenter);
    }

    /**
     * Returns the {@code onmozfullscreenerror} event handler.
     * @return the {@code onmozfullscreenerror} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOnmozfullscreenerror() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENERROR);
    }

    /**
     * Sets the {@code onmozfullscreenerror} event handler.
     * @param mozfullscreenerror the {@code onmozfullscreenerror} event handler
     */
    @JsxSetter({FF, FF78})
    public void setOnmozfullscreenerror(final Object mozfullscreenerror) {
        setEventHandler(Event.TYPE_MOZFULLSCREENERROR, mozfullscreenerror);
    }

    /**
     * Returns the {@code onsubmit} event handler.
     * @return the {@code onsubmit} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnsubmit() {
        return getEventHandler(Event.TYPE_SUBMIT);
    }

    /**
     * Sets the {@code onsubmit} event handler.
     * @param submit the {@code onsubmit} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnsubmit(final Object submit) {
        setEventHandler(Event.TYPE_SUBMIT, submit);
    }

    /**
     * Returns the {@code onmouseleave} event handler.
     * @return the {@code onmouseleave} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmouseleave() {
        return getEventHandler(Event.TYPE_MOUSELEAVE);
    }

    /**
     * Sets the {@code onmouseleave} event handler.
     * @param mouseleave the {@code onmouseleave} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnmouseleave(final Object mouseleave) {
        setEventHandler(Event.TYPE_MOUSELEAVE, mouseleave);
    }

    /**
     * Returns the {@code onmouseenter} event handler.
     * @return the {@code onmouseenter} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmouseenter() {
        return getEventHandler(Event.TYPE_MOUDEENTER);
    }

    /**
     * Sets the {@code onmouseenter} event handler.
     * @param mouseenter the {@code onmouseenter} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnmouseenter(final Object mouseenter) {
        setEventHandler(Event.TYPE_MOUDEENTER, mouseenter);
    }

    /**
     * Returns the {@code ondragleave} event handler.
     * @return the {@code ondragleave} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndragleave() {
        return getEventHandler(Event.TYPE_DRAGLEAVE);
    }

    /**
     * Sets the {@code ondragleave} event handler.
     * @param dragleave the {@code ondragleave} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndragleave(final Object dragleave) {
        setEventHandler(Event.TYPE_DRAGLEAVE, dragleave);
    }

    /**
     * Returns the {@code onmousewheel} event handler.
     * @return the {@code onmousewheel} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnmousewheel() {
        return getEventHandler(Event.TYPE_MOUSEWHEEL);
    }

    /**
     * Sets the {@code onmousewheel} event handler.
     * @param mousewheel the {@code onmousewheel} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnmousewheel(final Object mousewheel) {
        setEventHandler(Event.TYPE_MOUSEWHEEL, mousewheel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnpointerdown() {
        return getEventHandler(Event.TYPE_POINTERDOWN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnpointerdown(final Object pointerdown) {
        setEventHandler(Event.TYPE_POINTERDOWN, pointerdown);
    }

    /**
     * Returns the {@code onseeking} event handler.
     * @return the {@code onseeking} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnseeking() {
        return getEventHandler(Event.TYPE_SEEKING);
    }

    /**
     * Sets the {@code onseeking} event handler.
     * @param seeking the {@code onseeking} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnseeking(final Object seeking) {
        setEventHandler(Event.TYPE_SEEKING, seeking);
    }

    /**
     * Returns the {@code onblur} event handler.
     * @return the {@code onblur} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnblur() {
        return getEventHandler(Event.TYPE_BLUR);
    }

    /**
     * Sets the {@code onblur} event handler.
     * @param blur the {@code onblur} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnblur(final Object blur) {
        setEventHandler(Event.TYPE_BLUR, blur);
    }

    /**
     * Returns the {@code oncuechange} event handler.
     * @return the {@code oncuechange} event handler
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncuechange() {
        return getEventHandler(Event.TYPE_CUECHANGE);
    }

    /**
     * Sets the {@code oncuechange} event handler.
     * @param cuechange the {@code oncuechange} event handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncuechange(final Object cuechange) {
        setEventHandler(Event.TYPE_CUECHANGE, cuechange);
    }

    /**
     * Returns the {@code ondrop} event handler.
     * @return the {@code ondrop} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndrop() {
        return getEventHandler(Event.TYPE_DROP);
    }

    /**
     * Sets the {@code ondrop} event handler.
     * @param drop the {@code ondrop} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndrop(final Object drop) {
        setEventHandler(Event.TYPE_DROP, drop);
    }

    /**
     * Returns the {@code ondragstart} event handler.
     * @return the {@code ondragstart} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndragstart() {
        return getEventHandler(Event.TYPE_DRAGSTART);
    }

    /**
     * Sets the {@code ondragstart} event handler.
     * @param dragstart the {@code ondragstart} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndragstart(final Object dragstart) {
        setEventHandler(Event.TYPE_DRAGSTART, dragstart);
    }

    /**
     * Returns the {@code onmozfullscreenchange} event handler.
     * @return the {@code onmozfullscreenchange} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOnmozfullscreenchange() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onmozfullscreenchange} event handler.
     * @param mozfullscreenchange the {@code onmozfullscreenchange} event handler
     */
    @JsxSetter({FF, FF78})
    public void setOnmozfullscreenchange(final Object mozfullscreenchange) {
        setEventHandler(Event.TYPE_MOZFULLSCREENCHANGE, mozfullscreenchange);
    }

    /**
     * Returns the {@code ondurationchange} event handler.
     * @return the {@code ondurationchange} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOndurationchange() {
        return getEventHandler(Event.TYPE_DURATIONCHANGE);
    }

    /**
     * Sets the {@code ondurationchange} event handler.
     * @param durationchange the {@code ondurationchange} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOndurationchange(final Object durationchange) {
        setEventHandler(Event.TYPE_DURATIONCHANGE, durationchange);
    }

    /**
     * Returns the {@code onplaying} event handler.
     * @return the {@code onplaying} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnplaying() {
        return getEventHandler(Event.TYPE_PLAYNG);
    }

    /**
     * Sets the {@code onplaying} event handler.
     * @param playing the {@code onplaying} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnplaying(final Object playing) {
        setEventHandler(Event.TYPE_PLAYNG, playing);
    }

    /**
     * Returns the {@code onload} event handler.
     * @return the {@code onload} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnload() {
        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the {@code onload} event handler.
     * @param load the {@code onload} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnload(final Object load) {
        setEventHandler(Event.TYPE_LOAD, load);
    }

    /**
     * Returns the {@code onended} event handler.
     * @return the {@code onended} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnended() {
        return getEventHandler(Event.TYPE_ENDED);
    }

    /**
     * Sets the {@code onended} event handler.
     * @param ended the {@code onended} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnended(final Object ended) {
        setEventHandler(Event.TYPE_ENDED, ended);
    }

    /**
     * Returns the {@code onloadeddata} event handler.
     * @return the {@code onloadeddata} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnloadeddata() {
        return getEventHandler(Event.TYPE_LOADEDDATA);
    }

    /**
     * Sets the {@code onloadeddata} event handler.
     * @param loadeddata the {@code onloadeddata} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnloadeddata(final Object loadeddata) {
        setEventHandler(Event.TYPE_LOADEDDATA, loadeddata);
    }

    /**
     * Returns the {@code onshow} event handler.
     * @return the {@code onshow} event handler
     */
    @JsxGetter(FF78)
    public Function getOnshow() {
        return getEventHandler(Event.TYPE_SHOW);
    }

    /**
     * Sets the {@code onshow} event handler.
     * @param show the {@code onshow} event handler
     */
    @JsxSetter(FF78)
    public void setOnshow(final Object show) {
        setEventHandler(Event.TYPE_SHOW, show);
    }

    /**
     * Returns the {@code oncopy} event handler.
     * @return the {@code oncopy} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOncopy() {
        return getEventHandler(Event.TYPE_COPY);
    }

    /**
     * Sets the {@code oncopy} event handler.
     * @param copy the {@code oncopy} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOncopy(final Object copy) {
        setEventHandler(Event.TYPE_COPY, copy);
    }

    /**
     * Returns the {@code onpaste} event handler.
     * @return the {@code onpaste} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpaste() {
        return getEventHandler(Event.TYPE_PASTE);
    }

    /**
     * Sets the {@code onpaste} event handler.
     * @param paste the {@code onpaste} event handler
     */
    @JsxSetter({FF, FF78})
    public void setOnpaste(final Object paste) {
        setEventHandler(Event.TYPE_PASTE, paste);
    }

    /**
     * Returns the {@code onmouseout} event handler.
     * @return the {@code onmouseout} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmouseout() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OUT);
    }

    /**
     * Sets the {@code onmouseout} event handler.
     * @param mouseout the {@code onmouseout} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnmouseout(final Object mouseout) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OUT, mouseout);
    }

    /**
     * Returns the {@code onsuspend} event handler.
     * @return the {@code onsuspend} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnsuspend() {
        return getEventHandler(Event.TYPE_SUSPEND);
    }

    /**
     * Sets the {@code onsuspend} event handler.
     * @param suspend the {@code onsuspend} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnsuspend(final Object suspend) {
        setEventHandler(Event.TYPE_SUSPEND, suspend);
    }

    /**
     * Returns the {@code onvolumechange} event handler.
     * @return the {@code onvolumechange} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnvolumechange() {
        return getEventHandler(Event.TYPE_VOLUMECHANGE);
    }

    /**
     * Sets the {@code onvolumechange} event handler.
     * @param volumechange the {@code onvolumechange} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnvolumechange(final Object volumechange) {
        setEventHandler(Event.TYPE_VOLUMECHANGE, volumechange);
    }

    /**
     * Returns the {@code onwaiting} event handler.
     * @return the {@code onwaiting} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnwaiting() {
        return getEventHandler(Event.TYPE_WAITING);
    }

    /**
     * Sets the {@code onwaiting} event handler.
     * @param waiting the {@code onwaiting} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnwaiting(final Object waiting) {
        setEventHandler(Event.TYPE_WAITING, waiting);
    }

    /**
     * Returns the {@code oncanplay} event handler.
     * @return the {@code oncanplay} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOncanplay() {
        return getEventHandler(Event.TYPE_CANPLAY);
    }

    /**
     * Sets the {@code oncanplay} event handler.
     * @param canplay the {@code oncanplay} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOncanplay(final Object canplay) {
        setEventHandler(Event.TYPE_CANPLAY, canplay);
    }

    /**
     * Returns the {@code onmousedown} event handler.
     * @return the {@code onmousedown} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnmousedown() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_DOWN);
    }

    /**
     * Sets the {@code onmousedown} event handler.
     * @param mousedown the {@code onmousedown} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnmousedown(final Object mousedown) {
        setEventHandler(MouseEvent.TYPE_MOUSE_DOWN, mousedown);
    }

    /**
     * Returns the {@code onemptied} event handler.
     * @return the {@code onemptied} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnemptied() {
        return getEventHandler(Event.TYPE_EMPTIED);
    }

    /**
     * Sets the {@code onemptied} event handler.
     * @param emptied the {@code onemptied} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnemptied(final Object emptied) {
        setEventHandler(Event.TYPE_EMPTIED, emptied);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOngotpointercapture() {
        return getEventHandler(Event.TYPE_GOTPOINTERCAPTURE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOngotpointercapture(final Object gotpointercapture) {
        setEventHandler(Event.TYPE_GOTPOINTERCAPTURE, gotpointercapture);
    }

    /**
     * Returns the {@code onwheel} event handler for this element.
     * @return the {@code onwheel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    @Override
    public Function getOnwheel() {
        return getEventHandler(Event.TYPE_WHEEL);
    }

    /**
     * Sets the {@code onwheel} event handler for this element.
     * @param onwheel the {@code onwheel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    @Override
    public void setOnwheel(final Object onwheel) {
        setEventHandler(Event.TYPE_WHEEL, onwheel);
    }
}
