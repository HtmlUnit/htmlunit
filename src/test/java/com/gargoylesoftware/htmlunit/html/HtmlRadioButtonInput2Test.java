/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlRadioButtonInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Benoit Heinrich
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlRadioButtonInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_appendChild_docFragment() throws Exception {
        performTest(true, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild_docFragment() throws Exception {
        performTest(false, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_insertBefore_docFragment() throws Exception {
        performTest(true, false, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore_docFragment() throws Exception {
        performTest(false, false, false, true, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(true, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(false, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(true, false, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(false, false, true, true, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void checked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(true, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(false, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void checked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(true, false, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(false, false, false, true, true);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, false, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, false, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" })
    public void checked_appendChild() throws Exception {
        performTest(true, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild() throws Exception {
        performTest(false, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" })
    public void checked_insertBefore() throws Exception {
        performTest(true, false, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore() throws Exception {
        performTest(false, false, false, false, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_appendChild_fromHtml() throws Exception {
        performTest(true, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild_fromHtml() throws Exception {
        performTest(false, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-true", "true-true", "true-true", "true-true", "true-true" })
    public void checked_insertBefore_fromHtml() throws Exception {
        performTest(true, false, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore_fromHtml() throws Exception {
        performTest(false, false, true, false, false);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void checked_appendChild_cloneNode() throws Exception {
        performTest(true, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild_cloneNode() throws Exception {
        performTest(false, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void checked_insertBefore_cloneNode() throws Exception {
        performTest(true, false, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore_cloneNode() throws Exception {
        performTest(false, false, false, false, true);
    }

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" })
    public void checked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(true, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(false, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" },
            IE = { "true", "true-false", "true-false", "true-false", "true-false", "true-false" })
    public void checked_insertBefore_fromHtml_cloneNode() throws Exception {
        performTest(true, false, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false-true", "false-true", "true-true", "true-false", "true-false" },
            IE = { "false", "false-true", "false-true", "true-true", "true-true", "true-true" })
    public void notchecked_insertBefore_fromHtml_cloneNode() throws Exception {
        performTest(false, false, true, false, true);
    }

    private void performTest(final boolean checked,
            final boolean appendChild,
            final boolean fromHtml,
            final boolean useFragment,
            boolean cloneNode) throws Exception {
        String html = "<!DOCTYPE HTML>\n<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var existing = document.getElementById('rad1');\n";
        if (fromHtml) {
            html = html
                + "      var builder = document.createElement('div');\n"
                + "      builder.innerHTML = '<input type=\"radio\" id=\"rad2\" name=\"radar\"";
            if (checked) {
                html = html + " checked";
            }
            html = html + ">';\n"
                + "      var input = builder.firstChild;\n";
        }
        else {
            html = html
                + "      var input = document.createElement('input');\n"
                + "      input.type = 'radio';\n"
                + "      input.id = 'rad2';\n"
                + "      input.name = 'radar';\n";
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
            + "      alert(input.checked);\n"

            + "      var parent=document.getElementById('myDiv');\n"
            + "      var after=document.getElementById('divAfter');\n";
        if (useFragment) {
            html = html
                    + "      var appendix=document.createDocumentFragment();\n"
                    + "      appendix.appendChild(input);\n"
                    + "      alert(input.checked + '-' + existing.checked);\n";
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
            + "      input = document.getElementById('rad2');\n"
            + "      alert(input.checked + '-' + existing.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked + '-' + existing.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      alert(input.checked + '-' + existing.checked);\n"
            + "      parent.appendChild(input);\n"
            + "      alert(input.checked + '-' + existing.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked + '-' + existing.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'>\n"
            + "    <input type='radio' id='rad1' name='radar' checked>\n"
            + "  <div id='divAfter'></div></div></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true-true", "false-false", "true-true", "false-false", "false-false", "false-false" })
    public void defaultChecked() throws Exception {
        final String html =
            "<!DOCTYPE HTML>\n<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      radio = document.getElementById('rad1');\n"
            + "      radio2 = document.getElementById('rad2');\n"
            + "      alert(radio.checked + '-' + radio.defaultChecked);\n"
            + "      alert(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = true;\n"
            + "      alert(radio.checked + '-' + radio.defaultChecked);\n"
            + "      alert(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = false;\n"
            + "      alert(radio.checked + '-' + radio.defaultChecked);\n"
            + "      alert(radio2.checked + '-' + radio2.defaultChecked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>"
            + "    <input type='radio' id='rad1' name='radar' checked>\n"
            + "    <input type='radio' id='rad2' name='radar'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false-false", "false-false", "true-true", "false-false", "false-false", "false-false" })
    public void defaultChecked_notchecked() throws Exception {
        final String html =
            "<!DOCTYPE HTML>\n<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      radio = document.getElementById('rad1');\n"
            + "      radio2 = document.getElementById('rad2');\n"
            + "      alert(radio.checked + '-' + radio.defaultChecked);\n"
            + "      alert(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = true;\n"
            + "      alert(radio.checked + '-' + radio.defaultChecked);\n"
            + "      alert(radio2.checked + '-' + radio2.defaultChecked);\n"

            + "      radio.defaultChecked = false;\n"
            + "      alert(radio.checked + '-' + radio.defaultChecked);\n"
            + "      alert(radio2.checked + '-' + radio2.defaultChecked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form>"
            + "    <input type='radio' id='rad1' name='radar'>\n"
            + "    <input type='radio' id='rad2' name='radar'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2956588.
     * As of HttmlUnit-2.8-SNAPSHOT on 26.02.10, reading responseXML with xhtml namespace
     * was causing ClassCastException for IE simulation when it contained a checked radio button.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({ "send request", "response read" })
    public void checkedOnXmlResponse() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + "  alert('send request');\n"
            + "  var xhr = (window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'));\n"
            + "  xhr.open('GET', 'foo.xml', false);\n"
            + "  xhr.send('');\n"
            + "  var x = xhr.responseXML;\n" // this is what caused the exception
            + "  alert('response read');\n"
            + "</script>\n"
            + "</body></html>";

        final String xml
            = "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
            + "<body>\n"
            + "<input type='radio' name='radio' checked='checked'/>"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "foo,change,", IE = "")
    public void onchangeFires() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange='debug(\"foo\");debug(event.type);'>Check me</input>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "foo,change,boo,blur,", CHROME = "foo,change,")
    public void onchangeFires2() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio1'"
            + " onChange='debug(\"foo\");debug(event.type);'"
            + " onBlur='debug(\"boo\");debug(event.type);'"
            + ">Check Me</input>\n"
            + "<input type='radio' name='radioGroup' id='radio2'>Or Check Me</input>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1")).click();
        driver.findElement(By.id("radio2")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "First", FF = "Second")
    public void setChecked() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("radio")).click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "First", "Second" }, FF = "Second")
    public void setChecked2() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "<input id='myInput' type='text'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("radio")).click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        if (getBrowserVersion().isIE()) {
            driver.findElement(By.id("myInput")).click();
            assertEquals(getExpectedAlerts()[1], driver.getTitle());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault() throws Exception {
        final String html =
              "<html><head><script>\n"
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
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='radio' name='radio' id='radio'>Check me</input>\n"
            + "</form></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement radio = driver.findElement(By.id("radio"));
        assertFalse(radio.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "on-", "on-", "on-", "on-", })
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('radio1');\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.getElementById('radio2');\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'radio';\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"radio\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='radio1'>\n"
            + "  <input type='radio' id='radio2' checked='true'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "on-", "on-", "on-", "on-" },
            IE = { "on-on", "on-on", "on-on", "on-on" })
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('radio1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.getElementById('radio2');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'radio';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"radio\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='radio1'>\n"
            + "  <input type='radio' id='radio2' checked='true'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" })
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var radio = document.getElementById('testId');\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.value = 'newValue';\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.defaultValue = 'newDefault';\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='testId' name='radar' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" })
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var radio = document.getElementById('testId');\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.value = 'newValue';\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.defaultValue = 'newDefault';\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='testId' name='radar' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "default-default", "newValue-newValue", "newDefault-newDefault" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var radio = document.getElementById('testId');\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.defaultValue = 'default';\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"

            + "    radio.value = 'newValue';\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"
            + "    radio.defaultValue = 'newDefault';\n"
            + "    alert(radio.value + '-' + radio.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='testId' name='radar' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Call to JS function click() should trigger the onchange handler but neither the onfocus handler
     * nor the mousedown/up handlers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "changed", IE = { })
    @NotYetImplemented(Browser.IE)
    public void clickShouldTriggerOnchange() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var elt = document.getElementById('it');\n"
                + "    elt.click();\n"
                + "    document.getElementById('next').focus();\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<form>\n"
                + "  <input type='radio' id='it' onchange='alert(\"changed\")'"
                + "    onmousedown='alert(\"down\")' onmouseup='alert(\"up\")' onfocus='alert(\"focused\")'>Check me\n"
                + "  <input type='text' id='next'>\n"
                + "</form>\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
