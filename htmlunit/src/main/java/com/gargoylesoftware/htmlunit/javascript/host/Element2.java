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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Locale;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap2;
import com.gargoylesoftware.htmlunit.javascript.NashornJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.EventNode2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameSetElement2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.Source;

/**
 * A JavaScript object for {@code Element}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 */
@ScriptClass
public class Element2 extends EventNode2 {

    private NamedNodeMap2 attributes_;
    private Map<String, HTMLCollection> elementsByTagName_; // for performance and for equality (==)
    private CSSStyleDeclaration2 style_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static Element2 constructor(final boolean newObj, final Object self) {
        final Element2 host = new Element2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);
        createEventHandlers();
        style_ = new CSSStyleDeclaration2(this);
    }

    /**
     * Convert JavaScript snippets defined in the attribute map to executable event handlers.
     */
    private void createEventHandlers() {
        final DomElement htmlElt = getDomNodeOrDie();
        for (final DomAttr attr : htmlElt.getAttributesMap().values()) {
            final String eventName = attr.getName().toLowerCase(Locale.ROOT);
            if (eventName.startsWith("on")) {
                createEventHandler(eventName, attr.getValue());
            }
        }
    }

    /**
     * Callback method which allows different HTML element types to perform custom
     * initialization of computed styles. For example, body elements in most browsers
     * have default values for their margins.
     *
     * @param style the style to initialize
     */
    public void setDefaults(final ComputedCSSStyleDeclaration2 style) {
        // Empty by default; override as necessary.
    }

    /**
     * Create the event handler function from the attribute value.
     * @param eventName the event name (ex: "onclick")
     * @param attrValue the attribute value
     */
    protected void createEventHandler(final String eventName, String attrValue) {
        final DomElement htmlElt = getDomNodeOrDie();
        // TODO: check that it is an "allowed" event for the browser, and take care to the case
        attrValue = "function " + eventName + "(event) {" + attrValue + "\n}";
        final Source source = Source.sourceFor(eventName + " event for " + htmlElt
                + " in " + htmlElt.getPage().getUrl(), attrValue);

        ScriptObject thisObject = this;
        final Global global = getWindow().getWebWindow().getScriptableObject();
        if (thisObject instanceof HTMLBodyElement2 || thisObject instanceof HTMLFrameSetElement2) {
            thisObject = global;
        }
        final Context context = global.getContext();
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != global;
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            thisObject.addBoundProperties(global);
            final ScriptFunction script = context.compileScript(source, thisObject);
            ScriptRuntime.apply(script, thisObject);
            final ScriptFunction eventHandler = (ScriptFunction) thisObject.get(eventName);
            setEventHandler(eventName, eventHandler);
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getDomNodeOrDie() {
        return (DomElement) super.getDomNodeOrDie();
    }

    /**
     * Returns the style object for this element.
     * @return the style object for this element
     */
    @Getter
    public CSSStyleDeclaration2 getStyle() {
        return style_;
    }

    /**
     * Sets the styles for this element.
     * @param style the style of the element
     */
    @Setter
    public void setStyle(final String style) {
        if (!getBrowserVersion().hasFeature(JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY)) {
            getStyle().setCssText(style);
        }
    }

    /**
     * Gets the first ancestor instance of {@link Element}. It is mostly identical
     * to {@link #getParent()} except that it skips non {@link Element} nodes.
     * @return the parent element
     * @see #getParent()
     */
    @Override
    public Element2 getParentElement() {
        Node2 parent = getParent();
        while (parent != null && !(parent instanceof Element2)) {
            parent = parent.getParent();
        }
        return (Element2) parent;
    }

    /**
     * Retrieves an object that specifies the bounds of a collection of TextRectangle objects.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536433.aspx">MSDN doc</a>
     * @return an object that specifies the bounds of a collection of TextRectangle objects
     */
    @Function
    public ClientRect2 getBoundingClientRect() {
        if (!getDomNodeOrDie().isAttachedToPage()
                && getBrowserVersion().hasFeature(JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED)) {
            throw new RuntimeException("Element is not attache to a page");
        }

        final ClientRect2 textRectangle = new ClientRect2(1, 1, 1, 1);
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
        textRectangle.setProto(global.getPrototype(textRectangle.getClass()));

        return textRectangle;
    }

    /**
     * Removes the specified attribute.
     * @param namespaceURI the namespace URI of the attribute to remove
     * @param localName the local name of the attribute to remove
     */
    @Function
    public final void removeAttributeNS(final String namespaceURI, final String localName) {
        getDomNodeOrDie().removeAttributeNS(namespaceURI, localName);
    }

    /**
     * Returns the value of the specified attribute.
     * @param attributeName attribute name
     * @param flags IE-specific flags (see the MSDN documentation for more info)
     * @return the value of the specified attribute, {@code null} if the attribute is not defined
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536429.aspx">MSDN Documentation</a>
     * @see <a href="http://reference.sitepoint.com/javascript/Element/getAttribute">IE Bug Documentation</a>
     */
    @Function
    public Object getAttribute(final String attributeName, final Integer flags) {
        Object value = getDomNodeOrDie().getAttribute(attributeName);

        if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
            value = null;
        }

        return value;
    }

    /**
     * Sets an attribute.
     *
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    @Function
    public void setAttribute(final String name, final String value) {
        getDomNodeOrDie().setAttribute(name, value);
    }

    /**
     * Sets the attribute node for the specified attribute.
     * @param newAtt the attribute to set
     * @return the replaced attribute node, if any
     */
    @Function
    public Attr2 setAttributeNode(final Attr2 newAtt) {
        final String name = newAtt.getName();

        final NamedNodeMap2 nodes = getAttributes();
        final Attr2 replacedAtt = (Attr2) nodes.getNamedItemWithoutSytheticClassAttr(name);
        if (replacedAtt != null) {
            replacedAtt.detachFromParent();
        }

        final DomAttr newDomAttr = newAtt.getDomNodeOrDie();
        getDomNodeOrDie().setAttributeNode(newDomAttr);
        return replacedAtt;
    }

    /**
     * Returns the attributes of this XML element.
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/Node.attributes">Gecko DOM Reference</a>
     * @return the attributes of this XML element
     */
    @Override
    @Getter
    public NamedNodeMap2 getAttributes() {
        if (attributes_ == null) {
            attributes_ = createAttributesObject();
        }
        return attributes_;
    }

    /**
     * Creates the JS object for the property attributes. This object will the be cached.
     * @return the JS object
     */
    protected NamedNodeMap2 createAttributesObject() {
        return new NamedNodeMap2(getDomNodeOrDie());
    }

    /**
     * Sets the specified attribute.
     * @param namespaceURI the namespace URI
     * @param qualifiedName the qualified name of the attribute to look for
     * @param value the new attribute value
     */
    @Function
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value) {
        getDomNodeOrDie().setAttributeNS(namespaceURI, qualifiedName, value);
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name
     */
    @Getter
    public String getTagName() {
        return getNodeName();
    }

    /**
     * Returns the {@code clientLeft} attribute.
     * @return the {@code clientLeft} attribute
     */
    @Getter
    public int getClientLeft() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getBorderLeftValue();
    }

    /**
     * Returns {@code clientTop} attribute.
     * @return the {@code clientTop} attribute
     */
    @Getter
    public int getClientTop() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getBorderTopValue();
    }

    /**
     * Returns the {@code clientHeight} attribute.
     * @return the {@code clientHeight} attribute
     */
    @Getter
    public int getClientHeight() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getCalculatedHeight(false, true);
    }

    /**
     * Returns the {@code clientWidth} attribute.
     * @return the {@code clientWidth} attribute
     */
    @Getter
    public int getClientWidth() {
        final Global global = NashornJavaScriptEngine.getGlobal(getWindow().getWebWindow());
        final ComputedCSSStyleDeclaration2 style = Window2.getComputedStyle(global, this, null);
        return style.getCalculatedWidth(false, true);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Element2.class,
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
            super("Element",
                    staticHandle("constructor", Element2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("Element");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("Element");
        }
    }

}
