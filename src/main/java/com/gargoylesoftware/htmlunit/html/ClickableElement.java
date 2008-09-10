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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
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

    private static final long serialVersionUID = 5708324473968728785L;
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
     * @param <P> the page
     * @return the page that occupies this element's window after the element has been clicked
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P click() throws IOException {
        return (P) click(false, false, false);
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
     * @param <P> the page
     * @return the page that occupies this element's window after the element has been clicked
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final boolean shiftKey, final boolean ctrlKey, final boolean altKey)
        throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return (P) getPage();
        }

        mouseDown(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_LEFT);
        if (this instanceof HtmlInput) {
            ((HtmlPage) getPage()).setFocusedElement(this);
        }
        mouseUp(shiftKey, ctrlKey, altKey, MouseEvent.BUTTON_LEFT);

        final Event event = new MouseEvent(this, MouseEvent.TYPE_CLICK, shiftKey, ctrlKey, altKey,
                MouseEvent.BUTTON_LEFT);
        return (P) click(event);
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
     * @param <P> the page
     * @return the page that occupies this element's window after the element has been clicked
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P click(final Event event) throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return (P) getPage();
        }

        final SgmlPage page = getPage();

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
            return (P) currentPage;
        }
        return (P) doClickAction(currentPage);
    }

    /**
     * Simulates double-clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc. Note also that {@link #click()} is automatically called first.
     *
     * @param <P> the page
     * @return the page that occupies this element's window after the element has been double-clicked
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P dblClick() throws IOException {
        return (P) dblClick(false, false, false);
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
     * @param <P> the page
     * @return the page that occupies this element's window after the element has been double-clicked
     * @exception IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    public <P extends Page> P dblClick(final boolean shiftKey, final boolean ctrlKey, final boolean altKey) throws IOException {
        if (this instanceof DisabledElement && ((DisabledElement) this).isDisabled()) {
            return (P) getPage();
        }

        //call click event first
        final Page clickPage = click(shiftKey, ctrlKey, altKey);
        if (clickPage != getPage()) {
            if (mainLog_.isDebugEnabled()) {
                mainLog_.debug("dblClick() is ignored, as click() loaded a different page.");
            }
            return (P) clickPage;
        }

        final Event event = new MouseEvent(this, MouseEvent.TYPE_DBL_CLICK, shiftKey, ctrlKey, altKey,
                MouseEvent.BUTTON_LEFT);
        final ScriptResult scriptResult = fireEvent(event);
        if (scriptResult == null) {
            return (P) clickPage;
        }
        return (P) scriptResult.getNewPage();
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
