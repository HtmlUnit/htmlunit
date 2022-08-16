/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link Screen}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ronald Brill
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535868.aspx">MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
@RunWith(BrowserRunner.class)
public class ScreenTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1040", "1040"})
    public void availHeight() throws Exception {
        testNumericProperty("availHeight");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"0", "0"},
            IE = {"undefined", "1234"})
    public void availLeft() throws Exception {
        testNumericProperty("availLeft");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"0", "0"},
            IE = {"undefined", "1234"})
    public void availTop() throws Exception {
        testNumericProperty("availTop");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1920", "1920"})
    public void availWidth() throws Exception {
        testNumericProperty("availWidth");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            IE = {"0", "0"})
    public void bufferDepth() throws Exception {
        testNumericProperty("bufferDepth");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"24", "24"})
    public void colorDepth() throws Exception {
        testNumericProperty("colorDepth");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            IE = {"96", "96"})
    public void deviceXDPI() throws Exception {
        testNumericProperty("deviceXDPI");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            IE = {"96", "96"})
    public void deviceYDPI() throws Exception {
        testNumericProperty("deviceYDPI");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "false"},
            IE = {"true", "true"})
    public void fontSmoothingEnabled() throws Exception {
        testBooleanProperty("fontSmoothingEnabled");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1080", "1080"})
    public void height() throws Exception {
        testNumericProperty("height");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            FF = {"0", "0"},
            FF_ESR = {"0", "0"})
    public void left() throws Exception {
        testNumericProperty("left");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            FF = {"0", "0"},
            FF_ESR = {"0", "0"})
    public void top() throws Exception {
        testNumericProperty("top");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            IE = {"96", "96"})
    public void logicalXDPI() throws Exception {
        testNumericProperty("logicalXDPI");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            IE = {"96", "96"})
    public void logicalYDPI() throws Exception {
        testNumericProperty("logicalYDPI");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"24", "24"})
    public void pixelDepth() throws Exception {
        testNumericProperty("pixelDepth");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            IE = {"96", "96"})
    public void systemXDPI() throws Exception {
        testNumericProperty("systemXDPI");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "1234"},
            IE = {"96", "96"})
    public void systemYDPI() throws Exception {
        testNumericProperty("systemYDPI");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"undefined", "1234"})
    public void updateInterval() throws Exception {
        testNumericProperty("updateInterval");
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1920", "1920"})
    public void width() throws Exception {
        testNumericProperty("width");
    }

    private void testBooleanProperty(final String prop) throws Exception {
        final String html = "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        log(window.screen." + prop + ");\n"
            + "      } catch(e) { log('get exception') }\n"

            + "      try {\n"
            + "        window.screen." + prop + " = false;\n"
            + "        log(window.screen." + prop + ");\n"
            + "      } catch(e) { log('set exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    private void testNumericProperty(final String prop) throws Exception {
        final String html = "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        log(window.screen." + prop + ");\n"
            + "      } catch(e) { log('get exception') }\n"

            + "      try {\n"
            + "        window.screen." + prop + " = 1234;\n"
            + "        log(window.screen." + prop + ");\n"
            + "      } catch(e) { log('set exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[object ScreenOrientation]", "landscape-primary", "0"},
            IE = {"undefined", "exception"})
    public void orientation() throws Exception {
        final String html = "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var o = window.screen.orientation;"
            + "        log(o);\n"
            + "        log(o.type);\n"
            + "        log(o.angle);\n"
            + "      } catch(e) { log('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "landscape-primary",
            FF_ESR = "landscape-primary")
    public void mozOrientation() throws Exception {
        final String html = "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        var o = window.screen.mozOrientation;"
            + "        log(o);\n"
            + "      } catch(e) { log('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "undefined",
            FF_ESR = "undefined",
            IE = "undefined")
    public void isExtended() throws Exception {
        final String html = "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      try {\n"
            + "        log(window.screen.isExtended);\n"
            + "      } catch(e) { log('exception') }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
