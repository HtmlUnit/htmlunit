/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Enumerator}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class EnumeratorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Enumerator not supported",
            IE = { "true", "undefined", "undefined", "undefined", "true" })
    public void basicEmptyEnumerator() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  if (typeof(Enumerator) != 'undefined') {\n"
            + "    try {\n"
            + "      var en = new Enumerator();\n"
            + "      alert(en.atEnd());\n"
            + "      alert(en.item());\n"
            + "      alert(en.moveNext());\n"
            + "      alert(en.item());\n"
            + "      alert(en.atEnd());\n"
            + "    } catch(e) { alert('exception'); }"
            + "  } else {\n"
            + "    alert('Enumerator not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Enumerator not supported",
            IE = { "false", "[object]", "undefined", "undefined", "true" },
            IE11 = { "exception" })
    public void basicEnumerator() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  if (typeof(Enumerator) != 'undefined') {\n"
            + "    try {\n"
            + "      var en = new Enumerator(document.forms);\n"
            + "      alert(en.atEnd());\n"
            + "      alert(en.item());\n"
            + "      alert(en.moveNext());\n"
            + "      alert(en.item());\n"
            + "      alert(en.atEnd());\n"
            + "    } catch(e) { alert('exception'); }"
            + "  } else {\n"
            + "    alert('Enumerator not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Enumerator not supported",
            IE = { "exception" })
    public void basicEnumeratorWrongType() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  if (typeof(Enumerator) != 'undefined') {\n"
            + "    try {\n"
            + "      var en = new Enumerator(window);\n"
            + "    } catch(e) { alert('exception'); }"
            + "  } else {\n"
            + "    alert('Enumerator not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that the enumerator constructor can take a form argument and then enumerate over the
     * form's input elements (bug 2645480).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "f t1 t2", "Enumerator not supported" },
            IE = { "f t1 t2", "t1", "t2" },
            IE11 = { "f t1 t2", "exception" })
    public void formEnumerator() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <form id='f'>\n"
            + "    <input type='text' name='t1' id='t1' />\n"
            + "    <input type='text' name='t2' id='t2' />\n"
            + "    <div id='d'>d</div>\n"
            + "  </form>\n"
            + "<script>\n"
            + "  var f = document.forms.f, t1 = document.all.t1, t2 = document.all.t2;\n"
            + "  alert(f.id + ' ' + t1.id + ' ' + t2.id);\n"
            + "  if (typeof(Enumerator) != 'undefined') {\n"
            + "    try {\n"
            + "      var e = new Enumerator(f);\n"
            + "      for( ; !e.atEnd(); e.moveNext()) {\n"
            + "        alert(e.item().id);\n"
            + "      }\n"
            + "    } catch(e) { alert('exception'); }"
            + "  } else {\n"
            + "    alert('Enumerator not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "Enumerator not supported" },
            IE = { "undefined", "text" },
            IE11 = { "undefined", "exception" })
    public void item() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var form = document.forms.myForm;\n"
            + "  alert(form.elements.item(0).TyPe);\n"
            + "  if (typeof(Enumerator) != 'undefined') {\n"
            + "    try {\n"
            + "      alert(new Enumerator(form).item().TyPe);\n"
            + "    } catch(e) { alert('exception'); }"
            + "  } else {\n"
            + "    alert('Enumerator not supported');\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <form name='myForm'>\n"
            + "    <input name='myInput'>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
