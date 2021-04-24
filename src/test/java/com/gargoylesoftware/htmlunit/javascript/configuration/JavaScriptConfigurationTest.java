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

import static com.gargoylesoftware.htmlunit.BrowserVersion.FIREFOX;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.worker.DedicatedWorkerGlobalScope;

/**
 * Tests for {@link JavaScriptConfiguration}.
 *
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Joerg Werner
 */
public class JavaScriptConfigurationTest extends SimpleWebTestCase {

    private static final Log LOG = LogFactory.getLog(JavaScriptConfigurationTest.class);

    /**
     * Test if configuration map expands with each new instance of BrowserVersion used.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void configurationMapExpands() throws Exception {
        // get a reference to the leaky map
        final Field field = JavaScriptConfiguration.class.getDeclaredField("CONFIGURATION_MAP_");
        field.setAccessible(true);
        final Map<?, ?> leakyMap = (Map<?, ?>) field.get(null);

        leakyMap.clear();
        final int knownBrowsers = leakyMap.size();

        BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX_78)
                                                .setApplicationVersion("App")
                                                .setApplicationVersion("Version")
                                                .setUserAgent("User agent")
                                                .build();
        JavaScriptConfiguration.getInstance(browserVersion);

        browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX_78)
                            .setApplicationVersion("App2")
                            .setApplicationVersion("Version2")
                            .setUserAgent("User agent2")
                            .build();
        JavaScriptConfiguration.getInstance(browserVersion);

        assertEquals(knownBrowsers + 1, leakyMap.size());
    }

    /**
     * Regression test for Bug #899.
     * This test was throwing an OutOfMemoryError when the bug existed.
     * @throws Exception if an error occurs
     */
    @Test
    public void memoryLeak() throws Exception {
        final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

        long count = 0;
        while (count++ < 3000) {
            final BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX_78)
                                                    .setApplicationVersion("App" + generator.generate(20))
                                                    .setApplicationVersion("Version" + generator.generate(20))
                                                    .setUserAgent("User Agent" + generator.generate(20))
                                                    .build();
            JavaScriptConfiguration.getInstance(browserVersion);
            if (LOG.isInfoEnabled()) {
                LOG.info("count: " + count + "; memory stats: " + getMemoryStats());
            }
        }
        System.gc();
    }

    private static String getMemoryStats() {
        final Runtime rt = Runtime.getRuntime();
        final long free = rt.freeMemory() / 1024;
        final long total = rt.totalMemory() / 1024;
        final long max = rt.maxMemory() / 1024;
        final long used = total - free;
        final String format = "used: {0,number,0}K, free: {1,number,0}K, total: {2,number,0}K, max: {3,number,0}K";
        return MessageFormat.format(format,
                Long.valueOf(used), Long.valueOf(free), Long.valueOf(total), Long.valueOf(max));
    }

    /**
     * Tests that all classes in *.javascript.* which have {@link JsxClasses}/{@link JsxClass} annotation,
     * are included in {@link JavaScriptConfiguration#CLASSES_}.
     */
    @Test
    public void jsxClasses() {
        final List<String> foundJsxClasses = new ArrayList<>();
        for (final String className : getClassesForPackage(JavaScriptEngine.class)) {
            if (!className.contains("$")) {
                Class<?> klass = null;
                try {
                    klass = Class.forName(className);
                }
                catch (final Throwable t) {
                    continue;
                }
                if ("com.gargoylesoftware.htmlunit.javascript.host.intl".equals(klass.getPackage().getName())
                        || "Reflect".equals(klass.getSimpleName())) {
                    continue;
                }
                if (klass.getAnnotation(JsxClasses.class) != null) {
                    foundJsxClasses.add(className);
                }
                else if (klass.getAnnotation(JsxClass.class) != null) {
                    foundJsxClasses.add(className);
                }
            }
        }
        foundJsxClasses.remove(DedicatedWorkerGlobalScope.class.getName());
        final List<String> definedClasses = new ArrayList<>();
        for (final Class<?> klass : JavaScriptConfiguration.CLASSES_) {
            definedClasses.add(klass.getName());
        }
        foundJsxClasses.removeAll(definedClasses);
        if (!foundJsxClasses.isEmpty()) {
            fail("Class " + foundJsxClasses.get(0) + " is not in JavaScriptConfiguration.CLASSES_");
        }
    }

    /**
     * Return the classes inside the specified package and its sub-packages.
     * @param klass a class inside that package
     * @return a list of class names
     */
    public static List<String> getClassesForPackage(final Class<?> klass) {
        final List<String> list = new ArrayList<>();

        File directory = null;
        final String relPath = klass.getName().replace('.', '/') + ".class";

        final URL resource = JavaScriptConfiguration.class.getClassLoader().getResource(relPath);

        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }
        final String fullPath = resource.getFile();

        try {
            directory = new File(resource.toURI()).getParentFile();
        }
        catch (final URISyntaxException e) {
            throw new RuntimeException(klass.getName() + " (" + resource + ") does not appear to be a valid URL", e);
        }
        catch (final IllegalArgumentException e) {
            directory = null;
        }

        if (directory != null && directory.exists()) {
            addClasses(directory, klass.getPackage().getName(), list);
        }
        else {
            try {
                String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
                if (System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win")) {
                    jarPath = jarPath.replace("%20", " ");
                }
                try (JarFile jarFile = new JarFile(jarPath)) {
                    for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
                        final String entryName = entries.nextElement().getName();
                        if (entryName.endsWith(".class")) {
                            list.add(entryName.replace('/', '.').replace('\\', '.').replace(".class", ""));
                        }
                    }
                }
            }
            catch (final IOException e) {
                throw new RuntimeException(klass.getPackage().getName() + " does not appear to be a valid package", e);
            }
        }
        return list;
    }

    private static void addClasses(final File directory, final String packageName, final List<String> list) {
        final File[] files = directory.listFiles();
        if (files != null) {
            for (final File file : files) {
                final String name = file.getName();
                if (name.endsWith(".class")) {
                    list.add(packageName + '.' + name.substring(0, name.length() - 6));
                }
                else if (file.isDirectory() && !".git".equals(file.getName())) {
                    addClasses(file, packageName + '.' + file.getName(), list);
                }
            }
        }
    }

    /**
     * Tests that anything annotated with {@link JsxGetter} does not start with "set" and vice versa.
     */
    @Test
    public void methodPrefix() {
        for (final Class<?> klass : JavaScriptConfiguration.CLASSES_) {
            for (final Method method : klass.getMethods()) {
                final String methodName = method.getName();
                if (method.getAnnotation(JsxGetter.class) != null && methodName.startsWith("set")) {
                    fail("Method " + methodName + " in " + klass.getSimpleName() + " should not start with \"set\"");
                }
                if (method.getAnnotation(JsxSetter.class) != null && methodName.startsWith("get")) {
                    fail("Method " + methodName + " in " + klass.getSimpleName() + " should not start with \"get\"");
                }
            }
        }
    }

    /**
     * Tests that all classes included in {@link JavaScriptConfiguration#CLASSES_} defining an
     * {@link JsxClasses}/{@link JsxClass} annotation for at least one browser.
     */
    @Test
    public void obsoleteJsxClasses() {
        final JavaScriptConfiguration config = JavaScriptConfiguration.getInstance(FIREFOX);

        for (final Class<? extends SimpleScriptable> klass : config.getClasses()) {
            boolean found = false;
            for (final BrowserVersion browser : BrowserVersion.ALL_SUPPORTED_BROWSERS) {
                if (AbstractJavaScriptConfiguration.getClassConfiguration(klass, browser) != null) {
                    found = true;
                    break;
                }
            }
            assertTrue("Class " + klass
                    + " is member of JavaScriptConfiguration.CLASSES_ but does not define @JsxClasses/@JsxClass",
                    found);
        }
    }

    /**
     * Test of alphabetical order.
     */
    @Test
    public void lexicographicOrder() {
        String lastClassName = null;
        for (final Class<?> c : JavaScriptConfiguration.CLASSES_) {
            final String name = c.getSimpleName();
            if (lastClassName != null && name.compareToIgnoreCase(lastClassName) < 0) {
                fail("JavaScriptConfiguration.CLASSES_: '"
                    + name + "' should be before '" + lastClassName + "'");
            }
            lastClassName = name;
        }
    }

    /**
     * See issue 1890.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void original() throws Exception {
        final BrowserVersion browserVersion = BrowserVersion.CHROME;

        test(browserVersion);
    }

    /**
     * See issue 1890.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void cloned() throws Exception {
        final BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX)
                                                    .build();

        test(browserVersion);
    }

    /**
     * See issue 1890.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void clonedAndModified() throws Exception {
        final BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.FIREFOX)
                                                    .setUserAgent("foo")
                                                    .build();

        test(browserVersion);
    }

    private void test(final BrowserVersion browserVersion) throws IOException {
        try (WebClient webClient = new WebClient(browserVersion)) {
            final MockWebConnection conn = new MockWebConnection();
            conn.setDefaultResponse("<html><body onload='document.body.firstChild'></body></html>");
            webClient.setWebConnection(conn);

            webClient.getPage("http://localhost/");
        }
    }

}
