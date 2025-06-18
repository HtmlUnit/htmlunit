/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host.html;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link HTMLIFrameElement}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 * @author Marc Guillemot
 */
public class HTMLIFrameElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void onLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "    </script>\n"
            + "    <iframe id='i' onload='log(\"loaded\");' src='" + URL_SECOND + "'></iframe>\n"
            + "  </body>\n"
            + "</html>";

        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void onLoad_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "    </script>\n"
            + "    <iframe id='i' onload='log(\"loaded\");'></iframe>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void documentWrite_onLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "document.write(\"<iframe id='i' onload='log(\\\"loaded\\\");' src='" + URL_SECOND + "'></iframe>\");\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void documentWrite_onLoad_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      document.write(\"<iframe id='i' onload='log(\\\"loaded\\\");'></iframe>\");\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"loaded", "foo"})
    public void documentCreateElement_onLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    log(myFrame.contentWindow.document.body.innerHTML);\n"
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
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"loaded", ""})
    public void documentCreateElement_onLoad_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    log(myFrame.contentWindow.document.body.innerHTML);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "loaded", "foo"})
    public void documentCreateElement_onLoad2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.src = '" + URL_SECOND + "';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    log(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "loaded", ""})
    public void documentCreateElement_onLoad2_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    log(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A frame element that is not appended to the document should not be loaded.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("created")
    public void documentCreateElement_noAppendNoLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "var myFrame = document.createElement('iframe');\n"
            + "myFrame.src = 'notExisting.html';\n"
            + "log('created');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * Test case for issue ##1544.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "loaded"})
    public void documentCreateElement_iFrameInsideDiv() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<head><script type='text/javascript'>\n"
              + LOG_TITLE_FUNCTION
              + "  function createIFrame() {\n"
              + "    log('createIFrame');\n"
              + "    var content = document.getElementById('content');\n"
              + "    var newContent = document.createElement('div');\n"
              + "    newContent.innerHTML = '<iframe name=\"iFrame\" src=\"" + URL_SECOND + "\"></iframe>';\n"
              + "    content.appendChild(newContent);\n"
              + "  }\n"
              + "</script></head>\n"

              + "  <body>\n"
              + "    <div id='content'>content</div>\n"
              + "    <a id='test' onclick='createIFrame();'>insert frame</a>\n"
              + "  </body>\n"
              + "</html>";
        final String html2 = DOCTYPE_HTML
                + "<html><body>"
                + "<script>window.parent.document.title += 'loaded\\u00a7';</script></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("test")).click();

        verifyTitle2(driver, getExpectedAlerts());
        assertEquals(2, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "loaded", "foo"})
    public void documentCreateElement_onLoad3() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    myFrame.src = '" + URL_SECOND + "';\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    log(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "loaded", ""})
    public void documentCreateElement_onLoad3_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "    var myFrame = document.getElementById('myFrame');\n"
            + "    log(myFrame.contentWindow.document.body.innerHTML);\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void documentFragmentCreateElement_onLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
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
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void documentFragmentCreateElement_onLoad_noSrc() throws Exception {
        final String html =
              "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.src = '" + URL_SECOND + "';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad2_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad3() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    myFrame.src = '" + URL_SECOND + "';\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "loaded"})
    public void documentCreateElement_onLoad_srcAboutBlank() throws Exception {
        documentCreateElement_onLoad_srcX("about:blank");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "loaded"})
    public void documentCreateElement_onLoad_srcSomePage() throws Exception {
        documentCreateElement_onLoad_srcX("foo.html");
    }

    private void documentCreateElement_onLoad_srcX(final String iframeSrc) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "function createIFrame() {\n"
            + "  log('createIFrame');\n"
            + "  var myFrame = document.createElement('iframe');\n"
            + "  myFrame.onload = handleFrameLoad;\n"
            + "  myFrame.src = '" + iframeSrc + "';\n"
            + "  document.body.appendChild(myFrame);\n"
            + "}\n"
            + "function handleFrameLoad() {\n"
            + "  log('loaded');\n"
            + "}\n"
            + "</script>\n"
            + "<button id='it' onclick='createIFrame()'>click me</button>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("it")).click();
        if (useRealBrowser()) {
            Thread.sleep(400);
        }

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("createIFrame")
    public void documentFragmentCreateElement_onLoad3_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"fragment append done", "loaded"})
    public void documentDocumentFragmentCreateElement_onLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
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
            + "      log('fragment append done');\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"fragment append done", "loaded"})
    public void documentDocumentFragmentCreateElement_onLoad_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body>\n"
            + "    <script>\n"
            + "      var myFrame = document.createElement('iframe');\n"
            + "      myFrame.id = 'myFrame';\n"
            + "      myFrame.onload = handleFrameLoad;\n"
            + "      var fragment = document.createDocumentFragment();\n"
            + "      fragment.appendChild(myFrame);\n"
            + "      log('fragment append done');\n"
            + "      var body = document.getElementsByTagName('body')[0];\n"
            + "      body.appendChild(myFrame);\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "fragment append done", "loaded"})
    public void documentDocumentFragmentCreateElement_onLoad2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.src = '" + URL_SECOND + "';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "    log('fragment append done');\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "fragment append done", "loaded"})
    public void documentDocumentFragmentCreateElement_onLoad2_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "    log('fragment append done');\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "fragment append done", "loaded"})
    public void documentDocumentFragmentCreateElement_onLoad3() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    myFrame.src = '" + URL_SECOND + "';\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "    log('fragment append done');\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";
        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"createIFrame", "fragment append done", "loaded"})
    public void documentDocumentFragmentCreateElement_onLoad3_noSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><script type='text/javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function createIFrame() {\n"
            + "    log('createIFrame');\n"
            + "    var myFrame = document.createElement('iframe');\n"
            + "    myFrame.id = 'myFrame';\n"
            + "    myFrame.onload = handleFrameLoad;\n"
            + "    var fragment = document.createDocumentFragment();\n"
            + "    fragment.appendChild(myFrame);\n"
            + "    log('fragment append done');\n"
            + "    var body = document.getElementsByTagName('body')[0];\n"
            + "    body.appendChild(myFrame);\n"
            + "  }\n"
            + "  function handleFrameLoad() {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script></head>\n"

            + "  <body onload='createIFrame();' >\n"
            + "  </body>\n"
            + "</html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"left", "right", "bottom", "middle", "top", "wrong", ""})
    public void getAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <iframe id='i1' align='left' ></iframe>\n"
            + "  <iframe id='i2' align='right' ></iframe>\n"
            + "  <iframe id='i3' align='bottom' ></iframe>\n"
            + "  <iframe id='i4' align='middle' ></iframe>\n"
            + "  <iframe id='i5' align='top' ></iframe>\n"
            + "  <iframe id='i6' align='wrong' ></iframe>\n"
            + "  <iframe id='i7' ></iframe>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 7; i++) {\n"
            + "    log(document.getElementById('i' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CenTer", "8", "foo", "left", "right", "bottom", "middle", "top"})
    public void setAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <iframe id='i1' align='left' ></iframe>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "    log(elem.align);\n"
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"loaded", "loaded", "loaded"})
    public void onLoadCalledEachTimeFrameContentChanges() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <body>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "    </script>\n"
            + "    <iframe id='testFrame' onload='log(\"loaded\");'></iframe>\n"
            + "    <div id='d1' onclick='i.contentWindow.location.replace(\"blah.html\")'>1</div>\n"
            + "    <div id='d2' onclick='i.contentWindow.location.href=\"blah.html\"'>2</div>\n"
            + "    <script>var i = document.getElementById('testFrame')</script>\n"
            + "  </body>\n"
            + "</html>";

        final String frameHtml = DOCTYPE_HTML + "<html><body>foo</body></html>";

        getMockWebConnection().setDefaultResponse(frameHtml);

        final WebDriver driver = loadPage2(html);
        verifyTitle2(driver, getExpectedAlerts()[0]);
        driver.findElement(By.id("d1")).click();
        verifyTitle2(driver, new String[] {getExpectedAlerts()[0], getExpectedAlerts()[1]});
        driver.findElement(By.id("d2")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"about:blank", "§§URL§§", "§§URL§§"})
    public void location() throws Exception {
        final String html = DOCTYPE_HTML
              + "<html>\n"
              + "<head><script>\n"
              + LOG_TITLE_FUNCTION
              + "  function test() {\n"
              + "    var myFrame = document.createElement('iframe');\n"
              + "    document.body.appendChild(myFrame);\n"
              + "    var win = myFrame.contentWindow;\n"
              + "    var doc = win.document;\n"
              + "    log(win.location);\n"
              + "    doc.open();\n"
              + "    log(win.location);\n"
              + "    doc.write('');\n"
              + "    doc.close();\n"
              + "    log(win.location);\n"
              + "  }\n"
              + "</script></head>\n"
              + "  <body onload='test()'>\n"
              + "  </body>\n"
              + "</html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"loaded", "§§URL§§", "§§URL§§", "loaded", "about:blank"})
    public void removeSourceAttribute() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><script>\n"
                + LOG_TEXTAREA_FUNCTION

                + "  function test() {\n"
                + "    var myFrame = document.getElementById('i');\n"
                + "    var win = myFrame.contentWindow;\n"
                + "    log(win.location);\n"

                + "    myFrame.removeAttribute('src');\n"
                + "    log(win.location);\n"
                + "  }\n"

                + "  function test2() {\n"
                + "    var myFrame = document.getElementById('i');\n"
                + "    var win = myFrame.contentWindow;\n"
                + "    log(win.location);\n"
                + "  }\n"
                + "</script></head>\n"
                + "  <body>\n"
                + "    <iframe id='i' onload='log(\"loaded\");' src='" + URL_SECOND + "'></iframe>\n"

                + LOG_TEXTAREA
                + "    <button id='clickMe' onclick='test()'>Click Me</button>\n"
                + "    <button id='clickMe2' onclick='test2()'>Click Me</button>\n"
                + "  </body>\n"
                + "</html>";

        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        driver.findElement(By.id("clickMe2")).click();

        expandExpectedAlertsVariables(URL_SECOND);
        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"loaded", "§§URL§§", "[object HTMLDocument]", "http://localhost:22222/second/",
                "1", "[object Window]", "[object HTMLDocument]", "http://localhost:22222/second/",
                "0", "#[object Window]", "#[object HTMLDocument]", "http://localhost:22222/second/"})
    public void detach() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><script>\n"
                + LOG_TEXTAREA_FUNCTION

                + "  function test() {\n"
                + "    var myFrame = document.getElementById('i');\n"
                + "    var win = myFrame.contentWindow;\n"
                + "    log(win.location);\n"
                + "    log(win.document);\n"
                + "    log(win.document.URL);\n"
                + "    log(window.frames.length);\n"

                + "    myFrame.parentNode.removeChild(myFrame);\n"
                + "    log(win);\n"
                + "    log(win.document);\n"
                + "    log(win.document.URL);\n"
                + "    log(window.frames.length);\n"

                + "    window.setTimeout(function () "
                            + "{ log('#' + win); log('#' + win.document); log(win.document.URL); }, 42);\n"
                + "  }\n"

                + "</script></head>\n"
                + "  <body>\n"
                + "    <iframe id='i' onload='log(\"loaded\");' src='" + URL_SECOND + "'></iframe>\n"

                + LOG_TEXTAREA
                + "    <button id='clickMe' onclick='test()'>Click Me</button>\n"
                + "  </body>\n"
                + "</html>";

        final String html2 = DOCTYPE_HTML + "<html><body>foo</body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickMe")).click();
        Thread.sleep(200);

        expandExpectedAlertsVariables(URL_SECOND);
        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"iframe script", "loaded", "null", "[object Window]",
             "about:blank", "iframe script", "loaded"})
    @HtmlUnitNYI(CHROME = {"iframe script", "loaded", "null", "loaded", "[object Window]",
                           "about:blank", "iframe script", "loaded"},
            EDGE = {"iframe script", "loaded", "null", "loaded", "[object Window]",
                    "about:blank", "iframe script", "loaded"},
            FF = {"iframe script", "loaded", "null", "loaded", "[object Window]",
                  "about:blank", "iframe script", "loaded"},
            FF_ESR = {"iframe script", "loaded", "null", "loaded", "[object Window]",
                      "about:blank", "iframe script", "loaded"})
    public void detachAppend() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><script>\n"
                + LOG_TEXTAREA_FUNCTION

                + "  function test() {\n"
                + "    var myFrame = document.getElementById('i');\n"

                + "    var parent = myFrame.parentNode;\n"
                + "    parent.removeChild(myFrame);\n"
                + "    log(myFrame.contentWindow);\n"

                + "    parent.appendChild(myFrame);\n"
                + "    log(myFrame.contentWindow);\n"
                + "    log(myFrame.contentWindow.location);\n"
                + "  }\n"

                + "</script></head>\n"
                + "  <body>\n"
                + "    <iframe id='i' onload='log(\"loaded\");' src='" + URL_SECOND + "'></iframe>\n"

                + LOG_TEXTAREA
                + "    <button id='clickMe' onclick='test()'>Click Me</button>\n"
                + "  </body>\n"
                + "</html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>foo<script>parent.log('iframe script');</script></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        final WebDriver driver = loadPage2(html);
        Thread.sleep(DEFAULT_WAIT_TIME.toMillis());
        final int start = getMockWebConnection().getRequestCount();

        driver.findElement(By.id("clickMe")).click();
        Thread.sleep(DEFAULT_WAIT_TIME.toMillis());

        assertEquals(1, getMockWebConnection().getRequestCount() - start);

        expandExpectedAlertsVariables(URL_SECOND);
        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"iframe external script", "loaded", "null", "[object Window]",
             "about:blank", "iframe external script", "loaded"})
    @HtmlUnitNYI(CHROME = {"iframe external script", "loaded", "null", "loaded", "[object Window]",
                           "about:blank", "iframe external script", "loaded"},
            EDGE = {"iframe external script", "loaded", "null", "loaded", "[object Window]",
                    "about:blank", "iframe external script", "loaded"},
            FF = {"iframe external script", "loaded", "null", "loaded", "[object Window]",
                  "about:blank", "iframe external script", "loaded"},
            FF_ESR = {"iframe external script", "loaded", "null", "loaded", "[object Window]",
                      "about:blank", "iframe external script", "loaded"})
    public void detachAppendExternalScript() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<head><script>\n"
                + LOG_TEXTAREA_FUNCTION

                + "  function test() {\n"
                + "    var myFrame = document.getElementById('i');\n"

                + "    var parent = myFrame.parentNode;\n"
                + "    parent.removeChild(myFrame);\n"
                + "    log(myFrame.contentWindow);\n"

                + "    parent.appendChild(myFrame);\n"
                + "    log(myFrame.contentWindow);\n"
                + "    log(myFrame.contentWindow.location);\n"
                + "  }\n"

                + "</script></head>\n"
                + "  <body>\n"
                + "    <iframe id='i' onload='log(\"loaded\");' src='" + URL_SECOND + "'></iframe>\n"

                + LOG_TEXTAREA
                + "    <button id='clickMe' onclick='test()'>Click Me</button>\n"
                + "  </body>\n"
                + "</html>";

        final String html2 = DOCTYPE_HTML
                + "<html><body>foo<script src='"
                      + URL_SECOND + "ext.js'></script></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);
        final String js = "parent.log('iframe external script');";
        getMockWebConnection().setResponse(new URL(URL_SECOND, "ext.js"), js, MimeType.TEXT_JAVASCRIPT);

        final WebDriver driver = loadPage2(html);
        Thread.sleep(DEFAULT_WAIT_TIME.toMillis());
        final int start = getMockWebConnection().getRequestCount();

        driver.findElement(By.id("clickMe")).click();
        Thread.sleep(DEFAULT_WAIT_TIME.toMillis());

        assertEquals(2, getMockWebConnection().getRequestCount() - start);

        expandExpectedAlertsVariables(URL_SECOND);
        verifyTextArea2(driver, getExpectedAlerts());
    }
}
