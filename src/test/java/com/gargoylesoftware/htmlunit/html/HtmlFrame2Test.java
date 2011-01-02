/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link HtmlFrame}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlFrame2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void crossFrameJavascript() throws Exception {
        final String firstHtml = "<html><body>\n"
            + "<script>function render() { window.bar.real_render(); }</script>\n"
            + "<iframe src='" + URL_SECOND + "' onload='render();' name='bar'></iframe>\n"
            + "</body></html>";

        final String secondHtml = "<html><body>\n"
            + "<script>function real_render() { alert(2); }</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);
        loadPageWithAlerts(firstHtml);
    }

    /**
     * Regression test for for bug
     * <a href="http://sf.net/support/tracker.php?aid=2819477">2819477</a>: onload handler should be called only one
     * time!
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void iframeOnloadCalledOnlyOnce() throws Exception {
        final String firstHtml = "<html><body>\n"
            + "<iframe src='" + URL_SECOND + "' onload='alert(1)'></iframe>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "");
        loadPageWithAlerts2(firstHtml);
    }

    /**
     * URL about:blank has as special meaning as it is what is loaded in the frame.
     * before the real content is loaded.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void iframeOnloadAboutBlank() throws Exception {
        final String html = "<html><body>\n"
            + "<iframe src='about:blank' onload='alert(1)'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "second [object]", "third [object]", "parent [object]" },
            FF = { "third [object HTMLFormElement]", "second [object HTMLFormElement]",
            "parent [object HTMLFormElement]" })
    @NotYetImplemented
    //real FF sometimes alerts 'second' before 'third'
    public void postponeLoading() throws Exception {
        final String html = "<FRAMESET onload=\"alert('parent ' + window.parent.frames.third.document.frm)\">\n"
            + "  <FRAME name=second frameborder=0 src='" + URL_SECOND + "'>\n"
            + "  <FRAME name=third frameborder=0 src='" + URL_THIRD + "'>\n"
            + "</FRAMSET>";

        final String secondHtml = "<html>\n"
            + "<body onload=\"alert('second ' + window.parent.frames.third.document.frm)\">\n"
            + "</body></html>";

        final String thirdHtml = "<html>\n"
            + "<body onload=\"alert('third ' + window.parent.frames.third.document.frm)\">\n"
            + "  <form name='frm' id='frm'>\n"
            + "      <input type='text' id='one' name='one' value='something'>\n"
            + "  </form>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondHtml);
        getMockWebConnection().setResponse(URL_THIRD, thirdHtml);
        loadPageWithAlerts2(html);
    }
}
