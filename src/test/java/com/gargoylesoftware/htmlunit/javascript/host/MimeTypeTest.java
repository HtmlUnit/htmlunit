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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Unit tests for {@link MimeType}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class MimeTypeTest extends SimpleWebTestCase {

    /**
     * Tests default configuration of Flash plugin for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    public void testFlashMimeType() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var mimeTypeFlash = navigator.mimeTypes['application/x-shockwave-flash'];\n"
            + "  alert(mimeTypeFlash);\n"
            + "  alert(mimeTypeFlash.suffixes);\n"
            + "  var pluginFlash = mimeTypeFlash.enabledPlugin;\n"
            + "  alert(pluginFlash.name);\n"
            + "  alert(pluginFlash == navigator.plugins[pluginFlash.name]);\n"
            + "  alert(pluginFlash == navigator.plugins.namedItem(pluginFlash.name));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        final String[] expectedAlerts = {"[object MimeType]", "swf", "Shockwave Flash", "true", "true"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_3_6, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Tests default configuration of Flash plugin for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    public void testRemoveFlashMimeType() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test() {\n"
            + "  var mimeTypeFlash = navigator.mimeTypes['application/x-shockwave-flash'];\n"
            + "  alert(mimeTypeFlash);\n"
            + "  alert(navigator.plugins['Shockwave Flash']);\n"
            + "  alert(navigator.plugins.namedItem('Shockwave Flash'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";
        final String[] expectedAlerts = {"undefined", "undefined", "null"};
        createTestPageForRealBrowserIfNeeded(html, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        final Set<PluginConfiguration> plugins =
            new HashSet<PluginConfiguration>(BrowserVersion.FIREFOX_3_6.getPlugins());
        BrowserVersion.FIREFOX_3_6.getPlugins().clear();
        try {
            loadPage(BrowserVersion.FIREFOX_3_6, html, collectedAlerts);
            assertEquals(expectedAlerts, collectedAlerts);
        }
        finally {
            BrowserVersion.FIREFOX_3_6.getPlugins().addAll(plugins);
        }
    }
}
