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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLFrameElement} when used for {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Thomas Robbs
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLFrameElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Frame2")
    public void testFrameName() throws Exception {
        final String html
            = "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "    <frame id='frame1'>\n"
            + "    <frame name='Frame2' onload='alert(this.name)' id='frame2'>\n"
            + "</frameset></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void testContentDocument() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame').contentDocument == frames.foo.document);\n"
                + "}\n"
                + "</script></head>\n"
                + "<frameset rows='*' onload='test()'>\n"
                + "<frame name='foo' id='myFrame' src='about:blank'/>\n"
                + "</frameset>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void testContentWindow() throws Exception {
        final String html
            = "<html><head><title>first</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  alert(document.getElementById('myFrame').contentWindow == frames.foo);\n"
                + "}\n"
                + "</script></head>\n"
                + "<frameset rows='*' onload='test()'>\n"
                + "<frame name='foo' id='myFrame' src='about:blank'/>\n"
                + "</frameset>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1192854.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "frame=OK", "frames.length=2", "frame=OK", "frames.length=0", "frame=OK", "frames.length=0" })
    public void testFrameTag1192854() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "var root=this;\n"
            + "function listframes(frame) {\n"
            + "  if (frame == null) {\n"
            + "    alert('frame=null');\n"
            + "  } else {\n"
            + "    alert('frame=OK');\n"
            + "    var len = frame.frames.length;\n"
            + "    alert('frames.length=' + len);\n"
            + "    for (var i=0; i<len; i++) {\n"
            + "      listframes(frame.frames[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "document.write('<frameset id=\"frameset1\" "
            + "rows=\"50,50\"><frame id=\"frame1-1\" "
            + "src=\"about:blank\"><frame id=\"frame1-2\" "
            + "src=\"about:blank\"></frameset>');\n"
            + "listframes(root);\n"
            + "</script>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function handler() {\n}", "null" },
            FF = { "function handler() {}", "null" },
            IE10 = { "function handler() {}", "null" })
    @NotYetImplemented({ FF17, FF24 })
    public void testOnloadNull() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() {}\n"
            + "  function test() {\n"
            + "    var iframe = document.getElementById('myFrame');\n"
            + "    iframe.onload = handler;\n"
            + "    alert(iframe.onload);\n"
            + "    iframe.onload = null;\n"
            + "    alert(iframe.onload);\n"
            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <iframe id='myFrame'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
