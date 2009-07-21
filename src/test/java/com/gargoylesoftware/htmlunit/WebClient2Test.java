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
package com.gargoylesoftware.htmlunit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link WebClient} that run with BrowserRunner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WebClient2Test extends WebServerTestCase {

    /**
     * @throws Exception If an error occurs
     */
    @Test
    @Alerts(IE = "http://first/?param=\u00A3", FF = "http://first/?param=%A3")
    public void encodeURL() throws Exception {
        final String html = "<body onload='alert(window.location.href)'></body>";
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);

        webClient.setWebConnection(webConnection);
        webClient.getPage("http://first/?param=\u00A3");
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }
}
