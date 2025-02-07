/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link CSSMediaRule}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSMediaRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void ctor() throws Exception {
        final String html
            = "<html><body>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var rule = new CSSMediaRule();\n"
            + "  log(rule);\n"
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }
    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSSMediaRule]", "[object CSSMediaRule]"})
    public void scriptableToString() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(Object.prototype.toString.call(rule));\n"
            + "  log(rule);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@media screen {\n  p { background-color: rgb(255, 255, 255); }\n}")
    public void cssText() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.cssText);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@media screen {\n}")
    public void cssTextEmpty() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @media screen {};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.cssText);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@media screen {\n  p { }\n  div { }\n}")
    public void cssTextMultipleRules() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @media screen { p {} div {}};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.cssText);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@media print {\n  #navigation { display: none; }"
            + "\n  @media (max-width: 12cm) {\n  .note { float: none; }\n}\n}")
    @HtmlUnitNYI(CHROME = "@media print {\n  *#navigation { display: none; }"
                    + "\n  @media (max-width: 12cm) {\n  *.note { float: none; }\n}\n}",
            EDGE = "@media print {\n  *#navigation { display: none; }"
                    + "\n  @media (max-width: 12cm) {\n  *.note { float: none; }\n}\n}",
            FF = "@media print {\n  *#navigation { display: none; }"
                    + "\n  @media (max-width: 12cm) {\n  *.note { float: none; }\n}\n}",
            FF_ESR = "@media print {\n  *#navigation { display: none; }"
                    + "\n  @media (max-width: 12cm) {\n  *.note { float: none; }\n}\n}")
    public void cssTextNested() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @media print { #navigation { display: none }"
                    + "@media (max-width: 12cm) { .note { float: none } } }"
            + "</style>\n"

            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.cssText);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("@media screen {\n  p { background-color: rgb(255, 255, 255); }\n}")
    public void cssTextSet() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    rule.cssText = '@media screen { span { color: rgb(0, 0, 0); }}';\n"
            + "    log(rule.cssText);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void parentRule() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.parentRule);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSSMediaRule]", "[object CSSMediaRule]"})
    public void parentRuleNested() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media print { #navigation { display: none; } "
                    + "@media (max-width: 12cm) { .note { float: none; } } }"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var ruleOuter = styleSheet.cssRules[0];\n"
            + "  var ruleInner = ruleOuter.cssRules[1];\n"
            + "  log(ruleInner);\n"
            + "  log(ruleInner.parentRule);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("null")
    public void parentRuleSet() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    rule.parentRule = rule;\n"
            + "    log(rule.parentRule);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object CSSStyleSheet]")
    public void parentStyleSheet() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  log(rule.parentStyleSheet);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object CSSStyleSheet]")
    public void parentStyleSheetSet() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    rule.parentStyleSheet = null;\n"
            + "    log(rule.parentStyleSheet);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object MediaList]", "all", "1", "all", "all", "all"})
    public void mediaAll() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media all { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var mediaList = rule.media;\n"
            + "  log(Object.prototype.toString.call(mediaList));\n"
            + "  log(mediaList);\n"
            + "  log(mediaList.length);\n"
            + "  for (var i = 0; i < mediaList.length; i++) {\n"
            + "    log(mediaList.item(i));\n"
            + "  }\n"
            + "  log(mediaList.mediaText);\n"
            + "  log(rule.conditionText);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object MediaList]", "screen", "1", "screen", "screen", "screen"})
    public void media() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var mediaList = rule.media;\n"
            + "  log(Object.prototype.toString.call(mediaList));\n"
            + "  log(mediaList);\n"
            + "  log(mediaList.length);\n"
            + "  for (var i = 0; i < mediaList.length; i++) {\n"
            + "    log(mediaList.item(i));\n"
            + "  }\n"
            + "  log(mediaList.mediaText);\n"
            + "  log(rule.conditionText);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "only screen and (color)", "print and (max-width: 12cm) and (min-width: 30em)",
             "only screen and (color), print and (max-width: 12cm) and (min-width: 30em)",
             "only screen and (color), print and (max-width: 12cm) and (min-width: 30em)"})
    public void mediaQuery() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media only screen and  (color ),print and ( max-width:12cm) and (min-width: 30em) { "
                    + "p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var mediaList = rule.media;\n"
            + "  log(mediaList.length);\n"
            + "  for (var i = 0; i < mediaList.length; i++) {\n"
            + "    log(mediaList.item(i));\n"
            + "  }\n"
            + "  log(mediaList.mediaText);\n"
            + "  log(rule.conditionText);\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSSRuleList]", "[object CSSRuleList]", "1", "[object CSSStyleRule]",
             "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"})
    public void cssRules() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(Object.prototype.toString.call(rules));\n"
            + "  log(rules);\n"
            + "  log(rules.length);\n"
            + "  for (var i = 0; i < rules.length; i++) {\n"
            + "    log(rules.item(i));\n"
            + "    log(rules.item(i).cssText);\n"
            + "    log(rules.item(i).parentRule);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSSRuleList]", "[object CSSRuleList]", "1", "[object CSSStyleRule]",
             "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"})
    public void cssRulesMediaNotMatching() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media print { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(Object.prototype.toString.call(rules));\n"
            + "  log(rules);\n"
            + "  log(rules.length);\n"
            + "  for (var i = 0; i < rules.length; i++) {\n"
            + "    log(rules.item(i));\n"
            + "    log(rules.item(i).cssText);\n"
            + "    log(rules.item(i).parentRule);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "0", "2", "span { color: rgb(0, 0, 0); }", "[object CSSMediaRule]",
             "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"})
    public void insertRule() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    log(rule.insertRule('span { color:#000000; }'));\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "      log(rules.item(i).parentRule);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void insertRuleNull() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  try {\n"
            + "    rule.insertRule(null);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void insertRuleEmpty() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  try {\n"
            + "    rule.insertRule('');\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void insertRuleInvalid() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  try {\n"
            + "    rule.insertRule('%ab');\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "1", "2", "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]",
             "span { color: rgb(0, 0, 0); }", "[object CSSMediaRule]"})
    public void insertRuleWithIndex() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    log(rule.insertRule('span { color:#000000; }', 1));\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "      log(rules.item(i).parentRule);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void insertRuleNullWithIndex() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  try {\n"
            + "    rule.insertRule(null, 1);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "SyntaxError/DOMException"})
    public void insertRuleEmptyWithIndex() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    log(rule.insertRule('', 1));\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "      log(rules.item(i).parentRule);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void insertRuleInvalidWithIndex() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  try {\n"
            + "    rule.insertRule('%ab', 1);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "0", "2", "span { color: rgb(0, 0, 0); }", "[object CSSMediaRule]",
             "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"})
    public void insertRuleWithIndexNull() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    log(rule.insertRule('span { color:#000000; }', null));\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "      log(rules.item(i).parentRule);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    log('exception'+e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "0", "2", "span { color: rgb(0, 0, 0); }", "[object CSSMediaRule]",
             "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"})
    public void insertRuleWithIndexNaN() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    log(rule.insertRule('span { color:#000000; }', 'abc'));\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "      log(rules.item(i).parentRule);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    log('exception'+e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "IndexSizeError/DOMException"})
    public void insertRuleWithIndexNegative() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    rule.insertRule('span { color:#000000; }', 2);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "IndexSizeError/DOMException"})
    public void insertRuleWithIndexGreaterThanLength() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    rule.insertRule('span { color:#000000; }', 2);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "1", "p { background-color: rgb(255, 255, 255); }"})
    public void deleteRule() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; } span { color: rgb(0, 0, 0); }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    rule.deleteRule(1);\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "1", "span { color: rgb(0, 0, 0); }"})
    public void deleteRuleNull() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; } span { color: rgb(0, 0, 0); }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    rule.deleteRule(null);\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "1", "span { color: rgb(0, 0, 0); }"})
    public void deleteRuleNaN() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; } span { color: rgb(0, 0, 0); }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    rule.deleteRule('abc');\n"
            + "    log(rules.length);\n"
            + "    for (var i = 0; i < rules.length; i++) {\n"
            + "      log(rules.item(i).cssText);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "IndexSizeError/DOMException"})
    public void deleteRuleNegative() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; } span { color: rgb(0, 0, 0); }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    rule.deleteRule(-1);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "IndexSizeError/DOMException"})
    public void deleteRuleGreaterThanLength() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media screen { p { background-color:#FFFFFF; } span { color: rgb(0, 0, 0); }};\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var rules = rule.cssRules;\n"
            + "  log(rules.length);\n"
            + "  try {\n"
            + "    rule.deleteRule(2);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
