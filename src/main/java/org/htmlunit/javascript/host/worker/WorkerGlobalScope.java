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
package org.htmlunit.javascript.host.worker;

import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.WindowOrWorkerGlobalScope;
import org.htmlunit.javascript.host.WindowOrWorkerGlobalScopeMixin;
import org.htmlunit.javascript.host.event.EventTarget;

/**
 * The scope for the execution of {@link Worker}s.
 *
 * @author Ronald Brill
 */
@JsxClass
public class WorkerGlobalScope extends EventTarget implements WindowOrWorkerGlobalScope {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    @JsxFunction
    @Override
    public String btoa(final String stringToEncode) {
        return WindowOrWorkerGlobalScopeMixin.btoa(stringToEncode, this);
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding.
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @JsxFunction
    @Override
    public String atob(final String encodedData) {
        return WindowOrWorkerGlobalScopeMixin.atob(encodedData, this);
    }
}
