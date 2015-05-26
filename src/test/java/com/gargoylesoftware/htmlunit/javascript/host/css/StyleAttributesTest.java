/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;

/**
 * Tests for {@link StyleAttributes}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class StyleAttributesTest {

    /**
     * Test of alphabetical order.
     */
    @Test
    public void lexicographicOrder() {
        StyleAttributes.getDefinitions(BrowserVersion.CHROME);

        String lastName = null;
        for (final Definition definition : StyleAttributes.Definition.values()) {
            final String name = definition.name();
            if (lastName != null && name.compareToIgnoreCase(lastName) < 1) {
                fail("StyleAttributes.Definition: '"
                    + name + "' should be before '" + lastName + "'");
            }
            lastName = name;
        }
    }

}
