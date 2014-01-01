/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for a ProcessingInstruction.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(domClass = DomProcessingInstruction.class)
public final class ProcessingInstruction extends Node {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public ProcessingInstruction() {
    }

    /**
     * Returns the "target" attribute.
     * @return the target
     */
    @JsxGetter
    public String getTarget() {
        return ((DomProcessingInstruction) getDomNodeOrDie()).getTarget();
    }

    /**
     * Returns the "data" attribute.
     * @return the data
     */
    @JsxGetter
    public String getData() {
        return ((DomProcessingInstruction) getDomNodeOrDie()).getData();
    }

    /**
     * Sets the "data" attribute.
     * @param data the data
     */
    @JsxSetter
    public void setData(final String data) {
        ((DomProcessingInstruction) getDomNodeOrDie()).setData(data);
    }

}
