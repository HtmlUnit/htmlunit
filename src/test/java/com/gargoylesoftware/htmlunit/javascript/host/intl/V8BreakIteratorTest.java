/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link V8BreakIterator}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class V8BreakIteratorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "0, none, 4, letter, 5, none, 8, letter, 9, none, 13, letter, 14, none, 15, none, 19, letter, "
            + "20, none, 24, letter, 25, none, 29, letter, 30, none")
    @NotYetImplemented(CHROME)
    public void sample() throws Exception {
        test("Jack and Jill, went over hill!");
    }

    private void test(final String text) throws Exception {
        final String html = ""
            + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.Intl && window.Intl.v8BreakIterator) {\n"
            + "      var iterator = new Intl.v8BreakIterator(['en']);\n"
            + "      var text = '" + text.replace("'", "\\'") + "';\n"
            + "      iterator.adoptText(text);\n"

            + "      var pos = iterator.first();\n"
            + "      alert(pos);\n"
            + "      alert(iterator.breakType());\n"

            + "      while (pos !== -1) {\n"
            + "        var nextPos = iterator.next();\n"
            + "        if (nextPos === -1) {\n"
            + "          break;\n"
            + "        }\n"

            + "        alert(nextPos);\n"
            + "        alert(iterator.breakType());\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
