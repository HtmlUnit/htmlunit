/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.gargoylesoftware.htmlunit.html.FrameWindow;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * 
 * Base class for common WebWindow functionality. While public, this class is not 
 * exposed in any other places of the API. Internally we can cast to this class
 * when we need access to functionality that is not present in {@link WebWindow}
 * 
 * @version $Revision$
 * @author Brad Clarke
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public abstract class WebWindowImpl implements WebWindow {
    private WebClient webClient_;
    private Page enclosedPage_;
    private Object scriptObject_;
    private ThreadManager threadManager_ = new ThreadManager();
    private List childWindows_ = new ArrayList();
    private String name_ = "";

    /**
     * Never call this, used for Serialization.
     * @deprecated
     */
    protected WebWindowImpl() {
    }

    /**
     * Creates a window and associates it with the client
     * 
     * @param webClient The web client that "owns" this window.
     */
    public WebWindowImpl(final WebClient webClient) {
        Assert.notNull("webClient", webClient);
        webClient_ = webClient;
        performRegistration();
    }

    /**
     * Registers the window with the client.
     */
    protected void performRegistration() {
        webClient_.registerWebWindow(this);
    }

    /**
     * {@inheritDoc}
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     * {@inheritDoc}
     */
    public Page getEnclosedPage() {
        return enclosedPage_;
    }

    /**
     * {@inheritDoc}
     */
    public void setEnclosedPage(final Page page) {
        if (page == enclosedPage_) {
            return;
        }
        destroyChildren();
        enclosedPage_ = page;
        webClient_.initialize(this);
        webClient_.initialize(page);
    }

    /**
     * {@inheritDoc}
     */
    public void setScriptObject(final Object scriptObject) {
        scriptObject_ = scriptObject;
    }

    /**
     * {@inheritDoc}
     */
    public Object getScriptObject() {
        return scriptObject_;
    }

    /**
     * {@inheritDoc}
     */
    public ThreadManager getThreadManager() {
        return threadManager_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * Adds a child to this window, for shutdown purposes.
     * 
     * @param child The child window to associate with this window.
     */
    public void addChildWindow(final FrameWindow child) {
        childWindows_.add(child);
    }

    void destroyChildren() {
        getThreadManager().interruptAll();
        final ListIterator iter = childWindows_.listIterator();
        while (iter.hasNext()) {
            final WebWindowImpl child = (WebWindowImpl) iter.next();
            child.destroyChildren();
            iter.remove();
        }
    }
    
    /**
     * {@inheritDoc}
     */    
    public String getName() {
        return name_;
    }
    
    /**
     * {@inheritDoc}
     */    
    public void setName(final String name) {
        name_ = name;
    }    
}
