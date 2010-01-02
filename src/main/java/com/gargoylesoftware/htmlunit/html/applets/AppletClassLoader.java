/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.applets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * Class loader for loading applets.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class AppletClassLoader extends ClassLoader {

    private static final Log LOG = LogFactory.getLog(AppletClassLoader.class);

    private final Set<String> definedClasses_ = new HashSet<String>();
    private final Map<String, JarFile> jarFiles_ = new HashMap<String, JarFile>();

    /**
     * The constructor.
     */
    public AppletClassLoader() {
        super(AppletClassLoader.class.getClassLoader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class< ? > loadClass(final String name) throws ClassNotFoundException {
        if (!definedClasses_.contains(name) && jarFiles_.containsKey(name)) {
            defineClass(name);
        }
        return super.loadClass(name);
    }

    private void defineClass(final String name) {
        LOG.debug("Defining class " + name);
        final String classFileName = name.replace('.', '/') + ".class";
        final JarFile jarFile = jarFiles_.get(name);
        try {
            final InputStream is = jarFile.getInputStream(jarFile.getEntry(classFileName));
            final byte[] bytes = IOUtils.toByteArray(is);
            defineClass(name, bytes, 0, bytes.length);
            definedClasses_.add(name);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the content of specified WebResponse to the classpath for the applet.
     * @param webResponse the web response
     * @throws IOException in case of problem working with the response content
     */
    public void addToClassPath(final WebResponse webResponse) throws IOException {
        // normally content type should be "application/java-archive"
        // but it works when it is not the case
        // TODO: handle the case where it is not a jar archive
        readClassesFromJar(webResponse);
    }

    private void readClassesFromJar(final WebResponse webResponse) throws IOException {
        final File tmpFile = File.createTempFile("HtmlUnit", "jar");
        tmpFile.deleteOnExit();
        FileUtils.writeByteArrayToFile(tmpFile, webResponse.getContentAsBytes());
        final JarFile jarFile = new JarFile(tmpFile);
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String name = entry.getName();
            if (name.endsWith(".class")) {
                final String className = name.replace('/', '.').substring(0, name.length() - 6);
                jarFiles_.put(className, jarFile);
                LOG.trace("Jar entry: " + className);
            }
        }
    }

    /**
     * Reads the class name from the bytes of a .class file.
     * @param webResponse response containing the bytes the class
     * @return the full class name
     */
    public static String readClassName(final WebResponse webResponse) {
        return readClassName(webResponse.getContentAsBytes());
    }

    /**
     * Reads the class name from the bytes of a .class file.
     * @param bytes the class bytes
     * @return the full class name
     */
    public static String readClassName(final byte[] bytes) {
        // seems to work ;-)
        final StringBuilder sb = new StringBuilder();
        int i = 16; // skip 16 first bytes
        byte b = bytes[16];
        while (b != 7) {
            sb.append((char) b);
            b = bytes[++i];
        }
        return sb.toString().replace('/', '.');
    }
}
