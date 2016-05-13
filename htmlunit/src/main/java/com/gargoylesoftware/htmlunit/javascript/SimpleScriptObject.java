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
package com.gargoylesoftware.htmlunit.javascript;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;
import com.gargoylesoftware.htmlunit.html.HtmlHead;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Text2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameSetElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement2;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSVGElement2;
import com.gargoylesoftware.htmlunit.svg.SvgSvg;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

/**
 * Base class for Nashorn host objects in HtmlUnit.
 *
 * @author Ahmed Ashour
 */
public class SimpleScriptObject extends ScriptObject {

    private static final Log LOG = LogFactory.getLog(SimpleScriptObject.class);

    private DomNode domNode_;
    private boolean caseSensitive_ = true;

    private String className_;

    /**
     * Returns the JavaScript class name.
     * @return the JavaScript class name
     */
    @Override
    public String getClassName() {
        if (className_ != null) {
            return className_;
        }
        if (getProto() != null) {
            return getProto().getClassName();
        }
        String className = getClass().getSimpleName();
        if (className.isEmpty()) {
            // for anonymous class
            className = getClass().getSuperclass().getSimpleName();
        }
        return className;
    }

    /**
     * Gets the window that is the top scope for this object.
     * @return the window associated with this object
     */
    public Window2 getWindow() {
        return ((SimpleScriptObject) getDomNodeOrDie().getPage().getScriptObject2()).getWindow();
    }

    /**
     * Gets the browser version currently used.
     * @return the browser version
     */
    public BrowserVersion getBrowserVersion() {
        final DomNode node = getDomNodeOrNull();
        if (node != null) {
            return node.getPage().getWebClient().getBrowserVersion();
        }
        return getWindow().getWebWindow().getWebClient().getBrowserVersion();
    }

    /**
     * Returns the DOM node that corresponds to this JavaScript object or throw
     * an exception if one cannot be found.
     * @return the DOM node
     * @exception IllegalStateException If the DOM node could not be found.
     */
    public DomNode getDomNodeOrDie() throws IllegalStateException {
        if (domNode_ == null) {
            final String clazz = getClass().getName();
            throw new IllegalStateException("DomNode has not been set for this SimpleScriptable: " + clazz);
        }
        return domNode_;
    }

    /**
     * Returns the DOM node that corresponds to this JavaScript object
     * or null if a node hasn't been set.
     * @return the DOM node or null
     */
    public DomNode getDomNodeOrNull() {
        return domNode_;
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    public void setDomNode(final DomNode domNode) {
        setDomNode(domNode, true);
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     * @param assignScriptObject If true, call <code>setScriptObject</code> on domNode
     */
    protected void setDomNode(final DomNode domNode, final boolean assignScriptObject) {
        WebAssert.notNull("domNode", domNode);
        domNode_ = domNode;
        if (assignScriptObject) {
            domNode_.setScriptableObject(this);
        }
    }

    /**
     * Sets the HTML element that corresponds to this JavaScript object.
     * @param htmlElement the HTML element
     */
    public void setHtmlElement(final HtmlElement htmlElement) {
        setDomNode(htmlElement);
    }

    /**
     * Returns the JavaScript object that corresponds to the specified object.
     * New JavaScript objects will be created as needed. If a JavaScript object
     * cannot be created for a domNode then NOT_FOUND will be returned.
     *
     * @param object a {@link DomNode} or a {@link WebWindow}
     * @return the JavaScript object or NOT_FOUND
     */
    protected SimpleScriptObject getScriptableFor(final Object object) {
        if (object instanceof WebWindow) {
            return (SimpleScriptObject) ((WebWindow) object).getScriptObject2();
        }

        final DomNode domNode = (DomNode) object;

        final Object scriptObject = domNode.getScriptObject2();
        if (scriptObject != null) {
            return (SimpleScriptObject) scriptObject;
        }
        return makeScriptableFor(domNode);
    }

    /**
     * Builds a new the JavaScript object that corresponds to the specified object.
     * @param domNode the DOM node for which a JS object should be created
     * @return the JavaScript object
     */
    public SimpleScriptObject makeScriptableFor(final DomNode domNode) {
        SimpleScriptObject host = null;
        final Global global = NashornJavaScriptEngine.getGlobal(domNode.getPage().getEnclosingWindow().getScriptContext());
        if (domNode instanceof HtmlBody) {
            host = HTMLBodyElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlHtml) {
            host = HTMLHtmlElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlForm) {
            host = HTMLFormElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlTextInput) {
            host = HTMLInputElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlFrame) {
            host = HTMLFrameElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlFrameSet) {
            host = HTMLFrameSetElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlHead) {
            host = HTMLHeadElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlInlineFrame) {
            host = HTMLIFrameElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlDivision) {
            host = HTMLDivElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlTeletype) {
            host = HTMLElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlStyle) {
            host = HTMLStyleElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof SvgSvg) {
            host = SVGSVGElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof DomText) {
            host = Text2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlInput) {
            host = HTMLInputElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else if (domNode instanceof HtmlButton) {
            host = HTMLButtonElement2.constructor(true, global);
            host.setDomNode(domNode);
        }
        else {
            throw new RuntimeException("No ScriptObject found for " + domNode + " " + domNode.getClass().getName());
        }
        return host;
        // Get the JS class name for the specified DOM node.
        // Walk up the inheritance chain if necessary.
//        Class<? extends SimpleScriptObject> javaScriptClass = null;
//        if (domNode instanceof HtmlImage && "image".equals(((HtmlImage) domNode).getOriginalQualifiedName())
//                && ((HtmlImage) domNode).wasCreatedByJavascript()) {
//            if (domNode.hasFeature(HTMLIMAGE_HTMLELEMENT)) {
//                javaScriptClass = HTMLElement.class;
//            }
//            else if (domNode.hasFeature(HTMLIMAGE_HTMLUNKNOWNELEMENT)) {
//                javaScriptClass = HTMLUnknownElement.class;
//            }
//        }
//        if (javaScriptClass == null) {
//            final JavaScriptEngine javaScriptEngine = (JavaScriptEngine)
//                    getWindow().getWebWindow().getWebClient().getAbstractJavaScriptEngine();
//            for (Class<?> c = domNode.getClass(); javaScriptClass == null && c != null; c = c.getSuperclass()) {
//                javaScriptClass = javaScriptEngine.getJavaScriptClass(c);
//            }
//        }

//        final SimpleScriptObject scriptable;
//        if (javaScriptClass == null) {
//            // We don't have a specific subclass for this element so create something generic.
//            scriptable = new HTMLElement();
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("No JavaScript class found for element <" + domNode.getNodeName() + ">. Using HTMLElement");
//            }
//        }
//        else {
//            try {
//                scriptable = javaScriptClass.newInstance();
//            }
//            catch (final Exception e) {
//                throw Context.throwAsScriptRuntimeEx(e);
//            }
//        }
//        initParentScope(domNode, scriptable);
//
//        scriptable.setPrototype(getPrototype(javaScriptClass));
//        scriptable.setDomNode(domNode);

//        return scriptable;
    }

//    /**
//     * Gets the window that is the top scope for this object.
//     * @return the window associated with this object
//     * @throws RuntimeException if the window cannot be found, which should never occur
//     */
//    public Window getWindow() throws RuntimeException {
//        return getWindow(this);
//    }
//
//    /**
//     * Gets the window that is the top scope for the specified object.
//     * @param s the JavaScript object whose associated window is to be returned
//     * @return the window associated with the specified JavaScript object
//     * @throws RuntimeException if the window cannot be found, which should never occur
//     */
//    protected static Window getWindow(final Scriptable s) throws RuntimeException {
//        final Scriptable top = ScriptableObject.getTopLevelScope(s);
//        if (top instanceof Window) {
//            return (Window) top;
//        }
//        throw new RuntimeException("Unable to find window associated with " + s);
//    }
//

//    /**
//     * Gets the browser version currently used.
//     * @return the browser version
//     */
//    public BrowserVersion getBrowserVersion() {
//        final DomNode node = getDomNodeOrNull();
//        if (node != null) {
//            return node.getPage().getWebClient().getBrowserVersion();
//        }
//        return getWindow().getWebWindow().getWebClient().getBrowserVersion();
//    }

}
