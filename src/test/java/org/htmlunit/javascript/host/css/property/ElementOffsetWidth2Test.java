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

/**
 * Tests for offsetWidth calculations.
 *
 * @author Ronald Brill
 */
public class ElementOffsetWidth2Test extends WebDriverTestCase {

    /**
     * Basic case: absolutely positioned block with a single fixed-width child.
     * The outer div has no explicit width, so its offsetWidth should match
     * the child's width (300px).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"300", "300"})
    public void absolutelyPositionedShouldUseWidthsOfChildElements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 100px; left: 100px'>\n"
            + "    <div style='width:300px; height:300px'>abcd</div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * position:fixed should behave the same as position:absolute - shrink-wrap
     * to child content, not the document width.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "200"})
    public void fixedPositionedShouldUseWidthsOfChildElements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:fixed; top: 10px; left: 10px'>\n"
            + "    <div style='width:200px; height:100px'>text</div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Absolutely positioned block with two children side by side (inline-block).
     * The outer offsetWidth should reflect the total width of both children.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"504", "504"})
    @HtmlUnitNYI(CHROME = {"500", "500"},
            EDGE = {"500", "500"},
            FF = {"500", "500"},
            FF_ESR = {"500", "500"})
    public void absolutelyPositionedWithTwoInlineBlockChildren() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0; white-space:nowrap'>\n"
            + "    <div style='display:inline-block; width:200px; height:50px'></div>\n"
            + "    <div style='display:inline-block; width:300px; height:50px'></div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Deeply nested structure (3 levels): the outermost absolutely positioned
     * div has no width; the actual width is defined 3 levels in.
     * offsetWidth should propagate back up through all levels.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"150", "150"})
    @HtmlUnitNYI(CHROME = {"28", "28"},
            EDGE = {"28", "28"},
            FF = {"28", "28"},
            FF_ESR = {"28", "28"})
    public void absolutelyPositionedDeepNesting() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0'>\n"
            + "    <div>\n"
            + "      <div>\n"
            + "        <div style='width:150px; height:50px'>deep</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The wider of two stacked block children should determine the offsetWidth
     * of the absolutely positioned parent (max-content behaviour).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"400", "400"})
    @HtmlUnitNYI(CHROME = {"750", "750"},
            EDGE = {"750", "750"},
            FF = {"750", "750"},
            FF_ESR = {"750", "750"})
    public void absolutelyPositionedWidestChildWins() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0'>\n"
            + "    <div style='width:250px; height:30px'></div>\n"
            + "    <div style='width:400px; height:30px'></div>\n"
            + "    <div style='width:100px; height:30px'></div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * When the absolutely positioned element itself carries an explicit width,
     * that explicit width must win over the child-content width.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"500", "500"})
    public void absolutelyPositionedWithExplicitWidthUsesExplicitWidth() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0; width:500px'>\n"
            + "    <div style='width:200px; height:50px'>child</div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An absolutely positioned block with no children and no explicit width
     * should return 0 (empty shrink-wrap).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void absolutelyPositionedEmptyBlockIsZeroWidth() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0'></div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Nested absolutely positioned elements: child is itself absolutely
     * positioned inside the parent. The parent should shrink-wrap to the
     * child's declared width.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "350"})
    @HtmlUnitNYI(CHROME = {"350", "350"},
            EDGE = {"350", "350"},
            FF = {"350", "350"},
            FF_ESR = {"350", "350"})
    public void nestedAbsolutePositionedElements() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='outer' style='position:absolute; top: 0; left: 0'>\n"
            + "    <div id='inner' style='position:absolute; top: 0; left: 0; width:350px; height:80px'>\n"
            + "      content\n"
            + "    </div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let outer = document.getElementById('outer');\n"
            + "  let inner = document.getElementById('inner');\n"
            + "  log(outer.offsetWidth);\n"
            + "  log(inner.offsetWidth);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An absolutely positioned span (inline display) with a fixed-width child block.
     * Inline absolutely positioned elements should still shrink-wrap.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"220", "220"})
    @HtmlUnitNYI(CHROME = {"110", "110"},
            EDGE = {"110", "110"},
            FF = {"110", "110"},
            FF_ESR = {"110", "110"})
    public void absolutelyPositionedSpanWithBlockChild() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <span id='mySpan' style='position:absolute; top: 50px; left: 50px'>\n"
            + "    <div style='width:220px; height:40px'>inner block</div>\n"
            + "  </span>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('mySpan');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Absolutely positioned block with border and padding: offsetWidth
     * must include border + padding on top of the content/child width.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"319", "319.3333435058594"},
            FF = {"319", "319.33331298828125"},
            FF_ESR = {"319", "319.33331298828125"})
    @HtmlUnitNYI(CHROME = {"320", "320"},
            EDGE = {"320", "320"},
            FF = {"320", "320"},
            FF_ESR = {"320", "320"})
    public void absolutelyPositionedWithBorderAndPadding() throws Exception {
        // child width = 300px, padding = 5px each side, border = 5px each side
        // offsetWidth = 300 + 2*5 (padding) + 2*5 (border) = 320
        final String html = DOCTYPE_HTML
            + "<html><head><body>\n"
            + "  <div id='myDiv' style='position:absolute; top: 0; left: 0;"
            + "       padding:5px; border:5px solid black'>\n"
            + "    <div style='width:300px; height:100px'>child</div>\n"
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Four levels of nesting with no explicit widths except at the leaf.
     * Verifies that getContentWidth() recurses correctly all the way up.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"180", "180"})
    @HtmlUnitNYI(CHROME = {"28", "28"},
            EDGE = {"28", "28"},
            FF = {"28", "28"},
            FF_ESR = {"28", "28"})
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
            + "  </div>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  let e = document.getElementById('myDiv');\n"
            + "  log(e.offsetWidth);\n"
            + "  log(e.getBoundingClientRect().width);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
