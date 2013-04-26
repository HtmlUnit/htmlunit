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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlCheckBoxInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlCheckBoxInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" },
            IE = { "true", "false", "false" })
    public void checked_appendChild_docFragment() throws Exception {
        performTest(true, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_appendChild_docFragment() throws Exception {
        performTest(false, true, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" },
            IE = { "true", "false", "false" })
    public void checked_insertBefore_docFragment() throws Exception {
        performTest(true, false, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_insertBefore_docFragment() throws Exception {
        performTest(false, false, false, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" })
    public void checked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(true, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_appendChild_fromHtml_docFragment() throws Exception {
        performTest(false, true, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" })
    public void checked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(true, false, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_insertBefore_fromHtml_docFragment() throws Exception {
        performTest(false, false, true, true, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" },
            IE = { "true", "false", "false" })
    public void checked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(true, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_appendChild_docFragment_cloneNode() throws Exception {
        performTest(false, true, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" },
            IE = { "true", "false", "false" })
    public void checked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(true, false, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_insertBefore_docFragment_cloneNode() throws Exception {
        performTest(false, false, false, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" })
    public void checked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_appendChild_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, true, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true" })
    public void checked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(true, false, true, true, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false" })
    public void notchecked_insertBefore_fromHtml_docFragment_cloneNode() throws Exception {
        performTest(false, false, true, true, true);
    }

    private void performTest(final boolean checked,
            final boolean appendChild,
            final boolean fromHtml,
            final boolean useFragment,
            boolean cloneNode) throws Exception {
        String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
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
            + "      alert(input.checked);\n"

            + "      var parent=document.getElementById('myDiv');\n"
            + "      var after=document.getElementById('divAfter');\n";
        if (useFragment) {
            html = html
                    + "      var appendix=document.createDocumentFragment();\n"
                    + "      appendix.appendChild(input);\n"
                    + "      alert(input.checked);\n";
        }
        else {
            html = html
                    + "      var appendix=input\n";
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
            + "      alert(input.checked);\n";
        if (!useFragment) {
            html = html
                + "      parent.removeChild(input);\n"
                + "      alert(input.checked);\n"
                + "\n"
                + "      input.defaultChecked = true;\n"
                + "      alert(input.checked);\n"
                + "      parent.appendChild(input);\n"
                + "      alert(input.checked);\n"
                + "      parent.removeChild(input);\n"
                + "      alert(input.checked);\n";
        }
        html = html
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'><div id='divAfter'></div></div></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "true", "false", "false", "false", "false", "false" },
            IE6 = { "true", "false", "false", "false", "true", "true" })
    public void checked_appendChild() throws Exception {
        performTest(true, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_appendChild() throws Exception {
        performTest(false, true, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "true", "false", "false", "false", "false", "false" },
            IE6 = { "true", "false", "false", "false", "true", "true" })
    public void checked_insertBefore() throws Exception {
        performTest(true, false, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_insertBefore() throws Exception {
        performTest(false, false, false, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" })
    public void checked_appendChild_fromHtml() throws Exception {
        performTest(true, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_appendChild_fromHtml() throws Exception {
        performTest(false, true, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" })
    public void checked_insertBefore_fromHtml() throws Exception {
        performTest(true, false, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_insertBefore_fromHtml() throws Exception {
        performTest(false, false, true, false, false);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "true", "false", "false", "false", "true", "true" })
    public void checked_appendChild_cloneNode() throws Exception {
        performTest(true, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_appendChild_cloneNode() throws Exception {
        performTest(false, true, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void checked_insertBefore_cloneNode() throws Exception {
        performTest(true, false, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_insertBefore_cloneNode() throws Exception {
        performTest(false, false, false, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "false", "true", "true", "true", "true", "true" })
    public void checked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(true, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_appendChild_fromHtml_cloneNode() throws Exception {
        performTest(false, true, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "false", "true", "true", "true", "true", "true" })
    public void checked_cloneNode_insertBefore_fromHtml() throws Exception {
        performTest(true, false, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_insertBefore_fromHtml_cloneNode() throws Exception {
        performTest(false, false, true, false, true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "foo,change,", IE = { })
    public void onchangeFires() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='checkbox' id='chkbox' onchange='debug(\"foo\");debug(event.type);'>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("chkbox")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "foo,change,", DEFAULT = "foo,change,boo,blur,")
    @NotYetImplemented(CHROME)
    public void onchangeFires2() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='checkbox' id='chkbox'"
            + " onChange='debug(\"foo\");debug(event.type);'"
            + " onBlur='debug(\"boo\");debug(event.type);'"
            + ">\n"
            + "<input type='checkbox' id='chkbox2'>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("chkbox")).click();
        driver.findElement(By.id("chkbox2")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "First", DEFAULT = "Second")
    public void setChecked() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input id='myCheckbox' type='checkbox' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("myCheckbox")).click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "First", "Second" }, DEFAULT = "Second")
    public void setChecked2() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input id='myCheckbox' type='checkbox' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "<input id='myInput' type='text'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("myCheckbox")).click();
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
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='checkbox' name='checkbox' id='checkbox'>Check me</input>\n"
            + "</form></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox"));
        assertFalse(checkbox.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "on", "on", "on", "on" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('chkbox').value);\n"

            + "    var input = document.createElement('input');\n"
            + "    input.type = 'checkbox';\n"
            + "    alert(input.value);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"checkbox\">';\n"
            + "    var input = builder.firstChild;\n"
            + "    alert(input.value);\n"

            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='checkbox' id='chkbox'>\n"
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
    @Alerts("changed")
    public void clickShouldTriggerOnchange() throws Exception {
        final String html = "<html><body>\n"
            + "<input type='checkbox' id='it' onchange='alert(\"changed\")'"
            + "onmousedown='alert(\"down\")' onmouseup='alert(\"up\")' onfocus='alert(\"focused\")'>Check me\n"
            + "<script>\n"
            + "var elt = document.getElementById('it');\n"
            + "elt.click();\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
