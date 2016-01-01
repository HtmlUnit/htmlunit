/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link ImageData}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ImageDataTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "200, 100, 50, 255, 100, 50, 125, 255, 123, 111, 222, 255",
            CHROME = "200, 100, 50, 255, 101, 50, 125, 255, 123, 111, 222, 255")
    @NotYetImplemented(CHROME)
    public void getImageData() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  if (canvas.getContext) {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.fillStyle = 'rgb(200,100,50)';\n"
            + "    ctx.fillRect(0, 0, 2, 2);\n"
            + "    ctx.fillStyle = 'rgba(0, 0, 200, 0.5)';\n"
            + "    ctx.fillRect(1, 0, 2, 2);\n"
            + "    ctx.fillStyle = 'rgb(123,111,222)';\n"
            + "    ctx.fillRect(2, 0, 2, 2);\n"
            + "    var imageData = ctx.getImageData(0, 0, 3, 1);\n"
            + "    var data = imageData.data;\n"
            + "    for (var i = 0; i < data.length; i++) {\n"
            + "      alert(data[i]);\n"
            + "    }\n"
            + "  }\n"
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
    @Alerts("0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11")
    public void data() throws Exception {
        final String html =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  if (canvas.getContext) {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.fillStyle = 'rgb(200,100,50)';\n"
            + "    ctx.fillRect(0, 0, 2, 2);\n"
            + "    ctx.fillStyle = 'rgba(0, 0, 200, 0.5)';\n"
            + "    ctx.fillRect(1, 0, 2, 2);\n"
            + "    ctx.fillStyle = 'rgb(123,111,222)';\n"
            + "    ctx.fillRect(2, 0, 2, 2);\n"
            + "    var imageData = ctx.getImageData(0, 0, 3, 1);\n"
            + "    for (var i = 0; i < imageData.data.length; i++) {\n"
            + "      imageData.data[i] = i;\n"
            + "      alert(imageData.data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

}
