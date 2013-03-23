/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DOMTokenList}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DOMTokenListTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "3", "b", "true", "false", "c d" })
    public void various() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.body.classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      alert(list.item(1));\n"
            + "      alert(list.contains('c'));\n"
            + "      list.add('d');\n"
            + "      list.remove('a');\n"
            + "      alert(list.toggle('b'));\n"
            + "      alert(list);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()' class='a b c'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "null", "false", "# removed", "" })
    public void noAttribute() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.body.classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      alert(list.item(0));\n"
            + "      alert(list.contains('#'));\n"
            + "      list.remove('#'); alert('# removed');\n"
            + "      alert(document.body.className);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "undefined", "1", "#" })
    @NotYetImplemented(Browser.FF)
    public void noAttributeAdd() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.body.classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      alert(list.add('#'));\n"
            + "      alert(list.length);\n"
            + "      alert(document.body.className);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "true", "1", "#" })
    @NotYetImplemented(Browser.FF)
    public void noAttributeToggle() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.body.classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      alert(list.toggle('#'));\n"
            + "      alert(list.length);\n"
            + "      alert(document.body.className);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "3", "0", "3", "8" })
    @NotYetImplemented(Browser.FF)
    public void length() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      list = document.getElementById('d2').classList;\n"
            + "      alert(list.length);\n"
            + "      list = document.getElementById('d3').classList;\n"
            + "      alert(list.length);\n"
            + "      list = document.getElementById('d4').classList;\n"
            + "      alert(list.length);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b c '></div>\n"
            + "  <div id='d2' class=''></div>\n"
            + "  <div id='d3' class=' a b a'></div>\n"
            + "  <div id='d4' class=' a b \t c \n d \u000B e \u000C f \r g'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "a", "b", "c", "d", "\u000B", "e", "f", "g", "null", "null", "null" })
    @NotYetImplemented(Browser.FF)
    public void item() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      for (i=0; i<list.length; i++) {\n"
            + "        alert(list.item(i));\n"
            + "      }\n"
            + "      alert(list.item(-1));\n"
            + "      alert(list.item(list.length));\n"
            + "      alert(list.item(100));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class=' a b \t c \n d \u000B e \u000C f \r g'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "exception", "true", "false" })
    @NotYetImplemented(Browser.FF)
    public void contains() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      try {\n"
            + "        list.contains('ab\te');\n"
            + "      } catch(e) { alert('exception');}\n"
            + "      alert(list.contains('c'));\n"
            + "      alert(list.contains('xyz'));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a \t c \n d'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "exception", "3", "4", "true" })
    @NotYetImplemented(Browser.FF)
    public void add() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      try {\n"
            + "        list.add('ab\te');\n"
            + "      } catch(e) { alert('exception');}\n"
            + "      alert(list.length);\n"
            + "      list.add('##');\n"
            + "      alert(list.length);\n"
            + "      alert(list.contains('##'));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a \t c \n d'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "1", "2", "a \t #" })
    @NotYetImplemented(Browser.FF)
    public void addSpace() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      list.add('#');\n"
            + "      alert(list.length);\n"
            + "      alert(document.getElementById('d1').className);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a \t '></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "3", "3", "true" })
    public void addDuplicated() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      list.add('a');\n"
            + "      alert(list.length);\n"
            + "      alert(list.contains('a'));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a b a'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "exception", "3", "3", "2", "false" })
    @NotYetImplemented(Browser.FF)
    public void remove() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      try {\n"
            + "        list.remove('ab\te');\n"
            + "      } catch(e) { alert('exception');}\n"
            + "      alert(list.length);\n"
            + "      list.remove('##');\n"
            + "      alert(list.length);\n"
            + "      list.remove('c');\n"
            + "      alert(list.length);\n"
            + "      alert(list.contains('c'));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a \t c \n d'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "3", "1", "false" })
    @NotYetImplemented(Browser.FF)
    public void removeDuplicated() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      alert(list.length);\n"
            + "      list.remove('a');\n"
            + "      alert(list.length);\n"
            + "      alert(list.contains('a'));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a b a'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "exception", "2", "true", "false", "1", "false", "true", "2", "true" })
    @NotYetImplemented(Browser.FF)
    public void toggle() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test() {\n"
            + "    var list = document.getElementById('d1').classList;\n"
            + "    if (list) {\n"
            + "      try {\n"
            + "        list.toggle('ab e');\n"
            + "      } catch(e) { alert('exception');}\n"
            + "      alert(list.length);\n"
            + "      alert(list.contains('e'));\n"
            + "      alert(list.toggle('e'));\n"
            + "      alert(list.length);\n"
            + "      alert(list.contains('e'));\n"
            + "      alert(list.toggle('e'));\n"
            + "      alert(list.length);\n"
            + "      alert(list.contains('e'));\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='d1' class='a e'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
