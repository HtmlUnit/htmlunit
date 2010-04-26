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
package com.gargoylesoftware.htmlunit.util;

import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.ScopeProvider;
import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.SourceProvider;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;

/**
 * Utility class containing miscellaneous {@link WebClient}-related methods.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public final class WebClientUtils {

    /**
     * Disallow instantiation of this class.
     */
    private WebClientUtils() {
        // Empty.
    }

    /**
     * Attaches a visual (GUI) debugger to the specified client.
     * @param client the client to which the visual debugger is to be attached
     * @see <a href="http://www.mozilla.org/rhino/debugger.html">Mozilla Rhino Debugger Documentation</a>
     */
    public static void attachVisualDebugger(final WebClient client) {
        final ScopeProvider sp = null;
        final HtmlUnitContextFactory cf = client.getJavaScriptEngine().getContextFactory();
        final Main main = Main.mainEmbedded(cf, sp, "HtmlUnit JavaScript Debugger");

        final SourceProvider sourceProvider = new SourceProvider() {
            public String getSource(final DebuggableScript script) {
                String sourceName = script.getSourceName();
                if (sourceName.endsWith("(eval)")) {
                    return null; // script is result of eval call. Rhino already knows the source and we don't
                }
                if (sourceName.startsWith("script in ")) {
                    sourceName = StringUtils.substringBetween(sourceName, "script in ", " from");
                    for (final WebWindow ww : client.getWebWindows()) {
                        final WebResponse wr = ww.getEnclosedPage().getWebResponse();
                        if (sourceName.equals(wr.getWebRequest().getUrl().toString())) {
                            return wr.getContentAsString();
                        }
                    }
                }
                return null;
            }
        };
        main.setSourceProvider(sourceProvider);
    }

}
