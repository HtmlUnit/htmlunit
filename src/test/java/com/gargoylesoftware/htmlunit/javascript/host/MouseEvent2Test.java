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

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for mouse events support.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class MouseEvent2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eventCoordinates() throws Exception {
        final URL url = getClass().getClassLoader().getResource("event_coordinates.html");
        assertNotNull(url);

        final WebClient client = getWebClient();
        final HtmlPage page = client.getPage(url);
        assertEquals("Mouse Event coordinates", page.getTitleText());

        final HtmlTextArea textarea = page.getHtmlElementById("myTextarea");
        assertEquals("", textarea.asText());

        page.getHtmlElementById("div1").click();
        assertEquals("Click on DIV(id=div1): true, true, false, false" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        page.getHtmlElementById("span1").click();
        assertEquals("Click on SPAN(id=span1): true, true, true, false" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        page.getHtmlElementById("span2").click();
        assertEquals("Click on SPAN(id=span2): true, false, false, true" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        textarea.click();
        assertEquals("Click on TEXTAREA(id=myTextarea): true, false, false, false" + LINE_SEPARATOR, textarea.asText());
    }
}
