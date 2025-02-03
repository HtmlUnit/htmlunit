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
package org.htmlunit.javascript.host;

import javax.swing.Popup;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link Popup}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Popup2Test extends WebDriverTestCase {

    /**
     * Just test that a standard use of popup works without exception.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void popup() throws Exception {
        final String html = "<html><head></title><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var oPopup = window.createPopup();\n"
            + "    var oPopupBody = oPopup.document.body;\n"
            + "    oPopupBody.innerHTML = 'bla bla';\n"
            + "    oPopup.show(100, 100, 200, 50, document.body);\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for issue 3029277.
     * The setup of the window was not complete.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void popupBodyStyle() throws Exception {
        final String html = "<html><head><body>\n"
            + "<script language='javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    popup = window.createPopup();\n"
            + "    popupBody = popup.document.body;\n"
            + "    popupBody.style.backgroundColor = '#7f7fff';\n"
            + "    log('done');\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
