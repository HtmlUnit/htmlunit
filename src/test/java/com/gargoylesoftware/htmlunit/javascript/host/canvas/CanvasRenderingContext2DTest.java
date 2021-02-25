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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Unit tests for {@link CanvasRenderingContext2D}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CanvasRenderingContext2DTest extends WebDriverTestCase {

    /**
     * Function used in many tests.
     */
    public static final String LOG_TEXTAREA_FUNCTION = "  function log(msg) { "
            + "document.getElementById('myLog').value += msg + '§';}\n";

    /**
     * HtmlSniped to insert text area used for logging.
     */
    public static final String LOG_TEXTAREA = "  <textarea id='myLog' cols='80' rows='42'></textarea>\n";

    protected final WebDriver loadPageVerifyTextArea2(final String html) throws Exception {
        return loadPageTextArea2(html, getExpectedAlerts());
    }

    protected final WebDriver loadPageTextArea2(final String html, final String... expectedAlerts) throws Exception {
        final WebDriver driver = loadPage2(html);
        return verifyTextArea2(driver, expectedAlerts);
    }

    protected final WebDriver verifyTextArea2(final WebDriver driver,
            final String... expectedAlerts) throws Exception {
        final WebElement textArea = driver.findElement(By.id("myLog"));

        if (expectedAlerts.length == 0) {
            assertEquals("", textArea.getAttribute("value"));
            return driver;
        }

        if (!useRealBrowser()
                && expectedAlerts.length == 1
                && expectedAlerts[0].startsWith("data:image/png;base64,")) {
            String value = textArea.getAttribute("value");
            if (value.endsWith("§")) {
                value = value.substring(0, value.length() - 1);
            }
            compareImages(expectedAlerts[0], value);
            return driver;
        }

        final StringBuilder expected = new StringBuilder();
        for (int i = 0; i < expectedAlerts.length; i++) {
            expected.append(expectedAlerts[i].replaceAll("§§URL§§", URL_FIRST.toExternalForm())).append('§');
        }
        assertEquals(expected.toString(), textArea.getAttribute("value"));

        return driver;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("done")
    public void test() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.fillStyle = 'rgb(200,0,0)';\n"
            + "    ctx.fillRect(10, 10, 55, 50);\n"
            + "    ctx.fillStyle = 'rgba(0, 0, 200, 0.5)';\n"
            + "    ctx.fillRect(30, 30, 55, 50);\n"
            + "    ctx.drawImage(canvas, 1, 2);\n"
            + "    ctx.drawImage(canvas, 1, 2, 3, 4);\n"
            + "    ctx.drawImage(canvas, 1, 1, 1, 1, 1, 1, 1, 1);\n"
            + "    ctx.translate(10, 10);\n"
            + "    ctx.scale(10, 10);\n"
            + "    ctx.fillRect(30, 30, 55, 50);\n"
            + "    ctx.beginPath();\n"
            + "    ctx.moveTo(0, 10);\n"
            + "    ctx.lineTo(10, 10);\n"
            + "    ctx.quadraticCurveTo(0, 10, 15, 10);\n"
            + "    ctx.closePath();\n"
            + "    ctx.rotate(1.234);\n"
            + "    log('done');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <canvas id='myCanvas'></canvas></body>\n"
            + LOG_TEXTAREA
            + "</html>";
        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"addHitRegion", "drawCustomFocusRing", "drawSystemFocusRing", "removeHitRegion",
                        "scrollPathIntoView", "36 methods"},
            IE = {"addHitRegion", "drawCustomFocusRing", "drawSystemFocusRing", "ellipse",
                        "removeHitRegion", "scrollPathIntoView", "35 methods"})
    public void methods() throws Exception {
        final String[] methods = {"addHitRegion", "arc", "arcTo", "beginPath", "bezierCurveTo", "clearRect", "clip",
            "closePath", "createImageData", "createLinearGradient", "createPattern", "createRadialGradient",
            "drawImage", "drawCustomFocusRing", "drawSystemFocusRing", "ellipse", "fill", "fillRect", "fillText",
            "getImageData", "getLineDash", "isPointInPath", "lineTo", "measureText", "moveTo", "putImageData",
            "quadraticCurveTo", "rect", "removeHitRegion", "restore", "rotate", "save", "scale", "scrollPathIntoView",
            "setLineDash", "setTransform", "stroke", "strokeRect", "strokeText", "transform", "translate" };
        final String html = "<html><body>\n"
            + "<canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var nbMethods = 0;\n"
            + "  var methods = ['" + String.join("', '", methods) + "'];\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    for (var i = 0; i < methods.length; i++) {\n"
            + "      if (typeof ctx[methods[i]] == 'function')\n"
            + "        nbMethods++;\n"
            + "      else\n"
            + "        log(methods[i]);\n"
            + "    }\n"
            + "    log(nbMethods + ' methods');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    private void drawImage(final String png, final String canvasSetup, final String drawJS) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(png)) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok", "image/png", emptyList);
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "    var canvas = document.createElement('canvas');\n"
            + canvasSetup
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + drawJS
            + "      log(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' src='" + URL_SECOND + "'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAC60lEQVQ4T32TX2xTVRzHP/d27"
                    + "V273i0twTUiMhQeUBJFwQ62dVxxbIglZGa6EPTZwFCUxOe9+yIhRDL24gMJ/6FZgU3dDSYEAglIJDLIiA5Ey0Yd7W7vXbvd"
                    + "P+bepg3bA+fk5JzfOef3Sc75fb8CwMXxcanm8ZP9AUlqEEURUQTRPXi+2TCd1+5/1Kn8+Py24AYXRkYTG95d96tpWgiCgCC"
                    + "A4Pbyojw7DqIocPXGzcPJrg/6KhAPcCKVVto3NqvzpoXoASqjnFwZAb8Py7K4fOXGd93Jzm/dXA9wJjWstG7aoM6bJqIgeg"
                    + "mZgkm2VL4SkWBlRELXCxi6js/nc/589HB915Ytt6qAtpb3VPcJpg1X/y1RDMjItUEcHGQMzKLBW7EQdQEfAg7D6pVkb/f2d"
                    + "BWQcAGWzbW/dTJWkHCdTLReIiZZBMxZcgWDh89K7HxzCf6aGlKXRhcBWuPqM2OOwds5/KEwK5bUs1SuJd4oMqWVmNE0JrIz"
                    + "vLM8yutLw6Qu/rIQ0N4aV8cmC5y8lycUChEO1rEiItHRFCTss7mXyXHs9yk2r4zS9UYj5y/8vBCwuS2u/pHRuPnoPzpWRxj"
                    + "5B/7SbCzb5uNVQbSCzvfXn/B1SxPb1sY4l14MSDSrmlHiwNAYfesaOHJ3jul5Acdx6HpVYiKrcXuqyOAnb/NaYwNnFwDSw4"
                    + "rS0qzOFoucuj6OOlHgt6dz3meBg2BZnjp71kQ4sHUtcr1M6tJPyZ4d1SqkFaW9TbVMi2w2y9k7GU6P5ZieNbFth7Dko3tNl"
                    + "M/Xv8Kyl2NIAYnzQyPJnkoZj51IKR92KqorG9u2MQyD6RmNB091bAdWvSQTi8rIskzA70cUfRw/nUru7u0u6+DQD0ff/2zX"
                    + "p6OuCSpS9rTuOAiiQNlgYlWlricOHj6y45sv9w55gHgisXrfF3sHI5FIXdVp3knZkwucKUBuJj+7v++rPZOTj+941/r7+2v"
                    + "y+bx/sYNfFOu6bg4MDMz/DxMpOiCCBtdCAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACtUlEQVQ4jXXPz2vTdxzH8e/BMfw"
                    + "fKoLDgwcP23l0dGy3zcsOHj0LDtaLRTvaMlkPQztBBauwH3VOJSZt2oT8WKt+Eym2DekPutgkjbTNj+/vH/kmMc332/j0IG"
                    + "bEdB943T7vB6+XIAiCkM/nP44/TQ4lFlPjz1+kxp8vp8cXD0lkXjwnHPbCMbFf1U0qsoak6MiqjqIaqJqJqptohoWmmximx"
                    + "Vz0yc0ewB+KDsiqTrGiUJbUbkgzOkjVcTAti5m52C89gKIZlCSlc5wuyMQzMvGMwkpBQTdtdoslXm5lyW8X3kQXEp91Ae8n"
                    + "7FU0HqZK/LFRxZ9zeZxrEctZhDfKlFQLy3aw7Sq+2fA3XYCmm0iKTiC1y60llT83m8ztvWFF8VgvO4hZmakXuzi1Os3mfjc"
                    + "QDEUHNMMiV1S4OJdleKHMnbUa0wWPSr3NmvSaRE5hajHPZtGgud/qbaAbFslMke8DmwxFX3FFVJjasJGcFrVGk5VtiR8C68"
                    + "ysFtlvufhmY92AYVok/t3j18gqm9s7XBN3uBB6xfnZbeYzZaaXc3xxI8FMeo+W6/Y2MCybnbLCd7ef8XRplbO/L/H1nWW+m"
                    + "lziamSNC/eSfH7tH7aKGq77QYNgKDpgWjZlSeb6TJIz1yMcGw5yYjTMidEQn/wY5ORIkMsPEhiGiet5vRNMu4qmm7zcyvGz"
                    + "T+TTn4Icv+Tn2NBjTo1MM/xAJJsvUG808LyD7gn3/cEBy3awbAfDtCmWKqxnsvjFNL5naVYzeSRZpt5o4LouBwdt/vIF/gN"
                    + "u3P3tS7vqYDs1nFqdWr3xLrU69UaD180m+60WruvheQe0220mbt3+tgP09/efvP/3IzEcjac6icVT4dh8KhybT0U+yCNfIN"
                    + "nX13e6A4yNjR0ZHBw8+i4TRycm/j/v/01OTn4kCILwFgqgq1utHBHHAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACvElEQVQ4jXXPu29TZxzG8TO0Qvw"
                    + "FmVAlEEOHDu2SpSoJajdgqaKO3SIhpRJZGpUgkKiaoRSB2qK2MEDSVjEEOznkOL5ARGwjRBIrN6UmdhKUxJdzv/jYxvE5Md"
                    + "8OCLeu6Ss92/v76HkEQRCEmZmZQ9HZ1NDck4WR5NOFkeSz9MiTt0SKPf5SeNsTw7OfaIZFSdGRVQNFM1A1E0230AwL3bTRD"
                    + "QvTshEjj37uAO6KUq+iGeRLKkVZa4d0s4WUXRfLtgmJke87AFU3Kchq63hpWyGeUYhnVBa3VQzLYTdf4PlGltzm1qvp6OxH"
                    + "bcCbCXslnUC6wJ21MsGcx/1cg1jOJrxWpKDZ2I6L45QJhB6cagN0w0JWDULpXW7Ma4yu15nee8Wi6rNadElkFcae7eJWqtT"
                    + "r++1AUJR6ddMml1f5ejrL8GyRmysVJrd9StUmK/JLkjmVsaebrOdN6vuNzgaGaZPK5PkqtM5Q9AXfJlTG1hxkt0GlVmdxS+"
                    + "ZcaJWp5Tz7DY9AKNwOmJZN8q89rkWWWd/a4WpihwHpBWcfbPEoU2RyIceJn5JMLe3R8LzOBqbtsFNU+fzXOR7PL/PF7Xk+u"
                    + "7nAp7/N80NkhYHfU3x89SEbeR3P+0+DoCj1WrZDUVa4PpXizPUIR4ZFjl4Kc/SSxLELIscvipwfT2KaFp7vd06wnDK6YfF8"
                    + "I8d3Ewk+vCzy3jdBjgzd5/2LkwyPJ8hublOt1fD9g/YJo3eDvbbjYjsupuWQL5RYzWQJJpaYmFtiObOJrChUazU8z+PgoMl"
                    + "oYOIf4Novt046ZRfHreBWqlSqtdepVKnWarys19lvNPA8H98/oNlscuXHG6dbQHd39/E//gwkpEgs3Uo0lpai8bQUjafD/0"
                    + "4snh6/F0x1dXV90AJ6enre6evrO/w6g4cHB/8/b/719/e/KwiC8Dc3QqPsSaO0rwAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BA"
                    + "ACxjwv8YQUAAAKeSURBVDhPjVJNTxNRFD3zTSm1UyKlCsEIRCC6EVFijC5MFE1kqTv1B+jChXsSE6Nx48qYsDPRxI0rjR8J"
                    + "GDFGhRBRdiAhliKlLVRmhmk7nS/vey0IO8/Me2/m3nfuue++K4Dwen5ek5dXb6maFhdFEfSCO3YiAIqmNTc8dPZJ3cLB971"
                    + "6N37mxLGjE57nQxAEGszBVv5RW8OQAgv4PDX9aPjCuZucTSAtYLNcloIggE+DrUEQIiBCEAbEC/lgUWVZwqnBgRsvXr59wN"
                    + "kEHkCmZ1uZrwKymx5m1zz8KHhIGy63bRgmcrk8jvR03X4zNta/HYCBbRAFER6ddSJTwawdgSU3w5R1FH0Vk8s2JC2KVGofW"
                    + "pNJYcN09jPevwBsUJDplRLWiSCIKtQGGQfiIlJRCU2ahPeLJiRJhKZpNRJhOwCxYVV9fM1WsLLpwvE8eK6PtqiIUKwdEUEV"
                    + "SxsO37uFXRnkrCrKrktEBzmjBMu2IQQeuvYIiIgBvq2WsLBWqgWrY0cN2E2F6Iy4uNotohFlTC0VcedDBpPpIpaLFmZ+G/w"
                    + "6d/B3H6FD1/ApbaCw/gdTiznMrazhZ3Yd39MFjM/l4Fcd9LZEeLZbkLdmZlTEEKdTEh5+WcVMoQpFZu4QjzM+787LfQm0NE"
                    + "pcjFqCo5YBFYwZVUXFUG8rBtsasZcK7VcrcCsVqHT+K306rg+0QWvQSIzkfM6sZVCpeNwok2IymcS1k024dNjCQsGmbgS6k"
                    + "zGkmmOIxWIkopCWAMeh2yDwDIrFPC+sQL2uKDJ0PY7Ojnac7z+Ei8d70HOwHYlEgmcoULOxvfl8lpeCZ/Ds+dNMQo9/bNYT"
                    + "UfbPwd18qs91kKRhGuX7d+/9Yr/cNzIyIpumqbDv/4Vt297o6Kj7F1Q7+m7gqVhgAAAAAElFTkSuQmCC")
    public void drawImage() throws Exception {
        drawImage("html.png",
                "canvas.width = img.width; canvas.height = img.height;\n",
                "context.drawImage(img, 0, 0, canvas.width, canvas.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rendering...", "...done"})
    public void drawImage_noImage() throws Exception {
        final String html = "<html><body>\n"
            + "<img id='myImage'>\n"
            + "<canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var img = document.getElementById('myImage');\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var context = canvas.getContext('2d');\n"
            + "  log('rendering...');\n"
            + "  context.drawImage(img, 0, 0, 10, 10);\n"
            + "  log('...done');\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rendering...", "...done"})
    public void drawImage_invalidImage() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("invalid.png")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok", "image/png", emptyList);
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = "<html><body>\n"
            + "<img id='myImage'>\n"
            + "<canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "try {\n"
            + "  var img = document.getElementById('myImage');\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var context = canvas.getContext('2d');\n"
            + "  log('rendering...');\n"
            + "  context.drawImage(img, 0, 0, 10, 10);\n"
            + "  log('...done');\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQYV2P4z8DwHwAFAAH/plybXQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWP4z8DwHwAFAAH/q842iQAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWP4z8DwHwAFAAH/q842iQAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAANSURBVBhXY"
                + "/jPwPAfAAUAAf+mXJtdAAAAAElFTkSuQmCC")
    // The output depends on the deflation algorithm
    // check the output of: $pngcheck -v file.png
    // chrome gives: zlib: deflated, 256-byte window, fast compression
    // java gives:   zlib: deflated, 32K window, maximum compression
    // https://bugs.openjdk.java.net/browse/JDK-8056093
    public void drawImage_1x1_32bits() throws Exception {
        drawImage("1x1red_32_bit_depth.png",
                "canvas.width = img.width; canvas.height = img.height;\n",
                "context.drawImage(img, 0, 0, canvas.width, canvas.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQYV2P4z8DwHwAFAAH/plybXQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWP4z8DwHwAFAAH/q842iQAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWP4z8DwHwAFAAH/q842iQAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAANSURBVBhXY"
                + "/jPwPAfAAUAAf+mXJtdAAAAAElFTkSuQmCC")
    public void drawImage_1x1_24bits() throws Exception {
        drawImage("1x1red_24_bit_depth.png",
                "canvas.width = img.width; canvas.height = img.height;\n",
                "context.drawImage(img, 0, 0, canvas.width, canvas.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAF0lEQVQYV2P8z8Dwn5GBgZEBCuAM4gUAg+YCBZ6DWDYAAAA"
                + "ASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEElEQVQImWP4z8Dwn4EyAACCwQH/cCTq6QAAAABJRU5ErkJ"
                + "ggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEElEQVQImWP4z8Dwn4EyAACCwQH/cCTq6QAAAABJRU5ErkJ"
                + "ggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAQSURBVBh"
                + "XY/jPAEIUAAYGAILBAf8lQkk6AAAAAElFTkSuQmCC")
    public void drawImage3Arguments() throws Exception {
        drawImage("1x1red_32_bit_depth.png",
                "canvas.width = 4; canvas.height = 4;\n",
                "context.drawImage(img, 0, 0);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGklEQVQYV2NkQAOMWAX+MzD8Z2RgAEtiqAAAN+YCBdugwDMA"
                + "AAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEklEQVQImWNgIAr8Z2D4j1MSADcNAf/wu82GAAAAAElFTkSu"
                + "QmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEklEQVQImWNgIAr8Z2D4j1MSADcNAf/wu82GAAAAAElFTkSu"
                + "QmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAARSURBVBhX"
                + "YyAO/AcjrICBAQA3DQH/+OkmUAAAAABJRU5ErkJggg==")
    public void drawImage3ArgumentsPlacement() throws Exception {
        drawImage("1x1red_32_bit_depth.png",
                "canvas.width = 4; canvas.height = 4;\n",
                "context.drawImage(img, 1, 2);\n");
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAALUlEQVQYVzXI0Q0AIBDCUBjFSWVTR6nhov1qnpEwS/jIkp2EnagN"
                + "dAAG2oe3voowEQWmniBSAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJklEQVQImVXKwQkAIBDAsDiKm97m9SOIfYaKVrsIzEzxALp94B4H"
                + "kskZi4OAPRUAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJklEQVQImVXKwQkAIBDAsDiKm97m9SOIfYaKVrsIzEzxALp94B4H"
                + "kskZi4OAPRUAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAqSURBVBhXY/jP"
                + "AET/Ff+DaRBoaGgAc+ACIPAfCkBsJrAIEDAyMgJJBgYAY/AUlK91orEAAAAASUVORK5CYII=")
    public void drawImage3ArgumentsPlacementNegative() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 4; canvas.height = 4;\n",
                "context.drawImage(img, -1, -2);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAKklEQVQYVy3IsREAIAzEMP8oNMwZtuTSMIopQKXCVDtEkA3BoTl"
                + "vVhXhu4pUDldubjlMAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAJklEQVQImUXIsREAMAjEMDMKTeb8MdnGVFxUCp4KIuggZf9JImc"
                + "B04ARksgoEOwAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAJklEQVQImUXIsREAMAjEMDMKTeb8MdnGVFxUCp4KIuggZf9JImc"
                + "B04ARksgoEOwAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAlSURBVBhXY2BQ"
                + "+///PwPDf0YQwXCDAQj+KyJEGhsaQEIgwMAAAI7uDpdlPpgpAAAAAElFTkSuQmCC")
    public void drawImage3ArgumentsImageTooLarge() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 2; canvas.height = 5;\n",
                "context.drawImage(img, 0, 0);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAF0lEQVQYV2P8z8Dwn5GBgZEBCuAM4gUAg+YCBZ6DWDYAAAA"
                + "ASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEElEQVQImWP4z8Dwn4EyAACCwQH/cCTq6QAAAABJRU5ErkJ"
                + "ggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEElEQVQImWP4z8Dwn4EyAACCwQH/cCTq6QAAAABJRU5ErkJ"
                + "ggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAQSURBVBh"
                + "XY/jPAEIUAAYGAILBAf8lQkk6AAAAAElFTkSuQmCC")
    public void drawImage5Arguments() throws Exception {
        drawImage("1x1red_32_bit_depth.png",
                "canvas.width = 4; canvas.height = 4;\n",
                "context.drawImage(img, 0, 0, img.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGklEQVQYV2NkQAOMWAX+MzD8Z2RgAEtiqAAAN+YCBdugwDMA"
                + "AAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEklEQVQImWNgIAr8Z2D4j1MSADcNAf/wu82GAAAAAElFTkSu"
                + "QmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEklEQVQImWNgIAr8Z2D4j1MSADcNAf/wu82GAAAAAElFTkSu"
                + "QmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAARSURBVBhX"
                + "YyAO/AcjrICBAQA3DQH/+OkmUAAAAABJRU5ErkJggg==")
    public void drawImage5ArgumentsPlacement() throws Exception {
        drawImage("1x1red_32_bit_depth.png",
                "canvas.width = 4; canvas.height = 4;\n",
                "context.drawImage(img, 1, 2, img.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJUlEQVQYV2P8f4PhP6MGAyMDFDD+Z2D4z8iAV+D///+MjIxwLQCf"
                + "NQsFaCO3KAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGElEQVQImWP4f4PhPwMy+M9AUOD/fxQBAJOmCs946ShRAAAAAElF"
                + "TkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGElEQVQImWP4f4PhPwMy+M9AUOD/fxQBAJOmCs946ShRAAAAAElF"
                + "TkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAfSURBVBhXY/h"
                + "/g+E/AzIA8lAEmKA0HDAx/EdWwMAAADhoBtfglrXyAAAAAElFTkSuQmCC")
    public void drawImage5ArgumentsPlacementNegative() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 4; canvas.height = 4;\n",
                "context.drawImage(img, -3, -1, img.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAFklEQVQYV2P8z8DwnwEJMCJzQGzCAgCDawIDNDdv5wAAAABJR"
                + "U5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEUlEQVQImWP4z8DwHxkzkA4A2ncH+YHCac0AAAAASUVORK5CY"
                + "II=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAEUlEQVQImWP4z8DwHxkzkA4A2ncH+YHCac0AAAAASUVORK5CY"
                + "II=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAARSURBVBhXY/z"
                + "PwABEFAAGBgCDBQIA1ym4lAAAAABJRU5ErkJggg==")
    public void drawImageStretch() throws Exception {
        drawImage("1x1red_32_bit_depth.png",
                "canvas.width = 4; canvas.height = 4;\n",
                "context.drawImage(img, 0, 0, img.width, img.height, 0, 0, canvas.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAKklEQVQYVy3IsREAIAzEMP8oNMwZtuTSMIopQKXCVDtEkA3BoTl"
                + "vVhXhu4pUDldubjlMAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAJklEQVQImUXIsREAMAjEMDMKTeb8MdnGVFxUCp4KIuggZf9JImc"
                + "B04ARksgoEOwAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAJklEQVQImUXIsREAMAjEMDMKTeb8MdnGVFxUCp4KIuggZf9JImc"
                + "B04ARksgoEOwAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAlSURBVBhXY2BQ"
                + "+///PwPDf0YQwXCDAQj+KyJEGhsaQEIgwMAAAI7uDpdlPpgpAAAAAElFTkSuQmCC")
    public void drawImage5ArgumentsImageTooLarge() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 2; canvas.height = 5;\n",
                "context.drawImage(img, 0, 0, 4, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAj0lEQVQoU42QwQlCQQxEJyWIDawXsYJ/NQXZw5IeLGjsZBuQf1N"
                + "vkWyWj8I/bFgIs7wMkwgmSzp3dtcXUZ/WpR1r76H1TQggHfR4C4DbsL+PHnoB5DJA+MmVH1Rb07Ee0tFWxP+fYysFD9UOXEmU1r"
                + "b0G/i7j7sHHSFzChFQMuMeSFLNDCSTnbxO5JyrafAL84cwC261IdQAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAbUlEQVQYlb2OsQnDQAAD5c4eweDi03gE1xrlRzNZRGu48yiXxjw"
                + "PTgJpIlBxcAhJP2UFLyGTyWS8pDESzUOCTfC8unV8dOLAA2cmHolHnLnxbfEshb1W9lo5SwGp9e1dwEDo8lVMgm30afF/eQHveW"
                + "Ud7tvRzAAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAbUlEQVQYlb2OsQnDQAAD5c4eweDi03gE1xrlRzNZRGu48yiXxjw"
                + "PTgJpIlBxcAhJP2UFLyGTyWS8pDESzUOCTfC8unV8dOLAA2cmHolHnLnxbfEshb1W9lo5SwGp9e1dwEDo8lVMgm30afF/eQHveW"
                + "Ud7tvRzAAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACCSURBVChTvY7"
                + "BDcIwDEV/OLUjIHFIL6gTcMXZh3HYx2GTTBL327SoUrlw4SmO/3esr+A3rmZyUdNRolxv3gAeIPkV5kbxcEeea3fPeZrfe1ybTP"
                + "RsKkOU680fElvOeIm4xb1W5NZCO1xaE3cYv9t7V/YPPj/F6xcqU0spSOkY9leABXrPQu6AWg+SAAAAAElFTkSuQmCC")
    public void drawImage5ArgumentsStretchX() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 1, 1, 8, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAUUlEQVQoU2NkIBIwgtQ1CDf8r49sYGhc3sAAohmnMIDFkQFEYYP"
                + "Af/sDBgwHHS4w1Dd8AKnCrhCm8/////8bGxsZGhoa8CvE51wMnbgUDwWFAM8UFQtlZuKEAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAANUlEQVQYlWNgIAU0CDf8/5/D8B9G41bYIPB/v4PD/4YGgf//GfA"
                + "ohIH/////b2hoIKxwxAAAo0AXzo4A4+UAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAANUlEQVQYlWNgIAU0CDf8/5/D8B9G41bYIPB/v4PD/4YGgf//GfA"
                + "ohIH/////b2hoIKxwxAAAo0AXzo4A4+UAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAA1SURBVChTYyA"
                + "JNAg3/P+fw/AfRkOFUQATmMydwHDgigOYZpgCFsEP/gNBQ0MDVhNHImBgAADHvxPYgDOEzgAAAABJRU5ErkJggg==")
    public void drawImage5ArgumentsShrinkY() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 1, 1, 4, 3);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAOElEQVQYVwEtANL/AQAm//9A9sAAAUAcv/8wB9AAAb8JQP8QJ/A"
                + "AAb9ACP8QGP4AAUC/Gf8w3voAj4MRundMhZwAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAOElEQVQImQEtANL/AAAm//9AHL//AEAcv/9wI4//AL8JQP/PMDD"
                + "/AL9ACP/PWAb/AEC/Gf9wnRP/0o0VF9N8H0sAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAOElEQVQImQEtANL/AAAm//9AHL//AEAcv/9wI4//AL8JQP/PMDD"
                + "/AL9ACP/PWAb/AEC/Gf9wnRP/0o0VF9N8H0sAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAA0SURBVBhXY2R"
                + "Q+//f4dsBBiYQYcB+gYFxP6fDfwH1DwwsDOYnGBgk/jEwOuyX/G9w7xcDAHxREIP4we06AAAAAElFTkSuQmCC")
    public void drawImage5ArgumentsStretchImageTooLarge() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 2; canvas.height = 5;\n",
                "context.drawImage(img, 0, 0, 8, 12);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAWklEQVQoU7XQQQqAMAxE0T9H6ab0lsZbihuPMmJQV610YzYD4ZHA"
                + "iMnRpOMPWG3vQtVk0v8ig9mABleqDSAutg7kQubXxTWCJSILGMKnHttOKHWbeJcXvE0Xns6wHwst5jD0AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAO0lEQVQYldWMqREAIBDEQikY6rwy6SYIHscNlpiY7MIPNBU8viFo"
                + "x+1rWKwKbqePETEfs/AMFk8hyeMAl2Uvlc+P3zcAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAO0lEQVQYldWMqREAIBDEQikY6rwy6SYIHscNlpiY7MIPNBU8viFo"
                + "x+1rWKwKbqePETEfs/AMFk8hyeMAl2Uvlc+P3zcAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABHSURBVChTYxgS"
                + "QO3///8MDP/hNC4Akvx/A4ihNFQYC/ivCDEJRuMCIMmGhgaIifgUwgDQhWAA5WIAJigNBoyMjFAWOmBgAAAEXSqdGQ47LgAAAABJ"
                + "RU5ErkJggg==")
    public void drawImage5ArgumentsNegativeWidth() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 4, 4, -4, 6);\n");
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAWUlEQVQoU8XOQQqAMAxE0T9H6ab0lsZbihuPMmJQKGLFnSFkNo8k"
                + "4l7V9ipUTSZHn6O3BrMADY5UG0BcbG3Ihcy3jXMEU0QeGsLrDdtOKD3/2MPepP5SP8Idu74fCzQIyFMAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAPUlEQVQYlc2Mqw0AIBTEyigY5nxjsk0RfAQJBElNzV1hp6jg8glB"
                + "K04fh8ms4PS1GBG9eBuuw+BpyEvxIxoamS+VaAm+LAAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAPUlEQVQYlc2Mqw0AIBTEyigY5nxjsk0RfAQJBElNzV1hp6jg8glB"
                + "K04fh8ms4PS1GBG9eBuuw+BpyEvxIxoamS+VaAm+LAAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABGSURBVChTY8AA"
                + "av///2dg+A+ncQGQ5P8bQAylocJYwH9FiEkwGhcASTY0NEBMxKcQBoAuBAMol4EJSmMFjIyMUNZQAAwMAKgVKp2FLYz1AAAAAElF"
                + "TkSuQmCC")
    public void drawImage5ArgumentsNegativeHeight() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 4, 6, 4, -6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAXElEQVQoU6XQQQqAMAxE0T9H6ab0lsZbihuPMmJFKNRKwWxm8wiT"
                + "iMnRpKOH2fYulE1NbtNBg9mAAleqDCBOtg7kRM2vjWsES0StP4TPcbZdofTesYWt+fGewWNPzSgfC+"
                + "FFt4IAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAPUlEQVQYlbWMuw0AIBSEcBQb53xjug0WfjqNMZGG5g74T1HB5R2C"
                + "VpzeDpNZweljMSJ68TRch8HVkJviMw3cQS+VAulsRwAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAPUlEQVQYlbWMuw0AIBSEcBQb53xjug0WfjqNMZGG5g74T1HB5R2C"
                + "VpzeDpNZweljMSJ68TRch8HVkJviMw3cQS+VAulsRwAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABGSURBVChTY6AD"
                + "UPv//z8Dw384jQuAJP/fAGIoDRXGAv4rQkyC0bgASLKhoQFiIj6FMAB0IRhAuQxMUBorYGRkhLKoDxgYAAFaKp1TOe14AAAAAElF"
                + "TkSuQmCC")
    public void drawImage9Arguments() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 0, 0, img.width, img.height, 4, 2, img.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAi0lEQVQoU2M8FcD+n4GBgWFaBB+IYlgQ8ZoRzEADjOsDTMAKHyi8"
                + "YBD48IEhccEX7AoXJCRcYeXg4La/dk1U+u1bbsarV3EojI/XNo2IMFWRkMhiY2U1ZdTRwa4QZO3///8dGBgY6h88eOCgqKhIWOGB"
                + "AwccHB0dcSvE5ksMXxOjCKQGqzVYw5FYEwEUwCcLsjvElwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAbUlEQVQYlWM4FcD+/1QA+/+EFaL/E1aI/mfABTYEmPzfEGDyf0KB"
                + "zP8FCTy4FS5ISLiyNCPj/hM7uy//tbXxKIyP1766fXvCz/PnT/2/cgW3QgYGBob///87/P//f//9+/eJU7h//378Cgc5AABrbToy"
                + "XSlG0gAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAbUlEQVQYlWM4FcD+/1QA+/+EFaL/E1aI/mfABTYEmPzfEGDyf0KB"
                + "zP8FCTy4FS5ISLiyNCPj/hM7uy//tbXxKIyP1766fXvCz/PnT/2/cgW3QgYGBob///87/P//f//9+/eJU7h//378Cgc5AABrbToy"
                + "XSlG0gAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABzSURBVChTYzwV"
                + "wP6fAQimRfCBKIYFEa8ZwQw0wPSUQZcBhPVPsDPY7/gOFcYETB8EdK5+lTB5EHJW8WvCaXmoMBawIC5O+8r27Qk/zp8/9f/KFbAz"
                + "cIL///87/Pv3b//9+/dxKmSC0mDw4MEDKGsoAgYGAFZxKhyfkxO8AAAAAElFTkSuQmCC")
    public void drawImage9ArgumentsCrop() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 1, 2, 2, 4, 0, 0, img.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAXElEQVQoU2NkIBIwEqmOYegqVPv/P+HTAob6t40Mir8fgP2B1TPZ"
                + "YlP+m0udZPBS2sYgsu4tboX7HTj+S6j+ZVAw/MPAmfUft0LRV3L/Q9b8ZCiY8I1B/dZnsEIAKFkcC2BUM+"
                + "IAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAARUlEQVQYlWNgGAUwoP4/P1F8wcz7rAoz8arLFp+6apFB7P83QcL/"
                + "8So84Mi56noq6//v0xjxKxR/Jbcqc5r4/5tqvHCFALEhF41FNwM8AAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAARUlEQVQYlWNgGAUwoP4/P1F8wcz7rAoz8arLFp+6apFB7P83QcL/"
                + "8So84Mi56noq6//v0xjxKxR/Jbcqc5r4/5tqvHCFALEhF41FNwM8AAAAAElFTkSuQmCC",
            IE = {})
    @HtmlUnitNYI(IE = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAPklEQVR42mNgGAUwoPb/f4LE/P//GRj+41WXJTb1/yL92P//Iwko"
            + "PGDP8f9GCuv//4sJKGT4r/g/Yb4oitUApWwZ6B07eGEAAAAASUVORK5CYII=")
    public void drawImage9ArgumentsCropNegativStart() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, -1, -2, 3, 5, 4, 4, img.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAF0lEQVQoU2NkIBIwEqmOYVQh3pAiOngACmkAC5eMKzgAAAAASUV"
                + "ORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAADklEQVQYlWNgGAWDEwAAAZoAARbK02kAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAADklEQVQYlWNgGAWDEwAAAZoAARbK02kAAAAASUVORK5CYII=",
            IE = {})
    @HtmlUnitNYI(IE = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAADklEQVR42mNgGAWDEwAAAZoAAQDqGN4AAAAASUVORK5CYII=")
    public void drawImage9ArgumentsCropNegativWidth() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 0, 0, -3, 5, 4, 4, img.width, img.height);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAASElEQVQoU2Ocr5nwn4GBgSHx+gJGEI0LMM6v5oEobP2CX+EBB4f9"
                + "9gwMDowHDuBXCDJt/vz5/xMTEwkrxOc2mBxeU5ANGAoKAWtoEAsOuLTDAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAM0lEQVQYlWOYr5nwf75mwn8GQmBBNc//BdU8hBUecHDY/9/BgbBC"
                + "BgYGhvnz5xOncMQBAL0KD/QH8JFiAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAM0lEQVQYlWOYr5nwf75mwn8GQmBBNc//BdU8hBUecHDY/9/BgbBC"
                + "BgYGhvnz5xOncMQBAL0KD/QH8JFiAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAvSURBVChTY5iv"
                + "mfAfhBkIACaGoDUMYEwIHHBw2P/fwYGgiWAwf/584hSOMMDAAAB1Kw2mrjRWYgAAAABJRU5ErkJggg==")
    public void drawImage9ArgumentsStretch() throws Exception {
        drawImage("4x6.png",
                "canvas.width = 10; canvas.height = 10;\n",
                "context.drawImage(img, 0, 0, img.width, img.height, 0, 0, 2, 4);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAcElEQVQoU53QsQ2CUBQF0PNKYs0I7mDhBMbWNajsmMIV6FyG"
                + "BJ1DFyDkGxIo9AdDePV59yY3rLxY6WQwsZ+ei+AxB33BxAkVSjTBbQm+sMOAZ3DIYOKI+5TWo8M5eI/4t7rGBS2uM8rgvwW2"
                + "z7OU+gHU0xILszCyeAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAUUlEQVQYlb3KsQ1FYABG0TODHbQmMIFl3gB2MYcJRCOxhFIp"
                + "Ec2n0f665ya3O/y9UD83b6gLY1jC7w3u4QxHmEuoDVu4HjiFqoT7sIahiL7vBt20ISfDuXmqAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAUUlEQVQYlb3KsQ1FYABG0TODHbQmMIFl3gB2MYcJRCOxhFIp"
                + "Ec2n0f665ya3O/y9UD83b6gLY1jC7w3u4QxHmEuoDVu4HjiFqoT7sIahiL7vBt20ISfDuXmqAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABdSURBVChT"
                + "Y6A6YITScPCPgUEVyuRkYmC4BGWjAqAiLyDeAcRngTgfKowJgJJvgfgHEH8F4hNQYTAAmg4B/xkYbIDUDyCGif0DKhaCslEB"
                + "UKIaiC8A8TyciugNGBgAAdYaCnzestwAAAAASUVORK5CYII=")
    public void drawImageDataUrlPng() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "    var canvas = document.createElement('canvas');\n"
            + "    canvas.width = img.width;\n"
            + "    canvas.height = img.height;\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.drawImage(img, 0, 0, canvas.width, canvas.height);\n"
            + "      log(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' src='data:image/png;base64,"
                        + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABGdBTUEAALGP"
                        + "C/xhBQAAAAlwSFlzAAALEwAACxMBAJqcGAAAAAd0SU1FB9YGARc5KB0XV+IA"
                        + "AAAddEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIFRoZSBHSU1Q72QlbgAAAF1J"
                        + "REFUGNO9zL0NglAAxPEfdLTs4BZM4DIO4C7OwQg2JoQ9LE1exdlYvBBeZ7jq"
                        + "ch9//q1uH4TLzw4d6+ErXMMcXuHWxId3KOETnnXXV6MJpcq2MLaI97CER3N0"
                        + "vr4MkhoXe0rZigAAAABJRU5ErkJggg==' alt='red dot' />\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAvUlEQVQ4T2NkQAWODAwM5lChkwwMDPvR5AlyGaEqeEUYGLYIM"
                + "DDoBzAwcIPENjAwfP3AwHDxDQODDwMDw2eCJkEVgA0UYWA4mMDAYNXNwMCCrLGUgeHPAgaGY28YGOxJMdBRhYFh/W0GBn5sml"
                + "QZGD7eYWAIJNb7IBdWlDAwNKO7DmY4yJU9DAy1DAwMHcS4kiYGUt3LVI8UUNBQN9kgBTbVEjYxEUiUGlhOIUoxMYpGDSQmlPC"
                + "rGQ1DysMQAFq2MhXKNeEnAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkklEQVQ4je3QTQqCUBiG0bOSsAUU0SLMRdiPpa0kEFqm7UAnO"
                + "apBBRYVXZv6wDs93O/y3Bz7+2b+acQxoVpRr6gXVBFlb6zgfOHSXUbbB50nVK/YYzEnTEPAIqf9BG6osQsCsy9gSoM8BJz9cP"
                + "IkBBRRbt+8ck075hCEddGYakmT0sScemOdpijc/izszKGhoYCuJ19i4sc/7LEAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAjElEQVQ4je3SMQrCQBBA0dckZltLrxEkVxEUUudiqaw8l5VWw"
                + "ZXYRFSI4mrKPJj2M8Muryo0w1T+EAJtxmXDcccpIy7ZI6TXaEtiT/885SOapMpHYvfJiRLPb7ac3wVrutQNvwk2KcHJTxZo1y"
                + "OxkrjikBQbFIE2J9Z0Nd2C6/DCxS/Bu8k+9mw2++QGj8thmtPon2MAAAAASUVORK5CYII=",
            IE = {})
    @HtmlUnitNYI(IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAzUlEQVR42u2SvwpBYRyGn8Eiyqos/kxGVrkDo8VkJYNcgUtwD"
                + "crA7M+CLMoFGJQTirIapAgd78mmQ32nbOetZ/p+Pf2+7/3gX9lBvAGLHJxjcMvANgpVT7I9pPJwL4A9EZLbA5GFaxB6xsIKrI"
                + "oS2B/cRRKOGikbCdPwWLoIHbqw0UjfdElXmcMWLJ0fjGwq4WJ9EbZhqJGxkTABnRo8XYSn0FtWN71yWG3uSip8rW0d0RymERj"
                + "pbCYCXn5PWLR4v5mcqCeaXmV+/Pj5lRf9o2ZS6A1d/wAAAABJRU5ErkJggg==")
    public void drawImageDataUrlSvg() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "    var canvas = document.createElement('canvas');\n"
            + "    canvas.width = 20;\n"
            + "    canvas.height = 20;\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.drawImage(img, 0, 0, img.width, img.height);\n"
            + "      log(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' src='data:image/svg+xml,"
                    + "<svg xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.w3.org/2000/svg\" "
                            + "overflow=\"hidden\" width=\"10\" height=\"10\">"
                    + "  <circle cx=\"5\" cy=\"5\" r=\"4\" stroke=\"black\" stroke-width=\"1\" fill=\"red\" />"
                    + "</svg>' />\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"exception", "0", "true", "true"})
    public void measureText() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var canvas = document.getElementById('myCanvas');\n"
            + "        if (canvas.getContext){\n"
            + "          ctx = canvas.getContext('2d');\n"
            + "          try {\n"
            + "            log(ctx.measureText());\n"
            + "          } catch(e) { log('exception'); }\n"

            + "          var metrics = ctx.measureText('');\n"
            + "          log(metrics.width);\n"

            + "          metrics = ctx.measureText('a');\n"
            + "          log(metrics.width > 5);\n"

            + "          metrics = ctx.measureText('abc');\n"
            + "          log(metrics.width > 10);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    private void draw(final String canvasSetup, final String drawJS) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + drawJS
            + "      log(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + canvasSetup
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAABWEl"
                + "EQVRYR+3UvUodURTF8d99BMkLiI12QlqtBI0INqawuLFWtArYiVWwE4QUgbSSFElhKViktLXWzhewtpU"
                + "tc2Ey6P2AfbXZU82cM3udtf9nndPz/zOPDZy1hvvN+2+s4gYPnbru5wfs4xR7uMRd81PMneDoFZ12bfh"
                + "5jNreBEYvcIgfUzbatvR10OQkRkPgF37iL9ax1qge4BifsIzbMYmGxia2G5127TX+4Qr9l4z+wWKH9Be"
                + "0iS5gB9Hxx8ZkRCTGVxrq42x9GJ3DNywNq52EaNfoLCK3gwVisUHGzycgeo+gN7T2LYxutajFRrUbi12"
                + "YitG4DYLkd8yMSTTMRd1uE6fnzDUHcpTROLyfI8NdoiNunfebLqPZ7ItoEc0mkK1XGS2i2QSy9SqjRTS"
                + "bQLZeZbSIZhPI1quMFtFsAtl6ldEimk0gW68yWkSzCWTrVUaLaDaBbL0nX4NjZfKcb1cAAAAASUVORK5"
                + "CYII=",
            FF = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAn0lEQVR"
                + "Yhe3S4Q2DIBCG4W+WzuEgTuIozOIw7kJ/FJLzehSpJibmfRJixOP8BKW9SVJyc6nMS9KiMdvBvr31X3o"
                + "Nmws7L/o3aNOvhqukXK61LpexmOf2Y44Etb1b69coaA6G39FaV9ma6AR6QW24uhHSBUfv67bBeh80BfO"
                + "3BJU+u/5y91GvS4L6f3Qk6Kz97zSfCQoAAAAAAAAAwMO9AbwlTKQXMPMUAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAn0lEQVR"
                + "Yhe3S4Q2DIBCG4W+WzuEgTuIozOIw7kJ/FJLzehSpJibmfRJixOP8BKW9SVJyc6nMS9KiMdvBvr31X3o"
                + "Nmws7L/o3aNOvhqukXK61LpexmOf2Y44Etb1b69coaA6G39FaV9ma6AR6QW24uhHSBUfv67bBeh80BfO"
                + "3BJU+u/5y91GvS4L6f3Qk6Kz97zSfCQoAAAAAAAAAwMO9AbwlTKQXMPMUAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0I"
                + "Ars4c6QAAAARnQU1BAACxjwv8YQUAAADvSURBVFhH7ZJBDsFAFIbLURrHwBJHwMqWA3AGDmBtxxGwsHE"
                + "NbUTcg/+v92QibdKRWPm/5PNPTeZ15nWSD9pw/xq+mZtkbVmHhyXrsW6Iz1Xh80NYrG3yJ4Kp5a9pWE4"
                + "sozbq3bxAnpTPPDnlqX3MubqwBjse1iFepw/PMC3bKCd9IV1CMrNsWfYgTz6CGxt3YBfGcIJeZ8w/jB0"
                + "8QNbMyzbKSS50F7CMo+Ud+piklk5uGZJZkpsl61QSe0e/4QrDTvGT8vpEEbtRdiL2JSvILvtV2sIBrA"
                + "O/RnFHiychhBBCCCGEEEIIIYQQ4l9Ikiccki+D/HzKtwAAAABJRU5ErkJggg==")
    public void fillText() throws Exception {
        draw("<canvas id='myCanvas' width='42' height='42'>\n",
                "context.fillText('HtmlUnit', 3, 7);\n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object CanvasGradient]")
    public void createLinearGradient() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  var gradient = ctx.createLinearGradient(0, 0, 200, 0);\n"
            + "  log(gradient);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object CanvasGradient]")
    public void createRadialGradient() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  var gradient = ctx.createRadialGradient(100, 100, 100, 100, 100, 0);\n"
            + "  log(gradient);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1", "0.5", "0", "0.699999988079071", "0"},
            CHROME = {"1", "0.5", "0", "0.7", "0"},
            EDGE = {"1", "0.5", "0", "0.7", "0"})
    @NotYetImplemented({CHROME, EDGE})
    public void globalAlpha() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    log(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 0.5;\n"
            + "    log(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 0;\n"
            + "    log(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 0.7;\n"
            + "    log(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = null;\n"
            + "    log(ctx.globalAlpha);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0.5", "0.5", "0.5", "0.5"})
    public void globalAlphaInvalid() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.globalAlpha = 0.5;\n"
            + "    log(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = -1;\n"
            + "    log(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 'test';\n"
            + "    log(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = undefined;\n"
            + "    log(ctx.globalAlpha);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <canvas id='myCanvas'></canvas>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAHklEQVR42mNgGErgPxUwhoGUOmjUQBoYSNVY"
                + "pgoAANSxK9UjCpiOAAAAAElFTkSuQmCC")
    public void strokeRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.strokeRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAFklEQVR42mNgGErgP4V41MChYODg"
                + "BADEpF+hx8ArfgAAAABJRU5ErkJggg==")
    public void fillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAHklEQVR42mNggID/VMJwMGrgqIGjBo4aO"
                + "GrgiDMQAMu0ZqjgcrwWAAAAAElFTkSuQmCC")
    public void fillRectWidthHeight() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(1, 0, 18, 20);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR"
                + "42mNgGErgP4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
    public void fillRectRotate() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.rotate(.5);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAg0lEQVR42mNgGAW0ABrUNMwGiH8D8WYgVqGGgYeB+D8Ugwxe"
                + "TonBLkiG/UczeDIQC5Bq4HEcBsLwayBOAWIWYgyLIWAYMn5MjME8QNwAxJ9JMHg5MS6VAeJuIg12ISU8QQbPh0YINsO2kxvz"
                + "Bjgiy4LS9OmBZPB6auakACBWGC2daAsAdH9H/STLcEwAAAAASUVORK5CYII=")
    public void rotateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(.5); context.fillRect(6, 2, 12, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR"
                + "42mNgGErgP4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
    public void fillRectTranslate() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.translate(3, 4);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR42mNg"
                + "GAUjF/ynAI8aOFQMHAWjgJ4AABYtWaeMf5H/AAAAAElFTkSuQmCC")
    public void translateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.translate(3, 4); context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmElEQVR42mNgGAWjYHABAyDWAGIOahl4GIj/A/FnIN"
                + "4MxM1AHALEIuQYBnLVe6iB2DBIbj0QNwCxDxALEDLQBI9h2PBvIL4PxMuBOAOIHdANzCDRQHT8Gt3A5RQauBvdQFDY"
                + "7CcQjvjwZFxhyQINzxQgng/E14H4OxEG+pCaAhyg4TwfGhnoBkpQmmZ5gNgFiCuAePrQyr8APsZfqrO3m6kAAAAASU"
                + "VORK5CYII=")
    public void rotateTranslateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(0.2); context.translate(0, 4); context.fillRect(4, 4, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAhUlEQVR42mNgGAWjgGwgAcQc1DQwBo"
                + "j/A/FtIJ4NxBlAbEOJJTxA/B1qKDL+DMSXybVkMxYD/+Ow5DjUEpDPTHBZUkGkgdhwNzYDNSgw8DEQ"
                + "s2AzdDsQ/ybTUAt84akDxDnQcDpNpCUVpKQCASA2IGDJYUrTrgA0GYEsWQ3E54FYZAQWCgBOMlmQm3"
                + "NMhgAAAABJRU5ErkJggg==")
    public void transformTranslateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.setTransform(1, .2, .3, 1, 0, 0); context.translate(-5, 4); context.fillRect(4, 4, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQ4T2NkoDJgpLJ5DM"
                + "gG/qfQcLBZdDEQVzAQ8gFOF44gAymMZIh2qqdDAD9SCgvGd82WAAAAAElFTkSuQmCC")
    public void clearRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.clearRect(4, 4, 6, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXUlEQVQ4T2P878Lwn4GKgHHUQIpDcy"
                + "SHIeMeBkZYAFKSlOBhiGwgtpgh1hKiDSTWEpRYJuRKfGkK5oMhZiC6l0gJAqxeJibf4bMEZChVsh7M"
                + "EqoZiOwzqriQpgYCAH2SSD3nlbP6AAAAAElFTkSuQmCC")
    public void transformTranslateClearRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = '#ff4400'; context.fillRect(0, 0, 20, 20); "
                + "context.setTransform(1, .2, .3, 1, 0, 0); "
                + "context.translate(-5, 4); context.clearRect(4, 4, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAQUlEQVR42mNgoCHgoL"
                + "Zhr4H4MBBXALEBNQzlAeIQIJ4NxI+heDZUTIAaFugAcQkQ7wbi70Pa9Z+hFlIN8DAM"
                + "KQAA8ckQClhSuMUAAAAASUVORK5CYII=")
    public void moveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.lineTo(16, 6); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAgElEQVR42mNgGEr"
                + "g/6iBI8DA90DMQk0DLwOxBTUNzADi50C8HYhnA3ENEHsAMQ8lhooAsQvU8G4gPg"
                + "7En4F4PRAHUMvlIEsSgPg6EN8G4hxqhjUojPcD8WMgTqFmeJtAgwNkuAI1DQZ5/"
                + "z4QN1PTUFAYr6aFa0OgkUZVIED1UhoAGzcax1ioxw8AAAAASUVORK5CYII=")
    public void moveToBezierCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.bezierCurveTo(2, 17, 1, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAlklEQVR42mNgGErg"
            + "OxDfBuL9QNwNxAlArEGJgRxQAzyAuAGIVwPxayC+D7VAhRquZgFiCyBuhhoOcr0P"
            + "tYIE5IMcIH4MxOuBWIKaBndDXexCzUgEhfV7agYBA9SFn6HhTDVQAsTnoRFINbAf"
            + "ajDVgAY0U1DVlYepHes1QDybmgbaAPFpahqoAM1FVAM80BKLquD/4C+lAU3wGvNN"
            + "UrLUAAAAAElFTkSuQmCC")
    public void moveToQuadraticCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.quadraticCurveTo(19, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAJElEQVR42m"
            + "NgGAWjYHAAByBuoCIGE/+piIeAgVQPw1EwCkgEAB1tTchSTfsUAAAAAElF"
            + "TkSuQmCC")
    public void lineWidthMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.lineWidth = 4; context.moveTo(2, 10); context.lineTo(18, 10); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaUlEQVR42mNgGPGABYhdgFiAmobeBuL/UHo1EFdQ"
            + "aslkqIHYMMySElIMdMBjIDJWIcXQx0QYmEOKgdOJMHA/KQYGEOltGWIN5AHizUD8nICBGeTEugzUxc1YLFlPrbQK"
            + "s6RitCwYBVQAABQ1QYDFZuLyAAAAAElFTkSuQmCC")
    public void setTransformFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.setTransform(1, .2, .3, 1, 0, 0); context.fillRect(3, 3, 10, 7);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaUlEQVR42mNgGPGABYhdgFiAmobeBu"
            + "L/UHo1EFdQaslkqIHYMMySElIMdMBjIDJWIcXQx0QYmEOKgdOJMHA/KQYGEOltGWIN5AHizUD8nICB"
            + "GeTEugzUxc1YLFlPrbQKs6RitCwYBVQAABQ1QYDFZuLyAAAAAElFTkSuQmCC")
    public void transformFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.transform(1, .2, .3, 1, 0, 0); context.fillRect(3, 3, 10, 7);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGUlEQVR42m"
            + "NgGAWjYBSQBv6TiEfBKBg5AABEEAv1yW5vkQAAAABJRU5ErkJggg==")
    public void moveToLineToTransformStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 10); context.lineTo(13, 10);"
                + "context.transform(1, .2, .3, 1, 0, 0); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAVklEQVR42mNgGAWjYAQB"
            + "Dmob9h6IbwPxaiCuAeIAIFag1FADII4B4n4g3g3Er6EWHQbiyUCcAcQWQMxDiUUSQOwB"
            + "xCVAvBiILwPxZ0oNRQc8o0lupAEAZ+MMgTDcUPEAAAAASUVORK5CYII=")
    public void transformMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.transform(1, .2, .3, 1, 0, 0); context.moveTo(2, 10);"
                + "context.lineTo(13, 10); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGUlEQVR42mNgGAW"
            + "jYBSQBv5TAY+CUTA8AQC0ZBDwpDXmmwAAAABJRU5ErkJggg==")
    public void moveToLineToRotateStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 10); context.lineTo(18, 10); context.rotate(90); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAfUlEQVR42mNgYGDgYKAyeA7E84"
            + "FYgloGqgDxciD+DMT9QCxALYMNgHg7EL8H4hog5qGWwQ5AfBoaFDlAzEItg32A+DYUR1DLYJAh"
            + "CVDXngdiD2q5FpS0CqDhux+ITahlMCgFNENTxGZoCqEKAKXZyVAXUzVj8DCMglEwEgAA1S0T6X"
            + "hdMmMAAAAASUVORK5CYII=")
    public void rotateMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(.5); context.moveTo(1, 1); context.lineTo(18, 1); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR42mNgGErgP"
            + "4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
    public void rectFill() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rect(2, 2, 16, 6); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAoUlEQVR42mNgGAVAYAHE04F4PhCXALE"
            + "MpYa9B+ICIE4B4uVA/B2Iu4GYhxwDZ0MNQwYqQLwdiK8DsQY5BmbgkMuBut6FFANzoN7EBTyA+DkQCx"
            + "BrICgCPhPwmgCp3m4G4t1AzEKtZAOKzctYIocioAGNgABqGupCS0MzqO39+0C8HprIKYpxZI3TodlwO"
            + "dTFGaSmSVzptACao+aTmmuGKQAA9NQeZdHpsYYAAAAASUVORK5CYII=",
            IE = "context.ellipse not supported")
    public void ellipseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (!context.ellipse) { log('context.ellipse not supported'); return; }\n"
                 + "context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAlUlEQVR42mNgGAUg8P8/gwMQhwCxATUM2"
            + "wzE/5HwYyDOAGIOcl32Hwc+D8QKpBoYgsdAEP4MxC6kGGhAwECYoRykGHqbCENDSDEwhQgD15NiIAsQny"
            + "Zg4Hsg5iHFUBmoJnyG+pAa4y7QCMBl4Gxy0qULHpe+Jzexy+AJUxdysyMLNPuhJ6lmauR1A2iOAgUHy2h"
            + "RygAA45xHqtsvRxIAAAAASUVORK5CYII=",
            IE = "context.ellipse not supported")
    public void ellipseFill() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (!context.ellipse) { log('context.ellipse not supported'); return; }\n"
                + "context.fillStyle = 'yellow';"
                + "context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAc0lEQVR42mNgGAVDEvBQ07ASIH4"
            + "NxBzUMCwBiB8DsQa1XAcyzINahikA8XMgZqGWgSZQF+IDv0mN2c9ALIJDXgWIT5PqynYgng7EAl"
            + "A+KKZ1oOIg17uQaiALVPNrqAHfgfg2EC+GGkw24IBGEs9oHh+iAAAZGRFncAWu2AAAAABJRU5ErkJggg==")
    public void arcStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 4.3); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaklEQVR42mNgYGD4T2UMIagJRg0cNXDUQGIN5AH"
            + "iBiC+DOXfBuJ2qDhZBh4G4s1AbAI1RAeIVwPxeXIM/Q81DBtYDXUpyQaa4JDTgHqfZAMFcMhxkBNpNHEh1cPwNF"
            + "Isc0BdBjLsOqmxDABQejjNvtDkBwAAAABJRU5ErkJggg==")
    public void arcCircleStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(4, 16, 4, 0, 2 * Math.PI); context.stroke(); context.strokeRect(0, 0, 20, 20);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAATklEQVR42mNgGAVDEsgAcQIQNwBxDh"
            + "ALUGIYCxC/BuLdQNwOxN1AfB+IfSgxlAONbwDEj4FYgprBAPL+ZGoaqADE76kdWf9H0+soGDEAADmG"
            + "CbQK8bPUAAAAAElFTkSuQmCC")
    public void arcAnticlockwiseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 4.3, true); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAb0lEQVR42mNgGAWjgAeIG4D4"
            + "MhD/B+LbQNwOFSfLsMNAvBmITaB8HSBeDcTnyTG0AWoYNrAa6lKSwGWoy7ABDaj3SQKgMBPA"
            + "IccBlR9YF1I9DEGxeBopljmgLgMZdp2SpNMANQCWDrvJNWwUDBAAAFFYGYXPy8e+AAAAAElF"
            + "TkSuQmCC")
    public void arcCircleAnticlockwiseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 2 * Math.PI, true); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAU0lEQVR42mNgGAUjHTQwGADxdiB+DsTvgXg/Qz2D"
            + "CSWGfQbi/2j4N9BQC3IM3I3FMBg+TI6Bz/EY+H5QGEh1L1M5UhCGglz6GppsDpNv2CgYPgAA3MZQoURRzTMAAAAA"
            + "SUVORK5CYII=")
    public void arcFillPath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = 'green'; context.beginPath();"
                + "context.arc(10, 10, 4, 0, 2 * Math.PI); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAp0lEQVR42mNgGLqgnkGFoYGhHYgvA/F3IP4NZYP"
            + "EdIg3qIGBA4i7oQb8x4F/Qw3mIGQYDxCfxmMQOt4NxCz4DJxPgmEw3I0rzEwIeBMf1sDmuslkGgbC/dgMPE2Bgb"
            + "exGfidAgP/YzOQ3PDbDMQ22Ay8TIIhIN9Mxx4ZpCWZ50BcA8QixGQ1fMkG5PoUcMInCTQwrMaSE1zw5wbC+fg01"
            + "PsaDKNgQAAAd7buKpKXkaMAAAAASUVORK5CYII=")
    public void arcFillPathAngle() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = 'green'; context.beginPath();"
                + "context.arc(10, 10, 8, 2.3, 2 * Math.PI); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAYU"
            + "lEQVR42mNgGAXUBhJA/J8MLIHP0PlQTAwgSq0KEH+H0tRQR7TNpPiEoO0kuY4YF5DkOkKu"
            + "IMt1+FxClutwuYYi12FzEUWuQ3elBzVch+zK/9RwHbIr/1PLdTBgMzzKPwBs1inGPcAUbg"
            + "AAAABJRU5ErkJggg==")
    public void closePath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(4,4); context.lineTo(10,16);"
                + "context.lineTo(16,4); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
    public void closePathNoSubpath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.closePath();context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
    public void closePathPointOnly() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(4,4); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
    public void closePathTwice() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.closePath(); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA"
            + "XklEQVR42mNgoCHgIFGcoGHPgVgFTRzEfw3E8kD8Hw+WwGbofCjGJUZIHgOAXPMdyZWk"
            + "8hkIuZIcF+N0pQcO2wnJ43Tlfzy2E5LH6sr/eGwnJI8V2FAoPwpGwSjACwDUtCTLu8r4"
            + "+AAAAABJRU5ErkJggg==")
    public void closePathClosesOnlyLastSubpath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2,2); context.lineTo(5,8); context.lineTo(8,2);"
                + "context.moveTo(10,2); context.lineTo(13,8); context.lineTo(16,2); context.closePath();"
                + "context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA"
            + "N0lEQVR42mNg2Mf8HwWTCjD0U9vAfQzM/5Exyeah66e6gbjDglJAbQMp9TrtDRwFo2BI"
            + "g9EyAADAuXABKJOUSgAAAABJRU5ErkJggg==",
            IE = "no ctor")
    public void putImageDataInside() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (typeof ImageData != 'function') { log('no ctor'); return; }\n"
                + "      var arr = new Uint8ClampedArray(64);\n"
                + "      for (var i = 0; i < 32; i += 4) {\n"
                + "        arr[i + 0] = 0; arr[i + 1] = 190; arr[i + 2] = 3; arr[i + 3] = 255;\n"
                + "      }\n"
                + "      for (var i = 32; i < 64; i += 4) {\n"
                + "        arr[i + 0] = 190; arr[i + 1] = 0; arr[i + 2] = 3; arr[i + 3] = 255;\n"
                + "      }\n"

                + "      var imageData = new ImageData(arr, 4, 4);\n"
                + "      context.putImageData(imageData, 0, 0);\n"
                + "      context.putImageData(imageData, 2, 4);\n"
                + "      context.putImageData(imageData, 16, 0);\n"
                + "      context.putImageData(imageData, 16, 16);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAA"
            + "CNiR0NAAAANUlEQVR42mNg2Mf8H4T3MaBiBrIBtQ2k3EW0NhDd63A86AykXizTysBRMA"
            + "pGAV0A1UopNAMBDtE/AR/N0RYAAAAASUVORK5CYII=",
            IE = "no ctor")
    public void putImageDataOutside() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (typeof ImageData != 'function') { log('no ctor'); return; }\n"
                + "      var arr = new Uint8ClampedArray(64);\n"
                + "      for (var i = 0; i < 32; i += 4) {\n"
                + "        arr[i + 0] = 0; arr[i + 1] = 190; arr[i + 2] = 3; arr[i + 3] = 255;\n"
                + "      }\n"
                + "      for (var i = 32; i < 64; i += 4) {\n"
                + "        arr[i + 0] = 190; arr[i + 1] = 0; arr[i + 2] = 3; arr[i + 3] = 255;\n"
                + "      }\n"

                + "      var imageData = new ImageData(arr, 4, 4);\n"
                + "      context.putImageData(imageData, -2, 0);\n"
                + "      context.putImageData(imageData, 2, -2);\n"
                + "      context.putImageData(imageData, 2, 4);\n"
                + "      context.putImageData(imageData, 18, 18);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAA"
            + "CNiR0NAAAANklEQVR42mNgGNRgH/N/CM0AZVBqHpXMoZ2BdApXoLOp6nSqG4g1Keyjpg"
            + "VUN3AUjIJRwMAAAAneFQEGPcORAAAAAElFTkSuQmCC",
            IE = "no ctor")
    public void putImageDataDirty() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (typeof ImageData != 'function') { log('no ctor'); return; }\n"
                + "      var arr = new Uint8ClampedArray(64);\n"
                + "      for (var i = 0; i < 32; i += 4) {\n"
                + "        arr[i + 0] = 0; arr[i + 1] = 190; arr[i + 2] = 3; arr[i + 3] = 255;\n"
                + "      }\n"
                + "      for (var i = 32; i < 64; i += 4) {\n"
                + "        arr[i + 0] = 190; arr[i + 1] = 0; arr[i + 2] = 3; arr[i + 3] = 255;\n"
                + "      }\n"

                + "      var imageData = new ImageData(arr, 4, 4);\n"
                + "      context.putImageData(imageData, 0, 0, 1, 2, 1, 1);\n"
                + "      context.putImageData(imageData, 4, 4, 0, 2, 2, 2);\n"
                + "      context.putImageData(imageData, 8, 8, 0, 0, 2, 2);\n"
                + "      context.putImageData(imageData, 18, 0, 1, 1, 2, 3);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAvklEQVR42mNgGAW0AAJAXAHEh4H4PRD/B+LPQHwai"
            + "BuAWIQUwzKA+DkQrwfiACDmgYqDaA8gXg6VLyDGMJDtj4HYgoA6Hai6BkIuAylSINInIHX3cblUAOoNCxLDGuZSCX"
            + "SJCmiYwcB/EvBybF4HxaYPmQb+h8Y+CniPFJvkGPgZ3cD/WPikYtq6kJIw9MAWhlSPZVg6NCHRQJzpED2nEGvgfUJ"
            + "5GpaXiXHZfUJ5Gdmlr4F4NY4IWAy1tASXAQBr/Z3NB0Q1uQAAAABJRU5ErkJggg==",
            IE = "no ctor")
    public void clip() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (typeof ImageData != 'function') { log('no ctor'); return; }\n"
                + "context.moveTo(2,2); context.lineTo(5,8); context.lineTo(8,2);"
                + "context.arc(8, 12, 8, 0, 2 * Math.PI); context.stroke();"
                + "context.clip(); context.fillRect(4, 9, 19, 14);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAYklEQVR42mNgGLLgv/Bkjf8ik/qpZ"
            + "6DI5N9A/J+aBv4fNXC4GQhLJugGIYvhw9gMvE2BgY+xGDipnwIDp+PIaghvk2Dg7/+iUw2wh6Po5M"
            + "lkGDh9wJINlQsHUJgCg4Bh2AEAu6/n02vT9rUAAAAASUVORK5CYII=",
            IE = "no ctor")
    public void clipWindingEvenOdd() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (typeof ImageData != 'function') { log('no ctor'); return; }\n"
                + "context.rect(6, 2, 2, 16); context.rect(2, 10, 16, 5); context.clip('evenodd');"
                + "context.beginPath(); context.arc(10, 10, 8, 0, 2 * Math.PI);"
                + "context.fillStyle = 'deeppink';context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA7UlEQVQ4T+3Ur0pEQRTH8c/ULYIIg"
            + "iAbFsEmGIXFJvoOBv+wBsEgGmQtIth8htXiKxiFfQKDyWQwb9VguDK7szhcYb1cjPeUA8OZ7/zmd8"
            + "5MwBU2TWKYcu0U0s4IjTHNDbC6A5mHe8vcjbCFNewSHnJUMWneBbbT+mFgkNeUgV84RQs3uCTEQ8a"
            + "RgEfooY0TnAc+pjVl4NOPqiKCHwmvJWAnqipYiDBczwJWUfgnMA32zhzHn6x3WFpkf8D9e+5Pj/Yq"
            + "82c8r9C6ZeOA4YgoZBzxyhmw+0b/pXpPf1fO8LAetnl6zW9TY3L+fWy+AejsRJW3OyxAAAAAAElFT"
            + "kSuQmCC",
            IE = "no ctor")
    public void fillTextAndTransform() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (typeof ImageData != 'function') { log('no ctor'); return; }\n"
                + "context.moveTo(0, 0);\n"
                + "      context.lineTo(20, 0);\n"
                + "      context.moveTo(2, 0);\n"
                + "      context.lineTo(2, 20);\n"
                + "      context.moveTo(0, 10);\n"
                + "      context.lineTo(20, 10);\n"
                + "      context.stroke();\n"

                + "      context.fillStyle = 'blue';\n"
                + "      context.fillText('p', 2, 10);\n"

                + "      context.fillStyle = 'red';\n"
                + "      context.setTransform(1.0, 0.0, -0.0, 1.0, 11.0, 10.0);\n"
                + "      context.fillText('n', 0, 0);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXElEQVR42tXUSwrAIAxFUZd2d/6WVh"
            + "12IKLxQmkg03BCPq39KTLyKWZmBbkoiKnMqm1MXUWZneFg6k6UOVkhTN2OMpVFx9StlLk5R0zdTBnj"
            + "aWDq3sqYrw1T92109w9/gbiYNMcAAAAASUVORK5CYII=")
    public void pathFill() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2);\n"
                + "      context.lineTo(10, 18);\n"
                + "      context.lineTo(18, 2);\n"
                + "      context.closePath();\n"
                + "      context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAbUlEQVR42mNgGErgMhD/Jx"
            + "KvJsbAGiINew3EEsQYqEGkgRHU9PZqUsOxhoRwJMoiDTIMIximl0k0MIKa3l5NzdgmOvkg"
            + "e3sHtZJPDZILLlMj+WgguaCGEq8SE6YR1Cw4VlOrJKqhhldxhekQBwCxR4E7tKSxGgAAAA"
            + "BJRU5ErkJggg==")
    public void pathFillTransform() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "      context.moveTo(2, 2);\n"
                + "      context.lineTo(6, 14);\n"
                + "      context.lineTo(14, 2);\n"
                + "      context.closePath();\n"

                + "      context.setTransform(1.0, 0.0, 0.0, 1.0, 4.0, 4.0);\n"
                + "      context.moveTo(2, 2);\n"
                + "      context.lineTo(6, 14);\n"
                + "      context.lineTo(14, 2);\n"
                + "      context.closePath();\n"

                + "      context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAhElEQVR42t3SQQ3AIAwF0MqYBBzMCmLqqAbmAxmI6BoOO8AI"
            + "H8pla/KP/eEBRF+aZFEwghQyWJYtB1IYwMK4ky2z9/iwrVkVjwzZjJdlHdxpYQe8MO5ky9Rrs5PasE+iy0Ot2eUD22Japdbs"
            + "cgJbZg+1GVsOHmqvNK1Se4Xspr6wI/1ibuTa0+hbsogUAAAAAElFTkSuQmCC")
    public void pathFillTransform2() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "      context.beginPath();\n"
                + "      context.moveTo(2, 2);\n"
                + "      context.lineTo(6, 14);\n"
                + "      context.lineTo(14, 2);\n"
                + "      context.closePath();\n"
                + "      context.fill();\n"

                + "      context.setTransform(1.0, 0.0, 0.0, 1.0, 4.0, 4.0);\n"
                + "      context.fillStyle = 'red';"
                + "      context.beginPath();\n"
                + "      context.moveTo(2, 2);\n"
                + "      context.lineTo(6, 14);\n"
                + "      context.lineTo(14, 2);\n"
                + "      context.closePath();\n"
                + "      context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQklEQVR42mNgGAXUBw0M/1HwoDUQaBIKHkQGQgHC"
            + "IHSzB4uBSEYPegOxBwXVI4v6sU+tDEB1A3FmUYoNprqB9CqdACh5i4mujJYUAAAAAElFTkSuQmCC")
    public void saveRestore() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "      context.fillStyle = 'green';\n"
                + "      context.save();\n"
                + "      context.fillRect(4, 4, 4, 4);\n"

                + "      context.fillStyle = 'red';\n"
                + "      context.fillRect(6, 6, 4, 4);\n"
                + "      context.save();\n"

                + "      context.fillStyle = 'blue';\n"
                + "      context.fillRect(8, 8, 4, 4);\n"

                + "      context.restore();\n"
                + "      context.fillRect(12, 12, 4, 4);\n"

                + "      context.restore();\n"
                + "      context.fillRect(14, 14, 4, 4);\n"

                + "      context.restore();\n"
                + "      context.fillRect(16, 16, 4, 4);\n");
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAvUlEQVQ4T2NkQAWODAwM5lChkwwMDPvR5AlyGaEqeEUYGLYIM"
            + "DDoBzAwcIPENjAwfP3AwHDxDQODDwMDw2eCJkEVgA0UYWA4mMDAYNXNwMCCrLGUgeHPAgaGY28YGOxJMdBRhYFh/W0GBn5sml"
            + "QZGD7eYWAIJNb7IBdWlDAwNKO7DmY4yJU9DAy1DAwMHcS4kiYGUt3LVI8UUNBQN9kgBTbVEjYxEUiUGlhOIUoxMYpGDSQmlPC"
            + "rGQ1DysMQAFq2MhXKNeEnAAAAAElFTkSuQmCC",
        FF = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkklEQVQ4je3QTQqCUBiG0bOSsAUU0SLMRdiPpa0kEFqm7UAnO"
            + "apBBRYVXZv6wDs93O/y3Bz7+2b+acQxoVpRr6gXVBFlb6zgfOHSXUbbB50nVK/YYzEnTEPAIqf9BG6osQsCsy9gSoM8BJz9cP"
            + "IkBBRRbt+8ck075hCEddGYakmT0sScemOdpijc/izszKGhoYCuJ19i4sc/7LEAAAAASUVORK5CYII=",
        FF78 = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAjElEQVQ4je3SMQrCQBBA0dckZltLrxEkVxEUUudiqaw8l5VWw"
            + "ZXYRFSI4mrKPJj2M8Muryo0w1T+EAJtxmXDcccpIy7ZI6TXaEtiT/885SOapMpHYvfJiRLPb7ac3wVrutQNvwk2KcHJTxZo1y"
            + "OxkrjikBQbFIE2J9Z0Nd2C6/DCxS/Bu8k+9mw2++QGj8thmtPon2MAAAAASUVORK5CYII=",
        IE = {})
    @HtmlUnitNYI(IE = "data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAzUlEQVR42u2SvwpBYRyGn8Eiyqos/kxGVrkDo8VkJYNcgUtwD"
            + "crA7M+CLMoFGJQTirIapAgd78mmQ32nbOetZ/p+Pf2+7/3gX9lBvAGLHJxjcMvANgpVT7I9pPJwL4A9EZLbA5GFaxB6xsIKrI"
            + "oS2B/cRRKOGikbCdPwWLoIHbqw0UjfdElXmcMWLJ0fjGwq4WJ9EbZhqJGxkTABnRo8XYSn0FtWN71yWG3uSip8rW0d0RymERj"
            + "pbCYCXn5PWLR4v5mcqCeaXmV+/Pj5lRf9o2ZS6A1d/wAAAABJRU5ErkJggg==")
    public void imageOnLoad() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var img = new Image();\n"
            + "    img.onload = function() {\n"
            + "      var canvas = document.createElement('canvas');\n"
            + "      canvas.width = 20;\n"
            + "      canvas.height = 20;\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.drawImage(img, 0, 0);\n"

            + "      log(canvas.toDataURL());\n"
            + "    }\n"

            + "    img.src = 'data:image/svg+xml,"
                    + "<svg xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.w3.org/2000/svg\" "
                            + "overflow=\"hidden\" width=\"10\" height=\"10\">"
                    + "  <circle cx=\"5\" cy=\"5\" r=\"4\" stroke=\"black\" stroke-width=\"1\" fill=\"red\" />"
                    + "</svg>';"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }
}
