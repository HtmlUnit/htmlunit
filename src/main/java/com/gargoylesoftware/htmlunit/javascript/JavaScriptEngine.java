/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ARRAY_FROM;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ERROR_CAPTURE_STACK_TRACE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ERROR_STACK_TRACE_LIMIT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_FORM_DATA_ITERATOR_SIMPLE_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_PROTOTYPE_SAME_AS_HTML_IMAGE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INTL_NAMED_OBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OBJECT_GET_OWN_PROPERTY_SYMBOLS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_REFLECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_REFLECT_NAMED_OBJECT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SYMBOL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_URL_SEARCH_PARMS_ITERATOR_SIMPLE_NAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_ACTIVEXOBJECT_HIDDEN;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STRING_INCLUDES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STRING_REPEAT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STRING_STARTS_ENDS_WITH;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.STRING_TRIM_LEFT_RIGHT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.PropertyInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.javascript.host.DateCustom;
import com.gargoylesoftware.htmlunit.javascript.host.NumberCustom;
import com.gargoylesoftware.htmlunit.javascript.host.Reflect;
import com.gargoylesoftware.htmlunit.javascript.host.URLSearchParams;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.intl.Intl;
import com.gargoylesoftware.htmlunit.javascript.host.xml.FormData;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Symbol;
import net.sourceforge.htmlunit.corejs.javascript.UniqueTag;

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
 * @see <a href="http://groups-beta.google.com/group/netscape.public.mozilla.jseng/browse_thread/thread/b4edac57329cf49f/069e9307ec89111f">
 * Rhino and Java Browser</a>
 */
public class JavaScriptEngine implements AbstractJavaScriptEngine<Script> {

    private static final Log LOG = LogFactory.getLog(JavaScriptEngine.class);

    private WebClient webClient_;
    private final HtmlUnitContextFactory contextFactory_;
    private final JavaScriptConfiguration jsConfig_;

    private transient ThreadLocal<Boolean> javaScriptRunning_;
    private transient ThreadLocal<List<PostponedAction>> postponedActions_;
    private transient boolean holdPostponedActions_;

    /** The JavaScriptExecutor corresponding to all windows of this Web client */
    private transient JavaScriptExecutor javaScriptExecutor_;

    /**
     * Key used to place the scope in which the execution of some JavaScript code
     * started as thread local attribute in current context.
     * <p>This is needed to resolve some relative locations relatively to the page
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
        if (webClient == null) {
            throw new IllegalArgumentException("JavaScriptEngine ctor requires a webClient");
        }

        webClient_ = webClient;
        contextFactory_ = new HtmlUnitContextFactory(webClient);
        initTransientFields();

        jsConfig_ = JavaScriptConfiguration.getInstance(webClient.getBrowserVersion());
        RhinoException.useMozillaStackStyle(true);
    }

    /**
     * Returns the web client that this engine is associated with.
     * @return the web client
     */
    private WebClient getWebClient() {
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
    @Override
    public void initialize(final WebWindow webWindow, final Page page) {
        WebAssert.notNull("webWindow", webWindow);

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
     *  or no executor was required so far.
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
        final WebClient webClient = webWindow.getWebClient();
        final BrowserVersion browserVersion = webClient.getBrowserVersion();

        final Window window = new Window();
        ((SimpleScriptable) window).setClassName("Window");
        context.initSafeStandardObjects(window);

        final ClassConfiguration windowConfig = jsConfig_.getClassConfiguration("Window");
        if (windowConfig.getJsConstructor() != null) {
            final FunctionObject functionObject = new RecursiveFunctionObject("Window",
                    windowConfig.getJsConstructor(), window);
            ScriptableObject.defineProperty(window, "constructor", functionObject,
                    ScriptableObject.DONTENUM  | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        }
        else {
            defineConstructor(window, window, new Window());
        }

        // remove some objects, that Rhino defines in top scope but that we don't want
        deleteProperties(window, "Continuation");
        deleteProperties(window, "Iterator", "StopIteration");

        if (!browserVersion.hasFeature(JS_SYMBOL)) {
            deleteProperties(window, "Symbol");
        }

        final ScriptableObject errorObject = (ScriptableObject) ScriptableObject.getProperty(window, "Error");
        if (browserVersion.hasFeature(JS_ERROR_STACK_TRACE_LIMIT)) {
            errorObject.defineProperty("stackTraceLimit", 10, ScriptableObject.EMPTY);
        }
        else {
            ScriptableObject.deleteProperty(errorObject, "stackTraceLimit");
        }
        if (!browserVersion.hasFeature(JS_ERROR_CAPTURE_STACK_TRACE)) {
            ScriptableObject.deleteProperty(errorObject, "captureStackTrace");
        }

        if (browserVersion.hasFeature(JS_URL_SEARCH_PARMS_ITERATOR_SIMPLE_NAME)) {
            URLSearchParams.NativeParamsIterator.init(window, "Iterator");
        }
        else {
            URLSearchParams.NativeParamsIterator.init(window, "URLSearchParams Iterator");
        }
        if (browserVersion.hasFeature(JS_FORM_DATA_ITERATOR_SIMPLE_NAME)) {
            FormData.FormDataIterator.init(window, "Iterator");
        }
        else {
            FormData.FormDataIterator.init(window, "FormData Iterator");
        }

        final Intl intl = new Intl();
        intl.setParentScope(window);
        window.defineProperty(intl.getClassName(), intl, ScriptableObject.DONTENUM);
        if (browserVersion.hasFeature(JS_INTL_NAMED_OBJECT)) {
            intl.setClassName("Object");
        }
        intl.defineProperties(browserVersion);

        if (browserVersion.hasFeature(JS_REFLECT)) {
            final Reflect reflect = new Reflect();
            reflect.setParentScope(window);
            window.defineProperty(reflect.getClassName(), reflect, ScriptableObject.DONTENUM);

            if (browserVersion.hasFeature(JS_REFLECT_NAMED_OBJECT)) {
                reflect.setClassName("Object");
            }
        }

        final Map<Class<? extends Scriptable>, Scriptable> prototypes = new HashMap<>();
        final Map<String, Scriptable> prototypesPerJSName = new HashMap<>();

        final String windowClassName = Window.class.getName();
        for (final ClassConfiguration config : jsConfig_.getAll()) {
            final boolean isWindow = windowClassName.equals(config.getHostClass().getName());
            if (isWindow) {
                configureConstantsPropertiesAndFunctions(config, window);

                final HtmlUnitScriptable prototype = configureClass(config, window, browserVersion);
                prototypesPerJSName.put(config.getClassName(), prototype);
            }
            else {
                final HtmlUnitScriptable prototype = configureClass(config, window, browserVersion);
                if (config.isJsObject()) {
                    // Place object with prototype property in Window scope
                    final HtmlUnitScriptable obj = config.getHostClass().newInstance();
                    prototype.defineProperty("__proto__", prototype, ScriptableObject.DONTENUM);
                    obj.defineProperty("prototype", prototype, ScriptableObject.DONTENUM); // but not setPrototype!
                    obj.setParentScope(window);
                    obj.setClassName(config.getClassName());
                    ScriptableObject.defineProperty(window, obj.getClassName(), obj, ScriptableObject.DONTENUM);
                    // this obj won't have prototype, constants need to be configured on it again
                    configureConstants(config, obj);
                }
                prototypes.put(config.getHostClass(), prototype);
                prototypesPerJSName.put(config.getClassName(), prototype);
            }
        }

        for (final ClassConfiguration config : jsConfig_.getAll()) {
            final Executable jsConstructor = config.getJsConstructor();
            final String jsClassName = config.getClassName();
            Scriptable prototype = prototypesPerJSName.get(jsClassName);
            final String hostClassSimpleName = config.getHostClassSimpleName();

            if ("Image".equals(hostClassSimpleName)
                    && browserVersion.hasFeature(JS_IMAGE_PROTOTYPE_SAME_AS_HTML_IMAGE)) {
                prototype = prototypesPerJSName.get("HTMLImageElement");
            }
            if ("Option".equals(hostClassSimpleName)) {
                prototype = prototypesPerJSName.get("HTMLOptionElement");
            }

            switch (hostClassSimpleName) {
                case "WebKitMutationObserver":
                    prototype = prototypesPerJSName.get("MutationObserver");
                    break;

                case "webkitURL":
                    prototype = prototypesPerJSName.get("URL");
                    break;

                default:
            }
            if (prototype != null && config.isJsObject()) {
                if (jsConstructor == null) {
                    final ScriptableObject constructor;
                    if ("Window".equals(jsClassName)) {
                        constructor = (ScriptableObject) ScriptableObject.getProperty(window, "constructor");
                    }
                    else {
                        constructor = config.getHostClass().newInstance();
                        ((SimpleScriptable) constructor).setClassName(config.getClassName());
                    }
                    defineConstructor(window, prototype, constructor);
                    configureConstantsStaticPropertiesAndStaticFunctions(config, constructor);
                }
                else {
                    final BaseFunction function;
                    if ("Window".equals(jsClassName)) {
                        function = (BaseFunction) ScriptableObject.getProperty(window, "constructor");
                    }
                    else {
                        function = new RecursiveFunctionObject(jsClassName, jsConstructor, window);
                    }

                    if ("WebKitMutationObserver".equals(hostClassSimpleName)
                            || "webkitURL".equals(hostClassSimpleName)
                            || "Image".equals(hostClassSimpleName)
                            || "Option".equals(hostClassSimpleName)) {
                        final Object prototypeProperty = ScriptableObject.getProperty(window, prototype.getClassName());

                        if (function instanceof FunctionObject) {
                            try {
                                ((FunctionObject) function).addAsConstructor(window, prototype);
                            }
                            catch (final Exception e) {
                                // TODO see issue #1897
                                if (LOG.isWarnEnabled()) {
                                    final String newline = System.lineSeparator();
                                    LOG.warn("Error during JavaScriptEngine.init(WebWindow, Context)" + newline
                                            + e.getMessage() + newline
                                            + "prototype: " + prototype.getClassName());
                                }
                            }
                        }

                        ScriptableObject.defineProperty(window, hostClassSimpleName, function,
                                ScriptableObject.DONTENUM);

                        // the prototype class name is set as a side effect of functionObject.addAsConstructor
                        // so we restore its value
                        if (!hostClassSimpleName.equals(prototype.getClassName())) {
                            if (prototypeProperty == UniqueTag.NOT_FOUND) {
                                ScriptableObject.deleteProperty(window, prototype.getClassName());
                            }
                            else {
                                ScriptableObject.defineProperty(window, prototype.getClassName(),
                                        prototypeProperty, ScriptableObject.DONTENUM);
                            }
                        }
                    }
                    else {
                        if (function instanceof FunctionObject) {
                            try {
                                ((FunctionObject) function).addAsConstructor(window, prototype);
                            }
                            catch (final Exception e) {
                                // TODO see issue #1897
                                if (LOG.isWarnEnabled()) {
                                    final String newline = System.lineSeparator();
                                    LOG.warn("Error during JavaScriptEngine.init(WebWindow, Context)" + newline
                                            + e.getMessage() + newline
                                            + "prototype: " + prototype.getClassName());
                                }
                            }
                        }
                    }

                    configureConstantsStaticPropertiesAndStaticFunctions(config, function);
                }
            }
        }
        window.setPrototype(prototypesPerJSName.get(Window.class.getSimpleName()));

        // once all prototypes have been build, it's possible to configure the chains
        final Scriptable objectPrototype = ScriptableObject.getObjectPrototype(window);
        for (final Map.Entry<String, Scriptable> entry : prototypesPerJSName.entrySet()) {
            final String name = entry.getKey();
            final ClassConfiguration config = jsConfig_.getClassConfiguration(name);
            final Scriptable prototype = entry.getValue();
            if (!StringUtils.isEmpty(config.getExtendedClassName())) {
                final Scriptable parentPrototype = prototypesPerJSName.get(config.getExtendedClassName());
                prototype.setPrototype(parentPrototype);
            }
            else {
                prototype.setPrototype(objectPrototype);
            }
        }

        // IE ActiveXObject simulation
        // see http://msdn.microsoft.com/en-us/library/ie/dn423948%28v=vs.85%29.aspx
        // DEV Note: this is at the moment the only usage of HiddenFunctionObject
        //           if we need more in the future, we have to enhance our JSX annotations
        if (browserVersion.hasFeature(JS_WINDOW_ACTIVEXOBJECT_HIDDEN)) {
            final Scriptable prototype = prototypesPerJSName.get("ActiveXObject");
            if (null != prototype) {
                final Method jsConstructor = ActiveXObject.class.getDeclaredMethod("jsConstructor",
                        Context.class, Object[].class, Function.class, boolean.class);
                final FunctionObject functionObject = new HiddenFunctionObject("ActiveXObject", jsConstructor, window);
                try {
                    functionObject.addAsConstructor(window, prototype);
                }
                catch (final Exception e) {
                    // TODO see issue #1897
                    if (LOG.isWarnEnabled()) {
                        final String newline = System.lineSeparator();
                        LOG.warn("Error during JavaScriptEngine.init(WebWindow, Context)" + newline
                                + e.getMessage() + newline
                                + "prototype: " + prototype.getClassName());
                    }
                }
            }
        }

        configureRhino(webClient, browserVersion, window);

        window.setPrototypes(prototypes, prototypesPerJSName);
        window.initialize(webWindow, page);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param webClient the WebClient
     * @param browserVersion the BrowserVersion
     * @param scriptable the window or the DedicatedWorkerGlobalScope
     */
    public static void configureRhino(final WebClient webClient,
            final BrowserVersion browserVersion, final SimpleScriptable scriptable) {
        // Rhino defines too much methods for us, particularly since implementation of ECMAScript5
        final ScriptableObject stringPrototype = (ScriptableObject) ScriptableObject.getClassPrototype(scriptable, "String");
        deleteProperties(stringPrototype, "equals", "equalsIgnoreCase", "toSource");

        final ScriptableObject numberPrototype = (ScriptableObject) ScriptableObject.getClassPrototype(scriptable, "Number");
        deleteProperties(numberPrototype, "toSource");
        final ScriptableObject datePrototype = (ScriptableObject) ScriptableObject.getClassPrototype(scriptable, "Date");
        deleteProperties(datePrototype, "toSource");

        if (!browserVersion.hasFeature(STRING_INCLUDES)) {
            deleteProperties(stringPrototype, "includes");
        }
        if (!browserVersion.hasFeature(STRING_REPEAT)) {
            deleteProperties(stringPrototype, "repeat");
        }
        if (!browserVersion.hasFeature(STRING_STARTS_ENDS_WITH)) {
            deleteProperties(stringPrototype, "startsWith", "endsWith");
        }
        if (!browserVersion.hasFeature(STRING_TRIM_LEFT_RIGHT)) {
            deleteProperties(stringPrototype, "trimLeft", "trimRight");
        }

        deleteProperties(scriptable, "uneval");
        removePrototypeProperties(scriptable, "Object", "toSource");
        removePrototypeProperties(scriptable, "Array", "toSource");
        removePrototypeProperties(scriptable, "Function", "toSource");

        if (browserVersion.hasFeature(JS_WINDOW_ACTIVEXOBJECT_HIDDEN)) {
            ((IdFunctionObject) ScriptableObject.getProperty(scriptable, "Object")).delete("assign");

            // TODO
            deleteProperties(scriptable, "WeakSet");
        }
        deleteProperties(scriptable, "isXMLName");

        NativeFunctionToStringFunction.installFix(scriptable, browserVersion);

        datePrototype.defineFunctionProperties(new String[] {"toLocaleDateString", "toLocaleTimeString"},
                DateCustom.class, ScriptableObject.DONTENUM);

        if (!browserVersion.hasFeature(JS_OBJECT_GET_OWN_PROPERTY_SYMBOLS)) {
            ((ScriptableObject) ScriptableObject.getProperty(scriptable, "Object")).delete("getOwnPropertySymbols");
        }

        if (!browserVersion.hasFeature(JS_ARRAY_FROM)) {
            deleteProperties((ScriptableObject) ScriptableObject.getProperty(scriptable, "Array"), "from", "of");
        }

        numberPrototype.defineFunctionProperties(new String[] {"toLocaleString"},
                NumberCustom.class, ScriptableObject.DONTENUM);

        if (!webClient.getOptions().isWebSocketEnabled()) {
            deleteProperties(scriptable, "WebSocket");
        }
    }

    private static void defineConstructor(final Window window,
            final Scriptable prototype, final ScriptableObject constructor) {
        constructor.setParentScope(window);
        try {
            ScriptableObject.defineProperty(prototype, "constructor", constructor,
                    ScriptableObject.DONTENUM  | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        }
        catch (final Exception e) {
            // TODO see issue #1897
            if (LOG.isWarnEnabled()) {
                final String newline = System.lineSeparator();
                LOG.warn("Error during JavaScriptEngine.init(WebWindow, Context)" + newline
                        + e.getMessage() + newline
                        + "prototype: " + prototype.getClassName());
            }
        }

        try {
            ScriptableObject.defineProperty(constructor, "prototype", prototype,
                    ScriptableObject.DONTENUM  | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        }
        catch (final Exception e) {
            // TODO see issue #1897
            if (LOG.isWarnEnabled()) {
                final String newline = System.lineSeparator();
                LOG.warn("Error during JavaScriptEngine.init(WebWindow, Context)" + newline
                        + e.getMessage() + newline
                        + "prototype: " + prototype.getClassName());
            }
        }

        window.defineProperty(constructor.getClassName(), constructor, ScriptableObject.DONTENUM);
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
     * @param browserVersion the browser version
     * @throws InstantiationException if the new class cannot be instantiated
     * @throws IllegalAccessException if we don't have access to create the new instance
     * @return the created prototype
     */
    public static HtmlUnitScriptable configureClass(final ClassConfiguration config, final Scriptable window,
            final BrowserVersion browserVersion)
        throws InstantiationException, IllegalAccessException {

        final HtmlUnitScriptable prototype = config.getHostClass().newInstance();
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
        configureSymbols(config, scriptable);
        configureFunctions(config, scriptable);
    }

    private static void configureFunctions(final ClassConfiguration config, final ScriptableObject scriptable) {
        final int attributes = ScriptableObject.EMPTY;
        // the functions
        final Map<String, Method> functionMap = config.getFunctionMap();
        if (functionMap != null) {
            for (final Entry<String, Method> functionInfo : functionMap.entrySet()) {
                final String functionName = functionInfo.getKey();
                final Method method = functionInfo.getValue();
                final FunctionObject functionObject = new FunctionObject(functionName, method, scriptable);
                scriptable.defineProperty(functionName, functionObject, attributes);
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

    private static void configureSymbols(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        final Map<Symbol, Method> symbolMap = config.getSymbolMap();
        if (symbolMap != null) {
            for (final Entry<Symbol, Method> symbolInfo : symbolMap.entrySet()) {
                final Callable symbolFunction = new FunctionObject(
                                    symbolInfo.getKey().toString(), symbolInfo.getValue(), scriptable);
                scriptable.defineProperty(symbolInfo.getKey(), symbolFunction, ScriptableObject.DONTENUM);
            }
        }
    }

    /**
     * Register WebWindow with the JavaScriptExecutor.
     * @param webWindow the WebWindow to be registered.
     */
    @Override
    public synchronized void registerWindowAndMaybeStartEventLoop(final WebWindow webWindow) {
        final WebClient webClient = getWebClient();
        if (webClient != null) {
            if (javaScriptExecutor_ == null) {
                javaScriptExecutor_ = BackgroundJavaScriptFactory.theFactory().createJavaScriptExecutor(webClient);
            }
            javaScriptExecutor_.addWindow(webWindow);
        }
    }

    /**
     * Shutdown the JavaScriptEngine.
     */
    @Override
    public void shutdown() {
        webClient_ = null;
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
    public Script compile(final HtmlPage page, final String sourceCode,
                           final String sourceName, final int startLine) {
        final Scriptable scope = getScope(page, null);
        return compile(page, scope, sourceCode, sourceName, startLine);
    }

    /**
     * Compiles the specified JavaScript code in the context of a given scope.
     *
     * @param owningPage the page from which the code started
     * @param scope the scope in which to execute the javascript code
     * @param sourceCode the JavaScript code to execute
     * @param sourceName the name that will be displayed on error conditions
     * @param startLine the line at which the script source starts
     * @return the result of executing the specified code
     */
    public Script compile(final HtmlPage owningPage, final Scriptable scope, final String sourceCode,
            final String sourceName, final int startLine) {
        WebAssert.notNull("sourceCode", sourceCode);

        if (LOG.isTraceEnabled()) {
            final String newline = System.lineSeparator();
            LOG.trace("Javascript compile " + sourceName + newline + sourceCode + newline);
        }

        final ContextAction<Object> action = new HtmlUnitContextAction(scope, owningPage) {
            @Override
            public Object doRun(final Context cx) {
                return cx.compileString(sourceCode, sourceName, startLine, null);
            }

            @Override
            protected String getSourceCode(final Context cx) {
                return sourceCode;
            }
        };

        return (Script) getContextFactory().callSecured(action, owningPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(final HtmlPage page,
                           final String sourceCode,
                           final String sourceName,
                           final int startLine) {

        final Script script = compile(page, sourceCode, sourceName, startLine);
        if (script == null) { // happens with syntax error + throwExceptionOnScriptError = false
            return null;
        }
        return execute(page, script);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(final HtmlPage page, final Script script) {
        final Scriptable scope = getScope(page, null);
        return execute(page, scope, script);
    }

    /**
     * Executes the specified JavaScript code in the given scope.
     *
     * @param page the page that started the execution
     * @param scope the scope in which to execute
     * @param script the script to execute
     * @return the result of executing the specified code
     */
    public Object execute(final HtmlPage page, final Scriptable scope, final Script script) {
        final ContextAction<Object> action = new HtmlUnitContextAction(scope, page) {
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

        final ContextAction<Object> action = new HtmlUnitContextAction(scope, page) {
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
     * Facility for ContextAction usage.
     * ContextAction should be preferred because according to Rhino doc it
     * "guarantees proper association of Context instances with the current thread and is faster".
     */
    private abstract class HtmlUnitContextAction implements ContextAction<Object> {
        private final Scriptable scope_;
        private final HtmlPage page_;

        HtmlUnitContextAction(final Scriptable scope, final HtmlPage page) {
            scope_ = scope;
            page_ = page;
        }

        @Override
        public final Object run(final Context cx) {
            final Boolean javaScriptAlreadyRunning = javaScriptRunning_.get();
            javaScriptRunning_.set(Boolean.TRUE);

            try {
                // KEY_STARTING_SCOPE maintains a stack of scopes
                @SuppressWarnings("unchecked")
                Deque<Scriptable> stack = (Deque<Scriptable>) cx.getThreadLocal(JavaScriptEngine.KEY_STARTING_SCOPE);
                if (null == stack) {
                    stack = new ArrayDeque<>();
                    cx.putThreadLocal(KEY_STARTING_SCOPE, stack);
                }

                final Object response;
                stack.push(scope_);
                try {
                    cx.putThreadLocal(KEY_STARTING_PAGE, page_);
                    synchronized (page_) { // 2 scripts can't be executed in parallel for one page
                        if (page_ != page_.getEnclosingWindow().getEnclosedPage()) {
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

    void doProcessPostponedActions() {
        holdPostponedActions_ = false;

        final WebClient webClient = getWebClient();
        // shutdown was already called
        if (webClient == null) {
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
        if (actions != null) {
            postponedActions_.set(null);
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
            catch (final Exception e) {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * Adds an action that should be executed first when the script currently being executed has finished.
     * @param action the action
     */
    @Override
    public void addPostponedAction(final PostponedAction action) {
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
        // shutdown was already called
        final WebClient webClient = getWebClient();
        if (webClient == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("handleJavaScriptException('" + scriptException.getMessage()
                    + "') called after the shutdown of the Javascript engine - exception ignored.");
            }

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
        // shutdown was already called
        if (webClient == null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Caught script timeout error after the shutdown of the Javascript engine - ignored.");
            }
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
}
