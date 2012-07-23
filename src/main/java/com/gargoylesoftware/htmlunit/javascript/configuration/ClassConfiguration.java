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
import java.util.Set;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A container for all the JavaScript configuration information.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public final class ClassConfiguration {
    private static final String GETTER_PREFIX = "jsxGet_";
    private static final String SETTER_PREFIX = "jsxSet_";
    private static final String FUNCTION_PREFIX = "jsxFunction_";

    private Map<String, PropertyInfo> propertyMap_ = new HashMap<String, PropertyInfo>();
    private Map<String, FunctionInfo> functionMap_ = new HashMap<String, FunctionInfo>();
    private List<String> constants_ = new ArrayList<String>();
    private String extendedClassName_;
    private final Class<? extends SimpleScriptable> hostClass_;
    /**
     * The constructor method in the {@link #hostClass_}
     */
    private final Method jsConstructor_;
    private final String htmlClassName_;
    private final boolean jsObject_;

    /**
     * Constructor.
     *
     * @param hostClassName - the fully qualified name of the class implementing this functionality
     * @param jsConstructor the constructor of method <code>implementingClass</code>
     * @param extendedClassName - The name of the class that this class extends
     * @param htmlClassName the name of the HTML class that this object supports
     * @param jsObject boolean flag for if this object is a JavaScript object
     * @throws ClassNotFoundException - if the implementing class is not found
     */
    @SuppressWarnings("unchecked")
    public ClassConfiguration(final String hostClassName, final String jsConstructor,
        final String extendedClassName, final String htmlClassName, final boolean jsObject)
        throws ClassNotFoundException {
        extendedClassName_ = extendedClassName;
        hostClass_ = (Class<? extends SimpleScriptable>) Class.forName(hostClassName);
        if (jsConstructor != null && !jsConstructor.isEmpty()) {
            Method foundCtor = null;
            for (final Method method : hostClass_.getMethods()) {
                if (method.getName().equals(jsConstructor)) {
                    foundCtor = method;
                    break;
                }
            }
            if (foundCtor == null) {
                throw new IllegalStateException("Constructor method \"" + jsConstructor
                        + "\" in class \"" + hostClassName + " is not found.");
            }
            jsConstructor_ = foundCtor;
        }
        else {
            jsConstructor_ = null;
        }
        jsObject_ = jsObject;
        if (htmlClassName != null && !htmlClassName.isEmpty()) {
            htmlClassName_ = htmlClassName;
        }
        else {
            htmlClassName_ = null;
        }
    }

    /**
     * Add the property to the configuration.
     * @param name name of the property
     * @param readable flag for if the property is readable
     * @param writable flag for if the property is writable
     */
    public void addProperty(final String name, final boolean readable, final boolean writable) {
        final PropertyInfo info = new PropertyInfo();
        info.setReadable(readable);
        info.setWritable(writable);
        try {
            if (readable) {
                info.setReadMethod(hostClass_.getMethod(GETTER_PREFIX + name, (Class []) null));
            }
        }
        catch (final NoSuchMethodException e) {
            throw new IllegalStateException("Method '" + GETTER_PREFIX + name + "' was not found for "
                + name + " property in " + hostClass_.getName());
        }
        // For the setters, we have to loop through the methods since we do not know what type of argument
        // the method takes.
        if (writable) {
            final String setMethodName = SETTER_PREFIX + name;
            for (final Method method : hostClass_.getMethods()) {
                if (method.getName().equals(setMethodName) && method.getParameterTypes().length == 1) {
                    info.setWriteMethod(method);
                    break;
                }
            }
            if (info.getWriteMethod() == null) {
                throw new IllegalStateException("Method '" + SETTER_PREFIX + name + "' was not found for " + name
                    + " property in " + hostClass_.getName());
            }
        }
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
     * Returns the set of keys for the defined properties.
     * @return a set
     */
    public Set<String> propertyKeys() {
        return propertyMap_.keySet();
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
     * @param name - Name of the function
     */
    public void addFunction(final String name) {
        final FunctionInfo info = new FunctionInfo();
        final String setMethodName = FUNCTION_PREFIX + name;
        for (final Method method : hostClass_.getMethods()) {
            if (method.getName().equals(setMethodName)) {
                info.setFunctionMethod(method);
                break;
            }
        }
        if (info.getFunctionMethod() == null) {
            throw new IllegalStateException("Method '" + FUNCTION_PREFIX + name + "' was not found for " + name
                + " function in " + hostClass_.getName());
        }
        functionMap_.put(name, info);
    }

    /**
     * Adds properties and function definitions from the provided configuration.
     * @param virtualClassConfig the config to take definitions from.
     */
    void addAllDefinitions(final ClassConfiguration virtualClassConfig) {
        if (!virtualClassConfig.getHostClass().isAssignableFrom(getHostClass())) {
            throw new RuntimeException("Can't configure " + getHostClass() + " with info from "
                + virtualClassConfig.getHostClass());
        }
        propertyMap_.putAll(virtualClassConfig.propertyMap_);
        functionMap_.putAll(virtualClassConfig.functionMap_);
        constants_.addAll(virtualClassConfig.constants_);
    }

    /**
     * Sets the browser information for this named property.
     * @param propertyName - Name of the property to set
     * @param browserName - Browser name to set
     * @throws IllegalStateException - Property does not exist
     */
    public void setBrowser(final String propertyName, final String browserName)
        throws IllegalStateException {
        final PropertyInfo property = getPropertyInfo(propertyName);
        if (property == null) {
            throw new IllegalStateException("Property does not exist to set browser");
        }
        property.setBrowser(new BrowserInfo(browserName));
    }

    /**
     * @return the extendedClass
     */
    public String getExtendedClassName() {
        return extendedClassName_;
    }

    /**
     * @param extendedClass the extendedClass to set
     */
    public void setExtendedClassName(final String extendedClass) {
        extendedClassName_ = extendedClass;
    }

    /**
     * Returns the PropertyInfo for the given property name.
     * @param propertyName Name of property
     * @return the PropertyInfo for the given property name
     */
    protected PropertyInfo getPropertyInfo(final String propertyName) {
        return propertyMap_.get(propertyName);
    }

    private FunctionInfo getFunctionInfo(final String functionName) {
        return functionMap_.get(functionName);
    }

    /**
     * Test for value equality of the 2 objects.
     *
     * @param obj   the reference object with which to compare
     * @return <code>true</code> if the value of this object is the same as the obj
     * argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ClassConfiguration)) {
            return false;
        }
        final ClassConfiguration config = (ClassConfiguration) obj;
        if (propertyMap_.size() != config.propertyMap_.size()) {
            return false;
        }
        if (functionMap_.size() != config.functionMap_.size()) {
            return false;
        }
        final Set<String> keys = config.propertyMap_.keySet();
        for (final String key : keys) {
            if (!config.propertyMap_.get(key).valueEquals(propertyMap_.get(key))) {
                return false;
            }
        }

        for (final String key : config.functionMap_.keySet()) {
            if (!config.functionMap_.get(key).valueEquals(functionMap_.get(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Currently, this is the hashcode for the linkedClass name.
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hostClass_.getName().hashCode();
    }

    /**
     * Gets the method that implements the getter for the named property.
     *
     * @param propertyName the name of the property
     * @return the method that implements the getter for the named property
     */
    public Method getPropertyReadMethod(final String propertyName) {
        final PropertyInfo info = getPropertyInfo(propertyName);
        if (info == null) {
            return null;
        }
        return info.getReadMethod();
    }

    /**
     * Gets the method that implements the setter for the named property.
     *
     * @param propertyName the name of the property
     * @return the method that implements the setter for the named property
     */
    public Method getPropertyWriteMethod(final String propertyName) {
        final PropertyInfo info = getPropertyInfo(propertyName);
        if (info == null) {
            return null;
        }
        return info.getWriteMethod();
    }

    /**
     * Gets the method that implements the given function.
     *
     * @param functionName the name of the property
     * @return the method that implements the given function
     */
    public Method getFunctionMethod(final String functionName) {
        final FunctionInfo info = getFunctionInfo(functionName);
        if (info == null) {
            return null;
        }
        return info.getFunctionMethod();
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
     * @return the htmlClassname
     */
    public String getHtmlClassname() {
        return htmlClassName_;
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
    protected static class PropertyInfo {
        private boolean readable_ = false;
        private boolean writable_ = false;
        private boolean hasBrowsers_ = false;
        private Map<String, BrowserInfo> browserMap_;
        private Method readMethod_;
        private Method writeMethod_;

        /**
         * @return the readMethod
         */
        public Method getReadMethod() {
            return readMethod_;
        }

        /**
         * @param readMethod the readMethod to set
         */
        public void setReadMethod(final Method readMethod) {
            readMethod_ = readMethod;
        }

        /**
         * @return the writeMethod
         */
        public Method getWriteMethod() {
            return writeMethod_;
        }

        /**
         * @param writeMethod the writeMethod to set
         */
        public void setWriteMethod(final Method writeMethod) {
            writeMethod_ = writeMethod;
        }

        private void setBrowser(final BrowserInfo browserInfo) {
            if (browserMap_ == null) {
                hasBrowsers_ = true;
                browserMap_ = new HashMap<String, BrowserInfo>();
            }

            browserMap_.put(browserInfo.getBrowserName(), browserInfo);
        }

        /**
         * Test for value equality of the 2 objects
         *
         * @param obj   the reference object with which to compare
         * @return <code>true</code> if the value of this object is the same as the obj
         * argument; <code>false</code> otherwise.
         */
        private boolean valueEquals(final Object obj) {
            if (!(obj instanceof PropertyInfo)) {
                return false;
            }
            final PropertyInfo info = (PropertyInfo) obj;
            if (hasBrowsers_ != info.hasBrowsers_) {
                return false;
            }
            if (hasBrowsers_) {
                if (browserMap_.size() != info.browserMap_.size()) {
                    return false;
                }
                for (final Map.Entry<String, BrowserInfo> entry : browserMap_.entrySet()) {
                    if (!entry.getValue().valueEquals(info.browserMap_.get(entry.getKey()))) {
                        return false;
                    }
                }

            }
            return (readable_ == info.readable_)
                && (writable_ == info.writable_);
        }

        /**
         * @param readable the readable to set
         */
        private void setReadable(final boolean readable) {
            readable_ = readable;
        }

        /**
         * @param writable the writable to set
         */
        private void setWritable(final boolean writable) {
            writable_ = writable;
        }
    }

    private static class FunctionInfo {
        private boolean hasBrowsers_ = false;
        private Map<String, BrowserInfo> browserMap_;
        private Method functionMethod_;

        /**
         * Test for value equality of the 2 objects
         *
         * @param obj   the reference object with which to compare
         * @return <code>true</code> if the value of this object is the same as the obj
         * argument; <code>false</code> otherwise.
         */
        private boolean valueEquals(final Object obj) {
            if (!(obj instanceof FunctionInfo)) {
                return false;
            }
            final FunctionInfo info = (FunctionInfo) obj;
            if (hasBrowsers_ != info.hasBrowsers_) {
                return false;
            }
            if (hasBrowsers_) {
                if (browserMap_.size() != info.browserMap_.size()) {
                    return false;
                }
                for (final Map.Entry<String, BrowserInfo> entry : browserMap_.entrySet()) {
                    if (entry.getValue().valueEquals(info.browserMap_.get(entry.getKey()))) {
                        return false;
                    }
                }

            }
            return true;
        }

        /**
         * @return the functionMethod
         */
        public Method getFunctionMethod() {
            return functionMethod_;
        }

        /**
         * @param functionMethod the functionMethod to set
         */
        public void setFunctionMethod(final Method functionMethod) {
            functionMethod_ = functionMethod;
        }
    }

    private static final class BrowserInfo {
        private String browserName_;
        private String minVersion_;
        private String maxVersion_;
        private String lessThanVersion_;

        /**
         * Test for value equality of the 2 objects.
         *
         * @param obj the reference object with which to compare
         * @return <code>true</code> if the value of this object is the same as the obj
         * argument; <code>false</code> otherwise.
         */
        private boolean valueEquals(final Object obj) {
            if (!(obj instanceof BrowserInfo)) {
                return false;
            }
            final BrowserInfo info = (BrowserInfo) obj;
            if (minVersion_ != null && !minVersion_.equals(info.minVersion_)) {
                return false;
            }
            if (maxVersion_ != null && !maxVersion_.equals(info.maxVersion_)) {
                return false;
            }
            if (lessThanVersion_ != null && !lessThanVersion_.equals(info.lessThanVersion_)) {
                return false;
            }
            return (browserName_ == info.browserName_);
        }

        /**
         * @param browserName name of the browser
         */
        private BrowserInfo(final String browserName) {
            browserName_ = browserName;
        }

        /**
         * @return the browserName
         */
        private String getBrowserName() {
            return browserName_;
        }
    }

}
