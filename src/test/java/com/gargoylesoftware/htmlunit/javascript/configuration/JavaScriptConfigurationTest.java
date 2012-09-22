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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

/**
 * Tests for {@link JavaScriptConfiguration}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
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
        final Field field = JavaScriptConfiguration.class.getDeclaredField("ConfigurationMap_");
        field.setAccessible(true);
        final Map<?, ?> leakyMap = (Map<? , ?>) field.get(null);

        // maybe some BrowserVersions are already known
        final int knownBrowsers = leakyMap.size();
        for (int i = 0; i < 3; i++) {
            final BrowserVersion browserVersion = new BrowserVersion("App", "Version", "User agent", 1);
            JavaScriptConfiguration.getInstance(browserVersion);
        }
        assertEquals(knownBrowsers + 1, leakyMap.size());
    }

    /**
     * Regression test for bug 2854240.
     * This test was throwing an OutOfMemoryError when the bug existed.
     * @throws Exception if an error occurs
     */
    @Test
    public void memoryLeak() throws Exception {
        long count = 0;
        while (count++ < 3000) {
            final BrowserVersion browserVersion = new BrowserVersion(
                "App" + RandomStringUtils.randomAlphanumeric(20),
                "Version" + RandomStringUtils.randomAlphanumeric(20),
                "User Agent" + RandomStringUtils.randomAlphanumeric(20),
                1);
            JavaScriptConfiguration.getInstance(browserVersion);
            if (LOG.isInfoEnabled()) {
                LOG.info("count: " + count + "; memory stats: " + getMemoryStats());
            }
        }
        System.gc();
    }

    private String getMemoryStats() {
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
     * Tests that all classes in *.javascript.* which have {@link JsxClass} annotation,
     * are included in {@link JavaScriptConfiguration#CLASSES_}.
     */
    @Test
    public void jsxClasses() {
        String javaScriptPackageName = JavaScriptConfiguration.class.getPackage().getName();
        javaScriptPackageName = javaScriptPackageName.substring(0, javaScriptPackageName.lastIndexOf('.'));
        final List<String> foundJsxClasses = new ArrayList<String>();
        for (final String className : getClassesForPackage(javaScriptPackageName)) {
            if (!className.contains("$")) {
                Class<?> klass = null;
                try {
                    klass = Class.forName(className);
                }
                catch (final Throwable t) {
                    continue;
                }
                if (klass.getAnnotation(JsxClass.class) != null) {
                    foundJsxClasses.add(className);
                }
            }
        }
        final List<String> definedClasses = new ArrayList<String>();
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
