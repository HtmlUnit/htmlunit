/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link EventListenersContainer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class EventListenersContainerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"someName", "myevent", "[object Window]"})
    public void addEventListener() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function MyEventListener(name) {\n"
            + "    this.name = name;\n"
            + "  }\n"
            + "\n"
            + "  MyEventListener.prototype = {\n"
            + "    handleEvent: function(evt) {\n"
            + "      log(this.name);\n"
            + "      log(evt.type);\n"
            + "      log(evt.target);\n"
            + "    }\n"
            + "  }\n"
            + "\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var listener = new MyEventListener('someName');\n"
            + "      window.addEventListener('myevent', listener, false);\n"
            + "      window.dispatchEvent(new Event('myevent'));\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"someName", "myevent", "[object HTMLBodyElement]"})
    public void addEventListener_node() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function MyEventListener(name) {\n"
            + "    this.name = name;\n"
            + "  }\n"
            + "\n"
            + "  MyEventListener.prototype = {\n"
            + "    handleEvent: function(evt) {\n"
            + "      log(this.name);\n"
            + "      log(evt.type);\n"
            + "      log(evt.target);\n"
            + "    }\n"
            + "  }\n"
            + "\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var listener = new MyEventListener('someName');\n"
            + "      document.body.addEventListener('myevent', listener, false);\n"
            + "      document.body.dispatchEvent(new Event('myevent'));\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void addEventListener_no_handleEvent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function MyEventListener(name) {\n"
            + "    this.name = name;\n"
            + "  }\n"
            + "\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var listener = new MyEventListener('someName');\n"
            + "      window.addEventListener('myevent', listener, false);\n"
            + "      window.dispatchEvent(new Event('myevent'));\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
