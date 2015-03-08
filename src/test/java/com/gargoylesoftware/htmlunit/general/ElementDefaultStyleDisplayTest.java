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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests the result of the default 'display' style of an element.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ElementDefaultStyleDisplayTest extends WebDriverTestCase {

    private String test(final String tagName) throws Exception {
        return HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var e = document.getElementById('myId');\n"
            + "      var cs = window.getComputedStyle ? window.getComputedStyle(e, null) : e.currentStyle;\n"
            + "      var disp = cs ? cs.display : null;\n"
            + "      alert(disp);\n"
            + "    } catch (e) {alert('exception')}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <" + tagName + " id='myId'></" + tagName + ">\n"
            + "</body></html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void abbr() throws Exception {
        loadPageWithAlerts2(test("abbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void acronym() throws Exception {
        loadPageWithAlerts2(test("acronym"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void a() throws Exception {
        loadPageWithAlerts2(test("a"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void address() throws Exception {
        loadPageWithAlerts2(test("address"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline-block",
            CHROME = "inline")
    @NotYetImplemented
    public void applet() throws Exception {
        loadPageWithAlerts2(test("applet"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "none",
            CHROME = "inline")
    @NotYetImplemented({ IE, CHROME })
    public void area() throws Exception {
        loadPageWithAlerts2(test("area"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void article() throws Exception {
        loadPageWithAlerts2(test("article"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void aside() throws Exception {
        loadPageWithAlerts2(test("aside"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "none",
            CHROME = "inline")
    @NotYetImplemented({ IE8, CHROME })
    public void audio() throws Exception {
        loadPageWithAlerts2(test("audio"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    @NotYetImplemented({ FF, IE })
    public void bgsound() throws Exception {
        loadPageWithAlerts2(test("bgsound"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "none",
            CHROME = "inline")
    @NotYetImplemented
    public void base() throws Exception {
        loadPageWithAlerts2(test("base"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "none",
            CHROME = "inline")
    @NotYetImplemented
    public void basefont() throws Exception {
        loadPageWithAlerts2(test("basefont"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void bdi() throws Exception {
        loadPageWithAlerts2(test("bdi"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void bdo() throws Exception {
        loadPageWithAlerts2(test("bdo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void big() throws Exception {
        loadPageWithAlerts2(test("big"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void blink() throws Exception {
        loadPageWithAlerts2(test("blink"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void blockquote() throws Exception {
        loadPageWithAlerts2(test("blockquote"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void body() throws Exception {
        loadPageWithAlerts2(test("body"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void b() throws Exception {
        loadPageWithAlerts2(test("b"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void br() throws Exception {
        loadPageWithAlerts2(test("br"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline-block")
    public void button() throws Exception {
        loadPageWithAlerts2(test("button"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void canvas() throws Exception {
        loadPageWithAlerts2(test("canvas"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void caption() throws Exception {
        loadPageWithAlerts2(test("caption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void center() throws Exception {
        loadPageWithAlerts2(test("center"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void cite() throws Exception {
        loadPageWithAlerts2(test("cite"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void code() throws Exception {
        loadPageWithAlerts2(test("code"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void command() throws Exception {
        loadPageWithAlerts2(test("command"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented(IE8)
    public void datalist() throws Exception {
        loadPageWithAlerts2(test("datalist"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void dfn() throws Exception {
        loadPageWithAlerts2(test("dfn"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void dd() throws Exception {
        loadPageWithAlerts2(test("dd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void del() throws Exception {
        loadPageWithAlerts2(test("del"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline",
            CHROME = "block")
    public void details() throws Exception {
        loadPageWithAlerts2(test("details"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline",
            CHROME = "none")
    @NotYetImplemented(CHROME)
    public void dialog() throws Exception {
        loadPageWithAlerts2(test("dialog"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void dir() throws Exception {
        loadPageWithAlerts2(test("dir"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void div() throws Exception {
        loadPageWithAlerts2(test("div"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void dl() throws Exception {
        loadPageWithAlerts2(test("dl"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void dt() throws Exception {
        loadPageWithAlerts2(test("dt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void embed() throws Exception {
        loadPageWithAlerts2(test("embed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void em() throws Exception {
        loadPageWithAlerts2(test("em"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void fieldset() throws Exception {
        loadPageWithAlerts2(test("fieldset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void figcaption() throws Exception {
        loadPageWithAlerts2(test("figcaption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void figure() throws Exception {
        loadPageWithAlerts2(test("figure"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void font() throws Exception {
        loadPageWithAlerts2(test("font"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void form() throws Exception {
        loadPageWithAlerts2(test("form"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void footer() throws Exception {
        loadPageWithAlerts2(test("footer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented
    public void frame() throws Exception {
        loadPageWithAlerts2(test("frame"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented
    public void frameset() throws Exception {
        loadPageWithAlerts2(test("frameset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void head() throws Exception {
        loadPageWithAlerts2(test("head"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void header() throws Exception {
        loadPageWithAlerts2(test("header"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void h1() throws Exception {
        loadPageWithAlerts2(test("h1"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void h2() throws Exception {
        loadPageWithAlerts2(test("h2"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void h3() throws Exception {
        loadPageWithAlerts2(test("h3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void h4() throws Exception {
        loadPageWithAlerts2(test("h4"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void h5() throws Exception {
        loadPageWithAlerts2(test("h5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void h6() throws Exception {
        loadPageWithAlerts2(test("h6"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void hr() throws Exception {
        loadPageWithAlerts2(test("hr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented
    public void html() throws Exception {
        loadPageWithAlerts2(test("html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void iframe() throws Exception {
        loadPageWithAlerts2(test("iframe"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void q() throws Exception {
        loadPageWithAlerts2(test("q"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void img() throws Exception {
        loadPageWithAlerts2(test("img"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void image() throws Exception {
        loadPageWithAlerts2(test("image"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void ins() throws Exception {
        loadPageWithAlerts2(test("ins"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    @NotYetImplemented(IE)
    public void isindex() throws Exception {
        loadPageWithAlerts2(test("isindex"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void i() throws Exception {
        loadPageWithAlerts2(test("i"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void kbd() throws Exception {
        loadPageWithAlerts2(test("kbd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline-block")
    @NotYetImplemented({ IE, CHROME })
    public void keygen() throws Exception {
        loadPageWithAlerts2(test("keygen"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void label() throws Exception {
        loadPageWithAlerts2(test("label"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline",
            CHROME = "block")
    public void layer() throws Exception {
        loadPageWithAlerts2(test("layer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE)
    public void legend() throws Exception {
        loadPageWithAlerts2(test("legend"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented
    public void listing() throws Exception {
        loadPageWithAlerts2(test("listing"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("list-item")
    public void li() throws Exception {
        loadPageWithAlerts2(test("li"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented
    public void link() throws Exception {
        loadPageWithAlerts2(test("link"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE)
    public void main() throws Exception {
        loadPageWithAlerts2(test("main"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void map() throws Exception {
        loadPageWithAlerts2(test("map"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void mark() throws Exception {
        loadPageWithAlerts2(test("mark"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline-block")
    @NotYetImplemented
    public void marquee() throws Exception {
        loadPageWithAlerts2(test("marquee"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void menu() throws Exception {
        loadPageWithAlerts2(test("menu"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    @NotYetImplemented(FF)
    public void menuitem() throws Exception {
        loadPageWithAlerts2(test("menuitem"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented
    public void meta() throws Exception {
        loadPageWithAlerts2(test("meta"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline-block")
    @NotYetImplemented(IE)
    public void meter() throws Exception {
        loadPageWithAlerts2(test("meter"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "block",
            CHROME = "inline")
    @NotYetImplemented({ IE, FF })
    public void multicol() throws Exception {
        loadPageWithAlerts2(test("multicol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void nav() throws Exception {
        loadPageWithAlerts2(test("nav"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    @NotYetImplemented(IE)
    public void nextid() throws Exception {
        loadPageWithAlerts2(test("nextid"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void nobr() throws Exception {
        loadPageWithAlerts2(test("nobr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "none",
            CHROME = "inline")
    @NotYetImplemented
    public void noembed() throws Exception {
        loadPageWithAlerts2(test("noembed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented
    public void noframes() throws Exception {
        loadPageWithAlerts2(test("noframes"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    @NotYetImplemented(CHROME)
    public void nolayer() throws Exception {
        loadPageWithAlerts2(test("nolayer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "none",
            CHROME = "inline")
    @NotYetImplemented({ IE8, CHROME })
    public void noscript() throws Exception {
        loadPageWithAlerts2(test("noscript"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void object() throws Exception {
        loadPageWithAlerts2(test("object"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void ol() throws Exception {
        loadPageWithAlerts2(test("ol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented
    public void optgroup() throws Exception {
        loadPageWithAlerts2(test("optgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented
    public void option() throws Exception {
        loadPageWithAlerts2(test("option"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void output() throws Exception {
        loadPageWithAlerts2(test("output"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void p() throws Exception {
        loadPageWithAlerts2(test("p"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented(IE)
    public void param() throws Exception {
        loadPageWithAlerts2(test("param"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented
    public void plaintext() throws Exception {
        loadPageWithAlerts2(test("plaintext"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void pre() throws Exception {
        loadPageWithAlerts2(test("pre"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline-block")
    @NotYetImplemented(IE)
    public void progress() throws Exception {
        loadPageWithAlerts2(test("progress"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void rp() throws Exception {
        loadPageWithAlerts2(test("rp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    @NotYetImplemented(IE)
    public void rt() throws Exception {
        loadPageWithAlerts2(test("rt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    @NotYetImplemented(IE)
    public void ruby() throws Exception {
        loadPageWithAlerts2(test("ruby"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void s() throws Exception {
        loadPageWithAlerts2(test("s"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void samp() throws Exception {
        loadPageWithAlerts2(test("samp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented(IE8)
    public void script() throws Exception {
        loadPageWithAlerts2(test("script"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented(IE8)
    public void section() throws Exception {
        loadPageWithAlerts2(test("section"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline-block")
    @NotYetImplemented(CHROME)
    public void select() throws Exception {
        loadPageWithAlerts2(test("select"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void small() throws Exception {
        loadPageWithAlerts2(test("small"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void source() throws Exception {
        loadPageWithAlerts2(test("source"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void span() throws Exception {
        loadPageWithAlerts2(test("span"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void strike() throws Exception {
        loadPageWithAlerts2(test("strike"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void strong() throws Exception {
        loadPageWithAlerts2(test("strong"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented
    public void style() throws Exception {
        loadPageWithAlerts2(test("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void sub() throws Exception {
        loadPageWithAlerts2(test("sub"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline",
            CHROME = "block")
    public void summary() throws Exception {
        loadPageWithAlerts2(test("summary"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void sup() throws Exception {
        loadPageWithAlerts2(test("sup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("table")
    public void table() throws Exception {
        loadPageWithAlerts2(test("table"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void col() throws Exception {
        loadPageWithAlerts2(test("col"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void colgroup() throws Exception {
        loadPageWithAlerts2(test("colgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void tbody() throws Exception {
        loadPageWithAlerts2(test("tbody"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void td() throws Exception {
        loadPageWithAlerts2(test("td"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void th() throws Exception {
        loadPageWithAlerts2(test("th"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void tr() throws Exception {
        loadPageWithAlerts2(test("tr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline",
            CHROME = "inline-block")
    @NotYetImplemented({ IE, CHROME })
    public void textarea() throws Exception {
        loadPageWithAlerts2(test("textarea"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void tfoot() throws Exception {
        loadPageWithAlerts2(test("tfoot"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void thead() throws Exception {
        loadPageWithAlerts2(test("thead"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void tt() throws Exception {
        loadPageWithAlerts2(test("tt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void time() throws Exception {
        loadPageWithAlerts2(test("time"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    @NotYetImplemented
    public void title() throws Exception {
        loadPageWithAlerts2(test("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void track() throws Exception {
        loadPageWithAlerts2(test("track"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void u() throws Exception {
        loadPageWithAlerts2(test("u"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    public void ul() throws Exception {
        loadPageWithAlerts2(test("ul"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void var() throws Exception {
        loadPageWithAlerts2(test("var"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void video() throws Exception {
        loadPageWithAlerts2(test("video"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void wbr() throws Exception {
        loadPageWithAlerts2(test("wbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("block")
    @NotYetImplemented
    public void xmp() throws Exception {
        loadPageWithAlerts2(test("xmp"));
    }
}
