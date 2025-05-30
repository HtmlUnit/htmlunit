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

import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.net.MalformedURLException;
import java.net.URL;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.html.ScriptElement;
import org.htmlunit.html.ScriptElementSupport;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.Node;

/**
 * The JavaScript object that represents an {@code HTMLScriptElement}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlScript.class)
public class HTMLScriptElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the {@code src} property.
     * @return the {@code src} property
     */
    @JsxGetter
    public String getSrc() {
        final HtmlScript tmpScript = (HtmlScript) getDomNodeOrDie();
        String src = tmpScript.getSrcAttribute();
        if (ATTRIBUTE_NOT_DEFINED == src) {
            return src;
        }
        try {
            final URL expandedSrc = ((HtmlPage) tmpScript.getPage()).getFullyQualifiedUrl(src);
            src = expandedSrc.toString();
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }
        return src;
    }

    /**
     * Sets the {@code src} property.
     * @param src the {@code src} property
     */
    @JsxSetter
    public void setSrc(final String src) {
        getDomNodeOrDie().setAttribute(DomElement.SRC_ATTRIBUTE, src);
    }

    /**
     * Returns the {@code text} property.
     * @return the {@code text} property
     */
    @JsxGetter
    public String getText() {
        final StringBuilder scriptCode = new StringBuilder();
        for (final DomNode node : getDomNodeOrDie().getChildren()) {
            if (node instanceof DomText) {
                final DomText domText = (DomText) node;
                scriptCode.append(domText.getData());
            }
        }
        return scriptCode.toString();
    }

    /**
     * Sets the {@code text} property.
     * @param text the {@code text} property
     */
    @JsxSetter
    public void setText(final String text) {
        final HtmlElement htmlElement = getDomNodeOrDie();
        htmlElement.removeAllChildren();
        final DomNode textChild = new DomText(htmlElement.getPage(), text);
        htmlElement.appendChild(textChild);

        ScriptElementSupport.executeScriptIfNeeded((ScriptElement) htmlElement, false, false);
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.TYPE_ATTRIBUTE);
    }

    /**
     * Sets the {@code type} property.
     * @param type the {@code type} property
     */
    @JsxSetter
    public void setType(final String type) {
        getDomNodeOrDie().setAttribute(DomElement.TYPE_ATTRIBUTE, type);
    }

    /**
     * Overwritten for special handling.
     *
     * @param childObject the node to add to this node
     * @return the newly added child node
     */
    @Override
    public Node appendChild(final Object childObject) {
        final HtmlElement tmpScript = getDomNodeOrDie();
        final boolean wasEmpty = tmpScript.getFirstChild() == null;
        final Node node = super.appendChild(childObject);

        if (wasEmpty) {
            ScriptElementSupport.executeScriptIfNeeded((ScriptElement) tmpScript, false, false);
        }
        return node;
    }

    /**
     * Returns the {@code async} property.
     * @return the {@code async} property
     */
    @JsxGetter
    public boolean isAsync() {
        return getDomNodeOrDie().hasAttribute("async");
    }

    /**
     * Sets the {@code async} property.
     * @param async the {@code async} property
     */
    @JsxSetter
    public void setAsync(final boolean async) {
        if (async) {
            getDomNodeOrDie().setAttribute("async", "");
        }
        else {
            getDomNodeOrDie().removeAttribute("async");
        }
    }
}
