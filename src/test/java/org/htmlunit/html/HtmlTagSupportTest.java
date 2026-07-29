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
package org.htmlunit.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests whether the various {@code BrowserVersion}s still need dedicated
 * support for the legacy tags.
 *
 * @author Ronald Brill
 */
public class HtmlTagSupportTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"BLINK - HTMLUnknownElement", "BLINK - HTMLUnknownElement"})
    public void blink() throws Exception {
        tagSupport("blink");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"COMMENT - HTMLUnknownElement", "COMMENT - HTMLUnknownElement"})
    public void comment() throws Exception {
        tagSupport("comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"DIV - HTMLDivElement", "DIV - HTMLDivElement"})
    public void div() throws Exception {
        tagSupport("div");
    }

    /**
     * The tag actually in question.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"LAYER - HTMLElement", "LAYER - HTMLElement"},
            FF = {"LAYER - HTMLUnknownElement", "LAYER - HTMLUnknownElement"},
            FF_ESR = {"LAYER - HTMLUnknownElement", "LAYER - HTMLUnknownElement"})
    public void layer() throws Exception {
        tagSupport("layer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ILAYER - HTMLUnknownElement", "ILAYER - HTMLUnknownElement"})
    public void ilayer() throws Exception {
        tagSupport("ilayer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"LISTING - HTMLPreElement", "LISTING - HTMLPreElement"})
    public void listing() throws Exception {
        tagSupport("listing");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"MULTICOL - HTMLUnknownElement", "MULTICOL - HTMLUnknownElement"})
    public void multicol() throws Exception {
        tagSupport("multicol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"NEXTID - HTMLUnknownElement", "NEXTID - HTMLUnknownElement"})
    public void nextid() throws Exception {
        tagSupport("nextid");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"NOLAYER - HTMLElement", "NOLAYER - HTMLElement"},
            FF = {"NOLAYER - HTMLUnknownElement", "NOLAYER - HTMLUnknownElement"},
            FF_ESR = {"NOLAYER - HTMLUnknownElement", "NOLAYER - HTMLUnknownElement"})
    public void nolayer() throws Exception {
        tagSupport("nolayer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"PLAINTEXT - HTMLElement", "PLAINTEXT - HTMLElement"})
    public void plaintext() throws Exception {
        tagSupport("plaintext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SOUND - HTMLUnknownElement", "SOUND - HTMLUnknownElement"})
    public void sound() throws Exception {
        tagSupport("sound");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SPACER - HTMLUnknownElement", "SPACER - HTMLUnknownElement"})
    public void spacer() throws Exception {
        tagSupport("spacer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"XMP - HTMLPreElement", "XMP - HTMLPreElement"})
    public void xmp() throws Exception {
        tagSupport("xmp");
    }

    private void tagSupport(final String tagName) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    let el = document.getElementById('t');\n"
                + "    log(el.tagName + ' - ' + el.constructor.name);\n"

                + "    el = document.createElement('" + tagName + "');\n"
                + "    log(el.tagName + ' - ' + el.constructor.name);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<" + tagName + " id='t'></" + tagName + ">"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
