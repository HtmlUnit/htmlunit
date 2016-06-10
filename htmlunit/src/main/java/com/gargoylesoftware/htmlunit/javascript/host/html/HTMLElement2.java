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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement.DisplayStyle;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect2;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

public class HTMLElement2 extends Element2 {

    private static final Log LOG = LogFactory.getLog(HTMLElement2.class);

    private static final int BEHAVIOR_ID_UNKNOWN = -1;
    /** BEHAVIOR_ID_CLIENT_CAPS. */
    public static final int BEHAVIOR_ID_CLIENT_CAPS = 0;
    /** BEHAVIOR_ID_HOMEPAGE. */
    public static final int BEHAVIOR_ID_HOMEPAGE = 1;
    /** BEHAVIOR_ID_DOWNLOAD. */
    public static final int BEHAVIOR_ID_DOWNLOAD = 2;

    private static final String BEHAVIOR_CLIENT_CAPS = "#default#clientCaps";
    private static final String BEHAVIOR_HOMEPAGE = "#default#homePage";
    private static final String BEHAVIOR_DOWNLOAD = "#default#download";

    static final String POSITION_BEFORE_BEGIN = "beforebegin";
    static final String POSITION_AFTER_BEGIN = "afterbegin";
    static final String POSITION_BEFORE_END = "beforeend";
    static final String POSITION_AFTER_END = "afterend";

    /**
     * Static counter for {@link #uniqueID_}.
     */
    private static int UniqueID_Counter_ = 1;

    private final Set<String> behaviors_ = new HashSet<>();
    private long scrollLeft_;
    private long scrollTop_;
    private String uniqueID_;

    public static HTMLElement2 constructor(final boolean newObj, final Object self) {
        final HTMLElement2 host = new HTMLElement2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Adds the specified behavior to this HTML element. Currently only supports
     * the following default IE behaviors:
     * <ul>
     *   <li>#default#clientCaps</li>
     *   <li>#default#homePage</li>
     *   <li>#default#download</li>
     * </ul>
     * @param behavior the URL of the behavior to add, or a default behavior name
     * @return an identifier that can be user later to detach the behavior from the element
     */
    public int addBehavior(final String behavior) {
        // if behavior already defined, then nothing to do
        if (behaviors_.contains(behavior)) {
            return 0;
        }

        final Class<? extends HTMLElement2> c = getClass();
        if (BEHAVIOR_CLIENT_CAPS.equalsIgnoreCase(behavior)) {
//            defineProperty("availHeight", c, 0);
//            defineProperty("availWidth", c, 0);
//            defineProperty("bufferDepth", c, 0);
//            defineProperty("colorDepth", c, 0);
//            defineProperty("connectionType", c, 0);
//            defineProperty("cookieEnabled", c, 0);
//            defineProperty("cpuClass", c, 0);
//            defineProperty("height", c, 0);
//            defineProperty("javaEnabled", c, 0);
//            defineProperty("platform", c, 0);
//            defineProperty("systemLanguage", c, 0);
//            defineProperty("userLanguage", c, 0);
//            defineProperty("width", c, 0);
//            defineFunctionProperties(new String[] {"addComponentRequest"}, c, 0);
//            defineFunctionProperties(new String[] {"clearComponentRequest"}, c, 0);
//            defineFunctionProperties(new String[] {"compareVersions"}, c, 0);
//            defineFunctionProperties(new String[] {"doComponentRequest"}, c, 0);
//            defineFunctionProperties(new String[] {"getComponentVersion"}, c, 0);
//            defineFunctionProperties(new String[] {"isComponentInstalled"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_CLIENT_CAPS;
        }
        else if (BEHAVIOR_HOMEPAGE.equalsIgnoreCase(behavior)) {
//            defineFunctionProperties(new String[] {"isHomePage"}, c, 0);
//            defineFunctionProperties(new String[] {"setHomePage"}, c, 0);
//            defineFunctionProperties(new String[] {"navigateHomePage"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_HOMEPAGE;
        }
        else if (BEHAVIOR_DOWNLOAD.equalsIgnoreCase(behavior)) {
//            defineFunctionProperties(new String[] {"startDownload"}, c, 0);
//            behaviors_.add(BEHAVIOR_DOWNLOAD);
            return BEHAVIOR_ID_DOWNLOAD;
        }
        else {
            LOG.warn("Unimplemented behavior: " + behavior);
            return BEHAVIOR_ID_UNKNOWN;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlElement getDomNodeOrDie() {
        return (HtmlElement) super.getDomNodeOrDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlElement getDomNodeOrNull() {
        return (HtmlElement) super.getDomNodeOrNull();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @Getter
    public Element2 getParentElement() {
        return super.getParentElement();
    }

    /**
     * Returns the default display style.
     *
     * @return the default display style
     */
    public final String getDefaultStyleDisplay() {
        final HtmlElement htmlElt = getDomNodeOrDie();
        return htmlElt.getDefaultStyleDisplay().value();
    }

    /**
     * Retrieves an object that specifies the bounds of a collection of TextRectangle objects.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536433.aspx">MSDN doc</a>
     * @return an object that specifies the bounds of a collection of TextRectangle objects
     */
    @Override
    public ClientRect2 getBoundingClientRect() {
        final ClientRect2 textRectangle = super.getBoundingClientRect();

        int left = getPosX();
        int top = getPosY();

        // account for any scrolled ancestors
        Object parentNode = getOffsetParentInternal(false);
        while (parentNode != null
                && (parentNode instanceof HTMLElement)
                && !(parentNode instanceof HTMLBodyElement)) {
            final HTMLElement elem = (HTMLElement) parentNode;
            left -= elem.getScrollLeft();
            top -= elem.getScrollTop();

            parentNode = elem.getParentNode();
        }

        textRectangle.setBottom(top + getOffsetHeight());
        textRectangle.setLeft(left);
        textRectangle.setRight(left + getOffsetWidth());
        textRectangle.setTop(top);
        return textRectangle;
    }

    /**
     * Returns this element's <tt>offsetWidth</tt>, which is the element width plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <tt>offsetWidth</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534304.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    @Getter
    public int getOffsetWidth() {
        if (isDisplayNone() || !getDomNodeOrDie().isAttachedToPage()) {
            return 0;
        }

//        final MouseEvent2 event = MouseEvent2.getCurrentMouseEvent();
//        if (isAncestorOfEventTarget(event)) {
//            // compute appropriate offset width to pretend mouse event was produced within this element
//            return event.getClientX() - getPosX() + 50;
//        }
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getCalculatedWidth(true, true);
    }

    /**
     * Returns this element's <tt>offsetHeight</tt>, which is the element height plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <tt>offsetHeight</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534199.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    @Getter
    public final int getOffsetHeight() {
        if (isDisplayNone() || !getDomNodeOrDie().isAttachedToPage()) {
            return 0;
        }
//        final MouseEvent2 event = MouseEvent2.getCurrentMouseEvent();
//        if (isAncestorOfEventTarget(event)) {
//            // compute appropriate offset height to pretend mouse event was produced within this element
//            return event.getClientY() - getPosY() + 50;
//        }
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global,this, null);
        return style.getCalculatedHeight(true, true);
    }

    /**
     * Returns whether the {@code display} is {@code none} or not.
     * @return whether the {@code display} is {@code none} or not
     */
    protected final boolean isDisplayNone() {
        HTMLElement2 element = this;
        while (element != null) {
            final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow().getScriptContext());
            final CSSStyleDeclaration2 style = Window2.getComputedStyle(global, element, null);
            final String display = style.getDisplay();
            if (DisplayStyle.NONE.value().equals(display)) {
                return true;
            }
            element = element.getParentHTMLElement();
        }
        return false;
    }

    /**
     * Returns this element's X position.
     * @return this element's X position
     */
    public int getPosX() {
        int cumulativeOffset = 0;
        HTMLElement2 element = this;
        while (element != null) {
            cumulativeOffset += element.getOffsetLeft();
            if (element != this) {
                final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow().getScriptContext());
                final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, element, null);
                cumulativeOffset += style.getBorderLeftValue();
            }
            element = element.getOffsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Returns this element's Y position.
     * @return this element's Y position
     */
    public int getPosY() {
        int cumulativeOffset = 0;
        HTMLElement2 element = this;
        while (element != null) {
            cumulativeOffset += element.getOffsetTop();
            if (element != this) {
                final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow().getScriptContext());
                final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, element, null);
                cumulativeOffset += style.getBorderTopValue();
            }
            element = element.getOffsetParent();
        }
        return cumulativeOffset;
    }

    private Object getOffsetParentInternal(final boolean returnNullIfFixed) {
        DomNode currentElement = getDomNodeOrDie();

        if (currentElement.getParentNode() == null) {
            return null;
        }

        Object offsetParent = null;
        final HTMLElement2 htmlElement = (HTMLElement2) currentElement.getScriptObject2();
        if (returnNullIfFixed && "fixed".equals(htmlElement.getStyle().getStyleAttribute(
                StyleAttributes.Definition.POSITION, true))) {
            return null;
        }

        final Global global = NashornJavaScriptEngine.getGlobal(htmlElement.getWindow().getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, htmlElement, null);
        final String position = style.getPositionWithInheritance();
        final boolean staticPos = "static".equals(position);

        final boolean useTables = staticPos;

        while (currentElement != null) {

            final DomNode parentNode = currentElement.getParentNode();
            if (parentNode instanceof HtmlBody
                || (useTables && parentNode instanceof HtmlTableDataCell)
                || (useTables && parentNode instanceof HtmlTable)) {
                offsetParent = parentNode.getScriptObject2();
                break;
            }

            if (parentNode != null && parentNode.getScriptObject2() instanceof HTMLElement2) {
                final HTMLElement2 parentElement = (HTMLElement2) parentNode.getScriptObject2();
                final ComputedCSSStyleDeclaration2 parentStyle = Window2.getComputedStyle(global, parentElement, null);
                final String parentPosition = parentStyle.getPositionWithInheritance();
                final boolean parentIsStatic = "static".equals(parentPosition);
                if (!parentIsStatic) {
                    offsetParent = parentNode.getScriptableObject();
                    break;
                }
            }

            currentElement = currentElement.getParentNode();
        }

        return offsetParent;
    }

    /**
     * Returns this element's <tt>offsetLeft</tt>, which is the calculated left position of this
     * element relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetLeft</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534200.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @Getter
    public int getOffsetLeft() {
        if (this instanceof HTMLBodyElement2) {
            return 0;
        }

        int left = 0;
        final HTMLElement2 offsetParent = getOffsetParent();

        // Add the offset for this node.
        DomNode node = getDomNodeOrDie();
        HTMLElement2 element = (HTMLElement2) node.getScriptObject2();
        final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow().getScriptContext());
        ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, element, null);
        left += style.getLeft(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = style.getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return left;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptObject2() != offsetParent) {
            if (node.getScriptObject2() instanceof HTMLElement2) {
                element = (HTMLElement2) node.getScriptObject2();
                style = Window2.getComputedStyle(global, element, null);
                left += style.getLeft(true, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            style = Window2.getComputedStyle(global, offsetParent, null);
            left += style.getMarginLeftValue();
            left += style.getPaddingLeftValue();
        }

        return left;
    }

    /**
     * Gets the offset parent or {@code null} if this is not an {@link HTMLElement}.
     * @return the offset parent or {@code null}
     */
    private HTMLElement2 getOffsetParent() {
        final Object offsetParent = getOffsetParentInternal(false);
        if (offsetParent instanceof HTMLElement2) {
            return (HTMLElement2) offsetParent;
        }
        return null;
    }

    /**
     * Returns the {@code clientLeft} attribute.
     * @return the {@code clientLeft} attribute
     */
    @Getter
    public int getClientLeft() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getBorderLeftValue();
    }

    /**
     * Returns {@code clientTop} attribute.
     * @return the {@code clientTop} attribute
     */
    @Getter
    public int getClientTop() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getBorderTopValue();
    }

    /**
     * Gets the first ancestor instance of {@link HTMLElement}. It is mostly identical
     * to {@link #getParent()} except that it skips XML nodes.
     * @return the parent HTML element
     * @see #getParent()
     */
    public HTMLElement2 getParentHTMLElement() {
        Node2 parent = getParent();
        while (parent != null && !(parent instanceof HTMLElement2)) {
            parent = parent.getParent();
        }
        return (HTMLElement2) parent;
    }

    /**
     * Returns this element's {@code offsetTop}, which is the calculated top position of this
     * element relative to the {@code offsetParent}.
     *
     * @return this element's {@code offsetTop}
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534303.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @Getter
    public int getOffsetTop() {
        if (this instanceof HTMLBodyElement2) {
            return 0;
        }

        int top = 0;
        final HTMLElement2 offsetParent = getOffsetParent();

        // Add the offset for this node.
        DomNode node = getDomNodeOrDie();
        HTMLElement2 element = (HTMLElement2) node.getScriptObject2();
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, element, null);
        top += style.getTop(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = style.getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return top;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptObject2() != offsetParent) {
            if (node.getScriptObject2() instanceof HTMLElement2) {
                element = (HTMLElement2) node.getScriptObject2();
                style = Window2.getComputedStyle(global, element, null);
                top += style.getTop(false, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            final HTMLElement2 thiz = (HTMLElement2) getDomNodeOrDie().getScriptObject2();
            style = Window2.getComputedStyle(global, thiz, null);
            final boolean thisElementHasTopMargin = style.getMarginTopValue() != 0;

            style = Window2.getComputedStyle(global, offsetParent, null);
            if (!thisElementHasTopMargin) {
                top += style.getMarginTopValue();
            }
            top += style.getPaddingTopValue();
        }

        return top;
    }

    /**
     * Returns the {@code clientHeight} attribute.
     * @return the {@code clientHeight} attribute
     */
    @Getter
    public int getClientHeight() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getCalculatedHeight(false, true);
    }

    /**
     * Returns the {@code clientWidth} attribute.
     * @return the {@code clientWidth} attribute
     */
    @Getter
    public int getClientWidth() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow().getScriptContext());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global,this, null);
        return style.getCalculatedWidth(false, true);
    }

    /**
     * Sets the object as active without setting focus to the object.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536738.aspx">MSDN documentation</a>
     */
    @Function(@WebBrowser(IE))
    public void setActive() {
        final Window2 window = getWindow();
        final HTMLDocument2 document = (HTMLDocument2) Window2.getDocument(window);
        document.setActiveElement(this);
        if (window.getWebWindow() == window.getWebWindow().getWebClient().getCurrentWindow()) {
            final HtmlElement element = getDomNodeOrDie();
            ((HtmlPage) element.getPage()).setFocusedElement(element);
        }
    }

    /**
     * Gets the scrollTop value for this element.
     * @return the scrollTop value for this element
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534618.aspx">MSDN documentation</a>
     */
    @Getter
    public Long getScrollTop() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollTop_ < 0) {
            scrollTop_ = 0;
        }
        else if (scrollTop_ > 0) {
            final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(getWindow(), this, null);
            if (!style.isScrollable(false)) {
                scrollTop_ = 0;
            }
        }
        return scrollTop_;
    }

    /**
     * Sets the scrollTop value for this element.
     * @param scroll the scrollTop value for this element
     */
    @Setter
    public void setScrollTop(final long scroll) {
        scrollTop_ = scroll;
    }

    /**
     * Gets the scrollLeft value for this element.
     * @return the scrollLeft value for this element
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534617.aspx">MSDN documentation</a>
     */
    @Getter
    public Long getScrollLeft() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollLeft_ < 0) {
            scrollLeft_ = 0;
        }
        else if (scrollLeft_ > 0) {
            final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(getWindow(), this, null);
            if (!style.isScrollable(true)) {
                scrollLeft_ = 0;
            }
        }
        return scrollLeft_;
    }

    /**
     * Sets the scrollLeft value for this element.
     * @param scroll the scrollLeft value for this element
     */
    @Setter
    public void setScrollLeft(final long scroll) {
        scrollLeft_ = scroll;
    }

    /**
     * Gets the scrollHeight for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534615.aspx">MSDN documentation</a>
     */
    @Getter
    public int getScrollHeight() {
        return 10;
    }

    /**
     * Gets the scrollWidth for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534619.aspx">MSDN documentation</a>
     */
    @Getter
    public int getScrollWidth() {
        return 10;
    }

    /**
     * Returns the element ID.
     * @return the ID of this element
     */
    @Getter
    public String getId() {
        return getDomNodeOrDie().getId();
    }

    /**
     * Sets the identifier this element.
     * @param newId the new identifier of this element
     */
    @Setter
    public void setId(final String newId) {
        getDomNodeOrDie().setId(newId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        final DomNode domNode = getDomNodeOrDie();
        String nodeName = domNode.getNodeName();
        if (domNode.getHtmlPageOrNull() != null) {
            nodeName = nodeName.toUpperCase(Locale.ROOT);
        }
        return nodeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return null;
    }

    /**
     * Parses the given text as HTML or XML and inserts the resulting nodes into the tree in the position given by the
     * position argument.
     * @param position specifies where to insert the nodes, using one of the following values (case-insensitive):
     *        <code>beforebegin</code>, <code>afterbegin</code>, <code>beforeend</code>, <code>afterend</code>
     * @param text the text to parse
     *
     * @see <a href="http://www.w3.org/TR/DOM-Parsing/#methods-2">W3C Spec</a>
     * @see <a href="http://domparsing.spec.whatwg.org/#dom-element-insertadjacenthtml">WhatWG Spec</a>
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element.insertAdjacentHTML"
     *      >Mozilla Developer Network</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536452.aspx">MSDN</a>
     */
    @Function
    public void insertAdjacentHTML(final String position, final String text) {
        final Object[] values = getInsertAdjacentLocation(position);
        final DomNode domNode = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        // add the new nodes
        final DomNode proxyDomNode = new ProxyDomNode(domNode.getPage(), domNode, append);
        parseHtmlSnippet(proxyDomNode, text);
    }

    /**
     * Inserts the given element into the element at the location.
     * @param where specifies where to insert the element, using one of the following values (case-insensitive):
     *        beforebegin, afterbegin, beforeend, afterend
     * @param insertedElement the element to be inserted
     * @return an element object
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536451.aspx">MSDN</a>
     */
    @Function({@WebBrowser(CHROME), @WebBrowser(IE)})
    public Object insertAdjacentElement(final String where, final Object insertedElement) {
        if (insertedElement instanceof Node) {
            final DomNode childNode = ((Node) insertedElement).getDomNodeOrDie();
            final Object[] values = getInsertAdjacentLocation(where);
            final DomNode node = (DomNode) values[0];
            final boolean append = ((Boolean) values[1]).booleanValue();

            if (append) {
                node.appendChild(childNode);
            }
            else {
                node.insertBefore(childNode);
            }
            return insertedElement;
        }
        throw new RuntimeException("Passed object is not an element: " + insertedElement);
    }

    /**
     * Inserts the given text into the element at the specified location.
     * @param where specifies where to insert the text, using one of the following values (case-insensitive):
     *      beforebegin, afterbegin, beforeend, afterend
     * @param text the text to insert
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536453.aspx">MSDN</a>
     */
    @Function({@WebBrowser(CHROME), @WebBrowser(IE)})
    public void insertAdjacentText(final String where, final String text) {
        final Object[] values = getInsertAdjacentLocation(where);
        final DomNode node = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        final DomText domText = new DomText(node.getPage(), text);
        // add the new nodes
        if (append) {
            node.appendChild(domText);
        }
        else {
            node.insertBefore(domText);
        }
    }

    /**
     * Returns where and how to add the new node.
     * Used by {@link #insertAdjacentHTML(String, String)},
     * {@link #insertAdjacentElement(String, Object)} and
     * {@link #insertAdjacentText(String, String)}.
     * @param where specifies where to insert the element, using one of the following values (case-insensitive):
     *        beforebegin, afterbegin, beforeend, afterend
     * @return an array of 1-DomNode:parentNode and 2-Boolean:append
     */
    private Object[] getInsertAdjacentLocation(final String where) {
        final DomNode currentNode = getDomNodeOrDie();
        final DomNode node;
        final boolean append;

        // compute the where and how the new nodes should be added
        if (POSITION_AFTER_BEGIN.equalsIgnoreCase(where)) {
            if (currentNode.getFirstChild() == null) {
                // new nodes should appended to the children of current node
                node = currentNode;
                append = true;
            }
            else {
                // new nodes should be inserted before first child
                node = currentNode.getFirstChild();
                append = false;
            }
        }
        else if (POSITION_BEFORE_BEGIN.equalsIgnoreCase(where)) {
            // new nodes should be inserted before current node
            node = currentNode;
            append = false;
        }
        else if (POSITION_BEFORE_END.equalsIgnoreCase(where)) {
            // new nodes should appended to the children of current node
            node = currentNode;
            append = true;
        }
        else if (POSITION_AFTER_END.equalsIgnoreCase(where)) {
            if (currentNode.getNextSibling() == null) {
                // new nodes should appended to the children of parent node
                node = currentNode.getParentNode();
                append = true;
            }
            else {
                // new nodes should be inserted before current node's next sibling
                node = currentNode.getNextSibling();
                append = false;
            }
        }
        else {
            throw new RuntimeException("Illegal position value: \"" + where + "\"");
        }

        if (append) {
            return new Object[] {node, Boolean.TRUE};
        }
        return new Object[] {node, Boolean.FALSE};
    }

    /**
     * Parses the specified HTML source code, appending the resulting content at the specified target location.
     * @param target the node indicating the position at which the parsed content should be placed
     * @param source the HTML code extract to parse
     */
    private static void parseHtmlSnippet(final DomNode target, final String source) {
        try {
            HTMLParser.parseFragment(target, source);
        }
        catch (final IOException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw new RuntimeException("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
        catch (final SAXException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw new RuntimeException("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
    }

    /**
     * ProxyDomNode.
     */
    public static class ProxyDomNode extends HtmlDivision {

        private final DomNode target_;
        private final boolean append_;

        /**
         * Constructor.
         * @param page the page
         * @param target the target
         * @param append append or no
         */
        public ProxyDomNode(final SgmlPage page, final DomNode target, final boolean append) {
            super(HtmlDivision.TAG_NAME, page, null);
            target_ = target;
            append_ = append;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DomNode appendChild(final org.w3c.dom.Node node) {
            final DomNode domNode = (DomNode) node;
            if (append_) {
                return target_.appendChild(domNode);
            }
            target_.insertBefore(domNode);
            return domNode;
        }

        /**
         * Gets wrapped DomNode.
         * @return the node
         */
        public DomNode getDomNode() {
            return target_;
        }

        /**
         * Returns append or not.
         * @return append or not
         */
        public boolean isAppend() {
            return append_;
        }
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLElement2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @ClassConstructor({@WebBrowser(CHROME), @WebBrowser(FF)})
    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("HTMLElement", 
                    staticHandle("constructor", HTMLElement2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("HTMLElement");
        }
    }

    @ClassConstructor(@WebBrowser(IE))
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        public ObjectConstructor() {
            super("HTMLElement");
        }
    }
}
