/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
import org.htmlunit.annotations.StandardsMode;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.AlertsStandards;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the result of the default 'display' style of an element.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
@StandardsMode
public class ElementDefaultStyleDisplayTest extends WebDriverTestCase {

    private void test(final String tagName) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var e = document.createElement('" + tagName + "');\n"
            + "    log(window.getComputedStyle(e, null).display);\n"
            + "    document.body.appendChild(e);\n"
            + "    log(window.getComputedStyle(e, null).display);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void a() throws Exception {
        test("a");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            FF = {"", "none"},
            FF_ESR = {"", "none"},
            IE = {"inline", "inline"})
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"inline", "inline"})
    public void base() throws Exception {
        test("base");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"inline", "inline"})
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline-block"},
            IE = {"inline-block", "inline-block"})
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-caption"},
            IE = {"table-caption", "table-caption"})
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void command() throws Exception {
        test("command");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    @AlertsStandards(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"inline", "inline"})
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"inline", "inline"})
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void div() throws Exception {
        test("div");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void em() throws Exception {
        test("em");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void font() throws Exception {
        test("font");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            FF = {"", "inline"},
            FF_ESR = {"", "inline"},
            IE = {"block", "block"})
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void header() throws Exception {
        test("header");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void img() throws Exception {
        test("img");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void image() throws Exception {
        test("image");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void label() throws Exception {
        test("label");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"inline", "inline"})
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "list-item"},
            IE = {"list-item", "list-item"})
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"inline", "inline"})
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"inline", "inline"})
    public void main() throws Exception {
        test("main");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline-block"},
            IE = {"block", "block"})
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline-block"},
            IE = {"inline", "inline"})
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            FF = {"", "block"},
            FF_ESR = {"", "block"},
            IE = {"inline", "inline"})
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            CHROME = {"", "inline"},
            EDGE = {"", "inline"},
            IE = {"none", "none"})
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void object() throws Exception {
        test("object");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"inline", "inline"})
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"inline", "inline"})
    public void option() throws Exception {
        test("option");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void output() throws Exception {
        test("output");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void p() throws Exception {
        test("p");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"inline", "inline"})
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline-block"},
            IE = {"inline", "inline"})
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRb}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            FF = {"", "ruby-base"},
            FF_ESR = {"", "ruby-base"},
            IE = {"ruby-base", "ruby-base"})
    public void rb() throws Exception {
        test("rb");
    }

    /**
     * Test HtmlRbc.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void rbc() throws Exception {
        test("rbc");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"inline", "inline"})
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            FF = {"", "ruby-text"},
            FF_ESR = {"", "ruby-text"},
            IE = {"ruby-text", "ruby-text"})
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test HtmlRtc.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            FF = {"", "ruby-text-container"},
            FF_ESR = {"", "ruby-text-container"},
            IE = {"ruby-text-container", "ruby-text-container"})
    public void rtc() throws Exception {
        test("rtc");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            FF = {"", "ruby"},
            FF_ESR = {"", "ruby"},
            IE = {"ruby", "ruby"})
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline-block"},
            IE = {"inline-block", "inline-block"})
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void small() throws Exception {
        test("small");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void source() throws Exception {
        test("source");
    }

    /**
     * Test Spacer.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void spacer() throws Exception {
        test("spacer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"inline", "inline"})
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void svg() throws Exception {
        test("svg");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table"},
            IE = {"table", "table"})
    public void table() throws Exception {
        test("table");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-column"},
            IE = {"table-column", "table-column"})
    public void col() throws Exception {
        test("col");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-column-group"},
            IE = {"table-column-group", "table-column-group"})
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-row-group"},
            IE = {"table-row-group", "table-row-group"})
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-cell"},
            IE = {"table-cell", "table-cell"})
    public void td() throws Exception {
        test("td");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-cell"},
            IE = {"table-cell", "table-cell"})
    public void th() throws Exception {
        test("th");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-row"},
            IE = {"table-row", "table-row"})
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline-block"},
            IE = {"inline-block", "inline-block"})
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-footer-group"},
            IE = {"table-footer-group", "table-footer-group"})
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "table-header-group"},
            IE = {"table-header-group", "table-header-group"})
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"none", "none"})
    public void title() throws Exception {
        test("title");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void u() throws Exception {
        test("u");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "block"},
            IE = {"block", "block"})
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline-block"},
            IE = {"inline-block", "inline-block"})
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test HtmlContent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void content() throws Exception {
        test("content");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void picture() throws Exception {
        test("picture");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none"},
            IE = {"inline", "inline"})
    public void template() throws Exception {
        test("template");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSlot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "contents"},
            IE = {"inline", "inline"})
    public void slot() throws Exception {
        test("slot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline"},
            IE = {"inline", "inline"})
    public void arbitrary() throws Exception {
        test("abcdefg");
    }
}
