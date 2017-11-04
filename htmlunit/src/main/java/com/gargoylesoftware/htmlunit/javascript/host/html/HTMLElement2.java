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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_CR_NL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_VALUE_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_NULL_AS_STRING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_REMOVES_CHILDREN_FOR_DETACHED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_THROWS_FOR_DETACHED;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomElement;
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
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect2;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * The JavaScript object {@code HTMLElement} which is the base class for all HTML
 * objects. This will typically wrap an instance of {@link HtmlElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Hans Donner
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@ScriptClass
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

    private static final Pattern CLASS_NAMES_SPLIT_PATTERN = Pattern.compile("\\s");
    private static final Pattern PRINT_NODE_PATTERN = Pattern.compile("  ");
    private static final Pattern PRINT_NODE_QUOTE_PATTERN = Pattern.compile("\"");

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

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
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
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
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
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getCalculatedHeight(true, true);
    }

    /**
     * Returns whether the {@code display} is {@code none} or not.
     * @return whether the {@code display} is {@code none} or not
     */
    protected final boolean isDisplayNone() {
        HTMLElement2 element = this;
        while (element != null) {
            final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow());
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
                final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow());
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
                final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow());
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

        final Global global = NashornJavaScriptEngine.getGlobal(htmlElement.getWindow().getWebWindow());
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
        final Global global = NashornJavaScriptEngine.getGlobal(element.getWindow().getWebWindow());
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
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
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
     * Sets the object as active without setting focus to the object.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536738.aspx">MSDN documentation</a>
     */
    @Function(IE)
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
    @Function({CHROME, IE})
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
    @Function({CHROME, IE})
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

    /**
     * Sets an attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-F68F082">
     * the DOM reference</a>
     *
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    @Override
    public void setAttribute(String name, final String value) {
        getDomNodeOrDie().setAttribute(name, value);

        // call corresponding event handler setOnxxx if found
        if (!name.isEmpty()) {
            name = name.toLowerCase(Locale.ROOT);
            if (name.startsWith("on")) {
//                try {
//                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
//                    final Method method = getClass().getMethod("set" + name, METHOD_PARAMS_OBJECT);
//                    method.invoke(this, new Object[] {new EventHandler(getDomNodeOrDie(), name.substring(2), value)});
//                }
//                catch (final NoSuchMethodException e) {
//                    //silently ignore
//                }
//                catch (final IllegalAccessException e) {
//                    //silently ignore
//                }
//                catch (final InvocationTargetException e) {
//                    throw new RuntimeException(e.getCause());
//                }
            }
        }
    }

    /**
     * Removes the specified attribute.
     * @param attribute the attribute to remove
     */
    @Function
    public void removeAttributeNode(final Attr2 attribute) {
        final String name = attribute.getName();
        final Object namespaceUri = attribute.getNamespaceURI();
        if (namespaceUri instanceof String) {
            removeAttributeNS((String) namespaceUri, name);
            return;
        }
        removeAttributeNS(null, name);
    }

    /**
     * Replaces this element (including all child elements) with the supplied value.
     * @param value the new value for replacing this element
     */
    @Setter
    public void setOuterHTML(final Object value) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode parent = domNode.getParentNode();
        if (null == parent) {
            if (getBrowserVersion().hasFeature(JS_OUTER_HTML_REMOVES_CHILDREN_FOR_DETACHED)) {
                domNode.removeAllChildren();
            }
            if (getBrowserVersion().hasFeature(JS_OUTER_HTML_THROWS_FOR_DETACHED)) {
                throw new RuntimeException("outerHTML is readonly for detached nodes");
            }
            return;
        }

        if (value == null && !getBrowserVersion().hasFeature(JS_OUTER_HTML_NULL_AS_STRING)) {
            domNode.remove();
            return;
        }
        final String valueStr = value.toString();
        if (valueStr.isEmpty()) {
            domNode.remove();
            return;
        }

        final DomNode nextSibling = domNode.getNextSibling();
        domNode.remove();

        final DomNode target;
        final boolean append;
        if (nextSibling != null) {
            target = nextSibling;
            append = false;
        }
        else {
            target = parent;
            append = true;
        }

        final DomNode proxyDomNode = new ProxyDomNode(target.getPage(), target, append);
        parseHtmlSnippet(proxyDomNode, valueStr);
    }

    /**
     * Gets the outerHTML of the node.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534310.aspx">MSDN documentation</a>
     * @return the contents of this node as HTML
     */
    @Getter
    public String getOuterHTML() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printNode(buf, getDomNodeOrDie(), true);
        return buf.toString();
    }

    private void printNode(final StringBuilder builder, final DomNode node, final boolean html) {
        if (node instanceof DomComment) {
            if (html) {
                // Remove whitespace sequences.
                final String s = PRINT_NODE_PATTERN.matcher(node.getNodeValue()).replaceAll(" ");
                builder.append("<!--").append(s).append("-->");
            }
        }
        else if (node instanceof DomCharacterData) {
            // Remove whitespace sequences, possibly escape XML characters.
            String s = node.getNodeValue();
            if (html) {
                s = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(s);
            }
            builder.append(s);
        }
        else if (html) {
            final DomElement element = (DomElement) node;
            final Element scriptObject = (Element) node.getScriptableObject();
            final String tag = element.getTagName();

            HTMLElement htmlElement = null;
            if (scriptObject instanceof HTMLElement) {
                htmlElement = (HTMLElement) scriptObject;
            }
            builder.append("<").append(tag);
            // Add the attributes. IE does not use quotes, FF does.
            for (final DomAttr attr : element.getAttributesMap().values()) {
                if (!attr.getSpecified()) {
                    continue;
                }

                final String name = attr.getName();
                final String value = PRINT_NODE_QUOTE_PATTERN.matcher(attr.getValue()).replaceAll("&quot;");
                builder.append(' ').append(name).append("=");
                builder.append("\"");
                builder.append(value);
                builder.append("\"");
            }
            builder.append(">");
            // Add the children.
            final boolean isHtml = html
                    && !(scriptObject instanceof HTMLScriptElement)
                    && !(scriptObject instanceof HTMLStyleElement);
            printChildren(builder, node, isHtml);
            if (null == htmlElement || !htmlElement.isEndTagForbidden()) {
                builder.append("</").append(tag).append(">");
            }
        }
        else {
            final HtmlElement element = (HtmlElement) node;
            if ("p".equals(element.getTagName())) {
                if (getBrowserVersion().hasFeature(JS_INNER_TEXT_CR_NL)) {
                    builder.append("\r\n"); // \r\n because it's to implement something IE specific
                }
                else {
                    int i = builder.length() - 1;
                    while (i >= 0 && Character.isWhitespace(builder.charAt(i))) {
                        i--;
                    }
                    builder.setLength(i + 1);
                    builder.append("\n");
                }
            }
            if (!"script".equals(element.getTagName())) {
                printChildren(builder, node, html);
            }
        }
    }

    private void printChildren(final StringBuilder builder, final DomNode node, final boolean html) {
        for (final DomNode child : node.getChildren()) {
            printNode(builder, child, html);
        }
    }

    /**
     * Gets the innerHTML attribute.
     * @return the contents of this node as HTML
     */
    @Getter
    public String getInnerHTML() {
        final DomNode domNode;
        try {
            domNode = getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            throw new RuntimeException(e);
        }

        final StringBuilder buf = new StringBuilder();

        final String tagName = getTagName();
        boolean isPlain = "SCRIPT".equals(tagName);

        isPlain = isPlain || "STYLE".equals(tagName);

        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, domNode, !isPlain);
        return buf.toString();
    }

    /**
     * Gets the innerText attribute.
     * @return the contents of this node as text
     */
    @Getter
    public String getInnerText() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie(), false);
        return buf.toString();
    }

    /**
     * Replaces all child elements of this element with the supplied value.
     * @param value the new value for the contents of this element
     */
    @Setter
    public void setInnerHTML(final Object value) {
        final DomNode domNode;
        try {
            domNode = getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            throw new RuntimeException(e);
        }

        domNode.removeAllChildren();

        final boolean addChildForNull = getBrowserVersion().hasFeature(JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE);
        if ((value == null && addChildForNull) || (value != null && !"".equals(value))) {

            final String valueAsString = value.toString();
            parseHtmlSnippet(domNode, valueAsString);
        }
    }

    /**
     * Replaces all child elements of this element with the supplied text value.
     * @param value the new value for the contents of this element
     */
    @Setter
    public void setInnerText(final Object value) {
        final String valueString;
        if (value == null && getBrowserVersion().hasFeature(JS_INNER_TEXT_VALUE_NULL)) {
            valueString = null;
        }
        else {
            valueString = value.toString();
        }
        setInnerTextImpl(valueString);
    }

    /**
     * The worker for setInnerText.
     * @param value the new value for the contents of this node
     */
    protected void setInnerTextImpl(final String value) {
        final DomNode domNode = getDomNodeOrDie();

        domNode.removeAllChildren();

        if (value != null && !value.isEmpty()) {
            domNode.appendChild(new DomText(domNode.getPage(), value));
        }
    }

    /**
     * Removes a DOM node from this node.
     * @param childObject the node to remove from this node
     * @return the removed child node
     */
    @Override
    @Function
    public Object removeChild(final Object childObject) {
        if (!(childObject instanceof Node2)) {
            return null;
        }

        // Get XML node for the DOM node passed in
        final DomNode childNode = ((Node2) childObject).getDomNodeOrDie();

        if (!getDomNodeOrDie().isAncestorOf(childNode)) {
            throw new RuntimeException("NotFoundError: Failed to execute 'removeChild' on '"
                        + this + "': The node to be removed is not a child of this node.");
        }
        // Remove the child from the parent node
        childNode.remove();
        return childObject;
    }

    /**
     * Sets the focus to this element.
     */
    @Function
    public void focus() {
        final HtmlElement domNode = getDomNodeOrDie();
        if (domNode instanceof SubmittableElement) {
            domNode.focus();
        }

        // no action otherwise!
    }

    /**
     * Gets the attributes of the element in the form of a {@link org.xml.sax.Attributes}.
     * @param element the element to read the attributes from
     * @return the attributes
     */
    protected AttributesImpl readAttributes(final HtmlElement element) {
        final AttributesImpl attributes = new AttributesImpl();
        for (final DomAttr entry : element.getAttributesMap().values()) {
            final String name = entry.getName();
            final String value = entry.getValue();
            attributes.addAttribute(null, name, name, null, value);
        }

        return attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        final DomNode domNode = getDomNodeOrDie();
        if (domNode.getHtmlPageOrNull() != null) {
            final String prefix = domNode.getPrefix();
            if (prefix != null) {
                // create string builder only if needed (performance)
                final StringBuilder localName = new StringBuilder(prefix.toLowerCase(Locale.ROOT));
                localName.append(':');
                localName.append(domNode.getLocalName().toLowerCase(Locale.ROOT));
                return localName.toString();
            }
            return domNode.getLocalName().toLowerCase(Locale.ROOT);
        }
        return domNode.getLocalName();
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

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("HTMLElement",
                    staticHandle("constructor", HTMLElement2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("HTMLElement");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("HTMLElement");
        }
    }
}
