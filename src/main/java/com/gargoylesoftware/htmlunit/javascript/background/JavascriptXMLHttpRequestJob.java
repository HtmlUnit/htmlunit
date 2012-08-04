/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;

/**
 * A helper class for XMLHttpRequest.
 * @version $Revision$
 * @author Ronald Brill
 */
final class JavascriptXMLHttpRequestJob extends BasicJavaScriptJob {
    private final ContextFactory contextFactory_;
    private final ContextAction action_;

    public JavascriptXMLHttpRequestJob(final ContextFactory contextFactory, final ContextAction action) {
        super();
        contextFactory_ = contextFactory;
        action_ = action;
    }

    public void run() {
        contextFactory_.call(action_);
    }

    @Override
    public String toString() {
        return "XMLHttpRequest Job " + getId();
    }
}
