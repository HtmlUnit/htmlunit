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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxStaticFunction;
import org.htmlunit.javascript.configuration.JsxStaticGetter;
import org.htmlunit.javascript.host.event.EventTarget;

/**
 * A Notification.
 *
 * @see <a href="https://developer.mozilla.org/en/docs/Web/API/notification">
 *     MDN - Notification</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@JsxClass
public class Notification extends EventTarget {

    /**
     * The maximum number of actions supported.
     */
    @JsxConstant({CHROME, EDGE})
    public static final int maxActions = 2;

    /**
     * JavaScript constructor.
     * @param title the title
     */
    @JsxConstructor
    public void jsConstructor(final String title) {
        // Empty.
    }

    /**
     * Returns the {@code permission} static property.
     * @param thisObj the scriptable
     * @return the {@code permission} static property
     */
    @JsxStaticGetter
    public static String getPermission(final Scriptable thisObj) {
        return "default";
    }

    /**
     * Asks the user for permission.
     */
    @JsxStaticFunction
    public static void requestPermission() {
        // TODO
    }
}
