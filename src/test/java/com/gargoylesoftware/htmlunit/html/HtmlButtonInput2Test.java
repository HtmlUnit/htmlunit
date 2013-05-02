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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlButtonInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlButtonInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "initial-initial", "some text-some text", "some text-some text" })
    public void reset() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'some text';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "initial-initial", "default-default", "some text-some text", "newdefault-newdefault" })
    public void defaultValue() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'default';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'some text';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"
            + "    button.defaultValue = 'newdefault';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='button' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
