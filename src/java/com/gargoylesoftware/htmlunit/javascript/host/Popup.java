/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.Collections;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for IE's Popu.
 * 
 * @author Marc Guillemot
 * @author David K. Taylor
 * @version $Revision: 1223 $
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/popup.asp">MSDN documentation</a>
 */
public class Popup extends SimpleScriptable {

    private static final long serialVersionUID = 2016351591254223906L;
    private boolean opened_;
    private Document document_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public Popup() {
        opened_ = false;
    }
    
    void init(final Window openerJSWindow) {
        // build document
        
        document_ = new Document();
        document_.setPrototype(openerJSWindow.getPrototype(Document.class));
        document_.setParentScope(this);

        final WebWindow openerWindow = openerJSWindow.getWebWindow();
        // create the "page" associated to the document
        final WebWindow popupPseudoWindow = new PopupPseudoWebWindow(openerWindow.getWebClient());
        // take the WebResponse of the opener (not really correct, but...)
        final WebResponse webResponse = openerWindow.getEnclosedPage().getWebResponse();
        final HtmlPage popupPage = new HtmlPage(null, webResponse, popupPseudoWindow);
        popupPseudoWindow.setEnclosedPage(popupPage);
        final HtmlHtml html = new HtmlHtml(popupPage, Collections.EMPTY_MAP);
        popupPage.appendChild(html);
        final HtmlBody body = new HtmlBody(popupPage, Collections.EMPTY_MAP);
        html.appendChild(body);

        document_.setDomNode(popupPage);
    }

    /**
     * Returns the HTML document element in the popup.
     * @return HTML document.
     */
    public Object jsxGet_document() {
        return document_;
    }


    /**
     * Indicates if the popup is opened.
     * @return <code>true</code> if opened
     */
    public boolean jsxGet_isOpen() {
        return opened_;
    }

    /**
     * Hides the popup.
     */
    public void jsxFunction_hide() {
        opened_ = false;
    }

    /**
     * Shows the popup.
     */
    public void jsxFunction_show() {
        opened_ = true;
    }
}

/**
 * Simple implementation of {@link WebWindow} to allow the construction of the {@link HtmlPage} associated
 * with a {@link Popup}
 */
class PopupPseudoWebWindow implements WebWindow {
    private final WebClient webClient_;
    private Object scriptObject_;
    private Page enclosedPage_;
    
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
     * @see com.gargoylesoftware.htmlunit.WebWindow#getThreadManager()
     */
    public ThreadManager getThreadManager() {
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
     * @see com.gargoylesoftware.htmlunit.WebWindow#setEnclosedPage(com.gargoylesoftware.htmlunit.Page)
     */
    public void setEnclosedPage(final Page page) {
        enclosedPage_ = page;
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

}
