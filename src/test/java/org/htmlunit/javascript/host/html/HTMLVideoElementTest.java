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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLVideoElement}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 */
public class HTMLVideoElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void prototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "log(HTMLVideoElement.prototype == null);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLVideoElement]", "function HTMLVideoElement() { [native code] }"})
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elem = document.getElementById('v1');\n"
            + "    try {\n"
            + "      log(elem);\n"
            + "      log(HTMLVideoElement);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <video id='v1'/>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "VIDEO"})
    public void nodeTypeName() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<video id='v' src='flower.mp4'></video>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var video = document.getElementById('v');\n"
            + "  log(video.nodeType);"
            + "  log(video.nodeName);"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "§§URL§§flower.mp4", "§§URL§§tree.mp4",
             "<video id=\"v\" src=\"tree.mp4\"></video>"})
    public void src() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<video id='v' src='flower.mp4'></video>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var video = document.getElementById('v');\n"
            + "  var src = video.src;\n"
            + "  log(typeof src);"
            + "  log(src);"
            + "  video.src = 'tree.mp4';\n"
            + "  log(video.src);"
            + "  log(video.outerHTML);"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", "", "§§URL§§tree.mp4",
        "<video id=\"v\" src=\"tree.mp4\"><source src=\"flower.mp4\" type=\"video/mp4\"></video>"})
    public void srcChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<video id='v'><source src='flower.mp4' type='video/mp4'></video>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var video = document.getElementById('v');\n"
            + "  var src = video.src;\n"
            + "  log(typeof src);"
            + "  log(src);"
            + "  video.src = 'tree.mp4';\n"
            + "  log(video.src);"
            + "  log(video.outerHTML);"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", ""})
    public void srcNotDefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<video id='v'></video>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var src = document.getElementById('v').src;\n"
            + "  log(typeof src);"
            + "  log(src);"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", ""})
    public void currentSrc() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<video id='v' src='flower.mp4'></video>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var currentSrc = document.getElementById('v').currentSrc;\n"
            + "  log(typeof currentSrc);"
            + "  log(currentSrc);"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", ""})
    public void currentSrcChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<video id='v'><source src='flower.mp4' type='video/mp4'></video>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var currentSrc = document.getElementById('v').currentSrc;\n"
            + "  log(typeof currentSrc);"
            + "  log(currentSrc);"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"string", ""})
    public void currentSrcNotDefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<video id='v'></video>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  var currentSrc = document.getElementById('v').currentSrc;\n"
            + "  log(typeof currentSrc);"
            + "  log(currentSrc);"
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
