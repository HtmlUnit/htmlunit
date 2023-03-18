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
package org.htmlunit.general.huge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import org.htmlunit.WebClient;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.DefaultElementFactory;
import org.htmlunit.junit.BrowserParameterizedRunner;
import org.htmlunit.junit.BrowserParameterizedRunner.Default;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for an element to close another element, which is defined in
 * {@link org.htmlunit.cyberneko.HTMLElements}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class ElementClosesElementTest extends WebDriverTestCase {

    private static int ServerRestartCount_;

    private static final List<String> parentZero = Arrays.asList("area", "base", "basefont", "bgsound", "br",
            "command", "col", "colgroup",
            "embed", "frame", "head", "hr", "iframe", "image", "img", "input", "keygen",
            "link", "meta", "noembed", "noframes", "noscript", "param", "plaintext",
            "script", "select", "source", "style",
            "table", "tbody", "template", "textarea", "tfoot", "thead", "title",
            "tr", "track", "wbr", "xmp");

    private static final List<String> childZero = Arrays.asList("body", "caption", "col", "colgroup",
            "frame", "frameset", "head", "html", "tbody", "td", "tfoot", "th", "thead", "tr");

    private static final List<String> svgChildZero = Arrays.asList("b", "big", "blockquote", "body", "br",
            "center", "code", "dd", "div", "dl", "dt", "em", "embed", "h1", "h2", "h3", "h4", "h5", "h6", "head",
            "hr", "i", "img", "li", "listing", "menu", "meta", "nobr", "ol", "p", "pre", "ruby", "s", "small",
            "span", "strike", "strong", "sub", "sup", "table", "tt", "u", "ul", "var");

    private static final String resultScript = "  var e = document.getElementById('outer');\n"
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
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();
        final List<String> strings = new LinkedList<String>(Arrays.asList(DefaultElementFactory.SUPPORTED_TAGS_));

        for (final String parent : strings) {
            for (final String child : strings) {
                list.add(new Object[] {parent, child});
            }
        }
        return list;
    }

    private void test(final String parent, final String child) throws Exception {
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
            html = "<frame id='outer'><" + child + "><frame>\n";
            bodyEnd = "</frameset></html>";
        }
        else if ("frameset".equals(parent)) {
            bodyStart = "";
            html = "<frameset id='outer'><" + child + "><frameset>\n";
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

        String pageHtml = "<html><head>\n"
                + "<title>-</title>\n"
                + "</head>\n"
                + bodyStart
                + html
                + bodyEnd
                + "</html>";

        if ("basefont".equals(parent)
                || "base".equals(parent)
                || "isindex".equals(parent)) {
            pageHtml = "<html><head>\n"
                + "<" + parent + " id='outer'><" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }
        else if ("head".equals(parent)) {
            pageHtml = "<html><head id='outer'><" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }
        else if ("title".equals(parent)) {
            pageHtml = "<html><head>\n"
                + "<title id='outer'><" + child + ">\n"
                + "</head><body>\n"
                + "</body></html>";
        }

        String expected = "1";
        if (getExpectedAlerts().length == 1) {
            expected = getExpectedAlerts()[0];
        }
        else {
            if (childZero.contains(child)) {
                expected = "0";
            }
            else if (parentZero.contains(parent)) {
                expected = "0";
            }
            else if ("html".equals(parent)) {
                expected = "2";
            }

            if ("frameset".equals(parent)) {
                expected = "1";
            }

            if ("svg".equals(parent)) {
                expected = "1";
                if (getBrowserVersion().isIE()) {
                    expected = "exception";
                }
                else if (svgChildZero.contains(child)) {
                    expected = "0";
                }
            }

            if ("command".equals(parent) && getBrowserVersion().isFirefox()) {
                expected = "1";
            }

            if (getBrowserVersion().isIE()) {
                if ("isindex".equals(parent) && useRealBrowser()) {
                    // ie is really strange here - the isindex tag is replaced
                    // by a form containing an input field - an this has no childs
                    // simulating this in 2023 is not worth the time
                    expected = "0";
                }
                if ("template".equals(parent) && !childZero.contains(child)) {
                    expected = "1";
                }
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

        final String result = (String) ((JavascriptExecutor) driver).executeScript(resultScript);
        assertEquals(expected, result);
    }

    /**
     * The parent element name.
     */
    @Parameter
    public String parent_;

    /**
     * The child element name.
     */
    @Parameter(1)
    public String child_;

    /**
     * Cleanup.
     */
    @After
    public void after() {
        parent_ = null;
        child_ = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void closes() throws Exception {
        test(parent_, child_);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _a_a() throws Exception {
        test("a", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _button_button() throws Exception {
        test("button", "button");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _colgroup_col() throws Exception {
        test("colgroup", "col");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _colgroup_template() throws Exception {
        test("colgroup", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_body() throws Exception {
        test("command", "body");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_caption() throws Exception {
        test("command", "caption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_col() throws Exception {
        test("command", "col");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_colgroup() throws Exception {
        test("command", "colgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_frame() throws Exception {
        test("command", "frame");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_frameset() throws Exception {
        test("command", "frameset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_head() throws Exception {
        test("command", "head");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_html() throws Exception {
        test("command", "html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_tbody() throws Exception {
        test("command", "tbody");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_td() throws Exception {
        test("command", "td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_tfoot() throws Exception {
        test("command", "tfoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_th() throws Exception {
        test("command", "th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_thead() throws Exception {
        test("command", "thead");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _command_tr() throws Exception {
        test("command", "tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _dd_dd() throws Exception {
        test("dd", "dd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _dd_dt() throws Exception {
        test("dd", "dt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _dt_dd() throws Exception {
        test("dt", "dd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _dt_dt() throws Exception {
        test("dt", "dt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _form_form() throws Exception {
        test("form", "form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _form_isindex() throws Exception {
        test("form", "isindex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _frameset_frame() throws Exception {
        test("frameset", "frame");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h1_h1() throws Exception {
        test("h1", "h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h1_h2() throws Exception {
        test("h1", "h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h1_h3() throws Exception {
        test("h1", "h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h1_h4() throws Exception {
        test("h1", "h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h1_h5() throws Exception {
        test("h1", "h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h1_h6() throws Exception {
        test("h1", "h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h2_h1() throws Exception {
        test("h2", "h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h2_h2() throws Exception {
        test("h2", "h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h2_h3() throws Exception {
        test("h2", "h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h2_h4() throws Exception {
        test("h2", "h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h2_h5() throws Exception {
        test("h2", "h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h2_h6() throws Exception {
        test("h2", "h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h3_h1() throws Exception {
        test("h3", "h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h3_h2() throws Exception {
        test("h3", "h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h3_h3() throws Exception {
        test("h3", "h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h3_h4() throws Exception {
        test("h3", "h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h3_h5() throws Exception {
        test("h3", "h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h3_h6() throws Exception {
        test("h3", "h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h4_h1() throws Exception {
        test("h4", "h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h4_h2() throws Exception {
        test("h4", "h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h4_h3() throws Exception {
        test("h4", "h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h4_h4() throws Exception {
        test("h4", "h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h4_h5() throws Exception {
        test("h4", "h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h4_h6() throws Exception {
        test("h4", "h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h5_h1() throws Exception {
        test("h5", "h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h5_h2() throws Exception {
        test("h5", "h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h5_h3() throws Exception {
        test("h5", "h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h5_h4() throws Exception {
        test("h5", "h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h5_h5() throws Exception {
        test("h5", "h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h5_h6() throws Exception {
        test("h5", "h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h6_h1() throws Exception {
        test("h6", "h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h6_h2() throws Exception {
        test("h6", "h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h6_h3() throws Exception {
        test("h6", "h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h6_h4() throws Exception {
        test("h6", "h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h6_h5() throws Exception {
        test("h6", "h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _h6_h6() throws Exception {
        test("h6", "h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_base() throws Exception {
        test("head", "base");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_basefont() throws Exception {
        test("head", "basefont");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_bgsound() throws Exception {
        test("head", "bgsound");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF = "0",
            FF_ESR = "0")
    @HtmlUnitNYI(FF = "1",
            FF_ESR = "1")
    public void _head_command() throws Exception {
        test("head", "command");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_link() throws Exception {
        test("head", "link");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_meta() throws Exception {
        test("head", "meta");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_noframes() throws Exception {
        test("head", "noframes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_noscript() throws Exception {
        test("head", "noscript");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_script() throws Exception {
        test("head", "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_style() throws Exception {
        test("head", "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _head_template() throws Exception {
        test("head", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _head_title() throws Exception {
        test("head", "title");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_body() throws Exception {
        test("html", "body");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_caption() throws Exception {
        test("html", "caption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_col() throws Exception {
        test("html", "col");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_colgroup() throws Exception {
        test("html", "colgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_frame() throws Exception {
        test("html", "frame");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_frameset() throws Exception {
        test("html", "frameset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_head() throws Exception {
        test("html", "head");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_html() throws Exception {
        test("html", "html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_tbody() throws Exception {
        test("html", "tbody");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_td() throws Exception {
        test("html", "td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_tfoot() throws Exception {
        test("html", "tfoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_th() throws Exception {
        test("html", "th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_thead() throws Exception {
        test("html", "thead");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void _html_tr() throws Exception {
        test("html", "tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE = "0")
    public void _isindex_frameset() throws Exception {
        test("isindex", "frameset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _li_li() throws Exception {
        test("li", "li");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _nobr_nobr() throws Exception {
        test("nobr", "nobr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _option_optgroup() throws Exception {
        test("option", "optgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _option_option() throws Exception {
        test("option", "option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_address() throws Exception {
        test("p", "address");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_article() throws Exception {
        test("p", "article");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_aside() throws Exception {
        test("p", "aside");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_blockquote() throws Exception {
        test("p", "blockquote");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_center() throws Exception {
        test("p", "center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_dd() throws Exception {
        test("p", "dd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_details() throws Exception {
        test("p", "details");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE = "1")
    public void _p_dialog() throws Exception {
        test("p", "dialog");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_dir() throws Exception {
        test("p", "dir");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_div() throws Exception {
        test("p", "div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_dl() throws Exception {
        test("p", "dl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_dt() throws Exception {
        test("p", "dt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_fieldset() throws Exception {
        test("p", "fieldset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_figcaption() throws Exception {
        test("p", "figcaption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_figure() throws Exception {
        test("p", "figure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_footer() throws Exception {
        test("p", "footer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_form() throws Exception {
        test("p", "form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_h1() throws Exception {
        test("p", "h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_h2() throws Exception {
        test("p", "h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_h3() throws Exception {
        test("p", "h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_h4() throws Exception {
        test("p", "h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_h5() throws Exception {
        test("p", "h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_h6() throws Exception {
        test("p", "h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_header() throws Exception {
        test("p", "header");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_hr() throws Exception {
        test("p", "hr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _p_isindex() throws Exception {
        test("p", "isindex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_li() throws Exception {
        test("p", "li");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_listing() throws Exception {
        test("p", "listing");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE = "1")
    public void _p_main() throws Exception {
        test("p", "main");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_menu() throws Exception {
        test("p", "menu");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_nav() throws Exception {
        test("p", "nav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_ol() throws Exception {
        test("p", "ol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_p() throws Exception {
        test("p", "p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_plaintext() throws Exception {
        test("p", "plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_pre() throws Exception {
        test("p", "pre");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_section() throws Exception {
        test("p", "section");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_summary() throws Exception {
        test("p", "summary");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_ul() throws Exception {
        test("p", "ul");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void _p_xmp() throws Exception {
        test("p", "xmp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _ruby_blink() throws Exception {
        test("ruby", "blink");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _select_optgroup() throws Exception {
        test("select", "optgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _select_option() throws Exception {
        test("select", "option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _select_script() throws Exception {
        test("select", "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _select_template() throws Exception {
        test("select", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_caption() throws Exception {
        test("table", "caption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_col() throws Exception {
        test("table", "col");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_colgroup() throws Exception {
        test("table", "colgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_form() throws Exception {
        test("table", "form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_script() throws Exception {
        test("table", "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_style() throws Exception {
        test("table", "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_tbody() throws Exception {
        test("table", "tbody");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_td() throws Exception {
        test("table", "td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _table_template() throws Exception {
        test("table", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_tfoot() throws Exception {
        test("table", "tfoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_th() throws Exception {
        test("table", "th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_thead() throws Exception {
        test("table", "thead");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _table_tr() throws Exception {
        test("table", "tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tbody_form() throws Exception {
        test("tbody", "form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tbody_script() throws Exception {
        test("tbody", "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tbody_style() throws Exception {
        test("tbody", "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tbody_td() throws Exception {
        test("tbody", "td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _tbody_template() throws Exception {
        test("tbody", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tbody_th() throws Exception {
        test("tbody", "th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tbody_tr() throws Exception {
        test("tbody", "tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tfoot_form() throws Exception {
        test("tfoot", "form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tfoot_script() throws Exception {
        test("tfoot", "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tfoot_style() throws Exception {
        test("tfoot", "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tfoot_td() throws Exception {
        test("tfoot", "td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _tfoot_template() throws Exception {
        test("tfoot", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tfoot_th() throws Exception {
        test("tfoot", "th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tfoot_tr() throws Exception {
        test("tfoot", "tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _thead_form() throws Exception {
        test("thead", "form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _thead_script() throws Exception {
        test("thead", "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _thead_style() throws Exception {
        test("thead", "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _thead_td() throws Exception {
        test("thead", "td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _thead_template() throws Exception {
        test("thead", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _thead_th() throws Exception {
        test("thead", "th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _thead_tr() throws Exception {
        test("thead", "tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tr_form() throws Exception {
        test("tr", "form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tr_script() throws Exception {
        test("tr", "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tr_style() throws Exception {
        test("tr", "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tr_td() throws Exception {
        test("tr", "td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void _tr_template() throws Exception {
        test("tr", "template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void _tr_th() throws Exception {
        test("tr", "th");
    }
}
