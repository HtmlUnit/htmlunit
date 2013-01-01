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
package com.gargoylesoftware.htmlunit;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for {@link UrlFetchWebConnection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Pieter Herroelen
 */
@RunWith(BrowserRunner.class)
public class UrlFetchWebConnection2Test extends SimpleWebTestCase {

    /**
     * Test a HEAD request with additional headers.
     * @throws Exception if the test fails
     */
    @Test
    public void head() throws Exception {
        final WebRequest referenceRequest = getHeadRequest();

        getWebClient().setWebConnection(new UrlFetchWebConnection(getWebClient()));
        final WebRequest newRequest = getHeadRequest();

        // compare the two requests
        UrlFetchWebConnectionTest.compareRequests(referenceRequest, newRequest);
    }

    private WebRequest getHeadRequest() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var request;\n"
            + "    if (window.XMLHttpRequest)\n"
            + "      request = new XMLHttpRequest();\n"
            + "    else if (window.ActiveXObject)\n"
            + "      request = new ActiveXObject('Microsoft.XMLHTTP');\n"
            + "    request.open('HEAD', 'second.html', false);\n"
            + "    request.setRequestHeader('X-Foo', '123456');\n"
            + "    request.send('');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        getMockWebConnection().setDefaultResponse("");
        loadPage(html);

        return getMockWebConnection().getLastWebRequest();
    }

}
