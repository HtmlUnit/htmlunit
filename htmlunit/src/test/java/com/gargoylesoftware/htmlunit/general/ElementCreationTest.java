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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests the result of <code>document.createElement()</code>.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ElementCreationTest extends WebDriverTestCase {

    private void test(final String tagName) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.createElement('" + tagName + "'));\n"
            + "    } catch(e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAbbreviated}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAcronym}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAddress}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLBlockElement]")
    public void address() throws Exception {
        test("address");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAnchor}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void a() throws Exception {
        test("a");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlApplet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLAppletElement]",
            CHROME = "[object HTMLUnknownElement]")
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArea}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("")
    public void area() throws Exception {
        test("area");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlArticle}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void article() throws Exception {
        test("article");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAside}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlAudio}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLAudioElement]")
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            IE = "[object HTMLBGSoundElement]")
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBase}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLBaseElement]")
    public void base() throws Exception {
        test("base");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBaseFont}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLSpanElement]",
            CHROME = "[object HTMLElement]",
            FF = "[object HTMLElement]",
            IE = "[object HTMLBaseFontElement]")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalIsolation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]")
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBig}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void big() throws Exception {
        test("big");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlink}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            IE = "[object HTMLPhraseElement]")
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBlockQuote}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLQuoteElement]",
            IE = "[object HTMLBlockElement]")
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBody}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLBodyElement]")
    public void body() throws Exception {
        test("body");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBold}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void b() throws Exception {
        test("b");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlBreak}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLBRElement]")
    public void br() throws Exception {
        test("br");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlButton}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLButtonElement]")
    public void button() throws Exception {
        test("button");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCanvas}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLCanvasElement]")
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCaption}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableCaptionElement]")
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCenter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLBlockElement]")
    public void center() throws Exception {
        test("center");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCitation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlCode}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void code() throws Exception {
        test("code");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDataList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLDataListElement]")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinition}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLDDElement]")
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLDListElement]")
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLDTElement]")
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDeletedText}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLModElement]")
    public void del() throws Exception {
        test("del");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDirectory}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLDirectoryElement]")
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDivision}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLDivElement]")
    public void div() throws Exception {
        test("div");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmbed}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLEmbedElement]")
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlEmphasis}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void em() throws Exception {
        test("em");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlExample}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            CHROME = "[object HTMLPreElement]",
            IE = "[object HTMLBlockElement]")
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFieldSet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLFieldSetElement]")
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigure}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFigureCaption}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFont}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLFontElement]")
    public void font() throws Exception {
        test("font");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFooter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlForm}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLFormElement]")
    public void form() throws Exception {
        test("form");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrame}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLFrameElement]")
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlFrameSet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLFrameSetElement]")
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHead}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadElement]")
    public void head() throws Exception {
        test("head");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeader}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void header() throws Exception {
        test("header");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading1}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadingElement]")
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading2}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadingElement]")
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading3}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadingElement]")
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading4}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadingElement]")
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading5}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadingElement]")
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHeading6}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHeadingElement]")
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHorizontalRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHRElement]")
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlHtml}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLHtmlElement]")
    public void html() throws Exception {
        test("html");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLImageElement]")
    public void img() throws Exception {
        test("img");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlImage}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLImageElement]",
            CHROME = "[object HTMLUnknownElement]",
            FF = "[object HTMLElement]")
    public void image() throws Exception {
        test("image");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineFrame}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLIFrameElement]")
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLQuoteElement]")
    public void q() throws Exception {
        test("q");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInput}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLInputElement]")
    public void input() throws Exception {
        test("input");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlInsertedText}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLModElement]")
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlIsIndex}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            IE = "[object HTMLIsIndexElement]")
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlItalic}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void i() throws Exception {
        test("i");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeyboard}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlKeygen}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLSpanElement]",
            CHROME = "[object HTMLKeygenElement]",
            IE = "[object HTMLBlockElement]")
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLabel}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLLabelElement]")
    public void label() throws Exception {
        test("label");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLegend}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLLegendElement]")
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlLink}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLLinkElement]")
    public void link() throws Exception {
        test("link");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListing}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            CHROME = "[object HTMLPreElement]",
            IE = "[object HTMLBlockElement]")
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlListItem}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLLIElement]")
    public void li() throws Exception {
        test("li");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMain}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLUnknownElement]")
    public void main() throws Exception {
        test("main");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLMapElement]")
    public void map() throws Exception {
        test("map");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMark}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMarquee}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMarqueeElement]",
            FF = "[object HTMLDivElement]")
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenu}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLMenuElement]")
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMenuItem}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            FF = "[object HTMLMenuItemElement]")
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeta}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLMetaElement]")
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMeter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMeterElement]",
            IE = "[object HTMLUnknownElement]")
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlMultiColumn}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNav}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNextId}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            IE = "[object HTMLNextIdElement]")
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoBreak}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoEmbed}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoFrames}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlNoScript}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlObject}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLObjectElement]")
    public void object() throws Exception {
        test("object");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOption}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLOptionElement]")
    public void option() throws Exception {
        test("option");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOptionGroup}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLOptGroupElement]")
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOrderedList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLOListElement]")
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlOutput}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLOutputElement]",
            IE = "[object HTMLUnknownElement]")
    public void output() throws Exception {
        test("output");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParagraph}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLParagraphElement]")
    public void p() throws Exception {
        test("p");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlParameter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLParamElement]")
    public void param() throws Exception {
        test("param");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPlainText}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLBlockElement]")
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPreformattedText}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLPreElement]")
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlProgress}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLProgressElement]")
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRp}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]",
            FF = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRt}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]",
            FF = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlRuby}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]",
            FF = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlS}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void s() throws Exception {
        test("s");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSample}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlScript}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLScriptElement]")
    public void script() throws Exception {
        test("script");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void section() throws Exception {
        test("section");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSelect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLSelectElement]")
    public void select() throws Exception {
        test("select");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSmall}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void small() throws Exception {
        test("small");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSource}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLSourceElement]")
    public void source() throws Exception {
        test("source");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSpan}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLSpanElement]")
    public void span() throws Exception {
        test("span");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrike}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStrong}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlStyle}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLStyleElement]")
    public void style() throws Exception {
        test("style");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSubscript}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSummary}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]")
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlSuperscript}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTable}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableElement]")
    public void table() throws Exception {
        test("table");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableBody}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableSectionElement]")
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumn}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableColElement]")
    public void col() throws Exception {
        test("col");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableColElement]")
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableDataCell}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableCellElement]",
            IE = "[object HTMLTableDataCellElement]")
    public void td() throws Exception {
        test("td");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableFooter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableSectionElement]")
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeader}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableSectionElement]")
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableHeaderCell}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableCellElement]",
            IE = "[object HTMLTableHeaderCellElement]")
    public void th() throws Exception {
        test("th");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTableRow}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTableRowElement]")
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTrack}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTrackElement]")
    public void track() throws Exception {
        test("track");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTeletype}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTextArea}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTextAreaElement]")
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTime}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            FF = "[object HTMLTimeElement]")
    public void time() throws Exception {
        test("time");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlTitle}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLTitleElement]")
    public void title() throws Exception {
        // there seems to be a bug in ie8
        // document.createElement('title') creates a text element
        // instead of a title. But if you use the title html tag
        // you end with a title element.
        test("title");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnderlined}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void u() throws Exception {
        test("u");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlUnorderedList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUListElement]")
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVariable}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            IE = "[object HTMLPhraseElement]")
    public void var() throws Exception {
        test("var");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlVideo}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLVideoElement]")
    public void video() throws Exception {
        test("video");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlWordBreak}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLElement]")
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void attribute() throws Exception {
        test("attribute");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void clientInformation() throws Exception {
        test("clientInformation");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void clipboardData() throws Exception {
        test("clipboardData");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]")
    public void command() throws Exception {
        test("command");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void comment() throws Exception {
        test("comment");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void currentStyle() throws Exception {
        test("currentStyle");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void custom() throws Exception {
        test("custom");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void datagrid() throws Exception {
        test("datagrid");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void datatemplate() throws Exception {
        test("datatemplate");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void dataTransfer() throws Exception {
        test("dataTransfer");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void defaults() throws Exception {
        test("defaults");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDetails}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLDetailsElement]")
    public void details() throws Exception {
        test("details");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlDialog}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLDialogElement]")
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void document() throws Exception {
        test("document");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void DocumentCompatibleInfo() throws Exception {
        test("DocumentCompatibleInfo");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void event() throws Exception {
        test("event");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]",
            FF = "[object HTMLElement]")
    public void event_source() throws Exception {
        test("event-source");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void external() throws Exception {
        test("external");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void history() throws Exception {
        test("history");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void hn() throws Exception {
        test("hn");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void ilayer() throws Exception {
        test("ilayer");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void implementation() throws Exception {
        test("implementation");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void IMPORT() throws Exception {
        test("IMPORT");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]")
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void location() throws Exception {
        test("location");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void namespace() throws Exception {
        test("namespace");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void navigator() throws Exception {
        test("navigator");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void nest() throws Exception {
        test("nest");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]")
    public void noLayer() throws Exception {
        test("nolayer");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void page() throws Exception {
        test("page");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void popup() throws Exception {
        test("popup");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]",
            FF = "[object HTMLElement]")
    public void rb() throws Exception {
        test("rb");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void rbc() throws Exception {
        test("rbc");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLElement]",
            FF = "[object HTMLElement]")
    public void rtc() throws Exception {
        test("rtc");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void rule() throws Exception {
        test("rule");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void runtimeStyle() throws Exception {
        test("runtimeStyle");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void screen() throws Exception {
        test("screen");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void selection() throws Exception {
        test("selection");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void spacer() throws Exception {
        test("spacer");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void Storage() throws Exception {
        test("Storage");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void styleSheet() throws Exception {
        test("styleSheet");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void TextNode() throws Exception {
        test("TextNode");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void TextRange() throws Exception {
        test("TextRange");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void TextRectangle() throws Exception {
        test("TextRectangle");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void userProfile() throws Exception {
        test("userProfile");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void window() throws Exception {
        test("window");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void XDomainRequest() throws Exception {
        test("XDomainRequest");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void xml() throws Exception {
        test("xml");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object HTMLUnknownElement]")
    public void XMLHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            FF = "[object HTMLDataElement]")
    public void data() throws Exception {
        test("data");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlContent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLContentElement]",
            IE = "[object HTMLUnknownElement]")
    public void content() throws Exception {
        test("content");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "[object HTMLPictureElement]",
            FF = "[object HTMLPictureElement]")
    public void picture() throws Exception {
        test("picture");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.html.HtmlPicture}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTemplateElement]",
            IE = "[object HTMLUnknownElement]")
    public void template() throws Exception {
        test("template");
    }
}
