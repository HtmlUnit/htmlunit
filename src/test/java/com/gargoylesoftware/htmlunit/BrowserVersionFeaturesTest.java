/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.NONE;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link BrowserVersionFeatures}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BrowserVersionFeaturesTest extends SimpleWebTestCase {

    /**
     * Test of alphabetical order.
     */
    @Test
    @Browsers(NONE)
    public void lexicographicOrder() {
        String lastFeatureName = null;
        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            final String featureName = feature.name();
            if (lastFeatureName != null && featureName.compareTo(lastFeatureName) < 1) {
                fail("BrowserVersionFeatures.java: '"
                    + featureName + "' should be before '" + lastFeatureName + "'");
            }
            lastFeatureName = featureName;
        }
    }

    /**
     * Test of alphabetical order.
     */
    @Test
    @Browsers(NONE)
    public void unusedFeatures() {
        final List<BrowserVersion> browsers = new LinkedList<BrowserVersion>();
        browsers.add(BrowserVersion.FIREFOX_17);
        browsers.add(BrowserVersion.FIREFOX_24);
        browsers.add(BrowserVersion.INTERNET_EXPLORER_8);
        browsers.add(BrowserVersion.INTERNET_EXPLORER_9);
        browsers.add(BrowserVersion.INTERNET_EXPLORER_10);
        browsers.add(BrowserVersion.CHROME);

        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            boolean inUse = false;
            for (BrowserVersion browserVersion : browsers) {
                if (browserVersion.hasFeature(feature)) {
                    inUse = true;
                    continue;
                }
            }
            assertTrue("BrowserVersionFeatures.java: '" + feature.name() + "' in no longer in use.", inUse);
        }
    }
}
