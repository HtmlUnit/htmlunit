/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.geo;

import java.util.HashMap;
import java.util.Map;

import org.htmlunit.WebClientOptions;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link Geolocation}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author cd alexndr
 */
@RunWith(BrowserRunner.class)
public class GeolocationTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctor() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      new Geolocation();\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Geolocation]")
    public void navigatorGeolocation() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(navigator.geolocation);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"Latitude : 12.3456",
             "Longitude: 7.654321",
             "Accuracy: 0.1234"})
    public void getCurrentPosition() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function success(pos) {\n"
            + "    const crd = pos.coords;\n"
            + "    log(`Latitude : ${crd.latitude}`);\n"
            + "    log(`Longitude: ${crd.longitude}`);\n"
            + "    log(`Accuracy: ${crd.accuracy}`);\n"
            + "  }\n"

            + "  function error(err) {\n"
            + "    log(`ERROR(${err.code}): ${err.message}`);\n"
            + "  }"

            + "  function test() {\n"
            + "    try {\n"
            + "      navigator.geolocation.getCurrentPosition(success, error);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = getWebDriver();
        if (useRealBrowser()) {
            final Map<String, Object> pos = new HashMap<>();
            pos.put("latitude", 12.3456);
            pos.put("longitude", 7.654321);
            pos.put("accuracy", 0.1234);
            if (driver instanceof ChromeDriver) {
                ((ChromeDriver) driver).executeCdpCommand("Emulation.setGeolocationOverride", pos);
            }
            else if (driver instanceof EdgeDriver) {
                ((EdgeDriver) driver).executeCdpCommand("Emulation.setGeolocationOverride", pos);
            }
        }
        else {
            final WebClientOptions.Geolocation geo =
                    new WebClientOptions.Geolocation(12.3456, 7.654321, 0.1234, null, null, null, null);
            ((HtmlUnitDriver) driver).getWebClient().getOptions().setGeolocationEnabled(true);
            ((HtmlUnitDriver) driver).getWebClient().getOptions().setGeolocation(geo);
        }

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
