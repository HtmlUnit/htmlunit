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

//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAFklEQVR42mNgGErgP4V41MChYODg"
//                + "BADEpF+hx8ArfgAAAABJRU5ErkJggg==")
//    public void fillRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillRect(2, 2, 16, 6);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAHklEQVR42mNgg"
//                + "ID/VMJwMGrgqIGjBo4aOGrgiDMQAMu0ZqjgcrwWAAAAAElFTkSuQmCC")
//    public void fillRectWidthHeight() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillRect(1, 0, 18, 20);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR"
//                + "42mNgGErgP4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
//    public void fillRectRotate() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillRect(2, 2, 16, 6);\n"
//            + "      context.rotate(.5);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAoElEQVR42mNgGApAEIjZq"
//                + "WngIiC+D8RRQMxIDQP/AvF/KD4LxM6UGvgfC94OxHrUNPA/1OXzgViGWgbC8Hcgbgdifm"
//                + "oZCMNvgLgAiNkIGZgMxE9JMPgeEEcQShFcQFwNxB9JMHgmMd4XBeLJQPyLCAOtSIkoFSB"
//                + "ehcewdeQmKXMgPoRm2G8gVqc08fsB8TWogdOoleeZgTgViMUZRgFNAQB99VqDAzzhfQAA"
//                + "AABJRU5ErkJggg==")
//    public void rotateFillRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.rotate(.5);\n"
//            + "      context.fillRect(2, 2, 16, 6);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR"
//                + "42mNgGErgP4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
//    public void fillRectTranslate() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillRect(2, 2, 16, 6);\n"
//            + "      context.translate(3, 4);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR42mNg"
//                + "GAUjF/ynAI8aOFQMHAWjgJ4AABYtWaeMf5H/AAAAAElFTkSuQmCC")
//    public void translateFillRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.translate(3, 4);\n"
//            + "      context.fillRect(2, 2, 16, 6);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAnUlEQVR42mNg"
//                + "GAWjYHABMyBWoKaBN4D4PxC/AeIdQNwKxIFALEuOYbxA/A9qIDb8Eoi3AnEj"
//                + "EPsCsSQhA+3xGIYLPwXijUBcB8Se6AYWk2EgMr6PbuByCg1cjW4gyNmngPgH"
//                + "mQaW4wpLViA2AuI0IJ4FxOeA+BcRBjqTkgLYgdgUiDOBeC4QXwTi32gGClCa"
//                + "ZjmB2BKIc4G4Z2jlXwDv6F+fy5GnWQAAAABJRU5ErkJggg==")
//    public void rotateTranslateFillRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.rotate(0.2);\n"
//            + "      context.translate(0, 4);\n"
//            + "      context.fillRect(4, 4, 16, 6);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAb0lEQVR42mNgGAW"
//                + "jgGwgDMQs1DQwGYi/AfFxIJ4CxIlArEeJJRJA/A+I/6Nhiiw5hcXA/5RYUkukgd"
//                + "hwDzYDDSgw8CYub5/BEZbEYDVchgoAsRMQlwLxCiC+Q6QlxaSkAmIs2U9p2kW35"
//                + "DpUbKQBAEPdWeLDu8d4AAAAAElFTkSuQmCC")
//    public void transformTranslateFillRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.setTransform(1, .2, .3, 1, 0, 0);\n"
//            + "      context.translate(-5, 4);\n"
//            + "      context.fillRect(4, 4, 16, 6);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAGUlEQVR42mNgGErgP4WYfgaS64NRAwcr"
//                + "AACGL0e5EdSE/gAAAABJRU5ErkJggg==")
//    public void clearRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillRect(2, 2, 16, 6);\n"
//            + "      context.clearRect(4, 4, 6, 6);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAgUlEQVR42mP4/5/hPzUxw"
//                + "6iBI8jAN0D8m5oGzgFiTiC2AOJsIJ4HxBfJsQRm4HMgZmRAA+RYghyGpgxEAEKWIBvYxE"
//                + "AmAOotxmbgeQoMVMOVbIyxhSWRht7ElQ7fA/FeIO4C4nAgVibGEqCaHlISNkFLgHwHSnM"
//                + "KuiUaILERWDgAALV80B81fwPhAAAAAElFTkSuQmCC")
//    public void transformTranslateClearRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillStyle = '#ffff00';\n"
//            + "      context.fillRect(0, 0, 20, 20);\n"
//            + "      context.setTransform(1, .2, .3, 1, 0, 0);\n"
//            + "      context.translate(-5, 4);\n"
//            + "      context.clearRect(4, 4, 16, 6);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAQUlEQVR42mNgoCHgoL"
//                + "Zhr4H4MBBXALEBNQzlAeIQIJ4NxI+heDZUTIAaFugAcQkQ7wbi70Pa9Z+hFlIN8DAM"
//                + "KQAA8ckQClhSuMUAAAAASUVORK5CYII=")
//    public void moveToLineToStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(2, 2);\n"
//            + "      context.lineTo(16, 6);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAgElEQVR42mNgGEr"
//                + "g/6iBI8DA90DMQk0DLwOxBTUNzADi50C8HYhnA3ENEHsAMQ8lhooAsQvU8G4gPg"
//                + "7En4F4PRAHUMvlIEsSgPg6EN8G4hxqhjUojPcD8WMgTqFmeJtAgwNkuAI1DQZ5/"
//                + "z4QN1PTUFAYr6aFa0OgkUZVIED1UhoAGzcax1ioxw8AAAAASUVORK5CYII=")
//    public void moveToBezierCurveToStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(2, 2);\n"
//            + "      context.bezierCurveTo(2, 17, 1, 4, 19, 17);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAlklEQVR42mNgGErg"
//            + "OxDfBuL9QNwNxAlArEGJgRxQAzyAuAGIVwPxayC+D7VAhRquZgFiCyBuhhoOcr0P"
//            + "tYIE5IMcIH4MxOuBWIKaBndDXexCzUgEhfV7agYBA9SFn6HhTDVQAsTnoRFINbAf"
//            + "ajDVgAY0U1DVlYepHes1QDybmgbaAPFpahqoAM1FVAM80BKLquD/4C+lAU3wGvNN"
//            + "UrLUAAAAAElFTkSuQmCC")
//    public void moveToQuadraticCurveToStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(2, 2);\n"
//            + "      context.quadraticCurveTo(19, 4, 19, 17);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAJElEQVR42m"
//            + "NgGAWjYHAAByBuoCIGE/+piIeAgVQPw1EwCkgEAB1tTchSTfsUAAAAAElF"
//            + "TkSuQmCC")
//    public void lineWidthMoveToLineToStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.lineWidth = 4;\n"
//            + "      context.moveTo(2, 10);\n"
//            + "      context.lineTo(18, 10);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAcElEQVR42mNgGPGABYjdg"
//            + "ViEmoZeAeL/QPwQiNcBcRWllrRDDcSGYZaUkWKgFR4DkbE2sQYyAfFrIgysIMWVC4kw8C"
//            + "gpBoYQYeBfUiKKF4g3A/EzAobGkRPrkkDsA8T1WCxZTa20CrOkaLQsGAVUAACFI0GL08K"
//            + "upwAAAABJRU5ErkJggg==")
//    public void setTransformFillRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.setTransform(1, .2, .3, 1, 0, 0);\n"
//            + "      context.fillRect(3, 3, 10, 7);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAcElEQVR42mNgGPGABYjdg"
//            + "ViEmoZeAeL/QPwQiNcBcRWllrRDDcSGYZaUkWKgFR4DkbE2sQYyAfFrIgysIMWVC4kw8C"
//            + "gpBoYQYeBfUiKKF4g3A/EzAobGkRPrkkDsA8T1WCxZTa20CrOkaLQsGAVUAACFI0GL08K"
//            + "upwAAAABJRU5ErkJggg==")
//    public void transformFillRect() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.transform(1, .2, .3, 1, 0, 0);\n"
//            + "      context.fillRect(3, 3, 10, 7);\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGUlEQVR42m"
//            + "NgGAWjYBSQBv6TiEfBKBg5AABEEAv1yW5vkQAAAABJRU5ErkJggg==")
//    public void moveToLineToTransformStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(2, 10);\n"
//            + "      context.lineTo(13, 10);\n"
//            + "      context.transform(1, .2, .3, 1, 0, 0);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAVklEQVR42mNgGAWjYAQB"
//            + "Dmob9h6IbwPxaiCuAeIAIFag1FADII4B4n4g3g3Er6EWHQbiyUCcAcQWQMxDiUUSQOwB"
//            + "xCVAvBiILwPxZ0oNRQc8o0lupAEAZ+MMgTDcUPEAAAAASUVORK5CYII=")
//    public void transformMoveToLineToStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.transform(1, .2, .3, 1, 0, 0);\n"
//            + "      context.moveTo(2, 10);\n"
//            + "      context.lineTo(13, 10);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAGUlEQVR42mNgGAW"
//            + "jYBSQBv5TAY+CUTA8AQC0ZBDwpDXmmwAAAABJRU5ErkJggg==")
//    public void moveToLineToRotateStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(2, 10);\n"
//            + "      context.lineTo(18, 10);\n"
//            + "      context.rotate(90);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAfUlEQVR42mNgYGDgYKAyeA7E84"
//            + "FYgloGqgDxciD+DMT9QCxALYMNgHg7EL8H4hog5qGWwQ5AfBoaFDlAzEItg32A+DYUR1DLYJAh"
//            + "CVDXngdiD2q5FpS0CqDhux+ITahlMCgFNENTxGZoCqEKAKXZyVAXUzVj8DCMglEwEgAA1S0T6X"
//            + "hdMmMAAAAASUVORK5CYII=")
//    public void rotateMoveToLineToStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.rotate(.5);\n"
//            + "      context.moveTo(1, 1);\n"
//            + "      context.lineTo(18, 1);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAG0lEQVR42mNgGErgP"
//            + "4V41MChYOAoGAWjYOgCAGnPX6EKEWk8AAAAAElFTkSuQmCC")
//    public void rectFill() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.rect(2, 2, 16, 6);\n"
//            + "      context.fill();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAoUlEQVR42mNgGAVAYAHE04F4PhCXALE"
//            + "MpYa9B+ICIE4B4uVA/B2Iu4GYhxwDZ0MNQwYqQLwdiK8DsQY5BmbgkMuBut6FFANzoN7EBTyA+DkQCx"
//            + "BrICgCPhPwmgCp3m4G4t1AzEKtZAOKzctYIocioAGNgABqGupCS0MzqO39+0C8HprIKYpxZI3TodlwO"
//            + "dTFGaSmSVzptACao+aTmmuGKQAA9NQeZdHpsYYAAAAASUVORK5CYII=")
//    public void ellipseStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAlUlEQVR42mNgGAUg8P8/gwMQhwCxATUM2"
//            + "wzE/5HwYyDOAGIOcl32Hwc+D8QKpBoYgsdAEP4MxC6kGGhAwECYoRykGHqbCENDSDEwhQgD15NiIAsQny"
//            + "Zg4Hsg5iHFUBmoJnyG+pAa4y7QCMBl4Gxy0qULHpe+Jzexy+AJUxdysyMLNPuhJ6lmauR1A2iOAgUHy2h"
//            + "RygAA45xHqtsvRxIAAAAASUVORK5CYII=")
//    public void ellipseFill() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillStyle = 'yellow';\n"
//            + "      context.ellipse(10, 10, 8, 4, Math.PI / 4, 0, 1.5 * Math.PI);\n"
//            + "      context.fill();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAc0lEQVR42mNgGAVDEvBQ07ASIH4"
//            + "NxBzUMCwBiB8DsQa1XAcyzINahikA8XMgZqGWgSZQF+IDv0mN2c9ALIJDXgWIT5PqynYgng7EAl"
//            + "A+KKZ1oOIg17uQaiALVPNrqAHfgfg2EC+GGkw24IBGEs9oHh+iAAAZGRFncAWu2AAAAABJRU5ErkJggg==")
//    public void arcStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.arc(10, 10, 4, 0, 4.3);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAb0lEQVR42mNgGAWjgAeIG4D4"
//            + "MhD/B+LbQNwOFSfLsMNAvBmITaB8HSBeDcTnyTG0AWoYNrAa6lKSwGWoy7ABDaj3SQKgMBPA"
//            + "IccBlR9YF1I9DEGxeBopljmgLgMZdp2SpNMANQCWDrvJNWwUDBAAAFFYGYXPy8e+AAAAAElF"
//            + "TkSuQmCC")
//    public void arcCircleStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.arc(10, 10, 4, 0, 2 * Math.PI);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAATklEQVR42mNgGAVDEsgAcQIQNwBxDh"
//            + "ALUGIYCxC/BuLdQNwOxN1AfB+IfSgxlAONbwDEj4FYgprBAPL+ZGoaqADE76kdWf9H0+soGDEAADmG"
//            + "CbQK8bPUAAAAAElFTkSuQmCC")
//    public void arcAnticlockwiseStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.arc(10, 10, 4, 0, 4.3, true);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAb0lEQVR42mNgGAWjgAeIG4D4"
//            + "MhD/B+LbQNwOFSfLsMNAvBmITaB8HSBeDcTnyTG0AWoYNrAa6lKSwGWoy7ABDaj3SQKgMBPA"
//            + "IccBlR9YF1I9DEGxeBopljmgLgMZdp2SpNMANQCWDrvJNWwUDBAAAFFYGYXPy8e+AAAAAElF"
//            + "TkSuQmCC")
//    public void arcCircleAnticlockwiseStroke() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.arc(10, 10, 4, 0, 2 * Math.PI, true);\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAU0lEQVR42mNgGAUjHTQwGADxdiB+DsTvgXg/Qz2D"
//            + "CSWGfQbi/2j4N9BQC3IM3I3FMBg+TI6Bz/EY+H5QGEh1L1M5UhCGglz6GppsDpNv2CgYPgAA3MZQoURRzTMAAAAA"
//            + "SUVORK5CYII=")
//    public void arcFillPath() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillStyle = 'green';\n"
//            + "      context.beginPath();\n"
//            + "      context.arc(10, 10, 4, 0, 2 * Math.PI);\n"
//            + "      context.fill();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,"
//            + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAp0lEQVR42mNgGLqgnkGFoYGhHYgvA/F3IP4NZYP"
//            + "EdIg3qIGBA4i7oQb8x4F/Qw3mIGQYDxCfxmMQOt4NxCz4DJxPgmEw3I0rzEwIeBMf1sDmuslkGgbC/dgMPE2Bgb"
//            + "exGfidAgP/YzOQ3PDbDMQ22Ay8TIIhIN9Mxx4ZpCWZ50BcA8QixGQ1fMkG5PoUcMInCTQwrMaSE1zw5wbC+fg01"
//            + "PsaDKNgQAAAd7buKpKXkaMAAAAASUVORK5CYII=")
//    public void arcFillPathAngle() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.fillStyle = 'green';\n"
//            + "      context.beginPath();\n"
//            + "      context.arc(10, 10, 8, 2.3, 2 * Math.PI);\n"
//            + "      context.fill();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAYU"
//            + "lEQVR42mNgGAXUBhJA/J8MLIHP0PlQTAwgSq0KEH+H0tRQR7TNpPiEoO0kuY4YF5DkOkKu"
//            + "IMt1+FxClutwuYYi12FzEUWuQ3elBzVch+zK/9RwHbIr/1PLdTBgMzzKPwBs1inGPcAUbg"
//            + "AAAABJRU5ErkJggg==")
//    public void closePath() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(4,4);\n"
//            + "      context.lineTo(10,16);\n"
//            + "      context.lineTo(16,4);\n"
//            + "      context.closePath();\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
//            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
//    public void closePathNoSubpath() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.closePath();\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
//            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
//    public void closePathPointOnly() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(4,4);\n"
//            + "      context.closePath();\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAAFUl"
//            + "EQVR42mNgGAWjYBSMglEwCqgDAAZUAAEyx8IOAAAAAElFTkSuQmCC")
//    public void closePathTwice() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.closePath();\n"
//            + "      context.closePath();\n"
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA"
//            + "XklEQVR42mNgoCHgIFGcoGHPgVgFTRzEfw3E8kD8Hw+WwGbofCjGJUZIHgOAXPMdyZWk"
//            + "8hkIuZIcF+N0pQcO2wnJ43Tlfzy2E5LH6sr/eGwnJI8V2FAoPwpGwSjACwDUtCTLu8r4"
//            + "+AAAAABJRU5ErkJggg==")
//    public void closePathClosesOnlyLastSubpath() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//            + "      context.moveTo(2,2);\n"
//            + "      context.lineTo(5,8);\n"
//            + "      context.lineTo(8,2);\n"
//
//            + "      context.moveTo(10,2);\n"
//            + "      context.lineTo(13,8);\n"
//            + "      context.lineTo(16,2);\n"
//            + "      context.closePath();\n"
//
//            + "      context.stroke();\n"
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAA"
//            + "N0lEQVR42mNg2Mf8HwWTCjD0U9vAfQzM/5Exyeah66e6gbjDglJAbQMp9TrtDRwFo2BI"
//            + "g9EyAADAuXABKJOUSgAAAABJRU5ErkJggg==",
//            IE = "no ctor")
//    public void putImageDataInside() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"
//
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//
//            + "      var arr = new Uint8ClampedArray(64);\n"
//            + "      for (var i = 0; i < 32; i += 4) {\n"
//            + "        arr[i + 0] = 0; arr[i + 1] = 190; arr[i + 2] = 3; arr[i + 3] = 255;\n"
//            + "      }\n"
//            + "      for (var i = 32; i < 64; i += 4) {\n"
//            + "        arr[i + 0] = 190; arr[i + 1] = 0; arr[i + 2] = 3; arr[i + 3] = 255;\n"
//            + "      }\n"
//
//            + "      var imageData = new ImageData(arr, 4, 4);\n"
//            + "      context.putImageData(imageData, 0, 0);\n"
//            + "      context.putImageData(imageData, 2, 4);\n"
//            + "      context.putImageData(imageData, 16, 0);\n"
//            + "      context.putImageData(imageData, 16, 16);\n"
//
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAA"
//            + "CNiR0NAAAANUlEQVR42mNg2Mf8H4T3MaBiBrIBtQ2k3EW0NhDd63A86AykXizTysBRMA"
//            + "pGAV0A1UopNAMBDtE/AR/N0RYAAAAASUVORK5CYII=",
//            IE = "no ctor")
//    public void putImageDataOutside() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"
//
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//
//            + "      var arr = new Uint8ClampedArray(64);\n"
//            + "      for (var i = 0; i < 32; i += 4) {\n"
//            + "        arr[i + 0] = 0; arr[i + 1] = 190; arr[i + 2] = 3; arr[i + 3] = 255;\n"
//            + "      }\n"
//            + "      for (var i = 32; i < 64; i += 4) {\n"
//            + "        arr[i + 0] = 190; arr[i + 1] = 0; arr[i + 2] = 3; arr[i + 3] = 255;\n"
//            + "      }\n"
//
//            + "      var imageData = new ImageData(arr, 4, 4);\n"
//            + "      context.putImageData(imageData, -2, 0);\n"
//            + "      context.putImageData(imageData, 2, -2);\n"
//            + "      context.putImageData(imageData, 2, 4);\n"
//            + "      context.putImageData(imageData, 18, 18);\n"
//
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
//
//    /**
//     * @throws Exception if the test fails
//     */
//    @Test
//    @Alerts(DEFAULT = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAA"
//            + "CNiR0NAAAANklEQVR42mNgGNRgH/N/CM0AZVBqHpXMoZ2BdApXoLOp6nSqG4g1Keyjpg"
//            + "VUN3AUjIJRwMAAAAneFQEGPcORAAAAAElFTkSuQmCC",
//            IE = "no ctor")
//    public void putImageDataDirty() throws Exception {
//        final String html = "<html><head>\n"
//            + "<script>\n"
//            + "  function test() {\n"
//            + "    if (typeof ImageData != 'function') { alert('no ctor'); return; }\n"
//
//            + "    var canvas = document.getElementById('myCanvas');\n"
//            + "    if (canvas.getContext) {\n"
//            + "      var context = canvas.getContext('2d');\n"
//
//            + "      var arr = new Uint8ClampedArray(64);\n"
//            + "      for (var i = 0; i < 32; i += 4) {\n"
//            + "        arr[i + 0] = 0; arr[i + 1] = 190; arr[i + 2] = 3; arr[i + 3] = 255;\n"
//            + "      }\n"
//            + "      for (var i = 32; i < 64; i += 4) {\n"
//            + "        arr[i + 0] = 190; arr[i + 1] = 0; arr[i + 2] = 3; arr[i + 3] = 255;\n"
//            + "      }\n"
//
//            + "      var imageData = new ImageData(arr, 4, 4);\n"
//            + "      context.putImageData(imageData, 0, 0, 1, 2, 1, 1);\n"
//            + "      context.putImageData(imageData, 4, 4, 0, 2, 2, 2);\n"
//            + "      context.putImageData(imageData, 8, 8, 0, 0, 2, 2);\n"
//            + "      context.putImageData(imageData, 18, 0, 1, 1, 2, 3);\n"
//
//            + "      alert(canvas.toDataURL());\n"
//            + "    }\n"
//            + "  }\n"
//            + "</script>\n"
//            + "</head><body onload='test()'>\n"
//            + "  <canvas id='myCanvas' width='20', height='20' style='border: 1px solid red;'></canvas>"
//            + "</body></html>";
//
//        loadPageWithAlerts(html);
//    }
}
