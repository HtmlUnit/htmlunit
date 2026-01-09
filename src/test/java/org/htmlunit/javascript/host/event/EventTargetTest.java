/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.event;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link EventTarget}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class EventTargetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("not defined")
    public void cloneEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"before dispatchEvent()", "dispatchEvent() listener",
             "insertBefore start", "insertBefore done", "after dispatchEvent()", "external script"})
    @HtmlUnitNYI(
            CHROME = {"before dispatchEvent()", "dispatchEvent() listener",
                      "insertBefore start", "insertBefore done", "external script", "after dispatchEvent()"},
            EDGE = {"before dispatchEvent()", "dispatchEvent() listener",
                    "insertBefore start", "insertBefore done", "external script", "after dispatchEvent()"},
            FF = {"before dispatchEvent()", "dispatchEvent() listener",
                  "insertBefore start", "insertBefore done", "external script", "after dispatchEvent()"},
            FF_ESR = {"before dispatchEvent()", "dispatchEvent() listener",
                      "insertBefore start", "insertBefore done", "external script", "after dispatchEvent()"})
    public void dispatchEventPostponed() throws Exception {
        getMockWebConnection().setDefaultResponse("log('external script');", MimeType.TEXT_JAVASCRIPT);

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var listener = function(evt) {\n"
            + "    log('dispatchEvent() listener');\n"

            + "    var newnode = document.createElement('script');\n"
            + "    try {\n"
            + "      newnode.setAttribute('src', 'script.js');\n"
            + "      var outernode = document.getElementById('myId');\n"
            + "      log('insertBefore start');\n"
            + "      outernode.insertBefore(newnode, null);\n"
            + "      log('insertBefore done');\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"

            + "  document.getElementById('myId').addEventListener('TestEvent', listener);\n"

            + "  var myEvent = new Event('TestEvent');\n"

            + "  log('before dispatchEvent()');\n"
            + "  document.getElementById('myId').dispatchEvent(myEvent);\n"
            + "  log('after dispatchEvent()');\n"
            + "}\n"
            + "</script>\n"
            + "</head>"
            + "<body onload='test()'>\n"
            + "  <div id='myId'></div>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
