/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link HTMLMediaElement}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLMediaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void canPlayTypeBlank() throws Exception {
        canPlayType("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @HtmlUnitNYI(IE = "maybe")
    public void canPlayTypeVideoOgg() throws Exception {
        canPlayType("video/ogg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("maybe")
    public void canPlayTypeVideoMp4() throws Exception {
        canPlayType("video/mp4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @HtmlUnitNYI(IE = "maybe")
    public void canPlayTypeVideoWebm() throws Exception {
        canPlayType("video/webm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            CHROME = "probably",
            EDGE = "probably")
    @HtmlUnitNYI(CHROME = "maybe",
            EDGE = "maybe")
    public void canPlayTypeAudioMpeg() throws Exception {
        canPlayType("audio/mpeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("maybe")
    public void canPlayTypeAudioMp4() throws Exception {
        canPlayType("audio/mp4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "probably",
            IE = "")
    @HtmlUnitNYI(IE = "probably")
    public void canPlayTypeVideoOggCodecs() throws Exception {
        canPlayType("video/ogg; codecs=\"theora, vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("probably")
    public void canPlayTypeVideoMp4Codecs() throws Exception {
        canPlayType("video/mp4; codecs=\"avc1.4D401E, mp4a.40.2\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "probably",
            IE = "")
    @HtmlUnitNYI(IE = "probably")
    public void canPlayTypeAudioWebmCodecs() throws Exception {
        canPlayType("video/webm; codecs=\"vp8.0, vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "probably",
            IE = "")
    @HtmlUnitNYI(IE = "probably")
    public void canPlayTypeAudioOggCodecs() throws Exception {
        canPlayType("audio/ogg; codecs=\"vorbis\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("probably")
    public void canPlayTypeAudioMp4Codecs() throws Exception {
        canPlayType("audio/mp4; codecs=\"mp4a.40.5\"");
    }

    private void canPlayType(final String type) throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "try {\n"
            + "  var video = document.createElement('video');"
            + "  log(video.canPlayType('" + type + "'));\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLAudioElement]", "done"})
    public void pause() throws Exception {
        final String html = ""
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var a = new Audio('1.mp3');\n"
                + "    log(a);\n"
                + "    a.pause();\n"
                + "    log('done');\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
