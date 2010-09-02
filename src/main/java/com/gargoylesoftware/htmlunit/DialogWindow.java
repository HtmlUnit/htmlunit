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
package com.gargoylesoftware.htmlunit;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A window opened in JavaScript via either <tt>window.showModalDialog</tt> or <tt>window.showModelessDialog</tt>.
 * @version $Revision$
 * @author Daniel Gredler
 */
public class DialogWindow extends WebWindowImpl {

    /** The arguments object exposed via the <tt>dialogArguments</tt> JavaScript property. */
    private Object arguments_;

    /**
     * Creates a new instance.
     * @param webClient the web client that "owns" this window
     * @param arguments the arguments object exposed via the <tt>dialogArguments</tt> JavaScript property
     */
    protected DialogWindow(final WebClient webClient, final Object arguments) {
        super(webClient);
        arguments_ = arguments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded() {
        return getScriptObject() == null;
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getParentWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getTopWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScriptObject(final Object scriptObject) {
        final ScriptableObject so = (ScriptableObject) scriptObject;
        if (so != null) {
            so.put("dialogArguments", so, arguments_);
        }
        super.setScriptObject(scriptObject);
    }

    /**
     * Closes this window.
     */
    public void close() {
        destroyChildren();
        getWebClient().deregisterWebWindow(this);
    }

}
