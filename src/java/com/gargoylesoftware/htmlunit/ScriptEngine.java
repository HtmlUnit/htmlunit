/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  An abstract base class for scripting engines.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public abstract class ScriptEngine {
    private final WebClient webClient_;
    private final Log scriptEngineLog_ = LogFactory.getLog(ScriptEngine.class);

    /**
     * Create an instance for the specifed web client
     * @param webClient The web client.
     */
    protected ScriptEngine( final WebClient webClient ) {
        assertNotNull("webClient", webClient);
        webClient_ = webClient;
    }


    /**
     * Return the web client that this engine is associated with.
     * @return The web client.
     */
    public final WebClient getWebClient() {
        return webClient_;
    }


    /**
     * Throw a NullPointerException with the specified description if the object is null.
     * @param description The description
     * @param object The object to check for null.
     */
    protected final void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException(description);
        }
    }


    /**
     * Execute the specified source code in the context of the given page.
     * @param htmlPage The page
     * @param sourceCode The code to execute.
     * @param sourceName A name for the chunk of code that is going to be executed.  This will be
     * used in error messages.
     * @return The result of executing the specified code
     */
    public Object execute(
            final HtmlPage htmlPage, final String sourceCode, final String sourceName ) {

        return execute(htmlPage, sourceCode, sourceName, null);
    }


    /**
     * Execute the specified source code in the context of the given page.
     * @param htmlPage The page
     * @param sourceCode The code to execute.
     * @param sourceName A name for the chunk of code that is going to be executed.  This will be
     * used in error messages.
     * @return The result of executing the specified code
     */
    public abstract Object execute(
        final HtmlPage htmlPage, final String sourceCode, final String sourceName, final HtmlElement htmlElement );


    /**
     * Return the log object that is being used to log information about the script engine.
     * @return The log
     */
    public Log getScriptEngineLog() {
        return scriptEngineLog_;
    }
}
