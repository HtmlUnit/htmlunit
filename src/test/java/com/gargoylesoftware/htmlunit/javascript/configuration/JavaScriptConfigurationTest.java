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

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link JavaScriptConfiguration}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class JavaScriptConfigurationTest extends SimpleWebTestCase {

    private static final Log LOG = LogFactory.getLog(JavaScriptConfigurationTest.class);

    /**
     * Test if configuration map expands with each new instance of BrowserVersion used.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void configurationMapExpands() throws Exception {
        // get a reference to the leaky map
        final Field field = JavaScriptConfiguration.class.getDeclaredField("ConfigurationMap_");
        field.setAccessible(true);
        final Map<?, ?> leakyMap = (Map<? , ?>) field.get(null);
        for (int i = 0; i < 3; i++) {
            final BrowserVersion browserVersion = new BrowserVersion("App", "Version", "User agent", 1);
            JavaScriptConfiguration.getInstance(browserVersion);
        }
        assertEquals(1, leakyMap.size());
    }

    /**
     * Regression test for bug 2854240.
     * This test was throwing an OutOfMemoryError when the bug existed.
     * @throws Exception if an error occurs
     */
    @Test
    public void memoryLeak() throws Exception {
        long count = 0;
        while (count++ < 3000) {
            final BrowserVersion browserVersion = new BrowserVersion(
                "App" + RandomStringUtils.randomAlphanumeric(20),
                "Version" + RandomStringUtils.randomAlphanumeric(20),
                "User Agent" + RandomStringUtils.randomAlphanumeric(20),
                1);
            JavaScriptConfiguration.getInstance(browserVersion);
            if (LOG.isInfoEnabled()) {
                LOG.info("count: " + count + "; memory stats: " + getMemoryStats());
            }
        }
        System.gc();
    }

    private String getMemoryStats() {
        final Runtime rt = Runtime.getRuntime();
        final long free = rt.freeMemory() / 1024;
        final long total = rt.totalMemory() / 1024;
        final long max = rt.maxMemory() / 1024;
        final long used = total - free;
        final String format = "used: {0,number,0}K, free: {1,number,0}K, total: {2,number,0}K, max: {3,number,0}K";
        return MessageFormat.format(format,
                Long.valueOf(used), Long.valueOf(free), Long.valueOf(total), Long.valueOf(max));
    }

}
