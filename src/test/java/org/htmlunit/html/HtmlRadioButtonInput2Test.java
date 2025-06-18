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
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HtmlRadioButtonInput}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Benoit Heinrich
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlRadioButtonInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild_docFragment() throws Exception {
        performTest(true, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild_docFragment() throws Exception {
        performTest(false, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore_docFragment() throws Exception {
        performTest(true, false, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore_docFragment() throws Exception {
        performTest(false, false, false, true, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(true, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(false, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(true, false, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(false, false, true, true, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(true, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(false, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(true, false, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(false, false, false, true, true);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, false, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, false, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild() throws Exception {
        performTest(true, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild() throws Exception {
        performTest(false, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore() throws Exception {
        performTest(true, false, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore() throws Exception {
        performTest(false, false, false, false, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild_fromHtml() throws Exception {
        performTest(true, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild_fromHtml() throws Exception {
        performTest(false, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore_fromHtml() throws Exception {
        performTest(true, false, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore_fromHtml() throws Exception {
        performTest(false, false, true, false, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild_cloneNode() throws Exception {
        performTest(true, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild_cloneNode() throws Exception {
        performTest(false, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore_cloneNode() throws Exception {
        performTest(true, false, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore_cloneNode() throws Exception {
        performTest(false, false, false, false, true);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(true, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(false, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true-false", "true-false", "true-false", "true-false", "true-false"})
    public void checked_insertBefore_fromHtml_cloneNode() throws Exception {
        performTest(true, false, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false-true", "false-true", "true-true", "true-false", "true-false"})
    public void notchecked_insertBefore_fromHtml_cloneNode() throws Exception {
        performTest(false, false, true, false, true);
    }

    /**
     * @param checked whether the created {@code input} is checked or not
     * @param appendChild use {@code .appendChild} or {@code .insertBefore}
     * @param fromHtml create the {@code input} by {@code .innerHTML} or by {@code document.createElement()}
     * @param useFragment is {@code appendix} is a new {@code DocumentFragment} or the {@code input}
     * @param cloneNode use {@code .cloneNode()}
     */
    private void performTest(final boolean checked,
            final boolean appendChild,
            final boolean fromHtml,
            final boolean useFragment,
            boolean cloneNode) throws Exception {
        String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var existing = document.getElementById('rad1');\n";
        if (fromHtml) {
            html += ""
                + "      var builder = document.createElement('div');\n"
                + "      builder.innerHTML = '<input type=\"radio\" id=\"rad2\" name=\"radar\"";
            if (checked) {
                html += " checked";
            }
            html += ">';\n"
                + "      var input = builder.firstChild;\n";
        }
        else {
            html += ""
                + "      var input = document.createElement('input');\n"
                + "      input.type = 'radio';\n"
                + "      input.id = 'rad2';\n"
                + "      input.name = 'radar';\n";
            if (checked) {
                html += "      input.checked = true;\n";
            }
        }

        if (cloneNode && !useFragment) {
            html += "      input = input.cloneNode(true);\n";
            cloneNode = false;
        }
        html += ""
            + "      log(input.checked);\n"

            + "      var parent = document.getElementById('myDiv');\n"
            + "      var after = document.getElementById('divAfter');\n";
        if (useFragment) {
            html += ""
                    + "      var appendix = document.createDocumentFragment();\n"
                    + "      appendix.appendChild(input);\n"
                    + "      log(input.checked + '-' + existing.checked);\n";
        }
        else {
            html += "      var appendix = input;\n";
        }
        if (appendChild) {
            if (cloneNode) {
                html += "      parent.appendChild(appendix.cloneNode(true));\n";
            }
            else {
                html += "      parent.appendChild(appendix);\n";
            }
        }
        else {
            if (cloneNode) {
                html += "      parent.insertBefore(appendix.cloneNode(true), after);\n";
            }
            else {
                html += "      parent.insertBefore(appendix, after);\n";
            }
        }
        html += ""
            + "      input = document.getElementById('rad2');\n"
            + "      log(input.checked + '-' + existing.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      log(input.checked + '-' + existing.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      log(input.checked + '-' + existing.checked);\n"
            + "      parent.appendChild(input);\n"
            + "      log(input.checked + '-' + existing.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      log(input.checked + '-' + existing.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'>\n"
            + "    <input type='radio' id='rad1' name='radar' checked>\n"
            + "  <div id='divAfter'></div></div></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true-true", "false-false", "true-true", "false-false", "false-false", "false-false"})
    public void defaultChecked() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      radio = document.getElementById('rad1');\n"
            + "      radio2 = document.getElementById('rad2');\n"
            + "      log(radio.checked + '-' + radio.defaultChecked);\n"
            + "      log(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = true;\n"
            + "      log(radio.checked + '-' + radio.defaultChecked);\n"
            + "      log(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = false;\n"
            + "      log(radio.checked + '-' + radio.defaultChecked);\n"
            + "      log(radio2.checked + '-' + radio2.defaultChecked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='radio' id='rad1' name='radar' checked>\n"
            + "    <input type='radio' id='rad2' name='radar'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false-false", "false-false", "true-true", "false-false", "false-false", "false-false"})
    public void defaultChecked_notchecked() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      radio = document.getElementById('rad1');\n"
            + "      radio2 = document.getElementById('rad2');\n"
            + "      log(radio.checked + '-' + radio.defaultChecked);\n"
            + "      log(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = true;\n"
            + "      log(radio.checked + '-' + radio.defaultChecked);\n"
            + "      log(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = false;\n"
            + "      log(radio.checked + '-' + radio.defaultChecked);\n"
            + "      log(radio2.checked + '-' + radio2.defaultChecked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='radio' id='rad1' name='radar'>\n"
            + "    <input type='radio' id='rad2' name='radar'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug #1033.
     * As of 26.02.10, reading responseXML with xhtml namespace
     * was causing ClassCastException for IE simulation when it contained a checked radio button.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({"send request", "response read"})
    public void checkedOnXmlResponse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('send request');\n"
            + "  var xhr = new XMLHttpRequest();\n"
            + "  xhr.open('GET', 'foo.xml', false);\n"
            + "  xhr.send('');\n"
            + "  var x = xhr.responseXML;\n" // this is what caused the exception
            + "  log('response read');\n"
            + "</script>\n"
            + "</body></html>";

        final String xml
            = "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
            + "<body>\n"
            + "<input type='radio' name='radio' checked='checked'/>\n"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(xml, MimeType.TEXT_XML);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "change"})
    public void onchangeFires() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange='log(\"foo\");log(event.type);'>Check me</input>\n"
            + "</form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio")).click();

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo", "change", "boo", "blur"})
    public void onchangeFires2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio1'"
                        + " onChange='log(\"foo\");log(event.type);'"
                        + " onBlur='log(\"boo\");log(event.type);'"
            + ">Check Me</input>\n"
            + "<input type='radio' name='radioGroup' id='radio2'>Or Check Me</input>\n"
            + "</form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1")).click();
        driver.findElement(By.id("radio2")).click();

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Second")
    public void setChecked() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("radio")).click();
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Second")
    public void setChecked2() throws Exception {
        final String firstHtml = DOCTYPE_HTML
            + "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "<input id='myInput' type='text'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("radio")).click();
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e)\n"
            + "      e.preventDefault();\n"
            + "    else\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('radio1').onclick = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "  <input type='radio' id='radio1' name='radio1' />\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement radio = driver.findElement(By.id("radio1"));
        radio.click();
        assertFalse(radio.isSelected());
    }

    /**
     * Verifies that a HtmlRadioButton is unchecked by default.
     * The onClick tests make this assumption.
     * @throws Exception if the test fails
     */
    @Test
    public void defaultState() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='radio' name='radio' id='radio'>Check me</input>\n"
            + "</form></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement radio = driver.findElement(By.id("radio"));
        assertFalse(radio.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"on-", "on-", "on-", "on-"})
    public void defaultValues() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('radio1');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.getElementById('radio2');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'radio';\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"radio\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='radio1'>\n"
            + "  <input type='radio' id='radio2' checked='true'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"on-", "on-", "on-", "on-"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('radio1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.getElementById('radio2');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'radio';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"radio\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='radio1'>\n"
            + "  <input type='radio' id='radio2' checked='true'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault"})
    public void resetByClick() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var radio = document.getElementById('testId');\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.value = 'newValue';\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.defaultValue = 'newDefault';\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='testId' name='radar' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault"})
    public void resetByJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var radio = document.getElementById('testId');\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.value = 'newValue';\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.defaultValue = 'newDefault';\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='testId' name='radar' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"initial-initial", "default-default", "newValue-newValue", "newDefault-newDefault"})
    public void defaultValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var radio = document.getElementById('testId');\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.defaultValue = 'default';\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.value = 'newValue';\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"
            + "    radio.defaultValue = 'newDefault';\n"
            + "    log(radio.value + '-' + radio.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='testId' name='radar' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Call to JS function click() should trigger the onchange handler but neither the onfocus handler
     * nor the mousedown/up handlers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("changed")
    public void clickShouldTriggerOnchange() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var elt = document.getElementById('it');\n"
                + "    elt.click();\n"
                + "    document.getElementById('next').focus();\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<form>\n"
                + "  <input type='radio' id='it' onchange='log(\"changed\")'"
                + "    onmousedown='log(\"down\")' onmouseup='log(\"up\")' onfocus='log(\"focused\")'>Check me\n"
                + "  <input type='text' id='next'>\n"
                + "</form>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test <code>input.checked</code> if the radio <code>&lt;input&gt;</code> do not have distinct 'value'.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false,false", "true,false", "false,true"})
    public void radioInputChecked() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var r1 = document.forms.myForm.myRadio[0];\n"
            + "  var r2 = document.forms.myForm.myRadio[1];\n"
            + "  log(r1.checked + ',' + r2.checked);\n"
            + "  r1.checked = true;\n"
            + "  log(r1.checked + ',' + r2.checked);\n"
            + "  r2.checked = true;\n"
            + "  log(r1.checked + ',' + r2.checked);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "null", "true", "", "true", "yes"})
    public void checkedAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var checkbox = document.getElementById('r1');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('r2');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('r3');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body'>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r1' name='myRadio'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r2' name='myRadio' checked>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r3' name='myRadio' checked='yes'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "  <button id='clickMe' onClick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("r1")).click();
        driver.findElement(By.id("r2")).click();
        driver.findElement(By.id("r3")).click();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "null", "true", "null", "false", "null", "true", "", "false", "",
             "true", "", "true", "yes", "false", "yes", "true", "yes"})
    public void checkedAttributeJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var checkbox = document.getElementById('r1');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = true;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('r2');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = true;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('r3');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = true;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r1' name='myRadio'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r2' name='myRadio' checked>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r3' name='myRadio' checked='yes'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "null", "false", "null", "true", "", "true", "",
             "true", "yes", "true", "yes"})
    public void defaultCheckedAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var checkbox = document.getElementById('r1');\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = true;\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('r2');\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('r3');\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r1' name='myRadio'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r2' name='myRadio' checked>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='r3' name='myRadio' checked='yes'>\n"
            + "  <input type='radio' name='myRadio'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setCheckedOutsideForm() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<input type='radio' id='radio1' name='myRadio'>\n"
            + "<input type='radio' id='radio2' name='myRadio'>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' id='radio3' name='myRadio'>\n"
            + "  <input type='radio' id='radio4' name='myRadio'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement radio1 = driver.findElement(By.id("radio1"));
        final WebElement radio2 = driver.findElement(By.id("radio2"));
        final WebElement radio3 = driver.findElement(By.id("radio3"));
        final WebElement radio4 = driver.findElement(By.id("radio4"));

        assertFalse(radio1.isSelected());
        assertFalse(radio2.isSelected());
        assertFalse(radio3.isSelected());
        assertFalse(radio4.isSelected());

        radio1.click();

        assertTrue(radio1.isSelected());
        assertFalse(radio2.isSelected());
        assertFalse(radio3.isSelected());
        assertFalse(radio4.isSelected());

        radio2.click();

        assertFalse(radio1.isSelected());
        assertTrue(radio2.isSelected());
        assertFalse(radio3.isSelected());
        assertFalse(radio4.isSelected());

        radio3.click();

        assertFalse(radio1.isSelected());
        assertTrue(radio2.isSelected());
        assertTrue(radio3.isSelected());
        assertFalse(radio4.isSelected());

        radio4.click();

        assertFalse(radio1.isSelected());
        assertTrue(radio2.isSelected());
        assertFalse(radio3.isSelected());
        assertTrue(radio4.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    log(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "false", "true"})
    public void willValidate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('o1').willValidate);\n"
                + "      log(document.getElementById('o2').willValidate);\n"
                + "      log(document.getElementById('o3').willValidate);\n"
                + "      log(document.getElementById('o4').willValidate);\n"
                + "      log(document.getElementById('o5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <input type='radio' id='o1'>\n"
                + "    <input type='radio' id='o2' disabled>\n"
                + "    <input type='radio' id='o3' hidden>\n"
                + "    <input type='radio' id='o4' readonly>\n"
                + "    <input type='radio' id='o5' style='display: none'>\n"
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
             "true"})
    public void validationEmpty() throws Exception {
        validation("<input type='radio' id='e1'>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='radio' id='e1'>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='radio' id='e1'>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='radio' id='e1'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequired() throws Exception {
        validation("<input type='radio' id='e1' required>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-false-false-false-false-false-false-false-false-false-true",
             "true"})
    public void validationRequiredWithName() throws Exception {
        validation("<input type='radio' id='e1' name='r1' required>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredChecked() throws Exception {
        validation("<input type='radio' id='e1' required checked>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredCheckedWithName() throws Exception {
        validation("<input type='radio' id='e1' name='r1' required checked>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredClicked() throws Exception {
        validation("<input type='radio' id='e1' required>\n", "elem.click();");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredClickedWithName() throws Exception {
        validation("<input type='radio' id='e1' name='r1' required>\n", "elem.click();");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredClickUncheck() throws Exception {
        validation("<input type='radio' id='e1' name='test' required checked>\n"
                + "<input type='radio' id='e2' name='test'>\n", "document.getElementById('e2').click();");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredClickUncheck2() throws Exception {
        validation("<input type='radio' id='e1' name='test' required>\n"
                + "<input type='radio' id='e2' name='test'>\n", "document.getElementById('e2').click();");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredOther() throws Exception {
        validation("<input type='radio' id='e1' name='test' required>\n"
                + "<input type='radio' id='e2' name='test' checked>\n", "");
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
