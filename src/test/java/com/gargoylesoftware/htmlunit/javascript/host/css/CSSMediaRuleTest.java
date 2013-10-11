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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSMediaRule}.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class CSSMediaRuleTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "2", "screen", "print", "screen, print" },
            IE8 = { "Your browser does not support this example!" })
    // TODO: check which IE versions support what
    public void simple() throws Exception {
        final String html
            = "<html><body>\n"

            + "<style>"
            + "  @media screen, print { p { background-color:#FFFFFF; }};"
            + "</style>\n"

            + "<script>\n"
            + "  var styleSheet = document.styleSheets[0];\n"
            + "  if (styleSheet.cssRules) {\n"
            + "    var rule = styleSheet.cssRules[0];\n"
            + "    var mediaList = rule.media;\n"
            + "    alert(mediaList.length);\n"
            + "    for (var i = 0; i < mediaList.length; i++) {\n"
            + "      alert(mediaList.item(i));\n"
            + "    }\n"
            + "    alert(mediaList.mediaText);\n"
            + "  } else {// Internet Explorer\n"
            + "    alert('Your browser does not support this example!');\n"
            + "  }\n"
            + "</script>\n"

            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
