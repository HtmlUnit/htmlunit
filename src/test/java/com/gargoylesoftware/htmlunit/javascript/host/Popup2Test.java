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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF10;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Popup}.
 *
 * @version $Revision$
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
    @Browsers({ IE, FF10 })
    public void testPopup() throws Exception {
        final String html = "<html><head><title>First</title><body>\n"
            + "<script>\n"
            + "var oPopup = window.createPopup();\n"
            + "var oPopupBody = oPopup.document.body;\n"
            + "oPopupBody.innerHTML = 'bla bla';\n"
            + "oPopup.show(100, 100, 200, 50, document.body);\n"
            + "</script>\n"
            + "</body></html>";

        loadPage2(html);
    }

    /**
     * Regression test for issue 3029277.
     * The setup of the window was not complete.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers({ IE, FF10 })
    public void testPopupBodyStyle() throws Exception {
        final String html = "<html><head><title>First</title><body>\n"
            + "<script language='javascript'>\n"
            + "  popup = window.createPopup();\n"
            + "  popupBody = popup.document.body;\n"
            + "  popupBody.style.backgroundColor = '#7f7fff';\n"
            + "</script>\n"
            + "</body></html>";

        loadPage2(html);
    }
}
