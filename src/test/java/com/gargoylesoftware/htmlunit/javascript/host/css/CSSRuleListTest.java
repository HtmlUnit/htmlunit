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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link CSSRuleList}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CSSRuleListTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object CSSStyleRule]"})
    public void ruleList() throws Exception {
        final String html = "<html><head>\n"
                + "<style>\n"
                + "  BODY { font-size: 1234px; }\n"
                + "</style>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var rules = document.styleSheets[0].cssRules;\n"
                + "    log(rules.length);\n"
                + "    log(rules[0]);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("undefined")
    public void wrongRuleListAccess() throws Exception {
        final String html = "<html><head>\n"
                + "<style>\n"
                + "  BODY { font-size: 1234px; }\n"
                + "</style>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var rules = document.styleSheets[0].cssRules;\n"
                + "    var r = rules[1];\n"
                + "    log(r);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("true")
    public void has() throws Exception {
        final String html = "<html><head>\n"
                + "<style>\n"
                + "  BODY { font-size: 1234px; }\n"
                + "</style>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var rules = document.styleSheets[0].cssRules;\n"
                + "    log(0 in rules);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"0", "undefined"})
    public void ruleListUnknownAtRule() throws Exception {
        final String html = "<html><head>\n"
                + "<style>\n"
                + "  @UnknownAtRule valo-animate-in-fade {0 {opacity: 0;}}\n"
                + "</style>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var rules = document.styleSheets[0].cssRules;\n"
                + "    log(rules.length);\n"
                + "    log(rules[0]);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "[object CSSKeyframesRule]"})
    public void ruleListKeyframes() throws Exception {
        final String html = "<html><head>\n"
                + "<style>\n"
                + "  @keyframes mymove {from {top: 0px;} to {top: 200px;}}\n"
                + "</style>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var rules = document.styleSheets[0].cssRules;\n"
                + "    log(rules.length);\n"
                + "    log(rules[0]);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "false", "true", "false", "false"})
    public void in() throws Exception {
        final String html = "<html><head>\n"
                + "<style>\n"
                + "  BODY { font-size: 1234px; }\n"
                + "</style>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var rules = document.styleSheets[0].cssRules;\n"
                + "    log(rules.length);\n"
                + "    log(-1 in rules);\n"
                + "    log(0 in rules);\n"
                + "    log(1 in rules);\n"
                + "    log(42 in rules);\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("[object CSSStyleRule]")
    public void item() throws Exception {
        final String html = "<html><head>\n"
                + "<style>\n"
                + "  BODY { font-size: 1234px; }\n"
                + "</style>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var rules = document.styleSheets[0].cssRules;\n"
                + "    log(rules.item(0));\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
