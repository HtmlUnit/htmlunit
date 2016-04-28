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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;

/**
 * Tests for {@link HtmlPasswordInput}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlPasswordInput2Test extends SimpleWebTestCase {

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

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeLeftArrow() throws Exception {
        final String html = "<html><head></head><body><input type='password' id='t'/></body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlPasswordInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("tet", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("tet", t.getValueAttribute());
        t.type('s');
        assertEquals("test", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_SPACE);
        assertEquals("tes t", t.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeDelKey() throws Exception {
        final String html = "<html><head></head><body><input type='password' id='t'/></body></html>";
        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlPasswordInput t = page.getHtmlElementById("t");
        t.type('t');
        t.type('e');
        t.type('t');
        assertEquals("tet", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_LEFT);
        t.type(KeyboardEvent.DOM_VK_LEFT);
        assertEquals("tet", t.getValueAttribute());
        t.type(KeyboardEvent.DOM_VK_DELETE);
        assertEquals("tt", t.getValueAttribute());
    }
}
