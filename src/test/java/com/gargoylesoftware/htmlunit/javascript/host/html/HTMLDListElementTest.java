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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLDListElement}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLDListElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "false", "true", "true", "true", "null", "", "blah", "2",
                        "true", "false", "true", "false", "", "null", "", "null" },
            IE6 = { "false", "true", "true", "true", "false", "true", "true", "true",
                        "true", "false", "true", "false", "true", "false", "true", "false" },
            IE8 = { "false", "true", "true", "true", "false", "true", "true", "true",
                        "true", "false", "true", "false", "true", "false", "true", "false" })
    public void compact() throws Exception {
        final String html = "<html><body>\n"
            + "<dl id='dl1'><dt>a</dt><dd>b</dd></dl>\n"
            + "<dl compact='' id='dl2'><dt>a</dt><dd>b</dd></dl>\n"
            + "<dl compact='blah' id='dl3'><dt>a</dt><dd>b</dd></dl>\n"
            + "<dl compact='2' id='dl4'><dt>a</dt><dd>b</dd></dl>\n"
            + "<script>\n"
            + "alert(document.getElementById('dl1').compact);\n"
            + "alert(document.getElementById('dl2').compact);\n"
            + "alert(document.getElementById('dl3').compact);\n"
            + "alert(document.getElementById('dl4').compact);\n"
            + "alert(document.getElementById('dl1').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl2').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl3').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl4').getAttribute('compact'));\n"
            + "document.getElementById('dl1').compact = true;\n"
            + "document.getElementById('dl2').compact = false;\n"
            + "document.getElementById('dl3').compact = 'xyz';\n"
            + "document.getElementById('dl4').compact = null;\n"
            + "alert(document.getElementById('dl1').compact);\n"
            + "alert(document.getElementById('dl2').compact);\n"
            + "alert(document.getElementById('dl3').compact);\n"
            + "alert(document.getElementById('dl4').compact);\n"
            + "alert(document.getElementById('dl1').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl2').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl3').getAttribute('compact'));\n"
            + "alert(document.getElementById('dl4').getAttribute('compact'));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
