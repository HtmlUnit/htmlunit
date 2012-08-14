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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlPasswordInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlPasswordInput2Test extends WebTestCase {

    /**
     * How could this test be migrated to WebDriver? How to select the field's content?
     * @throws Exception if an error occurs
     */
    @Test
    public void typeWhileSelected() throws Exception {
        final String html =
              "<html><head></head><body>\n"
            + "<input type='password' id='myInput' value='Hello world'><br>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlPasswordInput input = page.getHtmlElementById("myInput");
        input.select();
        input.type("Bye World");
        assertEquals("Bye World", input.getValueAttribute());
    }

}
