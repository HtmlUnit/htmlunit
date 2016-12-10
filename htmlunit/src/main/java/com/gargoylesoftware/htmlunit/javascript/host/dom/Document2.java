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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlKeygen;
import com.gargoylesoftware.htmlunit.html.HtmlRp;
import com.gargoylesoftware.htmlunit.html.HtmlRt;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.host.Location2;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.WebBrowser;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;

@ScriptClass
public class Document2 extends Node2 {

    private static final Log LOG = LogFactory.getLog(Document.class);
    private static final Pattern TAG_NAME_PATTERN = Pattern.compile("\\w+");

    private Window2 window_;
    private DOMImplementation implementation_;
    private String designMode_;

    public static Document2 constructor(final boolean newObj, final Object self) {
        final Document2 host = new Document2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Sets the Window JavaScript object that encloses this document.
     * @param window the Window JavaScript object that encloses this document
     */
    public void setWindow(final Window2 window) {
        window_ = window;
    }

    @Override
    public Window2 getWindow() {
        return window_;
    }

    /**
     * Returns the page that this document is modeling.
     * @return the page that this document is modeling
     */
    public SgmlPage getPage() {
        return (SgmlPage) getDomNodeOrDie();
    }

    /**
     * Creates a new element with the given tag name.
     *
     * @param tagName the tag name
     * @return the new HTML element, or {@code undefined} if the tag is not supported
     */
    @Function
    public Object createElement(String tagName) {
        Object result = ScriptRuntime.UNDEFINED;
        try {
            final BrowserVersion browserVersion = getBrowserVersion();

            // FF3.6 supports document.createElement('div') or supports document.createElement('<div>')
            // but not document.createElement('<div name="test">')
            // IE9- supports also document.createElement('<div name="test">')
            // FF4+ and IE11 don't support document.createElement('<div>')
            if (browserVersion.hasFeature(BrowserVersionFeatures.JS_DOCUMENT_CREATE_ELEMENT_STRICT)
                  && (tagName.contains("<") || tagName.contains(">"))) {
                LOG.info("createElement: Provided string '"
                            + tagName + "' contains an invalid character; '<' and '>' are not allowed");
                throw new RuntimeException("String contains an invalid character");
            }
            else if (tagName.startsWith("<") && tagName.endsWith(">")) {
                tagName = tagName.substring(1, tagName.length() - 1);

                final Matcher matcher = TAG_NAME_PATTERN.matcher(tagName);
                if (!matcher.matches()) {
                    LOG.info("createElement: Provided string '" + tagName + "' contains an invalid character");
                    throw new RuntimeException("String contains an invalid character");
                }
            }

            final SgmlPage page = getPage();
            final org.w3c.dom.Node element = page.createElement(tagName);

            if (element instanceof BaseFrameElement) {
                ((BaseFrameElement) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlInput) {
                ((HtmlInput) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlImage) {
                ((HtmlImage) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlKeygen) {
                ((HtmlKeygen) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlRp) {
                ((HtmlRp) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlRt) {
                ((HtmlRt) element).markAsCreatedByJavascript();
            }
            else if (element instanceof HtmlUnknownElement) {
                ((HtmlUnknownElement) element).markAsCreatedByJavascript();
            }
            final Object jsElement = getScriptableFor(element);

            if (jsElement == ScriptRuntime.UNDEFINED) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("createElement(" + tagName
                        + ") cannot return a result as there isn't a JavaScript object for the element "
                        + element.getClass().getName());
                }
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to undefined
        }
        return result;
    }

    /**
     * Returns the value of the {@code location} property.
     * @return the value of the {@code location} property
     */
    @Getter
    public Location2 getLocation() {
        return Window2.getLocation(window_);
    }

    /**
     * Sets the {@code location} property. This will cause a reload of the window.
     * @param newLocation the URL of the new content
     * @throws IOException when location loading fails
     */
    @Setter
    public void setLocation(final String newLocation) throws IOException {
        Window2.getLocation(window_).setHref(newLocation);
    }

    /**
     * Creates a new HTML attribute with the specified name.
     *
     * @param attributeName the name of the attribute to create
     * @return an attribute with the specified name
     */
    @Function
    public Attr2 createAttribute(final String attributeName) {
        return (Attr2) getPage().createAttribute(attributeName).getScriptObject2();
    }

    /**
     * Returns all the descendant elements with the specified tag name.
     * @param tagName the name to search for
     * @return all the descendant elements with the specified tag name
     */
    @Function
    public HTMLCollection2 getElementsByTagName(final String tagName) {
        final HTMLCollection2 collection;
        if ("*".equals(tagName)) {
            collection = new HTMLCollection2(getDomNodeOrDie(), false) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        else {
            collection = new HTMLCollection2(getDomNodeOrDie(), false) {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return tagName.equalsIgnoreCase(node.getNodeName());
                }
            };
        }

        return collection;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Document2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @ClassConstructor({@WebBrowser(CHROME), @WebBrowser(FF)})
    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("Document", 
                    staticHandle("constructor", Document2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("Document");
        }
    }

    @ClassConstructor(@WebBrowser(IE))
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        public ObjectConstructor() {
            super("Document");
        }
    }
}
