/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.Context;
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


    private final Map pageInfos_ = new HashMap(89);

    /**
     * Create an instance for the specified webclient
     *
     * @param webClient The webClient that will own this engine.
     */
    public JavaScriptEngine( final WebClient webClient ) {
        super( webClient );
    }


    private synchronized PageInfo getPageInfo( final HtmlPage htmlPage ) {
        assertNotNull( "htmlPage", htmlPage );

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
                "Image", "Style", "Option"
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
     * Execute the specified javascript code in the.getContext() of a given html page.
     *
     * @param htmlPage The page that the code will execute within
     * @param sourceCode The javascript code to execute.
     * @param sourceName The name that will be displayed on error conditions
     * @return The result of executing the specified code.
     */
    public Object execute(
        final HtmlPage htmlPage, final String sourceCode, final String sourceName ) {

        assertNotNull( "sourceCode", sourceCode );

        final PageInfo pageInfo = getPageInfo(htmlPage);

        final int lineNumber = 1;
        final Object securityDomain = null;

        try {
            final Object result = pageInfo.getContext().evaluateString(
                    pageInfo.getScope(), sourceCode, sourceName, lineNumber, securityDomain );
            return result;
        }
        catch( final JavaScriptException e ) {
            throw new ScriptException( e, sourceCode );
        }
        catch( final Throwable t ) {
            throw new ScriptException(t, sourceCode);
        }
    }
}

