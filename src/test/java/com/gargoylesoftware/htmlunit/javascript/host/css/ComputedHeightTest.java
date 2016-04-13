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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link ComputedHeight}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ComputedHeightTest extends WebDriverTestCase {

    /**
     * Tests the relation between {@code fontSize} and {@code offsetHeight}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void offsetHeight() throws Exception {
        final String html
            = "<html><head><body>\n"
            + "  <div id='myDiv'>a</div>\n"
            + "  <textarea id='myTextarea' cols='120' rows='20'></textarea>\n"
            + "<script>\n"
            + "var e = document.getElementById('myDiv');\n"
            + "var array = [];\n"
            + "for (var i = 0; i <= 128; i++) {\n"
            + "  e.style.fontSize = i + 'px';\n"
            + "  array.push('array[' + i + '] = ' + e.offsetHeight + ';');\n"
            + "}\n"
            + "document.getElementById('myTextarea').value = array.join('\\n');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        final String expected = loadExpectation("ComputedHeightTest.properties", ".txt");
        final String actual = driver.findElement(By.id("myTextarea")).getAttribute("value");
        assertEquals(expected, actual);
    }
}
