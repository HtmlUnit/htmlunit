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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link Enumerator}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class EnumeratorTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "false", "[object]", "undefined", "undefined", "true" })
    public void basicEnumerator() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var en = new Enumerator(document.forms);\n"
            + "  alert(en.atEnd());\n"
            + "  alert(en.item());\n"
            + "  alert(en.moveNext());\n"
            + "  alert(en.item());\n"
            + "  alert(en.atEnd());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form/>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Verifies that the enumerator constructor can take a form argument and then enumerate over the
     * form's input elements (bug 2645480).
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "f t1 t2", "t1", "t2" })
    public void formEnumerator() throws Exception {
        final String html
            = "<html><body><form id='f'><input type='text' name='t1' id='t1' />\n"
            + "<input type='text' name='t2' id='t2' /><div id='d'>d</div></form><script>\n"
            + "var f = document.forms.f, t1 = document.all.t1, t2 = document.all.t2;\n"
            + "alert(f.id + ' ' + t1.id + ' ' + t2.id);\n"
            + "var e = new Enumerator(f);\n"
            + "for( ; !e.atEnd(); e.moveNext()) alert(e.item().id);\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "undefined", "text" })
    public void item() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var form = document.forms.myForm;\n"
            + "  alert(form.elements.item(0).TyPe);\n"
            + "  alert(new Enumerator(form).item().TyPe);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form name='myForm'>\n"
            + "    <input name='myInput'>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }
}
