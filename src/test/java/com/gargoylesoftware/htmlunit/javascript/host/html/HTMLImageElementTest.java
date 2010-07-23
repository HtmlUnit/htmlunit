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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLImageElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLImageElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLImageElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myId'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This test verifies that JavaScript can be used to get the <tt>src</tt> attribute of an <tt>&lt;img&gt;</tt> tag.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§foo.gif")
    public void getSrc() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function doTest(){\n"
            + "    alert(document.getElementById('anImage').src);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<img src='foo.gif' id='anImage'/>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(""); // to have a dummy response for the image
        loadPageWithAlerts2(html);
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

        loadPageWithAlerts2(html);
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

        final WebDriver driver = loadPage2(html);
        final WebElement image = driver.findElement(By.id("anImage"));
        assertEquals("bar.gif", image.getAttribute("src"));
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
    @Alerts("§§URL§§bar.gif")
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

        getMockWebConnection().setDefaultResponse(""); // to have a dummy response for the image
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void attributeName() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var oImg = document.getElementById('myImage');\n"
            + "  oImg.name = 'foo';\n"
            + "  alert(oImg.name);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<img src='foo.png' id='myImage'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(""); // to have a dummy response for the image
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_calledWhenImageDownloaded_static() throws Exception {
        final String html = "<html><body><img src='foo.png' onload='test()'>\n"
            + "<script>\n"
            + "  alert(0)\n" // first script to be sure that img onload doesn't get executed after first JS execution
            + "</script>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(1)\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final WebClient client = getWebClient();
        final URL imageUrl = new URL(getDefaultUrl(), "foo.png");

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(getDefaultUrl(), html);
        conn.setResponse(imageUrl, "foo", "image/png");
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        client.getPage(getDefaultUrl());
        assertEquals(imageUrl, conn.getLastWebRequest().getUrl());

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
        assertEquals(URL_THIRD, conn.getLastWebRequest().getUrl());

        final String[] expected = {"1"};
        assertEquals(expected, actual);
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute set from a script, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "image one", "image two" })
    public void onLoad_calledWhenImageDownloaded_mixed() throws Exception {
        final String html
            = "<html><body><img id='img' name='img'/><script>\n"
            + "  var i = document.getElementById('img');\n"
            + "  i.onload = function(){\n"
            + "    alert('image one');\n"
            + "    i.onload = function(){\n"
            + "      alert('image two');\n"
            + "    };\n"
            + "    i.src = '" + URL_THIRD + "';\n"
            + "  };\n"
            + "  i.src = '" + URL_SECOND + "';\n"
            + "  var t = setTimeout(function(){clearTimeout(t);}, 500);\n"
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
        client.waitForBackgroundJavaScript(1200);

        final List<String> requestedUrls = conn.getRequestedUrls(URL_FIRST);
        assertEquals(requestedUrls.size(), 3);
        assertEquals("", requestedUrls.get(0));
        assertEquals("second/", requestedUrls.get(1));
        assertEquals(URL_THIRD.toString(), requestedUrls.get(2));
        assertEquals(URL_THIRD, conn.getLastWebRequest().getUrl());
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
        assertEquals(URL_SECOND, conn.getLastWebRequest().getUrl());

        final String[] expected = {};
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_BadUrl() throws Exception {
        final String html = "<html><body><img src='http:// [/url]http://x.com/a/b' onload='alert(1)'/></body></html>";
        loadPageWithAlerts2(html);
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
        loadPageWithAlerts2(html);
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
        loadPageWithAlerts2(html);
    }

    /**
     * Test that image's width and height are numbers.
     * Regression test for bug
     * http://sourceforge.net/tracker/?func=detail&atid=448266&aid=2861064&group_id=47038
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "number: 300", "number: 200", "number", "number", "number", "number" })
    public void testWidthHeight() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function showInfos(imageId) {\n"
            + "    var img = document.getElementById(imageId);\n"
            + "    alert(typeof(img.width));\n"
            + "    alert(typeof(img.height));\n"
            + "  }\n"
            + "  function test() {\n"
            + "    var img1 = document.getElementById('myImage1');\n"
            + "    alert(typeof(img1.width) + ': ' + img1.width);\n"
            + "    alert(typeof(img1.height) + ': ' + img1.height);\n"
            + "    showInfos('myImage2');\n"
            + "    showInfos('myImage3');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage1' width='300' height='200'>\n"
            + "  <img id='myImage2'>\n"
            + "  <img id='myImage3' width='hello' height='hello'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
