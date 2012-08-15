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
package com.gargoylesoftware.htmlunit.html;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlOptionGroup}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HtmlOptionGroupTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLOptGroupElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <select>\n"
            + "    <optgroup id='myId' label='my label'>\n"
            + "      <option>My Option</option>\n"
            + "    </optgroup>\n"
            + "  </select>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlOptionGroup optionGroup = page.getHtmlElementById("myId");
        Assert.assertNotNull(optionGroup.getEnclosingSelect());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "false", "false", "true", "false", "true", "false", "false", "false" })
    public void testDisabled() throws Exception {
        final String html = "<html><body onload='test()'><form name='f'>\n"
            + "  <select name='s' id='s'>\n"
            + "    <optgroup id='g1' label='group 1'>\n"
            + "      <option value='o11' id='o11'>One</option>\n"
            + "      <option value='o12' id='o12'>Two</option>\n"
            + "    </optgroup>\n"
            + "    <optgroup id='g2' label='group 2' disabled='disabled'>\n"
            + "      <option value='o21' id='o21'>One</option>\n"
            + "      <option value='o22' id='o22'>Two</option>\n"
            + "    </optgroup>\n"
            + "  </select>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var g1 = document.getElementById('g1');\n"
            + "      var o11 = document.getElementById('o11');\n"
            + "      var g2 = document.getElementById('g2');\n"
            + "      var o21 = document.getElementById('o21');\n"
            + "      alert(g1.disabled);\n"
            + "      alert(o11.disabled);\n"
            + "      alert(g2.disabled);\n"
            + "      alert(o21.disabled);\n"
            + "      g1.disabled = true;\n"
            + "      g2.disabled = false;\n"
            + "      alert(g1.disabled);\n"
            + "      alert(o11.disabled);\n"
            + "      alert(g2.disabled);\n"
            + "      alert(o21.disabled);\n"
            + "    }\n"
            + "  </script>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final boolean disabled = getBrowserVersion().isFirefox();
        assertEquals(disabled, ((HtmlOptionGroup) page.getElementById("g1")).isDisabled());
        assertFalse(((HtmlOptionGroup) page.getElementById("g2")).isDisabled());
    }

}
