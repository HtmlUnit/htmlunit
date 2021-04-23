/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link GainNode}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class GainNodeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void inWindow() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('GainNode' in window);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1; -3.4028234663852886e+38; 3.4028234663852886e+38; 1; 0.5; ",
            IE = "AudioContext not available; ")
    public void ctor() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(msg) {\n"
            + "      var ta = document.getElementById('myTextArea');\n"
            + "      ta.value += msg + '; ';\n"
            + "    }\n"

            + "    function test() {\n"
            + "      if (!('AudioContext' in window)) {\n"
            + "        log('AudioContext not available');\n"
            + "        return;\n"
            + "      }\n"

            + "      var audioCtx = new AudioContext();\n"
            + "      var gainNode = new GainNode(audioCtx);\n"

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
            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        verifyAlerts(() -> textArea.getAttribute("value"), getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Error 1; Error 2; Error 3; Error 4; ",
            IE = "AudioContext not available; ")
    public void ctorAudiocontextMissing() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function log(msg) {\n"
            + "      var ta = document.getElementById('myTextArea');\n"
            + "      ta.value += msg + '; ';\n"
            + "    }\n"

            + "    function test() {\n"
            + "      if (!('AudioContext' in window)) {\n"
            + "        log('AudioContext not available');\n"
            + "        return;\n"
            + "      }\n"

            + "      var audioCtx = new AudioContext();\n"
            + "      try {\n"
            + "        gainNode = new GainNode();\n"
            + "      } catch(e) { log('Error 1'); }\n"

            + "      try {\n"
            + "        gainNode = new GainNode(undefined);\n"
            + "      } catch(e) { log('Error 2'); }\n"

            + "      try {\n"
            + "        gainNode = new GainNode(null);\n"
            + "      } catch(e) { log('Error 3'); }\n"

            + "      try {\n"
            + "        gainNode = new GainNode('wrong');\n"
            + "      } catch(e) { log('Error 4'); }\n"

            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='myTextArea' cols='80' rows='30'></textarea>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final WebElement textArea = driver.findElement(By.id("myTextArea"));
        verifyAlerts(() -> textArea.getAttribute("value"), getExpectedAlerts()[0]);
    }
}
