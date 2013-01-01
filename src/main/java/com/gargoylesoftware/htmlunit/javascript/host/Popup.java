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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.History;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;

/**
 * A JavaScript object for IE's Popup.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535882.aspx">MSDN documentation</a>
 */
@JsxClass(browsers = @WebBrowser(IE))
public class Popup extends SimpleScriptable {

    private boolean opened_;
    private HTMLDocument document_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public Popup() {
        opened_ = false;
    }

    void init(final Window openerJSWindow) {
        // build document

        document_ = new HTMLDocument();
        document_.setPrototype(openerJSWindow.getPrototype(HTMLDocument.class));
        document_.setParentScope(this);

        final WebWindow openerWindow = openerJSWindow.getWebWindow();
        // create the "page" associated to the document
        final WebWindow popupPseudoWindow = new PopupPseudoWebWindow(openerWindow.getWebClient());
        // take the WebResponse of the opener (not really correct, but...)
        final WebResponse webResponse = openerWindow.getEnclosedPage().getWebResponse();
        final HtmlPage popupPage = new HtmlPage(null, webResponse, popupPseudoWindow);
        setDomNode(popupPage);
        popupPseudoWindow.setEnclosedPage(popupPage);
        final HtmlHtml html = (HtmlHtml) HTMLParser.getFactory(HtmlHtml.TAG_NAME).createElement(
                popupPage, HtmlHtml.TAG_NAME, null);
        popupPage.appendChild(html);
        final HtmlBody body = (HtmlBody) HTMLParser.getFactory(HtmlBody.TAG_NAME).createElement(
                popupPage, HtmlBody.TAG_NAME, null);
        html.appendChild(body);

        document_.setDomNode(popupPage);
    }

    /**
     * Returns the HTML document element in the popup.
     * @return the HTML document element in the popup
     */
    @JsxGetter
    public Object getDocument() {
        return document_;
    }

    /**
     * Indicates if the popup is opened.
     * @return <code>true</code> if opened
     */
    @JsxGetter
    public boolean getIsOpen() {
        return opened_;
    }

    /**
     * Hides the popup.
     */
    @JsxFunction
    public void hide() {
        opened_ = false;
    }

    /**
     * Shows the popup.
     */
    @JsxFunction
    public void show() {
        opened_ = true;
    }
}

/**
 * Simple implementation of {@link WebWindow} to allow the construction of the {@link HtmlPage} associated
 * with a {@link Popup}.
 */
class PopupPseudoWebWindow implements WebWindow {

    private final WebClient webClient_;
    private Object scriptObject_;
    private Page enclosedPage_;

    private int innerHeight_ = 605;
    private int outerHeight_ = innerHeight_ + 150;
    private int innerWidth_ = 1256;
    private int outerWidth_ = innerWidth_ + 8;

    PopupPseudoWebWindow(final WebClient webClient) {
        webClient_ = webClient;
        webClient_.initialize(this);
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getEnclosedPage()
     */
    public Page getEnclosedPage() {
        return enclosedPage_;
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getName()
     */
    public String getName() {
        throw new RuntimeException("Not supported");
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getParentWindow()
     */
    public WebWindow getParentWindow() {
        throw new RuntimeException("Not supported");
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getScriptObject()
     */
    public Object getScriptObject() {
        return scriptObject_;
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getJobManager()
     */
    public JavaScriptJobManager getJobManager() {
        throw new RuntimeException("Not supported");
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getTopWindow()
     */
    public WebWindow getTopWindow() {
        throw new RuntimeException("Not supported");
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getWebClient()
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#getHistory()
     */
    public History getHistory() {
        throw new RuntimeException("Not supported");
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#setEnclosedPage(com.gargoylesoftware.htmlunit.Page)
     */
    public void setEnclosedPage(final Page page) {
        enclosedPage_ = page;
        webClient_.initialize(page);
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#setName(java.lang.String)
     */
    public void setName(final String name) {
        throw new RuntimeException("Not supported");
    }

    /**
     * @see com.gargoylesoftware.htmlunit.WebWindow#setScriptObject(java.lang.Object)
     */
    public void setScriptObject(final Object scriptObject) {
        scriptObject_ = scriptObject;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isClosed() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInnerWidth() {
        return innerWidth_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInnerWidth(final int innerWidth) {
        innerWidth_ = innerWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOuterWidth() {
        return outerWidth_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOuterWidth(final int outerWidth) {
        outerWidth_ = outerWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInnerHeight() {
        return innerHeight_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInnerHeight(final int innerHeight) {
        innerHeight_ = innerHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOuterHeight() {
        return outerHeight_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOuterHeight(final int outerHeight) {
        outerHeight_ = outerHeight;
    }

}
