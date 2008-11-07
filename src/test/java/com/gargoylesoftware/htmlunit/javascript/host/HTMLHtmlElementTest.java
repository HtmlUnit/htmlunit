/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
 * Unit tests for {@link HTMLHtmlElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLHtmlElementTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "2", "HEAD", "BODY", "null", "null" })
    public void childNodes_1() throws Exception {
        // The whitespace in this HTML is very important, because we're verifying
        // that it doesn't get included in the childNodes collection.
        final String html = "<html> \n <body> \n <script>\n"
            + "var nodes = document.documentElement.childNodes;\n"
            + "alert(nodes.length);\n"
            + "alert(nodes[0].nodeName);\n"
            + "alert(nodes[1].nodeName);\n"
            + "alert(nodes[0].previousSibling);\n"
            + "alert(nodes[1].nextSibling);\n"
            + "</script> \n </body> \n </html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "HEAD" })
    public void childNodes_2() throws Exception {
        // The whitespace in this HTML is very important, because we're verifying
        // that it doesn't get included in the childNodes collection.
        final String html = "<html> \n <head> \n <script>\n"
            + "var nodes = document.documentElement.childNodes;\n"
            + "alert(nodes.length);\n"
            + "alert(nodes[0].nodeName);\n"
            + "</script> \n </head> \n </html>";
        loadPageWithAlerts(html);
    }

}
