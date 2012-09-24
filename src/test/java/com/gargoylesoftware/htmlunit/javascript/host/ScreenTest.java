/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link Screen}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535868.aspx">MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
@RunWith(BrowserRunner.class)
public class ScreenTest extends WebTestCase {

    /**
     * Test all desired properties on screen (easy to copy and test in a real browser).
     * @throws Exception on test failure
     */
    @Test
    @Alerts("16")
    public void testProperties() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var props = {\n"
            + "           availHeight: 768, \n"
            + "           availLeft: 0, \n"
            + "           availTop: 0, \n"
            + "           availWidth: 1024, \n"
            + "           bufferDepth: 24, \n"
            + "           deviceXDPI: 96, \n"
            + "           deviceYDPI: 96, \n"
            + "           fontSmoothingEnabled: true, \n"
            + "           height: 768, \n"
            + "           left: 0, \n"
            + "           logicalXDPI: 96, \n"
            + "           logicalYDPI: 96, \n"
            + "           pixelDepth: 24, \n"
            + "           top: 0, \n"
            + "           updateInterval: 0, \n"
            + "           width: 1024 \n"
            + "       };\n"
            + "       var nbTests = 0;\n"
            + "       for (var i in props) {\n"
            + "           var myExpr = 'window.screen.' + i;\n"
            + "           var result = eval(myExpr);\n"
            + "           if (props[i] != result) {\n"
            + "             alert(myExpr + ': ' + result + ' != ' + props[i]);\n"
            + "           }\n"
            + "           nbTests++;\n"
            + "       }\n"
            + "       alert(nbTests);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
