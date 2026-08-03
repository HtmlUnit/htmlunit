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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLOutputElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HTMLOutputElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "2", "1", "2", "1", "1"})
    public void labels() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      debug(document.getElementById('e1'));\n"
            + "      debug(document.getElementById('e2'));\n"
            + "      debug(document.getElementById('e3'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      var labels = document.getElementById('e4').labels;\n"
            + "      document.body.removeChild(document.getElementById('l4'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      log(labels ? labels.length : labels);\n"
            + "    }\n"
            + "    function debug(e) {\n"
            + "      log(e.labels ? e.labels.length : e.labels);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <output id='e1'>e 1</output><br>\n"
            + "  <label>something <label> click here <output id='e2'>e 2</output></label></label><br>\n"
            + "  <label for='e3'> and here</label>\n"
            + "  <output id='e3'>e 3</output><br>\n"
            + "  <label id='l4' for='e4'> what about</label>\n"
            + "  <label> this<output id='e4'>e 4</output></label><br>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false"})
    public void willValidate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i2').willValidate);\n"
                + "      log(document.getElementById('i3').willValidate);\n"
                + "      log(document.getElementById('i4').willValidate);\n"
                + "      log(document.getElementById('i5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='i1'>button</output>"
                + "    <output id='i2' disabled></output>"
                + "    <output id='i3' hidden></output>"
                + "    <output id='i4' readonly></output>"
                + "    <output id='i5' style='display: none'></output>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true", "false", "true", ""})
    public void setCustomValidityOnPlainOutput() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      log(o.willValidate);\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.validity.customError);\n"
                + "      log(o.validity.valid);\n"
                + "      log(o.checkValidity());\n"
                + "      log(o.validationMessage);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='o'></output>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void checkValidityMirrorsWillValidateAcrossAllCases() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').checkValidity());\n"
                + "      log(document.getElementById('i2').checkValidity());\n"
                + "      log(document.getElementById('i3').checkValidity());\n"
                + "      log(document.getElementById('i4').checkValidity());\n"
                + "      log(document.getElementById('i5').checkValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='i1'>button</output>"
                + "    <output id='i2' disabled></output>"
                + "    <output id='i3' hidden></output>"
                + "    <output id='i4' readonly></output>"
                + "    <output id='i5' style='display: none'></output>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The reportValidity() coverage, entirely absent currently -- confirms it
     * returns the same boolean as checkValidity() for a plain output with a
     * custom validity message set.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void reportValidityMatchesCheckValidity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.checkValidity());\n"
                + "      log(o.reportValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='o'></output>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Clearing a previously-set custom validity message (empty string) must be
     * reversible -- checked via .validity.customError and .validity.valid
     * regardless of which way the plain-output ambiguity above resolves.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true"})
    public void clearCustomValidity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.validity.customError);\n"
                + "      o.setCustomValidity('');\n"
                + "      log(o.validity.customError);\n"
                + "      log(o.validity.valid);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='o'></output>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An output that IS a descendant of a disabled fieldset. Since
     * willValidate() already reports false uniformly for output regardless of
     * its OWN attributes, this checks whether fieldset-ancestor disabling is
     * even independently observable for output at all, or whether it's just
     * redundant with the element's own always-false state.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true"})
    public void outputInsideDisabledFieldset() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      log(o.willValidate);\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.checkValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset disabled><output id='o'></output></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void outputCheckValidityInvalidEventBehaviorOnPlainOutput() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      o.addEventListener('invalid', function() { log('invalid fired'); });\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.checkValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='o'></output>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void outputReportValidityFocusBehaviorOnPlainOutput() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.reportValidity());\n"
                + "      log(document.activeElement === o);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='o'></output>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
