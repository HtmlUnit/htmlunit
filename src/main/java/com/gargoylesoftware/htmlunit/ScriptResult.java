/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import org.mozilla.javascript.Undefined;

/**
 * This object contains the result of executing a chunk of script code.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public final class ScriptResult {

    /** The object that was returned from the script engine. */
    private final Object javaScriptResult_;

    /** The page that is currently loaded at the end of the script execution. */
    private final Page newPage_;

    /**
     * Creates a new instance.
     *
     * @param javaScriptResult the object that was returned from the script engine
     * @param newPage the page that is currently loaded at the end of the script execution
     */
    public ScriptResult(final Object javaScriptResult, final Page newPage) {
        javaScriptResult_ = javaScriptResult;
        newPage_ = newPage;
    }

    /**
     * Returns the object that was the output of the script engine.
     * @return the result from the script engine
     */
    public Object getJavaScriptResult() {
        return javaScriptResult_;
    }

    /**
     * Returns the page that is loaded at the end of the script execution.
     * @return the new page
     */
    public Page getNewPage() {
        return newPage_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ScriptResult[result=" + javaScriptResult_ + " page=" + newPage_ + "]";
    }

    /**
     * Utility method testing if a script result is <tt>false</tt>.
     * @param scriptResult a script result (may be <tt>null</tt>)
     * @return <tt>true</tt> if <tt>scriptResult</tt> is <tt>false</tt>
     */
    public static boolean isFalse(final ScriptResult scriptResult) {
        return scriptResult != null && Boolean.FALSE.equals(scriptResult.getJavaScriptResult());
    }

    /**
     * Utility method testing if a script result is undefined (there was no return value).
     * @param scriptResult a script result (may be <tt>null</tt>)
     * @return <tt>true</tt> if <tt>scriptResult</tt> is undefined (there was no return value)
     */
    public static boolean isUndefined(final ScriptResult scriptResult) {
        return scriptResult != null && scriptResult.getJavaScriptResult() instanceof Undefined;
    }

}
