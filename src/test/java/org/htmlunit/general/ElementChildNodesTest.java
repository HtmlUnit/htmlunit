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
package org.htmlunit.general;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the result of {@code element.childNodes.length}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class ElementChildNodesTest extends WebDriverTestCase {

    private static String test(final String tagName) {
        return "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  for (var i = 1; i <= 6; i++) {\n"
                + "    var element = document.getElementById('p' + i);\n"
                + "    if (element) {\n"
                + "      log(element.childNodes.length);\n"
                + "    }\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<p id='p1'> <" + tagName + "></" + tagName + "> </p>\n"
                + "<p id='p2'><" + tagName + "></" + tagName + "> </p>\n"
                + "<p id='p3'> <" + tagName + "></" + tagName + "></p>\n"
                + "<p id='p4'> <" + tagName + ">var x</" + tagName + "> </p>\n"
                + "<p id='p5'><" + tagName + ">var x</" + tagName + "> </p>\n"
                + "<p id='p6'> <" + tagName + ">var x</" + tagName + "></p>\n"
                + "</body></html>";
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void abbr() throws Exception {
        loadPageVerifyTitle2(test("abbr"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void acronym() throws Exception {
        loadPageVerifyTitle2(test("acronym"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void a() throws Exception {
        loadPageVerifyTitle2(test("a"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void address() throws Exception {
        loadPageVerifyTitle2(test("address"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void applet() throws Exception {
        loadPageVerifyTitle2(test("applet"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void area() throws Exception {
        loadPageVerifyTitle2(test("area"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void article() throws Exception {
        loadPageVerifyTitle2(test("article"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void aside() throws Exception {
        loadPageVerifyTitle2(test("aside"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void audio() throws Exception {
        loadPageVerifyTitle2(test("audio"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void bgsound() throws Exception {
        loadPageVerifyTitle2(test("bgsound"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void base() throws Exception {
        loadPageVerifyTitle2(test("base"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void basefont() throws Exception {
        loadPageVerifyTitle2(test("basefont"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void bdi() throws Exception {
        loadPageVerifyTitle2(test("bdi"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void bdo() throws Exception {
        loadPageVerifyTitle2(test("bdo"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void big() throws Exception {
        loadPageVerifyTitle2(test("big"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void blink() throws Exception {
        loadPageVerifyTitle2(test("blink"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void blockquote() throws Exception {
        loadPageVerifyTitle2(test("blockquote"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void body() throws Exception {
        loadPageVerifyTitle2(test("body"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void b() throws Exception {
        loadPageVerifyTitle2(test("b"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "3", "3", "5", "4", "4"})
    public void br() throws Exception {
        loadPageVerifyTitle2(test("br"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void button() throws Exception {
        loadPageVerifyTitle2(test("button"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void canvas() throws Exception {
        loadPageVerifyTitle2(test("canvas"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void caption() throws Exception {
        loadPageVerifyTitle2(test("caption"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void center() throws Exception {
        loadPageVerifyTitle2(test("center"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void cite() throws Exception {
        loadPageVerifyTitle2(test("cite"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void code() throws Exception {
        loadPageVerifyTitle2(test("code"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "2", "2", "3", "2", "3"},
            FF = {"3", "2", "2", "3", "2", "2"},
            FF_ESR = {"3", "2", "2", "3", "2", "2"})
    public void command() throws Exception {
        loadPageVerifyTitle2(test("command"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void datalist() throws Exception {
        loadPageVerifyTitle2(test("datalist"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void dfn() throws Exception {
        loadPageVerifyTitle2(test("dfn"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void dd() throws Exception {
        loadPageVerifyTitle2(test("dd"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void del() throws Exception {
        loadPageVerifyTitle2(test("del"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void details() throws Exception {
        loadPageVerifyTitle2(test("details"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void dialog() throws Exception {
        loadPageVerifyTitle2(test("dialog"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void dir() throws Exception {
        loadPageVerifyTitle2(test("dir"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void div() throws Exception {
        loadPageVerifyTitle2(test("div"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void dl() throws Exception {
        loadPageVerifyTitle2(test("dl"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void dt() throws Exception {
        loadPageVerifyTitle2(test("dt"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void embed() throws Exception {
        loadPageVerifyTitle2(test("embed"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void em() throws Exception {
        loadPageVerifyTitle2(test("em"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void fieldset() throws Exception {
        loadPageVerifyTitle2(test("fieldset"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void figcaption() throws Exception {
        loadPageVerifyTitle2(test("figcaption"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void figure() throws Exception {
        loadPageVerifyTitle2(test("figure"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void font() throws Exception {
        loadPageVerifyTitle2(test("font"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void form() throws Exception {
        loadPageVerifyTitle2(test("form"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void footer() throws Exception {
        loadPageVerifyTitle2(test("footer"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void frame() throws Exception {
        loadPageVerifyTitle2(test("frame"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void frameset() throws Exception {
        loadPageVerifyTitle2(test("frameset"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void h1() throws Exception {
        loadPageVerifyTitle2(test("h1"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void h2() throws Exception {
        loadPageVerifyTitle2(test("h2"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void h3() throws Exception {
        loadPageVerifyTitle2(test("h3"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void h4() throws Exception {
        loadPageVerifyTitle2(test("h4"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void h5() throws Exception {
        loadPageVerifyTitle2(test("h5"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void h6() throws Exception {
        loadPageVerifyTitle2(test("h6"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void head() throws Exception {
        loadPageVerifyTitle2(test("head"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void header() throws Exception {
        loadPageVerifyTitle2(test("header"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void hr() throws Exception {
        loadPageVerifyTitle2(test("hr"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void html() throws Exception {
        loadPageVerifyTitle2(test("html"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void iframe() throws Exception {
        loadPageVerifyTitle2(test("iframe"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void image() throws Exception {
        loadPageVerifyTitle2(test("image"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void img() throws Exception {
        loadPageVerifyTitle2(test("img"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void ins() throws Exception {
        loadPageVerifyTitle2(test("ins"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void isindex() throws Exception {
        loadPageVerifyTitle2(test("isindex"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void i() throws Exception {
        loadPageVerifyTitle2(test("i"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void kbd() throws Exception {
        loadPageVerifyTitle2(test("kbd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void keygen() throws Exception {
        loadPageVerifyTitle2(test("keygen"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void label() throws Exception {
        loadPageVerifyTitle2(test("label"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void layer() throws Exception {
        loadPageVerifyTitle2(test("layer"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void legend() throws Exception {
        loadPageVerifyTitle2(test("legend"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void listing() throws Exception {
        loadPageVerifyTitle2(test("listing"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void li() throws Exception {
        loadPageVerifyTitle2(test("li"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void link() throws Exception {
        loadPageVerifyTitle2(test("link"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void main() throws Exception {
        loadPageVerifyTitle2(test("main"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void map() throws Exception {
        loadPageVerifyTitle2(test("map"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void marquee() throws Exception {
        loadPageVerifyTitle2(test("marquee"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void mark() throws Exception {
        loadPageVerifyTitle2(test("mark"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void menu() throws Exception {
        loadPageVerifyTitle2(test("menu"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void menuitem() throws Exception {
        loadPageVerifyTitle2(test("menuitem"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void meta() throws Exception {
        loadPageVerifyTitle2(test("meta"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void meter() throws Exception {
        loadPageVerifyTitle2(test("meter"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void multicol() throws Exception {
        loadPageVerifyTitle2(test("multicol"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void nav() throws Exception {
        loadPageVerifyTitle2(test("nav"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void nextid() throws Exception {
        loadPageVerifyTitle2(test("nextid"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void nobr() throws Exception {
        loadPageVerifyTitle2(test("nobr"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void noembed() throws Exception {
        loadPageVerifyTitle2(test("noembed"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void noframes() throws Exception {
        loadPageVerifyTitle2(test("noframes"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void nolayer() throws Exception {
        loadPageVerifyTitle2(test("nolayer"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void noscript() throws Exception {
        loadPageVerifyTitle2(test("noscript"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void object() throws Exception {
        loadPageVerifyTitle2(test("object"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void ol() throws Exception {
        loadPageVerifyTitle2(test("ol"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void optgroup() throws Exception {
        loadPageVerifyTitle2(test("optgroup"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void option() throws Exception {
        loadPageVerifyTitle2(test("option"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void output() throws Exception {
        loadPageVerifyTitle2(test("output"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void p() throws Exception {
        loadPageVerifyTitle2(test("p"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void param() throws Exception {
        loadPageVerifyTitle2(test("param"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void plaintext() throws Exception {
        loadPageVerifyTitle2(test("plaintext"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void pre() throws Exception {
        loadPageVerifyTitle2(test("pre"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void progress() throws Exception {
        loadPageVerifyTitle2(test("progress"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void q() throws Exception {
        loadPageVerifyTitle2(test("q"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void ruby() throws Exception {
        loadPageVerifyTitle2(test("ruby"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void rt() throws Exception {
        loadPageVerifyTitle2(test("rt"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRtc}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void rtc() throws Exception {
        loadPageVerifyTitle2(test("rtc"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRb}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void rb() throws Exception {
        loadPageVerifyTitle2(test("rb"));
    }

    /**
     * Test HtmlRbc.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void rbc() throws Exception {
        loadPageVerifyTitle2(test("rbc"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void rp() throws Exception {
        loadPageVerifyTitle2(test("rp"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void s() throws Exception {
        loadPageVerifyTitle2(test("s"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void samp() throws Exception {
        loadPageVerifyTitle2(test("samp"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void script() throws Exception {
        loadPageVerifyTitle2(test("script"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void section() throws Exception {
        loadPageVerifyTitle2(test("section"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void select() throws Exception {
        loadPageVerifyTitle2(test("select"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void small() throws Exception {
        loadPageVerifyTitle2(test("small"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void source() throws Exception {
        loadPageVerifyTitle2(test("source"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void spacer() throws Exception {
        loadPageVerifyTitle2(test("spacer"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void span() throws Exception {
        loadPageVerifyTitle2(test("span"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void strike() throws Exception {
        loadPageVerifyTitle2(test("strike"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void strong() throws Exception {
        loadPageVerifyTitle2(test("strong"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void style() throws Exception {
        loadPageVerifyTitle2(test("style"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void sub() throws Exception {
        loadPageVerifyTitle2(test("sub"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void summary() throws Exception {
        loadPageVerifyTitle2(test("summary"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void sup() throws Exception {
        loadPageVerifyTitle2(test("sup"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void svg() throws Exception {
        loadPageVerifyTitle2(test("svg"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "3", "2"})
    public void table() throws Exception {
        loadPageVerifyTitle2(test("table"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void col() throws Exception {
        loadPageVerifyTitle2(test("col"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void colgroup() throws Exception {
        loadPageVerifyTitle2(test("colgroup"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void tbody() throws Exception {
        loadPageVerifyTitle2(test("tbody"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void td() throws Exception {
        loadPageVerifyTitle2(test("td"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void th() throws Exception {
        loadPageVerifyTitle2(test("th"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void tr() throws Exception {
        loadPageVerifyTitle2(test("tr"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void track() throws Exception {
        loadPageVerifyTitle2(test("track"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void textarea() throws Exception {
        loadPageVerifyTitle2(test("textarea"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void tfoot() throws Exception {
        loadPageVerifyTitle2(test("tfoot"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "1", "1", "1", "1", "1"})
    public void thead() throws Exception {
        loadPageVerifyTitle2(test("thead"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void tt() throws Exception {
        loadPageVerifyTitle2(test("tt"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void time() throws Exception {
        loadPageVerifyTitle2(test("time"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void title() throws Exception {
        loadPageVerifyTitle2(test("title"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void u() throws Exception {
        loadPageVerifyTitle2(test("u"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void ul() throws Exception {
        loadPageVerifyTitle2(test("ul"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void var() throws Exception {
        loadPageVerifyTitle2(test("var"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void video() throws Exception {
        loadPageVerifyTitle2(test("video"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void wbr() throws Exception {
        loadPageVerifyTitle2(test("wbr"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "0", "1", "1", "0", "1"})
    public void xmp() throws Exception {
        loadPageVerifyTitle2(test("xmp"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "3"})
    public void input() throws Exception {
        loadPageVerifyTitle2(test("input"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void data() throws Exception {
        loadPageVerifyTitle2(test("data"));
    }

    /**
     * Test HtmlContent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void content() throws Exception {
        loadPageVerifyTitle2(test("content"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void picture() throws Exception {
        loadPageVerifyTitle2(test("picture"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void template() throws Exception {
        loadPageVerifyTitle2(test("template"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSlot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void slot() throws Exception {
        loadPageVerifyTitle2(test("slot"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "2", "3", "2", "2"})
    public void arbitrary() throws Exception {
        loadPageVerifyTitle2(test("abcdefg"));
    }
}
