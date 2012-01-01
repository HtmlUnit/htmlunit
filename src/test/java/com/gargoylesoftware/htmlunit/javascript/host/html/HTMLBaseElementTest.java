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
 * Tests for {@link HTMLBaseElement}.
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLBaseElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "http://www.foo.com/images/", "", "", "_blank" })
    public void hrefAndTarget() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <base id='b1' href='http://www.foo.com/images/' />\n"
            + "    <base id='b2' target='_blank' />\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.getElementById('b1').href);\n"
            + "        alert(document.getElementById('b2').href);\n"
            + "        alert(document.getElementById('b1').target);\n"
            + "        alert(document.getElementById('b2').target);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

}
