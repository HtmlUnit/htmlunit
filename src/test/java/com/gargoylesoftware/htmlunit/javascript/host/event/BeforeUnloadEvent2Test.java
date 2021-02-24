/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.event;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link BeforeUnloadEvent}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BeforeUnloadEvent2Test extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Second")
    public void nothing() throws Exception {
        onbeforeunload("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First")
    public void setString() throws Exception {
        onbeforeunload("e.returnValue = 'Hello'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First")
    public void setNull() throws Exception {
        onbeforeunload("e.returnValue = null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First")
    public void returnString() throws Exception {
        onbeforeunload("return 'Hello'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Second",
            IE = "First")
    public void returnNull() throws Exception {
        onbeforeunload("return null");
    }

    private void onbeforeunload(final String functionBody) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>First</title><script>\n"
            + "  window.onbeforeunload = function (e) {\n"
            + "    " + functionBody + ";\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <a href='" + URL_SECOND + "'>Click Here</a>\n"
            + "</body></html>";

        final String html2 = "<html><head><title>Second</title></head><body></body></html>";
        getMockWebConnection().setResponse(URL_SECOND, html2);

        final AtomicBoolean called = new AtomicBoolean();
        getWebClient().setOnbeforeunloadHandler((page, event) -> {
            called.set(true);
            return false;
        });
        final HtmlPage page = loadPage(html);
        final HtmlPage page2 = page.getAnchorByText("Click Here").click();

        final boolean expectedFirst = "First".equals(getExpectedAlerts()[0]);
        if (expectedFirst) {
            assertSame(page, page2);
            assertTrue(called.get());
        }
        else {
            assertNotSame(page, page2);
            assertFalse(called.get());
        }
        assertEquals(getExpectedAlerts()[0], page2.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First")
    public void setNullReturnString() throws Exception {
        onbeforeunload("e.returnValue = null;\n"
                + "return 'Hello'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First")
    public void setStringReturnNull() throws Exception {
        onbeforeunload("e.returnValue = 'Hello';\n"
                + "return null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First")
    public void setNullReturnNull() throws Exception {
        onbeforeunload("e.returnValue = null;\n"
                + "return null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("First")
    public void setStringReturnString() throws Exception {
        onbeforeunload("e.returnValue = 'Hello';\n"
                + "return 'Hello'");
    }
}
