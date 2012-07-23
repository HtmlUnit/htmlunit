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
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.Namespace}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NamespaceTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "me,http://mysite" })
    public void test() throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:me='http://mysite'>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (document.namespaces)\n"
            + "      for (var i=0; i < document.namespaces.length; i++)\n"
            + "        alert(document.namespaces[i].name + ',' + document.namespaces[i].urn);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<app:dIv xmlns='http://anotherURL'></app:dIv>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
