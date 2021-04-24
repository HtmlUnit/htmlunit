/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.svg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link SvgElement}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SvgElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SVGElement]",
            IE = "[object Element]")
    public void simpleScriptable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <invalid id='myId'/>\n"
            + "  </svg>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            if ("[object SVGElement]".equals(getExpectedAlerts()[0])) {
                assertTrue(SvgElement.class.getName().equals(page.getElementById("myId").getClass().getName()));
            }
            else {
                assertTrue(DomElement.class.getName().equals(page.getElementById("myId").getClass().getName()));
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void oninput() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "  <title>Test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var testNode = document.getElementById('myId');\n"
            + "      alert('oninput' in document);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <invalid id='myId'/>\n"
            + "  </svg>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "myLine"})
    public void querySelectorAll() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var testNode = document.getElementById('myId');\n"
            + "  var redTags = testNode.querySelectorAll('.red');\n"
            + "  alert(redTags.length);\n"
            + "  alert(redTags.item(0).id);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <g id='myId'>\n"
            + "      <line id='myLine' x1='0' y1='0' x2='2' y2='4' class='red' />\n"
            + "    </g>\n"
            + "  </svg>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGLineElement]")
    public void querySelector() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var testNode = document.getElementById('myId');\n"
            + "  alert(testNode.querySelector('.red'));\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <g id='myId'>\n"
            + "      <line id='myLine' x1='0' y1='0' x2='2' y2='4' class='red' />\n"
            + "    </g>\n"
            + "  </svg>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SVGRect]")
    public void getBBox() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var testNode = document.getElementById('myId');\n"
            + "  alert(testNode.getBBox());\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <g id='myId'>\n"
            + "      <line id='myLine' x1='0' y1='0' x2='2' y2='4' class='red' />\n"
            + "    </g>\n"
            + "  </svg>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
