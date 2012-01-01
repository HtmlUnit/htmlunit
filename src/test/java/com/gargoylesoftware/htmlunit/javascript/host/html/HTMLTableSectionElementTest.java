/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLTableSectionElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLTableSectionElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"left", "hello", "left", "hi", "right" },
            IE = {"", "error", "", "left", "error", "left", "right" })
    public void align_thead() throws Exception {
        align("th");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"left", "hello", "left", "hi", "right" },
            IE = {"", "error", "", "left", "error", "left", "right" })
    public void align_tbody() throws Exception {
        align("tb");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"left", "hello", "left", "hi", "right" },
            IE = {"", "error", "", "left", "error", "left", "right" })
    public void align_tfoot() throws Exception {
        align("tf");
    }

    private void align(final String id) throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t = document.getElementById('" + id + "');\n"
            + "        alert(t.align);\n"
            + "        set(t, 'hello');\n"
            + "        alert(t.align);\n"
            + "        set(t, 'left');\n"
            + "        alert(t.align);\n"
            + "        set(t, 'hi');\n"
            + "        alert(t.align);\n"
            + "        set(t, 'right');\n"
            + "        alert(t.align);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.align = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <table id='t'>\n"
            + "      <thead id='th'/>\n"
            + "      <tbody id='tb'/>\n"
            + "      <tfoot id='tf'/>\n"
            + "    </table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "top", "baseline", "3", "middle", "8", "bottom" },
            IE = { "top", "baseline", "top", "error", "middle", "baseline", "bottom" })
    public void vAlign_thead() throws Exception {
        vAlign("th");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "top", "baseline", "3", "middle", "8", "bottom" },
            IE = { "top", "baseline", "top", "error", "middle", "baseline", "bottom" })
    public void vAlign_tbody() throws Exception {
        vAlign("tb");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "top", "baseline", "3", "middle", "8", "bottom" },
            IE = { "top", "baseline", "top", "error", "middle", "baseline", "bottom" })
    public void vAlign_tfoot() throws Exception {
        vAlign("tf");
    }

    private void vAlign(final String id) throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t1 = document.getElementById('" + id + "1');\n"
            + "        var t2 = document.getElementById('" + id + "2');\n"
            + "        var t3 = document.getElementById('" + id + "3');\n"
            + "        alert(t1.vAlign);\n"
            + "        alert(t2.vAlign);\n"
            + "        alert(t3.vAlign);\n"
            + "        set(t1, 'middle');\n"
            + "        set(t2, 8);\n"
            + "        set(t3, 'BOTtom');\n"
            + "        alert(t1.vAlign);\n"
            + "        alert(t2.vAlign);\n"
            + "        alert(t3.vAlign);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.vAlign = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <table id='t1'>\n"
            + "      <thead id='th1' valign='top'/>\n"
            + "      <tbody id='tb1' valign='top'/>\n"
            + "      <tfoot id='tf1' valign='top'/>\n"
            + "    </table>\n"
            + "    <table id='t2'>\n"
            + "      <thead id='th2' valign='baseline'/>\n"
            + "      <tbody id='tb2' valign='baseline'/>\n"
            + "      <tfoot id='tf2' valign='baseline'/>\n"
            + "    </table>\n"
            + "    <table id='t3'>\n"
            + "      <thead id='th3' valign='3'/>\n"
            + "      <tbody id='tb3' valign='3'/>\n"
            + "      <tfoot id='tf3' valign='3'/>\n"
            + "    </table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "p", "po", ".", "u", "8", "U8" }, IE = { "", "", "", "u", "8", "U8" })
    public void ch_thead() throws Exception {
        ch("th");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "p", "po", ".", "u", "8", "U8" }, IE = { "", "", "", "u", "8", "U8" })
    public void ch_tbody() throws Exception {
        ch("tb");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "p", "po", ".", "u", "8", "U8" }, IE = { "", "", "", "u", "8", "U8" })
    public void ch_tfoot() throws Exception {
        ch("tf");
    }

    private void ch(final String id) throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t1 = document.getElementById('" + id + "1');\n"
            + "        var t2 = document.getElementById('" + id + "2');\n"
            + "        var t3 = document.getElementById('" + id + "3');\n"
            + "        alert(t1.ch);\n"
            + "        alert(t2.ch);\n"
            + "        alert(t3.ch);\n"
            + "        set(t1, 'u');\n"
            + "        set(t2, 8);\n"
            + "        set(t3, 'U8');\n"
            + "        alert(t1.ch);\n"
            + "        alert(t2.ch);\n"
            + "        alert(t3.ch);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.ch = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <table id='t1'>\n"
            + "      <thead id='th1' char='p'/>\n"
            + "      <tbody id='tb1' char='p'/>\n"
            + "      <tfoot id='tf1' char='p'/>\n"
            + "    </table>\n"
            + "    <table id='t2'>\n"
            + "      <thead id='th2' char='po'/>\n"
            + "      <tbody id='tb2' char='po'/>\n"
            + "      <tfoot id='tf2' char='po'/>\n"
            + "    </table>\n"
            + "    <table id='t3'>\n"
            + "      <thead id='th3'/>\n"
            + "      <tbody id='tb3'/>\n"
            + "      <tfoot id='tf3'/>\n"
            + "    </table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "0", "4", "", "5", "0", "abc" }, IE = { "", "", "", "5.2", "-3", "abc" })
    public void chOff_thead() throws Exception {
        chOff("th");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "0", "4", "", "5", "0", "abc" }, IE = { "", "", "", "5.2", "-3", "abc" })
    public void chOff_tbody() throws Exception {
        chOff("tb");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "0", "4", "", "5", "0", "abc" }, IE = { "", "", "", "5.2", "-3", "abc" })
    public void chOff_tfoot() throws Exception {
        chOff("tf");
    }

    private void chOff(final String id) throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var t1 = document.getElementById('" + id + "1');\n"
            + "        var t2 = document.getElementById('" + id + "2');\n"
            + "        var t3 = document.getElementById('" + id + "3');\n"
            + "        alert(t1.chOff);\n"
            + "        alert(t2.chOff);\n"
            + "        alert(t3.chOff);\n"
            + "        set(t1, '5.2');\n"
            + "        set(t2, -3);\n"
            + "        set(t3, 'abc');\n"
            + "        alert(t1.chOff);\n"
            + "        alert(t2.chOff);\n"
            + "        alert(t3.chOff);\n"
            + "      }\n"
            + "      function set(e, value) {\n"
            + "        try {\n"
            + "          e.chOff = value;\n"
            + "        } catch (e) {\n"
            + "          alert('error');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <table id='t1'>\n"
            + "      <thead id='th1' charoff='0'/>\n"
            + "      <tbody id='tb1' charoff='0'/>\n"
            + "      <tfoot id='tf1' charoff='0'/>\n"
            + "    </table>\n"
            + "    <table id='t2'>\n"
            + "      <thead id='th2' charoff='4'/>\n"
            + "      <tbody id='tb2' charoff='4'/>\n"
            + "      <tfoot id='tf2' charoff='4'/>\n"
            + "    </table>\n"
            + "    <table id='t3'>\n"
            + "      <thead id='th3'/>\n"
            + "      <tbody id='tb3'/>\n"
            + "      <tfoot id='tf3'/>\n"
            + "    </table>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "<tr><td>world</td></tr>", IE = { "exception", "<tr><td>hello</td></tr>" })
    public void TBODY_innerHTML() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var t = document.getElementById('myId');\n"
            + "    try {\n"
            + "      t.innerHTML = '<tr><td>world</td></tr>';\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "    alert(t.innerHTML.toLowerCase());\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table>\n"
            + "    <tbody id='myId'><tr><td>hello</td></tr></tbody>\n"
            + "  </table>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
