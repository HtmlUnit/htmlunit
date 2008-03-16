/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Unit tests for {@link MimeType}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class MimeTypeTest extends WebTestCase {

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
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
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
            new HashSet<PluginConfiguration>(BrowserVersion.FIREFOX_2.getPlugins());
        BrowserVersion.FIREFOX_2.getPlugins().clear();
        try {
            loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
            assertEquals(expectedAlerts, collectedAlerts);
        }
        finally {
            BrowserVersion.FIREFOX_2.getPlugins().addAll(plugins);
        }
    }
}
