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
 * Tests for {@link Attr}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class AttrTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "true", "false" },
            FF = { "true", "exception thrown" })
    public void specified() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  try {\n"
            + "    var s = document.getElementById('testSelect');\n"
            + "    var o1 = s.options[0];\n"
            + "    alert(o1.getAttributeNode('value').specified);\n"
            + "    var o2 = s.options[1];\n"
            + "    alert(o2.getAttributeNode('value').specified);\n"
            + "  } catch(e) {\n"
            + "    alert('exception thrown');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option name='option1' value='foo'>One</option>\n"
            + "        <option>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "undefined", FF = "[object HTMLOptionElement]")
    public void ownerElement() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var s = document.getElementById('testSelect');\n"
            + "  var o1 = s.options[0];\n"
            + "  alert(o1.getAttributeNode('value').ownerElement);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' id='testSelect'>\n"
            + "        <option name='option1' value='foo'>One</option>\n"
            + "        <option>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
