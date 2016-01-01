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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
    @Alerts(IE = { "[object HTMLAudioElement]", "[object HTMLAudioElement]" },
            CHROME = { "[object HTMLAudioElement]", "function HTMLAudioElement() { [native code] }" },
            FF = { "[object HTMLAudioElement]", "function HTMLAudioElement() {\n    [native code]\n}" })
    public void type() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "  var elem = document.getElementById('a1');\n"
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
}
