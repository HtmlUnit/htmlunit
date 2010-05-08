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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

/**
 * Tests for {@link BrowserVersionFeatures}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class BrowserVersionFeaturesTest extends WebTestCase {

    /**
     * Test of alphabetical order.
     */
    @Test
    public void lexicographicOrder() {
        String lastFeatureName = null;
        for (BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            final String featureName = feature.name();
            if (lastFeatureName != null && featureName.compareTo(lastFeatureName) < 1) {
                fail("BrowserVersionFeatures.java: \""
                    + featureName + "\" should be before \"" + lastFeatureName + '"');
            }
            lastFeatureName = featureName;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lexicographicOrder_properties() throws Exception {
        lexicographicOrder_properties(BrowserVersion.INTERNET_EXPLORER_6);
        lexicographicOrder_properties(BrowserVersion.INTERNET_EXPLORER_7);
        lexicographicOrder_properties(BrowserVersion.INTERNET_EXPLORER_8);
        lexicographicOrder_properties(BrowserVersion.FIREFOX_2);
        lexicographicOrder_properties(BrowserVersion.FIREFOX_3);
        lexicographicOrder_properties(BrowserVersion.FIREFOX_3_6);
    }

    private void lexicographicOrder_properties(final BrowserVersion browserVersion) throws Exception {
        final String path = "com/gargoylesoftware/htmlunit/javascript/configuration/" + browserVersion.getNickname()
            + ".properties";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String lastFeatureName = null;
            String line;
            while ((line = reader.readLine()) != null) {
                final String featureName = line.trim();
                if (lastFeatureName != null && featureName.compareTo(lastFeatureName) < 1) {
                    fail(path + ": \"" + featureName + "\" should be before \"" + lastFeatureName + '"');
                }
                lastFeatureName = featureName;
            }
        }
        finally {
            reader.close();
        }
    }
}
