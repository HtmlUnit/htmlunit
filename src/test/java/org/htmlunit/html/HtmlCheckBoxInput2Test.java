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
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HtmlCheckBoxInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
public class HtmlCheckBoxInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_appendChild_docFragment() throws Exception {
        performTest(true, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_appendChild_docFragment() throws Exception {
        performTest(false, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_insertBefore_docFragment() throws Exception {
        performTest(true, false, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_insertBefore_docFragment() throws Exception {
        performTest(false, false, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(true, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(false, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(true, false, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(false, false, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(true, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(false, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(true, false, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(false, false, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void checked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, false, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void notchecked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, false, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_appendChild() throws Exception {
        performTest(true, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_appendChild() throws Exception {
        performTest(false, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_insertBefore() throws Exception {
        performTest(true, false, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_insertBefore() throws Exception {
        performTest(false, false, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_appendChild_fromHtml() throws Exception {
        performTest(true, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_appendChild_fromHtml() throws Exception {
        performTest(false, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_insertBefore_fromHtml() throws Exception {
        performTest(true, false, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_insertBefore_fromHtml() throws Exception {
        performTest(false, false, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_appendChild_cloneNode() throws Exception {
        performTest(true, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_appendChild_cloneNode() throws Exception {
        performTest(false, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_insertBefore_cloneNode() throws Exception {
        performTest(true, false, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_insertBefore_cloneNode() throws Exception {
        performTest(false, false, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(true, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(false, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true", "true"})
    public void checked_insertBefore_fromHtml_cloneNode() throws Exception {
        performTest(true, false, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "true"})
    public void notchecked_insertBefore_fromHtml_cloneNode() throws Exception {
        performTest(false, false, true, false, true);
    }

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
            + "    function test() {\n";
        if (fromHtml) {
            html = html
                + "      var builder = document.createElement('div');\n"
                + "      builder.innerHTML = '<input type=\"checkbox\"";
            if (checked) {
                html = html + " checked";
            }
            html = html + ">';\n"
                + "      var input = builder.firstChild;\n";
        }
        else {
            html = html
                + "      var input = document.createElement('input');\n"
                + "      input.type = 'checkbox';\n";
            if (checked) {
                html = html + "      input.checked = true;\n";
            }
        }

        if (cloneNode && !useFragment) {
            html = html
                    + "      input=input.cloneNode(true);\n";
            cloneNode = false;
        }
        html = html
            + "      log(input.checked);\n"

            + "      var parent=document.getElementById('myDiv');\n"
            + "      var after=document.getElementById('divAfter');\n";
        if (useFragment) {
            html = html
                    + "      var appendix=document.createDocumentFragment();\n"
                    + "      appendix.appendChild(input);\n"
                    + "      log(input.checked);\n";
        }
        else {
            html = html
                    + "      var appendix=input;\n";
        }
        if (appendChild) {
            if (cloneNode) {
                html = html + "      parent.appendChild(appendix.cloneNode(true));\n";
            }
            else {
                html = html + "      parent.appendChild(appendix);\n";
            }
        }
        else {
            if (cloneNode) {
                html = html + "      parent.insertBefore(appendix.cloneNode(true), after);\n";
            }
            else {
                html = html + "      parent.insertBefore(appendix, after);\n";
            }
        }
        html = html
            + "      input = parent.getElementsByTagName('input')[0];\n"
            + "      log(input.checked);\n";
        if (!useFragment) {
            html = html
                + "      parent.removeChild(input);\n"
                + "      log(input.checked);\n"
                + "\n"
                + "      input.defaultChecked = true;\n"
                + "      log(input.checked);\n"
                + "      parent.appendChild(input);\n"
                + "      log(input.checked);\n"
                + "      parent.removeChild(input);\n"
                + "      log(input.checked);\n";
        }
        html = html
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'><div id='divAfter'></div></div></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true-true", "true-true", "false-false", "false-false", "true-true", "false-false"})
    public void defaultChecked() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      chkbox = document.getElementById('chkboxChecked');\n"
            + "      log(chkbox.checked + '-' + chkbox.defaultChecked);\n"
            + "      chkbox.defaultChecked = true;\n"
            + "      log(chkbox.checked + '-' + chkbox.defaultChecked);\n"
            + "      chkbox.defaultChecked = false;\n"
            + "      log(chkbox.checked + '-' + chkbox.defaultChecked);\n"

            + "      chkbox = document.getElementById('chkboxNotChecked');\n"
            + "      log(chkbox.checked + '-' + chkbox.defaultChecked);\n"
            + "      chkbox.defaultChecked = true;\n"
            + "      log(chkbox.checked + '-' + chkbox.defaultChecked);\n"
            + "      chkbox.defaultChecked = false;\n"
            + "      log(chkbox.checked + '-' + chkbox.defaultChecked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <input type='checkbox' id='chkboxChecked' checked>\n"
            + "    <input type='checkbox' id='chkboxNotChecked'>\n"
            + "  </form>\n"
            + "</body></html>";

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
            + "  <input type='checkbox' id='chkbox' onchange='log(\"foo\");log(event.type);'>\n"
            + "</form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("chkbox")).click();

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onchange change", "onblur blur"})
    public void onchangeFires2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='checkbox' id='chkbox'"
            + "  onChange='log(\"onchange \" + event.type);'"
            + "  onBlur='log(\"onblur \" + event.type);'"
            + ">\n"
            + "<input type='checkbox' id='chkbox2'>\n"
            + "</form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("chkbox")).click();
        driver.findElement(By.id("chkbox2")).click();

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
            + "<input id='myCheckbox' type='checkbox' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("myCheckbox")).click();
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
            + "<input id='myCheckbox' type='checkbox' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "<input id='myInput' type='text'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("myCheckbox")).click();
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
            + "    document.getElementById('checkbox1').onclick = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<input type='checkbox' id='checkbox1'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        checkbox.click();
        assertFalse(checkbox.isSelected());
    }

    /**
     * Verifies that a HtmlCheckBox is unchecked by default.
     * The onClick tests make this assumption.
     * @throws Exception if the test fails
     */
    @Test
    public void defaultState() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='checkbox' name='checkbox' id='checkbox'>Check me</input>\n"
            + "</form></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox"));
        assertFalse(checkbox.isSelected());
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
            + "    var input = document.getElementById('chkbox1');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.getElementById('chkbox2');\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'checkbox';\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"checkbox\">';\n"
            + "    input = builder.firstChild;\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='chkbox1'>\n"
            + "  <input type='checkbox' id='chkbox2' checked='true'>\n"
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
            + "    var input = document.getElementById('chkbox1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.getElementById('chkbox2');\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'checkbox';\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"checkbox\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    log(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='chkbox1'>\n"
            + "  <input type='checkbox' id='chkbox2' checked='true'>\n"
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
            + "    var checkbox = document.getElementById('testId');\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    checkbox.value = 'newValue';\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    checkbox.defaultValue = 'newDefault';\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='testId' name='radar' value='initial'>\n"
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
            + "    var checkbox = document.getElementById('testId');\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    checkbox.value = 'newValue';\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    checkbox.defaultValue = 'newDefault';\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='testId' name='radar' value='initial'>\n"
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
            + "    var checkbox = document.getElementById('testId');\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    checkbox.defaultValue = 'default';\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"

            + "    checkbox.value = 'newValue';\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"
            + "    checkbox.defaultValue = 'newDefault';\n"
            + "    log(checkbox.value + '-' + checkbox.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='testId' name='radar' value='initial'>\n"
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
                + "</head><body onload='test()'>\n"
                + "<form>\n"
                + "  <input type='checkbox' id='it' onchange='log(\"changed\")'"
                + "    onmousedown='log(\"down\")' onmouseup='log(\"up\")' onfocus='log(\"focused\")'>Check me\n"
                + "  <input type='text' id='next'>\n"
                + "</form>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "null", "false", "", "false", "yes"})
    public void checkedAttribute() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var checkbox = document.getElementById('c1');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('c2');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('c3');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='c1' name='radar' value='initial'>\n"
            + "  <input type='checkbox' id='c2' name='radar' value='initial' checked>\n"
            + "  <input type='checkbox' id='c3' name='radar' value='initial' checked='yes'>\n"
            + "</form>\n"
            + "  <button id='clickMe' onClick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("c1")).click();
        driver.findElement(By.id("c2")).click();
        driver.findElement(By.id("c3")).click();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "null", "true", "null", "false", "null", "true", "", "false", "", "true", "",
                "true", "yes", "false", "yes", "true", "yes"})
    public void checkedAttributeJS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var checkbox = document.getElementById('c1');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = true;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('c2');\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = true;\n"
            + "    log(checkbox.checked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('c3');\n"
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
            + "<form>\n"
            + "  <input type='checkbox' id='c1' name='radar' value='initial'>\n"
            + "  <input type='checkbox' id='c2' name='radar' value='initial' checked>\n"
            + "  <input type='checkbox' id='c3' name='radar' value='initial' checked='yes'>\n"
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
            + "  function test() {\n"
            + LOG_TITLE_FUNCTION
            + "    var checkbox = document.getElementById('c1');\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = true;\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('c2');\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox = document.getElementById('c3');\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"

            + "    checkbox.checked = false;\n"
            + "    log(checkbox.defaultChecked);\n"
            + "    log(checkbox.getAttribute('checked'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='c1' name='radar' value='initial'>\n"
            + "  <input type='checkbox' id='c2' name='radar' value='initial' checked>\n"
            + "  <input type='checkbox' id='c3' name='radar' value='initial' checked='yes'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
            + "  <input type='checkbox' id='tester'>\n"
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
                + "    <input type='checkbox' id='o1'>\n"
                + "    <input type='checkbox' id='o2' disabled>\n"
                + "    <input type='checkbox' id='o3' hidden>\n"
                + "    <input type='checkbox' id='o4' readonly>\n"
                + "    <input type='checkbox' id='o5' style='display: none'>\n"
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
        validation("<input type='checkbox' id='e1'>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationCustomValidity() throws Exception {
        validation("<input type='checkbox' id='e1'>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-true-false-false-false-false-false-false-false-false-false",
             "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<input type='checkbox' id='e1'>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<input type='checkbox' id='e1'>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-false-false-false-false-false-false-false-false-false-true",
             "true"})
    public void validationRequired() throws Exception {
        validation("<input type='checkbox' id='e1' required>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredChecked() throws Exception {
        validation("<input type='checkbox' id='e1' required checked>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true",
             "false-false-false-false-false-false-false-false-false-true-false",
             "true"})
    public void validationRequiredClicked() throws Exception {
        validation("<input type='checkbox' id='e1' required>\n", "elem.click();");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false",
             "false-false-false-false-false-false-false-false-false-false-true",
             "true"})
    public void validationRequiredClickUncheck() throws Exception {
        validation("<input type='checkbox' id='e1' required checked>\n", "elem.click();");
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
