/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALLOW_CONST_ASSIGNMENT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CONSTRUCTOR;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DEFINE_GETTER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DONT_ENUM_FUNCTIONS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ECMA5_FUNCTIONS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FUNCTION_BIND;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FUNCTION_TOSOURCE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_HAS_OBJECT_WITH_PROTOTYPE_PROPERTY_IN_WINDOW_SCOPE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XML;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STRING_TRIM;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.StringCustom;
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
 * @author Amit Manjhi
 * @author Ronald Brill
 * @see <a href="http://groups-beta.google.com/group/netscape.public.mozilla.jseng/browse_thread/thread/b4edac57329cf49f/069e9307ec89111f">
 * Rhino and Java Browser</a>
 */
public class JavaScriptEngine {

    private static final Log LOG = LogFactory.getLog(JavaScriptEngine.class);

    private final WebClient webClient_;
    private final HtmlUnitContextFactory contextFactory_;
    private final JavaScriptConfiguration jsConfig_;

    private transient ThreadLocal<Boolean> javaScriptRunning_;
    private transient ThreadLocal<List<PostponedAction>> postponedActions_;
    private transient boolean holdPostponedActions_;

    /** The JavaScriptExecutor corresponding to all windows of this Web client */
    private transient JavaScriptExecutor javaScriptExecutor_;

    /**
     * Key used to place the scope in which the execution of some JavaScript code
     * started as thread local attribute in current context.<br/>
     * This is needed to resolve some relative locations relatively to the page
     * in which the script is executed and not to the page which location is changed.
     */
    public static final String KEY_STARTING_SCOPE = "startingScope";

    /**
     * Key used to place the {@link HtmlPage} for which the JavaScript code is executed
     * as thread local attribute in current context.
     */
    public static final String KEY_STARTING_PAGE = "startingPage";

    /**
     * Creates an instance for the specified {@link WebClient}.
     *
     * @param webClient the client that will own this engine
     */
    public JavaScriptEngine(final WebClient webClient) {
        webClient_ = webClient;
        contextFactory_ = new HtmlUnitContextFactory(webClient);
        initTransientFields();
        jsConfig_ = JavaScriptConfiguration.getInstance(webClient.getBrowserVersion());
    }

    /**
     * Returns the web client that this engine is associated with.
     * @return the web client
     */
    public final WebClient getWebClient() {
        return webClient_;
    }

    /**
     * Returns this JavaScript engine's Rhino {@link net.sourceforge.htmlunit.corejs.javascript.ContextFactory}.
     * @return this JavaScript engine's Rhino {@link net.sourceforge.htmlunit.corejs.javascript.ContextFactory}
     */
    public HtmlUnitContextFactory getContextFactory() {
        return contextFactory_;
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
                    LOG.error("Exception while initializing JavaScript for the page", e);
                    throw new ScriptException(null, e); // BUG: null is not useful.
                }

                return null;
            }
        };

        getContextFactory().call(action);
    }

    /**
     * Returns the JavaScriptExecutor.
     * @return the JavaScriptExecutor.
     */
    public JavaScriptExecutor getJavaScriptExecutor() {
        return javaScriptExecutor_;
    }

    /**
     * Initializes all the JS stuff for the window.
     * @param webWindow the web window
     * @param context the current context
     * @throws Exception if something goes wrong
     */
    private void init(final WebWindow webWindow, final Context context) throws Exception {
        final WebClient webClient = webWindow.getWebClient();
        final BrowserVersion browserVersion = webClient.getBrowserVersion();
        final Map<Class<? extends SimpleScriptable>, Scriptable> prototypes =
            new HashMap<Class<? extends SimpleScriptable>, Scriptable>();
        final Map<String, ScriptableObject> prototypesPerJSName = new HashMap<String, ScriptableObject>();
        final Window window = new Window();
        context.initStandardObjects(window);
        if (browserVersion.hasFeature(JS_CONSTRUCTOR)) {
            defineConstructor(window, window, new Window());
        }
        else {
            deleteProperties(window, "constructor");
        }

        // remove some objects, that Rhino defines in top scope but that we don't want
        deleteProperties(window, "java", "javax", "org", "com", "edu", "net",
                "JavaAdapter", "JavaImporter", "Continuation", "Packages", "getClass");
        if (!browserVersion.hasFeature(JS_XML)) {
            deleteProperties(window, "XML", "XMLList", "Namespace", "QName");
        }

        // put custom object to be called as very last prototype to call the fallback getter (if any)
        final Scriptable fallbackCaller = new FallbackCaller();
        ScriptableObject.getObjectPrototype(window).setPrototype(fallbackCaller);

        final boolean putPrototypeInWindowScope = browserVersion.hasFeature(JS_HAS_OBJECT_WITH_PROTOTYPE_PROPERTY_IN_WINDOW_SCOPE);
        for (final ClassConfiguration config : jsConfig_.getAll()) {
            final boolean isWindow = Window.class.getName().equals(config.getHostClass().getName());
            if (isWindow) {
                configureConstantsPropertiesAndFunctions(config, window);
            }
            else {
                final ScriptableObject prototype = configureClass(config, window);
                if (config.isJsObject()) {
                    // for FF, place object with prototype property in Window scope
                    if (putPrototypeInWindowScope) {
                        final SimpleScriptable obj = config.getHostClass().newInstance();
                        prototype.defineProperty("__proto__", prototype, ScriptableObject.DONTENUM);
                        obj.defineProperty("prototype", prototype, ScriptableObject.DONTENUM); // but not setPrototype!
                        obj.setParentScope(window);
                        ScriptableObject.defineProperty(window, obj.getClassName(), obj, ScriptableObject.DONTENUM);
                        // this obj won't have prototype, constants need to be configured on it again
                        configureConstants(config, obj);

                        if (obj.getClass() == Element.class && webWindow.getEnclosedPage() instanceof HtmlPage) {
                            final DomNode domNode =
                                new HtmlDivision(null, "", (SgmlPage) webWindow.getEnclosedPage(), null);
                            obj.setDomNode(domNode);
                        }
                    }
                    prototypes.put(config.getHostClass(), prototype);
                }
                prototypesPerJSName.put(config.getHostClass().getSimpleName(), prototype);
            }
        }

        // once all prototypes have been build, it's possible to configure the chains
        final Scriptable objectPrototype = ScriptableObject.getObjectPrototype(window);
        for (final Map.Entry<String, ScriptableObject> entry : prototypesPerJSName.entrySet()) {
            final String name = entry.getKey();
            final ClassConfiguration config = jsConfig_.getClassConfiguration(name);
            Scriptable prototype = entry.getValue();
            if (prototype.getPrototype() != null) {
                prototype = prototype.getPrototype(); // "double prototype" hack for FF
            }
            if (!StringUtils.isEmpty(config.getExtendedClassName())) {
                final Scriptable parentPrototype = prototypesPerJSName.get(config.getExtendedClassName());
                prototype.setPrototype(parentPrototype);
            }
            else {
                prototype.setPrototype(objectPrototype);
            }
        }

        for (final ClassConfiguration config : jsConfig_.getAll()) {
            final Method jsConstructor = config.getJsConstructor();
            final String jsClassName = config.getHostClass().getSimpleName();
            final ScriptableObject prototype = prototypesPerJSName.get(jsClassName);
            if (prototype != null) {
                if (jsConstructor != null) {
                    final FunctionObject functionObject = new FunctionObject(jsClassName, jsConstructor, window);
                    functionObject.addAsConstructor(window, prototype);
                    configureConstants(config, functionObject);
                }
                else {
                    if (browserVersion.hasFeature(JS_CONSTRUCTOR)) {
                        final Class<?> jsHostClass = config.getHostClass();
                        final ScriptableObject constructor = (ScriptableObject) jsHostClass.newInstance();
                        defineConstructor(window, prototype, constructor);
                        configureConstants(config, constructor);
                    }
                    else {
                        deleteProperties(prototype, "constructor");
                    }
                }
            }
        }

        // Rhino defines too much methods for us, particularly since implementation of ECMAScript5
        removePrototypeProperties(window, "String", "equals", "equalsIgnoreCase");
        if (browserVersion.hasFeature(STRING_TRIM)) {
            final ScriptableObject stringPrototype =
                (ScriptableObject) ScriptableObject.getClassPrototype(window, "String");
            stringPrototype.defineFunctionProperties(new String[] {"trimLeft", "trimRight"},
                StringCustom.class, ScriptableObject.EMPTY);
        }
        else {
            removePrototypeProperties(window, "String", "trim");
        }
        if (!browserVersion.hasFeature(JS_FUNCTION_BIND)) {
            removePrototypeProperties(window, "Function", "bind");
        }
        if (!browserVersion.hasFeature(JS_ECMA5_FUNCTIONS)) {
            removePrototypeProperties(window, "Date", "toISOString", "toJSON");
        }

        if (!browserVersion.hasFeature(JS_DEFINE_GETTER)) {
            removePrototypeProperties(window, "Object", "__defineGetter__", "__defineSetter__", "__lookupGetter__",
                    "__lookupSetter__");
        }

        // only FF has toSource
        if (!browserVersion.hasFeature(JS_FUNCTION_TOSOURCE)) {
            deleteProperties(window, "isXMLName", "uneval");
            removePrototypeProperties(window, "Object", "toSource");
            removePrototypeProperties(window, "Array", "toSource");
            removePrototypeProperties(window, "Date", "toSource");
            removePrototypeProperties(window, "Function", "toSource");
            removePrototypeProperties(window, "Number", "toSource");
            removePrototypeProperties(window, "String", "toSource");
        }

        NativeFunctionToStringFunction.installFix(window, webClient.getBrowserVersion());

        if (browserVersion.hasFeature(JS_ALLOW_CONST_ASSIGNMENT)) {
            makeConstWritable(window, "undefined", "NaN", "Infinity");
        }

        window.setPrototypes(prototypes);
        window.initialize(webWindow);
    }

    private void defineConstructor(final Window window, final Scriptable prototype, final ScriptableObject constructor) {
        constructor.setParentScope(window);
        ScriptableObject.defineProperty(prototype, "constructor", constructor,
                ScriptableObject.DONTENUM  | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        ScriptableObject.defineProperty(constructor, "prototype", prototype,
                ScriptableObject.DONTENUM  | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        window.defineProperty(constructor.getClassName(), constructor, ScriptableObject.DONTENUM);
    }

    private void makeConstWritable(final ScriptableObject scope, final String... constNames) {
        for (final String name : constNames) {
            final Object value = ScriptableObject.getProperty(scope, name);
            ScriptableObject.defineProperty(scope, name, value,
                    ScriptableObject.DONTENUM | ScriptableObject.PERMANENT);
        }
    }

    /**
     * Deletes the properties with the provided names.
     * @param scope the scope from which properties have to be removed
     * @param propertiesToDelete the list of property names
     */
    private void deleteProperties(final ScriptableObject scope, final String... propertiesToDelete) {
        for (final String property : propertiesToDelete) {
            scope.delete(property);
        }
    }

    /**
     * Removes prototype properties.
     * @param scope the scope
     * @param className the class for which properties should be removed
     * @param properties the properties to remove
     */
    private void removePrototypeProperties(final Scriptable scope, final String className,
            final String... properties) {
        final ScriptableObject prototype = (ScriptableObject) ScriptableObject.getClassPrototype(scope, className);
        for (final String property : properties) {
            prototype.delete(property);
        }
    }

    /**
     * Configures the specified class for access via JavaScript.
     * @param config the configuration settings for the class to be configured
     * @param window the scope within which to configure the class
     * @throws InstantiationException if the new class cannot be instantiated
     * @throws IllegalAccessException if we don't have access to create the new instance
     * @return the created prototype
     */
    private ScriptableObject configureClass(final ClassConfiguration config, final Scriptable window)
        throws InstantiationException, IllegalAccessException {

        final Class<?> jsHostClass = config.getHostClass();
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
        configureConstants(config, scriptable);

        // the properties
        for (final Entry<String, ClassConfiguration.PropertyInfo> propertyEntry : config.propertyEntries()) {
            final String propertyName = propertyEntry.getKey();
            final Method readMethod = propertyEntry.getValue().getReadMethod();
            final Method writeMethod = propertyEntry.getValue().getWriteMethod();
            scriptable.defineProperty(propertyName, null, readMethod, writeMethod, ScriptableObject.EMPTY);
        }

        int attributes;
        if (webClient_.getBrowserVersion().hasFeature(JS_DONT_ENUM_FUNCTIONS)) {
            attributes = ScriptableObject.DONTENUM;
        }
        else {
            attributes = ScriptableObject.EMPTY;
        }
        // the functions
        for (final Entry<String, Method> functionInfo : config.functionEntries()) {
            final String functionName = functionInfo.getKey();
            final Method method = functionInfo.getValue();
            final FunctionObject functionObject = new FunctionObject(functionName, method, scriptable);
            scriptable.defineProperty(functionName, functionObject, attributes);
        }
    }

    private void configureConstants(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        final Class<?> linkedClass = config.getHostClass();
        for (final String constant : config.constants()) {
            try {
                final Object value = linkedClass.getField(constant).get(null);
                scriptable.defineProperty(constant, value, ScriptableObject.EMPTY);
            }
            catch (final Exception e) {
                throw Context.reportRuntimeError("Cannot get field '" + constant + "' for type: "
                    + config.getHostClass().getName());
            }
        }
    }

    /**
     * Register WebWindow with the JavaScriptExecutor.
     * @param webWindow the WebWindow to be registered.
     */
    public void registerWindowAndMaybeStartEventLoop(final WebWindow webWindow) {
        if (javaScriptExecutor_ == null) {
            javaScriptExecutor_ = BackgroundJavaScriptFactory.theFactory().createJavaScriptExecutor(webClient_);
        }
        javaScriptExecutor_.addWindow(webWindow);
    }

    /**
     * Executes the jobs in the eventLoop till timeoutMillis expires or the eventLoop becomes empty.
     * No use in non-GAE mode (see {@link com.gargoylesoftware.htmlunit.gae.GAEUtils#isGaeMode}.
     * @param timeoutMillis the timeout in milliseconds
     * @return the number of jobs executed
     */
    public int pumpEventLoop(final long timeoutMillis) {
        if (javaScriptExecutor_ == null) {
            return 0;
        }
        return javaScriptExecutor_.pumpEventLoop(timeoutMillis);
    }

    /**
     * Shutdown JavaScriptExecutor.
     */
    public void shutdownJavaScriptExecutor() {
        if (javaScriptExecutor_ != null) {
            javaScriptExecutor_.shutdown();
            javaScriptExecutor_ = null;
        }
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
    public Script compile(final HtmlPage htmlPage, final String sourceCode,
                           final String sourceName, final int startLine) {

        WebAssert.notNull("sourceCode", sourceCode);

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

        return (Script) getContextFactory().call(action);
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
        if (script == null) { // happens with syntax error + throwExceptionOnScriptError = false
            return null;
        }
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

        return getContextFactory().call(action);
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
            final Function javaScriptFunction,
            final Scriptable thisObject,
            final Object [] args,
            final DomNode htmlElement) {

        final Scriptable scope = getScope(htmlPage, htmlElement);

        return callFunction(htmlPage, javaScriptFunction, scope, thisObject, args);
    }

    /**
     * Calls the given function taking care of synchronization issues.
     * @param htmlPage the HTML page that caused this script to executed
     * @param function the JavaScript function to execute
     * @param scope the execution scope
     * @param thisObject the 'this' object
     * @param args the function's arguments
     * @return the function result
     */
    public Object callFunction(final HtmlPage htmlPage, final Function function,
            final Scriptable scope, final Scriptable thisObject, final Object[] args) {

        final ContextAction action = new HtmlUnitContextAction(scope, htmlPage) {
            @Override
            public Object doRun(final Context cx) {
                return function.call(cx, scope, thisObject, args);
            }
            @Override
            protected String getSourceCode(final Context cx) {
                return cx.decompileFunction(function, 2);
            }
        };
        return getContextFactory().call(action);
    }

    private Scriptable getScope(final HtmlPage htmlPage, final DomNode htmlElement) {
        if (htmlElement != null) {
            return htmlElement.getScriptObject();
        }
        return (Window) htmlPage.getEnclosingWindow().getScriptObject();
    }

    /**
     * Indicates if JavaScript is running in current thread.<br/>
     * This allows code to know if there own evaluation is has been triggered by some JS code.
     * @return <code>true</code> if JavaScript is running
     */
    public boolean isScriptRunning() {
        return Boolean.TRUE.equals(javaScriptRunning_.get());
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
                // KEY_STARTING_SCOPE maintains a stack of scopes
                @SuppressWarnings("unchecked")
                Stack<Scriptable> stack = (Stack<Scriptable>) cx.getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
                if (null == stack) {
                    stack = new Stack<Scriptable>();
                    cx.putThreadLocal(KEY_STARTING_SCOPE, stack);
                }
                stack.push(scope_);

                final Object response;
                try {
                    cx.putThreadLocal(KEY_STARTING_PAGE, htmlPage_);
                    synchronized (htmlPage_) { // 2 scripts can't be executed in parallel for one page
                        if (htmlPage_ != htmlPage_.getEnclosingWindow().getEnclosedPage()) {
                            return null; // page has been unloaded
                        }
                        response = doRun(cx);
                    }
                }
                finally {
                    stack.pop();
                }
                // doProcessPostponedActions is synchronized
                // moved out of the sync block to avoid deadlocks
                if (!holdPostponedActions_) {
                    doProcessPostponedActions();
                }
                return response;
            }
            catch (final Exception e) {
                handleJavaScriptException(new ScriptException(htmlPage_, e, getSourceCode(cx)), true);
                return null;
            }
            catch (final TimeoutError e) {
                final JavaScriptErrorListener javaScriptErrorListener = getWebClient().getJavaScriptErrorListener();
                if (javaScriptErrorListener != null) {
                    javaScriptErrorListener.timeoutError(htmlPage_, e.getAllowedTime(), e.getExecutionTime());
                }
                if (getWebClient().getOptions().isThrowExceptionOnScriptError()) {
                    throw new RuntimeException(e);
                }
                LOG.info("Caught script timeout error", e);
                return null;
            }
            finally {
                javaScriptRunning_.set(javaScriptAlreadyRunning);
            }
        }

        protected abstract Object doRun(final Context cx);

        protected abstract String getSourceCode(final Context cx);
    }

    private synchronized void doProcessPostponedActions() {
        holdPostponedActions_ = false;

        try {
            getWebClient().loadDownloadedResponses();
        }
        catch (final RuntimeException e) {
            throw e;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }

        final List<PostponedAction> actions = postponedActions_.get();
        if (actions != null) {
            postponedActions_.set(null);
            try {
                for (final PostponedAction action : actions) {
                    // verify that the page that registered this PostponedAction is still alive
                    final Page owningPage = action.getOwningPage();
                    if (owningPage != null && owningPage == owningPage.getEnclosingWindow().getEnclosedPage()) {
                        action.execute();
                    }
                }
            }
            catch (final Exception e) {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * Adds an action that should be executed first when the script currently being executed has finished.
     * @param action the action
     */
    public void addPostponedAction(final PostponedAction action) {
        List<PostponedAction> actions = postponedActions_.get();
        if (actions == null) {
            actions = new ArrayList<PostponedAction>();
            postponedActions_.set(actions);
        }
        actions.add(action);
    }

    /**
     * Handles an exception that occurred during execution of JavaScript code.
     * @param scriptException the exception
     * @param triggerOnError if true, this triggers the onerror handler
     */
    protected void handleJavaScriptException(final ScriptException scriptException, final boolean triggerOnError) {
        // Trigger window.onerror, if it has been set.
        final HtmlPage page = scriptException.getPage();
        if (triggerOnError && page != null) {
            final WebWindow window = page.getEnclosingWindow();
            if (window != null) {
                final Window w = (Window) window.getScriptObject();
                if (w != null) {
                    try {
                        w.triggerOnError(scriptException);
                    }
                    catch (final Exception e) {
                        handleJavaScriptException(new ScriptException(page, e, null), false);
                    }
                }
            }
        }
        final JavaScriptErrorListener javaScriptErrorListener = getWebClient().getJavaScriptErrorListener();
        if (javaScriptErrorListener != null) {
            javaScriptErrorListener.scriptException(page, scriptException);
        }
        // Throw a Java exception if the user wants us to.
        if (getWebClient().getOptions().isThrowExceptionOnScriptError()) {
            throw scriptException;
        }
        // Log the error; ScriptException instances provide good debug info.
        LOG.info("Caught script exception", scriptException);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Indicates that no postponed action should be executed.
     */
    public void holdPosponedActions() {
        holdPostponedActions_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Process postponed actions, if any.
     */
    public void processPostponedActions() {
        doProcessPostponedActions();
    }

    /**
     * Re-initializes transient fields when an object of this type is deserialized.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initTransientFields();
    }

    private void initTransientFields() {
        javaScriptRunning_ = new ThreadLocal<Boolean>();
        postponedActions_ = new ThreadLocal<List<PostponedAction>>();
        holdPostponedActions_ = false;
    }

    private static class FallbackCaller extends ScriptableObject {
        private static final long serialVersionUID = 5142592186670858001L;

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
    }

    /**
     * Gets the class of the JavaScript object for the node class.
     * @param c the node class {@link DomNode} or some subclass.
     * @return <code>null</code> if none found
     */
    public Class<? extends SimpleScriptable> getJavaScriptClass(final Class<?> c) {
        return jsConfig_.getDomJavaScriptMapping().get(c);
    }

    /**
     * Gets the associated configuration.
     * @return the configuration
     */
    public JavaScriptConfiguration getJavaScriptConfiguration() {
        return jsConfig_;
    }
}
