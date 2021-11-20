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
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;

/**
 * Tests for {@link CSSPageRule}.
 *
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CSSPageRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSSPageRule]", "[object CSSPageRule]"})
    public void scriptableToString() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page { margin: 1cm; };\n"
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
    @Alerts(DEFAULT = "@page { margin: 1cm; }",
            IE = "@page  {\n\tmargin: 1cm;\n}")
    public void cssText() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @page { margin: 1cm; }\n"
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
    @Alerts(DEFAULT = "@page { }",
            IE = "@page  {\n\t\n}")
    public void cssTextEmpty() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @page { }\n"
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
    @Alerts(DEFAULT = "@page { margin-left: 4cm; margin-right: 3cm; }",
            IE = "@page  {\n\tmargin-right: 3cm; margin-left: 4cm;\n}")
    @HtmlUnitNYI(IE = "@page  {\n\tmargin-left: 4cm; margin-right: 3cm;\n}")
    public void cssTextMultipleRules() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @page {   margin-left:4cm;margin-right:  3cm; }\n"
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
    @Alerts(DEFAULT = "@page { margin: 1cm; }",
            IE = "@page  {\n\tmargin: 1cm;\n}")
    public void cssTextSet() throws Exception {
        final String html
            = "<html><body>\n"

            + LOG_TEXTAREA

            + "<style>\n"
            + "  @page { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    rule.cssText = '@page { margin: 2cm; }';\n"
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
            + "  @page { margin: 1cm; }\n"
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
            + "  @page { margin: 1cm; }\n"
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
            + "  @page { margin: 1cm; }\n"
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
            + "  @page { margin: 1cm; }\n"
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
    @Alerts(DEFAULT = "",
            FF = "undefined",
            FF_ESR = "undefined")
    public void selectorTextEmpty() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = ":first",
            IE = "",
            FF = "exception",
            FF_ESR = "exception")
    @HtmlUnitNYI(FF = "undefined",
            FF_ESR = "undefined")
    // FIXME FFs do not understand selectors for page rules at all and thus ignore the complete rule during parsing
    public void selectorText() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page :first { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = ":first",
            IE = "",
            FF = "exception",
            FF_ESR = "exception")
    @HtmlUnitNYI(FF = "undefined",
            FF_ESR = "undefined")
    // FIXME FFs do not understand selectors for page rules at all and thus ignore the complete rule during parsing
    public void selectorTextCaseInsensitive() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page :FiRsT { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = {":first", ":left"},
            IE = {"", "exception"},
            FF = "exception",
            FF_ESR = "exception")
    @HtmlUnitNYI(CHROME = {":first", ":first"},
            EDGE = {":first", ":first"},
            IE = {"", ""},
            FF = {"undefined", ":left"},
            FF_ESR = {"undefined", ":left"})
    // FIXME [CSSPARSER] no CSSPageRuleImpl.setSelectorText(String)
    // FIXME FFs do not understand selectors for page rules at all and thus ignore the complete rule during parsing
    public void selectorTextSet() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page :first { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
            + "    rule.selectorText = ':left';\n"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = {":first", "null"},
            IE = {"", "exception"},
            FF = "exception",
            FF_ESR = "exception")
    @HtmlUnitNYI(CHROME = {":first", ":first"},
            EDGE = {":first", ":first"},
            IE = {"", ""},
            FF = {"undefined", "null"},
            FF_ESR = {"undefined", "null"})
    // FIXME [CSSPARSER] no CSSPageRuleImpl.setSelectorText(String)
    // FIXME FFs do not understand selectors for page rules at all and thus ignore the complete rule during parsing
    public void selectorTextSetNull() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page :first { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
            + "    rule.selectorText = null;\n"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = {":first", ""},
            IE = {"", "exception"},
            FF = "exception",
            FF_ESR = "exception")
    @HtmlUnitNYI(CHROME = {":first", ":first"},
            EDGE = {":first", ":first"},
            IE = {"", ""},
            FF = {"undefined", ""},
            FF_ESR = {"undefined", ""})
    // FIXME [CSSPARSER] no CSSPageRuleImpl.setSelectorText(String)
    // FIXME FFs do not understand selectors for page rules at all and thus ignore the complete rule during parsing
    public void selectorTextSetEmpty() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page :first { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
            + "    rule.selectorText = '';\n"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = {":first", ":first"},
            IE = {"", "exception"},
            FF = "exception",
            FF_ESR = "exception")
    @HtmlUnitNYI(IE = {"", ""},
            FF = {"undefined", ":grey"},
            FF_ESR = {"undefined", ":grey"})
    // FIXME FFs do not understand selectors for page rules at all and thus ignore the complete rule during parsing
    public void selectorTextSetInvalid() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page :first { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
            + "    rule.selectorText = ':grey';\n"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = {":first", ":left"},
            IE = {"", "exception"},
            FF = "exception",
            FF_ESR = "exception")
    @HtmlUnitNYI(CHROME = {":first", ":first"},
            EDGE = {":first", ":first"},
            IE = {"", ""},
            FF = {"undefined", ":LeFt"},
            FF_ESR = {"undefined", ":LeFt"})
    // FIXME [CSSPARSER] no CSSPageRuleImpl.setSelectorText(String)
    // FIXME FFs do not understand selectors for page rules at all and thus ignore the complete rule during parsing
    public void selectorTextSetCaseInsensitive() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page :first { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  try {"
            + "    log(rule.selectorText);\n"
            + "    rule.selectorText = ':LeFt';\n"
            + "    log(rule.selectorText);\n"
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
    @Alerts(DEFAULT = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "4", "[object CSSPageRule]",
                       "margin: 1cm;", "margin-top", "margin-right", "margin-bottom", "margin-left"},
            FF = {"[object CSS2Properties]", "[object CSS2Properties]", "4", "[object CSSPageRule]",
                  "margin: 1cm;", "margin-top", "margin-right", "margin-bottom", "margin-left"},
            FF_ESR = {"[object CSS2Properties]", "[object CSS2Properties]", "4", "[object CSSPageRule]",
                    "margin: 1cm;", "margin-top", "margin-right", "margin-bottom", "margin-left"})
    @HtmlUnitNYI(CHROME = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]",
                           "1", "[object CSSPageRule]", "margin: 1cm;", "margin: 1cm"},
            EDGE = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]",
                    "1", "[object CSSPageRule]", "margin: 1cm;", "margin: 1cm"},
            FF = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]",
                  "1", "[object CSSPageRule]", "margin: 1cm;", "margin: 1cm"},
            FF_ESR = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]",
                    "1", "[object CSSPageRule]", "margin: 1cm;", "margin: 1cm"},
            IE = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]",
                  "1", "[object CSSPageRule]", "margin: 1cm;", "margin: 1cm"})
    // FIXME FF returns CSS2Properties vs. default returns CSSStyleDeclaration :(
    public void style() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page { margin: 1cm; }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var style = rule.style;\n"
            + "  log(Object.prototype.toString.call(style));\n"
            + "  log(style);\n"
            + "  log(style.length);\n"
            + "  log(style.parentRule);\n"
            + "  log(style.cssText);\n"
            + "  for (var i = 0; i < style.length; i++) {\n"
            + "    log(style.item(i));\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "0", "[object CSSPageRule]", ""},
            FF = {"[object CSS2Properties]", "[object CSS2Properties]", "0", "[object CSSPageRule]", ""},
            FF_ESR = {"[object CSS2Properties]", "[object CSS2Properties]", "0", "[object CSSPageRule]", ""})
    @HtmlUnitNYI(FF = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "0", "[object CSSPageRule]", ""},
            FF_ESR = {"[object CSSStyleDeclaration]", "[object CSSStyleDeclaration]", "0", "[object CSSPageRule]", ""})
    // FIXME FF returns CSS2Properties vs. default returns CSSStyleDeclaration :(
    public void styleEmpty() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>\n"
            + "  @page { }\n"
            + "</style>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  var rule = styleSheet.cssRules[0];\n"
            + "  var style = rule.style;\n"
            + "  log(Object.prototype.toString.call(style));\n"
            + "  log(style);\n"
            + "  log(style.length);\n"
            + "  log(style.parentRule);\n"
            + "  log(style.cssText);\n"
            + "  for (var i = 0; i < style.length; i++) {\n"
            + "    log(style.item(i));\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
