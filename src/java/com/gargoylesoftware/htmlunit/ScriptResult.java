/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 * This object contains the result of executing a chunk of script code.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class ScriptResult {
    private final Object javaScriptResult_;
    private final Page newPage_;

    /**
     * Create an instance
     *
     * @param javaScriptResult The object that was returned from the script engine
     * @param newPage The page that is currently loaded at the end of the script
     * execution.
     */
    public ScriptResult( final Object javaScriptResult, final Page newPage ) {
        javaScriptResult_ = javaScriptResult;
        newPage_ = newPage;
    }


    /**
     * Return the object that was the output of the script engine.
     * @return The result from the script engine.
     */
    public Object getJavaScriptResult() {
        return javaScriptResult_;
    }


    /**
     * Return the page that is loaded at the end of the script execution.
     * @return The new page.
     */
    public Page getNewPage() {
        return newPage_;
    }
}
