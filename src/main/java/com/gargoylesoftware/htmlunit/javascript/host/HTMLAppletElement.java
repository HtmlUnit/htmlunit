/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.applet.Applet;

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.HtmlApplet;

/**
 * The JavaScript object "HTMLAppletElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HTMLAppletElement extends HTMLElement {

    private static final long serialVersionUID = 1869359649341296910L;
    private Scriptable appletJSObject_;

    /**
     * Create an instance.
     */
    public HTMLAppletElement() {
        // Empty.
    }

    /**
     * Gets the JavaScript object for the applet.
     * @return the javascript object
     * @throws Exception in case of problem creating the applet or its JS wrapper
     */
    public Object getAppletObject() throws Exception {
        if (appletJSObject_ == null) {
            final HtmlApplet appletNode = (HtmlApplet) getDomNodeOrDie();
            final Applet applet = appletNode.getApplet();

            appletJSObject_ = new NativeJavaObject(getWindow(), applet, applet.getClass());
        }
        return appletJSObject_;
    }
}
