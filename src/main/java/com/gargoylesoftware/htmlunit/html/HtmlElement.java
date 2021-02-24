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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORM_SUBMISSION_FORM_ATTRIBUTE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLELEMENT_DETACH_ACTIVE_TRIGGERS_NO_KEYUP_EVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLELEMENT_REMOVE_ACTIVE_TRIGGERS_BLUR_EVENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.KEYBOARD_EVENT_SPECIAL_KEYPRESS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.MutationObserver;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * An abstract wrapper for HTML elements.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gudujarlson@sf.net">Mike J. Bresnahan</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David D. Kilzer
 * @author Mike Gallaher
 * @author Denis N. Antonioli
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Dmitri Zoubkov
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Ronny Shapiro
 */
public abstract class HtmlElement extends DomElement {

    /**
     * Enum for the different display styles.
     */
    public enum DisplayStyle {
        /** Empty string. */
        EMPTY(""),
        /** none. */
        NONE("none"),
        /** block. */
        BLOCK("block"),
        /** contents. */
        CONTENTS("contents"),
        /** inline. */
        INLINE("inline"),
        /** inline-block. */
        INLINE_BLOCK("inline-block"),
        /** list-item. */
        LIST_ITEM("list-item"),
        /** table. */
        TABLE("table"),
        /** table-cell. */
        TABLE_CELL("table-cell"),
        /** table-column. */
        TABLE_COLUMN("table-column"),
        /** table-column-group. */
        TABLE_COLUMN_GROUP("table-column-group"),
        /** table-row. */
        TABLE_ROW("table-row"),
        /** table-row-group. */
        TABLE_ROW_GROUP("table-row-group"),
        /** table-header-group. */
        TABLE_HEADER_GROUP("table-header-group"),
        /** table-footer-group. */
        TABLE_FOOTER_GROUP("table-footer-group"),
        /** table-caption. */
        TABLE_CAPTION("table-caption"),
        /** ruby. */
        RUBY("ruby"),
        /** ruby-text. */
        RUBY_TEXT("ruby-text");

        private final String value_;
        DisplayStyle(final String value) {
            value_ = value;
        }

        /**
         * The string used from js.
         * @return the value as string
         */
        public String value() {
            return value_;
        }
    }

    /**
     * Constant indicating that a tab index value is out of bounds (less than <tt>0</tt> or greater
     * than <tt>32767</tt>).
     *
     * @see #getTabIndex()
     */
    public static final Short TAB_INDEX_OUT_OF_BOUNDS = new Short(Short.MIN_VALUE);

    /** The listeners which are to be notified of attribute changes. */
    private Collection<HtmlAttributeChangeListener> attributeListeners_;

    /** The owning form for lost form children. */
    private HtmlForm owningForm_;

    private boolean shiftPressed_;
    private boolean ctrlPressed_;
    private boolean altPressed_;

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes a map ready initialized with the attributes for this element, or
     * {@code null}. The map will be stored as is, not copied.
     */
    protected HtmlElement(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        this(Html.XHTML_NAMESPACE, qualifiedName, page, attributes);
    }

    /**
     * Creates an instance of a DOM element that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes a map ready initialized with the attributes for this element, or
     * {@code null}. The map will be stored as is, not copied.
     */
    protected HtmlElement(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        attributeListeners_ = new LinkedHashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName,
            final String attributeValue, final boolean notifyAttributeChangeListeners,
            final boolean notifyMutationObservers) {

        // TODO: Clean up; this is a hack for HtmlElement living within an XmlPage.
        if (null == getHtmlPageOrNull()) {
            super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                    notifyMutationObservers);
            return;
        }

        final String oldAttributeValue = getAttribute(qualifiedName);
        final HtmlPage htmlPage = (HtmlPage) getPage();
        final boolean mappedElement = isAttachedToPage()
                    && HtmlPage.isMappedElement(htmlPage, qualifiedName);
        if (mappedElement) {
            // cast is save here because isMappedElement checks for HtmlPage
            htmlPage.removeMappedElement(this);
        }

        final HtmlAttributeChangeEvent event;
        if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
            event = new HtmlAttributeChangeEvent(this, qualifiedName, attributeValue);
        }
        else {
            event = new HtmlAttributeChangeEvent(this, qualifiedName, oldAttributeValue);
        }

        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);

        if (notifyAttributeChangeListeners) {
            notifyAttributeChangeListeners(event, this, oldAttributeValue, notifyMutationObservers);
        }

        fireAttributeChangeImpl(event, htmlPage, mappedElement, oldAttributeValue);
    }

    /**
     * Recursively notifies all {@link HtmlAttributeChangeListener}s.
     * @param event the event
     * @param element the element
     * @param oldAttributeValue the old attribute value
     * @param notifyMutationObservers whether to notify {@link MutationObserver}s or not
     */
    protected static void notifyAttributeChangeListeners(final HtmlAttributeChangeEvent event,
            final HtmlElement element, final String oldAttributeValue, final boolean notifyMutationObservers) {
        final Collection<HtmlAttributeChangeListener> listeners = element.attributeListeners_;
        if (ATTRIBUTE_NOT_DEFINED == oldAttributeValue) {
            synchronized (listeners) {
                for (final HtmlAttributeChangeListener listener : listeners) {
                    if (notifyMutationObservers || !(listener instanceof MutationObserver)) {
                        listener.attributeAdded(event);
                    }
                }
            }
        }
        else {
            synchronized (listeners) {
                for (final HtmlAttributeChangeListener listener : listeners) {
                    if (notifyMutationObservers || !(listener instanceof MutationObserver)) {
                        listener.attributeReplaced(event);
                    }
                }
            }
        }
        final DomNode parentNode = element.getParentNode();
        if (parentNode instanceof HtmlElement) {
            notifyAttributeChangeListeners(event, (HtmlElement) parentNode, oldAttributeValue, notifyMutationObservers);
        }
    }

    private void fireAttributeChangeImpl(final HtmlAttributeChangeEvent event,
            final HtmlPage htmlPage, final boolean mappedElement, final String oldAttributeValue) {
        if (mappedElement) {
            htmlPage.addMappedElement(this);
        }

        if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
            fireHtmlAttributeAdded(event);
            htmlPage.fireHtmlAttributeAdded(event);
        }
        else {
            fireHtmlAttributeReplaced(event);
            htmlPage.fireHtmlAttributeReplaced(event);
        }
    }

    /**
     * Sets the specified attribute. This method may be overridden by subclasses
     * which are interested in specific attribute value changes, but such methods <b>must</b>
     * invoke <tt>super.setAttributeNode()</tt>, and <b>should</b> consider the value of the
     * <tt>cloning</tt> parameter when deciding whether or not to execute custom logic.
     *
     * @param attribute the attribute to set
     * @return {@inheritDoc}
     */
    @Override
    public Attr setAttributeNode(final Attr attribute) {
        final String qualifiedName = attribute.getName();
        final String oldAttributeValue = getAttribute(qualifiedName);
        final HtmlPage htmlPage = (HtmlPage) getPage();
        final boolean mappedElement = isAttachedToPage()
                    && HtmlPage.isMappedElement(htmlPage, qualifiedName);
        if (mappedElement) {
            // cast is save here because isMappedElement checks for HtmlPage
            htmlPage.removeMappedElement(this);
        }

        final HtmlAttributeChangeEvent event;
        if (oldAttributeValue == ATTRIBUTE_NOT_DEFINED) {
            event = new HtmlAttributeChangeEvent(this, qualifiedName, attribute.getValue());
        }
        else {
            event = new HtmlAttributeChangeEvent(this, qualifiedName, oldAttributeValue);
        }
        notifyAttributeChangeListeners(event, this, oldAttributeValue, true);

        final Attr result = super.setAttributeNode(attribute);

        fireAttributeChangeImpl(event, htmlPage, mappedElement, oldAttributeValue);

        return result;
    }

    /**
     * Removes an attribute specified by name from this element.
     * @param attributeName the attribute attributeName
     */
    @Override
    public void removeAttribute(final String attributeName) {
        final String value = getAttribute(attributeName);
        if (value == ATTRIBUTE_NOT_DEFINED) {
            return;
        }

        final HtmlPage htmlPage = getHtmlPageOrNull();
        if (htmlPage != null) {
            htmlPage.removeMappedElement(this);
        }

        super.removeAttribute(attributeName);

        if (htmlPage != null) {
            htmlPage.addMappedElement(this);

            final HtmlAttributeChangeEvent event = new HtmlAttributeChangeEvent(this, attributeName, value);
            fireHtmlAttributeRemoved(event);
            htmlPage.fireHtmlAttributeRemoved(event);
        }
    }

    /**
     * Support for reporting HTML attribute changes. This method can be called when an attribute
     * has been added and it will send the appropriate {@link HtmlAttributeChangeEvent} to any
     * registered {@link HtmlAttributeChangeListener}s.
     *
     * Note that this method recursively calls this element's parent's
     * {@link #fireHtmlAttributeAdded(HtmlAttributeChangeEvent)} method.
     *
     * @param event the event
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    protected void fireHtmlAttributeAdded(final HtmlAttributeChangeEvent event) {
        final DomNode parentNode = getParentNode();
        if (parentNode instanceof HtmlElement) {
            ((HtmlElement) parentNode).fireHtmlAttributeAdded(event);
        }
    }

    /**
     * Support for reporting HTML attribute changes. This method can be called when an attribute
     * has been replaced and it will send the appropriate {@link HtmlAttributeChangeEvent} to any
     * registered {@link HtmlAttributeChangeListener}s.
     *
     * Note that this method recursively calls this element's parent's
     * {@link #fireHtmlAttributeReplaced(HtmlAttributeChangeEvent)} method.
     *
     * @param event the event
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    protected void fireHtmlAttributeReplaced(final HtmlAttributeChangeEvent event) {
        final DomNode parentNode = getParentNode();
        if (parentNode instanceof HtmlElement) {
            ((HtmlElement) parentNode).fireHtmlAttributeReplaced(event);
        }
    }

    /**
     * Support for reporting HTML attribute changes. This method can be called when an attribute
     * has been removed and it will send the appropriate {@link HtmlAttributeChangeEvent} to any
     * registered {@link HtmlAttributeChangeListener}s.
     *
     * Note that this method recursively calls this element's parent's
     * {@link #fireHtmlAttributeRemoved(HtmlAttributeChangeEvent)} method.
     *
     * @param event the event
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    protected void fireHtmlAttributeRemoved(final HtmlAttributeChangeEvent event) {
        synchronized (attributeListeners_) {
            for (final HtmlAttributeChangeListener listener : attributeListeners_) {
                listener.attributeRemoved(event);
            }
        }
        final DomNode parentNode = getParentNode();
        if (parentNode instanceof HtmlElement) {
            ((HtmlElement) parentNode).fireHtmlAttributeRemoved(event);
        }
    }

    /**
     * @return the same value as returned by {@link #getTagName()}
     */
    @Override
    public String getNodeName() {
        final String prefix = getPrefix();
        if (prefix != null) {
            // create string builder only if needed (performance)
            final StringBuilder name = new StringBuilder(prefix.toLowerCase(Locale.ROOT))
                .append(':')
                .append(getLocalName().toLowerCase(Locale.ROOT));
            return name.toString();
        }
        return getLocalName().toLowerCase(Locale.ROOT);
    }

    /**
     * Returns this element's tab index, if it has one. If the tab index is outside of the
     * valid range (less than <tt>0</tt> or greater than <tt>32767</tt>), this method
     * returns {@link #TAB_INDEX_OUT_OF_BOUNDS}. If this element does not have
     * a tab index, or its tab index is otherwise invalid, this method returns {@code null}.
     *
     * @return this element's tab index
     */
    public Short getTabIndex() {
        final String index = getAttributeDirect("tabindex");
        if (index == null || index.isEmpty()) {
            return null;
        }
        try {
            final long l = Long.parseLong(index);
            if (l >= 0 && l <= Short.MAX_VALUE) {
                return Short.valueOf((short) l);
            }
            return TAB_INDEX_OUT_OF_BOUNDS;
        }
        catch (final NumberFormatException e) {
            return null;
        }
    }

    /**
     * Returns the first element with the specified tag name that is an ancestor to this element, or
     * {@code null} if no such element is found.
     * @param tagName the name of the tag searched (case insensitive)
     * @return the first element with the specified tag name that is an ancestor to this element
     */
    public HtmlElement getEnclosingElement(final String tagName) {
        final String tagNameLC = tagName.toLowerCase(Locale.ROOT);

        for (DomNode currentNode = getParentNode(); currentNode != null; currentNode = currentNode.getParentNode()) {
            if (currentNode instanceof HtmlElement && currentNode.getNodeName().equals(tagNameLC)) {
                return (HtmlElement) currentNode;
            }
        }
        return null;
    }

    /**
     * Returns the form which contains this element, or {@code null} if this element is not inside
     * of a form.
     * @return the form which contains this element
     */
    public HtmlForm getEnclosingForm() {
        final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
        if (browserVersion.hasFeature(FORM_SUBMISSION_FORM_ATTRIBUTE)) {
            final String formId = getAttribute("form");
            if (formId != ATTRIBUTE_NOT_DEFINED) {
                final Element formById = getPage().getElementById(formId);
                if (formById instanceof HtmlForm) {
                    return (HtmlForm) formById;
                }
                return null;
            }
        }

        if (owningForm_ != null) {
            return owningForm_;
        }
        return (HtmlForm) getEnclosingElement("form");
    }

    /**
     * Returns the form which contains this element. If this element is not inside a form, this method
     * throws an {@link IllegalStateException}.
     * @return the form which contains this element
     */
    public HtmlForm getEnclosingFormOrDie() {
        final HtmlForm form = getEnclosingForm();
        if (form == null) {
            throw new IllegalStateException("Element is not contained within a form: " + this);
        }
        return form;
    }

    /**
     * Simulates typing the specified text while this element has focus.
     * Note that for some elements, typing '\n' submits the enclosed form.
     * @param text the text you with to simulate typing
     * @exception IOException If an IO error occurs
     */
    public void type(final String text) throws IOException {
        for (final char ch : text.toCharArray()) {
            type(ch);
        }
    }

    /**
     * Simulates typing the specified character while this element has focus, returning the page contained
     * by this element's window after typing. Note that it may or may not be the same as the original page,
     * depending on the JavaScript event handlers, etc. Note also that for some elements, typing <tt>'\n'</tt>
     * submits the enclosed form.
     *
     * @param c the character you wish to simulate typing
     * @return the page that occupies this window after typing
     * @exception IOException if an IO error occurs
     */
    public Page type(final char c) throws IOException {
        return type(c, true);
    }

    /**
     * Simulates typing the specified character while this element has focus, returning the page contained
     * by this element's window after typing. Note that it may or may not be the same as the original page,
     * depending on the JavaScript event handlers, etc. Note also that for some elements, typing <tt>'\n'</tt>
     * submits the enclosed form.
     *
     * @param c the character you wish to simulate typing
     * @param startAtEnd whether typing should start at the text end or not
     * @param lastType is this the last character to type
     * @return the page contained in the current window as returned by {@link WebClient#getCurrentWindow()}
     * @exception IOException if an IO error occurs
     */
    private Page type(final char c, final boolean lastType)
        throws IOException {
        if (isDisabledElementAndDisabled()) {
            return getPage();
        }

        // make enclosing window the current one
        getPage().getWebClient().setCurrentWindow(getPage().getEnclosingWindow());

        final HtmlPage page = (HtmlPage) getPage();
        if (page.getFocusedElement() != this) {
            focus();
        }
        final boolean isShiftNeeded = KeyboardEvent.isShiftNeeded(c, shiftPressed_);

        final Event shiftDown;
        final ScriptResult shiftDownResult;
        if (isShiftNeeded) {
            shiftDown = new KeyboardEvent(this, Event.TYPE_KEY_DOWN, KeyboardEvent.DOM_VK_SHIFT,
                    true, ctrlPressed_, altPressed_);
            shiftDownResult = fireEvent(shiftDown);
        }
        else {
            shiftDown = null;
            shiftDownResult = null;
        }

        final Event keyDown = new KeyboardEvent(this, Event.TYPE_KEY_DOWN, c,
                                                shiftPressed_ || isShiftNeeded, ctrlPressed_, altPressed_);
        final ScriptResult keyDownResult = fireEvent(keyDown);

        if (!keyDown.isAborted(keyDownResult)) {
            final Event keyPress = new KeyboardEvent(this, Event.TYPE_KEY_PRESS, c,
                    shiftPressed_ || isShiftNeeded, ctrlPressed_, altPressed_);
            final ScriptResult keyPressResult = fireEvent(keyPress);

            if ((shiftDown == null || !shiftDown.isAborted(shiftDownResult))
                    && !keyPress.isAborted(keyPressResult)) {
                doType(c, lastType);
            }
        }

        final WebClient webClient = page.getWebClient();
        if (this instanceof HtmlTextInput
                || this instanceof HtmlTextArea
                || this instanceof HtmlTelInput
                || this instanceof HtmlNumberInput
                || this instanceof HtmlSearchInput
                || this instanceof HtmlPasswordInput) {
            fireEvent(new KeyboardEvent(this, Event.TYPE_INPUT, c,
                                        shiftPressed_ || isShiftNeeded, ctrlPressed_, altPressed_));
        }

        HtmlElement eventSource = this;
        if (!isAttachedToPage()) {
            final BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();
            if (browserVersion.hasFeature(HTMLELEMENT_DETACH_ACTIVE_TRIGGERS_NO_KEYUP_EVENT)) {
                eventSource = null;
            }
            else {
                eventSource = page.getBody();
            }
        }

        if (eventSource != null) {
            final Event keyUp = new KeyboardEvent(this, Event.TYPE_KEY_UP, c,
                                                    shiftPressed_ || isShiftNeeded, ctrlPressed_, altPressed_);
            eventSource.fireEvent(keyUp);

            if (isShiftNeeded) {
                final Event shiftUp = new KeyboardEvent(this, Event.TYPE_KEY_UP,
                                        KeyboardEvent.DOM_VK_SHIFT,
                                        false, ctrlPressed_, altPressed_);
                eventSource.fireEvent(shiftUp);
            }
        }

        final HtmlForm form = getEnclosingForm();
        if (form != null && c == '\n' && isSubmittableByEnter()) {
            final HtmlSubmitInput submit = form.getFirstByXPath(".//input[@type='submit']");
            if (submit != null) {
                return submit.click();
            }
            form.submit((SubmittableElement) this);
            webClient.getJavaScriptEngine().processPostponedActions();
        }
        return webClient.getCurrentWindow().getEnclosedPage();
    }

    /**
     * Simulates typing the specified key code while this element has focus, returning the page contained
     * by this element's window after typing. Note that it may or may not be the same as the original page,
     * depending on the JavaScript event handlers, etc. Note also that for some elements, typing <tt>XXXXXXXXXXX</tt>
     * submits the enclosed form.
     *
     * An example of predefined values is {@link KeyboardEvent#DOM_VK_PAGE_DOWN}.
     *
     * @param keyCode the key code to simulate typing
     * @return the page that occupies this window after typing
     */
    public Page type(final int keyCode) {
        return type(keyCode, true, true, true, true);
    }

    /**
     * Simulates typing the specified {@link Keyboard} while this element has focus, returning the page contained
     * by this element's window after typing. Note that it may or may not be the same as the original page,
     * depending on the JavaScript event handlers, etc. Note also that for some elements, typing <tt>XXXXXXXXXXX</tt>
     * submits the enclosed form.
     *
     * @param keyboard the keyboard
     * @return the page that occupies this window after typing
     * @exception IOException if an IO error occurs
     */
    public Page type(final Keyboard keyboard) throws IOException {
        Page page = null;

        final List<Object[]> keys = keyboard.getKeys();

        if (keyboard.isStartAtEnd()) {
            if (this instanceof SelectableTextInput) {
                final SelectableTextInput textInput = (SelectableTextInput) this;
                textInput.setSelectionStart(textInput.getText().length());
            }
            else {
                final DomText domText = getDoTypeNode();
                if (domText != null) {
                    domText.moveSelectionToEnd();
                }
            }
        }

        for (int i = 0; i < keys.size(); i++) {
            final Object[] entry = keys.get(i);
            if (entry.length == 1) {
                type((char) entry[0], i == keys.size() - 1);
            }
            else {
                final int key = (int) entry[0];
                final boolean pressed = (boolean) entry[1];
                switch (key) {
                    case KeyboardEvent.DOM_VK_SHIFT:
                        shiftPressed_ = pressed;
                        break;

                    case KeyboardEvent.DOM_VK_CONTROL:
                        ctrlPressed_ = pressed;
                        break;

                    case KeyboardEvent.DOM_VK_ALT:
                        altPressed_ = pressed;
                        break;

                    default:
                }
                if (pressed) {
                    boolean keyPress = true;
                    boolean keyUp = true;
                    switch (key) {
                        case KeyboardEvent.DOM_VK_SHIFT:
                        case KeyboardEvent.DOM_VK_CONTROL:
                        case KeyboardEvent.DOM_VK_ALT:
                            keyPress = false;
                            keyUp = false;
                            break;

                        default:
                    }
                    page = type(key, true, keyPress, keyUp, i == keys.size() - 1);
                }
                else {
                    page = type(key, false, false, true, i == keys.size() - 1);
                }
            }
        }

        return page;
    }

    private Page type(final int keyCode,
                    final boolean fireKeyDown, final boolean fireKeyPress, final boolean fireKeyUp,
                    final boolean lastType) {
        if (isDisabledElementAndDisabled()) {
            return getPage();
        }

        final HtmlPage page = (HtmlPage) getPage();
        if (page.getFocusedElement() != this) {
            focus();
        }

        final Event keyDown;
        final ScriptResult keyDownResult;
        if (fireKeyDown) {
            keyDown = new KeyboardEvent(this, Event.TYPE_KEY_DOWN, keyCode, shiftPressed_, ctrlPressed_, altPressed_);
            keyDownResult = fireEvent(keyDown);
        }
        else {
            keyDown = null;
            keyDownResult = null;
        }

        final BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();

        final Event keyPress;
        final ScriptResult keyPressResult;
        if (fireKeyPress && browserVersion.hasFeature(KEYBOARD_EVENT_SPECIAL_KEYPRESS)) {
            keyPress = new KeyboardEvent(this, Event.TYPE_KEY_PRESS, keyCode,
                    shiftPressed_, ctrlPressed_, altPressed_);

            keyPressResult = fireEvent(keyPress);
        }
        else {
            keyPress = null;
            keyPressResult = null;
        }

        if (keyDown != null && !keyDown.isAborted(keyDownResult)
                && (keyPress == null || !keyPress.isAborted(keyPressResult))) {
            doType(keyCode, lastType);
        }

        if (this instanceof HtmlTextInput
            || this instanceof HtmlTextArea
            || this instanceof HtmlTelInput
            || this instanceof HtmlNumberInput
            || this instanceof HtmlSearchInput
            || this instanceof HtmlPasswordInput) {
            final Event input = new KeyboardEvent(this, Event.TYPE_INPUT, keyCode,
                    shiftPressed_, ctrlPressed_, altPressed_);
            fireEvent(input);
        }

        if (fireKeyUp) {
            final Event keyUp = new KeyboardEvent(this, Event.TYPE_KEY_UP, keyCode,
                    shiftPressed_, ctrlPressed_, altPressed_);
            fireEvent(keyUp);
        }

//        final HtmlForm form = getEnclosingForm();
//        if (form != null && keyCode == '\n' && isSubmittableByEnter()) {
//            if (!getPage().getWebClient().getBrowserVersion()
//                    .hasFeature(BUTTON_EMPTY_TYPE_BUTTON)) {
//                final HtmlSubmitInput submit = form.getFirstByXPath(".//input[@type='submit']");
//                if (submit != null) {
//                    return submit.click();
//                }
//            }
//            form.submit((SubmittableElement) this);
//            page.getWebClient().getJavaScriptEngine().processPostponedActions();
//        }
        return page.getWebClient().getCurrentWindow().getEnclosedPage();
    }

    /**
     * Performs the effective type action, called after the keyPress event and before the keyUp event.
     * @param c the character you with to simulate typing
     * @param lastType is this the last character to type
     */
    protected void doType(final char c, final boolean lastType) {
        final DomText domText = getDoTypeNode();
        if (domText != null) {
            domText.doType(c, this, lastType);
        }
    }

    /**
     * Performs the effective type action, called after the keyPress event and before the keyUp event.
     *
     * An example of predefined values is {@link KeyboardEvent#DOM_VK_PAGE_DOWN}.
     *
     * @param keyCode the key code wish to simulate typing
     * @param lastType is this the last to type
     */
    protected void doType(final int keyCode, final boolean lastType) {
        final DomText domText = getDoTypeNode();
        if (domText != null) {
            domText.doType(keyCode, this, lastType);
        }
    }

    /**
     * Returns the node to type into.
     * @return the node
     */
    private DomText getDoTypeNode() {
        final HTMLElement scriptElement = getScriptableObject();
        if (scriptElement.isIsContentEditable()
                || "on".equals(((Document) scriptElement.getOwnerDocument()).getDesignMode())) {

            DomNodeList<DomNode> children = getChildNodes();
            while (!children.isEmpty()) {
                final DomNode lastChild = children.get(children.size() - 1);
                if (lastChild instanceof DomText) {
                    return (DomText) lastChild;
                }
                children = lastChild.getChildNodes();
            }

            final DomText domText = new DomText(getPage(), "");
            appendChild(domText);
            return domText;
        }
        return null;
    }

    /**
     * Called from {@link DoTypeProcessor}.
     * @param newValue the new value
     * @param notifyAttributeChangeListeners to notify the associated {@link HtmlAttributeChangeListener}s
     */
    protected void typeDone(final String newValue, final boolean notifyAttributeChangeListeners) {
        // nothing
    }

    /**
     * Indicates if the provided character can by "typed" in the element.
     * @param c the character
     * @return {@code true} if it is accepted
     */
    protected boolean acceptChar(final char c) {
        // This range is this is private use area
        // see http://www.unicode.org/charts/PDF/UE000.pdf
        return (c < '\uE000' || c > '\uF8FF') && (c == ' ' || !Character.isWhitespace(c));
    }

    /**
     * Returns {@code true} if clicking Enter (ASCII 10, or '\n') should submit the enclosed form (if any).
     * The default implementation returns {@code false}.
     * @return {@code true} if clicking Enter should submit the enclosed form (if any)
     */
    protected boolean isSubmittableByEnter() {
        return false;
    }

    /**
     * Searches for an element based on the specified criteria, returning the first element which matches
     * said criteria. Only elements which are descendants of this element are included in the search.
     *
     * @param elementName the name of the element to search for
     * @param attributeName the name of the attribute to search for
     * @param attributeValue the value of the attribute to search for
     * @param <E> the sub-element type
     * @return the first element which matches the specified search criteria
     * @throws ElementNotFoundException if no element matches the specified search criteria
     */
    public final <E extends HtmlElement> E getOneHtmlElementByAttribute(final String elementName,
            final String attributeName,
        final String attributeValue) throws ElementNotFoundException {

        WebAssert.notNull("elementName", elementName);
        WebAssert.notNull("attributeName", attributeName);
        WebAssert.notNull("attributeValue", attributeValue);

        final List<E> list = getElementsByAttribute(elementName, attributeName, attributeValue);

        if (list.isEmpty()) {
            throw new ElementNotFoundException(elementName, attributeName, attributeValue);
        }

        return list.get(0);
    }

    /**
     * Returns all elements which are descendants of this element and match the specified search criteria.
     *
     * @param elementName the name of the element to search for
     * @param attributeName the name of the attribute to search for
     * @param attributeValue the value of the attribute to search for
     * @param <E> the sub-element type
     * @return all elements which are descendants of this element and match the specified search criteria
     */
    @SuppressWarnings("unchecked")
    public final <E extends HtmlElement> List<E> getElementsByAttribute(
            final String elementName,
            final String attributeName,
            final String attributeValue) {

        final List<E> list = new ArrayList<>();
        final String lowerCaseTagName = elementName.toLowerCase(Locale.ROOT);

        for (final HtmlElement next : getHtmlElementDescendants()) {
            if (next.getTagName().equals(lowerCaseTagName)) {
                final String attValue = next.getAttribute(attributeName);
                if (attValue != null && attValue.equals(attributeValue)) {
                    list.add((E) next);
                }
            }
        }
        return list;
    }

    /**
     * Appends a child element to this HTML element with the specified tag name
     * if this HTML element does not already have a child with that tag name.
     * Returns the appended child element, or the first existent child element
     * with the specified tag name if none was appended.
     * @param tagName the tag name of the child to append
     * @return the added child, or the first existing child if none was added
     */
    public final HtmlElement appendChildIfNoneExists(final String tagName) {
        final HtmlElement child;
        final List<HtmlElement> children = getElementsByTagName(tagName);
        if (children.isEmpty()) {
            // Add a new child and return it.
            child = (HtmlElement) ((HtmlPage) getPage()).createElement(tagName);
            appendChild(child);
        }
        else {
            // Return the first existing child.
            child = children.get(0);
        }
        return child;
    }

    /**
     * Removes the <tt>i</tt>th child element with the specified tag name
     * from all relationships, if possible.
     * @param tagName the tag name of the child to remove
     * @param i the index of the child to remove
     */
    public final void removeChild(final String tagName, final int i) {
        final List<HtmlElement> children = getElementsByTagName(tagName);
        if (i >= 0 && i < children.size()) {
            children.get(i).remove();
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * Returns {@code true} if this element has any JavaScript functions that need to be executed when the
     * specified event occurs.
     * @param eventName the name of the event, such as "onclick" or "onblur", etc
     * @return true if an event handler has been defined otherwise false
     */
    public final boolean hasEventHandlers(final String eventName) {
        if (getPage().getWebClient().isJavaScriptEngineEnabled()) {
            final Object jsObj = getScriptableObject();
            if (jsObj instanceof EventTarget) {
                return ((EventTarget) jsObj).hasEventHandlers(eventName);
            }
        }
        return false;
    }

    /**
     * Adds an HtmlAttributeChangeListener to the listener list.
     * The listener is registered for all attributes of this HtmlElement,
     * as well as descendant elements.
     *
     * @param listener the attribute change listener to be added
     * @see #removeHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void addHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (attributeListeners_) {
            attributeListeners_.add(listener);
        }
    }

    /**
     * Removes an HtmlAttributeChangeListener from the listener list.
     * This method should be used to remove HtmlAttributeChangeListener that were registered
     * for all attributes of this HtmlElement, as well as descendant elements.
     *
     * @param listener the attribute change listener to be removed
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void removeHtmlAttributeChangeListener(final HtmlAttributeChangeListener listener) {
        WebAssert.notNull("listener", listener);
        synchronized (attributeListeners_) {
            attributeListeners_.remove(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkChildHierarchy(final Node childNode) throws DOMException {
        if (!((childNode instanceof Element) || (childNode instanceof Text)
            || (childNode instanceof Comment) || (childNode instanceof ProcessingInstruction)
            || (childNode instanceof CDATASection) || (childNode instanceof EntityReference))) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "The Element may not have a child of this type: " + childNode.getNodeType());
        }
        super.checkChildHierarchy(childNode);
    }

    void setOwningForm(final HtmlForm form) {
        owningForm_ = form;
    }

    /**
     * Indicates if the attribute names are case sensitive.
     * @return {@code false}
     */
    @Override
    protected boolean isAttributeCaseSensitive() {
        return false;
    }

    /**
     * Returns the value of the attribute {@code lang}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code lang} or an empty string if that attribute isn't defined
     */
    public final String getLangAttribute() {
        return getAttributeDirect("lang");
    }

    /**
     * Returns the value of the attribute {@code xml:lang}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code xml:lang} or an empty string if that attribute isn't defined
     */
    public final String getXmlLangAttribute() {
        return getAttribute("xml:lang");
    }

    /**
     * Returns the value of the attribute {@code dir}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code dir} or an empty string if that attribute isn't defined
     */
    public final String getTextDirectionAttribute() {
        return getAttributeDirect("dir");
    }

    /**
     * Returns the value of the attribute {@code onclick}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onclick} or an empty string if that attribute isn't defined
     */
    public final String getOnClickAttribute() {
        return getAttributeDirect("onclick");
    }

    /**
     * Returns the value of the attribute {@code ondblclick}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code ondblclick} or an empty string if that attribute isn't defined
     */
    public final String getOnDblClickAttribute() {
        return getAttributeDirect("ondblclick");
    }

    /**
     * Returns the value of the attribute {@code onmousedown}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onmousedown} or an empty string if that attribute isn't defined
     */
    public final String getOnMouseDownAttribute() {
        return getAttributeDirect("onmousedown");
    }

    /**
     * Returns the value of the attribute {@code onmouseup}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onmouseup} or an empty string if that attribute isn't defined
     */
    public final String getOnMouseUpAttribute() {
        return getAttributeDirect("onmouseup");
    }

    /**
     * Returns the value of the attribute {@code onmouseover}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onmouseover} or an empty string if that attribute isn't defined
     */
    public final String getOnMouseOverAttribute() {
        return getAttributeDirect("onmouseover");
    }

    /**
     * Returns the value of the attribute {@code onmousemove}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onmousemove} or an empty string if that attribute isn't defined
     */
    public final String getOnMouseMoveAttribute() {
        return getAttributeDirect("onmousemove");
    }

    /**
     * Returns the value of the attribute {@code onmouseout}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onmouseout} or an empty string if that attribute isn't defined
     */
    public final String getOnMouseOutAttribute() {
        return getAttributeDirect("onmouseout");
    }

    /**
     * Returns the value of the attribute {@code onkeypress}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onkeypress} or an empty string if that attribute isn't defined
     */
    public final String getOnKeyPressAttribute() {
        return getAttributeDirect("onkeypress");
    }

    /**
     * Returns the value of the attribute {@code onkeydown}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onkeydown} or an empty string if that attribute isn't defined
     */
    public final String getOnKeyDownAttribute() {
        return getAttributeDirect("onkeydown");
    }

    /**
     * Returns the value of the attribute {@code onkeyup}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onkeyup} or an empty string if that attribute isn't defined
     */
    public final String getOnKeyUpAttribute() {
        return getAttributeDirect("onkeyup");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCanonicalXPath() {
        final DomNode parent = getParentNode();
        if (parent.getNodeType() == DOCUMENT_NODE) {
            return "/" + getNodeName();
        }
        return parent.getCanonicalXPath() + '/' + getXPathToken();
    }

    /**
     * Returns the XPath token for this node only.
     */
    private String getXPathToken() {
        final DomNode parent = getParentNode();
        int total = 0;
        int nodeIndex = 0;
        for (final DomNode child : parent.getChildren()) {
            if (child.getNodeType() == ELEMENT_NODE && child.getNodeName().equals(getNodeName())) {
                total++;
            }
            if (child == this) {
                nodeIndex = total;
            }
        }

        if (nodeIndex == 1 && total == 1) {
            return getNodeName();
        }
        return getNodeName() + '[' + nodeIndex + ']';
    }

    /**
     * @return true if the hidden attribute is set.
     */
    public boolean isHidden() {
        return ATTRIBUTE_NOT_DEFINED != getAttributeDirect("hidden");
    }

    /**
     * {@inheritDoc}
     * Overwritten to support the hidden attribute (html5).
     */
    @Override
    public boolean isDisplayed() {
        if (isHidden()) {
            return false;
        }
        return super.isDisplayed();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns the default display style.
     *
     * @return the default display style
     */
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.BLOCK;
    }

    /**
     * Helper for src retrieval and normalization.
     *
     * @return the value of the attribute {@code src} with all line breaks removed
     * or an empty string if that attribute isn't defined.
     */
    protected final String getSrcAttributeNormalized() {
        // at the moment StringUtils.replaceChars returns the org string
        // if nothing to replace was found but the doc implies, that we
        // can't trust on this in the future
        final String attrib = getAttributeDirect(SRC_ATTRIBUTE);
        if (ATTRIBUTE_NOT_DEFINED == attrib) {
            return attrib;
        }

        return StringUtils.replaceChars(attrib, "\r\n", "");
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Detach this node from all relationships with other nodes.
     * This is the first step of a move.
     */
    @Override
    protected void detach() {
        final SgmlPage page = getPage();
        if (!page.getWebClient().isJavaScriptEngineEnabled()) {
            super.detach();
            return;
        }

        final Object document = page.getScriptableObject();

        if (document instanceof HTMLDocument) {
            final HTMLDocument doc = (HTMLDocument) document;
            final Object activeElement = doc.getActiveElement();

            if (activeElement == getScriptableObject()) {
                doc.setActiveElement(null);
                if (hasFeature(HTMLELEMENT_REMOVE_ACTIVE_TRIGGERS_BLUR_EVENT)) {
                    ((HtmlPage) page).setFocusedElement(null);
                }
                else {
                    ((HtmlPage) page).setElementWithFocus(null);
                }
            }
            else {
                for (final DomNode child : getChildNodes()) {
                    if (activeElement == child.getScriptableObject()) {
                        doc.setActiveElement(null);
                        if (hasFeature(HTMLELEMENT_REMOVE_ACTIVE_TRIGGERS_BLUR_EVENT)) {
                            ((HtmlPage) page).setFocusedElement(null);
                        }
                        else {
                            ((HtmlPage) page).setElementWithFocus(null);
                        }

                        break;
                    }
                }
            }
        }
        super.detach();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (Event.TYPE_BLUR.equals(event.getType()) || Event.TYPE_FOCUS.equals(event.getType())) {
            return this instanceof SubmittableElement || getTabIndex() != null;
        }

        if (isDisabledElementAndDisabled()) {
            return false;
        }
        return super.handles(event);
    }

    /**
     * Returns whether the {@code SHIFT} is currently pressed.
     * @return whether the {@code SHIFT} is currently pressed
     */
    protected boolean isShiftPressed() {
        return shiftPressed_;
    }

    /**
     * Returns whether the {@code CTRL} is currently pressed.
     * @return whether the {@code CTRL} is currently pressed
     */
    public boolean isCtrlPressed() {
        return ctrlPressed_;
    }

    /**
     * Returns whether the {@code ALT} is currently pressed.
     * @return whether the {@code ALT} is currently pressed
     */
    public boolean isAltPressed() {
        return altPressed_;
    }

    /**
     * Returns whether this element satisfies all form validation constraints set.
     * @return whether this element satisfies all form validation constraints set
     */
    public boolean isValid() {
        return !isRequiredSupported() || getAttributeDirect("required") == ATTRIBUTE_NOT_DEFINED
                || !getAttributeDirect("value").isEmpty();
    }

    /**
     * Returns whether this element supports the {@code required} constraint.
     * @return whether this element supports the {@code required} constraint
     */
    protected boolean isRequiredSupported() {
        return false;
    }

    /**
     * Returns the {@code required} attribute.
     * @return the {@code required} attribute
     */
    public boolean isRequired() {
        return isRequiredSupported() && hasAttribute("required");
    }

    /**
     * Sets the {@code required} attribute.
     * @param required the new attribute value
     */
    public void setRequired(final boolean required) {
        if (isRequiredSupported()) {
            if (required) {
                setAttribute("required", "required");
            }
            else {
                removeAttribute("required");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final HtmlElement newnode = (HtmlElement) super.cloneNode(deep);
        synchronized (attributeListeners_) {
            newnode.attributeListeners_ = new LinkedHashSet<>(attributeListeners_);
        }

        return newnode;
    }
}
