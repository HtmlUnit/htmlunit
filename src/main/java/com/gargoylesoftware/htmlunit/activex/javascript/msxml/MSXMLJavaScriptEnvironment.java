/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;

/**
 * JavaScript environment for the MSXML ActiveX library.
 *
 * @version $Revision$
 * @author Frank Danek
 */
public class MSXMLJavaScriptEnvironment {

    private final MSXMLConfiguration config_;

    private Map<Class<? extends MSXMLScriptable>, Scriptable> prototypes_;

    /**
     * Creates an instance for the given {@link BrowserVersion}.
     *
     * @param browserVersion the browser version to use
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public MSXMLJavaScriptEnvironment(final BrowserVersion browserVersion) throws Exception {
        config_ = MSXMLConfiguration.getInstance(browserVersion);

        final Map<String, ScriptableObject> prototypesPerJSName = new HashMap<String, ScriptableObject>();
        final Map<Class<? extends MSXMLScriptable>, Scriptable> prototypes =
                new HashMap<Class<? extends MSXMLScriptable>, Scriptable>();

        // put custom object to be called as very last prototype to call the fallback getter (if any)
        final Scriptable fallbackCaller = new FallbackCaller();

        for (final ClassConfiguration config : config_.getAll()) {
            final ScriptableObject prototype = configureClass(config);
            if (config.isJsObject()) {
                prototypes.put((Class<? extends MSXMLScriptable>) config.getHostClass(), prototype);
            }
            prototypesPerJSName.put(config.getHostClass().getSimpleName(), prototype);
        }

        // once all prototypes have been build, it's possible to configure the chains
//        final Scriptable objectPrototype = ScriptableObject.getObjectPrototype(window);
        for (final Map.Entry<String, ScriptableObject> entry : prototypesPerJSName.entrySet()) {
            final String name = entry.getKey();
            final ClassConfiguration config = config_.getClassConfiguration(name);
            Scriptable prototype = entry.getValue();
            if (prototype.getPrototype() != null) {
                prototype = prototype.getPrototype(); // "double prototype" hack for FF
            }
            if (!StringUtils.isEmpty(config.getExtendedClassName())) {
                final Scriptable parentPrototype = prototypesPerJSName.get(config.getExtendedClassName());
                prototype.setPrototype(parentPrototype);
            }
            else {
//                prototype.setPrototype(objectPrototype);
                prototype.setPrototype(fallbackCaller);
            }
        }

        prototypes_ = prototypes;
    }

    /**
     * Configures the specified class for access via JavaScript.
     * @param config the configuration settings for the class to be configured
     * @param window the scope within which to configure the class
     * @throws InstantiationException if the new class cannot be instantiated
     * @throws IllegalAccessException if we don't have access to create the new instance
     * @return the created prototype
     */
    private ScriptableObject configureClass(final ClassConfiguration config/*, final Scriptable window*/)
        throws InstantiationException, IllegalAccessException {

        final Class<?> jsHostClass = config.getHostClass();
        final ScriptableObject prototype = (ScriptableObject) jsHostClass.newInstance();
//        prototype.setParentScope(window);

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

        final int attributes = ScriptableObject.DONTENUM;
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
     * Gets the class of the JavaScript object for the node class.
     * @param c the node class <code>DomNode</code> or some subclass.
     * @return <code>null</code> if none found
     */
    @SuppressWarnings("unchecked")
    public Class<? extends MSXMLScriptable> getJavaScriptClass(final Class<?> c) {
        return (Class<? extends MSXMLScriptable>) config_.getDomJavaScriptMapping().get(c);
    }

    /**
     * Returns the prototype object corresponding to the specified HtmlUnit class inside the window scope.
     * @param jsClass the class whose prototype is to be returned
     * @return the prototype object corresponding to the specified class inside the specified scope
     */
    public Scriptable getPrototype(final Class<? extends SimpleScriptable> jsClass) {
        return prototypes_.get(jsClass);
    }

    private static class FallbackCaller extends ScriptableObject {
        private static final long serialVersionUID = 1402030469837200894L;

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
}
