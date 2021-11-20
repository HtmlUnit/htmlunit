/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link TextRange}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author David Gileadi
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class TextRangeTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void text() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        var f = document.getElementById('foo');\n"
            + "        f.focus();\n"
            + "        var r = document.selection.createRange();\n"
            + "        log(f.value);\n"
            + "        r.text = 'bla bla';\n"
            + "        log(f.value);\n"
            + "        r.duplicate().text = 'bli bli';\n"
            + "        log(f.value);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'></textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "BODY")
    public void parentElement() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        log(document.body.createTextRange().parentElement().tagName);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void collapse() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        var f = document.getElementById('foo');\n"
            + "        f.focus();\n"
            + "        f.select();\n"
            + "        var r = document.selection.createRange();\n"
            + "        log(r.text);\n"
            + "        r.collapse();\n"
            + "        log(r.text);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Minimal test: just test that function is available.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void select() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        var r = document.selection.createRange();\n"
            + "        r.select();\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void moveEnd() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        var f = document.getElementById('foo');\n"
            + "        f.focus();\n"
            + "        f.select();\n"
            + "        var r = document.selection.createRange();\n"
            + "        log(r.text);\n"
            + "        r.moveEnd('character', -1);\n"
            + "        log(r.text);\n"
            + "        r.moveStart('character');\n"
            + "        log(r.text);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void moveOutOfBounds_input() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        var f = document.getElementById('foo');\n"
            + "        f.focus();\n"
            + "        f.select();\n"
            + "        var r = document.selection.createRange();\n"
            + "        log(r.text);\n"
            + "        r.moveEnd('character', -1);\n"
            + "        log(r.text);\n"
            + "        r.moveStart('character');\n"
            + "        log(r.text);\n"
            + "        log(r.moveEnd('character', 100));\n"
            + "        log(r.moveStart('character', -100));\n"
            + "        log(r.text);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<input id='foo' value='hello'/>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"true", "true", "false", "true"})
    public void inRange() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      try {\n"
            + "        var r1 = document.body.createTextRange();\n"
            + "        var r2 = r1.duplicate();\n"
            + "        log(r1.inRange(r2));\n"
            + "        log(r2.inRange(r1));\n"
            + "        r1.collapse();\n"
            + "        log(r1.inRange(r2));\n"
            + "        log(r2.inRange(r1));\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<textarea id='foo'>hello</textarea>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for
     * <a href="http://sourceforge.net/support/tracker.php?aid=2836591">Bug 2836591</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void inRange2() throws Exception {
        final String html = "<html><body>\n"
            + "<form name='f'><input name='q' value=''></form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var range = document.f.q.createTextRange();\n"
            + "    var selectionRange = document.selection.createRange();\n"
            + "    log(range.inRange(selectionRange));\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void moveToElementText() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<span id='s1'>abc</span><span id='s2'>xyz</span><span id='s3'>foo</span>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var r = document.selection.createRange();\n"
            + "      r.moveToElementText(document.getElementById('s3'));\n"
            + "      log(r.parentElement().id + ' ' + r.text + ' ' + r.htmlText);\n"
            + "    } catch(e) { log('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = {"hello", "world", "hello world"})
    public void setEndRange() throws Exception {
        final String html = "<html><body>\n"
            + "<form name='f'><input name='q' value='hello world'></form>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var range1 = document.f.q.createTextRange();\n"
            + "    var range2 = range1.duplicate();\n"
            + "    range1.moveEnd('character', -6);\n"
            + "    log(range1.text);\n"
            + "    range2.moveStart('character', 6);\n"
            + "    log(range2.text);\n"
            + "    var r3 = range1.duplicate();\n"
            + "    r3.setEndPoint('EndToEnd', range2);\n"
            + "    log(r3.text);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void createRangeParentElement() throws Exception {
        final String html =
            "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    s = document.selection.createRange();\n"
            + "    p = s.parentElement();\n"
            + "    log(p.tagName);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void createRangeHtmlText() throws Exception {
        final String html =
            "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    s = document.selection.createRange();\n"
            + "    t = s.htmlText;\n"
            + "    log(t);\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "ok")
    public void moveToBookmark() throws Exception {
        final String html =
            "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var rng = document.body.createTextRange();\n"
            + "    rng.moveToBookmark(rng.getBookmark());\n"
            + "    log('ok');\n"
            + "  } catch(e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void compareEndPoints() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      if (document.selection) {\n"
            + "        var r1 = document.selection.createRange();\n"
            + "        var r2 = document.selection.createRange();\n"
            + "        log(r1.compareEndPoints('StartToStart', r2));\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

}
