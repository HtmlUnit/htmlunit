/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Default implementation of {@link JavaScriptErrorListener} that does only
 * logging in some cases.
 *
 * @author Ronald Brill
 */
public class DefaultJavaScriptErrorListener implements JavaScriptErrorListener, Serializable {

    private static final Log LOG = LogFactory.getLog(DefaultJavaScriptErrorListener.class);

    @Override
    public void scriptException(final HtmlPage page, final ScriptException scriptException) {
    }

    @Override
    public void timeoutError(final HtmlPage page, final long allowedTime, final long executionTime) {
    }

    @Override
    public void malformedScriptURL(final HtmlPage page, final String url,
                final MalformedURLException malformedURLException) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Unable to build URL for script src tag [" + url + "]", malformedURLException);
        }
    }

    @Override
    public void loadScriptError(final HtmlPage page, final URL scriptUrl, final Exception exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Error loading JavaScript from [" + scriptUrl + "].", exception);
        }
    }
}
