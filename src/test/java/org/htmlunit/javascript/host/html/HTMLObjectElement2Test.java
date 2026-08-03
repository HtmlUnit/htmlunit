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
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HTMLObjectElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLObjectElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLFormElement]", "null"})
    public void form() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <object id='o1'></object>\n"
            + "</form>\n"
            + "<object id='o2'></object>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.getElementById('o1').form);\n"
            + "  log(document.getElementById('o2').form);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * There was an error that this code throws an illegal state ex.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XMLDocument]")
    public void responseXML_htmlObject() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.xml', false);\n"
            + "    xhr.send('');\n"
            + "    try {\n"
            + "      log(xhr.responseXML);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String xml = "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
                    + "<object classid='CLSID:test'/>\n"
                    + "</html>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Method willValidate must always be false
     * for an &lt;object&gt;, regardless of hidden/style/disabled-ancestor
     * state.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false"})
    public void willValidateAlwaysFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i2').willValidate);\n"
                + "      log(document.getElementById('i3').willValidate);\n"
                + "      log(document.getElementById('i4').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <object id='i1'></object>"
                + "    <object id='i2' hidden></object>"
                + "    <object id='i3' style='display: none'></object>"
                + "    <fieldset disabled><object id='i4'></object></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The checkValidity() must always return true on an &lt;object&gt;, regardless
     * of hidden/style/disabled-ancestor state.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void checkValidityAlwaysTrue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').checkValidity());\n"
                + "      log(document.getElementById('i2').checkValidity());\n"
                + "      log(document.getElementById('i3').checkValidity());\n"
                + "      log(document.getElementById('i4').checkValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <object id='i1'></object>"
                + "    <object id='i2' hidden></object>"
                + "    <object id='i3' style='display: none'></object>"
                + "    <fieldset disabled><object id='i4'></object></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The reportValidity() must always return true on an &lt;object&gt;, mirroring
     * checkValidity().
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void reportValidityAlwaysTrue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').reportValidity());\n"
                + "      log(document.getElementById('i2').reportValidity());\n"
                + "      log(document.getElementById('i3').reportValidity());\n"
                + "      log(document.getElementById('i4').reportValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <object id='i1'></object>"
                + "    <object id='i2' hidden></object>"
                + "    <object id='i3' style='display: none'></object>"
                + "    <fieldset disabled><object id='i4'></object></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Since an &lt;object&gt; is never itself a candidate for
     * constraint validation, a custom validity message set on it must have NO
     * effect on checkValidity() -- must still report true, and
     * .validity.valid must still be true.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "true", ""})
    public void setCustomValidityDoesNotAffectCheckValidity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.willValidate);\n"
                + "      log(o.validity.valid);\n"
                + "      log(o.checkValidity());\n"
                + "      log(o.validationMessage);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <object id='o'></object>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The validationMessage must always be empty on an &lt;object&gt;, even with a
     * custom validity message set and even when additionally barred via an
     * ancestor disabled fieldset.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", ""})
    public void validationMessageAlwaysEmpty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var plain = document.getElementById('plain');\n"
                + "      var inFieldset = document.getElementById('inFieldset');\n"
                + "      plain.setCustomValidity('error1');\n"
                + "      inFieldset.setCustomValidity('error2');\n"
                + "      log(plain.validationMessage);\n"
                + "      log(inFieldset.validationMessage);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <object id='plain'></object>"
                + "    <fieldset disabled><object id='inFieldset'></object></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A object with a custom validity message set must
     * never fire 'invalid' via checkValidity().
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void objectCheckValidityNeverFiresInvalidEvenWithCustomValidity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var o = document.getElementById('o');\n"
                + "      o.addEventListener('invalid', function() { log('unexpected invalid fired'); });\n"
                + "      o.setCustomValidity('some error');\n"
                + "      log(o.checkValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <object id='o'></object>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The reportValidity() must also never focus the object.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void objectReportValidityNeverFocusesEvenWithCustomValidity() throws Exception {
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
                + "    <object id='o'></object>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
