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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Symbol;

/**
 * A container for all the JavaScript configuration information for one class.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class ClassConfiguration {
    private Map<String, PropertyInfo> propertyMap_;
    private Map<Symbol, Method> symbolMap_;
    private Map<String, Method> functionMap_;
    private Map<String, PropertyInfo> staticPropertyMap_;
    private Map<String, Method> staticFunctionMap_;
    private List<ConstantInfo> constants_;
    private String extendedClassName_;
    private final Class<? extends HtmlUnitScriptable> hostClass_;
    private final String hostClassSimpleName_;

    /**
     * The constructor method in the {@link #hostClass_}
     */
    private Executable jsConstructor_;
    private final Class<?>[] domClasses_;
    private final boolean jsObject_;
    private final String className_;

    /**
     * Constructor.
     *
     * @param hostClass - the class implementing this functionality
     * @param domClasses the DOM classes that this object supports
     * @param jsObject boolean flag for if this object is a JavaScript object
     * @param className the class name, can be null
     * @param extendedClassName the extended class name
     */
    public ClassConfiguration(final Class<? extends HtmlUnitScriptable> hostClass, final Class<?>[] domClasses,
            final boolean jsObject, final String className, final String extendedClassName) {
        hostClass_ = hostClass;
        hostClassSimpleName_ = hostClass_.getSimpleName();
        jsObject_ = jsObject;
        domClasses_ = domClasses;
        if (className == null) {
            className_ = getHostClass().getSimpleName();
        }
        else {
            className_ = className;
        }
        extendedClassName_ = extendedClassName;
    }

    void setJSConstructor(final Executable jsConstructor) {
        if (jsConstructor_ != null) {
            throw new IllegalStateException("Can not have two constructors for "
                    + jsConstructor_.getDeclaringClass().getName());
        }
        jsConstructor_ = jsConstructor;
    }

    /**
     * Add the property to the configuration.
     * @param name name of the property
     * @param getter the getter method
     * @param setter the setter method
     */
    public void addProperty(final String name, final Method getter, final Method setter) {
        final PropertyInfo info = new PropertyInfo(getter, setter);
        if (propertyMap_ == null) {
            propertyMap_ = new HashMap<>();
        }
        propertyMap_.put(name, info);
    }

    /**
     * Add the static property to the configuration.
     * @param name name of the static property
     * @param getter the static getter method
     * @param setter the static setter method
     */
    public void addStaticProperty(final String name, final Method getter, final Method setter) {
        final PropertyInfo info = new PropertyInfo(getter, setter);
        if (staticPropertyMap_ == null) {
            staticPropertyMap_ = new HashMap<>();
        }
        staticPropertyMap_.put(name, info);
    }

    /**
     * Add the constant to the configuration.
     * @param name - Name of the configuration
     */
    public void addConstant(final String name) {
        if (constants_ == null) {
            constants_ = new ArrayList<>();
        }
        try {
            final Object value = getHostClass().getField(name).get(null);
            int flag = ScriptableObject.READONLY | ScriptableObject.PERMANENT;
            // https://code.google.com/p/chromium/issues/detail?id=500633
            if (getClassName().endsWith("Array")) {
                flag |= ScriptableObject.DONTENUM;
            }
            constants_.add(new ConstantInfo(name, value, flag));
        }
        catch (final NoSuchFieldException e) {
            throw Context.reportRuntimeError("Cannot get field '" + name + "' for type: " + getHostClass().getName()
                    + "reason: " + e.getMessage());
        }
        catch (final IllegalAccessException e) {
            throw Context.reportRuntimeError("Cannot get field '" + name + "' for type: " + getHostClass().getName()
                    + "reason: " + e.getMessage());
        }
    }

    /**
     * Returns the Map of entries for the defined properties.
     * @return the map
     */
    public Map<String, PropertyInfo> getPropertyMap() {
        return propertyMap_;
    }

    /**
     * Returns the Map of entries for the defined symbols.
     * @return the map
     */
    public Map<Symbol, Method> getSymbolMap() {
        return symbolMap_;
    }

    /**
     * Returns the set of entries for the defined static properties.
     * @return a set
     */
    public Map<String, PropertyInfo> getStaticPropertyMap() {
        return staticPropertyMap_;
    }

    /**
     * Returns the set of entries for the defined functions.
     * @return a set
     */
    public Map<String, Method> getFunctionMap() {
        return functionMap_;
    }

    /**
     * Returns the set of entries for the defined static functions.
     * @return a set
     */
    public Map<String, Method> getStaticFunctionMap() {
        return staticFunctionMap_;
    }

    /**
     * Returns the constant list.
     * @return a list
     */
    public List<ConstantInfo> getConstants() {
        return constants_;
    }

    /**
     * Add the function to the configuration.
     * @param symbol the symbol
     * @param method the method
     */
    public void addSymbol(final Symbol symbol, final Method method) {
        if (symbolMap_ == null) {
            symbolMap_ = new HashMap<>();
        }
        symbolMap_.put(symbol, method);
    }

    /**
     * Add the function to the configuration.
     * @param name the method name
     * @param method the method
     */
    public void addFunction(final String name, final Method method) {
        if (functionMap_ == null) {
            functionMap_ = new HashMap<>();
        }
        functionMap_.put(name, method);
    }

    /**
     * Add the static function to the configuration.
     * @param name the function name
     * @param method the method
     */
    public void addStaticFunction(final String name, final Method method) {
        if (staticFunctionMap_ == null) {
            staticFunctionMap_ = new HashMap<>();
        }
        staticFunctionMap_.put(name, method);
    }

    /**
     * @return the extendedClass
     */
    public String getExtendedClassName() {
        return extendedClassName_;
    }

    /**
     * Gets the class of the JavaScript host object.
     * @return the class of the JavaScript host object
     */
    public Class<? extends HtmlUnitScriptable> getHostClass() {
        return hostClass_;
    }

    /**
     * @return the hostClassSimpleName
     */
    public String getHostClassSimpleName() {
        return hostClassSimpleName_;
    }

    /**
     * Gets the JavaScript constructor method in {@link #getHostClass()}.
     * @return the JavaScript constructor method in {@link #getHostClass()}
     */
    public Executable getJsConstructor() {
        return jsConstructor_;
    }

    /**
     * Returns the DOM classes.
     *
     * @return the DOM classes
     */
    public Class<?>[] getDomClasses() {
        return domClasses_;
    }

    /**
     * @return the jsObject
     */
    public boolean isJsObject() {
        return jsObject_;
    }

    /**
     * Returns the class name.
     * @return the class name
     */
    public String getClassName() {
        return className_;
    }

    /**
     * Class used to contain the property information if the property is readable, writable and the
     * methods that implement the get and set functions.
     */
    public static class PropertyInfo {
        private final Method readMethod_;
        private final Method writeMethod_;

        /**
         * Constructor.
         *
         * @param readMethod the readMethod
         * @param writeMethod the writeMethod
         */
        public PropertyInfo(final Method readMethod, final Method writeMethod) {
            readMethod_ = readMethod;
            writeMethod_ = writeMethod;
        }

        /**
         * @return the readMethod
         */
        public Method getReadMethod() {
            return readMethod_;
        }

        /**
         * @return the writeMethod
         */
        public Method getWriteMethod() {
            return writeMethod_;
        }
    }

    /**
     * Class used to contain the constant information name, value and flag.
     */
    public static class ConstantInfo {
        private final String name_;
        private final Object value_;
        private final int flag_;

        /**
         * Constructor.
         *
         * @param name the name
         * @param value the value
         * @param flag the flag
         */
        public ConstantInfo(final String name, final Object value, final int flag) {
            name_ = name;
            value_ = value;
            flag_ = flag;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name_;
        }

        /**
         * @return the value
         */
        public Object getValue() {
            return value_;
        }

        /**
         * @return the flag
         */
        public int getFlag() {
            return flag_;
        }
    }
}
