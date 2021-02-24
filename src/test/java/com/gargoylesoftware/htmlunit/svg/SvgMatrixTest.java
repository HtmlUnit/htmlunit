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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
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
    @Alerts(DEFAULT = "function SVGMatrix() {\n    [native code]\n}",
            CHROME = "function SVGMatrix() { [native code] }",
            EDGE = "function SVGMatrix() { [native code] }",
            IE = "[object SVGMatrix]")
    public void simpleScriptable() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "<script>\n"
            + "  alert(window.SVGMatrix);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "function alertFields(m) {\n"
            + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
            + "  for (var i = 0; i < fields.length; i++) {\n"
            + "    fields[i] = m[fields[i]];\n"
            + "  }\n"
            + "  alert(fields.join(', '));\n"
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
            + "} catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "  var svg =  document.getElementById('myId');\n"
            + "try {\n"
            + "  var m = svg.createSVGMatrix();\n"
            + "  alert(typeof m.flipX);\n"
            + "  alert(typeof m.flipY);\n"
            + "  alert(typeof m.inverse);\n"
            + "  alert(typeof m.multiply);\n"
            + "  alert(typeof m.rotate);\n"
            + "  alert(typeof m.rotateFromVector);\n"
            + "  alert(typeof m.scale);\n"
            + "  alert(typeof m.scaleNonUniform);\n"
            + "  alert(typeof m.skewX);\n"
            + "  alert(typeof m.skewY);\n"
            + "  alert(typeof m.translate);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-1, -2, 3, 4, 5, 6")
    public void flipX() throws Exception {
        transformTest("flipX()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1, 2, -3, -4, 5, 6")
    public void flipY() throws Exception {
        transformTest("flipY()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-2, 1, 1.5, -0.5, 1, -2")
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
                + "function alertFields(m) {\n"
                + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
                + "  for (var i = 0; i < fields.length; i++) {\n"
                + "    fields[i] = m[fields[i]];\n"
                + "  }\n"
                + "  alert(fields.join(', '));\n"
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
                + "} catch(e) { alert('exception'); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("25, 38, 17, 26, 14, 20")
    public void multiply() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><body>\n"
                + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
                + "  </svg>\n"
                + "<script>\n"
                + "function alertFields(m) {\n"
                + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
                + "  for (var i = 0; i < fields.length; i++) {\n"
                + "    fields[i] = m[fields[i]];\n"
                + "  }\n"
                + "  alert(fields.join(', '));\n"
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
                + "  m = m.multiply(n);\n"
                + "  alertFields(m);\n"
                + "} catch(e) { alert('exception'); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1.2322946786880493, 2.307671070098877, 2.912292957305908, 3.8307511806488037, 5, 6",
            CHROME = "1.2322946209166628, 2.307671050377636, 2.912292905471539, 3.8307511434768218, 5, 6",
            EDGE = "1.2322946209166628, 2.307671050377636, 2.912292905471539, 3.8307511434768218, 5, 6",
            IE = "1.2322945594787597, 2.307671070098877, 2.912292718887329, 3.8307509422302246, 5, 6")
    public void rotate() throws Exception {
        transformTest("rotate(4.5)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "3.147735595703125, 4.346245765686035, -0.3029201924800873, -1.0536353588104248, 5, 6",
            CHROME = "3.1477355949224934, 4.346245800520598, -0.302920161854466, -1.053635345580751, 5, 6",
            EDGE = "3.1477355949224934, 4.346245800520598, -0.302920161854466, -1.053635345580751, 5, 6",
            IE = "3.147735595703125, 4.346245765686035, -0.30292022228240967, -1.0536353588104248, 5, 6")
    public void rotateFromVector() throws Exception {
        transformTest("rotateFromVector(17, 74)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "3, 4, -1, -2, 5, 6")
    @NotYetImplemented(IE)
    public void rotateFromVectorZeroX() throws Exception {
        transformTest("rotateFromVector(0, 74)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "1, 2, 3, 4, 5, 6")
    @NotYetImplemented(IE)
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
    @Alerts("3, 6, 9, 12, 5, 6")
    public void scale() throws Exception {
        transformTest("scale(3)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("7, 14, 21, 28, 5, 6")
    public void scaleNonUniform() throws Exception {
        transformTest("scale(7, 22)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1, 2, 3.0699267387390137, 4.139853477478027, 5, 6",
            CHROME = "1, 2, 3.0699268119435104, 4.139853623887021, 5, 6",
            EDGE = "1, 2, 3.0699268119435104, 4.139853623887021, 5, 6",
            IE = "1, 2, 3.0699267387390136, 4.139853477478027, 5, 6")
    public void skewX() throws Exception {
        transformTest("skewX(4)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "1.6926045417785645, 2.9234728813171387, 3, 4, 5, 6",
            CHROME = "1.6926045733766895, 2.9234727645022525, 3, 4, 5, 6",
            EDGE = "1.6926045733766895, 2.9234727645022525, 3, 4, 5, 6",
            IE = "1.6926045417785644, 2.9234728813171386, 3, 4, 5, 6")
    public void skewY() throws Exception {
        transformTest("skewY(13)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1, 2, 3, 4, 69, 100")
    public void translate() throws Exception {
        transformTest("translate(13 , 17)");
    }

    private void transformTest(final String transforamtion) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'>\n"
            + "  </svg>\n"
            + "<script>\n"
            + "function alertFields(m) {\n"
            + "  var fields = ['a', 'b', 'c', 'd', 'e', 'f'];\n"
            + "  for (var i = 0; i < fields.length; i++) {\n"
            + "    fields[i] = m[fields[i]];\n"
            + "  }\n"
            + "  alert(fields.join(', '));\n"
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
            + "  m = m." + transforamtion + ";\n"
            + "  alertFields(m);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();

        final WebDriver driver = loadPage2(html, URL_FIRST);
        final List<String> actualAlerts = getCollectedAlerts(DEFAULT_WAIT_TIME, driver, expectedAlerts.length);

        assertEquals(expectedAlerts.length, actualAlerts.size());
        if (useRealBrowser()) {
            for (int i = expectedAlerts.length - 1; i >= 0; i--) {
                assertEquals(expectedAlerts[i], actualAlerts.get(i));
            }
        }
        else {
            for (int i = expectedAlerts.length - 1; i >= 0; i--) {
                final String[] expected = StringUtils.split(expectedAlerts[i], ',');
                final String[] actual = StringUtils.split(actualAlerts.get(i), ',');

                assertEquals(expected.length, actual.length);
                for (int j = expected.length - 1; j >= 0; j--) {
                    try {
                        Assert.assertEquals(Double.parseDouble(expected[j]), Double.parseDouble(actual[j]), 0.000001);
                    }
                    catch (final NumberFormatException e) {
                        assertEquals(expected[j], actual[j]);
                    }
                }
            }
        }
    }
}
