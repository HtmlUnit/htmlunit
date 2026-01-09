/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.html.DomProcessingInstruction;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code ProcessingInstruction}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(domClass = DomProcessingInstruction.class)
public class ProcessingInstruction extends CharacterData {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the {@code target} attribute.
     * @return the target
     */
    @JsxGetter
    public String getTarget() {
        return ((DomProcessingInstruction) getDomNodeOrDie()).getTarget();
    }

    /**
     * Returns the {@code data} attribute.
     * @return the data
     */
    @Override
    @JsxGetter
    public String getData() {
        return ((DomProcessingInstruction) getDomNodeOrDie()).getData();
    }

    /**
     * Sets the {@code data} attribute.
     * @param data the data
     */
    @Override
    @JsxSetter
    public void setData(final String data) {
        ((DomProcessingInstruction) getDomNodeOrDie()).setData(data);
    }
}
