/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A container for all the javascript configuration information.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 */
public final class ClassConfiguration {
    private HashMap propertyMap_ = new HashMap();
    private HashMap functionMap_ = new HashMap();
    private String extendedClass_;
    /**
     * The fully qualified name of the class that implements this class.
     */
    private String className_;
    private Class linkedClass_;
    
    /**
     * Constructor
     * 
     * @param classname the name of the configuration class this entry is for
     * @param implementingClass - the fully qualified name of the class implimenting this functionality
     * @param extendedClass - The name of the class that this class extends
     * @throws ClassNotFoundException - If the implementing class is not found
     */
    public ClassConfiguration(final String classname, final String implementingClass, final String extendedClass) 
        throws ClassNotFoundException {
        init(classname, implementingClass, extendedClass);
    }
    
    /**
     * Constructor for when there is not an extending class
     * 
     * @param classname the name of the configuration class this entry is for
     * @param implementingClass - the fully qualified name of the class implimenting this functionality
     * @throws ClassNotFoundException - If the implementing class is not found
     */
    public ClassConfiguration(final String classname, final String implementingClass) throws ClassNotFoundException {
        init(classname, implementingClass, "");
    }

    private void init(final String classname, final String implementingClass, final String extendedClass) 
        throws ClassNotFoundException {
        className_ = classname;
        extendedClass_ = extendedClass;
        linkedClass_ = Class.forName(implementingClass);
    }
    
    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className_;
    }

    /**
     * @param className The className to set.
     */
    public void setClassName(final String className) {
        className_ = className;
    }

    /**
     * Add the property to the configuration
     * @param name - Name of the property
     * @param readable - Flag for if the property is readable
     * @param writeable - Flag for if the property is writeable
     */
    public void addProperty(final String name, final boolean readable, final boolean writeable) {
        final PropertyInfo info = new PropertyInfo();
        info.setReadable(readable);
        info.setWriteable(writeable);
        try {
            if (readable) {
                info.setReadMethod(linkedClass_.getMethod("jsGet_" + name, null));
            }
        }
        catch (final NoSuchMethodException e) {
            throw new IllegalStateException("Method 'jsGet_" + name + "' was not found for " + name + " property in " +
                linkedClass_.getName());
        }
        // For the setters, we have to loop through the methods since we do not know what type of argument
        // the method takes.
        if (writeable) {
            final Method[] methods = linkedClass_.getMethods();
            final String setMethodName = "jsSet_" + name;
            for( int i=0; i<methods.length; i++ ) {
                if( methods[i].getName().equals(setMethodName) && methods[i].getParameterTypes().length == 1 ) {
                    info.setWriteMethod(methods[i]);
                    break;
                }
            }
            if(info.getWriteMethod() == null) {
                throw new IllegalStateException("Method 'jsSet_" + name + "' was not found for " + name 
                    + " property in " + linkedClass_.getName());
            }
        }
        propertyMap_.put(name, info);
    }
    
    /**
     * Add the function to the configuration
     * @param name - Name of the function
     */
    public void addFunction(final String name) {
        final FunctionInfo info = new FunctionInfo();
        final Method[] methods = linkedClass_.getMethods();
        final String setMethodName = "jsFunction_" + name;
        for( int i=0; i<methods.length; i++ ) {
            if( methods[i].getName().equals(setMethodName)) {
                info.setFunctionMethod(methods[i]);
                break;
            }
        }
        if(info.getFunctionMethod() == null) {
            throw new IllegalStateException("Method 'jsFunction_" + name + "' was not found for " + name 
                + " function in " + linkedClass_.getName());
        }
        functionMap_.put(name, info);
    }

    
    /**
     * Set the browser information for this named property
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
     * @return Returns the extendedClass.
     */
    public String getExtendedClass() {
        return extendedClass_;
    }

    /**
     * @param extendedClass The extendedClass to set.
     */
    public void setExtendedClass(final String extendedClass) {
        extendedClass_ = extendedClass;
    }
    
    /**
     * Return the PropertyInfo for the given property name
     * @param propertyName Name of property
     * @return ClassConfiguration.PropertyInfo
     */
    protected PropertyInfo getPropertyInfo(final String propertyName) {
        return (PropertyInfo) propertyMap_.get(propertyName);
    }
    

    private FunctionInfo getFunctionInfo(final String functionName) {
        return (FunctionInfo) functionMap_.get(functionName);
    }

    /**
     * Test for value equality of the 2 objects
     * 
     * @param   obj   the reference object with which to compare.
     * @return  <code>true</code> if the value of this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    public boolean equals(final Object obj) {
        if (! (obj instanceof ClassConfiguration)) {
            return false;
        }
        final ClassConfiguration config = (ClassConfiguration) obj;
        if (propertyMap_.size() != config.propertyMap_.size()) {
            return false;
        }
        if (functionMap_.size() != config.functionMap_.size()) {
            return false;
        }
        final Set keys = config.propertyMap_.keySet();
        final Iterator it = keys.iterator();
        while (it.hasNext()) {
            final String key = (String) it.next();
            if (!(((PropertyInfo)config.propertyMap_.get(key)).valueEquals(propertyMap_.get(key)))) {
                return false;
            }
        }
        
        final Set fkeys = config.functionMap_.keySet();
        final Iterator fit = fkeys.iterator();
        while (fit.hasNext()) {
            final String fkey = (String) fit.next();
            if (!(((FunctionInfo)config.functionMap_.get(fkey)).valueEquals(functionMap_.get(fkey)))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Generate a hashCode for this object.  Currently, this is the hashcode for the name.
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return className_.hashCode();
    }
    
    /**
     * Gets the method that implements the getter for the named property
     * 
     * @param propertyName The name of the property
     * @return Method
     */
    public Method getPropertyReadMethod(final String propertyName) {
        final PropertyInfo info = getPropertyInfo(propertyName);
        if (info == null) {
            return null;
        }
        return info.getReadMethod();
    }
    
    /**
     * Gets the method that implements the setter for the named property
     * 
     * @param propertyName The name of the property
     * @return Method
     */
    public Method getPropertyWriteMethod(final String propertyName) {
        final PropertyInfo info = getPropertyInfo(propertyName);
        if (info == null) {
            return null;
        }
        return info.getWriteMethod();
    }
    
    /**
     * Gets the method that implements the given function
     * 
     * @param functionName The name of the property
     * @return Method
     */
    public Method getFunctionMethod(final String functionName) {
        final FunctionInfo info = getFunctionInfo(functionName);
        if (info == null) {
            return null;
        }
        return info.getFunctionMethod();
    }
    
    /**
     * 
     * @return Returns the linkedClass.
     */
    public Class getLinkedClass() {
        return linkedClass_;
    }

    /**
     * Class used to contain the property information if the property is readable, writeable and the
     * methods that implement the get and set functions.
     */
    protected class PropertyInfo {
        private boolean readable_ = false;
        private boolean writeable_ = false;
        private boolean hasBrowsers_ = false;
        private Map browserMap_;
        private Method readMethod_;
        private Method writeMethod_;

        /**
         * @return Returns the readMethod.
         */
        public Method getReadMethod() {
            return readMethod_;
        }

        /**
         * @param readMethod The readMethod to set.
         */
        public void setReadMethod(final Method readMethod) {
            readMethod_ = readMethod;
        }

        /**
         * @return Returns the writeMethod.
         */
        public Method getWriteMethod() {
            return writeMethod_;
        }

        /**
         * @param writeMethod The writeMethod to set.
         */
        public void setWriteMethod(final Method writeMethod) {
            writeMethod_ = writeMethod;
        }
        
        private void setBrowser(final BrowserInfo browserInfo) {
            if (browserMap_ == null) {
                hasBrowsers_ = true;
                browserMap_ = new HashMap();
            }
            
            browserMap_.put(browserInfo.getBrowserName(), browserInfo);
        }
        
        /**
         * Test for value equality of the 2 objects
         * 
         * @param   obj   the reference object with which to compare.
         * @return  <code>true</code> if the value of this object is the same as the obj
         *          argument; <code>false</code> otherwise.
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
                final Set keys = browserMap_.keySet();
                final Iterator it = keys.iterator();
                while (it.hasNext()) {
                    final String key = (String) it.next();
                    if (!(((BrowserInfo)browserMap_.get(key)).valueEquals(info.browserMap_.get(key)))) {
                        return false;
                    }
                }

            }
            return (readable_ == info.readable_) &&
                (writeable_ == info.writeable_);
        }
    
        /**
         * @param readable The readable to set.
         */
        private void setReadable(final boolean readable) {
            readable_ = readable;
        }

        /**
         * @param writeable The writeable to set.
         */
        private void setWriteable(final boolean writeable) {
            writeable_ = writeable;
        }
    }

    private class FunctionInfo {
        private boolean hasBrowsers_ = false;
        private Map browserMap_;
        private Method functionMethod_;
        
        /**
         * Test for value equality of the 2 objects
         * 
         * @param   obj   the reference object with which to compare.
         * @return  <code>true</code> if the value of this object is the same as the obj
         *          argument; <code>false</code> otherwise.
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
                final Set keys = browserMap_.keySet();
                final Iterator it = keys.iterator();
                while (it.hasNext()) {
                    final String key = (String) it.next();
                    if (!(((BrowserInfo)browserMap_.get(key)).valueEquals(info.browserMap_.get(key)))) {
                        return false;
                    }
                }

            }
            return true;
        }

        /**
         * @return Returns the functionMethod.
         */
        public Method getFunctionMethod() {
            return functionMethod_;
        }

        /**
         * @param functionMethod The functionMethod to set.
         */
        public void setFunctionMethod(final Method functionMethod) {
            functionMethod_ = functionMethod;
        }
    }

    private class BrowserInfo {
        private String browserName_;
        private String minVersion_;
        private String maxVersion_;
        private String lessThanVersion_;
        
        /**
         * Test for value equality of the 2 objects
         * 
         * @param   obj   the reference object with which to compare.
         * @return  <code>true</code> if the value of this object is the same as the obj
         *          argument; <code>false</code> otherwise.
         */
        private boolean valueEquals(final Object obj) {
            if (!(obj instanceof BrowserInfo)) {
                return false;
            }
            final BrowserInfo info = (BrowserInfo) obj;
            if (minVersion_ != null) {
                if (! minVersion_.equals(info.minVersion_)) {
                    return false;
                }
            }
            if (maxVersion_ != null) {
                if (! maxVersion_.equals(info.maxVersion_)) {
                    return false;
                }
            }
            if (lessThanVersion_ != null) {
                if (! lessThanVersion_.equals(info.lessThanVersion_)) {
                    return false;
                }
            }
            return (browserName_ == info.browserName_);
        }

        /**
         * @param browserName - Name of the browser 
         */
        public BrowserInfo(final String browserName) {
            browserName_ = browserName;
        }

        /**
         * @return Returns the browserName.
         */
        private String getBrowserName() {
            return browserName_;
        }
    }
}
