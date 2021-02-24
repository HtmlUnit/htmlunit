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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.PropertyInfo;

import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * JavaScript environment for the MSXML ActiveX library.
 *
 * @author Frank Danek
 */
public class MSXMLJavaScriptEnvironment {

    private final MSXMLConfiguration config_;

    private final Map<Class<? extends MSXMLScriptable>, Scriptable> prototypes_;

    /**
     * Creates an instance for the given {@link BrowserVersion}.
     *
     * @param browserVersion the browser version to use
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public MSXMLJavaScriptEnvironment(final BrowserVersion browserVersion) throws Exception {
        config_ = MSXMLConfiguration.getInstance(browserVersion);

        final Map<String, ScriptableObject> prototypesPerJSName = new HashMap<>();
        final Map<Class<? extends MSXMLScriptable>, Scriptable> prototypes = new HashMap<>();

        // put custom object to be called as very last prototype to call the fallback getter (if any)

        for (final ClassConfiguration config : config_.getAll()) {
            final ScriptableObject prototype = configureClass(config);
            if (config.isJsObject()) {
                prototypes.put((Class<? extends MSXMLScriptable>) config.getHostClass(), prototype);
            }
            prototypesPerJSName.put(config.getHostClass().getSimpleName(), prototype);
        }

        // once all prototypes have been build, it's possible to configure the chains
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
    private static ScriptableObject configureClass(final ClassConfiguration config/*, final Scriptable window*/)
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
    private static void configureConstantsPropertiesAndFunctions(final ClassConfiguration config,
            final ScriptableObject scriptable) {

        // the constants
        configureConstants(config, scriptable);

        // the properties
        final Map<String, PropertyInfo> propertyMap = config.getPropertyMap();
        if (propertyMap != null) {
            for (final Map.Entry<String, PropertyInfo> entry : propertyMap.entrySet()) {
                final PropertyInfo info = entry.getValue();
                final Method readMethod = info.getReadMethod();
                final Method writeMethod = info.getWriteMethod();
                scriptable.defineProperty(entry.getKey(), null, readMethod, writeMethod, ScriptableObject.EMPTY);
            }
        }

        final int attributes = ScriptableObject.DONTENUM;
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

    private static void configureConstants(final ClassConfiguration config,
            final ScriptableObject scriptable) {
        final List<ConstantInfo> constants = config.getConstants();
        if (constants != null) {
            for (final ConstantInfo constantInfo : constants) {
                scriptable.defineProperty(constantInfo.getName(), constantInfo.getValue(),
                        ScriptableObject.READONLY | ScriptableObject.PERMANENT);
            }
        }
    }

    /**
     * Gets the class of the JavaScript object for the node class.
     * @param c the node class <code>DomNode</code> or some subclass.
     * @return {@code null} if none found
     */
    @SuppressWarnings("unchecked")
    public Class<? extends MSXMLScriptable> getJavaScriptClass(final Class<?> c) {
        return (Class<? extends MSXMLScriptable>) config_.getDomJavaScriptMappingFor(c);
    }

    /**
     * Returns the prototype object corresponding to the specified HtmlUnit class inside the window scope.
     * @param jsClass the class whose prototype is to be returned
     * @return the prototype object corresponding to the specified class inside the specified scope
     */
    public Scriptable getPrototype(final Class<? extends SimpleScriptable> jsClass) {
        return prototypes_.get(jsClass);
    }
}
