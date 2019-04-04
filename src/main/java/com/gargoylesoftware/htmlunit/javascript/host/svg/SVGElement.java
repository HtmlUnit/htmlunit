/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.svg;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF52;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF60;
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
 */
@JsxClass(domClass = SvgElement.class)
public class SVGElement extends Element {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, FF})
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
    @JsxGetter({CHROME, FF})
    public Function getOnfocus() {
        return getEventHandler(Event.TYPE_FOCUS);
    }

    /**
     * Sets the {@code onfocus} event handler.
     * @param focus the {@code onfocus} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnfocus(final Object focus) {
        setEventHandler(Event.TYPE_FOCUS, focus);
    }

    /**
     * Returns the {@code ondragend} event handler.
     * @return the {@code ondragend} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndragend() {
        return getEventHandler("dragend");
    }

    /**
     * Sets the {@code ondragend} event handler.
     * @param dragend the {@code ondragend} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndragend(final Object dragend) {
        setEventHandler("dragend", dragend);
    }

    /**
     * Returns the {@code oninvalid} event handler.
     * @return the {@code oninvalid} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOninvalid() {
        return getEventHandler("invalid");
    }

    /**
     * Sets the {@code oninvalid} event handler.
     * @param invalid the {@code oninvalid} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOninvalid(final Object invalid) {
        setEventHandler("invalid", invalid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointercancel() {
        return getEventHandler("pointercancel");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointercancel(final Object pointercancel) {
        setEventHandler("pointercancel", pointercancel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointerout() {
        return getEventHandler("pointerout");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointerout(final Object pointerout) {
        setEventHandler("pointerout", pointerout);
    }

    /**
     * Returns the {@code onratechange} event handler.
     * @return the {@code onratechange} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnratechange() {
        return getEventHandler("ratechange");
    }

    /**
     * Sets the {@code onratechange} event handler.
     * @param ratechange the {@code onratechange} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnratechange(final Object ratechange) {
        setEventHandler("ratechange", ratechange);
    }

    /**
     * Returns the {@code onresize} event handler.
     * @return the {@code onresize} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnresize() {
        return getEventHandler("resize");
    }

    /**
     * Sets the {@code onresize} event handler.
     * @param resize the {@code onresize} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnresize(final Object resize) {
        setEventHandler("resize", resize);
    }

    /**
     * Returns the {@code oncanplaythrough} event handler.
     * @return the {@code oncanplaythrough} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOncanplaythrough() {
        return getEventHandler("canplaythrough");
    }

    /**
     * Sets the {@code oncanplaythrough} event handler.
     * @param canplaythrough the {@code oncanplaythrough} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOncanplaythrough(final Object canplaythrough) {
        setEventHandler("canplaythrough", canplaythrough);
    }

    /**
     * Returns the {@code oncancel} event handler.
     * @return the {@code oncancel} event handler
     */
    @JsxGetter(CHROME)
    public Function getOncancel() {
        return getEventHandler("cancel");
    }

    /**
     * Sets the {@code oncancel} event handler.
     * @param cancel the {@code oncancel} event handler
     */
    @JsxSetter(CHROME)
    public void setOncancel(final Object cancel) {
        setEventHandler("cancel", cancel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointerenter() {
        return getEventHandler("pointerenter");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointerenter(final Object pointerenter) {
        setEventHandler("pointerenter", pointerenter);
    }

    /**
     * Returns the {@code onselect} event handler.
     * @return the {@code onselect} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnselect() {
        return getEventHandler("select");
    }

    /**
     * Sets the {@code onselect} event handler.
     * @param select the {@code onselect} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnselect(final Object select) {
        setEventHandler("select", select);
    }

    /**
     * Returns the {@code onauxclick} event handler.
     * @return the {@code onauxclick} event handler
     */
    @JsxGetter(CHROME)
    public Function getOnauxclick() {
        return getEventHandler("auxclick");
    }

    /**
     * Sets the {@code onauxclick} event handler.
     * @param auxclick the {@code onauxclick} event handler
     */
    @JsxSetter(CHROME)
    public void setOnauxclick(final Object auxclick) {
        setEventHandler("auxclick", auxclick);
    }

    /**
     * Returns the {@code onpause} event handler.
     * @return the {@code onpause} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnpause() {
        return getEventHandler("pause");
    }

    /**
     * Sets the {@code onpause} event handler.
     * @param pause the {@code onpause} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnpause(final Object pause) {
        setEventHandler("pause", pause);
    }

    /**
     * Returns the {@code onloadstart} event handler.
     * @return the {@code onloadstart} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnloadstart() {
        return getEventHandler("loadstart");
    }

    /**
     * Sets the {@code onloadstart} event handler.
     * @param loadstart the {@code onloadstart} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnloadstart(final Object loadstart) {
        setEventHandler("loadstart", loadstart);
    }

    /**
     * Returns the {@code onprogress} event handler.
     * @return the {@code onprogress} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnprogress() {
        return getEventHandler("progress");
    }

    /**
     * Sets the {@code onprogress} event handler.
     * @param progress the {@code onprogress} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnprogress(final Object progress) {
        setEventHandler("progress", progress);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointerup() {
        return getEventHandler("pointerup");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointerup(final Object pointerup) {
        setEventHandler("pointerup", pointerup);
    }

    /**
     * Returns the {@code onscroll} event handler.
     * @return the {@code onscroll} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnscroll() {
        return getEventHandler("scroll");
    }

    /**
     * Sets the {@code onscroll} event handler.
     * @param scroll the {@code onscroll} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnscroll(final Object scroll) {
        setEventHandler("scroll", scroll);
    }

    /**
     * Returns the {@code onkeydown} event handler.
     * @return the {@code onkeydown} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnkeydown() {
        return getEventHandler(Event.TYPE_KEY_DOWN);
    }

    /**
     * Sets the {@code onkeydown} event handler.
     * @param keydown the {@code onkeydown} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnkeydown(final Object keydown) {
        setEventHandler(Event.TYPE_KEY_DOWN, keydown);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointerleave() {
        return getEventHandler("pointerleave");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointerleave(final Object pointerleave) {
        setEventHandler("pointerleave", pointerleave);
    }

    /**
     * Returns the {@code onclick} event handler.
     * @return the {@code onclick} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnclick() {
        return getEventHandler(MouseEvent.TYPE_CLICK);
    }

    /**
     * Sets the {@code onclick} event handler.
     * @param click the {@code onclick} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnclick(final Object click) {
        setEventHandler(MouseEvent.TYPE_CLICK, click);
    }

    /**
     * Returns the {@code onkeyup} event handler.
     * @return the {@code onkeyup} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnkeyup() {
        return getEventHandler(Event.TYPE_KEY_UP);
    }

    /**
     * Sets the {@code onkeyup} event handler.
     * @param keyup the {@code onkeyup} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnkeyup(final Object keyup) {
        setEventHandler(Event.TYPE_KEY_UP, keyup);
    }

    /**
     * Returns the {@code onchange} event handler.
     * @return the {@code onchange} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnchange() {
        return getEventHandler(Event.TYPE_CHANGE);
    }

    /**
     * Sets the {@code onchange} event handler.
     * @param change the {@code onchange} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnchange(final Object change) {
        setEventHandler(Event.TYPE_CHANGE, change);
    }

    /**
     * Returns the {@code onreset} event handler.
     * @return the {@code onreset} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnreset() {
        return getEventHandler(Event.TYPE_RESET);
    }

    /**
     * Sets the {@code onreset} event handler.
     * @param reset the {@code onreset} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnreset(final Object reset) {
        setEventHandler(Event.TYPE_RESET, reset);
    }

    /**
     * Returns the {@code onkeypress} event handler.
     * @return the {@code onkeypress} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnkeypress() {
        return getEventHandler(Event.TYPE_KEY_PRESS);
    }

    /**
     * Sets the {@code onkeypress} event handler.
     * @param keypress the {@code onkeypress} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnkeypress(final Object keypress) {
        setEventHandler(Event.TYPE_KEY_PRESS, keypress);
    }

    /**
     * Returns the {@code ontimeupdate} event handler.
     * @return the {@code ontimeupdate} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOntimeupdate() {
        return getEventHandler("timeupdate");
    }

    /**
     * Sets the {@code ontimeupdate} event handler.
     * @param timeupdate the {@code ontimeupdate} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOntimeupdate(final Object timeupdate) {
        setEventHandler("timeupdate", timeupdate);
    }

    /**
     * Returns the {@code ondblclick} event handler.
     * @return the {@code ondblclick} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndblclick() {
        return getEventHandler(MouseEvent.TYPE_DBL_CLICK);
    }

    /**
     * Sets the {@code ondblclick} event handler.
     * @param dblclick the {@code ondblclick} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndblclick(final Object dblclick) {
        setEventHandler(MouseEvent.TYPE_DBL_CLICK, dblclick);
    }

    /**
     * Returns the {@code ondrag} event handler.
     * @return the {@code ondrag} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndrag() {
        return getEventHandler("drag");
    }

    /**
     * Sets the {@code ondrag} event handler.
     * @param drag the {@code ondrag} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndrag(final Object drag) {
        setEventHandler("drag", drag);
    }

    /**
     * Returns the {@code onseeked} event handler.
     * @return the {@code onseeked} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnseeked() {
        return getEventHandler("seeked");
    }

    /**
     * Sets the {@code onseeked} event handler.
     * @param seeked the {@code onseeked} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnseeked(final Object seeked) {
        setEventHandler("seeked", seeked);
    }

    /**
     * Returns the {@code onabort} event handler.
     * @return the {@code onabort} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnabort() {
        return getEventHandler("abort");
    }

    /**
     * Sets the {@code onabort} event handler.
     * @param abort the {@code onabort} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnabort(final Object abort) {
        setEventHandler("abort", abort);
    }

    /**
     * Returns the {@code onloadedmetadata} event handler.
     * @return the {@code onloadedmetadata} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnloadedmetadata() {
        return getEventHandler("loadedmetadata");
    }

    /**
     * Sets the {@code onloadedmetadata} event handler.
     * @param loadedmetadata the {@code onloadedmetadata} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnloadedmetadata(final Object loadedmetadata) {
        setEventHandler("loadedmetadata", loadedmetadata);
    }

    /**
     * Returns the {@code ontoggle} event handler.
     * @return the {@code ontoggle} event handler
     */
    @JsxGetter({CHROME, FF52})
    public Function getOntoggle() {
        return getEventHandler("toggle");
    }

    /**
     * Sets the {@code ontoggle} event handler.
     * @param toggle the {@code ontoggle} event handler
     */
    @JsxSetter({CHROME, FF52})
    public void setOntoggle(final Object toggle) {
        setEventHandler("toggle", toggle);
    }

    /**
     * Returns the {@code onplay} event handler.
     * @return the {@code onplay} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnplay() {
        return getEventHandler("play");
    }

    /**
     * Sets the {@code onplay} event handler.
     * @param play the {@code onplay} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnplay(final Object play) {
        setEventHandler("play", play);
    }

    /**
     * Returns the {@code oncontextmenu} event handler.
     * @return the {@code oncontextmenu} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOncontextmenu() {
        return getEventHandler(MouseEvent.TYPE_CONTEXT_MENU);
    }

    /**
     * Sets the {@code oncontextmenu} event handler.
     * @param contextmenu the {@code oncontextmenu} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOncontextmenu(final Object contextmenu) {
        setEventHandler(MouseEvent.TYPE_CONTEXT_MENU, contextmenu);
    }

    /**
     * Returns the {@code onmousemove} event handler.
     * @return the {@code onmousemove} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnmousemove() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_MOVE);
    }

    /**
     * Sets the {@code onmousemove} event handler.
     * @param mousemove the {@code onmousemove} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnmousemove(final Object mousemove) {
        setEventHandler(MouseEvent.TYPE_MOUSE_MOVE, mousemove);
    }

    /**
     * Returns the {@code onerror} event handler.
     * @return the {@code onerror} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Sets the {@code onerror} event handler.
     * @param error the {@code onerror} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnerror(final Object error) {
        setEventHandler(Event.TYPE_ERROR, error);
    }

    /**
     * Returns the {@code onmouseup} event handler.
     * @return the {@code onmouseup} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnmouseup() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_UP);
    }

    /**
     * Sets the {@code onmouseup} event handler.
     * @param mouseup the {@code onmouseup} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnmouseup(final Object mouseup) {
        setEventHandler(MouseEvent.TYPE_MOUSE_UP, mouseup);
    }

    /**
     * Returns the {@code ondragover} event handler.
     * @return the {@code ondragover} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndragover() {
        return getEventHandler("dragover");
    }

    /**
     * Sets the {@code ondragover} event handler.
     * @param dragover the {@code ondragover} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndragover(final Object dragover) {
        setEventHandler("dragover", dragover);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointermove() {
        return getEventHandler("pointermove");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointermove(final Object pointermove) {
        setEventHandler("pointermove", pointermove);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(FF)
    public Function getOncut() {
        return getEventHandler("cut");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(FF)
    public void setOncut(final Object cut) {
        setEventHandler("cut", cut);
    }

    /**
     * Returns the {@code onmouseover} event handler.
     * @return the {@code onmouseover} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnmouseover() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OVER);
    }

    /**
     * Sets the {@code onmouseover} event handler.
     * @param mouseover the {@code onmouseover} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnmouseover(final Object mouseover) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OVER, mouseover);
    }

    /**
     * Returns the {@code oninput} event handler.
     * @return the {@code oninput} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOninput() {
        return getEventHandler(Event.TYPE_INPUT);
    }

    /**
     * Sets the {@code oninput} event handler.
     * @param input the {@code oninput} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOninput(final Object input) {
        setEventHandler(Event.TYPE_INPUT, input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnlostpointercapture() {
        return getEventHandler("lostpointercapture");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnlostpointercapture(final Object lostpointercapture) {
        setEventHandler("lostpointercapture", lostpointercapture);
    }

    /**
     * Returns the {@code onstalled} event handler.
     * @return the {@code onstalled} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnstalled() {
        return getEventHandler("stalled");
    }

    /**
     * Sets the {@code onstalled} event handler.
     * @param stalled the {@code onstalled} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnstalled(final Object stalled) {
        setEventHandler("stalled", stalled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointerover() {
        return getEventHandler("pointerover");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointerover(final Object pointerover) {
        setEventHandler("pointerover", pointerover);
    }

    /**
     * Returns the {@code onclose} event handler.
     * @return the {@code onclose} event handler
     */
    @JsxGetter(CHROME)
    public Function getOnclose() {
        return getEventHandler(Event.TYPE_CLOSE);
    }

    /**
     * Sets the {@code onclose} event handler.
     * @param close the {@code onclose} event handler
     */
    @JsxSetter(CHROME)
    public void setOnclose(final Object close) {
        setEventHandler(Event.TYPE_CLOSE, close);
    }

    /**
     * Returns the {@code ondragenter} event handler.
     * @return the {@code ondragenter} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndragenter() {
        return getEventHandler("dragenter");
    }

    /**
     * Sets the {@code ondragenter} event handler.
     * @param dragenter the {@code ondragenter} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndragenter(final Object dragenter) {
        setEventHandler("dragenter", dragenter);
    }

    /**
     * Returns the {@code onmozfullscreenerror} event handler.
     * @return the {@code onmozfullscreenerror} event handler
     */
    @JsxGetter(FF)
    public Function getOnmozfullscreenerror() {
        return getEventHandler("mozfullscreenerror");
    }

    /**
     * Sets the {@code onmozfullscreenerror} event handler.
     * @param mozfullscreenerror the {@code onmozfullscreenerror} event handler
     */
    @JsxSetter(FF)
    public void setOnmozfullscreenerror(final Object mozfullscreenerror) {
        setEventHandler("mozfullscreenerror", mozfullscreenerror);
    }

    /**
     * Returns the {@code onsubmit} event handler.
     * @return the {@code onsubmit} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnsubmit() {
        return getEventHandler(Event.TYPE_SUBMIT);
    }

    /**
     * Sets the {@code onsubmit} event handler.
     * @param submit the {@code onsubmit} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnsubmit(final Object submit) {
        setEventHandler(Event.TYPE_SUBMIT, submit);
    }

    /**
     * Returns the {@code onmouseleave} event handler.
     * @return the {@code onmouseleave} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnmouseleave() {
        return getEventHandler("mouseleave");
    }

    /**
     * Sets the {@code onmouseleave} event handler.
     * @param mouseleave the {@code onmouseleave} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnmouseleave(final Object mouseleave) {
        setEventHandler("mouseleave", mouseleave);
    }

    /**
     * Returns the {@code onmouseenter} event handler.
     * @return the {@code onmouseenter} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnmouseenter() {
        return getEventHandler("mouseenter");
    }

    /**
     * Sets the {@code onmouseenter} event handler.
     * @param mouseenter the {@code onmouseenter} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnmouseenter(final Object mouseenter) {
        setEventHandler("mouseenter", mouseenter);
    }

    /**
     * Returns the {@code ondragleave} event handler.
     * @return the {@code ondragleave} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndragleave() {
        return getEventHandler("dragleave");
    }

    /**
     * Sets the {@code ondragleave} event handler.
     * @param dragleave the {@code ondragleave} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndragleave(final Object dragleave) {
        setEventHandler("dragleave", dragleave);
    }

    /**
     * Returns the {@code onmousewheel} event handler.
     * @return the {@code onmousewheel} event handler
     */
    @JsxGetter(CHROME)
    public Function getOnmousewheel() {
        return getEventHandler("mousewheel");
    }

    /**
     * Sets the {@code onmousewheel} event handler.
     * @param mousewheel the {@code onmousewheel} event handler
     */
    @JsxSetter(CHROME)
    public void setOnmousewheel(final Object mousewheel) {
        setEventHandler("mousewheel", mousewheel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOnpointerdown() {
        return getEventHandler("pointerdown");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOnpointerdown(final Object pointerdown) {
        setEventHandler("pointerdown", pointerdown);
    }

    /**
     * Returns the {@code onseeking} event handler.
     * @return the {@code onseeking} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnseeking() {
        return getEventHandler("seeking");
    }

    /**
     * Sets the {@code onseeking} event handler.
     * @param seeking the {@code onseeking} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnseeking(final Object seeking) {
        setEventHandler("seeking", seeking);
    }

    /**
     * Returns the {@code onblur} event handler.
     * @return the {@code onblur} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnblur() {
        return getEventHandler(Event.TYPE_BLUR);
    }

    /**
     * Sets the {@code onblur} event handler.
     * @param blur the {@code onblur} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnblur(final Object blur) {
        setEventHandler(Event.TYPE_BLUR, blur);
    }

    /**
     * Returns the {@code oncuechange} event handler.
     * @return the {@code oncuechange} event handler
     */
    @JsxGetter(CHROME)
    public Function getOncuechange() {
        return getEventHandler("cuechange");
    }

    /**
     * Sets the {@code oncuechange} event handler.
     * @param cuechange the {@code oncuechange} event handler
     */
    @JsxSetter(CHROME)
    public void setOncuechange(final Object cuechange) {
        setEventHandler("cuechange", cuechange);
    }

    /**
     * Returns the {@code ondrop} event handler.
     * @return the {@code ondrop} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndrop() {
        return getEventHandler("drop");
    }

    /**
     * Sets the {@code ondrop} event handler.
     * @param drop the {@code ondrop} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndrop(final Object drop) {
        setEventHandler("drop", drop);
    }

    /**
     * Returns the {@code ondragstart} event handler.
     * @return the {@code ondragstart} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndragstart() {
        return getEventHandler("dragstart");
    }

    /**
     * Sets the {@code ondragstart} event handler.
     * @param dragstart the {@code ondragstart} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndragstart(final Object dragstart) {
        setEventHandler("dragstart", dragstart);
    }

    /**
     * Returns the {@code onmozfullscreenchange} event handler.
     * @return the {@code onmozfullscreenchange} event handler
     */
    @JsxGetter(FF)
    public Function getOnmozfullscreenchange() {
        return getEventHandler("mozfullscreenchange");
    }

    /**
     * Sets the {@code onmozfullscreenchange} event handler.
     * @param mozfullscreenchange the {@code onmozfullscreenchange} event handler
     */
    @JsxSetter(FF)
    public void setOnmozfullscreenchange(final Object mozfullscreenchange) {
        setEventHandler("mozfullscreenchange", mozfullscreenchange);
    }

    /**
     * Returns the {@code ondurationchange} event handler.
     * @return the {@code ondurationchange} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOndurationchange() {
        return getEventHandler("durationchange");
    }

    /**
     * Sets the {@code ondurationchange} event handler.
     * @param durationchange the {@code ondurationchange} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOndurationchange(final Object durationchange) {
        setEventHandler("durationchange", durationchange);
    }

    /**
     * Returns the {@code onplaying} event handler.
     * @return the {@code onplaying} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnplaying() {
        return getEventHandler("playing");
    }

    /**
     * Sets the {@code onplaying} event handler.
     * @param playing the {@code onplaying} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnplaying(final Object playing) {
        setEventHandler("playing", playing);
    }

    /**
     * Returns the {@code onload} event handler.
     * @return the {@code onload} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnload() {
        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the {@code onload} event handler.
     * @param load the {@code onload} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnload(final Object load) {
        setEventHandler(Event.TYPE_LOAD, load);
    }

    /**
     * Returns the {@code onended} event handler.
     * @return the {@code onended} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnended() {
        return getEventHandler("ended");
    }

    /**
     * Sets the {@code onended} event handler.
     * @param ended the {@code onended} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnended(final Object ended) {
        setEventHandler("ended", ended);
    }

    /**
     * Returns the {@code onloadeddata} event handler.
     * @return the {@code onloadeddata} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnloadeddata() {
        return getEventHandler("loadeddata");
    }

    /**
     * Sets the {@code onloadeddata} event handler.
     * @param loadeddata the {@code onloadeddata} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnloadeddata(final Object loadeddata) {
        setEventHandler("loadeddata", loadeddata);
    }

    /**
     * Returns the {@code onshow} event handler.
     * @return the {@code onshow} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnshow() {
        return getEventHandler("show");
    }

    /**
     * Sets the {@code onshow} event handler.
     * @param show the {@code onshow} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnshow(final Object show) {
        setEventHandler("show", show);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(FF)
    public Function getOncopy() {
        return getEventHandler("copy");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(FF)
    public void setOncopy(final Object copy) {
        setEventHandler("copy", copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(FF)
    public Function getOnpaste() {
        return getEventHandler("paste");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(FF)
    public void setOnpaste(final Object paste) {
        setEventHandler("paste", paste);
    }

    /**
     * Returns the {@code onmouseout} event handler.
     * @return the {@code onmouseout} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnmouseout() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OUT);
    }

    /**
     * Sets the {@code onmouseout} event handler.
     * @param mouseout the {@code onmouseout} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnmouseout(final Object mouseout) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OUT, mouseout);
    }

    /**
     * Returns the {@code onsuspend} event handler.
     * @return the {@code onsuspend} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnsuspend() {
        return getEventHandler("suspend");
    }

    /**
     * Sets the {@code onsuspend} event handler.
     * @param suspend the {@code onsuspend} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnsuspend(final Object suspend) {
        setEventHandler("suspend", suspend);
    }

    /**
     * Returns the {@code onvolumechange} event handler.
     * @return the {@code onvolumechange} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnvolumechange() {
        return getEventHandler("volumechange");
    }

    /**
     * Sets the {@code onvolumechange} event handler.
     * @param volumechange the {@code onvolumechange} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnvolumechange(final Object volumechange) {
        setEventHandler("volumechange", volumechange);
    }

    /**
     * Returns the {@code onwaiting} event handler.
     * @return the {@code onwaiting} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnwaiting() {
        return getEventHandler("waiting");
    }

    /**
     * Sets the {@code onwaiting} event handler.
     * @param waiting the {@code onwaiting} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnwaiting(final Object waiting) {
        setEventHandler("waiting", waiting);
    }

    /**
     * Returns the {@code oncanplay} event handler.
     * @return the {@code oncanplay} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOncanplay() {
        return getEventHandler("canplay");
    }

    /**
     * Sets the {@code oncanplay} event handler.
     * @param canplay the {@code oncanplay} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOncanplay(final Object canplay) {
        setEventHandler("canplay", canplay);
    }

    /**
     * Returns the {@code onmousedown} event handler.
     * @return the {@code onmousedown} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnmousedown() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_DOWN);
    }

    /**
     * Sets the {@code onmousedown} event handler.
     * @param mousedown the {@code onmousedown} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnmousedown(final Object mousedown) {
        setEventHandler(MouseEvent.TYPE_MOUSE_DOWN, mousedown);
    }

    /**
     * Returns the {@code onemptied} event handler.
     * @return the {@code onemptied} event handler
     */
    @JsxGetter({CHROME, FF})
    public Function getOnemptied() {
        return getEventHandler("emptied");
    }

    /**
     * Sets the {@code onemptied} event handler.
     * @param emptied the {@code onemptied} event handler
     */
    @JsxSetter({CHROME, FF})
    public void setOnemptied(final Object emptied) {
        setEventHandler("emptied", emptied);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(CHROME)
    public Function getOngotpointercapture() {
        return getEventHandler("gotpointercapture");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(CHROME)
    public void setOngotpointercapture(final Object gotpointercapture) {
        setEventHandler("gotpointercapture", gotpointercapture);
    }

    /**
     * Returns the {@code onanimationiteration} event handler.
     * @return the {@code onanimationiteration} event handler
     */
    @JsxGetter(FF52)
    public Function getOnanimationiteration() {
        return getEventHandler("animationiteration");
    }

    /**
     * Sets the {@code onanimationiteration} event handler.
     * @param animationiteration the {@code onanimationiteration} event handler
     */
    @JsxSetter(FF52)
    public void setOnanimationiteration(final Object animationiteration) {
        setEventHandler("animationiteration", animationiteration);
    }

    /**
     * Returns the {@code onwebkittransitionend} event handler.
     * @return the {@code onwebkittransitionend} event handler
     */
    @JsxGetter(FF52)
    public Function getOnwebkittransitionend() {
        return getEventHandler("webkittransitionend");
    }

    /**
     * Sets the {@code onwebkittransitionend} event handler.
     * @param webkittransitionend the {@code onwebkittransitionend} event handler
     */
    @JsxSetter(FF52)
    public void setOnwebkittransitionend(final Object webkittransitionend) {
        setEventHandler("webkittransitionend", webkittransitionend);
    }

    /**
     * Returns the {@code onanimationstart} event handler.
     * @return the {@code onanimationstart} event handler
     */
    @JsxGetter(FF52)
    public Function getOnanimationstart() {
        return getEventHandler("animationstart");
    }

    /**
     * Sets the {@code onanimationstart} event handler.
     * @param animationstart the {@code onanimationstart} event handler
     */
    @JsxSetter(FF52)
    public void setOnanimationstart(final Object animationstart) {
        setEventHandler("animationstart", animationstart);
    }

    /**
     * Returns the {@code onwebkitanimationstart} event handler.
     * @return the {@code onwebkitanimationstart} event handler
     */
    @JsxGetter(FF52)
    public Function getOnwebkitanimationstart() {
        return getEventHandler("webkitanimationstart");
    }

    /**
     * Sets the {@code onwebkitanimationstart} event handler.
     * @param webkitanimationstart the {@code onwebkitanimationstart} event handler
     */
    @JsxSetter(FF52)
    public void setOnwebkitanimationstart(final Object webkitanimationstart) {
        setEventHandler("webkitanimationstart", webkitanimationstart);
    }

    /**
     * Returns the {@code onwebkitanimationiteration} event handler.
     * @return the {@code onwebkitanimationiteration} event handler
     */
    @JsxGetter(FF52)
    public Function getOnwebkitanimationiteration() {
        return getEventHandler("webkitanimationiteration");
    }

    /**
     * Sets the {@code onwebkitanimationiteration} event handler.
     * @param webkitanimationiteration the {@code onwebkitanimationiteration} event handler
     */
    @JsxSetter(FF52)
    public void setOnwebkitanimationiteration(final Object webkitanimationiteration) {
        setEventHandler("webkitanimationiteration", webkitanimationiteration);
    }

    /**
     * Returns the {@code onanimationend} event handler.
     * @return the {@code onanimationend} event handler
     */
    @JsxGetter(FF52)
    public Function getOnanimationend() {
        return getEventHandler("animationend");
    }

    /**
     * Sets the {@code onanimationend} event handler.
     * @param animationend the {@code onanimationend} event handler
     */
    @JsxSetter(FF52)
    public void setOnanimationend(final Object animationend) {
        setEventHandler("animationend", animationend);
    }

    /**
     * Returns the {@code onwebkitanimationend} event handler.
     * @return the {@code onwebkitanimationend} event handler
     */
    @JsxGetter(FF52)
    public Function getOnwebkitanimationend() {
        return getEventHandler("webkitanimationend");
    }

    /**
     * Sets the {@code onwebkitanimationend} event handler.
     * @param webkitanimationend the {@code onwebkitanimationend} event handler
     */
    @JsxSetter(FF52)
    public void setOnwebkitanimationend(final Object webkitanimationend) {
        setEventHandler("webkitanimationend", webkitanimationend);
    }

    /**
     * Returns the {@code ontransitionend} event handler.
     * @return the {@code ontransitionend} event handler
     */
    @JsxGetter(FF52)
    public Function getOntransitionend() {
        return getEventHandler("transitionend");
    }

    /**
     * Sets the {@code ontransitionend} event handler.
     * @param transitionend the {@code ontransitionend} event handler
     */
    @JsxSetter(FF52)
    public void setOntransitionend(final Object transitionend) {
        setEventHandler("transitionend", transitionend);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(FF52)
    public Function getOnselectstart() {
        return getEventHandler("selectstart");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(FF52)
    public void setOnselectstart(final Object selectstart) {
        setEventHandler("selectstart", selectstart);
    }

    /**
     * Returns the {@code onloadend} event handler.
     * @return the {@code onloadend} event handler
     */
    @JsxGetter(FF52)
    public Function getOnloadend() {
        return getEventHandler("loadend");
    }

    /**
     * Sets the {@code onloadend} event handler.
     * @param loadend the {@code onloadend} event handler
     */
    @JsxSetter(FF52)
    public void setOnloadend(final Object loadend) {
        setEventHandler("loadend", loadend);
    }

    /**
     * Returns the {@code ondragexit} event handler.
     * @return the {@code ondragexit} event handler
     */
    @JsxGetter(FF52)
    public Function getOndragexit() {
        return getEventHandler("dragexit");
    }

    /**
     * Sets the {@code ondragexit} event handler.
     * @param dragexit the {@code ondragexit} event handler
     */
    @JsxSetter(FF52)
    public void setOndragexit(final Object dragexit) {
        setEventHandler("dragexit", dragexit);
    }

    /**
     * Returns the {@code onwheel} event handler for this element.
     * @return the {@code onwheel} event handler for this element
     */
    @JsxGetter({CHROME, FF60})
    @Override
    public Function getOnwheel() {
        return super.getOnwheel();
    }

    /**
     * Sets the {@code onwheel} event handler for this element.
     * @param onwheel the {@code onwheel} event handler for this element
     */
    @JsxSetter({CHROME, FF60})
    @Override
    public void setOnwheel(final Object onwheel) {
        super.setOnwheel(onwheel);
    }
}
