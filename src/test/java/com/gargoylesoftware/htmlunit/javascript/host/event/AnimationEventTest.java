/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link AnimationEvent}.
 *
 * @author Ahmed Ashour
 * @author Madis Pärn
 */
@RunWith(BrowserRunner.class)
public class AnimationEventTest extends WebDriverTestCase {

    /**
     * Test for feature-request #229.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object AnimationEvent]")
    @NotYetImplemented
    public void end() throws Exception {
        final String html
            = "<html><head>\n"
            + "<style>\n"
            + "  .animate {  animation: identifier .1s ; }\n"
            + "  @keyframes identifier {\n"
            + "    0% { width: 0px; }\n"
            + "    100% { width: 30px; }\n"
            + "  }\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var el = document.getElementById('div1');\n"
            + "  el.addEventListener('animationend', function(e) {\n"
            + "    alert(e);\n"
            + "  });\n"
            + "  el.className = 'animate';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div id='div1'>TXT</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
