/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * An abstract container for all the JavaScript configuration information.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public abstract class AbstractJavaScriptConfiguration {

    private static final Log LOG = LogFactory.getLog(AbstractJavaScriptConfiguration.class);

    private static final Map<String, String> CLASS_NAME_MAP_ = new HashMap<String, String>();

    private Map<Class<?>, Class<? extends SimpleScriptable>> domJavaScriptMap_;

    private final Map<String, ClassConfiguration> configuration_;

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
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
        final Map<String, ClassConfiguration> classMap =
                new HashMap<String, ClassConfiguration>(getClasses().length);

        for (final Class<? extends SimpleScriptable> klass : getClasses()) {
            final ClassConfiguration config = processClass(klass, browser);
            if (config != null) {
                classMap.put(config.getHostClass().getSimpleName(), config);
            }
        }
        return Collections.unmodifiableMap(classMap);
    }

    private ClassConfiguration processClass(final Class<? extends SimpleScriptable> klass,
        final BrowserVersion browser) {
        if (browser != null) {
            final String expectedBrowserName;
            if (browser.isIE()) {
                expectedBrowserName = "IE";
            }
            else if (browser.isFirefox()) {
                expectedBrowserName = "FF";
            }
            else {
                expectedBrowserName = "CHROME";
            }
            final float browserVersionNumeric = browser.getBrowserVersionNumeric();

            final String hostClassName = klass.getName();
            final JsxClasses jsxClasses = klass.getAnnotation(JsxClasses.class);
            if (jsxClasses != null
                    && isSupported(jsxClasses.browsers(), expectedBrowserName, browserVersionNumeric)) {
                if (klass.getAnnotation(JsxClass.class) != null) {
                    throw new RuntimeException("Invalid JsxClasses/JsxClass annotation; class '"
                        + hostClassName + "' has both.");
                }
                final boolean isJsObject = jsxClasses.isJSObject();
                final JsxClass[] jsxClassValues = jsxClasses.value();

                final Set<Class<?>> domClasses = new HashSet<Class<?>>();

                for (int i = 0; i < jsxClassValues.length; i++) {
                    final JsxClass jsxClass = jsxClassValues[i];

                    if (jsxClass != null
                            && isSupported(jsxClass.browsers(), expectedBrowserName, browserVersionNumeric)) {
                        domClasses.add(jsxClass.domClass());
                    }
                }

                final ClassConfiguration classConfiguration =
                        new ClassConfiguration(klass, domClasses.toArray(new Class<?>[0]), isJsObject);

                process(classConfiguration, hostClassName, expectedBrowserName,
                        browserVersionNumeric);
                return classConfiguration;
            }

            final JsxClass jsxClass = klass.getAnnotation(JsxClass.class);
            if (jsxClass != null && isSupported(jsxClass.browsers(), expectedBrowserName, browserVersionNumeric)) {

                final Set<Class<?>> domClasses = new HashSet<Class<?>>();
                final Class<?> domClass = jsxClass.domClass();
                if (domClass != null && domClass != Object.class) {
                    domClasses.add(domClass);
                }

                final ClassConfiguration classConfiguration
                    = new ClassConfiguration(klass, domClasses.toArray(new Class<?>[0]), true);

                process(classConfiguration, hostClassName, expectedBrowserName,
                        browserVersionNumeric);
                return classConfiguration;
            }
        }
        return null;
    }

    private void process(final ClassConfiguration classConfiguration,
            final String hostClassName, final String expectedBrowserName,
            final float browserVersionNumeric) {
        final String simpleClassName = hostClassName.substring(hostClassName.lastIndexOf('.') + 1);

//        if (ClassnameMap_.containsKey(hostClassName)) {
//            throw new RuntimeException("Invalid JsxClasses/JsxClass configuration; two mappings for class '"
//                + hostClassName + "'.");
//        }
        CLASS_NAME_MAP_.put(hostClassName, simpleClassName);
        final Map<String, Method> allGetters = new HashMap<String, Method>();
        final Map<String, Method> allSetters = new HashMap<String, Method>();
        for (final Method method : classConfiguration.getHostClass().getDeclaredMethods()) {
            for (final Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof JsxGetter) {
                    final JsxGetter jsxGetter = (JsxGetter) annotation;
                    if (isSupported(jsxGetter.value(), expectedBrowserName, browserVersionNumeric)) {
                        String property;
                        if (jsxGetter.propertyName().isEmpty()) {
                            property = method.getName().substring(3);
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
                    if (isSupported(((JsxFunction) annotation).value(),
                            expectedBrowserName, browserVersionNumeric)) {
                        classConfiguration.addFunction(method);
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
            if (jsxConstant != null
                    && isSupported(jsxConstant.value(), expectedBrowserName, browserVersionNumeric)) {
                classConfiguration.addConstant(field.getName());
            }
        }
        for (final Entry<String, Method> getterEntry : allGetters.entrySet()) {
            final String property = getterEntry.getKey();
            classConfiguration.addProperty(property,
                    getterEntry.getValue(), allSetters.get(property));
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
    public Map<Class<?>, Class<? extends SimpleScriptable>> getDomJavaScriptMapping() {
        if (domJavaScriptMap_ != null) {
            return domJavaScriptMap_;
        }

        final Map<Class<?>, Class<? extends SimpleScriptable>> map = new HashMap<Class<?>,
            Class<? extends SimpleScriptable>>();

        for (String hostClassName : configuration_.keySet()) {
            ClassConfiguration classConfig = getClassConfiguration(hostClassName);
            for (final Class<?> domClass : classConfig.getDomClasses()) {
                // preload and validate that the class exists
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Mapping " + domClass.getName() + " to " + hostClassName);
                }
                while (!classConfig.isJsObject()) {
                    hostClassName = classConfig.getExtendedClassName();
                    classConfig = getClassConfiguration(hostClassName);
                }
                map.put(domClass, classConfig.getHostClass());
            }
        }

        domJavaScriptMap_ = Collections.unmodifiableMap(map);

        return domJavaScriptMap_;
    }
}
