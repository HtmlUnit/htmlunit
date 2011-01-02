/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptableProxy;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * Proxy for a {@link HTMLDocument} script object. In theory we could satisfy single-document requirements
 * without a proxy, by reusing (with appropriate cleanup and re-initialization) a single {@link HTMLDocument}
 * instance across various pages. However, we allow users to keep references to old pages as they navigate
 * across a series of pages, and all of these pages need to be usable -- so we can't just leave these old
 * pages without a <tt>window.document</tt> object.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class HTMLDocumentProxy extends SimpleScriptableProxy<HTMLDocument> {

    private final WebWindow webWindow_;

    /**
     * Construct a proxy for the {@link HTMLDocument} of the {@link WebWindow}.
     * @param webWindow the window
     */
    public HTMLDocumentProxy(final WebWindow webWindow) {
        webWindow_ = webWindow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLDocument getDelegee() {
        final Window w = (Window) webWindow_.getScriptObject();
        return w.getDocument();
    }

}
