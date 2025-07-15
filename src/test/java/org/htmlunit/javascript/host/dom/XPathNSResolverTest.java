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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XPathNSResolver}.
 *
 * @author Ronald Brill
 */
public class XPathNSResolverTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError",
             "false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError"})
    public void windowScope() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html></body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('xPathNSResolver' in window);\n"
            + "  log(window.xPathNSResolver);\n"
            + "  try { log(xPathNSResolver); } catch(e) { logEx(e); };\n"
            + "  try { log(xPathNSResolver.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(xPathNSResolver.__proto__); } catch(e) { logEx(e); };\n"

            + "  log('XPathNSResolver' in window);\n"
            + "  log(window.XPathNSResolver);\n"
            + "  try { log(XPathNSResolver); } catch(e) { logEx(e); };\n"
            + "  try { log(XPathNSResolver.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(XPathNSResolver.__proto__); } catch(e) { logEx(e); };\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
