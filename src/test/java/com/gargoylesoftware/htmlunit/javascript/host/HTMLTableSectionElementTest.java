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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLTableSectionElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLTableSectionElementTest extends WebTestCase {

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
        loadPageWithAlerts(html);
    }

}
