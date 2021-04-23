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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Silent implementation of {@link JavaScriptErrorListener} that does no logging at all.
 *
 * @author Ronald Brill
 */
public class SilentJavaScriptErrorListener implements JavaScriptErrorListener, Serializable {

    @Override
    public void scriptException(final HtmlPage page, final ScriptException scriptException) {
    }

    @Override
    public void timeoutError(final HtmlPage page, final long allowedTime, final long executionTime) {
    }

    @Override
    public void malformedScriptURL(final HtmlPage page, final String url,
                final MalformedURLException malformedURLException) {
    }

    @Override
    public void loadScriptError(final HtmlPage page, final URL scriptUrl, final Exception exception) {
    }

    @Override
    public void warn(final String message, final String sourceName,
            final int line, final String lineSource, final int lineOffset) {
    }
}
