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

/**
 * Unit tests for {@code offsetHeight} of an element.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ElementOffsetHeightTest extends WebDriverTestCase {

    private static final String VALUE_ = "e == null ? e : e.offsetHeight";

    private void test(final String tagName) throws Exception {
        String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var e = document.getElementById('outer');\n"
                + "  log(" + VALUE_ + ");\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<" + tagName + " id='outer'><" + tagName + "></" + tagName + "></" + tagName + ">\n"
                + "</body></html>";

        if ("basefont".equals(tagName) || "isindex".equals(tagName)) {
            html = DOCTYPE_HTML
                    + "<html><head>\n"
                    + "<" + tagName + " id='outer'><" + tagName + ">\n"
                    + "<script>\n"
                    + "function test() {\n"
                    + "  var e = document.getElementById('outer');\n"
                    + "  alert(" + VALUE_ + ");\n"
                    + "}\n"
                    + "</script>\n"
                    + "</head><body onload='test()'>\n"
                    + "</body></html>";
        }

        if ("title".equals(tagName)) {
            // title is a bit special, we have to provide at least
            // one closing tab otherwise title spans to the end of the file
            html = DOCTYPE_HTML
                    + "<html><head>\n"
                    + "<script>\n"
                    + "function test() {\n"
                    + "  var e = document.getElementById('outer');\n"
                    + "  alert(" + VALUE_ + ");\n"
                    + "}\n"
                    + "</script>\n"
                    + "<title id='outer'><title></title>\n"
                    + "</head><body onload='test()'>\n"
                    + "</body></html>";
        }

        if ("frame".equals(tagName)) {
            html = DOCTYPE_HTML
                    + "<html><head>\n"
                    + "<script>\n"
                    + LOG_TITLE_FUNCTION
                    + "function test() {\n"
                    + "  var e = document.getElementById('outer');\n"
                    + "  log(" + VALUE_ + ");\n"
                    + "}\n"
                    + "</script>\n"
                    + "</head>\n"
                    + "<frameset onload='test()'>\n"
                    + "<frame id='outer'><frame>\n"
                    + "</frameset></html>";
        }
        if ("script".equals(tagName)) {
            html = DOCTYPE_HTML
                    + "<html><head>\n"
                    + "<script>\n"
                    + LOG_TITLE_FUNCTION
                    + "function test() {\n"
                    + "  var e = document.getElementById('outer');\n"
                    + "  log(" + VALUE_ + ");\n"
                    + "}\n"
                    + "</script>\n"
                    + "</head><body onload='test()'>\n"
                    + "<script id='outer'>//<script>\n"
                    + "</script>\n"
                    + "</body></html>";
        }
        if ("frameset".equals(tagName)) {
            html = DOCTYPE_HTML
                    + "<html><head>\n"
                    + "<script>\n"
                    + LOG_TITLE_FUNCTION
                    + "function test() {\n"
                    + "  var e = document.getElementById('outer');\n"
                    + "  log(" + VALUE_ + ");\n"
                    + "}\n"
                    + "</script>\n"
                    + "</head>\n"
                    + "<frameset onload='test()' id='outer'>\n"
                    + "<frameset>\n"
                    + "</frameset></html>";
        }

        if ("basefont".equals(tagName)
                || "isindex".equals(tagName)) {
            loadPageWithAlerts2(html);
            return;
        }

        loadPageVerifyTitle2(html);
    }

    private static String testInput(final String type) {
        return DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var e = document.getElementById('outer');\n"
                + "  log(" + VALUE_ + ");\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<input type='" + type + "' id='outer'>\n"
                + "</body></html>";
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void a() throws Exception {
        test("a");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void base() throws Exception {
        test("base");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "21")
    @HtmlUnitNYI(FF_ESR = "0")
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF = "18",
            FF_ESR = "18")
    @HtmlUnitNYI(FF = "0",
            FF_ESR = "0")
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("6")
    @HtmlUnitNYI(CHROME = "20",
            EDGE = "20",
            FF = "20",
            FF_ESR = "20")
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("150")
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "15")
    @HtmlUnitNYI(FF_ESR = "0")
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlCommand}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void command() throws Exception {
        test("command");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "18",
            FF = "19",
            FF_ESR = "19")
    @HtmlUnitNYI(FF = "18",
            FF_ESR = "18")
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void div() throws Exception {
        test("div");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void em() throws Exception {
        test("em");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("39")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void font() throws Exception {
        test("font");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "621",
            FF = "675",
            FF_ESR = "677",
            EDGE = "630")
    @HtmlUnitNYI(CHROME = "0",
            EDGE = "0",
            FF = "0",
            FF_ESR = "0")
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "621",
            EDGE = "630",
            FF = "675",
            FF_ESR = "677")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void header() throws Exception {
        test("header");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    @HtmlUnitNYI(CHROME = "0",
            EDGE = "0",
            FF = "0",
            FF_ESR = "0")
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("8")
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("154")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void image() throws Exception {
        test("image");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void img() throws Exception {
        test("img");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "15")
    @HtmlUnitNYI(FF_ESR = "0")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void label() throws Exception {
        test("label");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "18",
            FF = "19",
            FF_ESR = "19")
    @HtmlUnitNYI(CHROME = "0",
            EDGE = "0",
            FF = "0",
            FF_ESR = "0")
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void main() throws Exception {
        test("main");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("18")
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("16")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoLayer}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18")
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "17",
            FF = "0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void object() throws Exception {
        test("object");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "20",
            FF = "18",
            FF_ESR = "18")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18")
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("20")
    @HtmlUnitNYI(CHROME = "0",
            EDGE = "0",
            FF = "0",
            FF_ESR = "0")
    public void option() throws Exception {
        test("option");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void output() throws Exception {
        test("output");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void p() throws Exception {
        test("p");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("30")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "16",
            FF = "18")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF_ESR = "18")
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF = "17",
            FF_ESR = "17")
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRb}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF = "17",
            FF_ESR = "17")
    public void rb() throws Exception {
        test("rb");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF = "9",
            FF_ESR = "9")
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlRtc}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF = "9",
            FF_ESR = "9")
    @HtmlUnitNYI(FF = "0",
            FF_ESR = "0")
    public void rtc() throws Exception {
        test("rtc");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlS}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "15")
    @HtmlUnitNYI(FF_ESR = "0")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "19",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "20",
            EDGE = "20",
            FF = "20",
            FF_ESR = "20")
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "15")
    @HtmlUnitNYI(FF_ESR = "0")
    public void small() throws Exception {
        test("small");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void source() throws Exception {
        test("source");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void spacer() throws Exception {
        test("spacer");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "15")
    @HtmlUnitNYI(FF_ESR = "0")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "15")
    @HtmlUnitNYI(FF_ESR = "0")
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSvg}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void svg() throws Exception {
        test("svg");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void table() throws Exception {
        test("table");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void col() throws Exception {
        test("col");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void td() throws Exception {
        test("td");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void th() throws Exception {
        test("th");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "36",
            FF = "38",
            FF_ESR = "38")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "15")
    @HtmlUnitNYI(FF_ESR = "0")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void title() throws Exception {
        // title is a bit special, we have to provide at least
        // one closing tab otherwise title spans to the end of the file
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var e = document.getElementById('outer');\n"
            + "  alert(" + VALUE_ + ");\n"
            + "}\n"
            + "</script>\n"
            + "<title id='outer'><title></title>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void u() throws Exception {
        test("u");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("150")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("15")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void inputButton() throws Exception {
        loadPageVerifyTitle2(testInput("button"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "13",
            FF = "14",
            FF_ESR = "14")
    @HtmlUnitNYI(FF_ESR = "10")
    public void inputCheckbox() throws Exception {
        loadPageVerifyTitle2(testInput("checkbox"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void inputFile() throws Exception {
        loadPageVerifyTitle2(testInput("file"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void inputHidden() throws Exception {
        loadPageVerifyTitle2(testInput("hidden"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void inputPassword() throws Exception {
        loadPageVerifyTitle2(testInput("password"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "13",
            FF = "14",
            FF_ESR = "14")
    @HtmlUnitNYI(FF_ESR = "10")
    public void inputRadio() throws Exception {
        loadPageVerifyTitle2(testInput("radio"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void inputReset() throws Exception {
        loadPageVerifyTitle2(testInput("reset"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void inputSelect() throws Exception {
        loadPageVerifyTitle2(testInput("select"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void inputSubmit() throws Exception {
        loadPageVerifyTitle2(testInput("submit"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "21",
            FF = "22",
            FF_ESR = "22")
    @HtmlUnitNYI(CHROME = "17",
            EDGE = "17",
            FF = "18",
            FF_ESR = "18")
    public void inputText() throws Exception {
        loadPageVerifyTitle2(testInput("text"));
    }

    /**
     * Test {@link org.htmlunit.html.HtmlData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test HtmlContent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(FF_ESR = "0")
    public void content() throws Exception {
        test("content");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            FF_ESR = "17")
    @HtmlUnitNYI(CHROME = "18",
            EDGE = "18",
            FF = "18",
            FF_ESR = "18")
    public void picture() throws Exception {
        test("picture");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlTemplate}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void template() throws Exception {
        test("template");
    }

    /**
     * Test {@link org.htmlunit.html.HtmlSlot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void slot() throws Exception {
        test("slot");
    }

}
