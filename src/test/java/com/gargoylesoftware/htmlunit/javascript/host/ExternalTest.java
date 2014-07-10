/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Test for {@link External}.
 *
 * @version $Revision$
 * @author Peter Faller
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ExternalTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "external defined", "no AutoCompleteSaveForm" },
            IE = { "external defined", "AutoCompleteSaveForm defined" })
    public void autoCompleteSaveForm() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function fnSaveForm() {\n"
            + "  if (window.external) {\n"
            + "    alert('external defined');\n"
            + "    if ('AutoCompleteSaveForm' in window.external) {\n"
            + "      alert('AutoCompleteSaveForm defined');\n"
            + "      window.external.AutoCompleteSaveForm(oForm);\n"
            + "      oForm.AutoCompleteTest.value='';\n"
            + "      oForm.AutoCompleteIgnore.value='';\n"
            + "    } else {\n"
            + "      alert('no AutoCompleteSaveForm');\n"
            + "    }\n"
            + "  } else {\n"
            + "    alert('no external');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='fnSaveForm()'>\n"
            + "<form name='oForm'>\n"
            + "This text is saved:\n"
            + "<input type='text' name='AutoCompleteTest' value='abcdef'>\n"
            + "This text is not saved:"
            + "<input type='text' name='AutoCompleteIgnore' autocomplete='off' value='ghijklm'>\n"
            + "</form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("AddSearchProvider defined")
    public void addSearchProvider() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.external) {\n"
            + "    if ('AddSearchProvider' in window.external) {\n"
            + "      alert('AddSearchProvider defined');\n"
            + "    } else {\n"
            + "      alert('no AddSearchProvider');\n"
            + "    }\n"
            + "  } else {\n"
            + "    alert('no external');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "IsSearchProviderInstalled defined", "IsSearchProviderInstalled: 0" })
    @BuggyWebDriver({ IE, CHROME }) // fail with missing permission
    public void isSearchProviderInstalled() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.external) {\n"
            + "    if ('IsSearchProviderInstalled' in window.external) {\n"
            + "      alert('IsSearchProviderInstalled defined');\n"
            + "      var res = window.external.IsSearchProviderInstalled('http://htmlunit.sourceforge.net');\n"
            + "      alert('IsSearchProviderInstalled: ' + res);\n"
            + "    } else {\n"
            + "      alert('no IsSearchProviderInstalled');\n"
            + "    }\n"
            + "  } else {\n"
            + "    alert('no external');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
