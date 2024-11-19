/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPageTest;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DOMTokenList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Marek Gawlicki
 * @author Markus Winter
 */
@RunWith(BrowserRunner.class)
public class DOMTokenListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "b", "b", "true", "false", "c d", "<body onload=\"test()\" class=\"c d\"> </body>"})
    public void various() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.body.classList;\n"
            + "  log(list.length);\n"
            + "  log(list.item(1));\n"
            + "  log(list[1]);\n"
            + "  log(list.contains('c'));\n"

            + "  list.add('d');\n"
            + "  list.remove('a');\n"
            + "  log(list.toggle('b'));\n"

            + "  log(list);\n"
            + "  log(document.body.outerHTML);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()' class='a b c'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "null", "false", "# removed", "", "<body onload=\"test()\"> </body>"})
    public void noAttribute() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.body.classList;\n"
            + "  log(list.length);\n"
            + "  log(list.item(0));\n"
            + "  log(list.contains('#'));\n"
            + "  list.remove('#');"
            + "  log('# removed');\n"
            + "  log(document.body.className);\n"
            + "  log(document.body.outerHTML);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "undefined", "1", "#", "<body onload=\"test()\" class=\"#\"> </body>"})
    public void noAttributeAdd() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.body.classList;\n"
            + "  log(list.length);\n"
            + "  log(list.add('#'));\n"
            + "  log(list.length);\n"
            + "  log(document.body.className);\n"
            + "  log(document.body.outerHTML);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "true", "1", "#"})
    public void noAttributeToggle() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.body.classList;\n"
            + "  log(list.length);\n"
            + "  log(list.toggle('#'));\n"
            + "  log(list.length);\n"
            + "  log(document.body.className);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "0", "2", "8"})
    public void length() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.getElementById('d1').classList;\n"
            + "  log(list.length);\n"
            + "  list = document.getElementById('d2').classList;\n"
            + "  log(list.length);\n"
            + "  list = document.getElementById('d3').classList;\n"
            + "  log(list.length);\n"
            + "  list = document.getElementById('d4').classList;\n"
            + "  log(list.length);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b c '></div>\n"
            + "  <div id='d2' class=''></div>\n"
            + "  <div id='d3' class=' a b a'></div>\n"
            + "  <div id='d4' class=' a b \t c \n d \u000B e \u000C f \r g'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "b", "c", "d", "\u000B", "e", "f", "g", "null", "null", "null"})
    public void item() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.getElementById('d1').classList;\n"
            + "  for (var i = 0; i < list.length; i++) {\n"
            + "    log(list.item(i));\n"
            + "  }\n"
            + "  log(list.item(-1));\n"
            + "  log(list.item(list.length));\n"
            + "  log(list.item(100));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b \t c \n d \u000B e \u000C f \r g'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "b", "c", "d", "\u000B", "e", "f", "g"})
    public void forEach() throws Exception {
        final String html
                = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var list = document.getElementById('d1').classList;\n"
                + "  list.forEach((i) => {\n"
                + "    log(i);\n"
                + "  });\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "  <div id='d1' class=' a b \t c \n d \u000B e \u000C f \r g'></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "b", "c"})
    public void forEachDuplicates() throws Exception {
        final String html
                = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var list = document.getElementById('d1').classList;\n"
                + "  list.forEach((i) => {\n"
                + "    log(i);\n"
                + "  });\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "  <div id='d1' class=' a b a c'></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a#0#true", "b#1#true"})
    public void forEachAllParams() throws Exception {
        final String html
                = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var list = document.getElementById('d1').classList;\n"
                + "  list.forEach((val, idx, listObj) => {\n"
                + "    log(val + '#' + idx + '#' + (listObj === list));\n"
                + "  });\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "  <div id='d1' class=' a b  '></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"exception", "exception"})
    public void forEachWrongParam() throws Exception {
        final String html
                = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var list = document.getElementById('d1').classList;\n"
                + "  try {\n"
                + "    list.forEach();\n"
                + "  } catch(e) { log('exception'); }\n"
                + "  try {\n"
                + "    list.forEach('wrong');\n"
                + "  } catch(e) { log('exception'); }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "  <div id='d1' class=' a b \t c \n d \u000B e \u000C f \r g'></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"value", "done", "object", "0", "a"})
    public void entries() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.entries) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = list.entries().next();\n"
            + "    for (var x in i) {\n"
            + "      log(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    log(typeof v);\n"
            + "    log(v[0]);\n"
            + "    log(v[1]);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a x'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"})
    public void entriesPropertyDescriptor() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"

            + "    log('entries' in list);\n"
            + "    log(Object.getOwnPropertyDescriptor(list, 'entries'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(list), 'entries');\n"
            + "    if (desc === undefined) { log('no entries'); return; }\n"
            + "    log(typeof desc.value);\n"
            + "    log(desc.get);\n"
            + "    log(desc.set);\n"
            + "    log(desc.writable);\n"
            + "    log(desc.enumerable);\n"
            + "    log(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a x'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0,a", "1,x"})
    public void entriesForOf() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.entries) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of list.entries()) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a x'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("0,1,2,add,contains,entries,forEach,item,keys,length,remove,replace,supports,toggle,toString,value,values")
    @HtmlUnitNYI(CHROME = "0,1,2,add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values",
            EDGE = "0,1,2,add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values",
            FF = "0,1,2,add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values",
            FF_ESR = "0,1,2,add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values")
    public void forIn() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var all = [];\n"
                + "    for (var i in document.getElementById('d1').classList) {\n"
                + "      all.push(i);\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    log(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div id='d1' class=' a b g'></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("add,contains,entries,forEach,item,keys,length,remove,replace,supports,toggle,toString,value,values")
    @HtmlUnitNYI(CHROME = "add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values",
            EDGE = "add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values",
            FF = "add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values",
            FF_ESR = "add,contains,entries,forEach,item,keys,length,remove,replace,toggle,value,values")
    public void forInEmptyList() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var all = [];\n"
                + "    for (var i in document.getElementById('d1').classList) {\n"
                + "      all.push(i);\n"
                + "    }\n"
                + "    all.sort(sortFunction);\n"
                + "    log(all);\n"
                + "  }\n"
                + "  function sortFunction(s1, s2) {\n"
                + "    return s1.toLowerCase() > s2.toLowerCase() ? 1 : -1;\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div id='d1'></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "a", "b", "g"})
    public void iterator() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    var list = document.getElementById('d1').classList;\n"

                + "    if (typeof Symbol != 'undefined') {\n"
                + "      log(list[Symbol.iterator] === list.values);\n"
                + "    }\n"

                + "    if (!list.forEach) {\n"
                + "      log('no for..of');\n"
                + "      return;\n"
                + "    }\n"

                + "    for (var i of list) {\n"
                + "      log(i);\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <div id='d1' class=' a b g'></div>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a b", "2", "null", "undefined"})
    public void itemNegative() throws Exception {
        item("a b", -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a b", "2", "null", "undefined"})
    public void itemNegative2() throws Exception {
        item("a b", -123);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a b", "2", "a", "a"})
    public void itemFirst() throws Exception {
        item("a b", 0);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a b", "2", "b", "b"})
    public void itemLast() throws Exception {
        item("a b", 1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a b", "2", "null", "undefined"})
    public void itemOutside() throws Exception {
        item("a b", 13);
    }

    private void item(final String in, final int pos) throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elem = document.getElementById('d1');\n"

            + "    var config = { attributes: true, attributeOldValue: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.attributeName + ' changed old: ' + mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(elem, config);"

            + "    var list = elem.classList;\n"
            + "    if (!list) { log('no list'); return; }\n"

            + "    log(elem.className);\n"
            + "    log(list.length);\n"
            + "    try {\n"
            + "      log(list.item(" + pos + "));\n"
            + "      log(list[" + pos + "]);\n"
            + "    } catch(e) { log('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='" + in + "'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false"})
    public void containsEmpty() throws Exception {
        contains("a b", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false"})
    public void containsBlank() throws Exception {
        contains("a b", " ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false"})
    public void containsTab() throws Exception {
        contains("a b", "\t");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false"})
    public void containsCr() throws Exception {
        contains("a b", "\\r");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false"})
    public void containsNl() throws Exception {
        contains("a b", "\\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false"})
    public void containsVt() throws Exception {
        contains("a b", "\u000B");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "false"})
    public void containsInsideEmpty() throws Exception {
        contains("", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\\s\\t\\s\\n\\s\\s", "0", "false"})
    public void containsInsideWhitespace() throws Exception {
        contains(" \t \r  ", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "true"})
    public void containsInsideAtStart() throws Exception {
        contains("a b", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "true"})
    public void containsInsideAtEnd() throws Exception {
        contains("a b", "b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc\\sdef", "2", "false"})
    public void containsInsideSubstringAtStart() throws Exception {
        contains("abc def", "ab");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc\\sdef", "2", "false"})
    public void containsInsideSubstringAtEnd() throws Exception {
        contains("abc def", "bc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abcd\\sef", "2", "false"})
    public void containsInsideSubstringInside() throws Exception {
        contains("abcd ef", "bc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\s\\s", "1", "true"})
    public void containsInsideWhitespaceAtEnd() throws Exception {
        contains("a  ", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\\s\\sa", "1", "true"})
    public void containsInsideWhitespaceInFront() throws Exception {
        contains("  a", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\s\\t\\sc\\s\\n\\sd\\s\\se", "4", "true"})
    public void containsWhitespaceExisting() throws Exception {
        contains("a \t c \n d  e", "c");
    }

    private void contains(final String in, final String toAdd) throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var elem = document.getElementById('d1');\n"

            + "    var config = { attributes: true, attributeOldValue: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.attributeName + ' changed old: ' + mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(elem, config);"

            + "    var list = elem.classList;\n"
            + "    if (!list) { log('no list'); return; }\n"

            + "    log(elem.className);\n"
            + "    log(list.length);\n"
            + "    try {\n"
            + "      log(list.contains('" + toAdd + "'));\n"
            + "    } catch(e) { log('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='" + in + "'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb"})
    public void addEmpty() throws Exception {
        add("a b", "''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb"})
    public void addBlank() throws Exception {
        add("a b", "' '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb"})
    public void addTab() throws Exception {
        add("a b", "'\t'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb"})
    public void addCr() throws Exception {
        add("a b", "'\\r'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb"})
    public void addNl() throws Exception {
        add("a b", "'\\n'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "3", "a\\sb\\s\u000B", "class\\schanged\\sold:\\sa\\sb"})
    public void addVt() throws Exception {
        add("a b", "'\u000B'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "1", "a", "class\\schanged\\sold:\\s"})
    public void addToEmpty() throws Exception {
        add("", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\\s\\t\\s\\n\\s\\s", "0", "1", "a", "class\\schanged\\sold:\\s\\s\\t\\s\\n\\s\\s"})
    public void addToWhitespace() throws Exception {
        add(" \t \r  ", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\s\\s", "1", "2", "a\\sb", "class\\schanged\\sold:\\sa\\s\\s"})
    public void addToWhitespaceAtEnd() throws Exception {
        add("a  ", "'b'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "3", "a\\sb\\sc", "class\\schanged\\sold:\\sa\\sb"})
    public void addNotExisting() throws Exception {
        add("a b", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "2", "a\\sb", "class\\schanged\\sold:\\sa\\sb"})
    public void addExisting() throws Exception {
        add("a b", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"b\\sa", "2", "2", "b\\sa", "class\\schanged\\sold:\\sb\\sa"})
    public void addExisting2() throws Exception {
        add("b a", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"b\\sa\\sb", "2", "3", "b\\sa\\sc", "class\\schanged\\sold:\\sb\\sa\\sb"})
    public void addNormalizes() throws Exception {
        add("b a b", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sa", "2", "exception", "2", "a\\sb\\sa"})
    public void addElementWithBlank() throws Exception {
        add("a b a", "'a b'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sa\\tb", "2", "exception", "2", "a\\sb\\sa\\tb"})
    public void addElementWithTab() throws Exception {
        add("a b a\tb", "'a\tb'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\s\\t\\sc\\s\\n\\sd\\s\\se", "4", "4", "a\\sc\\sd\\se",
             "class\\schanged\\sold:\\sa\\s\\t\\sc\\s\\n\\sd\\s\\se"})
    public void addToWhitespaceExisting() throws Exception {
        add("a \t c \n d  e", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\se", "2", "4", "a\\se\\sc\\sb", "class\\schanged\\sold:\\sa\\se"})
    public void addTwoValues() throws Exception {
        add("a e", "'c', 'b'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\se", "2", "3", "a\\se\\sc", "class\\schanged\\sold:\\sa\\se"})
    public void addTwoValuesExisting() throws Exception {
        add("a e", "'c', 'e'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\se", "2", "4", "a\\se\\sc\\s7", "class\\schanged\\sold:\\sa\\se"})
    public void addTwoValuesNumber() throws Exception {
        add("a e", "'c', 7");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\se", "2", "4", "a\\se\\strue\\sfalse", "class\\schanged\\sold:\\sa\\se"})
    public void addTwoValuesBoolean() throws Exception {
        add("a e", "true, false");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\se", "2", "exception", "2", "a\\se"})
    public void addTwoValuesObject() throws Exception {
        add("a e", "'c', { color: 'blue' }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\se", "2", "4", "a\\se\\sc\\sundefined", "class\\schanged\\sold:\\sa\\se"})
    public void addTwoValuesUndefined() throws Exception {
        add("a e", "'c', undefined");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\se", "2", "4", "a\\se\\sc\\snull", "class\\schanged\\sold:\\sa\\se"})
    public void addTwoValuesNull() throws Exception {
        add("a e", "'c', null");
    }

    private void add(final String in, final String toAdd) throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var elem = document.getElementById('d1');\n"

            + "    var config = { attributes: true, attributeOldValue: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.attributeName + ' changed old: ' + mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(elem, config);"

            + "    var list = elem.classList;\n"
            + "    if (!list) { log('no list'); return; }\n"

            + "    log(elem.className);\n"
            + "    log(list.length);\n"
            + "    try {\n"
            + "      list.add(" + toAdd + ");\n"
            + "    } catch(e) { log('exception');}\n"
            + "    log(list.length);\n"
            + "    log(elem.className);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d1' class='" + in + "'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3"})
    public void addSvg() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var elem = document.getElementById('myId');\n"
            + "    var list = elem.classList;\n"
            + "    if (!list) { log('no list'); return; }\n"

            + "    log(list.length);\n"
            + "    try {\n"
            + "      list.add('new');\n"
            + "    } catch(e) { log('exception');}\n"
            + "    log(list.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' version='1.1'>\n"
            + "    <text id='myId' class='cls1, cls2'/>\n"
            + "  </svg>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"block", "none"})
    public void addStyleCheck() throws Exception {
        final String html
            = "<html><head>\n"
            + "<style>\n"
            + "  #d1.hidden { display: none; }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div1 = document.getElementById('d1');\n"
            + "  var list = div1.classList;\n"

            + "  log(getComputedStyle(div1, null).display);\n"
            + "  list.add('hidden');\n"
            + "  log(getComputedStyle(div1, null).display);\n"
            + "}\n"
            + "</script>"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d1' class='nice'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void removeEmpty() throws Exception {
        remove("a b", "''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void removeBlank() throws Exception {
        remove("a b", "' '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void removeTab() throws Exception {
        remove("a b", "'\t'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void removeCr() throws Exception {
        remove("a b", "'\\r'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void removeNl() throws Exception {
        remove("a b", "'\\n'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>", "class\\schanged\\sold:\\sa\\sb"})
    public void removeVt() throws Exception {
        remove("a b", "'\u000B'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "0", "", "<div\\sid=\"d1\"\\sclass=\"\"></div>", "class\\schanged\\sold:\\s"})
    public void removeFromEmpty() throws Exception {
        remove("", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\\s\\t\\s\\n\\s\\s", "0", "0", "",
             "<div\\sid=\"d1\"\\sclass=\"\"></div>",
             "class\\schanged\\sold:\\s\\s\\t\\s\\n\\s\\s"})
    public void removeFromWhitespace() throws Exception {
        remove(" \t \r  ", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>", "class\\schanged\\sold:\\sa\\sb"})
    public void removeNotExisting() throws Exception {
        remove("a b", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sa", "2", "1", "b", "<div\\sid=\"d1\"\\sclass=\"b\"></div>", "class\\schanged\\sold:\\sa\\sb\\sa"})
    public void removeDuplicated() throws Exception {
        remove("a b a", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sa", "2", "exception", "2", "a\\sb\\sa", "<div\\sid=\"d1\"\\sclass=\"a\\sb\\sa\"></div>"})
    public void removeElementWithBlank() throws Exception {
        remove("a b a", "'a b'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sa\\tb", "2", "exception", "2", "a\\sb\\sa\\tb",
             "<div\\sid=\"d1\"\\sclass=\"a\\sb\\sa\\tb\"></div>"})
    public void removeElementWithTab() throws Exception {
        remove("a b a\tb", "'a\tb'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "1", "0", "", "<div\\sid=\"d1\"\\sclass=\"\"></div>", "class\\schanged\\sold:\\sa"})
    public void removeLast() throws Exception {
        remove("a", "'a'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\s\\t\\sc\\s\\n\\sd\\s\\se", "4", "3", "a\\sd\\se",
             "<div\\sid=\"d1\"\\sclass=\"a\\sd\\se\"></div>",
             "class\\schanged\\sold:\\sa\\s\\t\\sc\\s\\n\\sd\\s\\se"})
    public void removeWhitespace() throws Exception {
        remove("a \t c \n d  e", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sc\\sa\\sc", "2", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\sa\\sc\\sa\\sc"})
    public void removeNormalizes() throws Exception {
        remove("a c a c", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"c", "1", "0", "",
             "<div\\sid=\"d1\"\\sclass=\"\"></div>",
             "class\\schanged\\sold:\\sc"})
    public void removeAll() throws Exception {
        remove("c", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "0", "",
             "<div\\sid=\"d1\"\\sclass=\"\"></div>",
             "class\\schanged\\sold:\\s"})
    public void removeAllFromEmpty() throws Exception {
        remove("", "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "0", "",
             "<div\\sid=\"d1\"></div>"})
    public void removeAllNotDefined() throws Exception {
        remove(null, "'c'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\sa\\sb"})
    public void removeTwo() throws Exception {
        remove("a b", "'b', 'd'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\s7", "3", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\sa\\sb\\s7"})
    public void removeTwoNumber() throws Exception {
        remove("a b 7", "'b', 7");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\strue", "3", "2", "a\\sb",
             "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>",
             "class\\schanged\\sold:\\sa\\sb\\strue"})
    public void removeTwoBoolean() throws Exception {
        remove("a b true", "true, false");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sundefined", "3", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\sa\\sb\\sundefined"})
    public void removeTwoUndefined() throws Exception {
        remove("a b undefined", "'b', undefined");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\snull\\s7", "3", "2", "a\\s7",
             "<div\\sid=\"d1\"\\sclass=\"a\\s7\"></div>",
             "class\\schanged\\sold:\\sa\\snull\\s7"})
    public void removeTwoNull() throws Exception {
        remove("a null 7", "'b', null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\s7", "3", "exception", "3", "a\\sb\\s7",
             "<div\\sid=\"d1\"\\sclass=\"a\\sb\\s7\"></div>"})
    public void removeTwoObject() throws Exception {
        remove("a b 7", "'b', { color: 'red' }");
    }

    private void remove(final String in, final String toRemove) throws Exception {
        String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var elem = document.getElementById('d1');\n"

            + "    var config = { attributes: true, attributeOldValue: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.attributeName + ' changed old: ' + mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(elem, config);"

            + "    var list = elem.classList;\n"
            + "    if (!list) { log('no list'); return; }\n"

            + "    log(elem.className);\n"
            + "    log(list.length);\n"
            + "    try {\n"
            + "      list.remove(" + toRemove + ");\n"
            + "    } catch(e) { log('exception');}\n"
            + "    log(list.length);\n"
            + "    log(elem.className);\n"
            + "    log(elem.outerHTML);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n";
        if (in == null) {
            html += "  <div id='d1'></div>\n";
        }
        else {
            html += "  <div id='d1' class='" + in + "'></div>\n";
        }

        html += "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "1", "exception", "1", "a", "<div\\sid=\"d1\"\\sclass=\"a\"></div>"})
    public void replaceEmptyOldToken() throws Exception {
        replace("a", "", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void replaceOldTokenContainingWhiteSpace() throws Exception {
        replace("a b", " a x", "abc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "1", "exception", "1", "a", "<div\\sid=\"d1\"\\sclass=\"a\"></div>"})
    public void replaceEmptyNewToken() throws Exception {
        replace("a", "abc", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "exception", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void replaceNewTokenContainingWhiteSpace() throws Exception {
        replace("a b", "abc", " a x");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "true", "2", "a\\sax",
             "<div\\sid=\"d1\"\\sclass=\"a\\sax\"></div>",
             "class\\schanged\\sold:\\sa\\sb"})
    public void replace() throws Exception {
        replace("a b", "b", "ax");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sc\\sb\\su", "4", "true", "4", "a\\sax\\sc\\su",
             "<div\\sid=\"d1\"\\sclass=\"a\\sax\\sc\\su\"></div>",
             "class\\schanged\\sold:\\sa\\sb\\sc\\sb\\su"})
    public void replaceOnce() throws Exception {
        replace("a b c b u", "b", "ax");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false", "2", "a\\sb", "<div\\sid=\"d1\"\\sclass=\"a\\sb\"></div>"})
    public void replaceNotFound() throws Exception {
        replace("a b", "ab", "ax");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "false", "0", "", "<div\\sid=\"d1\"\\sclass=\"\"></div>"})
    public void replaceInEmpty() throws Exception {
        replace("", "ab", "ax");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "false", "0", "",
             "<div\\sid=\"d1\"\\sclass=\"\"></div>"})
    public void replaceFromEmpty() throws Exception {
        replace("", "a", "c");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "false", "0", "",
             "<div\\sid=\"d1\"></div>"})
    public void replaceNotDefined() throws Exception {
        replace(null, "a", "c");
    }

    private void replace(final String in, final String oldToken, final String newToken) throws Exception {
        String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var elem = document.getElementById('d1');\n"

            + "    var config = { attributes: true, attributeOldValue: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.attributeName + ' changed old: ' + mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(elem, config);"

            + "    var list = elem.classList;\n"
            + "    if (!list) { log('no list'); return; }\n"

            + "    log(elem.className);\n"
            + "    log(list.length);\n"
            + "    try {\n"
            + "      var res = list.replace('" + oldToken + "', '" + newToken + "');\n"
            + "      log(res);\n"
            + "    } catch(e) { log('exception');}\n"
            + "    log(list.length);\n"
            + "    log(elem.className);\n"
            + "    log(elem.outerHTML);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n";
        if (in == null) {
            html += "  <div id='d1'></div>\n";
        }
        else {
            html += "  <div id='d1' class='" + in + "'></div>\n";
        }

        html += "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "none", "block"})
    public void removeStyleCheck() throws Exception {
        final String html
            = "<html><head>\n"
            + "<style>\n"
            + "  #d1.hidden { display: none; }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div1 = document.getElementById('d1');\n"
            + "  var list = div1.classList;\n"

            + "  log(getComputedStyle(div1, null).display);\n"
            + "  list.remove('hidden');\n"
            + "  log(getComputedStyle(div1, null).display);\n"
            + "}\n"
            + "</script>"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d1' class='hidden'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "false", "true", "false", "false"})
    public void in() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var list = document.getElementById('d1').classList;\n"
            + "  log(list.length);\n"
            + "  log(-1 in list);\n"
            + "  log(0 in list);\n"
            + "  log(2 in list);\n"
            + "  log(42 in list);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a e'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"exception", "exception", "2", "true", "false", "1", "false", "true", "2", "true",
             "class changed old: a e", "class changed old: a"})
    public void toggle() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var elem = document.getElementById('d1');\n"

            + "    var config = { attributes: true, attributeOldValue: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.attributeName + ' changed old: ' + mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(elem, config);"

            + "  var list = elem.classList;\n"
            + "  try {\n"
            + "    list.toggle('ab e');\n"
            + "  } catch(e) { log('exception');}\n"
            + "  try {\n"
            + "    list.toggle('');\n"
            + "  } catch(e) { log('exception');}\n"
            + "  log(list.length);\n"
            + "  log(list.contains('e'));\n"
            + "  log(list.toggle('e'));\n"
            + "  log(list.length);\n"
            + "  log(list.contains('e'));\n"
            + "  log(list.toggle('e'));\n"
            + "  log(list.length);\n"
            + "  log(list.contains('e'));\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a e'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "block", "none"})
    public void toggleStyleCheck() throws Exception {
        final String html
            = "<html><head>\n"
            + "<style>\n"
            + "  #d1.hidden { display: none; }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var div1 = document.getElementById('d1');\n"
            + "  var list = div1.classList;\n"
            + "  log(getComputedStyle(div1, null).display);\n"

            + "  list.toggle('hidden');\n"
            + "  log(getComputedStyle(div1, null).display);\n"

            + "  list.toggle('hidden');\n"
            + "  log(getComputedStyle(div1, null).display);\n"
            + "}\n"
            + "</script>"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d1' class='hidden'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "1", "exception", "1", "a", "<div\\sid=\"d1\"\\sclass=\"a\"></div>"})
    public void toggleEmptyToken() throws Exception {
        toggle("a", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "false", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\sa\\sb"})
    public void toggleStd() throws Exception {
        toggle("a b", "b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb\\sc\\sb\\su", "4", "false", "3", "a\\sc\\su",
             "<div\\sid=\"d1\"\\sclass=\"a\\sc\\su\"></div>",
             "class\\schanged\\sold:\\sa\\sb\\sc\\sb\\su"})
    public void toggleOnce() throws Exception {
        toggle("a b c b u", "b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\sb", "2", "true", "3", "a\\sb\\sab",
             "<div\\sid=\"d1\"\\sclass=\"a\\sb\\sab\"></div>",
             "class\\schanged\\sold:\\sa\\sb"})
    public void toggleNotFound() throws Exception {
        toggle("a b", "ab");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "1", "false", "0", "",
             "<div\\sid=\"d1\"\\sclass=\"\"></div>",
             "class\\schanged\\sold:\\sa"})
    public void toggleTheOnly() throws Exception {
        toggle("a", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "true", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\s"})
    public void toggleInEmpty() throws Exception {
        toggle("", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "true", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\s"})
    public void toggleFromEmpty() throws Exception {
        toggle("", "a");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "0", "true", "1", "a",
             "<div\\sid=\"d1\"\\sclass=\"a\"></div>",
             "class\\schanged\\sold:\\snull"})
    public void toggleNotDefined() throws Exception {
        toggle(null, "a");
    }

    private void toggle(final String in, final String token) throws Exception {
        String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var elem = document.getElementById('d1');\n"

            + "    var config = { attributes: true, attributeOldValue: true };\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        log(mutation.attributeName + ' changed old: ' + mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "    observer.observe(elem, config);"

            + "    var list = elem.classList;\n"
            + "    if (!list) { log('no list'); return; }\n"

            + "    log(elem.className);\n"
            + "    log(list.length);\n"
            + "    try {\n"
            + "      var res = list.toggle('" + token + "');\n"
            + "      log(res);\n"
            + "    } catch(e) { log('exception');}\n"
            + "    log(list.length);\n"
            + "    log(elem.className);\n"
            + "    log(elem.outerHTML);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n";
        if (in == null) {
            html += "  <div id='d1'></div>\n";
        }
        else {
            html += "  <div id='d1' class='" + in + "'></div>\n";
        }

        html += "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"value", "done", "number", "0"})
    public void keys() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.keys) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = list.keys().next();\n"
            + "    for (var x in i) {\n"
            + "      log(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    log(typeof v);\n"
            + "    log(v);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b g'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"})
    public void keysPropertyDescriptor() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"

            + "    log('keys' in list);\n"
            + "    log(Object.getOwnPropertyDescriptor(list, 'keys'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(list), 'keys');\n"
            + "    if (desc === undefined) { log('no keys'); return; }\n"
            + "    log(typeof desc.value);\n"
            + "    log(desc.get);\n"
            + "    log(desc.set);\n"
            + "    log(desc.writable);\n"
            + "    log(desc.enumerable);\n"
            + "    log(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b g'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "1", "2"})
    public void keysForOf() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.keys) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of list.keys()) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b g'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0,1,2", ""})
    public void objectKeys() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    log(Object.keys(list));\n"

            + "    var list = document.getElementById('b1').classList;\n"
            + "    log(Object.keys(list));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()' id='b1'>\n"
            + "  <div id='d1' class=' a b g'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"value", "done", "string", "a"})
    public void values() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.values) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    var i = list.values().next();\n"
            + "    for (var x in i) {\n"
            + "      log(x);\n"
            + "    }\n"
            + "    var v = i.value;\n"
            + "    log(typeof v);\n"
            + "    log(v);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b g'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "undefined", "function", "undefined", "undefined", "true", "true", "true"})
    public void valuesPropertyDescriptor() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"

            + "    log('values' in list);\n"
            + "    log(Object.getOwnPropertyDescriptor(list, 'values'));\n"

            + "    var desc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(list), 'values');\n"
            + "    if (desc === undefined) { log('no values'); return; }\n"
            + "    log(typeof desc.value);\n"
            + "    log(desc.get);\n"
            + "    log(desc.set);\n"
            + "    log(desc.writable);\n"
            + "    log(desc.enumerable);\n"
            + "    log(desc.configurable);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b g'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"a", "b", "g"})
    public void valuesForOf() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.values) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    for (var i of list.values()) {\n"
            + "      log(i);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b g'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("\\sa\\sb\\sa\\s\\s\\s\\sa\\sg\\s\\s")
    public void getValue() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.values) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    log(list.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b a    a g  '></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"\\sa\\sb\\sa\\s\\s\\s\\sa\\sg\\s\\s", "x\\sy", "z\\sz\\s\\s\\s\\s\\sx\\sz\\s\\s"})
    public void setValue() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (!list.values) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    log(list.value);\n"

            + "    list.value = 'x y';\n"
            + "    log(list.value);\n"

            + "    list.value = 'z z     x z  ';\n"
            + "    log(list.value);\n"

            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b a    a g  '></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"a b", "<div id=\"d1\" class=\"a b\"></div>", "", "<div id=\"d1\" class=\"\"></div>",
             "undefined", "<div id=\"d1\" class=\"undefined\"></div>",
             "null", "<div id=\"d1\" class=\"null\"></div>",
             "17", "<div id=\"d1\" class=\"17\"></div>"})
    public void setValueEmpty() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var div = document.getElementById('d1');\n"
            + "    var list = div.classList;\n"
            + "    if (!list.values) {\n"
            + "      log('not defined');\n"
            + "      return;\n"
            + "    }\n"
            + "    log(list.value);\n"
            + "    log(div.outerHTML);\n"

            + "    list.value = '';\n"
            + "    log(list.value);\n"
            + "    log(div.outerHTML);\n"

            + "    list.value = undefined;\n"
            + "    log(list.value);\n"
            + "    log(div.outerHTML);\n"

            + "    list.value = null;\n"
            + "    log(list.value);\n"
            + "    log(div.outerHTML);\n"


            + "    list.value = 17;\n"
            + "    log(list.value);\n"
            + "    log(div.outerHTML);\n"

            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='d1' class='a b'></div>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }
}
