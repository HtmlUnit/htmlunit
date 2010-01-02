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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptableProxy;

/**
 * Builds a proxy for the {@link Window} script object of a {@link WebWindow}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class WindowProxy extends SimpleScriptableProxy {
    private static final long serialVersionUID = -9124838618636683398L;
    private final WebWindow webWindow_;

    /**
     * Construct a proxy for the {@link Window} of the {@link WebWindow}.
     * @param webWindow the window
     */
    public WindowProxy(final WebWindow webWindow) {
        webWindow_ = webWindow;
    }

    /**
     * Gets the currently wrapped window JavaScript object.
     * @return the current window object
     */
    @Override
    public SimpleScriptable getWrappedScriptable() {
        return (Window) webWindow_.getScriptObject();
    }
}
