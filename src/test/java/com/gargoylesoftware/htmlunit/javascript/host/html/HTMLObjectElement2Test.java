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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLObjectElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLObjectElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLFormElement]", "null" },
            IE8 = { "[object]", "null" })
    public void form() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <object id='o1'></object>\n"
            + "</form>\n"
            + "<object id='o2'></object>\n"
            + "<script>\n"
            + "  alert(document.getElementById('o1').form);\n"
            + "  alert(document.getElementById('o2').form);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
