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
 * Tests for {@link ImageData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ImageDataTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"8", "1", "2",
                "0", "190", "3", "255", "0", "190", "3", "255",
              "8", "2", "1",
              "0", "190", "3", "255", "0", "190", "3", "255"},
            IE = "no ctor")
    public void ctorArray() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (typeof ImageData != 'function') { log('no ctor'); return; }"

            + "  var arr = new Uint8ClampedArray(8);\n"
            + "  for (var i = 0; i < arr.length; i += 4) {\n"
            + "    arr[i + 0] = 0;\n"
            + "    arr[i + 1] = 190;\n"
            + "    arr[i + 2] = 3;\n"
            + "    arr[i + 3] = 255;\n"
            + "  }\n"

            + "  var imageData = new ImageData(arr, 1);\n"
            + "  log(imageData.data.length);\n"
            + "  log(imageData.width);\n"
            + "  log(imageData.height);\n"

            + "  var data = imageData.data;\n"
            + "  for (var i = 0; i < data.length; i++) {\n"
            + "    log(data[i]);\n"
            + "  }\n"

            + "  var imageData = new ImageData(arr, 2);\n"
            + "  log(imageData.data.length);\n"
            + "  log(imageData.width);\n"
            + "  log(imageData.height);\n"

            + "  var data = imageData.data;\n"
            + "  for (var i = 0; i < data.length; i++) {\n"
            + "    log(data[i]);\n"
            + "  }\n"

            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"8", "1", "2",
                "0", "190", "3", "255", "0", "190", "3", "255"},
            IE = "no ctor")
    public void ctorArrayWidthHeight() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (typeof ImageData != 'function') { log('no ctor'); return; }"

            + "  var arr = new Uint8ClampedArray(8);\n"
            + "  for (var i = 0; i < arr.length; i += 4) {\n"
            + "    arr[i + 0] = 0;\n"
            + "    arr[i + 1] = 190;\n"
            + "    arr[i + 2] = 3;\n"
            + "    arr[i + 3] = 255;\n"
            + "  }\n"

            + "  var imageData = new ImageData(arr, 1, 2);\n"
            + "  log(imageData.data.length);\n"
            + "  log(imageData.width);\n"
            + "  log(imageData.height);\n"

            + "  var data = imageData.data;\n"
            + "  for (var i = 0; i < data.length; i++) {\n"
            + "    log(data[i]);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"exception", "exception", "exception", "exception", "exception", "exception", "exception"})
    public void ctorArrayInvalid() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"

            + "  try {\n"
            + "    var imageData = new ImageData();\n"
            + "  } catch (e) { log('exception');}\n"

            + "  try {\n"
            + "    var imageData = new ImageData(-2, 1);\n"
            + "  } catch (e) { log('exception');}\n"

            + "  try {\n"
            + "    var imageData = new ImageData(2, -1);\n"
            + "  } catch (e) { log('exception');}\n"

            + "  try {\n"
            + "    var imageData = new ImageData(-2, -1);\n"
            + "  } catch (e) { log('exception');}\n"

            + "  var arr = new Uint8ClampedArray(8);\n"
            + "  try {\n"
            + "    var imageData = new ImageData(arr, 3);\n"
            + "  } catch (e) { log('exception');}\n"

            + "  arr = new Uint8ClampedArray(11);\n"
            + "  try {\n"
            + "    var imageData = new ImageData(arr, 2);\n"
            + "  } catch (e) { log('exception');}\n"

            + "  arr = new Uint8ClampedArray(8);\n"
            + "  try {\n"
            + "    var imageData = new ImageData(arr, 2, 2);\n"
            + "  } catch (e) { log('exception');}\n"

            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"8", "2", "1", "0", "0", "0", "0", "0", "0", "0", "0"},
            IE = "no ctor")
    public void ctorWidthHeight() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (typeof ImageData != 'function') { log('no ctor'); return; }"

            + "  var imageData = new ImageData(2, 1);\n"
            + "  log(imageData.data.length);\n"
            + "  log(imageData.width);\n"
            + "  log(imageData.height);\n"

            + "  var data = imageData.data;\n"
            + "  for (var i = 0; i < data.length; i++) {\n"
            + "    log(data[i]);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"200", "100", "50", "255", "100", "50", "125", "255", "123", "111", "222", "255"})
    public void getImageData() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
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
            + "      log(data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"})
    public void getImageDataOutside() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
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
            + "    var imageData = ctx.getImageData(-10, -10, 3, 1);\n"
            + "    var data = imageData.data;\n"
            + "    for (var i = 0; i < data.length; i++) {\n"
            + "      log(data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"})
    public void getImageDataOutside2() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
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
            + "    var imageData = ctx.getImageData(500, 500, 3, 1);\n"
            + "    var data = imageData.data;\n"
            + "    for (var i = 0; i < data.length; i++) {\n"
            + "      log(data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "200", "100", "50", "255", "100", "50", "125", "255"})
    public void getImageDataPartlyOutside() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
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
            + "    var imageData = ctx.getImageData(-1, 0, 3, 1);\n"
            + "    var data = imageData.data;\n"
            + "    for (var i = 0; i < data.length; i++) {\n"
            + "      log(data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"})
    public void getImageDataPartlyOutside2() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
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
            + "    var imageData = ctx.getImageData(298, 149, 3, 1);\n"
            + "    var data = imageData.data;\n"
            + "    for (var i = 0; i < data.length; i++) {\n"
            + "      log(data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"200", "100", "50", "255", "200", "100", "50", "255", "0", "0", "0", "0"})
    public void getImageDataDrawAfter() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  if (canvas.getContext) {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.fillStyle = 'rgb(200,100,50)';\n"
            + "    ctx.fillRect(0, 0, 2, 2);\n"
            + "    var imageData = ctx.getImageData(0, 0, 3, 1);\n"

            + "    ctx.fillStyle = 'rgba(0, 0, 200, 0.5)';\n"
            + "    ctx.fillRect(1, 0, 2, 2);\n"
            + "    ctx.fillStyle = 'rgb(123,111,222)';\n"
            + "    ctx.fillRect(2, 0, 2, 2);\n"

            + "    var data = imageData.data;\n"
            + "    for (var i = 0; i < data.length; i++) {\n"
            + "      log(data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"})
    public void data() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
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
            + "      log(imageData.data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"8", "1", "2",
                "13", "0", "17", "0", "0", "0", "0", "42"})
    public void setValues() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  if (canvas.getContext) {\n"
            + "    var ctx = canvas.getContext('2d');\n"

            + "    var imageData = ctx.createImageData(1, 2);\n"
            + "    log(imageData.data.length);\n"
            + "    log(imageData.width);\n"
            + "    log(imageData.height);\n"

            + "    imageData.data[0] = 13;\n"
            + "    imageData.data[2] = 17;\n"
            + "    imageData.data[7] = 42;\n"
            + "    imageData.data[8] = 43;\n"
            + "    imageData.data[-5] = 7;\n"
            + "    imageData.data[100] = 11;\n"
            + "    for (var i = 0; i < imageData.data.length; i++) {\n"
            + "      log(imageData.data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"24", "2", "3",
                "0", "0", "17", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"})
    public void createImageData() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  if (canvas.getContext) {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.fillRect(1, 1, 13, 11);\n"

            + "    var imageData = ctx.createImageData(2, 3);\n"
            + "    log(imageData.data.length);\n"
            + "    log(imageData.width);\n"
            + "    log(imageData.height);\n"

            + "    imageData.data[2] = 17;\n"
            + "    for (var i = 0; i < imageData.data.length; i++) {\n"
            + "      log(imageData.data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"24", "2", "3",
                "0", "0", "17", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"})
    public void createImageDataFlipped() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  if (canvas.getContext) {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    ctx.fillRect(1, 1, 13, 11);\n"

            + "    var imageData = ctx.createImageData(-2, -3);\n"
            + "    log(imageData.data.length);\n"
            + "    log(imageData.width);\n"
            + "    log(imageData.height);\n"

            + "    imageData.data[2] = 17;\n"
            + "    for (var i = 0; i < imageData.data.length; i++) {\n"
            + "      log(imageData.data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"8",
                "0", "0", "17", "0", "0", "0", "0", "0",
                "8", "1", "2",
                "0", "0", "0", "0", "0", "0", "0", "0"})
    public void createImageDataFromImageData() throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('myCanvas');\n"
            + "  if (canvas.getContext) {\n"
            + "    var ctx = canvas.getContext('2d');\n"
            + "    var imageData = ctx.createImageData(1, 2);\n"
            + "    log(imageData.data.length);\n"
            + "    imageData.data[2] = 17;\n"
            + "    for (var i = 0; i < imageData.data.length; i++) {\n"
            + "      log(imageData.data[i]);\n"
            + "    }\n"

            + "    var imageDataCopy = ctx.createImageData(imageData);\n"
            + "    log(imageDataCopy.data.length);\n"
            + "    log(imageDataCopy.width);\n"
            + "    log(imageDataCopy.height);\n"
            + "    for (var i = 0; i < imageDataCopy.data.length; i++) {\n"
            + "      log(imageDataCopy.data[i]);\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><canvas id='myCanvas'></canvas></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }
}
