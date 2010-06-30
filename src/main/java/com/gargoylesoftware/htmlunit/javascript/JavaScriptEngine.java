/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
 * @see <a href="http://groups-beta.google.com/group/netscape.public.mozilla.jseng/browse_thread/thread/b4edac57329cf49f/069e9307ec89111f">
 * Rhino and Java Browser</a>
 */
public class JavaScriptEngine {

    private static final long serialVersionUID = -5414040051465432088L;
    private static final Log LOG = LogFactory.getLog(JavaScriptEngine.class);

    private final WebClient webClient_;
    private final HtmlUnitContextFactory contextFactory_;

    private transient ThreadLocal<Boolean> javaScriptRunning_;
    private transient ThreadLocal<List<PostponedAction>> postponedActions_;
    private transient ThreadLocal<Boolean> holdPostponedActions_;

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
     * Initializes all the JS stuff for the window.
     * @param webWindow the web window
     * @param context the current context
     * @throws Exception if something goes wrong
     */
    private void init(final WebWindow webWindow, final Context context) throws Exception {
        final WebClient webClient = webWindow.getWebClient();
        final BrowserVersion browserVersion = webClient.getBrowserVersion();
        final Map<Class< ? extends SimpleScriptable>, Scriptable> prototypes =
            new HashMap<Class< ? extends SimpleScriptable>, Scriptable>();
        final Map<String, Scriptable> prototypesPerJSName = new HashMap<String, Scriptable>();
        final Window window = new Window();
        final JavaScriptConfiguration jsConfig = JavaScriptConfiguration.getInstance(browserVersion);
        context.initStandardObjects(window);

        // remove some objects, that Rhino defines in top scope but that we don't want
        deleteProperties(window, "javax", "org", "com", "edu", "net", "JavaAdapter", "JavaImporter", "Continuation");
        if (browserVersion.isIE()) {
            deleteProperties(window, "Packages", "java", "getClass", "XML", "XMLList", "Namespace", "QName");
        }

        // put custom object to be called as very last prototype to call the fallback getter (if any)
        final Scriptable fallbackCaller = new FallbackCaller();
        ScriptableObject.getObjectPrototype(window).setPrototype(fallbackCaller);

        for (final String jsClassName : jsConfig.keySet()) {
            final ClassConfiguration config = jsConfig.getClassConfiguration(jsClassName);
            final boolean isWindow = Window.class.getName().equals(config.getHostClass().getName());
            if (isWindow) {
                configureConstantsPropertiesAndFunctions(config, window);
            }
            else {
                final ScriptableObject prototype = configureClass(config, window);
                if (config.isJsObject()) {
                    // for FF, place object with prototype property in Window scope
                    if (!browserVersion.isIE()) {
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
        for (final Map.Entry<String, Scriptable> entry : prototypesPerJSName.entrySet()) {
            final String name = entry.getKey();
            final ClassConfiguration config = jsConfig.getClassConfiguration(name);
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

        // eval hack (cf unit tests testEvalScopeOtherWindow and testEvalScopeLocal)
        final Member evalFn = Window.class.getMethod("custom_eval", new Class[]{String.class});
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

        // Rhino defines too much methods for us, particularly since implementation of ECMAScript5
        removePrototypeProperties(window, "String", "equals", "equalsIgnoreCase");
        if (browserVersion.hasFeature(BrowserVersionFeatures.STRING_TRIM)) {
            final ScriptableObject stringPrototype =
                (ScriptableObject) ScriptableObject.getClassPrototype(window, "String");
            stringPrototype.defineFunctionProperties(new String[] {"trimLeft", "trimRight"},
                StringCustom.class, ScriptableObject.EMPTY);
        }
        else {
            removePrototypeProperties(window, "String", "trim");
        }
        removePrototypeProperties(window, "Function", "bind");
        removePrototypeProperties(window, "Date", "toISOString", "toJSON");

        // in IE, not all standard methods exists
        if (browserVersion.isIE()) {
            deleteProperties(window, "isXMLName", "uneval");
            removePrototypeProperties(window, "Object", "__defineGetter__", "__defineSetter__", "__lookupGetter__",
                    "__lookupSetter__", "toSource");
            removePrototypeProperties(window, "Array", "every", "filter", "forEach", "indexOf", "lastIndexOf", "map",
                    "reduce", "reduceRight", "some", "toSource");
            removePrototypeProperties(window, "Date", "toSource");
            removePrototypeProperties(window, "Function", "toSource");
            removePrototypeProperties(window, "Number", "toSource");
            removePrototypeProperties(window, "String", "toSource");
        }
        else if ("FF2".equals(browserVersion.getNickname())) {
            removePrototypeProperties(window, "Array", "reduce", "reduceRight");
        }

        window.setPrototypes(prototypes);
        window.initialize(webWindow);
    }

    /**
     * Deletes the properties with the provided names.
     * @param window the scope from which properties have to be removed
     * @param propertiesToDelete the list of property names
     */
    private void deleteProperties(final Window window, final String... propertiesToDelete) {
        for (final String property : propertiesToDelete) {
            window.delete(property);
        }
    }

    /**
     * Removes prototype properties.
     * @param window the scope
     * @param className the class for which properties should be removed
     * @param properties the properties to remove
     */
    private void removePrototypeProperties(final Window window, final String className, final String... properties) {
        final ScriptableObject prototype = (ScriptableObject) ScriptableObject.getClassPrototype(window, className);
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

        final Class< ? > jsHostClass = config.getHostClass();
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
        for (final String propertyName : config.propertyKeys()) {
            final Method readMethod = config.getPropertyReadMethod(propertyName);
            final Method writeMethod = config.getPropertyWriteMethod(propertyName);
            scriptable.defineProperty(propertyName, null, readMethod, writeMethod, ScriptableObject.EMPTY);
        }

        int attributes = ScriptableObject.EMPTY;
        if (webClient_.getBrowserVersion().isIE()) {
            attributes = ScriptableObject.DONTENUM;
        }
        // the functions
        for (final String functionName : config.functionKeys()) {
            final Method method = config.getFunctionMethod(functionName);
            final FunctionObject functionObject = new FunctionObject(functionName, method, scriptable);
            scriptable.defineProperty(functionName, functionObject, attributes);
        }
    }

    private void configureConstants(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        for (final String constant : config.constants()) {
            final Class< ? > linkedClass = config.getHostClass();
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
            javaScriptExecutor_ = new JavaScriptExecutor(webClient_);
        }
        javaScriptExecutor_.addWindow(webWindow);
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
        return getContextFactory().call(action);
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
     * @param function the JavaScript function to execute
     * @param context the context in which execution should occur
     * @param scope the execution scope
     * @param thisObject the 'this' object
     * @param args the function's arguments
     * @return the function result
     */
    public Object callFunction(final HtmlPage htmlPage, final Function function, final Context context,
            final Scriptable scope, final Scriptable thisObject, final Object[] args) {

        synchronized (htmlPage) { // 2 scripts can't be executed in parallel for one page
            final Object result = function.call(context, scope, thisObject, args);
            doProcessPostponedActions();
            return result;
        }
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
                cx.putThreadLocal(KEY_STARTING_SCOPE, scope_);
                cx.putThreadLocal(KEY_STARTING_PAGE, htmlPage_);
                synchronized (htmlPage_) { // 2 scripts can't be executed in parallel for one page
                    final Object response = doRun(cx);
                    doProcessPostponedActions();
                    return response;
                }
            }
            catch (final Exception e) {
                handleJavaScriptException(new ScriptException(htmlPage_, e, getSourceCode(cx)));
                return null;
            }
            catch (final TimeoutError e) {
                if (getWebClient().isThrowExceptionOnScriptError()) {
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

    private void doProcessPostponedActions() {
        if (Boolean.TRUE.equals(holdPostponedActions_.get())) {
            return;
        }

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
        postponedActions_.set(null);
        if (actions != null) {
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
     */
    protected void handleJavaScriptException(final ScriptException scriptException) {
        // Trigger window.onerror, if it has been set.
        final HtmlPage page = scriptException.getPage();
        if (page != null) {
            final WebWindow window = page.getEnclosingWindow();
            if (window != null) {
                final Window w = (Window) window.getScriptObject();
                if (w != null) {
                    w.triggerOnError(scriptException);
                }
            }
        }
        // Throw a Java exception if the user wants us to.
        if (getWebClient().isThrowExceptionOnScriptError()) {
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
        holdPostponedActions_.set(Boolean.TRUE);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * Process postponed actions, if any.
     */
    public void processPostponedActions() {
        holdPostponedActions_.set(Boolean.FALSE);
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
        holdPostponedActions_ = new ThreadLocal<Boolean>();
    }

    private static class FallbackCaller extends ScriptableObject {
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

}
