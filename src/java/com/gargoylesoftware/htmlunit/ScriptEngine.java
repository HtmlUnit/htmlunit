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
        Assert.notNull("webClient", webClient);
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
     * @deprecated Use {@link Assert#assertNotNull(String,Object)}
     */
    protected final void assertNotNull( final String description, final Object object ) {
        Assert.notNull(description, object);
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
     * @param htmlElement The html element that will act as the context.
     * @return The result of executing the specified code
     */
    public abstract Object execute(
        final HtmlPage htmlPage, final String sourceCode, final String sourceName, final HtmlElement htmlElement );


    /**
     * Call a JavaScript function and return the result.
     * @param htmlPage The page
     * @param javaScriptFunction The function to call.
     * @param thisObject The this object for class method calls.
     * @param args The list of arguments to pass to the function.
     * @param htmlElement The html element that will act as the context.
     * @return The result of the function call.
     */
    public abstract Object callFunction(
        final HtmlPage htmlPage, final Object javaScriptFunction, final Object thisObject, Object [] args, final HtmlElement htmlElementScope );


    /**
     * Return the string representation of the JavaScript object in the context of the given page.
     * @param htmlPage The page
     * @param javaScriptObject The object to represent at a string.
     * @return The result string.
     */
    public abstract String toString(
        final HtmlPage htmlPage, final Object javaScriptObject );


    /**
     * Return the log object that is being used to log information about the script engine.
     * @return The log
     */
    public Log getScriptEngineLog() {
        return scriptEngineLog_;
    }
}
