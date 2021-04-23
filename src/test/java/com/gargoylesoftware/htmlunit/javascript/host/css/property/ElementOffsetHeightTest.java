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
package com.gargoylesoftware.htmlunit.javascript.host.css.property;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for ComputedHeight.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ElementOffsetHeightTest extends WebDriverTestCase {

    /**
     * Tests the relation between {@code fontSize} and {@code offsetHeight}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void offsetHeight() throws Exception {
        final String html
            = "<html><head><body>\n"
            + "  <div id='myDiv'>a</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "var e = document.getElementById('myDiv');\n"
            + "var array = [];\n"
            + "for (var i = 0; i <= 128; i++) {\n"
            + "  e.style.fontSize = i + 'px';\n"
            + "  array.push(e.offsetHeight);\n"
            + "}\n"
            + "document.getElementById('myTextarea').value = array.join(', ');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String expected = loadExpectation("ElementOffsetHeightTest.properties", ".txt");
        final String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        assertEquals(expected, actual);
    }

    /**
     * Try to do a line break if width is fixed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12, 27, 44, 60, 80, 108, 126, 161, 208, 216, 270, 288, 340, 407",
            FF = "14, 30, 48, 60, 80, 108, 126, 161, 208, 224, 279, 297, 350, 418",
            FF78 = "14, 30, 48, 60, 80, 108, 126, 161, 208, 224, 279, 297, 350, 418",
            IE = "14, 28, 46, 55, 81, 110, 124, 161, 202, 221, 269, 290, 345, 405")
    @NotYetImplemented // we will see other results on unix
    public void offsetHeightLineBreaks() throws Exception {
        final String html
            = "<html><head><body>\n"
            + "  <div id='myDiv' style='width: 400px'>"
            + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt "
            + "ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo "
            + "dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor "
            + "sit amet.</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var array = [];\n"
            + "  for (var i = 6; i <= 32; i+=2) {\n"
            + "    div.style.fontSize = i + 'px';\n"
            + "    array.push(div.offsetHeight);\n"
            + "  }\n"
            + "  document.getElementById('myTextarea').value = array.join(', ');\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true, true, true, true, true, true, true, true, true, true, true, true, true, true")
    public void offsetHeightLineBreaks2() throws Exception {
        final String html
            = "<html><head><body>\n"
            + "  <div id='myLine'>Lorem ipsum</div>\n"
            + "  <div id='myDiv' style='width: 400px'>"
            + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt "
            + "ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo "
            + "dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor "
            + "sit amet.</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var array = [];\n"
            + "  var lastHeight = 0;\n"

            + "  for (var i = 6; i <= 32; i+=2) {\n"
            + "    div.style.fontSize = i + 'px';\n"
            + "    var height = div.offsetHeight;"
            + "    array.push(height >= lastHeight);\n"
            + "    lastHeight = height;\n"
            + "  }\n"
            + "  document.getElementById('myTextarea').value = array.join(', ');\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * Try to do a line break if width is fixed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void offsetHeightManualLineBreaks() throws Exception {
        final String html
            = "<html><head><body>\n"

            + "  <div id='myDiv' style='width: 400px;font-size: 12px;'>"
            + "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
            + "sed diam nonumy eirmod tempor invidunt "
            + "ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo "
            + "dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor "
            + "sit amet.</div>\n"

            + "  <div id='myDivBr' style='width: 400px;font-size: 12px;'>"
            + "Lorem<br>ipsum<br>dolor<br>sit<br>amet, consetetur sadipscing elitr, "
            + "sed diam nonumy eirmod tempor invidunt "
            + "ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo "
            + "dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor "
            + "sit amet.</div>\n"

            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  var divBr = document.getElementById('myDivBr');\n"

            + "  document.getElementById('myTextarea').value = div.offsetHeight < divBr.offsetHeight;\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * Test case for #124.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"549", "273"},
            IE = {"552", "276"})
    @HtmlUnitNYI(CHROME = {"552", "294"},
            EDGE = {"552", "294"},
            FF = {"552", "294"},
            FF78 = {"552", "294"},
            IE = {"552", "294"})
    public void issue124() throws Exception {
        final String html
            = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "  <head>\n"
            + "    <style>\n"
            + "      .title-box {width: 960px; font-size: 60px;}\n"
            + "      .title-sizer {height: 300px;}\n"
            + "    </style>\n"
            + "  </head>\n"
            + "  <body>\n"
            + "    <div class='title-box'>\n"
            + "      <span class='title-sizer'>\n"
            + "        <span class='title'>\n"
            + "          8oz steak from Good and Gather. 8oz steak from Good and Gather. 8oz"
            + "          steak from Good and Gather. 8oz steak from Good and Gather. 8oz steak"
            + "          from Good and Gather. 8oz steak from Good and Gather. 8oz steak from"
            + "          Good and Gather. 8oz steak from Good and Gather."
            + "        </span>\n"
            + "      </span>\n"
            + "    </div>\n"
            + "  </body>\n"

            + "  <script>\n"
            + "    function getAttributeValue(element, attribute) {\n"
            + "      if (element) {\n"
            + "        return window.getComputedStyle(element)[attribute].split('px')[0];\n"
            + "      }\n"
            + "      return 0;\n"
            + "    }\n"

            + "    var titleSizer = document.querySelector('.title-sizer');\n"
            + "    var title = document.querySelector('.title');\n"
            + "    var titleHeight = titleSizer.offsetHeight;\n"
            + "    var titleFontSize = getAttributeValue(titleSizer, 'fontSize');\n"
            + "    var titleHeightGoal = getAttributeValue(titleSizer, 'height');\n"

            + "    alert(titleHeight);\r\n"

            + "    while (titleHeight > titleHeightGoal) {\n"
            + "      titleFontSize -= 1;\n"
            + "      title.style.fontSize = titleFontSize + 'px';\n"
            + "      titleHeight = titleSizer.offsetHeight;\n"
            + "    }\n"

            + "    alert(titleHeight);\n"
            + "  </script>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
