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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for <a href="http://code.google.com/appengine/">Google App Engine</a> support.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class GAESupportTest extends WebTestCase {
    private static final Set<String> whitelist = new HashSet<String>();

    /**
     * Loads the white list.
     * @throws IOException in case of problem
     */
    @Before
    @SuppressWarnings("unchecked")
    public void init() throws IOException {
        if (whitelist.isEmpty()) {
            System.out.println(getClass().getResource("whitelist.txt"));
            final InputStream is = getClass().getResourceAsStream("whitelist.txt");
            Assert.assertNotNull(is);
            final List<String> lines = (List<String>) IOUtils.readLines(is);
            IOUtils.closeQuietly(is);

            whitelist.addAll(lines);
        }
    }

    /**
     * Simulates GAE white list restrictions.
     * Fails as of HtmlUnit-2.7 due to usage of java.net.URLStreamHandler (and problably other classes).
     * @throws Exception if the test fails
     */
    @NotYetImplemented
    @Test
    public void whitelist() throws Exception {
        final ClassLoader cl = new ClassLoader(ClassLoader.getSystemClassLoader()) {
            @Override
            public Class<?> loadClass(final String name) throws ClassNotFoundException {
                if (name.startsWith("java") && !whitelist.contains(name)) {
                    throw new NoClassDefFoundError(name + " is a restricted class for GAE");
                }
                if (!name.startsWith("com.gargoylesoftware")) {
                    return super.loadClass(name);
                }
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
        };

        final Class<?> clazz = cl.loadClass(WebClient.class.getName());
        clazz.newInstance();
    }
}
