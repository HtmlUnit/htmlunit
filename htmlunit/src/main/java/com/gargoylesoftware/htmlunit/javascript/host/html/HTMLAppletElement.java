/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.applet.Applet;
import java.lang.reflect.Method;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * The JavaScript object {@code HTMLAppletElement}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
@JsxClass(domClass = HtmlApplet.class, browsers = { @WebBrowser(FF), @WebBrowser(IE), @WebBrowser(EDGE) })
public class HTMLAppletElement extends HTMLElement {

    /**
     * The constructor.
     */
    @JsxConstructor({ @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLAppletElement() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        if (domNode.getPage().getWebClient().getOptions().isAppletEnabled()) {
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
                    for (int i = 0; i < realArgs.length; i++) {
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
     * Returns the value of the {@code alt} property.
     * @return the value of the {@code alt} property
     */
    @JsxGetter
    public String getAlt() {
        final String alt = getDomNodeOrDie().getAttribute("alt");
        return alt;
    }

    /**
     * Returns the value of the {@code alt} property.
     * @param alt the value
     */
    @JsxSetter
    public void setAlt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the {@code align} property.
     * @return the value of the {@code align} property
     */
    @JsxGetter
    public String getAlign() {
        return getAlign(true);
    }

    /**
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     */
    @JsxSetter
    public void setAlign(final String align) {
        setAlign(align, false);
    }

    /**
     * Gets the {@code classid} attribute.
     * @return the {@code classid} attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getClassid() {
        return getDomNodeOrDie().getAttribute("classid");
    }

    /**
     * Sets the {@code classid} attribute.
     * @param classid the {@code classid} attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setClassid(final String classid) {
        getDomNodeOrDie().setAttribute("classid", classid);
        // see HTMLObjectElement.setClassid
    }

    /**
     * Returns the value of the {@code width} property.
     * @return the value of the {@code width} property
     */
    @JsxGetter(propertyName = "width")
    public String getWidth_js() {
        return getWidthOrHeight("width", Boolean.TRUE);
    }

    /**
     * Sets the value of the {@code width} property.
     * @param width the value of the {@code width} property
     */
    @JsxSetter
    public void setWidth(final String width) {
        setWidthOrHeight("width", width, true);
    }

    /**
     * Returns the value of the {@code height} property.
     * @return the value of the {@code height} property
     */
    @JsxGetter(propertyName = "height")
    public String getHeight_js() {
        return getWidthOrHeight("height", Boolean.TRUE);
    }

    /**
     * Sets the value of the {@code height} property.
     * @param height the value of the {@code height} property
     */
    @JsxSetter
    public void setHeight(final String height) {
        setWidthOrHeight("height", height, true);
    }

}
