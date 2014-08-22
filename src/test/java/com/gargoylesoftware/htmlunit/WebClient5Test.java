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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link WebClient}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Hans Donner
 * @author Paul King
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Sudhan Moghe
 * @author Ronald Brill
 */
public class WebClient5Test extends WebTestCase {

    /**
     * Tests if all JUnit 4 candidate test methods declare <tt>@Test</tt> annotation.
     * @throws Exception if the test fails
     */
    @Test
    public void testTests() throws Exception {
        testTests(new File("src/test/java"));
    }

    private void testTests(final File dir) throws Exception {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory()) {
                if (!".svn".equals(file.getName())) {
                    testTests(file);
                }
            }
            else {
                if (file.getName().endsWith(".java")) {
                    final int index = new File("src/test/java").getAbsolutePath().length();
                    String name = file.getAbsolutePath();
                    name = name.substring(index + 1, name.length() - 5);
                    name = name.replace(File.separatorChar, '.');
                    final Class<?> clazz;
                    try {
                        clazz = Class.forName(name);
                    }
                    catch (final Exception e) {
                        continue;
                    }
                    name = file.getName();
                    if (name.endsWith("Test.java") || name.endsWith("TestCase.java")) {
                        for (final Constructor<?> ctor : clazz.getConstructors()) {
                            if (ctor.getParameterTypes().length == 0) {
                                for (final Method method : clazz.getDeclaredMethods()) {
                                    if (Modifier.isPublic(method.getModifiers())
                                            && method.getAnnotation(Before.class) == null
                                            && method.getAnnotation(BeforeClass.class) == null
                                            && method.getAnnotation(After.class) == null
                                            && method.getAnnotation(AfterClass.class) == null
                                            && method.getAnnotation(Test.class) == null
                                            && method.getReturnType() == Void.TYPE
                                            && method.getParameterTypes().length == 0) {
                                        fail("Method '" + method.getName()
                                                + "' in " + name + " does not declare @Test annotation");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void addRequestHeader_Cookie() throws Exception {
        final WebClient client = new WebClient();
        try {
            client.addRequestHeader("Cookie", "some_value");
            fail("Should have thrown an exception ");
        }
        catch (final IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test that WebClient.getPage(String) calls WebClient.getPage(URL) with the right URL.
     * @throws Exception if the test fails
     */
    @Test
    public void testGetPageWithStringArg() throws Exception {
        final URL[] calledUrls = {null};
        final WebClient wc = new WebClient() {
            @Override
            @SuppressWarnings("unchecked")
            public Page getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
                calledUrls[0] = url;
                return null;
            }
        };

        wc.getPage(getDefaultUrl().toExternalForm());
        assertEquals(getDefaultUrl(), calledUrls[0]);
    }
}
