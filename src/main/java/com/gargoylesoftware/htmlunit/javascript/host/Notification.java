/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

/**
 * A Notification.
 *
 * @see <a href="https://developer.mozilla.org/en/docs/Web/API/notification">
 * MDN - Notification</a>
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF) })
public class Notification extends EventTarget {

    /**
     * In fact not a constant... but as a constant in a first step.
     */
    @JsxConstant(@WebBrowser(FF))
    public static final String permission = "default";

    /**
     * JavaScript constructor.
     * @param title the title
     */
    @JsxConstructor
    public void jsConstructor(final String title) {
        // Empty.
    }

    /**
     * Asks the user for permission.
     */
    @JsxStaticFunction
    public static void requestPermission() {
        // TODO
    }
}
