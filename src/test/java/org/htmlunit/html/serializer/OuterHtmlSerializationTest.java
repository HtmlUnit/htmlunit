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
 * Tests the HTML serialization ({@code outerHTML}) of an element with a text child for every HTML element.
 * The counterpart of {@link InnerHtmlSerializationTest}.
 *
 * <p>The DOM is built with {@code createElement} / {@code createTextNode}, so the parser is not involved and
 * only the serializer is under test. The text contains markup characters, an ampersand, an entity-like
 * sequence, quotes and a non-breaking space. It is appended to a detached element (nothing is loaded or
 * executed), and the element is wrapped in a detached {@code div}. Each test logs two lines:
 * <ol>
 *   <li>{@code element.outerHTML}: serializes the element itself, with its own tags (the same string as
 *       {@code container.innerHTML} of the innerHTML tests)</li>
 *   <li>{@code container.outerHTML}: serializes the wrapper {@code div} with the element as its child (the
 *       nested path)</li>
 * </ol>
 *
 * <p>The HTML serialization algorithm writes the text children of these HTML elements as is:
 * {@code script}, {@code style}, {@code xmp}, {@code iframe}, {@code noembed}, {@code noframes},
 * {@code plaintext} and {@code noscript} (with scripting enabled). All other text is escaped
 * ({@code &amp;}, {@code &lt;}, {@code &gt;}, {@code &nbsp;}), including {@code textarea} and {@code title}.
 * Void elements are written as start tag only, without end tag and without their children, also when the
 * void element itself is serialized. The same names in the SVG namespace are not special. In an XHTML document
 * the XML serialization is used: it always escapes, and the outermost element carries the namespace
 * declaration.
 *
 * @author Ronald Brill
 */
public class OuterHtmlSerializationTest extends WebDriverTestCase {

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
            + "log(el.outerHTML);\n"
            + "log(container.outerHTML);\n";

    // ---------- HTML elements: raw text, text is NOT escaped ----------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<script><b id=\"x\">a &amp; b & c \"q\"</b> z</script>", "<div><script><b id=\"x\">a &amp; b & c \"q\"</b> z</script></div>"})
    public void script_outerHtml() throws Exception {
        htmlElement("script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<style><b id=\"x\">a &amp; b & c \"q\"</b> z</style>", "<div><style><b id=\"x\">a &amp; b & c \"q\"</b> z</style></div>"})
    public void style_outerHtml() throws Exception {
        htmlElement("style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<xmp><b id=\"x\">a &amp; b & c \"q\"</b> z</xmp>", "<div><xmp><b id=\"x\">a &amp; b & c \"q\"</b> z</xmp></div>"})
    public void xmp_outerHtml() throws Exception {
        htmlElement("xmp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<iframe><b id=\"x\">a &amp; b & c \"q\"</b> z</iframe>", "<div><iframe><b id=\"x\">a &amp; b & c \"q\"</b> z</iframe></div>"})
    public void iframe_outerHtml() throws Exception {
        htmlElement("iframe");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noembed><b id=\"x\">a &amp; b & c \"q\"</b> z</noembed>", "<div><noembed><b id=\"x\">a &amp; b & c \"q\"</b> z</noembed></div>"})
    public void noembed_outerHtml() throws Exception {
        htmlElement("noembed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noframes><b id=\"x\">a &amp; b & c \"q\"</b> z</noframes>", "<div><noframes><b id=\"x\">a &amp; b & c \"q\"</b> z</noframes></div>"})
    public void noframes_outerHtml() throws Exception {
        htmlElement("noframes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<plaintext><b id=\"x\">a &amp; b & c \"q\"</b> z</plaintext>", "<div><plaintext><b id=\"x\">a &amp; b & c \"q\"</b> z</plaintext></div>"})
    public void plaintext_outerHtml() throws Exception {
        htmlElement("plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noscript><b id=\"x\">a &amp; b & c \"q\"</b> z</noscript>", "<div><noscript><b id=\"x\">a &amp; b & c \"q\"</b> z</noscript></div>"})
    public void noscript_outerHtml() throws Exception {
        htmlElement("noscript");
    }

    // ---------- HTML elements: escapable raw text, text is escaped ----------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</textarea>", "<div><textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</textarea></div>"})
    public void textarea_outerHtml() throws Exception {
        htmlElement("textarea");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</title>", "<div><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</title></div>"})
    public void title_outerHtml() throws Exception {
        htmlElement("title");
    }

    // ---------- HTML elements: void elements --------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<area>", "<div><area></div>"})
    public void area_outerHtml() throws Exception {
        htmlElement("area");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<base>", "<div><base></div>"})
    public void base_outerHtml() throws Exception {
        htmlElement("base");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<basefont>", "<div><basefont></div>"})
    public void basefont_outerHtml() throws Exception {
        htmlElement("basefont");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<bgsound>", "<div><bgsound></div>"})
    public void bgsound_outerHtml() throws Exception {
        htmlElement("bgsound");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<br>", "<div><br></div>"})
    public void br_outerHtml() throws Exception {
        htmlElement("br");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<col>", "<div><col></div>"})
    public void col_outerHtml() throws Exception {
        htmlElement("col");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<embed>", "<div><embed></div>"})
    public void embed_outerHtml() throws Exception {
        htmlElement("embed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<frame>", "<div><frame></div>"})
    public void frame_outerHtml() throws Exception {
        htmlElement("frame");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<hr>", "<div><hr></div>"})
    public void hr_outerHtml() throws Exception {
        htmlElement("hr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<img>", "<div><img></div>"})
    public void img_outerHtml() throws Exception {
        htmlElement("img");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<input>", "<div><input></div>"})
    public void input_outerHtml() throws Exception {
        htmlElement("input");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<keygen>", "<div><keygen></div>"})
    public void keygen_outerHtml() throws Exception {
        htmlElement("keygen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<link>", "<div><link></div>"})
    public void link_outerHtml() throws Exception {
        htmlElement("link");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<meta>", "<div><meta></div>"})
    public void meta_outerHtml() throws Exception {
        htmlElement("meta");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<param>", "<div><param></div>"})
    public void param_outerHtml() throws Exception {
        htmlElement("param");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<source>", "<div><source></div>"})
    public void source_outerHtml() throws Exception {
        htmlElement("source");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<track>", "<div><track></div>"})
    public void track_outerHtml() throws Exception {
        htmlElement("track");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<wbr>", "<div><wbr></div>"})
    public void wbr_outerHtml() throws Exception {
        htmlElement("wbr");
    }

    // ---------- HTML elements: template (content fragment) ------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<template>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</template>", "<div><template>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</template></div>"})
    public void template_outerHtml() throws Exception {
        htmlElement("template");
    }

    // ---------- HTML elements: all others, text is escaped ------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<a>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</a>", "<div><a>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</a></div>"})
    public void a_outerHtml() throws Exception {
        htmlElement("a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<abbr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</abbr>", "<div><abbr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</abbr></div>"})
    public void abbr_outerHtml() throws Exception {
        htmlElement("abbr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<acronym>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</acronym>", "<div><acronym>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</acronym></div>"})
    public void acronym_outerHtml() throws Exception {
        htmlElement("acronym");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<address>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</address>", "<div><address>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</address></div>"})
    public void address_outerHtml() throws Exception {
        htmlElement("address");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<applet>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</applet>", "<div><applet>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</applet></div>"})
    public void applet_outerHtml() throws Exception {
        htmlElement("applet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<article>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</article>", "<div><article>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</article></div>"})
    public void article_outerHtml() throws Exception {
        htmlElement("article");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<aside>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</aside>", "<div><aside>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</aside></div>"})
    public void aside_outerHtml() throws Exception {
        htmlElement("aside");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<audio>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</audio>", "<div><audio>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</audio></div>"})
    public void audio_outerHtml() throws Exception {
        htmlElement("audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<b>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</b>", "<div><b>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</b></div>"})
    public void b_outerHtml() throws Exception {
        htmlElement("b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<bdi>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</bdi>", "<div><bdi>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</bdi></div>"})
    public void bdi_outerHtml() throws Exception {
        htmlElement("bdi");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<bdo>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</bdo>", "<div><bdo>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</bdo></div>"})
    public void bdo_outerHtml() throws Exception {
        htmlElement("bdo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<big>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</big>", "<div><big>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</big></div>"})
    public void big_outerHtml() throws Exception {
        htmlElement("big");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<blink>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</blink>", "<div><blink>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</blink></div>"})
    public void blink_outerHtml() throws Exception {
        htmlElement("blink");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<blockquote>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</blockquote>", "<div><blockquote>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</blockquote></div>"})
    public void blockquote_outerHtml() throws Exception {
        htmlElement("blockquote");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<body>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</body>", "<div><body>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</body></div>"})
    public void body_outerHtml() throws Exception {
        htmlElement("body");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<button>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</button>", "<div><button>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</button></div>"})
    public void button_outerHtml() throws Exception {
        htmlElement("button");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<canvas>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</canvas>", "<div><canvas>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</canvas></div>"})
    public void canvas_outerHtml() throws Exception {
        htmlElement("canvas");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<caption>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</caption>", "<div><caption>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</caption></div>"})
    public void caption_outerHtml() throws Exception {
        htmlElement("caption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<center>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</center>", "<div><center>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</center></div>"})
    public void center_outerHtml() throws Exception {
        htmlElement("center");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<cite>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</cite>", "<div><cite>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</cite></div>"})
    public void cite_outerHtml() throws Exception {
        htmlElement("cite");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<code>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</code>", "<div><code>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</code></div>"})
    public void code_outerHtml() throws Exception {
        htmlElement("code");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<colgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</colgroup>", "<div><colgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</colgroup></div>"})
    public void colgroup_outerHtml() throws Exception {
        htmlElement("colgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<data>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</data>", "<div><data>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</data></div>"})
    public void data_outerHtml() throws Exception {
        htmlElement("data");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<datalist>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</datalist>", "<div><datalist>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</datalist></div>"})
    public void datalist_outerHtml() throws Exception {
        htmlElement("datalist");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<dd>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dd>", "<div><dd>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dd></div>"})
    public void dd_outerHtml() throws Exception {
        htmlElement("dd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<del>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</del>", "<div><del>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</del></div>"})
    public void del_outerHtml() throws Exception {
        htmlElement("del");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<details>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</details>", "<div><details>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</details></div>"})
    public void details_outerHtml() throws Exception {
        htmlElement("details");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<dfn>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dfn>", "<div><dfn>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dfn></div>"})
    public void dfn_outerHtml() throws Exception {
        htmlElement("dfn");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<dialog>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dialog>", "<div><dialog>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dialog></div>"})
    public void dialog_outerHtml() throws Exception {
        htmlElement("dialog");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<dir>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dir>", "<div><dir>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dir></div>"})
    public void dir_outerHtml() throws Exception {
        htmlElement("dir");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</div>", "<div><div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</div></div>"})
    public void div_outerHtml() throws Exception {
        htmlElement("div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<dl>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dl>", "<div><dl>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dl></div>"})
    public void dl_outerHtml() throws Exception {
        htmlElement("dl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<dt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dt>", "<div><dt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</dt></div>"})
    public void dt_outerHtml() throws Exception {
        htmlElement("dt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<em>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</em>", "<div><em>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</em></div>"})
    public void em_outerHtml() throws Exception {
        htmlElement("em");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<fieldset>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</fieldset>", "<div><fieldset>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</fieldset></div>"})
    public void fieldset_outerHtml() throws Exception {
        htmlElement("fieldset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<figcaption>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</figcaption>", "<div><figcaption>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</figcaption></div>"})
    public void figcaption_outerHtml() throws Exception {
        htmlElement("figcaption");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<figure>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</figure>", "<div><figure>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</figure></div>"})
    public void figure_outerHtml() throws Exception {
        htmlElement("figure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<font>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</font>", "<div><font>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</font></div>"})
    public void font_outerHtml() throws Exception {
        htmlElement("font");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<footer>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</footer>", "<div><footer>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</footer></div>"})
    public void footer_outerHtml() throws Exception {
        htmlElement("footer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<form>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</form>", "<div><form>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</form></div>"})
    public void form_outerHtml() throws Exception {
        htmlElement("form");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<frameset>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</frameset>", "<div><frameset>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</frameset></div>"})
    public void frameset_outerHtml() throws Exception {
        htmlElement("frameset");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<h1>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h1>", "<div><h1>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h1></div>"})
    public void h1_outerHtml() throws Exception {
        htmlElement("h1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<h2>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h2>", "<div><h2>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h2></div>"})
    public void h2_outerHtml() throws Exception {
        htmlElement("h2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<h3>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h3>", "<div><h3>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h3></div>"})
    public void h3_outerHtml() throws Exception {
        htmlElement("h3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<h4>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h4>", "<div><h4>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h4></div>"})
    public void h4_outerHtml() throws Exception {
        htmlElement("h4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<h5>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h5>", "<div><h5>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h5></div>"})
    public void h5_outerHtml() throws Exception {
        htmlElement("h5");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<h6>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h6>", "<div><h6>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</h6></div>"})
    public void h6_outerHtml() throws Exception {
        htmlElement("h6");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<head>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</head>", "<div><head>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</head></div>"})
    public void head_outerHtml() throws Exception {
        htmlElement("head");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<header>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</header>", "<div><header>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</header></div>"})
    public void header_outerHtml() throws Exception {
        htmlElement("header");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<hgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</hgroup>", "<div><hgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</hgroup></div>"})
    public void hgroup_outerHtml() throws Exception {
        htmlElement("hgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<html>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</html>", "<div><html>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</html></div>"})
    public void html_outerHtml() throws Exception {
        htmlElement("html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<i>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</i>", "<div><i>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</i></div>"})
    public void i_outerHtml() throws Exception {
        htmlElement("i");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<ins>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ins>", "<div><ins>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ins></div>"})
    public void ins_outerHtml() throws Exception {
        htmlElement("ins");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<kbd>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</kbd>", "<div><kbd>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</kbd></div>"})
    public void kbd_outerHtml() throws Exception {
        htmlElement("kbd");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<label>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</label>", "<div><label>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</label></div>"})
    public void label_outerHtml() throws Exception {
        htmlElement("label");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<legend>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</legend>", "<div><legend>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</legend></div>"})
    public void legend_outerHtml() throws Exception {
        htmlElement("legend");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<li>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</li>", "<div><li>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</li></div>"})
    public void li_outerHtml() throws Exception {
        htmlElement("li");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<listing>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</listing>", "<div><listing>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</listing></div>"})
    public void listing_outerHtml() throws Exception {
        htmlElement("listing");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<main>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</main>", "<div><main>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</main></div>"})
    public void main_outerHtml() throws Exception {
        htmlElement("main");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<map>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</map>", "<div><map>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</map></div>"})
    public void map_outerHtml() throws Exception {
        htmlElement("map");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<mark>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</mark>", "<div><mark>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</mark></div>"})
    public void mark_outerHtml() throws Exception {
        htmlElement("mark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<marquee>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</marquee>", "<div><marquee>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</marquee></div>"})
    public void marquee_outerHtml() throws Exception {
        htmlElement("marquee");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<menu>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</menu>", "<div><menu>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</menu></div>"})
    public void menu_outerHtml() throws Exception {
        htmlElement("menu");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<meter>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</meter>", "<div><meter>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</meter></div>"})
    public void meter_outerHtml() throws Exception {
        htmlElement("meter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<nav>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nav>", "<div><nav>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nav></div>"})
    public void nav_outerHtml() throws Exception {
        htmlElement("nav");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<nobr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nobr>", "<div><nobr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nobr></div>"})
    public void nobr_outerHtml() throws Exception {
        htmlElement("nobr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<object>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</object>", "<div><object>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</object></div>"})
    public void object_outerHtml() throws Exception {
        htmlElement("object");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<ol>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ol>", "<div><ol>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ol></div>"})
    public void ol_outerHtml() throws Exception {
        htmlElement("ol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<optgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</optgroup>", "<div><optgroup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</optgroup></div>"})
    public void optgroup_outerHtml() throws Exception {
        htmlElement("optgroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<option>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</option>", "<div><option>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</option></div>"})
    public void option_outerHtml() throws Exception {
        htmlElement("option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<output>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</output>", "<div><output>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</output></div>"})
    public void output_outerHtml() throws Exception {
        htmlElement("output");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<p>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</p>", "<div><p>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</p></div>"})
    public void p_outerHtml() throws Exception {
        htmlElement("p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<picture>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</picture>", "<div><picture>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</picture></div>"})
    public void picture_outerHtml() throws Exception {
        htmlElement("picture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<pre>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</pre>", "<div><pre>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</pre></div>"})
    public void pre_outerHtml() throws Exception {
        htmlElement("pre");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<progress>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</progress>", "<div><progress>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</progress></div>"})
    public void progress_outerHtml() throws Exception {
        htmlElement("progress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<q>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</q>", "<div><q>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</q></div>"})
    public void q_outerHtml() throws Exception {
        htmlElement("q");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<rb>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rb>", "<div><rb>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rb></div>"})
    public void rb_outerHtml() throws Exception {
        htmlElement("rb");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<rp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rp>", "<div><rp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rp></div>"})
    public void rp_outerHtml() throws Exception {
        htmlElement("rp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<rt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rt>", "<div><rt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rt></div>"})
    public void rt_outerHtml() throws Exception {
        htmlElement("rt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<rtc>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rtc>", "<div><rtc>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</rtc></div>"})
    public void rtc_outerHtml() throws Exception {
        htmlElement("rtc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<ruby>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ruby>", "<div><ruby>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ruby></div>"})
    public void ruby_outerHtml() throws Exception {
        htmlElement("ruby");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<s>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</s>", "<div><s>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</s></div>"})
    public void s_outerHtml() throws Exception {
        htmlElement("s");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<samp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</samp>", "<div><samp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</samp></div>"})
    public void samp_outerHtml() throws Exception {
        htmlElement("samp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<search>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</search>", "<div><search>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</search></div>"})
    public void search_outerHtml() throws Exception {
        htmlElement("search");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<section>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</section>", "<div><section>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</section></div>"})
    public void section_outerHtml() throws Exception {
        htmlElement("section");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<select>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</select>", "<div><select>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</select></div>"})
    public void select_outerHtml() throws Exception {
        htmlElement("select");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<slot>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</slot>", "<div><slot>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</slot></div>"})
    public void slot_outerHtml() throws Exception {
        htmlElement("slot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<small>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</small>", "<div><small>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</small></div>"})
    public void small_outerHtml() throws Exception {
        htmlElement("small");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<span>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</span>", "<div><span>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</span></div>"})
    public void span_outerHtml() throws Exception {
        htmlElement("span");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<strike>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</strike>", "<div><strike>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</strike></div>"})
    public void strike_outerHtml() throws Exception {
        htmlElement("strike");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<strong>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</strong>", "<div><strong>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</strong></div>"})
    public void strong_outerHtml() throws Exception {
        htmlElement("strong");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<sub>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</sub>", "<div><sub>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</sub></div>"})
    public void sub_outerHtml() throws Exception {
        htmlElement("sub");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<summary>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</summary>", "<div><summary>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</summary></div>"})
    public void summary_outerHtml() throws Exception {
        htmlElement("summary");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<sup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</sup>", "<div><sup>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</sup></div>"})
    public void sup_outerHtml() throws Exception {
        htmlElement("sup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<table>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</table>", "<div><table>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</table></div>"})
    public void table_outerHtml() throws Exception {
        htmlElement("table");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<tbody>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tbody>", "<div><tbody>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tbody></div>"})
    public void tbody_outerHtml() throws Exception {
        htmlElement("tbody");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<td>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</td>", "<div><td>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</td></div>"})
    public void td_outerHtml() throws Exception {
        htmlElement("td");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<tfoot>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tfoot>", "<div><tfoot>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tfoot></div>"})
    public void tfoot_outerHtml() throws Exception {
        htmlElement("tfoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<th>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</th>", "<div><th>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</th></div>"})
    public void th_outerHtml() throws Exception {
        htmlElement("th");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<thead>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</thead>", "<div><thead>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</thead></div>"})
    public void thead_outerHtml() throws Exception {
        htmlElement("thead");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<time>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</time>", "<div><time>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</time></div>"})
    public void time_outerHtml() throws Exception {
        htmlElement("time");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<tr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tr>", "<div><tr>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tr></div>"})
    public void tr_outerHtml() throws Exception {
        htmlElement("tr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<tt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tt>", "<div><tt>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</tt></div>"})
    public void tt_outerHtml() throws Exception {
        htmlElement("tt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<u>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</u>", "<div><u>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</u></div>"})
    public void u_outerHtml() throws Exception {
        htmlElement("u");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<ul>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ul>", "<div><ul>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</ul></div>"})
    public void ul_outerHtml() throws Exception {
        htmlElement("ul");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<var>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</var>", "<div><var>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</var></div>"})
    public void var_outerHtml() throws Exception {
        htmlElement("var");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<video>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</video>", "<div><video>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</video></div>"})
    public void video_outerHtml() throws Exception {
        htmlElement("video");
    }

    /**
     * Legacy (Netscape) element, unknown to the browsers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<nolayer>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nolayer>", "<div><nolayer>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</nolayer></div>"})
    public void nolayer_outerHtml() throws Exception {
        htmlElement("nolayer");
    }

    /**
     * Legacy (Netscape) element, unknown to the browsers.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<layer>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</layer>", "<div><layer>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</layer></div>"})
    public void layer_outerHtml() throws Exception {
        htmlElement("layer");
    }

    /**
     * Only the parser maps {@code image} to {@code img}; createElement creates an unknown element,
     * so this is not a void element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<image>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</image>", "<div><image>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</image></div>"})
    public void image_outerHtml() throws Exception {
        htmlElement("image");
    }

    /**
     * An {@code svg} created in the HTML namespace is an unknown element, not an svg element
     * (see {@link #svg_outerHtml()} for the one in the svg namespace).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<svg>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</svg>", "<div><svg>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</svg></div>"})
    public void svg_createElement_outerHtml() throws Exception {
        htmlElement("svg");
    }

    // ---------- Foreign content: same names, but not special ----------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<svg>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</svg>", "<div><svg>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</svg></div>"})
    public void svg_outerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "svg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</style>", "<div><style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</style></div>"})
    public void svg_style_outerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</script>", "<div><script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</script></div>"})
    public void svg_script_outerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</title>", "<div><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</title></div>"})
    public void svg_title_outerHtml() throws Exception {
        namespacedElement(SVG_NAMESPACE, "title");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<math>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</math>", "<div><math>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt;&nbsp;z</math></div>"})
    public void math_outerHtml() throws Exception {
        namespacedElement(MATHML_NAMESPACE, "math");
    }

    // ---------- XHTML document: XML serialization always escapes ------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div></div>"})
    @HtmlUnitNYI(
            CHROME = {"<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>", "<div><div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div></div>"},
            EDGE = {"<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>", "<div><div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div></div>"},
            FF = {"<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>", "<div><div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div></div>"},
            FF_ESR = {"<div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div>", "<div><div>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</div></div>"})
    public void div_outerHtmlXhtml() throws Exception {
        xhtmlElement("div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<script xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script></div>"})
    @HtmlUnitNYI(
            CHROME = {"<script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>", "<div><script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script></div>"},
            EDGE = {"<script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>", "<div><script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script></div>"},
            FF = {"<script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>", "<div><script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script></div>"},
            FF_ESR = {"<script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>", "<div><script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script></div>"})
    public void script_outerHtmlXhtml() throws Exception {
        xhtmlElement("script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<style xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style></div>"})
    @HtmlUnitNYI(
            CHROME = {"<style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>", "<div><style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style></div>"},
            EDGE = {"<style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>", "<div><style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style></div>"},
            FF = {"<style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>", "<div><style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style></div>"},
            FF_ESR = {"<style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>", "<div><style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style></div>"})
    public void style_outerHtmlXhtml() throws Exception {
        xhtmlElement("style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<xmp xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp></div>"})
    @HtmlUnitNYI(
            CHROME = {"<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>", "<div><xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp></div>"},
            EDGE = {"<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>", "<div><xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp></div>"},
            FF = {"<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>", "<div><xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp></div>"},
            FF_ESR = {"<xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>", "<div><xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp></div>"})
    public void xmp_outerHtmlXhtml() throws Exception {
        xhtmlElement("xmp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<iframe xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe></div>"})
    @HtmlUnitNYI(
            CHROME = {"<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>", "<div><iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe></div>"},
            EDGE = {"<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>", "<div><iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe></div>"},
            FF = {"<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>", "<div><iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe></div>"},
            FF_ESR = {"<iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>", "<div><iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe></div>"})
    public void iframe_outerHtmlXhtml() throws Exception {
        xhtmlElement("iframe");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noembed xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed></div>"})
    @HtmlUnitNYI(
            CHROME = {"<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>", "<div><noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed></div>"},
            EDGE = {"<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>", "<div><noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed></div>"},
            FF = {"<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>", "<div><noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed></div>"},
            FF_ESR = {"<noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>", "<div><noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed></div>"})
    public void noembed_outerHtmlXhtml() throws Exception {
        xhtmlElement("noembed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noframes xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes></div>"})
    @HtmlUnitNYI(
            CHROME = {"<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>", "<div><noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes></div>"},
            EDGE = {"<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>", "<div><noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes></div>"},
            FF = {"<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>", "<div><noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes></div>"},
            FF_ESR = {"<noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>", "<div><noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes></div>"})
    public void noframes_outerHtmlXhtml() throws Exception {
        xhtmlElement("noframes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<plaintext xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext></div>"})
    @HtmlUnitNYI(
            CHROME = {"<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>", "<div><plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext></div>"},
            EDGE = {"<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>", "<div><plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext></div>"},
            FF = {"<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>", "<div><plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext></div>"},
            FF_ESR = {"<plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>", "<div><plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext></div>"})
    public void plaintext_outerHtmlXhtml() throws Exception {
        xhtmlElement("plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noscript xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript></div>"})
    @HtmlUnitNYI(
            CHROME = {"<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>", "<div><noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript></div>"},
            EDGE = {"<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>", "<div><noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript></div>"},
            FF = {"<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>", "<div><noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript></div>"},
            FF_ESR = {"<noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>", "<div><noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript></div>"})
    public void noscript_outerHtmlXhtml() throws Exception {
        xhtmlElement("noscript");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<textarea xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea></div>"})
    @HtmlUnitNYI(
            CHROME = {"<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>", "<div><textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea></div>"},
            EDGE = {"<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>", "<div><textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea></div>"},
            FF = {"<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>", "<div><textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea></div>"},
            FF_ESR = {"<textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>", "<div><textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea></div>"})
    public void textarea_outerHtmlXhtml() throws Exception {
        xhtmlElement("textarea");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<title xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title></div>"})
    @HtmlUnitNYI(
            CHROME = {"<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>", "<div><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title></div>"},
            EDGE = {"<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>", "<div><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title></div>"},
            FF = {"<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>", "<div><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title></div>"},
            FF_ESR = {"<title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>", "<div><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title></div>"})
    public void title_outerHtmlXhtml() throws Exception {
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