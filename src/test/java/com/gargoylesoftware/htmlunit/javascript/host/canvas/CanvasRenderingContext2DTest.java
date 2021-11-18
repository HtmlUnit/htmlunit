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
    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAutJREFUO"
                    + "E99k19sU1Ucxz/3du1du94tLcE1IjIUHlASRcEOtnVccWyIJWRmuhD02cBQlMTnvfsiIUQy9uIDCf+hWYFN3Q0mBAIJSCQy"
                    + "yIgORMtGHe1u71273T/m3qYN2wPn5OSc3znn90nO+X2/AsDF8XGp5vGT/QFJahBFEVEE0T14vtkwndfuf9Sp/Pj8tuAGF0Z"
                    + "GExveXferaVoIgoAggOD28qI8Ow6iKHD1xs3Dya4P+ioQD3AilVbaNzar86aF6AEqo5xcGQG/D8uyuHzlxnfdyc5v3VwPcC"
                    + "Y1rLRu2qDOmyaiIHoJmYJJtlS+EpFgZURC1wsYuo7P53P+fPRwfdeWLbeqgLaW91T3CaYNV/8tUQzIyLVBHBxkDMyiwVuxE"
                    + "HUBHwIOw+qVZG/39nQVkHABls21v3UyVpBwnUy0XiImWQTMWXIFg4fPSux8cwn+mhpSl0YXAVrj6jNjjsHbOfyhMCuW1LNU"
                    + "riXeKDKllZjRNCayM7yzPMrrS8OkLv6yENDeGlfHJgucvJcnFAoRDtaxIiLR0RQk7LO5l8lx7PcpNq+M0vVGI+cv/LwQsLk"
                    + "trv6R0bj56D86VkcY+Qf+0mws2+bjVUG0gs7315/wdUsT29bGOJdeDEg0q5pR4sDQGH3rGjhyd47peQHHceh6VWIiq3F7qs"
                    + "jgJ2/zWmMDZxcA0sOK0tKszhaLnLo+jjpR4Lenc95ngYNgWZ46e9ZEOLB1LXK9TOrST8meHdUqpBWlvU21TItsNsvZOxlOj"
                    + "+WYnjWxbYew5KN7TZTP17/CspdjSAGJ80MjyZ5KGY+dSCkfdiqqKxvbtjEMg+kZjQdPdWwHVr0kE4vKyLJMwO9HFH0cP51K"
                    + "7u7tLuvg0A9H3/9s16ejrgkqUva07jgIokDZYGJVpa4nDh4+suObL/cOeYB4IrF63xd7ByORSF3Vad5J2ZMLnClAbiY/u7/"
                    + "vqz2Tk4/veNf6+/tr8vm8f7GDXxTrum4ODAzM/w8TKTogggbXQgAAAABJRU5ErkJggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAA1JREFUGFdj+M/A8B8ABQAB/6Zcm10A"
                + "AAAASUVORK5CYII=",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAA1JREFUGFdj+M/A8B8ABQAB/6Zcm10A"
                + "AAAASUVORK5CYII=",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAABdJREFUGFdj/M/A8J+RgYGRAQrgDOI"
                + "FAIPmAgWeg1g2AAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAABpJREFUGFdjZEADjFgF/jMw/GdkYABL"
                + "YqgAADfmAgXboMAzAAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAAC1JREFUGFc1yNENACAQwlAYxUllU0ep4aL9"
                + "ap6RMEv4yJKdhJ2oDXQABtqHt76KMBEFpp4gUgAAAABJRU5ErkJggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAACpJREFUGFctyLERACAMxDD/KDTMGbbk0jC"
                + "KKUClwlQ7RJANwaE5b1YV4buKVA5Xbm45TAAAAABJRU5ErkJggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAABdJREFUGFdj/M/A8J+RgYGRAQrgDOI"
                + "FAIPmAgWeg1g2AAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAABpJREFUGFdjZEADjFgF/jMw/GdkYABL"
                + "YqgAADfmAgXboMAzAAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAACVJREFUGFdj/H+D4T+jBgMjAxQw/mdg+M/I"
                + "gFfg////jIyMcC0AnzULBWgjtygAAAAASUVORK5CYII=",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAAXNSR0IArs4c6QAAABZJREFUGFdj/M/A8J8BCTAic0BswgIAg"
                + "2sCAzQ3b+cAAAAASUVORK5CYII=",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAACpJREFUGFctyLERACAMxDD/KDTMGbbk0jC"
                + "KKUClwlQ7RJANwaE5b1YV4buKVA5Xbm45TAAAAABJRU5ErkJggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAI9JREFUKFONkMEJQkEMRCcliA2sF7GCfzU"
                + "F2cOSHixo7GQbkH9Tb5Fslo/CP2xYCLO8DJMIJks6d3bXF1Gf1qUda++h9U0IIB30eAuA27C/jx56AeQyQPjJlR9UW9OxHtLRVs"
                + "T/n2MrBQ/VDlxJlNa29Bv4u4+7Bx0hcwoRUDLjHkhSzQwkk528TuScq2nwC/OHMAtutSHUAAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAFFJREFUKFNjZCASMILUNQg3/K+PbGBoXN7"
                + "AAKIZpzCAxZEBRGGDwH/7AwYMBx0uMNQ3fACpwq4QpvP/////GxsbGRoaGvArxOdcDJ24FA8FhQDPFBULZWbihAAAAABJRU5Erk"
                + "Jggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAAXNSR0IArs4c6QAAADhJREFUGFcBLQDS/wEAJv//QPbAAAFAHL/"
                + "/MAfQAAG/CUD/ECfwAAG/QAj/EBj+AAFAvxn/MN76AI+DEbp3TIWcAAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAFpJREFUKFO10EEKgDAMRNE/R+mm9JbGW4ob"
                + "jzJiUFetdGM2A+GRwIjJ0aTjD1ht70LVZNL/IoPZgAZXqg0gLrYO5ELm18U1giUiCxjCpx7bTih1m3iXF7xNF57OsB8LLeYw9AAA"
                + "AABJRU5ErkJggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAFlJREFUKFPFzkEKgDAMRNE/R+mm9JbGW4ob"
                + "jzJiUChixZ0hZDaPJOJe1fYqVE0mR5+jtwazAA2OVBtAXGxtyIXMt41zBFNEHhrC6w3bTig9/9jD3qT+Uj/CHbu+Hws0CMhTAAAA"
                + "AElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAFxJREFUKFOl0EEKgDAMRNE/R+mm9JbGW4ob"
                + "jzJiRSjUSsFsZvMIk4jJ0aSjh9n2LpRNTW7TQYPZgAJXqgwgTrYO5ETNr41rBEtErT+Ez3G2XaH03rGFrfnxnsFjT80oHwvhRbeC"
                + "AAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAItJREFUKFNjPBXA/p+BgYFhWgQfiGJYEPGa"
                + "EcxAA4zrA0zACh8ovGAQ+PCBIXHBF+wKFyQkXGHl4OC2v3ZNVPrtW27Gq1dxKIyP1zaNiDBVkZDIYmNlNWXU0cGuEGTt////HRgY"
                + "GOofPHjgoKioSFjhgQMHHBwdHXErxOZLDF8TowikBqs1WMORWBMBFMAnC7I7xJcAAAAASUVORK5CYII=",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAFxJREFUKFNjZCASMBKpjmHoKlT7/z/h0wK"
                + "G+reNDIq/H4D9gdUz2WJT/ptLnWTwUtrGILLuLW6F+x04/kuo/mVQMPzDwJn1H7dC0Vdy/0PW/GQomPCNQf3WZ7BCAChZHAtgVD"
                + "PiAAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAABdJREFUKFNjZCASMBKpjmFUId6QIjp4"
                + "AAppAAuXjCs4AAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAEhJREFUKFNjnK+Z8J+BgYEh8foCRhCNCzDO"
                + "r+aBKGz9gl/hAQeH/fYMDA6MBw7gVwgybf78+f8TExMJK8TnNpgcXlOQDRgKCgFraBALDri0wwAAAABJRU5ErkJggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAAXNSR0IArs4c6QAAAHBJREFUKFOd0LENglAUBdDzSmLNCO5g"
                + "4QTG1jWo7JjCFehchgSdQxcg5BsSKPQHQ3j1efcmN6y8WOlkMLGfnovgMQd9wcQJFUo0wW0JvrDDgGdwyGDiiPuU1qPDOXiP"
                + "+Le6xgUtrjPK4L8Fts+zlPoB1NMSC7MwsngAAAAASUVORK5CYII=",
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAL1JREFUOE9jZEAFjgwMDOZQoZMMDAz7"
                + "0eQJchmhKnhFGBi2CDAw6AcwMHCDxDYwMHz9wMBw8Q0Dgw8DA8NngiZBFYANFGFgOJjAwGDVzcDAgqyxlIHhzwIGhmNvGBjs"
                + "STHQUYWBYf1tBgZ+bJpUGRg+3mFgCCTW+yAXVpQwMDSjuw5mOMiVPQwMtQwMDB3EuJImBlLdy1SPFFDQUDfZIAU21RI2MRFI"
                + "lBpYTiFKMTGKRg0kJpTwqxkNQ8rDEABatjIVyjXhJwAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAAXNSR0IArs4c6QAAAVhJREFUWEft1L1KHVEUxfHffQTJC4iN"
                + "dkJarQSNCDamsLixVrQK2IlVsBOEFIG0khRJYSlYpLS11s4XsLaVLXNhMuj9gH212VPNnDN7nbX/Z53T8/8zjw2ctYb7zftv"
                + "rOIGD5267ucH7OMUe7jEXfNTzJ3g6BWddm34eYza3gRGL3CIH1M22rb0ddDkJEZD4Bd+4i/WsdaoHuAYn7CM2zGJhsYmthud"
                + "du01/uEK/ZeM/sFih/QXtIkuYAfR8cfGZEQkxlca6uNsfRidwzcsDaudhGjX6Cwit4MFYrFBxs8nIHqPoDe09i2MbrWoxUa1"
                + "G4tdmIrRuA2C5HfMjEk0zEXdbhOn58w1B3KU0Ti8nyPDXaIjbp33my6j2eyLaBHNJpCtVxktotkEsvUqo0U0m0C2XmW0iGYT"
                + "yNarjBbRbALZepXRIppNIFuvMlpEswlk61VGi2g2gWy9J1+DY2XynG9XAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAqElEQVRYhe2SwQ3DIAxF/yzMwSCeJKN0lg6TXeihRHKNacIh"
                + "6qHvSUgI8OclWPqkSnqGta0PSXroOubOn+We1Q98C8z27hSNXsPCLHCX1Prlh0Tro7q5LYpa3zvqt6R+z0RbMuIfNVfs59WF"
                + "r4pKUgm5vn4QvfL0MSQT8mfKJNc09uJPRaX3y5QgVO8QLRp7dEU0tlXWizNR/4EAAAAAAAAAAAAAAP/AC/GNUlQypcRWAAAA"
                + "AElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAAAqElEQVRYhe2SwQ3DIAxF/yzMw"
                + "SCeJKN0lg6TXeihRHKNacIh6qHvSUgI8OclWPqkSnqGta0PSXroOubOn+We1Q98C8z27hSNXsPCLHCX1Prlh0Tro7q5L"
                + "Ypa3zvqt6R+z0RbMuIfNVfs59WFr4pKUgm5vn4QvfL0MSQT8mfKJNc09uJPRaX3y5QgVO8QLRp7dEU0tlXWizNR/4EAA"
                + "AAAAAAAAAAAAP/AC/GNUlQypcRWAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAADJJREFUOE9jZKAyYISa10AlcxtobiC5"
                + "LoXpw3Dh8DbwPwMDQyPNI4WS1IPiQkoMQtELADW1DgtGULOnAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAAChJREFUOE9jZKAyYKSyeQzIBv6n0HCw"
                + "WaMGUhSKtA9DipwH00z1dAgAPKcGC8XskzoAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAACxJREFUOE9jZICA/1CaUoqRcdTA0TAk"
                + "OQRGkw3JQYahYTQMR8OQjBCgfrIBABkEFBUHz6OrAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAADdJREFUOE9jZKAyYKSyeQzIBv6n0HCw"
                + "WaMGUhSKtA9DipwH00zTdDjqQvJCYDRSyAs3ZF2DPwwBbfkGFYXESWwAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAANdJREFUOE/d1K1OA0EQAOCvOF6Al6gA"
                + "g8I0oRgwCESDqW7SBA+a1uFwFSRVVYCoBIWqxFVVVuF4AMxcctlwf80Z2GSTTWb225ndZDtaHp2WPX8TvMBy16tIWz7GCq+Y"
                + "xLqRnYLPuMwJs4A3ddU8eF7S6jTg7yo4D37gpGTDV6APZWgGDvFUdXrE17jH/Lf8DNzDbcz9mvA7TtPc9FEOAr2pgfbxVgVm"
                + "8W7A1wXwAoOylosK6gV8liQc4nMXMNtzhTsc4RHjogqafg4jvGDbFlj5Vk0r/AfgD1+PHBW7Um+dAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAADdJREFUOE9jZKAyYKSyeQzIBv6n0HCw"
                + "WaMGUhSKtA9DipwH00zTdDjqQvJCYDRSyAs3ZF2DPwwBbfkGFYXESWwAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAADFJREFUOE9jZKAyYKSyeQyjBlIeorjC"
                + "8D+5Ro8aCA+5wR+G5EbyaNYjO+QQGkdg8QUAebwGD4ULeHoAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAMxJREFUOE/t1LEJAjEUxvH/gYKVjStY"
                + "WYlOYOMGCnYOYeEKYu8INo4gOICgnaWCYCEIghvIgzx4hODlTBrBQLjj8vJ7+VJcQeZRZPb4g+k3+lt3OAIOwCUluI18AjrA"
                + "w8FH95Qm19gmCjaB14dNd4Nro1uoXsEBsIs9hasT0KbYSzoFZ8CyImjLz0BbPii4BiYJ4AYYW3AF9IA+UPsCngMLC6ohJxZU"
                + "cXmXWTaGwDYEhjbWvQbSrOsVtoBnLBhq0vCSTG3EsjiV1rP/HN50hx8VahodiQAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAALZJREFUOE/tkjEKAjEQRd8ewXoLK23s"
                + "1kZ7j+IRbG3st/M6tlYq2CxYCl7CTgZ2liEMMm5WsDCQIiR5b+YnBQOPYmAef2B+ol/LcAzc8+uje+U1sAfOwMXM66cSbbkE"
                + "Hs7lZwu3orcSm+ERWAYqUol0oqJOYoFbYBcAekdqYCMbFjgHTj2BDTBLgbK+AdOeUAE23j8cAVUyIxJpuY5+bJVILCqbJJ0c"
                + "gFUU6KXgSRY5QDfq3we+AOdFHSeUoHcMAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAAC1JREFUOE9jZKAyYKSyeQzIBv6n0HCw"
                + "WXQxEFcwEPIBTheOIAMpjGSIdqqnQwA/UgoLxnfNlgAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAF1JREFUOE9j/O/C8J+BioBx1ECKQ3Mk"
                + "hyHjHgZGWABSkpTgYYhsILaYIdYSog0k1hKUWCbkSnxpCuaDIWYgupdICQKsXiYm3+GzBGQoVbIezBKqGYjsM6q4kKYGAgB9"
                + "kkg955Wz+gAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAAXNSR0IArs4c6QAAAHVJREFUOE9jZKAyYEQyL5GBgWEXAwPD"
                + "U0rsgBmozMDA0MLAwODGwMBwF2owyPBDpBqO7EKYXjuowSDDQRaBDIZhgq7HZiCyo6SRDHdnYGC4Q8j1hAxE9zHM9SDDlaCG"
                + "10CDCayWVAOxuX4+siAlBmKNr8FvIAB+fhILDX78/QAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAONJREFUOE+t0z1KQ0EQAOAvpek8QDxE"
                + "CHgBMR4gjRBE0D7WYiH+JGBnZSM2go2djUo8iZUW1jaKhaCysguvkPB8mW0Gdplvh2GmJfi0gj0F3M9wiY3/KeAFvrHVWMqJ"
                + "VfAL21HgAT5xGAUe4QPHUWCC3jGOAi+xgEEU2MUN1vGKlxz/7VcHe4RVLKKDN9zhPsda+KxNSVWvoY9lXOMKt7PkuqvXxiZ2"
                + "8IhTTP+C64LV3LRNacxSzyd4qj42AUv+CTawh/NyOQ+YjB7Ocht28TwvWApLCzHEUhSY4BU8RIK/1YaDPzgGJBUK5TlaAAAA"
                + "AElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAOJJREFUOE/N1K9LhEEQBuDn/gSxGQQR"
                + "i0Ewm0TQIoegYLIJIjabSc4mBsHoxWuHQTCIUSxi0KrFcMFsMh4y4gfLwcn9mODClIX3mQmzW5N8aoV3iHe8/tZIrUrwHHOY"
                + "xwQecY87vAyql2CZmcQSVrCOT7Rwia+/8H5gb2YZu1jDKc76oYOCVX4Bx5jCAZ574WHBKr+HC2zjukRHBcNYxQ02cFuh44Bh"
                + "1NHEIj7iYlwwjBPMYCcLDKeDLTxlTBjgEaaxnwXO4iHWKQuMKd+wmQleoZ0JxufSyQQb6GaCP4/l/4Pfi58fhzJ0tsYAAAAA"
                + "SUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAADRJREFUOE9jZKAyYKSyeQyjBlIeokMr"
                + "DP9T6GGwb5G9PAINpDAIIdqHVrIZ9TJ5IUD1WAYAYokEFfnLcNAAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAL9JREFUOE/tkr0NwkAMhb+0SNRQsAM/"
                + "PWILaGGMlClAbAEFDYzBAEAmgBK2QJZO4ohs4qCjw80V9/T5PdsZiStLzOPnwBYwAU7A4xv3msML0AeuAXwOr6uJBlwBueHu"
                + "FsGXmkYDjoGjI+4AKKs6ayl3oFMDlRRrL3ADzGuAkkIW+FaWwymwd8TuVq/BAraBHTACeh/AC2Ab/3sOW4DDAJcGcZMDMGsK"
                + "1AzGTYoUQHMKnsiO3bwkf2Cjcani5DN8AnFEGRVy9qdcAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAL9JREFUOE/tkr0NwkAMhb+0SNRQsAM/"
                + "PWILaGGMlClAbAEFDYzBAEAmgBK2QJZO4ohs4qCjw80V9/T5PdsZiStLzOPnwBYwAU7A4xv3msML0AeuAXwOr6uJBlwBueHu"
                + "FsGXmkYDjoGjI+4AKKs6ayl3oFMDlRRrL3ADzGuAkkIW+FaWwymwd8TuVq/BAraBHTACeh/AC2Ab/3sOW4DDAJcGcZMDMGsK"
                + "1AzGTYoUQHMKnsiO3bwkf2Cjcani5DN8AnFEGRVy9qdcAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAEFJREFUOE9jZKAyYKSyeQyjBlIeoiM4"
                + "DBUYGBiSSAjBOlxqkcOwiQQD5zEwMDzApn4ERwoJwYdf6WgYUh6UVA9DAJn9AxWRWUx3AAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAGZJREFUOE9jZKAyYKSyeQyjBlIeoqNh"
                + "CA5DFgYGhj/khiauMHzAwMBwm4GB4QISvkqMJfgiRZuBgcEADSNbAGJjWEJqLKNbosrAwKCA7HJSDcTma5Qwp4aBKJaMGkhM"
                + "SsOvhuphCABLbg0VZ88YdgAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAADNJREFUOE9jZKAyYKSyeQyjBlIeoiM8"
                + "DBsoDEGwfuQwpLqBFDoQon2Ex/JoGJIXAlRPNgCWIAIVwMrcFwAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAALBJREFUOE/t0rEJAkEQheH/crEGEUwv"
                + "swkDS7AJDUTjM7cJ0RpsQixBLMBMBAMZeCMbnMvuamBwE8/7GB5TATVw5kdTAXvgBmyAy7eugT1gBSyBRvC9FDbQZwCsganQ"
                + "bQkagp4f6+KR4F0O3AZ6fiL4qSqOKXAM9PxMVdgnWMenGJwCen6uiw+q4toG54CW7wtdBB/xCOFc0LNDwdarXfyeUvBjjR2Y"
                + "8rrxna7DP+zwBU1tGRWRQb0vAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAADdJREFUOE9jZKAyYKSyeQzIBv6n0HCw"
                + "WaMGUhSKtA9DipwH00zTdDjqQvJCYDRSyAs3ZF2DPwwBbfkGFYXESWwAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAPdJREFUOE/F08EqBXEUB+DvPoTyAJLk"
                + "bpUl6i4slDzBZWMjFrK1F4ubjRJK3e7idssL2LCwshBJnoCsPAEdjdJkzH9uE7OcOt+cM79zGmp+GjV7/gTc4PNDbTzhAgO8"
                + "pkyT7zCwOexl2AxWMJm9OyxD8+AmbnCVK2ziGI/YxnMRnAdv0cJLQcEBpjCbCvbRxXnZaKngGhawWBc4gmss4W4Y9Kc9jC4j"
                + "2em6wHDO8Ib1qmjRpYyih0vsVEF/O70xHOG+Sqdltxyd7mICqylBlYFf00ZQW3jASbYJ41lw+99/SSoYNbFSy5hHYKd4R2dY"
                + "MCmbKh3+D/gBF7klFTbdOAAAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAOdJREFUOE/NlL0OAVEQRs+UCrVai8Qb"
                + "aK0HUCslOk+iU6hovQC1JyBCq9cqlCN3s5K191eyEVPPnDvfzDdXqDmkZh6/AaqSAU3gKsLlGxVWh6rsgWEJcgIWImxSwB/A"
                + "orOdp9A8NBPhFgJXgWNgGyh4ApkIB19OFdgDzhFpdxFaSUCTpMoR6EegI5F81la4ljIB1hHgSoRpErDo0izGWMcXXtlOY6vS"
                + "htx/jQDUKdt7KaoMIJ+TD+qUHTy9CNQpO3rLhfylZ6aW7CjwPUNVzPbnFUtZspOBJXAX6AAPlxe/BsY+iP8HvgC+wjsVfIfH"
                + "zwAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAMFJREFUOE/t1CFuAkEUxvHfVqEQpNyA"
                + "pI4z1IGoLgpNSYPEoVBcgBCSOlQ5AZIz4FAcoE0FqqKCbLIkZLuz2U1GMm4y3/u/+b68vETkk0TmuQMLE23hp2rWZRl2MMUJ"
                + "ixjAD/zhrSos1YV+2MMEL3VgZcAZHjCPBXxHF6MAcI0Dlvn3kOUBxngOAPdY4bMqMNXtcMZrrmiLJvpFzcrGpo0NGjhmxU/4"
                + "xRBfdYFXfWr/Mbt8F9m8Bd+XQ90x/q+PnuEFEm4WFfcJX5MAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAKRJREFUOE/t0i0KAlEUhuFngmCy6AJs"
                + "NpOLsFo0uRYn20Vcgha34AIEwSYWu2YxGOReJgkDM85M8+RzXr6fkyBV4yQZsC5o+gdWbuefYeUINZvhDL1M5APbHwRHhUsM"
                + "0cYlgwzwwhz3EuAIvOKE6dfhDh2MywJv6OccHbAuYX8RFB4xygFucMaqoMpouXZgnZabKSW8zQQthP8L08UbezwL5hfXPmUx"
                + "MRR+E/5OAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAHdJREFUOE/t1LENwjAQQNHnkoaWhp4u"
                + "FUNkCVo2ilBGSKZgBtaAAVIit5aDrOAihd376fwtXVD5hMqeBmaLXtHhgA+mX91LGt5xwhkXLLjhnYNLwPTejCP6WmB0nnjk"
                + "nr9lwgiOeGFIp9wKrv5LA/9fFa3hDht+AZ2HChWoIysFAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAJpJREFUOE/t1KENAkEQRuHvHAoFNeCo"
                + "AUkFoNCEXD2EoFFQAZIacNQACoUkmxyCy+4mXDaoWzuZl33/TKZS+FWFeXpgNNElRk3lgWMu91yGYxwwwK2BTPDCCvcYOAc8"
                + "44lFq/GEIea/AIPmBrOE3gW7mH7qhzWmWCeAe1yxbdf/BiyuHEyKDiUAi6/NJ+9ii93pEPXXplNsX01vh5geFQJIQ80AAAAA"
                + "SUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAHZJREFUOE/tlLENgDAMBM8TwVQUMARh"
                + "CCiYCiYyCgVCKE4RDFXS+965vCI4H3HmUYHvjeYdBnqUDqE5o5QdYSWwWNE2MMJgNgYHC2oDR7Zrsyc1bjrRpsJyG2rWaEhX"
                + "7kfgB1d2fpQo0LU2hR2vn0OhuNuYu8MD87geFZXKqbQAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAQhJREFUOE/N1EtKA0EUheGvxYUoug4F"
                + "0W0IKuguTCnuwVY0iCNnLkDBx8QFOFIh2YMjQTFiV4e0bSfpNEG8UFCDUz+nbt1TiSlXMmWe4cB9yz6tYwlzmEUXtzgT3FeZ"
                + "qQYGJ9ga4/5YsFPW/AYGV1it2YprwVpR+xMYHGG7JqwvawuD2wyAIevV3YSwKE+saLmJ234FbWw0AsZHys4WgR3MNwR2BAtl"
                + "YK8hLB4L0VzR4Xs+a5NyL8xI7cb+F4EvWKxJe0PqQ+rAU/XYBKfYHAlMdPWk2drzOjopMWrfsaqqh8xRcD7uBuXBLkfuUuJQ"
                + "K0tPrRoWvee80Y+1KAXRH35fk1rL9f/f4ReiOzgV5zzx9AAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAALdJREFUOE/Fk7ENAjEMRd/NQnE7sAAL"
                + "0FLdBDAITEBFewuwADtQsAuydIeiEPu7iCBVpNjvfyc/A53X0JnHT4A34JV0vgEOZW3tcA9cgGsSOAFHYF7rWyM/gHNZ5MBN"
                + "/ARsI4d21ixsQJvC3qMol66oB1QuXcEoNl5TKBYBvcbwOlSw62Z1FfKn1AD1WBJoaVkhtv/KXR0nNXKZS9vLwGeABnouTkb1"
                + "JbPA3QK69wIqzuc86/B/wDfAoCMVPpEgJgAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAChJREFUOE9jZKAyYKSyeQyjBlIeoqNh"
                + "OBqGZITAaLIhI9DQtIzAMAQASMYAFTvklLAAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAChJREFUOE9jZKAyYKSyeQyjBlIeoqNh"
                + "OBqGZITAaLIhI9DQtIzAMAQASMYAFTvklLAAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAChJREFUOE9jZKAyYKSyeQyjBlIeoqNh"
                + "OBqGZITAaLIhI9DQtIzAMAQASMYAFTvklLAAAAAASUVORK5CYII=",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAK5JREFUOE/tk7ERgkAQRR+1GNADDdiA"
                + "qREVSCFSAZEpDdiAPRjYi7POrbMsB4ezhlzE7J//bvl8Kv58KsM7AnfHt7Mb8HL6ATjbmQJPQAc0zvAArmnWA4PTW+ACjDq3"
                + "G6pZRX9JSf8wLfBXgL9gBpSBfcVSBDl9sqEAdUt5luy+2aSMSvoMKL5nMtcLjVrVbYbql6rI8RXapOeAoarvwFB82WKHiftH"
                + "CUeY/ZdD1DfenCEVhbDKAwAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAGZJREFUOE9jZNjH/J8BGTj9ZUThE+Kg"
                + "6WekuoH7GFBd6MRAmgvR9TNS3UB4EFEallCDEBFAbQMp9TrMp3AXUt1AQsmNWHnSEjERpo4aSEQgEVAyEsIQozykNNiobSBG"
                + "eUixA9EKaAA0fyYV+TYpjQAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAGtJREFUOE9jZNjH/J+BgYFhnxOIRAAn"
                + "hr+MqCLE8RipbuA+BogLyXURursZqW4g3AZoWML5TuSGIcwEahsI8zrMfHLDFJ40qG4gcamMsCqyEi8+Y0cNJBzohFSMhiGh"
                + "ECIsT70whBYuVDcQAEALHZVK+V18AAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAF1JREFUOE/tkskJACAMBBPszMLTmSjR"
                + "jxBhPfap38AwDqtCfkrlWaodaJJqlvIMd84zZP4hHehwquES6Np+uG0aDOnAMCMbxpL3VoAb0oGHy8eGH4gK/IaoEL7TGzaf"
                + "lBRV53guuAAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAXxJREFUOE+t1L9Ll1EUx/GXSJN/QKaT"
                + "EDVF0Bi2SBiC0CIERQQu/mhSnMRBHBp1aDAhipBoaQyypkDUTRDEEJokEBcJlSAjlAPPlcvT89Wn9I733vM+vz7nNLng05Tx"
                + "7uFTiR93l/Chrt8E7MMobpcMv6Idt/CtDjSPcBnTeI+bGEQvAtqBcLp2FjQHhsEUvuMGmvEFG3iENixhBh8bgXPgJMYxi8WK"
                + "EsTdFrowh/j/10nAeOwuYEPFr5R+MrqKFQxjBJ+roAHswStcwz42C8L1igCipq9xFwvoL6cfwPD0Bm8LQEglTllCiT+G+3iB"
                + "J0VmJ74DuI0rWTRHZ3Uye/9ZSO2k+wGcx+P/BB7iKV4m+wBGQ/KO/UuEwXmHhxcF/I1neUDnTXkHE+WUz9OUA3TmI1klm7o1"
                + "jA6v4k6uiiph1wXGEDyoEnY4SKMXot6rocNdPG80esk+oANoPQUYaf4p1typyyExYq5j48RCjZQu4xd+oAXrxYpruL6OAXr5"
                + "VhWJ6S7SAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAK1JREFUOE9jZKAyYKSyeQw4DfwvMsme"
                + "4T9THOPbnGRSLMVj4OT/IIMY3+SS5ItRAxHB/19k2IchzIswT8OSC7o4rjQJUw9PNv9FJt9lYGBQItPAe4xvcpXB6RZmwH/h"
                + "KXMZGP8nkWXgf8Z5sByF5MJJ9gwMjAfIMpDhvwPjm7yDKC4Ecf6LTJ7FwMCQipzliAjD2YxvctPgDsEVyEMhHVK5+CKlDERW"
                + "S1JZR4wlAO50XRUscFdZAAAAAElFTkSuQmCC",
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAO1JREFUOE/t1K9KREEUx/HP1C2CCIIg"
                + "GxbBJhiFxSb6Dgb/sAbBIBpkLSLYfIbV4isYhX0Cg8lkMG/VYLgyu7M4XGG9XIz3lAPDme/85nfOTMAVNk1imHLtFNLOCI0x"
                + "zQ2wugOZh3vL3I2whTXsEh5yVDFp3gW20/phYJDXlIFfOEULN7gkxEPGkYBH6KGNE5wHPqY1ZeDTj6oigh8JryVgJ6oqWIgw"
                + "XM8CVlH4JzAN9s4cx5+sd1haZH/A/XvuT4/2KvNnPK/QumXjgOGIKGQc8coZsPtG/6V6T39XzvCwHrZ5es1vU2Ny/n1svgHo"
                + "7ESVtzssQAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAlklEQVQ4je2SsQnDMBBF30yezCOI1NlAC3gEdW5cuXYX0miB"
                + "4Am+GyWWDwXFxKSJDz7ow7/H3SEAB/RJ7gCBNUfUfwJ9AHUgJbU2JGgFg9bQtQaMWbveAdO7EUSbKUz4ah9ATQHYZb4K/GTC"
                + "KtABPVxucI/reXzAfFoPYYTp6R8w24wBjlMhsFeblbMbflcb+gk8gT8GLkqKbVBaXVuaAAAAAElFTkSuQmCC",
            FF78 = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAlklEQVQ4je2SsQnDMBBF30yezCOI1NlAC3gEdW5cuXYX0miB"
                + "4Am+GyWWDwXFxKSJDz7ow7/H3SEAB/RJ7gCBNUfUfwJ9AHUgJbU2JGgFg9bQtQaMWbveAdO7EUSbKUz4ah9ATQHYZb4K/GTC"
                + "KtABPVxucI/reXzAfFoPYYTp6R8w24wBjlMhsFeblbMbflcb+gk8gT8GLkqKbVBaXVuaAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAJBJREFUOE+t1FsOgCAMRNFhc7pmXZ0p"
                + "CQSkj6GhP4YQL+erBYenHO5hDD4AruQDL4Bb/h2DEpNoZiQm0Sko54yy67RgRtl1WnBXOems4I5y0llBVrnovCCjXHReMFKq"
                + "uijoKVVdFLSUpo4JakpTxwT/SlfHBkelq2ODTSnfulG8YfdhW2t1o5wIRp1+zwrp4Ac9kx4V/3bxXQAAAABJRU5ErkJggg==",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAQpJREFUOE+11L8rR1EYx/HX9z+xSCaL"
                + "UISUMsgqKVEsVkoUg1LyDxgYiCSbQVlMDMofoSjCP6FT31vH7f44t66znHqeT+/nPL9OR8un0zJPDHzGcGKACywVaWPgLvYT"
                + "gF8YwlsdcBAvCcB1HJfp8jV8xFgF9A6zVUHzwC0cJrwyLznHcjDmgaEpoTlNTqjlJF6LgMH2gKkGxDWcZvqiOdzBQSLwCoux"
                + "tgg4iqcE4AdG8F4HjNO+x0wJ/E+qVSkH3x42MIEjTOegN5ivG+zYP44+nGC1e2f+Hwzgswkw1vZkI9E1ruAsdVPKdLeYwzUW"
                + "mmxKmXYT2+jHdxvAsEG9uKwbp3/9YOuCJ/l/AaR9JhUj/HYaAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAOlJREFUOE+tlNENwjAMRC/zVBVD8MkM"
                + "LMASMExX4AuG6B4MEXQhjYzrYFfUP5WS9HLv4iRh50o760EKzgAOwQ0mAGdrrRS8ArgFBF8ABgD8rkoK0h1dekVndGiWztDD"
                + "7qIu6lqwYXvKyt6UaqZasGFHA61ZDqlmarVNMRcNlKedRKaW4BbshtrLkONR7NI+C+ovQc4V7CNwfwKnTod8oXqCbPALHWTg"
                + "YdygFaonSOyRDZwBfeAmqifYKLPItA6aqGFBLsw1UzpeGjh69cx1+fNolEz1qeofQu9hxR5lA//l0Ht+5HzI4RbBN2t2MhWx"
                + "VaIzAAAAAElFTkSuQmCC",
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAHVJREFUOE/Vk8EOwFAEBNef+3M9lDY0"
                + "mhIOfccnmYwFYfjRMA9/BDLExcC1Lp4tbwGF/bgI30xTwzmgignOLClECtDrZqTFceCdoPipdw0XgX7aFoX9xumXT28NSGFP"
                + "oRfVNhwDXonGE9VC2XAPaORg2jdMgAdeyiQRQvsTZgAAAABJRU5ErkJggg==",
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAAXNSR0IArs4c6QAAAL1JREFUOE9jZEAFjgwMDOZQoZMMDAz7"
                + "0eQJchmhKnhFGBi2CDAw6AcwMHCDxDYwMHz9wMBw8Q0Dgw8DA8NngiZBFYANFGFgOJjAwGDVzcDAgqyxlIHhzwIGhmNvGBjs"
                + "STHQUYWBYf1tBgZ+bJpUGRg+3mFgCCTW+yAXVpQwMDSjuw5mOMiVPQwMtQwMDB3EuJImBlLdy1SPFFDQUDfZIAU21RI2MRFI"
                + "lBpYTiFKMTGKRg0kJpTwqxkNQ8rDEABatjIVyjXhJwAAAABJRU5ErkJggg==",
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
