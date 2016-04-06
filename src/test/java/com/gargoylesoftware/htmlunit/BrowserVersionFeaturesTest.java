/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserFeature;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * Tests for {@link BrowserVersionFeatures}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class BrowserVersionFeaturesTest  {

    /**
     * Test of alphabetical order.
     */
    @Test
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
     * Test of usage.
     * @throws Exception in case of problems
     */
    @Test
    public void unusedFeatures() throws Exception {
        final List<BrowserVersion> browsers = new LinkedList<>();
        browsers.add(BrowserVersion.FIREFOX_38);
        browsers.add(BrowserVersion.FIREFOX_45);
        browsers.add(BrowserVersion.INTERNET_EXPLORER);
        browsers.add(BrowserVersion.CHROME);
        browsers.add(BrowserVersion.EDGE);

        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            int useCount = 0;
            for (BrowserVersion browserVersion : browsers) {
                if (browserVersion.hasFeature(feature)) {
                    useCount++;
                }
            }
            assertTrue("BrowserVersionFeatures.java: '" + feature.name() + "' in no longer in use.", useCount > 0);
            assertTrue("BrowserVersionFeatures.java: '" + feature.name() + "' is enabled for all supported browsers.",
                    useCount < browsers.size());
        }

        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            final Field field = BrowserVersionFeatures.class.getField(feature.name());
            final BrowserFeature browserFeature = field.getAnnotation(BrowserFeature.class);

            if (browserFeature != null) {
                for (final WebBrowser annotatedBrowser : browserFeature.value()) {
                    boolean inUse = false;
                    for (BrowserVersion supportedBrowser : browsers) {
                        if (expectedBrowserName(supportedBrowser).equals(annotatedBrowser.value().name())
                                && annotatedBrowser.minVersion() <= supportedBrowser.getBrowserVersionNumeric()
                                && annotatedBrowser.maxVersion() >= supportedBrowser.getBrowserVersionNumeric()) {
                            inUse = true;
                            continue;
                        }
                    }
                    assertTrue("BrowserVersionFeatures.java: Annotation '"
                            + annotatedBrowser.toString() + "' of feature '"
                            + feature.name() + "' in no longer in use.", inUse);
                }
            }
        }
    }

    private static String expectedBrowserName(final BrowserVersion browser) {
        if (browser.isIE()) {
            return "IE";
        }
        if (browser.isFirefox()) {
            return "FF";
        }
        if (browser.isEdge()) {
            return "EDGE";
        }

        return "CHROME";
    }

    /**
     * Test of usage in the Java files.
     *
     * @throws Exception in case of problems
     */
    @Test
    public void unusedFeaturesInCode() throws Exception {
        final List<String> unusedFeatures = new ArrayList<>(BrowserVersionFeatures.values().length);
        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            unusedFeatures.add(feature.name());
        }
        unusedCheck(new File("src/main/java"), unusedFeatures);
        if (!unusedFeatures.isEmpty()) {
            fail("The following " + BrowserVersionFeatures.class.getSimpleName() + " "
                    + (unusedFeatures.size() == 1 ? "is" : "are") + " not used: "
                    + StringUtils.join(unusedFeatures, ", "));
        }
    }

    private void unusedCheck(final File dir, final List<String> unusedFeatures) throws IOException {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !".svn".equals(file.getName())) {
                unusedCheck(file, unusedFeatures);
            }
            else if (file.getName().endsWith(".java")) {
                final List<String> lines = FileUtils.readLines(file);
                final String browserVersionFeatures = BrowserVersionFeatures.class.getSimpleName();
                for (final String line : lines) {
                    for (final Iterator<String> it = unusedFeatures.iterator(); it.hasNext();) {
                        if (line.contains(browserVersionFeatures + '.' + it.next())) {
                            it.remove();
                        }
                    }
                }
            }
        }
    }
}
