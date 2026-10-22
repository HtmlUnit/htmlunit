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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link HTMLDocument} title property.
 *
 * @author Ronald Brill
 */
public class HTMLDocument4Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc d")
    public void titleBasic() throws Exception {
        titleTest("abc d");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc d")
    public void titleTrim() throws Exception {
        titleTest("   abc d ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void titleEmpty() throws Exception {
        titleTest("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void titleWhitespaceOnlySpaces() throws Exception {
        titleTest("   ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void titleMultipleInternalSpaces() throws Exception {
        titleTest("a  b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void titleLeadingTab() throws Exception {
        titleTest("\ta");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void titleInternalTab() throws Exception {
        titleTest("a\tb");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void titleNewline() throws Exception {
        titleTest("a\nb");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void titleCarriageReturnNewline() throws Exception {
        titleTest("a\r\nb");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void titleMixedWhitespace() throws Exception {
        titleTest("  \n \t  a  \t \n  b  \n \t  ");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00A0")
    public void titleNonBreakingSpace() throws Exception {
        titleTest("\u00A0");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\u00A0b")
    public void titleInternalNonBreakingSpace() throws Exception {
        titleTest("a\u00A0b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a?b")
    public void titleEnQuadSpace() throws Exception {
        titleTest("a\u2000b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a?b")
    public void titleIdeographicFullWidthSpace() throws Exception {
        titleTest("a\u3000b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<title>")
    public void titleEntityLessThanGreaterThan() throws Exception {
        titleTest("&lt;title&gt;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a & b")
    public void titleEntityAmpersand() throws Exception {
        titleTest("a &amp; b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\"hello\"")
    public void titleEntityDoubleQuote() throws Exception {
        titleTest("&quot;hello&quot;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'world'")
    public void titleEntitySingleQuote() throws Exception {
        titleTest("&apos;world&apos;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00A0")
    public void titleEntityNbsp() throws Exception {
        titleTest("&nbsp;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A")
    public void titleEntityDecimalCodePoint() throws Exception {
        titleTest("&#65;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A")
    public void titleEntityHexCodePoint() throws Exception {
        titleTest("&#x41;");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Café")
    public void titleUnicodeLatinExtended() throws Exception {
        titleTest("Café");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Résumé")
    public void titleUnicodeAccents() throws Exception {
        titleTest("Résumé");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void titleUnicodeJapanese() throws Exception {
        titleTest("こんにちは");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("??")
    public void titleUnicodeSupplementaryDeseret() throws Exception {
        titleTest("𐍈");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("??")
    public void titleUnicodeEmoji() throws Exception {
        titleTest("😃");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a ?? b")
    public void titleUnicodeEmojiMixed() throws Exception {
        titleTest("a 😃 b");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b>bold</b>")
    public void titleChildHtmlElement() throws Exception {
        titleTest("<b>bold</b>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<span>a</span> <span>b</span>")
    public void titleChildMultipleElements() throws Exception {
        titleTest("<span>a</span> <span>b</span>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a <!-- comment --> b")
    public void titleHtmlComment() throws Exception {
        titleTest("a <!-- comment --> b");
    }

    /**
     * @throws Exception if the test fails
     */
    public void titleTest(final String titleHtml) throws Exception {
        shutDownAll();

        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "  <title>" + titleHtml + "</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_SESSION_STORAGE_FUNCTION
            + "  log(document.title);\n"
            + "</script></head>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        verifySessionStorage2(driver, getExpectedAlerts()[0]);
    }
}
