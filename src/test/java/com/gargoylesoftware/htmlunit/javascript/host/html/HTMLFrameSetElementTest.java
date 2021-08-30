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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLFrameSetElement}.
 *
 * @author Bruce Chapman
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLFrameSetElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"20%,*", "*,*"})
    public void cols() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.getElementById('fs').cols);\n"
            + "  document.getElementById('fs').cols = '*,*';\n"
            + "  log(document.getElementById('fs').cols);\n"
            + "}\n"
            + "</script></head>\n"
            + "<frameset id='fs' cols='20%,*' onload='test()'>\n"
            + "  <frame name='left' src='about:blank' />\n"
            + "  <frame name='right' src='about:blank' />\n"
            + "</frameset>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"20%,*", "*,*"})
    public void rows() throws Exception {
        final String framesetContent =
            "<html><head></head>\n"
            + "<frameset id='fs' rows='20%,*'>\n"
            + "  <frame name='top' src='" + URL_SECOND + "' />\n"
            + "  <frame name='bottom' src='about:blank' />\n"
            + "</frameset>\n"
            + "</html>";

        final String frameContent =
            "<html><head><title>TopFrame</title>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  alert(parent.document.getElementById('fs').rows);\n"
            + "  parent.document.getElementById('fs').rows = '*,*';\n"
            + "  alert(parent.document.getElementById('fs').rows);\n"
            + "}</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, frameContent);

        loadPageWithAlerts2(framesetContent);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"<frameset id=\"fs\" onload=\"test()\"> </frameset>", "new"})
    public void outerHTML() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.getElementById('fs').outerHTML);\n"
            + "  document.getElementById('fs').outerHTML = '<div id=\"new\">text<div>';\n"
            + "  log(document.getElementById('new').id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<frameset id='fs' onload='test()'>\n"
            + "</frameset>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
