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
package com.gargoylesoftware.htmlunit.javascript;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.ScriptException;

/**
 * A listener for JavaScript exceptions.
 *
 * @author Ronald Brill
 */
public interface JavaScriptErrorListener {

    /**
     * Informs about a javascript exceptions.
     *
     * @param page the page that causes the problem
     * @param scriptException the occurred script exception
     */
    void scriptException(final InteractivePage page, final ScriptException scriptException);

    /**
     * Informs about a javascript timeout error.
     *
     * @param page the page that causes the problem
     * @param allowedTime the max time allowed for the execution
     * @param executionTime the already consumed time
     */
    void timeoutError(final InteractivePage page, final long allowedTime, final long executionTime);

    /**
     * Informs about a malformed url referencing to to script.
     *
     * @param page the page that causes the problem
     * @param url the malformed url
     * @param malformedURLException the occurred exception
     */
    void malformedScriptURL(final InteractivePage page, final String url,
            final MalformedURLException malformedURLException);

    /**
     * Informs about an exception during load of an javascript file refereed from a page.
     *
     * @param page the page that causes the problem
     * @param scriptUrl the url to load the script from
     * @param exception the occurred exception
     */
    void loadScriptError(final InteractivePage page, final URL scriptUrl, final Exception exception);
}
