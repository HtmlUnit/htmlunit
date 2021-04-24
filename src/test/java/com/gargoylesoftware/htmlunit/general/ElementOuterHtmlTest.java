/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests the result of <code>document.createElement(elementName).outerElement</code>.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ElementOuterHtmlTest extends WebDriverTestCase {

    private static String test(final String elementName) {
        return "<!DOCTYPE html><html><head>\n"
                + "  <script>\n"
                + "    function test() {\n"
                + "      var value = document.createElement('" + elementName + "').outerHTML;\n"
                + "      while (value && (value.charAt(0) == '\\r' || value.charAt(0) == '\\n')) {\n"
                + "        value = value.substring(1);\n"
                + "      }\n"
                + "      value = value.replace('<?XML:NAMESPACE PREFIX = PUBLIC NS = \"URN:COMPONENT\" />', '');\n"
                + "      value = value.replace('<?XML:NAMESPACE PREFIX = \"PUBLIC\" NS = \"URN:COMPONENT\" />', '');\n"
                + "      alert(value);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<abbr></abbr>")
    public void abbr() throws Exception {
        loadPageWithAlerts2(test("abbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<acronym></acronym>")
    public void acronym() throws Exception {
        loadPageWithAlerts2(test("acronym"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<a></a>")
    public void a() throws Exception {
        loadPageWithAlerts2(test("a"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<address></address>")
    public void address() throws Exception {
        loadPageWithAlerts2(test("address"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<applet></applet>")
    public void applet() throws Exception {
        loadPageWithAlerts2(test("applet"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<area>")
    public void area() throws Exception {
        loadPageWithAlerts2(test("area"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<article></article>")
    public void article() throws Exception {
        loadPageWithAlerts2(test("article"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<aside></aside>")
    public void aside() throws Exception {
        loadPageWithAlerts2(test("aside"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<audio></audio>")
    public void audio() throws Exception {
        loadPageWithAlerts2(test("audio"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<bgsound>")
    public void bgsound() throws Exception {
        loadPageWithAlerts2(test("bgsound"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<base>")
    public void base() throws Exception {
        loadPageWithAlerts2(test("base"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<basefont>",
            IE = "<basefont></basefont>")
    public void basefont() throws Exception {
        loadPageWithAlerts2(test("basefont"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<bdi></bdi>")
    public void bdi() throws Exception {
        loadPageWithAlerts2(test("bdi"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<bdo></bdo>")
    public void bdo() throws Exception {
        loadPageWithAlerts2(test("bdo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<big></big>")
    public void big() throws Exception {
        loadPageWithAlerts2(test("big"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<blink></blink>")
    public void blink() throws Exception {
        loadPageWithAlerts2(test("blink"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<blockquote></blockquote>")
    public void blockquote() throws Exception {
        loadPageWithAlerts2(test("blockquote"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<body></body>")
    public void body() throws Exception {
        loadPageWithAlerts2(test("body"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b></b>")
    public void b() throws Exception {
        loadPageWithAlerts2(test("b"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<br>")
    public void br() throws Exception {
        loadPageWithAlerts2(test("br"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<button></button>")
    public void button() throws Exception {
        loadPageWithAlerts2(test("button"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<canvas></canvas>")
    public void canvas() throws Exception {
        loadPageWithAlerts2(test("canvas"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<caption></caption>")
    public void caption() throws Exception {
        loadPageWithAlerts2(test("caption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<center></center>")
    public void center() throws Exception {
        loadPageWithAlerts2(test("center"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<cite></cite>")
    public void cite() throws Exception {
        loadPageWithAlerts2(test("cite"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<code></code>")
    public void code() throws Exception {
        loadPageWithAlerts2(test("code"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<col>")
    public void col() throws Exception {
        loadPageWithAlerts2(test("col"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<colgroup></colgroup>")
    public void colgroup() throws Exception {
        loadPageWithAlerts2(test("colgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<command></command>")
    public void command() throws Exception {
        loadPageWithAlerts2(test("command"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<datalist></datalist>")
    public void datalist() throws Exception {
        loadPageWithAlerts2(test("datalist"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dfn></dfn>")
    public void dfn() throws Exception {
        loadPageWithAlerts2(test("dfn"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dd></dd>")
    public void dd() throws Exception {
        loadPageWithAlerts2(test("dd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<del></del>")
    public void del() throws Exception {
        loadPageWithAlerts2(test("del"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<details></details>")
    public void details() throws Exception {
        loadPageWithAlerts2(test("details"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dialog></dialog>")
    public void dialog() throws Exception {
        loadPageWithAlerts2(test("dialog"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dir></dir>")
    public void dir() throws Exception {
        loadPageWithAlerts2(test("dir"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div></div>")
    public void div() throws Exception {
        loadPageWithAlerts2(test("div"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dl></dl>")
    public void dl() throws Exception {
        loadPageWithAlerts2(test("dl"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dt></dt>")
    public void dt() throws Exception {
        loadPageWithAlerts2(test("dt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<embed>")
    public void embed() throws Exception {
        loadPageWithAlerts2(test("embed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<em></em>")
    public void em() throws Exception {
        loadPageWithAlerts2(test("em"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<fieldset></fieldset>")
    public void fieldset() throws Exception {
        loadPageWithAlerts2(test("fieldset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<figcaption></figcaption>")
    public void figcaption() throws Exception {
        loadPageWithAlerts2(test("figcaption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<figure></figure>")
    public void figure() throws Exception {
        loadPageWithAlerts2(test("figure"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<font></font>")
    public void font() throws Exception {
        loadPageWithAlerts2(test("font"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<form></form>")
    public void form() throws Exception {
        loadPageWithAlerts2(test("form"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<footer></footer>")
    public void footer() throws Exception {
        loadPageWithAlerts2(test("footer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<frame>")
    public void frame() throws Exception {
        loadPageWithAlerts2(test("frame"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<frameset></frameset>")
    public void frameset() throws Exception {
        loadPageWithAlerts2(test("frameset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h1></h1>")
    public void h1() throws Exception {
        loadPageWithAlerts2(test("h1"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h2></h2>")
    public void h2() throws Exception {
        loadPageWithAlerts2(test("h2"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h3></h3>")
    public void h3() throws Exception {
        loadPageWithAlerts2(test("h3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h4></h4>")
    public void h4() throws Exception {
        loadPageWithAlerts2(test("h4"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h5></h5>")
    public void h5() throws Exception {
        loadPageWithAlerts2(test("h5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h6></h6>")
    public void h6() throws Exception {
        loadPageWithAlerts2(test("h6"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<head></head>")
    public void head() throws Exception {
        loadPageWithAlerts2(test("head"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<header></header>")
    public void header() throws Exception {
        loadPageWithAlerts2(test("header"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<hr>")
    public void hr() throws Exception {
        loadPageWithAlerts2(test("hr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<html></html>")
    public void html() throws Exception {
        loadPageWithAlerts2(test("html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<iframe></iframe>")
    public void iframe() throws Exception {
        loadPageWithAlerts2(test("iframe"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<q></q>")
    public void q() throws Exception {
        loadPageWithAlerts2(test("q"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ruby></ruby>")
    public void ruby() throws Exception {
        loadPageWithAlerts2(test("ruby"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<rt></rt>")
    public void rt() throws Exception {
        loadPageWithAlerts2(test("rt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<rp></rp>")
    public void rp() throws Exception {
        loadPageWithAlerts2(test("rp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<image></image>",
            IE = "<img>")
    public void image() throws Exception {
        loadPageWithAlerts2(test("image"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<img>")
    public void img() throws Exception {
        loadPageWithAlerts2(test("img"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ins></ins>")
    public void ins() throws Exception {
        loadPageWithAlerts2(test("ins"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<isindex></isindex>",
            IE = "<isindex>")
    public void isindex() throws Exception {
        loadPageWithAlerts2(test("isindex"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<i></i>")
    public void i() throws Exception {
        loadPageWithAlerts2(test("i"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<kbd></kbd>")
    public void kbd() throws Exception {
        loadPageWithAlerts2(test("kbd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<keygen>")
    public void keygen() throws Exception {
        loadPageWithAlerts2(test("keygen"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<label></label>")
    public void label() throws Exception {
        loadPageWithAlerts2(test("label"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<layer></layer>")
    public void layer() throws Exception {
        loadPageWithAlerts2(test("layer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<legend></legend>")
    public void legend() throws Exception {
        loadPageWithAlerts2(test("legend"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<listing></listing>")
    public void listing() throws Exception {
        loadPageWithAlerts2(test("listing"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<li></li>")
    public void li() throws Exception {
        loadPageWithAlerts2(test("li"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<link>")
    public void link() throws Exception {
        loadPageWithAlerts2(test("link"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<main></main>")
    public void main() throws Exception {
        loadPageWithAlerts2(test("main"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<map></map>")
    public void map() throws Exception {
        loadPageWithAlerts2(test("map"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<marquee></marquee>")
    public void marquee() throws Exception {
        loadPageWithAlerts2(test("marquee"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<mark></mark>")
    public void mark() throws Exception {
        loadPageWithAlerts2(test("mark"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<menu></menu>")
    public void menu() throws Exception {
        loadPageWithAlerts2(test("menu"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<menuitem></menuitem>")
    public void menuitem() throws Exception {
        loadPageWithAlerts2(test("menuitem"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<meta>")
    public void meta() throws Exception {
        loadPageWithAlerts2(test("meta"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<meter></meter>")
    public void meter() throws Exception {
        loadPageWithAlerts2(test("meter"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<multicol></multicol>")
    public void multicol() throws Exception {
        loadPageWithAlerts2(test("multicol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<nav></nav>")
    public void nav() throws Exception {
        loadPageWithAlerts2(test("nav"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<nextid></nextid>",
            IE = "<nextid>")
    public void nextid() throws Exception {
        loadPageWithAlerts2(test("nextid"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<nobr></nobr>")
    public void nobr() throws Exception {
        loadPageWithAlerts2(test("nobr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<noembed></noembed>")
    public void noembed() throws Exception {
        loadPageWithAlerts2(test("noembed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<noframes></noframes>")
    public void noframes() throws Exception {
        loadPageWithAlerts2(test("noframes"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<noscript></noscript>")
    public void noscript() throws Exception {
        loadPageWithAlerts2(test("noscript"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<nolayer></nolayer>")
    public void nolayer() throws Exception {
        loadPageWithAlerts2(test("nolayer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<object></object>")
    public void object() throws Exception {
        loadPageWithAlerts2(test("object"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ol></ol>")
    public void ol() throws Exception {
        loadPageWithAlerts2(test("ol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<optgroup></optgroup>")
    public void optgroup() throws Exception {
        loadPageWithAlerts2(test("optgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<option></option>")
    public void option() throws Exception {
        loadPageWithAlerts2(test("option"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<output></output>")
    public void output() throws Exception {
        loadPageWithAlerts2(test("output"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<p></p>")
    public void p() throws Exception {
        loadPageWithAlerts2(test("p"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<param>")
    public void param() throws Exception {
        loadPageWithAlerts2(test("param"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<plaintext></plaintext>")
    public void plaintext() throws Exception {
        loadPageWithAlerts2(test("plaintext"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<pre></pre>")
    public void pre() throws Exception {
        loadPageWithAlerts2(test("pre"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<progress></progress>")
    public void progress() throws Exception {
        loadPageWithAlerts2(test("progress"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<s></s>")
    public void s() throws Exception {
        loadPageWithAlerts2(test("s"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<samp></samp>")
    public void samp() throws Exception {
        loadPageWithAlerts2(test("samp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<script></script>")
    public void script() throws Exception {
        loadPageWithAlerts2(test("script"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<section></section>")
    public void section() throws Exception {
        loadPageWithAlerts2(test("section"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<select></select>")
    public void select() throws Exception {
        loadPageWithAlerts2(test("select"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<small></small>")
    public void small() throws Exception {
        loadPageWithAlerts2(test("small"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<source>")
    public void source() throws Exception {
        loadPageWithAlerts2(test("source"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<spacer></spacer>")
    public void spacer() throws Exception {
        loadPageWithAlerts2(test("spacer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<span></span>")
    public void span() throws Exception {
        loadPageWithAlerts2(test("span"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<strike></strike>")
    public void strike() throws Exception {
        loadPageWithAlerts2(test("strike"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<strong></strong>")
    public void strong() throws Exception {
        loadPageWithAlerts2(test("strong"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<style></style>")
    public void style() throws Exception {
        loadPageWithAlerts2(test("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<sub></sub>")
    public void sub() throws Exception {
        loadPageWithAlerts2(test("sub"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<summary></summary>")
    public void summary() throws Exception {
        loadPageWithAlerts2(test("summary"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<sup></sup>")
    public void sup() throws Exception {
        loadPageWithAlerts2(test("sup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<svg></svg>")
    public void svg() throws Exception {
        loadPageWithAlerts2(test("svg"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<table></table>")
    public void table() throws Exception {
        loadPageWithAlerts2(test("table"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tbody></tbody>")
    public void tbody() throws Exception {
        loadPageWithAlerts2(test("tbody"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<td></td>")
    public void td() throws Exception {
        loadPageWithAlerts2(test("td"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<th></th>")
    public void th() throws Exception {
        loadPageWithAlerts2(test("th"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tr></tr>")
    public void tr() throws Exception {
        loadPageWithAlerts2(test("tr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<track>")
    public void track() throws Exception {
        loadPageWithAlerts2(test("track"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<textarea></textarea>")
    public void textarea() throws Exception {
        loadPageWithAlerts2(test("textarea"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tfoot></tfoot>")
    public void tfoot() throws Exception {
        loadPageWithAlerts2(test("tfoot"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<thead></thead>")
    public void thead() throws Exception {
        loadPageWithAlerts2(test("thead"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tt></tt>")
    public void tt() throws Exception {
        loadPageWithAlerts2(test("tt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<time></time>")
    public void time() throws Exception {
        loadPageWithAlerts2(test("time"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<title></title>")
    public void title() throws Exception {
        loadPageWithAlerts2(test("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<u></u>")
    public void u() throws Exception {
        loadPageWithAlerts2(test("u"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ul></ul>")
    public void ul() throws Exception {
        loadPageWithAlerts2(test("ul"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<var></var>")
    public void var() throws Exception {
        loadPageWithAlerts2(test("var"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<video></video>")
    public void video() throws Exception {
        loadPageWithAlerts2(test("video"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<wbr>")
    public void wbr() throws Exception {
        loadPageWithAlerts2(test("wbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<xmp></xmp>")
    public void xmp() throws Exception {
        loadPageWithAlerts2(test("xmp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<abcdefg></abcdefg>")
    public void arbitrary() throws Exception {
        loadPageWithAlerts2(test("abcdefg"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<input>")
    public void input() throws Exception {
        loadPageWithAlerts2(test("input"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<data></data>")
    public void data() throws Exception {
        loadPageWithAlerts2(test("data"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<content></content>")
    public void content() throws Exception {
        loadPageWithAlerts2(test("content"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<picture></picture>")
    public void picture() throws Exception {
        loadPageWithAlerts2(test("picture"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<template></template>")
    public void template() throws Exception {
        loadPageWithAlerts2(test("template"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<slot></slot>")
    public void slot() throws Exception {
        loadPageWithAlerts2(test("slot"));
    }

}
