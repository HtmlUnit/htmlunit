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
package org.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.htmlunit.javascript.configuration.BrowserFeature;
import org.htmlunit.javascript.configuration.SupportedBrowser;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.thirdparty.com.google.common.base.Objects;

/**
 * Tests for {@link BrowserVersionFeatures}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class BrowserVersionFeaturesTest {

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
        browsers.add(BrowserVersion.CHROME);
        browsers.add(BrowserVersion.EDGE);
        browsers.add(BrowserVersion.FIREFOX);
        browsers.add(BrowserVersion.FIREFOX_ESR);

        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            int useCount = 0;
            for (final BrowserVersion browserVersion : browsers) {
                if (browserVersion.hasFeature(feature)) {
                    useCount++;
                }
            }
            assertTrue(useCount > 0, "BrowserVersionFeatures.java: '" + feature.name() + "' in no longer in use.");
            assertTrue(useCount < browsers.size(),
                    "BrowserVersionFeatures.java: '" + feature.name() + "' is enabled for all supported browsers.");
        }

        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            final Field field = BrowserVersionFeatures.class.getField(feature.name());
            final BrowserFeature browserFeature = field.getAnnotation(BrowserFeature.class);

            if (browserFeature != null) {
                for (final SupportedBrowser annotatedBrowser : browserFeature.value()) {
                    boolean inUse = false;
                    for (final BrowserVersion supportedBrowserVersion : browsers) {
                        if (Objects.equal(supportedBrowser(supportedBrowserVersion), annotatedBrowser)) {
                            inUse = true;
                            continue;
                        }
                    }
                    assertTrue(inUse,
                            "BrowserVersionFeatures.java: Annotation '"
                            + annotatedBrowser + "' of feature '"
                            + feature.name() + "' in no longer in use.");
                }
            }
        }
    }

    private static SupportedBrowser supportedBrowser(final BrowserVersion browser) {
        if (browser == BrowserVersion.EDGE) {
            return SupportedBrowser.EDGE;
        }
        if (browser == BrowserVersion.FIREFOX) {
            return SupportedBrowser.FF;
        }
        if (browser == BrowserVersion.FIREFOX_ESR) {
            return SupportedBrowser.FF_ESR;
        }

        return SupportedBrowser.CHROME;
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
                    + String.join(", ", unusedFeatures));
        }
    }

    private void unusedCheck(final File dir, final List<String> unusedFeatures) throws IOException {
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory() && !".git".equals(file.getName())) {
                    unusedCheck(file, unusedFeatures);
                }
                else if (file.getName().endsWith(".java")) {
                    final List<String> lines = FileUtils.readLines(file, ISO_8859_1);
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
}
