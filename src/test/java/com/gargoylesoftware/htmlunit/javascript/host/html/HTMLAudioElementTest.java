/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLAudioElement}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLAudioElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"[object HTMLAudioElement]", "[object HTMLAudioElement]"},
            CHROME = {"[object HTMLAudioElement]", "function HTMLAudioElement() { [native code] }"},
            FF = {"[object HTMLAudioElement]", "function HTMLAudioElement() {\n    [native code]\n}"})
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
}
