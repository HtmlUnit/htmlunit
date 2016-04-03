/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A container for all the JavaScript configuration information for one class.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class ClassConfiguration {
    private Map<String, PropertyInfo> propertyMap_ = new HashMap<>();
    private Map<String, Method> functionMap_ = new HashMap<>();
    private Map<String, PropertyInfo> staticPropertyMap_ = new HashMap<>();
    private Map<String, Method> staticFunctionMap_ = new HashMap<>();
    private List<ConstantInfo> constants_ = new ArrayList<>();
    private String extendedClassName_;
    private final Class<? extends HtmlUnitScriptable> hostClass_;
    private final String hostClassSimpleName_;

    /**
     * The constructor method in the {@link #hostClass_}
     */
    private Member jsConstructor_;
    private final Class<?>[] domClasses_;
    private final boolean jsObject_;
    private final boolean definedInStandardsMode_;
    private final String className_;

    /**
     * Constructor.
     *
     * @param hostClass - the class implementing this functionality
     * @param domClasses the DOM classes that this object supports
     * @param jsObject boolean flag for if this object is a JavaScript object
     * @param definedInStandardsMode should be defined in only Standards Mode
     * @param className the class name, can be null
     */
    public ClassConfiguration(final Class<? extends HtmlUnitScriptable> hostClass, final Class<?>[] domClasses,
            final boolean jsObject, final boolean definedInStandardsMode, final String className) {
        final Class<?> superClass = hostClass.getSuperclass();
        if (superClass != SimpleScriptable.class) {
            extendedClassName_ = superClass.getSimpleName();
        }
        else {
            extendedClassName_ = "";
        }
        hostClass_ = hostClass;
        hostClassSimpleName_ = hostClass_.getSimpleName();
        jsObject_ = jsObject;
        definedInStandardsMode_ = definedInStandardsMode;
        domClasses_ = domClasses;
        if (className == null) {
            className_ = getHostClass().getSimpleName();
        }
        else {
            className_ = className;
        }
    }

    void setJSConstructor(final Member jsConstructor) {
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
        staticPropertyMap_.put(name, info);
    }

    /**
     * Add the constant to the configuration.
     * @param name - Name of the configuration
     */
    public void addConstant(final String name) {
        try {
            final Object value = getHostClass().getField(name).get(null);
            int flag = ScriptableObject.READONLY | ScriptableObject.PERMANENT;
            // https://code.google.com/p/chromium/issues/detail?id=500633
            if (getClassName().endsWith("Array")) {
                flag |= ScriptableObject.DONTENUM;
            }
            constants_.add(new ConstantInfo(name, value, flag));
        }
        catch (final Exception e) {
            throw Context.reportRuntimeError("Cannot get field '" + name + "' for type: "
                + getHostClass().getName());
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
     * Returns the set of entries for the defined static properties.
     * @return a set
     */
    public Set<Entry<String, PropertyInfo>> getStaticPropertyEntries() {
        return staticPropertyMap_.entrySet();
    }

    /**
     * Returns the set of entries for the defined functions.
     * @return a set
     */
    public Set<Entry<String, Method>> getFunctionEntries() {
        return functionMap_.entrySet();
    }

    /**
     * Returns the set of entries for the defined static functions.
     * @return a set
     */
    public Set<Entry<String, Method>> getStaticFunctionEntries() {
        return staticFunctionMap_.entrySet();
    }

    /**
     * Returns the set of keys for the defined functions.
     * @return a set
     */
    public Set<String> getFunctionKeys() {
        return functionMap_.keySet();
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
     * @param name the method name
     * @param method the method
     */
    public void addFunction(final String name, final Method method) {
        functionMap_.put(name, method);
    }

    /**
     * Add the static function to the configuration.
     * @param name the function name
     * @param method the method
     */
    public void addStaticFunction(final String name, final Method method) {
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
    public Member getJsConstructor() {
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
     * Returns whether the class should be defined in only Standards Mode.
     * @return defineInStandardsMode
     */
    public boolean isDefinedInStandardsMode() {
        return definedInStandardsMode_;
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
