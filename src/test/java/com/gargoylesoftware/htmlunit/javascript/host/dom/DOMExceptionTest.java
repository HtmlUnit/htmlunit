/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DOMException}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class DOMExceptionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" },
            IE = "exception")
    public void constants() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  var properties = ['INDEX_SIZE_ERR', 'DOMSTRING_SIZE_ERR', 'HIERARCHY_REQUEST_ERR',"
            + " 'WRONG_DOCUMENT_ERR', 'INVALID_CHARACTER_ERR', 'NO_DATA_ALLOWED_ERR', 'NO_MODIFICATION_ALLOWED_ERR',"
            + " 'NOT_FOUND_ERR', 'NOT_SUPPORTED_ERR', 'INUSE_ATTRIBUTE_ERR', 'INVALID_STATE_ERR', 'SYNTAX_ERR',"
            + " 'INVALID_MODIFICATION_ERR', 'NAMESPACE_ERR', 'INVALID_ACCESS_ERR'];\n"
            + "  try {\n"
            + "    for (var i=0; i<properties.length; ++i) {\n"
            + "      alert(DOMException[properties[i]]);\n"
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
    @Alerts(FF = { "undefined", "undefined", "undefined", "undefined" },
            IE = "exception")
    public void properties() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  try {\n"
            + "    alert(DOMException.code);\n"
            + "    alert(DOMException.filename);\n"
            + "    alert(DOMException.lineNumber);\n"
            + "    alert(DOMException.message);\n"
            + "  } catch(e) { alert('exception');}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test exception throw by an illegal DOM appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "3", "Node cannot be inserted at the specified point in the hierarchy",
            "6", "§§URL§§", "HIERARCHY_REQUEST_ERR: 3", "1" },
            IE = { "1" })
    public void appendChild_illegal_node() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var htmlNode = document.documentElement;\n"
            + "  var body = document.body;\n"
            + "  try {\n"
            + "    body.appendChild(htmlNode);\n"
            + "  } catch(e) {\n"
            + "    alert(e.code);\n"
            + "    alert(e.message);\n"
            + "    alert(e.lineNumber);\n"
            + "    alert(e.filename);\n"
            + "    alert('HIERARCHY_REQUEST_ERR: ' + e.HIERARCHY_REQUEST_ERR);\n"
            + "  };\n"
            + "  alert(body.childNodes.length);\n"
            + "}\n"
            + "</script></head><body onload='test()'><span>hi</span></body></html>";

        loadPageWithAlerts2(html);
    }
}
