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
package org.htmlunit.html.parser;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link HTMLParser}.
 *
 * @author Ronald Brill
 */
public class HTMLParser6Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myDiv2\"></div><div>harhar</div><div id=\"myDiv3\"></div>")
    public void fragmentParser() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('<div>harhar</div>');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myDiv2\"></div><select><option value=\"da\">Dansk</option></select><div id=\"myDiv3\"></div>")
    public void fragmentParserHtmlHeadMissingBody() throws Exception {
        final String fragment = "<html><head></head><select><option value=\"da\">Dansk</option></select></body></html>";
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('" + fragment + "');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<select id=\"mySelect\"><option>op1</option></select>"
            + "<select><option value=\"da\">Dansk</option></select> ")
    public void fragmentParserHtmlInsideSelect() throws Exception {
        final String fragment = "<select><option value=\"da\">Dansk</option></select>";
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('mySelect');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('" + fragment + "');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select id='mySelect'><option>op1</option></select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<option id=\"myOption\">op1</option><option value=\"da\">Dansk</option>")
    public void fragmentParserHtmlInsideOption() throws Exception {
        final String fragment = "<select><option value=\"da\">Dansk</option></select>";
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myOption');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('" + fragment + "');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select><option id='myOption'>op1</option></select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myTest\"></div><div a<b=\"7\">xy</div> ")
    public void fragmentParserLtInAttributeName() throws Exception {
        final String fragment = "<div a<b=7>xy</div>";
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myTest');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('" + fragment + "');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myTest'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myTest\"></div><div <ab=\"7\">xy</div> ")
    public void fragmentParserLtFirstInAttributeName() throws Exception {
        final String fragment = "<div <ab=7>xy</div>";
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myTest');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('" + fragment + "');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myTest'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myTest\"></div><div 7=\"ab\">xy</div> ")
    public void fragmentParserNumericAttributeName() throws Exception {
        final String fragment = "<div 7=\"ab\">xy</div>";
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myTest');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('" + fragment + "');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myTest'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
