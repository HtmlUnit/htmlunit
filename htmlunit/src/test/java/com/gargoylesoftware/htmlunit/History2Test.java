/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link History} with {@link WebClient}.
 *
 * @author Madis Pärn
 */
@RunWith(BrowserRunner.class)
public class History2Test extends SimpleWebTestCase {

    /**
     * Tests history {@link WebClientOptions#setHistorySizeLimit(int) sizeLimit}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void historyCacheLimit() throws Exception {
        final String content = "<html><head><title></title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  for(var idx = 0; idx < 100; idx++) {\n"
                + "    history.pushState({}, 'Page '+idx, 'page_'+idx+'.html');\n"
                + "  }\n"
                + "  alert(history.length);\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        webClient.getOptions().setHistorySizeLimit(5);
        loadPageWithAlerts(content);
    }

}
