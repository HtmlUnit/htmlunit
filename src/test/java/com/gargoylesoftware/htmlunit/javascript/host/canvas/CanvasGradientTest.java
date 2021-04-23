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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link CanvasGradient}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CanvasGradientTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function")
    public void functions() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  var gradient = ctx.createRadialGradient(100, 100, 100, 100, 100, 0);\n"
            + "  alert(typeof gradient.addColorStop);\n"
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
    public void addColorStop() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  var gradient = ctx.createRadialGradient(100, 100, 100, 100, 100, 0);\n"
            + "  gradient.addColorStop(0, 'green');\n"
            + "  gradient.addColorStop(1, 'white');\n"
            + "  alert(gradient);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
