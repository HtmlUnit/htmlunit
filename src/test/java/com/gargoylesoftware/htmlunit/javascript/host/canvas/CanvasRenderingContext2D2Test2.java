/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CanvasRenderingContext2D2Test2 extends SimpleWebTestCase {

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
            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;''></canvas>"
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
            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;''></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABQAAAAKCAYAAAC0VX7mAAAAGUlEQVR42mNgGErgP4WYfgaS64NRAwcr"
                + "AACGL0e5EdSE/gAAAABJRU5ErkJggg==")
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
            + "  <canvas id='myCanvas' width='20', height='10' style='border: 1px solid red;''></canvas>"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
