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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link ApplicationCache}.
 *
 * @author Daniel Gredler
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ApplicationCacheTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object ApplicationCache]",
            CHROME = "undefined",
            EDGE = "undefined",
            FF = "[object OfflineResourceList]",
            FF_ESR = "[object OfflineResourceList]")
    public void scriptableToString() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(window.applicationCache);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function(e){}",
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void onchecking() throws Exception {
        eventHandler("onchecking");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function(e){}",
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void onerror() throws Exception {
        eventHandler("onerror");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function(e){}",
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void onnoupdate() throws Exception {
        eventHandler("onnoupdate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function(e){}",
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void ondownloading() throws Exception {
        eventHandler("ondownloading");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function(e){}",
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void onprogress() throws Exception {
        eventHandler("onprogress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function(e){}",
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void onupdateready() throws Exception {
        eventHandler("onupdateready");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function(e){}",
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void oncached() throws Exception {
        eventHandler("oncached");
    }

    private void eventHandler(final String handler) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.applicationCache) {\n"
            + "      window.applicationCache." + handler + " = function(e) {};\n"
            + "      var handler = window.applicationCache." + handler + ".toString();\n"

                     // normalize, testing function.toString() is done somewhere else
            + "      log(handler.replace(/(\\r|\\n|\\r\\n| )/gm, ''));\n"
            + "    } else {\n"
            + "      log('no applicationCache');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "false", "false", "true", "true"},
            CHROME = "no applicationCache",
            EDGE = "no applicationCache")
    public void eventListener() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    if (window.applicationCache) {\n"
            + "      log(window.applicationCache.addEventListener == null);\n"
            + "      log(window.applicationCache.removeEventListener == null);\n"
            + "      log(window.applicationCache.dispatchEvent == null);\n"

            + "      log(window.applicationCache.attachEvent == null);\n"
            + "      log(window.applicationCache.detachEvent == null);\n"
            + "    } else {\n"
            + "      log('no applicationCache');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
