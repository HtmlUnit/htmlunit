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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_OBJECT_CLASSID;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObjectImpl;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeJavaObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Wrapper;

/**
 * The JavaScript object {@code HTMLObjectElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlObject.class)
public class HTMLObjectElement extends FormChild implements Wrapper {

    private Scriptable wrappedActiveX_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLObjectElement() {
    }

    /**
     * Returns the value of the {@code alt} property.
     * @return the value of the {@code alt} property
     */
    @JsxGetter(@WebBrowser(IE))
    public String getAlt() {
        final String alt = getDomNodeOrDie().getAttribute("alt");
        return alt;
    }

    /**
     * Returns the value of the {@code alt} property.
     * @param alt the value
     */
    @JsxSetter(@WebBrowser(IE))
    public void setAlt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the {@code border} attribute.
     * @return the {@code border} attribute
     */
    @JsxGetter
    public String getBorder() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
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
    @JsxGetter(@WebBrowser(IE))
    public String getClassid() {
        final String classid = getDomNodeOrDie().getAttribute("classid");
        return classid;
    }

    /**
     * Sets the {@code classid} attribute.
     * @param classid the {@code classid} attribute
     */
    @JsxSetter(@WebBrowser(IE))
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
    public String getName() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the {@code name} attribute.
     * @param name the {@code name} attribute
     */
    @JsxSetter
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

}
