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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.htmlunit.WebClient;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.DefaultElementFactory;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for an element to close another element, which is defined in
 * {@link org.htmlunit.cyberneko.HTMLElements}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ElementClosesElementTest extends WebDriverTestCase {

    private static int ServerRestartCount_;

    private static final List<String> PARENT_ZERO = Arrays.asList("area", "base", "basefont", "bgsound", "br",
            "command", "col", "colgroup",
            "embed", "frame", "frameset", "head", "hr", "iframe", "image", "img", "input", "keygen",
            "link", "meta", "noembed", "noframes", "noscript", "param", "plaintext",
            "script", "select", "source", "style",
            "table", "tbody", "template", "textarea", "tfoot", "thead", "title",
            "tr", "track", "wbr", "xmp");

    private static final List<String> CHILD_ZERO = Arrays.asList("body", "caption", "col", "colgroup",
            "frame", "frameset", "head", "html", "tbody", "td", "tfoot", "th", "thead", "tr");

    private static final List<String> SVG_CHILD_ZERO = Arrays.asList("b", "big", "blockquote", "body", "br",
            "center", "code", "dd", "div", "dl", "dt", "em", "embed", "h1", "h2", "h3", "h4", "h5", "h6", "head",
            "hr", "i", "img", "li", "listing", "menu", "meta", "nobr", "ol", "p", "pre", "ruby", "s", "small",
            "span", "strike", "strong", "sub", "sup", "table", "tt", "u", "ul", "var");

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
            html = "<table><caption id='outer'><" + child + "></table>\n";
        }
        else if ("col".equals(parent)) {
            html = "<table><colgroup><col id='outer'><" + child + "></table>\n";
        }
        else if ("colgroup".equals(parent)) {
            html = "<table><colgroup id='outer'><" + child + "></table>\n";
        }
        else if ("frame".equals(parent)) {
            bodyStart = "<frameset>\n";
            html = "<frame id='outer'><" + child + "></frame>\n";
            bodyEnd = "</frameset></html>";
        }
        else if ("frameset".equals(parent)) {
            bodyStart = "";
            html = "<frameset id='outer'><" + child + "></frameset>\n";
            bodyEnd = "";
        }
        else if ("script".equals(parent)) {
            html = "<script id='outer'>//<" + child + ">\n</script>\n";
        }
        else if ("tbody".equals(parent)) {
            html = "<table><tbody id='outer'><" + child + "></table>\n";
        }
        else if ("td".equals(parent)) {
            html = "<table><tr><td id='outer'><" + child + "></table>\n";
        }
        else if ("tfoot".equals(parent)) {
            html = "<table><tfoot id='outer'><" + child + "></table>\n";
        }
        else if ("th".equals(parent)) {
            html = "<table><tr><th id='outer'><" + child + "></table>\n";
        }
        else if ("thead".equals(parent)) {
            html = "<table><thead id='outer'><" + child + "></table>\n";
        }
        else if ("tr".equals(parent)) {
            html = "<table><tr id='outer'><" + child + "></table>\n";
        }
        else {
            html = "<" + parent + " id='outer'><" + child + ">\n";
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
                + "<html><head>\n"
                + "<" + parent + " id='outer'><" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }
        else if ("head".equals(parent)) {
            pageHtml = DOCTYPE_HTML
                + "<html><head id='outer'><" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }
        else if ("title".equals(parent)) {
            pageHtml = DOCTYPE_HTML
                + "<html><head>\n"
                + "<title id='outer'><" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }

        String expected = "1";
        if (getExpectedAlerts().length == 1) {
            expected = getExpectedAlerts()[0];
        }
        else {
            if (CHILD_ZERO.contains(child)) {
                expected = "0";
            }
            else if (PARENT_ZERO.contains(parent)) {
                expected = "0";
            }
            else if ("html".equals(parent)) {
                expected = "2";
            }

            if ("svg".equals(parent)) {
                expected = "1";
                if (SVG_CHILD_ZERO.contains(child)) {
                    expected = "0";
                }
            }

            if ("command".equals(parent) && getBrowserVersion().isFirefox()) {
                expected = "1";
            }
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
        assertEquals(expected, result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    @Alerts("0")
    void _a_a() throws Exception {
        test("a", "a");
    }

    @Alerts("0")
    void _button_button() throws Exception {
        test("button", "button");
    }

    @Alerts("1")
    void _colgroup_col() throws Exception {
        test("colgroup", "col");
    }

    @Alerts("1")
    void _colgroup_template() throws Exception {
        test("colgroup", "template");
    }

    @Alerts("0")
    void _command_body() throws Exception {
        test("command", "body");
    }

    @Alerts("0")
    void _command_caption() throws Exception {
        test("command", "caption");
    }

    @Alerts("0")
    void _command_col() throws Exception {
        test("command", "col");
    }

    @Alerts("0")
    void _command_colgroup() throws Exception {
        test("command", "colgroup");
    }

    @Alerts("0")
    void _command_frame() throws Exception {
        test("command", "frame");
    }

    @Alerts("0")
    void _command_frameset() throws Exception {
        test("command", "frameset");
    }

    @Alerts("0")
    void _command_head() throws Exception {
        test("command", "head");
    }

    @Alerts("0")
    void _command_html() throws Exception {
        test("command", "html");
    }

    @Alerts("0")
    void _command_tbody() throws Exception {
        test("command", "tbody");
    }

    @Alerts("0")
    void _command_td() throws Exception {
        test("command", "td");
    }

    @Alerts("0")
    void _command_tfoot() throws Exception {
        test("command", "tfoot");
    }

    @Alerts("0")
    void _command_th() throws Exception {
        test("command", "th");
    }

    @Alerts("0")
    void _command_thead() throws Exception {
        test("command", "thead");
    }

    @Alerts("0")
    void _command_tr() throws Exception {
        test("command", "tr");
    }

    @Alerts("0")
    void _dd_dd() throws Exception {
        test("dd", "dd");
    }

    @Alerts("0")
    void _dd_dt() throws Exception {
        test("dd", "dt");
    }

    @Alerts("0")
    void _dt_dd() throws Exception {
        test("dt", "dd");
    }

    @Alerts("0")
    void _dt_dt() throws Exception {
        test("dt", "dt");
    }

    @Alerts("0")
    void _form_form() throws Exception {
        test("form", "form");
    }

    @Alerts("1")
    void _form_isindex() throws Exception {
        test("form", "isindex");
    }

    @Alerts("1")
    void _frameset_frame() throws Exception {
        test("frameset", "frame");
    }

    @Alerts("1")
    void _frameset_frameset() throws Exception {
        test("frameset", "frameset");
    }

    @Alerts("1")
    void _frameset_noframes() throws Exception {
        test("frameset", "noframes");
    }

    @Alerts("0")
    void _h1_h1() throws Exception {
        test("h1", "h1");
    }

    @Alerts("0")
    void _h1_h2() throws Exception {
        test("h1", "h2");
    }

    @Alerts("0")
    void _h1_h3() throws Exception {
        test("h1", "h3");
    }

    @Alerts("0")
    void _h1_h4() throws Exception {
        test("h1", "h4");
    }

    @Alerts("0")
    void _h1_h5() throws Exception {
        test("h1", "h5");
    }

    @Alerts("0")
    void _h1_h6() throws Exception {
        test("h1", "h6");
    }

    @Alerts("0")
    void _h2_h1() throws Exception {
        test("h2", "h1");
    }

    @Alerts("0")
    void _h2_h2() throws Exception {
        test("h2", "h2");
    }

    @Alerts("0")
    void _h2_h3() throws Exception {
        test("h2", "h3");
    }

    @Alerts("0")
    void _h2_h4() throws Exception {
        test("h2", "h4");
    }

    @Alerts("0")
    void _h2_h5() throws Exception {
        test("h2", "h5");
    }

    @Alerts("0")
    void _h2_h6() throws Exception {
        test("h2", "h6");
    }

    @Alerts("0")
    void _h3_h1() throws Exception {
        test("h3", "h1");
    }

    @Alerts("0")
    void _h3_h2() throws Exception {
        test("h3", "h2");
    }

    @Alerts("0")
    void _h3_h3() throws Exception {
        test("h3", "h3");
    }

    @Alerts("0")
    void _h3_h4() throws Exception {
        test("h3", "h4");
    }

    @Alerts("0")
    void _h3_h5() throws Exception {
        test("h3", "h5");
    }

    @Alerts("0")
    void _h3_h6() throws Exception {
        test("h3", "h6");
    }

    @Alerts("0")
    void _h4_h1() throws Exception {
        test("h4", "h1");
    }

    @Alerts("0")
    void _h4_h2() throws Exception {
        test("h4", "h2");
    }

    @Alerts("0")
    void _h4_h3() throws Exception {
        test("h4", "h3");
    }

    @Alerts("0")
    void _h4_h4() throws Exception {
        test("h4", "h4");
    }

    @Alerts("0")
    void _h4_h5() throws Exception {
        test("h4", "h5");
    }

    @Alerts("0")
    void _h4_h6() throws Exception {
        test("h4", "h6");
    }

    @Alerts("0")
    void _h5_h1() throws Exception {
        test("h5", "h1");
    }

    @Alerts("0")
    void _h5_h2() throws Exception {
        test("h5", "h2");
    }

    @Alerts("0")
    void _h5_h3() throws Exception {
        test("h5", "h3");
    }

    @Alerts("0")
    void _h5_h4() throws Exception {
        test("h5", "h4");
    }

    @Alerts("0")
    void _h5_h5() throws Exception {
        test("h5", "h5");
    }

    @Alerts("0")
    void _h5_h6() throws Exception {
        test("h5", "h6");
    }

    @Alerts("0")
    void _h6_h1() throws Exception {
        test("h6", "h1");
    }

    @Alerts("0")
    void _h6_h2() throws Exception {
        test("h6", "h2");
    }

    @Alerts("0")
    void _h6_h3() throws Exception {
        test("h6", "h3");
    }

    @Alerts("0")
    void _h6_h4() throws Exception {
        test("h6", "h4");
    }

    @Alerts("0")
    void _h6_h5() throws Exception {
        test("h6", "h5");
    }

    @Alerts("0")
    void _h6_h6() throws Exception {
        test("h6", "h6");
    }

    @Alerts("1")
    void _head_base() throws Exception {
        test("head", "base");
    }

    @Alerts("1")
    void _head_basefont() throws Exception {
        test("head", "basefont");
    }

    @Alerts("1")
    void _head_bgsound() throws Exception {
        test("head", "bgsound");
    }

    @Alerts(DEFAULT = "1",
            FF = "0",
            FF_ESR = "0")
    void _head_command() throws Exception {
        test("head", "command");
    }

    @Alerts("1")
    void _head_link() throws Exception {
        test("head", "link");
    }

    @Alerts("1")
    void _head_meta() throws Exception {
        test("head", "meta");
    }

    @Alerts("1")
    void _head_noframes() throws Exception {
        test("head", "noframes");
    }

    @Alerts("1")
    void _head_noscript() throws Exception {
        test("head", "noscript");
    }

    @Alerts("1")
    void _head_script() throws Exception {
        test("head", "script");
    }

    @Alerts("1")
    void _head_style() throws Exception {
        test("head", "style");
    }

    @Alerts("1")
    void _head_template() throws Exception {
        test("head", "template");
    }

    @Alerts("1")
    void _head_title() throws Exception {
        test("head", "title");
    }

    @Alerts("2")
    void _html_body() throws Exception {
        test("html", "body");
    }

    @Alerts("2")
    void _html_caption() throws Exception {
        test("html", "caption");
    }

    @Alerts("2")
    void _html_col() throws Exception {
        test("html", "col");
    }

    @Alerts("2")
    void _html_colgroup() throws Exception {
        test("html", "colgroup");
    }

    @Alerts("2")
    void _html_frame() throws Exception {
        test("html", "frame");
    }

    @Alerts("2")
    void _html_frameset() throws Exception {
        test("html", "frameset");
    }

    @Alerts("2")
    void _html_head() throws Exception {
        test("html", "head");
    }

    @Alerts("2")
    void _html_html() throws Exception {
        test("html", "html");
    }

    @Alerts("2")
    void _html_tbody() throws Exception {
        test("html", "tbody");
    }

    @Alerts("2")
    void _html_td() throws Exception {
        test("html", "td");
    }

    @Alerts("2")
    void _html_tfoot() throws Exception {
        test("html", "tfoot");
    }

    @Alerts("2")
    void _html_th() throws Exception {
        test("html", "th");
    }

    @Alerts("2")
    void _html_thead() throws Exception {
        test("html", "thead");
    }

    @Alerts("2")
    void _html_tr() throws Exception {
        test("html", "tr");
    }

    @Alerts("null")
    @HtmlUnitNYI(
            CHROME = "0",
            EDGE = "0",
            FF = "0",
            FF_ESR = "0")
    void _isindex_frameset() throws Exception {
        test("isindex", "frameset");
    }

    @Alerts("1")
    void _isindex_isindex() throws Exception {
        test("isindex", "isindex");
    }

    @Alerts("0")
    void _li_caption() throws Exception {
        test("li", "caption");
    }

    @Alerts("0")
    void _li_li() throws Exception {
        test("li", "li");
    }

    @Alerts("0")
    void _nobr_nobr() throws Exception {
        test("nobr", "nobr");
    }

    @Alerts("0")
    void _option_optgroup() throws Exception {
        test("option", "optgroup");
    }

    @Alerts("0")
    void _option_option() throws Exception {
        test("option", "option");
    }

    @Alerts("0")
    void _p_address() throws Exception {
        test("p", "address");
    }

    @Alerts("0")
    void _p_article() throws Exception {
        test("p", "article");
    }

    @Alerts("0")
    void _p_aside() throws Exception {
        test("p", "aside");
    }

    @Alerts("0")
    void _p_blockquote() throws Exception {
        test("p", "blockquote");
    }

    @Alerts("0")
    void _p_center() throws Exception {
        test("p", "center");
    }

    @Alerts("0")
    void _p_dd() throws Exception {
        test("p", "dd");
    }

    @Alerts("0")
    void _p_details() throws Exception {
        test("p", "details");
    }

    @Alerts("0")
    void _p_dialog() throws Exception {
        test("p", "dialog");
    }

    @Alerts("0")
    void _p_dir() throws Exception {
        test("p", "dir");
    }

    @Alerts("0")
    void _p_div() throws Exception {
        test("p", "div");
    }

    @Alerts("0")
    void _p_dl() throws Exception {
        test("p", "dl");
    }

    @Alerts("0")
    void _p_dt() throws Exception {
        test("p", "dt");
    }

    @Alerts("0")
    void _p_fieldset() throws Exception {
        test("p", "fieldset");
    }

    @Alerts("0")
    void _p_figcaption() throws Exception {
        test("p", "figcaption");
    }

    @Alerts("0")
    void _p_figure() throws Exception {
        test("p", "figure");
    }

    @Alerts("0")
    void _p_footer() throws Exception {
        test("p", "footer");
    }

    @Alerts("0")
    void _p_form() throws Exception {
        test("p", "form");
    }

    @Alerts("0")
    void _p_h1() throws Exception {
        test("p", "h1");
    }

    @Alerts("0")
    void _p_h2() throws Exception {
        test("p", "h2");
    }

    @Alerts("0")
    void _p_h3() throws Exception {
        test("p", "h3");
    }

    @Alerts("0")
    void _p_h4() throws Exception {
        test("p", "h4");
    }

    @Alerts("0")
    void _p_h5() throws Exception {
        test("p", "h5");
    }

    @Alerts("0")
    void _p_h6() throws Exception {
        test("p", "h6");
    }

    @Alerts("0")
    void _p_header() throws Exception {
        test("p", "header");
    }

    @Alerts("0")
    void _p_hr() throws Exception {
        test("p", "hr");
    }

    @Alerts("1")
    void _p_isindex() throws Exception {
        test("p", "isindex");
    }

    @Alerts("0")
    void _p_li() throws Exception {
        test("p", "li");
    }

    @Alerts("0")
    void _p_listing() throws Exception {
        test("p", "listing");
    }

    @Alerts("0")
    void _p_main() throws Exception {
        test("p", "main");
    }

    @Alerts("0")
    void _p_menu() throws Exception {
        test("p", "menu");
    }

    @Alerts("0")
    void _p_nav() throws Exception {
        test("p", "nav");
    }

    @Alerts("0")
    void _p_ol() throws Exception {
        test("p", "ol");
    }

    @Alerts("0")
    void _p_p() throws Exception {
        test("p", "p");
    }

    @Alerts("0")
    void _p_plaintext() throws Exception {
        test("p", "plaintext");
    }

    @Alerts("0")
    void _p_pre() throws Exception {
        test("p", "pre");
    }

    @Alerts("0")
    void _p_section() throws Exception {
        test("p", "section");
    }

    @Alerts("0")
    void _p_summary() throws Exception {
        test("p", "summary");
    }

    @Alerts("0")
    void _p_ul() throws Exception {
        test("p", "ul");
    }

    @Alerts("0")
    void _p_xmp() throws Exception {
        test("p", "xmp");
    }

    @Alerts("1")
    void _ruby_blink() throws Exception {
        test("ruby", "blink");
    }

    @Alerts("1")
    void _select_hr() throws Exception {
        test("select", "hr");
    }

    @Alerts("1")
    void _select_optgroup() throws Exception {
        test("select", "optgroup");
    }

    @Alerts("1")
    void _select_option() throws Exception {
        test("select", "option");
    }

    @Alerts("1")
    void _select_script() throws Exception {
        test("select", "script");
    }

    @Alerts("1")
    void _select_template() throws Exception {
        test("select", "template");
    }

    @Alerts("1")
    void _table_caption() throws Exception {
        test("table", "caption");
    }

    @Alerts("1")
    void _table_col() throws Exception {
        test("table", "col");
    }

    @Alerts("1")
    void _table_colgroup() throws Exception {
        test("table", "colgroup");
    }

    @Alerts("1")
    void _table_form() throws Exception {
        test("table", "form");
    }

    @Alerts("1")
    void _table_script() throws Exception {
        test("table", "script");
    }

    @Alerts("1")
    void _table_style() throws Exception {
        test("table", "style");
    }

    @Alerts("1")
    void _table_tbody() throws Exception {
        test("table", "tbody");
    }

    @Alerts("1")
    void _table_td() throws Exception {
        test("table", "td");
    }

    @Alerts("1")
    void _table_template() throws Exception {
        test("table", "template");
    }

    @Alerts("1")
    void _table_tfoot() throws Exception {
        test("table", "tfoot");
    }

    @Alerts("1")
    void _table_th() throws Exception {
        test("table", "th");
    }

    @Alerts("1")
    void _table_thead() throws Exception {
        test("table", "thead");
    }

    @Alerts("1")
    void _table_tr() throws Exception {
        test("table", "tr");
    }

    @Alerts("1")
    void _tbody_form() throws Exception {
        test("tbody", "form");
    }

    @Alerts("1")
    void _tbody_script() throws Exception {
        test("tbody", "script");
    }

    @Alerts("1")
    void _tbody_style() throws Exception {
        test("tbody", "style");
    }

    @Alerts("1")
    void _tbody_td() throws Exception {
        test("tbody", "td");
    }

    @Alerts("1")
    void _tbody_template() throws Exception {
        test("tbody", "template");
    }

    @Alerts("1")
    void _tbody_th() throws Exception {
        test("tbody", "th");
    }

    @Alerts("1")
    void _tbody_tr() throws Exception {
        test("tbody", "tr");
    }

    @Alerts("1")
    void _tfoot_form() throws Exception {
        test("tfoot", "form");
    }

    @Alerts("1")
    void _tfoot_script() throws Exception {
        test("tfoot", "script");
    }

    @Alerts("1")
    void _tfoot_style() throws Exception {
        test("tfoot", "style");
    }

    @Alerts("1")
    void _tfoot_td() throws Exception {
        test("tfoot", "td");
    }

    @Alerts("1")
    void _tfoot_template() throws Exception {
        test("tfoot", "template");
    }

    @Alerts("1")
    void _tfoot_th() throws Exception {
        test("tfoot", "th");
    }

    @Alerts("1")
    void _tfoot_tr() throws Exception {
        test("tfoot", "tr");
    }

    @Alerts("1")
    void _thead_form() throws Exception {
        test("thead", "form");
    }

    @Alerts("1")
    void _thead_script() throws Exception {
        test("thead", "script");
    }

    @Alerts("1")
    void _thead_style() throws Exception {
        test("thead", "style");
    }

    @Alerts("1")
    void _thead_td() throws Exception {
        test("thead", "td");
    }

    @Alerts("1")
    void _thead_template() throws Exception {
        test("thead", "template");
    }

    @Alerts("1")
    void _thead_th() throws Exception {
        test("thead", "th");
    }

    @Alerts("1")
    void _thead_tr() throws Exception {
        test("thead", "tr");
    }

    @Alerts("1")
    void _tr_form() throws Exception {
        test("tr", "form");
    }

    @Alerts("1")
    void _tr_script() throws Exception {
        test("tr", "script");
    }

    @Alerts("1")
    void _tr_style() throws Exception {
        test("tr", "style");
    }

    @Alerts("1")
    void _tr_td() throws Exception {
        test("tr", "td");
    }

    @Alerts("1")
    void _tr_template() throws Exception {
        test("tr", "template");
    }

    @Alerts("1")
    void _tr_th() throws Exception {
        test("tr", "th");
    }

    @Alerts("0")
    void _template_caption() throws Exception {
        test("template", "tr");
    }

    @Alerts("0")
    void _template_col() throws Exception {
        test("template", "col");
    }

    @Alerts("0")
    void _template_colgroup() throws Exception {
        test("template", "colgroup");
    }

    @Alerts("0")
    void _template_frame() throws Exception {
        test("template", "frame");
    }

    @Alerts("0")
    void _template_tbody() throws Exception {
        test("template", "tbody");
    }

    @Alerts("0")
    void _template_td() throws Exception {
        test("template", "td");
    }

    @Alerts("0")
    void _template_tfoot() throws Exception {
        test("template", "tfoot");
    }

    @Alerts("0")
    void _template_th() throws Exception {
        test("template", "th");
    }

    @Alerts("0")
    void _template_thead() throws Exception {
        test("template", "thead");
    }

    @Alerts("0")
    void _template_tr() throws Exception {
        test("template", "tr");
    }
}
