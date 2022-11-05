/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests the result of <code>document.createElement(elementName).outerHTML</code>.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ElementOuterHtmlTest extends WebDriverTestCase {

    private void test(final String elementName) throws Exception {
        final String html = "<!DOCTYPE html><html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var value = document.createElement('" + elementName + "').outerHTML;\n"
                + "      value = value.replace('<?XML:NAMESPACE PREFIX = PUBLIC NS = \"URN:COMPONENT\" />', '');\n"
                + "      value = value.replace('<?XML:NAMESPACE PREFIX = \"PUBLIC\" NS = \"URN:COMPONENT\" />', '');\n"
                + "      log(value);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<abbr></abbr>")
    public void abbr() throws Exception {
        test("abbr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<acronym></acronym>")
    public void acronym() throws Exception {
        test("acronym");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<a></a>")
    public void a() throws Exception {
        test("a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<address></address>")
    public void address() throws Exception {
        test("address");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<applet></applet>")
    public void applet() throws Exception {
        test("applet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<area>")
    public void area() throws Exception {
        test("area");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<article></article>")
    public void article() throws Exception {
        test("article");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<aside></aside>")
    public void aside() throws Exception {
        test("aside");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<audio></audio>")
    public void audio() throws Exception {
        test("audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<bgsound>")
    public void bgsound() throws Exception {
        test("bgsound");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<base>")
    public void base() throws Exception {
        test("base");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<basefont>",
            IE = "<basefont></basefont>")
    public void basefont() throws Exception {
        test("basefont");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<bdi></bdi>")
    public void bdi() throws Exception {
        test("bdi");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<bdo></bdo>")
    public void bdo() throws Exception {
        test("bdo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<big></big>")
    public void big() throws Exception {
        test("big");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<blink></blink>")
    public void blink() throws Exception {
        test("blink");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<blockquote></blockquote>")
    public void blockquote() throws Exception {
        test("blockquote");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<body></body>")
    public void body() throws Exception {
        test("body");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b></b>")
    public void b() throws Exception {
        test("b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<br>")
    public void br() throws Exception {
        test("br");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<button></button>")
    public void button() throws Exception {
        test("button");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<canvas></canvas>")
    public void canvas() throws Exception {
        test("canvas");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<caption></caption>")
    public void caption() throws Exception {
        test("caption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<center></center>")
    public void center() throws Exception {
        test("center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<cite></cite>")
    public void cite() throws Exception {
        test("cite");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<code></code>")
    public void code() throws Exception {
        test("code");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<col>")
    public void col() throws Exception {
        test("col");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<colgroup></colgroup>")
    public void colgroup() throws Exception {
        test("colgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<command></command>")
    public void command() throws Exception {
        test("command");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<datalist></datalist>")
    public void datalist() throws Exception {
        test("datalist");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dfn></dfn>")
    public void dfn() throws Exception {
        test("dfn");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dd></dd>")
    public void dd() throws Exception {
        test("dd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<del></del>")
    public void del() throws Exception {
        test("del");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<details></details>")
    public void details() throws Exception {
        test("details");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dialog></dialog>")
    public void dialog() throws Exception {
        test("dialog");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dir></dir>")
    public void dir() throws Exception {
        test("dir");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div></div>")
    public void div() throws Exception {
        test("div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dl></dl>")
    public void dl() throws Exception {
        test("dl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<dt></dt>")
    public void dt() throws Exception {
        test("dt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<embed>")
    public void embed() throws Exception {
        test("embed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<em></em>")
    public void em() throws Exception {
        test("em");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<fieldset></fieldset>")
    public void fieldset() throws Exception {
        test("fieldset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<figcaption></figcaption>")
    public void figcaption() throws Exception {
        test("figcaption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<figure></figure>")
    public void figure() throws Exception {
        test("figure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<font></font>")
    public void font() throws Exception {
        test("font");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<form></form>")
    public void form() throws Exception {
        test("form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<footer></footer>")
    public void footer() throws Exception {
        test("footer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<frame>")
    public void frame() throws Exception {
        test("frame");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<frameset></frameset>")
    public void frameset() throws Exception {
        test("frameset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h1></h1>")
    public void h1() throws Exception {
        test("h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h2></h2>")
    public void h2() throws Exception {
        test("h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h3></h3>")
    public void h3() throws Exception {
        test("h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h4></h4>")
    public void h4() throws Exception {
        test("h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h5></h5>")
    public void h5() throws Exception {
        test("h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<h6></h6>")
    public void h6() throws Exception {
        test("h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<head></head>")
    public void head() throws Exception {
        test("head");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<header></header>")
    public void header() throws Exception {
        test("header");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<hr>")
    public void hr() throws Exception {
        test("hr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<html></html>")
    public void html() throws Exception {
        test("html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<iframe></iframe>")
    public void iframe() throws Exception {
        test("iframe");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<q></q>")
    public void q() throws Exception {
        test("q");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ruby></ruby>")
    public void ruby() throws Exception {
        test("ruby");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<rt></rt>")
    public void rt() throws Exception {
        test("rt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<rp></rp>")
    public void rp() throws Exception {
        test("rp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<image></image>",
            IE = "<img>")
    public void image() throws Exception {
        test("image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<img>")
    public void img() throws Exception {
        test("img");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ins></ins>")
    public void ins() throws Exception {
        test("ins");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<isindex></isindex>",
            IE = "<isindex>")
    public void isindex() throws Exception {
        test("isindex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<i></i>")
    public void i() throws Exception {
        test("i");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<kbd></kbd>")
    public void kbd() throws Exception {
        test("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<keygen>")
    public void keygen() throws Exception {
        test("keygen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<label></label>")
    public void label() throws Exception {
        test("label");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<layer></layer>")
    public void layer() throws Exception {
        test("layer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<legend></legend>")
    public void legend() throws Exception {
        test("legend");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<listing></listing>")
    public void listing() throws Exception {
        test("listing");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<li></li>")
    public void li() throws Exception {
        test("li");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<link>")
    public void link() throws Exception {
        test("link");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<main></main>")
    public void main() throws Exception {
        test("main");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<map></map>")
    public void map() throws Exception {
        test("map");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<marquee></marquee>")
    public void marquee() throws Exception {
        test("marquee");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<mark></mark>")
    public void mark() throws Exception {
        test("mark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<menu></menu>")
    public void menu() throws Exception {
        test("menu");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<menuitem></menuitem>")
    public void menuitem() throws Exception {
        test("menuitem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<meta>")
    public void meta() throws Exception {
        test("meta");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<meter></meter>")
    public void meter() throws Exception {
        test("meter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<multicol></multicol>")
    public void multicol() throws Exception {
        test("multicol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<nav></nav>")
    public void nav() throws Exception {
        test("nav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<nextid></nextid>",
            IE = "<nextid>")
    public void nextid() throws Exception {
        test("nextid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<nobr></nobr>")
    public void nobr() throws Exception {
        test("nobr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<noembed></noembed>")
    public void noembed() throws Exception {
        test("noembed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<noframes></noframes>")
    public void noframes() throws Exception {
        test("noframes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<noscript></noscript>")
    public void noscript() throws Exception {
        test("noscript");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<nolayer></nolayer>")
    public void nolayer() throws Exception {
        test("nolayer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<object></object>")
    public void object() throws Exception {
        test("object");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ol></ol>")
    public void ol() throws Exception {
        test("ol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<optgroup></optgroup>")
    public void optgroup() throws Exception {
        test("optgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<option></option>")
    public void option() throws Exception {
        test("option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<output></output>")
    public void output() throws Exception {
        test("output");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<p></p>")
    public void p() throws Exception {
        test("p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<param>")
    public void param() throws Exception {
        test("param");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<plaintext></plaintext>")
    public void plaintext() throws Exception {
        test("plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<pre></pre>")
    public void pre() throws Exception {
        test("pre");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<progress></progress>")
    public void progress() throws Exception {
        test("progress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<s></s>")
    public void s() throws Exception {
        test("s");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<samp></samp>")
    public void samp() throws Exception {
        test("samp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<script></script>")
    public void script() throws Exception {
        test("script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<section></section>")
    public void section() throws Exception {
        test("section");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<select></select>")
    public void select() throws Exception {
        test("select");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<small></small>")
    public void small() throws Exception {
        test("small");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<source>")
    public void source() throws Exception {
        test("source");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<spacer></spacer>")
    public void spacer() throws Exception {
        test("spacer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<span></span>")
    public void span() throws Exception {
        test("span");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<strike></strike>")
    public void strike() throws Exception {
        test("strike");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<strong></strong>")
    public void strong() throws Exception {
        test("strong");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<style></style>")
    public void style() throws Exception {
        test("style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<sub></sub>")
    public void sub() throws Exception {
        test("sub");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<summary></summary>")
    public void summary() throws Exception {
        test("summary");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<sup></sup>")
    public void sup() throws Exception {
        test("sup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<svg></svg>")
    public void svg() throws Exception {
        test("svg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<table></table>")
    public void table() throws Exception {
        test("table");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tbody></tbody>")
    public void tbody() throws Exception {
        test("tbody");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<td></td>")
    public void td() throws Exception {
        test("td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<th></th>")
    public void th() throws Exception {
        test("th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tr></tr>")
    public void tr() throws Exception {
        test("tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<track>")
    public void track() throws Exception {
        test("track");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<textarea></textarea>")
    public void textarea() throws Exception {
        test("textarea");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tfoot></tfoot>")
    public void tfoot() throws Exception {
        test("tfoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<thead></thead>")
    public void thead() throws Exception {
        test("thead");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<tt></tt>")
    public void tt() throws Exception {
        test("tt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<time></time>")
    public void time() throws Exception {
        test("time");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<title></title>")
    public void title() throws Exception {
        test("title");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<u></u>")
    public void u() throws Exception {
        test("u");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<ul></ul>")
    public void ul() throws Exception {
        test("ul");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<var></var>")
    public void var() throws Exception {
        test("var");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<video></video>")
    public void video() throws Exception {
        test("video");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<wbr>")
    public void wbr() throws Exception {
        test("wbr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<xmp></xmp>")
    public void xmp() throws Exception {
        test("xmp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<abcdefg></abcdefg>")
    public void arbitrary() throws Exception {
        test("abcdefg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<input>")
    public void input() throws Exception {
        test("input");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<data></data>")
    public void data() throws Exception {
        test("data");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<content></content>")
    public void content() throws Exception {
        test("content");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<picture></picture>")
    public void picture() throws Exception {
        test("picture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<template></template>")
    public void template() throws Exception {
        test("template");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<slot></slot>")
    public void slot() throws Exception {
        test("slot");
    }
}
