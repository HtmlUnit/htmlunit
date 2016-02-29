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
package com.gargoylesoftware.htmlunit;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A window opened in JavaScript via either <tt>window.showModalDialog</tt> or <tt>window.showModelessDialog</tt>.
 * @author Daniel Gredler
 * @author Ronald Brill
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
        performRegistration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded() {
        return getScriptableObject() == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow getParentWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow getTopWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScriptableObject(final ScriptableObject scriptObject) {
        if (scriptObject != null) {
            scriptObject.put("dialogArguments", scriptObject, arguments_);
        }
        super.setScriptableObject(scriptObject);
    }

    /**
     * Closes this window.
     */
    public void close() {
        getJobManager().shutdown();
        destroyChildren();
        getWebClient().deregisterWebWindow(this);
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "DialogWindow[name=\"" + getName() + "\"]";
    }
}
