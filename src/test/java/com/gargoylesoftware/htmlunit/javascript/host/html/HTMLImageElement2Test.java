/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLImageElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLImageElement2Test extends SimpleWebTestCase {

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "1" })
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

        final MockWebConnection conn = getMockWebConnection();
        final URL imageUrl = new URL(getDefaultUrl(), "foo.png");
        conn.setResponse(imageUrl, "foo", "image/png");

        loadPageWithAlerts(html);
        assertEquals(imageUrl, conn.getLastWebRequest().getUrl());
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute, it gets downloaded
     * and the <tt>onload</tt> handler gets invoked.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void onLoad_calledWhenImageDownloaded_dynamic() throws Exception {
        final String html = "<html><body><script>\n"
            + "var i = document.createElement('img');\n"
            + "i.src = '" + URL_SECOND + "';\n"
            + "i.src = '" + URL_THIRD + "';\n"
            + "i.onload = function(){alert(1);};\n"
            + "</script></body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_SECOND, "foo", "image/png");
        conn.setResponse(URL_THIRD, "foo", "image/png");

        loadPageWithAlerts(html);
        assertEquals(URL_THIRD, conn.getLastWebRequest().getUrl());
    }

    /**
     * Verifies that if an image is created if the page is already
     * finished, the onload handler is called.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "2" })
    public void onLoad_calledWhenImageDownloaded_dynamic_twoSteps() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var i = document.createElement('img');\n"
            + "  i.src = '" + URL_SECOND + "';\n"
            + "  i.onload = function() {\n"
            + "    alert(1);\n"
            + "    var i2 = document.createElement('img');\n"
            + "    i2.src = '" + URL_THIRD + "';\n"
            + "    i2.onload = function() {\n"
            + "      alert(2);\n"
            + "    };\n"
            + "  };\n"
            + "</script></body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_SECOND, "foo", "image/png");
        conn.setResponse(URL_THIRD, "foo", "image/png");

        loadPageWithAlerts(html);
        assertEquals(URL_THIRD, conn.getLastWebRequest().getUrl());
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
            + "  i.setAttribute('src','" + URL_SECOND + "');\n"
            + "  var t = setTimeout(function(){clearTimeout(t);}, 500);\n"
            + "</script></body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_SECOND, "foo", "image/png");
        conn.setResponse(URL_THIRD, "foo", "image/png");

        loadPageWithAlerts(html);

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

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_SECOND, "foo", 404, "Not Found", "text/html", new ArrayList<NameValuePair>());

        loadPageWithAlerts(html);

        assertEquals(URL_SECOND, conn.getLastWebRequest().getUrl());
    }

    /**
     * Verifies that if an image has an <tt>onload</tt> attribute but javascript is disabled,
     * the image is not downloaded.
     * Issue: 3123380
     * @throws Exception if an error occurs
     */
    @Test
    public void onLoad_notDownloadedWhenJavascriptDisabled() throws Exception {
        final String html = "<html><body><img src='" + URL_SECOND + "' onload='alert(1)'></body></html>";

        final WebClient client = getWebClientWithMockWebConnection();
        client.getOptions().setJavaScriptEnabled(false);

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "foo", "image/png");

        loadPageWithAlerts(html);
        assertEquals(URL_FIRST, conn.getLastWebRequest().getUrl());
    }

}
