/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.configuration;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for JavaScriptConfiguration.
 *
 * @version  $Revision$
 * @author Chris Erskine
 */
public class ClassConfigurationTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public ClassConfigurationTest( final String name ) {
        super(name);
    }

    
    /**
     * Reset the JavaScriptConfiguration file for each test to it's inital clean state.
     * 
     * @throws Exception if the test fails
     */
    protected void setUp() throws Exception {
        super.setUp();
        JavaScriptConfiguration.resetClassForTesting();
    }


    /**
     * Test equality on a class configuration
     * @throws Exception - Exception on error
     */
    public void testConfigurationSimplePropertyEquality() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class.getName()); 
        final ClassConfiguration config2 = new ClassConfiguration(ConfigTestClass.class.getName());
        
        config1.addProperty("test", true, true);
        assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addProperty("test", true, true);
        assertTrue("Configs should now be equal", config1.equals(config2));
    }


    /**
     * Test equality on a class configuration for function
     * @throws Exception - Exception on error
     */
    public void testConfigurationSimpleFunctionEquality() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class.getName()); 
        final ClassConfiguration config2 = new ClassConfiguration(ConfigTestClass.class.getName());
        
        config1.addFunction("testFunction");
        assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addFunction("testFunction");
        assertTrue("Configs should now be equal", config1.equals(config2));
    }

    
    /**
     * Test equality on a class configuration
     *
     * @throws Exception - Exception on error
     */
    public void testConfigurationSimpleUnequalProperties() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class.getName()); 
        final ClassConfiguration config2 = new ClassConfiguration(ConfigTestClass.class.getName());
        
        config1.addProperty("test", true, true);
        assertFalse("Configs should not be equal", config1.equals(config2));
        config2.addProperty("test", true, false);
        assertFalse("Configs should not be equal due to different property values", config1.equals(config2));
    }

    
    /**
     * Test equality on a class configuration
     *
     * @throws Exception - Exception on error
     */
    public void testConfigurationPropertyEqualityWithBrowser() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class.getName()); 
        final ClassConfiguration config2 = new ClassConfiguration(ConfigTestClass.class.getName());
        
        config1.addProperty("test", true, true);
        config2.addProperty("test", true, true);
        config1.setBrowser("test", "Netscape");
        assertFalse("Should not be equal with browser added", config1.equals(config2));
        config2.setBrowser("test", "Netscape");
        assertTrue("Should be equal with browser added", config1.equals(config2));        
    }
    /**
     * Test equality on a class configuration mis-matched browsers
     *
     * @throws Exception - Exception on error
     */
    public void testConfigurationPropertyEqualityWithDifferentBrowsers() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class.getName()); 
        final ClassConfiguration config2 = new ClassConfiguration(ConfigTestClass.class.getName());
        
        config1.addProperty("test", true, true);
        config2.addProperty("test", true, true);
        config1.setBrowser("test", "Netscape");
        assertFalse("Should not be equal with browser added", config1.equals(config2));
        config2.setBrowser("test", "Microsoft Internet Explorer");
        assertFalse("Should be equal with different browser added", config1.equals(config2));        
    }
    

    /**
     * Test for throwing exception when setter method is not defined
     *
     * @throws Exception - Exception on error
     */
    public void testNoSetterMethod() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class.getName());
        try {
            config1.addProperty("getterOnly", true, true);
            fail("Should produce an exception due to not finding the methods");
        }
        catch (final IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * Test for throwing exception when setter method is not defined
     *
     * @throws Exception - Exception on error
     */
    public void testNoFunctionMethod() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class.getName());
        try {
            config1.addFunction("noTestFunction");
            fail("Should produce an exception due to not finding the methods");
        }
        catch (final IllegalStateException e) {
            assertTrue(true);
        }
    }

    /**
     * test class
     */
    protected class ConfigTestClass {
        private boolean test_ = false;
        
        /**
         * Dummy function
         */
        public void jsFunction_testFunction() {
        }
        /**
         * 
         * @return boolean
         */
        public boolean jsGet_test() {
            return test_;
        }
        /**
         * 
         * @return boolean
         */
        public boolean jsGet_getterOnly() {
            return test_;
        }

        /**
         * 
         * @param testFlag - test value
         */
        public void jsSet_test(final boolean testFlag) {
            test_ = testFlag;
        }
    }
}
