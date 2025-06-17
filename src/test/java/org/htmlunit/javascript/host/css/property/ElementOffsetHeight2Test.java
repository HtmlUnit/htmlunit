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
package org.htmlunit.javascript.host.css.property;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for ComputedHeight.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ElementOffsetHeight2Test extends WebDriverTestCase {

    /**
     * Tests the relation between {@code fontSize} and {@code offsetHeight}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void offsetHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
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
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
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
            FF_ESR = "14, 30, 48, 60, 80, 108, 126, 161, 208, 224, 279, 297, 350, 418")
    // Windows
    //    @HtmlUnitNYI(CHROME = "12, 27, 44, 60, 80, 108, 126, 161, 208, 216, 270, 320, 374, 407",
    //            EDGE = "12, 27, 44, 60, 80, 108, 126, 161, 208, 216, 270, 320, 374, 407",
    //            FF = "14, 30, 48, 60, 80, 108, 126, 161, 208, 224, 279, 330, 385, 418",
    //            FF_ESR = "14, 30, 48, 60, 80, 108, 126, 161, 208, 224, 279, 330, 385, 418")
    // Jenkins
    @HtmlUnitNYI(CHROME = "18, 27, 44, 75, 96, 108, 147, 184, 234, 243, 300, 352, 374, 481",
            EDGE = "18, 27, 44, 75, 96, 108, 147, 184, 234, 243, 300, 352, 374, 481",
            FF = "21, 30, 48, 75, 96, 108, 147, 184, 234, 252, 310, 363, 385, 494",
            FF_ESR = "21, 30, 48, 75, 96, 108, 147, 184, 234, 252, 310, 363, 385, 494")
    public void offsetHeightLineBreaks() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
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
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true, true, true, true, true, true, true, true, true, true, true, true, true, true")
    public void offsetHeightLineBreaks2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
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
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
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
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"

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
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * Tests the relation between {@code fontSize} and {@code offsetHeight}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "10, 11, 16, 18, 21, 27, 37, 55",
            FF = "11, 12, 16, 18, 21, 28, 38, 56",
            FF_ESR = "11, 12, 16, 18, 21, 28, 38, 56")
    public void offsetHeightSmallLarge() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv'>a</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "var e = document.getElementById('myDiv');\n"
            + "var array = [];\n"

            + "e.style.fontSize = 'xx-small';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'x-small';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'small';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'medium';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'large';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'x-large';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'xx-large';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'xxx-large';\n"
            + "array.push(e.offsetHeight);\n"

            + "document.getElementById('myTextarea').value = array.join(', ');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * Tests the relation between {@code fontSize} and {@code offsetHeight}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("16, 22")
    public void offsetHeightSmallerLarger() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv'>a</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "var e = document.getElementById('myDiv');\n"
            + "var array = [];\n"

            + "e.style.fontSize = 'smaller';\n"
            + "array.push(e.offsetHeight);\n"

            + "e.style.fontSize = 'larger';\n"
            + "array.push(e.offsetHeight);\n"

            + "document.getElementById('myTextarea').value = array.join(', ');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * Tests the relation between {@code fontSize} and {@code offsetHeight}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "11, 49, 6",
            FF = "12, 49, 3",
            FF_ESR = "12, 49, 3")
    @HtmlUnitNYI(CHROME = "11, 49, 2",
            EDGE = "11, 49, 2")
    public void offsetHeightUnits() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDivPX' style='font-size: 10px;'>a</div>\n"
            + "  <div id='myDivEM' style='font-size: 2.7em;'>a</div>\n"
            + "  <div id='myDivP' style='font-size: 10%;'>a</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "var array = [];\n"

            + "var e = document.getElementById('myDivPX');\n"
            + "array.push(e.offsetHeight);\n"

            + "e = document.getElementById('myDivEM');\n"
            + "array.push(e.offsetHeight);\n"

            + "e = document.getElementById('myDivP');\n"
            + "array.push(e.offsetHeight);\n"

            + "document.getElementById('myTextarea').value = array.join(', ');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * Tests the relation between {@code fontSize} and {@code offsetHeight}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "12, 49, 6",
            FF = "13, 49, 4",
            FF_ESR = "13, 49, 4")
    @HtmlUnitNYI(CHROME = "12, 49, 4",
            EDGE = "12, 49, 4",
            FF = "13, 49, 5",
            FF_ESR = "13, 49, 5")
    public void offsetHeightUnitsWidth() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDivPX' style='font-size: 11.4px; width: 40px;'>a</div>\n"
            + "  <div id='myDivEM' style='font-size: 2.7em; width: 40px;'>a</div>\n"
            + "  <div id='myDivP' style='font-size: 17%; width: 40px;'>a</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "var array = [];\n"

            + "var e = document.getElementById('myDivPX');\n"
            + "array.push(e.offsetHeight);\n"

            + "e = document.getElementById('myDivEM');\n"
            + "array.push(e.offsetHeight);\n"

            + "e = document.getElementById('myDivP');\n"
            + "array.push(e.offsetHeight);\n"

            + "document.getElementById('myTextarea').value = array.join(', ');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String actual = driver.findElement(By.id("myTextarea")).getDomProperty("value");
        assertEquals(getExpectedAlerts()[0], actual);
    }

    /**
     * Test case for #124.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"300", "549", "945", "60", "273", "938", "35"})
    @HtmlUnitNYI(CHROME = {"300", "552", "9690", "60", "294", "6885", "43"},
            EDGE = {"300", "552", "9690", "60", "294", "6885", "43"},
            FF = {"300", "552", "9690", "60", "294", "6885", "43"},
            FF_ESR = {"300", "552", "9690", "60", "294", "6885", "43"})
    public void issue124() throws Exception {
        final String html = DOCTYPE_HTML
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
            + LOG_TITLE_FUNCTION
            + "    function getAttributeValue(element, attribute) {\n"
            + "      if (element) {\n"
            + "        return window.getComputedStyle(element)[attribute].split('px')[0];\n"
            + "      }\n"
            + "      return 0;\n"
            + "    }\n"

            + "    var titleSizer = document.querySelector('.title-sizer');\n"
            + "    var title = document.querySelector('.title');\n"
            + "    var titleHeight = titleSizer.offsetHeight;\n"
            + "    var titleHeightGoal = getAttributeValue(titleSizer, 'height');\n"
            + "    var titleFontSize = getAttributeValue(titleSizer, 'fontSize');\n"

            + "    log(titleHeightGoal);\n"

            + "    log(titleHeight);\n"
            + "    log(titleSizer.offsetWidth);\n"
            + "    log(titleFontSize);\n"

            + "    while (titleHeight > titleHeightGoal) {\n"
            + "      titleFontSize -= 1;\n"
            + "      title.style.fontSize = titleFontSize + 'px';\n"
            + "      titleHeight = titleSizer.offsetHeight;\n"
            + "    }\n"

            + "    log(titleHeight);\n"
            + "    log(titleSizer.offsetWidth);\n"
            + "    log(titleFontSize);\n"
            + "  </script>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
