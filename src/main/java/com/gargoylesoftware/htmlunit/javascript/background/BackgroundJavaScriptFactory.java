/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.background;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * A factory for all the things we have to construct from outside of the
 * JavaScript engine.
 *
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
public class BackgroundJavaScriptFactory implements JavaScriptFactory {

    private static JavaScriptFactory Factory_ = new BackgroundJavaScriptFactory();

    /**
     * Returns the current factory.
     *
     * @return the active factory
     */
    public static JavaScriptFactory theFactory() {
        return Factory_;
    }

    /**
     * Set the factory to a new one.
     *
     * @param factory the new factory.
     */
    public static void setFactory(final JavaScriptFactory factory) {
        Factory_ = factory;
    }

    @Override
    public JavaScriptJob createJavaScriptJob(final int initialDelay, final Integer period, final String label,
            final WebWindow window, final String script) {
        return new JavaScriptStringJob(initialDelay, period, label, window, script);
    }

    @Override
    public JavaScriptJob createJavaScriptJob(final int initialDelay,
            final Integer period, final String label,
            final WebWindow window, final Function function, final Object[] args) {
        return new JavaScriptFunctionJob(initialDelay, period, label, window, function, args);
    }

    @Override
    public JavaScriptJob createJavascriptXMLHttpRequestJob(final ContextFactory contextFactory,
            final ContextAction<Object> action) {
        return new JavascriptXMLHttpRequestJob(contextFactory, action);
    }

    @Override
    public JavaScriptJob createJavaScriptJob(final int initialDelay, final Integer period, final Runnable runnable) {
        return new BasicJavaScriptJob(initialDelay, period) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public JavaScriptJob createDownloadBehaviorJob(final URL url, final Function callback, final WebClient client) {
        return new DownloadBehaviorJob(url, callback, client);
    }

    @Override
    public JavaScriptExecutor createJavaScriptExecutor(final WebClient webClient) {
        return new DefaultJavaScriptExecutor(webClient);
    }

    @Override
    public JavaScriptJobManager createJavaScriptJobManager(final WebWindow webWindow) {
        return new JavaScriptJobManagerImpl(webWindow);
    }

    /**
     * The constructor.
     */
    protected BackgroundJavaScriptFactory() {
        super();
    }
}
