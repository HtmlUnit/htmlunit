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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link TextRange}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class TextRangeTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "", "bla bla", "bla blabli bli" })
    public void text() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var f = document.getElementById('foo');\n"
            + "      f.focus();\n"
            + "      var r = document.selection.createRange();\n"
            + "      alert(f.value);\n"
            + "      r.text = 'bla bla';\n"
            + "      alert(f.value);\n"
            + "      r.duplicate().text = 'bli bli';\n"
            + "      alert(f.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'></textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("BODY")
    public void parentElement() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.body.createTextRange().parentElement().tagName);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "hello", "" })
    public void collapse() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var f = document.getElementById('foo');\n"
            + "      f.focus();\n"
            + "      f.select();\n"
            + "      var r = document.selection.createRange();\n"
            + "      alert(r.text);\n"
            + "      r.collapse();\n"
            + "      alert(r.text);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Minimal test: just test that function is available.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("")
    public void select() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var r = document.selection.createRange();\n"
            + "      r.select();\n"
             + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "hello", "hell", "ell" })
    public void moveEnd() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var f = document.getElementById('foo');\n"
            + "      f.focus();\n"
            + "      f.select();\n"
            + "      var r = document.selection.createRange();\n"
            + "      alert(r.text);\n"
            + "      r.moveEnd('character', -1);\n"
            + "      alert(r.text);\n"
            + "      r.moveStart('character');\n"
            + "      alert(r.text);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "true", "true", "false", "true" })
    public void inRange() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var r1 = document.body.createTextRange();\n"
            + "      var r2 = r1.duplicate();\n"
            + "      alert(r1.inRange(r2));\n"
            + "      alert(r2.inRange(r1));\n"
            + "      r1.collapse();\n"
            + "      alert(r1.inRange(r2));\n"
            + "      alert(r2.inRange(r1));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts(html);
    }

    /**
     * Regression test for
     * <a href="http://sourceforge.net/support/tracker.php?aid=2836591">Bug 2836591</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("false")
    public void inRange2() throws Exception {
        final String html = "<html><body>"
            + "<form name='f'><input name='q' value=''></form>"
            + "<script>"
            + "  var range = document.f.q.createTextRange();\n"
            + "  var selectionRange = document.selection.createRange();\n"
            + "  alert(range.inRange(selectionRange));\n"
            + "</script>"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("s3 foo <SPAN id=s3>foo</SPAN>")
    public void moveToElementText() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<span id='s1'>abc</span><span id='s2'>xyz</span><span id='s3'>foo</span>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var r = document.selection.createRange();\n"
            + "    r.moveToElementText(document.getElementById('s3'));\n"
            + "    alert(r.parentElement().id + ' ' + r.text + ' ' + r.htmlText);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "hello", "world", "hello world" })
    public void setEndRange() throws Exception {
        final String html = "<html><body>\n"
            + "<form name='f'><input name='q' value='hello world'></form>\n"
            + "<script>\n"
            + "var range1 = document.f.q.createTextRange();\n"
            + "var range2 = range1.duplicate();\n"
            + "range1.moveEnd('character', -6);\n"
            + "alert(range1.text);\n"
            + "range2.moveStart('character', 6);\n"
            + "alert(range2.text);\n"
            + "var r3 = range1.duplicate();\n"
            + "r3.setEndPoint('EndToEnd',  range2)\n"
            + "alert(r3.text)\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }
}
