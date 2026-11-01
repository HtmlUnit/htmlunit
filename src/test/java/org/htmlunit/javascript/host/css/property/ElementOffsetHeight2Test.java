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

    /**
     * Basic case: absolutely positioned block with a single fixed-height child.
     * The outer div has no explicit height, so its offsetHeight should match
     * the child's height (300px).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"300", "300"})
    public void absolutelyPositionedShouldUseHeightsOfChildElements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 100px; left: 100px'>\n"
            + "    <div style='width:300px; height:300px'>abcd</div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Style position:fixed should behave the same as position:absolute - shrink-wrap
     * to the child content.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"100", "100"})
    public void fixedPositionedShouldUseHeightsOfChildElements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:fixed; top: 10px; left: 10px'>\n"
            + "    <div style='width:200px; height:100px'>text</div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Absolutely positioned block with two children side by side (inline-block).
     * The outer offsetHeight should reflect the height of the line (the higher child).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"84", "84"},
            FF = {"84", "83.5"},
            FF_ESR = {"84", "83.5"})
    @HtmlUnitNYI(CHROME = {"130", "130"},
            EDGE = {"130", "130"},
            FF = {"130", "130"},
            FF_ESR = {"130", "130"})
    public void absolutelyPositionedWithTwoInlineBlockChildren() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0; white-space:nowrap'>\n"
            + "    <div style='display:inline-block; width:200px; height:50px'></div>\n"
            + "    <div style='display:inline-block; width:300px; height:80px'></div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Deeply nested structure (3 levels): the outermost absolutely positioned
     * div has no height; the actual height is defined 3 levels in.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50", "50"})
    public void absolutelyPositionedDeepNesting() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0'>\n"
            + "    <div>\n"
            + "      <div>\n"
            + "        <div style='width:150px; height:50px'>deep</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The heights of stacked block children add up
     * (the counterpart of absolutelyPositionedWidestChildWins for the width).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"120", "120"})
    public void absolutelyPositionedStackedChildrenAddUp() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0'>\n"
            + "    <div style='width:250px; height:30px'></div>\n"
            + "    <div style='width:400px; height:40px'></div>\n"
            + "    <div style='width:100px; height:50px'></div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * When the absolutely positioned element itself carries an explicit height,
     * that explicit height must win over the child-content height.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"500", "500"})
    public void absolutelyPositionedWithExplicitHeightUsesExplicitHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0; height:500px'>\n"
            + "    <div style='width:200px; height:50px'>child</div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An absolutely positioned block with no children and no explicit height
     * should return 0.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void absolutelyPositionedEmptyBlockIsZeroHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0'></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Nested absolutely positioned elements: the child is itself absolutely
     * positioned inside the parent; the child does not contribute to the height of the parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "80"})
    @HtmlUnitNYI(CHROME = {"80", "80"},
            EDGE = {"80", "80"},
            FF = {"80", "80"},
            FF_ESR = {"80", "80"})
    public void nestedAbsolutePositionedElements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='outer' style='position:absolute; top: 0; left: 0'>\n"
            + "    <div id='inner' style='position:absolute; top: 0; left: 0; width:350px; height:80px'>\n"
            + "      content\n"
            + "    </div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.getElementById('outer').offsetHeight);\n"
            + "  log(document.getElementById('inner').offsetHeight);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An absolutely positioned span (inline display) with a fixed-height child block.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"40", "40"})
    public void absolutelyPositionedSpanWithBlockChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <span id='myDiv' style='position:absolute; top: 50px; left: 50px'>\n"
            + "    <div style='width:220px; height:40px'>inner block</div>\n"
            + "  </span>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Absolutely positioned block with border and padding: offsetHeight
     * must include border + padding on top of the content/child height.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"120", "120"})
    public void absolutelyPositionedWithBorderAndPadding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0; padding:5px; border:5px solid black'>\n"
            + "    <div style='width:300px; height:100px'>child</div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Four levels of nesting with no explicit heights except at the leaf.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"60", "60"})
    public void absolutelyPositionedFourLevelNesting() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0'>\n"
            + "    <div>\n"
            + "      <div>\n"
            + "        <div>\n"
            + "          <div style='width:180px; height:60px'>leaf</div>\n"
            + "        </div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Unlike the width, the height of a static block is not taken from the parent;
     * the block inside of a parent with height:500px has the height of its content.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"30", "30"})
    public void blockInsideParentWithExplicitHeightUsesContentHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div style='height:500px'>\n"
            + "    <div id='myDiv'>\n"
            + "      <div style='width:300px; height:30px'></div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetHeight);\n"
            + "  log(e.getBoundingClientRect().height);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The height of an absolute positioned element without height is defined by top and bottom
     * (the height of the containing block minus top and bottom; GWT layout panels are using this).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "150"})
    @HtmlUnitNYI(CHROME = {"0", "18"},
            EDGE = {"0", "18"},
            FF = {"0", "18"},
            FF_ESR = {"0", "18"})
    public void absolutelyPositionedHeightFromTopAndBottom() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div style='position: relative; height: 200px'>\n"
            + "    <div id='layer1' style='position: absolute; left: 0; top: 0; right: 0; bottom: 0'></div>\n"
            + "  </div>\n"
            + "  <div style='position: relative; height: 200px'>\n"
            + "    <div id='layer2' style='position: absolute; left: 0; top: 20px; right: 0; bottom: 30px'>abc</div>\n"
            + "  </div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.getElementById('layer1').offsetHeight);\n"
            + "  log(document.getElementById('layer2').offsetHeight);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
