/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A wrapper for the <a href="http://www.mozilla.org/rhino">Rhino javascript engine</a>
 * that provides browser specific features.<br/>
 * Like all classes in this package, this class is not intended for direct use
 * and may change without notice.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @see <a href="http://groups-beta.google.com/group/netscape.public.mozilla.jseng/browse_thread/thread/b4edac57329cf49f/069e9307ec89111f">
 * Rhino and Java Browser</a>
 */
public final class JavaScriptEngine extends ScriptEngine {

    private static final ThreadLocal javaScriptRunning_ = new ThreadLocal();
    private static final ContextListener contextListener_;

    /**
     * Key used to place the scope in which the execution of some javascript code
     * started as thread local attribute in current context.<br/>
     * This is needed to resolve some relative locations relatively to the page 
     * in which the script is executed and not to the page which location is changed.
     */
    public static final String KEY_STARTING_SCOPE = "startingScope";

    /**
     * Initialize a context listener so we can count JS contexts and make sure we
     * are freeing them as necessary.
     */
    static {
        final HtmlUnitContextFactory contextFactory = new HtmlUnitContextFactory(getScriptEngineLog());
        ContextFactory.initGlobal(contextFactory);

        contextListener_ = new ContextListener();
        contextFactory.addListener( contextListener_ );
    }

    /**
     * Create an instance for the specified webclient
     *
     * @param webClient The webClient that will own this engine.
     */
    public JavaScriptEngine( final WebClient webClient ) {
        super( webClient );
    }

    /**
     * {@inheritDoc}
     */
    public void initialize( final WebWindow webWindow ) {

        Assert.notNull( "webWindow", webWindow );
        final WebClient webClient = webWindow.getWebClient();

        final Context context = Context.enter();
        try {
            final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(webClient.getBrowserVersion());
            final Window window = (Window) context.initStandardObjects( new Window() );

            final Iterator it = jsConfig.keySet().iterator();
            while (it.hasNext()) {
                final String jsClassName = (String) it.next();
                final ClassConfiguration config = jsConfig.getClassConfiguration(jsClassName);
                final boolean isWindow = Window.class.getName().equals( config.getLinkedClass().getName() );
                if( config.isJsObject() && !isWindow ) {
                    configureClass( config, window, jsClassName );
                }
            }

            ScriptableObject.defineClass(window, ElementArray.class);
            ScriptableObject.defineClass(window, OptionsArray.class);
            window.initialize(webWindow);
        }
        catch( final Exception e ) {
            getLog().error("Exception while initializing JavaScript for the page", e);
            throw new ScriptException(null, e); // BUG: null is not useful.
        }
        finally {
            Context.exit();
        }
    }

    /**
     * Configures the specified class for access via JavaScript.
     * @param config The configuration settings for the class to be configured.
     * @param scope The scope within which to configure the class.
     * @param name The name under which the class will be available in JavaScript.
     * @throws InstantiationException If the new class cannot be instantiated
     * @throws IllegalAccessException If we don't have access to create the new instance.
     * @throws InvocationTargetException if an exception is thrown during creation of the new object.
     */
    private void configureClass( final ClassConfiguration config, final Scriptable scope, final String name )
        throws InstantiationException, IllegalAccessException, InvocationTargetException {

        final Class jsHostClass = config.getLinkedClass();
        ScriptableObject.defineClass(scope, jsHostClass);
        final ScriptableObject prototype = (ScriptableObject) ScriptableObject.getClassPrototype(scope, name);

        final Iterator propertiesIterator = config.propertyKeys().iterator();
        while (propertiesIterator.hasNext()) {
            final String entryKey = (String) propertiesIterator.next();
            final Method readMethod = config.getPropertyReadMethod(entryKey);
            final Method writeMethod = config.getPropertyWriteMethod(entryKey);
            prototype.defineProperty(entryKey, null, readMethod, writeMethod, 0);
        }

        final Iterator functionsIterator = config.functionKeys().iterator();
        while (functionsIterator.hasNext()) {
            final String entryKey = (String) functionsIterator.next();
            final Method method = config.getFunctionMethod(entryKey);
            final FunctionObject functionObject = new FunctionObject(entryKey, method, prototype);
            prototype.defineProperty(entryKey, functionObject, 0);
        }
    }

    /**
     * Return the log object for this class
     * @return The log object
     */
    protected Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Execute the specified javascript code in the context of a given html page.
     *
     * @param htmlPage The page that the code will execute within
     * @param sourceCode The javascript code to execute.
     * @param sourceName The name that will be displayed on error conditions.
     * @param htmlElement The element that will be used as context.
     * @return The result of executing the specified code.
     */
    public Object execute( final HtmlPage htmlPage,
                           String sourceCode,
                           final String sourceName,
                           final HtmlElement htmlElement ) {

        Assert.notNull( "sourceCode", sourceCode );

        // Pre process the source code
        sourceCode = preProcess(htmlPage, sourceCode, sourceName, htmlElement);

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
            while( startIndex < endIndex && eachChar != '\n' && eachChar != '\r' ) {
                eachChar = sourceCode.charAt( ++startIndex );
            }

            sourceCode = sourceCode.substring(startIndex, endIndex);
        }

        final Scriptable scope;
        if( htmlElement != null ) {
            scope = (Scriptable) htmlElement.getScriptObject();
        }
        else {
            scope = (Window) htmlPage.getEnclosingWindow().getScriptObject();
        }
        final int lineNumber = 1;
        final Object securityDomain = null;

        final Boolean javaScriptAlreadyRunning = (Boolean) javaScriptRunning_.get();
        javaScriptRunning_.set(Boolean.TRUE);
        final Context context = Context.enter();
        context.putThreadLocal(KEY_STARTING_SCOPE, scope);
        try {
            final Object result;
            synchronized (htmlPage) // 2 scripts can't be executed in parallel for one page
            {
                result = context.evaluateString( scope, sourceCode, sourceName, lineNumber, securityDomain );
            }
            return result;
        }
        catch (final Exception e ) {
            final ScriptException scriptException = new ScriptException(htmlPage, e, sourceCode );
            if (getWebClient().isThrowExceptionOnScriptError()) {
                throw scriptException;
            }
            else {
                // use a ScriptException to log it because it provides good information
                // on the source code
                getLog().info("Catched script exception", scriptException);
                return null;
            }
        }
        catch (final TimeoutError e) {
            if (getWebClient().isThrowExceptionOnScriptError()) {
                throw new RuntimeException(e);
            }
            else {
                getLog().info("Catched script timeout error", e);
                return null;
            }
        }
        finally {
            javaScriptRunning_.set(javaScriptAlreadyRunning);
            Context.exit();
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
            final HtmlElement htmlElement ) {

        final Scriptable scope;
        if( htmlElement != null ) {
            scope = (Scriptable) htmlElement.getScriptObject();
        }
        else {
            scope = (Window) htmlPage.getEnclosingWindow().getScriptObject();
        }
        // some js code (like onchange handlers) should not be triggered from JS code:
        // => keep trace of JS running or not
        final Boolean javaScriptAlreadyRunning = (Boolean) javaScriptRunning_.get();
        javaScriptRunning_.set(Boolean.TRUE);
        final Function function = (Function) javaScriptFunction;
        final Context context = Context.enter();
        context.putThreadLocal(KEY_STARTING_SCOPE, scope);
        try {
            return callFunction(htmlPage, function, context, scope, (Scriptable) thisObject, args);
        }
        catch (final Exception e ) {
            final String sourceCode = context.decompileFunction(function, 2);
            final ScriptException scriptException = new ScriptException(htmlPage, e, sourceCode);
            if (getWebClient().isThrowExceptionOnScriptError()) {
                throw scriptException;
            }
            else {
                // use a ScriptException to log it because it provides good information
                // on the source code
                getLog().info("Catched script exception", scriptException);
                return null;
            }
        }
        catch (final TimeoutError e) {
            if (getWebClient().isThrowExceptionOnScriptError()) {
                throw new RuntimeException(e);
            }
            else {
                getLog().info("Catched script timeout error", e);
                return null;
            }
        }
        finally {
            javaScriptRunning_.set(javaScriptAlreadyRunning);
            Context.exit();
        }
    }

    /**
     * Calls the given function taking care of synchronisation issues. 
     * @param htmlPage the html page that caused this script to executed
     * @param function the js function to execute
     * @param context the context in which execution should occur
     * @param scope the execution scope
     * @param thisObject the 'this' object
     * @param args the function's arguments
     * @return the function result
     */
    public Object callFunction(final HtmlPage htmlPage, final Function function, final Context context, 
            final Scriptable scope, final Scriptable thisObject, final Object[] args) {

        synchronized (htmlPage) // 2 scripts can't be executed in parallel for one page
        {
            return function.call(context, scope, thisObject, args);
        }
    }

    /**
     * Indicates if JavaScript is running in current thread. <br/>
     * This allows code to know if there own evaluation is has been  triggered by some JS code.
     * @return <code>true</code> if JavaScript is running.
     */
    public boolean isScriptRunning() {
        return Boolean.TRUE.equals(javaScriptRunning_.get());
    }

    /** 
     * Set the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @param timeout the timeout value
     */
    public static void setTimeout(final long timeout) {
        HtmlUnitContextFactory.setTimeout(timeout);
    }

    /**
     * Returns the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @return the timeout value
     */
    public static long getTimeout() {
        return HtmlUnitContextFactory.getTimeout();
    }

    /**
     * Returns the current number of unexited contexts.
     * @return The current number of unexited contexts.
     * @see Context#enter()
     * @see Context#exit()
     */
    public static int getContextCount() {
        return contextListener_.getContextCount();
    }

    /**
     * Listens to <tt>Context</tt> creation and release events so we can ensure that resources
     * are being released by the JavaScript engine.
     * @see Context#enter()
     * @see Context#exit()
     */
    private static class ContextListener implements ContextFactory.Listener {
        private final Set contexts_ = Collections.synchronizedSet(new HashSet());
        public void contextCreated( final Context c ) {
            contexts_.add( c );
        }
        public void contextReleased( final Context c ) {
            contexts_.remove( c );
        }
        /** @return the context count */
        public int getContextCount() {
            return contexts_.size();
        }
    }

}

