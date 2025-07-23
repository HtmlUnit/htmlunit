/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript;

import static org.htmlunit.BrowserVersionFeatures.JS_ERROR_CAPTURE_STACK_TRACE;
import static org.htmlunit.BrowserVersionFeatures.JS_ERROR_STACK_TRACE_LIMIT;
import static org.htmlunit.BrowserVersionFeatures.JS_ITERATOR_VISIBLE_IN_WINDOW;
import static org.htmlunit.BrowserVersionFeatures.JS_WINDOW_INSTALL_TRIGGER_NULL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.Page;
import org.htmlunit.ScriptException;
import org.htmlunit.WebAssert;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Callable;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.ContextFactory;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.JavaScriptException;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.NativeArrayIterator;
import org.htmlunit.corejs.javascript.NativeConsole;
import org.htmlunit.corejs.javascript.NativeFunction;
import org.htmlunit.corejs.javascript.RhinoException;
import org.htmlunit.corejs.javascript.Script;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.StackStyle;
import org.htmlunit.corejs.javascript.Symbol;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import org.htmlunit.javascript.background.JavaScriptExecutor;
import org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import org.htmlunit.javascript.configuration.ClassConfiguration;
import org.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import org.htmlunit.javascript.configuration.ClassConfiguration.PropertyInfo;
import org.htmlunit.javascript.configuration.JavaScriptConfiguration;
import org.htmlunit.javascript.configuration.ProxyAutoConfigJavaScriptConfiguration;
import org.htmlunit.javascript.host.ConsoleCustom;
import org.htmlunit.javascript.host.NumberCustom;
import org.htmlunit.javascript.host.URLSearchParams;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.html.HTMLImageElement;
import org.htmlunit.javascript.host.html.HTMLOptionElement;
import org.htmlunit.javascript.host.intl.Intl;
import org.htmlunit.javascript.host.xml.FormData;
import org.htmlunit.javascript.polyfill.Polyfill;

/**
 * A wrapper for the <a href="http://www.mozilla.org/rhino">Rhino JavaScript engine</a>
 * that provides browser specific features.
 *
 * <p>Like all classes in this package, this class is not intended for direct use
 * and may change without notice.</p>
 *
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
 * @author Frank Danek
 * @author Lai Quang Duong
 * @author Sven Strickroth
 *
 * @see <a href="http://groups-beta.google.com/group/netscape.public.mozilla.jseng/browse_thread/thread/b4edac57329cf49f/069e9307ec89111f">
 *     Rhino and Java Browser</a>
 */
public class JavaScriptEngine implements AbstractJavaScriptEngine<Script> {

    private static final Log LOG = LogFactory.getLog(JavaScriptEngine.class);

    /** ScriptRuntime.emptyArgs. */
    public static final Object[] EMPTY_ARGS = ScriptRuntime.emptyArgs;

    /** org.htmlunit.corejs.javascript.Undefined.instance. */
    public static final Object UNDEFINED = org.htmlunit.corejs.javascript.Undefined.instance;

    private WebClient webClient_;
    private HtmlUnitContextFactory contextFactory_;
    private JavaScriptConfiguration jsConfig_;

    private transient ThreadLocal<Boolean> javaScriptRunning_;
    private transient ThreadLocal<List<PostponedAction>> postponedActions_;
    private transient boolean holdPostponedActions_;
    private transient boolean shutdownPending_;

    /** The JavaScriptExecutor corresponding to all windows of this Web client */
    private transient JavaScriptExecutor javaScriptExecutor_;

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
        if (webClient == null) {
            throw new IllegalArgumentException("JavaScriptEngine ctor requires a webClient");
        }

        webClient_ = webClient;
        contextFactory_ = new HtmlUnitContextFactory(webClient);
        initTransientFields();

        jsConfig_ = JavaScriptConfiguration.getInstance(webClient.getBrowserVersion());
        RhinoException.setStackStyle(StackStyle.MOZILLA_LF);
    }

    /**
     * Returns the web client that this engine is associated with.
     * @return the web client
     */
    private WebClient getWebClient() {
        return webClient_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlUnitContextFactory getContextFactory() {
        return contextFactory_;
    }

    /**
     * Performs initialization for the given webWindow.
     * @param webWindow the web window to initialize for
     */
    @Override
    public void initialize(final WebWindow webWindow, final Page page) {
        WebAssert.notNull("webWindow", webWindow);

        if (shutdownPending_) {
            return;
        }

        getContextFactory().call(cx -> {
            try {
                init(webWindow, page, cx);
            }
            catch (final Exception e) {
                LOG.error("Exception while initializing JavaScript for the page", e);
                throw new ScriptException(null, e); // BUG: null is not useful.
            }
            return null;
        });
    }

    /**
     * Returns the JavaScriptExecutor.
     * @return the JavaScriptExecutor or null if javascript is disabled
     *         or no executor was required so far.
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
    private void init(final WebWindow webWindow, final Page page, final Context context) throws Exception {
        final WebClient webClient = getWebClient();
        final BrowserVersion browserVersion = webClient.getBrowserVersion();

        final Window jsWindowScope = new Window();
        jsWindowScope.setClassName("Window");

        context.initSafeStandardObjects(jsWindowScope);
        configureRhino(webClient, browserVersion, jsWindowScope);

        final Map<Class<? extends Scriptable>, Scriptable> prototypes = new HashMap<>();
        final Map<String, Scriptable> prototypesPerJSName = new HashMap<>();

        final ClassConfiguration windowConfig = jsConfig_.getWindowClassConfiguration();
        final FunctionObject functionObject = new FunctionObject(jsWindowScope.getClassName(),
                        windowConfig.getJsConstructor().getValue(), jsWindowScope);
        ScriptableObject.defineProperty(jsWindowScope, "constructor", functionObject,
                ScriptableObject.DONTENUM  | ScriptableObject.PERMANENT | ScriptableObject.READONLY);

        configureConstantsPropertiesAndFunctions(windowConfig, jsWindowScope);

        final HtmlUnitScriptable windowPrototype = configureClass(windowConfig, jsWindowScope);
        jsWindowScope.setPrototype(windowPrototype);
        prototypes.put(windowConfig.getHostClass(), windowPrototype);
        prototypesPerJSName.put(windowConfig.getClassName(), windowPrototype);

        configureScope(jsWindowScope, windowConfig, functionObject, jsConfig_, browserVersion, prototypes, prototypesPerJSName);

        URLSearchParams.NativeParamsIterator.init(jsWindowScope, "URLSearchParams Iterator");
        FormData.FormDataIterator.init(jsWindowScope, "FormData Iterator");

        // strange but this is the reality for browsers
        // because there will be still some sites using this for browser detection the property is
        // set to null
        // https://stackoverflow.com/questions/9847580/how-to-detect-safari-chrome-ie-firefox-and-opera-browsers
        // https://bugzilla.mozilla.org/show_bug.cgi?id=1442035
        if (browserVersion.hasFeature(JS_WINDOW_INSTALL_TRIGGER_NULL)) {
            jsWindowScope.put("InstallTrigger", jsWindowScope, null);
        }

        // special handling for image/option
        final Method imageCtor = HTMLImageElement.class.getDeclaredMethod("jsConstructorImage");
        additionalCtor(jsWindowScope, prototypesPerJSName.get("HTMLImageElement"), imageCtor, "Image", "HTMLImageElement");
        final Method optionCtor = HTMLOptionElement.class.getDeclaredMethod("jsConstructorOption",
                new Class[] {Object.class, String.class, boolean.class, boolean.class});
        additionalCtor(jsWindowScope, prototypesPerJSName.get("HTMLOptionElement"), optionCtor, "Option", "HTMLOptionElement");

        if (!webClient.getOptions().isWebSocketEnabled()) {
            deleteProperties(jsWindowScope, "WebSocket");
        }

        jsWindowScope.setPrototypes(prototypes);
        jsWindowScope.initialize(webWindow, page);

        applyPolyfills(webClient, browserVersion, context, jsWindowScope);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param jsScope the js scope to set up
     * @param scopeConfig the {@link ClassConfiguration} that is used for the scope
     * @param scopeContructorFunctionObject the (already registered) ctor
     * @param jsConfig the complete jsConfig
     * @param browserVersion the {@link BrowserVersion}
     * @param prototypes map of prototypes
     * @param prototypesPerJSName map of prototypes with the class name as key
     * @throws Exception in case of error
     */
    public static void configureScope(final HtmlUnitScriptable jsScope,
            final ClassConfiguration scopeConfig,
            final FunctionObject scopeContructorFunctionObject,
            final AbstractJavaScriptConfiguration jsConfig,
            final BrowserVersion browserVersion,
            final Map<Class<? extends Scriptable>, Scriptable> prototypes,
            final Map<String, Scriptable> prototypesPerJSName) throws Exception {

        final Scriptable objectPrototype = ScriptableObject.getObjectPrototype(jsScope);

        final Map<String, Function> ctorPrototypesPerJSName = new HashMap<>();
        for (final ClassConfiguration config : jsConfig.getAll()) {
            final String jsClassName = config.getClassName();
            Scriptable prototype = prototypesPerJSName.get(jsClassName);
            final String extendedClassName =
                    StringUtils.isEmpty(config.getExtendedClassName()) ? null : config.getExtendedClassName();

            // setup the prototypes
            if (config == scopeConfig) {
                if (extendedClassName == null) {
                    prototype.setPrototype(objectPrototype);
                }
                else {
                    prototype.setPrototype(prototypesPerJSName.get(extendedClassName));
                }

                // setup constructors
                addAsConstructorAndAlias(scopeContructorFunctionObject, jsScope, prototype, config);
                configureConstantsStaticPropertiesAndStaticFunctions(config, scopeContructorFunctionObject);

                // adjust prototype if needed
                if (extendedClassName != null) {
                    scopeContructorFunctionObject.setPrototype(ctorPrototypesPerJSName.get(extendedClassName));
                }
            }
            else {
                final HtmlUnitScriptable classPrototype = configureClass(config, jsScope);
                prototypes.put(config.getHostClass(), classPrototype);
                prototypesPerJSName.put(jsClassName, classPrototype);
                prototype = classPrototype;

                if (extendedClassName == null) {
                    classPrototype.setPrototype(objectPrototype);
                }
                else {
                    classPrototype.setPrototype(prototypesPerJSName.get(extendedClassName));
                }

                // setup constructors
                if (prototype != null && config.isJsObject()) {
                    final Map.Entry<String, Member> jsConstructor = config.getJsConstructor();
                    final FunctionObject function = new FunctionObject(jsConstructor.getKey(), jsConstructor.getValue(), jsScope);
                    ctorPrototypesPerJSName.put(jsClassName, function);

                    addAsConstructorAndAlias(function, jsScope, prototype, config);
                    configureConstantsStaticPropertiesAndStaticFunctions(config, function);

                    // adjust prototype if needed
                    if (extendedClassName != null) {
                        function.setPrototype(ctorPrototypesPerJSName.get(extendedClassName));
                    }
                }
            }
        }
    }

    private static void addAsConstructorAndAlias(final FunctionObject function,
            final Scriptable scope, final Scriptable prototype, final ClassConfiguration config) {
        try {
            function.addAsConstructor(scope, prototype, ScriptableObject.DONTENUM);

            final String alias = config.getJsConstructorAlias();
            if (alias != null) {
                ScriptableObject.defineProperty(scope, alias, function, ScriptableObject.DONTENUM);
            }
        }
        catch (final Exception e) {
            // TODO see issue #1897
            if (LOG.isWarnEnabled()) {
                final String newline = System.lineSeparator();
                LOG.warn("Error during JavaScriptEngine.init(WebWindow, Context)" + newline
                        + e.getMessage() + newline
                        + "prototype: " + prototype.getClassName(), e);
            }
        }
    }

    private static void additionalCtor(final Window window, final Scriptable proto,
            final Method ctorMethod, final String prop, final String clazzName) throws Exception {
        final FunctionObject function = new FunctionObject(prop, ctorMethod, window);
        final Object prototypeProperty = ScriptableObject.getProperty(window, clazzName);
        try {
            function.addAsConstructor(window, proto, ScriptableObject.DONTENUM);
        }
        catch (final Exception e) {
            // TODO see issue #1897
            if (LOG.isWarnEnabled()) {
                final String newline = System.lineSeparator();
                LOG.warn("Error during JavaScriptEngine.init(WebWindow, Context)" + newline
                        + e.getMessage() + newline
                        + "prototype: " + proto.getClassName(), e);
            }
        }
        ScriptableObject.defineProperty(window, prop, function, ScriptableObject.DONTENUM);
        ScriptableObject.defineProperty(window, clazzName, prototypeProperty, ScriptableObject.DONTENUM);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param webClient the WebClient
     * @param browserVersion the BrowserVersion
     * @param scope the window or the DedicatedWorkerGlobalScope
     */
    public static void configureRhino(final WebClient webClient,
            final BrowserVersion browserVersion, final HtmlUnitScriptable scope) {

        NativeConsole.init(scope, false, webClient.getWebConsole());
        final ScriptableObject console = (ScriptableObject) ScriptableObject.getProperty(scope, "console");
        console.defineFunctionProperties(new String[] {"timeStamp"}, ConsoleCustom.class, ScriptableObject.DONTENUM);

        // Rhino defines too much methods for us, particularly since implementation of ECMAScript5
        final ScriptableObject stringPrototype = (ScriptableObject) ScriptableObject.getClassPrototype(scope, "String");
        deleteProperties(stringPrototype, "equals", "equalsIgnoreCase", "toSource");

        final ScriptableObject numberPrototype = (ScriptableObject) ScriptableObject.getClassPrototype(scope, "Number");
        deleteProperties(numberPrototype, "toSource");
        final ScriptableObject datePrototype = (ScriptableObject) ScriptableObject.getClassPrototype(scope, "Date");
        deleteProperties(datePrototype, "toSource");

        deleteProperties(scope, "uneval");
        removePrototypeProperties(scope, "Object", "toSource");
        removePrototypeProperties(scope, "Array", "toSource");
        removePrototypeProperties(scope, "Function", "toSource");

        deleteProperties(scope, "isXMLName");

        NativeFunctionToStringFunction.installFix(scope, browserVersion);

        numberPrototype.defineFunctionProperties(new String[] {"toLocaleString"},
                NumberCustom.class, ScriptableObject.DONTENUM);

        // remove some objects, that Rhino defines in top scope but that we don't want
        deleteProperties(scope, "Continuation", "StopIteration");
        if (!browserVersion.hasFeature(JS_ITERATOR_VISIBLE_IN_WINDOW)) {
            deleteProperties(scope, "Iterator");
        }

        final ScriptableObject errorObject = (ScriptableObject) ScriptableObject.getProperty(scope, "Error");
        if (browserVersion.hasFeature(JS_ERROR_STACK_TRACE_LIMIT)) {
            errorObject.defineProperty("stackTraceLimit", 10, ScriptableObject.EMPTY);
        }
        else {
            ScriptableObject.deleteProperty(errorObject, "stackTraceLimit");
        }
        if (!browserVersion.hasFeature(JS_ERROR_CAPTURE_STACK_TRACE)) {
            ScriptableObject.deleteProperty(errorObject, "captureStackTrace");
        }

        // add Intl
        final Intl intl = new Intl();
        intl.setParentScope(scope);
        scope.defineProperty(intl.getClassName(), intl, ScriptableObject.DONTENUM);
        intl.defineProperties(browserVersion);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param webClient the WebClient
     * @param browserVersion the BrowserVersion
     * @param context the current context
     * @param scriptable the window or the DedicatedWorkerGlobalScope
     * @throws IOException in case of problems
     */
    public static void applyPolyfills(final WebClient webClient, final BrowserVersion browserVersion,
            final Context context, final HtmlUnitScriptable scriptable) throws IOException {

        if (webClient.getOptions().isFetchPolyfillEnabled()) {
            Polyfill.getFetchPolyfill().apply(context, scriptable);
        }
    }

    /**
     * Deletes the properties with the provided names.
     * @param scope the scope from which properties have to be removed
     * @param propertiesToDelete the list of property names
     */
    private static void deleteProperties(final Scriptable scope, final String... propertiesToDelete) {
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
    private static void removePrototypeProperties(final Scriptable scope, final String className,
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
     * @throws Exception in case of errors
     */
    public static HtmlUnitScriptable configureClass(final ClassConfiguration config, final Scriptable window)
        throws Exception {

        final HtmlUnitScriptable prototype = config.getHostClass().getDeclaredConstructor().newInstance();
        prototype.setParentScope(window);
        prototype.setClassName(config.getClassName());

        configureConstantsPropertiesAndFunctions(config, prototype);

        return prototype;
    }

    /**
     * Configures constants, static properties and static functions on the object.
     * @param config the configuration for the object
     * @param scriptable the object to configure
     */
    private static void configureConstantsStaticPropertiesAndStaticFunctions(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        configureConstants(config, scriptable);
        configureStaticProperties(config, scriptable);
        configureStaticFunctions(config, scriptable);
    }

    /**
     * Configures constants, properties and functions on the object.
     * @param config the configuration for the object
     * @param scriptable the object to configure
     */
    private static void configureConstantsPropertiesAndFunctions(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        configureConstants(config, scriptable);
        configureProperties(config, scriptable);
        configureFunctions(config, scriptable);
        configureSymbolConstants(config, scriptable);
        configureSymbols(config, scriptable);
    }

    private static void configureFunctions(final ClassConfiguration config, final ScriptableObject scriptable) {
        // the functions
        final Map<String, Method> functionMap = config.getFunctionMap();
        if (functionMap != null) {
            for (final Entry<String, Method> functionInfo : functionMap.entrySet()) {
                final String functionName = functionInfo.getKey();
                final Method method = functionInfo.getValue();
                final FunctionObject functionObject = new FunctionObject(functionName, method, scriptable);
                scriptable.defineProperty(functionName, functionObject, ScriptableObject.EMPTY);
            }
        }
    }

    private static void configureConstants(final ClassConfiguration config, final ScriptableObject scriptable) {
        final List<ConstantInfo> constants = config.getConstants();
        if (constants != null) {
            for (final ConstantInfo constantInfo : constants) {
                scriptable.defineProperty(constantInfo.getName(), constantInfo.getValue(), constantInfo.getFlag());
            }
        }
    }

    private static void configureProperties(final ClassConfiguration config, final ScriptableObject scriptable) {
        final Map<String, PropertyInfo> propertyMap = config.getPropertyMap();
        if (propertyMap != null) {
            for (final Entry<String, PropertyInfo> propertyEntry : propertyMap.entrySet()) {
                final PropertyInfo info = propertyEntry.getValue();
                final Method readMethod = info.getReadMethod();
                final Method writeMethod = info.getWriteMethod();
                scriptable.defineProperty(propertyEntry.getKey(), null, readMethod, writeMethod, ScriptableObject.EMPTY);
            }
        }
    }

    private static void configureStaticProperties(final ClassConfiguration config, final ScriptableObject scriptable) {
        final Map<String, PropertyInfo> staticPropertyMap = config.getStaticPropertyMap();
        if (staticPropertyMap != null) {
            for (final Entry<String, ClassConfiguration.PropertyInfo> propertyEntry : staticPropertyMap.entrySet()) {
                final String propertyName = propertyEntry.getKey();
                final Method readMethod = propertyEntry.getValue().getReadMethod();
                final Method writeMethod = propertyEntry.getValue().getWriteMethod();
                final int flag = ScriptableObject.EMPTY;

                scriptable.defineProperty(propertyName, null, readMethod, writeMethod, flag);
            }
        }
    }

    private static void configureStaticFunctions(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        final Map<String, Method> staticFunctionMap = config.getStaticFunctionMap();
        if (staticFunctionMap != null) {
            for (final Entry<String, Method> staticFunctionInfo : staticFunctionMap.entrySet()) {
                final String functionName = staticFunctionInfo.getKey();
                final Method method = staticFunctionInfo.getValue();
                final FunctionObject staticFunctionObject = new FunctionObject(functionName, method,
                        scriptable);
                scriptable.defineProperty(functionName, staticFunctionObject, ScriptableObject.EMPTY);
            }
        }
    }

    private static void configureSymbolConstants(final ClassConfiguration config, final ScriptableObject scriptable) {
        final Map<Symbol, String> symbolConstantMap = config.getSymbolConstantMap();
        if (symbolConstantMap != null) {
            for (final Entry<Symbol, String> symbolInfo : symbolConstantMap.entrySet()) {
                scriptable.defineProperty(symbolInfo.getKey(), symbolInfo.getValue(), ScriptableObject.DONTENUM | ScriptableObject.READONLY);
            }
        }
    }

    private static void configureSymbols(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        final Map<Symbol, Method> symbolMap = config.getSymbolMap();
        if (symbolMap != null) {
            for (final Entry<Symbol, Method> symbolInfo : symbolMap.entrySet()) {
                final Symbol symbol = symbolInfo.getKey();
                final Method method = symbolInfo.getValue();
                final String methodName = method.getName();

                final Callable symbolFunction = scriptable.has(methodName, scriptable)
                        ? (Callable) scriptable.get(methodName, scriptable)
                        : new FunctionObject(methodName, method, scriptable);
                scriptable.defineProperty(symbol, symbolFunction, ScriptableObject.DONTENUM);
            }
        }
    }

    /**
     * Register WebWindow with the JavaScriptExecutor.
     * @param webWindow the WebWindow to be registered.
     */
    @Override
    public synchronized void registerWindowAndMaybeStartEventLoop(final WebWindow webWindow) {
        if (shutdownPending_) {
            return;
        }

        final WebClient webClient = getWebClient();
        if (webClient != null) {
            if (javaScriptExecutor_ == null) {
                javaScriptExecutor_ = BackgroundJavaScriptFactory.theFactory().createJavaScriptExecutor(webClient);
            }
            javaScriptExecutor_.addWindow(webWindow);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareShutdown() {
        shutdownPending_ = true;
    }

    /**
     * Shutdown the JavaScriptEngine.
     */
    @Override
    public void shutdown() {
        webClient_ = null;
        contextFactory_ = null;
        jsConfig_ = null;

        if (javaScriptExecutor_ != null) {
            javaScriptExecutor_.shutdown();
            javaScriptExecutor_ = null;
        }
        if (postponedActions_ != null) {
            postponedActions_.remove();
        }
        if (javaScriptRunning_ != null) {
            javaScriptRunning_.remove();
        }
        holdPostponedActions_ = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Script compile(final HtmlPage owningPage, final Scriptable scope, final String sourceCode,
            final String sourceName, final int startLine) {
        WebAssert.notNull("sourceCode", sourceCode);

        if (LOG.isTraceEnabled()) {
            final String newline = System.lineSeparator();
            LOG.trace("Javascript compile " + sourceName + newline + sourceCode + newline);
        }

        final HtmlUnitCompileContextAction action = new HtmlUnitCompileContextAction(owningPage, sourceCode, sourceName, startLine);
        return (Script) getContextFactory().callSecured(action, owningPage);
    }

    /**
     * Forwards this to the {@link HtmlUnitContextFactory} but with checking shutdown handling.
     *
     * @param <T> return type of the action
     * @param action the contextAction
     * @param page the page
     * @return the result of the call
     */
    public final <T> T callSecured(final ContextAction<T> action, final HtmlPage page) {
        if (shutdownPending_ || webClient_ == null) {
            // shutdown was already called
            return null;
        }

        return getContextFactory().callSecured(action, page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(final HtmlPage page,
                           final Scriptable scope,
                           final String sourceCode,
                           final String sourceName,
                           final int startLine) {
        final Script script = compile(page, scope, sourceCode, sourceName, startLine);
        if (script == null) {
            // happens with syntax error + throwExceptionOnScriptError = false
            return null;
        }
        return execute(page, scope, script);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(final HtmlPage page, final Scriptable scope, final Script script) {
        if (shutdownPending_ || webClient_ == null) {
            // shutdown was already called
            return null;
        }

        final HtmlUnitContextAction action = new HtmlUnitContextAction(page) {
            @Override
            public Object doRun(final Context cx) {
                return script.exec(cx, scope);
            }

            @Override
            protected String getSourceCode(final Context cx) {
                return null;
            }
        };

        return getContextFactory().callSecured(action, page);
    }

    /**
     * Calls a JavaScript function and return the result.
     * @param page the page
     * @param javaScriptFunction the function to call
     * @param thisObject the this object for class method calls
     * @param args the list of arguments to pass to the function
     * @param node the HTML element that will act as the context
     * @return the result of the function call
     */
    public Object callFunction(
            final HtmlPage page,
            final Function javaScriptFunction,
            final Scriptable thisObject,
            final Object[] args,
            final DomNode node) {

        final Scriptable scope = getScope(page, node);

        return callFunction(page, javaScriptFunction, scope, thisObject, args);
    }

    /**
     * Calls the given function taking care of synchronization issues.
     * @param page the interactive page that caused this script to executed
     * @param function the JavaScript function to execute
     * @param scope the execution scope
     * @param thisObject the 'this' object
     * @param args the function's arguments
     * @return the function result
     */
    public Object callFunction(final HtmlPage page, final Function function,
            final Scriptable scope, final Scriptable thisObject, final Object[] args) {
        if (shutdownPending_ || webClient_ == null) {
            // shutdown was already called
            return null;
        }

        final HtmlUnitContextAction action = new HtmlUnitContextAction(page) {
            @Override
            public Object doRun(final Context cx) {
                if (ScriptRuntime.hasTopCall(cx)) {
                    return function.call(cx, scope, thisObject, args);
                }
                return ScriptRuntime.doTopCall(function, cx, scope, thisObject, args, cx.isStrictMode());
            }
            @Override
            protected String getSourceCode(final Context cx) {
                return cx.decompileFunction(function, 2);
            }
        };
        return getContextFactory().callSecured(action, page);
    }

    private static Scriptable getScope(final HtmlPage page, final DomNode node) {
        if (node != null) {
            return node.getScriptableObject();
        }
        return page.getEnclosingWindow().getScriptableObject();
    }

    /**
     * Indicates if JavaScript is running in current thread.
     * <p>This allows code to know if there own evaluation is has been triggered by some JS code.
     * @return {@code true} if JavaScript is running
     */
    @Override
    public boolean isScriptRunning() {
        return Boolean.TRUE.equals(javaScriptRunning_.get());
    }

    /**
     * Special ContextAction only for compiling. This reduces some code and avoid
     * some calls.
     */
    private final class HtmlUnitCompileContextAction implements ContextAction<Object> {
        private final HtmlPage page_;
        private final String sourceCode_;
        private final String sourceName_;
        private final int startLine_;

        HtmlUnitCompileContextAction(final HtmlPage page, final String sourceCode, final String sourceName, final int startLine) {
            page_ = page;
            sourceCode_ = sourceCode;
            sourceName_ = sourceName;
            startLine_ = startLine;
        }

        @Override
        public Object run(final Context cx) {
            try {
                final Object response;
                cx.putThreadLocal(KEY_STARTING_PAGE, page_);
                synchronized (page_) { // 2 scripts can't be executed in parallel for one page
                    if (page_ != page_.getEnclosingWindow().getEnclosedPage()) {
                        return null; // page has been unloaded
                    }
                    response = cx.compileString(sourceCode_, sourceName_, startLine_, null);

                }

                return response;
            }
            catch (final Exception e) {
                handleJavaScriptException(new ScriptException(page_, e, sourceCode_), true);
                return null;
            }
            catch (final TimeoutError e) {
                handleJavaScriptTimeoutError(page_, e);
                return null;
            }
        }
    }

    /**
     * Facility for ContextAction usage.
     * ContextAction should be preferred because according to Rhino doc it
     * "guarantees proper association of Context instances with the current thread and is faster".
     */
    private abstract class HtmlUnitContextAction implements ContextAction<Object> {
        private final HtmlPage page_;

        HtmlUnitContextAction(final HtmlPage page) {
            page_ = page;
        }

        @Override
        public final Object run(final Context cx) {
            final Boolean javaScriptAlreadyRunning = javaScriptRunning_.get();
            javaScriptRunning_.set(Boolean.TRUE);

            try {
                final Object response;
                synchronized (page_) { // 2 scripts can't be executed in parallel for one page
                    if (page_ != page_.getEnclosingWindow().getEnclosedPage()) {
                        return null; // page has been unloaded
                    }
                    response = doRun(cx);
                }

                cx.processMicrotasks();

                // doProcessPostponedActions is synchronized
                // moved out of the sync block to avoid deadlocks
                if (!holdPostponedActions_) {
                    doProcessPostponedActions();
                }

                return response;
            }
            catch (final Exception e) {
                handleJavaScriptException(new ScriptException(page_, e, getSourceCode(cx)), true);
                return null;
            }
            catch (final TimeoutError e) {
                handleJavaScriptTimeoutError(page_, e);
                return null;
            }
            finally {
                javaScriptRunning_.set(javaScriptAlreadyRunning);
            }
        }

        protected abstract Object doRun(Context cx);

        protected abstract String getSourceCode(Context cx);
    }

    private void doProcessPostponedActions() {
        holdPostponedActions_ = false;

        final WebClient webClient = getWebClient();
        if (webClient == null) {
            // shutdown was already called
            postponedActions_.set(null);
            return;
        }

        try {
            webClient.loadDownloadedResponses();
        }
        catch (final RuntimeException e) {
            throw e;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }

        final List<PostponedAction> actions = postponedActions_.get();
        if (actions != null && actions.size() > 0) {
            postponedActions_.set(new ArrayList<>());
            try {
                for (final PostponedAction action : actions) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Processing PostponedAction " + action);
                    }

                    // verify that the page that registered this PostponedAction is still alive
                    if (action.isStillAlive()) {
                        action.execute();
                    }
                }
            }
            catch (final RuntimeException e) {
                throw e;
            }
            catch (final Exception e) {
                throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * Adds an action that should be executed first when the script currently being executed has finished.
     * @param action the action
     */
    @Override
    public void addPostponedAction(final PostponedAction action) {
        if (shutdownPending_) {
            return;
        }

        List<PostponedAction> actions = postponedActions_.get();
        if (actions == null) {
            actions = new ArrayList<>();
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
        final WebClient webClient = getWebClient();
        if (shutdownPending_ || webClient == null) {
            // shutdown was already called
            return;
        }

        // Trigger window.onerror, if it has been set.
        final HtmlPage page = scriptException.getPage();
        if (triggerOnError && page != null) {
            final WebWindow window = page.getEnclosingWindow();
            if (window != null) {
                final Window w = window.getScriptableObject();
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

        webClient.getJavaScriptErrorListener().scriptException(page, scriptException);
        // Throw a Java exception if the user wants us to.
        if (webClient.getOptions().isThrowExceptionOnScriptError()) {
            throw scriptException;
        }
    }

    /**
     * Handles an exception that occurred during execution of JavaScript code.
     * @param page the page in which the script causing this exception was executed
     * @param e the timeout error that was thrown from the script engine
     */
    protected void handleJavaScriptTimeoutError(final HtmlPage page, final TimeoutError e) {
        final WebClient webClient = getWebClient();
        if (shutdownPending_ || webClient == null) {
            // shutdown was already called
            return;
        }

        webClient.getJavaScriptErrorListener().timeoutError(page, e.getAllowedTime(), e.getExecutionTime());
        if (webClient.getOptions().isThrowExceptionOnScriptError()) {
            throw new RuntimeException(e);
        }
        LOG.info("Caught script timeout error", e);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * Indicates that no postponed action should be executed.
     */
    @Override
    public void holdPosponedActions() {
        holdPostponedActions_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * Process postponed actions, if any.
     */
    @Override
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
        javaScriptRunning_ = new ThreadLocal<>();
        postponedActions_ = new ThreadLocal<>();
        holdPostponedActions_ = false;
        shutdownPending_ = false;
    }

    /**
     * Gets the class of the JavaScript object for the node class.
     * @param c the node class {@link DomNode} or some subclass.
     * @return {@code null} if none found
     */
    public Class<? extends HtmlUnitScriptable> getJavaScriptClass(final Class<?> c) {
        return jsConfig_.getDomJavaScriptMappingFor(c);
    }

    /**
     * Gets the associated configuration.
     * @return the configuration
     */
    @Override
    public JavaScriptConfiguration getJavaScriptConfiguration() {
        return jsConfig_;
    }

    /**
     * Returns the javascript timeout.
     * @return the javascript timeout
     */
    @Override
    public long getJavaScriptTimeout() {
        return getContextFactory().getTimeout();
    }

    /**
     * Sets the javascript timeout.
     * @param timeout the timeout
     */
    @Override
    public void setJavaScriptTimeout(final long timeout) {
        getContextFactory().setTimeout(timeout);
    }

    /**
     * Convert the value to a JavaScript Number value.
     *
     * @param value a JavaScript value
     * @return the corresponding double value converted using the ECMA rules
     */
    public static double toNumber(final Object value) {
        return ScriptRuntime.toNumber(value);
    }

    /**
     * Convert the value to a JavaScript String value.
     *
     * @param value a JavaScript value
     * @return the corresponding String value converted using the ECMA rules
     */
    public static String toString(final Object value) {
        return ScriptRuntime.toString(value);
    }

    /**
     * Convert the value to a JavaScript boolean value.
     *
     * @param value a JavaScript value
     * @return the corresponding boolean value converted using the ECMA rules
     */
    public static boolean toBoolean(final Object value) {
        return ScriptRuntime.toBoolean(value);
    }

    /**
     * Rethrow the exception wrapping it as the script runtime exception.
     *
     * @param e the exception to rethrow
     * @return RuntimeException as dummy the method always throws
     */
    public static RuntimeException throwAsScriptRuntimeEx(final Throwable e) {
        throw Context.throwAsScriptRuntimeEx(e);
    }

    /**
     * Report a runtime error using the error reporter for the current thread.
     *
     * @param message the error message to report
     * @return RuntimeException as dummy the method always throws
     */
    public static RuntimeException reportRuntimeError(final String message) {
        throw Context.reportRuntimeError(message);
    }

    /**
     * Report a runtime error using the error reporter for the current thread.
     *
     * @param message the error message to report
     * @return EcmaError
     */
    public static EcmaError syntaxError(final String message) {
        return ScriptRuntime.syntaxError(message);
    }

    /**
     * Report a runtime error using the error reporter for the current thread.
     *
     * @param message the error message to report
     * @return EcmaError
     */
    public static EcmaError typeError(final String message) {
        return ScriptRuntime.typeError(message);
    }

    /**
     * Report a TypeError with the message "Illegal constructor.".
     *
     * @return EcmaError
     */
    public static EcmaError typeErrorIllegalConstructor() {
        throw JavaScriptEngine.typeError("Illegal constructor.");
    }

    /**
     * Report a runtime error using the error reporter for the current thread.
     *
     * @param message the error message to report
     * @return EcmaError
     */
    public static EcmaError rangeError(final String message) {
        return ScriptRuntime.rangeError(message);
    }

    /**
     * @param error the error
     * @param message the message
     * @return a new EcmaError
     */
    public static EcmaError constructError(final String error, final String message) {
        return ScriptRuntime.constructError(error, message);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Creates a {@link DOMException} and encapsulates it into a Rhino-compatible exception.
     *
     * @param scope the parent scope
     * @param message the exception message
     * @param type the exception type
     * @return the created exception
     */
    public static RhinoException asJavaScriptException(final HtmlUnitScriptable scope, final String message, final int type) {
        final DOMException domException = new DOMException(message, type);
        domException.setParentScope(scope);
        domException.setPrototype(scope.getPrototype(DOMException.class));

        final EcmaError helper = ScriptRuntime.syntaxError("helper");
        String fileName = helper.sourceName();
        if (fileName != null) {
            fileName = fileName.replaceFirst("script in (.*) from .*", "$1");
        }
        domException.setLocation(fileName, helper.lineNumber());

        return new JavaScriptException(domException, fileName, helper.lineNumber());
    }

    /**
     * Create an array with a specified initial length.
     *
     * @param scope the scope to create the object in
     * @param length the initial length (JavaScript arrays may have additional properties added
     *     dynamically).
     * @return the new array object
     */
    public static Scriptable newArray(final Scriptable scope, final int length) {
        final NativeArray result = new NativeArray(length);
        ScriptRuntime.setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.Array);
        return result;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Create a new ArrayIterator of type NativeArrayIterator.ARRAY_ITERATOR_TYPE.KEYS
     *
     * @param scope the scope to create the object in
     * @param arrayLike the backend
     * @return the new NativeArrayIterator
     */
    public static Scriptable newArrayIteratorTypeKeys(final Scriptable scope, final Scriptable arrayLike) {
        return new NativeArrayIterator(scope, arrayLike, NativeArrayIterator.ARRAY_ITERATOR_TYPE.KEYS);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Create a new ArrayIterator of type NativeArrayIterator.ARRAY_ITERATOR_TYPE.VALUES
     *
     * @param scope the scope to create the object in
     * @param arrayLike the backend
     * @return the new NativeArrayIterator
     */
    public static Scriptable newArrayIteratorTypeValues(final Scriptable scope, final Scriptable arrayLike) {
        return new NativeArrayIterator(scope, arrayLike, NativeArrayIterator.ARRAY_ITERATOR_TYPE.VALUES);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Create a new ArrayIterator of type NativeArrayIterator.ARRAY_ITERATOR_TYPE.ENTRIES
     *
     * @param scope the scope to create the object in
     * @param arrayLike the backend
     * @return the new NativeArrayIterator
     */
    public static Scriptable newArrayIteratorTypeEntries(final Scriptable scope, final Scriptable arrayLike) {
        return new NativeArrayIterator(scope, arrayLike, NativeArrayIterator.ARRAY_ITERATOR_TYPE.ENTRIES);
    }

    /**
     * Create an array with a specified initial length.
     *
     * @param scope the scope to create the object in
     * @param elements the initial elements. Each object in this array must be an acceptable
     *     JavaScript type and type of array should be exactly Object[], not SomeObjectSubclass[].
     * @return the new array object
     */
    public static Scriptable newArray(final Scriptable scope, final Object[] elements) {
        if (elements.getClass().getComponentType() != ScriptRuntime.ObjectClass) {
            throw new IllegalArgumentException();
        }

        final NativeArray result = new NativeArray(elements);
        ScriptRuntime.setBuiltinProtoAndParent(result, scope, TopLevel.Builtins.Array);
        return result;
    }

    /**
     * @param o the object to convert
     * @return int value
     */
    public static int toInt32(final Object o) {
        return ScriptRuntime.toInt32(o);
    }

    /**
     * @param o the object to convert
     * @return double value
     */
    public static double toInteger(final Object o) {
        return ScriptRuntime.toInteger(o);
    }

    /**
     * @param args an array
     * @param index the index in the array
     * @return double value
     */
    public static double toInteger(final Object[] args, final int index) {
        return ScriptRuntime.toInteger(args, index);
    }

    /**
     * @param obj the value to check
     * @return whether obj is undefined
     */
    public static boolean isUndefined(final Object obj) {
        return org.htmlunit.corejs.javascript.Undefined.isUndefined(obj);
    }

    /**
     * @param obj the value to check
     * @return whether obj is NAN
     */
    public static boolean isNaN(final Object obj) {
        return ScriptRuntime.isNaN(obj);
    }

    /**
     * @return the top call scope
     */
    public static Scriptable getTopCallScope() {
        return ScriptRuntime.getTopCallScope(Context.getCurrentContext());
    }

    /**
     * Tries to uncompress the JavaScript code in the provided response.
     * @param scriptSource the souce
     * @param scriptName the name
     * @return the uncompressed JavaScript code
     */
    public static String uncompressJavaScript(final String scriptSource, final String scriptName) {
        final ContextFactory factory = new ContextFactory();
        final ContextAction<Object> action = cx -> {
            cx.setInterpretedMode(true);
            final Script script = cx.compileString(scriptSource, scriptName, 0, null);
            return cx.decompileScript(script, 4);
        };

        return (String) factory.call(action);
    }

    /**
     * Evaluates the <code>FindProxyForURL</code> method of the specified content.
     * @param browserVersion the browser version to use
     * @param content the JavaScript content
     * @param url the URL to be retrieved
     * @return semicolon-separated result
     */
    public static String evaluateProxyAutoConfig(final BrowserVersion browserVersion, final String content, final URL url) {
        try (Context cx = Context.enter()) {
            final ProxyAutoConfigJavaScriptConfiguration jsConfig =
                    ProxyAutoConfigJavaScriptConfiguration.getInstance(browserVersion);

            final ScriptableObject scope = cx.initSafeStandardObjects();

            for (final ClassConfiguration config : jsConfig.getAll()) {
                configureFunctions(config, scope);
            }

            cx.evaluateString(scope, "var ProxyConfig = function() {}; ProxyConfig.bindings = {}", "<init>", 1, null);
            cx.evaluateString(scope, content, "<Proxy Auto-Config>", 1, null);

            final Object[] functionArgs = {url.toExternalForm(), url.getHost()};
            final NativeFunction f = (NativeFunction) scope.get("FindProxyForURL", scope);
            final Object result = f.call(cx, scope, scope, functionArgs);
            return toString(result);
        }
    }
}
