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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A container for all the JavaScript configuration information for one class.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class ClassConfiguration {
    private Map<String, PropertyInfo> propertyMap_ = new HashMap<String, PropertyInfo>();
    private Map<String, Method> functionMap_ = new HashMap<String, Method>();
    private List<String> constants_ = new ArrayList<String>();
    private String extendedClassName_;
    private final Class<? extends SimpleScriptable> hostClass_;
    /**
     * The constructor method in the {@link #hostClass_}
     */
    private Method jsConstructor_;
    private final Class<?>[] domClasses_;
    private final boolean jsObject_;

    /**
     * Constructor.
     *
     * @param hostClass - the class implementing this functionality
     * @param domClasses the DOM classes that this object supports
     * @param jsObject boolean flag for if this object is a JavaScript object
     */
    public ClassConfiguration(final Class<? extends SimpleScriptable> hostClass, final Class<?>[] domClasses,
            final boolean jsObject) {
        final Class<?> superClass = hostClass.getSuperclass();
        if (superClass != SimpleScriptable.class) {
            extendedClassName_ = superClass.getSimpleName();
        }
        else {
            extendedClassName_ = "";
        }
        hostClass_ = hostClass;
        jsObject_ = jsObject;
        domClasses_ = domClasses;
    }

    void setJSConstructor(final Method jsConstructor) {
        if (jsConstructor_ != null) {
            throw new IllegalStateException("Can not have two constructors.");
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
     * Add the constant to the configuration.
     * @param name - Name of the configuration
     */
    public void addConstant(final String name) {
        constants_.add(name);
    }

    /**
     * Returns the set of entries for the defined properties.
     * @return a set
     */
    public Set<Entry<String, PropertyInfo>> propertyEntries() {
        return propertyMap_.entrySet();
    }

    /**
     * Returns the set of entries for the defined functions.
     * @return a set
     */
    public Set<Entry<String, Method>> functionEntries() {
        return functionMap_.entrySet();
    }

    /**
     * Returns the set of keys for the defined functions.
     * @return a set
     */
    public Set<String> functionKeys() {
        return functionMap_.keySet();
    }

    /**
     * Returns the constant list.
     * @return a list
     */
    public List<String> constants() {
        return constants_;
    }

    /**
     * Add the function to the configuration.
     * @param method the method
     */
    public void addFunction(final Method method) {
        functionMap_.put(method.getName(), method);
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
    public Class<? extends SimpleScriptable> getHostClass() {
        return hostClass_;
    }

    /**
     * Gets the JavaScript constructor method in {@link #getHostClass()}.
     * @return the JavaScript constructor method in {@link #getHostClass()}
     */
    public Method getJsConstructor() {
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
     * Class used to contain the property information if the property is readable, writable and the
     * methods that implement the get and set functions.
     */
    public static class PropertyInfo {
        private Method readMethod_;
        private Method writeMethod_;

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
}
