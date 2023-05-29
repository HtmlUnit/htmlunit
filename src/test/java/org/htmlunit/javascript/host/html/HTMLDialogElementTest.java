/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link HTMLDialogElement}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDialogElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "null", "true", "", "false", "null", "true", "",
                       "true", "", "true", "TrUE", "false", "null"},
            IE = "No")
    public void open() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dia = document.getElementById('tester');\n"
            + "        if (typeof HTMLDialogElement !== 'function') { log('No'); return; }\n"

            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.open = true;\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.open = false;\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.open = 'true';\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.open = 'faLSE';\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.setAttribute('open', 'TrUE');\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.removeAttribute('open');\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <dialog id='tester'>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "null", "false", "null", "true", "", "true", "blah", "false", "null"},
            IE = "No")
    public void openString() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dia = document.getElementById('tester');\n"
            + "        if (typeof HTMLDialogElement !== 'function') { log('No'); return; }\n"

            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.open = '';\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.open = 'abc';\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.setAttribute('open', 'blah');\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.removeAttribute('open');\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <dialog id='tester'>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "null", "true", "", "true", ""},
            IE = "No")
    public void show() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dia = document.getElementById('tester');\n"
            + "        if (typeof HTMLDialogElement !== 'function') { log('No'); return; }\n"

            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.show();\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.show();\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <dialog id='tester'>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "", "true", ""},
            IE = "No")
    public void showAlreadyOpend() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dia = document.getElementById('tester');\n"
            + "        if (typeof HTMLDialogElement !== 'function') { log('No'); return; }\n"

            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.show();\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <dialog id='tester' open>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "null", "true", "", "InvalidStateError"},
            IE = "No")
    public void showModal() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dia = document.getElementById('tester');\n"
            + "        if (typeof HTMLDialogElement !== 'function') { log('No'); return; }\n"

            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.showModal();\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        try {\n"
            + "          dia.showModal();\n"
            + "          log(dia.open);\n"
            + "          log(dia.getAttribute('open'));\n"
            + "        } catch(e) { log('InvalidStateError'); }"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <dialog id='tester'>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "", "InvalidStateError"},
            IE = "No")
    public void showModalAlreadyOpend() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dia = document.getElementById('tester');\n"
            + "        if (typeof HTMLDialogElement !== 'function') { log('No'); return; }\n"

            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        try {\n"
            + "          dia.showModal();\n"
            + "          log(dia.open);\n"
            + "          log(dia.getAttribute('open'));\n"
            + "        } catch(e) { log('InvalidStateError'); }"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <dialog id='tester' open>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "null", "true", "", "true", ""},
            IE = "No")
    public void showAfterShowModal() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var dia = document.getElementById('tester');\n"
            + "        if (typeof HTMLDialogElement !== 'function') { log('No'); return; }\n"

            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        dia.showModal();\n"
            + "        log(dia.open);\n"
            + "        log(dia.getAttribute('open'));\n"

            + "        try {\n"
            + "          dia.show();\n"
            + "          log(dia.open);\n"
            + "          log(dia.getAttribute('open'));\n"
            + "        } catch(e) { log('InvalidStateError'); }"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <dialog id='tester'>\n"
            + "      <p>HtmlUNit dialog</p>\n"
            + "    </dialog>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
