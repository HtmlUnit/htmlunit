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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.html.HtmlAudio;
import com.gargoylesoftware.htmlunit.html.HtmlMedia;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;

/**
 * The JavaScript object {@code HTMLAudioElement}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlAudio.class)
public class HTMLAudioElement extends HTMLMediaElement {

    /**
     * The constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLAudioElement() {
    }

    /**
     * Gets the JavaScript property {@code nodeType} for the current node.
     * @return the node type
     */
    @JsxGetter
    @Override
    public short getNodeType() {
        return Node.ELEMENT_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getNodeName() {
        final HtmlMedia element = (HtmlMedia) getDomNodeOrNull();
        if (element == null) {
            return "AUDIO";
        }
        return element.getNodeName();
    }
}
