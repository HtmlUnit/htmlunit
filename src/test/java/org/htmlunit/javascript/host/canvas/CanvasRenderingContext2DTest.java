/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAmElEQVQYV42PzQ3CMAyF7Q2QukC4ICbotR6IHTA7MJC9SRZA"
                + "4kS5GdsKUWlVCSuS814+/wThz8DkTmbTS4AfnJKHll3TrOAQJmhxRr9cWvt7y6Hdx3MD0Y42yQxXfiZx40Pm0KTv3461FBCi"
                + "BEgESq19+z56+R/zdWPN4L8+Riyh3LeBIkLMDKoa9hZcF/auew9rfzN6r/AD8z8wC6n68A8AAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAUElEQVQYV2NkIBIwgtTVC9f/b4hsZGhYXs8AohmnMIDFkQFY"
                + "oKFe4L/9QQOGg/YXGOobP4BUYVcI0/kfCBoaGhgaGxvxK8TnXAyduBQPBYUAzvAVCzcdbJgAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAIAAAAFCAYAAABvsz2cAAAANUlEQVQIW2NkUPv/3/7bAQZGe5n9/w3YLjAw7ue0/y+g9gHI"
                + "sOf4LyD5j4HRYb/Ef4O7vxgAjuARs3OGyksAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAgklEQVQYV2P8z8Dw/4A9B0NjgwDDQQdOhv+M9xkZsADG/Q4O"
                + "/z/wf2F4oPCC4aPAF4aGxg/YFTY0NNxXevKEw+HiRQHZV684GB8+xK6wqalJ39/AwERNQiKDg5XVhNHQELtCkHP+//9vD6Qa"
                + "gNiBEQiwupEkhdhMQBfDag1Oq6lqIgCfzScLcG6H/gAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAWUlEQVQYV2NkIBIwEqmOYegqVPufFvdhgcnCV4kgT6SBPIzV"
                + "M3Fi81bbih8NSVGZy8C4HqIGq8L99uyrJVT/hWjY/WZgjMOjkOG/4uqEBZ9D5ie+AakCGwYAh4cWC3rmpLIAAAAASUVORK5C"
                + "YII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAR0lEQVQYV2Ocrxn/nwEIEq8vZATRuADj/CoeiMK2L/gV7ndw"
                + "2O/w/78D48GD+BWCTJs/f/7/xMREwgrxuQ0mh9cUZAOGgkIA7moSC5sf8xAAAAAASUVORK5CYII=")
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
    public void drawImageDataUrlSvg() throws Exception {
        readExpectedAlertFromPng("drawImageDataUrlSvg");
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "    var canvas = document.createElement('canvas');\n"
            + "    canvas.width = 100;\n"
            + "    canvas.height = 100;\n"
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
                            + "overflow=\"hidden\" width=\"100\" height=\"100\">"
                    + "  <rect x=\"5\" y=\"5\" width=\"90\" height=\"90\" fill=\"lightblue\" stroke=\"navy\" stroke-width=\"2\" />"
                    + "  <circle cx=\"50\" cy=\"50\" r=\"30\" fill=\"brown\" stroke=\"darkblue\" stroke-width=\"2\" />"
                    + "  <ellipse cx=\"50\" cy=\"50\" rx=\"20\" ry=\"10\" fill=\"yellow\" />"
                    + "  <line x1=\"5\" y1=\"5\" x2=\"95\" y2=\"95\" stroke=\"green\" stroke-width=\"2\" />"
                    + "  <line x1=\"95\" y1=\"5\" x2=\"5\" y2=\"95\" stroke=\"green\" stroke-width=\"2\" />"
                    + "  <polygon points=\"50,10 61,35 90,35 65,57 74,84 50,65 26,84 35,57 10,35 39,35\" "
                            + "fill=\"gold\" stroke=\"blue\" stroke-width=\"1\" />"
                    + "  <text x=\"50\" y=\"90\" font-size=\"8\" text-anchor=\"middle\" fill=\"black\">HtmlUnit</text>"
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
                + "iVBORw0KGgoAAAANSUhEUgAAACoAAAAqCAYAAADFw8lbAAABUklEQVRYR+2UPw4BQRTG7TU0Cu5Aq+EGFBqJhgsoFAolB6Bx"
                + "AEdAodC4BImGY/B9yRt51vqz8YTE2+RlZ2Znvvnm995slLl+KugOECU1PJR2F+8Zoh5bk9TNY3CNyCI2iD5ioSae0I7u6Oi1"
                + "bczZcW188jOjjzbQ+75jVOtcDpnGaA4KNcRRqJelT+EqYi47dPBevkiUGaJuUdZSZytrmdmxjBeSjIYN9clG6DD1gShTwnQy"
                + "tbrNjLQQvRRG96JNHR4+cW0aonGjFGW9BnOhTRINZZTUpghdowc5JL+tEBOl8zWjvIxMb7iEJNdE8MJ+zChJ8CG1V4lyPi9F"
                + "qEP2QyafGeV33oubGhUfv/e69y/7Oadu1DolTtSJWhOw1vMadaLWBKz1vEadqDUBaz2vUSdqTcBaz2vUiVoTsNbzGnWi1gSs"
                + "9bxGnag1AWs9r9G/JXoGyiFdcUG+TPcAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAATklEQVR4AcSRQQ4AEAwEl5fzcnroqkgcaIWQ9GDHaDOCF4FF"
                + "uBEHBAovZhPYFHdrqXG8M0zjCWfBLzsxFl+BX3rIAZrWVNHwxKxKfncfHQAA//82wWCHAAAABklEQVQDACTVIhXYHwYXAAAA"
                + "AElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAS0lEQVQoU2NkoDJghJrnAKRBmBJwAKj5AMzABqiBIEFygD1Q"
                + "00EgbkA2EGQQyGByQD1QE8iskW4gKGDJjRRQCkGJFKonG3JiFqseAAjhEgvKgKaUAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAS0lEQVQoU2NkoDJghJrnAKRBmBJwAKj5AMzABqiBIEFygD1Q"
                + "00EgbkA2EGQQyGByQD1QE8iskW4gKGDJjRRQCkGJFKonG3JiFqseAAjhEgvKgKaUAAAAAElFTkSuQmCC")
    public void strokeRect() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.strokeRect(2, 2, 16, 6);\n");
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAUklEQVR4AexSQQoAIAiTXl4vr4EIXQpqSgiFedK5TYs4vw/I"
                + "G5rHwwqxHl/CJd+yhECNcIY6hsj5GD5ZSt9ZbB6eMGsAXNXHHzaGc2GSOZSpewAAAP//Hp6MbgAAAAZJREFUAwDAtSEpzZc0"
                + "HAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAT0lEQVQ4T2NkoDJghLOoBEYNpBzQLAwdoJgScACEYQY2QA0E"
                + "CZID7BkYGA6CzEE2EJkmFdRDfTtqIJXDEBRT5MYyKIWgxDLV0yHVwAg0EACHiRQVPOJrRgAAABBkZUJHMkQ5QUUyQ0ZDRjhD"
                + "QURGRF7KuR4AAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAXklEQVQ4T2NkoDJgpLJ5DKMGUh6iNAtDB6DbQJgScACo+QDM"
                + "hQ1QA0GC5AB7oKaDQNyAbCDIIJDB5IB6oCaQWaMGUjkMQTFFbiyDkhxKLFM9HZKTVLDqoVnWG7wuBACHiRQV+zydrAAAAABJ"
                + "RU5ErkJggg==")
    public void strokeRectNegativeWidthHeight() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.strokeRect(18, 15, -16, -7);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAM0lEQVR4AezSsQ0AQAhC0X+3/86KhRNIY2Iilq8gfCCcKVAe"
                + "T88RGpTnuQPnPe7p0DVuEgAA//+7ekUdAAAABklEQVQDANkqJgFsPe35AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAIUlEQVQoU2NkYGD4z0BFwAg1EERTA/wfNZBiMJLDkGoAALI2"
                + "EgGlGGPkAAAAEGRlQkdBMDQxMTRBNzhFRDExMjNGvbvLKQAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAJUlEQVQoU2NkYGD4D8RUA4xQA0E0NcD/UQMpDsaRHIYUBx7M"
                + "AACyNhIBkGIQ5AAAAABJRU5ErkJggg==")
    public void strokeRectBorder() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.strokeRect(0.5, 0.5, 19, 9);\n");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA60lEQVQ4T2NkoDJgpLJ5DEPTQCFgMLwjNyjQvawANOgyEC8B"
                + "4mYgfkaqwegGrgAaEA415DuQ7gfiDiD+TKzByAZaATUdxaLxFVCsCYhnAvEfQgYjGwgyDGQoLnATKFEJxOvxGQozMICQQiRD"
                + "QBaX4/ANPB2KAhXUAHE2EDMT8hZUvhWqB0U5eqSoAWW7gdiPCEM9gWp2oKvDlVOsgQo7gRhEYwM7gYIe2CQIZb1AoKZ2IFZH"
                + "06wL5F8hx0CQHhYgTgfiOiAWA+KVQByBK0gIuRBZHy+QA4rdOUD8gBoGEhFPDEO0+CLKa7gUAQCpFx4VDtwfyAAAAABJRU5E"
                + "rkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAyUlEQVQ4T2NkoDJgpLJ5DKMGUh6iQysMlYEefgbE3ynxOLKX"
                + "bwANUgHim0B8HglfALLfEWsJzEBOoIYvQMyEQ+MjNEtAFj7GphZmoD1Q8gCxroCqe41kyTkgexVIHGZgBZDdTqKByMpBwaSB"
                + "bOAKICecAgNXAvVGIBtYBeT4AbEBELOTYXApUE8PsoEwM1iADD0gNkXC2kA2MwFLPIHyO7AZiE0fKAUYo1kCSl4w8A/IkATi"
                + "V8QaiM0SfqCgGdQSWSCdCVM0+PMyAOlZHxXNWGcZAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAxUlEQVQ4T2NkoDJgpLJ5DKMGUh6iNAtDaaDb3gHxd0rdCHNh"
                + "AtCgOUB8DYjPIOGLQPZPUiyBGSgF1PQEiNGD4A9Q7AqaJZeB/F+4LEE24ARQkTkRrgEZdgnNEpClf0F6kQ2sAPLbiTAQm5JO"
                + "oCBIP4qBGkD+dTINvAnUB9KPEWZ7gGL2QMxChsFaIAdhS4fsQAl9IDZBwiDFzAQsqQTKdxCbsDmBio3RLFFD8+FBIN+BWAOx"
                + "OY4XzRI9IN+CEgOxhsDgNxAAub8hIw8WPeEAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABBklEQVQ4T2P878Lwn4GKgHHUQIpDc+iE4VOgX4WAmJNSP0O8"
                + "zMiwgEGAIYXhPYMW0EATMGYE4v8M+kA2OymWwMLwGcNuBhlGRtRE/r+BgYXhKIMO3BKIZbpAVWy4LEFEChODBeMuhpOEXPM/"
                + "FGjYBwY9FEv+gy1lBulFjuUOxj0MlYQMxCb/35WhHOjqDlQDGRluMO5m0CTLQA8GdYY/DDfQXQh0L4MLgzXDQcYGoDSJABi5"
                + "14BaNLEl7J9Agy8CvXAGqACCBRmuMa5m+IvPDqCB7UD5CmJzyneg4rNgw5mgeDvDLeRUAQxHe6AjDhBrIDbHfUax5B/DJaDP"
                + "TlBiINYQGPwGAgAm8mgw6jTe3wAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAY0lEQVQoU2NkoDJgRDIvGsjeDsTvKLED2cAlQIM8gfgO1OBt"
                + "QPoUqYYjGwjTawY12AtIq0ANB7mcKNdjMxDZUUJQw0EuJ8r1hAxE9zE218cgKyLVQGyuX0otA7HGFyUupI+BAC5XEQsxJDCz"
                + "AAAAAElFTkSuQmCC")
    public void moveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.lineTo(16, 6); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAACFUlEQVR4AeyWOUhcURiFhyyQLkuRIgRCEkI2SEgCWUiRIi6l"
                + "Vm4gCmojCiKIqIWC4IYgaGVrYWMhWomIuIGNjQsoKCiCihZiI1qJ33n4w3NQdEZn5i8cznfPfQPD+7i8e9/cizj/hAUfeXQ1"
                + "wUrkqsBdTPABZq/AXUxwB7Mn4C4meIDZnSCLEHPuVjDmJYv6ga3gMd9vg7uYoDbJW+yegquYoKR2GZ6Dq4QFlzFzLXiC4H1w"
                + "lfAKbmH2HlwlLLiB2VdwlbDgGmZ/wFXCgkOYvYCX4CZhQUn1MTSAm0QLNmH2BWYgD1KeaMFDjP7CMJTBPoxBNxTBR0hqogXt"
                + "5h1M/sMH6IRVKIAlGIEa+AEJz2WCduM9JhLqoTNBB3kLrfNynBbV9CdISK4SvOimU3xZCo+hEXR2ztODkA+3mngEwwLTXOjZ"
                + "fEj3QwmsQx08gxvnpoJnAkENMKZBFujZXaFrQfJUfLlNQTNYYKJV/Ud/h0UohLiSCEET0d+3XC7KoQL0pvpGx5RECpqIdvov"
                + "LiZgDurh2kmGoMl0MXkNP2ESfsOVSaagZDYZskE7XseVDnwuL0+yBc2kl4kO+3Raz+Yb+sKkSlAyOi8zmOi5nKW1oajzSaWg"
                + "mTQz0RuolW6Hc/EgKCHt9M9MtIlG6XcQxIugZI4YckCyeoUyjUQ8CQZCDG1QDEE8CkpMf/HULlcwELPB6wqaX+QUAAD//0Ll"
                + "GgcAAAAGSURBVAMASgJHUeIbv/kAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAB2UlEQVRYR+3YPciNYRzH8c/DJkWKyUJ5yUKJopREmAxKRGIg"
                + "byVvYbLQM4jRYJKiSJkMSqGYyMRGNpu3ibLof7ruq9vj0eGc85zzH3zH33UP3859/8/1u64xyRlL7lcFz2EWzmcTbgQPYTkO"
                + "ZxXciW3YlVVwC45ja1bBNbiCtVkFF2Mc27MKzsUbzMsqOA0/MD2rYPAZC/Alk2R7J7mHM3hfkwS0BZ/gAp7WJAFtwVt4gNs1"
                + "SUBbMPbjT7hekwS0BXeXnWRPTRLQFlyGa1hfkwS0BYO32Ix3NRkxEwWjbs3BpZqMmImCwTGsKsPyvKYjYjLBIHrhUTpb4Lci"
                + "+gLPyo4zNP4k2DATq7ES6zADs/EQ9/GyPjlFdBOcjJCNQdqA+biJG/hQnxggvQi2WYK95S/qOy7jVV0dAP0KtolzTZSNaEMX"
                + "8biu9MEgBRvi1UczX4STeF1XemAqBBs24WqZ/lO9Tv9UCjbsLweyeO0h/E8MQ7DhLPaVUvLXgzRMwWBp6Z1RiuP77MqwBRtO"
                + "lP65o1uDH5VgEEfcKCVfcbr4/MYoBRtiwqMkb8THmhYyCAYr8AhHcLe4dcgi2HCnvPKDTZBNMDhQLlIXZhUM4oajc4GQ8Rf8"
                + "hf+C/fITjixBKYX0CLQAAAAQZGVCRzAxMEJGQzlCNTQ5NjgxRkXTNOB+AAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAB1ElEQVRYR+3YPShFYRzH8XvZpEgxWSgvWShRlJIIk0GJSAzk"
                + "reQtTBYyiNFgkqJImQxKoZjIxEY2m7eJsvj+dB7dbm73uq//walvnXuX+6lznvv8z/H7jB9+4z6fA84BzaJ5a2AHHAJWTsNW"
                + "gZ3A2qjLKrAF2Di1WgXWAFulWqvAYmDL1G4VmAvslvKsAtOAfVK6VaBcL1RAr5aQgTvJPrAZerAKPAW2QGdWgdvADmnHKlD7"
                + "8TNtWAV2eztJj1VgGbB1qrcKlOuOmuneCjJ4YNW4lUNLVoFyjVGVt1guUg0NNfJrLhwlbYHvJOglnZN2nKQd4Z5JMpFUUyXV"
                + "UQZl0xEd0FWipeGAv/2+sFpIDZRPW7RJj4nARgMMdJTwoZf0F/VBK3QdT2iswECLnms0bGgaWqSTeEDjCXQeXXpN5kU0STex"
                + "QBMBdJ4mTtZIq3+Kolr9iQQ6aD8neiDTZRf4T0cygA40y0kfaSiJeCElEyhoKWnu1FCs+zPskWygA01wovmzw8OGhKYKKJAe"
                + "cTWUvNF0KGEqgc6kFa4huZGegqEWgDJV0DGN0F4g0grQmXa9Sz7ovrAGlGuA9CK1UB8sAuXSG47vFwhWgT+34T8w1P9bpN9/"
                + "AY4sQSljOVZZAAAAAElFTkSuQmCC")
    public void moveToBezierCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='40', height='40' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.bezierCurveTo(4, 34, 2, 8, 38, 36); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAABxUlEQVR4AeyVPShFYRjHT0oZZUGKfJVkkJSPDFJkEBkQUjLI"
                + "hEGZyOhjko9FRNmwKJHvJCwGyWAwYDEYLMx+z8kdvO4517nvueee4b39f+fp3vs+7/u///ue96RYIX8Zg7p/kEnQJKibgG6/"
                + "2YMmQd0EdPvNHjQJ6iag22/2oElQNwHdfrMHTYI/CZRQe2EMxmEIWqAYEqpYe7Ca1Q/hBFohCzKgAobhAp5hEwYhD3yVm8F2"
                + "VrqGLciBLogkKGaaeJ8NdXAE9fAIO9AGvsjJYC2zb4OYWKG66dWyrA0G9EAa7MEEXEE3aMnJ4BKzDoAkQ/GkNUZXwjTINjij"
                + "NkNcimZQJn1iNkmFErd26ayBVViGdSgET4pmUJJb9DSL+2C5gQoY8gJ3MAL/lmowl85MOAe/NcmEVSDH0wG1HGJKNVhKh9yF"
                + "lITogVkbYR9uYRRcpRqUX5jq2uHPl/NMI2HI2SqBOJ6fqsF0Gt8gCMmZ2cBC93ADHfBHqsEPRrxDkJpisT6YhRn4JdXgHN8u"
                + "QNA6ZsEyyAd5tBZRbakGv+xPk3P5ZNlOOIVLsKUatD9M8kWeQP0RD2E0KN7knJRqhdWgbU4uoTf4DQAA//9d0N0DAAAABklE"
                + "QVQDAD1/O1HLUOolAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAABgklEQVRYR+3Vv0tWURzH8ZdEGvUXBE4W0RARCK1B4OYUtAQt"
                + "BYUS2BCUJgj9GC1oC2lqqggagobGDKJ/oCVag9RawqFJDhy+2FGfHjs+dYfzHj/nXs77fs6PO6TjDHXcrwlW05a4ltZgLa3B"
                + "WlqDtbQGa2kN1tIarKU1WEtrsJZ+GxzGaRzHfvzCV3zA93hqAPQjuIiz+Ik3GM3CSXQSq3iZx97FW3tEL8GRPOlrPIh0K8dw"
                + "CtdwGE+whLV4ooKdBA/gI05G0h9HcRkn8Bm3sR6jf8FOgsu4ifeR7J7ruI+HmI90l2wneA5nMBNJHanFu5jC40j7ZDvBZ3hU"
                + "2V5JmmchH6qL+BQjf6AUPIgVHIpkbxnHU7zCXKQ9KAXT0l7A1UgGwyym81w9r6ZS8BKO5H0zaNJ9egffcCvSglIwfdk+3Itk"
                + "8KTb4jwm8KOcrhS8kX9j6ZD8S9LefJu31ovNE5eC/5vnucU4A10TTFzJP4l0FjopmBjDly4LBl1c4t/ovOAGUngyKRdIPigA"
                + "AAAQZGVCR0ZGNEIxQjE4N0Q5NUY1Qjb0UOldAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAABnElEQVRYR+2Yu0oDQRSGE8QL+gSClYqkEBEEW0GwsxJsBBsF"
                + "gyJoIXgFwUupgp2IlZWKYBGwsFRBfAEbsRW8NmJh5XdgCMmQXWZzkmWKGfhIsefy558zs7DZjOcr67m+TBCo3aHgYHBQ64A2"
                + "P8xgcFDrgDY/zGBwUOuANj/MYHBQ64A2P8xgWg420WgQctAIf/AKD/ClFRGX77LFexQYhh+4hg4QwSJ0FD7g0jy7rbXYOIHN"
                + "pmmB3/2Yxj0864d5aIcTOIbPWoiNEthC8UfoS9ikm/hp6IVnWIffhDXKwqME3hG1DPeK4ovk7sIBbFRbp5LAMYoNwUK1Ra08"
                + "cXEbZuEoac1KAs8ocqh0z9YhfTZBDtUkPLkKtQW2kvgOba4FEsYNEH8KV7DmkmsLlK2dgLxLsiJmldw50yv2arIFTpHUBTI3"
                + "9V5yn27BG6xENbMFyj9rgJ16qyupL7fFOIzAt93XFrhEgLzG5JCkuWQ2b8xoXZQ2dnnVpSn03LhYPAO+CRQzZkC2Xc6Ct19Y"
                + "O9H24rPA4lj5uMVlM++9wH9SeDIp/dhlMwAAAABJRU5ErkJggg==")
    public void bezierCurveToWithoutMoveToUsesControlPoint1AsStart() throws Exception {
        draw("<canvas id='myCanvas' width='40' height='40'></canvas>\n",
                "context.beginPath(); context.bezierCurveTo(4, 34, 2, 8, 38, 36); context.stroke();\n");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA10lEQVQ4T83TMQ4BQRSAYRuFjqj2ANxAIdFzAEqN6BWipyHU"
                + "FBuF3hVcgEohGr0rqEThf5sZmWLEMq+wyZ/MbjLfTDKzUU75iRyvz3hPl5A1XHAD1KDYwILb7lkXcUE7p2xgwW1bxks6foJ9"
                + "oG9OjY9rOtOAbu/grKCd32OwohktfOi3oDXmDOrUoocL/wqK0aSpgV9mCCjImPI0sWIoKI5crREd5EUDLOJcqaQFijOkgpy8"
                + "xg4FbFOXOlpgBWxHVS1Qdil/T6wJpqetCSaAJ00wvdv/Dz4ByKocwaSTblsAAAAASUVORK5CYII=")
    public void moveToQuadraticCurveToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(2, 2); context.quadraticCurveTo(19, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAh0lEQVR4AezWQQqAMBBDUfHkHt10l003yQhCv0xxEFKHZxfe"
                + "188vBmw/0FGCj7TW0m2ujhKcY7OdEDSMqEUwYrMQgoYRtQhGbBZC0DCidlJw/SisFQ2yC00OuHtH9ZwBKz6FERRCVQhWfAoP"
                + "CWqnj4oBW1gEEWwF2jxnEMFWoM1zBhFsBdr8CwAA//8Lay2uAAAABklEQVQDAKHoAlHKHpf2AAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAd0lEQVRYR+3SMQqAMBBE0YknFrHXWsQz24S9wJ9AIPPKLWT9"
                + "2abJtcn3y4KY84lPSZukpyYGzgWv/r27JgZLLXj0J37r9w2WKpgbpPZ+g19NDHKDVApSSxUcwllwiCxIpSCVglQKUilIpSCV"
                + "glQKUilIpSA1fcEfX7kNKdMLfVYAAAAQZGVCRzY3NDMxMzk2NDAxRTJCQjmxPy2iAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAiUlEQVRYR+3VMQqAMBBE0eiJRey1FvHMRkhhYbU/gaBfsJww"
                + "vF2SIXX+DZ33SxakE6opuOQyY/53WuqZr1lwzQff520WDArMZcRHMP8ac8RU81eCU9nBk6p5zSgYFPAlCcK1jdW8B5s0tSBl"
                + "VVBBKkDz7qCCVIDm3UEFqQDNu4MKUgGadwc/L3gBX7kNKeTInzcAAAAASUVORK5CYII=")
    public void quadraticCurveToWithoutMoveToUsesControlPointAsStart() throws Exception {
        draw("<canvas id='myCanvas' width='40' height='40'></canvas>\n",
                "context.beginPath(); context.quadraticCurveTo(19, 4, 19, 17); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAWUlEQVR4AeSTOw4AIAhDwfvfWRsSwsynLBqpDta+YDwyPKgX"
                + "XsB2CnYRKqEldIVKqKDrFOzyew+tB12hvrLDZX+L+2xdIbSkqqwQKugyheMxVwgjrrAbJ3wAAAD//xfmZWUAAAAGSURBVAMA"
                + "VqMcKSf/HRgAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAO0lEQVQ4T2NkoDJghLOoBJAN/A9nkQfAZo1wA6kCaBrLVAGj"
                + "BoIBqTkGJdiwheEINJAigC0MKQKD30AA5KoIFUiNZwUAAAAQZGVCR0FDMTFGNDNBNEY4REFEMzZPPfqFAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAOklEQVQ4T2NkoDJgpLJ5DMgG/qfQcLBZI9xACoMQop2msTzq"
                + "QvJCAFukkJpjUMwYNZC8iEDWNQJzCgDkqggV2JZl+AAAAABJRU5ErkJggg==")
    public void lineWidthMoveToLineToStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.lineWidth = 4;"
                + "context.moveTo(2, 4); context.lineTo(18, 4); context.stroke();"
                + "context.moveTo(4, 14); context.lineTo(16, 14); context.stroke();");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0klEQVQ4T2NkoDJgpLJ5DDQ3kA3o4iggPgvE14D4L6k+wObC"
                + "E0BDzIH4OxBfgBoOsuAMEF8nZAk2AxuAmupxuOwH1JKjQLoUiP+jq8NmoAVQ0XEivGoJVAPyDQrAZiBI7AkQSxEwtBEoD/IN"
                + "QQNBCmYAcToBA08C5UG+IcrAAKCq9QQMBIWfDBA/Q1aHKx3yABX1ALEpEOsCMSsOw1OB4nOIMRBZDShtggw1RsIgPkh8IxCD"
                + "fAMH5OYUkGF6QKwOxEupYSDO4CXXhaMGUh7L9AtDAEzqHRUeyJjbAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0klEQVQ4T2NkoDJgpLJ5DDQ3kA3o4iggPgvE14D4L6k+wObC"
                + "E0BDzIH4OxBfgBoOsuAMEF8nZAk2AxuAmupxuOwH1JKjQLoUiP+jq8NmoAVQ0XEivGoJVAPyDQrAZiBI7AkQSxEwtBEoD/IN"
                + "QQNBCmYAcToBA08C5UG+IcrAAKCq9QQMBIWfDBA/Q1aHKx3yABX1ALEpEOsCMSsOw1OB4nOIMRBZDShtggw1RsIgPkh8IxCD"
                + "fAMH5OYUkGF6QKwOxEupYSDO4CXXhaMGUh7L9AtDAEzqHRUeyJjbAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAR0lEQVQ4T2NkoDJgpLJ5DKMGUh6iIzgMBYChV0BkCH4AqpuA"
                + "Sy1yGIIMBBlMDAAZCDIYA4zgSCEm3IhSMxqGRAUTXkVUD0MA33oEFT5oGBkAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAb0lEQVQ4T2NkoDJgpLJ5DKMGUh6io2FImzBkAxp7DYhvAPFF"
                + "IL4EpW8B6X+ErMQVKfxAjYZArAfE+lBaCUjfQ7IAZNF5IP6IbAkpscwE1KiGZAHIIg0g1gLiXzBDSTGQkG/B8qMGEhVMeBUN"
                + "/jAEAMq8DhXVLTYNAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAApklEQVR4AeySQQ6AIBAD1Y+rL1dIMHCguqQeOIwRgZRCHXZb"
                + "Jn8I6F4QBCHoEnD91CAEXQKunxqEoEvA9VODEHQJuP5eDe5p08Nszx6qf9s/HV3fXsCsXvnTtNH5WryqL/J31wt4Jltu7V+O"
                + "zlvv6DgdX99ewKpOMCKgewk/EXRjaD8BNZuYAsEYJ70KgppNTIFgjJNeBUHNJqZAMMZJr4KgZhNTbgAAAP//19jLvwAAAAZJ"
                + "REFUAwAEaCRRkkDmZQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAWUlEQVRYR+3RsQ0AIAzEQDI5YnM6RJHOBY/kWyAvp0a4Ct/n"
                + "QMwXUxakLEhZkLIgZUHKgtSXBeeZ/8a6z3YF4wdG6QpGcSBlQcqClAUpC1IWpCxIWZCKL7gBPnECKXxp57IAAAAQZGVCRzQw"
                + "Nzg3QkYwOEZERTQ2MDQELIZaAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAAZklEQVRYR+3TMQ6AMBAEseTliJ/TUtA5xUkM/UiHWfYa/uzh"
                + "960O1C+UYIIqoH0bTFAFtG+DCaqA9m3wl4KXvjX297v/2uD4AxHgbN5frJ4JJqgC2rfBBFVA+zaYoApo3wYTVAHtHz5xAim9"
                + "TIFfAAAAAElFTkSuQmCC")
    public void moveToLineToRotateStroke() throws Exception {
        draw("<canvas id='myCanvas' width='40', height='40' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(4, 20); context.lineTo(36, 20); context.rotate(90); context.stroke();\n");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAvklEQVQ4T2NkoDJgBJo3FYg/AXE3EL+j1HyQgZxAXATEaVDD"
                + "JwPp7+QaDDIQBkSBjAYg9gHiFiCeD8R/SDUY2UCYXjUgowuINYG4GojXAvF/Yg3GZiBMrzWQMQOIfwBxARAfJcZQfAaC9DMB"
                + "cRw0CM4B6SogvoLPYEIGwvTCIq4UKLARiOuB+AE2g4k1EKYXFnEJQIHZQNwKxK+RDSbVQPSIWwAU2EANA3EGI7kuHDUQEQKj"
                + "YUhM9sevhuphCAApYRoVT9kKewAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAAA8AAAAPCAYAAAA71pVKAAAAM0lEQVR4AeSQMQoAAAgCr/7/51qjSWgpEtwUTp2BFpQDolp"
                + "ZswBbweyZ19gGVo2go4clAAAA///5PcsNAAAABklEQVQDAIrNCB/oIL/fAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAA8AAAAPCAYAAAA71pVKAAAAMUlEQVQoU2NkoAAwUqCXgTqa/zMwABECAE0laDBcwahmcGg"
                + "NhQAjJ6UR9Bc+QwdOMwDg/BAQhXpW1wAAABBkZUJHNDA5Q0Q5NjM5MDEwMTFBMheAMmQAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAAA8AAAAPCAYAAAA71pVKAAAAMUlEQVQoU2NkoAAwUqCXgTqa/zMwABECAE0laDBcwahmcGg"
                + "NhQAjJ6UR9Bc+QwdOMwDg/BAQhXpW1wAAAABJRU5ErkJggg==")
    public void rectRespectsTransform() throws Exception {
        draw("<canvas id='myCanvas' width='15', height='15' style='border: 1px solid red;'></canvas>\n",
                " context.translate(7,1);"
                + " context.rotate(Math.PI/2);"
                + " context.fillStyle='red';"
                + " context.beginPath();"
                + " context.rect(2,1,8,4);"
                + " context.fill();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAtklEQVR4AeySzQ2CQBBGJ9ZjA3ryZgNWYgXagFZgBzagJ0+"
                + "WYAPSABXA+w6TELILw8+FBDKPhdnJY9nZjc18zSU8+bqmCiX6IjvDEWyssCm6IdrDCwYLU6KnRE50hb2iqDAs6hMOFuWEo0"
                + "VtoVr+J3mHAt6whUsCUvnwpuwoKeEBP1BUurU48K6PMKTDhZpV+1XsXEn6s48fcp3RFHYWRidXYXSn8nXL2kM/tH7mUqNq8"
                + "v9rZjUAAAD//+rycfYAAAAGSURBVAMAU4goqXKrNPwAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAjElEQVQ4T+2U2w2AIAxFL5PpCk6kbuQI6iI6irlJS4jyxk/"
                + "OD+WRE9oUDH7G2KiNAcDBoFVI0SKekQu1QlfEcdeNUmFQpOQKkyIlJcwWKSFhsUh5C6tFiiu8ZKTkltjHaiMPKpwBTAA2mY"
                + "dgBmxg3t6LK9Q0YyTPdWG4NkKv4Re3bfhBpp4aG/uM9eEDBxwnFbCeB5YAAAAQZGVCRzU5OURBQjU0NzA2MEQ0QTTD2dNVA"
                + "AAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAiElEQVQ4T2NkoDJgpJJ59kBzDoLMotRAkEENUHMcKDEQ2SC"
                + "QgQdgPiXVhTgNItVAggYRayDRBhEykGSDcBlItkHYDLwPFQTF2AM86bMRX9qFxXI9UFEgEG8gkNBhCRiUVLACZANBbJwKob"
                + "pBFuNVN2rgaBhipjSSkg2ogISXazjSLShhH8KXXgEHHCcVPC38rQAAAABJRU5ErkJggg==")
    public void rectCurrentPointIsTopLeft() throws Exception {
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "context.beginPath(); context.rect(2, 10, 10, 10); context.lineTo(18, 2); context.stroke();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABE0lEQVR4AeyTPa4BYRSGv9wF3PIm1wYkaj0hKoUoNRagV4i"
                + "GQmxAFCo78NNKhF4tLMAO9DwHZ3KGmc9MojR5nzk/c743k5OZH/fhyxr28E4CYy+qaUcNxaioTRMvj1xjgVpmCTf9c5/CEG"
                + "6makjtNs45Gbb03b2ncUutapEc4QQ5WICzhlInIcPQCupQgi4ESmsoBk1Oz6ACOwgpqaHsZ8/JXxjBGCL1ztAuvYPDHM4QK"
                + "5+hLP3AydDSqb2KMsxzQpdeJg8tndqrZ8MB02uIXTrPvFLDLFNtqMIE/sB+j5oX6Xulhg2mlmCXrn8H7UD68QeN50QNpS+m"
                + "+iYS9e+Q3CKzsVjD2KE0D76GabYVPfvxHV4BAAD//0OS0iMAAAAGSURBVAMAJ+czKZHzx5AAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAzElEQVQ4T+2SMRIBQRBF32Yy15BxA8UFpEKHkOMEhEJHECk"
                + "y3ECVQ8gIhapVTxtqZmp2bSDwou7Zv69mf21BzRQ2wcSmNDObXrSBkwxOKLIesNc9Rhc4AFM7gbm+25HFF8rsB0P4uQGw0h"
                + "svXKCKsAG0ND8Cbva0gnAD9IEhsLZTj1yhlC6fdwd2iVyW0JUunye9xXJPUsJQ6aHcGyFhU0Wyf5ZeWnjVG4koVHop4Rg4A"
                + "1s9CyE/9jFHKCyBi21xojLBF9bCX/g9v9/hA2fSKRVyeZCUAAAAEGRlQkc4REQxQzZBNjlDQzQ5NjJG/7bzkwAAAABJRU5E"
                + "rkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA10lEQVQ4T2NkoDJgRDKvnkizG7Go0weKXQSJwwwEGeYIxAc"
                + "IGGoPlD8IxA1I6vqheg3QDQQZjqwQm9kgi2Hq/IHsBUAMcvEEmGJkFxJrIAdQswbU4AQg/QHZZlIN3AbU7ATEEUC8AZsXiD"
                + "UQFOgg7/0A4p34goYYA2GBDvIeKNzwBg0+A7EFOnKkYPMxSrKB2cwP9R6Ijx7oJBv4HpoMQAZhC3SSDCwBGnIFiHdg9QtEE"
                + "JSwDxETKSDFM4D4BR7DYFIN+NQg52UizCKsZNRAwmFESMXgD0MAZ9IpFR4WhlUAAAAASUVORK5CYII=")
    public void rectCurrentPointIsTopLeftNegativeHeight() throws Exception {
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "context.beginPath(); context.rect(2, 12, 10, -10); context.lineTo(18, 2); context.stroke();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAtklEQVR4AdzPQRHCQAyF4QUBKMAAClCAAgygAAUcYFCAAgy"
                + "gAAUowAAKUAB/MpND222abffUTl7abtJvpstU+TLwh+uFcawMjG0HtuYDngJ/qyvRX76y/SVH4lYUFGRFu5EPOZBslYAGrH"
                + "m4kzfZk0YZuODUy4V5uzYcPMiL7IiWgfritDOzHMpx2tKeRCsKyrKHylxTAsoHg2gpOIiOAV10LNiLTgGz6FSwg9YADZV7q"
                + "gUqJu0PAAD//6TBlYMAAAAGSURBVAMAoyAaKSNu+dIAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAnUlEQVQ4T+3SMQrCMBSH8a8X9gSF2sHNuQfwAJ7C3bG7s6O"
                + "TUCp/SR5aTNukb/SDEvIoP0JIhXNVWMewpor/LfYHk7XAEXjYJNHaO1R34AB0wNOmk3LA2A3YAydgsGmoBIz1QA2cbZLxvn"
                + "SixnbfXYEdcNFmLajmUPW2ckA1hxaBKoUWg+oXuglUU3QzqD5RF1BF1A1UQvW5gZY7+AIswh0VBjqD9wAAABBkZUJHMzE0R"
                + "jJEQUYyNjNBMDQzQ5qLT2IAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAn0lEQVQ4T2NkoDJghJr3n4C5MHUErR81EGcQNQJleoD4C6F"
                + "AJDYMQea8AeJWIJ4GxL9wGUyKgTAzHgEZDUC8CIj/ohtMjoEwM64DGTVAvA7ZUGLTF8hF9Ti8eR4ongfER0DyxBoIUovPUL"
                + "hZpBhIyFCwWaQaiM9Qsg3EZShFBmIzlGID0Q2lioHIhlLNQJihoGRFVizjSN8QYXKSDV4DASzCHRW9NsqlAAAAAElFTkSuQ"
                + "mCC")
    public void rectSeparatePath() throws Exception {
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "context.beginPath(); context.rect(1, 1, 8, 8);\n"
                + "context.lineTo(18, 10); context.lineTo(18, 18); context.closePath(); context.fill();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABG0lEQVR4AdTSsStFURwH8Jt/wCT+BpFBKQMlZTCIwSBlUMoi"
                + "MtjNDGSxMCgZJDIYlMmglEEWyR9AJn+A8vkNhnt653n39pb3+n7Offe+c76dd+/tKtr86czCdXdhg2fOWaWHlpL+5SibtPKJ"
                + "KU6Z5oEodmietDBm7xju+eSKGeZY5oQ+skkLY9Fbg9kvro3wzRnZpIVRNpqdXRRrfpsgm7TwzszYpUO9pIUXavoZpFbSwi8t"
                + "uxxRK2lhlBwaXjmgchoVRsmWYYBtKiVX+KFlhXEq7TRXqKd4NyzQzSMtPahmhTqK2OmSL8dccs0svYyxSSn/Ff5NjgcVL/yN"
                + "C4vcMswPpbRaGIvilYrieSdD7LFPKVUKSwtzJ20v/AUAAP//tDEvlAAAAAZJREFUAwDytSgpRne/fwAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABAklEQVQ4T72UvQ4BQRRGbaOT6L2AQqHTeAK0HkAnkSgoJOIn"
                + "QYSEREGn0mppRCVRKMVPPIBapVBIOLeQyMTuzLKxySlm987ZOzvzreXz+LI89vn+IozSdQWCcIYpzE1XonYosi404AoyToMf"
                + "qrDRiVWhdDOAtTIxzLgEe+g7SVXhkuIibG0m5bgfgrKdVBWOKVzBxKGLFM9mpsIEhdJFUvetTIVSt4A8nL6RfjqHMURZyHgl"
                + "FI9szA1GbqVOSWkju0PNjVQXvQ4yScvQVKoTiqcAEejBUSc2EYpDNqoOD5A07SAAcWi9v8RU+Joj51SyLWm5QBMOvwh1K/7P"
                + "/1DbhVPBEzD3JBVTQqvpAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABAklEQVQ4T72UvQ4BQRRGbaOT6L2AQqHTeAK0HkAnkSgoJOIn"
                + "QYSEREGn0mppRCVRKMVPPIBapVBIOLeQyMTuzLKxySlm987ZOzvzreXz+LI89vn+IozSdQWCcIYpzE1XonYosi404AoyToMf"
                + "qrDRiVWhdDOAtTIxzLgEe+g7SVXhkuIibG0m5bgfgrKdVBWOKVzBxKGLFM9mpsIEhdJFUvetTIVSt4A8nL6RfjqHMURZyHgl"
                + "FI9szA1GbqVOSWkju0PNjVQXvQ4yScvQVKoTiqcAEejBUSc2EYpDNqoOD5A07SAAcWi9v8RU+Joj51SyLWm5QBMOvwh1K/7P"
                + "/1DbhVPBEzD3JBVTQqvpAAAAAElFTkSuQmCC")
    public void ellipseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI); context.stroke();\n");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABKklEQVQ4T2NkoDJgpLJ5DPQx8P9/BmGgyxWB+CojI8N3UnyB"
                + "4kKgQcxAzVuA2ANqyF8gvRqIW4AGXyXGYHQD7YGaDmDR+B8oNheIS4AGf8RnMLqBPkDFm/FouAlyPdDQB7jUoBsICrd7BLz2"
                + "CCivADQU5GoMgBHLwHA8C1RlRMBQe6CBh4g10BGocB8BA6cADcwlykCQIqAr1wCpYDyGvgTKSWLzNtaEDTRQEKgB5HVQmOIC"
                + "dkADD6NL4swpQENVgIqPALE4DhMnAQ3MJ9pAqNflgfReIFbGYugLoJgUurcJ5mWgS4WAGruBOAmLobZAA0G+gAOCBsJUAg3W"
                + "BbKrgDgMiJmg4k1AA+vJMhDJYC4gWwuIuUFhDDQQlN9JdyGuqCYpUog1hKYuBAD36EAVpWqveAAAAABJRU5ErkJggg==")
    public void ellipseFill() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = 'yellow';"
                + "context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI); context.fill();\n");
    }

    /**
     * Verifies that a line is drawn from the current path position to the
     * start of the ellipse when ellipse() follows a moveTo().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApklEQVR4AeyQyw2DMBBEnZSRBpIGckwpqSQK4iPRCKVwowJo"
                + "gDbgDbIRsiwhDCcEYphlvX5e790c/FzA/QM99wxfDOhr9cSnN/bKAvwhfKwSXAcYB0xJbFFJvaA9Lgn2Jp6Bip0GFyw8lLuF"
                + "1kMdZhT63fq5HzUtelh1eIOCHSq/JsFyimqrAhc0Gsh+I0BFIOkAQrMLOAH8j5uhn4/+v4DRo5s3jgAAAP//GEme5wAAAAZJ"
                + "REFUAwAaXx8pYc4j6gAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAfklEQVQ4T+3QIQ7CQBCF4a9B4eEecIjegwSL4AZwCAQJouUc"
                + "3AqHIWSTKQZEk1lR0d+8tyP+zE6jMs23VWIW5pncDfd4o6ux4RXPcCxxKMNBeIocyxYrPOLd4ob7IDxHjmWD9R9hn/nyBa/o"
                + "CxxLyQgLu8g+Mi38YRbmmf4NPwbFDRWaALN4AAAAEGRlQkc3QjgxMkIwQzAxMjY1MDdFtbTXPQAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAe0lEQVQ4T2NkoDJgpLJ5DKMGUh6igy4Mk4F++gfE82F+o8SF"
                + "M4CGfAJikBmcQJwDMhRmYD2JoWcIVC8CxLuh+lyB9GwgXggzsIFEAw2A6kWxGLiAEi9PARr4E+oQZiBdgOxlEh0IV54AZS2g"
                + "RqRgdQQlXh41EBICgz8MAQbFDRWlQ+mHAAAAAElFTkSuQmCC")
    public void ellipseConnectsFromCurrentPoint() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.moveTo(1, 10);\n"
                + "context.ellipse(16, 10, 2, 2, 0, 0, 2 * Math.PI);\n"
                + "context.stroke();\n");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAArElEQVQ4T2NkoDJgpLJ5DKMGYg1RcaDoS2LDmlAYJgINegPE"
                + "m6lhYDfQkG9AXE+sYSB1uFxoAJRrB2JPUgzDZ2AeUFIZiPOpZWAa0CBjIE7HYeBsoPhJIJ6DLo/Py0VAxXE4DFwKFO8A4svE"
                + "GghSVwb1NrorQa66DsS92CwjlGxAhupBYxuklguIz+EyDF+kIFsOMtAciP9Dww3Dm8iKCbmQ1EgeLW1IDjFMDQCsfxYVL43f"
                + "UAAAAABJRU5ErkJggg==")
    public void arcStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 4.3); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA10lEQVR4AcyUPQ5BQRRGh0JHotfZgUat0Esshg6lwjKUGq1C"
                + "o9FLVDqJ3gY4J5nCk5CZZCSS72Tu3J/PHcWrhxDmIZ8ZM+Js5dTwQdFCDgtmxJnXM2hYo1hMGrphUcP/37DYczXyPyz+ZI2L"
                + "4YbFzDT6qeGAX5hGjAnz5YYNxrawgnbE2FyTe5Y0HDNxhh5MIsbm1tyzpGGLCY04KjLXIZP1fA0vDH3SjkIfkqVhcnNKo4bd"
                + "L41DakdIloZ3upfwLnNXkntIloYbukdwg0PE2NyJu1/lVMITAAD//2CzrtYAAAAGSURBVAMAHHMxPWQvHuMAAAAASUVORK5C"
                + "YII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAwklEQVQ4T2NkYGA4AMVAimJgzwg0ogGKKTYNaED9qIEUB+No"
                + "GFIchDROh0ZAB5pAHXkKSF8gw8HwWP4F1KwOxD+ghnAC6StA3EWioWAD9wLxTSDOQtM8E8i/S6KhYANBXjPA4ZIlUAMvEelS"
                + "sIFnkMIOXd90qPxcahk4G2jQSSCeQ4qBF4GK9XFoWAoU7wDiy6QYuBuo+B4Qp6NpArnqOhD3EmkYSBk4DPcD8W8gFgdiUPJh"
                + "AmJWIH4GxMdJMAyk1AEAvZs0gSroYl8AAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAwklEQVQ4T2NkYGA4AMVAimJgzwg0ogGKKTYNaED9qIEUB+No"
                + "GFIchDROh0ZAB5pAHXkKSF8gw8HwWP4F1KwOxD+ghnAC6StA3EWioWAD9wLxTSDOQtM8E8i/S6KhYANBXjPA4ZIlUAMvEelS"
                + "sIFnkMIOXd90qPxcahk4G2jQSSCeQ4qBF4GK9XFoWAoU7wDiy6QYuBuo+B4Qp6NpArnqOhD3EmkYSBk4DPcD8W8gFgdiUPJh"
                + "AmJWIH4GxMdJMAyk1AEAvZs0gSroYl8AAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAa0lEQVQ4T2NkoDJgpLJ5DKMGYg1RWaCoBxCDgucUEF/AF+7E"
                + "hGEt0ACQof+BmBOIrwBxFy5DiTEQXe9MoMBdXIaSYyDIgiVQAy+h20augbOABp0G4tnUMhBnvJDrwlEDESEwGoaUF7dUD0MA"
                + "jksLFQkR+1oAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA0klEQVQ4T2NkoDJgpLJ5DKMGYg1RI6CoCVTmFJC+gC/cCYVh"
                + "FVCzOhD/gBrCCaSvAHEXLkPxGQgyTAaIs9A0zwTy7+IyFJeBIG/mA3E8DpcsgRp4CV0el4GpQIWGWFwH0z8LyDgNxLNJMdAU"
                + "qDgNhwtBBp0E4jnEGmgAVFgExHE4DFwKFO8A4svEGghSVwbEykCcjqYJ5KrrQNyLzTJCyQZkqB4QfwZiFiDmAuJzuAwDWUDI"
                + "QJAakIFmUNeAwg3Dm8guJcZAHMGIXXjUQJKCC6tiABZOHBWsf4c4AAAAAElFTkSuQmCC")
    public void arcCircleAnticlockwiseStroke() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.arc(10, 10, 4, 0, 2 * Math.PI, true); context.stroke();\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAALklEQVR4AezSIRIAAAgCQcf/P9pOPIlnM0DYYad8Fv5BNdQQ"
                + "CDgbgBYRDQMEvAcAAP//GaAxmgAAAAZJREFUAwCLOAAp5PB2EAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAJUlEQVQ4T2NkoDJghLOoBEYNpByMhiHlYDQMKQejYUg5GPxh"
                + "CABIxgAV3h2cLwAAABBkZUJHOUE0NTUyMEREQzBBQkMzMt9ZyuoAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAKElEQVQ4T2NkoDJgpLJ5DKMGUh6io2E4GoZkhMBosiEj0NC0"
                + "jMAwBABIxgAVO+SUsAAAAABJRU5ErkJggg==")
    public void arcClockwiseFullCircle() throws Exception {
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "context.fillStyle = 'green';"
                + "context.beginPath();"
                + "context.arc(10, 10, 8, 0, 0, false);"
                + "context.fill();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAvElEQVR4AczR3Q2CMBSG4VdGcRG9cwsvTHQMLY6h8cYpvMRB"
                + "dBSwH/9BiJQ2kRM+KCXnyQmNCFx/BA0ZhpfNjTMrBsp1wqV1dqQkxFzoKVewITL2GB50ajpYQJvupL4gaNLWP/UHNWnKVg8l"
                + "DAhrygoF6vRz0gWM844ft/GgwYA9U3vrud7V3nhQHcNoos+KG6gOw/ekEXfKcgfV2EYXXDny1LYyDVRnhZ446LXKdFBCgWpV"
                + "xw+smWYxf/ADAAD//0Jyy1MAAAAGSURBVAMAL6geKQ+rf20AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAzUlEQVQ4T+WRsQ4BQRRFz4ZCJX6DKH2D75BodES1CeIVGhVK"
                + "pU6l8xkKBQ2VRG8rzSbkkRm7icLOTudU995JTjJ5AZ4JbPLE70LhBuyBLbBGuNi3BFmED5t55Q0l2oREds0hNBwp0mTI1Qx5"
                + "hcoKoWWKD2FMgRojTlp8CJUpQqjBl3CH0NDgSxghVDRkEQowtj2Ng1ARJsDA9g8HhLqGbEJFmAE92984HCVJWhoDVYSzFjeh"
                + "IsyBLrBE6JjZXajoN8ss6HM3Uz7hF/5Q+ASPLzEVJSxJigAAABBkZUJHMUZDRDZDM0E0NzAxRUM4OOVKyr8AAAAASUVORK5C"
                + "YII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA2ElEQVQ4T2NkoDJgpLJ5DMQb2MDwAWj5RSDeCsQrGBoYHmFz"
                + "DCkG/kcyAMRex8DBkMxQwfAR2WByDYSZcY2BhcGNoYbhKUyAUgNB5iwEej+Bmgb+YWBm0GKoZbgNMpQaLgSZ0wl0ZQU1DTwL"
                + "NNCEmgZ+BBooQKqBDUAN9TgyAhkGgkxqYGgBktVYDL0KlNMhzYUwUxoY+oHMAjRDyYgUZBNQDf0DlNIEuvAOeS5EuHQCkJkP"
                + "xDOBhmWQnrCxxQYo7fExTGQoYvhOHQOxWEJ8TsGRXtCFB7+BAI8vMRUtkNPJAAAAAElFTkSuQmCC")
    public void arcClockwiseWrapAround() throws Exception {
        // Clockwise from 3π/2 (pointing down, 270°) to π/4 (pointing right+up, 45°).
        // Correct clockwise span = 135°, sweeping through 315° (bottom-right).
        // Pixel at angle 315° from centre (10,10) at radius ~7: approx (15, 15).
        // Pixel at angle 180° (left side, x=2,y=10) must NOT be painted.
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "context.fillStyle = 'green';"
                + "context.beginPath();"
                + "context.arc(10, 10, 8, 3 * Math.PI / 2, Math.PI / 4, false);"
                + "context.lineTo(10, 10);"  // close to centre so fill() paints the sector
                + "context.fill();");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABL0lEQVQ4T+XSv0tVcRyH8dfJC7bm1Ca4hBS4twdu/gEONukS"
                + "OhkOol/QJeVKFDqmk4PYnjiK4CKBoklQ4ORqQ1CWKQf0c7/Xn/fC3Xqmz/M+5zzTKbSYIq4WcXtwWqe/+tGLpxfvfsa2innj"
                + "DuPdjOvBpIJ3GERb7PWcYEaHacN+x3otuKLNvo/oi+1uNnR4kUfrg8kMRsMbY17y6lJqwSlPnDoIb5x/6JZ8LaUWTN5iJLw5"
                + "ZiWvyyMP7uJZeHPsSHrKIw/+Qnt4cxxLHpVHHjyLu3HKb1YxJ9kqhzz4DV3hd/MTH1RUr/7geXARL8Nv5kjhvXYLxvyINSMP"
                + "PsdmeD1fFN54bNmQP7HeQC1YkixhIJw1hapJ67Hcw9XgQ3zCdw9UTdiLZw1SH2wB/2HwHOqsPRXt5T+VAAAAEGRlQkcxNUJC"
                + "OENFMDQ1NDZGN0NEUCTI8QAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABPElEQVQ4T83UzyuEQRzH8feiuHJyUy4S5e6u3PwBDpy4iBM5"
                + "iKndC1qJ1hEnB3EnRykXKfIjtVt7cuWg/OYz5Wlnxz7MPm3y1Pcw02deM8/MNClq/KVq7BEPZmjjlUFN2K/qUtnsqeqEBnLM"
                + "UKy0mO+gURxWVCOq+pg/eFb/Ai1kGOfJzZSD2wIu2VVgIHArDoX2uWg5aDQrTAZiUSyHYSxqlMA0HbxxXSVm4++qTqE3tlEC"
                + "DctqTyQA7ZBFgVM+eK6O7oTgmcAeH3xUR2NC8E5gsw9+JMDsmB3VksBjH8yroz0QfVBuXTc2619w91A2FBr+BbzVMa5qY9aY"
                + "5r5S1gV7FTiKAa8EzdPKFqO8/DSpf7E3FR5yBuwLyjLHQeBWeI+DoUkD91QF6gTNchEKRbk/fL6qXdpX/v+v8BPqrD0V/Gmy"
                + "MAAAAABJRU5ErkJggg==")
    public void arcPartialClockwiseNoSpuriousLine() throws Exception {
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "context.fillStyle = 'green';"
                + "context.beginPath();"
                + "context.arc(10, 10, 8, 2.3, 2 * Math.PI, false);"
                + "context.fill();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void arcMdn() throws Exception {
        readExpectedAlertFromPng("arcMdn");
        draw("<canvas id='myCanvas' width='150', height='200' style='border: 1px solid red;'></canvas>\n",
                "for (let i = 0; i <= 3; i++) {\n"
                + "  for (let j = 0; j <= 2; j++) {\n"
                + "    context.beginPath();\n"
                + "    let x = 25 + j * 50;\n"
                + "    let y = 25 + i * 50; // y coordinate\r\n"
                + "    let radius = 20;\n"
                + "    let startAngle = 0;\n"
                + "    let endAngle = Math.PI + (Math.PI * j) / 2;\n"
                + "    let counterclockwise = i % 2 === 1;\n"
                + "    context.arc(x, y, radius, startAngle, endAngle, counterclockwise);\n"
                + "    if (i > 1) {\n"
                + "      context.fill();\n"
                + "    } else {\n"
                + "      context.stroke();\n"
                + "    }\n"
                + "  }\n"
                + "}");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAn0lEQVQ4T2NkoDJgpLJ5DKMGUh6i+MOwgUECaMVsILaAWnWc"
                + "gZUhnaGa4Tkuq3Eb2MogyfCb4RRQowya5idAvj5DA8M7bIbiNrCBYQlQQzQOlywEGphAqoEvgBrEcRj4EmggKDgwAD4XUt1A"
                + "qntZCuifk1gjhZ1Bj6GS4T1pXgaphsT0TCDLEqr5BJBOBYYfKDiwgtG8TOu8TIb5VI8UAIOoHhWKBN6HAAAAAElFTkSuQmCC")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABPElEQVQ4T83UzyuEQRzH8feiuHJyUy4S5e6u3PwBDpy4iBM5"
                + "iKndC1qJ1hEnB3EnRykXKfIjtVt7cuWg/OYz5Wlnxz7MPm3y1Pcw02deM8/MNClq/KVq7BEPZmjjlUFN2K/qUtnsqeqEBnLM"
                + "UKy0mO+gURxWVCOq+pg/eFb/Ai1kGOfJzZSD2wIu2VVgIHArDoX2uWg5aDQrTAZiUSyHYSxqlMA0HbxxXSVm4++qTqE3tlEC"
                + "DctqTyQA7ZBFgVM+eK6O7oTgmcAeH3xUR2NC8E5gsw9+JMDsmB3VksBjH8yroz0QfVBuXTc2619w91A2FBr+BbzVMa5qY9aY"
                + "5r5S1gV7FTiKAa8EzdPKFqO8/DSpf7E3FR5yBuwLyjLHQeBWeI+DoUkD91QF6gTNchEKRbk/fL6qXdpX/v+v8BPqrD0V/Gmy"
                + "MAAAAABJRU5ErkJggg==")
    public void arcFillPathAngle() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "context.fillStyle = 'green'; context.beginPath();"
                + "context.arc(10, 10, 8, 2.3, 2 * Math.PI); context.fill();\n");
    }

    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAACA0lEQVR4AdSWPS81QRiGvW/vB5BISFCJispHKEhUCmolhaBG"
                + "dOh9JBJKLY1CdMRHpyEhEQqF0PsDXPfmzMkuNo5nn9kg9z0zuzvPPZfZs2fP/7pf/venAN/YzKImwld/agd9/3WnNM8d3HVi"
                + "ysR4Ap5nkp0OPAEPnZgyMV6AB6Q+YXd5AW65k1UCPQC1e0eVPPfOA3DenSoVWBRwkqxbnFYnB9P4GN/h14o11jld0xxOf68i"
                + "gMvE7+CgPgZ7+Apv4gHcjusr1ljndE1zNFc1XM6XFVBwS6nYdcaneAzXKs1VjWpzayyAU6Sl4e45nsFWqVYZX9b/BFBPawcp"
                + "2zhIb4/WcFCgV4ayPkXUAiiwESpH8Q0OWmHQg72kLGVm8vIA9eLXrWxitsA+fs/pw73ANW8pU9nV3DTgP84GTzDWrcx7fc1x"
                + "PZYy2WnAWhfsYqKeQLooUrbWSMItgMNJZdymuoYFcCguW5JeXcMC2JhExG2aQ7wFsCEUR+wLAX7B9e2p8O3wkz4JtezgS1JZ"
                + "UmMBfC6JLVnGAqifSUlxGY0F8KwMsLCGBfCa4n1ciiyAAltTU4atgLrNG78ZUGyzNA84qqw7GKDaGFzgaCoKKLBemlUcRR6A"
                + "Aluk6cfuT7cXIGx1enDGGXRjAZ/QP+JC8gQMIJcMdMsH6VuwfiDQ2RQD0EaSU/UOAAD//xM3XBUAAAAGSURBVAMAnIpNUfTt"
                + "+w4AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAACKUlEQVRYR82XTSgFURiG711gwYKUWLAjRX6LonRTZCFFypYi"
                + "xUr+Vja2FCkloWxFKUqy8hsLZUGxUixQiJQF+XlfzdTp3ts1882ce8/U28xp5nvPc745Z+Y7wYDhR9BwvoAK+OMR9hXx6R49"
                + "IsL9BLyCe6HJgLuAC5kMuAi4bpMBWwG3birgG8AyoU9TAZcB1uk3HP38WMVf8MmHrk0F1JY9PzLIuVcM3ejInlfAbxg0QTth"
                + "cCVoN0MNUC6Ubd2/x/kW2oY2oXMng/IyBwfQwbTSSRmu56BqJx3jGX7Y+6GLWM9LAD8s4wXLOBnnKajPIZj6GP//HOQwxMUW"
                + "cbgFvINDG3RsOeXgvAFVCuDUkH00+KF/CvdxCsiRcrUOKSYpuD6CKjzC2eF7uKgPz+R/gAQ7hEYtGJVlCY0un+BsmwlcjKie"
                + "KuA7bjxbGeJq24JWoYcoEMzaqc9wtOOXoQi6tL2lFTWzWqMBkJZrULsXwFIEn2mCoy2nVRb0yIYkg2OIG9cISGvWlawvRYAH"
                + "iKvVDLgC/w4pIP+7/IXpPDiFyqWA/JMk6aSD9wuUIQVkBZPmElAy1/+6kARye1lgMiDLpUaTAQcBN2kyIF8vX7ObQzKVxHOQ"
                + "gSw261wQxh2wCnAnJgOSbR7qcQgZ9wySy03BmhBAQjot+RMGSEhummag3hivO6GANhc3TrNQtG2nEYA2KKuQFigE5UHcuKc6"
                + "XEwRj4lHJu3QbZzxgL8Py2Ep9LQBRQAAABBkZUJHQkYyQ0JCNThGQkIwMTA5OVly9bMAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAACKUlEQVRYR82XTSgFURiG711gwYKUWLAjRX6LonRTZCFFypYi"
                + "xUr+Vja2FCkloWxFKUqy8hsLZUGxUixQiJQF+XlfzdTp3ts1882ce8/U28xp5nvPc745Z+Y7wYDhR9BwvoAK+OMR9hXx6R49"
                + "IsL9BLyCe6HJgLuAC5kMuAi4bpMBWwG3birgG8AyoU9TAZcB1uk3HP38WMVf8MmHrk0F1JY9PzLIuVcM3ejInlfAbxg0QTth"
                + "cCVoN0MNUC6Ubd2/x/kW2oY2oXMng/IyBwfQwbTSSRmu56BqJx3jGX7Y+6GLWM9LAD8s4wXLOBnnKajPIZj6GP//HOQwxMUW"
                + "cbgFvINDG3RsOeXgvAFVCuDUkH00+KF/CvdxCsiRcrUOKSYpuD6CKjzC2eF7uKgPz+R/gAQ7hEYtGJVlCY0un+BsmwlcjKie"
                + "KuA7bjxbGeJq24JWoYcoEMzaqc9wtOOXoQi6tL2lFTWzWqMBkJZrULsXwFIEn2mCoy2nVRb0yIYkg2OIG9cISGvWlawvRYAH"
                + "iKvVDLgC/w4pIP+7/IXpPDiFyqWA/JMk6aSD9wuUIQVkBZPmElAy1/+6kARye1lgMiDLpUaTAQcBN2kyIF8vX7ObQzKVxHOQ"
                + "gSw261wQxh2wCnAnJgOSbR7qcQgZ9wySy03BmhBAQjot+RMGSEhummag3hivO6GANhc3TrNQtG2nEYA2KKuQFigE5UHcuKc6"
                + "XEwRj4lHJu3QbZzxgL8Py2Ep9LQBRQAAAABJRU5ErkJggg==")
    public void arcClockwiseVsAnticlockwiseDiffer() throws Exception {
        draw("<canvas id='myCanvas' width='40', height='40' style='border: 1px solid red;'></canvas>\n",
                " context.beginPath();\n"
                + " context.moveTo(2, 2);\n"
                + " context.arc(2, 2, 17, 0, Math.PI/2, false);\n"
                + " context.closePath();\n"
                + " context.fill();\n"

                + " context.beginPath();\n"
                + " context.moveTo(25, 25);\n"
                + " context.arc(25, 25, 13, 0, Math.PI/2, true);\n"
                + " context.closePath();\n"
                + " context.fill();");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA60lEQVR4AdSQyxHCMBBDPbRAQVAEdXCgAiqgCa40QB9QAfQB"
                + "epNRsB3/DrmQWcWaXa2sZBNWfv7T8Kq/cB4EWkl/lX/yQaO9MFIfiXYCOzqmyg1var+Eh9BL+ZTmLbCjY6rckO5Fr5PQKzRo"
                + "E13J0Dcmn5JsheCZtfO4ZMiQm0kAL4EZmsWsZuibnSRedM+aeBZqhohIQBJ4DHrM4t7MW4ZO4EQsmXtGL0HLECFJSAQHcHrw"
                + "InqGTkIygIl78AV6hiyQiGQATq+KEUMSbeUA4KL1GjFk+6gX0NGuUcO7bICOdo0atl2i6eqGXwAAAP//15WmRAAAAAZJREFU"
                + "AwAtpCcpojzDOAAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApElEQVQ4T+WT0Q2AIAxEwYncQDcxTmLcRCfRCVxJ2w8Iadoe"
                + "JsQfSfrV412BI4bGKzbmhU+Ay8up11IvJxyouVNtldCJdDPVmfTakQ9qsmsWGXA2Z93oTcg9VahAVWPrUdCUpqkFRFOahl5s"
                + "rE2umQe0NrrXgYItN6OrgD9FAtBjQSCnJUFuLXcyTujIZS4T0A18DZChF1VH1aMvWQtEnNz/IfABG4IjFfQNX+kAAAAASUVO"
                + "RK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAApElEQVQ4T+WT0Q2AIAxEwYncQDcxTmLcRCfRCVxJ2w8Iadoe"
                + "JsQfSfrV412BI4bGKzbmhU+Ay8up11IvJxyouVNtldCJdDPVmfTakQ9qsmsWGXA2Z93oTcg9VahAVWPrUdCUpqkFRFOahl5s"
                + "rE2umQe0NrrXgYItN6OrgD9FAtBjQSCnJUFuLXcyTujIZS4T0A18DZChF1VH1aMvWQtEnNz/IfABG4IjFfQNX+kAAAAASUVO"
                + "RK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA1ElEQVR4AeyQ0Q3CMBBDK1ZgIPhjAebggwVgApbglwXYgw3o"
                + "IPVLZPUaXaV89LPVueezT06Uw7DxFwMvSXbU3vKfDdAkzeXAq6SH0BYaHji3puaTgKdWy4GfOg7RNMcDf+38BN8SPmrGU6vl"
                + "QKaXfnfBBUfzDEfzDEfzXHoM9EncDLBgLXI8EDV4QQxE4EROBnC0CDQ8AI9e4W0gNzrKAXDRRaHhAfjCZGgD0W76AbW08EBq"
                + "ZoFfbQK1tPBAamaB6WKvuAf2vtT63v6G62/T62z+hhMAAAD//yTOSwYAAAAGSURBVAMAxcwiKYpExDIAAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAiklEQVQ4T2NkoDJgJMG8ehxqG5HFYQbaAwVBEg5omvZDxf8D"
                + "6YVAvABNPh7ITwTiAzBxZBfCNMMk0S0hJA82E9lAUg1AtwDDQJAAshcJBQE2eRQXggyEuRIUZiAN8LCBhhEheQwDQfrOATET"
                + "EBvgiFW88qQkGxzmowqPGkhUMOFVNBqGo2FIRggAACy9IRWISF6KAAAAAElFTkSuQmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAiklEQVQ4T2NkoDJgJMG8ehxqG5HFYQbaAwVBEg5omvZDxf8D"
                + "6YVAvABNPh7ITwTiAzBxZBfCNMMk0S0hJA82E9lAUg1AtwDDQJAAshcJBQE2eRQXggyEuRIUZiAN8LCBhhEheQwDQfrOATET"
                + "EBvgiFW88qQkGxzmowqPGkhUMOFVNBqGo2FIRggAACy9IRWISF6KAAAAAElFTkSuQmCC")
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
     * Verifies putImageData() maps source pixels row-major into destination.
     * Source 2x2:
     *   (0,0)=red, (1,0)=green
     *   (0,1)=blue,(1,1)=yellow
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("255,0,0,255|0,255,0,255|0,0,255,255|255,255,0,255")
    public void putImageDataPixelOrder() throws Exception {
        verify("<canvas id='myCanvas' width='2' height='2'></canvas>\n",
                "    var src = new Uint8ClampedArray([\n"
                + "      255,0,0,255,    0,255,0,255,\n"
                + "      0,0,255,255,    255,255,0,255\n"
                + "    ]);\n"
                + "    var img = new ImageData(src, 2, 2);\n"
                + "    context.putImageData(img, 0, 0);\n"
                + "    var d = context.getImageData(0, 0, 2, 2).data;\n"
                + "    log(d[0]+','+d[1]+','+d[2]+','+d[3] + '|'\n"
                + "      + d[4]+','+d[5]+','+d[6]+','+d[7] + '|'\n"
                + "      + d[8]+','+d[9]+','+d[10]+','+d[11] + '|'\n"
                + "      + d[12]+','+d[13]+','+d[14]+','+d[15]);\n");
    }

    /**
     * Verifies dirty rectangle picks exactly one source pixel (1,0)=green
     * and writes it to destination (0,0).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0,0,0,0|0,255,0,255")
    public void putImageDataDirtySinglePixel() throws Exception {
        verify("<canvas id='myCanvas' width='2' height='2'></canvas>\n",
                "    var src = new Uint8ClampedArray([\n"
                + "      255,0,0,255,    0,255,0,255,\n"
                + "      0,0,255,255,    255,255,0,255\n"
                + "    ]);\n"
                + "    var img = new ImageData(src, 2, 2);\n"
                + "    context.putImageData(img, 0, 0, 1, 0, 1, 1);\n"
                + "    var p00 = context.getImageData(0, 0, 1, 1).data;\n"
                + "    var p10 = context.getImageData(1, 0, 1, 1).data;\n"
                + "    log(p00[0]+','+p00[1]+','+p00[2]+','+p00[3] + '|'\n"
                + "      + p10[0]+','+p10[1]+','+p10[2]+','+p10[3]);\n");
    }

    /**
     * Verifies dirty rectangle clips correctly when destination is offset.
     * Takes source pixel (1,1)=yellow and places it at dest (2,1).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("255,255,0,255")
    public void putImageDataDirtyWithDestinationOffset() throws Exception {
        verify("<canvas id='myCanvas' width='4' height='3'></canvas>\n",
                "    var src = new Uint8ClampedArray([\n"
                + "      255,0,0,255,    0,255,0,255,\n"
                + "      0,0,255,255,    255,255,0,255\n"
                + "    ]);\n"
                + "    var img = new ImageData(src, 2, 2);\n"
                + "    context.putImageData(img, 1, 0, 1, 1, 1, 1);\n"
                + "    var d = context.getImageData(2, 1, 1, 1).data;\n"
                + "    log(d[0]+','+d[1]+','+d[2]+','+d[3]);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABnElEQVR4AayUyyttYRiH1zmjMzinMzh/wmFGRkaM5JIyc5kY"
                + "YSADikxFJhK5lMjExGVkqFwyRiHJQImZkSglxADPg/Vte1tk7+x+z3ov3/f+Wpf29zP65t9bw6oEb3s1Cf0PW7FhHTt6IVNj"
                + "NBYgD76k2HDxdbfGpkVcpuA3bMEy2CN8rtjQXSNc+mENVqAWNmEDlLlr1RYf8dawgE0+2iGxHY6hAfogH3bhHGbAHuG9YkM3"
                + "VLLcAsXQBd4xIaiJrAI6wL3OkKZLQx+hlbZfdJb475X4vVI+yzvWdJKqDZxxljQlDTspu+EKlHcg5pks0RiECXDGWdKUNCyk"
                + "nIdYfhB5pJHEEP1SmIMSSPv6Gq7TzFU/GPSdE16k4clLmtP1F1NlEKRhKHJI7pg5giAN/4cq++SCkVMI0rA8VNknfxnZhiAN"
                + "D6gaIVvdM7AH+xCk4SjVMPyBbOT7G8gc0NCTZJqFVfiqqX8Cb8RZxlLS0Mr/pSdJ2hdzIYMH6msYh6TzM4oNWY80bY6iyPPv"
                + "hngJPtYt8Qzs7RDroQcS9QQAAP//GDiSeAAAAAZJREFUAwBHcUv73HC01wAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABgUlEQVQ4T62UuS9FQRSH3/MfWEIs0SoUtkhEQ6NBgiglKqIQ"
                + "nUQiYkskiKVQo6MRa9Q0hALR6RSWWGLXUOD7vcxNzrt5uO/lnuTLZOae+e6ZO3cmGgk5oiH7Ip6wBvEI1PpesEe/CLKCvthW"
                + "uOOku7Tp0APtoH4J1MHzf2IrVJXjcAZN8AYHcAFVUAHLMAiXv4mtsJKkbZiFfVet/QRbjJ1CMYzCSSKpJ5RsGCad6Nu1Wq6N"
                + "a1fpPO0AHPulEha6qlrdQyWlQWmCCnIZO4I8WIduuLJ5Ei7CKmhJQaKRpC5YgAbo8AsfGcgwg1pu0PggMRtevQmqcAL6UhQ+"
                + "MK8T1sISyjME2vVYqMIp6E2xQi25H2bCqvAWkTZowwqf6OioeZHMpnwyKRPerVDbvwn6rxTJCJfIbzPFxL5hAcxBS5LCc/Kr"
                + "4cYvVL8MxqA+YIUv5Oky0dmOC3s5lPNEN4lumr9CR03H9DBRkv/GzidpGprhHnQKvuAOcmAF9IvFLdOKfwCGhFAVxZXYrgAA"
                + "AABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABgUlEQVQ4T62UuS9FQRSH3/MfWEIs0SoUtkhEQ6NBgiglKqIQ"
                + "nUQiYkskiKVQo6MRa9Q0hALR6RSWWGLXUOD7vcxNzrt5uO/lnuTLZOae+e6ZO3cmGgk5oiH7Ip6wBvEI1PpesEe/CLKCvthW"
                + "uOOku7Tp0APtoH4J1MHzf2IrVJXjcAZN8AYHcAFVUAHLMAiXv4mtsJKkbZiFfVet/QRbjJ1CMYzCSSKpJ5RsGCad6Nu1Wq6N"
                + "a1fpPO0AHPulEha6qlrdQyWlQWmCCnIZO4I8WIduuLJ5Ei7CKmhJQaKRpC5YgAbo8AsfGcgwg1pu0PggMRtevQmqcAL6UhQ+"
                + "MK8T1sISyjME2vVYqMIp6E2xQi25H2bCqvAWkTZowwqf6OioeZHMpnwyKRPerVDbvwn6rxTJCJfIbzPFxL5hAcxBS5LCc/Kr"
                + "4cYvVL8MxqA+YIUv5Oky0dmOC3s5lPNEN4lumr9CR03H9DBRkv/GzidpGprhHnQKvuAOcmAF9IvFLdOKfwCGhFAVxZXYrgAA"
                + "AABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAyUlEQVQ4T2NkoDJgpLJ5DDgN/C84UY6BiTGS8W1eJymW4jZQ"
                + "ZPJ/kEGMb3JJ8sWogYjg/z/8w/C/8OQPwFTJD/M0LLnAvI43Lf5n+Mj4NlcAnMxgCoEaLwDZ+mQZyPD/LOObPBNUA4UndTAw"
                + "MpaTZeD//53AHFWBaiDfRFUGVsZrQENZkHMIQS////+H4TezJuOn7DsoBoI4/4UmzQDm33SSDPz3fybju7wMuM9wBfYQSIfg"
                + "4ospApgcuqhSfJFiCLJakso6YiwBAMSpYxVbkG1GAAAAAElFTkSuQmCC")
    public void clipWindingEvenOdd() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>\n",
                "if (typeof ImageData != 'function') { log('no ctor'); return; }\n"
                + "context.rect(6, 2, 2, 16); context.rect(2, 10, 16, 5); context.clip('evenodd');"
                + "context.beginPath(); context.arc(10, 10, 8, 0, 2 * Math.PI);"
                + "context.fillStyle = 'deeppink';context.fill();\n");
    }

    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAiklEQVR4AazSQQ6AIAwEwOqfvfsA/4wuCQa00G2BpAkHOm3I"
                + "7rL4ZDCJJKNOdm4GjcfXJrIMBHaI44w2dGOY2wNDWA8MYxo4hX1BFTPiVOL2pqD8oYphIlHobcEnZ65oVEOANb1lw+oNff1h"
                + "6IyCKhYFu1gEHGJe0MQ8IIWxII1RoDej0dhgGbVuAAAA//84BJj8AAAABklEQVQDAC8YNymOsx6WAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAhElEQVQ4T63SSw6AIAwEUDm0e13rnRVIJBHazrTQsKN5fDpp"
                + "W1ypeE9dZh258WTOZsA7N+0MVnoQ6MIQ6MYsMIRpYBiTwCmsB0WMiFRxWqy+KV9aNAjwd5EKWgXA4VUzoPhFUVAdXgQ0k+AF"
                + "Yaw8IMRaDokpUxgLqhmVLgKfjHLa7y8HXybpNxX5BeGTAAAAEGRlQkcwNEI0NzJBRDMzQkY3OTk10kkJJQAAAABJRU5ErkJg"
                + "gg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAhElEQVQ4T63SSw6AIAwEUDm0e13rnRVIJBHazrTQsKN5fDpp"
                + "W1ypeE9dZh258WTOZsA7N+0MVnoQ6MIQ6MYsMIRpYBiTwCmsB0WMiFRxWqy+KV9aNAjwd5EKWgXA4VUzoPhFUVAdXgQ0k+AF"
                + "Yaw8IMRaDokpUxgLqhmVLgKfjHLa7y8HXybpNxX5BeGTAAAAAElFTkSuQmCC")
    public void clipUsesAllSubpaths() throws Exception {
        draw("<canvas id='myCanvas' width='20', height='20'></canvas>\n",
                "  context.beginPath();\n"
                + "\n"
                + "  // subpath #1: small triangle near top-left\n"
                + "  context.moveTo(1,1);\n"
                + "  context.lineTo(10,1);\n"
                + "  context.lineTo(1,10);\n"
                + "  context.closePath();\n"
                + "\n"
                + "  // subpath #2: small triangle near bottom-right\n"
                + "  context.moveTo(8,8);\n"
                + "  context.lineTo(18,8);\n"
                + "  context.lineTo(8,18);\n"
                + "  context.closePath();\n"
                + "\n"
                + "  context.clip();\n"
                + "  context.fillStyle = 'red';\n"
                + "  context.fillRect(0,0,20,20);\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABKElEQVR4AeyTsS4EURRAz1OoSHR2fYBOKBQKhUah8AEKUfoA"
                + "yW6iQSOxn+ELthOJBIVSYXXbSmw0IrIainGuWLv7KplVkJjck/vem/vOvLmZGQP2gXO5kD2JeWl6wktFITwwl5bF3hCafy7+"
                + "lLA2C0VTXqSQet4IF9flWopPanlN/spTFkzLjKwprZrzeHJhUhZlSfGE+Sty4QmkrnSAWwmxaShOE1jDvavPMhS5ME7lE4s4"
                + "2ZyVscn0/QhhfHcr0FFwU4G3R7crqkvadhz3P7CpG8ewGmvzsHMFCxXYjXmPEDrGVkRq3cH4ISQ/8Eab7GpAewv0QAu6y9B8"
                + "gNfBshDG0/1TqvZs88ybMS9NCHVExInSUYxGYUA4iqa/91/Y70XZ0e/v4TsAAAD//xxf2nEAAAAGSURBVAMAiWBcKTpY08sA"
                + "AAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA40lEQVQ4T2NkYGBoAGJ7IGYE4gNATBEAGQIC9VADQYZTBEa2"
                + "gZNNGRhyvgIDMAwaiJnAeJqBHKD/GRiygPwEIAaqBYMeYJiVIqtBCkOwgUZAQ6QgCv4D9TPC5CEiUAOBgmZAtgpQ6DCQLYnP"
                + "QKALGcOhBp4A0tFA/l2YBqiBjkBDQqEWPCdkIDEuJGggKA0CE3aVNANDOhcDg5wMxEVTtwCD4Ayyd6YBw86cgUHRmIFhFUj8"
                + "EwNDCR8wHNG93IAwMPAXUM8aZAWkstEjBSkMSTUKon5k5xSw90ERRBEYDUPKwxAA2I49lItsoRgAAAAASUVORK5CYII=",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA40lEQVQ4T2NkYGBoAGJ7IGYE4gNATBEAGQIC9VADQYZTBEa2"
                + "gZNNGRhyvgIDMAwaiJnAeJqBHKD/GRiygPwEIAaqBYMeYJiVIqtBCkOwgUZAQ6QgCv4D9TPC5CEiUAOBgmZAtgpQ6DCQLYnP"
                + "QKALGcOhBp4A0tFA/l2YBqiBjkBDQqEWPCdkIDEuJGggKA0CE3aVNANDOhcDg5wMxEVTtwCD4Ayyd6YBw86cgUHRmIFhFUj8"
                + "EwNDCR8wHNG93IAwMPAXUM8aZAWkstEjBSkMSTUKon5k5xSw90ERRBEYDUPKwxAA2I49lItsoRgAAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAj0lEQVQ4T63USw6AIAxFUViZunJxZ7YDCJ9+Hg2dGDG9nJE5"
                + "HZ58uJf6YKH4Fbzgo72bd/sgH7zB4EN7DBqC/B5RNp0UjCibTgruKgedFtxRDjotiCoXnRVElIvOCnpKUecFLaWo84KaUtUh"
                + "QUmp6pDgrDR1aLBXmjo0WJX85Lg56P+whsqpoNdp31EhHPwBPdMeFdd8Eu8AAAAASUVORK5CYII=")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA3ElEQVQ4T62UYQrCMAyFu8MIQ397Epm30CvofRyeQ/C3CB7C"
                + "M/gCnaRZ0r3CAmWwZl+Tt5d2aeXoVuYlDXwBviUPuCHv6OVq4BUJFwL4Rc4GS56z0ECpTqpcCqlMKnTDavjJp0f5d2wcaida"
                + "INu2Zf41tUC2bQ0sNPVss9S2ra7Q1AO2tD3T1AOybbv2iSbljb56rCfWPvirA96Pdi8CisHP2UIPx0qhfSKgtL3LBraa0pMS"
                + "+dVq2jQpEXSyUvOkREBp+5S1dC+F6UP2PtSa1ka5uA+riewmWyHLSz+ljSYVkapvPQAAAABJRU5ErkJggg==")
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA7UlEQVQ4T62U0Q3CMAxE3WEq+OSTGZiADsEMMA5doCPACDAF"
                + "K8DZaqLUtYlBqVRVSk7PubPTjho/XWMelcAn4JtggRG6o6UtgRcIzgHgC5oeL39XTwncYvcRAA7QXD2dzrBm27WaCmhgtl0j"
                + "qxOOAEmmGphtRwOds+wBkkytsZHDRQOFdgAkZ2oBf7GdrXoZ8nrUtoxPsvoNyHtie0803YkOzogsrNaAbPvEJ3gT3RjudVUX"
                + "8+4y297xAAOoG25arZ0wFwZQN9y0GgayENA056uuRi0vdLNtyVR39V+gZFoOsNN586Z42tB68z/2B2u6MhWr0Q5cAAAAAElF"
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABh0lEQVR4AayTOUsDURSFv0QQCxXFpRFsBFERF7QSLVwKCxc"
                + "ULATFSjsLyzTmBiwULCws/Q/GVFZiI5YKLkVQJGCnjajYCOOJkxCTzGSm8HC+uTdv7jtMhnlR/lnhAo0RjGFCKFwgbChrXQ"
                + "Q6ODBGi1LWxAYxmlQrOjiwmsKT1bBSMU03gwMjLGvOtcOq2/hfywP3qCNOO8YAxhzQK/IeIsEsRg87tKnWUiI30JjAuNRwm"
                + "i8yRMgAV+JEFNshpYU7vnlWfSLBLcaFmEaKCjDOgOzfyf5uVB/WzTg0AOsYp0jZABXZeOCdfnXHIqzPNdiFcU9OhcDswj6f"
                + "GIvAoQjyEca4+OCPigPzN4xNtY/Cz2mMwudEQd6BS1RppEP4uRPDc6/nIt3MECSHKa8R78Aov59AbsOr6oKYFy/CdYRJtym"
                + "+egfCaG7sAKNFJEVKtAK7AkIHGvXAmxjE2KJURkxvrw9HR8DwOSnFmyYwRsU1ftrmBmMMj6eMUiojSVjFKTua5YFhw3zmfg"
                + "AAAP//QkE87wAAAAZJREFUAwCjf0gpzAOviQAAAABJRU5ErkJggg==",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABeklEQVQ4T63TPWsVQRTG8d9yDYgYsVKCBFFBgoWN2PgR1DJ"
                + "aWaignWBhsAiac4UUvoCFlSiIpNOU0Y9gIzYWio34gsTYCL4hBInsZhwJO3u9YP7NnOc5zMPOmdnKOlPlahBXHGzWy54mp5"
                + "PhAsO9tJ5KTif/Dgxb8CmpbcKXVBcZJvACric1JdxIdZHBgSsqfW8xnpz3ZuxUWUm6RTvwgZ4XxprjcQi3cm+Vc3higyV7f"
                + "XTcr9zJgWE35rELW1NvWD7jDSaF13+/MOzHY+zI3nB8wGHheS3WHnnWdsse4UD2BvPMiCOmLf0x2jMMG/EQR7NXZgHHhJ/Z"
                + "KQbWrN7uN2zK3lq+mzFauu1yYJjAy6xL9Ey45FXWia7As7iddZkzwt2sEl2BcziR1KLKVDMGrtG80Zr7wslUZ7oC67+j3ng"
                + "TfeFH8uuZ9nEe74Q9aUemHThrzLJ5PadLM2oI+3DHiEnTFrNfDLxq1EVfsx5E2Cya15BpB/4n6x74GwzQSxWBq+FDAAAAEG"
                + "RlQkc0NTkwNzE3NDI0MjkzNjNEaAUR+gAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABiUlEQVQ4T7WUzytEURTHv69HSchqJEkoycJGNv4ELIeVhVH"
                + "slMVMFsJ9ahZ+lIWVKMkOS/wJNpqNBdnIjzTDRvmVmjS+970b4/2Ye/269X3Tveeczzvn3PPGwh8vy4g3h27XbwZHOn8zoM"
                + "CGCxJI/B4oUEPInQLFCH0oBdVnKJAkYFFBUgQu/RxYgAUHlwQ0Ksg1ZtHE00IUNJjhNmycoJ4BMaqHWvEFj3N/iDLcog05D"
                + "OKt2O4BBVr43KWaqVpd4332e+4vqDg5558ZCnTy8IBq+Cbwhv69hB3LuK8lp1GHPPZ53mUIzaAcfZhi+WoFeyhQQdsO1a+B"
                + "7tE+wMxegz30R3q3+8TjygjoM2+7Ouy2w+dQoJ2g05IZ2vSZxpnfJwo4RsdVTcmjLHfdFLhFxyHlnGVpKY6yfPkCJWdUrk0"
                + "Ch02B8uuQgcuUw8AXN1C4PXWoCeqK+1Y9ME1QnkNuYySsRwrcwd81jkycI5MthgZ7OM/bm8Sjpn+eWaCKktPwsfT/Nkbkfw"
                + "S+AwzQSxWgcpYbAAAAAElFTkSuQmCC")
    public void fillWindingRuleNonZero() throws Exception {
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "  context.fillStyle = 'green';"
                + "  var cx = 10, cy = 10, r = 9, ir = 3.5;"
                + "  context.beginPath();"
                + "  for (var i = 0; i < 5; i++) {"
                + "    var outer = (i * 4 * Math.PI / 5) - Math.PI / 2;"
                + "    var inner = outer + 2 * Math.PI / 5;"
                + "    if (i === 0) context.moveTo(cx + r * Math.cos(outer), cy + r * Math.sin(outer));"
                + "    else         context.lineTo(cx + r * Math.cos(outer), cy + r * Math.sin(outer));"
                + "    context.lineTo(cx + ir * Math.cos(inner), cy + ir * Math.sin(inner));"
                + "  }"
                + "  context.closePath();"
                + "  context.fill('nonzero');\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABv0lEQVR4AayUP0gcYRDFf3uGQIoQMXXKEEJICIEQAgkniM31"
                + "WiiCWAgiCtb+m0MRe+EqKzutrLSxFBEtBEsbwUIQREUURU/XN7eu3nmrt4rv5t1833xv3g4fd5vhlZHO0PiJ8YMUSGcY0ElA"
                + "FymQzhC65eVUejpqG5omC3mHM0/H03ZQ2xDa7kzClxiaHjLJB8b5xDB/ZXYhFm5ZZJR/DPGZCT7iWiqRKW2NHHmWMTYJOOJc"
                + "LLJDhv/4mdGr7MypluUNW1ywL+2J6tviqphDiA0XdNhHSL34ngi7qk1Hy7LvM3za3VLF7zWkTg/pwVhAyIhRjLDBW35rsyR6"
                + "LGIc8BCTHKq0KHoslXq813fivaE2DLKH0QwUNN26cnIE+FkB13oP96g0jOt+ZyFf4m1VvqYJ11CNxwzrJR3AmBEbiGE04LWA"
                + "FmXX8BDJhtBIBP8hr5CnvURYUdlrSsQaylHLEEKu9blUU1GsE+N4lmH2tmucb3zXdHOMMqvaV3FK9Ig1vr5j9YSG380xV/zC"
                + "GKaVK2IYRYx+4A8Bp0RaylFt6PdnZBljo1xYsTbW8L+gaysOkl4OxjxpkaBNmjCtXaLuBgAA///Rj8HkAAAABklEQVQDAJG2"
                + "ZykaQwoBAAAAAElFTkSuQmCC",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABwklEQVQ4T62UO0sDQRDH/0cURFSsIiJBVAjBwkb8EkklRCtB"
                + "I6SxEh8oiGZPCBh8dRYmIsFOLWz0S4iNhaKFqEGiNoKKCEHify9rvOQuufOxMJeb3ZnfPHYuGv55aa54i+gz7BZw7GTvDiiw"
                + "bYAEIn8HCjQR8qhAXkKfq0GdMxSYImBZQaYJXPk9MA8NOm4I8ClIBjG0czdfCWrNcBcenKGVDl46+ug6zPdLBfBzL829DGrw"
                + "AD/uMYgPM7wAFOjkc5/SQWk2GYR4dlSSjUCQ+qFp74nv15Qwba++MxTo4aZ0bqtanrUNd7QPEnYq/UpLjqMFOSN6L2WNRpO2"
                + "vRJY5f4E5QS1CGGO5atl7aFAHc/2KFsEHtgCdfSzj6M8G6DNu7WH5V6FsmZovFQhw1nedsLutu3nUCBA0DlFfiFjxSwK2W9Q"
                + "IpyFAOZxUR7QHqgjypI2i5ejUZcrjySfXzMZZaCUO6DADg2HlHGWpcUIk8EFRc6oXGlqI26B8uuQjusUnY5vhqNAvaED45Rb"
                + "6l3OwDhBOQ65h7do0yMF7uZvkiMT5shkzVBrDxNo5P2+lEe21QUamOVrdaArUmUj57+vHwb4BB9yZRXJStrYAAAAAElFTkSu"
                + "QmCC",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABrUlEQVQ4T63TPWsVQRTG8d8SAyIqVkqQICqEYGEjfomkEqKV"
                + "oAncxkp8IULQnAgBg2+dhVEk2GkKG/0SYmOhaCFqkBgbQUWEICu7Gccrd+81YP7NnOc5zLMzZ3cLG0yRq15cdrheL3manK6s"
                + "LzDcS+t4crry78CwHZ+S2il8SXUj6wk8h6tJnReupbqR3oGlwox3GEzOkml7FMqkO+gMfKDPCwP19QqDSifwOnWHFBaUlmyy"
                + "YshHx/xMvZq1wLAPi9iLHalXMSo8yaoijOBx1nzGW4wJb/6cMByk3rw7Oc3X6xzDB4wIzyvx95Vn7bJaP/0Qbghnc6+dcB1n"
                + "8Ey/UVNWfrc6Zxg24yHuCo+y386MI0oTOCr8yH5jYMXatSaFK9lrJ1wwba5jHF0DwzBeUv8hp/Ip1k5/C+P6DLvoVdqRaQ6c"
                + "0VK6ndSSQquuSvNtL6Ml3El1pjkw3MfxpJYVpusxVB31N1qxIJxMdaZbYPVZVBtv1ucN35O/pdacxnthf9qR6QycNWDVoj4T"
                + "TTOqCQcwr9+YKcvZbwycs82kr1n3ImwVvmXdGPifbHjgLx9yZRVdLa+qAAAAEGRlQkc2NjMzNTUxOUYyMkFGRjQ10618dQAA"
                + "AABJRU5ErkJggg==")
    public void fillWindingRuleEvenOdd() throws Exception {
        draw("<canvas id='myCanvas' width='20' height='20'></canvas>\n",
                "  context.fillStyle = 'green';"
                + "  var cx = 10, cy = 10, r = 9, ir = 3.5;"
                + "  context.beginPath();"
                + "  for (var i = 0; i < 5; i++) {"
                + "    var outer = (i * 4 * Math.PI / 5) - Math.PI / 2;"
                + "    var inner = outer + 2 * Math.PI / 5;"
                + "    if (i === 0) context.moveTo(cx + r * Math.cos(outer), cy + r * Math.sin(outer));"
                + "    else         context.lineTo(cx + r * Math.cos(outer), cy + r * Math.sin(outer));"
                + "    context.lineTo(cx + ir * Math.cos(inner), cy + ir * Math.sin(inner));"
                + "  }"
                + "  context.closePath();"
                + "  context.fill('evenodd');\n");
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

    @Test
    @Alerts("255")
    public void saveRestoreRestoresGlobalAlphaForRendering() throws Exception {
        verify("<canvas id='myCanvas' width='2' height='2'></canvas>\n",
                "  context.save();\n"
                + "  context.globalAlpha = 0.5;\n"
                // should restore to 1.0 for both property and drawing behavior
                + "  context.restore();\n"
                + "  context.fillStyle = 'rgba(255,0,0,1)';\n"
                + "  context.fillRect(0,0,1,1);\n"
                + "  var d = context.getImageData(0,0,1,1).data;\n"
                + "  log('' + d[3]);\n");
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
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABF0lEQVQ4T+2SvUoDQRSFv5SC5UIUK30ACUFIbyws4wMsBkO6"
                + "RJ9AiBhUsEqvstoKVgELwdRa5cc6pTbGLqTUM+saNsvGHUljkYFlmDn3fnvPvZNicmV13AiunrW3I3riMfUTkYYT0dwlcMzd"
                + "G7x3wNN+mEgJBfjAZTjdhsoVLIaTd2HYhPMPOLKFGmB2C24fYC0uKQ/9R9iR1rWBGmB5DxqXsBCXUIKRKq9KU0jy8oGy1vCm"
                + "AKWNbmBfcRfJODDAjCzfyfJqXMKmLLegIK1nC2QF6urVwXVkKK6Gcg9nA+k2MBMzfjaa9PE6FLU7n9/PZvAim69Qs4VNAIMk"
                + "McnpM8wnW5vhH44r/EsVv8XOgbN3ct7Df9jDL+YBMhUCa42EAAAAAElFTkSuQmCC")
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

    /**
     * Verifies getImageData() returns data in row-major order:
     * (x increases first within each row, then next row).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("255,0,0,255|0,255,0,255|0,0,255,255|255,255,0,255")
    public void getImageDataPixelOrder() throws Exception {
        verify("<canvas id='myCanvas' width='2' height='2'></canvas>\n",
                // 2x2 image with unique colors per pixel:\n"
                // (0,0)=red,   (1,0)=green\n"
                // (0,1)=blue,  (1,1)=yellow\n"
                "    context.fillStyle = 'rgba(255,0,0,1)'; context.fillRect(0, 0, 1, 1);\n"
                + "context.fillStyle = 'rgba(0,255,0,1)'; context.fillRect(1, 0, 1, 1);\n"
                + "context.fillStyle = 'rgba(0,0,255,1)'; context.fillRect(0, 1, 1, 1);\n"
                + "context.fillStyle = 'rgba(255,255,0,1)'; context.fillRect(1, 1, 1, 1);\n"

                + "var d = context.getImageData(0, 0, 2, 2).data;\n"
                + "var s = ''\n"
                + "  + d[0] + ',' + d[1] + ',' + d[2] + ',' + d[3] + '|'   // (0,0)\n"
                + "  + d[4] + ',' + d[5] + ',' + d[6] + ',' + d[7] + '|'   // (1,0)\n"
                + "  + d[8] + ',' + d[9] + ',' + d[10] + ',' + d[11] + '|' // (0,1)\n"
                + "  + d[12] + ',' + d[13] + ',' + d[14] + ',' + d[15];    // (1,1)\n"
                + "log(s);\n");
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

    private void verify(final String canvasSetup, final String drawJS) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + drawJS
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + canvasSetup
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }
}
