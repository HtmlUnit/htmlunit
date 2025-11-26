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
package org.htmlunit.general.huge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.htmlunit.WebClient;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.DefaultElementFactory;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for an element close tag to close another element, which is defined in
 * {@link org.htmlunit.cyberneko.HTMLElements}.
 *
 * @author Ronald Brill
 */
public class ElementClosesElement2Test extends WebDriverTestCase {

    private static int ServerRestartCount_;

    private static final String RESULT_SCRIPT = "  var e = document.getElementById('outer');\n"
            + "  var res = '-';\n"
            + "  try {\n"
            + "    res = e == null ? e : e.children.length;\n"
            + "  } catch(e) { res = 'exception'; }\n"
            + "  return '' + res;";

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        final List<Arguments> list = new ArrayList<>();
        final List<String> strings = new ArrayList<>(DefaultElementFactory.SUPPORTED_TAGS_);
        strings.add("unknown");

        for (final String parent : strings) {
            for (final String child : strings) {
                list.add(Arguments.of(parent, child));
            }
        }
        return list;
    }

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @ParameterizedTest(name = "_{0}_{1}")
    @MethodSource("data")
    void test(final String parent, final String child) throws Exception {
        String bodyStart = "<body>\n";
        String bodyEnd = "</body>\n";

        final String html;
        if ("caption".equals(parent)) {
            html = "<table id='outer'><caption></" + child + "></table>\n";
        }
        else if ("col".equals(parent)) {
            html = "<table><colgroup id='outer'><col></" + child + "></table>\n";
        }
        else if ("colgroup".equals(parent)) {
            html = "<table id='outer'><colgroup></" + child + "></table>\n";
        }
        else if ("frame".equals(parent)) {
            bodyStart = "<frameset id='outer'>\n";
            html = "<frame></" + child + "></frame>\n";
            bodyEnd = "</frameset></html>";
        }
        else if ("frameset".equals(parent)) {
            bodyStart = "";
            html = "<frameset id='outer'></" + child + "></frameset>\n";
            bodyEnd = "";
        }
        else if ("script".equals(parent)) {
            html = "<script id='outer'>//</" + child + ">\n</script>\n";
        }
        else if ("tbody".equals(parent)) {
            html = "<table id='outer'><tbody></" + child + "></table>\n";
        }
        else if ("td".equals(parent)) {
            html = "<table><tr id='outer'><td></" + child + "></table>\n";
        }
        else if ("tfoot".equals(parent)) {
            html = "<table id='outer'><tfoot></" + child + "></table>\n";
        }
        else if ("th".equals(parent)) {
            html = "<table><tr id='outer'><th></" + child + "></table>\n";
        }
        else if ("thead".equals(parent)) {
            html = "<table id='outer'><thead></" + child + "></table>\n";
        }
        else if ("tr".equals(parent)) {
            html = "<table id='outer'><tr></" + child + "></table>\n";
        }
        else {
            bodyStart = "<body id='outer'>\n";
            html = "<" + parent + "></" + child + ">\n";
        }

        String pageHtml = DOCTYPE_HTML
                + "<html><head>\n"
                + "<title>-</title>\n"
                + "</head>\n"
                + bodyStart
                + html
                + bodyEnd
                + "</html>";

        if ("basefont".equals(parent)
                || "base".equals(parent)
                || "isindex".equals(parent)) {
            pageHtml = DOCTYPE_HTML
                + "<html><head id='outer'>\n"
                + "<" + parent + "></" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }
        else if ("head".equals(parent)) {
            pageHtml = DOCTYPE_HTML
                + "<html id='outer'><head></" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }
        else if ("title".equals(parent)) {
            pageHtml = DOCTYPE_HTML
                + "<html><head id='outer'>\n"
                + "<title></" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }

        String expected = "1";
        if (getExpectedAlerts().length == 1) {
            expected = getExpectedAlerts()[0];
        }
        else if ("command".equals(parent) && getBrowserVersion().isFirefox()) {
            expected = "1";
        }

        ServerRestartCount_++;
        if (ServerRestartCount_ == 200) {
            stopWebServers();
            ServerRestartCount_ = 0;
        }

        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            final WebClient webClient = ((HtmlUnitDriver) driver).getWebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
        }

        loadPage2(pageHtml);

        final String result = (String) ((JavascriptExecutor) driver).executeScript(RESULT_SCRIPT);
        try {
            assertEquals(expected, result);
        }
        catch (final AssertionError e) {
            System.out.println("    @Alerts(\"" + result + "\")\r\n"
                    + "    void _" + parent + "_" + child + "() throws Exception {\r\n"
                    + "        test(\""+ parent + "\", \"" + child + "\");\r\n"
                    + "    }\r\n");
        }
        assertEquals(expected, result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    @Alerts("2")
    void _area_br() throws Exception {
        test("area", "br");
    }

    @Alerts("2")
    void _area_p() throws Exception {
        test("area", "p");
    }

    @Alerts("0")
    void _body_abbr() throws Exception {
        test("body", "abbr");
    }

    @Alerts("0")
    void _body_acronym() throws Exception {
        test("body", "acronym");
    }

    @Alerts("0")
    void _body_a() throws Exception {
        test("body", "a");
    }

    @Alerts("0")
    void _body_address() throws Exception {
        test("body", "address");
    }

    @Alerts("0")
    void _body_area() throws Exception {
        test("body", "area");
    }

    @Alerts("0")
    void _body_article() throws Exception {
        test("body", "article");
    }

    @Alerts("0")
    void _body_aside() throws Exception {
        test("body", "aside");
    }

    @Alerts("0")
    void _body_audio() throws Exception {
        test("body", "audio");
    }

    @Alerts("0")
    void _body_base() throws Exception {
        test("body", "base");
    }

    @Alerts("0")
    void _body_basefont() throws Exception {
        test("body", "basefont");
    }

    @Alerts("0")
    void _body_bdi() throws Exception {
        test("body", "bdi");
    }

    @Alerts("0")
    void _body_bdo() throws Exception {
        test("body", "bdo");
    }

    @Alerts("0")
    void _body_big() throws Exception {
        test("body", "big");
    }

    @Alerts("0")
    void _body_blockquote() throws Exception {
        test("body", "blockquote");
    }

    @Alerts("0")
    void _body_body() throws Exception {
        test("body", "body");
    }

    @Alerts("0")
    void _body_b() throws Exception {
        test("body", "b");
    }

    @Alerts("0")
    void _body_button() throws Exception {
        test("body", "button");
    }

    @Alerts("0")
    void _body_canvas() throws Exception {
        test("body", "canvas");
    }

    @Alerts("0")
    void _body_caption() throws Exception {
        test("body", "caption");
    }

    @Alerts("0")
    void _body_center() throws Exception {
        test("body", "center");
    }

    @Alerts("0")
    void _body_cite() throws Exception {
        test("body", "cite");
    }

    @Alerts("0")
    void _body_code() throws Exception {
        test("body", "code");
    }

    @Alerts("0")
    void _body_data() throws Exception {
        test("body", "data");
    }

    @Alerts("0")
    void _body_datalist() throws Exception {
        test("body", "datalist");
    }

    @Alerts("0")
    void _body_dfn() throws Exception {
        test("body", "dfn");
    }

    @Alerts("0")
    void _body_dd() throws Exception {
        test("body", "dd");
    }

    @Alerts("0")
    void _body_del() throws Exception {
        test("body", "del");
    }

    @Alerts("0")
    void _body_details() throws Exception {
        test("body", "details");
    }

    @Alerts("0")
    void _body_dialog() throws Exception {
        test("body", "dialog");
    }

    @Alerts("0")
    void _body_dir() throws Exception {
        test("body", "dir");
    }

    @Alerts("0")
    void _body_div() throws Exception {
        test("body", "div");
    }

    @Alerts("0")
    void _body_dl() throws Exception {
        test("body", "dl");
    }

    @Alerts("0")
    void _body_dt() throws Exception {
        test("body", "dt");
    }

    @Alerts("0")
    void _body_embed() throws Exception {
        test("body", "embed");
    }

    @Alerts("0")
    void _body_em() throws Exception {
        test("body", "em");
    }

    @Alerts("0")
    void _body_fieldset() throws Exception {
        test("body", "fieldset");
    }

    @Alerts("0")
    void _body_figcaption() throws Exception {
        test("body", "figcaption");
    }

    @Alerts("0")
    void _body_figure() throws Exception {
        test("body", "figure");
    }

    @Alerts("0")
    void _body_font() throws Exception {
        test("body", "font");
    }

    @Alerts("0")
    void _body_form() throws Exception {
        test("body", "form");
    }

    @Alerts("0")
    void _body_footer() throws Exception {
        test("body", "footer");
    }

    @Alerts("0")
    void _body_frame() throws Exception {
        test("body", "frame");
    }

    @Alerts("0")
    void _body_frameset() throws Exception {
        test("body", "frameset");
    }

    @Alerts("0")
    void _body_head() throws Exception {
        test("body", "head");
    }

    @Alerts("0")
    void _body_header() throws Exception {
        test("body", "header");
    }

    @Alerts("0")
    void _body_h1() throws Exception {
        test("body", "h1");
    }

    @Alerts("0")
    void _body_h2() throws Exception {
        test("body", "h2");
    }

    @Alerts("0")
    void _body_h3() throws Exception {
        test("body", "h3");
    }

    @Alerts("0")
    void _body_h4() throws Exception {
        test("body", "h4");
    }

    @Alerts("0")
    void _body_h5() throws Exception {
        test("body", "h5");
    }

    @Alerts("0")
    void _body_h6() throws Exception {
        test("body", "h6");
    }

    @Alerts("0")
    void _body_hr() throws Exception {
        test("body", "hr");
    }

    @Alerts("0")
    void _body_html() throws Exception {
        test("body", "html");
    }

    @Alerts("0")
    void _body_iframe() throws Exception {
        test("body", "iframe");
    }

    @Alerts("0")
    void _body_q() throws Exception {
        test("body", "q");
    }

    @Alerts("0")
    void _body_img() throws Exception {
        test("body", "img");
    }

    @Alerts("0")
    void _body_image() throws Exception {
        test("body", "image");
    }

    @Alerts("0")
    void _body_input() throws Exception {
        test("body", "input");
    }

    @Alerts("0")
    void _body_ins() throws Exception {
        test("body", "ins");
    }

    @Alerts("0")
    void _body_i() throws Exception {
        test("body", "i");
    }

    @Alerts("0")
    void _body_kbd() throws Exception {
        test("body", "kbd");
    }

    @Alerts("0")
    void _body_label() throws Exception {
        test("body", "label");
    }

    @Alerts("0")
    void _body_layer() throws Exception {
        test("body", "layer");
    }

    @Alerts("0")
    void _body_legend() throws Exception {
        test("body", "legend");
    }

    @Alerts("0")
    void _body_listing() throws Exception {
        test("body", "listing");
    }

    @Alerts("0")
    void _body_li() throws Exception {
        test("body", "li");
    }

    @Alerts("0")
    void _body_link() throws Exception {
        test("body", "link");
    }

    @Alerts("0")
    void _body_main() throws Exception {
        test("body", "main");
    }

    @Alerts("0")
    void _body_map() throws Exception {
        test("body", "map");
    }

    @Alerts("0")
    void _body_mark() throws Exception {
        test("body", "mark");
    }

    @Alerts("0")
    void _body_marquee() throws Exception {
        test("body", "marquee");
    }

    @Alerts("0")
    void _body_menu() throws Exception {
        test("body", "menu");
    }

    @Alerts("0")
    void _body_meta() throws Exception {
        test("body", "meta");
    }

    @Alerts("0")
    void _body_meter() throws Exception {
        test("body", "meter");
    }

    @Alerts("0")
    void _body_nav() throws Exception {
        test("body", "nav");
    }

    @Alerts("0")
    void _body_nobr() throws Exception {
        test("body", "nobr");
    }

    @Alerts("0")
    void _body_noembed() throws Exception {
        test("body", "noembed");
    }

    @Alerts("0")
    void _body_noframes() throws Exception {
        test("body", "noframes");
    }

    @Alerts("0")
    void _body_nolayer() throws Exception {
        test("body", "nolayer");
    }

    @Alerts("0")
    void _body_noscript() throws Exception {
        test("body", "noscript");
    }

    @Alerts("0")
    void _body_object() throws Exception {
        test("body", "object");
    }

    @Alerts("0")
    void _body_ol() throws Exception {
        test("body", "ol");
    }

    @Alerts("0")
    void _body_optgroup() throws Exception {
        test("body", "optgroup");
    }

    @Alerts("0")
    void _body_option() throws Exception {
        test("body", "option");
    }

    @Alerts("0")
    void _body_output() throws Exception {
        test("body", "output");
    }

    @Alerts("0")
    void _body_param() throws Exception {
        test("body", "param");
    }

    @Alerts("0")
    void _body_picture() throws Exception {
        test("body", "picture");
    }

    @Alerts("0")
    void _body_plaintext() throws Exception {
        test("body", "plaintext");
    }

    @Alerts("0")
    void _body_pre() throws Exception {
        test("body", "pre");
    }

    @Alerts("0")
    void _body_progress() throws Exception {
        test("body", "progress");
    }

    @Alerts("0")
    void _body_rb() throws Exception {
        test("body", "rb");
    }

    @Alerts("0")
    void _body_rp() throws Exception {
        test("body", "rp");
    }

    @Alerts("0")
    void _body_rt() throws Exception {
        test("body", "rt");
    }

    @Alerts("0")
    void _body_rtc() throws Exception {
        test("body", "rtc");
    }

    @Alerts("0")
    void _body_ruby() throws Exception {
        test("body", "ruby");
    }

    @Alerts("0")
    void _body_s() throws Exception {
        test("body", "s");
    }

    @Alerts("0")
    void _body_samp() throws Exception {
        test("body", "samp");
    }

    @Alerts("0")
    void _body_script() throws Exception {
        test("body", "script");
    }

    @Alerts("0")
    void _body_section() throws Exception {
        test("body", "section");
    }

    @Alerts("0")
    void _body_select() throws Exception {
        test("body", "select");
    }

    @Alerts("0")
    void _body_slot() throws Exception {
        test("body", "slot");
    }

    @Alerts("0")
    void _body_small() throws Exception {
        test("body", "small");
    }

    @Alerts("0")
    void _body_source() throws Exception {
        test("body", "source");
    }

    @Alerts("0")
    void _body_span() throws Exception {
        test("body", "span");
    }

    @Alerts("0")
    void _body_strike() throws Exception {
        test("body", "strike");
    }

    @Alerts("0")
    void _body_strong() throws Exception {
        test("body", "strong");
    }

    @Alerts("0")
    void _body_style() throws Exception {
        test("body", "style");
    }

    @Alerts("0")
    void _body_sub() throws Exception {
        test("body", "sub");
    }

    @Alerts("0")
    void _body_summary() throws Exception {
        test("body", "summary");
    }

    @Alerts("0")
    void _body_sup() throws Exception {
        test("body", "sup");
    }

    @Alerts("0")
    void _body_svg() throws Exception {
        test("body", "svg");
    }

    @Alerts("0")
    void _body_table() throws Exception {
        test("body", "table");
    }

    @Alerts("0")
    void _body_col() throws Exception {
        test("body", "col");
    }

    @Alerts("0")
    void _body_colgroup() throws Exception {
        test("body", "colgroup");
    }

    @Alerts("0")
    void _body_tbody() throws Exception {
        test("body", "tbody");
    }

    @Alerts("0")
    void _body_td() throws Exception {
        test("body", "td");
    }

    @Alerts("0")
    void _body_th() throws Exception {
        test("body", "th");
    }

    @Alerts("0")
    void _body_tr() throws Exception {
        test("body", "tr");
    }

    @Alerts("0")
    void _body_textarea() throws Exception {
        test("body", "textarea");
    }

    @Alerts("0")
    void _body_tfoot() throws Exception {
        test("body", "tfoot");
    }

    @Alerts("0")
    void _body_thead() throws Exception {
        test("body", "thead");
    }

    @Alerts("0")
    void _body_tt() throws Exception {
        test("body", "tt");
    }

    @Alerts("0")
    void _body_template() throws Exception {
        test("body", "template");
    }

    @Alerts("0")
    void _body_time() throws Exception {
        test("body", "time");
    }

    @Alerts("0")
    void _body_title() throws Exception {
        test("body", "title");
    }

    @Alerts("0")
    void _body_track() throws Exception {
        test("body", "track");
    }

    @Alerts("0")
    void _body_u() throws Exception {
        test("body", "u");
    }

    @Alerts("0")
    void _body_ul() throws Exception {
        test("body", "ul");
    }

    @Alerts("0")
    void _body_var() throws Exception {
        test("body", "var");
    }

    @Alerts("0")
    void _body_video() throws Exception {
        test("body", "video");
    }

    @Alerts("0")
    void _body_wbr() throws Exception {
        test("body", "wbr");
    }

    @Alerts("0")
    void _body_xmp() throws Exception {
        test("body", "xmp");
    }

    @Alerts("0")
    void _body_unknown() throws Exception {
        test("body", "unknown");
    }

    @Alerts("2")
    void _br_br() throws Exception {
        test("br", "br");
    }

    @Alerts("2")
    void _br_p() throws Exception {
        test("br", "p");
    }

    @Alerts("2")
    void _embed_br() throws Exception {
        test("embed", "br");
    }

    @Alerts("2")
    void _embed_p() throws Exception {
        test("embed", "p");
    }

    @Alerts("0")
    void _frameset_abbr() throws Exception {
        test("frameset", "abbr");
    }

    @Alerts("0")
    void _frameset_acronym() throws Exception {
        test("frameset", "acronym");
    }

    @Alerts("0")
    void _frameset_a() throws Exception {
        test("frameset", "a");
    }

    @Alerts("0")
    void _frameset_address() throws Exception {
        test("frameset", "address");
    }

    @Alerts("0")
    void _frameset_area() throws Exception {
        test("frameset", "area");
    }

    @Alerts("0")
    void _frameset_article() throws Exception {
        test("frameset", "article");
    }

    @Alerts("0")
    void _frameset_aside() throws Exception {
        test("frameset", "aside");
    }

    @Alerts("0")
    void _frameset_audio() throws Exception {
        test("frameset", "audio");
    }

    @Alerts("0")
    void _frameset_base() throws Exception {
        test("frameset", "base");
    }

    @Alerts("0")
    void _frameset_basefont() throws Exception {
        test("frameset", "basefont");
    }

    @Alerts("0")
    void _frameset_bdi() throws Exception {
        test("frameset", "bdi");
    }

    @Alerts("0")
    void _frameset_bdo() throws Exception {
        test("frameset", "bdo");
    }

    @Alerts("0")
    void _frameset_big() throws Exception {
        test("frameset", "big");
    }

    @Alerts("0")
    void _frameset_blockquote() throws Exception {
        test("frameset", "blockquote");
    }

    @Alerts("0")
    void _frameset_body() throws Exception {
        test("frameset", "body");
    }

    @Alerts("0")
    void _frameset_b() throws Exception {
        test("frameset", "b");
    }

    @Alerts("0")
    void _frameset_br() throws Exception {
        test("frameset", "br");
    }

    @Alerts("0")
    void _frameset_button() throws Exception {
        test("frameset", "button");
    }

    @Alerts("0")
    void _frameset_canvas() throws Exception {
        test("frameset", "canvas");
    }

    @Alerts("0")
    void _frameset_caption() throws Exception {
        test("frameset", "caption");
    }

    @Alerts("0")
    void _frameset_center() throws Exception {
        test("frameset", "center");
    }

    @Alerts("0")
    void _frameset_cite() throws Exception {
        test("frameset", "cite");
    }

    @Alerts("0")
    void _frameset_code() throws Exception {
        test("frameset", "code");
    }

    @Alerts("0")
    void _frameset_data() throws Exception {
        test("frameset", "data");
    }

    @Alerts("0")
    void _frameset_datalist() throws Exception {
        test("frameset", "datalist");
    }

    @Alerts("0")
    void _frameset_dfn() throws Exception {
        test("frameset", "dfn");
    }

    @Alerts("0")
    void _frameset_dd() throws Exception {
        test("frameset", "dd");
    }

    @Alerts("0")
    void _frameset_del() throws Exception {
        test("frameset", "del");
    }

    @Alerts("0")
    void _frameset_details() throws Exception {
        test("frameset", "details");
    }

    @Alerts("0")
    void _frameset_dialog() throws Exception {
        test("frameset", "dialog");
    }

    @Alerts("0")
    void _frameset_dir() throws Exception {
        test("frameset", "dir");
    }

    @Alerts("0")
    void _frameset_div() throws Exception {
        test("frameset", "div");
    }

    @Alerts("0")
    void _frameset_dl() throws Exception {
        test("frameset", "dl");
    }

    @Alerts("0")
    void _frameset_dt() throws Exception {
        test("frameset", "dt");
    }

    @Alerts("0")
    void _frameset_embed() throws Exception {
        test("frameset", "embed");
    }

    @Alerts("0")
    void _frameset_em() throws Exception {
        test("frameset", "em");
    }

    @Alerts("0")
    void _frameset_fieldset() throws Exception {
        test("frameset", "fieldset");
    }

    @Alerts("0")
    void _frameset_figcaption() throws Exception {
        test("frameset", "figcaption");
    }

    @Alerts("0")
    void _frameset_figure() throws Exception {
        test("frameset", "figure");
    }

    @Alerts("0")
    void _frameset_font() throws Exception {
        test("frameset", "font");
    }

    @Alerts("0")
    void _frameset_form() throws Exception {
        test("frameset", "form");
    }

    @Alerts("0")
    void _frameset_footer() throws Exception {
        test("frameset", "footer");
    }

    @Alerts("0")
    void _frameset_frame() throws Exception {
        test("frameset", "frame");
    }

    @Alerts("0")
    void _frameset_frameset() throws Exception {
        test("frameset", "frameset");
    }

    @Alerts("0")
    void _frameset_head() throws Exception {
        test("frameset", "head");
    }

    @Alerts("0")
    void _frameset_header() throws Exception {
        test("frameset", "header");
    }

    @Alerts("0")
    void _frameset_h1() throws Exception {
        test("frameset", "h1");
    }

    @Alerts("0")
    void _frameset_h2() throws Exception {
        test("frameset", "h2");
    }

    @Alerts("0")
    void _frameset_h3() throws Exception {
        test("frameset", "h3");
    }

    @Alerts("0")
    void _frameset_h4() throws Exception {
        test("frameset", "h4");
    }

    @Alerts("0")
    void _frameset_h5() throws Exception {
        test("frameset", "h5");
    }

    @Alerts("0")
    void _frameset_h6() throws Exception {
        test("frameset", "h6");
    }

    @Alerts("0")
    void _frameset_hr() throws Exception {
        test("frameset", "hr");
    }

    @Alerts("0")
    void _frameset_html() throws Exception {
        test("frameset", "html");
    }

    @Alerts("0")
    void _frameset_iframe() throws Exception {
        test("frameset", "iframe");
    }

    @Alerts("0")
    void _frameset_q() throws Exception {
        test("frameset", "q");
    }

    @Alerts("0")
    void _frameset_img() throws Exception {
        test("frameset", "img");
    }

    @Alerts("0")
    void _frameset_image() throws Exception {
        test("frameset", "image");
    }

    @Alerts("0")
    void _frameset_input() throws Exception {
        test("frameset", "input");
    }

    @Alerts("0")
    void _frameset_ins() throws Exception {
        test("frameset", "ins");
    }

    @Alerts("0")
    void _frameset_i() throws Exception {
        test("frameset", "i");
    }

    @Alerts("0")
    void _frameset_kbd() throws Exception {
        test("frameset", "kbd");
    }

    @Alerts("0")
    void _frameset_label() throws Exception {
        test("frameset", "label");
    }

    @Alerts("0")
    void _frameset_layer() throws Exception {
        test("frameset", "layer");
    }

    @Alerts("0")
    void _frameset_legend() throws Exception {
        test("frameset", "legend");
    }

    @Alerts("0")
    void _frameset_listing() throws Exception {
        test("frameset", "listing");
    }

    @Alerts("0")
    void _frameset_li() throws Exception {
        test("frameset", "li");
    }

    @Alerts("0")
    void _frameset_link() throws Exception {
        test("frameset", "link");
    }

    @Alerts("0")
    void _frameset_main() throws Exception {
        test("frameset", "main");
    }

    @Alerts("0")
    void _frameset_map() throws Exception {
        test("frameset", "map");
    }

    @Alerts("0")
    void _frameset_mark() throws Exception {
        test("frameset", "mark");
    }

    @Alerts("0")
    void _frameset_marquee() throws Exception {
        test("frameset", "marquee");
    }

    @Alerts("0")
    void _frameset_menu() throws Exception {
        test("frameset", "menu");
    }

    @Alerts("0")
    void _frameset_meta() throws Exception {
        test("frameset", "meta");
    }

    @Alerts("0")
    void _frameset_meter() throws Exception {
        test("frameset", "meter");
    }

    @Alerts("0")
    void _frameset_nav() throws Exception {
        test("frameset", "nav");
    }

    @Alerts("0")
    void _frameset_nobr() throws Exception {
        test("frameset", "nobr");
    }

    @Alerts("0")
    void _frameset_noembed() throws Exception {
        test("frameset", "noembed");
    }

    @Alerts("0")
    void _frameset_noframes() throws Exception {
        test("frameset", "noframes");
    }

    @Alerts("0")
    void _frameset_nolayer() throws Exception {
        test("frameset", "nolayer");
    }

    @Alerts("0")
    void _frameset_noscript() throws Exception {
        test("frameset", "noscript");
    }

    @Alerts("0")
    void _frameset_object() throws Exception {
        test("frameset", "object");
    }

    @Alerts("0")
    void _frameset_ol() throws Exception {
        test("frameset", "ol");
    }

    @Alerts("0")
    void _frameset_optgroup() throws Exception {
        test("frameset", "optgroup");
    }

    @Alerts("0")
    void _frameset_option() throws Exception {
        test("frameset", "option");
    }

    @Alerts("0")
    void _frameset_output() throws Exception {
        test("frameset", "output");
    }

    @Alerts("0")
    void _frameset_p() throws Exception {
        test("frameset", "p");
    }

    @Alerts("0")
    void _frameset_param() throws Exception {
        test("frameset", "param");
    }

    @Alerts("0")
    void _frameset_picture() throws Exception {
        test("frameset", "picture");
    }

    @Alerts("0")
    void _frameset_plaintext() throws Exception {
        test("frameset", "plaintext");
    }

    @Alerts("0")
    void _frameset_pre() throws Exception {
        test("frameset", "pre");
    }

    @Alerts("0")
    void _frameset_progress() throws Exception {
        test("frameset", "progress");
    }

    @Alerts("0")
    void _frameset_rb() throws Exception {
        test("frameset", "rb");
    }

    @Alerts("0")
    void _frameset_rp() throws Exception {
        test("frameset", "rp");
    }

    @Alerts("0")
    void _frameset_rt() throws Exception {
        test("frameset", "rt");
    }

    @Alerts("0")
    void _frameset_rtc() throws Exception {
        test("frameset", "rtc");
    }

    @Alerts("0")
    void _frameset_ruby() throws Exception {
        test("frameset", "ruby");
    }

    @Alerts("0")
    void _frameset_s() throws Exception {
        test("frameset", "s");
    }

    @Alerts("0")
    void _frameset_samp() throws Exception {
        test("frameset", "samp");
    }

    @Alerts("0")
    void _frameset_script() throws Exception {
        test("frameset", "script");
    }

    @Alerts("0")
    void _frameset_section() throws Exception {
        test("frameset", "section");
    }

    @Alerts("0")
    void _frameset_select() throws Exception {
        test("frameset", "select");
    }

    @Alerts("0")
    void _frameset_slot() throws Exception {
        test("frameset", "slot");
    }

    @Alerts("0")
    void _frameset_small() throws Exception {
        test("frameset", "small");
    }

    @Alerts("0")
    void _frameset_source() throws Exception {
        test("frameset", "source");
    }

    @Alerts("0")
    void _frameset_span() throws Exception {
        test("frameset", "span");
    }

    @Alerts("0")
    void _frameset_strike() throws Exception {
        test("frameset", "strike");
    }

    @Alerts("0")
    void _frameset_strong() throws Exception {
        test("frameset", "strong");
    }

    @Alerts("0")
    void _frameset_style() throws Exception {
        test("frameset", "style");
    }

    @Alerts("0")
    void _frameset_sub() throws Exception {
        test("frameset", "sub");
    }

    @Alerts("0")
    void _frameset_summary() throws Exception {
        test("frameset", "summary");
    }

    @Alerts("0")
    void _frameset_sup() throws Exception {
        test("frameset", "sup");
    }

    @Alerts("0")
    void _frameset_svg() throws Exception {
        test("frameset", "svg");
    }

    @Alerts("0")
    void _frameset_table() throws Exception {
        test("frameset", "table");
    }

    @Alerts("0")
    void _frameset_col() throws Exception {
        test("frameset", "col");
    }

    @Alerts("0")
    void _frameset_colgroup() throws Exception {
        test("frameset", "colgroup");
    }

    @Alerts("0")
    void _frameset_tbody() throws Exception {
        test("frameset", "tbody");
    }

    @Alerts("0")
    void _frameset_td() throws Exception {
        test("frameset", "td");
    }

    @Alerts("0")
    void _frameset_th() throws Exception {
        test("frameset", "th");
    }

    @Alerts("0")
    void _frameset_tr() throws Exception {
        test("frameset", "tr");
    }

    @Alerts("0")
    void _frameset_textarea() throws Exception {
        test("frameset", "textarea");
    }

    @Alerts("0")
    void _frameset_tfoot() throws Exception {
        test("frameset", "tfoot");
    }

    @Alerts("0")
    void _frameset_thead() throws Exception {
        test("frameset", "thead");
    }

    @Alerts("0")
    void _frameset_tt() throws Exception {
        test("frameset", "tt");
    }

    @Alerts("0")
    void _frameset_template() throws Exception {
        test("frameset", "template");
    }

    @Alerts("0")
    void _frameset_time() throws Exception {
        test("frameset", "time");
    }

    @Alerts("0")
    void _frameset_title() throws Exception {
        test("frameset", "title");
    }

    @Alerts("0")
    void _frameset_track() throws Exception {
        test("frameset", "track");
    }

    @Alerts("0")
    void _frameset_u() throws Exception {
        test("frameset", "u");
    }

    @Alerts("0")
    void _frameset_ul() throws Exception {
        test("frameset", "ul");
    }

    @Alerts("0")
    void _frameset_var() throws Exception {
        test("frameset", "var");
    }

    @Alerts("0")
    void _frameset_video() throws Exception {
        test("frameset", "video");
    }

    @Alerts("0")
    void _frameset_wbr() throws Exception {
        test("frameset", "wbr");
    }

    @Alerts("0")
    void _frameset_xmp() throws Exception {
        test("frameset", "xmp");
    }

    @Alerts("0")
    void _frameset_unknown() throws Exception {
        test("frameset", "unknown");
    }

    @Alerts("2")
    void _head_abbr() throws Exception {
        test("head", "abbr");
    }

    @Alerts("2")
    void _head_acronym() throws Exception {
        test("head", "acronym");
    }

    @Alerts("2")
    void _head_a() throws Exception {
        test("head", "a");
    }

    @Alerts("2")
    void _head_address() throws Exception {
        test("head", "address");
    }

    @Alerts("2")
    void _head_area() throws Exception {
        test("head", "area");
    }

    @Alerts("2")
    void _head_article() throws Exception {
        test("head", "article");
    }

    @Alerts("2")
    void _head_aside() throws Exception {
        test("head", "aside");
    }

    @Alerts("2")
    void _head_audio() throws Exception {
        test("head", "audio");
    }

    @Alerts("2")
    void _head_base() throws Exception {
        test("head", "base");
    }

    @Alerts("2")
    void _head_basefont() throws Exception {
        test("head", "basefont");
    }

    @Alerts("2")
    void _head_bdi() throws Exception {
        test("head", "bdi");
    }

    @Alerts("2")
    void _head_bdo() throws Exception {
        test("head", "bdo");
    }

    @Alerts("2")
    void _head_big() throws Exception {
        test("head", "big");
    }

    @Alerts("2")
    void _head_blockquote() throws Exception {
        test("head", "blockquote");
    }

    @Alerts("2")
    void _head_body() throws Exception {
        test("head", "body");
    }

    @Alerts("2")
    void _head_b() throws Exception {
        test("head", "b");
    }

    @Alerts("2")
    void _head_br() throws Exception {
        test("head", "br");
    }

    @Alerts("2")
    void _head_button() throws Exception {
        test("head", "button");
    }

    @Alerts("2")
    void _head_canvas() throws Exception {
        test("head", "canvas");
    }

    @Alerts("2")
    void _head_caption() throws Exception {
        test("head", "caption");
    }

    @Alerts("2")
    void _head_center() throws Exception {
        test("head", "center");
    }

    @Alerts("2")
    void _head_cite() throws Exception {
        test("head", "cite");
    }

    @Alerts("2")
    void _head_code() throws Exception {
        test("head", "code");
    }

    @Alerts("2")
    void _head_data() throws Exception {
        test("head", "data");
    }

    @Alerts("2")
    void _head_datalist() throws Exception {
        test("head", "datalist");
    }

    @Alerts("2")
    void _head_dfn() throws Exception {
        test("head", "dfn");
    }

    @Alerts("2")
    void _head_dd() throws Exception {
        test("head", "dd");
    }

    @Alerts("2")
    void _head_del() throws Exception {
        test("head", "del");
    }

    @Alerts("2")
    void _head_details() throws Exception {
        test("head", "details");
    }

    @Alerts("2")
    void _head_dialog() throws Exception {
        test("head", "dialog");
    }

    @Alerts("2")
    void _head_dir() throws Exception {
        test("head", "dir");
    }

    @Alerts("2")
    void _head_div() throws Exception {
        test("head", "div");
    }

    @Alerts("2")
    void _head_dl() throws Exception {
        test("head", "dl");
    }

    @Alerts("2")
    void _head_dt() throws Exception {
        test("head", "dt");
    }

    @Alerts("2")
    void _head_embed() throws Exception {
        test("head", "embed");
    }

    @Alerts("2")
    void _head_em() throws Exception {
        test("head", "em");
    }

    @Alerts("2")
    void _head_fieldset() throws Exception {
        test("head", "fieldset");
    }

    @Alerts("2")
    void _head_figcaption() throws Exception {
        test("head", "figcaption");
    }

    @Alerts("2")
    void _head_figure() throws Exception {
        test("head", "figure");
    }

    @Alerts("2")
    void _head_font() throws Exception {
        test("head", "font");
    }

    @Alerts("2")
    void _head_form() throws Exception {
        test("head", "form");
    }

    @Alerts("2")
    void _head_footer() throws Exception {
        test("head", "footer");
    }

    @Alerts("2")
    void _head_frame() throws Exception {
        test("head", "frame");
    }

    @Alerts("2")
    void _head_frameset() throws Exception {
        test("head", "frameset");
    }

    @Alerts("2")
    void _head_head() throws Exception {
        test("head", "head");
    }

    @Alerts("2")
    void _head_header() throws Exception {
        test("head", "header");
    }

    @Alerts("2")
    void _head_h1() throws Exception {
        test("head", "h1");
    }

    @Alerts("2")
    void _head_h2() throws Exception {
        test("head", "h2");
    }

    @Alerts("2")
    void _head_h3() throws Exception {
        test("head", "h3");
    }

    @Alerts("2")
    void _head_h4() throws Exception {
        test("head", "h4");
    }

    @Alerts("2")
    void _head_h5() throws Exception {
        test("head", "h5");
    }

    @Alerts("2")
    void _head_h6() throws Exception {
        test("head", "h6");
    }

    @Alerts("2")
    void _head_hr() throws Exception {
        test("head", "hr");
    }

    @Alerts("2")
    void _head_html() throws Exception {
        test("head", "html");
    }

    @Alerts("2")
    void _head_iframe() throws Exception {
        test("head", "iframe");
    }

    @Alerts("2")
    void _head_q() throws Exception {
        test("head", "q");
    }

    @Alerts("2")
    void _head_img() throws Exception {
        test("head", "img");
    }

    @Alerts("2")
    void _head_image() throws Exception {
        test("head", "image");
    }

    @Alerts("2")
    void _head_input() throws Exception {
        test("head", "input");
    }

    @Alerts("2")
    void _head_ins() throws Exception {
        test("head", "ins");
    }

    @Alerts("2")
    void _head_i() throws Exception {
        test("head", "i");
    }

    @Alerts("2")
    void _head_kbd() throws Exception {
        test("head", "kbd");
    }

    @Alerts("2")
    void _head_label() throws Exception {
        test("head", "label");
    }

    @Alerts("2")
    void _head_layer() throws Exception {
        test("head", "layer");
    }

    @Alerts("2")
    void _head_legend() throws Exception {
        test("head", "legend");
    }

    @Alerts("2")
    void _head_listing() throws Exception {
        test("head", "listing");
    }

    @Alerts("2")
    void _head_li() throws Exception {
        test("head", "li");
    }

    @Alerts("2")
    void _head_link() throws Exception {
        test("head", "link");
    }

    @Alerts("2")
    void _head_main() throws Exception {
        test("head", "main");
    }

    @Alerts("2")
    void _head_map() throws Exception {
        test("head", "map");
    }

    @Alerts("2")
    void _head_mark() throws Exception {
        test("head", "mark");
    }

    @Alerts("2")
    void _head_marquee() throws Exception {
        test("head", "marquee");
    }

    @Alerts("2")
    void _head_menu() throws Exception {
        test("head", "menu");
    }

    @Alerts("2")
    void _head_meta() throws Exception {
        test("head", "meta");
    }

    @Alerts("2")
    void _head_meter() throws Exception {
        test("head", "meter");
    }

    @Alerts("2")
    void _head_nav() throws Exception {
        test("head", "nav");
    }

    @Alerts("2")
    void _head_nobr() throws Exception {
        test("head", "nobr");
    }

    @Alerts("2")
    void _head_noembed() throws Exception {
        test("head", "noembed");
    }

    @Alerts("2")
    void _head_noframes() throws Exception {
        test("head", "noframes");
    }

    @Alerts("2")
    void _head_nolayer() throws Exception {
        test("head", "nolayer");
    }

    @Alerts("2")
    void _head_noscript() throws Exception {
        test("head", "noscript");
    }

    @Alerts("2")
    void _head_object() throws Exception {
        test("head", "object");
    }

    @Alerts("2")
    void _head_ol() throws Exception {
        test("head", "ol");
    }

    @Alerts("2")
    void _head_optgroup() throws Exception {
        test("head", "optgroup");
    }

    @Alerts("2")
    void _head_option() throws Exception {
        test("head", "option");
    }

    @Alerts("2")
    void _head_output() throws Exception {
        test("head", "output");
    }

    @Alerts("2")
    void _head_p() throws Exception {
        test("head", "p");
    }

    @Alerts("2")
    void _head_param() throws Exception {
        test("head", "param");
    }

    @Alerts("2")
    void _head_picture() throws Exception {
        test("head", "picture");
    }

    @Alerts("2")
    void _head_plaintext() throws Exception {
        test("head", "plaintext");
    }

    @Alerts("2")
    void _head_pre() throws Exception {
        test("head", "pre");
    }

    @Alerts("2")
    void _head_progress() throws Exception {
        test("head", "progress");
    }

    @Alerts("2")
    void _head_rb() throws Exception {
        test("head", "rb");
    }

    @Alerts("2")
    void _head_rp() throws Exception {
        test("head", "rp");
    }

    @Alerts("2")
    void _head_rt() throws Exception {
        test("head", "rt");
    }

    @Alerts("2")
    void _head_rtc() throws Exception {
        test("head", "rtc");
    }

    @Alerts("2")
    void _head_ruby() throws Exception {
        test("head", "ruby");
    }

    @Alerts("2")
    void _head_s() throws Exception {
        test("head", "s");
    }

    @Alerts("2")
    void _head_samp() throws Exception {
        test("head", "samp");
    }

    @Alerts("2")
    void _head_script() throws Exception {
        test("head", "script");
    }

    @Alerts("2")
    void _head_section() throws Exception {
        test("head", "section");
    }

    @Alerts("2")
    void _head_select() throws Exception {
        test("head", "select");
    }

    @Alerts("2")
    void _head_slot() throws Exception {
        test("head", "slot");
    }

    @Alerts("2")
    void _head_small() throws Exception {
        test("head", "small");
    }

    @Alerts("2")
    void _head_source() throws Exception {
        test("head", "source");
    }

    @Alerts("2")
    void _head_span() throws Exception {
        test("head", "span");
    }

    @Alerts("2")
    void _head_strike() throws Exception {
        test("head", "strike");
    }

    @Alerts("2")
    void _head_strong() throws Exception {
        test("head", "strong");
    }

    @Alerts("2")
    void _head_style() throws Exception {
        test("head", "style");
    }

    @Alerts("2")
    void _head_sub() throws Exception {
        test("head", "sub");
    }

    @Alerts("2")
    void _head_summary() throws Exception {
        test("head", "summary");
    }

    @Alerts("2")
    void _head_sup() throws Exception {
        test("head", "sup");
    }

    @Alerts("2")
    void _head_svg() throws Exception {
        test("head", "svg");
    }

    @Alerts("2")
    void _head_table() throws Exception {
        test("head", "table");
    }

    @Alerts("2")
    void _head_col() throws Exception {
        test("head", "col");
    }

    @Alerts("2")
    void _head_colgroup() throws Exception {
        test("head", "colgroup");
    }

    @Alerts("2")
    void _head_tbody() throws Exception {
        test("head", "tbody");
    }

    @Alerts("2")
    void _head_td() throws Exception {
        test("head", "td");
    }

    @Alerts("2")
    void _head_th() throws Exception {
        test("head", "th");
    }

    @Alerts("2")
    void _head_tr() throws Exception {
        test("head", "tr");
    }

    @Alerts("2")
    void _head_textarea() throws Exception {
        test("head", "textarea");
    }

    @Alerts("2")
    void _head_tfoot() throws Exception {
        test("head", "tfoot");
    }

    @Alerts("2")
    void _head_thead() throws Exception {
        test("head", "thead");
    }

    @Alerts("2")
    void _head_tt() throws Exception {
        test("head", "tt");
    }

    @Alerts("2")
    void _head_template() throws Exception {
        test("head", "template");
    }

    @Alerts("2")
    void _head_time() throws Exception {
        test("head", "time");
    }

    @Alerts("2")
    void _head_title() throws Exception {
        test("head", "title");
    }

    @Alerts("2")
    void _head_track() throws Exception {
        test("head", "track");
    }

    @Alerts("2")
    void _head_u() throws Exception {
        test("head", "u");
    }

    @Alerts("2")
    void _head_ul() throws Exception {
        test("head", "ul");
    }

    @Alerts("2")
    void _head_var() throws Exception {
        test("head", "var");
    }

    @Alerts("2")
    void _head_video() throws Exception {
        test("head", "video");
    }

    @Alerts("2")
    void _head_wbr() throws Exception {
        test("head", "wbr");
    }

    @Alerts("2")
    void _head_xmp() throws Exception {
        test("head", "xmp");
    }

    @Alerts("2")
    void _head_unknown() throws Exception {
        test("head", "unknown");
    }

    @Alerts("2")
    void _hr_br() throws Exception {
        test("hr", "br");
    }

    @Alerts("2")
    void _hr_p() throws Exception {
        test("hr", "p");
    }

    @Alerts("0")
    void _html_abbr() throws Exception {
        test("html", "abbr");
    }

    @Alerts("0")
    void _html_acronym() throws Exception {
        test("html", "acronym");
    }

    @Alerts("0")
    void _html_a() throws Exception {
        test("html", "a");
    }

    @Alerts("0")
    void _html_address() throws Exception {
        test("html", "address");
    }

    @Alerts("0")
    void _html_area() throws Exception {
        test("html", "area");
    }

    @Alerts("0")
    void _html_article() throws Exception {
        test("html", "article");
    }

    @Alerts("0")
    void _html_aside() throws Exception {
        test("html", "aside");
    }

    @Alerts("0")
    void _html_audio() throws Exception {
        test("html", "audio");
    }

    @Alerts("0")
    void _html_base() throws Exception {
        test("html", "base");
    }

    @Alerts("0")
    void _html_basefont() throws Exception {
        test("html", "basefont");
    }

    @Alerts("0")
    void _html_bdi() throws Exception {
        test("html", "bdi");
    }

    @Alerts("0")
    void _html_bdo() throws Exception {
        test("html", "bdo");
    }

    @Alerts("0")
    void _html_big() throws Exception {
        test("html", "big");
    }

    @Alerts("0")
    void _html_blockquote() throws Exception {
        test("html", "blockquote");
    }

    @Alerts("0")
    void _html_body() throws Exception {
        test("html", "body");
    }

    @Alerts("0")
    void _html_b() throws Exception {
        test("html", "b");
    }

    @Alerts("0")
    void _html_button() throws Exception {
        test("html", "button");
    }

    @Alerts("0")
    void _html_canvas() throws Exception {
        test("html", "canvas");
    }

    @Alerts("0")
    void _html_caption() throws Exception {
        test("html", "caption");
    }

    @Alerts("0")
    void _html_center() throws Exception {
        test("html", "center");
    }

    @Alerts("0")
    void _html_cite() throws Exception {
        test("html", "cite");
    }

    @Alerts("0")
    void _html_code() throws Exception {
        test("html", "code");
    }

    @Alerts("0")
    void _html_data() throws Exception {
        test("html", "data");
    }

    @Alerts("0")
    void _html_datalist() throws Exception {
        test("html", "datalist");
    }

    @Alerts("0")
    void _html_dfn() throws Exception {
        test("html", "dfn");
    }

    @Alerts("0")
    void _html_dd() throws Exception {
        test("html", "dd");
    }

    @Alerts("0")
    void _html_del() throws Exception {
        test("html", "del");
    }

    @Alerts("0")
    void _html_details() throws Exception {
        test("html", "details");
    }

    @Alerts("0")
    void _html_dialog() throws Exception {
        test("html", "dialog");
    }

    @Alerts("0")
    void _html_dir() throws Exception {
        test("html", "dir");
    }

    @Alerts("0")
    void _html_div() throws Exception {
        test("html", "div");
    }

    @Alerts("0")
    void _html_dl() throws Exception {
        test("html", "dl");
    }

    @Alerts("0")
    void _html_dt() throws Exception {
        test("html", "dt");
    }

    @Alerts("0")
    void _html_embed() throws Exception {
        test("html", "embed");
    }

    @Alerts("0")
    void _html_em() throws Exception {
        test("html", "em");
    }

    @Alerts("0")
    void _html_fieldset() throws Exception {
        test("html", "fieldset");
    }

    @Alerts("0")
    void _html_figcaption() throws Exception {
        test("html", "figcaption");
    }

    @Alerts("0")
    void _html_figure() throws Exception {
        test("html", "figure");
    }

    @Alerts("0")
    void _html_font() throws Exception {
        test("html", "font");
    }

    @Alerts("0")
    void _html_form() throws Exception {
        test("html", "form");
    }

    @Alerts("0")
    void _html_footer() throws Exception {
        test("html", "footer");
    }

    @Alerts("0")
    void _html_frame() throws Exception {
        test("html", "frame");
    }

    @Alerts("0")
    void _html_frameset() throws Exception {
        test("html", "frameset");
    }

    @Alerts("0")
    void _html_head() throws Exception {
        test("html", "head");
    }

    @Alerts("0")
    void _html_header() throws Exception {
        test("html", "header");
    }

    @Alerts("0")
    void _html_h1() throws Exception {
        test("html", "h1");
    }

    @Alerts("0")
    void _html_h2() throws Exception {
        test("html", "h2");
    }

    @Alerts("0")
    void _html_h3() throws Exception {
        test("html", "h3");
    }

    @Alerts("0")
    void _html_h4() throws Exception {
        test("html", "h4");
    }

    @Alerts("0")
    void _html_h5() throws Exception {
        test("html", "h5");
    }

    @Alerts("0")
    void _html_h6() throws Exception {
        test("html", "h6");
    }

    @Alerts("0")
    void _html_hr() throws Exception {
        test("html", "hr");
    }

    @Alerts("0")
    void _html_html() throws Exception {
        test("html", "html");
    }

    @Alerts("0")
    void _html_iframe() throws Exception {
        test("html", "iframe");
    }

    @Alerts("0")
    void _html_q() throws Exception {
        test("html", "q");
    }

    @Alerts("0")
    void _html_img() throws Exception {
        test("html", "img");
    }

    @Alerts("0")
    void _html_image() throws Exception {
        test("html", "image");
    }

    @Alerts("0")
    void _html_input() throws Exception {
        test("html", "input");
    }

    @Alerts("0")
    void _html_ins() throws Exception {
        test("html", "ins");
    }

    @Alerts("0")
    void _html_i() throws Exception {
        test("html", "i");
    }

    @Alerts("0")
    void _html_kbd() throws Exception {
        test("html", "kbd");
    }

    @Alerts("0")
    void _html_label() throws Exception {
        test("html", "label");
    }

    @Alerts("0")
    void _html_layer() throws Exception {
        test("html", "layer");
    }

    @Alerts("0")
    void _html_legend() throws Exception {
        test("html", "legend");
    }

    @Alerts("0")
    void _html_listing() throws Exception {
        test("html", "listing");
    }

    @Alerts("0")
    void _html_li() throws Exception {
        test("html", "li");
    }

    @Alerts("0")
    void _html_link() throws Exception {
        test("html", "link");
    }

    @Alerts("0")
    void _html_main() throws Exception {
        test("html", "main");
    }

    @Alerts("0")
    void _html_map() throws Exception {
        test("html", "map");
    }

    @Alerts("0")
    void _html_mark() throws Exception {
        test("html", "mark");
    }

    @Alerts("0")
    void _html_marquee() throws Exception {
        test("html", "marquee");
    }

    @Alerts("0")
    void _html_menu() throws Exception {
        test("html", "menu");
    }

    @Alerts("0")
    void _html_meta() throws Exception {
        test("html", "meta");
    }

    @Alerts("0")
    void _html_meter() throws Exception {
        test("html", "meter");
    }

    @Alerts("0")
    void _html_nav() throws Exception {
        test("html", "nav");
    }

    @Alerts("0")
    void _html_nobr() throws Exception {
        test("html", "nobr");
    }

    @Alerts("0")
    void _html_noembed() throws Exception {
        test("html", "noembed");
    }

    @Alerts("0")
    void _html_noframes() throws Exception {
        test("html", "noframes");
    }

    @Alerts("0")
    void _html_nolayer() throws Exception {
        test("html", "nolayer");
    }

    @Alerts("0")
    void _html_noscript() throws Exception {
        test("html", "noscript");
    }

    @Alerts("0")
    void _html_object() throws Exception {
        test("html", "object");
    }

    @Alerts("0")
    void _html_ol() throws Exception {
        test("html", "ol");
    }

    @Alerts("0")
    void _html_optgroup() throws Exception {
        test("html", "optgroup");
    }

    @Alerts("0")
    void _html_option() throws Exception {
        test("html", "option");
    }

    @Alerts("0")
    void _html_output() throws Exception {
        test("html", "output");
    }

    @Alerts("0")
    void _html_param() throws Exception {
        test("html", "param");
    }

    @Alerts("0")
    void _html_picture() throws Exception {
        test("html", "picture");
    }

    @Alerts("0")
    void _html_plaintext() throws Exception {
        test("html", "plaintext");
    }

    @Alerts("0")
    void _html_pre() throws Exception {
        test("html", "pre");
    }

    @Alerts("0")
    void _html_progress() throws Exception {
        test("html", "progress");
    }

    @Alerts("0")
    void _html_rb() throws Exception {
        test("html", "rb");
    }

    @Alerts("0")
    void _html_rp() throws Exception {
        test("html", "rp");
    }

    @Alerts("0")
    void _html_rt() throws Exception {
        test("html", "rt");
    }

    @Alerts("0")
    void _html_rtc() throws Exception {
        test("html", "rtc");
    }

    @Alerts("0")
    void _html_ruby() throws Exception {
        test("html", "ruby");
    }

    @Alerts("0")
    void _html_s() throws Exception {
        test("html", "s");
    }

    @Alerts("0")
    void _html_samp() throws Exception {
        test("html", "samp");
    }

    @Alerts("0")
    void _html_script() throws Exception {
        test("html", "script");
    }

    @Alerts("0")
    void _html_section() throws Exception {
        test("html", "section");
    }

    @Alerts("0")
    void _html_select() throws Exception {
        test("html", "select");
    }

    @Alerts("0")
    void _html_slot() throws Exception {
        test("html", "slot");
    }

    @Alerts("0")
    void _html_small() throws Exception {
        test("html", "small");
    }

    @Alerts("0")
    void _html_source() throws Exception {
        test("html", "source");
    }

    @Alerts("0")
    void _html_span() throws Exception {
        test("html", "span");
    }

    @Alerts("0")
    void _html_strike() throws Exception {
        test("html", "strike");
    }

    @Alerts("0")
    void _html_strong() throws Exception {
        test("html", "strong");
    }

    @Alerts("0")
    void _html_style() throws Exception {
        test("html", "style");
    }

    @Alerts("0")
    void _html_sub() throws Exception {
        test("html", "sub");
    }

    @Alerts("0")
    void _html_summary() throws Exception {
        test("html", "summary");
    }

    @Alerts("0")
    void _html_sup() throws Exception {
        test("html", "sup");
    }

    @Alerts("0")
    void _html_svg() throws Exception {
        test("html", "svg");
    }

    @Alerts("0")
    void _html_table() throws Exception {
        test("html", "table");
    }

    @Alerts("0")
    void _html_col() throws Exception {
        test("html", "col");
    }

    @Alerts("0")
    void _html_colgroup() throws Exception {
        test("html", "colgroup");
    }

    @Alerts("0")
    void _html_tbody() throws Exception {
        test("html", "tbody");
    }

    @Alerts("0")
    void _html_td() throws Exception {
        test("html", "td");
    }

    @Alerts("0")
    void _html_th() throws Exception {
        test("html", "th");
    }

    @Alerts("0")
    void _html_tr() throws Exception {
        test("html", "tr");
    }

    @Alerts("0")
    void _html_textarea() throws Exception {
        test("html", "textarea");
    }

    @Alerts("0")
    void _html_tfoot() throws Exception {
        test("html", "tfoot");
    }

    @Alerts("0")
    void _html_thead() throws Exception {
        test("html", "thead");
    }

    @Alerts("0")
    void _html_tt() throws Exception {
        test("html", "tt");
    }

    @Alerts("0")
    void _html_template() throws Exception {
        test("html", "template");
    }

    @Alerts("0")
    void _html_time() throws Exception {
        test("html", "time");
    }

    @Alerts("0")
    void _html_title() throws Exception {
        test("html", "title");
    }

    @Alerts("0")
    void _html_track() throws Exception {
        test("html", "track");
    }

    @Alerts("0")
    void _html_u() throws Exception {
        test("html", "u");
    }

    @Alerts("0")
    void _html_ul() throws Exception {
        test("html", "ul");
    }

    @Alerts("0")
    void _html_var() throws Exception {
        test("html", "var");
    }

    @Alerts("0")
    void _html_video() throws Exception {
        test("html", "video");
    }

    @Alerts("0")
    void _html_wbr() throws Exception {
        test("html", "wbr");
    }

    @Alerts("0")
    void _html_xmp() throws Exception {
        test("html", "xmp");
    }

    @Alerts("0")
    void _html_unknown() throws Exception {
        test("html", "unknown");
    }

    @Alerts("2")
    void _img_br() throws Exception {
        test("img", "br");
    }

    @Alerts("2")
    void _img_p() throws Exception {
        test("img", "p");
    }

    @Alerts("2")
    void _image_br() throws Exception {
        test("image", "br");
    }

    @Alerts("2")
    void _image_p() throws Exception {
        test("image", "p");
    }

    @Alerts("2")
    void _input_br() throws Exception {
        test("input", "br");
    }

    @Alerts("2")
    void _input_p() throws Exception {
        test("input", "p");
    }

    @Alerts("2")
    void _link_br() throws Exception {
        test("link", "br");
    }

    @Alerts("2")
    void _link_p() throws Exception {
        test("link", "p");
    }

    @Alerts("2")
    void _meta_br() throws Exception {
        test("meta", "br");
    }

    @Alerts("2")
    void _meta_p() throws Exception {
        test("meta", "p");
    }

    @Alerts("2")
    void _param_br() throws Exception {
        test("param", "br");
    }

    @Alerts("2")
    void _param_p() throws Exception {
        test("param", "p");
    }

    @Alerts("0")
    void _script_abbr() throws Exception {
        test("script", "abbr");
    }

    @Alerts("0")
    void _script_acronym() throws Exception {
        test("script", "acronym");
    }

    @Alerts("0")
    void _script_a() throws Exception {
        test("script", "a");
    }

    @Alerts("0")
    void _script_address() throws Exception {
        test("script", "address");
    }

    @Alerts("0")
    void _script_area() throws Exception {
        test("script", "area");
    }

    @Alerts("0")
    void _script_article() throws Exception {
        test("script", "article");
    }

    @Alerts("0")
    void _script_aside() throws Exception {
        test("script", "aside");
    }

    @Alerts("0")
    void _script_audio() throws Exception {
        test("script", "audio");
    }

    @Alerts("0")
    void _script_base() throws Exception {
        test("script", "base");
    }

    @Alerts("0")
    void _script_basefont() throws Exception {
        test("script", "basefont");
    }

    @Alerts("0")
    void _script_bdi() throws Exception {
        test("script", "bdi");
    }

    @Alerts("0")
    void _script_bdo() throws Exception {
        test("script", "bdo");
    }

    @Alerts("0")
    void _script_big() throws Exception {
        test("script", "big");
    }

    @Alerts("0")
    void _script_blockquote() throws Exception {
        test("script", "blockquote");
    }

    @Alerts("0")
    void _script_body() throws Exception {
        test("script", "body");
    }

    @Alerts("0")
    void _script_b() throws Exception {
        test("script", "b");
    }

    @Alerts("0")
    void _script_br() throws Exception {
        test("script", "br");
    }

    @Alerts("0")
    void _script_button() throws Exception {
        test("script", "button");
    }

    @Alerts("0")
    void _script_canvas() throws Exception {
        test("script", "canvas");
    }

    @Alerts("0")
    void _script_caption() throws Exception {
        test("script", "caption");
    }

    @Alerts("0")
    void _script_center() throws Exception {
        test("script", "center");
    }

    @Alerts("0")
    void _script_cite() throws Exception {
        test("script", "cite");
    }

    @Alerts("0")
    void _script_code() throws Exception {
        test("script", "code");
    }

    @Alerts("0")
    void _script_data() throws Exception {
        test("script", "data");
    }

    @Alerts("0")
    void _script_datalist() throws Exception {
        test("script", "datalist");
    }

    @Alerts("0")
    void _script_dfn() throws Exception {
        test("script", "dfn");
    }

    @Alerts("0")
    void _script_dd() throws Exception {
        test("script", "dd");
    }

    @Alerts("0")
    void _script_del() throws Exception {
        test("script", "del");
    }

    @Alerts("0")
    void _script_details() throws Exception {
        test("script", "details");
    }

    @Alerts("0")
    void _script_dialog() throws Exception {
        test("script", "dialog");
    }

    @Alerts("0")
    void _script_dir() throws Exception {
        test("script", "dir");
    }

    @Alerts("0")
    void _script_div() throws Exception {
        test("script", "div");
    }

    @Alerts("0")
    void _script_dl() throws Exception {
        test("script", "dl");
    }

    @Alerts("0")
    void _script_dt() throws Exception {
        test("script", "dt");
    }

    @Alerts("0")
    void _script_embed() throws Exception {
        test("script", "embed");
    }

    @Alerts("0")
    void _script_em() throws Exception {
        test("script", "em");
    }

    @Alerts("0")
    void _script_fieldset() throws Exception {
        test("script", "fieldset");
    }

    @Alerts("0")
    void _script_figcaption() throws Exception {
        test("script", "figcaption");
    }

    @Alerts("0")
    void _script_figure() throws Exception {
        test("script", "figure");
    }

    @Alerts("0")
    void _script_font() throws Exception {
        test("script", "font");
    }

    @Alerts("0")
    void _script_form() throws Exception {
        test("script", "form");
    }

    @Alerts("0")
    void _script_footer() throws Exception {
        test("script", "footer");
    }

    @Alerts("0")
    void _script_frame() throws Exception {
        test("script", "frame");
    }

    @Alerts("0")
    void _script_frameset() throws Exception {
        test("script", "frameset");
    }

    @Alerts("0")
    void _script_head() throws Exception {
        test("script", "head");
    }

    @Alerts("0")
    void _script_header() throws Exception {
        test("script", "header");
    }

    @Alerts("0")
    void _script_h1() throws Exception {
        test("script", "h1");
    }

    @Alerts("0")
    void _script_h2() throws Exception {
        test("script", "h2");
    }

    @Alerts("0")
    void _script_h3() throws Exception {
        test("script", "h3");
    }

    @Alerts("0")
    void _script_h4() throws Exception {
        test("script", "h4");
    }

    @Alerts("0")
    void _script_h5() throws Exception {
        test("script", "h5");
    }

    @Alerts("0")
    void _script_h6() throws Exception {
        test("script", "h6");
    }

    @Alerts("0")
    void _script_hr() throws Exception {
        test("script", "hr");
    }

    @Alerts("0")
    void _script_html() throws Exception {
        test("script", "html");
    }

    @Alerts("0")
    void _script_iframe() throws Exception {
        test("script", "iframe");
    }

    @Alerts("0")
    void _script_q() throws Exception {
        test("script", "q");
    }

    @Alerts("0")
    void _script_img() throws Exception {
        test("script", "img");
    }

    @Alerts("0")
    void _script_image() throws Exception {
        test("script", "image");
    }

    @Alerts("0")
    void _script_input() throws Exception {
        test("script", "input");
    }

    @Alerts("0")
    void _script_ins() throws Exception {
        test("script", "ins");
    }

    @Alerts("0")
    void _script_i() throws Exception {
        test("script", "i");
    }

    @Alerts("0")
    void _script_kbd() throws Exception {
        test("script", "kbd");
    }

    @Alerts("0")
    void _script_label() throws Exception {
        test("script", "label");
    }

    @Alerts("0")
    void _script_layer() throws Exception {
        test("script", "layer");
    }

    @Alerts("0")
    void _script_legend() throws Exception {
        test("script", "legend");
    }

    @Alerts("0")
    void _script_listing() throws Exception {
        test("script", "listing");
    }

    @Alerts("0")
    void _script_li() throws Exception {
        test("script", "li");
    }

    @Alerts("0")
    void _script_link() throws Exception {
        test("script", "link");
    }

    @Alerts("0")
    void _script_main() throws Exception {
        test("script", "main");
    }

    @Alerts("0")
    void _script_map() throws Exception {
        test("script", "map");
    }

    @Alerts("0")
    void _script_mark() throws Exception {
        test("script", "mark");
    }

    @Alerts("0")
    void _script_marquee() throws Exception {
        test("script", "marquee");
    }

    @Alerts("0")
    void _script_menu() throws Exception {
        test("script", "menu");
    }

    @Alerts("0")
    void _script_meta() throws Exception {
        test("script", "meta");
    }

    @Alerts("0")
    void _script_meter() throws Exception {
        test("script", "meter");
    }

    @Alerts("0")
    void _script_nav() throws Exception {
        test("script", "nav");
    }

    @Alerts("0")
    void _script_nobr() throws Exception {
        test("script", "nobr");
    }

    @Alerts("0")
    void _script_noembed() throws Exception {
        test("script", "noembed");
    }

    @Alerts("0")
    void _script_noframes() throws Exception {
        test("script", "noframes");
    }

    @Alerts("0")
    void _script_nolayer() throws Exception {
        test("script", "nolayer");
    }

    @Alerts("0")
    void _script_noscript() throws Exception {
        test("script", "noscript");
    }

    @Alerts("0")
    void _script_object() throws Exception {
        test("script", "object");
    }

    @Alerts("0")
    void _script_ol() throws Exception {
        test("script", "ol");
    }

    @Alerts("0")
    void _script_optgroup() throws Exception {
        test("script", "optgroup");
    }

    @Alerts("0")
    void _script_option() throws Exception {
        test("script", "option");
    }

    @Alerts("0")
    void _script_output() throws Exception {
        test("script", "output");
    }

    @Alerts("0")
    void _script_p() throws Exception {
        test("script", "p");
    }

    @Alerts("0")
    void _script_param() throws Exception {
        test("script", "param");
    }

    @Alerts("0")
    void _script_picture() throws Exception {
        test("script", "picture");
    }

    @Alerts("0")
    void _script_plaintext() throws Exception {
        test("script", "plaintext");
    }

    @Alerts("0")
    void _script_pre() throws Exception {
        test("script", "pre");
    }

    @Alerts("0")
    void _script_progress() throws Exception {
        test("script", "progress");
    }

    @Alerts("0")
    void _script_rb() throws Exception {
        test("script", "rb");
    }

    @Alerts("0")
    void _script_rp() throws Exception {
        test("script", "rp");
    }

    @Alerts("0")
    void _script_rt() throws Exception {
        test("script", "rt");
    }

    @Alerts("0")
    void _script_rtc() throws Exception {
        test("script", "rtc");
    }

    @Alerts("0")
    void _script_ruby() throws Exception {
        test("script", "ruby");
    }

    @Alerts("0")
    void _script_s() throws Exception {
        test("script", "s");
    }

    @Alerts("0")
    void _script_samp() throws Exception {
        test("script", "samp");
    }

    @Alerts("0")
    void _script_script() throws Exception {
        test("script", "script");
    }

    @Alerts("0")
    void _script_section() throws Exception {
        test("script", "section");
    }

    @Alerts("0")
    void _script_select() throws Exception {
        test("script", "select");
    }

    @Alerts("0")
    void _script_slot() throws Exception {
        test("script", "slot");
    }

    @Alerts("0")
    void _script_small() throws Exception {
        test("script", "small");
    }

    @Alerts("0")
    void _script_source() throws Exception {
        test("script", "source");
    }

    @Alerts("0")
    void _script_span() throws Exception {
        test("script", "span");
    }

    @Alerts("0")
    void _script_strike() throws Exception {
        test("script", "strike");
    }

    @Alerts("0")
    void _script_strong() throws Exception {
        test("script", "strong");
    }

    @Alerts("0")
    void _script_style() throws Exception {
        test("script", "style");
    }

    @Alerts("0")
    void _script_sub() throws Exception {
        test("script", "sub");
    }

    @Alerts("0")
    void _script_summary() throws Exception {
        test("script", "summary");
    }

    @Alerts("0")
    void _script_sup() throws Exception {
        test("script", "sup");
    }

    @Alerts("0")
    void _script_svg() throws Exception {
        test("script", "svg");
    }

    @Alerts("0")
    void _script_table() throws Exception {
        test("script", "table");
    }

    @Alerts("0")
    void _script_col() throws Exception {
        test("script", "col");
    }

    @Alerts("0")
    void _script_colgroup() throws Exception {
        test("script", "colgroup");
    }

    @Alerts("0")
    void _script_tbody() throws Exception {
        test("script", "tbody");
    }

    @Alerts("0")
    void _script_td() throws Exception {
        test("script", "td");
    }

    @Alerts("0")
    void _script_th() throws Exception {
        test("script", "th");
    }

    @Alerts("0")
    void _script_tr() throws Exception {
        test("script", "tr");
    }

    @Alerts("0")
    void _script_textarea() throws Exception {
        test("script", "textarea");
    }

    @Alerts("0")
    void _script_tfoot() throws Exception {
        test("script", "tfoot");
    }

    @Alerts("0")
    void _script_thead() throws Exception {
        test("script", "thead");
    }

    @Alerts("0")
    void _script_tt() throws Exception {
        test("script", "tt");
    }

    @Alerts("0")
    void _script_template() throws Exception {
        test("script", "template");
    }

    @Alerts("0")
    void _script_time() throws Exception {
        test("script", "time");
    }

    @Alerts("0")
    void _script_title() throws Exception {
        test("script", "title");
    }

    @Alerts("0")
    void _script_track() throws Exception {
        test("script", "track");
    }

    @Alerts("0")
    void _script_u() throws Exception {
        test("script", "u");
    }

    @Alerts("0")
    void _script_ul() throws Exception {
        test("script", "ul");
    }

    @Alerts("0")
    void _script_var() throws Exception {
        test("script", "var");
    }

    @Alerts("0")
    void _script_video() throws Exception {
        test("script", "video");
    }

    @Alerts("0")
    void _script_wbr() throws Exception {
        test("script", "wbr");
    }

    @Alerts("0")
    void _script_xmp() throws Exception {
        test("script", "xmp");
    }

    @Alerts("0")
    void _script_unknown() throws Exception {
        test("script", "unknown");
    }

    @Alerts("2")
    void _source_br() throws Exception {
        test("source", "br");
    }

    @Alerts("2")
    void _source_p() throws Exception {
        test("source", "p");
    }

    @Alerts("2")
    void _svg_br() throws Exception {
        test("svg", "br");
    }

    @Alerts("2")
    void _svg_p() throws Exception {
        test("svg", "p");
    }

    @Alerts("2")
    void _table_br() throws Exception {
        test("table", "br");
    }

    @Alerts("2")
    void _table_p() throws Exception {
        test("table", "p");
    }

    @Alerts("2")
    void _track_br() throws Exception {
        test("track", "br");
    }

    @Alerts("2")
    void _track_p() throws Exception {
        test("track", "p");
    }

    @Alerts("2")
    void _wbr_br() throws Exception {
        test("wbr", "br");
    }

    @Alerts("2")
    void _wbr_p() throws Exception {
        test("wbr", "p");
    }
}
