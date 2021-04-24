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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link HTMLAudioElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLAudioElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLAudioElement]", "function HTMLAudioElement() { [native code] }"},
            FF = {"[object HTMLAudioElement]", "function HTMLAudioElement() {\n    [native code]\n}"},
            FF78 = {"[object HTMLAudioElement]", "function HTMLAudioElement() {\n    [native code]\n}"},
            IE = {"[object HTMLAudioElement]", "[object HTMLAudioElement]"})
    public void type() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('a1');\n"
            + "    try {\n"
            + "      alert(elem);\n"
            + "      alert(HTMLAudioElement);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <audio id='a1'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLAudioElement]", "done"})
    public void audio() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = new Audio('1.mp3');\n"
            + "    alert(a);\n"
            + "    a.play();\n"
            + "    alert('done');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * Checks whether the specific {@code parent} is an actual parent of the given {@code child}.
     *
     * @param parent the parent host name
     * @param child the child host name
     * @throws Exception if an error occurs
     */
    protected void parentOf(final String parent, final String child) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>" + (getBrowserVersion().isIE() ? "Blank Page" : "New Tab") + "</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(isParentOf(" + parent + ", " + child + "));\n"
            + "    } catch(e) { alert('false'); }\n"
            + "  }\n"

            + "  /*\n"
            + "   * Returns true if o1 prototype is parent/grandparent of o2 prototype\n"
            + "   */\n"
            + "  function isParentOf(o1, o2) {\n"
            + "    o1.prototype.myCustomFunction = function() {};\n"
            + "    return o2.prototype.myCustomFunction != undefined;\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented
    public void Audio_HTMLAudioElement() throws Exception {
        parentOf("Audio", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void HTMLAudioElement_Audio() throws Exception {
        parentOf("HTMLAudioElement", "Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void doNotRetrieveStream() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <audio controls>\n"
            + "    <source src='horse.ogg' type='audio/ogg'>\n"
            + "    <source src='horse.mp3' type='audio/mpeg'>\n"
            + "    Your browser does not support the audio element.\n"
            + "  </audio>\n"
            + "</body></html>";

        loadPage2(html);
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLAudioElement]", "maybe", "done"},
            IE = {"[object HTMLAudioElement]", "", "done"})
    @NotYetImplemented(IE)
    public void nullConstructor() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = new Audio(null);\n"
            + "    alert(a);\n"
            + "    alert(a.canPlayType('audio/ogg'));\n"
            + "    alert('done');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void canPlayType() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('a1');\n"
            + "    alert(typeof elem.canPlayType === 'function');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <audio id='a1'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_AudioOgg() throws Exception {
        canPlayType("audio/ogg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_VideoOgg() throws Exception {
        canPlayType("video/ogg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_ApplicationOgg() throws Exception {
        canPlayType("application/ogg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("maybe")
    @NotYetImplemented
    public void canPlayType_Mp4() throws Exception {
        canPlayType("video/mp4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "maybe",
            FF78 = "maybe")
    @NotYetImplemented({FF, FF78})
    public void canPlayType_AudioWave() throws Exception {
        canPlayType("audio/wave");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_AudioWav() throws Exception {
        canPlayType("audio/wav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_AudioXWav() throws Exception {
        canPlayType("audio/x-wav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "maybe",
            FF78 = "maybe")
    @NotYetImplemented({FF, FF78})
    public void canPlayType_AudioPnWav() throws Exception {
        canPlayType("audio/x-pn-wav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_AudioWebm() throws Exception {
        canPlayType("audio/webm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_VideoWebm() throws Exception {
        canPlayType("video/webm");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "maybe",
            CHROME = "probably",
            EDGE = "probably")
    @NotYetImplemented
    public void canPlayType_AudioMpeg() throws Exception {
        canPlayType("audio/mpeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "probably",
            FF = "maybe",
            FF78 = "maybe",
            IE = "")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void canPlayType_AudioFlac() throws Exception {
        canPlayType("audio/flac");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            FF = "maybe",
            FF78 = "maybe")
    @NotYetImplemented({FF, FF78})
    public void canPlayType_AudioXFlac() throws Exception {
        canPlayType("audio/x-flac");
    }

    /**
     * @throws Exception if the test fails
     */
    private void canPlayType(final String mimeType) throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('a1');\n"
            + "    alert(elem.canPlayType('" + mimeType + "'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <audio id='a1'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLAudioElement]", "1"})
    public void newAudioNodeType() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = new Audio();\n"
            + "    alert(a);\n"
            + "    alert(a.nodeType);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLAudioElement]", "AUDIO"})
    public void newAudioNodeName() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var a = new Audio();\n"
            + "    alert(a);\n"
            + "    alert(a.nodeName);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
