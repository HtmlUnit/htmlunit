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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for {@link Navigator}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class Navigator2Test extends SimpleWebTestCase {

    /**
     * Tests the "cookieEnabled" property.
     * @throws Exception on test failure
     */
    @Test
    public void cookieEnabled() throws Exception {
        cookieEnabled(true);
        cookieEnabled(false);
    }

    private void cookieEnabled(final boolean cookieEnabled) throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.cookieEnabled);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        setExpectedAlerts(Boolean.toString(cookieEnabled));
        if (!cookieEnabled) {
            getWebClient().getCookieManager().setCookiesEnabled(cookieEnabled);
        }

        loadPageWithAlerts(html);
    }

    /**
     * Tests the "javaEnabled" method.
     * @throws Exception on test failure
     */
    @Test
    public void javaEnabled() throws Exception {
        attribute("javaEnabled()", "false");
        final WebClient webClient = getWebClient();
        webClient.getOptions().setAppletEnabled(true);
        attribute("javaEnabled()", "true");
    }

    /**
     * Generic method for testing the value of a specific navigator attribute.
     * @param name the name of the attribute to test
     * @param value the expected value for the named attribute
     * @throws Exception on test failure
     */
    void attribute(final String name, final String value) throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       alert('" + name + " = ' + window.navigator." + name + ");\n"
                + "    }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload=\'doTest()\'>\n"
                + "</body>\n"
                + "</html>";

        setExpectedAlerts(name + " = " + value);
        loadPageWithAlerts(html);
    }

}
