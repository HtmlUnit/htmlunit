/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import static org.htmlunit.BrowserVersionFeatures.JS_OFFSET_PARENT_NULL_IF_FIXED;
import static org.htmlunit.css.CssStyleSheet.ABSOLUTE;
import static org.htmlunit.css.CssStyleSheet.FIXED;
import static org.htmlunit.html.DisabledElement.ATTRIBUTE_DISABLED;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static org.htmlunit.html.DomElement.ATTRIBUTE_VALUE_EMPTY;
import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.css.ComputedCssStyleDeclaration;
import org.htmlunit.css.StyleAttributes;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlAbbreviated;
import org.htmlunit.html.HtmlAcronym;
import org.htmlunit.html.HtmlAddress;
import org.htmlunit.html.HtmlArticle;
import org.htmlunit.html.HtmlAside;
import org.htmlunit.html.HtmlBaseFont;
import org.htmlunit.html.HtmlBidirectionalIsolation;
import org.htmlunit.html.HtmlBidirectionalOverride;
import org.htmlunit.html.HtmlBig;
import org.htmlunit.html.HtmlBody;
import org.htmlunit.html.HtmlBold;
import org.htmlunit.html.HtmlBreak;
import org.htmlunit.html.HtmlCenter;
import org.htmlunit.html.HtmlCitation;
import org.htmlunit.html.HtmlCode;
import org.htmlunit.html.HtmlDefinition;
import org.htmlunit.html.HtmlDefinitionDescription;
import org.htmlunit.html.HtmlDefinitionTerm;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlEmphasis;
import org.htmlunit.html.HtmlFigure;
import org.htmlunit.html.HtmlFigureCaption;
import org.htmlunit.html.HtmlFooter;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlHeader;
import org.htmlunit.html.HtmlItalic;
import org.htmlunit.html.HtmlKeyboard;
import org.htmlunit.html.HtmlLayer;
import org.htmlunit.html.HtmlMain;
import org.htmlunit.html.HtmlMark;
import org.htmlunit.html.HtmlNav;
import org.htmlunit.html.HtmlNoBreak;
import org.htmlunit.html.HtmlNoEmbed;
import org.htmlunit.html.HtmlNoFrames;
import org.htmlunit.html.HtmlNoLayer;
import org.htmlunit.html.HtmlNoScript;
import org.htmlunit.html.HtmlPlainText;
import org.htmlunit.html.HtmlRb;
import org.htmlunit.html.HtmlRp;
import org.htmlunit.html.HtmlRt;
import org.htmlunit.html.HtmlRtc;
import org.htmlunit.html.HtmlRuby;
import org.htmlunit.html.HtmlS;
import org.htmlunit.html.HtmlSample;
import org.htmlunit.html.HtmlSection;
import org.htmlunit.html.HtmlSmall;
import org.htmlunit.html.HtmlStrike;
import org.htmlunit.html.HtmlStrong;
import org.htmlunit.html.HtmlSubscript;
import org.htmlunit.html.HtmlSummary;
import org.htmlunit.html.HtmlSuperscript;
import org.htmlunit.html.HtmlTable;
import org.htmlunit.html.HtmlTableDataCell;
import org.htmlunit.html.HtmlTeletype;
import org.htmlunit.html.HtmlUnderlined;
import org.htmlunit.html.HtmlVariable;
import org.htmlunit.html.HtmlWordBreak;
import org.htmlunit.html.serializer.HtmlSerializerInnerOuterText;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.ClientRect;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.css.CSSStyleDeclaration;
import org.htmlunit.javascript.host.dom.DOMStringMap;
import org.htmlunit.javascript.host.dom.Node;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventHandler;
import org.htmlunit.javascript.host.event.MouseEvent;

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
@JsxClass(domClass = HtmlAbbreviated.class)
@JsxClass(domClass = HtmlAcronym.class)
@JsxClass(domClass = HtmlAddress.class)
@JsxClass(domClass = HtmlArticle.class)
@JsxClass(domClass = HtmlAside.class)
@JsxClass(domClass = HtmlBaseFont.class)
@JsxClass(domClass = HtmlBidirectionalIsolation.class)
@JsxClass(domClass = HtmlBidirectionalOverride.class)
@JsxClass(domClass = HtmlBig.class)
@JsxClass(domClass = HtmlBold.class)
@JsxClass(domClass = HtmlCenter.class)
@JsxClass(domClass = HtmlCitation.class)
@JsxClass(domClass = HtmlCode.class)
@JsxClass(domClass = HtmlDefinition.class)
@JsxClass(domClass = HtmlDefinitionDescription.class)
@JsxClass(domClass = HtmlDefinitionTerm.class)
@JsxClass(domClass = HtmlElement.class, value = {FF, FF_ESR})
@JsxClass(domClass = HtmlEmphasis.class)
@JsxClass(domClass = HtmlFigure.class)
@JsxClass(domClass = HtmlFigureCaption.class)
@JsxClass(domClass = HtmlFooter.class)
@JsxClass(domClass = HtmlHeader.class)
@JsxClass(domClass = HtmlItalic.class)
@JsxClass(domClass = HtmlKeyboard.class)
@JsxClass(domClass = HtmlLayer.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlMark.class)
@JsxClass(domClass = HtmlNav.class)
@JsxClass(domClass = HtmlNoBreak.class)
@JsxClass(domClass = HtmlNoEmbed.class)
@JsxClass(domClass = HtmlNoFrames.class)
@JsxClass(domClass = HtmlNoLayer.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlNoScript.class)
@JsxClass(domClass = HtmlPlainText.class)
@JsxClass(domClass = HtmlRuby.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlRb.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlRp.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlRt.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlRtc.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlS.class)
@JsxClass(domClass = HtmlSample.class)
@JsxClass(domClass = HtmlSection.class)
@JsxClass(domClass = HtmlSmall.class)
@JsxClass(domClass = HtmlStrike.class)
@JsxClass(domClass = HtmlStrong.class)
@JsxClass(domClass = HtmlSubscript.class)
@JsxClass(domClass = HtmlSummary.class)
@JsxClass(domClass = HtmlSuperscript.class)
@JsxClass(domClass = HtmlTeletype.class)
@JsxClass(domClass = HtmlUnderlined.class)
@JsxClass(domClass = HtmlWordBreak.class)
@JsxClass(domClass = HtmlMain.class)
@JsxClass(domClass = HtmlVariable.class)
public class HTMLElement extends Element {

    private static final Class<?>[] METHOD_PARAMS_OBJECT = {Object.class};
    private static final Set<String> ENTER_KEY_HINT_VALUES = new HashSet<>();

    // private static final Log LOG = LogFactory.getLog(HTMLElement.class);

    static {
        ENTER_KEY_HINT_VALUES.add("enter");
        ENTER_KEY_HINT_VALUES.add("done");
        ENTER_KEY_HINT_VALUES.add("go");
        ENTER_KEY_HINT_VALUES.add("next");
        ENTER_KEY_HINT_VALUES.add("previous");
        ENTER_KEY_HINT_VALUES.add("search");
        ENTER_KEY_HINT_VALUES.add("send");
    }

    private boolean endTagForbidden_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        final String name = domNode.getLocalName();
        if ("wbr".equalsIgnoreCase(name)
                || "basefont".equalsIgnoreCase(name)
                || "track".equalsIgnoreCase(name)) {
            endTagForbidden_ = true;
        }

        if ("input".equalsIgnoreCase(name)
                || "button".equalsIgnoreCase(name)
                || "textarea".equalsIgnoreCase(name)
                || "select".equalsIgnoreCase(name)) {
            final HtmlForm form = ((HtmlElement) domNode).getEnclosingForm();
            if (form != null) {
                setParentScope(getScriptableFor(form));
            }
        }
    }

    /**
     * Returns the value of the JavaScript {@code form} attribute.
     *
     * @return the value of the JavaScript {@code form} attribute
     */
    public HTMLFormElement getForm() {
        final HtmlForm form = getDomNodeOrDie().getEnclosingForm();
        if (form == null) {
            return null;
        }
        return (HTMLFormElement) getScriptableFor(form);
    }

    /**
     * Returns the element title.
     * @return the title of this element
     */
    @JsxGetter
    public String getTitle() {
        return getDomNodeOrDie().getAttributeDirect("title");
    }

    /**
     * Sets the title of this element.
     * @param newTitle the new title of this element
     */
    @JsxSetter
    public void setTitle(final String newTitle) {
        getDomNodeOrDie().setAttribute("title", newTitle);
    }

    /**
     * Returns the element autofocus property.
     * @return the autofocus of this element
     */
    @JsxGetter
    public boolean getAutofocus() {
        return getDomNodeOrDie().hasAttribute("autofocus");
    }

    /**
     * Sets the autofocus of this element.
     * @param newAutofocus the new autofocus of this element
     */
    @JsxSetter
    public void setAutofocus(final boolean newAutofocus) {
        if (newAutofocus) {
            getDomNodeOrDie().setAttribute("autofocus", "");
        }
        else {
            getDomNodeOrDie().removeAttribute("autofocus");
        }
    }

    /**
     * Returns true if this element is disabled.
     * @return true if this element is disabled
     */
    public boolean isDisabled() {
        return getDomNodeOrDie().hasAttribute(ATTRIBUTE_DISABLED);
    }

    /**
     * Sets whether or not to disable this element.
     * @param disabled True if this is to be disabled
     */
    public void setDisabled(final boolean disabled) {
        final HtmlElement element = getDomNodeOrDie();
        if (disabled) {
            element.setAttribute(ATTRIBUTE_DISABLED, ATTRIBUTE_DISABLED);
        }
        else {
            element.removeAttribute(ATTRIBUTE_DISABLED);
        }
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
                final StringBuilder localName = new StringBuilder(
                                org.htmlunit.util.StringUtils.toRootLowerCase(prefix))
                    .append(':')
                    .append(org.htmlunit.util.StringUtils
                                .toRootLowerCase(domNode.getLocalName()));
                return localName.toString();
            }
            return org.htmlunit.util.StringUtils.toRootLowerCase(domNode.getLocalName());
        }
        return domNode.getLocalName();
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
                try {
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    final Method method = getClass().getMethod("set" + name, METHOD_PARAMS_OBJECT);
                    final EventHandler eventHandler = new EventHandler(getDomNodeOrDie(), name.substring(2), value);
                    eventHandler.setPrototype(ScriptableObject.getClassPrototype(this, "Function"));
                    method.invoke(this, eventHandler);
                }
                catch (final NoSuchMethodException | IllegalAccessException ignored) {
                    // silently ignore
                }
                catch (final InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                }
            }
        }
    }

    /**
     * Gets the attribute node for the specified attribute.
     * @param attributeName the name of the attribute to retrieve
     * @return the attribute node for the specified attribute
     */
    @Override
    public HtmlUnitScriptable getAttributeNode(final String attributeName) {
        return getAttributes().getNamedItem(attributeName);
    }

    /**
     * Gets the innerText attribute.
     * (see https://html.spec.whatwg.org/multipage/dom.html#the-innertext-idl-attribute)
     * @return the contents of this node as text
     */
    @JsxGetter
    public String getInnerText() {
        final HtmlSerializerInnerOuterText ser = new HtmlSerializerInnerOuterText(getBrowserVersion());
        return ser.asText(getDomNodeOrDie());
    }

    /**
     * Replaces all child elements of this element with the supplied text value.
     * (see https://html.spec.whatwg.org/multipage/dom.html#the-innertext-idl-attribute)
     * @param value the new value for the contents of this element
     */
    @JsxSetter
    public void setInnerText(final Object value) {
        final String valueString;
        if (value == null) {
            valueString = null;
        }
        else {
            valueString = JavaScriptEngine.toString(value);
        }

        final DomNode domNode = getDomNodeOrDie();
        final SgmlPage page = domNode.getPage();
        domNode.removeAllChildren();

        if (StringUtils.isNotEmpty(valueString)) {
            final String[] parts = valueString.split("\\r?\\n");
            for (int i = 0; i < parts.length; i++) {
                if (i != 0) {
                    domNode.appendChild(page.createElement(HtmlBreak.TAG_NAME));
                }
                domNode.appendChild(new DomText(page, parts[i]));
            }
        }
    }

    /**
     * The outerText property of the HTMLElement interface returns the same value as HTMLElement.innerText.
     * (see https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/outerText)
     * @return the contents of this node as text
     */
    @JsxGetter
    public String getOuterText() {
        return getInnerText();
    }

    /**
     * Replaces the whole current node with the given text.
     * (see https://html.spec.whatwg.org/multipage/dom.html#the-innertext-idl-attribute)
     * @param value the new value for the contents of this element
     */
    @JsxSetter
    public void setOuterText(final Object value) {
        final String valueString;
        if (value == null) {
            valueString = null;
        }
        else {
            valueString = JavaScriptEngine.toString(value);
        }

        final DomNode domNode = getDomNodeOrDie();
        final SgmlPage page = domNode.getPage();

        if (StringUtils.isEmpty(valueString)) {
            domNode.getParentNode().insertBefore(new DomText(page, ""), domNode);
        }
        else {
            final String[] parts = valueString.split("\\r?\\n");
            for (int i = 0; i < parts.length; i++) {
                if (i != 0) {
                    domNode.getParentNode().insertBefore(page.createElement(HtmlBreak.TAG_NAME), domNode);
                }
                domNode.getParentNode().insertBefore(new DomText(page, parts[i]), domNode);
            }
        }

        domNode.remove();
    }

    /**
     * Replaces all child elements of this element with the supplied text value.
     * @param value the new value for the contents of this element
     */
    @Override
    public void setTextContent(final Object value) {
        final DomNode domNode = getDomNodeOrDie();
        domNode.removeAllChildren();

        if (value != null) {
            final String textValue = JavaScriptEngine.toString(value);
            if (StringUtils.isNotEmpty(textValue)) {
                domNode.appendChild(new DomText(domNode.getPage(), textValue));
            }
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
     * Returns this element's <code>offsetHeight</code>, which is the element height plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <code>offsetHeight</code>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534199.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    @JsxGetter
    public int getOffsetHeight() {
        if (isDisplayNone() || !getDomNodeOrDie().isAttachedToPage()) {
            return 0;
        }
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offset height to pretend mouse event was produced within this element
            return event.getClientY() - getPosY() + 50;
        }
        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        return style.getCalculatedHeight(true, true);
    }

    /**
     * Returns this element's <code>offsetWidth</code>, which is the element width plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <code>offsetWidth</code>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534304.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    @JsxGetter
    public int getOffsetWidth() {
        if (isDisplayNone() || !getDomNodeOrDie().isAttachedToPage()) {
            return 0;
        }

        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offset width to pretend mouse event was produced within this element
            return event.getClientX() - getPosX() + 50;
        }
        final ComputedCssStyleDeclaration style = getWindow().getWebWindow().getComputedStyle(getDomNodeOrDie(), null);
        return style.getCalculatedWidth(true, true);
    }

    /**
     * Returns {@code true} if this element's node is an ancestor of the specified event's target node.
     * @param event the event whose target node is to be checked
     * @return {@code true} if this element's node is an ancestor of the specified event's target node
     */
    protected boolean isAncestorOfEventTarget(final MouseEvent event) {
        if (event == null) {
            return false;
        }
        if (!(event.getSrcElement() instanceof HTMLElement)) {
            return false;
        }
        final HTMLElement srcElement = (HTMLElement) event.getSrcElement();
        return getDomNodeOrDie().isAncestorOf(srcElement.getDomNodeOrDie());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HTMLElement for " + getDomNodeOrNull();
    }

    /**
     * Gets the first ancestor instance of {@link HTMLElement}. It is mostly identical
     * to {@link #getParent()} except that it skips XML nodes.
     * @return the parent HTML element
     * @see #getParent()
     */
    public HTMLElement getParentHTMLElement() {
        Node parent = getParent();
        while (parent != null && !(parent instanceof HTMLElement)) {
            parent = parent.getParent();
        }
        return (HTMLElement) parent;
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
     * Remove focus from this element.
     */
    @JsxFunction
    public void blur() {
        getDomNodeOrDie().blur();
    }

    /**
     * Sets the focus to this element.
     */
    @JsxFunction
    public void focus() {
        getDomNodeOrDie().focus();
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
     * Click this element. This simulates the action of the user clicking with the mouse.
     * @throws IOException if this click triggers a page load that encounters problems
     */
    @JsxFunction
    public void click() throws IOException {
        // when triggered from js the visibility is ignored
        getDomNodeOrDie().click(false, false, false, true, true, true, false);
    }

    /**
     * Returns the {@code spellcheck} property.
     * @return the {@code spellcheck} property
     */
    @JsxGetter({FF, FF_ESR})
    public boolean isSpellcheck() {
        return JavaScriptEngine.toBoolean(getDomNodeOrDie().getAttributeDirect("spellcheck"));
    }

    /**
     * Sets the {@code spellcheck} property.
     * @param spellcheck the {@code spellcheck} property
     */
    @JsxSetter({FF, FF_ESR})
    public void setSpellcheck(final boolean spellcheck) {
        getDomNodeOrDie().setAttribute("spellcheck", Boolean.toString(spellcheck));
    }

    /**
     * Returns the {@code lang} property.
     * @return the {@code lang} property
     */
    @JsxGetter
    public String getLang() {
        return getDomNodeOrDie().getAttributeDirect("lang");
    }

    /**
     * Sets the {@code lang} property.
     * @param lang the {@code lang} property
     */
    @JsxSetter
    public void setLang(final String lang) {
        getDomNodeOrDie().setAttribute("lang", lang);
    }

    /**
     * Returns the {@code dir} property.
     * @return the {@code dir} property
     */
    @JsxGetter
    public String getDir() {
        return getDomNodeOrDie().getAttributeDirect("dir");
    }

    /**
     * Sets the {@code dir} property.
     * @param dir the {@code dir} property
     */
    @JsxSetter
    public void setDir(final String dir) {
        getDomNodeOrDie().setAttribute("dir", dir);
    }

    /**
     * Returns the value of the tabIndex attribute.
     * @return the value of the tabIndex attribute
     */
    @JsxGetter
    public int getTabIndex() {
        return (int) JavaScriptEngine.toNumber(getDomNodeOrDie().getAttributeDirect("tabindex"));
    }

    /**
     * Sets the {@code tabIndex} property.
     * @param tabIndex the {@code tabIndex} property
     */
    @JsxSetter
    public void setTabIndex(final int tabIndex) {
        getDomNodeOrDie().setAttribute("tabindex", Integer.toString(tabIndex));
    }

    /**
     * Returns the {@code accessKey} property.
     * @return the {@code accessKey} property
     */
    @JsxGetter
    public String getAccessKey() {
        return getDomNodeOrDie().getAttributeDirect("accesskey");
    }

    /**
     * Sets the {@code accessKey} property.
     * @param accessKey the {@code accessKey} property
     */
    @JsxSetter
    public void setAccessKey(final String accessKey) {
        getDomNodeOrDie().setAttribute("accesskey", accessKey);
    }

    /**
     * Returns the value of the specified attribute (width or height).
     * @return the value of the specified attribute (width or height)
     * @param attributeName the name of the attribute to return (<code>"width"</code> or <code>"height"</code>)
     * @param returnNegativeValues if {@code true}, negative values are returned;
     *        if {@code false}, this method returns an empty string in lieu of negative values;
     *        if {@code null}, this method returns <code>0</code> in lieu of negative values
     */
    protected String getWidthOrHeight(final String attributeName, final Boolean returnNegativeValues) {
        return getDomNodeOrDie().getAttribute(attributeName);
    }

    /**
     * Sets the value of the specified attribute (width or height).
     * @param attributeName the name of the attribute to set (<code>"width"</code> or <code>"height"</code>)
     * @param value the value of the specified attribute (width or height)
     * @param allowNegativeValues if {@code true}, negative values will be stored;
     *        if {@code false}, negative values cause an exception to be thrown;<br>
     *        this check/conversion is only done if the feature JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES
     *        is set for the simulated browser
     */
    protected void setWidthOrHeight(final String attributeName, final String value, final boolean allowNegativeValues) {
        getDomNodeOrDie().setAttribute(attributeName, value);
    }

    /**
     * Sets the specified color attribute to the specified value.
     * @param name the color attribute's name
     * @param value the color attribute's value
     */
    protected void setColorAttribute(final String name, final String value) {
        getDomNodeOrDie().setAttribute(name, value);
    }

    /**
     * Returns the value of the {@code align} property.
     * @param returnInvalidValues if {@code true}, this method will return any value, including technically
     *        invalid values; if {@code false}, this method will return an empty string instead of invalid values
     * @return the value of the {@code align} property
     */
    protected String getAlign(final boolean returnInvalidValues) {
        return getDomNodeOrDie().getAttributeDirect("align");
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     * @param ignoreIfNoError if {@code true}, the invocation will be a no-op if it does not trigger an error
     *        (i.e., it will not actually set the align attribute)
     */
    protected void setAlign(final String align, final boolean ignoreIfNoError) {
        if (!ignoreIfNoError) {
            getDomNodeOrDie().setAttribute("align", align);
        }
    }

    /**
     * Returns the value of the {@code vAlign} property.
     * @param valid the valid values; if {@code null}, any value is valid
     * @param defaultValue the default value to use, if necessary
     * @return the value of the {@code vAlign} property
     */
    protected String getVAlign(final String[] valid, final String defaultValue) {
        final String valign = getDomNodeOrDie().getAttributeDirect("valign");
        final String valignLC = valign.toLowerCase(Locale.ROOT);
        if (valid == null || ArrayUtils.contains(valid, valignLC)) {
            return valign;
        }
        return defaultValue;
    }

    /**
     * Sets the value of the {@code vAlign} property.
     * @param vAlign the value of the {@code vAlign} property
     * @param valid the valid values; if {@code null}, any value is valid
     */
    protected void setVAlign(final Object vAlign, final String[] valid) {
        final String valign = JavaScriptEngine.toString(vAlign);
        final String valignLC = valign.toLowerCase(Locale.ROOT);
        if (valid == null || ArrayUtils.contains(valid, valignLC)) {
            getDomNodeOrDie().setAttribute("valign", valign);
        }
        else {
            throw JavaScriptEngine.reportRuntimeError("Cannot set the vAlign property to invalid value: " + vAlign);
        }
    }

    /**
     * Returns the value of the {@code ch} property.
     * @return the value of the {@code ch} property
     */
    protected String getCh() {
        return getDomNodeOrDie().getAttributeDirect("char");
    }

    /**
     * Sets the value of the {@code ch} property.
     * @param ch the value of the {@code ch} property
     */
    protected void setCh(final String ch) {
        getDomNodeOrDie().setAttribute("char", ch);
    }

    /**
     * Returns the value of the {@code chOff} property.
     * @return the value of the {@code chOff} property
     */
    protected String getChOff() {
        return getDomNodeOrDie().getAttribute("charOff");
    }

    /**
     * Sets the value of the {@code chOff} property.
     * @param chOff the value of the {@code chOff} property
     */
    protected void setChOff(String chOff) {
        try {
            final float f = Float.parseFloat(chOff);
            final int i = (int) f;
            if (i == f) {
                chOff = Integer.toString(i);
            }
            else {
                chOff = Float.toString(f);
            }
        }
        catch (final NumberFormatException ignored) {
            // ignore
        }
        getDomNodeOrDie().setAttribute("charOff", chOff);
    }

    /**
     * Returns this element's X position.
     * @return this element's X position
     */
    public int getPosX() {
        int cumulativeOffset = 0;
        HTMLElement element = this;
        final WebWindow webWindow = element.getWindow().getWebWindow();
        while (element != null) {
            cumulativeOffset += element.getOffsetLeft();
            if (element != this) {
                final ComputedCssStyleDeclaration style =
                        webWindow.getComputedStyle(element.getDomNodeOrDie(), null);
                cumulativeOffset += style.getBorderLeftValue();
            }
            element = element.getOffestParentElement(false);
        }
        return cumulativeOffset;
    }

    /**
     * Returns this element's Y position.
     * @return this element's Y position
     */
    public int getPosY() {
        int cumulativeOffset = 0;
        HTMLElement element = this;
        final WebWindow webWindow = element.getWindow().getWebWindow();
        while (element != null) {
            cumulativeOffset += element.getOffsetTop();
            if (element != this) {
                final ComputedCssStyleDeclaration style =
                        webWindow.getComputedStyle(element.getDomNodeOrDie(), null);
                cumulativeOffset += style.getBorderTopValue();
            }
            element = element.getOffestParentElement(false);
        }
        return cumulativeOffset;
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
    @JsxGetter
    public int getOffsetTop() {
        if (this instanceof HTMLBodyElement) {
            return 0;
        }

        int top = 0;

        // Add the offset for this node.
        final HtmlElement htmlElement = getDomNodeOrDie();
        final WebWindow webWindow = getWindow().getWebWindow();
        ComputedCssStyleDeclaration style = webWindow.getComputedStyle(htmlElement, null);
        top += style.getTop(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = style.getPositionWithInheritance();
        if (ABSOLUTE.equals(position) || FIXED.equals(position)) {
            return top;
        }

        final HtmlElement offsetParent = getOffsetParentInternal(false);

        // Add the offset for the ancestor nodes.
        DomNode parentNode = htmlElement.getParentNode();
        while (parentNode != null && parentNode != offsetParent) {
            if (parentNode instanceof HtmlElement) {
                style = webWindow.getComputedStyle((HtmlElement) parentNode, null);
                top += style.getTop(false, true, true);
            }
            parentNode = parentNode.getParentNode();
        }

        if (offsetParent != null) {
            style = webWindow.getComputedStyle(htmlElement, null);
            final boolean thisElementHasTopMargin = style.getMarginTopValue() != 0;

            style = webWindow.getComputedStyle(offsetParent, null);
            if (!thisElementHasTopMargin) {
                top += style.getMarginTopValue();
            }
            top += style.getPaddingTopValue();
        }

        return top;
    }

    /**
     * Returns this element's <code>offsetLeft</code>, which is the calculated left position of this
     * element relative to the <code>offsetParent</code>.
     *
     * @return this element's <code>offsetLeft</code>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534200.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @JsxGetter
    public int getOffsetLeft() {
        if (this instanceof HTMLBodyElement) {
            return 0;
        }

        int left = 0;

        // Add the offset for this node.
        final HtmlElement htmlElement = getDomNodeOrDie();
        final WebWindow webWindow = getWindow().getWebWindow();
        ComputedCssStyleDeclaration style = webWindow.getComputedStyle(htmlElement, null);
        left += style.getLeft(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = style.getPositionWithInheritance();
        if (ABSOLUTE.equals(position) || FIXED.equals(position)) {
            return left;
        }

        final HtmlElement offsetParent = getOffsetParentInternal(false);

        DomNode parentNode = htmlElement.getParentNode();
        while (parentNode != null && parentNode != offsetParent) {
            if (parentNode instanceof HtmlElement) {
                style = webWindow.getComputedStyle((HtmlElement) parentNode, null);
                left += style.getLeft(true, true, true);
            }
            parentNode = parentNode.getParentNode();
        }

        if (offsetParent != null) {
            style = webWindow.getComputedStyle(offsetParent, null);
            left += style.getMarginLeftValue();
            left += style.getPaddingLeftValue();
        }

        return left;
    }

    /**
     * Returns this element's <code>offsetParent</code>. The <code>offsetLeft</code> and
     * <code>offsetTop</code> attributes are relative to the <code>offsetParent</code>.
     *
     * @return this element's <code>offsetParent</code>. This may be <code>undefined</code> when this node is
     *         not attached or {@code null} for <code>body</code>.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534302.aspx">MSDN Documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_el_ref20.html">Gecko DOM Reference</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://www.w3.org/TR/REC-CSS2/box.html">Box Model</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @JsxGetter(propertyName = "offsetParent")
    public HtmlUnitScriptable getOffsetParent_js() {
        final boolean feature = getBrowserVersion().hasFeature(JS_OFFSET_PARENT_NULL_IF_FIXED);
        return getOffestParentElement(feature);
    }

    private HTMLElement getOffestParentElement(final boolean returnNullIfFixed) {
        final HtmlElement html = getOffsetParentInternal(returnNullIfFixed);
        if (html == null) {
            return null;
        }
        return html.getScriptableObject();
    }

    private HtmlElement getOffsetParentInternal(final boolean returnNullIfFixed) {
        DomNode currentElement = getDomNodeOrDie();

        if (currentElement.getParentNode() == null) {
            return null;
        }

        if (returnNullIfFixed
                && FIXED.equals(getStyle().getStyleAttribute(
                StyleAttributes.Definition.POSITION, true))) {
            return null;
        }

        final WebWindow webWindow = getWindow().getWebWindow();
        final ComputedCssStyleDeclaration style = webWindow.getComputedStyle((DomElement) currentElement, null);
        final String position = style.getPositionWithInheritance();
        final boolean staticPos = "static".equals(position);

        while (currentElement != null) {

            final DomNode parentNode = currentElement.getParentNode();
            if (parentNode instanceof HtmlBody
                || (staticPos && parentNode instanceof HtmlTableDataCell)
                || (staticPos && parentNode instanceof HtmlTable)) {
                return (HtmlElement) parentNode;
            }

            if (parentNode != null && parentNode instanceof HtmlElement) {
                final HTMLElement parentElement = parentNode.getScriptableObject();
                final ComputedCssStyleDeclaration parentStyle =
                        webWindow.getComputedStyle(parentElement.getDomNodeOrDie(), null);
                final String parentPosition = parentStyle.getPositionWithInheritance();
                if (!"static".equals(parentPosition)) {
                    return (HtmlElement) parentNode;
                }
            }

            currentElement = currentElement.getParentNode();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientRect getBoundingClientRect() {
        final ClientRect textRectangle = super.getBoundingClientRect();

        int left = getPosX();
        int top = getPosY();

        // account for any scrolled ancestors
        Node parentNode = getOffestParentElement(false);
        while ((parentNode instanceof HTMLElement)
                && !(parentNode instanceof HTMLBodyElement)) {
            final HTMLElement elem = (HTMLElement) parentNode;
            left -= elem.getScrollLeft();
            top -= elem.getScrollTop();

            parentNode = elem.getParent();
        }

        textRectangle.setBottom(top + getOffsetHeight());
        textRectangle.setLeft(left);
        textRectangle.setRight(left + getOffsetWidth());
        textRectangle.setTop(top);

        return textRectangle;
    }

    /**
     * Returns the {@code dataset} attribute.
     * @return the {@code dataset} attribute
     */
    @JsxGetter
    public DOMStringMap getDataset() {
        return new DOMStringMap(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return endTagForbidden_;
    }

    /**
     * @return whether the tag is lower case in .outerHTML/.innerHTML
     */
    protected boolean isLowerCaseInOuterHtml() {
        return false;
    }

    /**
     * Sets the {@code onchange} event handler for this element.
     * @param onchange the {@code onchange} event handler for this element
     */
    @JsxSetter
    public void setOnchange(final Object onchange) {
        setEventHandler(Event.TYPE_CHANGE, onchange);
    }

    /**
     * Returns the {@code onchange} event handler for this element.
     * @return the {@code onchange} event handler for this element
     */
    @JsxGetter
    public Function getOnchange() {
        return getEventHandler(Event.TYPE_CHANGE);
    }

    /**
     * Returns the {@code onsubmit} event handler for this element.
     * @return the {@code onsubmit} event handler for this element
     */
    @JsxGetter
    public Function getOnsubmit() {
        return getEventHandler(Event.TYPE_SUBMIT);
    }

    /**
     * Sets the {@code onsubmit} event handler for this element.
     * @param onsubmit the {@code onsubmit} event handler for this element
     */
    @JsxSetter
    public void setOnsubmit(final Object onsubmit) {
        setEventHandler(Event.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Returns the {@code onwheel} event handler for this element.
     * @return the {@code onwheel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    @Override
    public Function getOnwheel() {
        return super.getOnwheel();
    }

    /**
     * Sets the {@code onwheel} event handler for this element.
     * @param onwheel the {@code onwheel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    @Override
    public void setOnwheel(final Object onwheel) {
        super.setOnwheel(onwheel);
    }

    /**
     * Returns the {@code contentEditable} property.
     * @return the {@code contentEditable} property
     */
    @JsxGetter
    public String getContentEditable() {
        final String attribute = getDomNodeOrDie().getAttribute("contentEditable");
        if (ATTRIBUTE_NOT_DEFINED == attribute) {
            return "inherit";
        }
        if (attribute == ATTRIBUTE_VALUE_EMPTY) {
            return "true";
        }
        return attribute;
    }

    /**
     * Sets the {@code contentEditable} property.
     * @param contentEditable the {@code contentEditable} property to set
     */
    @JsxSetter
    public void setContentEditable(final String contentEditable) {
        getDomNodeOrDie().setAttribute("contentEditable", contentEditable);
    }

    /**
     * Returns the {@code isContentEditable} property.
     * @return the {@code isContentEditable} property
     */
    @JsxGetter
    public boolean isIsContentEditable() {
        final String attribute = getContentEditable();
        if ("true".equals(attribute)) {
            return true;
        }

        if ("inherit".equals(attribute)) {
            final DomNode parent = getDomNodeOrDie().getParentNode();
            if (parent != null) {
                final HtmlUnitScriptable parentScriptable = parent.getScriptableObject();
                if (parentScriptable instanceof HTMLElement) {
                    return ((HTMLElement) parentScriptable).isIsContentEditable();
                }
            }
        }
        return false;
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
     * Sets the {@code onclick} event handler for this element.
     * @param handler the {@code onclick} event handler for this element
     */
    @JsxSetter
    public void setOnclick(final Object handler) {
        setEventHandler(MouseEvent.TYPE_CLICK, handler);
    }

    /**
     * Returns the {@code onclick} event handler for this element.
     * @return the {@code onclick} event handler for this element
     */
    @JsxGetter
    public Function getOnclick() {
        return getEventHandler(MouseEvent.TYPE_CLICK);
    }

    /**
     * Sets the {@code ondblclick} event handler for this element.
     * @param handler the {@code ondblclick} event handler for this element
     */
    @JsxSetter
    public void setOndblclick(final Object handler) {
        setEventHandler(MouseEvent.TYPE_DBL_CLICK, handler);
    }

    /**
     * Returns the {@code ondblclick} event handler for this element.
     * @return the {@code ondblclick} event handler for this element
     */
    @JsxGetter
    public Function getOndblclick() {
        return getEventHandler(MouseEvent.TYPE_DBL_CLICK);
    }

    /**
     * Sets the {@code onblur} event handler for this element.
     * @param handler the {@code onblur} event handler for this element
     */
    @JsxSetter
    public void setOnblur(final Object handler) {
        setEventHandler(Event.TYPE_BLUR, handler);
    }

    /**
     * Returns the {@code onblur} event handler for this element.
     * @return the {@code onblur} event handler for this element
     */
    @JsxGetter
    public Function getOnblur() {
        return getEventHandler(Event.TYPE_BLUR);
    }

    /**
     * Sets the {@code onfocus} event handler for this element.
     * @param handler the {@code onfocus} event handler for this element
     */
    @JsxSetter
    public void setOnfocus(final Object handler) {
        setEventHandler(Event.TYPE_FOCUS, handler);
    }

    /**
     * Returns the {@code onfocus} event handler for this element.
     * @return the {@code onfocus} event handler for this element
     */
    @JsxGetter
    public Function getOnfocus() {
        return getEventHandler(Event.TYPE_FOCUS);
    }

    /**
     * Sets the {@code onkeydown} event handler for this element.
     * @param handler the {@code onkeydown} event handler for this element
     */
    @JsxSetter
    public void setOnkeydown(final Object handler) {
        setEventHandler(Event.TYPE_KEY_DOWN, handler);
    }

    /**
     * Returns the {@code onkeydown} event handler for this element.
     * @return the {@code onkeydown} event handler for this element
     */
    @JsxGetter
    public Function getOnkeydown() {
        return getEventHandler(Event.TYPE_KEY_DOWN);
    }

    /**
     * Sets the {@code onkeypress} event handler for this element.
     * @param handler the {@code onkeypress} event handler for this element
     */
    @JsxSetter
    public void setOnkeypress(final Object handler) {
        setEventHandler(Event.TYPE_KEY_PRESS, handler);
    }

    /**
     * Returns the {@code onkeypress} event handler for this element.
     * @return the {@code onkeypress} event handler for this element
     */
    @JsxGetter
    public Function getOnkeypress() {
        return getEventHandler(Event.TYPE_KEY_PRESS);
    }

    /**
     * Sets the {@code onkeyup} event handler for this element.
     * @param handler the {@code onkeyup} event handler for this element
     */
    @JsxSetter
    public void setOnkeyup(final Object handler) {
        setEventHandler(Event.TYPE_KEY_UP, handler);
    }

    /**
     * Returns the {@code onkeyup} event handler for this element.
     * @return the {@code onkeyup} event handler for this element
     */
    @JsxGetter
    public Function getOnkeyup() {
        return getEventHandler(Event.TYPE_KEY_UP);
    }

    /**
     * Sets the {@code onmousedown} event handler for this element.
     * @param handler the {@code onmousedown} event handler for this element
     */
    @JsxSetter
    public void setOnmousedown(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_DOWN, handler);
    }

    /**
     * Returns the {@code onmousedown} event handler for this element.
     * @return the {@code onmousedown} event handler for this element
     */
    @JsxGetter
    public Function getOnmousedown() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_DOWN);
    }

    /**
     * Sets the {@code onmousemove} event handler for this element.
     * @param handler the {@code onmousemove} event handler for this element
     */
    @JsxSetter
    public void setOnmousemove(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_MOVE, handler);
    }

    /**
     * Returns the {@code onmousemove} event handler for this element.
     * @return the {@code onmousemove} event handler for this element
     */
    @JsxGetter
    public Function getOnmousemove() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_MOVE);
    }

    /**
     * Sets the {@code onmouseout} event handler for this element.
     * @param handler the {@code onmouseout} event handler for this element
     */
    @JsxSetter
    public void setOnmouseout(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OUT, handler);
    }

    /**
     * Returns the {@code onmouseout} event handler for this element.
     * @return the {@code onmouseout} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseout() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OUT);
    }

    /**
     * Sets the {@code onmouseover} event handler for this element.
     * @param handler the {@code onmouseover} event handler for this element
     */
    @JsxSetter
    public void setOnmouseover(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OVER, handler);
    }

    /**
     * Returns the {@code onmouseover} event handler for this element.
     * @return the {@code onmouseover} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseover() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OVER);
    }

    /**
     * Sets the {@code onmouseup} event handler for this element.
     * @param handler the {@code onmouseup} event handler for this element
     */
    @JsxSetter
    public void setOnmouseup(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_UP, handler);
    }

    /**
     * Returns the {@code onmouseup} event handler for this element.
     * @return the {@code onmouseup} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseup() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_UP);
    }

    /**
     * Sets the {@code oncontextmenu} event handler for this element.
     * @param handler the {@code oncontextmenu} event handler for this element
     */
    @JsxSetter
    public void setOncontextmenu(final Object handler) {
        setEventHandler(MouseEvent.TYPE_CONTEXT_MENU, handler);
    }

    /**
     * Returns the {@code oncontextmenu} event handler for this element.
     * @return the {@code oncontextmenu} event handler for this element
     */
    @JsxGetter
    public Function getOncontextmenu() {
        return getEventHandler(MouseEvent.TYPE_CONTEXT_MENU);
    }

    /**
     * Sets the {@code onresize} event handler for this element.
     * @param handler the {@code onresize} event handler for this element
     */
    @JsxSetter
    public void setOnresize(final Object handler) {
        setEventHandler(Event.TYPE_RESIZE, handler);
    }

    /**
     * Returns the {@code onresize} event handler for this element.
     * @return the {@code onresize} event handler for this element
     */
    @JsxGetter
    public Function getOnresize() {
        return getEventHandler(Event.TYPE_RESIZE);
    }

    /**
     * Sets the {@code onerror} event handler for this element.
     * @param handler the {@code onerror} event handler for this element
     */
    @JsxSetter
    public void setOnerror(final Object handler) {
        setEventHandler(Event.TYPE_ERROR, handler);
    }

    /**
     * Returns the {@code onerror} event handler for this element.
     * @return the {@code onerror} event handler for this element
     */
    @JsxGetter
    public Function getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Returns the {@code oninput} event handler for this element.
     * @return the {@code oninput} event handler for this element
     */
    @JsxGetter
    public Function getOninput() {
        return getEventHandler(Event.TYPE_INPUT);
    }

    /**
     * Sets the {@code oninput} event handler for this element.
     * @param oninput the {@code oninput} event handler for this element
     */
    @JsxSetter
    public void setOninput(final Object oninput) {
        setEventHandler(Event.TYPE_INPUT, oninput);
    }

    /**
     * Returns the {@code hidden} property.
     * @return the {@code hidden} property
     */
    @JsxGetter
    public boolean isHidden() {
        return getDomNodeOrDie().isHidden();
    }

    /**
     * Sets the {@code hidden} property.
     * @param hidden the {@code hidden} value
     */
    @JsxSetter
    public void setHidden(final Object hidden) {
        if (hidden instanceof Boolean) {
            getDomNodeOrDie().setHidden((Boolean) hidden);
            return;
        }
        getDomNodeOrDie().setHidden(JavaScriptEngine.toString(hidden));
    }

    /**
     * Returns the {@code onabort} event handler for this element.
     * @return the {@code onabort} event handler for this element
     */
    @JsxGetter
    public Function getOnabort() {
        return getEventHandler(Event.TYPE_ABORT);
    }

    /**
     * Sets the {@code onabort} event handler for this element.
     * @param onabort the {@code onabort} event handler for this element
     */
    @JsxSetter
    public void setOnabort(final Object onabort) {
        setEventHandler(Event.TYPE_ABORT, onabort);
    }

    /**
     * Returns the {@code onauxclick} event handler for this element.
     * @return the {@code onauxclick} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnauxclick() {
        return getEventHandler(Event.TYPE_AUXCLICK);
    }

    /**
     * Sets the {@code onauxclick} event handler for this element.
     * @param onauxclick the {@code onauxclick} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnauxclick(final Object onauxclick) {
        setEventHandler(Event.TYPE_AUXCLICK, onauxclick);
    }

    /**
     * Returns the {@code oncancel} event handler for this element.
     * @return the {@code oncancel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncancel() {
        return getEventHandler(Event.TYPE_CANCEL);
    }

    /**
     * Sets the {@code oncancel} event handler for this element.
     * @param oncancel the {@code oncancel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncancel(final Object oncancel) {
        setEventHandler(Event.TYPE_CANCEL, oncancel);
    }

    /**
     * Returns the {@code oncanplay} event handler for this element.
     * @return the {@code oncanplay} event handler for this element
     */
    @JsxGetter
    public Function getOncanplay() {
        return getEventHandler(Event.TYPE_CANPLAY);
    }

    /**
     * Sets the {@code oncanplay} event handler for this element.
     * @param oncanplay the {@code oncanplay} event handler for this element
     */
    @JsxSetter
    public void setOncanplay(final Object oncanplay) {
        setEventHandler(Event.TYPE_CANPLAY, oncanplay);
    }

    /**
     * Returns the {@code oncanplaythrough} event handler for this element.
     * @return the {@code oncanplaythrough} event handler for this element
     */
    @JsxGetter
    public Function getOncanplaythrough() {
        return getEventHandler(Event.TYPE_CANPLAYTHROUGH);
    }

    /**
     * Sets the {@code oncanplaythrough} event handler for this element.
     * @param oncanplaythrough the {@code oncanplaythrough} event handler for this element
     */
    @JsxSetter
    public void setOncanplaythrough(final Object oncanplaythrough) {
        setEventHandler(Event.TYPE_CANPLAYTHROUGH, oncanplaythrough);
    }

    /**
     * Returns the {@code onclose} event handler for this element.
     * @return the {@code onclose} event handler for this element
     */
    @JsxGetter
    public Function getOnclose() {
        return getEventHandler(Event.TYPE_CLOSE);
    }

    /**
     * Sets the {@code onclose} event handler for this element.
     * @param onclose the {@code onclose} event handler for this element
     */
    @JsxSetter
    public void setOnclose(final Object onclose) {
        setEventHandler(Event.TYPE_CLOSE, onclose);
    }

    /**
     * Returns the {@code oncuechange} event handler for this element.
     * @return the {@code oncuechange} event handler for this element
     */
    @JsxGetter
    public Function getOncuechange() {
        return getEventHandler(Event.TYPE_CUECHANGE);
    }

    /**
     * Sets the {@code oncuechange} event handler for this element.
     * @param oncuechange the {@code oncuechange} event handler for this element
     */
    @JsxSetter
    public void setOncuechange(final Object oncuechange) {
        setEventHandler(Event.TYPE_CUECHANGE, oncuechange);
    }

    /**
     * Returns the {@code ondrag} event handler for this element.
     * @return the {@code ondrag} event handler for this element
     */
    @JsxGetter
    public Function getOndrag() {
        return getEventHandler(Event.TYPE_DRAG);
    }

    /**
     * Sets the {@code ondrag} event handler for this element.
     * @param ondrag the {@code ondrag} event handler for this element
     */
    @JsxSetter
    public void setOndrag(final Object ondrag) {
        setEventHandler(Event.TYPE_DRAG, ondrag);
    }

    /**
     * Returns the {@code ondragend} event handler for this element.
     * @return the {@code ondragend} event handler for this element
     */
    @JsxGetter
    public Function getOndragend() {
        return getEventHandler(Event.TYPE_DRAGEND);
    }

    /**
     * Sets the {@code ondragend} event handler for this element.
     * @param ondragend the {@code ondragend} event handler for this element
     */
    @JsxSetter
    public void setOndragend(final Object ondragend) {
        setEventHandler(Event.TYPE_DRAGEND, ondragend);
    }

    /**
     * Returns the {@code ondragenter} event handler for this element.
     * @return the {@code ondragenter} event handler for this element
     */
    @JsxGetter
    public Function getOndragenter() {
        return getEventHandler(Event.TYPE_DRAGENTER);
    }

    /**
     * Sets the {@code ondragenter} event handler for this element.
     * @param ondragenter the {@code ondragenter} event handler for this element
     */
    @JsxSetter
    public void setOndragenter(final Object ondragenter) {
        setEventHandler(Event.TYPE_DRAGENTER, ondragenter);
    }

    /**
     * Returns the {@code ondragleave} event handler for this element.
     * @return the {@code ondragleave} event handler for this element
     */
    @JsxGetter
    public Function getOndragleave() {
        return getEventHandler(Event.TYPE_DRAGLEAVE);
    }

    /**
     * Sets the {@code ondragleave} event handler for this element.
     * @param ondragleave the {@code ondragleave} event handler for this element
     */
    @JsxSetter
    public void setOndragleave(final Object ondragleave) {
        setEventHandler(Event.TYPE_DRAGLEAVE, ondragleave);
    }

    /**
     * Returns the {@code ondragover} event handler for this element.
     * @return the {@code ondragover} event handler for this element
     */
    @JsxGetter
    public Function getOndragover() {
        return getEventHandler(Event.TYPE_DRAGOVER);
    }

    /**
     * Sets the {@code ondragover} event handler for this element.
     * @param ondragover the {@code ondragover} event handler for this element
     */
    @JsxSetter
    public void setOndragover(final Object ondragover) {
        setEventHandler(Event.TYPE_DRAGOVER, ondragover);
    }

    /**
     * Returns the {@code ondragstart} event handler for this element.
     * @return the {@code ondragstart} event handler for this element
     */
    @JsxGetter
    public Function getOndragstart() {
        return getEventHandler(Event.TYPE_DRAGSTART);
    }

    /**
     * Sets the {@code ondragstart} event handler for this element.
     * @param ondragstart the {@code ondragstart} event handler for this element
     */
    @JsxSetter
    public void setOndragstart(final Object ondragstart) {
        setEventHandler(Event.TYPE_DRAGSTART, ondragstart);
    }

    /**
     * Returns the {@code ondrop} event handler for this element.
     * @return the {@code ondrop} event handler for this element
     */
    @JsxGetter
    public Function getOndrop() {
        return getEventHandler(Event.TYPE_DROP);
    }

    /**
     * Sets the {@code ondrop} event handler for this element.
     * @param ondrop the {@code ondrop} event handler for this element
     */
    @JsxSetter
    public void setOndrop(final Object ondrop) {
        setEventHandler(Event.TYPE_DROP, ondrop);
    }

    /**
     * Returns the {@code ondurationchange} event handler for this element.
     * @return the {@code ondurationchange} event handler for this element
     */
    @JsxGetter
    public Function getOndurationchange() {
        return getEventHandler(Event.TYPE_DURATIONCHANGE);
    }

    /**
     * Sets the {@code ondurationchange} event handler for this element.
     * @param ondurationchange the {@code ondurationchange} event handler for this element
     */
    @JsxSetter
    public void setOndurationchange(final Object ondurationchange) {
        setEventHandler(Event.TYPE_DURATIONCHANGE, ondurationchange);
    }

    /**
     * Returns the {@code onemptied} event handler for this element.
     * @return the {@code onemptied} event handler for this element
     */
    @JsxGetter
    public Function getOnemptied() {
        return getEventHandler(Event.TYPE_EMPTIED);
    }

    /**
     * Sets the {@code onemptied} event handler for this element.
     * @param onemptied the {@code onemptied} event handler for this element
     */
    @JsxSetter
    public void setOnemptied(final Object onemptied) {
        setEventHandler(Event.TYPE_EMPTIED, onemptied);
    }

    /**
     * Returns the {@code onended} event handler for this element.
     * @return the {@code onended} event handler for this element
     */
    @JsxGetter
    public Function getOnended() {
        return getEventHandler(Event.TYPE_ENDED);
    }

    /**
     * Sets the {@code onended} event handler for this element.
     * @param onended the {@code onended} event handler for this element
     */
    @JsxSetter
    public void setOnended(final Object onended) {
        setEventHandler(Event.TYPE_ENDED, onended);
    }

    /**
     * Returns the {@code ongotpointercapture} event handler for this element.
     * @return the {@code ongotpointercapture} event handler for this element
     */
    @JsxGetter
    public Function getOngotpointercapture() {
        return getEventHandler(Event.TYPE_GOTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code ongotpointercapture} event handler for this element.
     * @param ongotpointercapture the {@code ongotpointercapture} event handler for this element
     */
    @JsxSetter
    public void setOngotpointercapture(final Object ongotpointercapture) {
        setEventHandler(Event.TYPE_GOTPOINTERCAPTURE, ongotpointercapture);
    }

    /**
     * Returns the {@code oninvalid} event handler for this element.
     * @return the {@code oninvalid} event handler for this element
     */
    @JsxGetter
    public Function getOninvalid() {
        return getEventHandler(Event.TYPE_INVALID);
    }

    /**
     * Sets the {@code oninvalid} event handler for this element.
     * @param oninvalid the {@code oninvalid} event handler for this element
     */
    @JsxSetter
    public void setOninvalid(final Object oninvalid) {
        setEventHandler(Event.TYPE_INVALID, oninvalid);
    }

    /**
     * Returns the {@code onload} event handler for this element.
     * @return the {@code onload} event handler for this element
     */
    @JsxGetter
    public Function getOnload() {
        if (this instanceof HTMLBodyElement) {
            return getWindow().getEventHandler(Event.TYPE_LOAD);
        }

        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the {@code onload} event handler for this element.
     * @param onload the {@code onload} event handler for this element
     */
    @JsxSetter
    public void setOnload(final Object onload) {
        if (this instanceof HTMLBodyElement) {
            getWindow().setEventHandler(Event.TYPE_LOAD, onload);
            return;
        }

        setEventHandler(Event.TYPE_LOAD, onload);
    }

    /**
     * Returns the {@code onloadeddata} event handler for this element.
     * @return the {@code onloadeddata} event handler for this element
     */
    @JsxGetter
    public Function getOnloadeddata() {
        return getEventHandler(Event.TYPE_LOADEDDATA);
    }

    /**
     * Sets the {@code onloadeddata} event handler for this element.
     * @param onloadeddata the {@code onloadeddata} event handler for this element
     */
    @JsxSetter
    public void setOnloadeddata(final Object onloadeddata) {
        setEventHandler(Event.TYPE_LOADEDDATA, onloadeddata);
    }

    /**
     * Returns the {@code onloadedmetadata} event handler for this element.
     * @return the {@code onloadedmetadata} event handler for this element
     */
    @JsxGetter
    public Function getOnloadedmetadata() {
        return getEventHandler(Event.TYPE_LOADEDMETADATA);
    }

    /**
     * Sets the {@code onloadedmetadata} event handler for this element.
     * @param onloadedmetadata the {@code onloadedmetadata} event handler for this element
     */
    @JsxSetter
    public void setOnloadedmetadata(final Object onloadedmetadata) {
        setEventHandler(Event.TYPE_LOADEDMETADATA, onloadedmetadata);
    }

    /**
     * Returns the {@code onloadstart} event handler for this element.
     * @return the {@code onloadstart} event handler for this element
     */
    @JsxGetter
    public Function getOnloadstart() {
        return getEventHandler(Event.TYPE_LOAD_START);
    }

    /**
     * Sets the {@code onloadstart} event handler for this element.
     * @param onloadstart the {@code onloadstart} event handler for this element
     */
    @JsxSetter
    public void setOnloadstart(final Object onloadstart) {
        setEventHandler(Event.TYPE_LOAD_START, onloadstart);
    }

    /**
     * Returns the {@code onlostpointercapture} event handler for this element.
     * @return the {@code onlostpointercapture} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnlostpointercapture() {
        return getEventHandler(Event.TYPE_LOSTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code onlostpointercapture} event handler for this element.
     * @param onlostpointercapture the {@code onlostpointercapture} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnlostpointercapture(final Object onlostpointercapture) {
        setEventHandler(Event.TYPE_LOSTPOINTERCAPTURE, onlostpointercapture);
    }

    /**
     * Returns the {@code onmouseenter} event handler for this element.
     * @return the {@code onmouseenter} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseenter() {
        return getEventHandler(Event.TYPE_MOUDEENTER);
    }

    /**
     * Sets the {@code onmouseenter} event handler for this element.
     * @param onmouseenter the {@code onmouseenter} event handler for this element
     */
    @JsxSetter
    public void setOnmouseenter(final Object onmouseenter) {
        setEventHandler(Event.TYPE_MOUDEENTER, onmouseenter);
    }

    /**
     * Returns the {@code onmouseleave} event handler for this element.
     * @return the {@code onmouseleave} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseleave() {
        return getEventHandler(Event.TYPE_MOUSELEAVE);
    }

    /**
     * Sets the {@code onmouseleave} event handler for this element.
     * @param onmouseleave the {@code onmouseleave} event handler for this element
     */
    @JsxSetter
    public void setOnmouseleave(final Object onmouseleave) {
        setEventHandler(Event.TYPE_MOUSELEAVE, onmouseleave);
    }

    /**
     * Returns the {@code onmousewheel} event handler for this element.
     * @return the {@code onmousewheel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnmousewheel() {
        return getEventHandler(Event.TYPE_MOUSEWHEEL);
    }

    /**
     * Sets the {@code onmousewheel} event handler for this element.
     * @param onmousewheel the {@code onmousewheel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnmousewheel(final Object onmousewheel) {
        setEventHandler(Event.TYPE_MOUSEWHEEL, onmousewheel);
    }

    /**
     * Returns the {@code onpause} event handler for this element.
     * @return the {@code onpause} event handler for this element
     */
    @JsxGetter
    public Function getOnpause() {
        return getEventHandler(Event.TYPE_PAUSE);
    }

    /**
     * Sets the {@code onpause} event handler for this element.
     * @param onpause the {@code onpause} event handler for this element
     */
    @JsxSetter
    public void setOnpause(final Object onpause) {
        setEventHandler(Event.TYPE_PAUSE, onpause);
    }

    /**
     * Returns the {@code onplay} event handler for this element.
     * @return the {@code onplay} event handler for this element
     */
    @JsxGetter
    public Function getOnplay() {
        return getEventHandler(Event.TYPE_PLAY);
    }

    /**
     * Sets the {@code onplay} event handler for this element.
     * @param onplay the {@code onplay} event handler for this element
     */
    @JsxSetter
    public void setOnplay(final Object onplay) {
        setEventHandler(Event.TYPE_PLAY, onplay);
    }

    /**
     * Returns the {@code onplaying} event handler for this element.
     * @return the {@code onplaying} event handler for this element
     */
    @JsxGetter
    public Function getOnplaying() {
        return getEventHandler(Event.TYPE_PLAYING);
    }

    /**
     * Sets the {@code onplaying} event handler for this element.
     * @param onplaying the {@code onplaying} event handler for this element
     */
    @JsxSetter
    public void setOnplaying(final Object onplaying) {
        setEventHandler(Event.TYPE_PLAYING, onplaying);
    }

    /**
     * Returns the {@code onpointercancel} event handler for this element.
     * @return the {@code onpointercancel} event handler for this element
     */
    @JsxGetter
    public Function getOnpointercancel() {
        return getEventHandler(Event.TYPE_POINTERCANCEL);
    }

    /**
     * Sets the {@code onpointercancel} event handler for this element.
     * @param onpointercancel the {@code onpointercancel} event handler for this element
     */
    @JsxSetter
    public void setOnpointercancel(final Object onpointercancel) {
        setEventHandler(Event.TYPE_POINTERCANCEL, onpointercancel);
    }

    /**
     * Returns the {@code onpointerdown} event handler for this element.
     * @return the {@code onpointerdown} event handler for this element
     */
    @JsxGetter
    public Function getOnpointerdown() {
        return getEventHandler(Event.TYPE_POINTERDOWN);
    }

    /**
     * Sets the {@code onpointerdown} event handler for this element.
     * @param onpointerdown the {@code onpointerdown} event handler for this element
     */
    @JsxSetter
    public void setOnpointerdown(final Object onpointerdown) {
        setEventHandler(Event.TYPE_POINTERDOWN, onpointerdown);
    }

    /**
     * Returns the {@code onpointerenter} event handler for this element.
     * @return the {@code onpointerenter} event handler for this element
     */
    @JsxGetter
    public Function getOnpointerenter() {
        return getEventHandler(Event.TYPE_POINTERENTER);
    }

    /**
     * Sets the {@code onpointerenter} event handler for this element.
     * @param onpointerenter the {@code onpointerenter} event handler for this element
     */
    @JsxSetter
    public void setOnpointerenter(final Object onpointerenter) {
        setEventHandler(Event.TYPE_POINTERENTER, onpointerenter);
    }

    /**
     * Returns the {@code onpointerleave} event handler for this element.
     * @return the {@code onpointerleave} event handler for this element
     */
    @JsxGetter
    public Function getOnpointerleave() {
        return getEventHandler(Event.TYPE_POINTERLEAVE);
    }

    /**
     * Sets the {@code onpointerleave} event handler for this element.
     * @param onpointerleave the {@code onpointerleave} event handler for this element
     */
    @JsxSetter
    public void setOnpointerleave(final Object onpointerleave) {
        setEventHandler(Event.TYPE_POINTERLEAVE, onpointerleave);
    }

    /**
     * Returns the {@code onpointermove} event handler for this element.
     * @return the {@code onpointermove} event handler for this element
     */
    @JsxGetter
    public Function getOnpointermove() {
        return getEventHandler(Event.TYPE_POINTERMOVE);
    }

    /**
     * Sets the {@code onpointermove} event handler for this element.
     * @param onpointermove the {@code onpointermove} event handler for this element
     */
    @JsxSetter
    public void setOnpointermove(final Object onpointermove) {
        setEventHandler(Event.TYPE_POINTERMOVE, onpointermove);
    }

    /**
     * Returns the {@code onpointerout} event handler for this element.
     * @return the {@code onpointerout} event handler for this element
     */
    @JsxGetter
    public Function getOnpointerout() {
        return getEventHandler(Event.TYPE_POINTEROUT);
    }

    /**
     * Sets the {@code onpointerout} event handler for this element.
     * @param onpointerout the {@code onpointerout} event handler for this element
     */
    @JsxSetter
    public void setOnpointerout(final Object onpointerout) {
        setEventHandler(Event.TYPE_POINTEROUT, onpointerout);
    }

    /**
     * Returns the {@code onpointerover} event handler for this element.
     * @return the {@code onpointerover} event handler for this element
     */
    @JsxGetter
    public Function getOnpointerover() {
        return getEventHandler(Event.TYPE_POINTEROVER);
    }

    /**
     * Sets the {@code onpointerover} event handler for this element.
     * @param onpointerover the {@code onpointerover} event handler for this element
     */
    @JsxSetter
    public void setOnpointerover(final Object onpointerover) {
        setEventHandler(Event.TYPE_POINTEROVER, onpointerover);
    }

    /**
     * Returns the {@code onpointerup} event handler for this element.
     * @return the {@code onpointerup} event handler for this element
     */
    @JsxGetter
    public Function getOnpointerup() {
        return getEventHandler(Event.TYPE_POINTERUP);
    }

    /**
     * Sets the {@code onpointerup} event handler for this element.
     * @param onpointerup the {@code onpointerup} event handler for this element
     */
    @JsxSetter
    public void setOnpointerup(final Object onpointerup) {
        setEventHandler(Event.TYPE_POINTERUP, onpointerup);
    }

    /**
     * Returns the {@code onprogress} event handler for this element.
     * @return the {@code onprogress} event handler for this element
     */
    @JsxGetter
    public Function getOnprogress() {
        return getEventHandler(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the {@code onprogress} event handler for this element.
     * @param onprogress the {@code onprogress} event handler for this element
     */
    @JsxSetter
    public void setOnprogress(final Object onprogress) {
        setEventHandler(Event.TYPE_PROGRESS, onprogress);
    }

    /**
     * Returns the {@code onratechange} event handler for this element.
     * @return the {@code onratechange} event handler for this element
     */
    @JsxGetter
    public Function getOnratechange() {
        return getEventHandler(Event.TYPE_RATECHANGE);
    }

    /**
     * Sets the {@code onratechange} event handler for this element.
     * @param onratechange the {@code onratechange} event handler for this element
     */
    @JsxSetter
    public void setOnratechange(final Object onratechange) {
        setEventHandler(Event.TYPE_RATECHANGE, onratechange);
    }

    /**
     * Returns the {@code onreset} event handler for this element.
     * @return the {@code onreset} event handler for this element
     */
    @JsxGetter
    public Function getOnreset() {
        return getEventHandler(Event.TYPE_RESET);
    }

    /**
     * Sets the {@code onreset} event handler for this element.
     * @param onreset the {@code onreset} event handler for this element
     */
    @JsxSetter
    public void setOnreset(final Object onreset) {
        setEventHandler(Event.TYPE_RESET, onreset);
    }

    /**
     * Returns the {@code onscroll} event handler for this element.
     * @return the {@code onscroll} event handler for this element
     */
    @JsxGetter
    public Function getOnscroll() {
        return getEventHandler(Event.TYPE_SCROLL);
    }

    /**
     * Sets the {@code onscroll} event handler for this element.
     * @param onscroll the {@code onscroll} event handler for this element
     */
    @JsxSetter
    public void setOnscroll(final Object onscroll) {
        setEventHandler(Event.TYPE_SCROLL, onscroll);
    }

    /**
     * Returns the {@code onscrollend} event handler for this element.
     * @return the {@code onscrollend} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF})
    public Function getOnscrollend() {
        return getEventHandler(Event.TYPE_SCROLLEND);
    }

    /**
     * Sets the {@code onscrollend} event handler for this element.
     * @param onscrollend the {@code onscrollend} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF})
    public void setOnscrollend(final Object onscrollend) {
        setEventHandler(Event.TYPE_SCROLLEND, onscrollend);
    }

    /**
     * Returns the {@code onseeked} event handler for this element.
     * @return the {@code onseeked} event handler for this element
     */
    @JsxGetter
    public Function getOnseeked() {
        return getEventHandler(Event.TYPE_SEEKED);
    }

    /**
     * Sets the {@code onseeked} event handler for this element.
     * @param onseeked the {@code onseeked} event handler for this element
     */
    @JsxSetter
    public void setOnseeked(final Object onseeked) {
        setEventHandler(Event.TYPE_SEEKED, onseeked);
    }

    /**
     * Returns the {@code onseeking} event handler for this element.
     * @return the {@code onseeking} event handler for this element
     */
    @JsxGetter
    public Function getOnseeking() {
        return getEventHandler(Event.TYPE_SEEKING);
    }

    /**
     * Sets the {@code onseeking} event handler for this element.
     * @param onseeking the {@code onseeking} event handler for this element
     */
    @JsxSetter
    public void setOnseeking(final Object onseeking) {
        setEventHandler(Event.TYPE_SEEKING, onseeking);
    }

    /**
     * Returns the {@code onselect} event handler for this element.
     * @return the {@code onselect} event handler for this element
     */
    @JsxGetter
    public Function getOnselect() {
        return getEventHandler(Event.TYPE_SELECT);
    }

    /**
     * Sets the {@code onselect} event handler for this element.
     * @param onselect the {@code onselect} event handler for this element
     */
    @JsxSetter
    public void setOnselect(final Object onselect) {
        setEventHandler(Event.TYPE_SELECT, onselect);
    }

    /**
     * Returns the {@code onstalled} event handler for this element.
     * @return the {@code onstalled} event handler for this element
     */
    @JsxGetter
    public Function getOnstalled() {
        return getEventHandler(Event.TYPE_STALLED);
    }

    /**
     * Sets the {@code onstalled} event handler for this element.
     * @param onstalled the {@code onstalled} event handler for this element
     */
    @JsxSetter
    public void setOnstalled(final Object onstalled) {
        setEventHandler(Event.TYPE_STALLED, onstalled);
    }

    /**
     * Returns the {@code onsuspend} event handler for this element.
     * @return the {@code onsuspend} event handler for this element
     */
    @JsxGetter
    public Function getOnsuspend() {
        return getEventHandler(Event.TYPE_SUSPEND);
    }

    /**
     * Sets the {@code onsuspend} event handler for this element.
     * @param onsuspend the {@code onsuspend} event handler for this element
     */
    @JsxSetter
    public void setOnsuspend(final Object onsuspend) {
        setEventHandler(Event.TYPE_SUSPEND, onsuspend);
    }

    /**
     * Returns the {@code ontimeupdate} event handler for this element.
     * @return the {@code ontimeupdate} event handler for this element
     */
    @JsxGetter
    public Function getOntimeupdate() {
        return getEventHandler(Event.TYPE_TIMEUPDATE);
    }

    /**
     * Sets the {@code ontimeupdate} event handler for this element.
     * @param ontimeupdate the {@code ontimeupdate} event handler for this element
     */
    @JsxSetter
    public void setOntimeupdate(final Object ontimeupdate) {
        setEventHandler(Event.TYPE_TIMEUPDATE, ontimeupdate);
    }

    /**
     * Returns the {@code ontoggle} event handler for this element.
     * @return the {@code ontoggle} event handler for this element
     */
    @JsxGetter
    public Function getOntoggle() {
        return getEventHandler(Event.TYPE_TOGGLE);
    }

    /**
     * Sets the {@code ontoggle} event handler for this element.
     * @param ontoggle the {@code ontoggle} event handler for this element
     */
    @JsxSetter
    public void setOntoggle(final Object ontoggle) {
        setEventHandler(Event.TYPE_TOGGLE, ontoggle);
    }

    /**
     * Returns the {@code onvolumechange} event handler for this element.
     * @return the {@code onvolumechange} event handler for this element
     */
    @JsxGetter
    public Function getOnvolumechange() {
        return getEventHandler(Event.TYPE_VOLUMECHANGE);
    }

    /**
     * Sets the {@code onvolumechange} event handler for this element.
     * @param onvolumechange the {@code onvolumechange} event handler for this element
     */
    @JsxSetter
    public void setOnvolumechange(final Object onvolumechange) {
        setEventHandler(Event.TYPE_VOLUMECHANGE, onvolumechange);
    }

    /**
     * Returns the {@code onwaiting} event handler for this element.
     * @return the {@code onwaiting} event handler for this element
     */
    @JsxGetter
    public Function getOnwaiting() {
        return getEventHandler(Event.TYPE_WAITING);
    }

    /**
     * Sets the {@code onwaiting} event handler for this element.
     * @param onwaiting the {@code onwaiting} event handler for this element
     */
    @JsxSetter
    public void setOnwaiting(final Object onwaiting) {
        setEventHandler(Event.TYPE_WAITING, onwaiting);
    }

    /**
     * Returns the {@code oncopy} event handler for this element.
     * @return the {@code oncopy} event handler for this element
     */
    @JsxGetter
    public Function getOncopy() {
        return getEventHandler(Event.TYPE_COPY);
    }

    /**
     * Sets the {@code oncopy} event handler for this element.
     * @param oncopy the {@code oncopy} event handler for this element
     */
    @JsxSetter
    public void setOncopy(final Object oncopy) {
        setEventHandler(Event.TYPE_COPY, oncopy);
    }

    /**
     * Returns the {@code oncut} event handler for this element.
     * @return the {@code oncut} event handler for this element
     */
    @JsxGetter
    public Function getOncut() {
        return getEventHandler(Event.TYPE_CUT);
    }

    /**
     * Sets the {@code oncut} event handler for this element.
     * @param oncut the {@code oncut} event handler for this element
     */
    @JsxSetter
    public void setOncut(final Object oncut) {
        setEventHandler(Event.TYPE_CUT, oncut);
    }

    /**
     * Returns the {@code onpaste} event handler for this element.
     * @return the {@code onpaste} event handler for this element
     */
    @JsxGetter
    public Function getOnpaste() {
        return getEventHandler(Event.TYPE_PASTE);
    }

    /**
     * Sets the {@code onpaste} event handler for this element.
     * @param onpaste the {@code onpaste} event handler for this element
     */
    @JsxSetter
    public void setOnpaste(final Object onpaste) {
        setEventHandler(Event.TYPE_PASTE, onpaste);
    }

    /**
     * Returns the {@code onmozfullscreenchange} event handler for this element.
     * @return the {@code onmozfullscreenchange} event handler for this element
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnmozfullscreenchange() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onmozfullscreenchange} event handler for this element.
     * @param onmozfullscreenchange the {@code onmozfullscreenchange} event handler for this element
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnmozfullscreenchange(final Object onmozfullscreenchange) {
        setEventHandler(Event.TYPE_MOZFULLSCREENCHANGE, onmozfullscreenchange);
    }

    /**
     * Returns the {@code onmozfullscreenerror} event handler for this element.
     * @return the {@code onmozfullscreenerror} event handler for this element
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnmozfullscreenerror() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENERROR);
    }

    /**
     * Sets the {@code onmozfullscreenerror} event handler for this element.
     * @param onmozfullscreenerror the {@code onmozfullscreenerror} event handler for this element
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnmozfullscreenerror(final Object onmozfullscreenerror) {
        setEventHandler(Event.TYPE_MOZFULLSCREENERROR, onmozfullscreenerror);
    }

    /**
     * Returns the {@code onselectstart} event handler for this element.
     * @return the {@code onselectstart} event handler for this element
     */
    @JsxGetter
    public Function getOnselectstart() {
        return getEventHandler(Event.TYPE_SELECTSTART);
    }

    /**
     * Sets the {@code onselectstart} event handler for this element.
     * @param onselectstart the {@code onselectstart} event handler for this element
     */
    @JsxSetter
    public void setOnselectstart(final Object onselectstart) {
        setEventHandler(Event.TYPE_SELECTSTART, onselectstart);
    }

    /**
     * Returns the value of the JavaScript attribute {@code name}.
     *
     * @return the value of this attribute
     */
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.NAME_ATTRIBUTE);
    }

    /**
     * Sets the value of the JavaScript attribute {@code name}.
     *
     * @param newName the new name
     */
    public void setName(final String newName) {
        getDomNodeOrDie().setAttribute(DomElement.NAME_ATTRIBUTE, newName);
    }

    /**
     * Returns the value of the JavaScript attribute {@code value}.
     *
     * @return the value of this attribute
     */
    public Object getValue() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.VALUE_ATTRIBUTE);
    }

    /**
     * Sets the value of the JavaScript attribute {@code value}.
     *
     * @param newValue the new value
     */
    public void setValue(final Object newValue) {
        getDomNodeOrDie().setAttribute(DomElement.VALUE_ATTRIBUTE, JavaScriptEngine.toString(newValue));
    }

    /**
     * Returns the value of the JavaScript attribute {@code enterKeyHint}.
     *
     * @return the value of this attribute
     */
    @JsxGetter
    public String getEnterKeyHint() {
        String value = getDomNodeOrDie().getAttributeDirect("enterkeyhint");
        if (ATTRIBUTE_NOT_DEFINED == value || ATTRIBUTE_VALUE_EMPTY == value) {
            return "";
        }

        value = value.toLowerCase(Locale.ROOT);
        if (ENTER_KEY_HINT_VALUES.contains(value)) {
            return value;
        }
        return "";
    }

    /**
     * Sets the value of the JavaScript attribute {@code enterKeyHint}.
     *
     * @param enterKeyHint the new value
     */
    @JsxSetter
    public void setEnterKeyHint(final Object enterKeyHint) {
        if (enterKeyHint == null || JavaScriptEngine.isUndefined(enterKeyHint)) {
            getDomNodeOrDie().removeAttribute("enterkeyhint");
            return;
        }
        getDomNodeOrDie().setAttribute("enterkeyhint", JavaScriptEngine.toString(enterKeyHint));
    }

    /**
     * Returns the {@code onanimationcancel} event handler.
     * @return the {@code onanimationcancel} event handler
     */
    @JsxGetter({FF, FF_ESR})
    public Function getOnanimationcancel() {
        return getEventHandler(Event.TYPE_ANIMATIONCANCEL);
    }

    /**
     * Sets the {@code onanimationcancel} event handler.
     * @param onanimationcancel the {@code onanimationcancel} event handler
     */
    @JsxSetter({FF, FF_ESR})
    public void setOnanimationcancel(final Object onanimationcancel) {
        setEventHandler(Event.TYPE_ANIMATIONCANCEL, onanimationcancel);
    }

    /**
     * Returns the {@code onanimationend} event handler.
     * @return the {@code onanimationend} event handler
     */
    @JsxGetter
    public Function getOnanimationend() {
        return getEventHandler(Event.TYPE_ANIMATIONEND);
    }

    /**
     * Sets the {@code onanimationend} event handler.
     * @param onanimationend the {@code onanimationend} event handler
     */
    @JsxSetter
    public void setOnanimationend(final Object onanimationend) {
        setEventHandler(Event.TYPE_ANIMATIONEND, onanimationend);
    }

    /**
     * Returns the {@code onanimationiteration} event handler.
     * @return the {@code onanimationiteration} event handler
     */
    @JsxGetter
    public Function getOnanimationiteration() {
        return getEventHandler(Event.TYPE_ANIMATIONITERATION);
    }

    /**
     * Sets the {@code onanimationiteration} event handler.
     * @param onanimationiteration the {@code onanimationiteration} event handler
     */
    @JsxSetter
    public void setOnanimationiteration(final Object onanimationiteration) {
        setEventHandler(Event.TYPE_ANIMATIONITERATION, onanimationiteration);
    }

    /**
     * Returns the {@code onanimationstart} event handler.
     * @return the {@code onanimationstart} event handler
     */
    @JsxGetter
    public Function getOnanimationstart() {
        return getEventHandler(Event.TYPE_ANIMATIONSTART);
    }

    /**
     * Sets the {@code onanimationstart} event handler.
     * @param onanimationstart the {@code onanimationstart} event handler
     */
    @JsxSetter
    public void setOnanimationstart(final Object onanimationstart) {
        setEventHandler(Event.TYPE_ANIMATIONSTART, onanimationstart);
    }

    /**
     * Returns the {@code onselectionchange} event handler for this element.
     * @return the {@code onselectionchange} event handler for this element
     */
    @JsxGetter
    public Function getOnselectionchange() {
        return getEventHandler(Event.TYPE_SELECTIONCHANGE);
    }

    /**
     * Sets the {@code onselectionchange} event handler for this element.
     * @param onselectionchange the {@code onselectionchange} event handler for this element
     */
    @JsxSetter
    public void setOnselectionchange(final Object onselectionchange) {
        setEventHandler(Event.TYPE_SELECTIONCHANGE, onselectionchange);
    }

    /**
     * Returns the {@code ontransitioncancel} event handler for this element.
     * @return the {@code ontransitioncancel} event handler for this element
     */
    @JsxGetter
    public Function getOntransitioncancel() {
        return getEventHandler(Event.TYPE_ONTRANSITIONCANCEL);
    }

    /**
     * Sets the {@code ontransitioncancel} event handler for this element.
     * @param ontransitioncancel the {@code ontransitioncancel} event handler for this element
     */
    @JsxSetter
    public void setOntransitioncancel(final Object ontransitioncancel) {
        setEventHandler(Event.TYPE_ONTRANSITIONCANCEL, ontransitioncancel);
    }

    /**
     * Returns the {@code ontransitionend} event handler for this element.
     * @return the {@code ontransitionend} event handler for this element
     */
    @JsxGetter
    public Function getOntransitionend() {
        return getEventHandler(Event.TYPE_ONTRANSITIONEND);
    }

    /**
     * Sets the {@code ontransitionend} event handler for this element.
     * @param ontransitionend the {@code ontransitionend} event handler for this element
     */
    @JsxSetter
    public void setOntransitionend(final Object ontransitionend) {
        setEventHandler(Event.TYPE_ONTRANSITIONEND, ontransitionend);
    }

    /**
     * Returns the {@code ontransitionrun} event handler for this element.
     * @return the {@code ontransitionrun} event handler for this element
     */
    @JsxGetter
    public Function getOntransitionrun() {
        return getEventHandler(Event.TYPE_ONTRANSITIONRUN);
    }

    /**
     * Sets the {@code ontransitionrun} event handler for this element.
     * @param ontransitionrun the {@code ontransitionrun} event handler for this element
     */
    @JsxSetter
    public void setOntransitionrun(final Object ontransitionrun) {
        setEventHandler(Event.TYPE_ONTRANSITIONRUN, ontransitionrun);
    }

    /**
     * Returns the {@code ontransitionstart} event handler for this element.
     * @return the {@code ontransitionstart} event handler for this element
     */
    @JsxGetter
    public Function getOntransitionstart() {
        return getEventHandler(Event.TYPE_ONTRANSITIONSTART);
    }

    /**
     * Sets the {@code ontransitionstart} event handler for this element.
     * @param ontransitionstart the {@code ontransitionstart} event handler for this element
     */
    @JsxSetter
    public void setOntransitionstart(final Object ontransitionstart) {
        setEventHandler(Event.TYPE_ONTRANSITIONSTART, ontransitionstart);
    }
}
