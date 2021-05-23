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
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;
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
     * Closes the real IE; the drawImage test are producing strange
     * results without this.
     */
    @After
    public void shutDownRealBrowsersAfter() {
        shutDownRealIE();
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
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok", MimeType.IMAGE_PNG, emptyList);
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
            FF78 = "data:image/png;base64,"
                    + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACtUlEQVQ4jXXPz2vTdxzH8e/BMfwfKoLDgwcP23l0dGy3zcsO"
                    + "Hj0LDtaLRTvaMlkPQztBBauwH3VOJSZt2oT8WKt+Eym2DekPutgkjbTNj+/vH/kmMc332/j0IGbEdB943T7vB6+XIAiCkM/n"
                    + "P44/TQ4lFlPjz1+kxp8vp8cXD0lkXjwnHPbCMbFf1U0qsoak6MiqjqIaqJqJqptohoWmmximxVz0yc0ewB+KDsiqTrGiUJbU"
                    + "bkgzOkjVcTAti5m52C89gKIZlCSlc5wuyMQzMvGMwkpBQTdtdoslXm5lyW8X3kQXEp91Ae8n7FU0HqZK/LFRxZ9zeZxrEctZ"
                    + "hDfKlFQLy3aw7Sq+2fA3XYCmm0iKTiC1y60llT83m8ztvWFF8VgvO4hZmakXuzi1Os3mfjcQDEUHNMMiV1S4OJdleKHMnbUa"
                    + "0wWPSr3NmvSaRE5hajHPZtGgud/qbaAbFslMke8DmwxFX3FFVJjasJGcFrVGk5VtiR8C68ysFtlvufhmY92AYVok/t3j18gq"
                    + "m9s7XBN3uBB6xfnZbeYzZaaXc3xxI8FMeo+W6/Y2MCybnbLCd7ef8XRplbO/L/H1nWW+mlziamSNC/eSfH7tH7aKGq77QYNg"
                    + "KDpgWjZlSeb6TJIz1yMcGw5yYjTMidEQn/wY5ORIkMsPEhiGiet5vRNMu4qmm7zcyvGzT+TTn4Icv+Tn2NBjTo1MM/xAJJsv"
                    + "UG808LyD7gn3/cEBy3awbAfDtCmWKqxnsvjFNL5naVYzeSRZpt5o4LouBwdt/vIF/gNu3P3tS7vqYDs1nFqdWr3xLrU69UaD"
                    + "180m+60WruvheQe0220mbt3+tgP09/efvP/3IzEcjac6icVT4dh8KhybT0U+yCNfINnX13e6A4yNjR0ZHBw8+i4TRycm/j/v"
                    + "/01OTn4kCILwFgqgq1utHBHHAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                    + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAKeSURBVDhP"
                    + "jVJNTxNRFD3zTSm1UyKlCsEIRCC6EVFijC5MFE1kqTv1B+jChXsSE6Nx48qYsDPRxI0rjR8JGDFGhRBRdiAhliKlLVRmhmk7"
                    + "nS/vey0IO8/Me2/m3nfuue++K4Dwen5ek5dXb6maFhdFEfSCO3YiAIqmNTc8dPZJ3cLB9716N37mxLGjE57nQxAEGszBVv5R"
                    + "W8OQAgv4PDX9aPjCuZucTSAtYLNcloIggE+DrUEQIiBCEAbEC/lgUWVZwqnBgRsvXr59wNkEHkCmZ1uZrwKymx5m1zz8KHhI"
                    + "Gy63bRgmcrk8jvR03X4zNta/HYCBbRAFER6ddSJTwawdgSU3w5R1FH0Vk8s2JC2KVGofWpNJYcN09jPevwBsUJDplRLWiSCI"
                    + "KtQGGQfiIlJRCU2ahPeLJiRJhKZpNRJhOwCxYVV9fM1WsLLpwvE8eK6PtqiIUKwdEUEVSxsO37uFXRnkrCrKrktEBzmjBMu2"
                    + "IQQeuvYIiIgBvq2WsLBWqgWrY0cN2E2F6Iy4uNotohFlTC0VcedDBpPpIpaLFmZ+G/w6d/B3H6FD1/ApbaCw/gdTiznMrazh"
                    + "Z3Yd39MFjM/l4Fcd9LZEeLZbkLdmZlTEEKdTEh5+WcVMoQpFZu4QjzM+787LfQm0NEpcjFqCo5YBFYwZVUXFUG8rBtsasZcK"
                    + "7VcrcCsVqHT+K306rg+0QWvQSIzkfM6sZVCpeNwok2IymcS1k024dNjCQsGmbgS6kzGkmmOIxWIkopCWAMeh2yDwDIrFPC+s"
                    + "QL2uKDJ0PY7Ojnac7z+Ei8d70HOwHYlEgmcoULOxvfl8lpeCZ/Ds+dNMQo9/bNYTUfbPwd18qs91kKRhGuX7d+/9Yr/cNzIy"
                    + "IpumqbDv/4Vt297o6Kj7F1Q7+m7gqVhgAAAAAElFTkSuQmCC")
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
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok", MimeType.IMAGE_PNG, emptyList);
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAlSURBVBhXY2B"
                + "Q+///PwPDf0YQwXCDAQj+KyJEGhsaQEIgwMAAAI7uDpdlPpgpAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAlSURBVBhXY2B"
                + "Q+///PwPDf0YQwXCDAQj+KyJEGhsaQEIgwMAAAI7uDpdlPpgpAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAjElEQVQ4je3SMQrCQBBA0dckZltLrxEkVxEUUudiqaw8l5VW"
                + "wZXYRFSI4mrKPJj2M8Muryo0w1T+EAJtxmXDcccpIy7ZI6TXaEtiT/885SOapMpHYvfJiRLPb7ac3wVrutQNvwk2KcHJTxZo"
                + "1yOxkrjikBQbFIE2J9Z0Nd2C6/DCxS/Bu8k+9mw2++QGj8thmtPon2MAAAAASUVORK5CYII=",
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
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAqElEQVRYhe2SwQ3DIAxF/yzMwSCeJKN0lg6TXeihRHKNacIh"
                + "6qHvSUgI8OclWPqkSnqGta0PSXroOubOn+We1Q98C8z27hSNXsPCLHCX1Prlh0Tro7q5LYpa3zvqt6R+z0RbMuIfNVfs59WF"
                + "r4pKUgm5vn4QvfL0MSQT8mfKJNc09uJPRaX3y5QgVO8QLRp7dEU0tlXWizNR/4EAAAAAAAAAAAAAAP/AC/GNUlQypcRWAAAA"
                + "AElFTkSuQmCC",
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAMklEQVQ4T2NkoDJghJrXQCVzG2huILkuhenDcOHwNvA/AwND"
                + "I80jhZLUg+JCSgxC0QsANbUOC0ZQs6cAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAMklEQVQ4T2NkoDJghJrXQCVzG2huILkuhenDcOHwNvA/AwND"
                + "I80jhZLUg+JCSgxC0QsANbUOC0ZQs6cAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAOElEQVQokWNgoBFwYGBgaKAQOyAb2MDAwLCfAsNgelEMRBEg"
                + "EWDoH6EGUjVSHCgwDIYdiPQNaQAAWucr/QQSzQIAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAOElEQVQokWNgoBFwYGBgaKAQOyAb2MDAwLCfAsNgelEMRBEg"
                + "EWDoH6EGUjVSHCgwDIYdiPQNaQAAWucr/QQSzQIAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABTSURBVDhP"
                + "Y6A2YITSDlBMCTgAwkwQNtgwewiTLADSi+KgBigmF8D1w1xINTD4DYTFMsj/oIA9COaRDmB6G5jBXAiAGU4OeAjEoGTzAMyj"
                + "HmBgAADuaAqOh9GTVgAAAABJRU5ErkJggg==")
    public void strokeRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.strokeRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAKElEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TPV0CAA8pwYLxeyTOgAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAKElEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TPV0CAA8pwYLxeyTOgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAFklEQVQokWNgGErgP4V41MChYODgBADEpF+hDnSEnAAAAABJ"
                + "RU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAFklEQVQokWNgGErgP4V41MChYODgBADEpF+hDnSEnAAAAABJ"
                + "RU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAhSURBVDhP"
                + "Yxj0gBFKg8B/KE0uAJvFBGZSEYwaOPwBAwMAkWIBDOc7sxkAAAAASUVORK5CYII=")
    public void fillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALElEQVQ4T2NkgID/UJpSipFx1MDRMCQ5BEaTDclBhqFhNAxH"
                + "w5CMEKB+sgEAGQQUFQfPo6sAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALElEQVQ4T2NkgID/UJpSipFx1MDRMCQ5BEaTDclBhqFhNAxH"
                + "w5CMEKB+sgEAGQQUFQfPo6sAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAHklEQVQ4jWNggID/VMJwMGrgqIGjBo4aOGrgiDMQAMu0Zqj6"
                + "rPAwAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAHklEQVQ4jWNggID/VMJwMGrgqIGjBo4aOGrgiDMQAMu0Zqj6"
                + "rPAwAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAArSURBVDhP"
                + "Y2SAgP9QmlLAyARlUA2MGkg5GDWQcjBqIOVg1EDKwWA3kIEBAND2AShZ31fQAAAAAElFTkSuQmCC")
    public void fillRectWidthHeight() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(1, 0, 18, 20);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGErgP4V41MChYOAoGAWjYOgCAGnPX6Ee+YVU"
                + "AAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGErgP4V41MChYOAoGAWjYOgCAGnPX6Ee+YVU"
                + "AAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAnSURBVDhP"
                + "Yxj0gBFKg8B/KE0uAJvFBGZSEYwaOApGwSgYBSDAwAAA4YcBDBgCJpwAAAAASUVORK5CYII=")
    public void fillRectRotate() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.rotate(.5);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA10lEQVQ4T93UrU4DQRAA4K84XoCXqACDwjShGDAIRIOpbtIE"
                + "D5rW4XAVJFVVgKgEharEVVVW4XgAzFxy2XB/zRnYZJNNZvbbmd1kO1oenZY9fxO8wHLXq0hbPsYKr5jEupGdgs+4zAmzgDd1"
                + "1Tx4XtLqNODvKjgPfuCkZMNXoA9laAYO8VR1esTXuMf8t/wM3MNtzP2a8DtO09z0UQ4CvamB9vFWBWbxbsDXBfACg7KWiwrq"
                + "BXyWJBzicxcw23OFOxzhEeOiCpp+DiO8YNsWWPlWTSv8B+APX48cFbtSb50AAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA10lEQVQ4T93UrU4DQRAA4K84XoCXqACDwjShGDAIRIOpbtIE"
                + "D5rW4XAVJFVVgKgEharEVVVW4XgAzFxy2XB/zRnYZJNNZvbbmd1kO1oenZY9fxO8wHLXq0hbPsYKr5jEupGdgs+4zAmzgDd1"
                + "1Tx4XtLqNODvKjgPfuCkZMNXoA9laAYO8VR1esTXuMf8t/wM3MNtzP2a8DtO09z0UQ4CvamB9vFWBWbxbsDXBfACg7KWiwrq"
                + "BXyWJBzicxcw23OFOxzhEeOiCpp+DiO8YNsWWPlWTSv8B+APX48cFbtSb50AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkElEQVQ4je3MrRIBYQCF4ecCqLqgCJosaJKgqi5A1WVBEyQz"
                + "GlmWZRchq6vsznyDWd/+aPvOnJmTHpr+Ua9ObIgE5/RX7pSC2XbolsUmb1i4NVpFwWsOmOCBZSw2/YGFu2Meg67wLADvY9AO"
                + "NpHgOAbM6uOQgx2LYGEjXL6Ag7Jg1gy3FNtWxcIWaNcJNn32Ann7R7XCBLW+AAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkElEQVQ4je3MrRIBYQCF4ecCqLqgCJosaJKgqi5A1WVBEyQz"
                + "GlmWZRchq6vsznyDWd/+aPvOnJmTHpr+Ua9ObIgE5/RX7pSC2XbolsUmb1i4NVpFwWsOmOCBZSw2/YGFu2Meg67wLADvY9AO"
                + "NpHgOAbM6uOQgx2LYGEjXL6Ag7Jg1gy3FNtWxcIWaNcJNn32Ann7R7XCBLW+AAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACWSURBVDhP"
                + "7cytDoJQGIfxcwFY6QSLwUY20EwGK5ULoNrNBJqBxEaTbDabuQgyFZ4zDxtj78Z349l+O+9O+Ku9TTqad5Vc1MjNvbg39GDr"
                + "BQezuqI71vWEhUl9IY21SoQY1Q3SiKSAj8EeqCCNSBIMZiOCNNDnYXQnpJCGtAyzuuCD/uAZi7rjBz0W64+1CnD4n3vbpFQD"
                + "eftHtWY6PUwAAAAASUVORK5CYII=")
    public void rotateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(.5); context.fillRect(6, 2, 12, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGErgP4V41MChYOAoGAWjYOgCAGnPX6Ee+YVU"
                + "AAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGErgP4V41MChYOAoGAWjYOgCAGnPX6Ee+YVU"
                + "AAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAnSURBVDhP"
                + "Yxj0gBFKg8B/KE0uAJvFBGZSEYwaOApGwSgYBSDAwAAA4YcBDBgCJpwAAAAASUVORK5CYII=")
    public void fillRectTranslate() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.translate(3, 4);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAMUlEQVQ4T2NkoDJgpLJ5DKMGUh6iuMLwP7lGjxoID7nBH4bk"
                + "RvJo1iM75BAaR2DxBQB5vAYPhQt4egAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAMUlEQVQ4T2NkoDJgpLJ5DKMGUh6iuMLwP7lGjxoID7nBH4bk"
                + "RvJo1iM75BAaR2DxBQB5vAYPhQt4egAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGAUjF/ynAI8aOFQMHAWjgJ4AABYtWaeYl32X"
                + "AAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGAUjF/ynAI8aOFQMHAWjgJ4AABYtWaeYl32X"
                + "AAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAkSURBVDhP"
                + "YxgFIwAwQml08B9KkwyYoDTVwKiBo2AUDAfAwAAAf3YBCwCfFioAAAAASUVORK5CYII=")
    public void translateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.translate(3, 4); context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAzElEQVQ4T+3UsQkCMRTG8f+BgpWNK1hZiU5g4wYKdg5h4Qpi"
                + "7wg2jiA4gKCdpYJgIQiCG8iDPHiE4OVMGsFAuOPy8nv5UlxB5lFk9viD6Tf6W3c4Ag7AJSW4jXwCOsDDwUf3lCbX2CYKNoHX"
                + "h013g2ujW6hewQGwiz2FqxPQpthLOgVnwLIiaMvPQFs+KLgGJgngBhhbcAX0gD5Q+wKeAwsLqiEnFlRxeZdZNobANgSGNta9"
                + "BtKs6xW2gGcsGGrS8JJMbcSyOJXWs/8c3nSHHxVqGh2JAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAzElEQVQ4T+3UsQkCMRTG8f+BgpWNK1hZiU5g4wYKdg5h4Qpi"
                + "7wg2jiA4gKCdpYJgIQiCG8iDPHiE4OVMGsFAuOPy8nv5UlxB5lFk9viD6Tf6W3c4Ag7AJSW4jXwCOsDDwUf3lCbX2CYKNoHX"
                + "h013g2ujW6hewQGwiz2FqxPQpthLOgVnwLIiaMvPQFs+KLgGJgngBhhbcAX0gD5Q+wKeAwsLqiEnFlRxeZdZNobANgSGNta9"
                + "BtKs6xW2gGcsGGrS8JJMbcSyOJXWs/8c3nSHHxVqGh2JAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAoklEQVQ4je3QsQpBARiG4ceglDKYlTKalcFkMBmUizAom7IZ"
                + "yOgCDFaDTRlcgkGZbDaDcgsGFsNJ54RDMnjrW5/+fv79+62KKHwS3OGCE1YYoYl8HCxzw6J2xBIDNJB7BFYfgGE7YIE+6kgH"
                + "wW4MMLj9/YWzN8H5PTjEGueYYC/qlwmU0MIEmyfBWhQYVhJltDHFNgTMvgKGlUIFHYzfxb7bFf97X7D//5t+AAAAAElFTkSu"
                + "QmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAoklEQVQ4je3QsQpBARiG4ceglDKYlTKalcFkMBmUizAom7IZ"
                + "yOgCDFaDTRlcgkGZbDaDcgsGFsNJ54RDMnjrW5/+fv79+62KKHwS3OGCE1YYoYl8HCxzw6J2xBIDNJB7BFYfgGE7YIE+6kgH"
                + "wW4MMLj9/YWzN8H5PTjEGueYYC/qlwmU0MIEmyfBWhQYVhJltDHFNgTMvgKGlUIFHYzfxb7bFf97X7D//5t+AAAAAElFTkSu"
                + "QmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAC3SURBVDhP"
                + "7dIxCsIwFIDhqAgudvECzk6iXsChV/ECehkvI66CurnpIHQQBMXBTdD/vSYQNEKsi0J/+EiyvLSlpqzsB6rZVeqgibOeClax"
                + "q7SBDD1ijZVnj6jcwASXfBvsABnsX5ThJTdwiFm+jU4G+hfMcYU2wf0LW2hVu/bsWjR5Us0N3GGBm54+T145mHzTAUaYYonQ"
                + "Kz5Lofm/zbvqkE/St2TfhV8LJ9nEDAzVgBvexhh/kTEPbs42HzJH+t4AAAAASUVORK5CYII=")
    public void rotateTranslateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(0.2); context.translate(0, 4); context.fillRect(4, 4, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAtklEQVQ4T+2SMQoCMRBF3x7BegsrbezWRnuP4hFsbey38zq2"
                + "VirYLFgKXsJOBnaWIQwyblawMJAiJHlv5icFA49iYB5/YH6iX8twDNzz66N75TWwB87AxczrpxJtuQQezuVnC7eitxKb4RFY"
                + "BipSiXSiok5igVtgFwB6R2pgIxsWOAdOPYENMEuBsr4B055QATbePxwBVTIjEmm5jn5slUgsKpsknRyAVRTopeBJFjlAN+rf"
                + "B74A50UdJ5SgdwwAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAtklEQVQ4T+2SMQoCMRBF3x7BegsrbezWRnuP4hFsbey38zq2"
                + "VirYLFgKXsJOBnaWIQwyblawMJAiJHlv5icFA49iYB5/YH6iX8twDNzz66N75TWwB87AxczrpxJtuQQezuVnC7eitxKb4RFY"
                + "BipSiXSiok5igVtgFwB6R2pgIxsWOAdOPYENMEuBsr4B055QATbePxwBVTIjEmm5jn5slUgsKpsknRyAVRTopeBJFjlAN+rf"
                + "B74A50UdJ5SgdwwAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZUlEQVQ4jWNgGAWjgGwgQG0DExgYGP4zMDCsZ2Bg6IfyDSgx"
                + "UABqIDZMtiX38RhKliUNJBiIjueT6m1C+D0uV+6nwFAFXIYyMDAwBECDYD0D8WFbgM9AcizZT6qBxFhC9QwyBAAApwtZ2OX4"
                + "JhcAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZUlEQVQ4jWNgGAWjgGwgQG0DExgYGP4zMDCsZ2Bg6IfyDSgx"
                + "UABqIDZMtiX38RhKliUNJBiIjueT6m1C+D0uV+6nwFAFXIYyMDAwBECDYD0D8WFbgM9AcizZT6qBxFhC9QwyBAAApwtZ2OX4"
                + "JhcAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACgSURBVDhP"
                + "YxgFo4B8IAClKQbMUDoCiM8DsQEQWwCxJBCDwAsoTTRghNIgF76HMDHABiB+AMQXgfgCFOMEMANB4D4QK0CYBAFRljQA8X8y"
                + "8XwgxgAgb2NTTAzGFVwM+4EYmwZiMDi4YLEMAwuBuBGIQWFzE4g/ADEHEBOTrB4C8QnkSCEEAoAYlKz0oTR6BB4AYkdSDMQG"
                + "0C0xBOKRBRgYAItyQgsDc+IZAAAAAElFTkSuQmCC")
    public void transformTranslateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.setTransform(1, .2, .3, 1, 0, 0); context.translate(-5, 4); context.fillRect(4, 4, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZdDEQVzAQ8gFOF44g"
                + "AymMZIh2qqdDAD9SCgvGd82WAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZdDEQVzAQ8gFOF44g"
                + "AymMZIh2qqdDAD9SCgvGd82WAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAGUlEQVQokWNgGErgP4WYfgaS64NRAwcrAACGL0e5XiFXSgAA"
                + "AABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAGUlEQVQokWNgGErgP4WYfgaS64NRAwcrAACGL0e5XiFXSgAA"
                + "AABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAtSURBVDhP"
                + "Yxj0gBFKg8B/KE0uAJvFBGZSEWBzIbIYMiDkA9q4cPAbONgBAwMAI5ADC8QrQ2IAAAAASUVORK5CYII=")
    public void clearRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.clearRect(4, 4, 6, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXUlEQVQ4T2P878Lwn4GKgHHUQIpDcySHIeMeBkZYAFKSlOBh"
                + "iGwgtpgh1hKiDSTWEpRYJuRKfGkK5oMhZiC6l0gJAqxeJibf4bMEZChVsh7MEqoZiOwzqriQpgYCAH2SSD3nlbP6AAAAAElF"
                + "TkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXUlEQVQ4T2P878Lwn4GKgHHUQIpDcySHIeMeBkZYAFKSlOBh"
                + "iGwgtpgh1hKiDSTWEpRYJuRKfGkK5oMhZiC6l0gJAqxeJibf4bMEZChVsh7MEqoZiOwzqriQpgYCAH2SSD3nlbP6AAAAAElF"
                + "TkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAi0lEQVQ4je2TwQ2DMAxF3wiMwCiRbKQeGYER2IAROgIjdBRG"
                + "YARG+BwBQSgpuVStJV/fi51v5Chn8wf+EHDKCzR6ABm1nFZGL2e49UIilSpZdvigjEFTJOtP6a4Cd4KK5ggYHfstMFDEYhM+"
                + "hhrjWQ5fcjoZ9dXdynmmBvtUIifkuJSNRM70PbecrWcimdKSONB+gwAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAi0lEQVQ4je2TwQ2DMAxF3wiMwCiRbKQeGYER2IAROgIjdBRG"
                + "YARG+BwBQSgpuVStJV/fi51v5Chn8wf+EHDKCzR6ABm1nFZGL2e49UIilSpZdvigjEFTJOtP6a4Cd4KK5ggYHfstMFDEYhM+"
                + "hhrjWQ5fcjoZ9dXdynmmBvtUIifkuJSNRM70PbecrWcimdKSONB+gwAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAADcSURBVDhP"
                + "7ZTRDYJADIb/kviuG+gEMoIRfdcN3EDdwA1cwREYgMQ4gbiBI/CskVrOItEQ4XLHm19y4b9e0vZKr8QzMDwS6Ncbf4fudONQ"
                + "+iYzOw+UGcaUgJBjKc63sg7MSPXMCtPYRYZBgoHaPuApFhxgKHIsVwmJEL5O6qleyh0jOuFqdAM/g5gMixVhpyZreI5V6ef9"
                + "l5mwVmnPDbGqqm0I6EuWE91aIaXKpG6mXLXTRgwxMS4iUjyQtqltHmEvtdy0Hl9NQcztCEenefgdhHs4Ozmso5u37A/gCR//"
                + "XBF+op2TAAAAAElFTkSuQmCC")
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAdUlEQVQ4T2NkoDJgRDIvkYGBYRcDA8NTSuyAGajMwMDQwsDA"
                + "4MbAwHAXajDI8EOkGo7sQpheO6jBIMNBFoEMhmGCrsdmILKjpJEMd2dgYLhDyPWEDET3Mcz1IMOVoIbXQIMJrJZUA7G5fj6y"
                + "ICUGYo2vwW8gAH5+EgsNfvz9AAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAdUlEQVQ4T2NkoDJgRDIvkYGBYRcDA8NTSuyAGajMwMDQwsDA"
                + "4MbAwHAXajDI8EOkGo7sQpheO6jBIMNBFoEMhmGCrsdmILKjpJEMd2dgYLhDyPWEDET3Mcz1IMOVoIbXQIMJrJZUA7G5fj6y"
                + "ICUGYo2vwW8gAH5+EgsNfvz9AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAUklEQVQokdXQQRGAMBAEwfYRjMCDaMFLvAQtQUs88MhJOB6s"
                + "gN6q4cMdKJngjYkHDWcWXAMccdBxYcvAt8B64CPOagbOytCsLNPKlLaCPRP86V4K4A4qOXwp8AAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAUklEQVQokdXQQRGAMBAEwfYRjMCDaMFLvAQtQUs88MhJOB6s"
                + "gN6q4cMdKJngjYkHDWcWXAMccdBxYcvAt8B64CPOagbOytCsLNPKlLaCPRP86V4K4A4qOXwp8AAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABZSURBVDhP"
                + "Y6A2YIbSIOALxF+B+DOYRyEQAeLlQPwGiE8BcQsQ2wExVQDIIJCBIIPfAjHIokQglgZiigHIEJBhIENBhtPU9aDgAVkECi6q"
                + "AJDrQRE5sgEDAwCWpRA/yw9O8gAAAABJRU5ErkJggg==")
    public void moveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.lineTo(16, 6); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA40lEQVQ4T63TPUpDQRAA4C+l6TxAPEQIeAExHiCNEETQPtZi"
                + "If4kYGdlIzaCjZ2NSjyJlRbWNoqFoLKyC6+Q8HyZbQZ2mW+HYaYl+LSCPQXcz3CJjf8p4AW+sdVYyolV8AvbUeABPnEYBR7h"
                + "A8dRYILeMY4CL7GAQRTYxQ3W8YqXHP/tVwd7hFUsooM33OE+x1r4rE1JVa+hj2Vc4wq3s+S6q9fGJnbwiFNM/4LrgtXctE1p"
                + "zFLPJ3iqPjYBS/4JNrCH83I5D5iMHs5yG3bxPC9YCksLMcRSFJjgFTxEgr/VhoM/OAYkFQrlOVoAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA40lEQVQ4T63TPUpDQRAA4C+l6TxAPEQIeAExHiCNEETQPtZi"
                + "If4kYGdlIzaCjZ2NSjyJlRbWNoqFoLKyC6+Q8HyZbQZ2mW+HYaYl+LSCPQXcz3CJjf8p4AW+sdVYyolV8AvbUeABPnEYBR7h"
                + "A8dRYILeMY4CL7GAQRTYxQ3W8YqXHP/tVwd7hFUsooM33OE+x1r4rE1JVa+hj2Vc4wq3s+S6q9fGJnbwiFNM/4LrgtXctE1p"
                + "zFLPJ3iqPjYBS/4JNrCH83I5D5iMHs5yG3bxPC9YCksLMcRSFJjgFTxEgr/VhoM/OAYkFQrlOVoAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkklEQVQ4jdXTqw0CURCG0ZOVrKADHNlsCxSARGLQiC2BkPAI"
                + "aASGBEMLWJrAkWCgAgxBEBziUsII7lfAEX9myKUF5pHgMgtw9tfgFOtIsMEuEqzwxAg1WhHoGEdc8cYZGwwicOhhghMeP7yK"
                + "wrtY4YW9NEtIbelNP9JVFFFwBwfcMYxCoY+LNEMZhRbY4hYFZtYXWq4ZzM6yLlgAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkklEQVQ4jdXTqw0CURCG0ZOVrKADHNlsCxSARGLQiC2BkPAI"
                + "aASGBEMLWJrAkWCgAgxBEBziUsII7lfAEX9myKUF5pHgMgtw9tfgFOtIsMEuEqzwxAg1WhHoGEdc8cYZGwwicOhhghMeP7yK"
                + "wrtY4YW9NEtIbelNP9JVFFFwBwfcMYxCoY+LNEMZhRbY4hYFZtYXWq4ZzM6yLlgAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAC1SURBVDhP"
                + "5dOhCgJBEMbx1QOLwWC/JuIrWGxGo2/gQ1wTNBuMFl/BavQFbMIVLVaxCHecCvqfu12wzwiCH/yY3Q3D3jLnfj6Rr1MMsCt3"
                + "itR9rfmqTmhols+GL19V+doN72hUS11Cwyva1VKXMDY3LHHGExkeUGWCDVLk2GOBEUzSR4ItLpDmXZikgxnkaVbowSQtyG9a"
                + "YA6zsYuxxgljObDKEAfIMzTlwCLy2TJyx3L3b3HuDVsZGxJRyvPSAAAAAElFTkSuQmCC")
    public void moveToBezierCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.bezierCurveTo(2, 17, 1, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA4klEQVQ4T83Ur0uEQRAG4Of+BLEZBBGLQTCbRNAih6Bgsgki"
                + "NptJziYGwejFa4dBMIhRLGLQqsVwwWwyHjLiB8vByf2Y4MKUhfeZCbNbk3xqhXeId7z+1kitSvAcc5jHBB5xjzu8DKqXYJmZ"
                + "xBJWsI5PtHCJr7/wfmBvZhm7WMMpzvqhg4JVfgHHmMIBnnvhYcEqv4cLbOO6REcFw1jFDTZwW6HjgGHU0cQiPuJiXDCME8xg"
                + "JwsMp4MtPGVMGOARprGfBc7iIdYpC4wp37CZCV6hnQnG59LJBBvoZoI/j+X/g9+Lnx+HMnS2xgAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA4klEQVQ4T83Ur0uEQRAG4Of+BLEZBBGLQTCbRNAih6Bgsgki"
                + "NptJziYGwejFa4dBMIhRLGLQqsVwwWwyHjLiB8vByf2Y4MKUhfeZCbNbk3xqhXeId7z+1kitSvAcc5jHBB5xjzu8DKqXYJmZ"
                + "xBJWsI5PtHCJr7/wfmBvZhm7WMMpzvqhg4JVfgHHmMIBnnvhYcEqv4cLbOO6REcFw1jFDTZwW6HjgGHU0cQiPuJiXDCME8xg"
                + "JwsMp4MtPGVMGOARprGfBc7iIdYpC4wp37CZCV6hnQnG59LJBBvoZoI/j+X/g9+Lnx+HMnS2xgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmElEQVQ4jdXNoY4BAADG8d88g2BTKJJ8NlGxu3JdMU1QBEXw"
                + "BBc0mSdQkFXbJVmwCUyx8wIKb/Bd8N+++vv4pwoYoIVSCpxhiyvOWKKPSuKgjA4WuOAXvQT86gsrHDBMwk2sn3g1CY9xQzuJ"
                + "fuMPH0m0hyOKSXT6XKwi7qgn0Ql+kmAD+yQIJ9SS4AafSXCObhLcYZQE36QH4bUZnfYO4BkAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmElEQVQ4jdXNoY4BAADG8d88g2BTKJJ8NlGxu3JdMU1QBEXw"
                + "BBc0mSdQkFXbJVmwCUyx8wIKb/Bd8N+++vv4pwoYoIVSCpxhiyvOWKKPSuKgjA4WuOAXvQT86gsrHDBMwk2sn3g1CY9xQzuJ"
                + "fuMPH0m0hyOKSXT6XKwi7qgn0Ql+kmAD+yQIJ9SS4AafSXCObhLcYZQE36QH4bUZnfYO4BkAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAC8SURBVDhP"
                + "5dOxCkFRHMfxg2xG3cVCSpGZMliVxW4zm6zewwt4B5InsChlVpSFSYhJ+f7OPV7g3v/mW5/uOWf4d+re66zLhWcWYxTwxhOJ"
                + "yoSnBs7QCD7YYBUckaoShpjjgi1GMKuPBQ6Y6MCqDpbQ8IoOrJrihp7fGTXAHS2/M0ov6oSi39HvO0zaDmV0oc/LJN3ugaY2"
                + "aW+oXsijjbUOLNKwfby064ya/mGrdMOq5cArIsuBdUTx8o9y7gudKxyCiC+bbQAAAABJRU5ErkJggg==")
    public void moveToQuadraticCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.quadraticCurveTo(19, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAANElEQVQ4T2NkoDJgpLJ5DKMGUh6iQysM/1PoYbBvkb08Ag2k"
                + "MAgh2odWshn1MnkhQPVYBgBiiQQV+ctw0AAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAANElEQVQ4T2NkoDJgpLJ5DKMGUh6iQysM/1PoYbBvkb08Ag2k"
                + "MAgh2odWshn1MnkhQPVYBgBiiQQV+ctw0AAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGklEQVQ4jWNgGAWjYHCB/xTiUQNHwSgYOAAAZ/s/wXgqXt0A"
                + "AAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGklEQVQ4jWNgGAWjYHCB/xTiUQNHwSgYOAAAZ/s/wXgqXt0A"
                + "AAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAoSURBVDhP"
                + "YxgFo2AQAEYoDQL/oTS5AGwWE5hJRTD4DRwFo2DgAQMDAN0JAQjYZYmBAAAAAElFTkSuQmCC")
    public void lineWidthMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.lineWidth = 4; context.moveTo(2, 10); context.lineTo(18, 10); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAv0lEQVQ4T+2SvQ3CQAyFv7RI1FCwAz89YgtoYYyUKUBsAQUN"
                + "jMEAQCaAErZAlk7iiGzioKPDzRX39Pk92xmJK0vM4+fAFjABTsDjG/eawwvQB64BfA6vq4kGXAG54e4WwZeaRgOOgaMj7gAo"
                + "qzprKXegUwOVFGsvcAPMa4CSQhb4VpbDKbB3xO5Wr8ECtoEdMAJ6H8ALYBv/ew5bgMMAlwZxkwMwawrUDMZNihRAcwqeyI7d"
                + "vCR/YKNxqeLkM3wCcUQZFXL2p1wAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAv0lEQVQ4T+2SvQ3CQAyFv7RI1FCwAz89YgtoYYyUKUBsAQUN"
                + "jMEAQCaAErZAlk7iiGzioKPDzRX39Pk92xmJK0vM4+fAFjABTsDjG/eawwvQB64BfA6vq4kGXAG54e4WwZeaRgOOgaMj7gAo"
                + "qzprKXegUwOVFGsvcAPMa4CSQhb4VpbDKbB3xO5Wr8ECtoEdMAJ6H8ALYBv/ew5bgMMAlwZxkwMwawrUDMZNihRAcwqeyI7d"
                + "vCR/YKNxqeLkM3wCcUQZFXL2p1wAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZ0lEQVQ4jWNgGAUMDAwO1DbwPgMDw38ovZ6BgaGBUkv6oQZi"
                + "w8iWEA0M8BiIjBVIMRTmbXy4gBQD8Xkbhs+TYmAAEQaS5G0BBgaG/UQYSJK3YUAB6uIGLJasJ8dAfJaQ5cJRMArQAACZ8kHB"
                + "SAvL+gAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZ0lEQVQ4jWNgGAUMDAwO1DbwPgMDw38ovZ6BgaGBUkv6oQZi"
                + "w8iWEA0M8BiIjBVIMRTmbXy4gBQD8Xkbhs+TYmAAEQaS5G0BBgaG/UQYSJK3YUAB6uIGLJasJ8dAfJaQ5cJRMArQAACZ8kHB"
                + "SAvL+gAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACKSURBVDhP"
                + "7dHBDUAwFMbxikWM4OhoBKMYwQZGMQJHN0yAmy34XrwmTRVP49h/8kudXltVISrn1auIV7MZElhghAk65lUN+w3arIEKxKXg"
                + "GmajW4ijk7iGmEoQ93RtbQBxBbiG2C7Xjnm12yCDt/+0Qn9+yqOhdGJ62RbME9KL/5Le5NPDhELOlDoAyg88S5w3VsoAAAAA"
                + "SUVORK5CYII=")
    public void setTransformFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.setTransform(1, .2, .3, 1, 0, 0); context.fillRect(3, 3, 10, 7);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAv0lEQVQ4T+2SvQ3CQAyFv7RI1FCwAz89YgtoYYyUKUBsAQUN"
                + "jMEAQCaAErZAlk7iiGzioKPDzRX39Pk92xmJK0vM4+fAFjABTsDjG/eawwvQB64BfA6vq4kGXAG54e4WwZeaRgOOgaMj7gAo"
                + "qzprKXegUwOVFGsvcAPMa4CSQhb4VpbDKbB3xO5Wr8ECtoEdMAJ6H8ALYBv/ew5bgMMAlwZxkwMwawrUDMZNihRAcwqeyI7d"
                + "vCR/YKNxqeLkM3wCcUQZFXL2p1wAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAv0lEQVQ4T+2SvQ3CQAyFv7RI1FCwAz89YgtoYYyUKUBsAQUN"
                + "jMEAQCaAErZAlk7iiGzioKPDzRX39Pk92xmJK0vM4+fAFjABTsDjG/eawwvQB64BfA6vq4kGXAG54e4WwZeaRgOOgaMj7gAo"
                + "qzprKXegUwOVFGsvcAPMa4CSQhb4VpbDKbB3xO5Wr8ECtoEdMAJ6H8ALYBv/ew5bgMMAlwZxkwMwawrUDMZNihRAcwqeyI7d"
                + "vCR/YKNxqeLkM3wCcUQZFXL2p1wAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZ0lEQVQ4jWNgGAUMDAwO1DbwPgMDw38ovZ6BgaGBUkv6oQZi"
                + "w8iWEA0M8BiIjBVIMRTmbXy4gBQD8Xkbhs+TYmAAEQaS5G0BBgaG/UQYSJK3YUAB6uIGLJasJ8dAfJaQ5cJRMArQAACZ8kHB"
                + "SAvL+gAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZ0lEQVQ4jWNgGAUMDAwO1DbwPgMDw38ovZ6BgaGBUkv6oQZi"
                + "w8iWEA0M8BiIjBVIMRTmbXy4gBQD8Xkbhs+TYmAAEQaS5G0BBgaG/UQYSJK3YUAB6uIGLJasJ8dAfJaQ5cJRMArQAACZ8kHB"
                + "SAvL+gAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACKSURBVDhP"
                + "7dHBDUAwFMbxikWM4OhoBKMYwQZGMQJHN0yAmy34XrwmTRVP49h/8kudXltVISrn1auIV7MZElhghAk65lUN+w3arIEKxKXg"
                + "GmajW4ijk7iGmEoQ93RtbQBxBbiG2C7Xjnm12yCDt/+0Qn9+yqOhdGJ62RbME9KL/5Le5NPDhELOlDoAyg88S5w3VsoAAAAA"
                + "SUVORK5CYII=")
    public void transformFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.transform(1, .2, .3, 1, 0, 0); context.fillRect(3, 3, 10, 7);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQUlEQVQ4T2NkoDJgpLJ5DKMGUh6iIzgMFRgYGJJICME6XGqR"
                + "w7CJBAPnMTAwPMCmfgRHCgnBh1/paBhSHpRUD0MAmf0DFZFZTHcAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQUlEQVQ4T2NkoDJgpLJ5DKMGUh6iIzgMFRgYGJJICME6XGqR"
                + "w7CJBAPnMTAwPMCmfgRHCgnBh1/paBhSHpRUD0MAmf0DFZFZTHcAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAIUlEQVQ4jWNgGAWjYJiDBhIw0Qb2E4kFKHD4KBgFQw8AACnP"
                + "C6czPZCAAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAIUlEQVQ4jWNgGAWjYJiDBhIw0Qb2E4kFKHD4KBgFQw8AACnP"
                + "C6czPZCAAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAqSURBVDhP"
                + "YxgFo2A4AkYoDQINUJoYgFMtE5SGAX4isQAQj4JRMFIAAwMAXz4BqtdD7oMAAAAASUVORK5CYII=")
    public void moveToLineToTransformStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 10); context.lineTo(13, 10);"
                + "context.transform(1, .2, .3, 1, 0, 0); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZklEQVQ4T2NkoDJgpLJ5DKMGUh6io2EIDkMWBgaGP+SGJq4w"
                + "fMDAwHCbgYHhAhK+Sowl+CJFm4GBwQANI1sAYmNYQmoso1uiysDAoIDsclINxOZrlDCnhoEolowaSExKw6+G6mEIAEtuDRVn"
                + "zxh2AAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZklEQVQ4T2NkoDJgpLJ5DKMGUh6io2EIDkMWBgaGP+SGJq4w"
                + "fMDAwHCbgYHhAhK+Sowl+CJFm4GBwQANI1sAYmNYQmoso1uiysDAoIDsclINxOZrlDCnhoEolowaSExKw6+G6mEIAEtuDRVn"
                + "zxh2AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQklEQVQ4jWNgGAWjYIQAAVoY+p6BgWE9AwNDPwMDQwADA4MC"
                + "NQx1YGBgSIAaup+BgeE/PSx5Tw1D0QFNwnwUDGYAAER6Cjb+yQ7RAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQklEQVQ4jWNgGAWjYIQAAVoY+p6BgWE9AwNDPwMDQwADA4MC"
                + "NQx1YGBgSIAaup+BgeE/PSx5Tw1D0QFNwnwUDGYAAER6Cjb+yQ7RAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABWSURBVDhP"
                + "YxgFo2CkAAEoTRZghtLI4AcQvwdiCyjmhIp9AGKCgBFKYwMOQKwAxPpAbADlbwDiB0B8EIgvQNkoAJ+B2AC6JSAsCMRUBRSF"
                + "+SgYeoCBAQD2+wpun2ZPIwAAAABJRU5ErkJggg==")
    public void transformMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.transform(1, .2, .3, 1, 0, 0); context.moveTo(2, 10);"
                + "context.lineTo(13, 10); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAM0lEQVQ4T2NkoDJgpLJ5DKMGUh6iIzwMGygMQbB+5DCkuoEU"
                + "OhCifYTH8mgYkhcCVE82AJYgAhXAytwXAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAM0lEQVQ4T2NkoDJgpLJ5DKMGUh6iIzwMGygMQbB+5DCkuoEU"
                + "OhCifYTH8mgYkhcCVE82AJYgAhXAytwXAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGklEQVQ4jWNgGAWjYJiDBgox7Q0cBaNg2AAAkUIQAVuzCpUA"
                + "AAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGklEQVQ4jWNgGAWjYJiDBgox7Q0cBaNg2AAAkUIQAVuzCpUA"
                + "AAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAkSURBVDhP"
                + "YxgFo2A4AkYoDQINUJpcANbPBGaOglEwCvABBgYAX1ABBKc2Rr8AAAAASUVORK5CYII=")
    public void moveToLineToRotateStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 10); context.lineTo(18, 10); context.rotate(90); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAsElEQVQ4T+3SsQkCQRCF4f9ysQYRTC+zCQNLsAkNROMztwnR"
                + "GmxCLEEswEwEAxl4Ixucy+5qYHATz/sYHlMBNXDmR1MBe+AGbIDLt66BPWAFLIFG8L0UNtBnAKyBqdBtCRqCnh/r4pHgXQ7c"
                + "Bnp+IvipKo4pcAz0/ExV2CdYx6cYnAJ6fq6LD6ri2gbngJbvC10EH/EI4VzQs0PB1qtd/J5S8GONHZjyuvGdrsM/7PAFTW0Z"
                + "FZFBvS8AAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAsElEQVQ4T+3SsQkCQRCF4f9ysQYRTC+zCQNLsAkNROMztwnR"
                + "GmxCLEEswEwEAxl4Ixucy+5qYHATz/sYHlMBNXDmR1MBe+AGbIDLt66BPWAFLIFG8L0UNtBnAKyBqdBtCRqCnh/r4pHgXQ7c"
                + "Bnp+IvipKo4pcAz0/ExV2CdYx6cYnAJ6fq6LD6ri2gbngJbvC10EH/EI4VzQs0PB1qtd/J5S8GONHZjyuvGdrsM/7PAFTW0Z"
                + "FZFBvS8AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAdklEQVQ4je3MsQ0BAQAF0NdKxAQicRtco1YpjKAxgsIGl4tY"
                + "4HISuQk0RlAYQXMjaEwgYomvcm+Axw80OGGWCsc44IMao1Q8xxlP7FIpLHDFA5tkvMYdN6yS8RY9LiiT8R4vtJim0gmOeKNK"
                + "pVCgwzKZDgb/4wtTphDgBFZFZQAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAdklEQVQ4je3MsQ0BAQAF0NdKxAQicRtco1YpjKAxgsIGl4tY"
                + "4HISuQk0RlAYQXMjaEwgYomvcm+Axw80OGGWCsc44IMao1Q8xxlP7FIpLHDFA5tkvMYdN6yS8RY9LiiT8R4vtJim0gmOeKNK"
                + "pVCgwzKZDgb/4wtTphDgBFZFZQAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACESURBVDhP"
                + "7cixCYNQFIXhV9hIIK2NhJANUjiChbiBRVZIkQ0UQiYQCeICFq5gkRFSZAULNxDJf91AOOX74btwj6PIjrIODU7bJ+iAJ1ZU"
                + "CCHpjDcm3G1QlWDAF4UNqjJ8MCK1QdUNP/S42qDqgRk1YhsUHfHCghIBJF3QIt8+n8+3K+f+D8wRAv4wDRcAAAAASUVORK5C"
                + "YII=")
    public void rotateMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(.5); context.moveTo(1, 1); context.lineTo(18, 1); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGErgP4V41MChYOAoGAWjYOgCAGnPX6Ee+YVU"
                + "AAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVQ4jWNgGErgP4V41MChYOAoGAWjYOgCAGnPX6Ee+YVU"
                + "AAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAnSURBVDhP"
                + "Yxj0gBFKg8B/KE0uAJvFBGZSEYwaOApGwSgYBSDAwAAA4YcBDBgCJpwAAAAASUVORK5CYII=")
    public void rectFill() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rect(2, 2, 16, 6); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA90lEQVQ4T8XTwSoFcRQH4O8+hPIAkuRulSXqLiyUPMFlYyMW"
                + "srUXi5uNEkrd7uJ2ywvYsLCyEEmegKw8AR2N0mTMf24Ts5w635wzv3Maan4aNXv+BNzg80NtPOECA7ymTJPvMLA57GXYDFYw"
                + "mb07LEPz4CZucJUrbOIYj9jGcxGcB2/RwktBwQGmMJsK9tHFedloqeAaFrBYFziCayzhbhj0pz2MLiPZ6brAcM7whvWqaNGl"
                + "jKKHS+xUQX87vTEc4b5Kp2W3HJ3uYgKrKUGVgV/TRlBbeMBJtgnjWXD7339JKhg1sVLLmEdgp3hHZ1gwKZsqHf4P+AEXuSUV"
                + "Nt04AAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA90lEQVQ4T8XTwSoFcRQH4O8+hPIAkuRulSXqLiyUPMFlYyMW"
                + "srUXi5uNEkrd7uJ2ywvYsLCyEEmegKw8AR2N0mTMf24Ts5w635wzv3Maan4aNXv+BNzg80NtPOECA7ymTJPvMLA57GXYDFYw"
                + "mb07LEPz4CZucJUrbOIYj9jGcxGcB2/RwktBwQGmMJsK9tHFedloqeAaFrBYFziCayzhbhj0pz2MLiPZ6brAcM7whvWqaNGl"
                + "jKKHS+xUQX87vTEc4b5Kp2W3HJ3uYgKrKUGVgV/TRlBbeMBJtgnjWXD7339JKhg1sVLLmEdgp3hHZ1gwKZsqHf4P+AEXuSUV"
                + "Nt04AAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAm0lEQVQ4je3UsRWCMBRG4W8AqZ2JFg+sYOci2OkaqC0DoNbO"
                + "wgAWL5VHPSLp9FZpcpP38r/wByU69NijnisbsU7rDQZc0Xwj7JLskQo3tFOFfbrZM4p04HGKcCfKfMdyirDGecqGT7hglVPY"
                + "iAcockq3OOQUwknkLxsLEZFO5vJb0dMqp7QR5Q8ip6WYqHGuuBYfRi9a8Wqqfok70oEc6oUScX0AAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAm0lEQVQ4je3UsRWCMBRG4W8AqZ2JFg+sYOci2OkaqC0DoNbO"
                + "wgAWL5VHPSLp9FZpcpP38r/wByU69NijnisbsU7rDQZc0Xwj7JLskQo3tFOFfbrZM4p04HGKcCfKfMdyirDGecqGT7hglVPY"
                + "iAcockq3OOQUwknkLxsLEZFO5vJb0dMqp7QR5Q8ip6WYqHGuuBYfRi9a8Wqqfok70oEc6oUScX0AAAAASUVORK5CYII=",
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA50lEQVQ4T82UvQ4BURBGz5QKtVqLxBtorQdQKyU6T6JTqGi9"
                + "ALUnIEKr1yqUI3ezkrX3V7IRU8+cO9/MN1eoOaRmHr8BqpIBTeAqwuUbFVaHquyBYQlyAhYibFLAH8Cis52n0Dw0E+EWAleB"
                + "Y2AbKHgCmQgHX04V2APOEWl3EVpJQJOkyhHoR6AjkXzWVriWMgHWEeBKhGkSsOjSLMZYxxde2U5jq9KG3H+NANQp23spqgwg"
                + "n5MP6pQdPL0I1Ck7esuF/KVnppbsKPA9Q1XM9ucVS1myk4ElcBfoAA+XF78Gxj6I/we+AL7COxV8h8fPAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA50lEQVQ4T82UvQ4BURBGz5QKtVqLxBtorQdQKyU6T6JTqGi9"
                + "ALUnIEKr1yqUI3ezkrX3V7IRU8+cO9/MN1eoOaRmHr8BqpIBTeAqwuUbFVaHquyBYQlyAhYibFLAH8Cis52n0Dw0E+EWAleB"
                + "Y2AbKHgCmQgHX04V2APOEWl3EVpJQJOkyhHoR6AjkXzWVriWMgHWEeBKhGkSsOjSLMZYxxde2U5jq9KG3H+NANQp23spqgwg"
                + "n5MP6pQdPL0I1Ck7esuF/KVnppbsKPA9Q1XM9ucVS1myk4ElcBfoAA+XF78Gxj6I/we+AL7COxV8h8fPAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAi0lEQVQ4je3TTQ2AMAyG4U/CJCBhEpCAJBwgAQlIQAISkICE"
                + "l0tJduCnGzsRmvT6pF/TSn9JEqgBtbWwAbSBsJ5B3ZvJUiztqQSMF9jRCyjmgOEBBLTlTrk4UP9OHbEBjblTzrVjB0f0vFOy"
                + "E6oX29AIWqvETtBws9OyDzK4O5m2LwYTuDHc/zHfrh1q4TK9oNRCpgAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAi0lEQVQ4je3TTQ2AMAyG4U/CJCBhEpCAJBwgAQlIQAISkICE"
                + "l0tJduCnGzsRmvT6pF/TSn9JEqgBtbWwAbSBsJ5B3ZvJUiztqQSMF9jRCyjmgOEBBLTlTrk4UP9OHbEBjblTzrVjB0f0vFOy"
                + "E6oX29AIWqvETtBws9OyDzK4O5m2LwYTuDHc/zHfrh1q4TK9oNRCpgAAAABJRU5ErkJggg==",
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAwUlEQVQ4T+3UIW4CQRTG8d9WoRCk3ICkjjPUgaguCk1Jg8Sh"
                + "UFyAEJI6VDkBkjPgUBygTQWqooJssiRku7PZTUYybjLf+7/5vry8ROSTROa5AwsTbeGnatZlGXYwxQmLGMAP/OGtKizVhX7Y"
                + "wwQvdWBlwBkeMI8FfEcXowBwjQOW+feQ5QHGeA4A91jhsyow1e1wxmuuaIsm+kXNysamjQ0aOGbFT/jFEF91gVd9av8xu3wX"
                + "2bwF35dD3TH+r4+e4QUSbhYV9wlfkwAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAwUlEQVQ4T+3UIW4CQRTG8d9WoRCk3ICkjjPUgaguCk1Jg8Sh"
                + "UFyAEJI6VDkBkjPgUBygTQWqooJssiRku7PZTUYybjLf+7/5vry8ROSTROa5AwsTbeGnatZlGXYwxQmLGMAP/OGtKizVhX7Y"
                + "wwQvdWBlwBkeMI8FfEcXowBwjQOW+feQ5QHGeA4A91jhsyow1e1wxmuuaIsm+kXNysamjQ0aOGbFT/jFEF91gVd9av8xu3wX"
                + "2bwF35dD3TH+r4+e4QUSbhYV9wlfkwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAbklEQVQ4je3QsQ2AIBCF4b81LuAmzGNJ6xxuIAWtVs7BOBbU"
                + "FpydIUKusLivIrzjJQcYswKLVtkBnMCoUTYBl0bRYwZ2zUIPhEq+ycxnDsiVPMtMkwikl/skWbNBHmbKikHOUbJujvJfno41"
                + "zQ/codMRZHZFRf8AAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAbklEQVQ4je3QsQ2AIBCF4b81LuAmzGNJ6xxuIAWtVs7BOBbU"
                + "FpydIUKusLivIrzjJQcYswKLVtkBnMCoUTYBl0bRYwZ2zUIPhEq+ycxnDsiVPMtMkwikl/skWbNBHmbKikHOUbJujvJfno41"
                + "zQ/codMRZHZFRf8AAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACPSURBVDhP"
                + "YxgFIwAwQ2lsgBOI5wGxABBfAAlQChYD8SkIk3JgAsRXIUzSABOURgdOQLwXwiQN4DLwExCzQ5hYwUwgzoYwiQM2QHwPwsQK"
                + "QMFhC2ESDzYC8QwIEwVMAeJlECbpYC0Qg1w6FYhBMX4biJcAMSsQYwWMUBofMANiUKzzAPFuID4PxKNg6AAGBgDVbRIf3/D2"
                + "vgAAAABJRU5ErkJggg==")
    public void arcStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 4.3); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApElEQVQ4T+3SLQoCURSG4WeCYLLoAmw2k4uwWjS5FifbRVyC"
                + "FrfgAgTBJha7ZjEY5F4mCQMzzkzz5HNevp+TIFXjJBmwLmj6B1Zu559h5Qg1m+EMvUzkA9sfBEeFSwzRxiWDDPDCHPcS4Ai8"
                + "4oTp1+EOHYzLAm/o5xwdsC5hfxEUHjHKAW5wxqqgymi5dmCdlpspJbzNBC2E/wvTxRt7PAvmF9c+ZTExFH4T/k4AAAAASUVO"
                + "RK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApElEQVQ4T+3SLQoCURSG4WeCYLLoAmw2k4uwWjS5FifbRVyC"
                + "FrfgAgTBJha7ZjEY5F4mCQMzzkzz5HNevp+TIFXjJBmwLmj6B1Zu559h5Qg1m+EMvUzkA9sfBEeFSwzRxiWDDPDCHPcS4Ai8"
                + "4oTp1+EOHYzLAm/o5xwdsC5hfxEUHjHKAW5wxqqgymi5dmCdlpspJbzNBC2E/wvTxRt7PAvmF9c+ZTExFH4T/k4AAAAASUVO"
                + "RK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAf0lEQVQ4je3SsQnDMBAF0NdmIs+ULVRkC7vwJiKtIfY2bty4"
                + "0JEqCJSoChYcHOjz4HSCjNSpsmh6nXSBF/j34IB71PAL+MCMHRPG6GfcvgE3LB8ul0CbwaMS2LWNn+BVCUwN2BtcK4FRWVIT"
                + "2H3k7kt5Bnoo77lGvylfKjVUPgEr0zlzAxnltAAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAf0lEQVQ4je3SsQnDMBAF0NdmIs+ULVRkC7vwJiKtIfY2bty4"
                + "0JEqCJSoChYcHOjz4HSCjNSpsmh6nXSBF/j34IB71PAL+MCMHRPG6GfcvgE3LB8ul0CbwaMS2LWNn+BVCUwN2BtcK4FRWVIT"
                + "2H3k7kt5Bnoo77lGvylfKjVUPgEr0zlzAxnltAAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACjSURBVDhP"
                + "Y2RgYNgPxAeBmBrAHkQ0gJnUAQ1MUAbVwKiBlINRAykHyAYqAnEyFIPYZAGYgfOA+BgQO0ExiA0SIxmADPQHYl4glgTiaCgG"
                + "sUFiZBn6GUpjA8+BmBTvgwuH+xA2VrAPiPHJYwBiYhkUSUQDkIH4vASKIJAriQYgA+8A8WowDxWAxLYDMUlehlUBAkCsAsQw"
                + "zSBXgyzaCOYRDRjsAb/2Fyf2EU9MAAAAAElFTkSuQmCC")
    public void arcCircleStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(4, 16, 4, 0, 2 * Math.PI); context.stroke(); context.strokeRect(0, 0, 20, 20);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAd0lEQVQ4T+3UsQ3CMBBA0eeShpaGni4VQ2QJWjaKUEZIpmAG"
                + "1oABUiK3loOs4CKF3fvp/C1dUPmEyp4GZote0eGAD6Zf3Usa3nHCGRcsuOGdg0vA9N6MI/paYHSeeOSev2XCCI54YUin3Aqu"
                + "/ksD/18VreEOG34BnYcKFagjKwUAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAd0lEQVQ4T+3UsQ3CMBBA0eeShpaGni4VQ2QJWjaKUEZIpmAG"
                + "1oABUiK3loOs4CKF3fvp/C1dUPmEyp4GZote0eGAD6Zf3Usa3nHCGRcsuOGdg0vA9N6MI/paYHSeeOSev2XCCI54YUin3Aqu"
                + "/ksD/18VreEOG34BnYcKFagjKwUAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAR0lEQVQ4je3QMQ2AMBRF0bNWBDqwVGbkAAl6KqgzA9RA8xeS"
                + "fwTc5D3SLy2o2LBGBHdcONBxo0SEh/ZFQ3VB84fT+2lKacoDTsgIDIbBFacAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAR0lEQVQ4je3QMQ2AMBRF0bNWBDqwVGbkAAl6KqgzA9RA8xeS"
                + "fwTc5D3SLy2o2LBGBHdcONBxo0SEh/ZFQ3VB84fT+2lKacoDTsgIDIbBFacAAAAASUVORK5CYII=",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABUSURBVDhP"
                + "YxgFgw8wQ2l8QBKI/YHYA4hfAvF7IMYJmKA0PpAAxL5ArAzEJ4B4NRBTFSwH4jkQJvXAQyBWgzBRATFhiA0YQ+lzUHoUjIJR"
                + "QAJgYAAAKqEIH3WHwEcAAAAASUVORK5CYII=")
    public void arcAnticlockwiseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 4.3, true); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmklEQVQ4T+3UoQ0CQRBG4e8cCgU14KgBSQWg0IRcPYSgUVAB"
                + "khpw1AAKhSSbHILL7iZcNqhbO5mXff9MplL4VYV5emA00SVGTeWBYy73XIZjHDDArYFM8MIK9xg4BzzjiUWr8YQh5r8Ag+YG"
                + "s4TeBbuYfuqHNaZYJ4B7XLFt1/8GLK4cTIoOJQCLr80n72KL3ekQ9demU2xfTW+HmB4VAkhDzQAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmklEQVQ4T+3UoQ0CQRBG4e8cCgU14KgBSQWg0IRcPYSgUVAB"
                + "khpw1AAKhSSbHILL7iZcNqhbO5mXff9MplL4VYV5emA00SVGTeWBYy73XIZjHDDArYFM8MIK9xg4BzzjiUWr8YQh5r8Ag+YG"
                + "s4TeBbuYfuqHNaZYJ4B7XLFt1/8GLK4cTIoOJQCLr80n72KL3ekQ9demU2xfTW+HmB4VAkhDzQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaUlEQVQ4je2SQQrAIAwE59oX5U2+yRzyHh/kuQfTQ6FSgoFS"
                + "cEAIblhcXNj8EgGKH1kxOgADOqBA9dlcC2NAe7hvroUQf82MTjB+YUScob4TMtQXwxDpkSH5U+Bem8qIuVSbi7Ribz7iBHkE"
                + "GXFzGcnHAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaUlEQVQ4je2SQQrAIAwE59oX5U2+yRzyHh/kuQfTQ6FSgoFS"
                + "cEAIblhcXNj8EgGKH1kxOgADOqBA9dlcC2NAe7hvroUQf82MTjB+YUScob4TMtQXwxDpkSH5U+Bem8qIuVSbi7Ribz7iBHkE"
                + "GXFzGcnHAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACJSURBVDhP"
                + "YxgFgw8wQml8wByIjSBMhnNAfBLCJB1wAvECIP4GxHOAeDaUDRIDyZEMQBpPQZgoACQGkiMJgLwJcg0uAJIDqcEAzFAaHfgB"
                + "8Rsg3gzmYQJFKH0GSsMBE5TGBghF2HQoTRQg28v4AFUjBQSQkw0oyYCSDogNEsOZbOiasEfBgAAGBgDFsxrAzDMHqwAAAABJ"
                + "RU5ErkJggg==")
    public void arcCircleAnticlockwiseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 2 * Math.PI, true); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAdklEQVQ4T+2UsQ2AMAwEzxPBVBQwBGEIKJgKJjIKBUIoThEM"
                + "VdL73rm8IjgfceZRge+N5h0GepQOoTmjlB1hJbBY0TYwwmA2BgcLagNHtmuzJzVuOtGmwnIbatZoSFfuR+AHV3Z+lCjQtTaF"
                + "Ha+fQ6G425i7wwPzuB4VlcqptAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAdklEQVQ4T+2UsQ2AMAwEzxPBVBQwBGEIKJgKJjIKBUIoThEM"
                + "VdL73rm8IjgfceZRge+N5h0GepQOoTmjlB1hJbBY0TYwwmA2BgcLagNHtmuzJzVuOtGmwnIbatZoSFfuR+AHV3Z+lCjQtTaF"
                + "Ha+fQ6G425i7wwPzuB4VlcqptAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZElEQVQ4jWNgGAUjHTQwCDA0MPQzNDCcZ2hguM/QwDCfoYFB"
                + "gRLD3jM0MPxHw+/JMxTiGnTDYHg+OQbex2Pg+0FhID4vryfHQAGcrmtgMCDdQISh86GGvGdoYNhPfrIZBcMHAABnQk69XCBV"
                + "ZwAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZElEQVQ4jWNgGAUjHTQwCDA0MPQzNDCcZ2hguM/QwDCfoYFB"
                + "gRLD3jM0MPxHw+/JMxTiGnTDYHg+OQbex2Pg+0FhID4vryfHQAGcrmtgMCDdQISh86GGvGdoYNhPfrIZBcMHAABnQk69XCBV"
                + "ZwAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAClSURBVDhP"
                + "YxgFIwAwQmnsoJ5BAEgWAFX5A2kQewPDf4ZGIPwAksYGcBsIMoyRYT+QZQARgIMHQEMNcRnKBKWxgQIgRjcMBBSAFvVD2RgA"
                + "t4GMDPFQFjYQAKUxAD4X4gM4wxCfgRugNDZwAEpjANwGgmKTgeEChIMCQJECksMKmKE0JjjI8IPBnmElMCw5gDwNIP4BxDuA"
                + "hkUCjXsAUjIKRiZgYAAArLcaWxWRPP8AAAAASUVORK5CYII=")
    public void arcFillPath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = 'green'; context.beginPath();"
                + "context.arc(10, 10, 4, 0, 2 * Math.PI); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABCElEQVQ4T83US0oDQRSF4a/FhSi6DgXRbQgq6C5MKe7BVjSI"
                + "I2cuQMHHxAU4UiHZgyNBMWJXh7RtJ+k0QbxQUINTP6du3VOJKVcyZZ7hwH3LPq1jCXOYRRe3OBPcV5mpBgYn2Brj/liwU9b8"
                + "BgZXWK3ZimvBWlH7ExgcYbsmrC9rC4PbDIAh69XdhLAoT6xouYnbfgVtbDQCxkfKzhaBHcw3BHYEC2VgryEsHgvRXNHhez5r"
                + "k3IvzEjtxv4XgS9YrEl7Q+pD6sBT9dgEp9gcCUx09aTZ2vM6Oikxat+xqqqHzFFwPu4G5cEuR+5S4lArS0+tGha957zRj7Uo"
                + "BdEffl+TWsv1/9/hF6I7OBXnPPH0AAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABCElEQVQ4T83US0oDQRSF4a/FhSi6DgXRbQgq6C5MKe7BVjSI"
                + "I2cuQMHHxAU4UiHZgyNBMWJXh7RtJ+k0QbxQUINTP6du3VOJKVcyZZ7hwH3LPq1jCXOYRRe3OBPcV5mpBgYn2Brj/liwU9b8"
                + "BgZXWK3ZimvBWlH7ExgcYbsmrC9rC4PbDIAh69XdhLAoT6xouYnbfgVtbDQCxkfKzhaBHcw3BHYEC2VgryEsHgvRXNHhez5r"
                + "k3IvzEjtxv4XgS9YrEl7Q+pD6sBT9dgEp9gcCUx09aTZ2vM6Oikxat+xqqqHzFFwPu4G5cEuR+5S4lArS0+tGha957zRj7Uo"
                + "BdEffl+TWsv1/9/hF6I7OBXnPPH0AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmUlEQVQ4je3TawnDMBSG4VdCJFRCJVRKJUTC52ASKqESIqUS"
                + "ImH9kQ46dtJcGINBD5x/4eHcAv8bYkB4REBExIZYEQ+Ea8X8gTwv0tdADrEUoHOuNZXVYq+cr6ortZnLwQLViWXmmTbYCxqz"
                + "TGfRC8ZvggExWmBoqiidlwG1LSUe74ytfoKu0Jan48vNBjS1Q+/okh/0Hb+IHUTl6U4n5uMBAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmUlEQVQ4je3TawnDMBSG4VdCJFRCJVRKJUTC52ASKqESIqUS"
                + "ImH9kQ46dtJcGINBD5x/4eHcAv8bYkB4REBExIZYEQ+Ea8X8gTwv0tdADrEUoHOuNZXVYq+cr6ortZnLwQLViWXmmTbYCxqz"
                + "TGfRC8ZvggExWmBoqiidlwG1LSUe74ytfoKu0Jan48vNBjS1Q+/okh/0Hb+IHUTl6U4n5uMBAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAECSURBVDhP"
                + "7ZPtEYIwDIaDLsAIuIFOoE4gI+gE4gTABI4gG3hOoG7ACG4gG+CbNkC1BZQ77/zhwzUfUN42JdDP44m3iWkKG2LGCj7AKDBu"
                + "GDmVlOLi3MIWjMmHXeNJDM+xGy2aSFZjCyZ0hA110ssF85cSK8biNTFFWCKS7B0CmuONK4SFZod8Zh6dEbWX2UZJE5TP50sj"
                + "dUOzwPhcjPFoK5EhqL/mULgjFOYOuTWG4hQcygVnWH9pU9DZqC3w3AxCM9U2KZpdaARLOknURaEaWgttTKEKs218ZHfJXuGy"
                + "eMEMIp2VPP8pCe1hzcZmoRQ+7xOqcP16B9gAQjtXSX++DdEDir42LCGgXtoAAAAASUVORK5CYII=")
    public void arcFillPathAngle() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = 'green'; context.beginPath();"
                + "context.arc(10, 10, 8, 2.3, 2 * Math.PI); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAt0lEQVQ4T8WTsQ0CMQxF381CcTuwAAvQUt0EMAhMQEV7C7AA"
                + "O1CwC7J0h6IQ+7uIIFWk2O9/Jz8DndfQmcdPgDfglXS+AQ5lbe1wD1yAaxI4AUdgXutbIz+Ac1nkwE38BGwjh3bWLGxAm8Le"
                + "oyiXrqgHVC5dwSg2XlMoFgG9xvA6VLDrZnUV8qfUAPVYEmhpWSG2/8pdHSc1cplL28vAZ4AGei5ORvUls8DdArr3AirO5zzr"
                + "8H/AN8CgIxU+kSAmAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAt0lEQVQ4T8WTsQ0CMQxF381CcTuwAAvQUt0EMAhMQEV7C7AA"
                + "O1CwC7J0h6IQ+7uIIFWk2O9/Jz8DndfQmcdPgDfglXS+AQ5lbe1wD1yAaxI4AUdgXutbIz+Ac1nkwE38BGwjh3bWLGxAm8Le"
                + "oyiXrqgHVC5dwSg2XlMoFgG9xvA6VLDrZnUV8qfUAPVYEmhpWSG2/8pdHSc1cplL28vAZ4AGei5ORvUls8DdArr3AirO5zzr"
                + "8H/AN8CgIxU+kSAmAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXklEQVQ4je2PsREAIAgDM6QDOZ07WLuIDTR6aEA7/Tu6JL7A"
                + "8+TgmSQAzTHUpLOkMCHJFCJHB9mHqTBtxxZcdruS225XDNlZ5bCdNXBkN44c2yk6dMVOqXLX0C9/ZjqpYSbR+dDHYAAAAABJ"
                + "RU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXklEQVQ4je2PsREAIAgDM6QDOZ07WLuIDTR6aEA7/Tu6JL7A"
                + "8+TgmSQAzTHUpLOkMCHJFCJHB9mHqTBtxxZcdruS225XDNlZ5bCdNXBkN44c2yk6dMVOqXLX0C9/ZjqpYSbR+dDHYAAAAABJ"
                + "RU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACASURBVDhP"
                + "7c7BCYAwDAXQqiu6gYs4mUcnEMENHMOkzVcotElbb/ogpEISv/ueTjrM0kvde7102Kim8DThWd7JWqnG8MziGZ5VWQetP/a0"
                + "YXM60BaK0kFqqTgdpBar0kG8XJ0O4gNN6QBHmtMBDr2SDg4p1SBdc1ItVLv/+j2cuwDuXB2rdVDEoAAAAABJRU5ErkJggg==")
    public void closePath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(4,4); context.lineTo(10,16);"
                + "context.lineTo(16,4); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUlEQVQ4jWNgGAWjYBSMglEwCqgDAAZUAAHyXCJfAAAAAElF"
                + "TkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUlEQVQ4jWNgGAWjYBSMglEwCqgDAAZUAAHyXCJfAAAAAElF"
                + "TkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAWSURBVDhP"
                + "YxgFo2AUjIJRMAqoAhgYAAZUAAE6HI5PAAAAAElFTkSuQmCC")
    public void closePathNoSubpath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.closePath();context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUlEQVQ4jWNgGAWjYBSMglEwCqgDAAZUAAHyXCJfAAAAAElF"
                + "TkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUlEQVQ4jWNgGAWjYBSMglEwCqgDAAZUAAHyXCJfAAAAAElF"
                + "TkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAWSURBVDhP"
                + "YxgFo2AUjIJRMAqoAhgYAAZUAAE6HI5PAAAAAElFTkSuQmCC")
    public void closePathPointOnly() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(4,4); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUlEQVQ4jWNgGAWjYBSMglEwCqgDAAZUAAHyXCJfAAAAAElF"
                + "TkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUlEQVQ4jWNgGAWjYBSMglEwCqgDAAZUAAHyXCJfAAAAAElF"
                + "TkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAWSURBVDhP"
                + "YxgFo2AUjIJRMAqoAhgYAAZUAAE6HI5PAAAAAElFTkSuQmCC")
    public void closePathTwice() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.closePath(); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAArklEQVQ4T+2TsRGCQBBFH7UY0AMN2ICpERVIIVIBkSkN2IA9"
                + "GNiLs86tsywHh7OGXMTsn/9u+Xwq/nwqwzsCd8e3sxvwcvoBONuZAk9ABzTO8ACuadYDg9Nb4AKMOrcbqllFf0lJ/zAt8FeA"
                + "v2AGlIF9xVIEOX2yoQB1S3mW7L7ZpIxK+gwovmcy1wuNWtVthuqXqsjxFdqk54Chqu/AUHzZYoeJ+0cJR5j9l0PUN96cIRWF"
                + "sMoDAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAArklEQVQ4T+2TsRGCQBBFH7UY0AMN2ICpERVIIVIBkSkN2IA9"
                + "GNiLs86tsywHh7OGXMTsn/9u+Xwq/nwqwzsCd8e3sxvwcvoBONuZAk9ABzTO8ACuadYDg9Nb4AKMOrcbqllFf0lJ/zAt8FeA"
                + "v2AGlIF9xVIEOX2yoQB1S3mW7L7ZpIxK+gwovmcy1wuNWtVthuqXqsjxFdqk54Chqu/AUHzZYoeJ+0cJR5j9l0PUN96cIRWF"
                + "sMoDAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAWElEQVQ4jWNgGCDQQABjAD4GBoZjWMSPQeVCGBgYHmMx6DFU"
                + "Dis4hiYZgmYJIXkMQKoB6PJ4XYnLdkLyOF2Jy3ZC8ljBTSgmVx6rK/DZTkh+FIyCUUB/AADnCiBrRndbagAAAABJRU5ErkJg"
                + "gg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAWElEQVQ4jWNgGCDQQABjAD4GBoZjWMSPQeVCGBgYHmMx6DFU"
                + "Dis4hiYZgmYJIXkMQKoB6PJ4XYnLdkLyOF2Jy3ZC8ljBTSgmVx6rK/DZTkh+FIyCUUB/AADnCiBrRndbagAAAABJRU5ErkJg"
                + "gg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAB2SURBVDhP"
                + "Y6A2YITShEADlMYF4PJMUJoPiI9BmCgAJAaSuwLEySABNAASA8lhBSDNIRAmGIDYyJYQkscApBqALo8VwBSha4YBQvIYAKYQ"
                + "phEdEJLHCm5CMS6AV54ZSiODF0C8D4ivgXmYgJD8KBgFo4DegIEBAO+EHxk/jOycAAAAAElFTkSuQmCC")
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZklEQVQ4T2Nk2Mf8nwEZOP1lROET4qDpZ6S6gfsYUF3oxECa"
                + "C9H1M1LdQHgQURqWUIMQEUBtAyn1OsyncBdS3UBCyY1YedISMRGmjhpIRCARUDISwhCjPKQ02KhtIEZ5SLED0QpoADR/JhX5"
                + "NimNAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZklEQVQ4T2Nk2Mf8nwEZOP1lROET4qDpZ6S6gfsYUF3oxECa"
                + "C9H1M1LdQHgQURqWUIMQEUBtAyn1OsyncBdS3UBCyY1YedISMRGmjhpIRCARUDISwhCjPKQ02KhtIEZ5SLED0QpoADR/JhX5"
                + "NimNAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4jWNg2Mf8HwWTCjD0U9vAfQzM/5Exyeah66e6gTid"
                + "TjGgtoGUep32Bo6CUTCkwWgZAADAuXABXILcxAAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4jWNg2Mf8HwWTCjD0U9vAfQzM/5Exyeah66e6gTid"
                + "TjGgtoGUep32Bo6CUTCkwWgZAADAuXABXILcxAAAAABJRU5ErkJggg==",
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAa0lEQVQ4T2Nk2Mf8n4GBgWGfE4hEACeGv4yoIsTxGKlu4D4G"
                + "iAvJdRG6uxmpbiDcBmhYwvlO5IYhzARqGwjzOsx8csMUnjSobiBxqYywKrISLz5jRw0kHOiEVIyGIaEQIixPvTCEFi5UNxAA"
                + "QAsdlUr5XXwAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAa0lEQVQ4T2Nk2Mf8n4GBgWGfE4hEACeGv4yoIsTxGKlu4D4G"
                + "iAvJdRG6uxmpbiDcBmhYwvlO5IYhzARqGwjzOsx8csMUnjSobiBxqYywKrISLz5jRw0kHOiEVIyGIaEQIixPvTCEFi5UNxAA"
                + "QAsdlUr5XXwAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAOklEQVQ4jWNg2Mf8n2Ef8/99DKiYgWxAbQMpdxGtDUSYDPE6"
                + "HA86A6kXy7QycBSMglFAF0C1UgrNQAAO0T8Blr60fgAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAOklEQVQ4jWNg2Mf8n2Ef8/99DKiYgWxAbQMpdxGtDUSYDPE6"
                + "HA86A6kXy7QycBSMglFAF0C1UgrNQAAO0T8Blr60fgAAAABJRU5ErkJggg==",
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXUlEQVQ4T+2SyQkAIAwEE+zMwtOZKNGPEGE99qnfwDAOq0J+"
                + "SuVZqh1okmqW8gx3zjNk/iEd6HCq4RLo2n64bRoM6cAwIxvGkvdWgBvSgYfLx4YfiAr8hqgQvtMbNp+UFFXneC64AAAAAElF"
                + "TkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXUlEQVQ4T+2SyQkAIAwEE+zMwtOZKNGPEGE99qnfwDAOq0J+"
                + "SuVZqh1okmqW8gx3zjNk/iEd6HCq4RLo2n64bRoM6cAwIxvGkvdWgBvSgYfLx4YfiAr8hqgQvtMbNp+UFFXneC64AAAAAElF"
                + "TkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAOElEQVQ4jWNgGNRgH/N/CM0AZVBqHpXMoZ2B9AH7GJj/U9Xp"
                + "VDcQ0wbm//DkMDgNHAWjYBQwMAAACd4VASBR1nMAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAOElEQVQ4jWNgGNRgH/N/CM0AZVBqHpXMoZ2B9AH7GJj/U9Xp"
                + "VDcQ0wbm//DkMDgNHAWjYBQwMAAACd4VASBR1nMAAAAASUVORK5CYII=",
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABfElEQVQ4T63Uv0uXURTH8ZdIk39AppMQNUXQGLZIGILQIgRF"
                + "BC7+aFKcxEEcGnVoMCGKkGhpDLKmQNRNEMQQmiQQFwmVICOUA8+Vy9Pz1af0jvfe8z6/Puc0ueDTlPHu4VOJH3eX8KGu3wTs"
                + "wyhulwy/oh238K0ONI9wGdN4j5sYRC8C2oFwunYWNAeGwRS+4waa8QUbeIQ2LGEGHxuBc+AkxjGLxYoSxN0WujCH+P/XScB4"
                + "7C5gQ8WvlH4yuooVDGMEn6ugAezBK1zDPjYLwvWKAKKmr3EXC+gvpx/A8PQGbwtASCVOWUKJP4b7eIEnRWYnvgO4jStZNEdn"
                + "dTJ7/1lI7aT7AZzH4/8EHuIpXib7AEZD8o79S4TBeYeHFwX8jWd5QOdNeQcT5ZTP05QDdOYjWSWbujWMDq/iTq6KKmHXBcYQ"
                + "PKgSdjhIoxei3quhw108bzR6yT6gA2g9BRhp/inW3KnLITFirmPjxEKNlC7jF36gBevFimu4vo4BevlWFYnpLtIAAAAASUVO"
                + "RK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABfElEQVQ4T63Uv0uXURTH8ZdIk39AppMQNUXQGLZIGILQIgRF"
                + "BC7+aFKcxEEcGnVoMCGKkGhpDLKmQNRNEMQQmiQQFwmVICOUA8+Vy9Pz1af0jvfe8z6/Puc0ueDTlPHu4VOJH3eX8KGu3wTs"
                + "wyhulwy/oh238K0ONI9wGdN4j5sYRC8C2oFwunYWNAeGwRS+4waa8QUbeIQ2LGEGHxuBc+AkxjGLxYoSxN0WujCH+P/XScB4"
                + "7C5gQ8WvlH4yuooVDGMEn6ugAezBK1zDPjYLwvWKAKKmr3EXC+gvpx/A8PQGbwtASCVOWUKJP4b7eIEnRWYnvgO4jStZNEdn"
                + "dTJ7/1lI7aT7AZzH4/8EHuIpXib7AEZD8o79S4TBeYeHFwX8jWd5QOdNeQcT5ZTP05QDdOYjWSWbujWMDq/iTq6KKmHXBcYQ"
                + "PKgSdjhIoxei3quhw108bzR6yT6gA2g9BRhp/inW3KnLITFirmPjxEKNlC7jF36gBevFimu4vo4BevlWFYnpLtIAAAAASUVO"
                + "RK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABCElEQVQ4jb3UP07DUAzH8c+AGLlCl54B1Dt0LBI7EwNbB8TA"
                + "RXoBlhwAqROIbqBuiIETwFKCVP6pZcCRovS1JG3FT/Jgx/7mOX4O/6weuusU7mGUiD/gFe11oKM4EezgDE/IMI5YI/VwF4AZ"
                + "3jDEAC+Yx7NWXeAxvnGJI4uf4BZXmETun7Ac5wEqt1+ojWf0I3cptBVv7YT/GJZSN6CHUZNsP8NFye8lTldWHzdRk6USZtgt"
                + "+fOGtjD9+4rfBDbFQRU42AA4k9ikTYCfONlmyzn2q8CtD6V6bZoM5LoKY/Fi1wW+W7HXxep1asK+cLoMVoZOarQ5rQMr1PL7"
                + "yyom+BGWR2y4qk34AQ9nqmQoAfOBAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABCElEQVQ4jb3UP07DUAzH8c+AGLlCl54B1Dt0LBI7EwNbB8TA"
                + "RXoBlhwAqROIbqBuiIETwFKCVP6pZcCRovS1JG3FT/Jgx/7mOX4O/6weuusU7mGUiD/gFe11oKM4EezgDE/IMI5YI/VwF4AZ"
                + "3jDEAC+Yx7NWXeAxvnGJI4uf4BZXmETun7Ac5wEqt1+ojWf0I3cptBVv7YT/GJZSN6CHUZNsP8NFye8lTldWHzdRk6USZtgt"
                + "+fOGtjD9+4rfBDbFQRU42AA4k9ikTYCfONlmyzn2q8CtD6V6bZoM5LoKY/Fi1wW+W7HXxep1asK+cLoMVoZOarQ5rQMr1PL7"
                + "yyom+BGWR2y4qk34AQ9nqmQoAfOBAAAAAElFTkSuQmCC",
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAArUlEQVQ4T2NkoDJgpLJ5DDgN/C8yyZ7hP1Mc49ucZFIsxWPg"
                + "5P8ggxjf5JLki1EDEcH/X2TYhyHMizBPw5ILujiuNAlTD082/0Um32VgYFAi08B7jG9ylcHpFmbAf+EpcxkY/yeRZeB/xnmw"
                + "HIXkwkn2DAyMB8gykOG/A+ObvIMoLgRx/otMnsXAwJCKnOWICMPZjG9y0+AOwRXIQyEdUrn4IqUMRFZLUllHjCUA7nRdFSxw"
                + "V1kAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAArUlEQVQ4T2NkoDJgpLJ5DDgN/C8yyZ7hP1Mc49ucZFIsxWPg"
                + "5P8ggxjf5JLki1EDEcH/X2TYhyHMizBPw5ILujiuNAlTD082/0Um32VgYFAi08B7jG9ylcHpFmbAf+EpcxkY/yeRZeB/xnmw"
                + "HIXkwkn2DAyMB8gykOG/A+ObvIMoLgRx/otMnsXAwJCKnOWICMPZjG9y0+AOwRXIQyEdUrn4IqUMRFZLUllHjCUA7nRdFSxw"
                + "V1kAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAbklEQVQ4jWNgGLLgv+hUg/8ikwqoZ6DI5Pf/RSb/p6aB/0cN"
                + "HG4G/heech9mCLJByGJ48HtsBs6nwMD9WLw4sYF8A6f0Yxoo0K9ApoHv/4tMdsAejiKTCkg3cGID4RhiGIzJBsNA0akGRHtn"
                + "SAEAyuPmw0SBrRQAAAAASUVORK5CYII=",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAbklEQVQ4jWNgGLLgv+hUg/8ikwqoZ6DI5Pf/RSb/p6aB/0cN"
                + "HG4G/heech9mCLJByGJ48HtsBs6nwMD9WLw4sYF8A6f0Yxoo0K9ApoHv/4tMdsAejiKTCkg3cGID4RhiGIzJBsNA0akGRHtn"
                + "SAEAyuPmw0SBrRQAAAAASUVORK5CYII=",
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
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAlklEQVQ4je2SsQnDMBBF30yezCOI1NlAC3gEdW5cuXYX0miB"
                + "4Am+GyWWDwXFxKSJDz7ow7/H3SEAB/RJ7gCBNUfUfwJ9AHUgJbU2JGgFg9bQtQaMWbveAdO7EUSbKUz4ah9ATQHYZb4K/GTC"
                + "KtABPVxucI/reXzAfFoPYYTp6R8w24wBjlMhsFeblbMbflcb+gk8gT8GLkqKbVBaXVuaAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAjklEQVQ4je2TwQmFMBBEX4+2YAE2EEgBFpJU4SWnnFOBjcy/"
                + "iOYvQgSDF12Yw4TZx7JkATywbPIdBNb0qHcCXQQFkDYlGxIMgqQjtLaAqtoDaDoBrpVPgqEx4R6fQPMJMFQ+tIBXJmwCPbDA"
                + "WCCXYz25YD6tg5hhf89QHESTs8Bul2J3eK/+6B/wAz4M/AGbI3F9tohoTwAAAABJRU5ErkJggg==",
            IE = "no ctor")
    @HtmlUnitNYI(FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAcElEQVR42mNgYGD4T2UMBygcaoCRaeB/EJ4NxO+AOBCPopVA"
                + "/AWIA4gxEKjovw8QX8ajyAeIg4D4FzEGsgExOxB/waOIGcYmxkAPIPYG4nO4FGFj4zNwGRC/BGJ3cg1ESuX/qZ1T/v8fzSmj"
                + "Bg4HAwGGsHedCdecqgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAcElEQVR42mNgYGD4T2UMBygcaoCRaeB/EJ4NxO+AOBCPopVA"
                + "/AWIA4gxEKjovw8QX8ajyAeIg4D4FzEGsgExOxB/waOIGcYmxkAPIPYG4nO4FGFj4zNwGRC/BGJ3cg1ESuX/qZ1T/v8fzSmj"
                + "Bg4HAwGGsHedCdecqgAAAABJRU5ErkJggg==")
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkElEQVQ4T63UWw6AIAxE0WFzumZdnSkJBKSPoaE/hhAv56sF"
                + "h6cc7mEMPgCu5AMvgFv+HYMSk2hmJCbRKSjnjLLrtGBG2XVacFc56azgjnLSWUFWuei8IKNcdF4wUqq6KOgpVV0UtJSmjglq"
                + "SlPHBP9KV8cGR6WrY4NNKd+6Ubxh92Fba3WjnAhGnX7PCungBz2THhX/dvFdAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAkElEQVQ4T63UWw6AIAxE0WFzumZdnSkJBKSPoaE/hhAv56sF"
                + "h6cc7mEMPgCu5AMvgFv+HYMSk2hmJCbRKSjnjLLrtGBG2XVacFc56azgjnLSWUFWuei8IKNcdF4wUqq6KOgpVV0UtJSmjglq"
                + "SlPHBP9KV8cGR6WrY4NNKd+6Ubxh92Fba3WjnAhGnX7PCungBz2THhX/dvFdAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZElEQVQ4jdXOwQkAIQxE0SnJEuzcEizJPUjAg2aTSUD8MNfh"
                + "AS/VAQxybXdYAoc1U7nVRZRHHaNUdYzyV+dRmnQepVlnUbp0FqVbpykpnaakddKqDOmkVVkzDoGpTNFJBYm6u30fnn+BCcsO"
                + "DQAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZElEQVQ4jdXOwQkAIQxE0SnJEuzcEizJPUjAg2aTSUD8MNfh"
                + "AS/VAQxybXdYAoc1U7nVRZRHHaNUdYzyV+dRmnQepVlnUbp0FqVbpykpnaakddKqDOmkVVkzDoGpTNFJBYm6u30fnn+BCcsO"
                + "DQAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABvSURBVDhP"
                + "1c5BCoAwDETRHMkjeHOP4JE0XXwoGNMkLYgfZjeLJ7/q1F3FHbpHm846R7brzCpKU0cV5auOMkpXRxnlUEcRZUhHEWVYR54y"
                + "pSNPmdaRpSzpyFKWddQrp3TUK6d11JRLdNSUy3RfJnIDH55/ge/XbBAAAAAASUVORK5CYII=")
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABCklEQVQ4T7XUvytHURjH8df3P7FIJotQhJQyyCopUSxWShSD"
                + "UvIPGBiIJJtBWUwMyh+hKMI/oVPfW8ft/ji3rrOcep5P7+c8v05Hy6fTMk8MfMZwYoALLBVpY+Au9hOAXxjCWx1wEC8JwHUc"
                + "l+nyNXzEWAX0DrNVQfPALRwmvDIvOcdyMOaBoSmhOU1OqOUkXouAwfaAqQbENZxm+qI53MFBIvAKi7G2CDiKpwTgB0bwXgeM"
                + "077HTAn8T6pVKQffHjYwgSNM56A3mK8b7Ng/jj6cYLV7Z/4fDOCzCTDW9mQj0TWu4Cx1U8p0t5jDNRaabEqZdhPb6Md3G8Cw"
                + "Qb24rBunf/1g64In+X8BpH0mFSP8dhoAAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABCklEQVQ4T7XUvytHURjH8df3P7FIJotQhJQyyCopUSxWShSD"
                + "UvIPGBiIJJtBWUwMyh+hKMI/oVPfW8ft/ji3rrOcep5P7+c8v05Hy6fTMk8MfMZwYoALLBVpY+Au9hOAXxjCWx1wEC8JwHUc"
                + "l+nyNXzEWAX0DrNVQfPALRwmvDIvOcdyMOaBoSmhOU1OqOUkXouAwfaAqQbENZxm+qI53MFBIvAKi7G2CDiKpwTgB0bwXgeM"
                + "077HTAn8T6pVKQffHjYwgSNM56A3mK8b7Ng/jj6cYLV7Z/4fDOCzCTDW9mQj0TWu4Cx1U8p0t5jDNRaabEqZdhPb6Md3G8Cw"
                + "Qb24rBunf/1g64In+X8BpH0mFSP8dhoAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAeUlEQVQ4jdWSwQ2AMAwDPUJGZZOOwgiMwgiMAK+KCBE4Iz61"
                + "1K+bu0QaKaukHb6ZFDZYtkkKUhiwcCJlPW/YCDWHYuOPKLbl1Nk2cupgI6cUG5+PdGIvD4XW+bQ0wZ1T+3wiTXB1aqFW5Z9R"
                + "q3RsG7VKdvpLstPBcwARMoH8labnlgAAAABJRU5ErkJggg==",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAeUlEQVQ4jdWSwQ2AMAwDPUJGZZOOwgiMwgiMAK+KCBE4Iz61"
                + "1K+bu0QaKaukHb6ZFDZYtkkKUhiwcCJlPW/YCDWHYuOPKLbl1Nk2cupgI6cUG5+PdGIvD4XW+bQ0wZ1T+3wiTXB1aqFW5Z9R"
                + "q3RsG7VKdvpLstPBcwARMoH8labnlgAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACUSURBVDhP"
                + "1ZLBCYAwDEULLuAIHl3DjdzEURzBURzBqzf9gQZKSMkPnvzwKIi2fd+UX+UED8kOwmzA+9hygRGEkZe8DSwroBNpU6ptWG1L"
                + "9yBWuyXsNPO3hbDTjDbVKatNj49EtY+6eqTGR7T1Bl6n6fGRjfQGttOUqhfbaUq1F9UOVYe6RpnADBZwy4OvaTv9dUp5Afx3"
                + "gfLtY6izAAAAAElFTkSuQmCC")
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA6UlEQVQ4T62U0Q3CMAxEL/NUFUPwyQwswBIwTFfgC4boHgwR"
                + "dCGNjOtgV9Q/lZL0cu/iJGHnSjvrQQrOAA7BDSYAZ2utFLwCuAUEXwAGAPyuSgrSHV16RWd0aJbO0MPuoi7qWrBhe8rK3pRq"
                + "plqwYUcDrVkOqWZqtU0xFw2Up51EppbgFuyG2suQ41Hs0j4L6i9BzhXsI3B/AqdOh3yheoJs8AsdZOBh3KAVqidI7JENnAF9"
                + "4CaqJ9gos8i0DpqoYUEuzDVTOl4aOHr1zHX582iUTPWp6h9C72HFHmUD/+XQe37kfMjhFsE3a3YyFbFVojMAAAAASUVORK5C"
                + "YII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA6UlEQVQ4T62U0Q3CMAxEL/NUFUPwyQwswBIwTFfgC4boHgwR"
                + "dCGNjOtgV9Q/lZL0cu/iJGHnSjvrQQrOAA7BDSYAZ2utFLwCuAUEXwAGAPyuSgrSHV16RWd0aJbO0MPuoi7qWrBhe8rK3pRq"
                + "plqwYUcDrVkOqWZqtU0xFw2Up51EppbgFuyG2suQ41Hs0j4L6i9BzhXsI3B/AqdOh3yheoJs8AsdZOBh3KAVqidI7JENnAF9"
                + "4CaqJ9gos8i0DpqoYUEuzDVTOl4aOHr1zHX582iUTPWp6h9C72HFHmUD/+XQe37kfMjhFsE3a3YyFbFVojMAAAAASUVORK5C"
                + "YII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAh0lEQVQ4jdXQyw2AIBBF0VvC7GzTTijFjf1MCZQwLvzEaAhP"
                + "cKEvYXvhAH+aAyGeSQkmMZYBU4ImBkcltq/GlqjnHWyHCP0ULzrYSY/lqPypA2F6sPqnT9jSn6rsKvXGHmDuoV7ZGbAAb6We"
                + "Z2wvCEg91NtifWUztRT1VmopmLqpl6C9Qv3EFlkx1KTcSB3bAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAh0lEQVQ4jdXQyw2AIBBF0VvC7GzTTijFjf1MCZQwLvzEaAhP"
                + "cKEvYXvhAH+aAyGeSQkmMZYBU4ImBkcltq/GlqjnHWyHCP0ULzrYSY/lqPypA2F6sPqnT9jSn6rsKvXGHmDuoV7ZGbAAb6We"
                + "Z2wvCEg91NtifWUztRT1VmopmLqpl6C9Qv3EFlkx1KTcSB3bAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAACjSURBVDhP"
                + "1ZLBDYAgDEU7gifndBNG8eI+jMAG6i8JpKkNFPWgL/mBA/36CPQrInI4syJdAmIN6yRkQrrwIatAZ0Hc9LRdqpKqjeYDG1f2"
                + "xoeqNprNYR2UJazNO83aOGEW6KCwe6du7ZaqxKXtUZVk7Zlo00UlHlUJa+cHjMFolA0/H1bJf4DhoMqGVC/wsCocUjUp2ndU"
                + "TVj7saqEi15R/QBEJzGv1JNAAQutAAAAAElFTkSuQmCC")
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
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAdUlEQVQ4T9WTwQ7AUAQE15/7cz2UNjSaEg59xyeZjAVh+NEw"
                + "D38EMsTFwLUuni1vAYX9uAjfTFPDOaCKCc4sKUQK0OtmpMVx4J2g+Kl3DReBftoWhf3G6ZdPbw1IYU+hF9U2HANeicYT1ULZ"
                + "cA9o5GDaN0yAB17KJBFC+xNmAAAAAElFTkSuQmCC",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAdUlEQVQ4T9WTwQ7AUAQE15/7cz2UNjSaEg59xyeZjAVh+NEw"
                + "D38EMsTFwLUuni1vAYX9uAjfTFPDOaCKCc4sKUQK0OtmpMVx4J2g+Kl3DReBftoWhf3G6ZdPbw1IYU+hF9U2HANeicYT1ULZ"
                + "cA9o5GDaN0yAB17KJBFC+xNmAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAARUlEQVQ4jWNgGAXUBw0M/1HwoDXwPwMqHkQGQgHCIHSzB4uB"
                + "SEYPegPRjKdVZFE/9qmVAahuIBygG0ixwVQ3EJfB1DYQACh5i4ny3fVlAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAARUlEQVQ4jWNgGAXUBw0M/1HwoDXwPwMqHkQGQgHCIHSzB4uB"
                + "SEYPegPRjKdVZFE/9qmVAahuIBygG0ixwVQ3EJfB1DYQACh5i4ny3fVlAAAAAElFTkSuQmCC",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABpSURBVDhP"
                + "7ZPRCsAgCEV1X+6fO2ladGkxWDQfdl7MEDmI0s9r2GNDSP11IYOaCYfHZdwaqpSsYoWPTDcYOqZZTBlGaj9T032GDQXFfIY9"
                + "MdvAGnQ98hgy7Glc1PeGFbx5J5FhAKaLDYlOKXoSF+jZIBEAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAjElEQVQ4je3SMQrCQBBA0dckZltLrxEkVxEUUudiqaw8l5VW"
                + "wZXYRFSI4mrKPJj2M8Muryo0w1T+EAJtxmXDcccpIy7ZI6TXaEtiT/885SOapMpHYvfJiRLPb7ac3wVrutQNvwk2KcHJTxZo"
                + "1yOxkrjikBQbFIE2J9Z0Nd2C6/DCxS/Bu8k+9mw2++QGj8thmtPon2MAAAAASUVORK5CYII=",
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
