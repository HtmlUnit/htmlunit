/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Test for {@link External}.
 *
 * @version $Revision$
 * @author Peter Faller
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ExternalTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
//    public void AutoCompleteSaveForm() throws Exception {
    public void autoCompleteSaveForm() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function fnSaveForm() {\n"
            + "  window.external.AutoCompleteSaveForm(oForm);\n"
            + "  oForm.AutoCompleteTest.value='';\n"
            + "  oForm.AutoCompleteIgnore.value='';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='fnSaveForm()'>\n"
            + "<form name='oForm'>\n"
            + "\n"
            + "This text is saved:\n"
            + "<input type='text' name='AutoCompleteTest' value='abcdef'>\n"
            + "\n"
            + "This text is not saved:"
            + "<input type='text' name='AutoCompleteIgnore' autocomplete='off' value='ghijklm'>\n"
            + "\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }
}
