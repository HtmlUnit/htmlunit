/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 * Intermediate base class for "clickable" HTML elements.  As defined
 * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation,
 * this class is a base class for all HTML elements except these:
 * applet, base, basefront, bdo, br, font, frame, frameset, head, html,
 * iframe, isindex, meta, param, script, style, and title.
 *
 * @version $Revision$
 * @author David K. Taylor
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public abstract class ClickableElement extends StyledElement {

    /**
     *  Create an instance
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param  page The page that contains this element
     * @param attributes the initial attributes
     */
    protected ClickableElement(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Simulate clicking this element.
     *
     * @return The page that occupies this window after this element is
     * clicked. It may be the same window or it may be a freshly loaded one.
     * @exception IOException If an IO error occurs
     */
    public Page click()
        throws IOException {
        return click(false, false, false);
    }

    /**
     * Simulate clicking this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after this element is
     * clicked. It may be the same window or it may be a freshly loaded one.
     * @exception IOException If an IO error occurs
     */
    public Page click(final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (this instanceof DisabledElement) {
            if (((DisabledElement) this).isDisabled()) {
                return getPage();
            }
        }

        final HtmlPage page = getPage();

        boolean stateUpdated = false;
        if (isStateUpdateFirst()) {
            doClickAction(page);
            stateUpdated = true;
        }
        final Event event = new Event(this, Event.TYPE_CLICK, shiftKey, ctrlKey, altKey);
        final ScriptResult scriptResult = fireEvent(event);
        final Page currentPage;
        if (scriptResult == null) {
            currentPage = page;
        }
        else {
            currentPage = scriptResult.getNewPage();
        }
        if (stateUpdated || ScriptResult.isFalse(scriptResult)) {
            return currentPage;
        }
        else {
            return doClickAction(currentPage);
        }
    }

    /**
     * Simulate double clicking this element, note that
     * {@link #click()} is called first.
     *
     * @return The page that occupies this window after this element is double
     * clicked. It may be the same window or it may be a freshly loaded one.
     * @exception IOException If an IO error occurs
     */
    public Page dblClick()
        throws IOException {
        return dblClick(false, false, false);
    }

    /**
     * Simulate double clicking this element, note that
     * {@link #click(boolean, boolean, boolean)} is called first.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after this element is double
     * clicked. It may be the same window or it may be a freshly loaded one.
     * @exception IOException If an IO error occurs
     */
    public Page dblClick(final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if( this instanceof DisabledElement ) {
            if( ((DisabledElement) this).isDisabled() ) {
                return getPage();
            }
        }
        
        //call click event first
        final Page clickPage = click(shiftKey, ctrlKey, altKey);
        if( clickPage != getPage() ) {
            getLog().warn( "dblClick() is ignored, as click() loaded a different page." );
            return clickPage;
        }

        final Event event = new Event(this, Event.TYPE_DBL_CLICK, shiftKey, ctrlKey, altKey );
        final ScriptResult scriptResult = fireEvent(event);
        if (scriptResult == null) {
            return clickPage;
        }
        else {
            return scriptResult.getNewPage();
        }
    }

    /**
     * This method will be called if there either wasn't an onclick handler or
     * there was but the result of that handler wasn't <code>false</code>.
     * This is the default behaviour of clicking the element.  
     * The default implementation returns
     * the current page - subclasses requiring different behaviour (like
     * {@link HtmlSubmitInput}) will override this method.
     *
     * @param defaultPage The default page to return if the action does not
     * load a new page.
     * @return The page that is currently loaded after execution of this method
     * @throws IOException If an IO error occurred
     */
    protected Page doClickAction(final Page defaultPage) throws IOException {
        return defaultPage;
    }

    /**
     * Simulate moving the mouse over this element.
     *
     * @return The page that occupies this window after the mouse moves over this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOver() {
        return mouseOver(false, false, false);
    }
    
    /**
     * Simulate moving the mouse over this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after the mouse moves over this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOver(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        return doMouseEvent(Event.TYPE_MOUSE_OVER, shiftKey, ctrlKey, altKey);
    }

    /**
     * Simulate moving the mouse inside this element.
     *
     * @return The page that occupies this window after the mouse moves inside this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseMove() {
        return mouseMove(false, false, false);
    }
    
    /**
     * Simulate moving the mouse inside this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after the mouse moves inside this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseMove(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        return doMouseEvent(Event.TYPE_MOUSE_MOVE, shiftKey, ctrlKey, altKey);
    }

    /**
     * Simulate moving the mouse out of this element.
     *
     * @return The page that occupies this window after the mouse moves out of this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOut() {
        return mouseOut(false, false, false);
    }
    
    /**
     * Simulate moving the mouse out of this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after the mouse moves out of this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseOut(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        return doMouseEvent(Event.TYPE_MOUSE_OUT, shiftKey, ctrlKey, altKey);
    }

    /**
     * Simulate clicking the mouse in this element.
     *
     * @return The page that occupies this window after the mouse is clicked in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseDown() {
        return mouseDown(false, false, false);
    }
    
    /**
     * Simulate clicking the mouse in this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after the mouse is clicked in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseDown(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        return doMouseEvent(Event.TYPE_MOUSE_DOWN, shiftKey, ctrlKey, altKey);
    }

    /**
     * Simulate releasing the mouse click in this element.
     *
     * @return The page that occupies this window after the mouse click is released in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseUp() {
        return mouseUp(false, false, false);
    }
    
    /**
     * Simulate releasing the mouse click in this element.
     *
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after the mouse click is released in this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    public Page mouseUp(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        return doMouseEvent(Event.TYPE_MOUSE_UP, shiftKey, ctrlKey, altKey);
    }

    /**
     * Simulate the given mouse event.
     * 
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * 
     * @return The page that occupies this window after the mouse event occur on this element.
     * It may be the same window or it may be a freshly loaded one.
     */
    private Page doMouseEvent(final String eventType, final boolean shiftKey, final boolean ctrlKey,
        final boolean altKey) {
        if (this instanceof DisabledElement) {
            if (((DisabledElement) this).isDisabled()) {
                return getPage();
            }
        }

        final HtmlPage page = getPage();
        final Event event = new Event(this, eventType, shiftKey, ctrlKey, altKey);
        final ScriptResult scriptResult = fireEvent(event);
        final Page currentPage;
        if (scriptResult == null) {
            currentPage = page;
        }
        else {
            currentPage = scriptResult.getNewPage();
        }
        return currentPage;
    }

    /**
     * Return the value of the attribute "lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLangAttribute() {
        return getAttributeValue("lang");
    }

    /**
     * Return the value of the attribute "xml:lang".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "xml:lang"
     * or an empty string if that attribute isn't defined.
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue("xml:lang");
    }

    /**
     * Return the value of the attribute "dir".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "dir"
     * or an empty string if that attribute isn't defined.
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue("dir");
    }

    /**
     * Return the value of the attribute "onclick".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onclick"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnClickAttribute() {
        return getAttributeValue("onclick");
    }

    /**
     * Return the value of the attribute "ondblclick".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "ondblclick"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnDblClickAttribute() {
        return getAttributeValue("ondblclick");
    }

    /**
     * Return the value of the attribute "onmousedown".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmousedown"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseDownAttribute() {
        return getAttributeValue("onmousedown");
    }

    /**
     * Return the value of the attribute "onmouseup".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseup"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseUpAttribute() {
        return getAttributeValue("onmouseup");
    }

    /**
     * Return the value of the attribute "onmouseover".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseover"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseOverAttribute() {
        return getAttributeValue("onmouseover");
    }

    /**
     * Return the value of the attribute "onmousemove".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmousemove"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseMoveAttribute() {
        return getAttributeValue("onmousemove");
    }

    /**
     * Return the value of the attribute "onmouseout".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onmouseout"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnMouseOutAttribute() {
        return getAttributeValue("onmouseout");
    }

    /**
     * Return the value of the attribute "onkeypress".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeypress"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyPressAttribute() {
        return getAttributeValue("onkeypress");
    }

    /**
     * Return the value of the attribute "onkeydown".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeydown"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyDownAttribute() {
        return getAttributeValue("onkeydown");
    }

    /**
     * Return the value of the attribute "onkeyup".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "onkeyup"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnKeyUpAttribute() {
        return getAttributeValue("onkeyup");
    }

    /**
     * Return true if the state update should be done before onclick event
     * handling.
     * This is expected to be overridden to return "true" by derived classes
     * like HtmlCheckBoxInput.
     * @return Return true if state update should be done first.
     */
    protected boolean isStateUpdateFirst() {
        return false;
    }
}
