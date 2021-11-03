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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link EventTarget}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class EventTargetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("not defined")
    public void cloneEvent() throws Exception {
        final String html = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('myId');\n"
            + "    if (div.attachEvent) {\n"
            + "      div.attachEvent('onclick', clickFn = function() {\n"
            + "        log('called!');\n"
            + "      });\n"
            + "      var clone = div.cloneNode(true);\n"
            + "      clone.fireEvent('onclick');\n"
            + "    } else {\n"
            + "        log('not defined');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myId'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
