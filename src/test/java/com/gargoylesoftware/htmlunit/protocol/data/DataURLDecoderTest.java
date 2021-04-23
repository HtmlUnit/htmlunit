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
package com.gargoylesoftware.htmlunit.protocol.data;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DataUrlDecoder}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DataURLDecoderTest extends WebDriverTestCase {

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts({"one", "two", "three", "four", "five's"})
    public void dataProtocol() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "var d1, d2, d3, d4, d5;\n"
            + "</script>\n"
            + "<script src=\"data:text/javascript,d1%20%3D%20'one'%3B\"></script>\n"
            + "<script src=\"data:text/javascript;base64,ZDIgPSAndHdvJzs%3D\"></script>\n"
            + "<script src=\""
            + "data:text/javascript;base64,%5a%44%4d%67%50%53%41%6e%64%47%68%79%5a%57%55%6e%4f%77%3D%3D\"></script>\n"
            + "<script src=\"data:text/javascript;base64,%20ZD%20Qg%0D%0APS%20An%20Zm91cic%0D%0A%207%20\"></script>\n"
            + "<script src=\"data:text/javascript,d5%20%3D%20'five%5Cu0027s'%3B\"></script>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(d1);\n"
            + "  alert(d2);\n"
            + "  alert(d3);\n"
            + "  alert(d4);\n"
            + "  alert(d5);\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(DEFAULT = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAUElEQVQ4jWP4TyFgIFsjAwN5BjAwMMA1oxgAkyAGY/UCugQu"
                + "m/G6gBhnE2UAhiJ8coQUYTOMKC/gDDS6GYBPjORYINoAchITAzbTB84AcgAA8YbvLfsmafwAAAAASUVORK5CYII=",
            CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAcElEQVQ4T62SSQ4AIAgD7f8fjSERg4iIC1dgoE1BRFQeCrcA"
                + "AIVvHwN4kUse7wBpZNRo1QMgssMecD9YAfTboYROtTqbYSzvGDAtRCYO5rRB7Xj6Az14DRCINTblQZSFL0Fyk2h17xI5BekL"
                + "YHfV61ee8pnRMQN+rwAAAABJRU5ErkJggg==",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAcElEQVQ4T62SSQ4AIAgD7f8fjSERg4iIC1dgoE1BRFQeCrcA"
                + "AIVvHwN4kUse7wBpZNRo1QMgssMecD9YAfTboYROtTqbYSzvGDAtRCYO5rRB7Xj6Az14DRCINTblQZSFL0Fyk2h17xI5BekL"
                + "YHfV61ee8pnRMQN+rwAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABwSURBVDhP"
                + "pZPbDsAgCENh///P27qsxhFEGCcxPiht8aLnjTQ43rmMqj5zWQCFGAw+WqBihrnrj8C8YLEG3JtqgcWegSuAgpWjZZvACllc"
                + "AbpF0Un5Gi1LAbpG7iBMsCsGvx4SoPhIkHHzaB9i8zuLXD7IOwUJXM3pAAAAAElFTkSuQmCC")
    public void base64WithPlus() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var canvas = document.getElementById('canvas');\n"
            + "  var img = document.getElementById('image');\n"
            + "  canvas.width = img.width;\n"
            + "  canvas.height = img.height;\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  ctx.drawImage(img, 0, 0);\n"
            + "  alert(canvas.toDataURL());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img id='image' src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAABlBMVEU"
            + "AAAD///+l2Z/dAAAAM0lEQVR4nGP4/5/h/1+G/58ZDrAz3D/McH8yw83NDDeNGe4Ug9C9zwz3gVLMDA/A6P9/AFGGFyjOXZtQAAAA"
            + "AElFTkSuQmCC'>\n"
            + "  <canvas id='canvas'></canvas>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
