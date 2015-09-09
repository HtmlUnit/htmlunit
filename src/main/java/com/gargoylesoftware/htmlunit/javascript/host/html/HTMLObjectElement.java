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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_OBJECT_CLASSID;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OBJECT_OBJECT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
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
@JsxClasses({
        @JsxClass(domClass = HtmlObject.class,
                browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) }),
        @JsxClass(domClass = HtmlObject.class,
            isJSObject = false, browsers = @WebBrowser(value = IE, maxVersion = 8))
    })
public class HTMLObjectElement extends FormChild implements Wrapper {

    private Scriptable wrappedActiveX_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
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
    @JsxGetter(@WebBrowser(value = IE, minVersion = 11))
    public String getClassid() {
        final String classid = getDomNodeOrDie().getAttribute("classid");
        return classid;
    }

    /**
     * Sets the {@code classid} attribute.
     * @param classid the {@code classid} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, minVersion = 11))
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
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        if ((String.class.equals(hint) || hint == null) && getBrowserVersion().hasFeature(JS_OBJECT_OBJECT)) {
            final HtmlElement htmlElement = getDomNodeOrNull();
            if (htmlElement != null && !((HtmlPage) htmlElement.getPage()).isQuirksMode()) {
                return "[object]";
            }
        }
        return super.getDefaultValue(hint);
    }

    /**
     * Returns the {@code dataFld} attribute.
     * @return the {@code dataFld} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataFld() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataFld} attribute.
     * @param dataFld {@code dataFld} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataFld(final String dataFld) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code dataFormatAs} attribute.
     * @return the {@code dataFormatAs} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataFormatAs() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataFormatAs} attribute.
     * @param dataFormatAs {@code dataFormatAs} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataFormatAs(final String dataFormatAs) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code dataSrc} attribute.
     * @return the {@code dataSrc} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataSrc() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataSrc} attribute.
     * @param dataSrc {@code dataSrc} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataSrc(final String dataSrc) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
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
