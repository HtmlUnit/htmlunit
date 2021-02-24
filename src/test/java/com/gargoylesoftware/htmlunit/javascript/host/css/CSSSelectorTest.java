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
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Tests for CSS selectors.
 *
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSSelectorTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0"})
    public void querySelectorAll_nullUndefined() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll(null).length);\n"
            + "  log(document.querySelectorAll(undefined).length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"exception", "exception"})
    public void querySelectorAll_emptyString() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    log(document.querySelectorAll(''));\n"
            + "  } catch(e) {log('exception')}\n"
            + "  try {\n"
            + "    log(document.querySelectorAll('  '));\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for bug 1287.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"li2", "li1", "li2", "li1", "li3", "li1", "2", "li1", "li2"})
    public void nth_child() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll('li:nth-child(2)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(n)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(2n)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(2n+1)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(2n+1)')[1].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(2n-1)')[0].id);\n"

            + "  log(document.querySelectorAll('li:nth-child(-n+2)').length);\n"
            + "  log(document.querySelectorAll('li:nth-child(-n+2)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(-n+2)')[1].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "li2", "2", "li1", "li3"})
    public void nth_child_even_odd() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll('li:nth-child(even)').length);\n"
            + "  log(document.querySelectorAll('li:nth-child(eVen)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(odd)').length);\n"
            + "  log(document.querySelectorAll('li:nth-child(OdD)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-child(ODD)')[1].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "[object HTMLBodyElement]", "1", "0"})
    public void childSelector_html_body() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll('html > body').length);\n"
            + "  log(document.querySelectorAll('html > body')[0]);\n"
            + "  log(document.querySelectorAll('  \\t\\r\\n  html > body  \\t\\r\\n  ').length);\n"

            + "  elem = document.getElementById('root');\n"
            + "  log(elem.querySelectorAll('html > body').length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div id='root'>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void nth_child_no_argument() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    log(document.querySelectorAll('li:nth-child()'));\n"
            + "  } catch (e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"li1", "li4", "li7", "li10"})
    public void nth_child_equation() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('li:nth-child(3n+1)');\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "  <li id='li4'></li>\n"
            + "  <li id='li5'></li>\n"
            + "  <li id='li6'></li>\n"
            + "  <li id='li7'></li>\n"
            + "  <li id='li8'></li>\n"
            + "  <li id='li9'></li>\n"
            + "  <li id='li10'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Exception should be thrown for an invalid selector.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void invalid() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    log(document.querySelectorAll('td:gt(4)').length);\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "ul2"})
    public void directAdjacentSelector() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('p+ul');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='ul1'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='ul2'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "thing1"})
    public void prefixAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[id^=\"thing\"]');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing1'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void prefixAttributeEmpty() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[id^=\"\"]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].outerHTML.replace(/^\\s+|\\s+$/g, ''));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing1'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "something"})
    public void suffixAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[id$=\"thing\"]');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing2'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void suffixAttributeEmpty() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[id$=\"\"]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].outerHTML.replace(/^\\s+|\\s+$/g, ''));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing2'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "something", "thing2"})
    public void substringAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[id*=\"thing\"]');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"
            + "  log(list[1].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing2'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void substringAttributeEmpty() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[id*=\"\"]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].outerHTML.replace(/^\\s+|\\s+$/g, ''));\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='something'></ul>\n"
            + "  <p></p>\n"
            + "  <ul id='thing2'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "id1", "id2"})
    public void oneOfAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[title~=\"w2\"]');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"
            + "  log(list[1].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='id1' title='w1 w2 w3'></ul>\n"
            + "  <p id='id2' title='w2'></p>\n"
            + "  <ul id='id3' title='w1w2 w3'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void oneOfAttributeEmpty() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[title~=\"\"]');\n"
            + "  log(list.length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='id1' title='w1 w2 w3'></ul>\n"
            + "  <p id='id2' title='w2'></p>\n"
            + "  <ul id='id3' title='w1w2 w3'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "id2", "id3"})
    public void hasAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[title]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='id1'></ul>\n"
            + "  <p id='id2' title='w2'></p>\n"
            + "  <ul id='id3' title=''></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"5", "id1", "id2", "id5", "id6", "id7"})
    public void hyphenSeparatedAttributeValue() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[title|=\"abc\"]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='id1' title='abc'></ul>\n"
            + "  <p id='id2' title='abc-def'></p>\n"
            + "  <p id='id3' title='x-abc-def'></p>\n"
            + "  <p id='id4' title='abc -def'></p>\n"
            + "  <p id='id5' title='abc- def'></p>\n"
            + "  <p id='id6' title='abc-def gh'></p>\n"
            + "  <p id='id7' title='abc-def-gh'></p>\n"
            + "  <p id='id8' title='xabc'></p>\n"
            + "  <ul id='id9' title='abcd'></ul>\n"
            + "  <p id='id10' title='abc def'></p>\n"
            + "  <p id='id11' title=' abc-def gh'></p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "id1", "id4"})
    public void hyphenSeparatedAttributeValueHyphenInSelector() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[title|=\"ab-c\"]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='id1' title='ab-c'></ul>\n"
            + "  <p id='id2' title='ab-cd'></p>\n"
            + "  <ul id='id3' title='ab-c d'></ul>\n"
            + "  <p id='id4' title='ab-c-d'></p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "id2", "id6"})
    public void hyphenSeparatedAttributeValueEmpty() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[title|=\"\"]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='id1' title='abc'></ul>\n"
            + "  <p id='id2' title=''></p>\n"
            + "  <ul id='id3' title=' '></ul>\n"
            + "  <p id='id4' title=' -abc'></p>\n"
            + "  <p id='id5' title=' -abc'></p>\n"
            + "  <p id='id6' title='-abc'></p>\n"
            + "  <p id='id7' title='\\t'></p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "id3"})
    public void emptyAttributeValue() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('[title=\"\"]');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <ul id='id1' title='w1'></ul>\n"
            + "  <p id='id2' title=' '></p>\n"
            + "  <ul id='id3' title=''></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "ul2", "ul3"})
    public void generalAdjacentSelector() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('div~ul');\n"
            + "  log(list.length);\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    log(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div></div>\n"
            + "  <p></p>\n"
            + "  <ul id='ul2'></ul>\n"
            + "  <ul id='ul3'></ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"li3", "2", "li1", "li3"})
    public void nth_last_child() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll('li:nth-last-child(1)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-last-child(odd)').length);\n"
            + "  log(document.querySelectorAll('li:nth-last-child(odd)')[0].id);\n"
            + "  log(document.querySelectorAll('li:nth-last-child(odd)')[1].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "div1", "div3", "2", "div1", "div3", "2", "div1", "div3", "0"})
    public void nth_last_child2() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll('.nthchild1 > :nth-last-child(odd)').length);\n"
            + "  log(document.querySelectorAll('.nthchild1 > :nth-last-child(odd)')[0].id);\n"
            + "  log(document.querySelectorAll('.nthchild1 > :nth-last-child(odd)')[1].id);\n"

            + "  elem = document.getElementById('root');\n"
            + "  log(elem.querySelectorAll('.nthchild1 > :nth-last-child(odd)').length);\n"
            + "  log(elem.querySelectorAll('.nthchild1 > :nth-last-child(odd)')[0].id);\n"
            + "  log(elem.querySelectorAll('.nthchild1 > :nth-last-child(odd)')[1].id);\n"

            + "  elem = document.getElementById('parent');\n"
            + "  log(elem.querySelectorAll('.nthchild1 > :nth-last-child(odd)').length);\n"
            + "  log(elem.querySelectorAll('.nthchild1 > :nth-last-child(odd)')[0].id);\n"
            + "  log(elem.querySelectorAll('.nthchild1 > :nth-last-child(odd)')[1].id);\n"

            + "  elem = document.getElementById('div1');\n"
            + "  log(elem.querySelectorAll('.nthchild1 > :nth-last-child(odd)').length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div id='root'>\n"
            + "  <div id='parent' class='nthchild1'>\n"
            + "    <div id='div1'>1</div>\n"
            + "    <div id='div2'>2</div>\n"
            + "    <div id='div3'>3</div>\n"
            + "  </div>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id3")
    public void nth_of_type() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('p:nth-of-type(2)')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<section>\n"
            + "  <h1 id='id11'></h1>\n"
            + "  <p id='id2'></p>\n"
            + "  <p id='id3'></p>\n"
            + "</section>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id3")
    public void nth_last_of_type() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('p:nth-last-of-type(1)')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<section>\n"
            + "  <h1 id='id1'></h1>\n"
            + "  <p  id='id2'></p>\n"
            + "  <p  id='id3'></p>\n"
            + "</section>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void pseudoAfter() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Pseudo-After</title><script>\n"
            + "function test() {\n"
            + "  var list = document.querySelectorAll('#li1:after');\n"
            + "  for (var i = 0 ; i < list.length; i++) {\n"
            + "    alert(list[i].id);\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "checkbox2", "1", "checkbox2"})
    public void pseudoCheckboxChecked() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('input[type=checkbox]:checked');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"

            + "  var list = document.querySelectorAll('#t2 > input[type=checkbox]:checked');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='t2'>\n"
            + "    <input type='checkbox' name='checkbox1' id='checkbox1' value='foo'>\n"
            + "    <input type='checkbox' name='checkbox2' id='checkbox2' value='bar' checked>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "radio2", "1", "radio2"})
    public void pseudoRadioChecked() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll('input[type=radio]:checked');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"

            + "  var list = document.querySelectorAll('#t2 > input[type=radio]:checked');\n"
            + "  log(list.length);\n"
            + "  log(list[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='t2'>\n"
            + "    <input type='radio' name='radio1' id='radio1' value='foo'>\n"
            + "    <input type='radio' name='radio2' id='radio2' value='bar' checked>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("li1")
    public void first_child() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('li:first-child')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("li3")
    public void last_child() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('li:last-child')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "  <li id='li3'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id2")
    public void first_of_type() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('p:first-of-type')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<section>\n"
            + "  <h1 id='id1'></h1>\n"
            + "  <p  id='id2'></p>\n"
            + "  <h1 id='id3'></h1>\n"
            + "  <p  id='id4'></p>\n"
            + "  <h1 id='id5'></h1>\n"
            + "</section>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * See http://dev.w3.org/csswg/selectors3/#negation and
     * http://dev.w3.org/csswg/selectors3/#simple-selectors-dfn.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "link_2", "link_3"},
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception")
    public void invalid_not() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var found = document.querySelectorAll('p a:not(a:first-of-type)');\n"
            + "    log(found.length);\n"
            + "    log(found[0].id);\n"
            + "    log(found[1].id);\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<p>\n"
            + "  <strong id='strong'>This</strong> is a short blurb\n"
            + "  <a id='link_1' href='#'>with a link</a> or\n"
            + "  <a id='link_2' href='#'>two</a>.\n"
            + "  <a id='link_3' href='#'>three</a>.\n"
            + "  Or <cite id='with_title' title='hello world!'>a citation</cite>.\n"
            + "</p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id4")
    public void last_of_type() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('p:last-of-type')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<section>\n"
            + "  <h1 id='id1'></h1>\n"
            + "  <p  id='id2'></p>\n"
            + "  <h1 id='id3'></h1>\n"
            + "  <p  id='id4'></p>\n"
            + "  <h1 id='id5'></h1>\n"
            + "</section>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id3")
    public void only_child() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('h1:only-child')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<section>\n"
            + "  <h1 id='id1'></h1>\n"
            + "  <p  id='id2'></p>\n"
            + "</section>\n"
            + "<section>\n"
            + "  <h1 id='id3'></h1>\n"
            + "</section>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id3")
    public void only_of_type() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('p:only-of-type')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<section>\n"
            + "  <p  id='id1'></p>\n"
            + "  <p  id='id2'></p>\n"
            + "</section>\n"
            + "<section>\n"
            + "  <p  id='id3'></p>\n"
            + "</section>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"id2", "span1"})
    public void empty() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.querySelectorAll('p:empty')[0].id);\n"
            + "  log(document.querySelectorAll('span:empty')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <p id='id1'>Hello, World!</p>\n"
            + "  <p id='id2'></p>\n"
            + "  <span id='span1'><!-- a comment --></span>\n"
            + "  <span id='span2'>a text</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("id2")
    public void not() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.querySelectorAll('input:not([type=\"file\"])')[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1' type='file'>\n"
            + "  <input id='id2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "id2",
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception")
    public void notWithFirstOfType() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    alert(document.querySelectorAll('div:not(div:first-of-type)')[0].id);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='id1'>1</div>\n"
            + "  <div id='id2'>2</div>\n"
            + "  <div id='id3'>3</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "id2", "id3", "2", "id1", "id3", "2", "id1", "id2",
                    "3", "id1", "id2", "id3"},
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception")
    public void notWithNthOfType() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = document.querySelectorAll('div:not(div:nth-of-type(1))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"

            + "    res = document.querySelectorAll('div:not(div:nth-of-type(2))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"

            + "    res = document.querySelectorAll('div:not(div:nth-of-type(3))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"

            + "    res = document.querySelectorAll('div:not(div:nth-of-type(4))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"
            + "    log(res[2].id);\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='id1'>1</div>\n"
            + "  <div id='id2'>2</div>\n"
            + "  <div id='id3'>3</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "id2",
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception")
    public void notWithLastOfType() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    alert(document.querySelectorAll('div:not(div:last-of-type)')[1].id);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='id1'>1</div>\n"
            + "  <div id='id2'>2</div>\n"
            + "  <div id='id3'>3</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "id1", "id2", "2", "id1", "id3", "2", "id2", "id3",
                    "3", "id1", "id2", "id3"},
            FF78 = "exception",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception")
    public void notWithNthLastOfType() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = document.querySelectorAll('div:not(div:nth-last-of-type(1))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"

            + "    res = document.querySelectorAll('div:not(div:nth-last-of-type(2))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"

            + "    res = document.querySelectorAll('div:not(div:nth-last-of-type(3))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"

            + "    res = document.querySelectorAll('div:not(div:nth-last-of-type(4))');\n"
            + "    log(res.length);\n"
            + "    log(res[0].id);\n"
            + "    log(res[1].id);\n"
            + "    log(res[2].id);\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='id1'>1</div>\n"
            + "  <div id='id2'>2</div>\n"
            + "  <div id='id3'>3</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "item_2", "item_3"})
    public void childNot() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=9'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var res = document.querySelectorAll('#list li:not(#item_1)');\n"
            + "  log(res.length);\n"
            + "  log(res[0].id);\n"
            + "  log(res[1].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <ul id='list'>\n"
            + "    <li id='item_1'>1</li>\n"
            + "    <li id='item_2'>2</li>\n"
            + "    <li id='item_3'>3</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "item_2"})
    public void childNotNot() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=9'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var res = document.querySelectorAll('#list li:not(#item_1):not(#item_3)');\n"
            + "  log(res.length);\n"
            + "  log(res[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <ul id='list'>\n"
            + "    <li id='item_1'>1</li>\n"
            + "    <li id='item_2'>2</li>\n"
            + "    <li id='item_3'>3</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "undefined", "1", "[object HTMLInputElement]", "id2"},
            IE = {"1", "[object HTMLBodyElement]", "1", "[object HTMLInputElement]", "id2"})
    public void focus() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  found = document.querySelectorAll(':focus');\n"
            + "  log(found.length);\n"
            + "  log(found[0]);\n"
            + "\n"
            + "  document.getElementById('id2').focus();\n"
            + "\n"
            + "  found = document.querySelectorAll(':focus');\n"
            + "  log(found.length);\n"
            + "  log(found[0]);\n"
            + "  log(found[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1'>\n"
            + "  <input id='id2'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"5", "cb1", "rd1", "sl1", "ml1", "ml3"})
    public void checked() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  found = document.querySelectorAll(':checked');\n"
            + "  log(found.length);\n"
            + "  for (var i = 0; i < found.length; i++) { log(found[i].id); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1'>\n"
            + "  <input id='id2' disabled='disabled'>\n"
            + "  <input id='id3' type'hidden'>\n"
            + "  <input type='checkbox' name='checkboxes' id='cb1' checked='checked' value='On' />\n"
            + "  <input type='checkbox' name='checkboxes' id='cb2' value='Off' />\n"
            + "  <input type='radio' name='radiobuttons' id='rd1' checked='checked' value='On' />\n"
            + "  <input type='radio' name='radiobuttons' id='rd2' value='Off' />\n"
            + "  <select name='sl'>\n"
            + "    <option value='sl1' id='sl1' selected='selected'>SL One</option>\n"
            + "    <option value='sl2' id='sl2' >SL Two</option>\n"
            + "  </select>\n"
            + "  <select name='ml'  multiple=multiple'>\n"
            + "    <option value='ml1' id='ml1' selected='selected'>ML One</option>\n"
            + "    <option value='ml2' id='ml2' >ML Two</option>\n"
            + "    <option value='ml3' id='ml3' selected='selected'>ML Three</option>\n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "cb1", "rd1", "2", "cb2", "rd2"})
    public void checkedChanged() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  found = document.querySelectorAll(':checked');\n"
            + "  log(found.length);\n"
            + "  for (var i = 0; i < found.length; i++) { log(found[i].id); }\n"

            + "  document.getElementById('cb1').checked = false;\n"
            + "  document.getElementById('cb2').checked = true;\n"
            + "  document.getElementById('rd1').checked = false;\n"
            + "  document.getElementById('rd2').checked = true;\n"
            + "  found = document.querySelectorAll(':checked');\n"
            + "  log(found.length);\n"
            + "  for (var i = 0; i < found.length; i++) { log(found[i].id); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='checkbox' name='checkboxes' id='cb1' checked='checked' value='On' />\n"
            + "  <input type='checkbox' name='checkboxes' id='cb2' value='Off' />\n"
            + "  <input type='radio' name='radiobuttons' id='rd1' checked='checked' value='On' />\n"
            + "  <input type='radio' name='radiobuttons' id='rd2' value='Off' />\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "cb1", "rd1", "2", "cb1", "rd1"})
    public void checkedAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  found = document.querySelectorAll('[checked]');\n"
            + "  log(found.length);\n"
            + "  for (var i = 0; i < found.length; i++) { log(found[i].id); }\n"

            + "  document.getElementById('cb1').checked = false;\n"
            + "  document.getElementById('cb2').checked = true;\n"
            + "  document.getElementById('rd1').checked = false;\n"
            + "  document.getElementById('rd2').checked = true;\n"
            + "  found = document.querySelectorAll('[checked]');\n"
            + "  log(found.length);\n"
            + "  for (var i = 0; i < found.length; i++) { log(found[i].id); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='checkbox' name='checkboxes' id='cb1' checked='checked' value='On' />\n"
            + "  <input type='checkbox' name='checkboxes' id='cb2' value='Off' />\n"
            + "  <input type='radio' name='radiobuttons' id='rd1' checked='checked' value='On' />\n"
            + "  <input type='radio' name='radiobuttons' id='rd2' value='Off' />\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "1-iy", "1-iy", "2", "1-iy", "1-iz"})
    public void selectedChecked() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(document.getElementById('s1').selectedIndex);\n"
            + "  var sel = document.querySelectorAll('[selected]');\n"
            + "  log(sel.length + '-' + sel[0].id);\n"
            + "  sel = document.querySelectorAll(':checked');\n"
            + "  log(sel.length + '-' + sel[0].id);\n"

            + "  document.getElementById('iz').selected = 'selected';\n"
            + "  log(document.getElementById('s1').selectedIndex);\n"
            + "  var sel = document.querySelectorAll('[selected]');\n"
            + "  log(sel.length + '-' + sel[0].id);\n"
            + "  sel = document.querySelectorAll(':checked');\n"
            + "  log(sel.length + '-' + sel[0].id);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <select id='s1'>\n"
            + "    <option id='ix' value='x'>x</option>\n"
            + "    <option id='iy' value='y' selected>y</option> \n"
            + "    <option id='iz' value='z'>z</option> \n"
            + "  </select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "id1", "id3"})
    public void enabled() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  found = document.querySelectorAll('input:enabled');\n"
            + "  log(found.length);\n"
            + "  for (var i = 0; i < found.length; i++) { log(found[i].id); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1'>\n"
            + "  <input id='id2' disabled='disabled'>\n"
            + "  <input id='id3' type'hidden'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "id2"})
    public void disabled() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  found = document.querySelectorAll('input:disabled');\n"
            + "  log(found.length);\n"
            + "  for (var i = 0; i < found.length; i++) { log(found[i].id); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1' >\n"
            + "  <input id='id2' disabled='disabled'>\n"
            + "  <input id='id3' type'hidden'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "id2"})
    public void target() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  found = document.querySelectorAll(':target');\n"
            + "  alert(found.length);\n"
            + "  if (found.length > 0) { alert(found[0].id); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1' >\n"
            + "  <input id='id2'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        loadPageWithAlerts2(UrlUtils.getUrlWithNewRef(URL_FIRST, "id2"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void targetNoHash() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  found = document.querySelectorAll(':target');\n"
            + "  alert(found.length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1' >\n"
            + "  <input id='id2'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void targetUnknown() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  found = document.querySelectorAll(':target');\n"
            + "  alert(found.length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='id1' >\n"
            + "  <input id='id2'>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        loadPageWithAlerts2(UrlUtils.getUrlWithNewRef(URL_FIRST, "id3"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "[object HTMLHtmlElement]"})
    public void root() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.querySelectorAll(':root');\n"
            + "  log(list.length);\n"
            + "  log(list[0]);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"first", "second"})
    public void escapedAttributeValue() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "  <input id='first' name='foo[bar]'>\n"
            + "  <input id='second' name='foo.bar'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(document.querySelectorAll('input[name=foo\\\\[bar\\\\]]')[0].id);\n"
            + "} catch(e) {log(e)}\n"
            + "try {\n"
            + "  log(document.querySelectorAll('input[name=foo\\\\.bar]')[0].id);\n"
            + "} catch(e) {log(e)}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * See issue #1685.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"6", "3"})
    public void differentWhitespaceClassName() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "  <input id='first' class='foo'>\n"
            + "  <input id='first' class='\tfoo\n'>\n"
            + "  <input id='first' class='foo bar'>\n"
            + "  <input id='second' class='foo\tbar'>\n"
            + "  <input id='third' class='foo\r\nbar'>\n"

            + "  <input id='third' class='foobar foo'>\n"

            + "  <input id='third' class='foobar'>\n"
            + "  <input id='third' class='abcfoobar'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(document.querySelectorAll('.foo').length);\n"
            + "  log(document.querySelectorAll('.bar').length);\n"
            + "} catch(e) {log('exception')}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"first", "second", "third"})
    public void escapedClassName() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "  <input id='first' class='foo[bar]'>\n"
            + "  <input id='second' class='foo.bar'>\n"
            + "  <input id='third' class='foo:bar'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(document.querySelectorAll('.foo\\\\[bar\\\\]')[0].id);\n"
            + "  log(document.querySelectorAll('.foo\\\\.bar')[0].id);\n"
            + "  log(document.querySelectorAll('.foo\\\\:bar')[0].id);\n"
            + "} catch(e) {log('exception')}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"silly:id::with:colons", "silly:id::with:colons", "silly~id", "silly~id"})
    public void escapedId() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "  <input id='silly:id::with:colons'>\n"
            + "  <input id='silly~id'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(document.querySelectorAll('#silly\\\\:id\\\\:\\\\:with\\\\:colons')[0].id);\n"
            + "  log(document.querySelectorAll(\"#silly\\\\:id\\\\:\\\\:with\\\\:colons\")[0].id);\n"

            + "  log(document.querySelectorAll('#silly\\\\~id')[0].id);\n"
            + "  log(document.querySelectorAll(\"#silly\\\\~id\")[0].id);\n"
            + "} catch(e) {log('exception ' + e)}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void invalidSelectors() throws Exception {
        final String html
            = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>Invalid Selectors</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var list = document.querySelectorAll('li:foo() ~ li');\n"
            + "    alert(list.length);\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<ul id='ul'>\n"
            + "  <li id='li1'></li>\n"
            + "  <li id='li2'></li>\n"
            + "</ul>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "null", "null"})
    public void activeEmptyDetached() throws Exception {
        emptyAndDetached("*:active");
        emptyAndDetached(":active");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"null", "exception", "null"})
    public void checkedEmptyDetached() throws Exception {
        emptyAndDetached("*:checked");
        emptyAndDetached(":checked");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"null", "exception", "null"})
    public void disabledEmptyDetached() throws Exception {
        emptyAndDetached("*:disabled");
        emptyAndDetached(":disabled");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "[object HTMLSpanElement]"},
            IE = {"null", "exception", "[object HTMLSpanElement]"})
    public void emptyEmptyDetached() throws Exception {
        emptyAndDetached("*:empty");
        emptyAndDetached(":empty");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"null", "exception", "null"})
    public void enabledEmptyDetached() throws Exception {
        emptyAndDetached("*:enabled");
        emptyAndDetached(":enabled");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "null", "[object HTMLSpanElement]"})
    public void firstchildEmptyDetached() throws Exception {
        emptyAndDetached("*:first-child");
        emptyAndDetached(":first-child");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "[object HTMLSpanElement]"},
            IE = {"null", "exception", "[object HTMLSpanElement]"})
    public void firstoftypeEmptyDetached() throws Exception {
        emptyAndDetached("*:first-of-type");
        emptyAndDetached(":first-of-type");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "null", "null"})
    public void focusEmptyDetached() throws Exception {
        emptyAndDetached("*:focus");
        emptyAndDetached(":focus");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "null", "null"})
    public void hoverEmptyDetached() throws Exception {
        emptyAndDetached("*:hover");
        emptyAndDetached(":hover");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "[object HTMLSpanElement]"},
            IE = {"null", "exception", "[object HTMLSpanElement]"})
    public void lastchildEmptyDetached() throws Exception {
        emptyAndDetached("*:last-child");
        emptyAndDetached(":last-child");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "[object HTMLSpanElement]"},
            IE = {"null", "exception", "[object HTMLSpanElement]"})
    public void lastoftypeEmptyDetached() throws Exception {
        emptyAndDetached("*:last-of-type");
        emptyAndDetached(":last-of-type");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "null", "null"})
    public void linkEmptyDetached() throws Exception {
        emptyAndDetached("*:link");
        emptyAndDetached(":link");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "[object HTMLSpanElement]"},
            IE = {"null", "exception", "[object HTMLSpanElement]"})
    public void notEmptyDetached() throws Exception {
        emptyAndDetached("*:not(p)");
        emptyAndDetached(":not(p)");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"null", "exception", "null"})
    public void nthchildEmptyDetached() throws Exception {
        emptyAndDetached("*:nth-child(2n)");
        emptyAndDetached(":nth-child(2n)");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"null", "exception", "null"})
    public void nthlastchildEmptyDetached() throws Exception {
        emptyAndDetached("*:nth-last-child(2n)");
        emptyAndDetached(":nth-last-child(2n)");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"null", "exception", "null"})
    public void nthoftypeEmptyDetached() throws Exception {
        emptyAndDetached("*:nth-of-type(2n)");
        emptyAndDetached(":nth-of-type(2n)");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "[object HTMLSpanElement]"},
            IE = {"null", "exception", "[object HTMLSpanElement]"})
    public void onlychildEmptyDetached() throws Exception {
        emptyAndDetached("*:only-child");
        emptyAndDetached(":only-child");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "[object HTMLSpanElement]"},
            IE = {"null", "exception", "[object HTMLSpanElement]"})
    public void onlyoftypeEmptyDetached() throws Exception {
        emptyAndDetached("*:only-of-type");
        emptyAndDetached(":only-of-type");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"null", "null", "null"},
            IE = {"null", "exception", "null"})
    public void rootEmptyDetached() throws Exception {
        emptyAndDetached("*:root");
        emptyAndDetached(":root");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "null", "null"})
    public void visitedEmptyDetached() throws Exception {
        emptyAndDetached("*:visited");
        emptyAndDetached(":visited");
    }

    private void emptyAndDetached(final String selector) throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div = document.getElementById('myDiv');\n"
            + "  try {\n"
            + "    found = div.querySelector('" + selector + "');\n"
            + "    log(found);\n"
            + "  } catch(e) {log('exception')}\n"

            + "  div = document.createElement('div');\n"
            + "  try {\n"
            + "    found = div.querySelector('" + selector + "');\n"
            + "    log(found);\n"
            + "  } catch(e) {log('exception')}\n"

            + "  var input = document.createElement('span');\n"
            + "  div.appendChild(input);\n"
            + "  try {\n"
            + "    found = div.querySelector('" + selector + "');\n"
            + "    log(found);\n"
            + "  } catch(e) {log('exception')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></myDiv>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "<nested>Three</nested>", "Four",
                "1", "Two", "0", "0"},
            IE = {"2", "undefined", "undefined",
                    "1", "undefined", "0", "0"})
    public void xmlTagName() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var xmlString = [\n"
            + "                 '<ResultSet>',\n"
            + "                 '<Result>One</Result>',\n"
            + "                 '<RESULT>Two</RESULT>',\n"
            + "                 '<result><nested>Three</nested></result>',\n"
            + "                 '<result>Four</result>',\n"
            + "                 '</ResultSet>'\n"
            + "                ].join('');\n"
            + "  if (window.DOMParser) {\n"
            + "    var parser = new DOMParser();\n"
            + "    xml = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  } else { // IE\n"
            + "    var parser = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    parser.async = 'false';\n"
            + "    parser.loadXML(xmlString);\n"
            + "  }\n"
            + "  var xmlDoc = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  var de = xmlDoc.documentElement;\n"
            + "  try {\n"

            + "    var res = de.querySelectorAll('result');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"
            + "    log(res[1].innerHTML);\n"

            + "    res = de.querySelectorAll('RESULT');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"

            + "    res = de.querySelectorAll('resulT');\n"
            + "    log(res.length);\n"

            + "    res = de.querySelectorAll('rEsulT');\n"
            + "    log(res.length);\n"
            + "  } catch(e) {log('exception ' + e)}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"2", "ONE", "<CHILD>Two</CHILD>",
                    "0",
                    "2", "ONE", "<CHILD>Two</CHILD>",
                    "1", "ONE",
                    "1", "Two"},
            IE = {"2", "undefined", "undefined",
                    "0",
                    "2", "undefined", "undefined",
                    "1", "undefined",
                    "1", "undefined"})
    public void xmlAttribute() throws Exception {
        final String html = "<html><head>\n"
            + "<meta http-equiv='X-UA-Compatible' content='IE=edge'>\n"
            + "</head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var xmlString = [\n"
            + "                 '<ResultSet>',\n"
            + "                 '<RESULT thinger=\"blah\">ONE</RESULT>',\n"
            + "                 '<RESULT thinger=\"gadzooks\"><CHILD>Two</CHILD></RESULT>',\n"
            + "                 '</ResultSet>'\n"
            + "                ].join('');\n"
            + "  if (window.DOMParser) {\n"
            + "    var parser = new DOMParser();\n"
            + "    xml = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  } else { // IE\n"
            + "    var parser = new ActiveXObject('Microsoft.XMLDOM');\n"
            + "    parser.async = 'false';\n"
            + "    parser.loadXML(xmlString);\n"
            + "  }\n"
            + "  var xmlDoc = parser.parseFromString(xmlString, 'text/xml');\n"
            + "  var de = xmlDoc.documentElement;\n"
            + "  try {\n"

            + "    var res = de.querySelectorAll('RESULT');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"
            + "    log(res[1].innerHTML);\n"

            + "    res = de.querySelectorAll('RESULT[THINGER]');\n"
            + "    log(res.length);\n"

            + "    res = de.querySelectorAll('RESULT[thinger]');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"
            + "    log(res[1].innerHTML);\n"

            + "    res = de.querySelectorAll('RESULT[thinger=blah]');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"

            + "    res = de.querySelectorAll('RESULT > CHILD');\n"
            + "    log(res.length);\n"
            + "    log(res[0].innerHTML);\n"

            + "  } catch(e) {log('exception ' + e)}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }
}
