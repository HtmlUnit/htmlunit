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

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride;
import com.gargoylesoftware.htmlunit.html.HtmlBig;
import com.gargoylesoftware.htmlunit.html.HtmlBlink;
import com.gargoylesoftware.htmlunit.html.HtmlBlockQuote;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlCenter;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlHeading4;
import com.gargoylesoftware.htmlunit.html.HtmlHeading5;
import com.gargoylesoftware.htmlunit.html.HtmlHeading6;
import com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlMarquee;
import com.gargoylesoftware.htmlunit.html.HtmlMultiColumn;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlNoEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import com.gargoylesoftware.htmlunit.html.HtmlS;
import com.gargoylesoftware.htmlunit.html.HtmlSample;
import com.gargoylesoftware.htmlunit.html.HtmlSmall;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlStrike;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubscript;
import com.gargoylesoftware.htmlunit.html.HtmlSuperscript;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumn;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup;
import com.gargoylesoftware.htmlunit.html.HtmlTableFooter;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlUnderlined;
import com.gargoylesoftware.htmlunit.html.HtmlVariable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadingElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLQuoteElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement;

/**
 * A container for all the JavaScript configuration information.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public final class JavaScriptConfiguration {

    private static final Log LOG = LogFactory.getLog(JavaScriptConfiguration.class);

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static Map<BrowserVersion, JavaScriptConfiguration> ConfigurationMap_ =
        new HashMap<BrowserVersion, JavaScriptConfiguration>(11);

    private static Map<String, String> ClassnameMap_ = new HashMap<String, String>();

    private Map<Class<? extends DomElement>, Class<? extends SimpleScriptable>> domJavaScriptMap_;

    private final Map<String, ClassConfiguration> configuration_;

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    private JavaScriptConfiguration(final BrowserVersion browser) {
        configuration_ = buildUsageMap(browser);
    }

    /**
     * Returns the instance that represents the configuration for the specified {@link BrowserVersion}.
     * This method is synchronized to allow multi-threaded access to the JavaScript configuration.
     * @param browserVersion the {@link BrowserVersion}
     * @return the instance for the specified {@link BrowserVersion}
     */
    public static synchronized JavaScriptConfiguration getInstance(final BrowserVersion browserVersion) {
        if (browserVersion == null) {
            throw new IllegalStateException("BrowserVersion must be defined");
        }
        JavaScriptConfiguration configuration = ConfigurationMap_.get(browserVersion);

        if (configuration == null) {
            configuration = new JavaScriptConfiguration(browserVersion);
            ConfigurationMap_.put(browserVersion, configuration);
        }
        return configuration;
    }

    /**
     * Returns the configuration that has all entries. No constraints are put on the returned entries.
     *
     * @return the instance containing all entries from the configuration file
     */
    static JavaScriptConfiguration getAllEntries() {
        final JavaScriptConfiguration configuration = new JavaScriptConfiguration(null);
        return configuration;
    }

    /**
     * Gets all the configurations.
     * @return the class configurations
     */
    public Iterable<ClassConfiguration> getAll() {
        return configuration_.values();
    }

    private Map<String, ClassConfiguration> buildUsageMap(final BrowserVersion browser) {
        final Map<String, ClassConfiguration> classMap = new HashMap<String, ClassConfiguration>(100);

        String packageName = getClass().getPackage().getName();
        packageName = packageName.substring(0, packageName.lastIndexOf('.'));
        for (final String className : getClassesForPackage(packageName)) {
            if (!className.contains("$")) {
                final ClassConfiguration config = processClass(className, browser);
                if (config != null) {
                    classMap.put(config.getHostClass().getSimpleName(), config);
                }
            }
        }

        return Collections.unmodifiableMap(classMap);
    }

    private ClassConfiguration processClass(final String className, final BrowserVersion browser) {
        try {
            if (browser != null) {
                final Class<?> klass = Class.forName(className);
                if (SimpleScriptable.class.isAssignableFrom(klass)) {
                    final JsxClass jsxClass = klass.getAnnotation(JsxClass.class);
                    if (jsxClass != null && isSupported(jsxClass.browsers(), browser)) {
                        final String hostClassName = className;

                        final String domClassName = jsxClass.domClass() != Object.class
                                ? jsxClass.domClass().getName() : "";

                        final boolean jsObjectFlag = jsxClass.isJSObject();
                        @SuppressWarnings("unchecked")
                        final ClassConfiguration classConfiguration = new ClassConfiguration(
                                (Class<? extends SimpleScriptable>) klass,
                                domClassName, jsObjectFlag);

                        final String simpleClassName = hostClassName.substring(hostClassName.lastIndexOf('.') + 1);
                        ClassnameMap_.put(hostClassName, simpleClassName);
                        final Map<String, Method> allGetters = new HashMap<String, Method>();
                        final Map<String, Method> allSetters = new HashMap<String, Method>();
                        for (final Method m : classConfiguration.getHostClass().getDeclaredMethods()) {
                            for (final Annotation annotation : m.getAnnotations()) {
                                if (annotation instanceof JsxGetter) {
                                    if (isSupported(((JsxGetter) annotation).value(), browser)) {
                                        final String property = m.getName().substring("jsxGet_".length());
                                        allGetters.put(property, m);
                                    }
                                }
                                else if (annotation instanceof JsxSetter) {
                                    if (isSupported(((JsxSetter) annotation).value(), browser)) {
                                        final String property = m.getName().substring("jsxSet_".length());
                                        allSetters.put(property, m);
                                    }
                                }
                                else if (annotation instanceof JsxFunction) {
                                    if (isSupported(((JsxFunction) annotation).value(), browser)) {
                                        classConfiguration.addFunction(m);
                                    }
                                }
                                else if (annotation instanceof JsxConstructor) {
                                    classConfiguration.setJSConstructor(m);
                                }
                            }
                        }
                        for (final Field f : classConfiguration.getHostClass().getDeclaredFields()) {
                            final JsxConstant jsxConstant = f.getAnnotation(JsxConstant.class);
                            if (jsxConstant != null && isSupported(jsxConstant.value(), browser)) {
                                classConfiguration.addConstant(f.getName());
                            }
                        }
                        for (final String property : allGetters.keySet()) {
                            classConfiguration.addProperty(property,
                                    allGetters.get(property), allSetters.get(property));
                        }
                        return classConfiguration;
                    }
                }
            }
        }
        catch (final Throwable t) {
            //ignore
        }
        return null;
    }

    private static boolean isSupported(final WebBrowser[] browsers, final BrowserVersion browserVersion) {
        for (final WebBrowser browser : browsers) {
            final String expectedBrowserName;
            if (browserVersion.isIE()) {
                expectedBrowserName = "IE";
            }
            else if (browserVersion.isFirefox()) {
                expectedBrowserName = "FF";
            }
            else {
                expectedBrowserName = "CHROME";
            }
            if (browser.value().name().equals(expectedBrowserName)
                    && browser.minVersion() <= browserVersion.getBrowserVersionNumeric()
                    && browser.maxVersion() >= browserVersion.getBrowserVersionNumeric()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the class configuration for the supplied JavaScript class name.
     * @param hostClassName the js class name
     * @return the class configuration for the supplied JavaScript class name
     */
    public ClassConfiguration getClassConfiguration(final String hostClassName) {
        return configuration_.get(hostClassName);
    }

    /**
     * Returns the classname that the given class implements. If the class is
     * the input class, then the name is extracted from the type that the Input class
     * is masquerading as.
     * FIXME - Implement the Input class processing
     * @param clazz
     * @return the classname
     */
    String getClassnameForClass(final Class<?> clazz) {
        final String name = ClassnameMap_.get(clazz.getName());
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
    @SuppressWarnings("unchecked")
    public Map<Class<? extends DomElement>, Class<? extends SimpleScriptable>>
    getDomJavaScriptMapping() {
        if (domJavaScriptMap_ != null) {
            return domJavaScriptMap_;
        }

        final Map<Class<? extends DomElement>, Class<? extends SimpleScriptable>> map =
            new HashMap<Class<? extends DomElement>, Class<? extends SimpleScriptable>>();

        for (String hostClassName : configuration_.keySet()) {
            ClassConfiguration classConfig = getClassConfiguration(hostClassName);
            final String domClassName = classConfig.getDomClassName();
            if (domClassName != null) {
                try {
                    final Class<? extends DomElement> domClass =
                        (Class<? extends DomElement>) Class.forName(domClassName);
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
                catch (final ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
        }
        map.put(HtmlHeading1.class, HTMLHeadingElement.class);
        map.put(HtmlHeading2.class, HTMLHeadingElement.class);
        map.put(HtmlHeading3.class, HTMLHeadingElement.class);
        map.put(HtmlHeading4.class, HTMLHeadingElement.class);
        map.put(HtmlHeading5.class, HTMLHeadingElement.class);
        map.put(HtmlHeading6.class, HTMLHeadingElement.class);

        map.put(HtmlInlineQuotation.class, HTMLQuoteElement.class);
        map.put(HtmlBlockQuote.class, HTMLQuoteElement.class);

        map.put(HtmlAbbreviated.class, HTMLSpanElement.class);
        map.put(HtmlAcronym.class, HTMLSpanElement.class);
        map.put(HtmlAddress.class, HTMLSpanElement.class);
        map.put(HtmlBackgroundSound.class, HTMLSpanElement.class);
        map.put(HtmlBidirectionalOverride.class, HTMLSpanElement.class);
        map.put(HtmlBig.class, HTMLSpanElement.class);
        map.put(HtmlBold.class, HTMLSpanElement.class);
        map.put(HtmlBlink.class, HTMLSpanElement.class);
        map.put(HtmlCenter.class, HTMLSpanElement.class);
        map.put(HtmlCitation.class, HTMLSpanElement.class);
        map.put(HtmlCode.class, HTMLSpanElement.class);
        map.put(HtmlDefinition.class, HTMLSpanElement.class);
        map.put(HtmlDefinitionDescription.class, HTMLSpanElement.class);
        map.put(HtmlDefinitionTerm.class, HTMLSpanElement.class);
        map.put(HtmlEmphasis.class, HTMLSpanElement.class);
        map.put(HtmlItalic.class, HTMLSpanElement.class);
        map.put(HtmlKeyboard.class, HTMLSpanElement.class);
        map.put(HtmlListing.class, HTMLSpanElement.class);
        map.put(HtmlMultiColumn.class, HTMLSpanElement.class);
        map.put(HtmlNoBreak.class, HTMLSpanElement.class);
        map.put(HtmlPlainText.class, HTMLSpanElement.class);
        map.put(HtmlS.class, HTMLSpanElement.class);
        map.put(HtmlSample.class, HTMLSpanElement.class);
        map.put(HtmlSmall.class, HTMLSpanElement.class);
        map.put(HtmlSpan.class, HTMLSpanElement.class);
        map.put(HtmlStrike.class, HTMLSpanElement.class);
        map.put(HtmlStrong.class, HTMLSpanElement.class);
        map.put(HtmlSubscript.class, HTMLSpanElement.class);
        map.put(HtmlSuperscript.class, HTMLSpanElement.class);
        map.put(HtmlTeletype.class, HTMLSpanElement.class);
        map.put(HtmlUnderlined.class, HTMLSpanElement.class);
        map.put(HtmlVariable.class, HTMLSpanElement.class);
        map.put(HtmlExample.class, HTMLSpanElement.class);

        map.put(HtmlDivision.class, HTMLDivElement.class);
        map.put(HtmlMarquee.class, HTMLDivElement.class);
        map.put(HtmlNoEmbed.class, HTMLDivElement.class);
        map.put(HtmlNoFrames.class, HTMLDivElement.class);
        map.put(HtmlNoScript.class, HTMLDivElement.class);

        map.put(HtmlTableBody.class, HTMLTableSectionElement.class);
        map.put(HtmlTableHeader.class, HTMLTableSectionElement.class);
        map.put(HtmlTableFooter.class, HTMLTableSectionElement.class);

        map.put(HtmlTableColumn.class, HTMLTableColElement.class);
        map.put(HtmlTableColumnGroup.class, HTMLTableColElement.class);

        domJavaScriptMap_ = Collections.unmodifiableMap(map);

        return domJavaScriptMap_;
    }

    /**
     * Return the classes inside the specified package and its sub-packages.
     * @param packageName the package name
     * @return a list of class names
     */
    public static List<String> getClassesForPackage(final String packageName) {
        final List<String> list = new ArrayList<String>();

        File directory = null;
        final String relPath = packageName.replace('.', '/') + '/' + JavaScriptEngine.class.getSimpleName() + ".class";

        final URL resource = JavaScriptConfiguration.class.getClassLoader().getResource(relPath);

        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }
        final String fullPath = resource.getFile();

        try {
            directory = new File(resource.toURI()).getParentFile();
        }
        catch (final URISyntaxException e) {
            throw new RuntimeException(packageName + " (" + resource + ") does not appear to be a valid URL", e);
        }
        catch (final IllegalArgumentException e) {
            directory = null;
        }

        if (directory != null && directory.exists()) {
            addClasses(directory, packageName, list);
        }
        else {
            try {
                String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    jarPath = jarPath.replace("%20", " ");
                }
                final JarFile jarFile = new JarFile(jarPath);
                for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
                    final String entryName = entries.nextElement().getName();
                    if (entryName.endsWith(".class")) {
                        list.add(entryName.replace('/', '.').replace('\\', '.').replace(".class", ""));
                    }
                }
            }
            catch (final IOException e) {
                throw new RuntimeException(packageName + " does not appear to be a valid package", e);
            }
        }
        return list;
    }

    private static void addClasses(final File directory, final String packageName, final List<String> list) {
        for (final File file: directory.listFiles()) {
            final String name = file.getName();
            if (name.endsWith(".class")) {
                list.add(packageName + '.' + name.substring(0, name.length() - 6));
            }
            else if (file.isDirectory() && !".svn".equals(file.getName())) {
                addClasses(file, packageName + '.' + file.getName(), list);
            }
        }
    }
}
