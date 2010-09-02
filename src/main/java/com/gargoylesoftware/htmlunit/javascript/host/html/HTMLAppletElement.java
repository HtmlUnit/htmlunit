/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.applet.Applet;
import java.lang.reflect.Method;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.html.DomNode;
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

    /**
     * Creates an instance.
     */
    public HTMLAppletElement() {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        if (domNode.getPage().getWebClient().isAppletEnabled()) {
            try {
                createAppletMethodAndProperties();
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createAppletMethodAndProperties() throws Exception {
        final HtmlApplet appletNode = (HtmlApplet) getDomNodeOrDie();
        final Applet applet = appletNode.getApplet();
        if (applet == null) {
            return;
        }

        // Rhino should provide the possibility to declare delegate for Functions as it does for properties!!!
        for (final Method method : applet.getClass().getMethods()) {
            final Function f = new BaseFunction() {
                @Override
                public Object call(final Context cx, final Scriptable scope,
                        final Scriptable thisObj, final Object[] args) {

                    final Object[] realArgs = new Object[method.getParameterTypes().length];
                    for (int i = 0; i < realArgs.length; ++i) {
                        final Object arg;
                        if (i > args.length) {
                            arg = null;
                        }
                        else {
                            arg = Context.jsToJava(args[i], method.getParameterTypes()[i]);
                        }
                        realArgs[i] = arg;
                    }
                    try {
                        return method.invoke(applet, realArgs);
                    }
                    catch (final Exception e) {
                        throw Context.throwAsScriptRuntimeEx(e);
                    }
                }
            };
            ScriptableObject.defineProperty(this, method.getName(), f, ScriptableObject.READONLY);
        }
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
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }
}
