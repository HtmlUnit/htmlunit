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
import org.junit.jupiter.api.Disabled;
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
    public void div_outerHtmlXhtml() throws Exception {
        xhtmlElement("div");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<script xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><script>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</script></div>"})
    public void script_outerHtmlXhtml() throws Exception {
        xhtmlElement("script");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<style xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><style>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</style></div>"})
    public void style_outerHtmlXhtml() throws Exception {
        xhtmlElement("style");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<xmp xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><xmp>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</xmp></div>"})
    public void xmp_outerHtmlXhtml() throws Exception {
        xhtmlElement("xmp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<iframe xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><iframe>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</iframe></div>"})
    public void iframe_outerHtmlXhtml() throws Exception {
        xhtmlElement("iframe");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noembed xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><noembed>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noembed></div>"})
    public void noembed_outerHtmlXhtml() throws Exception {
        xhtmlElement("noembed");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noframes xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><noframes>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noframes></div>"})
    public void noframes_outerHtmlXhtml() throws Exception {
        xhtmlElement("noframes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<plaintext xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><plaintext>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</plaintext></div>"})
    public void plaintext_outerHtmlXhtml() throws Exception {
        xhtmlElement("plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<noscript xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><noscript>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</noscript></div>"})
    public void noscript_outerHtmlXhtml() throws Exception {
        xhtmlElement("noscript");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<textarea xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><textarea>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</textarea></div>"})
    public void textarea_outerHtmlXhtml() throws Exception {
        xhtmlElement("textarea");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<title xmlns=\"http://www.w3.org/1999/xhtml\">&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title>", "<div xmlns=\"http://www.w3.org/1999/xhtml\"><title>&lt;b id=\"x\"&gt;a &amp;amp; b &amp; c \"q\"&lt;/b&gt; z</title></div>"})
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

    // ---------- attributes --------------------------------------------------------

    /**
     * A simple attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div title=\"x\"></div>")
    public void attrSimple_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.setAttribute('title', 'x');\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A double quote inside of an attribute value is escaped.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"a&quot;b\"></span></div>")
    public void attrDoubleQuote_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', 'a\"b');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A single quote inside of an attribute value is not escaped.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"a'b\"></span></div>")
    public void attrSingleQuote_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', \"a'b\");\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Ampersands and entity like text inside of an attribute value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"a&amp;b &amp;amp; &amp;lt; &amp;#65;\"></span></div>")
    public void attrAmpersand_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', 'a&b &amp; &lt; &#65;');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Less than and greater than inside of an attribute value (newer browsers escape them).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"a&lt;b&gt;c\"></span></div>")
    public void attrLessAndGreaterThan_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', 'a<b>c');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A non-breaking space inside of an attribute value is escaped.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"a&nbsp;b\"></span></div>")
    public void attrNbsp_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', 'a\\u00a0b');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Tab, line feed and carriage return inside of an attribute value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"a b c d\"></span></div>")
    public void attrWhitespaceCharacters_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', 'a\\tb\\nc\\rd');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * An attribute with an empty value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"\"></span></div>")
    public void attrEmptyValue_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', '');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Boolean attributes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div><button disabled=\"\" hidden=\"hidden\"></button></div>", "<div><button hidden=\"hidden\"></button></div>"})
    public void attrBoolean_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var b = document.createElement('button');\n"
                + "b.setAttribute('disabled', '');\n"
                + "b.setAttribute('hidden', 'hidden');\n"
                + "b.disabled = true;\n"
                + "d.appendChild(b);\n"
                + "log(d.outerHTML);\n"
                + "b.disabled = false;\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The case of attribute names in a HTML element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span title=\"x\" data-foo=\"y\"></span></div>")
    public void attrNameCaseHtml_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('TITLE', 'x');\n"
                + "c.setAttribute('Data-Foo', 'y');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The case of attribute names set with setAttributeNS.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span TITLE=\"x\"></span></div>")
    public void attrNameCaseNamespaced_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttributeNS(null, 'TITLE', 'x');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The order of the attributes and replacing a value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div><span b=\"4\" a=\"2\" c=\"3\"></span></div>",
             "<div><span b=\"4\" c=\"3\" a=\"5\"></span></div>"})
    public void attrOrder_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('b', '1');\n"
                + "c.setAttribute('a', '2');\n"
                + "c.setAttribute('c', '3');\n"
                + "c.setAttribute('b', '4');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n"
                + "c.removeAttribute('a');\n"
                + "c.setAttribute('a', '5');\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Attributes at void elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><br class=\"x\"><img src=\"a.png?x=1&amp;y=2\"></div>")
    public void attrOnVoidElement_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var b = document.createElement('br');\n"
                + "b.setAttribute('class', 'x');\n"
                + "var i = document.createElement('img');\n"
                + "i.setAttribute('src', 'a.png?x=1&y=2');\n"
                + "d.appendChild(b);\n"
                + "d.appendChild(i);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The xlink:href attribute at an svg element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><use xlink:href=\"#a&amp;b\"></use></div>")
    public void attrXlinkHref_outerHtml() throws Exception {
        htmlScript("var s = document.createElementNS('http://www.w3.org/2000/svg', 'use');\n"
                + "s.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', '#a&b');\n"
                + "var d = document.createElement('div');\n"
                + "d.appendChild(s);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The xml:lang attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span xml:lang=\"en\"></span></div>")
    public void attrXmlLang_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:lang', 'en');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A namespace declaration set as attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span xmlns:foo=\"urn:x\"></span></div>")
    public void attrXmlnsDeclaration_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttributeNS('http://www.w3.org/2000/xmlns/', 'xmlns:foo', 'urn:x');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The case of svg attribute names.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><svg viewBox=\"0 0 1 1\" preserveAspectRatio=\"none\"></svg></div>")
    public void attrSvgViewBox_outerHtml() throws Exception {
        htmlScript("var s = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
                + "s.setAttribute('viewBox', '0 0 1 1');\n"
                + "s.setAttribute('preserveAspectRatio', 'none');\n"
                + "var d = document.createElement('div');\n"
                + "d.appendChild(s);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The style attribute after setting style properties.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div style=\"color: red; margin-top: 1px;\"></div>", "<div style=\"margin-top: 1px;\"></div>"})
    public void attrStyleProperties_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.style.color = 'red';\n"
                + "d.style.marginTop = '1px';\n"
                + "log(d.outerHTML);\n"
                + "d.style.color = '';\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The style attribute after setting cssText.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div style=\"color: red; margin: 0px; background: url(&quot;x.png&quot;);\"></div>",
             "color: red; margin: 0px; background: url(\"x.png\");"})
    public void attrStyleCssText_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.style.cssText = 'COLOR: Red;margin:0  ;  background : URL(x.png)';\n"
                + "log(d.outerHTML);\n"
                + "log(d.getAttribute('style'));\n");
    }

    /**
     * The class attribute after changes via className and classList.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div class=\"a b\"></div>\u00A7<div class=\"a b c\"></div>", "<div class=\"b c\"></div>"})
    public void attrClass_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.className = 'a  b';\n"
                + "log(d.outerHTML);\n"
                + "d.classList.add('c');\n"
                + "log(d.outerHTML);\n"
                + "d.classList.remove('a');\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Non ASCII characters inside of an attribute value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div title=\"\u00E4\u00F6\u00FC\u20AC\uD83D\uDE00\"></div>")
    public void attrUnicode_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.setAttribute('title', '\\u00e4\\u00f6\\u00fc\\u20ac\\ud83d\\ude00');\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The data attributes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div data-foo-bar=\"x\" data-baz=\"\"></div>")
    public void attrDataset_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.dataset.fooBar = 'x';\n"
                + "d.dataset.baz = '';\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The srcdoc attribute with markup.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><iframe srcdoc=\"&lt;b id=&quot;x&quot;&gt;a &amp; b&lt;/b&gt;\"></iframe></div>")
    public void attrSrcdoc_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var f = document.createElement('iframe');\n"
                + "f.setAttribute('srcdoc', '<b id=\"x\">a & b</b>');\n"
                + "d.appendChild(f);\n"
                + "log(d.outerHTML);\n");
    }

    // ---------- properties that do not change the attributes ----------------------

    /**
     * The value property of an input does not change the attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div><input></div>", "<div><input value=\"w\"></div>", "<div><input value=\"w\"></div>"})
    public void attrInputValueProperty_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var i = document.createElement('input');\n"
                + "i.value = 'v';\n"
                + "d.appendChild(i);\n"
                + "log(d.outerHTML);\n"
                + "i.setAttribute('value', 'w');\n"
                + "log(d.outerHTML);\n"
                + "i.value = 'z';\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The checked property of an input does not change the attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div><input type=\"checkbox\"></div>", "<div><input type=\"checkbox\" checked=\"\"></div>"})
    public void attrInputCheckedProperty_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var i = document.createElement('input');\n"
                + "i.type = 'checkbox';\n"
                + "i.checked = true;\n"
                + "d.appendChild(i);\n"
                + "log(d.outerHTML);\n"
                + "i.setAttribute('checked', '');\n"
                + "i.checked = false;\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The selected property of an option does not change the attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div><select><option>a</option><option>b</option></select></div>",
             "<div><select><option selected=\"\">a</option><option>b</option></select></div>"})
    public void attrOptionSelectedProperty_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var s = document.createElement('select');\n"
                + "s.innerHTML = '<option>a</option><option>b</option>';\n"
                + "d.appendChild(s);\n"
                + "s.selectedIndex = 1;\n"
                + "log(d.outerHTML);\n"
                + "s.options[0].setAttribute('selected', '');\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The value property of a textarea does not change the content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><textarea>a</textarea></div>")
    public void attrTextareaValueProperty_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var t = document.createElement('textarea');\n"
                + "t.textContent = 'a';\n"
                + "t.value = 'b';\n"
                + "d.appendChild(t);\n"
                + "log(d.outerHTML);\n");
    }

    // ---------- other node types and tree structures ------------------------------

    /**
     * A comment node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><!-- c --></div>")
    public void nodeComment_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createComment(' c '));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A comment node that contains markup like text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><!--a--b>c<!--d--></div>")
    public void nodeCommentSpecialContent_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createComment('a--b>c<!' + '--d'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A processing instruction in a HTML document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<div><?xml-stylesheet href=\"a&b\"?></div>",
            FF = "<div><?xml-stylesheet href=\"a&b\"></div>",
            FF_ESR = "<div><?xml-stylesheet href=\"a&b\"></div>")
    public void nodeProcessingInstruction_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "try {\n"
                + "    d.appendChild(document.createProcessingInstruction('xml-stylesheet', 'href=\"a&b\"'));\n"
                + "    log(d.outerHTML);\n"
                + "} catch (e) {\n"
                + "    log(e.name);\n"
                + "}\n");
    }

    /**
     * A CDATA section is not supported in a HTML document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NotSupportedError")
    public void nodeCdataSection_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "try {\n"
                + "    d.appendChild(document.createCDATASection('a<b>c'));\n"
                + "    log(d.outerHTML);\n"
                + "} catch (e) {\n"
                + "    log(e.name);\n"
                + "}\n");
    }

    /**
     * Elements, text and comments mixed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div>a<b>b<!--c--></b>d</div>")
    public void nodeMixedChildren_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createTextNode('a'));\n"
                + "var b = document.createElement('b');\n"
                + "b.appendChild(document.createTextNode('b'));\n"
                + "b.appendChild(document.createComment('c'));\n"
                + "d.appendChild(b);\n"
                + "d.appendChild(document.createTextNode('d'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Empty text nodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><b></b></div>")
    public void nodeEmptyTextNodes_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createTextNode(''));\n"
                + "d.appendChild(document.createElement('b'));\n"
                + "d.appendChild(document.createTextNode(''));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Children moved from a document fragment.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div><b></b>t</div>", "0"})
    public void nodeDocumentFragment_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var f = document.createDocumentFragment();\n"
                + "f.appendChild(document.createElement('b'));\n"
                + "f.appendChild(document.createTextNode('t'));\n"
                + "d.appendChild(f);\n"
                + "log(d.outerHTML);\n"
                + "log(f.childNodes.length);\n");
    }

    /**
     * A child appended to a void element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<br>", "<div><br></div>"})
    public void nodeVoidElementWithChild_outerHtml() throws Exception {
        htmlScript("var b = document.createElement('br');\n"
                + "b.appendChild(document.createTextNode('x'));\n"
                + "var d = document.createElement('div');\n"
                + "d.appendChild(b);\n"
                + "log(b.outerHTML);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Unknown and custom elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><my-element></my-element><foo.bar></foo.bar><x:y></x:y></div>")
    public void nodeUnknownAndCustomElements_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createElement('my-element'));\n"
                + "d.appendChild(document.createElement('foo.bar'));\n"
                + "d.appendChild(document.createElement('x:y'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The case of element names.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><span></span><SPAN></SPAN><foreignObject></foreignObject><FOO></FOO></div>")
    public void nodeElementNameCase_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createElement('SPAN'));\n"
                + "d.appendChild(document.createElementNS('http://www.w3.org/1999/xhtml', 'SPAN'));\n"
                + "d.appendChild(document.createElementNS('http://www.w3.org/2000/svg', 'foreignObject'));\n"
                + "d.appendChild(document.createElementNS('http://www.w3.org/2000/svg', 'FOO'));\n"
                + "log(d.outerHTML);\n");
    }

    // ---------- pre, textarea, listing --------------------------------------------

    /**
     * A pre, textarea and listing with a leading line feed in the first text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><pre> abc</pre><textarea> abc</textarea><listing> abc</listing></div>")
    public void nodePreLeadingNewline_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "['pre', 'textarea', 'listing'].forEach(function(n) {\n"
                + "    var e = document.createElement(n);\n"
                + "    e.appendChild(document.createTextNode('\\nabc'));\n"
                + "    d.appendChild(e);\n"
                + "});\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A pre with two leading line feeds.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><pre> abc</pre></div>")
    public void nodePreLeadingTwoNewlines_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var e = document.createElement('pre');\n"
                + "e.appendChild(document.createTextNode('\\n\\nabc'));\n"
                + "d.appendChild(e);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A pre with an empty first text node followed by a line feed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><pre> abc</pre></div>")
    public void nodePreEmptyFirstTextNode_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var e = document.createElement('pre');\n"
                + "e.appendChild(document.createTextNode(''));\n"
                + "e.appendChild(document.createTextNode('\\nabc'));\n"
                + "d.appendChild(e);\n"
                + "log(d.outerHTML);\n");
    }

    // ---------- template, shadow dom, documents -----------------------------------

    /**
     * The content of a template with elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<template><b title=\"a&amp;b\">x&lt;y</b></template>",
             "<div><template><b title=\"a&amp;b\">x&lt;y</b></template></div>", "0"})
    public void nodeTemplateContent_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var t = document.createElement('template');\n"
                + "var b = document.createElement('b');\n"
                + "b.setAttribute('title', 'a&b');\n"
                + "b.appendChild(document.createTextNode('x<y'));\n"
                + "t.content.appendChild(b);\n"
                + "d.appendChild(t);\n"
                + "log(t.outerHTML);\n"
                + "log(d.outerHTML);\n"
                + "log(t.childNodes.length);\n");
    }

    /**
     * The shadow root is not part of the serialization.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("<div><b></b></div>")
    public void nodeShadowRootNotSerialized_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createElement('b'));\n"
                + "var s = d.attachShadow({mode: 'open'});\n"
                + "s.appendChild(document.createElement('i'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * getHTML() with serializable shadow roots.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts({"x", "<template shadowrootmode=\"open\" shadowrootserializable=\"\"><i></i></template>x"})
    public void nodeGetHtmlWithShadowRoot_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createTextNode('x'));\n"
                + "var s = d.attachShadow({mode: 'open', serializable: true});\n"
                + "s.appendChild(document.createElement('i'));\n"
                + "try {\n"
                + "    log(d.getHTML());\n"
                + "    log(d.getHTML({serializableShadowRoots: true}));\n"
                + "} catch (e) {\n"
                + "    log(e.name);\n"
                + "}\n");
    }

    /**
     * Special characters inside of text nodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div>a b ]]&gt; \u2028 \uD83D\uDE00 &nbsp; &amp;nbsp;</div>")
    public void nodeTextEdgeCases_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createTextNode('a\\r\\nb ]]> \\u2028 \\ud83d\\ude00 \\u00a0 &nbsp;'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A lone surrogate and a null character inside of text nodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("17")
    public void nodeLoneSurrogateAndNull_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createTextNode('a\\ud800b'));\n"
                + "d.appendChild(document.createTextNode('c\\u0000d'));\n"
                + "log(d.outerHTML.length);\n");
    }

    /**
     * The document element of a detached HTML document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<html><head><title>t</title></head><body><b></b></body></html>",
             "<head><title>t</title></head>"})
    public void nodeDetachedHtmlDocument_outerHtml() throws Exception {
        htmlScript("var doc = document.implementation.createHTMLDocument('t');\n"
                + "doc.body.appendChild(doc.createElement('b'));\n"
                + "log(doc.documentElement.outerHTML);\n"
                + "log(doc.head.outerHTML);\n");
    }

    /**
     * The root element of a detached XML document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<root><child a=\"1&amp;2\">x&lt;y</child></root>")
    public void nodeXmlDocument_outerHtml() throws Exception {
        htmlScript("var doc = document.implementation.createDocument(null, 'root');\n"
                + "var c = doc.createElement('child');\n"
                + "c.setAttribute('a', '1&2');\n"
                + "c.appendChild(doc.createTextNode('x<y'));\n"
                + "doc.documentElement.appendChild(c);\n"
                + "log(doc.documentElement.outerHTML);\n");
    }

    /**
     * Namespaces inside of a detached XML document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<p:root xmlns:p=\"urn:a\"><q:child xmlns:q=\"urn:b\" xmlns:r=\"urn:c\" r:attr=\"v\"/></p:root>",
            FF = "<p:root xmlns:p=\"urn:a\"><q:child xmlns:q=\"urn:b\" r:attr=\"v\" xmlns:r=\"urn:c\"/></p:root>",
            FF_ESR = "<p:root xmlns:p=\"urn:a\"><q:child xmlns:q=\"urn:b\" r:attr=\"v\" xmlns:r=\"urn:c\"/></p:root>")
    public void nodeXmlDocumentNamespaces_outerHtml() throws Exception {
        htmlScript("var doc = document.implementation.createDocument('urn:a', 'p:root');\n"
                + "var c = doc.createElementNS('urn:b', 'q:child');\n"
                + "c.setAttributeNS('urn:c', 'r:attr', 'v');\n"
                + "doc.documentElement.appendChild(c);\n"
                + "log(doc.documentElement.outerHTML);\n");
    }

    /**
     * Svg and math elements inside of HTML elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div><svg><foreignObject><p></p></foreignObject><circle></circle></svg><math></math></div>")
    public void nodeForeignContentInHtml_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var s = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
                + "var fo = document.createElementNS('http://www.w3.org/2000/svg', 'foreignObject');\n"
                + "fo.appendChild(document.createElement('p'));\n"
                + "s.appendChild(fo);\n"
                + "s.appendChild(document.createElementNS('http://www.w3.org/2000/svg', 'circle'));\n"
                + "d.appendChild(s);\n"
                + "d.appendChild(document.createElementNS('http://www.w3.org/1998/Math/MathML', 'math'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Serializing an element without a parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b x=\"y\">t</b>")
    public void nodeElementWithoutParent_outerHtml() throws Exception {
        htmlScript("var b = document.createElement('b');\n"
                + "b.setAttribute('x', 'y');\n"
                + "b.appendChild(document.createTextNode('t'));\n"
                + "log(b.outerHTML);\n");
    }

    /**
     * The serialization after removing, replacing and inserting nodes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<div><i></i><b></b></div>", "<div><a></a><i></i></div>", "<div>x&lt;y</div>"})
    public void nodeAfterDomChanges_outerHtml() throws Exception {
        htmlScript("var d = document.createElement('div');\n"
                + "var a = document.createElement('a');\n"
                + "var b = document.createElement('b');\n"
                + "var c = document.createElement('i');\n"
                + "d.appendChild(a);\n"
                + "d.appendChild(b);\n"
                + "d.replaceChild(c, a);\n"
                + "log(d.outerHTML);\n"
                + "d.insertBefore(a, c);\n"
                + "d.removeChild(b);\n"
                + "log(d.outerHTML);\n"
                + "d.textContent = 'x<y';\n"
                + "log(d.outerHTML);\n");
    }

    // ---------- XHTML document: XML serialization ---------------------------------

    /**
     * The XML serialization escapes the attribute values.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><span title=\"a&quot;b&amp;c&lt;d&gt;e\u00A0f&#9;g&#"
                + "10;h\"></span></div>",
            EDGE = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><span title=\"a&quot;b&amp;c&lt;d&gt;e\u00A0f&#9;g&#"
                + "10;h\"></span></div>",
            FF = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><span title=\"a&quot;b&amp;c&lt;d&gt;e\u00A0f&#9;g&#"
                + "xA;h\"></span></div>",
            FF_ESR = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><span title=\"a&quot;b&amp;c&lt;d&gt;e\u00A0f&#9;g&#"
                + "xA;h\"></span></div>")
    public void attrEscaping_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('title', 'a\"b&c<d>e\\u00a0f\\tg\\nh');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Boolean attributes are written with a value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><button disabled=\"\"></button></div>",
            FF = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><button disabled=\"disabled\"></button></div>",
            FF_ESR = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><button disabled=\"disabled\"></button></div>")
    public void attrBoolean_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var b = document.createElement('button');\n"
                + "b.setAttribute('disabled', '');\n"
                + "d.appendChild(b);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The case of attribute names.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><span TITLE=\"x\" Other=\"y\"></span></div>")
    public void attrNameCase_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttribute('TITLE', 'x');\n"
                + "c.setAttributeNS(null, 'Other', 'y');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Namespaced attributes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><use xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlin"
                + "k=\"http://www.w3.org/1999/xlink\" xlink:href=\"#a\" xml:lang=\"en\"/></div>",
            EDGE = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><use xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlin"
                + "k=\"http://www.w3.org/1999/xlink\" xlink:href=\"#a\" xml:lang=\"en\"/></div>",
            FF = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><use xmlns=\"http://www.w3.org/2000/svg\" xlink:href"
                + "=\"#a\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:lang=\"en\"/></div>",
            FF_ESR = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><use xmlns=\"http://www.w3.org/2000/svg\" xlink:href"
                + "=\"#a\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:lang=\"en\"/></div>")
    public void attrNamespaced_outerHtmlXhtml() throws Exception {
        xhtmlScript("var s = document.createElementNS('http://www.w3.org/2000/svg', 'use');\n"
                + "s.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', '#a');\n"
                + "s.setAttributeNS('http://www.w3.org/XML/1998/namespace', 'xml:lang', 'en');\n"
                + "var d = document.createElement('div');\n"
                + "d.appendChild(s);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Void elements are written as empty elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><br /><img /><input /></div>")
    public void voidElements_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createElement('br'));\n"
                + "d.appendChild(document.createElement('img'));\n"
                + "d.appendChild(document.createElement('input'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A void element with a child.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><br>x</br></div>")
    public void voidElementWithChild_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var b = document.createElement('br');\n"
                + "b.appendChild(document.createTextNode('x'));\n"
                + "d.appendChild(b);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Empty elements that are not void.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><div></div><script></script><textarea></textarea><p>"
                + "</p></div>",
            EDGE = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><div></div><script></script><textarea></textarea><p>"
                + "</p></div>",
            FF = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><div></div><script></script><textarea></textarea><p>"
                + "</p></div>",
            FF_ESR = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><div></div><script></script><textarea></textarea><p>"
                + "</p></div>")
    public void emptyNonVoidElements_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createElement('div'));\n"
                + "d.appendChild(document.createElement('script'));\n"
                + "d.appendChild(document.createElement('textarea'));\n"
                + "d.appendChild(document.createElement('p'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A comment node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><!-- c --></div>")
    public void comment_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createComment(' c '));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A processing instruction.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><?target data=\"1\"?></div>")
    public void processingInstruction_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createProcessingInstruction('target', 'data=\"1\"'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A CDATA section.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><![CDATA[a<b>&c]]></div>")
    public void cdataSection_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createCDATASection('a<b>&c'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The end marker of a CDATA section inside of a text node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\">a]]&gt;b</div>")
    public void cdataSectionEnd_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createTextNode('a]]' + '>b'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The XML serialization escapes text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\">&lt;a&gt; &amp; \"b\" \u00A0 </div>")
    public void textEscaping_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "d.appendChild(document.createTextNode('<a> & \"b\" \\u00a0 \\r\\n'));\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * An element with a prefix.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p:foo xmlns:p=\"urn:x\"><p:bar/></p:foo></div>")
    public void prefixedElement_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElementNS('urn:x', 'p:foo');\n"
                + "c.appendChild(document.createElementNS('urn:x', 'p:bar'));\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A child with another default namespace.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><foo xmlns=\"urn:x\"><bar/><span xmlns=\"http://www."
                + "w3.org/1999/xhtml\"></span></foo></div>",
            EDGE = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><foo xmlns=\"urn:x\"><bar/><span xmlns=\"http://www."
                + "w3.org/1999/xhtml\"></span></foo></div>",
            FF = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><foo xmlns=\"urn:x\"><bar/><span xmlns=\"http://www."
                + "w3.org/1999/xhtml\"></span></foo></div>",
            FF_ESR = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><foo xmlns=\"urn:x\"><bar/><span xmlns=\"http://www."
                + "w3.org/1999/xhtml\"></span></foo></div>")
    public void defaultNamespaceChange_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElementNS('urn:x', 'foo');\n"
                + "c.appendChild(document.createElementNS('urn:x', 'bar'));\n"
                + "c.appendChild(document.createElementNS('http://www.w3.org/1999/xhtml', 'span'));\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A namespace declaration set as attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><span xmlns:foo=\"urn:x\" foo:a=\"1\"></span></div>")
    public void namespaceDeclarationAttribute_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var c = document.createElement('span');\n"
                + "c.setAttributeNS('http://www.w3.org/2000/xmlns/', 'xmlns:foo', 'urn:x');\n"
                + "c.setAttributeNS('urn:x', 'foo:a', '1');\n"
                + "d.appendChild(c);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * An svg element inside of an XHTML element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><svg xmlns=\"http://www.w3.org/2000/svg\"><circle/><"
                + "/svg></div>",
            EDGE = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><svg xmlns=\"http://www.w3.org/2000/svg\"><circle/><"
                + "/svg></div>",
            FF = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><svg xmlns=\"http://www.w3.org/2000/svg\"><circle/><"
                + "/svg></div>",
            FF_ESR = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><svg xmlns=\"http://www.w3.org/2000/svg\"><circle/><"
                + "/svg></div>")
    public void svgInXhtml_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var s = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
                + "s.appendChild(document.createElementNS('http://www.w3.org/2000/svg', 'circle'));\n"
                + "d.appendChild(s);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * The content of a template.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<template xmlns=\"http://www.w3.org/1999/xhtml\"><b></b></template>",
             "<div xmlns=\"http://www.w3.org/1999/xhtml\"><template><b></b></template></div>"})
    public void templateContent_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var t = document.createElement('template');\n"
                + "t.content.appendChild(document.createElement('b'));\n"
                + "d.appendChild(t);\n"
                + "log(t.outerHTML);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Nested elements with attributes.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><ul class=\"x\"><li>a</li></ul></div>")
    public void nestedElements_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var u = document.createElement('ul');\n"
                + "u.setAttribute('class', 'x');\n"
                + "var l = document.createElement('li');\n"
                + "l.appendChild(document.createTextNode('a'));\n"
                + "u.appendChild(l);\n"
                + "d.appendChild(u);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * A pre with a leading line feed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div xmlns=\"http://www.w3.org/1999/xhtml\"><pre> abc</pre></div>")
    public void preLeadingNewline_outerHtmlXhtml() throws Exception {
        xhtmlScript("var d = document.createElement('div');\n"
                + "var e = document.createElement('pre');\n"
                + "e.appendChild(document.createTextNode('\\nabc'));\n"
                + "d.appendChild(e);\n"
                + "log(d.outerHTML);\n");
    }

    /**
     * Serializing an element without a parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b xmlns=\"http://www.w3.org/1999/xhtml\" x=\"y\"></b>")
    public void elementWithoutParent_outerHtmlXhtml() throws Exception {
        xhtmlScript("var b = document.createElement('b');\n"
                + "b.setAttribute('x', 'y');\n"
                + "log(b.outerHTML);\n");
    }

    // ------------------------------------------------------------------ helpers for the scripts above

    private void htmlScript(final String script) throws Exception {
        final String html = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + script
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    private void xhtmlScript(final String script) throws Exception {
        final String xhtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head><title></title>\n"
            + "<script>//<![CDATA[\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + script
            + "  }\n"
            + "//]]></script></head>\n"
            + "<body onload=\"test()\"></body></html>";

        final WebDriver driver = loadPage2(xhtml, URL_FIRST, "application/xhtml+xml", StandardCharsets.UTF_8);
        verifyTitle2(driver, getExpectedAlerts());
    }
}
