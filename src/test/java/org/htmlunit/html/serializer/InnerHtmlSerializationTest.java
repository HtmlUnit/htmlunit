/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html.serializer;

import java.nio.charset.StandardCharsets;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests the HTML serialization ({@code innerHTML}) of a text child for every HTML element.
 *
 * <p>The DOM is built with {@code createElement} / {@code createTextNode}, so the parser is not involved and
 * only the serializer is under test. The text contains markup characters, an ampersand, an entity-like
 * sequence, quotes and a non-breaking space. It is appended to a detached element (nothing is loaded or
 * executed), and the element is wrapped in a detached {@code div}. Each test logs two lines:
 * <ol>
 *   <li>{@code element.innerHTML}: serializes the element's children (the top level path)</li>
 *   <li>{@code container.innerHTML}: serializes the element as a child of its parent (the nested path)</li>
 * </ol>
 *
 * <p>The HTML serialization algorithm writes the text children of these HTML elements as is:
 * {@code script}, {@code style}, {@code xmp}, {@code iframe}, {@code noembed}, {@code noframes},
 * {@code plaintext} and {@code noscript} (with scripting enabled). All other text is escaped
 * ({@code &amp;}, {@code &lt;}, {@code &gt;}, {@code &nbsp;}), including {@code textarea} and {@code title}.
 * Children of void elements are not serialized when the element is serialized as a child, only when the void
 * element itself is the container. The same names in the SVG namespace are not special, and the XML
 * serialization of an XHTML document always escapes.
 *
 * @author Ronald Brill
 */
public class InnerHtmlSerializationTest extends WebDriverTestCase {

    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";
    private static final String MATHML_NAMESPACE = "http://www.w3.org/1998/Math/MathML";

    /** Creates the element and logs both serializations; '%CREATE%' is replaced by the creation expression. */
    private static final String PROBE =
              "var text = '<b id=\"x\">a &amp; b & c \"q\"</b>\\u00a0z';\n"
            + "var el = %CREATE%;\n"
            + "var target = el.nodeName.toLowerCase() == 'template' ? el.content : el;\n"
            + "target.appendChild(document.createTextNode(text));\n"
            + "var container = document.createElement('div');\n"
            + "container.appendChild(el);\n"
            + "log(el.innerHTML);\n"
            + "log(container.innerHTML);\n";

    // ---------- HTML elements: raw text, text is NOT escaped ----------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<script><b id=\"x\">a &amp; b & c \"q\"</b> z</script>"})
    public void script_innerHtml() throws Exception {
        htmlElement("script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<style><b id=\"x\">a &amp; b & c \"q\"</b> z</style>"})
    public void style_innerHtml() throws Exception {
        htmlElement("style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<xmp><b id=\"x\">a &amp; b & c \"q\"</b> z</xmp>"})
    public void xmp_innerHtml() throws Exception {
        htmlElement("xmp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<iframe><b id=\"x\">a &amp; b & c \"q\"</b> z</iframe>"})
    public void iframe_innerHtml() throws Exception {
        htmlElement("iframe");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<noembed><b id=\"x\">a &amp; b & c \"q\"</b> z</noembed>"})
    public void noembed_innerHtml() throws Exception {
        htmlElement("noembed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<noframes><b id=\"x\">a &amp; b & c \"q\"</b> z</noframes>"})
    public void noframes_innerHtml() throws Exception {
        htmlElement("noframes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<plaintext><b id=\"x\">a &amp; b & c \"q\"</b> z</plaintext>"})
    public void plaintext_innerHtml() throws Exception {
        htmlElement("plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<noscript><b id=\"x\">a &amp; b & c \"q\"</b> z</noscript>"})
    public void noscript_innerHtml() throws Exception {
        htmlElement("noscript");
    }

    // ---------- HTML elements: escapable raw text, text is escaped ----------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</textarea>"})
    public void textarea_innerHtml() throws Exception {
        htmlElement("textarea");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</title>"})
    public void title_innerHtml() throws Exception {
        htmlElement("title");
    }

    // ---------- HTML elements: void elements --------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<area>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<area>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<area>"})
    public void area_innerHtml() throws Exception {
        htmlElement("area");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<base>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<base>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<base>"})
    public void base_innerHtml() throws Exception {
        htmlElement("base");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<basefont>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<basefont>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<basefont>"})
    public void basefont_innerHtml() throws Exception {
        htmlElement("basefont");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<bgsound>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<bgsound>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<bgsound>"})
    public void bgsound_innerHtml() throws Exception {
        htmlElement("bgsound");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<br>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<br>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<br>"})
    public void br_innerHtml() throws Exception {
        htmlElement("br");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<col>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<col>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<col>"})
    public void col_innerHtml() throws Exception {
        htmlElement("col");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<embed>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<embed>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<embed>"})
    public void embed_innerHtml() throws Exception {
        htmlElement("embed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<frame>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<frame>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<frame>"})
    public void frame_innerHtml() throws Exception {
        htmlElement("frame");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<hr>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<hr>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<hr>"})
    public void hr_innerHtml() throws Exception {
        htmlElement("hr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<img>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<img>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<img>"})
    public void img_innerHtml() throws Exception {
        htmlElement("img");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<input>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<input>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<input>"})
    public void input_innerHtml() throws Exception {
        htmlElement("input");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<keygen>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<keygen>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<keygen>"})
    public void keygen_innerHtml() throws Exception {
        htmlElement("keygen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<link>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<link>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<link>"})
    public void link_innerHtml() throws Exception {
        htmlElement("link");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<meta>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<meta>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<meta>"})
    public void meta_innerHtml() throws Exception {
        htmlElement("meta");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<param>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<param>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<param>"})
    public void param_innerHtml() throws Exception {
        htmlElement("param");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<source>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<source>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<source>"})
    public void source_innerHtml() throws Exception {
        htmlElement("source");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<track>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<track>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<track>"})
    public void track_innerHtml() throws Exception {
        htmlElement("track");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "<wbr>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<wbr>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<wbr>"})
    public void wbr_innerHtml() throws Exception {
        htmlElement("wbr");
    }

    // ---------- HTML elements: template (content fragment) ------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<template>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</template>"})
    public void template_innerHtml() throws Exception {
        htmlElement("template");
    }

    // ---------- HTML elements: all others, text is escaped ------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<a>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</a>"})
    public void a_innerHtml() throws Exception {
        htmlElement("a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<abbr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</abbr>"})
    public void abbr_innerHtml() throws Exception {
        htmlElement("abbr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<acronym>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</acronym>"})
    public void acronym_innerHtml() throws Exception {
        htmlElement("acronym");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<address>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</address>"})
    public void address_innerHtml() throws Exception {
        htmlElement("address");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<applet>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</applet>"})
    public void applet_innerHtml() throws Exception {
        htmlElement("applet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<article>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</article>"})
    public void article_innerHtml() throws Exception {
        htmlElement("article");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<aside>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</aside>"})
    public void aside_innerHtml() throws Exception {
        htmlElement("aside");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<audio>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</audio>"})
    public void audio_innerHtml() throws Exception {
        htmlElement("audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<b>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</b>"})
    public void b_innerHtml() throws Exception {
        htmlElement("b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<bdi>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</bdi>"})
    public void bdi_innerHtml() throws Exception {
        htmlElement("bdi");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<bdo>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</bdo>"})
    public void bdo_innerHtml() throws Exception {
        htmlElement("bdo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<big>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</big>"})
    public void big_innerHtml() throws Exception {
        htmlElement("big");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<blink>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</blink>"})
    public void blink_innerHtml() throws Exception {
        htmlElement("blink");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<blockquote>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</blockquote>"})
    public void blockquote_innerHtml() throws Exception {
        htmlElement("blockquote");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<body>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</body>"})
    public void body_innerHtml() throws Exception {
        htmlElement("body");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<button>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</button>"})
    public void button_innerHtml() throws Exception {
        htmlElement("button");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<canvas>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</canvas>"})
    public void canvas_innerHtml() throws Exception {
        htmlElement("canvas");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<caption>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</caption>"})
    public void caption_innerHtml() throws Exception {
        htmlElement("caption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<center>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</center>"})
    public void center_innerHtml() throws Exception {
        htmlElement("center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<cite>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</cite>"})
    public void cite_innerHtml() throws Exception {
        htmlElement("cite");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<code>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</code>"})
    public void code_innerHtml() throws Exception {
        htmlElement("code");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<colgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</colgroup>"})
    public void colgroup_innerHtml() throws Exception {
        htmlElement("colgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<data>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</data>"})
    public void data_innerHtml() throws Exception {
        htmlElement("data");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<datalist>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</datalist>"})
    public void datalist_innerHtml() throws Exception {
        htmlElement("datalist");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<dd>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dd>"})
    public void dd_innerHtml() throws Exception {
        htmlElement("dd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<del>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</del>"})
    public void del_innerHtml() throws Exception {
        htmlElement("del");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<details>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</details>"})
    public void details_innerHtml() throws Exception {
        htmlElement("details");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<dfn>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dfn>"})
    public void dfn_innerHtml() throws Exception {
        htmlElement("dfn");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<dialog>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dialog>"})
    public void dialog_innerHtml() throws Exception {
        htmlElement("dialog");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<dir>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dir>"})
    public void dir_innerHtml() throws Exception {
        htmlElement("dir");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</div>"})
    public void div_innerHtml() throws Exception {
        htmlElement("div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<dl>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dl>"})
    public void dl_innerHtml() throws Exception {
        htmlElement("dl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<dt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dt>"})
    public void dt_innerHtml() throws Exception {
        htmlElement("dt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<em>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</em>"})
    public void em_innerHtml() throws Exception {
        htmlElement("em");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<fieldset>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</fieldset>"})
    public void fieldset_innerHtml() throws Exception {
        htmlElement("fieldset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<figcaption>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</figcaption>"})
    public void figcaption_innerHtml() throws Exception {
        htmlElement("figcaption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<figure>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</figure>"})
    public void figure_innerHtml() throws Exception {
        htmlElement("figure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<font>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</font>"})
    public void font_innerHtml() throws Exception {
        htmlElement("font");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<footer>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</footer>"})
    public void footer_innerHtml() throws Exception {
        htmlElement("footer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<form>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</form>"})
    public void form_innerHtml() throws Exception {
        htmlElement("form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<frameset>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</frameset>"})
    public void frameset_innerHtml() throws Exception {
        htmlElement("frameset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<h1>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h1>"})
    public void h1_innerHtml() throws Exception {
        htmlElement("h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<h2>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h2>"})
    public void h2_innerHtml() throws Exception {
        htmlElement("h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<h3>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h3>"})
    public void h3_innerHtml() throws Exception {
        htmlElement("h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<h4>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h4>"})
    public void h4_innerHtml() throws Exception {
        htmlElement("h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<h5>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h5>"})
    public void h5_innerHtml() throws Exception {
        htmlElement("h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<h6>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h6>"})
    public void h6_innerHtml() throws Exception {
        htmlElement("h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<head>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</head>"})
    public void head_innerHtml() throws Exception {
        htmlElement("head");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<header>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</header>"})
    public void header_innerHtml() throws Exception {
        htmlElement("header");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<hgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</hgroup>"})
    public void hgroup_innerHtml() throws Exception {
        htmlElement("hgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<html>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</html>"})
    public void html_innerHtml() throws Exception {
        htmlElement("html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<i>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</i>"})
    public void i_innerHtml() throws Exception {
        htmlElement("i");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<ins>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ins>"})
    public void ins_innerHtml() throws Exception {
        htmlElement("ins");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<kbd>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</kbd>"})
    public void kbd_innerHtml() throws Exception {
        htmlElement("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<label>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</label>"})
    public void label_innerHtml() throws Exception {
        htmlElement("label");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<legend>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</legend>"})
    public void legend_innerHtml() throws Exception {
        htmlElement("legend");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<li>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</li>"})
    public void li_innerHtml() throws Exception {
        htmlElement("li");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<listing>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</listing>"})
    public void listing_innerHtml() throws Exception {
        htmlElement("listing");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<main>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</main>"})
    public void main_innerHtml() throws Exception {
        htmlElement("main");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<map>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</map>"})
    public void map_innerHtml() throws Exception {
        htmlElement("map");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<mark>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</mark>"})
    public void mark_innerHtml() throws Exception {
        htmlElement("mark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<marquee>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</marquee>"})
    public void marquee_innerHtml() throws Exception {
        htmlElement("marquee");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<menu>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</menu>"})
    public void menu_innerHtml() throws Exception {
        htmlElement("menu");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<meter>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</meter>"})
    public void meter_innerHtml() throws Exception {
        htmlElement("meter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<nav>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nav>"})
    public void nav_innerHtml() throws Exception {
        htmlElement("nav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<nobr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nobr>"})
    public void nobr_innerHtml() throws Exception {
        htmlElement("nobr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<object>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</object>"})
    public void object_innerHtml() throws Exception {
        htmlElement("object");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<ol>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ol>"})
    public void ol_innerHtml() throws Exception {
        htmlElement("ol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<optgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</optgroup>"})
    public void optgroup_innerHtml() throws Exception {
        htmlElement("optgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<option>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</option>"})
    public void option_innerHtml() throws Exception {
        htmlElement("option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<output>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</output>"})
    public void output_innerHtml() throws Exception {
        htmlElement("output");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<p>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</p>"})
    public void p_innerHtml() throws Exception {
        htmlElement("p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<picture>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</picture>"})
    public void picture_innerHtml() throws Exception {
        htmlElement("picture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<pre>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</pre>"})
    public void pre_innerHtml() throws Exception {
        htmlElement("pre");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<progress>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</progress>"})
    public void progress_innerHtml() throws Exception {
        htmlElement("progress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<q>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</q>"})
    public void q_innerHtml() throws Exception {
        htmlElement("q");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<rb>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rb>"})
    public void rb_innerHtml() throws Exception {
        htmlElement("rb");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<rp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rp>"})
    public void rp_innerHtml() throws Exception {
        htmlElement("rp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<rt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rt>"})
    public void rt_innerHtml() throws Exception {
        htmlElement("rt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<rtc>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rtc>"})
    public void rtc_innerHtml() throws Exception {
        htmlElement("rtc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<ruby>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ruby>"})
    public void ruby_innerHtml() throws Exception {
        htmlElement("ruby");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<s>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</s>"})
    public void s_innerHtml() throws Exception {
        htmlElement("s");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<samp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</samp>"})
    public void samp_innerHtml() throws Exception {
        htmlElement("samp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<search>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</search>"})
    public void search_innerHtml() throws Exception {
        htmlElement("search");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<section>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</section>"})
    public void section_innerHtml() throws Exception {
        htmlElement("section");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<select>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</select>"})
    public void select_innerHtml() throws Exception {
        htmlElement("select");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<slot>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</slot>"})
    public void slot_innerHtml() throws Exception {
        htmlElement("slot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<small>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</small>"})
    public void small_innerHtml() throws Exception {
        htmlElement("small");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<span>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</span>"})
    public void span_innerHtml() throws Exception {
        htmlElement("span");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<strike>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</strike>"})
    public void strike_innerHtml() throws Exception {
        htmlElement("strike");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<strong>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</strong>"})
    public void strong_innerHtml() throws Exception {
        htmlElement("strong");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<sub>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</sub>"})
    public void sub_innerHtml() throws Exception {
        htmlElement("sub");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<summary>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</summary>"})
    public void summary_innerHtml() throws Exception {
        htmlElement("summary");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<sup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</sup>"})
    public void sup_innerHtml() throws Exception {
        htmlElement("sup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<table>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</table>"})
    public void table_innerHtml() throws Exception {
        htmlElement("table");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<tbody>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tbody>"})
    public void tbody_innerHtml() throws Exception {
        htmlElement("tbody");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<td>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</td>"})
    public void td_innerHtml() throws Exception {
        htmlElement("td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<tfoot>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tfoot>"})
    public void tfoot_innerHtml() throws Exception {
        htmlElement("tfoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<th>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</th>"})
    public void th_innerHtml() throws Exception {
        htmlElement("th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<thead>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</thead>"})
    public void thead_innerHtml() throws Exception {
        htmlElement("thead");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<time>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</time>"})
    public void time_innerHtml() throws Exception {
        htmlElement("time");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<tr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tr>"})
    public void tr_innerHtml() throws Exception {
        htmlElement("tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<tt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tt>"})
    public void tt_innerHtml() throws Exception {
        htmlElement("tt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<u>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</u>"})
    public void u_innerHtml() throws Exception {
        htmlElement("u");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<ul>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ul>"})
    public void ul_innerHtml() throws Exception {
        htmlElement("ul");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<var>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</var>"})
    public void var_innerHtml() throws Exception {
        htmlElement("var");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<video>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</video>"})
    public void video_innerHtml() throws Exception {
        htmlElement("video");
    }

    // ---------- Foreign content: same names, but not special ----------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<svg>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</svg>"})
    public void svg_innerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "svg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</style>"})
    public void svg_style_innerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</script>"})
    public void svg_script_innerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</title>"})
    public void svg_title_innerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "title");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z", "<math>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</math>"})
    public void math_innerHtml() throws Exception {
        namespacedElement(MATHML_NAMESPACE, "math");
    }

    // ---------- XHTML document: XML serialization always escapes ------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<div xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>"})
    public void div_innerHtmlXhtml() throws Exception {
        xhtmlElement("div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<script xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>"},
            FF = {"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<script xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>"},
            FF_ESR = {"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<script xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>"})
    public void script_innerHtmlXhtml() throws Exception {
        xhtmlElement("script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<style xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>"},
            FF = {"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<style xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>"},
            FF_ESR = {"<b id=\"x\">a &amp; b & c \"q\"</b> z", "<style xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>"})
    public void style_innerHtmlXhtml() throws Exception {
        xhtmlElement("style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<xmp xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>"})
    public void xmp_innerHtmlXhtml() throws Exception {
        xhtmlElement("xmp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<iframe xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>"})
    public void iframe_innerHtmlXhtml() throws Exception {
        xhtmlElement("iframe");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noembed xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>"})
    public void noembed_innerHtmlXhtml() throws Exception {
        xhtmlElement("noembed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noframes xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>"})
    public void noframes_innerHtmlXhtml() throws Exception {
        xhtmlElement("noframes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<plaintext xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>"})
    public void plaintext_innerHtmlXhtml() throws Exception {
        xhtmlElement("plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noscript xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>"})
    public void noscript_innerHtmlXhtml() throws Exception {
        xhtmlElement("noscript");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<textarea xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>"})
    public void textarea_innerHtmlXhtml() throws Exception {
        xhtmlElement("textarea");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<title xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>"})
    @HtmlUnitNYI(
            CHROME = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>"},
            EDGE = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>"},
            FF = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>"},
            FF_ESR = {"&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z", "<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>"})
    public void title_innerHtmlXhtml() throws Exception {
        xhtmlElement("title");
    }


    // ------------------------------------------------------------------ helpers

    private void htmlElement(final String tag) throws Exception {
        htmlPage("document.createElement('" + tag + "')");
    }

    private void namespacedElement(final String namespace, final String tag) throws Exception {
        htmlPage("document.createElementNS('" + namespace + "', '" + tag + "')");
    }

    private void xhtmlElement(final String tag) throws Exception {
        final String create = "document.createElement('" + tag + "')";
        final String xhtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head><title></title>\n"
            + "<script>//<![CDATA[\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + PROBE.replace("%CREATE%", create)
            + "  }\n"
            + "//]]></script></head>\n"
            + "<body onload=\"test()\"></body></html>";

        final WebDriver driver = loadPage2(xhtml, URL_FIRST, "application/xhtml+xml", StandardCharsets.UTF_8);
        verifyTitle2(driver, getExpectedAlerts());
    }

    private void htmlPage(final String create) throws Exception {
        final String html = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + PROBE.replace("%CREATE%", create)
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }
}