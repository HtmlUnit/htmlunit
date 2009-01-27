/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for the client's browsing history.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 */
public class History extends SimpleScriptable {

    private static final long serialVersionUID = -285158453206844475L;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public History() { }

    /**
     * Returns the "length" property. Currently hardcoded to return 0.
     * @return the "length" property
     */
    public int jsxGet_length() {
        getLog().debug("javascript: history.length not implemented yet - returning 0");
        return 0;
    }

    /**
     * JavaScript function "back". Currently not implemented
     */
    public void jsxFunction_back() {
        getLog().debug("javascript: history.back() not implemented yet");
    }

    /**
     * JavaScript function "forward". Currently not implemented
     */
    public void jsxFunction_forward() {
        getLog().debug("javascript: history.forward() not implemented yet");
    }

    /**
     * JavaScript function "go". Currently not implemented
     * @param relativeUrl the relative URL
     */
    public void jsxFunction_go(final String relativeUrl) {
        getLog().debug("javascript: history.go(String) not implemented yet");
    }
}

