/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlInsertedText}, and {@link HtmlDeletedText}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HtmlModificationTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId1'));\n"
            + "    alert(document.getElementById('myId2'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  Some text is <ins id='myId1'>inserted</ins> or <del id='myId2'>deleted</del>\n"
            + "</body></html>";

        //both values should be HTMLModElement
        //see http://forums.mozillazine.org/viewtopic.php?t=623715
        final String[] expectedAlerts = {"[object HTMLInsElement]", "[object HTMLDelElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertTrue(HtmlInsertedText.class.isInstance(page.getHtmlElementById("myId1")));
        assertTrue(HtmlDeletedText.class.isInstance(page.getHtmlElementById("myId2")));
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
