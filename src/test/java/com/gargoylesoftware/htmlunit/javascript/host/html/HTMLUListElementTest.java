/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HTMLUListElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class HTMLUListElementTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "false", "true", "true", "true", "null", "", "blah", "2",
                   "true", "false", "true", "false", "", "null", "", "null" },
        IE = { "false", "true", "true", "true", "false", "true", "true", "true",
               "true", "false", "true", "false", "true", "false", "true", "false" })
    public void compact() throws Exception {
        final String html = "<html><body>\n"
            + "<ul id='u1'><li>a</li><li>b</li></ul>\n"
            + "<ul compact='' id='u2'><li>a</li><li>b</li></ul>\n"
            + "<ul compact='blah' id='u3'><li>a</li><li>b</li></ul>\n"
            + "<ul compact='2' id='u4'><li>a</li><li>b</li></ul>\n"
            + "<script>\n"
            + "alert(document.getElementById('u1').compact);\n"
            + "alert(document.getElementById('u2').compact);\n"
            + "alert(document.getElementById('u3').compact);\n"
            + "alert(document.getElementById('u4').compact);\n"
            + "alert(document.getElementById('u1').getAttribute('compact'));\n"
            + "alert(document.getElementById('u2').getAttribute('compact'));\n"
            + "alert(document.getElementById('u3').getAttribute('compact'));\n"
            + "alert(document.getElementById('u4').getAttribute('compact'));\n"
            + "document.getElementById('u1').compact = true;\n"
            + "document.getElementById('u2').compact = false;\n"
            + "document.getElementById('u3').compact = 'xyz';\n"
            + "document.getElementById('u4').compact = null;\n"
            + "alert(document.getElementById('u1').compact);\n"
            + "alert(document.getElementById('u2').compact);\n"
            + "alert(document.getElementById('u3').compact);\n"
            + "alert(document.getElementById('u4').compact);\n"
            + "alert(document.getElementById('u1').getAttribute('compact'));\n"
            + "alert(document.getElementById('u2').getAttribute('compact'));\n"
            + "alert(document.getElementById('u3').getAttribute('compact'));\n"
            + "alert(document.getElementById('u4').getAttribute('compact'));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

}
