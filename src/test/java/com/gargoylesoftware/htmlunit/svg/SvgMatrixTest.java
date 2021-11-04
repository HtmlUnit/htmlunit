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
package com.gargoylesoftware.htmlunit.svg;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SvgMatrixTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGMatrix() { [native code] }",
            IE = "[object SVGMatrix]")
    public void simpleScriptable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.SVGMatrix);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1, 0, 0, 1, 0, 0", "2, 3, 4, 5, 6, 7"})
    public void fields() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function alertFields(m) {\n"
            + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
            + "  for (var i = 0; i < fields.length; i++) {\n"
            + "    fields[i] = m[fields[i]];\n"
            + "  }\n"
            + "  log(fields.join(', '));\n"
            + "}\n"
            + "var svg =  document.getElementById('myId');\n"
            + "try {\n"
            + "  var m = svg.createSVGMatrix();\n"
            + "  alertFields(m);\n"
            + "  m.a = 2;\n"
            + "  m.b = 3;\n"
            + "  m.c = 4;\n"
            + "  m.d = 5;\n"
            + "  m.e = 6;\n"
            + "  m.f = 7;\n"
            + "  alertFields(m);\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function", "function", "function", "function", "function", "function", "function",
             "function", "function", "function"})
    public void methods() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var svg =  document.getElementById('myId');\n"
            + "try {\n"
            + "  var m = svg.createSVGMatrix();\n"
            + "  log(typeof m.flipX);\n"
            + "  log(typeof m.flipY);\n"
            + "  log(typeof m.inverse);\n"
            + "  log(typeof m.multiply);\n"
            + "  log(typeof m.rotate);\n"
            + "  log(typeof m.rotateFromVector);\n"
            + "  log(typeof m.scale);\n"
            + "  log(typeof m.scaleNonUniform);\n"
            + "  log(typeof m.skewX);\n"
            + "  log(typeof m.skewY);\n"
            + "  log(typeof m.translate);\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "-1", "-2", "3", "4", "5", "6"})
    public void flipX() throws Exception {
        transformTest("flipX()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "1", "2", "-3", "-4", "5", "6"})
    public void flipY() throws Exception {
        transformTest("flipY()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "-2", "1", "1.5", "-0.5", "1", "-2"})
    public void inverse() throws Exception {
        transformTest("inverse()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void inverseNotPossible() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>\n"
                + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
                + "  </svg>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function alertFields(m) {\n"
                + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
                + "  for (var i = 0; i < fields.length; i++) {\n"
                + "    fields[i] = m[fields[i]];\n"
                + "  }\n"
                + "  log(fields.join(', '));\n"
                + "}\n"
                + "var svg =  document.getElementById('myId');\n"
                + "try {\n"
                + "  var m = svg.createSVGMatrix();\n"
                + "  m.a = 1;\n"
                + "  m.b = 1;\n"
                + "  m.c = 1;\n"
                + "  m.d = 1;\n"
                + "  m.e = 5;\n"
                + "  m.f = 6;\n"
                + "  m = m.inverse();\n"
                + "  alertFields(m);\n"
                + "} catch(e) { log('exception'); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "25", "38", "17", "26", "14", "20"})
    public void multiply() throws Exception {
        transformTest("multiply(n)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "1.2322946786880493", "2.307671070098877",
                       "2.912292957305908", "3.8307511806488037", "5", "6"},
            CHROME = {"false", "1.2322946209166628", "2.307671050377636",
                      "2.912292905471539", "3.8307511434768218", "5", "6"},
            EDGE = {"false", "1.2322946209166628", "2.307671050377636",
                    "2.912292905471539", "3.8307511434768218", "5", "6"},
            IE = {"false", "1.2322945594787597", "2.307671070098877",
                  "2.912292718887329", "3.8307509422302246", "5", "6"})
    public void rotate() throws Exception {
        transformTest("rotate(4.5)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "3.147735595703125", "4.346245765686035",
                       "-0.3029201924800873", "-1.0536353588104248", "5", "6"},
            CHROME = {"false", "3.1477355949224934", "4.346245800520598",
                      "-0.302920161854466", "-1.053635345580751", "5", "6"},
            EDGE = {"false", "3.1477355949224934", "4.346245800520598",
                    "-0.302920161854466", "-1.053635345580751", "5", "6"},
            IE = {"false", "3.147735595703125", "4.346245765686035",
                  "-0.30292022228240967", "-1.0536353588104248", "5", "6"})
    public void rotateFromVector() throws Exception {
        transformTest("rotateFromVector(17, 74)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"false", "3", "4", "-1", "-2", "5", "6"})
    @HtmlUnitNYI(IE = "exception")
    public void rotateFromVectorZeroX() throws Exception {
        transformTest("rotateFromVector(0, 74)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"false", "1", "2", "3", "4", "5", "6"})
    @HtmlUnitNYI(IE = "exception")
    public void rotateFromVectorZeroY() throws Exception {
        transformTest("rotateFromVector(17, 0)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rotateFromVectorZeroXY() throws Exception {
        transformTest("rotateFromVector(0, 0)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "3", "6", "9", "12", "5", "6"})
    public void scale() throws Exception {
        transformTest("scale(3)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "7", "14", "21", "28", "5", "6"})
    public void scaleNonUniform() throws Exception {
        transformTest("scale(7, 22)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "1", "2", "3.0699267387390137", "4.139853477478027", "5", "6"},
            CHROME = {"false", "1", "2", "3.0699268119435104", "4.139853623887021", "5", "6"},
            EDGE = {"false", "1", "2", "3.0699268119435104", "4.139853623887021", "5", "6"},
            IE = {"false", "1", "2", "3.0699267387390136", "4.139853477478027", "5", "6"})
    public void skewX() throws Exception {
        transformTest("skewX(4)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"false", "1.6926045417785645", "2.9234728813171387", "3", "4", "5", "6"},
            CHROME = {"false", "1.6926045733766895", "2.9234727645022525", "3", "4", "5", "6"},
            EDGE = {"false", "1.6926045733766895", "2.9234727645022525", "3", "4", "5", "6"},
            IE = {"false", "1.6926045417785644", "2.9234728813171386", "3", "4", "5", "6"})
    public void skewY() throws Exception {
        transformTest("skewY(13)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "1", "2", "3", "4", "69", "100"})
    public void translate() throws Exception {
        transformTest("translate(13 , 17)");
    }

    private void transformTest(final String transforamtion) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function alertFields(m) {\n"
            + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
            + "  for (var i = 0; i < fields.length; i++) {\n"
            + "    log(m[fields[i]]);\n"
            + "  }\n"
            + "}\n"
            + "var svg =  document.getElementById('myId');\n"
            + "try {\n"
            + "  var m = svg.createSVGMatrix();\n"
            + "  m.a = 1;\n"
            + "  m.b = 2;\n"
            + "  m.c = 3;\n"
            + "  m.d = 4;\n"
            + "  m.e = 5;\n"
            + "  m.f = 6;\n"

            + "  var n = svg.createSVGMatrix();\n"
            + "  n.a = 7;\n"
            + "  n.b = 6;\n"
            + "  n.c = 5;\n"
            + "  n.d = 4;\n"
            + "  n.e = 3;\n"
            + "  n.f = 2;\n"

            + "  r = m." + transforamtion + ";\n"
            + "  log(m === r);\n"
            + "  alertFields(r);\n"
            + "} catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();

        final WebDriver driver = loadPage2(html, URL_FIRST);
        final String[] actualAlerts = driver.getTitle().split("§");

        assertEquals(expectedAlerts.length, actualAlerts.length);
        if (useRealBrowser()) {
            for (int i = 0; i < expectedAlerts.length; i++) {
                assertEquals("Expected: " + String.join(",", expectedAlerts)
                                + " Actual: " + String.join(",", actualAlerts),
                        expectedAlerts[i], actualAlerts[i]);
            }
        }
        else {
            for (int i = 0; i < expectedAlerts.length; i++) {
                try {
                    Assert.assertEquals(
                            Double.parseDouble(expectedAlerts[i]),
                            Double.parseDouble(actualAlerts[i]), 0.000001);
                }
                catch (final NumberFormatException e) {
                    assertEquals(expectedAlerts[i], actualAlerts[i]);
                }
            }
        }
    }
}
