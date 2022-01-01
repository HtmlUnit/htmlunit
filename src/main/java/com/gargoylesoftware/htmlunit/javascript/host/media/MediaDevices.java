/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.media;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.LambdaConstructor;
import net.sourceforge.htmlunit.corejs.javascript.LambdaFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A JavaScript object for {@code MediaDevices}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF_ESR})
public class MediaDevices extends EventTarget {

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public MediaDevices() {
    }

    @JsxFunction
    public Object getUserMedia() {
        final Scriptable scope = ScriptableObject.getTopLevelScope(this);
        final LambdaConstructor ctor = (LambdaConstructor) getProperty(scope, "Promise");
        final LambdaFunction reject = (LambdaFunction) getProperty(ctor, "reject");
        return reject.call(Context.getCurrentContext(), this, ctor,
                new Object[] {new DOMException("HtmlUnit does not support media streaming.",
                        DOMException.NOT_FOUND_ERR)});
    }
}
