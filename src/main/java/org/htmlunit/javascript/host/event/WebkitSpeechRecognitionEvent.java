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

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code webkitSpeechRecognitionEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(value = {CHROME, EDGE}, className = "webkitSpeechRecognitionEvent")
public class WebkitSpeechRecognitionEvent extends Event {

    /**
     * Creates an instance.
     */
    public WebkitSpeechRecognitionEvent() {
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor(functionName = "SpeechRecognitionEvent")
    public void jsConstructor() {
    }
}
