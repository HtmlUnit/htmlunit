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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
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
    @Alerts(DEFAULT = { "loaded", "foo" },
            IE8 = { })
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
    @Alerts(DEFAULT = { "loaded", "" },
            IE8 = { })
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
    @Alerts(DEFAULT = { "createIFrame", "loaded", "foo" },
            IE8 = { "createIFrame" })
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
    @Alerts(DEFAULT = { "createIFrame", "loaded", "" },
            IE8 = { "createIFrame" })
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
     * A frame element that is not appended to the document should not be loaded.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("created")
    public void documentCreateElement_noAppendNoLoad() throws Exception {
        final String html = "<html><body><script>\n"
            + "var myFrame = document.createElement('iframe');\n"
            + "myFrame.src = 'notExisting.html';\n"
            + "alert('created');\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * Test case for issue ##1544.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "createIFrame", "loaded" })
    @NotYetImplemented
    public void documentCreateElement_iFrameInsideDiv() throws Exception {
        final String html =
                "<html>\n"
              + "<head><script type='text/javascript'>\n"
              + "  function createIFrame() {\n"
              + "      alert('createIFrame');\n"
              + "      var content = document.getElementById('content');\n"
              + "      var newContent = document.createElement('div');\n"
              + "      newContent.innerHTML = '<iframe name=\"iFrame\" src=\"" + URL_SECOND + "\"></iframe>';\n"
              + "      content.appendChild(newContent);\n"
              + "  }\n"
              + "</script></head>\n"

              + "  <body>\n"
              + "    <div id='content'>content</div>"
              + "    <a id='test' onclick='createIFrame();'>insert frame</a>"
              + "  </body>\n"
              + "</html>";
        final String html2 = "<html><body><script>alert('loaded')</script></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("test")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "createIFrame", "loaded", "foo" },
            IE8 = { "createIFrame" })
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
    @Alerts(DEFAULT = { "createIFrame", "loaded", "" },
            IE8 = { "createIFrame" })
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
            IE8 = "fragment append done")
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
            IE8 = { "createIFrame", "fragment append done" })
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
            IE8 = { "createIFrame", "fragment append done" })
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
    @NotYetImplemented(IE8)
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
    @Alerts(DEFAULT = { "CenTer", "8", "foo", "left", "right", "bottom", "middle", "top" },
            IE = { "center", "error", "center", "error", "center", "left", "right", "bottom", "middle", "top" })
    @NotYetImplemented(IE8)
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "loaded", "loaded", "loaded" })
    public void onLoadCalledEachTimeFrameContentChanges() throws Exception {
        final String html =
              "<html>\n"
            + "  <body>\n"
            + "    <iframe id='testFrame' onload='alert(\"loaded\");'></iframe>\n"
            + "    <div id='d1' onclick='i.contentWindow.location.replace(\"blah.html\")'>1</div>\n"
            + "    <div id='d2' onclick='i.contentWindow.location.href=\"blah.html\"'>2</div>\n"
            + "    <script>var i = document.getElementById('testFrame')</script>\n"
            + "  </body>\n"
            + "</html>";

        final String frameHtml = "<html><body>foo</body></html>";

        getMockWebConnection().setDefaultResponse(frameHtml);

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("d1")).click();
        driver.findElement(By.id("d2")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
