/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ClassConfiguration}.
 *
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ClassConfigurationTest {

    /**
     * @throws Exception on error
     */
    @Test
    public void forJSFlagTrue() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class, null, true, null, "");
        assertTrue("JSObject Flag should have been set", config1.isJsObject());
    }

    /**
     * @throws Exception on error
     */
    @Test
    public void forJSFlagFalse() throws Exception {
        final ClassConfiguration config1 = new ClassConfiguration(ConfigTestClass.class, null, false, null, "");
        assertFalse("JSObject Flag should not have been set", config1.isJsObject());
    }

    /**
     * Test class.
     */
    protected class ConfigTestClass extends HtmlUnitScriptable {
        private boolean test_ = false;

        /**
         * Dummy function.
         */
        public void function() {
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
        public void jsxSet_test(final Boolean testFlag) {
            test_ = testFlag;
        }
    }
}
