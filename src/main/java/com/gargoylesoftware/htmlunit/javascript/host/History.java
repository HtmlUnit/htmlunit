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

import java.io.IOException;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for the client's browsing history.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Daniel Gredler
 */
public class History extends SimpleScriptable {

    private static final long serialVersionUID = -285158453206844475L;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public History() {
        // Empty.
    }

    /**
     * Returns the "length" property.
     * @return the "length" property
     */
    public int jsxGet_length() {
        final WebWindow w = getWindow().getWebWindow();
        return w.getHistory().getLength();
    }

    /**
     * JavaScript function "back".
     */
    public void jsxFunction_back() {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().back();
        }
        catch (final IOException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * JavaScript function "forward".
     */
    public void jsxFunction_forward() {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().forward();
        }
        catch (final IOException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * JavaScript function "go".
     * @param relativeIndex the relative index
     */
    public void jsxFunction_go(final int relativeIndex) {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().go(relativeIndex);
        }
        catch (final IOException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

}
