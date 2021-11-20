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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Unit tests for BoxObject.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BoxObjectTest extends WebDriverTestCase {

    /**
     * Tests box object attributes relating to HTML elements: firstChild, lastChild, previousSibling, etc.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void elementAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <span id='foo'>foo</span><div id='d'><span id='a'>a</span><span id='b'>b</span></div><span id='bar'>bar</span>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var div = document.getElementById('d');\n"
            + "          var spanFoo = document.getElementById('foo');\n"
            + "          var spanA = document.getElementById('a');\n"
            + "          var spanB = document.getElementById('b');\n"
            + "          var spanBar = document.getElementById('bar');\n"
            + "          var box = document.getBoxObjectFor(div);\n"
            + "          log(box.element == div);\n"
            + "          log(box.firstChild == spanA);\n"
            + "          log(box.lastChild == spanB);\n"
            + "          log(box.previousSibling == spanFoo);\n"
            + "          log(box.nextSibling == spanBar);\n"
            + "        } catch (e) {log('exception')}\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Tests box object attributes relating to position and size: x, y, screenX, screenY, etc.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void positionAndSizeAttributes() throws Exception {
        final String html =
              "<html>\n"
            + "  <body onload='test()'>\n"
            + "    <style>#d { position:absolute; left:50px; top:100px; width:500px; height:400px; border:3px; padding: 5px; margin: 23px; }</style>\n"
            + "    <div id='d'>daniel</div>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        try {\n"
            + "          var div = document.getElementById('d');\n"
            + "          var box = document.getBoxObjectFor(div);\n"
            + "          log(box.x + '-' + box.y);\n"
            + "          log(box.screenX + '-' + box.screenY);\n"
            + "          log(box.width + '-' + box.height);\n"
            + "        } catch (e) {log('exception')}\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

}
