/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.util.WeakHashMap;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * A wrapper for the <a href="http://www.mozilla.org/rhino">Rhino javascript engine</a>
 * that provides browser specific features.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class JavaScriptEngine extends ScriptEngine {
    /** Information specific to the javascript engine */
    public static final class PageInfo {
        private final JavaScriptEngine engine_;
        /**
         * Create an instance
         * @param engine The javascript engine that created this object
         */
        public PageInfo( final JavaScriptEngine engine) {
            engine_ = engine;
        }

        /** The javascript.getContext() */
        private Context context_;
        /** The javascript.getScope() */
        private Scriptable scope_;

        /**
         * Return the context
         * @return the context
         */
        public Context getContext() {
            return context_;
        }
        /**
         * Return the scope
         * @return the scope
         */
        public Scriptable getScope() {
            return scope_;
        }
        /**
         * Return the javascript engine
         * @return the javascript engine
         */
        public JavaScriptEngine getJavaScriptEngine() {
            return engine_;
        }
    }


    /**
     * Map where keys are {@link HtmlPage}s and values are {@link PageInfo}s
     */
    private final Map pageInfos_ = new WeakHashMap(89);

    /**
     * Create an instance for the specified webclient
     *
     * @param webClient The webClient that will own this engine.
     */
    public JavaScriptEngine( final WebClient webClient ) {
        super( webClient );
    }


    private synchronized PageInfo getPageInfo( final HtmlPage htmlPage ) {
        Assert.notNull( "htmlPage", htmlPage );

        final PageInfo existingPageInfo = (PageInfo)pageInfos_.get(htmlPage);
        if( existingPageInfo != null ) {
            return existingPageInfo;
        }

        try {
            final PageInfo newPageInfo = new PageInfo(this);

            newPageInfo.context_ = Context.enter();
            newPageInfo.getContext().setErrorReporter(
                new StrictErrorReporter(getScriptEngineLog()) );
            final Scriptable parentScope = newPageInfo.getContext().initStandardObjects(null);

            final String hostClassNames[] = {
                "HTMLElement" ,"Window", "Document", "Form", "Input", "Navigator",
                "Screen", "History", "Radio", "Location", "Text", "Button", "Checkbox",
                "FileUpload", "Hidden", "Select", "Password", "Reset", "Submit", "Textarea",
                "Image", "Style", "Option", "Anchor"
            };

            for( int i=0; i<hostClassNames.length; i++ ) {
                final String className
                    = "com.gargoylesoftware.htmlunit.javascript.host."+hostClassNames[i];
                try {
                    ScriptableObject.defineClass( parentScope, Class.forName(className) );
                }
                catch( final ClassNotFoundException e ) {
                    throw new NoClassDefFoundError(className);
                }
            }

            ScriptableObject.defineClass(parentScope, DocumentAllArray.class);
            ScriptableObject.defineClass(parentScope, FormElementsArray.class);

            final Window window = (Window)newPageInfo.getContext().newObject(
                parentScope, "Window", new Object[0]);
            newPageInfo.scope_ = window;
            newPageInfo.getScope().setParentScope(parentScope);
            window.setPageInfo(newPageInfo);
            window.initialize(htmlPage);

            pageInfos_.put( htmlPage, newPageInfo );

            return newPageInfo;
        }
        catch( final Exception e ) {
            throw new ScriptException(e);
        }
//        Context.exit();
    }


    /**
     * Determine the scope for the page and element.
     * @param htmlPage The page
     * @param htmlElementScope The element that will be used as context or null if
     * the page should be used as context.
     * @return The JavaScript execution scope.
     */
    private Scriptable getScope(
        final PageInfo pageInfo, final HtmlElement htmlElementScope ) {

        Scriptable scope;
        if( htmlElementScope == null ) {
            scope = pageInfo.getScope();
        }
        else {
            scope = (Scriptable)htmlElementScope.getScriptObject();
            if( scope == null ) {
                scope = ((SimpleScriptable)pageInfo.getScope()).getScriptableFor(htmlElementScope);
            }
            scope.setParentScope(pageInfo.getScope());
        }
        return scope;
    }


    /**
     * Execute the specified javascript code in the context of a given html page.
     *
     * @param htmlPage The page that the code will execute within
     * @param sourceCode The javascript code to execute.
     * @param sourceName The name that will be displayed on error conditions
     * @param htmlElementScope The element that will be used as context or null if
     * the page should be used as context.
     * @return The result of executing the specified code.
     */
    public Object execute(
        final HtmlPage htmlPage, String sourceCode, final String sourceName, final HtmlElement htmlElementScope ) {

        Assert.notNull( "sourceCode", sourceCode );

        // Remove html comments around the source if needed
        sourceCode = sourceCode.trim();
        if( sourceCode.startsWith("<!--") ) {
            int startIndex = 4;

            final int endIndex;
            if( sourceCode.endsWith("-->") ) {
                endIndex = sourceCode.length()-3;
            }
            else {
                endIndex = sourceCode.length();
            }

            // Anything on the same line as the opening comment should be ignored
            char eachChar = sourceCode.charAt(startIndex);
            while( startIndex <= endIndex && eachChar != '\n' && eachChar != '\r' ) {
                eachChar = sourceCode.charAt( ++startIndex );
            }

            sourceCode = sourceCode.substring(startIndex, endIndex);
        }

        final PageInfo pageInfo = getPageInfo(htmlPage);

        final int lineNumber = 1;
        final Object securityDomain = null;

        final Scriptable scope = getScope( pageInfo, htmlElementScope );

        try {
            final Object result = pageInfo.getContext().evaluateString(
                    scope, sourceCode, sourceName, lineNumber, securityDomain );
            return result;
        }
        catch( final JavaScriptException e ) {
            throw new ScriptException( e, sourceCode );
        }
        catch( final Throwable t ) {
            throw new ScriptException(t, sourceCode);
        }
    }


    /**
     * Call a JavaScript function and return the result.
     * @param htmlPage The page
     * @param javaScriptFunction The function to call.
     * @param thisObject The this object for class method calls.
     * @param args The list of arguments to pass to the function.
     * @param htmlElement The html element that will act as the context.
     * @return The result of the function call.
     */
    public Object callFunction(
            final HtmlPage htmlPage, 
            final Object javaScriptFunction, 
            final Object thisObject, 
            final Object [] args, 
            final HtmlElement htmlElementScope ) {
                
        final PageInfo pageInfo = getPageInfo(htmlPage);

        final Scriptable scope = getScope( pageInfo, htmlElementScope );
        try {
            final Object result = ((Function) javaScriptFunction).call( pageInfo.getContext(),
                scope, (Scriptable) thisObject, args );
            return result;
        }
        catch( final JavaScriptException e ) {
            throw new ScriptException( e, toString( htmlPage, javaScriptFunction ) );
        }
        catch( final Throwable t ) {
            throw new ScriptException(t, toString( htmlPage, javaScriptFunction ) );
        }
    }


    /**
     * Return the string representation of the JavaScript object in the context of the given page.
     * @param htmlPage The page
     * @param javaScriptObject The object to represent at a string.
     * @return The result string.
     */
    public String toString( final HtmlPage htmlPage, final Object javaScriptObject ) {

        final PageInfo pageInfo = getPageInfo(htmlPage);

        final String result = pageInfo.getContext().toString(
                javaScriptObject );
        return result;
    }
}

