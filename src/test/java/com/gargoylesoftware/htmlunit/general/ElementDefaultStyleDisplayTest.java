/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.AlertsStandards;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.annotations.StandardsMode;

/**
 * Tests the result of the default 'display' style of an element.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
@StandardsMode
public class ElementDefaultStyleDisplayTest extends WebDriverTestCase {

    private static String test(final String tagName) throws Exception {
        return "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var e = document.createElement('" + tagName + "');\n"
            + "      alert(window.getComputedStyle(e, null).display);\n"
            + "      document.body.appendChild(e);\n"
            + "      alert(window.getComputedStyle(e, null).display);\n"
            + "    } catch (e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void abbr() throws Exception {
        loadPageWithAlerts2(test("abbr"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void acronym() throws Exception {
        loadPageWithAlerts2(test("acronym"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void a() throws Exception {
        loadPageWithAlerts2(test("a"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void address() throws Exception {
        loadPageWithAlerts2(test("address"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline-block" },
            IE = { "inline", "inline" })
    public void applet() throws Exception {
        loadPageWithAlerts2(test("applet"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "none", "none" },
            IE = { "inline", "inline" })
    public void area() throws Exception {
        loadPageWithAlerts2(test("area"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void article() throws Exception {
        loadPageWithAlerts2(test("article"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void aside() throws Exception {
        loadPageWithAlerts2(test("aside"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void audio() throws Exception {
        loadPageWithAlerts2(test("audio"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void bgsound() throws Exception {
        loadPageWithAlerts2(test("bgsound"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "none", "none" },
            IE = { "inline", "inline" })
    public void base() throws Exception {
        loadPageWithAlerts2(test("base"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "none", "none" },
            IE = { "inline", "inline" })
    public void basefont() throws Exception {
        loadPageWithAlerts2(test("basefont"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void bdi() throws Exception {
        loadPageWithAlerts2(test("bdi"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void bdo() throws Exception {
        loadPageWithAlerts2(test("bdo"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void big() throws Exception {
        loadPageWithAlerts2(test("big"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void blink() throws Exception {
        loadPageWithAlerts2(test("blink"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void blockquote() throws Exception {
        loadPageWithAlerts2(test("blockquote"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void body() throws Exception {
        loadPageWithAlerts2(test("body"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void b() throws Exception {
        loadPageWithAlerts2(test("b"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void br() throws Exception {
        loadPageWithAlerts2(test("br"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline-block" },
            IE = { "inline-block", "inline-block" })
    public void button() throws Exception {
        loadPageWithAlerts2(test("button"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void canvas() throws Exception {
        loadPageWithAlerts2(test("canvas"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-caption" },
            FF = { "block", "table-caption" },
            IE = { "table-caption", "table-caption" })
    public void caption() throws Exception {
        loadPageWithAlerts2(test("caption"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void center() throws Exception {
        loadPageWithAlerts2(test("center"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void cite() throws Exception {
        loadPageWithAlerts2(test("cite"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void code() throws Exception {
        loadPageWithAlerts2(test("code"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void command() throws Exception {
        loadPageWithAlerts2(test("command"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void datalist() throws Exception {
        loadPageWithAlerts2(test("datalist"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void dfn() throws Exception {
        loadPageWithAlerts2(test("dfn"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "inline" },
            IE = { "block", "block" })
    @AlertsStandards(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void dd() throws Exception {
        loadPageWithAlerts2(test("dd"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void del() throws Exception {
        loadPageWithAlerts2(test("del"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void details() throws Exception {
        loadPageWithAlerts2(test("details"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void dialog() throws Exception {
        loadPageWithAlerts2(test("dialog"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void dir() throws Exception {
        loadPageWithAlerts2(test("dir"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void div() throws Exception {
        loadPageWithAlerts2(test("div"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void dl() throws Exception {
        loadPageWithAlerts2(test("dl"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void dt() throws Exception {
        loadPageWithAlerts2(test("dt"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void embed() throws Exception {
        loadPageWithAlerts2(test("embed"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void em() throws Exception {
        loadPageWithAlerts2(test("em"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void fieldset() throws Exception {
        loadPageWithAlerts2(test("fieldset"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void figcaption() throws Exception {
        loadPageWithAlerts2(test("figcaption"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void figure() throws Exception {
        loadPageWithAlerts2(test("figure"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void font() throws Exception {
        loadPageWithAlerts2(test("font"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void form() throws Exception {
        loadPageWithAlerts2(test("form"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void footer() throws Exception {
        loadPageWithAlerts2(test("footer"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "inline" },
            IE = { "block", "block" })
    public void frame() throws Exception {
        loadPageWithAlerts2(test("frame"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void frameset() throws Exception {
        loadPageWithAlerts2(test("frameset"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void head() throws Exception {
        loadPageWithAlerts2(test("head"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void header() throws Exception {
        loadPageWithAlerts2(test("header"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void h1() throws Exception {
        loadPageWithAlerts2(test("h1"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void h2() throws Exception {
        loadPageWithAlerts2(test("h2"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void h3() throws Exception {
        loadPageWithAlerts2(test("h3"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void h4() throws Exception {
        loadPageWithAlerts2(test("h4"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void h5() throws Exception {
        loadPageWithAlerts2(test("h5"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void h6() throws Exception {
        loadPageWithAlerts2(test("h6"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void hr() throws Exception {
        loadPageWithAlerts2(test("hr"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void html() throws Exception {
        loadPageWithAlerts2(test("html"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void iframe() throws Exception {
        loadPageWithAlerts2(test("iframe"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void q() throws Exception {
        loadPageWithAlerts2(test("q"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void img() throws Exception {
        loadPageWithAlerts2(test("img"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void image() throws Exception {
        loadPageWithAlerts2(test("image"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void ins() throws Exception {
        loadPageWithAlerts2(test("ins"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void isindex() throws Exception {
        loadPageWithAlerts2(test("isindex"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void i() throws Exception {
        loadPageWithAlerts2(test("i"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void kbd() throws Exception {
        loadPageWithAlerts2(test("kbd"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeygen}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void keygen() throws Exception {
        loadPageWithAlerts2(test("keygen"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void label() throws Exception {
        loadPageWithAlerts2(test("label"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void layer() throws Exception {
        loadPageWithAlerts2(test("layer"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "inline", "inline" })
    public void legend() throws Exception {
        loadPageWithAlerts2(test("legend"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void listing() throws Exception {
        loadPageWithAlerts2(test("listing"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "list-item" },
            FF = { "block", "list-item" },
            IE = { "list-item", "list-item" })
    public void li() throws Exception {
        loadPageWithAlerts2(test("li"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "inline", "inline" })
    public void link() throws Exception {
        loadPageWithAlerts2(test("link"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "inline", "inline" })
    public void main() throws Exception {
        loadPageWithAlerts2(test("main"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void map() throws Exception {
        loadPageWithAlerts2(test("map"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void mark() throws Exception {
        loadPageWithAlerts2(test("mark"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline-block" },
            IE = { "block", "block" })
    public void marquee() throws Exception {
        loadPageWithAlerts2(test("marquee"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void menu() throws Exception {
        loadPageWithAlerts2(test("menu"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void menuitem() throws Exception {
        loadPageWithAlerts2(test("menuitem"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void meta() throws Exception {
        loadPageWithAlerts2(test("meta"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline-block" },
            IE = { "inline", "inline" })
    public void meter() throws Exception {
        loadPageWithAlerts2(test("meter"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "block" },
            IE = { "inline", "inline" })
    public void multicol() throws Exception {
        loadPageWithAlerts2(test("multicol"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void nav() throws Exception {
        loadPageWithAlerts2(test("nav"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void nextid() throws Exception {
        loadPageWithAlerts2(test("nextid"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void nobr() throws Exception {
        loadPageWithAlerts2(test("nobr"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void noembed() throws Exception {
        loadPageWithAlerts2(test("noembed"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void noframes() throws Exception {
        loadPageWithAlerts2(test("noframes"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void nolayer() throws Exception {
        loadPageWithAlerts2(test("nolayer"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void noscript() throws Exception {
        loadPageWithAlerts2(test("noscript"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void object() throws Exception {
        loadPageWithAlerts2(test("object"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void ol() throws Exception {
        loadPageWithAlerts2(test("ol"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "inline", "inline" })
    public void optgroup() throws Exception {
        loadPageWithAlerts2(test("optgroup"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "inline", "inline" })
    public void option() throws Exception {
        loadPageWithAlerts2(test("option"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void output() throws Exception {
        loadPageWithAlerts2(test("output"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void p() throws Exception {
        loadPageWithAlerts2(test("p"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "inline", "inline" })
    public void param() throws Exception {
        loadPageWithAlerts2(test("param"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void plaintext() throws Exception {
        loadPageWithAlerts2(test("plaintext"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void pre() throws Exception {
        loadPageWithAlerts2(test("pre"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline-block" },
            IE = { "inline", "inline" })
    public void progress() throws Exception {
        loadPageWithAlerts2(test("progress"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "none", "none" },
            IE = { "inline", "inline" })
    public void rp() throws Exception {
        loadPageWithAlerts2(test("rp"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "ruby-text" },
            IE = { "ruby-text", "ruby-text" })
    public void rt() throws Exception {
        loadPageWithAlerts2(test("rt"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "ruby" },
            IE = { "ruby", "ruby" })
    public void ruby() throws Exception {
        loadPageWithAlerts2(test("ruby"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void s() throws Exception {
        loadPageWithAlerts2(test("s"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void samp() throws Exception {
        loadPageWithAlerts2(test("samp"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void script() throws Exception {
        loadPageWithAlerts2(test("script"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void section() throws Exception {
        loadPageWithAlerts2(test("section"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline-block" },
            IE = { "inline-block", "inline-block" })
    public void select() throws Exception {
        loadPageWithAlerts2(test("select"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void small() throws Exception {
        loadPageWithAlerts2(test("small"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void source() throws Exception {
        loadPageWithAlerts2(test("source"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void span() throws Exception {
        loadPageWithAlerts2(test("span"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void strike() throws Exception {
        loadPageWithAlerts2(test("strike"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void strong() throws Exception {
        loadPageWithAlerts2(test("strong"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void style() throws Exception {
        loadPageWithAlerts2(test("style"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void sub() throws Exception {
        loadPageWithAlerts2(test("sub"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void summary() throws Exception {
        loadPageWithAlerts2(test("summary"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void sup() throws Exception {
        loadPageWithAlerts2(test("sup"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table" },
            FF = { "table", "table" },
            IE = { "table", "table" })
    public void table() throws Exception {
        loadPageWithAlerts2(test("table"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-column" },
            FF = { "block", "table-column" },
            IE = { "table-column", "table-column" })
    public void col() throws Exception {
        loadPageWithAlerts2(test("col"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-column-group" },
            FF = { "block, table-column-group" },
            IE = { "table-column-group, table-column-group" })
    public void colgroup() throws Exception {
        loadPageWithAlerts2(test("colgroup"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-row-group" },
            FF = { "block", "table-row-group" },
            IE = { "table-row-group", "table-row-group" })
    public void tbody() throws Exception {
        loadPageWithAlerts2(test("tbody"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-cell" },
            FF = { "block", "table-cell" },
            IE = { "table-cell", "table-cell" })
    public void td() throws Exception {
        loadPageWithAlerts2(test("td"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-cell" },
            FF = { "block", "table-cell" },
            IE = { "table-cell", "table-cell" })
    public void th() throws Exception {
        loadPageWithAlerts2(test("th"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-row" },
            FF = { "block", "table-row" },
            IE = { "table-row", "table-row" })
    public void tr() throws Exception {
        loadPageWithAlerts2(test("tr"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline" },
            IE = { "inline-block", "inline-block" })
    public void textarea() throws Exception {
        loadPageWithAlerts2(test("textarea"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-footer-group" },
            FF = { "block", "table-footer-group" },
            IE = { "table-footer-group", "table-footer-group" })
    public void tfoot() throws Exception {
        loadPageWithAlerts2(test("tfoot"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "table-header-group" },
            FF = { "block", "table-header-group" },
            IE = { "table-header-group", "table-header-group" })
    public void thead() throws Exception {
        loadPageWithAlerts2(test("thead"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void tt() throws Exception {
        loadPageWithAlerts2(test("tt"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void time() throws Exception {
        loadPageWithAlerts2(test("time"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "none", "none" })
    public void title() throws Exception {
        loadPageWithAlerts2(test("title"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void track() throws Exception {
        loadPageWithAlerts2(test("track"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void u() throws Exception {
        loadPageWithAlerts2(test("u"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void ul() throws Exception {
        loadPageWithAlerts2(test("ul"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void var() throws Exception {
        loadPageWithAlerts2(test("var"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void video() throws Exception {
        loadPageWithAlerts2(test("video"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void wbr() throws Exception {
        loadPageWithAlerts2(test("wbr"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "block" },
            FF = { "block", "block" },
            IE = { "block", "block" })
    public void xmp() throws Exception {
        loadPageWithAlerts2(test("xmp"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline-block" },
            FF = { "block", "inline" },
            IE = { "inline-block", "inline-block" })
    public void input() throws Exception {
        loadPageWithAlerts2(test("input"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void data() throws Exception {
        loadPageWithAlerts2(test("data"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlContent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void content() throws Exception {
        loadPageWithAlerts2(test("content"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "inline" },
            FF = { "block", "inline" },
            IE = { "inline", "inline" })
    public void picture() throws Exception {
        loadPageWithAlerts2(test("picture"));
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "", "none" },
            FF = { "none", "none" },
            IE = { "inline", "inline" })
    public void template() throws Exception {
        loadPageWithAlerts2(test("template"));
    }
}
