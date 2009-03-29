/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
 * @author Daniel Gredler
 */
public class HTMLAppletElement extends HTMLElement {

    private static final long serialVersionUID = 1869359649341296910L;
    private Scriptable appletJSObject_;

    /**
     * Creates an instance.
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

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    public String jsxGet_alt() {
        String alt = getDomNodeOrDie().getAttribute("alt");
        if (alt == NOT_FOUND) {
            alt = "";
        }
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    public void jsxSet_alt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    public String jsxGet_border() {
        String border = getDomNodeOrDie().getAttribute("border");
        if (border == NOT_FOUND) {
            border = "";
        }
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    public String jsxGet_align() {
        return getAlign();
    }

    /**
     * Returns the value of the "align" property.
     * @param align the value of the "align" property
     */
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }

}
