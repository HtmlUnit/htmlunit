/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
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
     * @deprecated Use {@link Assert#notNull(String,Object)}
     */
    protected final void assertNotNull( final String description, final Object object ) {
        Assert.notNull(description, object);
    }

    /**
     * perform initialization for the given page
     * @param page the page to initialize for
     */
    public abstract void initialize(HtmlPage page);

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
     * @param htmlElementScope The html element that will act as the context.
     * @return The result of the function call.
     */
    public abstract Object callFunction(
        final HtmlPage htmlPage,
        final Object javaScriptFunction,
        final Object thisObject,
        final Object [] args,
        final HtmlElement htmlElementScope );


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
    
    /**
     * Pre process the specified source code in the context of the given page using the processor specified
     * in the webclient. This method delegates to the pre processor handler specified in the
     * <code>WebClient</code>. If no pre processor handler is defined, the original source code is returned
     * unchanged.
     * @param htmlPage The page
     * @param sourceCode The code to process.
     * @param sourceName A name for the chunk of code.  This will be used in error messages.
     * @param htmlElement The html element that will act as the context.
     * @return The source code after being pre processed
     * @see com.gargoylesoftware.htmlunit.ScriptPreProcessor
     */
    public String preProcess(
        final HtmlPage htmlPage, final String sourceCode, final String sourceName, final HtmlElement htmlElement ) {

        String newSourceCode = sourceCode;
        final ScriptPreProcessor preProcessor = getWebClient().getScriptPreProcessor();
        if ( preProcessor != null ) {
            newSourceCode = preProcessor.preProcess(htmlPage, sourceCode, sourceName, htmlElement);
            if ( newSourceCode == null ) {
                newSourceCode = "";
            }
        }
        return newSourceCode;
    }

    /**
     * Indicates if Script is running in current thread. <br/>
     * This allows code to know if there own evaluation is has been triggered by some script code.
     * @return <code>true</code> if script is running.
     */
    public abstract boolean isScriptRunning();
}
