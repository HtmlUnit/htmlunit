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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;

/**
 * Intermediate base class for "clickable" HTML elements. As defined
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
 * @author Dmitri Zoubkov
 */
public abstract class ClickableElement extends StyledElement {

    private final transient Log mainLog_ = LogFactory.getLog(getClass());
    
    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    protected ClickableElement(final String namespaceURI, final String qualifiedName, final Page page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.
     *
     * @return the page that occupies this element's window after the element has been clicked
     * @exception IOException if an IO error occurs
     */
    public Page click() throws IOException {
        return click(false, false, false);
    }

    /**
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.
     *
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the click
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the click
     * @param altKey <tt>true</tt> if ALT is pressed during the click
     * @return the page that occupies this element's window after the element has been clicked
     * @exception IOException if an IO error occurs
     */
    public Page click(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return getPage();
        }
        final Event event = new MouseEvent(this, MouseEvent.TYPE_CLICK, shiftKey, ctrlKey, altKey,
                MouseEvent.BUTTON_LEFT);
        return click(event);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.
     *
     * @param event the click event used
     * @return the page that occupies this element's window after the element has been clicked
     * @exception IOException if an IO error occurs
     */
    public Page click(final Event event) throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return getPage();
        }

        final HtmlPage page = getPage();

        boolean stateUpdated = false;
        if (isStateUpdateFirst()) {
            doClickAction(page);
            stateUpdated = true;
        }
        final ScriptResult scriptResult = fireEvent(event);
        final Page currentPage;
        if (scriptResult == null) {
            currentPage = page;
        }
        else {
            currentPage = scriptResult.getNewPage();
        }

        if (stateUpdated || ScriptResult.isFalse(scriptResult) || event.isPreventDefault()) {
            return currentPage;
        }
        return doClickAction(currentPage);
    }

    /**
     * Simulates double-clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc. Note also that {@link #click()} is automatically called first.
     *
     * @return the page that occupies this element's window after the element has been double-clicked
     * @exception IOException if an IO error occurs
     */
    public Page dblClick() throws IOException {
        return dblClick(false, false, false);
    }

    /**
     * Simulates double-clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc. Note also that {@link #click(boolean, boolean, boolean)} is automatically
     * called first.
     *
     * @param shiftKey <tt>true</tt> if SHIFT is pressed during the double-click
     * @param ctrlKey <tt>true</tt> if CTRL is pressed during the double-click
     * @param altKey <tt>true</tt> if ALT is pressed during the double-click
     * @return the page that occupies this element's window after the element has been double-clicked
     * @exception IOException if an IO error occurs
     */
    public Page dblClick(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return getPage();
        }
        
        //call click event first
        final Page clickPage = click(shiftKey, ctrlKey, altKey);
        if (clickPage != getPage()) {
            if (mainLog_.isDebugEnabled()) {
                mainLog_.debug("dblClick() is ignored, as click() loaded a different page.");
            }
            return clickPage;
        }

        final Event event = new MouseEvent(this, MouseEvent.TYPE_DBL_CLICK, shiftKey, ctrlKey, altKey,
                MouseEvent.BUTTON_LEFT);
        final ScriptResult scriptResult = fireEvent(event);
        if (scriptResult == null) {
            return clickPage;
        }
        return scriptResult.getNewPage();
    }

    /**
     * <p>This method will be called if there either wasn't an <tt>onclick</tt> handler, or if
     * there was one, but the result of that handler wasn't <tt>false</tt>. This is the default
     * behavior of clicking the element.<p>
     *
     * <p>The default implementation returns the current page. Subclasses requiring different
     * behavior (like {@link HtmlSubmitInput}) will override this method.</p>
     *
     * @param defaultPage the default page to return if the action does not load a new page
     * @return the page that is currently loaded after execution of this method
     * @throws IOException if an IO error occurs
     */
    protected Page doClickAction(final Page defaultPage) throws IOException {
        return defaultPage;
    }

    /**
     * Returns the value of the attribute "lang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "lang" or an empty string if that attribute isn't defined
     */
    public final String getLangAttribute() {
        return getAttributeValue("lang");
    }

    /**
     * Returns the value of the attribute "xml:lang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "xml:lang" or an empty string if that attribute isn't defined
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue("xml:lang");
    }

    /**
     * Returns the value of the attribute "dir". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "dir" or an empty string if that attribute isn't defined
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue("dir");
    }

    /**
     * Returns the value of the attribute "onclick". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onclick" or an empty string if that attribute isn't defined
     */
    public final String getOnClickAttribute() {
        return getAttributeValue("onclick");
    }

    /**
     * Returns the value of the attribute "ondblclick". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "ondblclick" or an empty string if that attribute isn't defined
     */
    public final String getOnDblClickAttribute() {
        return getAttributeValue("ondblclick");
    }

    /**
     * Returns the value of the attribute "onmousedown". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onmousedown" or an empty string if that attribute isn't defined
     */
    public final String getOnMouseDownAttribute() {
        return getAttributeValue("onmousedown");
    }

    /**
     * Returns the value of the attribute "onmouseup". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onmouseup" or an empty string if that attribute isn't defined
     */
    public final String getOnMouseUpAttribute() {
        return getAttributeValue("onmouseup");
    }

    /**
     * Returns the value of the attribute "onmouseover". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onmouseover" or an empty string if that attribute isn't defined
     */
    public final String getOnMouseOverAttribute() {
        return getAttributeValue("onmouseover");
    }

    /**
     * Returns the value of the attribute "onmousemove". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onmousemove" or an empty string if that attribute isn't defined
     */
    public final String getOnMouseMoveAttribute() {
        return getAttributeValue("onmousemove");
    }

    /**
     * Returns the value of the attribute "onmouseout". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onmouseout" or an empty string if that attribute isn't defined
     */
    public final String getOnMouseOutAttribute() {
        return getAttributeValue("onmouseout");
    }

    /**
     * Returns the value of the attribute "onkeypress". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onkeypress" or an empty string if that attribute isn't defined
     */
    public final String getOnKeyPressAttribute() {
        return getAttributeValue("onkeypress");
    }

    /**
     * Returns the value of the attribute "onkeydown". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onkeydown" or an empty string if that attribute isn't defined
     */
    public final String getOnKeyDownAttribute() {
        return getAttributeValue("onkeydown");
    }

    /**
     * Returns the value of the attribute "onkeyup". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onkeyup" or an empty string if that attribute isn't defined
     */
    public final String getOnKeyUpAttribute() {
        return getAttributeValue("onkeyup");
    }

    /**
     * Returns <tt>true</tt> if state updates should be done before onclick event handling. This method
     * returns <tt>false</tt> by default, and is expected to be overridden to return <tt>true</tt> by
     * derived classes like {@link HtmlCheckBoxInput}.
     * @return <tt>true</tt> if state updates should be done before onclick event handling
     */
    protected boolean isStateUpdateFirst() {
        return false;
    }

}
