/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLIFrameElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void onLoad() throws Exception {
        final String html =
              "<html>\n"
            + "  <body>\n"
            + "    <iframe id='i' onload='alert(\"loaded\");' src='" + URL_SECOND + "'></iframe>\n"
            + "  </body>\n"
            + "</html>";

        final String html2 = "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void onLoad_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "  <body>\n"
            + "    <iframe id='i' onload='alert(\"loaded\");'></iframe>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void documentWrite_onLoad() throws Exception {
        final String html =
              "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      document.write(\"<iframe id='i' onload='alert(\\\"loaded\\\");' src='" + URL_SECOND + "'>"
                                        + "</iframe>\");\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void documentWrite_onLoad_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + "      document.write(\"<iframe id='i' onload='alert(\\\"loaded\\\");'></iframe>\");\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "loaded", "foo" })
    public void documentCreateElement_onLoad() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    alert(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body>\n"
            + "    <script>\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      myFrame.onload = handleFrameLoad\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "loaded", "" }, IE = { })
    public void documentCreateElement_onLoad_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    alert(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body>\n"
            + "    <script>\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "createIFrame", "loaded", "foo" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad2() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      myFrame.onload = handleFrameLoad\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    alert(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "createIFrame", "loaded", "foo" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad3() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    alert(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "createIFrame", "loaded", "" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad2_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    alert(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "createIFrame", "loaded", "" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad3_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    alert(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
