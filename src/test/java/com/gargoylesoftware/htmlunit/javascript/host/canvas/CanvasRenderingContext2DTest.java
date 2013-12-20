/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link CanvasRenderingContext2D}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CanvasRenderingContext2DTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "done",
            IE8 = "exception")
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
    @Alerts(DEFAULT = { "addHitRegion", "drawCustomFocusRing", "drawSystemFocusRing", "ellipse", "getLineDash",
                "removeHitRegion", "scrollPathIntoView", "setLineDash", "33 methods" },
            IE8 = "exception",
            IE11 = { "addHitRegion", "drawCustomFocusRing", "drawSystemFocusRing", "ellipse", "removeHitRegion",
                "scrollPathIntoView", "35 methods" })
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
            + "  var methods = ['" + StringUtils.join(methods, "', '") + "'];\n"
            + "  try {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    for (var i=0; i<methods.length; ++i) {\n"
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
}
