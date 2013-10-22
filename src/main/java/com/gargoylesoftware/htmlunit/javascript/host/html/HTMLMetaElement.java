/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object "HTMLMetaElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(domClass = HtmlMeta.class)
public class HTMLMetaElement extends HTMLElement {

    /**
     * Returns "charset" attribute.
     * @return the charset attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getCharset() {
        return "";
    }

    /**
     * Sets the "charset" attribute.
     * @param charset the charset attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setCharset(final String charset) {
        //empty
    }

    /**
     * Returns "content" attribute.
     * @return the content attribute
     */
    @JsxGetter
    public String getContent() {
        return getDomNodeOrDie().getAttribute("content");
    }

    /**
     * Sets the "content" attribute.
     * @param content the content attribute
     */
    @JsxSetter
    public void setContent(final String content) {
        getDomNodeOrDie().setAttribute("content", content);
    }

    /**
     * Returns "http-equiv" attribute.
     * @return the http-equiv attribute
     */
    @JsxGetter
    public String getHttpEquiv() {
        return getDomNodeOrDie().getAttribute("http-equiv");
    }

    /**
     * Sets the "http-equiv" attribute.
     * @param httpEquiv the http-equiv attribute
     */
    @JsxSetter
    public void setHttpEquiv(final String httpEquiv) {
        getDomNodeOrDie().setAttribute("http-equiv", httpEquiv);
    }

    /**
     * Returns "name" attribute.
     * @return the name attribute
     */
    @JsxGetter
    public String getName() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the "name" attribute.
     * @param name the name attribute
     */
    @JsxSetter
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

    /**
     * Returns "scheme" attribute.
     * @return the scheme attribute
     */
    @JsxGetter
    public String getScheme() {
        return getDomNodeOrDie().getAttribute("scheme");
    }

    /**
     * Sets the "scheme" attribute.
     * @param scheme the scheme attribute
     */
    @JsxSetter
    public void setScheme(final String scheme) {
        getDomNodeOrDie().setAttribute("scheme", scheme);
    }

    /**
     * Returns "url" attribute.
     * @return the url attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getUrl() {
        return "";
    }

    /**
     * Sets the "url" attribute.
     * @param url the url attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setUrl(final String url) {
        //empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEndTagForbidden() {
        return true;
    }
}
