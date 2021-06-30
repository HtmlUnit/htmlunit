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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
    @Alerts(DEFAULT = "@media screen {\n  p { background-color: rgb(255, 255, 255); }\n}",
            IE = "@media screen {\n\tp { background-color: rgb(255, 255, 255); }\n}")
    @HtmlUnitNYI(CHROME = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            EDGE = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            FF = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            FF78 = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            IE = "@media screen {p { background-color: rgb(255, 255, 255) } }")
    // FIXME output formatting in rule.cssText -> CSSParser
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
    @Alerts(DEFAULT = "@media screen {\n  p { background-color: rgb(255, 255, 255); }\n}",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            EDGE = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            FF = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            FF78 = "@media screen {p { background-color: rgb(255, 255, 255) } }",
            IE = "@media screen {p { background-color: rgb(255, 255, 255) } }")
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
            + "    log('exception');\n"
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
            + "    log('exception');\n"
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object MediaList]", "screen", "1", "screen", "screen", "screen"},
            IE = {"[object MediaList]", "screen", "1", "screen", "screen", "undefined"})
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
    @Alerts(DEFAULT = {"2", "only screen and (color)", "print", "only screen and (color), print",
                       "only screen and (color), print"},
            IE = {"2", "only screen and (color)", "print", "only screen and (color), print", "undefined"})
    public void mediaQuery() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @media only screen and (color),print { p { background-color:#FFFFFF; }};\n"
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
    @HtmlUnitNYI(CHROME = {"[object CSSRuleList]", "[object CSSRuleList]", "1", "[object CSSStyleRule]",
                           "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            EDGE = {"[object CSSRuleList]", "[object CSSRuleList]", "1", "[object CSSStyleRule]",
                    "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            IE = {"[object CSSRuleList]", "[object CSSRuleList]", "1", "[object CSSStyleRule]",
                  "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            FF = {"[object CSSRuleList]", "[object CSSRuleList]", "1", "[object CSSStyleRule]",
                  "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            FF78 = {"[object CSSRuleList]", "[object CSSRuleList]", "1", "[object CSSStyleRule]",
                    "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"})
    // FIXME output formatting in rule.cssText -> CSSParser
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
    @Alerts(DEFAULT = {"1", "exception"},
            FF = {"1", "0", "2", "span { color: rgb(0, 0, 0); }", "[object CSSMediaRule]",
                  "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"},
            FF78 = {"1", "0", "2", "span { color: rgb(0, 0, 0); }", "[object CSSMediaRule]",
                    "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"})
    @HtmlUnitNYI(FF = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                       "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            FF78 = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                    "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"})
    // FIXME set rule.parentRule in CSSMediaRuleImpl.insertRule(String, int) -> CSSParser
    // FIXME output formatting in rule.cssText -> CSSParser
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
            + "    log('exception');\n"
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
    @HtmlUnitNYI(CHROME = {"1", "1", "2", "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]",
                           "span { color: rgb(0, 0, 0) }", "null"},
            EDGE = {"1", "1", "2", "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]",
                    "span { color: rgb(0, 0, 0) }", "null"},
            IE = {"1", "1", "2", "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]",
                  "span { color: rgb(0, 0, 0) }", "null"},
            FF = {"1", "1", "2", "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]",
                  "span { color: rgb(0, 0, 0) }", "null"},
            FF78 = {"1", "1", "2", "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]",
                    "span { color: rgb(0, 0, 0) }", "null"})
    // FIXME set rule.parentRule in CSSMediaRuleImpl.insertRule(String, int) -> CSSParser
    // FIXME output formatting in rule.cssText -> CSSParser
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1", "exception"},
            IE = {"1", "-1", "1", "p { background-color: rgb(255, 255, 255); }", "[object CSSMediaRule]"})
    @HtmlUnitNYI(IE = {"1", "exception"})
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
            + "    log('exception');\n"
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
    @HtmlUnitNYI(CHROME = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                           "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            EDGE = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                    "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            IE = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                  "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            FF = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                  "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            FF78 = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                    "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"})
    // FIXME set rule.parentRule in CSSMediaRuleImpl.insertRule(String, int) -> CSSParser
    // FIXME output formatting in rule.cssText -> CSSParser
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
    @HtmlUnitNYI(CHROME = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                           "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            EDGE = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                    "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            IE = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                  "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            FF = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                  "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"},
            FF78 = {"1", "0", "2", "span { color: rgb(0, 0, 0) }", "null",
                    "p { background-color: rgb(255, 255, 255) }", "[object CSSMediaRule]"})
    // FIXME set rule.parentRule in CSSMediaRuleImpl.insertRule(String, int) -> CSSParser
    // FIXME output formatting in rule.cssText -> CSSParser
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
    @Alerts({"1", "exception"})
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "exception"})
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
            + "    log('exception');\n"
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
    @HtmlUnitNYI(CHROME = {"2", "1", "p { background-color: rgb(255, 255, 255) }"},
            EDGE = {"2", "1", "p { background-color: rgb(255, 255, 255) }"},
            IE = {"2", "1", "p { background-color: rgb(255, 255, 255) }"},
            FF = {"2", "1", "p { background-color: rgb(255, 255, 255) }"},
            FF78 = {"2", "1", "p { background-color: rgb(255, 255, 255) }"})
    // FIXME output formatting in rule.cssText -> CSSParser
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
            + "    log('exception');\n"
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
    @HtmlUnitNYI(CHROME = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            EDGE = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            IE = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            FF = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            FF78 = {"2", "1", "span { color: rgb(0, 0, 0) }"})
    // FIXME output formatting in rule.cssText -> CSSParser
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
            + "    log('exception');\n"
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
    @HtmlUnitNYI(CHROME = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            EDGE = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            IE = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            FF = {"2", "1", "span { color: rgb(0, 0, 0) }"},
            FF78 = {"2", "1", "span { color: rgb(0, 0, 0) }"})
    // FIXME output formatting in rule.cssText -> CSSParser
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "exception"})
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "exception"})
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
            + "    log('exception');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
