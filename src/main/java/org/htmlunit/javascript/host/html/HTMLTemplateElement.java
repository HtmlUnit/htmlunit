/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.html.HtmlTemplate;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.dom.DocumentFragment;

/**
 * The JavaScript object {@code HTMLTemplateElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlTemplate.class, value = {CHROME, EDGE, FF, FF_ESR})
public class HTMLTemplateElement extends HTMLElement {

    /**
     * Creates a new instance.
     */
    public HTMLTemplateElement() {
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * @return the value of the {@code content} property
     */
    @JsxGetter
    public DocumentFragment getContent() {
        final DocumentFragment result = new DocumentFragment();
        result.setPrototype(getPrototype(result.getClass()));
        result.setParentScope(getParentScope());
        result.setDomNode(((HtmlTemplate) getDomNodeOrDie()).getContent());

        return result;
    }
}
