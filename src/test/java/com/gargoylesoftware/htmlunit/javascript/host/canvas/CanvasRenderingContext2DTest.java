/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

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

    public static void compareImages(final String[] expected, final List<String> current) throws IOException {
        assertEquals(expected.length, current.size());
        for (int i = 0; i < expected.length; i++) {
            compareImages(expected[i], current.get(i));
        }
    }

    public static void compareImages(final String expected, final String current) throws IOException {
        final String expectedBase64Image = expected.split(",")[1];
        final byte[] expectedImageBytes = Base64.getDecoder().decode(expectedBase64Image);

        final String currentBase64Image = current.split(",")[1];
        final byte[] currentImageBytes = Base64.getDecoder().decode(currentBase64Image);

        try (ByteArrayInputStream expectedBis = new ByteArrayInputStream(expectedImageBytes)) {
            final BufferedImage expectedImage = ImageIO.read(expectedBis);

            try (ByteArrayInputStream currentBis = new ByteArrayInputStream(currentImageBytes)) {
                final BufferedImage currentImage = ImageIO.read(currentBis);

                final ImageComparison imageComparison = new ImageComparison(expectedImage, currentImage);
                // imageComparison.setMinimalRectangleSize(10);
                imageComparison.setPixelToleranceLevel(0.2);
                imageComparison.setAllowingPercentOfDifferentPixels(5);

                final ImageComparisonResult imageComparisonResult = imageComparison.compareImages();
                final ImageComparisonState imageComparisonState = imageComparisonResult.getImageComparisonState();

                if (ImageComparisonState.SIZE_MISMATCH == imageComparisonState) {
                    fail("different size");
                }
                else if (ImageComparisonState.MISMATCH == imageComparisonState) {
//                    ImageComparisonUtil.saveImage(new File("c:\\rbri\\compare\\expected.png"), expectedImage);
//                    ImageComparisonUtil.saveImage(new File("c:\\rbri\\compare\\current.png"), currentImage);
//                    ImageComparisonUtil.saveImage(
//                            new File("c:\\rbri\\compare\\filename.png"), imageComparisonResult.getResult());
                    fail("different image");
                }
            }
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("done")
    public void test() throws Exception {
        final String html =
            "<html><head><script>\n"
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
            + "    alert('done');\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
            + "<script>\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var nbMethods = 0;\n"
            + "  var methods = ['" + String.join("', '", methods) + "'];\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    for (var i = 0; i < methods.length; i++) {\n"
            + "      if (typeof ctx[methods[i]] == 'function')\n"
            + "        nbMethods++;\n"
            + "      else\n"
            + "        alert(methods[i]);\n"
            + "    }\n"
            + "    alert(nbMethods + ' methods');\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAH0lEQVQ4T2NkoBAwUq"
                    + "ifYdQAhtEwYBgNA1A+Gvi8AAAmmAARf9qcXAAAAABJRU5ErkJggg==",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAC60lEQVQ4T32TX2xTVRzHP/d27"
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
                    + "y+bx/sYNfFOu6bg4MDMz/DxMpOiCCBtdCAAAAAElFTkSuQmCC"},
            FF = {"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAEklEQVQ4jWNgGAWjYBSMA"
                    + "ggAAAQQAAF/TXiOAAAAAElFTkSuQmCC",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACtUlEQVQ4jXXPz2vTdxzH8e/BMfw"
                    + "fKoLDgwcP23l0dGy3zcsOHj0LDtaLRTvaMlkPQztBBauwH3VOJSZt2oT8WKt+Eym2DekPutgkjbTNj+/vH/kmMc332/j0IG"
                    + "bEdB943T7vB6+XIAiCkM/nP44/TQ4lFlPjz1+kxp8vp8cXD0lkXjwnHPbCMbFf1U0qsoak6MiqjqIaqJqJqptohoWmmximx"
                    + "Vz0yc0ewB+KDsiqTrGiUJbUbkgzOkjVcTAti5m52C89gKIZlCSlc5wuyMQzMvGMwkpBQTdtdoslXm5lyW8X3kQXEp91Ae8n"
                    + "7FU0HqZK/LFRxZ9zeZxrEctZhDfKlFQLy3aw7Sq+2fA3XYCmm0iKTiC1y60llT83m8ztvWFF8VgvO4hZmakXuzi1Os3mfjc"
                    + "QDEUHNMMiV1S4OJdleKHMnbUa0wWPSr3NmvSaRE5hajHPZtGgud/qbaAbFslMke8DmwxFX3FFVJjasJGcFrVGk5VtiR8C68"
                    + "ysFtlvufhmY92AYVok/t3j18gqm9s7XBN3uBB6xfnZbeYzZaaXc3xxI8FMeo+W6/Y2MCybnbLCd7ef8XRplbO/L/H1nWW+m"
                    + "lziamSNC/eSfH7tH7aKGq77QYNgKDpgWjZlSeb6TJIz1yMcGw5yYjTMidEQn/wY5ORIkMsPEhiGiet5vRNMu4qmm7zcyvGz"
                    + "T+TTn4Icv+Tn2NBjTo1MM/xAJJsvUG808LyD7gn3/cEBy3awbAfDtCmWKqxnsvjFNL5naVYzeSRZpt5o4LouBwdt/vIF/gN"
                    + "u3P3tS7vqYDs1nFqdWr3xLrU69UaD180m+60WruvheQe0220mbt3+tgP09/efvP/3IzEcjac6icVT4dh8KhybT0U+yCNfIN"
                    + "nX13e6A4yNjR0ZHBw8+i4TRycm/j/v/01OTn4kCILwFgqgq1utHBHHAAAAAElFTkSuQmCC"},
            FF78 = {"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAEklEQVQ4jWNgGAWjYBSMA"
                    + "ggAAAQQAAF/TXiOAAAAAElFTkSuQmCC",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACvElEQVQ4jXXPu29TZxzG8TO0Qvw"
                    + "FmVAlEEOHDu2SpSoJajdgqaKO3SIhpRJZGpUgkKiaoRSB2qK2MEDSVjEEOznkOL5ARGwjRBIrN6UmdhKUxJdzv/jYxvE5Md"
                    + "8OCLeu6Ss92/v76HkEQRCEmZmZQ9HZ1NDck4WR5NOFkeSz9MiTt0SKPf5SeNsTw7OfaIZFSdGRVQNFM1A1E0230AwL3bTRD"
                    + "QvTshEjj37uAO6KUq+iGeRLKkVZa4d0s4WUXRfLtgmJke87AFU3Kchq63hpWyGeUYhnVBa3VQzLYTdf4PlGltzm1qvp6OxH"
                    + "bcCbCXslnUC6wJ21MsGcx/1cg1jOJrxWpKDZ2I6L45QJhB6cagN0w0JWDULpXW7Ma4yu15nee8Wi6rNadElkFcae7eJWqtT"
                    + "r++1AUJR6ddMml1f5ejrL8GyRmysVJrd9StUmK/JLkjmVsaebrOdN6vuNzgaGaZPK5PkqtM5Q9AXfJlTG1hxkt0GlVmdxS+"
                    + "ZcaJWp5Tz7DY9AKNwOmJZN8q89rkWWWd/a4WpihwHpBWcfbPEoU2RyIceJn5JMLe3R8LzOBqbtsFNU+fzXOR7PL/PF7Xk+u"
                    + "7nAp7/N80NkhYHfU3x89SEbeR3P+0+DoCj1WrZDUVa4PpXizPUIR4ZFjl4Kc/SSxLELIscvipwfT2KaFp7vd06wnDK6YfF8"
                    + "I8d3Ewk+vCzy3jdBjgzd5/2LkwyPJ8hublOt1fD9g/YJo3eDvbbjYjsupuWQL5RYzWQJJpaYmFtiObOJrChUazU8z+PgoMl"
                    + "oYOIf4Novt046ZRfHreBWqlSqtdepVKnWarys19lvNPA8H98/oNlscuXHG6dbQHd39/E//gwkpEgs3Uo0lpai8bQUjafD/0"
                    + "4snh6/F0x1dXV90AJ6enre6evrO/w6g4cHB/8/b/719/e/KwiC8Dc3QqPsSaO0rwAAAABJRU5ErkJggg=="},
            IE = {"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAA"
                    + "RnQU1BAACxjwv8YQUAAAATSURBVDhPYxgFo2AUjAIwYGAAAAQQAAGnRHxjAAAAAElFTkSuQmCC",
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BA"
                    + "ACxjwv8YQUAAAKeSURBVDhPjVJNTxNRFD3zTSm1UyKlCsEIRCC6EVFijC5MFE1kqTv1B+jChXsSE6Nx48qYsDPRxI0rjR8J"
                    + "GDFGhRBRdiAhliKlLVRmhmk7nS/vey0IO8/Me2/m3nfuue++K4Dwen5ek5dXb6maFhdFEfSCO3YiAIqmNTc8dPZJ3cLB971"
                    + "6N37mxLGjE57nQxAEGszBVv5RW8OQAgv4PDX9aPjCuZucTSAtYLNcloIggE+DrUEQIiBCEAbEC/lgUWVZwqnBgRsvXr59wN"
                    + "kEHkCmZ1uZrwKymx5m1zz8KHhIGy63bRgmcrk8jvR03X4zNta/HYCBbRAFER6ddSJTwawdgSU3w5R1FH0Vk8s2JC2KVGofW"
                    + "pNJYcN09jPevwBsUJDplRLWiSCIKtQGGQfiIlJRCU2ahPeLJiRJhKZpNRJhOwCxYVV9fM1WsLLpwvE8eK6PtqiIUKwdEUEV"
                    + "SxsO37uFXRnkrCrKrktEBzmjBMu2IQQeuvYIiIgBvq2WsLBWqgWrY0cN2E2F6Iy4uNotohFlTC0VcedDBpPpIpaLFmZ+G/w"
                    + "6d/B3H6FD1/ApbaCw/gdTiznMrazhZ3Yd39MFjM/l4Fcd9LZEeLZbkLdmZlTEEKdTEh5+WcVMoQpFZu4QjzM+787LfQm0NE"
                    + "pcjFqCo5YBFYwZVUXFUG8rBtsasZcK7VcrcCsVqHT+K306rg+0QWvQSIzkfM6sZVCpeNwok2IymcS1k024dNjCQsGmbgS6k"
                    + "zGkmmOIxWIkopCWAMeh2yDwDIrFPC+sQL2uKDJ0PY7Ojnac7z+Ei8d70HOwHYlEgmcoULOxvfl8lpeCZ/Ds+dNMQo9/bNYT"
                    + "UfbPwd18qs91kKRhGuX7d+/9Yr/cNzIyIpumqbDv/4Vt297o6Kj7F1Q7+m7gqVhgAAAAAElFTkSuQmCC"})
    public void drawImage() throws Exception {
        try (InputStream is = getClass().getResourceAsStream("html.png")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok", "image/png", emptyList);
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "    var canvas = document.createElement('canvas');\n"
            + "    canvas.width = img.width;\n"
            + "    canvas.height = img.height;\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      alert(canvas.toDataURL());\n"
            + "      context.drawImage(img, 0, 0, canvas.width, canvas.height);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' src='" + URL_SECOND + "'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final String[] expected = getExpectedAlerts();
        final List<String> current = getCollectedAlerts(DEFAULT_WAIT_TIME, driver, expected.length);
        compareImages(expected, current);
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
            + "<script>\n"
            + "try {\n"
            + "  var img = document.getElementById('myImage');\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var context = canvas.getContext('2d');\n"
            + "  alert('rendering...');\n"
            + "  context.drawImage(img, 0, 0, 10, 10);\n"
            + "  alert('...done');\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
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
            + "<script>\n"
            + "try {\n"
            + "  var img = document.getElementById('myImage');\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var context = canvas.getContext('2d');\n"
            + "  alert('rendering...');\n"
            + "  context.drawImage(img, 0, 0, 10, 10);\n"
            + "  alert('...done');\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    private void drawImage(final String fileName) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok", "image/png", emptyList);
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "    var canvas = document.createElement('canvas');\n"
            + "    canvas.width = img.width;\n"
            + "    canvas.height = img.height;\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.drawImage(img, 0, 0, canvas.width, canvas.height);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' src='" + URL_SECOND + "'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final String[] expected = getExpectedAlerts();
        final List<String> current = getCollectedAlerts(DEFAULT_WAIT_TIME, driver, expected.length);
        compareImages(expected, current);
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
        drawImage("1x1red_32_bit_depth.png");
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
        drawImage("1x1red_24_bit_depth.png");
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
            + "      function test() {\n"
            + "        var canvas = document.getElementById('myCanvas');\n"
            + "        if (canvas.getContext){\n"
            + "          ctx = canvas.getContext('2d');\n"
            + "          try {\n"
            + "            alert(ctx.measureText());\n"
            + "          } catch(e) { alert('exception'); }\n"

            + "          var metrics = ctx.measureText('');\n"
            + "          alert(metrics.width);\n"

            + "          metrics = ctx.measureText('a');\n"
            + "          alert(metrics.width > 5);\n"

            + "          metrics = ctx.measureText('abc');\n"
            + "          alert(metrics.width > 10);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var canvas = document.getElementById('myCanvas');\n"
            + "        if (canvas.getContext){\n"
            + "          var context = canvas.getContext('2d');\n"
            + "          context.fillText('HtmlUnit', 3, 7);\n"
            + "          alert(canvas.toDataURL());\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'><canvas id='myCanvas' width='42' height='42'></canvas></body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);

        final String[] expected = getExpectedAlerts();
        final List<String> current = getCollectedAlerts(DEFAULT_WAIT_TIME, driver, expected.length);
        compareImages(expected, current);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object CanvasGradient]")
    public void createLinearGradient() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  var gradient = ctx.createLinearGradient(0, 0, 200, 0);\n"
            + "  alert(gradient);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object CanvasGradient]")
    public void createRadialGradient() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  var gradient = ctx.createRadialGradient(100, 100, 100, 100, 100, 0);\n"
            + "  alert(gradient);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
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
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    alert(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 0.5;\n"
            + "    alert(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 0;\n"
            + "    alert(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 0.7;\n"
            + "    alert(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = null;\n"
            + "    alert(ctx.globalAlpha);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0.5", "0.5", "0.5", "0.5"})
    public void globalAlphaInvalid() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.globalAlpha = 0.5;\n"
            + "    alert(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = -1;\n"
            + "    alert(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = 'test';\n"
            + "    alert(ctx.globalAlpha);\n"
            + "    ctx.globalAlpha = undefined;\n"
            + "    alert(ctx.globalAlpha);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
