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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link External}.
 *
 * @author Peter Faller
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ExternalTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"external defined", "no AutoCompleteSaveForm"})
    public void autoCompleteSaveForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function fnSaveForm() {\n"
            + "  if (window.external) {\n"
            + "    log('external defined');\n"
            + "    if ('AutoCompleteSaveForm' in window.external) {\n"
            + "      log('AutoCompleteSaveForm defined');\n"
            + "      window.external.AutoCompleteSaveForm(oForm);\n"
            + "      oForm.AutoCompleteTest.value = '';\n"
            + "      oForm.AutoCompleteIgnore.value = '';\n"
            + "    } else {\n"
            + "      log('no AutoCompleteSaveForm');\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('no external');\n"
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
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("AddSearchProvider defined")
    public void addSearchProvider() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.external) {\n"
            + "    if ('AddSearchProvider' in window.external) {\n"
            + "      log('AddSearchProvider defined');\n"
            + "    } else {\n"
            + "      log('no AddSearchProvider');\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('no external');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"IsSearchProviderInstalled defined", "IsSearchProviderInstalled: undefined"})
    public void isSearchProviderInstalled() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (window.external) {\n"
            + "    if ('IsSearchProviderInstalled' in window.external) {\n"
            + "      log('IsSearchProviderInstalled defined');\n"
            + "      try {\n"
            + "        var res = window.external.IsSearchProviderInstalled('http://htmlunit.sourceforge.net');\n"
            + "        log('IsSearchProviderInstalled: ' + res);\n"
            + "      } catch(e) { logEx(e); }\n"
            + "    } else {\n"
            + "      log('no IsSearchProviderInstalled');\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('no external');\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "[object External]", "[object External]", "undefined", "[object External]",
                       "true", "function External() { [native code] }", "function External() { [native code] }",
                       "[object External]", "function () { [native code] }"},
            FF = {"true", "[object External]", "[object External]", "undefined", "[object External]",
                  "false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError"},
            FF_ESR = {"true", "[object External]", "[object External]", "undefined", "[object External]",
                      "false", "undefined", "ReferenceError", "ReferenceError", "ReferenceError"})
    public void windowScope() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html></body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('external' in window);\n"
            + "  log(window.external);\n"
            + "  try { log(external); } catch(e) { logEx(e); };\n"
            + "  try { log(external.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(external.__proto__); } catch(e) { logEx(e); };\n"

            + "  log('External' in window);\n"
            + "  log(window.External);\n"
            + "  try { log(External); } catch(e) { logEx(e); };\n"
            + "  try { log(External.prototype); } catch(e) { logEx(e); };\n"
            + "  try { log(External.__proto__); } catch(e) { logEx(e); };\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
