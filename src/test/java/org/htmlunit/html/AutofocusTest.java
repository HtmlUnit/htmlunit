/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the {@code autofocus} attribute.
 *
 * @author Ronald Brill
 */
public class AutofocusTest extends WebDriverTestCase {

    /**
     * We like to start with a new browser for each test.
     * @throws Exception If an error occurs
     */
    @AfterEach
    public void shutDownRealBrowsers() throws Exception {
        shutDownAll();
    }

    /**
     * Issue #607: the input with the autofocus attribute is the focused element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i2")
    public void input() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<input id='i2' autofocus='autofocus'>\n");
    }

    /**
     * The active element of the driver is the element with the autofocus attribute.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i2")
    public void activeElementOfDriver() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<input id='i1'>\n"
                + "<input id='i2' autofocus>\n"
                + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        Thread.sleep(200);
        assertEquals(String.join(",", getExpectedAlerts()),
                driver.switchTo().activeElement().getAttribute("id"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("t1")
    public void textArea() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<textarea id='t1' autofocus></textarea>\n");
    }

    /**
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("s1")
    public void select() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<select id='s1' autofocus><option>a</option></select>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b1")
    public void button() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<button id='b1' autofocus>b</button>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a1")
    public void anchor() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<a id='a1' href='#' autofocus>a</a>\n");
    }

    /**
     * A div is not focusable.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void div() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<div id='d1' autofocus>d</div>\n");
    }

    /**
     * A div with tabindex is focusable.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("d1")
    public void divTabIndex() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<div id='d1' tabindex='-1' autofocus>d</div>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("d1")
    @HtmlUnitNYI(CHROME = "BODY", EDGE = "BODY", FF = "BODY", FF_ESR = "BODY")
    public void contentEditable() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<div id='d1' contenteditable='true' autofocus>d</div>\n");
    }

    /**
     * The first element with the autofocus attribute wins.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i1")
    public void twoElements() throws Exception {
        autofocus("<input id='i1' autofocus>\n"
                + "<input id='i2' autofocus>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void disabled() throws Exception {
        autofocus("<input id='i1' disabled autofocus>\n"
                + "<input id='i2'>\n");
    }

    /**
     * A disabled element with the autofocus attribute followed by an enabled one.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i2")
    public void disabledFollowedByEnabled() throws Exception {
        autofocus("<input id='i1' disabled autofocus>\n"
                + "<input id='i2' autofocus>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i2")
    public void readonly() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<input id='i2' readonly autofocus>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void hiddenInput() throws Exception {
        autofocus("<input id='i1' type='hidden' autofocus>\n"
                + "<input id='i2'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void notDisplayed() throws Exception {
        autofocus("<input id='i1' style='display: none' autofocus>\n"
                + "<input id='i2'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void notVisible() throws Exception {
        autofocus("<input id='i1' style='visibility: hidden' autofocus>\n"
                + "<input id='i2'>\n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void hiddenAttribute() throws Exception {
        autofocus("<input id='i1' hidden autofocus>\n"
                + "<input id='i2'>\n");
    }

    /**
     * An element focused by a script before the autofocus is processed.
     * The element with the autofocus attribute is written and the other element is
     * focused by the same script; this way the browser has no chance to process the
     * autofocus in between.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i1")
    public void focusedByScript() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<script>\n"
                + "  document.write(\"<input id='i2' autofocus>\");\n"
                + "  document.getElementById('i1').focus();\n"
                + "</script>\n");
    }

    /**
     * An element focused by a script before the element with the autofocus attribute is parsed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i1")
    public void focusedByScriptBefore() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<script>document.getElementById('i1').focus();</script>\n"
                + "<input id='i2' autofocus>\n");
    }

    /**
     * An element with the autofocus attribute added by a script while parsing.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i2")
    public void addedWhileParsing() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<script>\n"
                + "  var inp = document.createElement('input');\n"
                + "  inp.id = 'i2';\n"
                + "  inp.autofocus = true;\n"
                + "  document.body.appendChild(inp);\n"
                + "</script>\n");
    }

    /**
     * An element with the autofocus attribute added by a script after the page was loaded.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i2")
    public void addedAfterLoad() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<script>\n"
                + "  window.addEventListener('load', function() {\n"
                + "    var inp = document.createElement('input');\n"
                + "    inp.id = 'i2';\n"
                + "    inp.autofocus = true;\n"
                + "    document.body.appendChild(inp);\n"
                + "  });\n"
                + "</script>\n");
    }

    /**
     * An element with the autofocus attribute added by a script after another one was focused.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i1")
    public void addedAfterLoadSecond() throws Exception {
        autofocus("<input id='i1' autofocus>\n"
                + "<script>\n"
                + "  window.addEventListener('load', function() {\n"
                + "    setTimeout(function() {\n"
                + "      var inp = document.createElement('input');\n"
                + "      inp.id = 'i2';\n"
                + "      inp.autofocus = true;\n"
                + "      document.body.appendChild(inp);\n"
                + "    }, 10);\n"
                + "  });\n"
                + "</script>\n");
    }

    /**
     * Setting the autofocus property of an element already in the document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void setAutofocusProperty() throws Exception {
        autofocus("<input id='i1'>\n"
                + "<input id='i2'>\n"
                + "<script>document.getElementById('i2').autofocus = true;</script>\n");
    }

    /**
     * Removing the element with the autofocus attribute while parsing.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BODY")
    public void removedWhileParsing() throws Exception {
        autofocus("<input id='i1' autofocus>\n"
                + "<input id='i2'>\n"
                + "<script>document.body.removeChild(document.getElementById('i1'));</script>\n");
    }

    /**
     * Removing the autofocus attribute while parsing.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("i1")
    public void attributeRemovedWhileParsing() throws Exception {
        autofocus("<input id='i1' autofocus>\n"
                + "<input id='i2'>\n"
                + "<script>document.getElementById('i1').removeAttribute('autofocus');</script>\n");
    }

    /**
     * An element with the autofocus attribute inside an iframe.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"top fr", "frame f2"})
    @HtmlUnitNYI(CHROME = {"top BODY", "frame f2"},
            EDGE = {"top BODY", "frame f2"},
            FF = {"top BODY", "frame f2"},
            FF_ESR = {"top BODY", "frame f2"})
    public void iframe() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    setTimeout(function() {\n"
                + "      var fr = document.getElementById('fr');\n"
                + "      log('top ' + activeId(document));\n"
                + "      log('frame ' + activeId(fr.contentWindow.document));\n"
                + "    }, 200);\n"
                + "  }\n"
                + "  function activeId(doc) {\n"
                + "    var e = doc.activeElement;\n"
                + "    return e ? (e.id || e.tagName) : 'null';\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<input id='i1'>\n"
                + "<iframe id='fr' src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>\n";

        final String frame = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<input id='f1'>\n"
                + "<input id='f2' autofocus>\n"
                + "</body></html>\n";
        getMockWebConnection().setResponse(URL_SECOND, frame);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Loads a page with the given body content and logs the active element some time
     * after the load event.
     * <p>
     * Only the final state is checked; the point in time the browsers process the autofocus
     * (before or after the load event) and the focus events (fired only if the window has the
     * system focus) depend on the browser and the environment and make the tests flaky.
     * </p>
     *
     * @param body the content of the body
     * @throws Exception if the test fails
     */
    private void autofocus(final String body) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    setTimeout(function() { log(activeId()); }, 200);\n"
                + "  }\n"
                + "  function activeId() {\n"
                + "    var e = document.activeElement;\n"
                + "    return e ? (e.id || e.tagName) : 'null';\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + body
                + "</body></html>\n";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
