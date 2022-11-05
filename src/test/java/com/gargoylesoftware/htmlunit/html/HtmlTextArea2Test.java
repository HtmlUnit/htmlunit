/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link HtmlTextArea}.
 *
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlTextArea2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-", "-", "newValue-", "newValue-", "newValue-newDefault", "newValue-newDefault"})
    public void resetByClick() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    text.value = 'newValue';\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    text.defaultValue = 'newDefault';\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <textarea id='testId' value='initial'></textarea>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-", "-", "newValue-", "newValue-", "newValue-newDefault", "newValue-newDefault"})
    public void resetByJS() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    text.value = 'newValue';\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    text.defaultValue = 'newDefault';\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    log(text.value + '-' + text.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <textarea id='testId' value='initial'></textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-", "default-default", "some text-default", "some text-newdefault"},
            IE = {"-", "-default", "some text-default", "some text-newdefault"})
    public void defaultValue() throws Exception {
        final String html = "<!DOCTYPE HTML>\n"
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    text.defaultValue = 'default';\n"
            + "    log(text.value + '-' + text.defaultValue);\n"

            + "    text.value = 'some text';\n"
            + "    log(text.value + '-' + text.defaultValue);\n"
            + "    text.defaultValue = 'newdefault';\n"
            + "    log(text.value + '-' + text.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <textarea id='testId' value='initial'></textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(" foo \n bar\n test\n a <p>html snippet</p>\n")
    public void defaultValue2() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('textArea1').defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<textarea id='textArea1'> foo \n bar\r\n test\r a "
            + "<p>html snippet</p>\n"
            + "</textarea>\n"
            + "</form></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "7",
            IE = "textLength not available")
    public void textLength() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    if(text.textLength) {\n"
            + "      log(text.textLength);\n"
            + "    } else {\n"
            + "      log('textLength not available');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <textarea id='testId'>initial</textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void selection() throws Exception {
        final String html =
              "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(getSelection(document.getElementById('text1')).length);\n"
            + "  }\n"
            + "  function getSelection(element) {\n"
            + "    return element.value.substring(element.selectionStart, element.selectionEnd);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='text1'></textarea>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "3,11", "3,10"},
            IE = {"0,0", "0,0", "3,3", "3,10"})
    public void selection2_1() throws Exception {
        selection2(3, 10);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "11,11", "11,11"},
            IE = {"0,0", "0,0", "0,0", "0,11"})
    public void selection2_2() throws Exception {
        selection2(-3, 15);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "11,11", "10,11", "5,5"},
            IE = {"0,0", "0,0", "10,10", "5,5"})
    public void selection2_3() throws Exception {
        selection2(10, 5);
    }

    private void selection2(final int selectionStart, final int selectionEnd) throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myTextInput'>Bonjour</textarea>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var input = document.getElementById('myTextInput');\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.value = 'Hello there';\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.selectionStart = " + selectionStart + ";\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.selectionEnd = " + selectionEnd + ";\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "4,5", "10,10", "4,4", "1,1"},
            IE = {"0,0", "4,5", "0,0", "0,0", "0,0"})
    public void selectionOnUpdate() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myTextInput'>Hello</textarea>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var input = document.getElementById('myTextInput');\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "  input.selectionStart = 4;\n"
            + "  input.selectionEnd = 5;\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "  input.value = 'abcdefghif';\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "  input.value = 'abcd';\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"

            + "  input.selectionStart = 0;\n"
            + "  input.selectionEnd = 4;\n"

            + "  input.value = 'a';\n"
            + "  log(input.selectionStart + ',' + input.selectionEnd);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(" foo \n bar\n test\n a <p>html snippet</p>")
    public void getVisibleText() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea id='tester'> foo \n bar\r\n test\r a "
            + "<p>html snippet</p>\n"
            + "</textarea>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Hello World",
            IE = "Hello WorldHtmlUnit")
    @NotYetImplemented(IE)
    public void getVisibleTextValueChangedWithTyping() throws Exception {
        final String html
            = "<html>"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "<textarea id='tester'>Hello World</textarea>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textArea = driver.findElement(By.id("tester"));
        textArea.sendKeys("HtmlUnit");
        final String text = textArea.getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Hello World",
            IE = "HtmlUnit")
    @NotYetImplemented(IE)
    public void getVisibleTextValueChangedFromJs() throws Exception {
        final String html
            = "<html>"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    document.getElementById('tester').value = 'HtmlUnit';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<textarea id='tester'>Hello World</textarea>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextAndVisibility() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea id='tester' style='visibility:hidden'> foo \n bar "
            + "</textarea>\n"
            + "</form></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement textArea = driver.findElement(By.id("tester"));
        assertEquals(getExpectedAlerts()[0], textArea.getText());

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(" foo \n bar <p>html snippet</p>")
    public void parentAsText() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1'> foo \n bar "
            + "<p>html snippet</p>\n"
            + "</textarea>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement textArea = driver.findElement(By.id("form1"));
        assertEquals(getExpectedAlerts()[0], textArea.getText());
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"1", "a", "", "b", "<!--comment-->2", "c", "<!--comment-->", "d"})
    public void textUpdate() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myText'>1</textarea>\n"
            + "<textarea id='myTextEmpty'></textarea>\n"
            + "<textarea id='myTextComment'><!--comment-->2</textarea>\n"
            + "<textarea id='myTextCommentOnly'><!--comment--></textarea>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var text = document.getElementById('myText');\n"
            + "  log(text.value);\n"
            + "  text.value = 'a';\n"
            + "  log(text.value);\n"

            + "  text = document.getElementById('myTextEmpty');\n"
            + "  log(text.value);\n"
            + "  text.value = 'b';\n"
            + "  log(text.value);\n"

            + "  text = document.getElementById('myTextComment');\n"
            + "  log(text.value);\n"
            + "  text.value = 'c';\n"
            + "  log(text.value);\n"

            + "  text = document.getElementById('myTextCommentOnly');\n"
            + "  log(text.value);\n"
            + "  text.value = 'd';\n"
            + "  log(text.value);\n"

            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"", "xyz", "1", "a", "1"})
    public void textUpdateFromJSText() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myText'></textarea>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var text = document.getElementById('myText');\n"
            + "  log(text.value);\n"

            + "  var txt = document.createTextNode('xyz');\n"
            + "  text.appendChild(txt);\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"

            + "  text.value = 'a';\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "1", "a", "1"},
            IE = {"", "123", "1", "a", "1"})
    public void textUpdateFromJSSpan() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myText'></textarea>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var text = document.getElementById('myText');\n"
            + "  log(text.value);\n"

            + "  var span = document.createElement('span');\n"
            + "  span.innerHTML = '123';\n"
            + "  text.appendChild(span);\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"

            + "  text.value = 'a';\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "", "1", "xyz", "2", "a", "2"},
            IE = {"", "123", "1", "123xyz", "2", "a", "1"})
    public void textUpdateFromJSSpanAndText() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myText'></textarea>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var text = document.getElementById('myText');\n"
            + "  log(text.value);\n"

            + "  var span = document.createElement('span');\n"
            + "  span.innerHTML = '123';\n"
            + "  text.appendChild(span);\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"

            + "  var txt = document.createTextNode('xyz');\n"
            + "  text.appendChild(txt);\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"

            + "  text.value = 'a';\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @Alerts({"", "", "1", "a", "1"})
    public void textUpdateFromJSComment() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "<textarea id='myText'></textarea>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var text = document.getElementById('myText');\n"
            + "  log(text.value);\n"

            + "  var comment = document.createComment('comment');\n"
            + "  text.appendChild(comment);\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"

            + "  text.value = 'a';\n"
            + "  log(text.value);\n"
            + "  log(text.childNodes.length);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "2", "1", "2", "1", "1"},
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined"})
    public void labels() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    debug(document.getElementById('e1'));\n"
            + "    debug(document.getElementById('e2'));\n"
            + "    debug(document.getElementById('e3'));\n"
            + "    debug(document.getElementById('e4'));\n"
            + "    var labels = document.getElementById('e4').labels;\n"
            + "    document.body.removeChild(document.getElementById('l4'));\n"
            + "    debug(document.getElementById('e4'));\n"
            + "    log(labels ? labels.length : labels);\n"
            + "  }\n"
            + "  function debug(e) {\n"
            + "    log(e.labels ? e.labels.length : e.labels);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <textarea id='e1'>e 1</textarea><br>\n"
            + "  <label>something <label> click here <textarea id='e2'>e 2</textarea></label></label><br>\n"
            + "  <label for='e3'> and here</label>\n"
            + "  <textarea id='e3'>e 3</textarea><br>\n"
            + "  <label id='l4' for='e4'> what about</label>\n"
            + "  <label> this<textarea id='e4'>e 4</textarea></label><br>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "false", "true"},
            IE = {"true", "false", "true", "true", "true"})
    public void willValidate() throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('t1').willValidate);\n"
                + "      log(document.getElementById('t2').willValidate);\n"
                + "      log(document.getElementById('t3').willValidate);\n"
                + "      log(document.getElementById('t4').willValidate);\n"
                + "      log(document.getElementById('t5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <textarea id='t1'>t1</textarea>\n"
                + "    <textarea id='t2' disabled>t2</textarea>\n"
                + "    <textarea id='t3' hidden>t3</textarea>\n"
                + "    <textarea id='t4' readonly>t4</textarea>\n"
                + "    <textarea id='t5' style='display: none'>t5</textarea>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationEmpty() throws Exception {
        validation("<textarea id='e1'>t1</textarea>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationCustomValidity() throws Exception {
        validation("<textarea id='e1'>t1</textarea>\n", "elem.setCustomValidity('Invalid');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-true-false-false-false-false-false-false-false-false-false",
                       "true"},
            IE = {"false",
                  "undefined-true-false-false-false-false-false-undefined-false-false-false",
                  "true"})
    public void validationBlankCustomValidity() throws Exception {
        validation("<textarea id='e1'>t1</textarea>\n", "elem.setCustomValidity(' ');\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationResetCustomValidity() throws Exception {
        validation("<textarea id='e1'>t1</textarea>\n",
                "elem.setCustomValidity('Invalid');elem.setCustomValidity('');");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-false-false-false-false-false-false-false-false-false-true",
                       "true"},
            IE = {"false",
                  "undefined-false-false-false-false-false-false-undefined-false-false-true",
                  "true"})
    public void validationRequired() throws Exception {
        validation("<textarea id='e1' required></textarea>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationRequiredWithText() throws Exception {
        validation("<textarea id='e1' required>HtmlUnit</textarea>\n", "");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "false-false-false-false-false-false-false-false-false-true-false",
                       "true"},
            IE = {"true",
                  "undefined-false-false-false-false-false-false-undefined-false-true-false",
                  "true"})
    public void validationRequiredValueSet() throws Exception {
        validation("<textarea id='e1' required></textarea>\n", "elem.value='A';");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false",
                       "false-false-false-false-false-false-false-false-false-false-true",
                       "true"},
            IE = {"false",
                  "undefined-false-false-false-false-false-false-undefined-false-false-true",
                  "true"})
    public void validationRequiredValueClear() throws Exception {
        validation("<textarea id='e1' required>abc</textarea>\n", "elem.value='';");
    }

    private void validation(final String htmlPart, final String jsPart) throws Exception {
        final String html =
                "<html><head>\n"
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
