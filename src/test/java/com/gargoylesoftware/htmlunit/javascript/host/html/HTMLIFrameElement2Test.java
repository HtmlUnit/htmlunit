/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
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
    @Alerts(DEFAULT = { "loaded", "foo" }, IE = { })
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
            + "      myFrame.onload = handleFrameLoad;\n"
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
    @Alerts(DEFAULT = { "loaded", "" }, IE = { })
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
            + "      myFrame.onload = handleFrameLoad;\n"
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
    @Alerts(DEFAULT = { "createIFrame", "loaded", "foo" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad2() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
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
    @Alerts(DEFAULT = { "createIFrame", "loaded", "" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad2_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
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
    @Alerts(DEFAULT = { "createIFrame", "loaded", "foo" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad3() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
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
    @Alerts(DEFAULT = { "createIFrame", "loaded", "" }, IE = { "createIFrame" })
    public void documentCreateElement_onLoad3_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
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
    public void documentFragmentCreateElement_onLoad() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body>\n"
            + "    <script>\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
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
    public void documentFragmentCreateElement_onLoad_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body>\n"
            + "    <script>\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad2() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad2_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad3() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad3_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts(DEFAULT = { "fragment append done", "loaded" },
            IE = "fragment append done")
    public void documentDocumentFragmentCreateElement_onLoad() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body>\n"
            + "    <script>\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "      alert('fragment append done');\n"
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
    @Alerts({ "fragment append done", "loaded" })
    public void documentDocumentFragmentCreateElement_onLoad_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body>\n"
            + "    <script>\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "      alert('fragment append done');\n"
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
    @Alerts(DEFAULT = { "createIFrame", "fragment append done", "loaded" },
            IE = { "createIFrame", "fragment append done" })
    public void documentDocumentFragmentCreateElement_onLoad2() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "      alert('fragment append done');\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts({ "createIFrame", "fragment append done", "loaded" })
    public void documentDocumentFragmentCreateElement_onLoad2_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "      alert('fragment append done');\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts(DEFAULT = { "createIFrame", "fragment append done", "loaded" },
            IE = { "createIFrame", "fragment append done" })
    public void documentDocumentFragmentCreateElement_onLoad3() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      myFrame.src = '" + URL_SECOND + "';\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "      alert('fragment append done');\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts({ "createIFrame", "fragment append done", "loaded" })
    public void documentDocumentFragmentCreateElement_onLoad3_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + "  function createIFrame() {\n"
            + "      alert('createIFrame');\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "      alert('fragment append done');\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    alert('loaded');\n"
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
    @Alerts(DEFAULT = { "left", "right", "bottom", "middle", "top", "wrong", "" },
            IE = { "left", "right", "bottom", "middle", "top", "", "" })
    @NotYetImplemented(IE)
    public void getAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <iframe id='i1' align='left' ></iframe>\n"
            + "  <iframe id='i2' align='right' ></iframe>\n"
            + "  <iframe id='i3' align='bottom' ></iframe>\n"
            + "  <iframe id='i4' align='middle' ></iframe>\n"
            + "  <iframe id='i5' align='top' ></iframe>\n"
            + "  <iframe id='i6' align='wrong' ></iframe>\n"
            + "  <iframe id='i7' ></iframe>\n"

            + "<script>\n"
            + "  for (i=1; i<=7; i++) {\n"
            + "    alert(document.getElementById('i'+i).align);\n"
            + "  };\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "center", "8", "foo", "left", "right", "bottom", "middle", "top" },
            IE = { "center", "error", "center", "error", "center", "left", "right", "bottom", "middle", "top" })
    @NotYetImplemented(IE)
    public void setAlign() throws Exception {
        final String html
            = "<html><body>\n"
            + "  <iframe id='i1' align='left' ></iframe>\n"

            + "<script>\n"
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch (e) { alert('error'); }\n"
            + "    alert(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('i1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'bottom');\n"
            + "  setAlign(elem, 'middle');\n"
            + "  setAlign(elem, 'top');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
