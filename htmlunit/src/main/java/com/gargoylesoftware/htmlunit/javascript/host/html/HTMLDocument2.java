/*
 * Copyright (c) 2015 Gargoyle Software Inc.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (http://www.gnu.org/licenses/).
 */
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_GET_ELEMENTS_BY_NAME_EMPTY_RETURNS_NOTHING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_GET_ELEMENT_BY_ID_ALSO_BY_NAME_IN_QUICKS_MODE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_GET_ELEMENT_BY_ID_CASE_SENSITIVE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAttributeChangeEvent;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ECMAErrors;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

import net.sourceforge.htmlunit.corejs.javascript.UniqueTag;

public class HTMLDocument2 extends Document2 {

    private static final Log LOG = LogFactory.getLog(HTMLDocument2.class);

    /** The buffer that will be used for calls to document.write(). */
    private final StringBuilder writeBuffer_ = new StringBuilder();
    private boolean writeInCurrentDocument_ = true;
    private String domain_;
    private String uniqueID_;
    private String lastModified_;
    private String compatMode_;
    private int documentMode_ = -1;

    private boolean closePostponedAction_;

    public static HTMLDocument2 constructor(final boolean newObj, final Object self) {
        final HTMLDocument2 host = new HTMLDocument2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    /**
     * Returns the HTML page that this document is modeling.
     * @return the HTML page that this document is modeling
     */
    public HtmlPage getPage() {
        return (HtmlPage) getDomNodeOrDie();
    }

    /**
     * Returns the element with the specified ID, or {@code null} if that element could not be found.
     * @param id the ID to search for
     * @return the element, or {@code null} if it could not be found
     */
    @Function
    public Object getElementById(final String id) {
        implicitCloseIfNecessary();
        Object result = null;
        try {
            final boolean caseSensitive = getBrowserVersion().hasFeature(JS_GET_ELEMENT_BY_ID_CASE_SENSITIVE);
            final DomElement domElement = getPage().getElementById(id, caseSensitive);
            final Object jsElement = getScriptableFor(domElement);
            if (jsElement == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getElementById(" + id
                            + ") cannot return a result as there isn't a JavaScript object for the HTML element "
                            + domElement.getClass().getName());
                }
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to null
            final BrowserVersion browser = getBrowserVersion();
            if (browser.hasFeature(JS_GET_ELEMENT_BY_ID_ALSO_BY_NAME_IN_QUICKS_MODE)
                    && getPage().isQuirksMode()) {
                final HTMLCollection elements = getElementsByName(id);
                result = elements.get(0, elements);
                if (result instanceof UniqueTag) {
                    return null;
                }
                LOG.warn("getElementById(" + id + ") did a getElementByName for Internet Explorer");
                return result;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("getElementById(" + id + "): no DOM node found with this id");
            }
        }
        return result;
    }

    /**
     * Returns all HTML elements that have a "name" attribute with the specified value.
     *
     * Refer to <a href="http://www.w3.org/TR/DOM-Level-2-HTML/html.html#ID-71555259">
     * The DOM spec</a> for details.
     *
     * @param elementName - value of the {@code name} attribute to look for
     * @return all HTML elements that have a "name" attribute with the specified value
     */
    @JsxFunction
    public HTMLCollection getElementsByName(final String elementName) {
        implicitCloseIfNecessary();
        if (getBrowserVersion().hasFeature(JS_GET_ELEMENTS_BY_NAME_EMPTY_RETURNS_NOTHING)
                && StringUtils.isEmpty(elementName)
                || "null".equals(elementName)) {
//            return HTMLCollection.emptyCollection(getWindow());
        }
        // Null must me changed to '' for proper collection initialization.
        final String expElementName = "null".equals(elementName) ? "" : elementName;

        final HtmlPage page = getPage();
        final String description = "HTMLDocument.getElementsByName('" + elementName + "')";
        final HTMLCollection collection = new HTMLCollection(page, true, description) {
            @Override
            protected List<Object> computeElements() {
                return new ArrayList<Object>(page.getElementsByName(expElementName));
            }

            @Override
            protected EffectOnCache getEffectOnCache(final HtmlAttributeChangeEvent event) {
                if ("name".equals(event.getName())) {
                    return EffectOnCache.RESET;
                }
                return EffectOnCache.NONE;
            }
        };

        return collection;
    }

    /**
     * Closes the document implicitly, i.e. flushes the <tt>document.write</tt> buffer (IE only).
     */
    private void implicitCloseIfNecessary() {
        if (!writeInCurrentDocument_) {
            try {
                close();
            }
            catch (final IOException e) {
                throw ECMAErrors.referenceError(e, "error");
            }
        }
    }

    /**
     * JavaScript function "close".
     *
     * See http://www.whatwg.org/specs/web-apps/current-work/multipage/section-dynamic.html for
     * a good description of the semantics of open(), write(), writeln() and close().
     *
     * @throws IOException if an IO problem occurs
     */
    @JsxFunction
    public void close() throws IOException {
        if (writeInCurrentDocument_) {
            LOG.warn("close() called when document is not open.");
        }
        else {
            final HtmlPage page = getPage();
            final URL url = page.getUrl();
            final StringWebResponse webResponse = new StringWebResponse(writeBuffer_.toString(), url);
            webResponse.setFromJavascript(true);
            writeInCurrentDocument_ = true;
            writeBuffer_.setLength(0);

            final WebClient webClient = page.getWebClient();
            final WebWindow window = page.getEnclosingWindow();
            webClient.loadWebResponseInto(webResponse, window);
        }
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLDocument2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("HTMLDocument", 
                    staticHandle("constructor", HTMLDocument2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {
        public ScriptFunction getElementById;
        public ScriptFunction getElementsByName;

        public ScriptFunction G$getElementById() {
            return getElementById;
        }

        public void S$getElementById(final ScriptFunction function) {
            this.getElementById = function;
        }

        public ScriptFunction G$getElementsByName() {
            return getElementById;
        }

        public void S$getElementsByName(final ScriptFunction function) {
            this.getElementById = function;
        }

        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "HTMLDocument";
        }
    }
}
