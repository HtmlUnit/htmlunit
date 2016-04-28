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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An abstract container for all the JavaScript configuration information.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public abstract class AbstractJavaScriptConfiguration {

    private static final Log LOG = LogFactory.getLog(AbstractJavaScriptConfiguration.class);

    private static final Map<String, String> CLASS_NAME_MAP_ = new HashMap<>();

    private Map<Class<?>, Class<? extends HtmlUnitScriptable>> domJavaScriptMap_;

    private final Map<String, ClassConfiguration> configuration_;

    /**
     * Constructor.
     * @param browser the browser version to use
     */
    protected AbstractJavaScriptConfiguration(final BrowserVersion browser) {
        configuration_ = buildUsageMap(browser);
    }

    /**
     * @return the classes configured by this configuration
     */
    protected abstract Class<? extends SimpleScriptable>[] getClasses();

    /**
     * Gets all the configurations.
     * @return the class configurations
     */
    public Iterable<ClassConfiguration> getAll() {
        return configuration_.values();
    }

    private Map<String, ClassConfiguration> buildUsageMap(final BrowserVersion browser) {
        final Map<String, ClassConfiguration> classMap = new HashMap<>(getClasses().length);

        for (final Class<? extends SimpleScriptable> klass : getClasses()) {
            final ClassConfiguration config = getClassConfiguration(klass, browser);
            if (config != null) {
                classMap.put(config.getClassName(), config);
            }
        }
        return Collections.unmodifiableMap(classMap);
    }

    /**
     * Returns the class configuration of the given {@code klass}.
     *
     * @param klass the class
     * @param browser the browser version
     * @return the class configuration
     */
    public static ClassConfiguration getClassConfiguration(final Class<? extends HtmlUnitScriptable> klass,
        final BrowserVersion browser) {
        if (browser != null) {
            final String expectedBrowserName;
            if (browser.isIE()) {
                expectedBrowserName = "IE";
            }
            else if (browser.isFirefox()) {
                expectedBrowserName = "FF";
            }
            else if (browser.isEdge()) {
                expectedBrowserName = "EDGE";
            }
            else {
                expectedBrowserName = "CHROME";
            }
            final float browserVersionNumeric = browser.getBrowserVersionNumeric();

            final String hostClassName = klass.getName();
            final JsxClasses jsxClasses = klass.getAnnotation(JsxClasses.class);
            if (jsxClasses != null) {
                if (klass.getAnnotation(JsxClass.class) != null) {
                    throw new RuntimeException("Invalid JsxClasses/JsxClass annotation; class '"
                        + hostClassName + "' has both.");
                }
                final JsxClass[] jsxClassValues = jsxClasses.value();
                if (jsxClassValues.length == 1) {
                    throw new RuntimeException("No need to specify JsxClasses with a single JsxClass for "
                            + hostClassName);
                }
                final Set<Class<?>> domClasses = new HashSet<>();

                boolean isJsObject = false;
                boolean isDefinedInStandardsMode = false;
                String className = null;
                for (int i = 0; i < jsxClassValues.length; i++) {
                    final JsxClass jsxClass = jsxClassValues[i];

                    if (jsxClass != null
                            && isSupported(jsxClass.browsers(), expectedBrowserName, browserVersionNumeric)) {
                        domClasses.add(jsxClass.domClass());
                        if (jsxClass.isJSObject()) {
                            isJsObject = true;
                        }
                        if (jsxClass.isDefinedInStandardsMode()) {
                            isDefinedInStandardsMode = true;
                        }
                        if (!jsxClass.className().isEmpty()) {
                            className = jsxClass.className();
                        }
                    }
                }

                final ClassConfiguration classConfiguration =
                        new ClassConfiguration(klass, domClasses.toArray(new Class<?>[0]), isJsObject,
                                isDefinedInStandardsMode, className);

                process(classConfiguration, hostClassName, expectedBrowserName, browserVersionNumeric);
                return classConfiguration;
            }

            final JsxClass jsxClass = klass.getAnnotation(JsxClass.class);
            if (jsxClass != null && isSupported(jsxClass.browsers(), expectedBrowserName, browserVersionNumeric)) {

                final Set<Class<?>> domClasses = new HashSet<>();
                final Class<?> domClass = jsxClass.domClass();
                if (domClass != null && domClass != Object.class) {
                    domClasses.add(domClass);
                }

                String className = jsxClass.className();
                if (className.isEmpty()) {
                    className = null;
                }
                final ClassConfiguration classConfiguration
                    = new ClassConfiguration(klass, domClasses.toArray(new Class<?>[0]), jsxClass.isJSObject(),
                            jsxClass.isDefinedInStandardsMode(), className);

                process(classConfiguration, hostClassName, expectedBrowserName, browserVersionNumeric);
                return classConfiguration;
            }
        }
        return null;
    }

    private static void process(final ClassConfiguration classConfiguration,
            final String hostClassName, final String expectedBrowserName,
            final float browserVersionNumeric) {
        final String simpleClassName = hostClassName.substring(hostClassName.lastIndexOf('.') + 1);

        CLASS_NAME_MAP_.put(hostClassName, simpleClassName);
        final Map<String, Method> allGetters = new HashMap<>();
        final Map<String, Method> allSetters = new HashMap<>();
        for (final Constructor<?> constructor : classConfiguration.getHostClass().getDeclaredConstructors()) {
            for (final Annotation annotation : constructor.getAnnotations()) {
                if (annotation instanceof JsxConstructor) {
                    if (isSupported(((JsxConstructor) annotation).value(),
                            expectedBrowserName, browserVersionNumeric)) {
                        classConfiguration.setJSConstructor(constructor);
                    }
                }
            }
        }
        for (final Method method : classConfiguration.getHostClass().getDeclaredMethods()) {
            for (final Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof JsxGetter) {
                    final JsxGetter jsxGetter = (JsxGetter) annotation;
                    if (isSupported(jsxGetter.value(), expectedBrowserName, browserVersionNumeric)) {
                        String property;
                        if (jsxGetter.propertyName().isEmpty()) {
                            final int prefix = method.getName().startsWith("is") ? 2 : 3;
                            property = method.getName().substring(prefix);
                            property = Character.toLowerCase(property.charAt(0)) + property.substring(1);
                        }
                        else {
                            property = jsxGetter.propertyName();
                        }
                        allGetters.put(property, method);
                    }
                }
                else if (annotation instanceof JsxSetter) {
                    final JsxSetter jsxSetter = (JsxSetter) annotation;
                    if (isSupported(jsxSetter.value(), expectedBrowserName, browserVersionNumeric)) {
                        String property;
                        if (jsxSetter.propertyName().isEmpty()) {
                            property = method.getName().substring(3);
                            property = Character.toLowerCase(property.charAt(0)) + property.substring(1);
                        }
                        else {
                            property = jsxSetter.propertyName();
                        }
                        allSetters.put(property, method);
                    }
                }
                else if (annotation instanceof JsxFunction) {
                    final JsxFunction jsxFunction = (JsxFunction) annotation;
                    if (isSupported(jsxFunction.value(), expectedBrowserName, browserVersionNumeric)) {
                        final String name;
                        if (jsxFunction.functionName().isEmpty()) {
                            name = method.getName();
                        }
                        else {
                            name = jsxFunction.functionName();
                        }
                        classConfiguration.addFunction(name, method);
                    }
                }
                else if (annotation instanceof JsxStaticGetter) {
                    final JsxStaticGetter jsxStaticGetter = (JsxStaticGetter) annotation;
                    if (isSupported(jsxStaticGetter.value(), expectedBrowserName, browserVersionNumeric)) {
                        final int prefix = method.getName().startsWith("is") ? 2 : 3;
                        String property = method.getName().substring(prefix);
                        property = Character.toLowerCase(property.charAt(0)) + property.substring(1);
                        classConfiguration.addStaticProperty(property, method, null);
                    }
                }
                else if (annotation instanceof JsxStaticFunction) {
                    final JsxStaticFunction jsxStaticFunction = (JsxStaticFunction) annotation;
                    if (isSupported(jsxStaticFunction.value(), expectedBrowserName, browserVersionNumeric)) {
                        final String name;
                        if (jsxStaticFunction.functionName().isEmpty()) {
                            name = method.getName();
                        }
                        else {
                            name = jsxStaticFunction.functionName();
                        }
                        classConfiguration.addStaticFunction(name, method);
                    }
                }
                else if (annotation instanceof JsxConstructor) {
                    if (isSupported(((JsxConstructor) annotation).value(),
                            expectedBrowserName, browserVersionNumeric)) {
                        classConfiguration.setJSConstructor(method);
                    }
                }
            }
        }
        for (final Field field : classConfiguration.getHostClass().getDeclaredFields()) {
            final JsxConstant jsxConstant = field.getAnnotation(JsxConstant.class);
            if (jsxConstant != null && isSupported(jsxConstant.value(), expectedBrowserName, browserVersionNumeric)) {
                classConfiguration.addConstant(field.getName());
            }
        }
        for (final Entry<String, Method> getterEntry : allGetters.entrySet()) {
            final String property = getterEntry.getKey();
            classConfiguration.addProperty(property, getterEntry.getValue(), allSetters.get(property));
        }
    }

    private static boolean isSupported(final WebBrowser[] browsers, final String expectedBrowserName,
            final float expectedVersionNumeric) {
        for (final WebBrowser browser : browsers) {
            if (browser.value().name().equals(expectedBrowserName)
                    && browser.minVersion() <= expectedVersionNumeric
                    && browser.maxVersion() >= expectedVersionNumeric) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the class configuration for the supplied JavaScript class name.
     * @param hostClassName the JavaScript class name
     * @return the class configuration for the supplied JavaScript class name
     */
    public ClassConfiguration getClassConfiguration(final String hostClassName) {
        return configuration_.get(hostClassName);
    }

    /**
     * Returns the class name that the given class implements. If the class is
     * the input class, then the name is extracted from the type that the Input class
     * is masquerading as.
     * FIXME - Implement the Input class processing
     * @param clazz
     * @return the class name
     */
    String getClassnameForClass(final Class<?> clazz) {
        final String name = CLASS_NAME_MAP_.get(clazz.getName());
        if (name == null) {
            throw new IllegalStateException("Did not find the mapping of the class to the classname for "
                + clazz.getName());
        }
        return name;
    }

    /**
     * Returns an immutable map containing the DOM to JavaScript mappings. Keys are
     * java classes for the various DOM classes (e.g. HtmlInput.class) and the values
     * are the JavaScript class names (e.g. "HTMLAnchorElement").
     * @return the mappings
     */
    public Map<Class<?>, Class<? extends HtmlUnitScriptable>> getDomJavaScriptMapping() {
        if (domJavaScriptMap_ != null) {
            return domJavaScriptMap_;
        }

        final Map<Class<?>, Class<? extends HtmlUnitScriptable>> map = new HashMap<>(configuration_.size());

        final boolean debug = LOG.isDebugEnabled();
        for (final String hostClassName : configuration_.keySet()) {
            final ClassConfiguration classConfig = getClassConfiguration(hostClassName);
            for (final Class<?> domClass : classConfig.getDomClasses()) {
                // preload and validate that the class exists
                if (debug) {
                    LOG.debug("Mapping " + domClass.getName() + " to " + hostClassName);
                }
                map.put(domClass, classConfig.getHostClass());
            }
        }

        domJavaScriptMap_ = Collections.unmodifiableMap(map);

        return domJavaScriptMap_;
    }
}
