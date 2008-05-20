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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A wrapper for the <a href="http://www.mozilla.org/rhino">Rhino JavaScript engine</a>
 * that provides browser specific features.<br/>
 * Like all classes in this package, this class is not intended for direct use
 * and may change without notice.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @see <a href="http://groups-beta.google.com/group/netscape.public.mozilla.jseng/browse_thread/thread/b4edac57329cf49f/069e9307ec89111f">
 * Rhino and Java Browser</a>
 */
public class JavaScriptEngine implements Serializable {

    private static final long serialVersionUID = -5414040051465432088L;
    private final WebClient webClient_;
    private static final Log ScriptEngineLog_ = LogFactory.getLog(JavaScriptEngine.class);

    private static final ThreadLocal<Boolean> javaScriptRunning_ = new ThreadLocal<Boolean>();
    /**
     * Cache parsed scripts (only for js files, not for js code embedded in HTML code).
     * The WeakHashMap allows cached scripts to be GCed when the WebResponses are not retained
     * in the {@link com.gargoylesoftware.htmlunit.Cache} anymore.
     */
    private final transient Map<WebResponse, Script> cachedScripts_ =
        Collections.synchronizedMap(new WeakHashMap<WebResponse, Script>());

    /**
     * Key used to place the scope in which the execution of some JavaScript code
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
    }

    /**
     * Creates an instance for the specified webclient.
     *
     * @param webClient the webClient that will own this engine
     */
    public JavaScriptEngine(final WebClient webClient) {
        webClient_ = webClient;
    }

    /**
     * Returns the web client that this engine is associated with.
     * @return the web client
     */
    public final WebClient getWebClient() {
        return webClient_;
    }

    /**
     * Performs initialization for the given webWindow.
     * @param webWindow the web window to initialize for
     */
    public void initialize(final WebWindow webWindow) {
        WebAssert.notNull("webWindow", webWindow);

        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                try {
                    init(webWindow, cx);
                }
                catch (final Exception e) {
                    getLog().error("Exception while initializing JavaScript for the page", e);
                    throw new ScriptException(null, e); // BUG: null is not useful.
                }
                
                return null;
            }
        };

        ContextFactory.getGlobal().call(action);
    }

    /**
     * Initializes all the JS stuff for the window.
     * @param webWindow the web window
     * @param context the current context
     * @throws Exception if something goes wrong
     */
    private void init(final WebWindow webWindow, final Context context) throws Exception {
        final WebClient webClient = webWindow.getWebClient();
        final Map<Class< ? extends SimpleScriptable>, Scriptable> prototypes =
            new HashMap<Class< ? extends SimpleScriptable>, Scriptable>();
        final Map<String, Scriptable> prototypesPerJSName = new HashMap<String, Scriptable>();
        final Window window = new Window(this);
        final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(webClient.getBrowserVersion());
        context.initStandardObjects(window);
        StringPrimitivePrototypeBugFixer.installWorkaround(window);
        
        // put custom object to be called as very last prototype to call the fallback getter (if any)
        final Scriptable fallbackCaller = new ScriptableObject() {
            private static final long serialVersionUID = -7124423159070941606L;

            @Override
            public Object get(final String name, final Scriptable start) {
                if (start instanceof ScriptableWithFallbackGetter) {
                    return ((ScriptableWithFallbackGetter) start).getWithFallback(name);
                }
                return NOT_FOUND;
            }

            @Override
            public String getClassName() {
                return "htmlUnitHelper-fallbackCaller";
            }
        };
        ScriptableObject.getObjectPrototype(window).setPrototype(fallbackCaller);
        
        for (final String jsClassName : jsConfig.keySet()) {
            final ClassConfiguration config = jsConfig.getClassConfiguration(jsClassName);
            final boolean isWindow = Window.class.getName().equals(config.getLinkedClass().getName());
            if (isWindow) {
                configureConstantsPropertiesAndFunctions(config, window);
            }
            else {
                Scriptable prototype = configureClass(config, window);
                if (config.isJsObject()) {
                    // for FF, place object with prototype property in Window scope
                    if (!getWebClient().getBrowserVersion().isIE()) {
                        final Scriptable obj = config.getLinkedClass().newInstance();
                        obj.put("prototype", obj, prototype);
                        obj.setPrototype(prototype);
                        obj.setParentScope(window);
                        ScriptableObject.defineProperty(window, config.getClassName(), obj, ScriptableObject.DONTENUM);
                        // use this instance in prototype map to allow instanceof to work
                        prototype = obj;

                        if (obj.getClass() == Element.class && webWindow.getEnclosedPage() instanceof HtmlPage) {
                            final DomNode domNode =
                                new HtmlElement(null, "", (HtmlPage) webWindow.getEnclosedPage(), null) {
                                    private static final long serialVersionUID = -5614158965497997095L;
                                };
                            ((SimpleScriptable) obj).setDomNode(domNode);
                        }
                    }
                    prototypes.put(config.getLinkedClass(), prototype);
                }
                prototypesPerJSName.put(config.getClassName(), prototype);
            }
        }

        // once all prototypes have been build, it's possible to configure the chains
        final Scriptable objectPrototype = ScriptableObject.getObjectPrototype(window);
        for (final Map.Entry<String, Scriptable> entry : prototypesPerJSName.entrySet()) {
            final String name = entry.getKey();
            final ClassConfiguration config = jsConfig.getClassConfiguration(name);
            Scriptable prototype = entry.getValue();
            if (prototype.getPrototype() != null) {
                prototype = prototype.getPrototype(); // "double prototype" hack for FF
            }
            if (!StringUtils.isEmpty(config.getExtendedClass())) {
                final Scriptable parentPrototype = prototypesPerJSName.get(config.getExtendedClass());
                prototype.setPrototype(parentPrototype);
            }
            else {
                prototype.setPrototype(objectPrototype);
            }
        }
        
        // eval hack (cf unit tests testEvalScopeOtherWindow and testEvalScopeLocal
        final Class< ? >[] evalFnTypes = {String.class};
        final Member evalFn = Window.class.getMethod("custom_eval", evalFnTypes);
        final FunctionObject jsCustomEval = new FunctionObject("eval", evalFn, window);
        window.associateValue("custom_eval", jsCustomEval);
        
        for (final String jsClassName : jsConfig.keySet()) {
            final ClassConfiguration config = jsConfig.getClassConfiguration(jsClassName);
            final Method jsConstructor = config.getJsConstructor();
            if (jsConstructor != null) {
                final Scriptable prototype = prototypesPerJSName.get(jsClassName);
                if (prototype != null) {
                    final FunctionObject jsCtor = new FunctionObject(jsClassName, jsConstructor, window);
                    jsCtor.addAsConstructor(window, prototype);
                }
            }
        }

        window.setPrototypes(prototypes);
        window.initialize(webWindow);
    }

    /**
     * Configures the specified class for access via JavaScript.
     * @param config the configuration settings for the class to be configured
     * @param window the scope within which to configure the class
     * @throws InstantiationException if the new class cannot be instantiated
     * @throws IllegalAccessException if we don't have access to create the new instance
     * @throws InvocationTargetException if an exception is thrown during creation of the new object
     * @return the created prototype
     */
    private Scriptable configureClass(final ClassConfiguration config, final Scriptable window)
        throws InstantiationException, IllegalAccessException {

        final Class< ? > jsHostClass = config.getLinkedClass();
        final ScriptableObject prototype = (ScriptableObject) jsHostClass.newInstance();
        prototype.setParentScope(window);

        configureConstantsPropertiesAndFunctions(config, prototype);
        
        return prototype;
    }

    /**
     * Configures constants, properties and functions on the object.
     * @param config the configuration for the object
     * @param scriptable the object to configure
     */
    private void configureConstantsPropertiesAndFunctions(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        
        // the constants
        for (final String constant : config.constants()) {
            final Class< ? > linkedClass = config.getLinkedClass();
            try {
                final Object value = linkedClass.getField(constant).get(null);
                scriptable.defineProperty(constant, value, ScriptableObject.CONST);
            }
            catch (final Exception e) {
                throw Context.reportRuntimeError("Cannot get field '" + constant + "' for type: "
                    + config.getClassName());
            }
        }
        // the properties
        for (final String entryKey : config.propertyKeys()) {
            final Method readMethod = config.getPropertyReadMethod(entryKey);
            final Method writeMethod = config.getPropertyWriteMethod(entryKey);
            scriptable.defineProperty(entryKey, null, readMethod, writeMethod, ScriptableObject.EMPTY);
        }

        // the functions
        for (final String entryKey : config.functionKeys()) {
            final Method method = config.getFunctionMethod(entryKey);
            final FunctionObject functionObject = new FunctionObject(entryKey, method, scriptable);
            scriptable.defineProperty(entryKey, functionObject, ScriptableObject.EMPTY);
        }
    }

    /**
     * Returns the log object for this class.
     * @return the log object
     */
    protected Log getLog() {
        return LogFactory.getLog(getClass());
    }

    /**
     * Compiles the specified JavaScript code in the context of a given HTML page.
     *
     * @param htmlPage the page that the code will execute within
     * @param sourceCode the JavaScript code to execute
     * @param sourceName the name that will be displayed on error conditions
     * @param startLine the line at which the script source starts
     * @return the result of executing the specified code
     */
    public Script compile(final HtmlPage htmlPage,
                           String sourceCode,
                           final String sourceName,
                           final int startLine) {

        WebAssert.notNull("sourceCode", sourceCode);

        // Pre process the source code
        sourceCode = preProcess(htmlPage, sourceCode, sourceName, null);
        
        // PreProcess IE Conditional Compilation if needed
        final BrowserVersion browserVersion = htmlPage.getWebClient().getBrowserVersion();
        if (browserVersion.isIE() && browserVersion.getBrowserVersionNumeric() >= 4) {
            final ScriptPreProcessor ieCCPreProcessor = new IEConditionalCompilationScriptPreProcessor();
            sourceCode = ieCCPreProcessor.preProcess(htmlPage, sourceCode, sourceName, null);
        }

        // Remove HTML comments around the source if needed
        final String sourceCodeTrimmed = sourceCode.trim();
        if (sourceCodeTrimmed.startsWith("<!--")) {
            sourceCode = sourceCode.replaceFirst("<!--", "// <!--");
        }
        // IE ignores the last line containing uncommented -->
        if (getWebClient().getBrowserVersion().isIE() && sourceCodeTrimmed.endsWith("-->")) {
            final int lastDoubleSlash = sourceCode.lastIndexOf("//");
            final int lastNewLine = Math.max(sourceCode.lastIndexOf('\n'), sourceCode.lastIndexOf('\r'));
            if (lastNewLine > lastDoubleSlash) {
                sourceCode = sourceCode.substring(0, lastNewLine);
            }
        }

        final Scriptable scope = getScope(htmlPage, null);
        final String source = sourceCode;
        final ContextAction action = new HtmlUnitContextAction(scope, htmlPage) {
            @Override
            public Object doRun(final Context cx) {
                return cx.compileString(source, sourceName, startLine, null);
            }

            @Override
            protected String getSourceCode(final Context cx) {
                return source;
            }
        };

        return (Script) ContextFactory.getGlobal().call(action);
    }

    /**
     * Executes the specified JavaScript code in the context of a given HTML page.
     *
     * @param htmlPage the page that the code will execute within
     * @param sourceCode the JavaScript code to execute
     * @param sourceName the name that will be displayed on error conditions
     * @param startLine the line at which the script source starts
     * @return the result of executing the specified code
     */
    public Object execute(final HtmlPage htmlPage,
                           final String sourceCode,
                           final String sourceName,
                           final int startLine) {

        final Script script = compile(htmlPage, sourceCode, sourceName, startLine);
        return execute(htmlPage, script);
    }

    /**
     * Executes the specified JavaScript code in the context of a given HTML page.
     *
     * @param htmlPage the page that the code will execute within
     * @param script the script to execute
     * @return the result of executing the specified code
     */
    public Object execute(final HtmlPage htmlPage, final Script script) {
        final Scriptable scope = getScope(htmlPage, null);

        final ContextAction action = new HtmlUnitContextAction(scope, htmlPage) {
            @Override
            public Object doRun(final Context cx) {
                return script.exec(cx, scope);
            }

            @Override
            protected String getSourceCode(final Context cx) {
                return null;
            }
        };
        
        return ContextFactory.getGlobal().call(action);
    }

    /**
     * Calls a JavaScript function and return the result.
     * @param htmlPage the page
     * @param javaScriptFunction the function to call
     * @param thisObject the this object for class method calls
     * @param args the list of arguments to pass to the function
     * @param htmlElement the HTML element that will act as the context
     * @return the result of the function call
     */
    public Object callFunction(
            final HtmlPage htmlPage,
            final Object javaScriptFunction,
            final Object thisObject,
            final Object [] args,
            final DomNode htmlElement) {

        final Scriptable scope = getScope(htmlPage, htmlElement);
        
        final Function function = (Function) javaScriptFunction;
        final ContextAction action = new HtmlUnitContextAction(scope, htmlPage) {
            @Override
            public Object doRun(final Context cx) {
                return callFunction(htmlPage, function, cx, scope, (Scriptable) thisObject, args);
            }
            @Override
            protected String getSourceCode(final Context cx) {
                return cx.decompileFunction(function, 2);
            }
        };
        return ContextFactory.getGlobal().call(action);
    }

    private Scriptable getScope(final HtmlPage htmlPage, final DomNode htmlElement) {
        final Scriptable scope;
        if (htmlElement != null) {
            scope = htmlElement.getScriptObject();
        }
        else {
            scope = (Window) htmlPage.getEnclosingWindow().getScriptObject();
        }
        return scope;
    }

    /**
     * Calls the given function taking care of synchronization issues.
     * @param htmlPage the HTML page that caused this script to executed
     * @param function the js function to execute
     * @param context the context in which execution should occur
     * @param scope the execution scope
     * @param thisObject the 'this' object
     * @param args the function's arguments
     * @return the function result
     */
    public Object callFunction(final HtmlPage htmlPage, final Function function, final Context context,
            final Scriptable scope, final Scriptable thisObject, final Object[] args) {

        synchronized (htmlPage) { // 2 scripts can't be executed in parallel for one page
            return function.call(context, scope, thisObject, args);
        }
    }

    /**
     * Indicates if JavaScript is running in current thread. <br/>
     * This allows code to know if there own evaluation is has been  triggered by some JS code.
     * @return <code>true</code> if JavaScript is running
     */
    public boolean isScriptRunning() {
        return Boolean.TRUE.equals(javaScriptRunning_.get());
    }

    /**
     * Sets the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @param timeout the timeout value
     * @deprecated use WebClient.setJavaScriptTimeout() instead
     */
    @Deprecated
    public static void setTimeout(final long timeout) {
        HtmlUnitContextFactory.setTimeout(timeout);
    }

    /**
     * Returns the number of milliseconds a script is allowed to execute before
     * being terminated. A value of 0 or less means no timeout.
     *
     * @return the timeout value
     * @deprecated use WebClient.getJavaScriptTimeout() instead
     */
    @Deprecated
    public static long getTimeout() {
        return HtmlUnitContextFactory.getTimeout();
    }

    /**
     * Facility for ContextAction usage.
     * ContextAction should be preferred because according to Rhino doc it
     * "guarantees proper association of Context instances with the current thread and is faster".
     */
    private abstract class HtmlUnitContextAction implements ContextAction {
        private final Scriptable scope_;
        private final HtmlPage htmlPage_;
        public HtmlUnitContextAction(final Scriptable scope, final HtmlPage htmlPage) {
            scope_ = scope;
            htmlPage_ = htmlPage;
        }

        public final Object run(final Context cx) {
            final Boolean javaScriptAlreadyRunning = javaScriptRunning_.get();
            javaScriptRunning_.set(Boolean.TRUE);

            try {
                cx.putThreadLocal(KEY_STARTING_SCOPE, scope_);
                synchronized (htmlPage_) { // 2 scripts can't be executed in parallel for one page
                    return doRun(cx);
                }
            }
            catch (final Exception e) {
                final ScriptException scriptException = new ScriptException(htmlPage_, e, getSourceCode(cx));
                if (getWebClient().isThrowExceptionOnScriptError()) {
                    throw scriptException;
                }
                // use a ScriptException to log it because it provides good information
                // on the source code
                getLog().info("Caught script exception", scriptException);
                return null;
            }
            catch (final TimeoutError e) {
                if (getWebClient().isThrowExceptionOnScriptError()) {
                    throw new RuntimeException(e);
                }
                getLog().info("Caught script timeout error", e);
                return null;
            }
            finally {
                javaScriptRunning_.set(javaScriptAlreadyRunning);
            }
        }

        protected abstract Object doRun(final Context cx);

        protected abstract String getSourceCode(final Context cx);
    }

    /**
     * Returns the log object that is being used to log information about the script engine.
     * @return the log
     */
    public static Log getScriptEngineLog() {
        return ScriptEngineLog_;
    }

    /**
     * Pre process the specified source code in the context of the given page using the processor specified
     * in the webclient. This method delegates to the pre processor handler specified in the
     * <code>WebClient</code>. If no pre processor handler is defined, the original source code is returned
     * unchanged.
     * @param htmlPage the page
     * @param sourceCode the code to process
     * @param sourceName a name for the chunk of code (used in error messages)
     * @param htmlElement the HTML element that will act as the context
     * @return the source code after being pre processed
     * @see com.gargoylesoftware.htmlunit.ScriptPreProcessor
     */
    public String preProcess(
        final HtmlPage htmlPage, final String sourceCode, final String sourceName, final HtmlElement htmlElement) {

        String newSourceCode = sourceCode;
        final ScriptPreProcessor preProcessor = getWebClient().getScriptPreProcessor();
        if (preProcessor != null) {
            newSourceCode = preProcessor.preProcess(htmlPage, sourceCode, sourceName, htmlElement);
            if (newSourceCode == null) {
                newSourceCode = "";
            }
        }
        return newSourceCode;
    }

    /**
     * Gets the cached script for the given response.
     * @param webResponse the response corresponding to the script code
     * @return the parsed script
     */
    public Script getCachedScript(final WebResponse webResponse) {
        return cachedScripts_.get(webResponse);
    }

    /**
     * Caches a parsed script.
     * @param webResponse the response corresponding to the script code; a weak reference to this object
     *        will be used as the key for the cache
     * @param script the parsed script to cache
     */
    public void cacheScript(final WebResponse webResponse, final Script script) {
        cachedScripts_.put(webResponse, script);
    }
}
