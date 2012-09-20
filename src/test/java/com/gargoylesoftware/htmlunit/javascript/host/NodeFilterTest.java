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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME16;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link NodeFilter}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NodeFilterTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "2", "3", "1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048" },
            IE = "exception")
    public void constants() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  var properties = ['FILTER_ACCEPT', 'FILTER_REJECT', 'FILTER_SKIP',\n"
            + "'SHOW_ELEMENT', 'SHOW_ATTRIBUTE', 'SHOW_TEXT', 'SHOW_CDATA_SECTION',\n"
            + "'SHOW_ENTITY_REFERENCE', 'SHOW_ENTITY', 'SHOW_PROCESSING_INSTRUCTION', 'SHOW_COMMENT',\n"
            + "'SHOW_DOCUMENT', 'SHOW_DOCUMENT_TYPE', 'SHOW_DOCUMENT_FRAGMENT', 'SHOW_NOTATION'];\n"
            + "  try {\n"
            + "    for (var i=0; i<properties.length; ++i) {\n"
            + "      alert(NodeFilter[properties[i]]);\n"
            + "    }\n"
            + "  } catch(e) { alert('exception');}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(CHROME16)
    @Alerts(DEFAULT = "-1", FF3_6 = "4294967295", FF10 = "4294967295", IE = "exception")
    public void constants_SHOW_ALL() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  try {\n"
            + "      alert(NodeFilter.SHOW_ALL);\n"
            + "  } catch(e) { alert('exception');}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }
}
