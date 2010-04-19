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
package com.gargoylesoftware.htmlunit.gae;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Test runner for GAE support tests. This runner uses a custom class loader that
 * tries to enforce GAE class loading rules. Test class and HtmlUnit classes are loaded by the
 * same loader what allows to write tests "normally" without any need
 * for reflection to access method/members.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class GAETestRunner extends BlockJUnit4ClassRunner {
    private static final Set<String> whitelist = loadWhiteList();

    static class GAELikeClassLoader extends ClassLoader {
        @Override
        public Class<?> loadClass(final String name) throws ClassNotFoundException {
            final String baseName = StringUtils.substringBefore(name, "$");
            if (baseName.startsWith("java") && !whitelist.contains(baseName)) {
                throw new NoClassDefFoundError(name + " is a restricted class for GAE");
            }
            if (!name.startsWith("com.gargoylesoftware")) {
                return super.loadClass(name);
            }
            super.loadClass(name);
            final InputStream is = getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                IOUtils.copy(is, bos);
                final byte[] bytes = bos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            }
            catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Called by JUnit.
     * @param klass the test class
     * @throws Throwable in case of problem
     */
    public GAETestRunner(final Class<?> klass) throws Throwable {
        super(getClassFromGAELikeClassLoader(klass));
    }

    private static Class<?> getClassFromGAELikeClassLoader(final Class<?> klass) {
        final ClassLoader gaeLikeLoader = new GAELikeClassLoader();
        try {
            return gaeLikeLoader.loadClass(klass.getName());
        }
        catch (final ClassNotFoundException e) {
            throw new RuntimeException("Can't find existing test class through GAELikeClassLoader: " + klass.getName());
        }
    }

    /**
     * Loads the white list.
     * @return the list of classes in the white list
     */
    @SuppressWarnings("unchecked")
    private static Set<String> loadWhiteList() {
        final InputStream is = GAETestRunner.class.getResourceAsStream("whitelist.txt");
        Assert.assertNotNull(is);
        List<String> lines;
        try {
            lines = IOUtils.readLines(is);
        }
        catch (final IOException e) {
            throw new Error("Failed to load while list content", e);
        }
        finally {
            IOUtils.closeQuietly(is);
        }

        return new HashSet<String>(lines);
    }
}
