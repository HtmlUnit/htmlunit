/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Unit tests for {@link CanvasRenderingContext2D}.
 *
 * Most of the test are disabled because the renderer on
 * the build server Ubuntu/Xvfb produces different results.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CanvasRenderingContext2D2Test extends SimpleWebTestCase {

    private static boolean SKIP_ = false;

    static {
        try {
            final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice[] devices = env.getScreenDevices();
            if (devices.length == 1) {
                final GraphicsDevice device = devices[0];
                if (device.getDisplayMode().getBitDepth() < 0) {
                    SKIP_ = true;
                }
            }
        }
        catch (final HeadlessException e) {
            // skip most of the tests in headless mode
            SKIP_ = true;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAHklEQVR42mNgGErgPxUwhoGUOmjUQBoYSNVY"
                + "pgoAANSxK9UjCpiOAAAAAElFTkSuQmCC")
    public void strokeRect() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.strokeRect(2, 2, 16, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAFklEQVR42mNgGErgP4V41MChYODg"
                + "BADEpF+hx8ArfgAAAABJRU5ErkJggg==")
    public void fillRect() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillRect(2, 2, 16, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAHklEQVR42mNggID/VMJwMGrgqIGjBo4aO"
                + "GrgiDMQAMu0ZqjgcrwWAAAAAElFTkSuQmCC")
    public void fillRectWidthHeight() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillRect(1, 0, 18, 20);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR"
                + "42mNgGErgP4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
    public void fillRectRotate() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillRect(2, 2, 16, 6);\n"
            + "      context.rotate(.5);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.rotate(.5);\n"
            + "      context.fillRect(6, 2, 12, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR"
                + "42mNgGErgP4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
    public void fillRectTranslate() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillRect(2, 2, 16, 6);\n"
            + "      context.translate(3, 4);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR42mNg"
                + "GAUjF/ynAI8aOFQMHAWjgJ4AABYtWaeMf5H/AAAAAElFTkSuQmCC")
    public void translateFillRect() throws Exception {
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.translate(3, 4);\n"
            + "      context.fillRect(2, 2, 16, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.rotate(0.2);\n"
            + "      context.translate(0, 4);\n"
            + "      context.fillRect(4, 4, 16, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.setTransform(1, .2, .3, 1, 0, 0);\n"
            + "      context.translate(-5, 4);\n"
            + "      context.fillRect(4, 4, 16, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAIklEQVR42mNgGErgP4WYTgbiAqMGEmUgxF"
                + "TcBpKZdig0EABvINcp3bQJMgAAAABJRU5ErkJggg==")
    public void clearRect() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillRect(2, 2, 16, 6);\n"
            + "      context.clearRect(4, 4, 6, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAu0lEQVR42mP478Lwn5qYYdTAEWNgmM"
                + "T//14cVDSwPeY/GDy9/f//ttn//0/I+P+/wIYsSyAG+vL8///z+38M8O3z///3L5NkCSIMj2/+TxQA"
                + "WXLtOMQSkM+yTFAsQRg4p+I/2WBVNxYDEzXIN/DV4///3VmwJJtT2/////ObPENzLfCkwxSd//8n50"
                + "DC6eZp4iwBBRnRCTtA4P//dAP8llw+TGFOAVkCSkYgSw6u/v//zvn//4NFRmLhAAByuCKZBcaxyQAA"
                + "AABJRU5ErkJggg==")
    public void transformTranslateClearRect() throws Exception {
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillStyle = '#ff4400';\n"
            + "      context.fillRect(0, 0, 20, 20);\n"
            + "      context.setTransform(1, .2, .3, 1, 0, 0);\n"
            + "      context.translate(-5, 4);\n"
            + "      context.clearRect(4, 4, 16, 6);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(2, 2);\n"
            + "      context.lineTo(16, 6);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(2, 2);\n"
            + "      context.bezierCurveTo(2, 17, 1, 4, 19, 17);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(2, 2);\n"
            + "      context.quadraticCurveTo(19, 4, 19, 17);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.lineWidth = 4;\n"
            + "      context.moveTo(2, 10);\n"
            + "      context.lineTo(18, 10);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.setTransform(1, .2, .3, 1, 0, 0);\n"
            + "      context.fillRect(3, 3, 10, 7);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.transform(1, .2, .3, 1, 0, 0);\n"
            + "      context.fillRect(3, 3, 10, 7);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGUlEQVR42m"
            + "NgGAWjYBSQBv6TiEfBKBg5AABEEAv1yW5vkQAAAABJRU5ErkJggg==")
    public void moveToLineToTransformStroke() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(2, 10);\n"
            + "      context.lineTo(13, 10);\n"
            + "      context.transform(1, .2, .3, 1, 0, 0);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.transform(1, .2, .3, 1, 0, 0);\n"
            + "      context.moveTo(2, 10);\n"
            + "      context.lineTo(13, 10);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGUlEQVR42mNgGAW"
            + "jYBSQBv5TAY+CUTA8AQC0ZBDwpDXmmwAAAABJRU5ErkJggg==")
    public void moveToLineToRotateStroke() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(2, 10);\n"
            + "      context.lineTo(18, 10);\n"
            + "      context.rotate(90);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.rotate(.5);\n"
            + "      context.moveTo(1, 1);\n"
            + "      context.lineTo(18, 1);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR42mNgGErgP"
            + "4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
    public void rectFill() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.rect(2, 2, 16, 6);\n"
            + "      context.fill();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      if (context.ellipse) {\n"
            + "        context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI);\n"
            + "        context.stroke();\n"
            + "        alert(canvas.toDataURL());\n"
            + "      } else {\n"
            + "        alert('context.ellipse not supported');\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      if (context.ellipse) {\n"
            + "        context.fillStyle = 'yellow';\n"
            + "        context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI);\n"
            + "        context.fill();\n"
            + "        alert(canvas.toDataURL());\n"
            + "      } else {\n"
            + "        alert('context.ellipse not supported');\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.arc(10, 10, 4, 0, 4.3);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.arc(4, 16, 4, 0, 2 * Math.PI);\n"
            + "      context.stroke();\n"
            + "      context.strokeRect(0, 0, 20, 20);\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.arc(10, 10, 4, 0, 4.3, true);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.arc(10, 10, 4, 0, 2 * Math.PI, true);\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillStyle = 'green';\n"
            + "      context.beginPath();\n"
            + "      context.arc(10, 10, 4, 0, 2 * Math.PI);\n"
            + "      context.fill();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.fillStyle = 'green';\n"
            + "      context.beginPath();\n"
            + "      context.arc(10, 10, 8, 2.3, 2 * Math.PI);\n"
            + "      context.fill();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(4,4);\n"
            + "      context.lineTo(10,16);\n"
            + "      context.lineTo(16,4);\n"
            + "      context.closePath();\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
    public void closePathNoSubpath() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.closePath();\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
    public void closePathPointOnly() throws Exception {
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(4,4);\n"
            + "      context.closePath();\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
    public void closePathTwice() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.closePath();\n"
            + "      context.closePath();\n"
            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"
            + "      context.moveTo(2,2);\n"
            + "      context.lineTo(5,8);\n"
            + "      context.lineTo(8,2);\n"

            + "      context.moveTo(10,2);\n"
            + "      context.lineTo(13,8);\n"
            + "      context.lineTo(16,2);\n"
            + "      context.closePath();\n"

            + "      context.stroke();\n"
            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"

            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

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
            + "      context.putImageData(imageData, 16, 16);\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"

            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

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
            + "      context.putImageData(imageData, 18, 18);\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"

            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

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
            + "      context.putImageData(imageData, 18, 0, 1, 1, 2, 3);\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"

            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

            + "      context.arc(8, 12, 8, 0, 2 * Math.PI);\n"
            + "      context.stroke();\n"
            + "      context.clip();\n"
            + "      context.fillRect(4, 9, 19, 14);\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"

            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

            + "      context.rect(6, 2, 2, 16);\n"
            + "      context.rect(2, 10, 16, 5);\n"
            + "      context.clip('evenodd');\n"

            + "      context.beginPath();\n"
            + "      context.arc(10, 10, 8, 0, 2 * Math.PI);\n"
            + "      context.fillStyle = 'deeppink';\n"
            + "      context.fill();\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAA"
            + "AiklEQVR42mNgYGD4T2UMBygcagBaGPgfhH2A+AsQ7wdiURyKSoD4PhD/AmIPQgbOBmImII4H4mk4"
            + "FHUAMRPIMJChhAyUhHLZgPgDDkUsyHwCBqII/cKmCB8fmwv5kFz4khoG9kC5oDDso4aBaUD8A4g3I"
            + "FxLmoFIqfw/tXMKyGWDO6eMGjhqIDEAALK8kB4mQXliAAAAAElFTkSuQmCC",
            IE = "no ctor")
    public void fillTextAndTransform() throws Exception {
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"

            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

            + "      context.moveTo(0, 0);\n"
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
            + "      context.fillText('n', 0, 0);\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

            + "      context.moveTo(2, 2);\n"
            + "      context.lineTo(10, 18);\n"
            + "      context.lineTo(18, 2);\n"
            + "      context.closePath();\n"
            + "      context.fill();\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

            + "      context.moveTo(2, 2);\n"
            + "      context.lineTo(6, 14);\n"
            + "      context.lineTo(14, 2);\n"
            + "      context.closePath();\n"

            + "      context.setTransform(1.0, 0.0, 0.0, 1.0, 4.0, 4.0);\n"
            + "      context.moveTo(2, 2);\n"
            + "      context.lineTo(6, 14);\n"
            + "      context.lineTo(14, 2);\n"
            + "      context.closePath();\n"

            + "      context.fill();\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        Assume.assumeFalse(SKIP_);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

            + "      context.beginPath();\n"
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
            + "      context.fill();\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAQklEQVR42mNgGAXUBw0M/1HwoDUQaBIKHkQGQgHC"
            + "IHSzB4uBSEYPegOxBwXVI4v6sU+tDEB1A3FmUYoNprqB9CqdACh5i4mujJYUAAAAAElFTkSuQmCC")
    public void saveRestore() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var canvas = document.getElementById('myCanvas');\n"
            + "    if (canvas.getContext) {\n"
            + "      var context = canvas.getContext('2d');\n"

            + "      context.fillStyle = 'green';\n"
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
            + "      context.fillRect(16, 16, 4, 4);\n"

            + "      alert(canvas.toDataURL());\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
