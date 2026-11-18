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
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlMeta}.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class HtmlMetaTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLMetaElement]")
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<meta id='m' http-equiv='content-type' content='text/html'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('m'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(page.getHtmlElementById("m") instanceof HtmlMeta);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void getText() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><meta id='m' http-equiv='a' content='b'></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("m")).getText();
        assertEquals("", text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><meta id='m' http-equiv='a' content='b'></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        final boolean displayed = driver.findElement(By.id("m")).isDisplayed();
        assertFalse(displayed);
    }

    /**
     * The set-cookie pragma is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void setCookie() throws Exception {
        setCookie("<meta http-equiv='set-cookie' content='meta=1; path=/'>\n", "");
    }

    /**
     * The set-cookie pragma is ignored; the http-equiv value is case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void setCookieUpperCase() throws Exception {
        setCookie("<meta http-equiv='SET-COOKIE' content='meta=1; path=/'>\n", "");
    }

    /**
     * The set-cookie pragma is ignored also in the body.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void setCookieInBody() throws Exception {
        setCookie("", "<meta http-equiv='set-cookie' content='meta=1; path=/'>\n");
    }

    /**
     * The set-cookie pragma of a meta element created by script is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void setCookieCreatedByScript() throws Exception {
        setCookie("", "<script>\n"
                + "  var meta = document.createElement('meta');\n"
                + "  meta.httpEquiv = 'set-cookie';\n"
                + "  meta.content = 'meta=1; path=/';\n"
                + "  document.head.appendChild(meta);\n"
                + "</script>\n");
    }

    /**
     * The set-cookie pragma of a meta element added by innerHTML is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void setCookieInnerHtml() throws Exception {
        setCookie("", "<div id='d'></div>\n"
                + "<script>\n"
                + "  document.getElementById('d').innerHTML = "
                + "\"<meta http-equiv='set-cookie' content='meta=1; path=/'>\";\n"
                + "</script>\n");
    }

    /**
     * The set-cookie pragma of a meta element written by document.write() is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void setCookieDocumentWrite() throws Exception {
        setCookie("", "<script>\n"
                + "  document.write(\"<meta http-equiv='set-cookie' content='meta=1; path=/'>\");\n"
                + "</script>\n");
    }

    /**
     * The set-cookie pragma does not overwrite an existing cookie.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("meta=0")
    public void setCookieDoesNotOverwrite() throws Exception {
        setCookie("<script>document.cookie = 'meta=0; path=/';</script>\n"
                + "<meta http-equiv='set-cookie' content='meta=1; path=/'>\n", "");
    }

    private void setCookie(final String head, final String body) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + head
            + "</head><body>\n"
            + body
            + "<script>\n"
            + "  log(document.cookie);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
