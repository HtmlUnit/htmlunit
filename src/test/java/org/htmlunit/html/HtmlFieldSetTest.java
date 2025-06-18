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
package org.htmlunit.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlFieldSet}.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlFieldSetTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLFieldSetElement]", "[object HTMLFormElement]"})
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var fs = document.getElementById('fs');\n"
            + "    log(fs);\n"
            + "    log(fs.form);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <fieldset id='fs'>\n"
            + "      <legend>Legend</legend>\n"
            + "    </fieldset>\n"
            + "  </form>\n"
            + "</body></html>";
        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlElement element = toHtmlElement(driver.findElement(By.id("fs")));
            assertTrue(element instanceof HtmlFieldSet);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "center", "8", "foo"})
    public void align() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form>\n"
            + "  <fieldset id='fs1' align='left'>\n"
            + "    <legend>Legend</legend>\n"
            + "  </fieldset>\n"
            + "  <fieldset id='fs2' align='right'>\n"
            + "    <legend>Legend</legend>\n"
            + "  </fieldset>\n"
            + "  <fieldset id='fs3' align='3'>\n"
            + "    <legend>Legend</legend>\n"
            + "  </fieldset>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function set(fs, value) {\n"
            + "    try {\n"
            + "      fs.align = value;\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "  var fs1 = document.getElementById('fs1');\n"
            + "  var fs2 = document.getElementById('fs2');\n"
            + "  var fs3 = document.getElementById('fs3');\n"
            + "  log(fs1.align);\n"
            + "  log(fs2.align);\n"
            + "  log(fs3.align);\n"
            + "  set(fs1, 'center');\n"
            + "  set(fs2, '8');\n"
            + "  set(fs3, 'foo');\n"
            + "  log(fs1.align);\n"
            + "  log(fs2.align);\n"
            + "  log(fs3.align);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void disabled() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<form>\n"
            + "  <fieldset id='fs1' disabled>\n"
            + "    <input type'text' id='txt1' />\n"
            + "  </fieldset>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var fs1 = document.getElementById('fs1');\n"
            + "  var txt1 = document.getElementById('txt1');\n"
            + "  log(fs1.disabled);\n"
            + "  log(txt1.disabled);\n"
            + "</script>\n"
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
                + "      log(document.getElementById('f1').willValidate);\n"
                + "      log(document.getElementById('f2').willValidate);\n"
                + "      log(document.getElementById('f3').willValidate);\n"
                + "      log(document.getElementById('f4').willValidate);\n"
                + "      log(document.getElementById('f5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f1' />\n"
                + "    <fieldset id='f2' disabled />\n"
                + "    <fieldset id='f3' />\n"
                + "    <fieldset id='f4' readonly />\n"
                + "    <fieldset id='f5' style='display: none' />\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "false"})
    public void validationEmpty() throws Exception {
        validation("<fieldset id='e1' />\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-true-false-false-false-false-false-false-false-false-false",
             "false"})
    public void validationCustomValidity() throws Exception {
        validation("<fieldset id='e1' />\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-true-false-false-false-false-false-false-false-false-false",
             "false"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<fieldset id='e1' />\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "false"})
    public void validationResetCustomValidity() throws Exception {
        validation("<fieldset id='e1' />\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "false"})
    public void validationRequired() throws Exception {
        validation("<fieldset id='e1' required/>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "false"})
    public void validationDisabled() throws Exception {
        validation("<fieldset id='e1' disabled/>\n", "");
    }

    private void validation(final String htmlPart, final String jsPart) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function logValidityState(s) {\n"
                + "      log(s.badInput"
                        + "+ '-' + s.customError"
                        + "+ '-' + s.patternMismatch"
                        + "+ '-' + s.rangeOverflow"
                        + "+ '-' + s.rangeUnderflow"
                        + "+ '-' + s.stepMismatch"
                        + "+ '-' + s.tooLong"
                        + "+ '-' + s.tooShort"
                        + " + '-' + s.typeMismatch"
                        + " + '-' + s.valid"
                        + " + '-' + s.valueMissing);\n"
                + "    }\n"
                + "    function test() {\n"
                + "      var elem = document.getElementById('e1');\n"
                + jsPart
                + "      log(elem.checkValidity());\n"
                + "      logValidityState(elem.validity);\n"
                + "      log(elem.willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + htmlPart
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
