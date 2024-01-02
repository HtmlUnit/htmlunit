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
package org.htmlunit.javascript.host.event;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.DomNode;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for {@code SubmitEvent}.
 *
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF_ESR})
public class SubmitEvent extends Event {

    private HTMLElement submitter_;

    /**
     * Default constructor.
     */
    public SubmitEvent() {
    }

    /**
     * Ctor.
     * @param domNode the DOM node that triggered the event
     * @param submitElement the element that has triggerd this event
     *
     */
    public SubmitEvent(final DomNode domNode, final HTMLElement submitElement) {
        super(domNode, Event.TYPE_SUBMIT);
        submitter_ = submitElement;
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            final Object submitter = details.get("submitter");
            if (submitter instanceof HTMLElement) {
                submitter_ = (HTMLElement) submitter;
            }
        }
    }

    /**
     * @return the submitter
     */
    @JsxGetter
    public HTMLElement getSubmitter() {
        return submitter_;
    }
}
