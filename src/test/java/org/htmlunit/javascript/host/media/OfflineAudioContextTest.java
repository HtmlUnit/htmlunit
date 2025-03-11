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
package org.htmlunit.javascript.host.media;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link OfflineAudioContext}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class OfflineAudioContextTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void inWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      log('OfflineAudioContext' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "TypeError", "[object OfflineAudioContext]"})
    public void ctor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "    function test() {\n"
            + "      if (!('OfflineAudioContext' in window)) {\n"
            + "        log('OfflineAudioContext not available');\n"
            + "        return;\n"
            + "      }\n"

            + "      log(typeof OfflineAudioContext);\n"
            + "      try {\n"
            + "        log(new OfflineAudioContext());\n"
            + "      } catch(e) { logEx(e); }\n"
            + "      log(new OfflineAudioContext({length: 44100 * 1, sampleRate: 44100}));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object AudioBufferSourceNode]")
    public void createBufferSource() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (!('OfflineAudioContext' in window)) {\n"
            + "        log('OfflineAudioContext not available');\n"
            + "        return;\n"
            + "      }\n"

            + "      var audioCtx = new OfflineAudioContext({length: 44100 * 1, sampleRate: 44100});\n"
            + "      var source = audioCtx.createBufferSource();\n"
            + "      log(source);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OfflineAudioContext prep done", "Error with decoding audio data", "EncodingError/DOMException"})
    @HtmlUnitNYI(CHROME = {"OfflineAudioContext prep done", "Error with decoding audio data",
                           "NotSupportedError/DOMException"},
            EDGE = {"OfflineAudioContext prep done", "Error with decoding audio data",
                    "NotSupportedError/DOMException"},
            FF = {"OfflineAudioContext prep done", "Error with decoding audio data",
                  "NotSupportedError/DOMException"},
            FF_ESR = {"OfflineAudioContext prep done", "Error with decoding audio data",
                      "NotSupportedError/DOMException"})
    public void decodeAudioData() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "    function test() {\n"
            + "      if (!('OfflineAudioContext' in window)) {\n"
            + "        log('OfflineAudioContext not available');\n"
            + "        return;\n"
            + "      }\n"

            + "      var audioCtx = new OfflineAudioContext({length: 44100 * 1, sampleRate: 44100});\n"
            + "      var audioData = new ArrayBuffer(0);\n"
            + "      audioCtx.decodeAudioData(audioData,\n"
            + "             function(buffer) { log('Decoding audio data done'); },\n"
            + "             function(e) { log('Error with decoding audio data'); logEx(e); }\n"
            + "           );\n"
            + "      log('OfflineAudioContext prep done');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"OfflineAudioContext prep done", "Error with decoding audio data", "EncodingError/DOMException"})
    @HtmlUnitNYI(CHROME = {"OfflineAudioContext prep done", "Error with decoding audio data",
                           "NotSupportedError/DOMException"},
            EDGE = {"OfflineAudioContext prep done", "Error with decoding audio data",
                    "NotSupportedError/DOMException"},
            FF = {"OfflineAudioContext prep done", "Error with decoding audio data",
                  "NotSupportedError/DOMException"},
            FF_ESR = {"OfflineAudioContext prep done", "Error with decoding audio data",
                      "NotSupportedError/DOMException"})
    public void decodeAudioData2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "    function test() {\n"
            + "      if (!('OfflineAudioContext' in window)) {\n"
            + "        log('OfflineAudioContext not available');\n"
            + "        return;\n"
            + "      }\n"

            + "      var audioCtx = new OfflineAudioContext({length: 44100 * 1, sampleRate: 44100});\n"
            + "      var audioData = new ArrayBuffer(0);\n"
            + "      audioCtx.decodeAudioData(audioData).then(\n"
            + "             function(buffer) { log('Decoding audio data done'); },\n"
            + "             function(e) { log('Error with decoding audio data'); logEx(e); }\n"
            + "           );\n"
            + "      log('OfflineAudioContext prep done');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "-3.4028234663852886e+38", "3.4028234663852886e+38", "1", "0.5"})
    public void createGain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "    function test() {\n"
            + "      if (!('OfflineAudioContext' in window)) {\n"
            + "        log('OfflineAudioContext not available');\n"
            + "        return;\n"
            + "      }\n"

            + "      var audioCtx = new OfflineAudioContext({length: 44100 * 1, sampleRate: 44100});\n"
            + "      var gainNode = audioCtx.createGain();\n"
            + "      log(gainNode.gain.defaultValue);\n"
            + "      log(gainNode.gain.minValue);\n"
            + "      log(gainNode.gain.maxValue);\n"
            + "      log(gainNode.gain.value);\n"

            + "      gainNode.gain.value = 0.5;\n"
            + "      log(gainNode.gain.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function startRendering() { [native code] }")
    public void startRendering() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (!('OfflineAudioContext' in window)) {\n"
            + "        log('OfflineAudioContext not available');\n"
            + "        return;\n"
            + "      }\n"
            + "      var offlineCtx = new OfflineAudioContext(2, 44100*40, 44100);\n"
            + "      log(offlineCtx.startRendering);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
