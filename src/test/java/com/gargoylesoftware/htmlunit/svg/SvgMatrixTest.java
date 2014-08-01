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
package com.gargoylesoftware.htmlunit.svg;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class SvgMatrixTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SVGMatrix]",
            FF = "function SVGMatrix() {\n    [native code]\n}",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void simpleScriptable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<script>\n"
            + "  alert(window.SVGMatrix);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1, 0, 0, 1, 0, 0", "2, 3, 4, 5, 6, 7" },
            IE8 = "exception")
    public void fields() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + "function alertFields(m) {\n"
            + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
            + "  for (var i=0; i<fields.length; ++i) {\n"
            + "    fields[i] = m[fields[i]];\n"
            + "  }\n"
            + "  alert(fields.join(', '));\n"
            + "}\n"
            + "var svg =  document.getElementById('myId');\n"
            + "try {\n"
            + "  var m = svg.createSVGMatrix();\n"
            + "  alertFields(m);\n"
            + "  m.a = 2;\n"
            + "  m.b = 3;\n"
            + "  m.c = 4;\n"
            + "  m.d = 5;\n"
            + "  m.e = 6;\n"
            + "  m.f = 7;\n"
            + "  alertFields(m);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "function", "function", "function", "function", "function", "function", "function",
            "function", "function", "function" },
            IE8 = "exception")
    public void methods() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + "  var svg =  document.getElementById('myId');\n"
            + "try {\n"
            + "  var m = svg.createSVGMatrix();\n"
            + "  alert(typeof m.flipX);\n"
            + "  alert(typeof m.flipY);\n"
            + "  alert(typeof m.inverse);\n"
            + "  alert(typeof m.multiply);\n"
            + "  alert(typeof m.rotate);\n"
            + "  alert(typeof m.rotateFromVector);\n"
            + "  alert(typeof m.scale);\n"
            + "  alert(typeof m.scaleNonUniform);\n"
            + "  alert(typeof m.skewX);\n"
            + "  alert(typeof m.skewY);\n"
            + "  alert(typeof m.translate);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
