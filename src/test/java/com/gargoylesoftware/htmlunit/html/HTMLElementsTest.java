/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE10;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link org.cyberneko.html.HTMLElements}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLElementsTest extends WebDriverTestCase {

    private String elementClosesItself(final String tagName) {
        return "<html><head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  var e = document.getElementById('outer');\n"
                + "  alert(e == null ? e : e.childNodes.length);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<" + tagName + " id='outer'><" + tagName + ">\n"
                + "</body></html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_abbr() throws Exception {
        loadPageWithAlerts2(elementClosesItself("abbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_acronym() throws Exception {
        loadPageWithAlerts2(elementClosesItself("acronym"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_a() throws Exception {
        loadPageWithAlerts2(elementClosesItself("a"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_address() throws Exception {
        loadPageWithAlerts2(elementClosesItself("address"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_applet() throws Exception {
        loadPageWithAlerts2(elementClosesItself("applet"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_area() throws Exception {
        loadPageWithAlerts2(elementClosesItself("area"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_audio() throws Exception {
        loadPageWithAlerts2(elementClosesItself("audio"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_bgsound() throws Exception {
        loadPageWithAlerts2(elementClosesItself("bgsound"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_base() throws Exception {
        loadPageWithAlerts2(elementClosesItself("base"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_basefont() throws Exception {
        loadPageWithAlerts2(elementClosesItself("basefont"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_bdo() throws Exception {
        loadPageWithAlerts2(elementClosesItself("bdo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_big() throws Exception {
        loadPageWithAlerts2(elementClosesItself("big"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_blink() throws Exception {
        loadPageWithAlerts2(elementClosesItself("blink"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_blockquote() throws Exception {
        loadPageWithAlerts2(elementClosesItself("blockquote"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    public void elementClosesItself_body() throws Exception {
        loadPageWithAlerts2(elementClosesItself("body"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_b() throws Exception {
        loadPageWithAlerts2(elementClosesItself("b"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_br() throws Exception {
        loadPageWithAlerts2(elementClosesItself("br"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_button() throws Exception {
        loadPageWithAlerts2(elementClosesItself("button"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_canvas() throws Exception {
        loadPageWithAlerts2(elementClosesItself("canvas"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_caption() throws Exception {
        loadPageWithAlerts2(elementClosesItself("caption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_center() throws Exception {
        loadPageWithAlerts2(elementClosesItself("center"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_cite() throws Exception {
        loadPageWithAlerts2(elementClosesItself("cite"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_code() throws Exception {
        loadPageWithAlerts2(elementClosesItself("code"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_dfn() throws Exception {
        loadPageWithAlerts2(elementClosesItself("dfn"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_dd() throws Exception {
        loadPageWithAlerts2(elementClosesItself("dd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_del() throws Exception {
        loadPageWithAlerts2(elementClosesItself("del"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_dir() throws Exception {
        loadPageWithAlerts2(elementClosesItself("dir"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_div() throws Exception {
        loadPageWithAlerts2(elementClosesItself("div"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_dl() throws Exception {
        loadPageWithAlerts2(elementClosesItself("dl"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_dt() throws Exception {
        loadPageWithAlerts2(elementClosesItself("dt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_embed() throws Exception {
        loadPageWithAlerts2(elementClosesItself("embed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_em() throws Exception {
        loadPageWithAlerts2(elementClosesItself("em"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_fieldset() throws Exception {
        loadPageWithAlerts2(elementClosesItself("fieldset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_font() throws Exception {
        loadPageWithAlerts2(elementClosesItself("font"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    @NotYetImplemented(IE8)
    public void elementClosesItself_form() throws Exception {
        loadPageWithAlerts2(elementClosesItself("form"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(FF)
    public void elementClosesItself_frame() throws Exception {
        loadPageWithAlerts2(elementClosesItself("frame"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented
    public void elementClosesItself_frameset() throws Exception {
        loadPageWithAlerts2(elementClosesItself("frameset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
    @NotYetImplemented(IE8)
    public void elementClosesItself_h1() throws Exception {
        loadPageWithAlerts2(elementClosesItself("h1"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
    @NotYetImplemented(IE8)
    public void elementClosesItself_h2() throws Exception {
        loadPageWithAlerts2(elementClosesItself("h2"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
    @NotYetImplemented(IE8)
    public void elementClosesItself_h3() throws Exception {
        loadPageWithAlerts2(elementClosesItself("h3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
    @NotYetImplemented(IE8)
    public void elementClosesItself_h4() throws Exception {
        loadPageWithAlerts2(elementClosesItself("h4"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
    @NotYetImplemented(IE8)
    public void elementClosesItself_h5() throws Exception {
        loadPageWithAlerts2(elementClosesItself("h5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
    @NotYetImplemented(IE8)
    public void elementClosesItself_h6() throws Exception {
        loadPageWithAlerts2(elementClosesItself("h6"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void elementClosesItself_head() throws Exception {
        loadPageWithAlerts2(elementClosesItself("head"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_hr() throws Exception {
        loadPageWithAlerts2(elementClosesItself("hr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE8 = "null")
    @NotYetImplemented(FF)
    public void elementClosesItself_html() throws Exception {
        loadPageWithAlerts2(elementClosesItself("html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_iframe() throws Exception {
        loadPageWithAlerts2(elementClosesItself("iframe"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_q() throws Exception {
        loadPageWithAlerts2(elementClosesItself("q"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_image() throws Exception {
        loadPageWithAlerts2(elementClosesItself("image"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_img() throws Exception {
        loadPageWithAlerts2(elementClosesItself("img"));
    }
    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_ins() throws Exception {
        loadPageWithAlerts2(elementClosesItself("ins"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_isindex() throws Exception {
        loadPageWithAlerts2(elementClosesItself("isindex"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_i() throws Exception {
        loadPageWithAlerts2(elementClosesItself("i"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_kbd() throws Exception {
        loadPageWithAlerts2(elementClosesItself("kbd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_label() throws Exception {
        loadPageWithAlerts2(elementClosesItself("label"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_legend() throws Exception {
        loadPageWithAlerts2(elementClosesItself("legend"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_listing() throws Exception {
        loadPageWithAlerts2(elementClosesItself("listing"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_li() throws Exception {
        loadPageWithAlerts2(elementClosesItself("li"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_link() throws Exception {
        loadPageWithAlerts2(elementClosesItself("link"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_map() throws Exception {
        loadPageWithAlerts2(elementClosesItself("map"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_marquee() throws Exception {
        loadPageWithAlerts2(elementClosesItself("marquee"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_menu() throws Exception {
        loadPageWithAlerts2(elementClosesItself("menu"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_meta() throws Exception {
        loadPageWithAlerts2(elementClosesItself("meta"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_meter() throws Exception {
        loadPageWithAlerts2(elementClosesItself("meter"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_multicol() throws Exception {
        loadPageWithAlerts2(elementClosesItself("multicol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_nobr() throws Exception {
        loadPageWithAlerts2(elementClosesItself("nobr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_noembed() throws Exception {
        loadPageWithAlerts2(elementClosesItself("noembed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_noframes() throws Exception {
        loadPageWithAlerts2(elementClosesItself("noframes"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    public void elementClosesItself_noscript() throws Exception {
        loadPageWithAlerts2(elementClosesItself("noscript"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_object() throws Exception {
        loadPageWithAlerts2(elementClosesItself("object"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_ol() throws Exception {
        loadPageWithAlerts2(elementClosesItself("ol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented
    public void elementClosesItself_optgroup() throws Exception {
        loadPageWithAlerts2(elementClosesItself("optgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_option() throws Exception {
        loadPageWithAlerts2(elementClosesItself("option"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_p() throws Exception {
        loadPageWithAlerts2(elementClosesItself("p"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_param() throws Exception {
        loadPageWithAlerts2(elementClosesItself("param"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_plaintext() throws Exception {
        loadPageWithAlerts2(elementClosesItself("plaintext"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_pre() throws Exception {
        loadPageWithAlerts2(elementClosesItself("pre"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_progress() throws Exception {
        loadPageWithAlerts2(elementClosesItself("progress"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented({ FF, IE10 })
    public void elementClosesItself_ruby() throws Exception {
        loadPageWithAlerts2(elementClosesItself("ruby"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_rp() throws Exception {
        loadPageWithAlerts2(elementClosesItself("rp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_rt() throws Exception {
        loadPageWithAlerts2(elementClosesItself("rt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_s() throws Exception {
        loadPageWithAlerts2(elementClosesItself("s"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_samp() throws Exception {
        loadPageWithAlerts2(elementClosesItself("samp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented
    public void elementClosesItself_script() throws Exception {
        loadPageWithAlerts2(elementClosesItself("script"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_select() throws Exception {
        loadPageWithAlerts2(elementClosesItself("select"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_small() throws Exception {
        loadPageWithAlerts2(elementClosesItself("small"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @NotYetImplemented
    public void elementClosesItself_source() throws Exception {
        loadPageWithAlerts2(elementClosesItself("source"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(FF)
    public void elementClosesItself_spacer() throws Exception {
        loadPageWithAlerts2(elementClosesItself("spacer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_span() throws Exception {
        loadPageWithAlerts2(elementClosesItself("span"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_strike() throws Exception {
        loadPageWithAlerts2(elementClosesItself("strike"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_strong() throws Exception {
        loadPageWithAlerts2(elementClosesItself("strong"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_style() throws Exception {
        loadPageWithAlerts2(elementClosesItself("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_sub() throws Exception {
        loadPageWithAlerts2(elementClosesItself("sub"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_sup() throws Exception {
        loadPageWithAlerts2(elementClosesItself("sup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE8 = "1")
    @NotYetImplemented(FF)
    public void elementClosesItself_table() throws Exception {
        loadPageWithAlerts2(elementClosesItself("table"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_col() throws Exception {
        loadPageWithAlerts2(elementClosesItself("col"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_colgroup() throws Exception {
        loadPageWithAlerts2(elementClosesItself("colgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_tbody() throws Exception {
        loadPageWithAlerts2(elementClosesItself("tbody"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_td() throws Exception {
        loadPageWithAlerts2(elementClosesItself("td"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_th() throws Exception {
        loadPageWithAlerts2(elementClosesItself("th"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_tr() throws Exception {
        loadPageWithAlerts2(elementClosesItself("tr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_textarea() throws Exception {
        loadPageWithAlerts2(elementClosesItself("textarea"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_tfoot() throws Exception {
        loadPageWithAlerts2(elementClosesItself("tfoot"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_thead() throws Exception {
        loadPageWithAlerts2(elementClosesItself("thead"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_tt() throws Exception {
        loadPageWithAlerts2(elementClosesItself("tt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(FF)
    public void elementClosesItself_title() throws Exception {
        loadPageWithAlerts2(elementClosesItself("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_u() throws Exception {
        loadPageWithAlerts2(elementClosesItself("u"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_ul() throws Exception {
        loadPageWithAlerts2(elementClosesItself("ul"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_var() throws Exception {
        loadPageWithAlerts2(elementClosesItself("var"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE8 = "0")
    @NotYetImplemented(IE8)
    public void elementClosesItself_video() throws Exception {
        loadPageWithAlerts2(elementClosesItself("video"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void elementClosesItself_wbr() throws Exception {
        loadPageWithAlerts2(elementClosesItself("wbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void elementClosesItself_xmp() throws Exception {
        loadPageWithAlerts2(elementClosesItself("xmp"));
    }
}
