/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests the result of <code>element.childNodes.length</code>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class ElementChildNodesTest extends WebDriverTestCase {

    private String test(final String tagName) {
        return "<html><head><title>test_getChildNodes</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  for (var i = 1; i <= 6; i++) {\n"
                + "    alert(document.getElementById('p' + i).childNodes.length);\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<p id='p1'> <" + tagName + "></" + tagName + "> </p>\n"
                + "<p id='p2'><" + tagName + "></" + tagName + "> </p>\n"
                + "<p id='p3'> <" + tagName + "></" + tagName + "></p>\n"
                + "<p id='p4'> <" + tagName + ">something</" + tagName + "> </p>\n"
                + "<p id='p5'><" + tagName + ">something</" + tagName + "> </p>\n"
                + "<p id='p6'> <" + tagName + ">something</" + tagName + "></p>\n"
                + "</body></html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_abbr() throws Exception {
        loadPageWithAlerts2(test("abbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_acronym() throws Exception {
        loadPageWithAlerts2(test("acronym"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_a() throws Exception {
        loadPageWithAlerts2(test("a"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_address() throws Exception {
        loadPageWithAlerts2(test("address"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_applet() throws Exception {
        loadPageWithAlerts2(test("applet"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_area() throws Exception {
        loadPageWithAlerts2(test("area"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_article() throws Exception {
        loadPageWithAlerts2(test("article"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_aside() throws Exception {
        loadPageWithAlerts2(test("aside"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_audio() throws Exception {
        loadPageWithAlerts2(test("audio"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_bgsound() throws Exception {
        loadPageWithAlerts2(test("bgsound"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "1", "1", "1", "2", "2", "2" })
    public void childNodes_base() throws Exception {
        loadPageWithAlerts2(test("base"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    @NotYetImplemented({ FF, IE11, CHROME })
    public void childNodes_basefont() throws Exception {
        loadPageWithAlerts2(test("basefont"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_bdi() throws Exception {
        loadPageWithAlerts2(test("bdi"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_bdo() throws Exception {
        loadPageWithAlerts2(test("bdo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_big() throws Exception {
        loadPageWithAlerts2(test("big"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_blink() throws Exception {
        loadPageWithAlerts2(test("blink"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_blockquote() throws Exception {
        loadPageWithAlerts2(test("blockquote"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "0", "0", "0", "1", "1", "1" })
    @NotYetImplemented({ FF, IE11, CHROME })
    public void childNodes_body() throws Exception {
        loadPageWithAlerts2(test("body"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_b() throws Exception {
        loadPageWithAlerts2(test("b"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "4", "3", "3", "5", "4", "4" },
            IE8 = { "2", "2", "2", "3", "3", "3" })
    @NotYetImplemented
    public void childNodes_br() throws Exception {
        loadPageWithAlerts2(test("br"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_button() throws Exception {
        loadPageWithAlerts2(test("button"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_canvas() throws Exception {
        loadPageWithAlerts2(test("canvas"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_caption() throws Exception {
        loadPageWithAlerts2(test("caption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_center() throws Exception {
        loadPageWithAlerts2(test("center"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_cite() throws Exception {
        loadPageWithAlerts2(test("cite"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_code() throws Exception {
        loadPageWithAlerts2(test("code"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            CHROME = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    @NotYetImplemented({ CHROME, IE8 })
    public void childNodes_command() throws Exception {
        loadPageWithAlerts2(test("command"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_datalist() throws Exception {
        loadPageWithAlerts2(test("datalist"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_dfn() throws Exception {
        loadPageWithAlerts2(test("dfn"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dd() throws Exception {
        loadPageWithAlerts2(test("dd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_del() throws Exception {
        loadPageWithAlerts2(test("del"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_details() throws Exception {
        loadPageWithAlerts2(test("details"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_dialog() throws Exception {
        loadPageWithAlerts2(test("dialog"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dir() throws Exception {
        loadPageWithAlerts2(test("dir"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_div() throws Exception {
        loadPageWithAlerts2(test("div"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dl() throws Exception {
        loadPageWithAlerts2(test("dl"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dt() throws Exception {
        loadPageWithAlerts2(test("dt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "3", "3", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_embed() throws Exception {
        loadPageWithAlerts2(test("embed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_em() throws Exception {
        loadPageWithAlerts2(test("em"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_fieldset() throws Exception {
        loadPageWithAlerts2(test("fieldset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_figcaption() throws Exception {
        loadPageWithAlerts2(test("figcaption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_figure() throws Exception {
        loadPageWithAlerts2(test("figure"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_font() throws Exception {
        loadPageWithAlerts2(test("font"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_form() throws Exception {
        loadPageWithAlerts2(test("form"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_footer() throws Exception {
        loadPageWithAlerts2(test("footer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_frame() throws Exception {
        loadPageWithAlerts2(test("frame"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "1", "1", "3", "3", "2" })
    @NotYetImplemented
    public void childNodes_frameset() throws Exception {
        loadPageWithAlerts2(test("frameset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h1() throws Exception {
        loadPageWithAlerts2(test("h1"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h2() throws Exception {
        loadPageWithAlerts2(test("h2"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h3() throws Exception {
        loadPageWithAlerts2(test("h3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h4() throws Exception {
        loadPageWithAlerts2(test("h4"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h5() throws Exception {
        loadPageWithAlerts2(test("h5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h6() throws Exception {
        loadPageWithAlerts2(test("h6"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "0", "0", "0", "1", "1", "1" })
    public void childNodes_head() throws Exception {
        loadPageWithAlerts2(test("head"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_header() throws Exception {
        loadPageWithAlerts2(test("header"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_hr() throws Exception {
        loadPageWithAlerts2(test("hr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "0", "0", "0", "1", "1", "1" })
    public void childNodes_html() throws Exception {
        loadPageWithAlerts2(test("html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_iframe() throws Exception {
        loadPageWithAlerts2(test("iframe"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "3", "3", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_image() throws Exception {
        loadPageWithAlerts2(test("image"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "3", "3", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_img() throws Exception {
        loadPageWithAlerts2(test("img"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_ins() throws Exception {
        loadPageWithAlerts2(test("ins"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            CHROME = { "3", "2", "2", "3", "2", "2" },
            FF31 = { "1", "0", "1", "1", "0", "1" },
            IE11 = { "1", "0", "1", "1", "0", "1" })
    @NotYetImplemented({ FF, IE11 })
    public void childNodes_isindex() throws Exception {
        loadPageWithAlerts2(test("isindex"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_i() throws Exception {
        loadPageWithAlerts2(test("i"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_kbd() throws Exception {
        loadPageWithAlerts2(test("kbd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_keygen() throws Exception {
        loadPageWithAlerts2(test("keygen"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_label() throws Exception {
        loadPageWithAlerts2(test("label"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_layer() throws Exception {
        loadPageWithAlerts2(test("layer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_legend() throws Exception {
        loadPageWithAlerts2(test("legend"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_listing() throws Exception {
        loadPageWithAlerts2(test("listing"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_li() throws Exception {
        loadPageWithAlerts2(test("li"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_link() throws Exception {
        loadPageWithAlerts2(test("link"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE11 = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented({FF, CHROME, IE8 })
    public void childNodes_main() throws Exception {
        loadPageWithAlerts2(test("main"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_map() throws Exception {
        loadPageWithAlerts2(test("map"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_marquee() throws Exception {
        loadPageWithAlerts2(test("marquee"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_mark() throws Exception {
        loadPageWithAlerts2(test("mark"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_menu() throws Exception {
        loadPageWithAlerts2(test("menu"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_menuitem() throws Exception {
        loadPageWithAlerts2(test("menuitem"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_meta() throws Exception {
        loadPageWithAlerts2(test("meta"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_meter() throws Exception {
        loadPageWithAlerts2(test("meter"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_multicol() throws Exception {
        loadPageWithAlerts2(test("multicol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_nav() throws Exception {
        loadPageWithAlerts2(test("nav"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_nextid() throws Exception {
        loadPageWithAlerts2(test("nextid"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_nobr() throws Exception {
        loadPageWithAlerts2(test("nobr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_noembed() throws Exception {
        loadPageWithAlerts2(test("noembed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_noframes() throws Exception {
        loadPageWithAlerts2(test("noframes"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_nolayer() throws Exception {
        loadPageWithAlerts2(test("nolayer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_noscript() throws Exception {
        loadPageWithAlerts2(test("noscript"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_object() throws Exception {
        loadPageWithAlerts2(test("object"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_ol() throws Exception {
        loadPageWithAlerts2(test("ol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_optgroup() throws Exception {
        loadPageWithAlerts2(test("optgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_option() throws Exception {
        loadPageWithAlerts2(test("option"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_output() throws Exception {
        loadPageWithAlerts2(test("output"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_p() throws Exception {
        loadPageWithAlerts2(test("p"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_param() throws Exception {
        loadPageWithAlerts2(test("param"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1" },
            IE8 = { "0" })
    @NotYetImplemented
    public void childNodes_plaintext() throws Exception {
        loadPageWithAlerts2(test("plaintext"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_pre() throws Exception {
        loadPageWithAlerts2(test("pre"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_progress() throws Exception {
        loadPageWithAlerts2(test("progress"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_q() throws Exception {
        loadPageWithAlerts2(test("q"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_ruby() throws Exception {
        loadPageWithAlerts2(test("ruby"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented()
    public void childNodes_rt() throws Exception {
        loadPageWithAlerts2(test("rt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented()
    public void childNodes_rp() throws Exception {
        loadPageWithAlerts2(test("rp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_s() throws Exception {
        loadPageWithAlerts2(test("s"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_samp() throws Exception {
        loadPageWithAlerts2(test("samp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    @NotYetImplemented
    public void childNodes_script() throws Exception {
        loadPageWithAlerts2(test("script"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_section() throws Exception {
        loadPageWithAlerts2(test("section"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_select() throws Exception {
        loadPageWithAlerts2(test("select"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_small() throws Exception {
        loadPageWithAlerts2(test("small"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_source() throws Exception {
        loadPageWithAlerts2(test("source"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_spacer() throws Exception {
        loadPageWithAlerts2(test("spacer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_span() throws Exception {
        loadPageWithAlerts2(test("span"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_strike() throws Exception {
        loadPageWithAlerts2(test("strike"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_strong() throws Exception {
        loadPageWithAlerts2(test("strong"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_style() throws Exception {
        loadPageWithAlerts2(test("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_sub() throws Exception {
        loadPageWithAlerts2(test("sub"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_summary() throws Exception {
        loadPageWithAlerts2(test("summary"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_sup() throws Exception {
        loadPageWithAlerts2(test("sup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "3", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    @NotYetImplemented({ FF, IE11, CHROME })
    public void childNodes_table() throws Exception {
        loadPageWithAlerts2(test("table"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_col() throws Exception {
        loadPageWithAlerts2(test("col"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_colgroup() throws Exception {
        loadPageWithAlerts2(test("colgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_tbody() throws Exception {
        loadPageWithAlerts2(test("tbody"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_td() throws Exception {
        loadPageWithAlerts2(test("td"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_th() throws Exception {
        loadPageWithAlerts2(test("th"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_tr() throws Exception {
        loadPageWithAlerts2(test("tr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_track() throws Exception {
        loadPageWithAlerts2(test("track"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_textarea() throws Exception {
        loadPageWithAlerts2(test("textarea"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_tfoot() throws Exception {
        loadPageWithAlerts2(test("tfoot"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_thead() throws Exception {
        loadPageWithAlerts2(test("thead"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_tt() throws Exception {
        loadPageWithAlerts2(test("tt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_time() throws Exception {
        loadPageWithAlerts2(test("time"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    @NotYetImplemented({ FF, IE11, CHROME })
    public void childNodes_title() throws Exception {
        loadPageWithAlerts2(test("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_u() throws Exception {
        loadPageWithAlerts2(test("u"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_ul() throws Exception {
        loadPageWithAlerts2(test("ul"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_var() throws Exception {
        loadPageWithAlerts2(test("var"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_video() throws Exception {
        loadPageWithAlerts2(test("video"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_wbr() throws Exception {
        loadPageWithAlerts2(test("wbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_xmp() throws Exception {
        loadPageWithAlerts2(test("xmp"));
    }

}
