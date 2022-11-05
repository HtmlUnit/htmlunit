/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link DataUrlDecoder}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DataURLDecoderTest extends WebDriverTestCase {

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts({"one", "two", "three", "four", "five's"})
    public void dataProtocol() throws Exception {
        final String html = "<html><head>\n"
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
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(d1);\n"
            + "  log(d2);\n"
            + "  log(d3);\n"
            + "  log(d4);\n"
            + "  log(d5);\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts(CHROME = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAHBJREFUOE+tkkkOACAIA+3/H40hEYOI"
                + "iAtXYKBNQURUHgq3AACFbx8DeJFLHu8AaWTUaNUDILLDHnA/WAH026GETrU6m2Es7xgwLUQmDua0Qe14+gM9eA0QiDU25UGU"
                + "hS9BcpNode8SOQXpC2B31etXnvKZ0TEDfq8AAAAASUVORK5CYII=",
            EDGE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAHBJREFUOE+tkkkOACAIA+3/H40hEYOI"
                + "iAtXYKBNQURUHgq3AACFbx8DeJFLHu8AaWTUaNUDILLDHnA/WAH026GETrU6m2Es7xgwLUQmDua0Qe14+gM9eA0QiDU25UGU"
                + "hS9BcpNode8SOQXpC2B31etXnvKZ0TEDfq8AAAAASUVORK5CYII=",
            FF = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAcElEQVQ4T62SSQ4AIAgD7f8frZKIQUTEhSsw0KbItdJD4RYA"
                + "INHtYwAtUvHjHcCNiBqpegB4dugD5gcrgHzbldCpWmczjOQdA6YFz8TBnDYoHQ9/IAevAQzRxoY88LLwJUhmErXuXSKnIH0B"
                + "7K5a/QKe8pnRes1vRwAAAABJRU5ErkJggg==",
            FF_ESR = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAcElEQVQ4T62SSQ4AIAgD7f8frZKIQUTEhSsw0KbItdJD4RYA"
                + "INHtYwAtUvHjHcCNiBqpegB4dugD5gcrgHzbldCpWmczjOQdA6YFz8TBnDYoHQ9/IAevAQzRxoY88LLwJUhmErXuXSKnIH0B"
                + "7K5a/QKe8pnRes1vRwAAAABJRU5ErkJggg==",
            IE = "data:image/png;base64,"
                + "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAABwSURBVDhP"
                + "pZPbDsAgCENh///P27qsxhFEGCcxPiht8aLnjTQ43rmMqj5zWQCFGAw+WqBihrnrj8C8YLEG3JtqgcWegSuAgpWjZZvACllc"
                + "AbpF0Un5Gi1LAbpG7iBMsCsGvx4SoPhIkHHzaB9i8zuLXD7IOwUJXM3pAAAAAElFTkSuQmCC")
    public void base64WithPlus() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var canvas = document.getElementById('canvas');\n"
            + "  var img = document.getElementById('image');\n"
            + "  canvas.width = img.width;\n"
            + "  canvas.height = img.height;\n"
            + "  var ctx = canvas.getContext('2d');\n"
            + "  ctx.drawImage(img, 0, 0);\n"
            + "  log(canvas.toDataURL());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <img id='image' src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAABlBMVEU"
            + "AAAD///+l2Z/dAAAAM0lEQVR4nGP4/5/h/1+G/58ZDrAz3D/McH8yw83NDDeNGe4Ug9C9zwz3gVLMDA/A6P9/AFGGFyjOXZtQAAAA"
            + "AElFTkSuQmCC'>\n"
            + "  <canvas id='canvas'></canvas>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @Alerts("HtmlUnit")
    public void withPlus() throws Exception {
        final String dataUrl = "data:text/html;charset=utf-8,"
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  var result = 'Html';\n"
            + "  result += 'Unit§';\n"
            + "  window.document.title = result;\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = getWebDriver();
        driver.get(dataUrl);
        verifyTitle2(driver, getExpectedAlerts());
    }
}
