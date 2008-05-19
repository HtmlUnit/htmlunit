/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLImageElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 */
public class HTMLImageElementTest extends WebTestCase {

    /**
     * This test verifies that JavaScript can be used to get the src
     * attribute of an <img> tag.
     * @throws Exception if the test fails
     */
    @Test
    public void test_getSrc() throws Exception {
        final String content
            = "<html><head><title></title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.getElementById('anImage').src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {
            "http://www.gargoylesoftware.com/foo.gif"
        };

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * This test verifies that when JavaScript is used to modify the src
     * attribute, the value is persisted to the corresponding <img> node
     * in the DOM tree.
     * @throws Exception if the test fails
     */
    @Test
    public void test_setSrc() throws Exception {
        final String content
            = "<html><head><title></title><script>\n"
            + "function doTest(){\n"
            + "    document.getElementById('anImage').src = 'bar.gif';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(content, null);
        final HtmlImage image = (HtmlImage) page.getHtmlElementById("anImage");
        assertEquals("bar.gif", image.getSrcAttribute());
    }
    
    /**
     * JavaScript can be used to preload images, as follows:
     * <code>var newImage = new Image(); newImage.src = 'foo.gif';</code>.
     * When <code>new Image()</code> is called, HtmlUnit creates a new JavaScript
     * Image object. However, no corresponding DOM node is created, which is
     * just as well, since browers don't create one either.
     * This test verifies that the above JavaScript code can be invoked without
     * throwing an "IllegalStateException: DomNode has not been set for this
     * SimpleScriptable."
     * @throws Exception if the test fails
     */
    @Test
    public void test_setSrc_newImage() throws Exception {
        final String content
            = "<html><head><title></title><script>\n"
            + "function doTest(){\n"
            + "    var preloadImage = new Image();\n"
            + "    preloadImage.src = 'bar.gif';\n"
            + "    alert(preloadImage.src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        final String[] expectedAlerts = {
            "http://www.gargoylesoftware.com/bar.gif"
        };
        
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test_AttributeName() throws Exception {
        final String content
            = "<html><head><title></title><script>\n"
            + "function test()\n"
            + "{\n"
            + "  var oImg = document.getElementById('myImage');\n"
            + "  oImg.name = 'foo';\n"
            + "  alert(oImg.name);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<img src='foo.png' id='myImage'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"foo"};
        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
