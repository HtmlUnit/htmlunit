/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLTableRowElement}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLTableRowElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLTableRowElement]", IE = "[object]")
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table>\n"
            + "    <tr id='myId'/>\n"
            + "  </table>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "4", "td1", "3", "td2", "td4", "2", "td3", "exception", "exception" })
    public void deleteCell() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var tr = document.getElementById('myId');\n"
            + "    alert(tr.cells.length);\n"
            + "    alert(tr.cells[0].id);\n"
            + "    tr.deleteCell(0);\n"
            + "    alert(tr.cells.length);\n"
            + "    alert(tr.cells[0].id);\n"
            + "    alert(tr.cells[tr.cells.length-1].id);\n"
            + "    tr.deleteCell(-1);\n"
            + "    alert(tr.cells.length);\n"
            + "    alert(tr.cells[tr.cells.length-1].id);\n"
            + "    try { tr.deleteCell(25); } catch(e) { alert('exception'); }\n"
            + "    try { tr.deleteCell(-2); } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table>\n"
            + "    <tr id='myId'/>\n"
            + "      <td id='td1'>1</td>\n"
            + "      <td id='td2'>2</td>\n"
            + "      <td id='td3'>3</td>\n"
            + "      <td id='td4'>4</td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "4", "exception", "4" }, IE = { "4", "3" })
    public void deleteCell_noArg() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var tr = document.getElementById('myId');\n"
            + "    alert(tr.cells.length);\n"
            + "    try { tr.deleteCell(); } catch(e) { alert('exception'); }\n"
            + "    alert(tr.cells.length);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <table>\n"
            + "    <tr id='myId'/>\n"
            + "      <td id='td1'>1</td>\n"
            + "      <td id='td2'>2</td>\n"
            + "      <td id='td3'>3</td>\n"
            + "      <td id='td4'>4</td>\n"
            + "    </tr>\n"
            + "  </table>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
