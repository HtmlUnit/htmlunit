/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLImageElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLImageElementTest extends WebTestCase {

    /**
     * This test verifies that JavaScript can be used to get the <tt>src</tt> attribute of an <tt>&lt;img&gt;</tt> tag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("http://www.gargoylesoftware.com/foo.gif")
    public void getSrc() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    alert(document.getElementById('anImage').src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getSrc_newImage_srcNotSet() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    var oImg = new Image();\n"
            + "    alert(oImg.src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * This test verifies that when JavaScript is used to modify the <tt>src</tt> attribute, the value is
     * persisted to the corresponding <tt>&lt;img&gt;</tt> node in the DOM tree.
     * @throws Exception if the test fails
     */
    @Test
    public void setSrc() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    document.getElementById('anImage').src = 'bar.gif';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null, URL_FIRST);
        final HtmlImage image = page.getHtmlElementById("anImage");
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
    @Alerts("http://www.gargoylesoftware.com/bar.gif")
    public void setSrc_newImage() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    var preloadImage = new Image();\n"
            + "    preloadImage.src = 'bar.gif';\n"
            + "    alert(preloadImage.src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void attributeName() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test()\n"
            + "{\n"
            + "  var oImg = document.getElementById('myImage');\n"
            + "  oImg.name = 'foo';\n"
            + "  alert(oImg.name);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<img src='foo.png' id='myImage'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_calledWhenImageDownloaded_static() throws Exception {
        final String html = "<html><body><img src='" + URL_SECOND + "' onload='test()'>\n"
            + "<script>\n"
            + "  alert(0)\n" // first script to be sure that img onload doesn't get executed after first JS execution
            + "</script>\n"
            + "<script>\n"
            + "function test()\n"
            + "{\n"
            + "  alert(1)\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "foo", "image/png");
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        client.getPage(URL_FIRST);
        assertEquals(URL_SECOND, conn.getLastWebRequestSettings().getUrl());

        final String[] expected = {"0", "1"};
        assertEquals(expected, actual);
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_calledWhenImageDownloaded_dynamic() throws Exception {
        final String html = "<html><body><script>\n"
            + "var i = document.createElement('img');\n"
            + "i.src = '" + URL_SECOND + "';\n"
            + "i.src = '" + URL_THIRD + "';\n"
            + "i.onload = function(){alert(1);};\n"
            + "</script></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "foo", "image/png");
        conn.setResponse(URL_THIRD, "foo", "image/png");
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        client.getPage(URL_FIRST);
        assertEquals(URL_THIRD, conn.getLastWebRequestSettings().getUrl());

        final String[] expected = {"1"};
        assertEquals(expected, actual);
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, the <tt>onload</tt> handler
     * does not get invoked if we can't download the image.
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_notCalledWhenImageNotDownloaded() throws Exception {
        final String html = "<html><body><img src='" + URL_SECOND + "' onload='alert(1)'></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "foo", 404, "not found", "text/html", new ArrayList<NameValuePair>());
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        client.getPage(URL_FIRST);
        assertEquals(URL_SECOND, conn.getLastWebRequestSettings().getUrl());

        final String[] expected = {};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "relative", "", "" })
    public void newImage() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    var i = new Image();\n"
            + "    alert(i.style != null);\n"
            + "    i.style.position = 'relative';\n"
            + "    alert(i.style.position);\n"
            + "    alert(i.border);\n"
            + "    alert(i.alt);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"", "hello", "left", "hi", "right" },
            IE = {"", "error", "", "left", "error", "left", "right" })
    public void align() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var img = document.getElementById('i');\n"
            + "        alert(img.align);\n"
            + "        set(img, 'hello');\n"
            + "        alert(img.align);\n"
            + "        set(img, 'left');\n"
            + "        alert(img.align);\n"
            + "        set(img, 'hi');\n"
            + "        alert(img.align);\n"
            + "        set(img, 'right');\n"
            + "        alert(img.align);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.align = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><img id='i' /></body>\n"
            + "</html>";
        loadPageWithAlerts(html);
    }

}
