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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLAppletElement}.
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLAppletElement2Test extends SimpleWebTestCase {

    /**
     * Tests calling applet method from JavaScript code.
     * @throws Exception if the test fails
     */
    @Test
    public void callAppletMethodFromJS() throws Exception {
        final URL url = getClass().getResource("/applets/simpleAppletDoIt.html");

        final WebClient webClient = getWebClient();
        final List<String> collectedStatus = new ArrayList<String>();
        final StatusHandler statusHandler = new StatusHandler() {
            public void statusMessageChanged(final Page page, final String message) {
                collectedStatus.add(message);
            }
        };
        webClient.setStatusHandler(statusHandler);
        webClient.getOptions().setAppletEnabled(true);

        final HtmlPage page = webClient.getPage(url);

        final HtmlButton button1 = page.getHtmlElementById("button1");
        button1.click();

        final HtmlButton button2 = page.getHtmlElementById("button2");
        button2.click();

        final String[] expectedStatus = {"Called: doIt('hello')", "Called: doIt('12345')"};
        assertEquals(expectedStatus, collectedStatus);
    }
}
