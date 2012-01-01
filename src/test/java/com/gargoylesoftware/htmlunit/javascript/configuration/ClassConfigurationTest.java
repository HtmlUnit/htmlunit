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

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link JavaScriptConfiguration}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class ClassConfigurationTest extends WebTestCase {

    /**
     * Constructor.
     */
    public ClassConfigurationTest() {
        JavaScriptConfiguration.resetClassForTesting();
    }

    /**
     * Tests equality on a class configuration.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationSimplePropertyEquality() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        Assert.assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addProperty("test", true, true);
        assertTrue("Configs should now be equal", config1.equals(config2));
    }

    /**
     * Tests equality on a class configuration for function.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationSimpleFunctionEquality() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addFunction("testFunction");
        Assert.assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addFunction("testFunction");
        assertTrue("Configs should now be equal", config1.equals(config2));
    }

    /**
     * Tests equality on a class configuration.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationSimpleUnequalProperties() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        Assert.assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addProperty("test", true, false);
        Assert.assertFalse("Configs should not be equal due to different property values", config1.equals(config2));
    }

    /**
     * @throws Exception on error
     */
    @Test
    public void testForJSFlagTrue() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        assertTrue("JSObject Flag should have been set", config1.isJsObject());
    }

    /**
     * @throws Exception on error
     */
    @Test
    public void testForJSFlagFalse() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, false);
        Assert.assertFalse("JSObject Flag should not have been set", config1.isJsObject());
    }

    /**
     * Tests equality on a class configuration.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationPropertyEqualityWithBrowser() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        config2.addProperty("test", true, true);
        config1.setBrowser("test", "Netscape");
        Assert.assertFalse("Should not be equal with browser added", config1.equals(config2));
        config2.setBrowser("test", "Netscape");
        assertTrue("Should be equal with browser added", config1.equals(config2));
    }

    /**
     * Tests equality on a class configuration mis-matched browsers.
     * @throws Exception - Exception on error
     */
    @Test
    public void testConfigurationPropertyEqualityWithDifferentBrowsers() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        final ClassConfiguration config2 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);

        config1.addProperty("test", true, true);
        config2.addProperty("test", true, true);
        config1.setBrowser("test", "Netscape");
        Assert.assertFalse("Should not be equal with browser added", config1.equals(config2));
        config2.setBrowser("test", "Microsoft Internet Explorer");
        Assert.assertFalse("Should be equal with different browser added", config1.equals(config2));
    }

    /**
     * Test for throwing exception when setter method is not defined.
     * @throws Exception - Exception on error
     */
    @Test
    public void testNoSetterMethod() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        try {
            config1.addProperty("getterOnly", true, true);
            fail("Should produce an exception due to not finding the methods");
        }
        catch (final IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Test for throwing exception when setter method is not defined.
     * @throws Exception - Exception on error
     */
    @Test
    public void testNoFunctionMethod() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(
            ConfigTestClass.class.getName(), null, null, null, true);
        try {
            config1.addFunction("noTestFunction");
            fail("Should produce an exception due to not finding the methods");
        }
        catch (final IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Test class.
     */
    protected class ConfigTestClass {
        private boolean test_ = false;

        /**
         * Dummy function.
         */
        public void jsxFunction_testFunction() {
        }

        /**
         * @return boolean
         */
        public boolean jsxGet_test() {
            return test_;
        }

        /**
         * @return boolean
         */
        public boolean jsxGet_getterOnly() {
            return test_;
        }

        /**
         * @param testFlag - test value
         */
        public void jsxSet_test(final boolean testFlag) {
            test_ = testFlag;
        }
    }
}
