/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_OBJECT_CLASSID;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.applet.Applet;
import java.lang.reflect.Method;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObjectImpl;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeJavaObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Wrapper;

/**
 * The JavaScript object {@code HTMLObjectElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlObject.class)
public class HTMLObjectElement extends HTMLElement implements Wrapper {

    private Scriptable wrappedActiveX_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLObjectElement() {
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
        final HtmlObject appletNode = (HtmlObject) getDomNodeOrDie();
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
    @JsxGetter(IE)
    public String getAlt() {
        return getDomNodeOrDie().getAttributeDirect("alt");
    }

    /**
     * Returns the value of the {@code alt} property.
     * @param alt the value
     */
    @JsxSetter(IE)
    public void setAlt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter
    public String getBorder() {
        return getDomNodeOrDie().getAttributeDirect("border");
    }

    /**
     * Sets the {@code border} attribute.
     * @param border the {@code border} attribute
     */
    @JsxSetter
    public void setBorder(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Gets the {@code classid} attribute.
     * @return the {@code classid} attribute
     */
    @JsxGetter(IE)
    public String getClassid() {
        return getDomNodeOrDie().getAttributeDirect("classid");
    }

    /**
     * Sets the {@code classid} attribute.
     * @param classid the {@code classid} attribute
     */
    @JsxSetter(IE)
    public void setClassid(final String classid) {
        getDomNodeOrDie().setAttribute("classid", classid);
        if (classid.indexOf(':') != -1 && getBrowserVersion().hasFeature(HTML_OBJECT_CLASSID)) {
            final WebClient webClient = getWindow().getWebWindow().getWebClient();
            final Map<String, String> map = webClient.getActiveXObjectMap();
            if (map != null) {
                final String xClassString = map.get(classid);
                if (xClassString != null) {
                    try {
                        final Class<?> xClass = Class.forName(xClassString);
                        final Object object = xClass.newInstance();
                        boolean contextCreated = false;
                        if (Context.getCurrentContext() == null) {
                            new HtmlUnitContextFactory(webClient).enterContext();
                            contextCreated = true;
                        }
                        wrappedActiveX_ = Context.toObject(object, getParentScope());
                        if (contextCreated) {
                            Context.exit();
                        }
                    }
                    catch (final Exception e) {
                        throw Context.reportRuntimeError("ActiveXObject Error: failed instantiating class "
                                + xClassString + " because " + e.getMessage() + ".");
                    }
                    return;
                }
            }
            if (webClient.getOptions().isActiveXNative()
                    && System.getProperty("os.name").contains("Windows")) {
                try {
                    wrappedActiveX_ = new ActiveXObjectImpl(classid);
                    wrappedActiveX_.setParentScope(getParentScope());
                }
                catch (final Exception e) {
                    Context.throwAsScriptRuntimeEx(e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        // for java mocks do a bit more, we have handle unknown properties
        // ourself
        if (wrappedActiveX_ instanceof NativeJavaObject) {
            final NativeJavaObject obj = (NativeJavaObject) wrappedActiveX_;
            final Object result = obj.get(name, start);
            if (Scriptable.NOT_FOUND != result) {
                return result;
            }
            return super.get(name, start);
        }

        if (wrappedActiveX_ != null) {
            return wrappedActiveX_.get(name, start);
        }
        return super.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        // for java mocks do a bit more, we have handle unknown properties
        // ourself
        if (wrappedActiveX_ instanceof NativeJavaObject) {
            if (wrappedActiveX_.has(name, start)) {
                wrappedActiveX_.put(name, start, value);
            }
            else {
                super.put(name, start, value);
            }
            return;
        }

        if (wrappedActiveX_ != null) {
            wrappedActiveX_.put(name, start, value);
            return;
        }

        super.put(name, start, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unwrap() {
        if (wrappedActiveX_ instanceof Wrapper) {
            return ((Wrapper) wrappedActiveX_).unwrap();
        }
        return wrappedActiveX_;
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
     * Returns the {@code name} attribute.
     * @return the {@code name} attribute
     */
    @JsxGetter
    @Override
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect("name");
    }

    /**
     * Sets the {@code name} attribute.
     * @param name the {@code name} attribute
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

    /**
     * Returns the value of the JavaScript {@code form} attribute.
     *
     * @return the value of the JavaScript {@code form} attribute
     */
    @JsxGetter
    @Override
    public HTMLFormElement getForm() {
        final HtmlForm form = getDomNodeOrDie().getEnclosingForm();
        if (form == null) {
            return null;
        }
        return (HTMLFormElement) getScriptableFor(form);
    }

    /**
     * Checks whether the element has any constraints and whether it satisfies them.
     * @return if the element is valid
     */
    @JsxFunction
    public boolean checkValidity() {
        return getDomNodeOrDie().isValid();
    }

}
