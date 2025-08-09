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
package org.htmlunit.javascript.host.canvas;

import java.io.InputStream;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * Unit tests for {@link CanvasRenderingContext2D}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class CanvasRenderingContext2DTest extends WebDriverTestCase {

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
            + "  } catch(e) { logEx(e); }\n"
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
    @Alerts({"addHitRegion", "drawCustomFocusRing", "drawSystemFocusRing", "removeHitRegion",
             "scrollPathIntoView", "36 methods"})
    public void methods() throws Exception {
        final String[] methods = {"addHitRegion", "arc", "arcTo", "beginPath", "bezierCurveTo", "clearRect", "clip",
            "closePath", "createImageData", "createLinearGradient", "createPattern", "createRadialGradient",
            "drawImage", "drawCustomFocusRing", "drawSystemFocusRing", "ellipse", "fill", "fillRect", "fillText",
            "getImageData", "getLineDash", "isPointInPath", "lineTo", "measureText", "moveTo", "putImageData",
            "quadraticCurveTo", "rect", "removeHitRegion", "restore", "rotate", "save", "scale", "scrollPathIntoView",
            "setLineDash", "setTransform", "stroke", "strokeRect", "strokeText", "transform", "translate" };
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
            + "  } catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    private void drawImage(final String png, final String canvasSetup, final String drawJS) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(png)) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok",
                    MimeType.IMAGE_PNG, Collections.emptyList());
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts(DEFAULT = "data:image/png;base64,"
                    + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAC8ElEQVR4AVyRy08TURTGvzsz7QgYKUawAkH64GHEhKCEEIO"
                    + "JLBDcsDHGjfIH6NLEdGsiPha4MXFLotHEBTEBhBJepQ+gMj5YSUC0vEuhUgpl2k6nnpmGgp6Zb87cc8/3u3PncqD4ND8vDo"
                    + "25H437/J0TUzOdHv9Mp+9/Ub3POdZB7f/cOiC9uNRQe+nis0qrxVFhKXfYy8sdtnKLw26xOuxWq6PCZnNU2K2Oxvra7t7B4"
                    + "VfHCTpgTz7gVVVFiqRlVU1DTWtSkaasCYxBEHhcbbh8v6fX+eIQogMECDTPSCBpmWF9T8HsloLvIQWBSFKv70R2EQxuoqba"
                    + "9nBwZKQOFDqAst7AMQ6KCriWZczu5yAqnMauYEI4ZcT0yj54MQ9m8zkUFRaynYhcrPmOADRijGFmLYZtMjDOCOMJAefzOZj"
                    + "zeJwUeYwu7oLnOYiiSN2ZOwugT0A0kcLUuoy1vSTiigIlmUJJHoc0J9A0A9QElnbioAEOIwugaQSjCRwkk2SMIxiJIbq/D6"
                    + "YqsJ1iyOFUfNmIYWErRn6tO4M4AlBN+9vWnCTu2jnk4gD+pTAejy9jOhDGSjiKr6sR0LEQIGPWnlmAVi0zifAEIght/4F/M"
                    + "Yi5tS3Mr2/jWyCEkbkgUok4qgtzQGtpXl0ZAA+9aODSaDLzeDm5Ad/CBn6uhrC4FsJrbwDO+TCuFYsozNWaGXgBemQAKQWg"
                    + "EzAajLhRfRYNJbk4I4JWlJGUZRhp/7cvmNBxpQTiCRGMLpAFFDpAllNUYhAEAUVFRbjXWIHuW9XoarWgq82Kd3dq8OB6FUq"
                    + "KzTAaDGCMIU7bIT90QDi8yRgDGMdgMAgwmfJhLStFS10l2uqrUGUpRUFBAZmNZOZIwObmOjmQAbx9/2a5r39gwuudlNwenz"
                    + "Th9koTHpJ3SnK5JyWXyyuNjbmlkVGXNDw6Ln3o+eh5/uTpb1BwJLQ1N/+SPk+1Dg8NNGXlpHdnf9Mwaei4Bvub/D5PS3v7z"
                    + "R+a9y8AAAD//4c5aowAAAAGSURBVAMAjetKO7d7dGIAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                    + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAC6UlEQVQ4T12TXUgUURTH//s57rqj7Yq5VKaWgpbRl7p+rl+"
                    + "ZWihi6VP1qqREJvTsey9FBIIS9CBEZbVpqJkTihgKpSSlsYWa2ppu6u7sjrs6M9ud2davGc69d+7c8+Occ/9HAfLY7XZqZm"
                    + "Hpdhili1QqyQYZpGn/43Kz38sv5D/Zva+QPt72DVrTz58a5HkBCoWCGEBGeU2m4BwIEK4CI2PjjyrKihpDEBnQ2d1bmJORx"
                    + "mwRgFIGhCzoHDKtRgVBEDA0PHavqqL0ruS7Dci1pBMATwBK2cHh4eH0B48YKSCBDF6vB5zXC7VaHfg5t5hWVmz9vA3Iy8xg"
                    + "pBR4ERj57YdPS4MO0yFAXhoceB+H02Y9wrUqggygf2i4oqbycvc2wCoBBBEf571wCDoYwmmYIiiYKQFafgPrHg5za35UnYy"
                    + "CRq1Gd//ADsBGapCdZWHWuE20T6xDozcgLioC0XQYLDFKLLN+uFkWs043zsWacDzagO537/dGkE8AU388eDbtgl6vh0EXjj"
                    + "iSd0k8iUYlYtqxjo4vyyhIMKHsRAy6+j4QQOlOCgXZFuarg8WnX39RkmRE3yIww4oQRBFXEnVgPV7cH11CU048ylPNeNPbv"
                    + "zeCgpxMhuX8aO6aQuPZSLR+28TqFikXuf+yoxQJn8XEsg/ttWdwLCYStt5dEUg1yCOADZ8Pz0ftYGY9GF/ZlItFFAQFuXtJ"
                    + "oTUpRjRfTAUdQcPWw+xNoTA3ixHINTqdTrycdODF1DpWN3iIYgAGSoXqFBNupB3B4UNmUFoKr3v6dlLo6LQVXiouZCTZiCR"
                    + "njuOw6mbxY8UL4o/EgzTMJqILmoZWoyHRqPD0la3iWk11sIgP2x4XXa+9OiA1QUjKstZJ/gqif6XUXJL9V6nUEw9a2yrvNN"
                    + "R3yQCr1ZpUV9/QfsBkDN/uNPlPsCf3d6bb7d5obrp1c2FhYVI+1tLSona5XJqgcyxiY3c37N71/Py8vJGcnMzX1dVt/QNcN"
                    + "Toga4nOWgAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                    + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAC6UlEQVQ4T12TXUgUURTH//s57rqj7Yq5VKaWgpbRl7p+rl+"
                    + "ZWihi6VP1qqREJvTsey9FBIIS9CBEZbVpqJkTihgKpSSlsYWa2ppu6u7sjrs6M9ud2davGc69d+7c8+Occ/9HAfLY7XZqZm"
                    + "Hpdhili1QqyQYZpGn/43Kz38sv5D/Zva+QPt72DVrTz58a5HkBCoWCGEBGeU2m4BwIEK4CI2PjjyrKihpDEBnQ2d1bmJORx"
                    + "mwRgFIGhCzoHDKtRgVBEDA0PHavqqL0ruS7Dci1pBMATwBK2cHh4eH0B48YKSCBDF6vB5zXC7VaHfg5t5hWVmz9vA3Iy8xg"
                    + "pBR4ERj57YdPS4MO0yFAXhoceB+H02Y9wrUqggygf2i4oqbycvc2wCoBBBEf571wCDoYwmmYIiiYKQFafgPrHg5za35UnYy"
                    + "CRq1Gd//ADsBGapCdZWHWuE20T6xDozcgLioC0XQYLDFKLLN+uFkWs043zsWacDzagO537/dGkE8AU388eDbtgl6vh0EXjj"
                    + "iSd0k8iUYlYtqxjo4vyyhIMKHsRAy6+j4QQOlOCgXZFuarg8WnX39RkmRE3yIww4oQRBFXEnVgPV7cH11CU048ylPNeNPbv"
                    + "zeCgpxMhuX8aO6aQuPZSLR+28TqFikXuf+yoxQJn8XEsg/ttWdwLCYStt5dEUg1yCOADZ8Pz0ftYGY9GF/ZlItFFAQFuXtJ"
                    + "oTUpRjRfTAUdQcPWw+xNoTA3ixHINTqdTrycdODF1DpWN3iIYgAGSoXqFBNupB3B4UNmUFoKr3v6dlLo6LQVXiouZCTZiCR"
                    + "njuOw6mbxY8UL4o/EgzTMJqILmoZWoyHRqPD0la3iWk11sIgP2x4XXa+9OiA1QUjKstZJ/gqif6XUXJL9V6nUEw9a2yrvNN"
                    + "R3yQCr1ZpUV9/QfsBkDN/uNPlPsCf3d6bb7d5obrp1c2FhYVI+1tLSona5XJqgcyxiY3c37N71/Py8vJGcnMzX1dVt/QNcN"
                    + "Toga4nOWgAAAABJRU5ErkJggg==")
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
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
            + "} catch(e) { logEx(e); }\n"
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
            getMockWebConnection().setResponse(URL_SECOND, directBytes, 200, "ok",
                    MimeType.IMAGE_PNG, Collections.emptyList());
            getMockWebConnection().setDefaultResponse("Test");
        }

        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
            + "} catch(e) { logEx(e); }\n"
            + "</script></body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR4AWL6z8DwHwAAAP//A3ONEwAAAAZJREFUAwAFCgIB"
                + "yRpMngAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQIW2P4z8DwHwAFAAH/F1FwBgAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQIW2P4z8DwHwAFAAH/F1FwBgAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR4AWL6z8DwHwAAAP//A3ONEwAAAAZJREFUAwAFCgIB"
                + "yRpMngAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQIW2P4z8DwHwAFAAH/F1FwBgAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQIW2P4z8DwHwAFAAH/F1FwBgAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAIklEQVR4AWSIsQ0AAAiDsP//rB1MlxIGghYsQQOWoNRHjQMA"
                + "AP//SMpI1gAAAAZJREFUAwCEdgIJz8+ExgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAF0lEQVQIW2P8z8Dwn5GBAYggAM4gXgAAg+YCBUgKw5EAAAAA"
                + "SUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAF0lEQVQIW2P8z8Dwn5GBAYggAM4gXgAAg+YCBUgKw5EAAAAA"
                + "SUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAIklEQVR4AWyKsQ0AAAiD0P9/1qZupoSJ0DxyGJC3+iiQmAUA"
                + "AP//C9I1QgAAAAZJREFUAwA4dgIJmGt15AAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGUlEQVQIW2NkQAOMWAX+MzD8B8qAJTFUAAA35gIFxQ+VugAA"
                + "AABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGUlEQVQIW2NkQAOMWAX+MzD8B8qAJTFUAAA35gIFxQ+VugAA"
                + "AABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAMElEQVR4ASSJgQkAMRDC4m3ym/qTdhR7WJAoZgIhX9rA/DbR"
                + "2fkytkV9+ky5kLSECwAA//99UEdwAAAABklEQVQDAKv1DwQf8pyHAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAL0lEQVQIWzXI0REAEBAD0Y1OVCqd6kQ4w/vaWQXS0lmaCCTb"
                + "GTbljoocd5Q/XmoDijQRBTzmAeYAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAL0lEQVQIWzXI0REAEBAD0Y1OVCqd6kQ4w/vaWQXS0lmaCCTb"
                + "GTbljoocd5Q/XmoDijQRBTzmAeYAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAOUlEQVR4AQAtANL/AgAm////AAD/Av/aAQAA2AAAAgH/IQAA"
                + "KAAAAv8B3wCBgIAAAgEAAAGAgIABAAAA//+kky/SAAAABklEQVQDAFRaDQuxWv84AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAK0lEQVQIWy3IsRUAEBQEwd1SJOr8uvQkOnECJhzpSZYYCBM0"
                + "Lcf9ZlQh3wWKWA5Xk6MBwQAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAK0lEQVQIWy3IsRUAEBQEwd1SJOr8uvQkOnECJhzpSZYYCBM0"
                + "Lcf9ZlQh3wWKWA5Xk6MBwQAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAIklEQVR4AWSIsQ0AAAiDsP//rB1MlxIGghYsQQOWoNRHjQMA"
                + "AP//SMpI1gAAAAZJREFUAwCEdgIJz8+ExgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAF0lEQVQIW2P8z8Dwn5GBAYggAM4gXgAAg+YCBUgKw5EAAAAA"
                + "SUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAF0lEQVQIW2P8z8Dwn5GBAYggAM4gXgAAg+YCBUgKw5EAAAAA"
                + "SUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAIklEQVR4AWyKsQ0AAAiD0P9/1qZupoSJ0DxyGJC3+iiQmAUA"
                + "AP//C9I1QgAAAAZJREFUAwA4dgIJmGt15AAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGUlEQVQIW2NkQAOMWAX+MzD8B8qAJTFUAAA35gIFxQ+VugAA"
                + "AABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAGUlEQVQIW2NkQAOMWAX+MzD8B8qAJTFUAAA35gIFxQ+VugAA"
                + "AABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJklEQVR4AVSLMQ0AAAzCGtTMv5q5YTuBQEKPyouJiAn6q19VuAwO"
                + "AAD//wnhEpIAAAAGSURBVAMA4FAFBQG8BCwAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJElEQVQIW2P8f4PhP6MGAyMDFDD+ZwAKMOAV+P//PyMQwLQAAJ81"
                + "CwUd8tccAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJElEQVQIW2P8f4PhP6MGAyMDFDD+ZwAKMOAV+P//PyMQwLQAAJ81"
                + "CwUd8tccAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAIElEQVR4AWTIsQ0AAAzCsMD/P9MVlUhe4kCaBWrmNeMAAAD/"
                + "/zfOO90AAAAGSURBVAMA3R8ICcQj3MsAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAFklEQVQIW2P8z8AARAjAiMwBsQkLAACDawID+0h1rgAAAABJ"
                + "RU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAFklEQVQIW2P8z8AARAjAiMwBsQkLAACDawID+0h1rgAAAABJ"
                + "RU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAOUlEQVR4AQAtANL/AgAm////AAD/Av/aAQAA2AAAAgH/IQAAKAA"
                + "AAv8B3wCBgIAAAgEAAAGAgIABAAAA//+kky/SAAAABklEQVQDAFRaDQuxWv84AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAK0lEQVQIWy3IsRUAEBQEwd1SJOr8uvQkOnECJhzpSZYYCBM0Lcf"
                + "9ZlQh3wWKWA5Xk6MBwQAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAK0lEQVQIWy3IsRUAEBQEwd1SJOr8uvQkOnECJhzpSZYYCBM0Lcf"
                + "9ZlQh3wWKWA5Xk6MBwQAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAr0lEQVR4AYyOsQ3CQAxFv2mAEUAUSYMY47wFHTAOI9CQbHIe"
                + "A9EkVKxAZ2wfSIkiJE5+vu/zl88z/HmKcauaNlnzMgWuHa8VsADCqHdCXjPSRQLXjte4lS/DSFqDzyvIdRG4drzGbmBU6tCc"
                + "5nhU+8B15ieSvIrLcpkIUN33dGxbOjQNV10nUFvtC1B2tHsSIgJmBhFFLyaG+iQiEoPtkJnJnp3fE80wisnEUXdQvAEAAP//"
                + "VmhWRgAAAAZJREFUAwCOaUMVSmui5AAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAmElEQVQYV42PzQ3CMAyF7Q2QukC4ICbotR6IHTA7MJC9SRZA"
                + "4kS5GdsKUWlVCSuS814+/wThz8DkTmbTS4AfnJKHll3TrOAQJmhxRr9cWvt7y6Hdx3MD0Y42yQxXfiZx40Pm0KTv3461FBCi"
                + "BEgESq19+z56+R/zdWPN4L8+Riyh3LeBIkLMDKoa9hZcF/auew9rfzN6r/AD8z8wC6n68A8AAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAkUlEQVQYV62PwQ0CMQwE7RIQDYQPooL7nguih8g9UJDpJA2c"
                + "+AE/s3ai04GExAMrkbPWeLVh+rE4uaO73I3qoil1X7OHlocRIE7Q40x4nIf9ZfTQmPNpgOwHn+1JVW/dse66I7Rg/ubYSqGr"
                + "SAKzGZXW1vQruP2PI26Y4fYtFEdtocw7QDMTVSX0zn6C3/T/wRfzizALwpK/7gAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAW0lEQVR4AdTLwQ2AQAgEwMUWbEj70Rpu7cCHBVGEfWgHqCQk"
                + "p+Hh9/gsZIcOP8che5pNsMjs1yHmFboPeBJbxgCH5CmjqpRyYCFT6TAauYekxF3nC9bFd28BXgAAAP//HA3MPwAAAAZJREFU"
                + "AwD/kxYV5nc6CgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAUElEQVQYV2NkIBIwgtTVC9f/b4hsZGhYXs8AohmnMIDFkQFY"
                + "oKFe4L/9QQOGg/YXGOobP4BUYVcI0/kfCBoaGhgaGxvxK8TnXAyduBQPBYUAzvAVCzcdbJgAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAATklEQVQYV2NkIBIwgtQ1CDf8r49sYGhc3sAAohmnMIDFkQFE"
                + "YYPAf/sDBgwHHS4w1Dd8AKnCrhCm8z8QNDY2AjU24FeIz7kYOnEpHgoKAc8UFQug8KnwAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAAOUlEQVR4AQAtANL/AgAm//9AHL//AkD2wAAwB9AAAn/tgQBf"
                + "DaEAAgA3yAAAKNYAAoF/EQChRQ0AAAAA//8ndWI0AAAABklEQVQDAHWwDkGwhZlYAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAANUlEQVQIW2NkUPv/3/7bAQZGe5n9/w3YLjAw7ue0/y+g9gHI"
                + "sOf4LyD5j4HRYb/Ef4O7vxgAjuARs3OGyksAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAANElEQVQIW2NkUPv/3+HbAQZGB5n9/w3YLzAw7ud0+C+g/gHI"
                + "cOD4LyDxDyi1X/K/wb1fDACPgxG6JQ+97QAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAWUlEQVR4AcSQQQqAQAwDJ/2JF/GZ9ZfixafEddGDsAVv5tBA"
                + "mNLQ4KN+BWfbYB4vOod3wdbI2wuOkCdY4HEKhXWwZnJ5wfQ42mFlpuhN3cPReP1RamsjCjgBAAD//8KC5xQAAAAGSURBVAMA"
                + "atMbENm3LJ4AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAX0lEQVQYV7WQQQqAMBADE3/SS+kvrb8UL/7EmIp62kovBkJg"
                + "GXbDEoPiIIc/wCxpI5iFKxFfoQBhddNiO1k6IJV0cMekhJafG5daMdtNXfB5j5u6hUEretk7bODNhOAJzrgfCzJhsNQAAAAA"
                + "SUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAX0lEQVQYV7WQQQqAMBADE3/SS+kvrb8UL/7EmIp62kovBkJg"
                + "GXbDEoPiIIc/wCxpI5iFKxFfoQBhddNiO1k6IJV0cMekhJafG5daMdtNXfB5j5u6hUEretk7bODNhOAJzrgfCzJhsNQAAAAA"
                + "SUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAZUlEQVR4AazOQQ6AIAxE0V9v4sZ4zHpL48ajjIUUE23c2QAT"
                + "4BE68a5FEoiReV+gDoM9ZGY6CjTNsMJIsgqUnWzutEzTo8D42Nzd6J2qo7YU2A7HNItnufmEFhXmlp8w0GP8Dy8AAAD//z4L"
                + "JNsAAAAGSURBVAMAB4kbFQG6XKkAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAX0lEQVQYV8WOQQqAMBADE3/SS+kvrb8UL/7EmJYKRap4M+w2"
                + "sAxNiLuipI1gFKqjTHt6VoCw+pK8dqYHkAo6uGNSQPHXH5ecMXtb5Dj6quGmbuFoa9ixB3um0l/0I3gCu8YfCw1yrNcAAAAA"
                + "SUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAX0lEQVQYV8WOQQqAMBADE3/SS+kvrb8UL/7EmJYKRap4M+w2"
                + "sAxNiLuipI1gFKqjTHt6VoCw+pK8dqYHkAo6uGNSQPHXH5ecMXtb5Dj6quGmbuFoa9ixB3um0l/0I3gCu8YfCw1yrNcAAAAA"
                + "SUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAZklEQVR4AaTO0QqAIAyF4X+9STfRY663jG56lNOUGcQIgoZ6UD6d"
                + "Ex/rB1wkgRiZHcuLOgz2kJnpKNA0wwojySpQdrK50zJNjwKjsbm70X+qjtpSYDsc0yyu5eYVWlSYW77CQI9xAQAA///FOIgeAAAA"
                + "BklEQVQDAGECGxXylwAXAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAY0lEQVQYV6WQXQqAMAyDE2+yl+EtnbcUX7yJMf7BYJsMLJQ89KNJ"
                + "S3QWOzmUYJS0EozCpbiZAhQgLJ6MbivHBkgF7dwwKODUz41zSpjcj2Xd+j3OSZ3C1q5qxhzMmR/vaTz2AM0wHwuiBt/PAAAAAElF"
                + "TkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAY0lEQVQYV6WQXQqAMAyDE2+yl+EtnbcUX7yJMf7BYJsMLJQ89KNJ"
                + "S3QWOzmUYJS0EozCpbiZAhQgLJ6MbivHBkgF7dwwKODUz41zSpjcj2Xd+j3OSZ3C1q5qxhzMmR/vaTz2AM0wHwuiBt/PAAAAAElF"
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAk0lEQVR4AayKIQ6DQBBFZ1cUgakhqWtS2bTpivqmDkdNU7tX"
                + "6RE4Ag5ugEQSSHCgx3AABCAXBgWEcfzkJfPnP5l9LEPo0DEEMJE13IF4pBa84p7RAGRzvJXd6Ynf4tLq/MyLYMxPed7f8f0K"
                + "oogXdRCUV9fFg1It2jYvzhdEnNfFLakJIZKR9xhBfYtJ3BrWv/3FAQAA//92obYoAAAABklEQVQDAK0fJxWpARh1AAAAAElF"
                + "TkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAgklEQVQYV2P8z8Dw/4A9B0NjgwDDQQdOhv+M9xkZsADG/Q4O"
                + "/z/wf2F4oPCC4aPAF4aGxg/YFTY0NNxXevKEw+HiRQHZV684GB8+xK6wqalJ39/AwERNQiKDg5XVhNHQELtCkHP+//9vD6Qa"
                + "gNiBEQiwupEkhdhMQBfDag1Oq6lqIgCfzScLcG6H/gAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAiUlEQVQYV2M8FcD+nwEIpkXwgSiGBRGvGcEMNMC4IcAErPCB"
                + "wgsGgQ8fGBIWfMGucEFCwhVWDg5u+2vXRKXfvuVmvHoVh8L4eG3TiAhTFQmJLDZWVlNGHR3sCkHW/v//3wFI1T948MBBUVGR"
                + "sMIDBw44ODo64laIzZcYviZGEUgNVmuwhiOxJgIAFNAnC2GN/IoAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAZElEQVR4AeTQIQqAMBjF8YdgUotBFk0Gm2EXEKtNzHarBxHB"
                + "bBUP4VUEDzAGO8C3tKUx1vfyr7x/gsBFABuihV30pTWZKM7XqzzRsxfFqIyDE07tBs5vZIPww/mpcHQl/j23UAMAAP//0Pds"
                + "sAAAAAZJREFUAwD2WBFROHUfbwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAWUlEQVQYV2NkIBIwEqmOYegqVPufFvdhgcnCV4kgT6SBPIzV"
                + "M3Fi81bbih8NSVGZy8C4HqIGq8L99uyrJVT/hWjY/WZgjMOjkOG/4uqEBZ9D5ie+AakCGwYAh4cWC3rmpLIAAAAASUVORK5C"
                + "YII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAX0lEQVQYV2NkIBIwEqmOYegqVP+fn/hhoVbduwYGxd8P0kEe"
                + "xuqZbPGpq8wlT4R6KW1jEFn3FqwGq8IDjpyrxFX+hCoY/mHgzPqPW6H4K7lVQWt+hhZM+MagfuszWCEAHxEcC87YTb4AAAAA"
                + "SUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAIElEQVR4AezQoQ0AAADCMML/R/MBQSA3PVVrjLFC/XkCAAD/"
                + "/3Nn3qIAAAAGSURBVAMAEzgAFbNrw9wAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAF0lEQVQYV2NkIBIwEqmOYVQh3pAiOngACmkAC8i6MuwAAAAA"
                + "SUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAF0lEQVQYV2NkIBIwEqmOYVQh3pAiOngACmkAC8i6MuwAAAAA"
                + "SUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAATElEQVR4AdTKMREAIQxE0b04uZIeAWABAxBlIAARadAFEyoq"
                + "UpOZPyn2UXVlajCOkDp2FvyHlzK8wQAKIrHlbEMVzPzpv0W38dxegAsAAP//XAhpcAAAAAZJREFUAwCP3w4VdtN2awAAAABJ"
                + "RU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAR0lEQVQYV2Ocrxn/nwEIEq8vZATRuADj/CoeiMK2L/gV7ndw"
                + "2O/w/78D48GD+BWCTJs/f/7/xMREwgrxuQ0mh9cUZAOGgkIA7moSC5sf8xAAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAASElEQVQYV2Ocr5nwnwEIEq8vYATRuADjgmoesMKE1i/4FR5w"
                + "cNhvz8DgwHjgAH6FINPmz5//PzExkbBCfG6DyeE1BdmAoaAQAGtwEAvm0nZdAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAfUlEQVR4AZyNwQ1AQBBFhxL0IHESBThwFQ246kUFEg240Ica"
                + "lOHshLdiJ/Yg2dj8l83+eZkNxfP8F0+R+CF9f+ZsRKgY9jBBCRpHpB2hgAQa0KjItpx2B9sddBHvO7aUQGShGWCFGWq6jfuO"
                + "iubFoIMMWlDJzBzRFF94ixcAAAD///gP/JcAAAAGSURBVAMAeyASFSc4nisAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAaklEQVQYV2NkIBIwEqmOAUPhfwYGVahmTqDkJZhBKAqBiryA"
                + "EnlALArEi4CSE3EpfAuU4Abiv0B8GajQAkMh0DQboOBKqGm/gfRFIPYBKn4HUoxudTVQLBSIzwFxCUwRhkJ8IUB+8OAyFQDU"
                + "0xILsSkm0QAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAaklEQVQYV2NkIBIwEqmOAUPhfwYGVahmTqDkJZhBKAqBiryA"
                + "EnlALArEi4CSE3EpfAuU4Abiv0B8GajQAkMh0DQboOBKqGm/gfRFIPYBKn4HUoxudTVQLBSIzwFxCUwRhkJ8IUB+8OAyFQDU"
                + "0xILsSkm0QAAAABJRU5ErkJggg==")
    public void drawImageDataUrlPng() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA8ElEQVR4AeyQMYoCQRREH7LZIrvBHmE33lDYZJncQG/gIRRM"
                + "jEwEPYQ30MBcTARDYz2CgSJmBlbJTGAzA92YOvyC6qrfD6ZrPH6Zjv1c9rJpUwDrX7D6hlkXhpa9M+HqUvTcgbq46MDfDj7G"
                + "8GbZO3MXTdNiTco+4dcQ+Ydx5k5hJkWNgY0WvFdt512jqg9zA8PsqbOBmzlcqih5t6nqw9zA5RG2PbiGpTN3ypdS1BjIAZpT"
                + "WP/AyRDL3pm7KFK+dAfKn3Xxfw/tCQwse2fupOgpgMUF/9pIB8teNm1CYNrtku0XsORREqPXGyY+WMn6DQAA//9HegNzAAAA"
                + "BklEQVQDALe2MilY1lPyAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABF0lEQVQ4T+2SvUoDQRSFv5SC5UIUK30ACUFIbyws4wMsBkO6"
                + "RJ9AiBhUsEqvstoKVgELwdRa5cc6pTbGLqTUM+saNsvGHUljkYFlmDn3fnvPvZNicmV13AiunrW3I3riMfUTkYYT0dwlcMzd"
                + "G7x3wNN+mEgJBfjAZTjdhsoVLIaTd2HYhPMPOLKFGmB2C24fYC0uKQ/9R9iR1rWBGmB5DxqXsBCXUIKRKq9KU0jy8oGy1vCm"
                + "AKWNbmBfcRfJODDAjCzfyfJqXMKmLLegIK1nC2QF6urVwXVkKK6Gcg9nA+k2MBMzfjaa9PE6FLU7n9/PZvAim69Qs4VNAIMk"
                + "McnpM8wnW5vhH44r/EsVv8XOgbN3ct7Df9jDL+YBMhUCa42EAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA4klEQVQ4T+2STQ4BQRCFPyT+V27AESQyC5cwQqzY2zuGvT0b"
                + "QnAJCyGO4AhiIRgJUU1P4mdkesJSbzpV/frlvaoX4vlYUuZ1ayX3/OXdtwxpRCIDvR2UauCoZh9iSZhuoS7lwZdJA26EQjbM"
                + "QXkB4cePBbisYbKBShBCKwozByJen2JwPkHR1L5S2BRP7S6Iw/fTgH0PUkEUmhC2hLBjQqoU/tyyWsooC/bSeyljWUrVRJ3C"
                + "uLGJ69jYKjbqYSCxSd83rGJzDEro4n8WbFMBvjjXsi/QFPAnNJ3UZ9x/ht/P8AqUpzEVnisiKgAAAABJRU5ErkJggg==")
    public void drawImageDataUrlSvg() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
    @Alerts({"TypeError", "0", "true", "true"})
    public void measureText() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "      function test() {\n"
            + "        var canvas = document.getElementById('myCanvas');\n"
            + "        if (canvas.getContext){\n"
            + "          ctx = canvas.getContext('2d');\n"
            + "          try {\n"
            + "            log(ctx.measureText());\n"
            + "          } catch(e) { logEx(e); }\n"

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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAABtElEQVR4AeyUPS8EURSGh5YIlWzlR6xCqVNR+EhQkGh9tPwE"
                + "arQSGo2IRKEVlWYTfoJEbC8kOp53MjdZY8adk9yCzdmcZ+7Hue+Zu++9u4PZ90+b4SH0xi4DQZOt8mhBLLTmlEXDoHqqSzcP"
                + "5W7pqaX5EZoP2mmyuba8UeZrQy+dqc2mS3QptQ5vsAB5WDa6iWINzmEJruC9YJb2CT5BfZpGoZN6ZKV0QtrgqN6xRe4SWlUb"
                + "3SAhUWCfseKYxxmswAeMwjgswxFMwRwsgiUeWDwAZe0Nc6o7T9ut2ugJCQkDe4yrQoV0PMrpzunIXhi8giUuisW/aqs2WuiS"
                + "Nc9U2oEQk3R0TfTF6DYL60YnKHsPY9A0dGWkC1dJx1l3SuWaI0zcQbu80Q6T29AbBwyEjll/F3qp7qrmSGXXPPQrpcmCXm5p"
                + "ThohXbhK0iuv9aohvfpVWtUYItkpb5S5vxm+0dTn4o66o6kdSF3P76g7mtqB1PX8jrqjqR1IXa/v7mhqg8z13FGzZRGBOxox"
                + "yJx2R82WRQTuaMQgc9odNVsWEbijEYPMaXfUbFlE4I5GDDKn/42jXwAAAP//4JrchgAAAAZJREFUAwDXF0VVgodBgAAAAABJ"
                + "RU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAABUklEQVRYR+2UPw4BQRTG7TU0Cu5Aq+EGFBqJhgsoFAolB6Bx"
                + "AEdAodC4BImGY/B9yRt51vqz8YTE2+RlZ2Znvvnm995slLl+KugOECU1PJR2F+8Zoh5bk9TNY3CNyCI2iD5ioSae0I7u6Oi1"
                + "bczZcW188jOjjzbQ+75jVOtcDpnGaA4KNcRRqJelT+EqYi47dPBevkiUGaJuUdZSZytrmdmxjBeSjIYN9clG6DD1gShTwnQy"
                + "tbrNjLQQvRRG96JNHR4+cW0aonGjFGW9BnOhTRINZZTUpghdowc5JL+tEBOl8zWjvIxMb7iEJNdE8MJ+zChJ8CG1V4lyPi9F"
                + "qEP2QyafGeV33oubGhUfv/e69y/7Oadu1DolTtSJWhOw1vMadaLWBKz1vEadqDUBaz2vUSdqTcBaz2vUiVoTsNbzGnWi1gSs"
                + "9bxGnag1AWs9r9G/JXoGyiFdcUG+TPcAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAABOUlEQVRYR+2UMW7CUBBE4R5UhGMALTlCSEWFBAeAJiUVB0iu"
                + "QMEJUgYppzAUXARmpHW0WHYSKyORYiyt/jf2DvPfjtzt3F5D3L6gHtPPq9hvsb6ilpWeptsnPBjH+z/p1mnk/k63hdHPmkN8"
                + "5/mvRrP2sI3ROTofUG+oj6C1CLUR1kPsp1h3qN8aPePdGWoS/WusnF7uL+qMln+YT8TmTJQiG9QgBMs9R/wc425rlHHro96T"
                + "7ld02hCtGs35K83lLGajNMB8V7NPmrxKLe6LexqlgQuK0TmFORriyHtqo3sIHlE5o/wC5HE3EaU3PsuxqstiE9F+dfRx2P+3"
                + "2Kh6JiZqomoCaj1n1ETVBNR6zqiJqgmo9ZxRE1UTUOs5oyaqJqDWc0ZNVE1AreeMmqiagFrPGTVRNQG13hX4a00ZAyWoGQAA"
                + "AABJRU5ErkJggg==")
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
    @HtmlUnitNYI(CHROME = {"1", "0.5", "0", "0.699999988079071", "0"},
            EDGE = {"1", "0.5", "0", "0.699999988079071", "0"})
    public void globalAlpha() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
            + "  } catch(e) { logEx(e); }\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
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
            + "  } catch(e) { logEx(e); }\n"
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAARUlEQVR4AczQwQoAIAgD0NWX9+flwRV41AVGQZe9lhPiRXCZ"
                + "qzggaJ5mE9zOZVt6HP8ajvtE8cIvF5kXj2DfGWabxRwOAAAA//94K/bLAAAABklEQVQDALcXIRXO7FmwAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAS0lEQVQoU2NkoDJghJrnAKRBmBJwAKj5AMzABqiBIEFygD1Q"
                + "00EgbkA2EGQQyGByQD1QE8iskW4gKGDJjRRQCkGJFKonG3JiFqseAAjhEgvKgKaUAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAP0lEQVQoU2NkoDJghJrnAKRBmBJwAKj5AMzABiDHHogPkmki"
                + "TG8DsoEgs0AGkwNg+ka6gVSNFKonG3JiFqseAAklEgtbZz3HAAAAAElFTkSuQmCC")
    public void strokeRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.strokeRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAMklEQVR4AeSRwQkAAAgCrf13LmkF9VVgP+vQhnmiB4ewimgH"
                + "ooT3QV3PCYv5KaId/pYXAAD//7KhJgoAAAAGSURBVAMA6cMQFanju38AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAKElEQVQoU2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TPV0CAA8pwYL+jNAvAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAKElEQVQoU2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TPV0CAA8pwYL+jNAvAAAAABJRU5ErkJggg==")
    public void fillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAANklEQVR4AezSMQ4AAAQDQPH/PyMRi7G6qRCTDhduXVGLMTaB"
                + "lcdpBd4dZShDQEBvA6Ctk4eGCQAA//89UfTWAAAABklEQVQDABbFEhevU8KSAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALElEQVQ4T2NkgID/UJpSipFx1MDRMCQ5BEaTDclBhqFhNAxH"
                + "w5CMEKB+sgEAGQQUFQfPo6sAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALElEQVQ4T2NkgID/UJpSipFx1MDRMCQ5BEaTDclBhqFhNAxH"
                + "w5CMEKB+sgEAGQQUFQfPo6sAAAAASUVORK5CYII=")
    public void fillRectWidthHeight() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(1, 0, 18, 20);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQElEQVR4AeySwQ0AIAgDq/vvrA1xg5NXIaG/QnOw9blaBx6H"
                + "JW271JqwNlAJT7jMj7TtmisXBSbhf8jgPXcgwwsAAP//gPpVDQAAAAZJREFUAwDyABApkn0/fwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==")
    public void fillRectRotate() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.rotate(.5);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA+UlEQVR4AezQrw8BYRzHcZM1/wBJUQSKpAgEimSmSJJpbBqa"
                + "IJigsdlsgqCZTdXMZpNMs/kHNPM+893O3T33wwSBfV7n7r7Pffbs8fu+/PsXPg80+7x+eDGeYZyeJRZIwHOMhc1XQ47/LUYI"
                + "w3X0hRm+ykOfCg8ndBGAY/SFsjurjxq8PKMO20hhmVVJ2CXIsIcjSrCMFE6YtnCDUyIsGGMNU6TwzqSNEPpwE+1cTeukUAZX"
                + "bmqIYgpVZgxsd8j8LQeeikhhBWMsd6ctMu5Qe6e34SGNAnbQMuCyh2WcCuWjOTcxVNGBMm4LpWDIzQXKeC1UFsng9wsfAAAA"
                + "//9zeQ59AAAABklEQVQDAGucHykhpMwKAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA60lEQVQ4T2NkoDJgpLJ5DEPTQCFgMLwjNyjQvawANOgyEC8B"
                + "4mYgfkaqwegGrgAaEA415DuQ7gfiDiD+TKzByAZaATUdxaLxFVCsCYhnAvEfQgYjGwgyDGQoLnATKFEJxOvxGQozMICQQiRD"
                + "QBaX4/ANPB2KAhXUAHE2EDMT8hZUvhWqB0U5eqSoAWW7gdiPCEM9gWp2oKvDlVOsgQo7gRhEYwM7gYIe2CQIZb1AoKZ2IFZH"
                + "06wL5F8hx0CQHhYgTgfiOiAWA+KVQByBK0gIuRBZHy+QA4rdOUD8gBoGEhFPDEO0+CLKa7gUAQCpFx4VDtwfyAAAAABJRU5E"
                + "rkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0ElEQVQ4T2NkoDJgpLJ5DEPTQFVgMNwmNyjQvWwKNOgUEG8E"
                + "4lYgPk2qwegGrgcaEIBkyBwguw2I7xNrMLKBXkBNW3FobIca/IWQwcgGHgEqtsaj4S3U0D58hsIMBHkT5F1iwE1o+C7GphjZ"
                + "hdVABSDMSYypQDULgDgRXS16pIgBFVQBcT4RhroA1ewlZCBMXgvq2igcBq8EikcQ8jI2eXuowa5okvpA/iVyDITpCYEGhSGQ"
                + "ngrEObiChNTCIQ1o0HIg/kwtAwnGFakuHAYGAgBd6xwVKif+GwAAAABJRU5ErkJggg==")
    public void rotateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(.5); context.fillRect(6, 2, 12, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQElEQVR4AeySwQ0AIAgDq/vvrA1xg5NXIaG/QnOw9blaBx6H"
                + "JW271JqwNlAJT7jMj7TtmisXBSbhf8jgPXcgwwsAAP//gPpVDQAAAAZJREFUAwDyABApkn0/fwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==")
    public void fillRectTranslate() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.translate(3, 4);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAOUlEQVR4AeyQwQkAAAgCq/13Ll8NYNEnQ6GXHBe2fBqcCz1z"
                + "mGClekYIQC4ibG+Oj6ocwtwwDx0WAAAA//9NhoXGAAAABklEQVQDANhvDynTI/KmAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAMUlEQVQ4T2NkoDJgpLJ5DKMGUh6iuMLwP7lGjxoID7nBH4bk"
                + "RvJo1iM75BAaR2DxBQB5vAYPhQt4egAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAMUlEQVQ4T2NkoDJgpLJ5DKMGUh6iuMLwP7lGjxoID7nBH4bk"
                + "RvJo1iM75BAaR2DxBQB5vAYPhQt4egAAAABJRU5ErkJggg==")
    public void translateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.translate(3, 4); context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABC0lEQVR4AezQz+pBQRTAcf0Wv/ptfjtloZSlnX9l4QGUopS9"
                + "slCUB7AQCw8gZWGhPICilCewUMgLKGWhKFkqiu+RGbqkm2vH7XzuPTN35sxpfmxvfr4FrV/oh99hkht0w1Lc3mGJSjOs0EcF"
                + "CbhgOlTBf3Z4IGHnFUEBbcyxRA9lxOHEw1AF/Q//XicdpFEU0cHiostX5uSfNGIzW5B9dyFdxpiVrqt819AFfTKwYKL2qg63"
                + "TAxxwCsxVptUwSwTIfwiiAwa0AvJn8Vdh2rxkWQEKSZFA+RyiByWI29iCmPog1WHxgW34z0DuY463zS8+EMYebSwwTnMFDwv"
                + "NLx2jAeoIQUdrxbUBYzJCQAA//8yJyR8AAAABklEQVQDAF8lJymD/dqYAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAyUlEQVQ4T2NkoDJgpLJ5DKMGUh6iQysMlYEefgbE3ynxOLKX"
                + "bwANUgHim0B8HglfALLfEWsJzEBOoIYvQMyEQ+MjNEtAFj7GphZmoD1Q8gCxroCqe41kyTkgexVIHGZgBZDdTqKByMpBwaSB"
                + "bOAKICecAgNXAvVGIBtYBeT4AbEBELOTYXApUE8PsoEwM1iADD0gNkXC2kA2MwFLPIHyO7AZiE0fKAUYo1kCSl4w8A/IkATi"
                + "V8QaiM0SfqCgGdQSWSCdCVM0+PMyAOlZHxXNWGcZAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAxElEQVQ4T2NkoDJgpLJ5DKMGUh6iQysMtYAe/gHE9yjxOLKX"
                + "rwINAhn6GojPAfFZJPyQWEtgBvIBNXzEo+kF1HBki55gUw8z0BEouY9YV0DVgQxEtuAAkP8VZmAJkNNNooHIyu8COSogAZiB"
                + "y4DsSAoMXA3UG4ZsYDOQ4wrExkDMQobBFUA9ncgGwswAuRhkqBGUBrFBmBBwAyrYjc1AbBpZ0SwAWWaAplAYyH9HrIHYLOFA"
                + "8okCkF2M7EVC3iFJnuqFAwBzIx8VNVMmtgAAAABJRU5ErkJggg==")
    public void rotateTranslateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(0.2); context.translate(0, 4); context.fillRect(4, 4, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA4ElEQVR4AeyQsQ4BQRCGNxq9B6Aj0aipKbwBpZqad0BHreYF"
                + "qJSoJGpqLa3SNyeTbNaKc3uN5C7z7d3M7fz/zuZMyk8mGH6h/3OHpfBhXwo6cov0ATuYQQ9q8HOo4IbOPDRgAAs4gZjsecc2"
                + "UcErTQdwQ0zqFH0mc+pvk6gg/4ycUt7fUJM+G3WSKd9R2ILrqJJsaWubLXikeIYkUaVJMLYgNVNhKUAThrCEuCbRKV1B+s2d"
                + "ZQsT6IJtMiJfwQXc+CjobpRcTcYkHSiDTqImRan5Tkg9VvhMbiGCXtfUBZ8AAAD//zEggMEAAAAGSURBVAMA7bomKdvSkgUA"
                + "AAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAxUlEQVQ4T2NkoDJgpLJ5DKMGUh6iNAtDaaDb3gHxd0rdCHNh"
                + "AtCgOUB8DYjPIOGLQPZPUiyBGSgF1PQEiNGD4A9Q7AqaJZeB/F+4LEE24ARQkTkRrgEZdgnNEpClf0F6kQ2sAPLbiTAQm5JO"
                + "oCBIP4qBGkD+dTINvAnUB9KPEWZ7gGL2QMxChsFaIAdhS4fsQAl9IDZBwiDFzAQsqQTKdxCbsDmBio3RLFFD8+FBIN+BWAOx"
                + "OY4XzRI9IN+CEgOxhsDgNxAAub8hIw8WPeEAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAoUlEQVQ4T2NkoDJgpLJ5DKMGUh6iNAtDAaDbPlDuPgZ4LCcA"
                + "DZsPxBuA+AEQXwTiC1BMkj0wL4Nc+B6HTpIsQQ7D+0ADFYh0Dk5LkA1sABpWT6SB6MoWAAUSQYLIBuLzNiF7QBEqiG4giL8f"
                + "iB0I6cYhrwiKUFzpMAAoaQDE+lCamLAtBKqdQErCJmTJAaCBjqQYiM2n6JYYUmoghiWD30AAk4cbJe1EwXcAAAAASUVORK5C"
                + "YII=")
    public void transformTranslateFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.setTransform(1, .2, .3, 1, 0, 0); context.translate(-5, 4); context.fillRect(4, 4, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAANUlEQVR4AeSQOw0AQAhD350m/CvAEyygoDDRpOnW32cYe4YO"
                + "obCH7jXsBFUvNzR4CimMf5gAAAD//8jOg4wAAAAGSURBVAMA0CMgFVJk6gcAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DHADzzAw/KfEcBMGiFmjBpIfirQP"
                + "Q/LdhqqT6ukQACo9DAtpppU3AAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DHADzzAw/KfEcBMGiFmjBpIfirQP"
                + "Q/LdhqqT6ukQACo9DAtpppU3AAAAAElFTkSuQmCC")
    public void fillStyleNullFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = '#cc0000'; context.fillStyle = null; context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAANUlEQVR4AeSQOw0AQAhD350m/CvAEyygoDDRpOnW32cYe4YO"
                + "obCH7jXsBFUvNzR4CimMf5gAAAD//8jOg4wAAAAGSURBVAMA0CMgFVJk6gcAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DHADzzAw/KfEcBMGiFmjBpIfirQP"
                + "Q/LdhqqT6ukQACo9DAtpppU3AAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DHADzzAw/KfEcBMGiFmjBpIfirQP"
                + "Q/LdhqqT6ukQACo9DAtpppU3AAAAAElFTkSuQmCC")
    public void fillStyleUndefinedFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = '#cc0000'; context.fillStyle = undefined; context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAANUlEQVR4AeSQOw0AQAhD350m/CvAEyygoDDRpOnW32cYe4YO"
                + "obCH7jXsBFUvNzR4CimMf5gAAAD//8jOg4wAAAAGSURBVAMA0CMgFVJk6gcAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DHADzzAw/KfEcBMGiFmjBpIfirQP"
                + "Q/LdhqqT6ukQACo9DAtpppU3AAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DHADzzAw/KfEcBMGiFmjBpIfirQP"
                + "Q/LdhqqT6ukQACo9DAtpppU3AAAAAElFTkSuQmCC")
    public void fillStyleUnknownFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = '#cc0000'; context.fillStyle = 'pipi'; context.fillRect(2, 2, 16, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAQklEQVR4AbyQSQoAMAgD0/7/z20QPKYeahQXcB3caBbrwkPY"
                + "H+M4YCWMC3RLGNO1jhDWGI8OK2H+Tt3Puoox1054AQAA//8XBWeFAAAABklEQVQDAPFbEBXPTi1HAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DMgG/qfQcLBZdDEQVzAQ8gFOF44g"
                + "AymMZIh2qqdDAD9SCgsSO8biAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAALUlEQVQoU2NkoDJgpLJ5DMgG/qfQcLBZdDEQVzAQ8gFOF44g"
                + "AymMZIh2qqdDAD9SCgsSO8biAAAAAElFTkSuQmCC")
    public void clearRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.fillRect(2, 2, 16, 6); context.clearRect(4, 4, 6, 6);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAeUlEQVR4AeyTSwqAMAxE257NY3u3ShavzCIGNXEhWAhJy+Tl"
                + "Ax1za7PSRis+PzC/0A/tsO+tY5nB3ZEBq79axAV6yQon9nQLaN/PE0RvgM2jW0Aesv5doI2t9qTbsEOFE58VYY8h0EsGrF51"
                + "t4GaTAzc7iVAA2HlwAMAAP//TK1liwAAAAZJREFUAwBzV2BR34pW7AAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABBklEQVQ4T2P878Lwn4GKgHHUQIpDc+iE4VOgX4WAmJNSP0O8"
                + "zMiwgEGAIYXhPYMW0EATMGYE4v8M+kA2OymWwMLwGcNuBhlGRtRE/r+BgYXhKIMO3BKIZbpAVWy4LEFEChODBeMuhpOEXPM/"
                + "FGjYBwY9FEv+gy1lBulFjuUOxj0MlYQMxCb/35WhHOjqDlQDGRluMO5m0CTLQA8GdYY/DDfQXQh0L4MLgzXDQcYGoDSJABi5"
                + "14BaNLEl7J9Agy8CvXAGqACCBRmuMa5m+IvPDqCB7UD5CmJzyneg4rNgw5mgeDvDLeRUAQxHe6AjDhBrIDbHfUax5B/DJaDP"
                + "TlBiINYQGPwGAgAm8mgw6jTe3wAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAxUlEQVQ4T2P878Lwn4GKgHHUQIpDc+iE4QegXwUo9i/QAIiX"
                + "/zMsYNzLkPjfmSGAgZFBAcjXB9IGQHkQJgnAwvAD4x4GQWw6SbUEESksDIqMOxgeEOMcfJYgx3ID0JWNxBiIrua/K0MCMJjm"
                + "g8SRDcTpbUKW/HcARigLw3t0A0F8R6ArDxAyAEdY3wdFKK6EvQGo6QLQGxcZWBkuEBO2wNTSD9RTQEpOwWsJ0EAHoIH7STEQ"
                + "m09RLGH4w3CeUgMxLBn8BgIAZv1hLbrWWZAAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAoklEQVR4AayQKw6DUBBFmy6hlZX1dUUi2QOSPSCRSFgDkj0g"
                + "kUjQSCR74EyG4ZOQAAFyz7wZ8ubm5j0fN39LwwDvD1ySGX5x8aCGCmJw4bTMsGXThzeEIEooPeRwOL0ZsjOppIvAgR8UIOkb"
                + "zt30W4bsTeroMpD0L05Ln9JbenkuRtWeod6aq6X/88vSy3Mxqs4a6pZWS6/TWK8Yjhbr43bDAQAA//8nEaLcAAAABklEQVQD"
                + "AC1bFhX1lozgAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAY0lEQVQoU2NkoDJgRDIvGsjeDsTvKLED2cAlQIM8gfgO1OBt"
                + "QPoUqYYjGwjTawY12AtIq0ANB7mcKNdjMxDZUUJQw0EuJ8r1hAxE9zE218cgKyLVQGyuX0otA7HGFyUupI+BAC5XEQsxJDCz"
                + "AAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAcElEQVQoU2NkoDJgRDLPF8g+B8RPKbEDZqAI0JDJQOwKxPeA"
                + "eBcUHyLVcGQXwvTaARluUKyMZDjIEoKux2YgsqOkkQwHWXKXkOsJGYjuY2TXKwEldwNxLhC/gSkk1UB01xsBBTYjC1JiINb4"
                + "GvwGAgB9VhILFc2B9QAAAABJRU5ErkJggg==")
    public void moveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.lineTo(16, 6); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABB0lEQVR4AbSTrYoCYRSGZ/cOdtuG/UmbNi0sLIKYBEXvwCaC"
                + "dyBqMlkMYhEULIrZYrQZNIjBYhZsJouY1OcNn4wiIp5xeJ9zZoY5z3zDzDx7AW8PE5ZYqKDZ4lb4geYdzHFCifYqVpxwiWgB"
                + "5jih+s5sQyARzXuiBPrIeim/SM1xK6xi+ocw/MAL3BUnnDJdgTw0YQZz0I1i9JvjhBqoURIQAn2TKfoKCrCFDsThavzC8wu1"
                + "6jInI/AKI9BN+/QoXMw1oX9gw0EdvqEHbWjAF5zkVqF/qMXBG6xhDBk45h6hG86xk4QsdOETPItQ8xPKH+i3HdLNQjlEkZKG"
                + "wIRyDVSsjyzHCQcAAAD//yNQAOEAAAAGSURBVAMAnuoiKaqIdsMAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA4klEQVQ4T63TzwoBURSA8RmvwMpOUqxslaews7HyAv4nNhgp"
                + "SizY2nsCeQh7axt7C4UF39G9NVLTNHNufRlTfnMa97qO8nKVPceCI+A3jeM+wIIToBd5WmAT6ElbLVAme9BUC2yZCTdaYAco"
                + "QQstsA6Uo74WmAea05qudKF7FNy/sTMAXUpSmlJ0pIP5DOUHnRSZukwlqtDSbKtbkBz26MnUVZrRjnokB+FvhQX9P2zwpW0m"
                + "lnf+s6KAFlhxUTTv/WRvxgHFyNLe/GlDuREXtIMNuKhRQQsUWHbFWRP8TqsOfgB13yEV/GLnewAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA3ElEQVQ4T2NkoDJgpLJ5DDADG4AG/wfiRkotgBkIMujfoDfw"
                + "L9CFTdT0MlUNrAa6jBOIa6jlwkygQXpADKIpArBYVgeacgqIs4D4HBA/BOJv5JiMnLBTgQZ4A7EGEMsD8XUgPgDFm4g1HF9O"
                + "sQIaYg/EDkBsDMSLgHgmEN/EZzixWU8VaEgcEBcA8XIg7of6AMNsYg2EaeQHMgqBuBKIu4G4DohBOQwOSDUQplEOyABlV1CQ"
                + "VADxKpgEuQbC9LtBvX8U6vKvlBoIMpgJiCcAMSiFKFPDQJSIGfwGAgAmkSMVCV26awAAAABJRU5ErkJggg==")
    public void moveToBezierCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.bezierCurveTo(2, 17, 1, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA+klEQVR4AazSPcuBURjA8btnfIZnf8iCQbJQZFM2RZRMJmX3"
                + "EmUio08gm5VMBspiNFmYLcxSfAH/S+FE5OXS9TsOOf9O3X4s5ZcZLNNOwYOPxww6qOQxwg5j1ODHy2MGS5yKwwk32vjHAHMU"
                + "8YunYwbNH275MEQBLlQQxBpVPJxHwdsDU77IIoowZgjgbl4Nng8u2KTRhUTlIbK9zrvB88kOmwR6iOEynwYlMGHJQG5r4/00"
                + "3wQlIA9ObtuSD+LboDTqLBGEYGkEpSP/2ZxstIJ9Ykmo3XBF7ACf1g1pWUsWr2ZwQ9CuGdwT/NMMNgg2NYP0LLWnfIrJcgQA"
                + "AP//BP9DGwAAAAZJREFUAwBBPSApbk+eOwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA10lEQVQ4T83TMQ4BQRSAYRuFjqj2ANxAIdFzAEqN6BWipyHU"
                + "FBuF3hVcgEohGr0rqEThf5sZmWLEMq+wyZ/MbjLfTDKzUU75iRyvz3hPl5A1XHAD1KDYwILb7lkXcUE7p2xgwW1bxks6foJ9"
                + "oG9OjY9rOtOAbu/grKCd32OwohktfOi3oDXmDOrUoocL/wqK0aSpgV9mCCjImPI0sWIoKI5crREd5EUDLOJcqaQFijOkgpy8"
                + "xg4FbFOXOlpgBWxHVS1Qdil/T6wJpqetCSaAJ00wvdv/Dz4ByKocwaSTblsAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA4klEQVQ4T2NkoDJghJrHBKSzgPgaFL8g1x5kA6cCDdGC4r9A"
                + "+gQQ74DiB8RaADMQXb00UMAeiD2A2B2InwDxFCCeT8hgXAai6/MCCmQDsQbU4H5cBhNrIEy/FZBRA8T/gTgHiO+jG0yqgTD9"
                + "VUBGGRCHA/FOZEPJNRBkhj8QLwZiFyA+BTOUEgNBZiQCcQMQGwPxG5AApQaCzJgAdV0BtQwUgUaOJZC+Qg0XghxWB8R8QFxC"
                + "LQPNgYbNAWJdahkIciUoNzlR08DtQAMnUdPAhUAD91HTQFDiXk1NA8HJcfAbCABA6CBYte9o5AAAAABJRU5ErkJggg==")
    public void moveToQuadraticCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.quadraticCurveTo(19, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAOElEQVR4AezSsQ0AQAhCUe7231mJK2D3NcEOixe/lucO5qBw"
                + "w7JgEtcluOEzQhLXRTccg3QB/7ABAAD//9S22NcAAAAGSURBVAMAml4QKQrN4ykAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAANElEQVQ4T2NkoDJgpLJ5DKMGUh6iQysM/1PoYbBvkb08Ag2k"
                + "MAgh2odWshn1MnkhQPVYBgBiiQQV+ctw0AAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAANElEQVQ4T2NkoDJgpLJ5DKMGUh6iQysM/1PoYbBvkb08Ag2k"
                + "MAgh2odWshn1MnkhQPVYBgBiiQQV+ctw0AAAAABJRU5ErkJggg==")
    public void lineWidthMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.lineWidth = 4; context.moveTo(2, 10); context.lineTo(18, 10); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0ElEQVR4AeySPQrCQBCFg6UXsBL0BN7A3kZBj2OjIHgZbRT0"
                + "AN7BUhsLrQVrvwcSsjubNSzbCIb5sn+z702GtIrMz+8JtunACDqQFP4nv1BZwx0usIE5NDbxBblbHPSCPsxgBdqTyZX5FmTC"
                + "YCMkeLRp5U6P2RRkMmA0ERI8kfWAb6E2mJyQoJJiVepcZBccomr+hliFOy7cIBamyjrBJyoT6H4YMy7AN2ksyN0yVOWe1RJ8"
                + "kzN7TtRV6CQFFlUT5zhV0BGpLv6C1W6kzbP38A0AAP//p8YVwwAAAAZJREFUAwDiUB0pG3Y4HQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0klEQVQ4T2NkoDJgpLJ5DDQ3kA3o4iggPgvE14D4L6k+wObC"
                + "E0BDzIH4OxBfgBoOsuAMEF8nZAk2AxuAmupxuOwH1JKjQLoUiP+jq8NmoAVQ0XEivGoJVAPyDQrAZiBI7AkQSxEwtBEoD/IN"
                + "QQNBCmYAcToBA08C5UG+IcrAAKCq9QQMBIWfDBA/Q1aHKx3yABX1ALEpEOsCMSsOw1OB4nOIMRBZDShtggw1RsIgPkh8IxCD"
                + "fAMH5OYUkGF6QKwOxEupYSDO4CXXhaMGUh7L9AtDAEzqHRUeyJjbAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApUlEQVQ4T2NkoDJgpLJ5DHQx0AHo6gPkuhybC+8DDVMA4gdA"
                + "fAGIL0ItIMoSbAb2Aw0owOFCZEsasKnBZqABUOF5IrysCPUFilJckQLzNj5zC4GSE9AV4DIQn7dhZoDC15BYAwOACteT421c"
                + "LhSAGghKQiR5m5iEDUpCoIgCYXsgRrZkA5AfiGwjMQZicyHMEhCNEjHkGogzGEYNJCLlEVAy+MMQAEwqFxVGqYQqAAAAAElF"
                + "TkSuQmCC")
    public void setTransformFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.setTransform(1, .2, .3, 1, 0, 0); context.fillRect(3, 3, 10, 7);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0ElEQVR4AeySPQrCQBCFg6UXsBL0BN7A3kZBj2OjIHgZbRT0"
                + "AN7BUhsLrQVrvwcSsjubNSzbCIb5sn+z702GtIrMz+8JtunACDqQFP4nv1BZwx0usIE5NDbxBblbHPSCPsxgBdqTyZX5FmTC"
                + "YCMkeLRp5U6P2RRkMmA0ERI8kfWAb6E2mJyQoJJiVepcZBccomr+hliFOy7cIBamyjrBJyoT6H4YMy7AN2ksyN0yVOWe1RJ8"
                + "kzN7TtRV6CQFFlUT5zhV0BGpLv6C1W6kzbP38A0AAP//p8YVwwAAAAZJREFUAwDiUB0pG3Y4HQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0klEQVQ4T2NkoDJgpLJ5DDQ3kA3o4iggPgvE14D4L6k+wObC"
                + "E0BDzIH4OxBfgBoOsuAMEF8nZAk2AxuAmupxuOwH1JKjQLoUiP+jq8NmoAVQ0XEivGoJVAPyDQrAZiBI7AkQSxEwtBEoD/IN"
                + "QQNBCmYAcToBA08C5UG+IcrAAKCq9QQMBIWfDBA/Q1aHKx3yABX1ALEpEOsCMSsOw1OB4nOIMRBZDShtggw1RsIgPkh8IxCD"
                + "fAMH5OYUkGF6QKwOxEupYSDO4CXXhaMGUh7L9AtDAEzqHRUeyJjbAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApUlEQVQ4T2NkoDJgpLJ5DHQx0AHo6gPkuhybC+8DDVMA4gdA"
                + "fAGIL0ItIMoSbAb2Aw0owOFCZEsasKnBZqABUOF5IrysCPUFilJckQLzNj5zC4GSE9AV4DIQn7dhZoDC15BYAwOACteT421c"
                + "LhSAGghKQiR5m5iEDUpCoIgCYXsgRrZkA5AfiGwjMQZicyHMEhCNEjHkGogzGEYNJCLlEVAy+MMQAEwqFxVGqYQqAAAAAElF"
                + "TkSuQmCC")
    public void transformFillRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.transform(1, .2, .3, 1, 0, 0); context.fillRect(3, 3, 10, 7);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAT0lEQVR4AeyRsQ0AIAgEjZM4qYWTOopPQWIBiQLlG7DwzYUc"
                + "vRUfAvNC6bDO4QBqfTS+2qVL2XbsvsoAZqhACSeu13YHuIHg5YtAOgwYOAAAAP//3hzLLwAAAAZJREFUAwDgrQ0pRqECRAAA"
                + "AABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAR0lEQVQ4T2NkoDJgpLJ5DKMGUh6iIzgMBYChV0BkCH4AqpuA"
                + "Sy1yGIIMBBlMDAAZCDIYA4zgSCEm3IhSMxqGRAUTXkVUD0MA33oEFT5oGBkAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAPUlEQVQ4T2NkoDJgpLJ5DKMGUh6iIzwMG0gIQZxqkcMQpIif"
                + "SEMbgeo+YFM7wiOFyODDr2w0DCkPRqqHIQCa5QMVIJiVTAAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAi0lEQVR4AeyRvQ2AIBBGjXEFe3sbl3AKl7BxChuXcAqXsLG3"
                + "dwUL3zVcJPgXsMPwyEngXfhIk8BfFPoHGjP8J8PMR+t6lB3hChP00EAJr4ZLKAcLphZmqGCADR6bXAk5myxMI3RQQw52E7kJ"
                + "yzruhLpLK7uJ3OSU+VehqrWSzM1fCKGRSRGFkoIfBwAAAP//6D4DfwAAAAZJREFUAwA4kBEpb4W4rAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAb0lEQVQ4T2NkoDJgpLJ5DKMGUh6io2FImzBkAxp7DYhvAPFF"
                + "IL4EpW8B6X+ErMQVKfxAjYZArAfE+lBaCUjfQ7IAZNF5IP6IbAkpscwE1KiGZAHIIg0g1gLiXzBDSTGQkG/B8qMGEhVMeBUN"
                + "/jAEAMq8DhXVLTYNAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZ0lEQVQ4T2NkoDJgpLJ5DKMGUh6io2EIDkMBIP5AbmjiCsP3"
                + "QAMPAPEDID4IxBegbIL24IsUB6BuBSDWB2IDIAbxNxCyhNRYRrcEZJEgsrNJNRCbl1HCnBoGolgyaiDBZEZQAdXDEABMtg0V"
                + "HTQi5gAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAPklEQVR4AeyRwQ0AMAgCSSfv6LWscPLDRH4gOY+Wp4EcaBnu"
                + "MrwTR3bsUvQppN335hv6ApUoQ1rO/jY0BiQPAAD//0ygKtcAAAAGSURBVAMAjggQKb2HceMAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAM0lEQVQ4T2NkoDJgpLJ5DKMGUh6iIzwMGygMQbB+5DCkuoEU"
                + "OhCifYTH8mgYkhcCVE82AJYgAhXAytwXAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAM0lEQVQ4T2NkoDJgpLJ5DKMGUh6iIzwMGygMQbB+5DCkuoEU"
                + "OhCifYTH8mgYkhcCVE82AJYgAhXAytwXAAAAAElFTkSuQmCC")
    public void moveToLineToRotateStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 10); context.lineTo(18, 10); context.rotate(90); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA1UlEQVR4AeySIQsCQRBGF4tYLYKIoEGz3WCxGbSatVhsIkax"
                + "2kRMBpvYTQajYDWLv8Ag/gDfhIFdOI7b9eLB95jZ8L0wbM4YU4TUIsItth1U4e+IcITlDS9YQQGCI8Iv7QXUoQQinjKDIkIt"
                + "imjMowcdeMAQvGILtXhnGcAMJnCFLiRKlFCLZ5Y27GEDJ2hBbOKEWjywNOEGF5BfUWFGJolQi2uWGnxA7r1k5sGJj1CKIpuz"
                + "NKAMfXDiK9Tyk0X+75HpJFToSOxHJrSvEbZnNwy7m936AQAA//9brpUoAAAABklEQVQDAI8+GinfMmcuAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAvklEQVQ4T2NkoDJgBJo3FYg/AXE3EL+j1HyQgZxAXATEaVDD"
                + "JwPp7+QaDDIQBkSBjAYg9gHiFiCeD8R/SDUY2UCYXjUgowuINYG4GojXAvF/Yg3GZiBMrzWQMQOIfwBxARAfJcZQfAaC9DMB"
                + "cRw0CM4B6SogvoLPYEIGwvTCIq4UKLARiOuB+AE2g4k1EKYXFnEJQIHZQNwKxK+RDSbVQPSIWwAU2EANA3EGI7kuHDUQEQKj"
                + "YUhM9sevhuphCAApYRoVT9kKewAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAqklEQVQ4T2NkYGAQB+KXQEwVwAg0ZR4Q/wLiNiB+RKmpIAO5"
                + "gbgaiCuAuBVq8HdyDQYZCAMKQEYVEPtDDZ1IjqHIBsL0m0INVoUavIwUg7EZCNPvCQ2KP9Cg2E2MwfgMhOmPg7r4MtTF5/EZ"
                + "TIyBMP3FUINXQg1+gs1gUgwE6eeDGloCNbQJSIOCBA5INRCmUQlq8HogvZUaBuIMRnJdOGog5bE8GoY0DEMATKUZFZcLTogA"
                + "AAAASUVORK5CYII=")
    public void rotateMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rotate(.5); context.moveTo(1, 1); context.lineTo(18, 1); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQElEQVR4AeySwQ0AIAgDq/vvrA1xg5NXIaG/QnOw9blaBx6H"
                + "JW271JqwNlAJT7jMj7TtmisXBSbhf8jgPXcgwwsAAP//gPpVDQAAAAZJREFUAwDyABApkn0/fwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAN0lEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZowZSFIq0D0OKnAfT"
                + "TNN0OOpC8kJgNFLICzdkXYM/DAFt+QYVhcRJbAAAAABJRU5ErkJggg==")
    public void rectFill() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.rect(2, 2, 16, 6); context.fill();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABG0lEQVR4AdTSsStFURwH8Ms/YBJ/g8iglIFiMBjEYJBSlLKI"
                + "Una7gSwWBiWDRAaDMhmUMsgi+QPIZDf4/AbDPTnPu7e3vNf3c8+7953z7d53bmfR4k97Fq77FzZ44oxVumkq6SNH2YSVj0xy"
                + "whT3RLGhcdLCDtN3uOODS6aZZZljeskmLVwy85U0zy4M88Up2aSFUTaSnV0Ua34bJ5u08NbMeDRDvaSF52r6GKBW0sJPLbEp"
                + "h8ZaSQuj5MDhhX0q56/CKNly6GebSskVvmtZYYxKd5or1FO8OczTxQNNbVSjQh1F3OmiL0dccMUMPYyySSn/Ff5Ojo2KF/7a"
                + "hQVuGOKbUpotjEXxSkXxnJNBdtmjlCqFpYW5k5YX/gAAAP//sifpEAAAAAZJREFUAwDzBigpOdOYUwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABAklEQVQ4T72UvQ4BQRRGbaOT6L2AQqHTeAK0HkAnkSgoJOIn"
                + "QYSEREGn0mppRCVRKMVPPIBapVBIOLeQyMTuzLKxySlm987ZOzvzreXz+LI89vn+IozSdQWCcIYpzE1XonYosi404AoyToMf"
                + "qrDRiVWhdDOAtTIxzLgEe+g7SVXhkuIibG0m5bgfgrKdVBWOKVzBxKGLFM9mpsIEhdJFUvetTIVSt4A8nL6RfjqHMURZyHgl"
                + "FI9szA1GbqVOSWkju0PNjVQXvQ4yScvQVKoTiqcAEejBUSc2EYpDNqoOD5A07SAAcWi9v8RU+Joj51SyLWm5QBMOvwh1K/7P"
                + "/1DbhVPBEzD3JBVTQqvpAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABAElEQVQ4T72UPQuBURSAfazMTAblP4jBKCOF1eAPIJvB7iMx"
                + "KYMyIbKJzeRH2GxmVvKcQrlxP0pOPcP7vvc8nXPvPa/X8+Pw/tjn+YswQ9VlCMABdjC37UStUGSSXIMjxKAIfujCzCRWhQsS"
                + "tjBUEnM8N2EDdZ1UFUpCD9YfkoK8G4MPst+kqnDw2Le+pooQ3062wjwLK5Aw7ZWtUNbtoQ2yn87x6R4WsDQgCWdX47eLLRVG"
                + "QU7XKXSTssIUhriLUSeUSZnADUq27dvMcgtZGpqwNFVrIxSHHFQVrjAFmfEIdEA6eYWt8Jkg9zQFMuMXGMHbVLkKTR3/539o"
                + "rEK34A43YyQVd6W5xwAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABBklEQVR4AdSTMQ6CMBSGq5uDq7uzJnoCV2HTQVfdTLyMV5BV"
                + "DoCzJ9AE4sgxHPGrgcTSvgoJi+T/81r6+vFegb7q+PpTYFGoAG/wpO2JWC0DuQJJ8AWnzO94x7iRDCAbA3Yt8bdmTM6sJXjM"
                + "2CsDSOYQS9IPy4AupAR9vw586pseD1iLsSgD2OuplMwH9mlElbpaZ44BLDNOZfSFtbRoAakyIlm/aYKolbRiAcvEI/GFJYlt"
                + "O4FUmUPS5+SDOtt2AoEpoDeiD+psWwQCq6D693OdqbNtL7CE5lQbMt7j+idltf0TCOQjoBGeM5niLQ6ZH4iGGgOrXUAyHGPX"
                + "MajWwAosxc6BbwAAAP//HE25RwAAAAZJREFUAwAtrTgpYLf2KQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABKklEQVQ4T2NkoDJgpLJ5DPQx8P9/BmGgyxWB+CojI8N3UnyB"
                + "4kKgQcxAzVuA2ANqyF8gvRqIW4AGXyXGYHQD7YGaDmDR+B8oNheIS4AGf8RnMLqBPkDFm/FouAlyPdDQB7jUoBsICrd7BLz2"
                + "CCivADQU5GoMgBHLwHA8C1RlRMBQe6CBh4g10BGocB8BA6cADcwlykCQIqAr1wCpYDyGvgTKSWLzNtaEDTRQEKgB5HVQmOIC"
                + "dkADD6NL4swpQENVgIqPALE4DhMnAQ3MJ9pAqNflgfReIFbGYugLoJgUurcJ5mWgS4WAGruBOAmLobZAA0G+gAOCBsJUAg3W"
                + "BbKrgDgMiJmg4k1AA+vJMhDJYC4gWwuIuUFhDDQQlN9JdyGuqCYpUog1hKYuBAD36EAVpWqveAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA7ElEQVQ4T73UgQ2CMBQE0HYD3aAjOIIjwAayiU5g3MANYAPd"
                + "QDfQDXQDvEtKUuH3/5IQmhBMgMddafVu4eEX9tw6YN+7gOTBe3ef22CSENgZyAHHJmJEL8C7EvwPjMkeCZYaHdDaQsfgDg8Q"
                + "zI0nLjSAeRbHGGTNj5HiC3BbBPIm1GZCJtVGnZtT6aNYtfmiK8BGeqO4DpHyhpv3SsRs7RzIuSSqVRdrZ3dKXEIvJaVYW916"
                + "QJmwxREEWKxt7mWgrE9UmtNJbRMckgGu8JvbMk17wtc+pumLwQQmyKl4SztmNmgs+JX+D60U2vUf7SQ5FX401SMAAAAASUVO"
                + "RK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAvUlEQVR4AeyQsREBQRSGjRmhDjSgArFAqgG6EFACVYg0IBWI"
                + "JVINyAUa8H0zBDfrrd0hczf/d7f33rvv7rbb+fHRCr/f0JI97NW8JifsI9rDHIqTE+6wXGALxYmEYwwDWEJVIuEIywGqEwk/"
                + "iTYMrCBJJDwxOYEo9pxJ+pHwyOQV/BIujViz50yj4U0ktDfjNIQzKBHX1uxRTpMT3hmfwgJuT1xbs0cpTU74mvbX1tyIa5Zx"
                + "SoTx0286fyh8AAAA//8Yjg1hAAAABklEQVQDAE+RGClJbfH3AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAArElEQVQ4T2NkoDJgpLJ5DKMGYg1RcaDoS2LDmlAYJgINegPE"
                + "m6lhYDfQkG9AXE+sYSB1uFxoAJRrB2JPUgzDZ2AeUFIZiPOpZWAa0CBjIE7HYeBsoPhJIJ6DLo/Py0VAxXE4DFwKFO8A4svE"
                + "GghSVwb1NrorQa66DsS92CwjlGxAhupBYxuklguIz+EyDF+kIFsOMtAciP9Dww3Dm8iKCbmQ1EgeLW1IDjFMDQCsfxYVL43f"
                + "UAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAt0lEQVQ4T2NkoDJgpLJ5DKMGUh6i+MKQE2j8TCDeD8TzibUK"
                + "n4GLgYaoA7EZsYaB1OEy0AQotxCItUkxDJ+BZUBJGSDOo5aBGUCDDIE4HYeBoLC9BMRT0eVxedkGqHARECvhMPAqUBxk6WFi"
                + "DQSp2wDEL6AakfVNAXKEgDgKm2WEst5aqNe3A2lTIBYE4pNAnAjEv8kxEKQHlGxAsc4DxLuB+DyOYAALE3IhPr1Y5UYNJDnI"
                + "MDQAAHzHFBVJ6doGAAAAAElFTkSuQmCC")
    public void arcStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 4.3); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0UlEQVR4AeSULQ7CQBCFBwSOEDyOG2DQCDQJlwEHSATHwCKw"
                + "CAwGT4LCkaA5AXwvXQR/zU6zpmnyXjv7duZLd0XrZja3Yp79mhOQ3IpAF/b9IfYCspdG5QA+0hw2o+jItaxM8xQwDSlQKg4c"
                + "cA3TYNWUfukOG4xt8Qq3g1Ura7J2ScAxE2fcw5Ng1crWrF0SsMWEQLzepKxD4jq+gBeG/mnHRh9HS8Do5phGAbs5jUP2jjha"
                + "At7pXuJPKbsS7nG0BNzQPcI3fAhWrezE2vM3tycAAAD//+zgow8AAAAGSURBVAMAwIUvO4TGjUoAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAwklEQVQ4T2NkYGA4AMVAimJgzwg0ogGKKTYNaED9qIEUB+No"
                + "GFIchDROh0ZAB5pAHXkKSF8gw8HwWP4F1KwOxD+ghnAC6StA3EWioWAD9wLxTSDOQtM8E8i/S6KhYANBXjPA4ZIlUAMvEelS"
                + "sIFnkMIOXd90qPxcahk4G2jQSSCeQ4qBF4GK9XFoWAoU7wDiy6QYuBuo+B4Qp6NpArnqOhD3EmkYSBk4DPcD8W8gFgdiUPJh"
                + "AmJWIH4GxMdJMAyk1AEAvZs0gSroYl8AAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAsUlEQVQ4T2NkYGDYD8QHgZgawJ4RaEoDFFPDwIZRAykOxtEw"
                + "pDgIGWgbhuZABxpBHXkOSJ8kw8FgF7YAsQwQhwHxUqgh0UB6FRBnAvF3EgwGG3geiH8DsRmaxlNA/jUgTiDVQJBhrDg0fQOK"
                + "O5LgfbALzwKxMQ4D5wDFU0h1ISgCYJGBrnc2UAAkP51IQ8EupLqXqR4poOKfD4h1gPgSEINcrQvEV4B4CxD/JdK7IGX2AF1J"
                + "M5M1IFdKAAAAAElFTkSuQmCC")
    public void arcCircleStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(4, 16, 4, 0, 2 * Math.PI); context.stroke(); context.strokeRect(0, 0, 20, 20);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAgUlEQVR4AeyQsQ2AIBBFib2FvUMYGysLp9IRbKycwQVsHcR1"
                + "fCRUcjEHUh75n0su3AN+5QovA/4PVJPhyDVL8ET9lAbYQWiCN+qJayxKA9yZnIN76o0PLEoDfA96eEtT/H4OEJa72AYcKRfo"
                + "X7lGNBq5QEZlGVDOJaVrGaakJZ99AAAA///kBf+kAAAABklEQVQDAHKaCSkUoB16AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAa0lEQVQ4T2NkoDJgpLJ5DKMGYg1RWaCoBxCDgucUEF/AF+7E"
                + "hGEt0ACQof+BmBOIrwBxFy5DiTEQXe9MoMBdXIaSYyDIgiVQAy+h20augbOABp0G4tnUMhBnvJDrwlEDESEwGoaUF7dUD0MA"
                + "jksLFQkR+1oAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAcElEQVQ4T2NkoDJgpLJ5DKMGYg1RSaCoExBLAfE6IL6LL9yJ"
                + "CcNKoAH6QPwBiIOB+AAQh+IylBgD0fUuBwp8BeIUbIaSYyDInEdA7ALEt9ANJdfAxUCDDgLxHGoZiDNeyHXhqIGIEBgNQ8qL"
                + "W6qHIQCR/wsV2AyBPAAAAABJRU5ErkJggg==")
    public void arcAnticlockwiseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 4.3, true); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0klEQVR4AeyQoRECMRREb/AIPA1gMWgElgYoAwElgKAMGsAi"
                + "0BgsDeARNMB7M0HchZ/JDDi42b372d3sTdJrvvz8Cz+/0Jo7nPKbdaIzY4xSYZ9tB7iDg0RnNT2kHKXCPfErHMNVorOaHlKO"
                + "qNCjDYlbxKcFNT0zLcNFVDjBPMIIemYyPyrMgrVCVHimYAYj6JnJ/KjwRPIGt7ALNT0zXa+JCg0ueI3gBVoindX0kHOUCh/E"
                + "53AJ74nOanpIOUqFr7RH27CQzowxagrj3W+cHyx8AgAA//89TZ+gAAAABklEQVQDAF0MHikQ7OLGAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0klEQVQ4T2NkoDJgpLJ5DKMGYg1RI6CoCVTmFJC+gC/cCYVh"
                + "FVCzOhD/gBrCCaSvAHEXLkPxGQgyTAaIs9A0zwTy7+IyFJeBIG/mA3E8DpcsgRp4CV0el4GpQIWGWFwH0z8LyDgNxLNJMdAU"
                + "qDgNhwtBBp0E4jnEGmgAVFgExHE4DFwKFO8A4svEGghSVwbEykCcjqYJ5KrrQNyLzTJCyQZkqB4QfwZiFiDmAuJzuAwDWUDI"
                + "QJAakIFmUNeAwg3Dm8guJcZAHMGIXXjUQJKCC6tiABZOHBWsf4c4AAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAsUlEQVQ4T2NkoDJgpLJ5DKMGYg1Rc6CoEVTmHJA+iS/c8YUh"
                + "J1DjdCAOA+KlUEOigfQqIM4E4u/YDMZn4AKgBi0gNkPTeArIvwbECaQYCPLmfiDmwuG9b0BxR2zex+VCkJdA4ZaKw8DZQHFQ"
                + "eIKCBAXgM9AYqDIFh4FzcMnhMpDqXgY5jKqRAjKQA4hnADEs2YB8EwXEoGSTAcQ/SIllZLVUS9g44gO/8GhpQ1awoWgCAGzL"
                + "HRVrJ2zIAAAAAElFTkSuQmCC")
    public void arcCircleAnticlockwiseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 2 * Math.PI, true); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAr0lEQVR4AeyRMQ6CUBBEH8RGr2Nta6sXIoRwIWltrb2ONgZ0"
                + "Fip+5odEY8UmC8vMnxeyv+THtQK/X+ifd1ixp+FCzSNaszR8+T9UsOTGwImCbbRmafIM0wM3VAGZBwWXN9enbw/sOU5n0lfG"
                + "88AUs0jxwJKrJWQ8D3zRMvBMoNLkJcYoeGDLnZ7D52K6AAtU0IUmb8wnTw/UUQVrzjTsojVLw1ce6HPWWYF2NYuNNwAAAP//"
                + "XYrofwAAAAZJREFUAwCFiigpn6iF9wAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAn0lEQVQ4T2NkoDJgpLJ5DKMGUh6i+MOwgUECaMVsILaAWnWc"
                + "gZUhnaGa4Tkuq3Eb2MogyfCb4RRQowya5idAvj5DA8M7bIbiNrCBYQlQQzQOlywEGphAqoEvgBrEcRj4EmggKDgwAD4XUt1A"
                + "qntZCuifk1gjhZ1Bj6GS4T1pXgaphsT0TCDLEqr5BJBOBYYfKDiwgtG8TOu8TIb5VI8UAIOoHhWKBN6HAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAfElEQVQ4T2NkoDJgpLJ5DKMGUh6i+MOwmkGegZVhItAaU6hV"
                + "pxl+M+QztDI8xGU1bgMhht0AauRA0/wDaKgGLkNxG9jAsAFokD8Ol2xkaGAIwCaHz8CnQA1SOAx8BjRQesANpLKXqR4poACi"
                + "arIhM42PFg5kBhySNqqHIQDrciAVd504BAAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABLElEQVR4AcyQoU4DQRRFDxv+AQ2CoPAgQWywOAzYJSgUrjwI"
                + "CoWiaBQO2yAQCPgAVA3hG1AkkLZvdmf7ptt222mapjdz983OvHtmMglz1gKB1+wgNNVt9Z+3mzdxe4zW6Bs6UId3jWTqTfWq"
                + "t5tnuD3Xo4vVMQwUWtrkQFpqR0bRS6hBYHFqGjZMmKcUGUoZsHiXaW5WZsuahW9qwA7HZUd0DbIGhL1okAX62RC4YfvRs342"
                + "BEZTfOBJ6746HyHwK1+Z7vOrbXf8s4VwpH7DKwS++rW68k2XC21YQzjnhjYVGTDhsbJnvyt86M8JwjpX3Gr9YYwM2MhDD5W+"
                + "Z71RyiW7SM2BmAzo1oRTLS31PQnbCId6oxciNAh0QeEA4YwGn8ygYeAMkDCy/MAeAAAA//+6th9mAAAABklEQVQDAAx2OinL"
                + "04YhAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABPElEQVQ4T83UzyuEQRzH8feiuHJyUy4S5e6u3PwBDpy4iBM5"
                + "iKndC1qJ1hEnB3EnRykXKfIjtVt7cuWg/OYz5Wlnxz7MPm3y1Pcw02deM8/MNClq/KVq7BEPZmjjlUFN2K/qUtnsqeqEBnLM"
                + "UKy0mO+gURxWVCOq+pg/eFb/Ai1kGOfJzZSD2wIu2VVgIHArDoX2uWg5aDQrTAZiUSyHYSxqlMA0HbxxXSVm4++qTqE3tlEC"
                + "DctqTyQA7ZBFgVM+eK6O7oTgmcAeH3xUR2NC8E5gsw9+JMDsmB3VksBjH8yroz0QfVBuXTc2619w91A2FBr+BbzVMa5qY9aY"
                + "5r5S1gV7FTiKAa8EzdPKFqO8/DSpf7E3FR5yBuwLyjLHQeBWeI+DoUkD91QF6gTNchEKRbk/fL6qXdpX/v+v8BPqrD0V/Gmy"
                + "MAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABBklEQVQ4T82UjRGCMAyFwwa6AW7gCDqBjuAIMoFhAmUC3UCd"
                + "QDaQDXQDGcEXpJgWKj/neebu3QGJX1/T1IC+HMGXeeQHMk2x2BJaQCGUQ3cog2Li4r0WdSDTCFUraAPJsy8Eym6yCXgsnXXp"
                + "RgrkXBfaQKY1ktsuJFVjOX0DXz27tGzTt9YETqW/6lCGuTML7ACMXKC4m/Xcrimveqm3fEM2HAjM4XDsOhwKTAGKAJT5tHp4"
                + "xbscTJeQoT5BiQGZH+ktMz7KMH8KASXQwZyqW6yBciseHpps61yCGq9c3aF84WKoZbhNCCiGMt/d9Ts0GaY9HkOoanRLG6z0"
                + "D/+++thStf/v8AmQKzYVpXVGrAAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA5UlEQVR4AdSQyxHCMBBDPbRAQxRBHRyogApogisN0Acd0Ah6"
                + "wyjYjj97yCWZVbKz0spyDmnjZ5+GD/2FWxBoJf1XfeWzqJMQLbTsLPra8CnmI7yFWUo0aNmR/Fe1IdO7XldhVmjQFrqWoU8s"
                + "rlJspWTO2oVuGUJyMgnoW4BDs+J6hj7ZSfJFz6zJudQzREQCktDnYAaXz5Z+ZOgETsSSe3PMCowMEZKERPSAnhl9EzNDJyEZ"
                + "wMQz+hVmhiyQiGSAnlkXEUMSHeUA6NX2K2LI9kUvoM+4ooYv2QB9xhU1HLtk7OaGXwAAAP//3VWVfAAAAAZJREFUAwBZ+yYp"
                + "OJqsPwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApElEQVQ4T+WT0Q2AIAxEwYncQDcxTmLcRCfRCVxJ2w8Iadoe"
                + "JsQfSfrV412BI4bGKzbmhU+Ay8up11IvJxyouVNtldCJdDPVmfTakQ9qsmsWGXA2Z93oTcg9VahAVWPrUdCUpqkFRFOahl5s"
                + "rE2umQe0NrrXgYItN6OrgD9FAtBjQSCnJUFuLXcyTujIZS4T0A18DZChF1VH1aMvWQtEnNz/IfABG4IjFfQNX+kAAAAASUVO"
                + "RK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAr0lEQVQ4T2NkoDJgpLJ5DDQ3sIFMF8P1obswBGhgPxDPJdLg"
                + "ZKC6QiBeA1OPzcvHgJJ9yIpwGA6yvAiIrZDlsRmIVSEWQ7FajCtSCLkSp6W4DCTkSpwW4ks2uDThtQyfgbg04g0OQgkbXTOh"
                + "oCCYU9ANIBRZBA0EpRaYISA2RrpDT06EvAxSD3MliE0wwRNjIMigm1CXqBPKksQaCHIlCMDzLC6DiTWQkMPg8oPfQAB2pSIV"
                + "qh5VTAAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALklEQVR4AezSIRIAAAgCQcf/P9pOPIlnM0DYYad8Fv5BNdQQ"
                + "CDgbgBYRDQMEvAcAAP//GaAxmgAAAAZJREFUAwCLOAAp5PB2EAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==")
    public void closePathNoSubpath() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.closePath();context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALklEQVR4AezSIRIAAAgCQcf/P9pOPIlnM0DYYad8Fv5BNdQQ"
                + "CDgbgBYRDQMEvAcAAP//GaAxmgAAAAZJREFUAwCLOAAp5PB2EAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==")
    public void closePathPointOnly() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(4,4); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALklEQVR4AezSIRIAAAgCQcf/P9pOPIlnM0DYYad8Fv5BNdQQ"
                + "CDgbgBYRDQMEvAcAAP//GaAxmgAAAAZJREFUAwCLOAAp5PB2EAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==")
    public void closePathTwice() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.closePath(); context.closePath(); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0UlEQVR4AeyQ0Q3CQAxDK1ZgIf5YgDn4YAGYgCX4ZQH2YAMW"
                + "we9OVtMole6jn63inmNHvlMO08ZfDDwX2VF7yX8koEmay4EXSXchFxoeOGVTPRqeaC8Hvns7RdMcD/w08xX8SjganuReDqR7"
                + "6ncTXHA093A093A09+2Mgb6JlwEGrEWOB6IGb4iBCNzIzQCOFoGGB+DRazwH8qKjHAAXXRQaHoAvTJociHbVD+goCw+UZhX4"
                + "0STQURYeKM0qsBwcFffA0U2tz+07XN/NqLP5Dv8AAAD//6/XRzQAAAAGSURBVAMAxh0iKRS6NNsAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAiklEQVQ4T2NkoDJgJMG8ehxqG5HFYQbaAwVBEg5omvZDxf8D"
                + "6YVAvABNPh7ITwTiAzBxZBfCNMMk0S0hJA82E9lAUg1AtwDDQJAAshcJBQE2eRQXggyEuRIUZiAN8LCBhhEheQwDQfrOATET"
                + "EBvgiFW88qQkGxzmowqPGkhUMOFVNBqGo2FIRggAACy9IRWISF6KAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAm0lEQVQ4T2NkoDJgJNK8BgLq4PIwA/mAGnYAsRWaxmNAvgcQ"
                + "uwFxPxDPRZNPBvILgXgNTBzZhSDNfUiSIUB2EZIlhOTBZiIbSKoB6BZgGAgSgCkCsZFdB/MRIXkUF4I0wVwJYiN7H2YgIXkM"
                + "A0Eab0J1q6NFAIyLVx5bsgG5AgTgMYdmMF55YtMhDsdiCo8aSHRQ4VQ4GoYjIQwBM+0eFQy1UnkAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAdklEQVR4AexU0QrAIAi8tT/rw/2zsWXgcMIIUWEPCw4tvLwO"
                + "soH28wE4l+E3J31Z3qgDGkuGKdBczvMVdhzbBAF94PbTKHnbTq7cMWK+QunM79eQc2+sU2i98CqT+jqF0iEaf4VRB4EPelg+"
                + "D6Ou6f/PebqHFwAAAP//nUdSmAAAAAZJREFUAwCqLTcpdpR/5AAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZklEQVQ4T2Nk2Mf8nwEZOP1lROET4qDpZ6S6gfsYUF3oxECa"
                + "C9H1M1LdQHgQURqWUIMQEUBtAyn1OsyncBdS3UBCyY1YedISMRGmjhpIRCARUDISwhCjPKQ02KhtIEZ5SLED0QpoADR/JhX5"
                + "NimNAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAZklEQVQ4T2Nk2Mf8nwEZOP1lROET4qDpZ6S6gfsYUF3oxECa"
                + "C9H1M1LdQHgQURqWUIMQEUBtAyn1OsyncBdS3UBCyY1YedISMRGmjhpIRCARUDISwhCjPKQ02KhtIEZ5SLED0QpoADR/JhX5"
                + "NimNAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaklEQVR4AeyUQQoAIQhF/9TNOrg3GxqE+WALidJ2BR8hEx9v"
                + "UYHUrhHUboPNUzbn3LEiDdA0vI+NOzFp5BOSSj0OmZB47XxCblKPNrxfrecI6ZJ1lYzvzxFyQ7RewqhB4DoMOPx//nSHHwAA"
                + "AP//+UMdZQAAAAZJREFUAwDnWCep6qrEzgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaUlEQVQ4T2Nk2Mf8nwEI9jmBSARwYvjLiCpCHI+R6gbuY4C4"
                + "kFwXobubkeoGwm2AhiWc70RuGMJMoLaBMK/DzCc3TOFJg+oGEpfKCKsiK/HiM3bUQMKBTkjFaBgSCiHC8tQLQ2jhQnUDAUAL"
                + "HZUFr6Y/AAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAaUlEQVQ4T2Nk2Mf8nwEI9jmBSARwYvjLiCpCHI+R6gbuY4C4"
                + "kFwXobubkeoGwm2AhiWc70RuGMJMoLaBMK/DzCc3TOFJg+oGEpfKCKsiK/HiM3bUQMKBTkjFaBgSCiHC8tQLQ2jhQnUDAUAL"
                + "HZUFr6Y/AAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAd0lEQVR4AeySUQoAIQhE3e1mHdybLW3jRwRSSsxnwUTo9JDB"
                + "V8iHC9TSDKhSGmNQrSIGrPI9DCAYBsSDIQxGBWIoB0SeEJoncsATyPzHAZEDNEx9FQQahf3DAff2uBsDa18pKGaZIwaaLX9d"
                + "YD6rlfNmuEomX/8BAAD//wdK4uwAAAAGSURBVAMAVhUTKWG0g3UAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXklEQVQ4T+2SQQoAIAgEjX7Ww/tZVHoKCrZyj3pUGNZhk5An"
                + "UXk1dwNWyb1Ic8OV44asH9KBCqcmPAI1th5+nW4J6cCtRrMKtit3LcAO6cDH5uOEAUQGwiEyhO90hwOflBRVULNh6QAAAABJ"
                + "RU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXklEQVQ4T+2SQQoAIAgEjX7Ww/tZVHoKCrZyj3pUGNZhk5An"
                + "UXk1dwNWyb1Ic8OV44asH9KBCqcmPAI1th5+nW4J6cCtRrMKtit3LcAO6cDH5uOEAUQGwiEyhO90hwOflBRVULNh6QAAAABJ"
                + "RU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABnElEQVR4AayUyyttYRiH1zmjMzinMzh/wmFGRkaMJFJmLhMj"
                + "DGRAkanIRCKXEpmYuIwMlUvGKCIZKDEzIqWEGOB52Ovb9rbI3tn9nvVevu/9tS7t72f0zb+3htUJ3vZqE/oftmLDenb0QbbG"
                + "aSxCAXxJseFSarfGpiVcpuE3bMMK2CN8rtjQXaNcBmAdVqEOtmATlNG1GouPeGtYxCYf7YjYASfQCP1QCHtwAbNgj/BesaEb"
                + "qlhuhVLoBu+YENRM5p7OVHSGNFMa+ghttP2ic8R/KeL3Svki71jTKap2cMZZ0rQ07KLsgWtQ3oGYZ7NMYwgmwRlnSdPSsJhy"
                + "AWL5QeSJRhLD9MthHsog4+truEEzX/1g0HdOeJWGp69pXtdfTFVAkIahyCO5Z+YYgjT8H6rck0tGziBIw8pQ5Z78ZWQHgjQ8"
                + "pGqCXPXAwD4cQJCGY1Qj8Adyke9vMHtAQ0+SGRbW4Kum/gm8EWcZS0tDK/+XniQZX8yFLB6pb2ACks7PKDZkPdK0JYoiz79b"
                + "4hX4WHfEc7C3S2yAXkjUMwAAAP//vvA2KgAAAAZJREFUAwBGz0v7qwwAqAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABgUlEQVQ4T62UuS9FQRSH3/MfWEIs0SoUtkhEQ6NBgiglKqIQ"
                + "nUQiYkskiKVQo6MRa9Q0hALR6RSWWGLXUOD7vcxNzrt5uO/lnuTLZOae+e6ZO3cmGgk5oiH7Ip6wBvEI1PpesEe/CLKCvthW"
                + "uOOku7Tp0APtoH4J1MHzf2IrVJXjcAZN8AYHcAFVUAHLMAiXv4mtsJKkbZiFfVet/QRbjJ1CMYzCSSKpJ5RsGCad6Nu1Wq6N"
                + "a1fpPO0AHPulEha6qlrdQyWlQWmCCnIZO4I8WIduuLJ5Ei7CKmhJQaKRpC5YgAbo8AsfGcgwg1pu0PggMRtevQmqcAL6UhQ+"
                + "MK8T1sISyjME2vVYqMIp6E2xQi25H2bCqvAWkTZowwqf6OioeZHMpnwyKRPerVDbvwn6rxTJCJfIbzPFxL5hAcxBS5LCc/Kr"
                + "4cYvVL8MxqA+YIUv5Oky0dmOC3s5lPNEN4lumr9CR03H9DBRkv/GzidpGprhHnQKvuAOcmAF9IvFLdOKfwCGhFAVxZXYrgAA"
                + "AABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABiElEQVQ4T62UTStHQRSH/RdkIRtLRUlslFIWLKQsWIjEJ/Cy"
                + "sUCSslI2skFZUN6+ALKwULL0XvKykFLiM1DewvOrMxrj3v4X99TT3Jk585uZe86ZTE7Klsmi18X8E2wl3dcJFrJgG+qDhVf0"
                + "i6EGbpKI+ifcZ8E0rEEujEAfnEEZ1MJrNlFfUNcbgzvogEc4hFvrF9FuwLD5RGr7gr14LNgJN2kHgl+wR/8B6kx0KUrRCUps"
                + "Biah1Rzd9d26cj4OYArGYQiWQ1EJlsIlNNuCa3OqjDiBNluFfhOror33/SS4DucwYRP6lzIFJ8oUrHbYgWroDAXfGciHF5v4"
                + "iBGKG1ZGvLlJnfAUlGfOfiOopG+EI19wkY7y7S+C2rwNvipJJ/yPoBJ9EObTurLysgmOfcHUgxKmTdKgKCAn0OCH3yX2BYMt"
                + "oEpIKviMbwX8SGxt0AOzoGpRzWYz5Z0eibnQ0X8cJKr61dsYZ7qmbDRKTBPhi13C2AoocopgngmoigpgF7rh2zX93T8BvvJW"
                + "FWV1b98AAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAuElEQVR4AeySzQ3CMAyFcUdALAMTUQmmAYmeGYQjbMEEiAUq"
                + "NbwgXn4OJnKgPbXKk+s072vspFn8+ZkO6JbHtVsdTtYC9B027grYFjINHWjCxMUzMPai9m28HuLO9ZCj5LEXL+aF2LOidId3"
                + "TlbE4E2BlwoQLcEbgYOc+dUcE28AynN3A6iDrKP7eN++APQZDqGFssNg/iW23ktlQE7+EnXgIBuAzS1Qgb4vKDMrBz8oDhVY"
                + "dCoLXgAAAP//d8fzjgAAAAZJREFUAwAZhFYp0xyimQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAyUlEQVQ4T2NkoDJgpLJ5DDgN/C84UY6BiTGS8W1eJymW4jZQ"
                + "ZPJ/kEGMb3JJ8sWogYjg/z/8w/C/8OQPwFTJD/M0LLnAvI43Lf5n+Mj4NlcAnMxgCoEaLwDZ+mQZyPD/LOObPBNUA4UndTAw"
                + "MpaTZeD//53AHFWBaiDfRFUGVsZrQENZkHMIQS////+H4TezJuOn7DsoBoI4/4UmzQDm33SSDPz3fybju7wMuM9wBfYQSIfg"
                + "4ospApgcuqhSfJFiCLJakso6YiwBAMSpYxVbkG1GAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAArUlEQVQ4T2NkoDJgpLJ5DDgN/C84UYeBmTmI8U1OEymW4jZQ"
                + "ZPJ/kEGMb3JJ8sWogYjg/z/8wxDoxadAD0vBPA1LLjCvE0iLz4DqpcHJDKbwv8ikTUCuL3kG/t/M+CbPD83AKXUMDP8byTOQ"
                + "sR6WoxAuBGc1pstkGfj3ny7j+/wrKC4Ecf6LTCkCkr3IWY5wGDIWA13XB3cIrsAeAumQ2sUXKWUgslqSyjpiLAEAdMteFUCj"
                + "kQkAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABIklEQVR4AeyTMUoDQRRA31pYKdiZeAArRQsLC8HKwsIDWIil"
                + "BxASsIqNYI7hCdKJIIiFpYXRKq1gsBGR2Gixvi9Zk0wlWQsFl//4f2f/vJkddieABnDRJ+pSFMJLhUEpmY5GCM0/F39KWJuH"
                + "vCWvkks9PQgHt+Ra8j61tCd95RkbZmVONpVWzWk8OzAtK7KqeMr8FanwFLKedIFbCbFpJM4ysIcHR19kJFJh7MoV89jZop0x"
                + "yfT9CGF8e+vQXYCbCrw/OV1RXbI963j+iYe6fQIbMbYE+1ewXIGDuC8IoXUR7XuYPILsEJodkqsJnV3QA23orUHrEd6G20IY"
                + "q/uXVO9g59yHcT82IdQRETvKjqMqw5CwjGYw9184OItxq99/hh8AAAD//6px7r0AAAAGSURBVAMAZkBbKcNIUoIAAAAASUVO"
                + "RK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA40lEQVQ4T2NkYGBoAGJ7IGYE4gNATBEAGQIC9VADQYZTBEa2"
                + "gZNNGRhyvgIDMAwaiJnAeJqBHKD/GRiygPwEIAaqBYMeYJiVIqtBCkOwgUZAQ6QgCv4D9TPC5CEiUAOBgmZAtgpQ6DCQLYnP"
                + "QKALGcOhBp4A0tFA/l2YBqiBjkBDQqEWPCdkIDEuJGggKA0CE3aVNANDOhcDg5wMxEVTtwCD4Ayyd6YBw86cgUHRmIFhFUj8"
                + "EwNDCR8wHNG93IAwMPAXUM8aZAWkstEjBSkMSTUKon5k5xSw90ERRBEYDUPKwxAA2I49lItsoRgAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA20lEQVQ4T2NkYGBoAGJ7IAaBg1CabIoRqhNkKAjA6FEDiQ8B"
                + "pDCcbcHAkPIBqDUcqr2QgYFxArJR/xkYCoD8ECC2hor3Ag0oQVaDbqAW0BA5iIL/QP2MMHmICNRAoKANkG0EFNoAZEPVQ3Rh"
                + "cSFjBNTAI0A6D6jkHMwFUAMtgJrAaoD8R4QMJMaFBA0EpT1gwm6SYWCIZmdgUJKFuGjOTgaG1BPI4TObgcFCn4FBxoyBYQ1I"
                + "/D0DQ6EgA0M/ehgiGejxk4HBDKyYXIAnDMkzcjTrjZY2ZKQcqicbAN2xPZWYt5iSAAAAAElFTkSuQmCC")
    @HtmlUnitNYI(FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAcElEQVR42mNgYGD4T2UMBygcaoCRaeB/EJ4NxO+AOBCPopVA"
                + "/AWIA4gxEKjovw8QX8ajyAeIg4D4FzEGsgExOxB/waOIGcYmxkAPIPYG4nO4FGFj4zNwGRC/BGJ3cg1ESuX/qZ1T/v8fzSmj"
                + "Bg4HAwGGsHedCdecqgAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAcElEQVR42mNgYGD4T2UMBygcaoCRaeB/EJ4NxO+AOBCPopVA"
                + "/AWIA4gxEKjovw8QX8ajyAeIg4D4FzEGsgExOxB/waOIGcYmxkAPIPYG4nO4FGFj4zNwGRC/BGJ3cg1ESuX/qZ1T/v8fzSmj"
                + "Bg4HAwGGsHedCdecqgAAAABJRU5ErkJggg==")
    @DisabledOnOs(OS.LINUX)
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAxUlEQVR4AbSOCw7DIAhAze6/A22X23gkElSktGmXPqd8Hrza"
                + "zb9HhR9Z9ncReqW1Nb/hWyPXDuv1wq+4QP5OffSANnkhAZvEo8jQMwuZBEVXoxasfhaSGCYSSFhqIyETIfFoihrQRz8iIbll"
                + "MsGJsGYnZDJMDnuSAwv0y05IPtyAhLDNZUI2AOkfPmIwBPsjE1ITbRLFqFWOhGwCWiwHd5Br/B0J6fIb+Tu5hYqQjTyLxAcq"
                + "QurZDLinVIV9w1RGsiqktsQfAAD///+F28sAAAAGSURBVAMA3NsuKRKJJt4AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAj0lEQVQ4T63USw6AIAxFUViZunJxZ7YDCJ9+Hg2dGDG9nJE5"
                + "HZ58uJf6YKH4Fbzgo72bd/sgH7zB4EN7DBqC/B5RNp0UjCibTgruKgedFtxRDjotiCoXnRVElIvOCnpKUecFLaWo84KaUtUh"
                + "QUmp6pDgrDR1aLBXmjo0WJX85Lg56P+whsqpoNdp31EhHPwBPdMeFdd8Eu8AAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAmUlEQVQ4T63U0Q2AIAxFUdjAUVzBzd3AVRzBPhMJSFseDU1M"
                + "NITr4YecFk9e3Et18JL4HvzBKfsO7K2DiCEaGcQQbYL4jiiLTgtGlEWnBWeVjc4KzigbnRVklZ3OCzLKTucFR0pVNwp6SlU3"
                + "ClpKU8cENaWpY4J/patjg7XS1bHBT3nLy3ujeMPeh1Bu8uDIS4KjTllnhXTwAVWiHxW3nihqAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABGklEQVR4AbST3UoCURSFo9frYeoioiCI6CG6qIuIiOgyioJU"
                + "/AUFL3wDRUFRwQtfQL91wOHMzJ7jGVBZ33bPPmsW54zO6cmBP0cN7LLZTSSv+Ez5O/wxHfnhnNEtmPIDf01HfnjPaAym/MA+"
                + "jjaEpFM8hgx+oHzfKgHOWLOe8wtzp2xg3U3LFR1fj8HdlQ3sMa1BGT1gHoJTNlDDqkok7/ieIZEV2EhWw82U5WtIyQrs4Ngd"
                + "+4++SHcsTCAlK1AG7XJNcwP/kNUng9RRuXYqCtSvfYljALqZr0RLugswVRTYxP0EUkXF44p+BqaKAn3ziIsvkD4oyZ+YPqeY"
                + "QN3UoqzgHIIqE6iwRTCNxdhAvUFv+PcqNnBv0M6wBQAA//+T4a7rAAAABklEQVQDAL2YOinNYVJ0AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA3ElEQVQ4T62UYQrCMAyFu8MIQ397Epm30CvofRyeQ/C3CB7C"
                + "M/gCnaRZ0r3CAmWwZl+Tt5d2aeXoVuYlDXwBviUPuCHv6OVq4BUJFwL4Rc4GS56z0ECpTqpcCqlMKnTDavjJp0f5d2wcaida"
                + "INu2Zf41tUC2bQ0sNPVss9S2ra7Q1AO2tD3T1AOybbv2iSbljb56rCfWPvirA96Pdi8CisHP2UIPx0qhfSKgtL3LBraa0pMS"
                + "+dVq2jQpEXSyUvOkREBp+5S1dC+F6UP2PtSa1ka5uA+riewmWyHLSz+ljSYVkapvPQAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAzElEQVQ4T7WU0Q2DQAxDYQNGKJt0BTZgBDpJO0pH6AaMwAgw"
                + "AonUQyHEnE8CJMQH1ruz5aSuLn7qi3mVBU4Cf5AHfEXXRVoLfItgIICLaFp59Xt4LLCRvzMBfInmg3Q+w5xtaDUd4IGsbX/B"
                + "7SAPZG1b4C7TqDY52/52u0wjYIntQ6YRkLUd1gdNSrL9E39PUJGwPgiotvt/gcdggqhJsRdR2wrUAvtM6UlB5feZFk0KgqZM"
                + "iycFAW2m4VJAo3dmO2V6uj9uXbDE5spLVqfJJhVVgK1QAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABBElEQVR4AbSRsRHCMAxFBXPSsAIrQQ8NJRULwBwZIvznO/mc"
                + "WDIpgNOPiSQ/9MXefvz5K/ClYeeNOqsvjHbCW9jRJyelTlIYLfAadvRJYED7ijIt8K13pCONiypIRxwtkI5qO1noQUueA9Wd"
                + "roHVdiXzM2NhnzWUrjUQy8gqubQNH6edGdDStAaSLMNBRSQGugi22GkErMMVck5jqmrV2yIggyF7mt29MTgXVr0eAakx3PQw"
                + "O+qlwHW20Vn1YgbENnawBdz7OclR43unDMhUvmzg7cXQqjdkQK+b/kXgiFxqlSL6CqRJwvbQqnpKbAVie2i10PTYBMS25DvV"
                + "tTw2AfPrfeUDAAD//0zqw3cAAAAGSURBVAMAkVVLKYjWD1YAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA7UlEQVQ4T62U0Q3CMAxE3WEq+OSTGZiADsEMMA5doCPACDAF"
                + "K8DZaqLUtYlBqVRVSk7PubPTjho/XWMelcAn4JtggRG6o6UtgRcIzgHgC5oeL39XTwncYvcRAA7QXD2dzrBm27WaCmhgtl0j"
                + "qxOOAEmmGphtRwOds+wBkkytsZHDRQOFdgAkZ2oBf7GdrXoZ8nrUtoxPsvoNyHtie0803YkOzogsrNaAbPvEJ3gT3RjudVUX"
                + "8+4y297xAAOoG25arZ0wFwZQN9y0GgayENA056uuRi0vdLNtyVR39V+gZFoOsNN586Z42tB68z/2B2u6MhWr0Q5cAAAAAElF"
                + "TkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA7UlEQVQ4T62UwQ3CMAxF3Q24caXzcGIDRqCT0FF6YY6OwJUb"
                + "bADfVhOlrk0MSqWqUvL1nP/ttKPGT9eYRyXwDvghWGCC7mRpS+AVgksA+IKmx8vfzVMCd9h9BoADNKOn0xnWbLtWUwENzLZr"
                + "ZHXCCSDJVAOz7WigS5Y9QJKpNTZyuGig0A6A5Ewt4C+2s1UvQ16P2pbxSVa/AXlPbO+Jbg+iozMiK6s1INs+8wneRDPDva7q"
                + "Yt5dZtsMHAHUDTet1k6YCwOoG25aDQNZCGia801Xo5ZXusW2ZKq7+i9QMi0H2Om8eVM8bWi9+R/7A246MhXPMagrAAAAAElF"
                + "TkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAe0lEQVR4AeyTWwrAIAwEJz15bm43pqUPECqI/nRhVER0iLox"
                + "OP+GKqhTuENf5tWwOFTUFfHVc4KhYwRkTCUNJBmiIudb7QTD42gDC+jMPMNLrIqGbEVFLHeudTlaYZgnv1tzCHA9g4DMekOj"
                + "cfthKdYbcsZ5/KhzerjhDgAA//9EkkqJAAAABklEQVQDACUWJCWDyd5BAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAeklEQVQ4T9WTUQ7AQAREubmbqzS0pdHsCh/dT5K3YwaE5ofN"
                + "PPgjkICdDbQ3xXvkKSCTj0t+XvI7VdgHVGFi5OklBkul8qk0bbYDbwfZp15VOAj0aZsVVo3pL63CEzkGxLCnoBdVVtgGvMaP"
                + "J6qNbYVzQCMHpXWFCfAAXsokEVEKzTkAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAeklEQVQ4T9WTUQ7AQAREubmbqzS0pdHsCh/dT5K3YwaE5ofN"
                + "PPgjkICdDbQ3xXvkKSCTj0t+XvI7VdgHVGFi5OklBkul8qk0bbYDbwfZp15VOAj0aZsVVo3pL63CEzkGxLCnoBdVVtgGvMaP"
                + "J6qNbYVzQCMHpXWFCfAAXsokEVEKzTkAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA8ElEQVR4AeyQMYoCQRREH7LZIrvBHmE33lDYZJncQG/gIRRM"
                + "jEwEPYQ30MBcTARDYz2CgSJmBlbJTGAzA92YOvyC6qrfD6ZrPH6Zjv1c9rJpUwDrX7D6hlkXhpa9M+HqUvTcgbq46MDfDj7G"
                + "8GbZO3MXTdNiTco+4dcQ+Ydx5k5hJkWNgY0WvFdt512jqg9zA8PsqbOBmzlcqih5t6nqw9zA5RG2PbiGpTN3ypdS1BjIAZpT"
                + "WP/AyRDL3pm7KFK+dAfKn3Xxfw/tCQwse2fupOgpgMUF/9pIB8teNm1CYNrtku0XsORREqPXGyY+WMn6DQAA//9HegNzAAAA"
                + "BklEQVQDALe2MilY1lPyAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABF0lEQVQ4T+2SvUoDQRSFv5SC5UIUK30ACUFIbyws4wMsBkO6"
                + "RJ9AiBhUsEqvstoKVgELwdRa5cc6pTbGLqTUM+saNsvGHUljkYFlmDn3fnvPvZNicmV13AiunrW3I3riMfUTkYYT0dwlcMzd"
                + "G7x3wNN+mEgJBfjAZTjdhsoVLIaTd2HYhPMPOLKFGmB2C24fYC0uKQ/9R9iR1rWBGmB5DxqXsBCXUIKRKq9KU0jy8oGy1vCm"
                + "AKWNbmBfcRfJODDAjCzfyfJqXMKmLLegIK1nC2QF6urVwXVkKK6Gcg9nA+k2MBMzfjaa9PE6FLU7n9/PZvAim69Qs4VNAIMk"
                + "McnpM8wnW5vhH44r/EsVv8XOgbN3ct7Df9jDL+YBMhUCa42EAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA4klEQVQ4T+2STQ4BQRCFPyT+V27AESQyC5cwQqzY2zuGvT0b"
                + "QnAJCyGO4AhiIRgJUU1P4mdkesJSbzpV/frlvaoX4vlYUuZ1ayX3/OXdtwxpRCIDvR2UauCoZh9iSZhuoS7lwZdJA26EQjbM"
                + "QXkB4cePBbisYbKBShBCKwozByJen2JwPkHR1L5S2BRP7S6Iw/fTgH0PUkEUmhC2hLBjQqoU/tyyWsooC/bSeyljWUrVRJ3C"
                + "uLGJ69jYKjbqYSCxSd83rGJzDEro4n8WbFMBvjjXsi/QFPAnNJ3UZ9x/ht/P8AqUpzEVnisiKgAAAABJRU5ErkJggg==")
    public void imageOnLoad() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
