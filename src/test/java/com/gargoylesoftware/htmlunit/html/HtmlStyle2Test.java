/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlStyle}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlStyle2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLStyleElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<style type='text/css' id='myId'>\n"
            + "img { border: 0px }\n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlStyle.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asText() throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "  <style type='text/css' id='s'>\n"
                + "    img { border: 0px }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final String text = driver.findElement(By.id("s")).getText();
        assertEquals("", text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html
                = "<html>\n"
                + "<head>\n"
                + "  <title>foo</title>\n"
                + "  <style type='text/css' id='s'>\n"
                + "    img { border: 0px }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final boolean displayed = driver.findElement(By.id("s")).isDisplayed();
        assertFalse(displayed);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E\nF G H")
    public void getText() throws Exception {
        getText("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E\nF G H")
    public void getTextWhiteSpaceNormal() throws Exception {
        getText("white-space: normal");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E\nF G H")
    public void getTextWhiteSpaceNowrap() throws Exception {
        getText("white-space: nowrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE\nF\nG \n H  ")
    public void getTextWhiteSpacePre() throws Exception {
        getText("white-space: pre");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE\nF\nG \n H  ")
    public void getTextWhiteSpacePreWrap() throws Exception {
        getText("white-space: pre-wrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D \nE\nF\nG \n H")
    public void getTextWhiteSpacePreLine() throws Exception {
        getText("white-space: pre-line");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "A B C D E\nF G H",
            CHROME = "A B C D \nE\nF\nG \n H")
    @NotYetImplemented(CHROME)
    public void getTextWhiteSpaceBreakSpaces() throws Exception {
        getText("white-space: break-spaces");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E\nF G H")
    public void getTextWhiteSpaceUnset() throws Exception {
        getText("white-space: unset");
    }

    private void getText(final String style) throws Exception {
        final String html
                = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <div id='tester' style='" + style + ";'>"
                            + "  A B  C\t \t  D \r\nE<br>F\nG \n H  "
                            + "</div>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        // asText has to be in sync with WebDriver
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().asText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE\nF\nG \n H  ")
    public void getTextPre() throws Exception {
        getTextPre("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E\nF G H")
    public void getTextPreWhiteSpaceNormal() throws Exception {
        getTextPre("white-space: normal");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E\nF G H")
    public void getTextPreWhiteSpaceNowrap() throws Exception {
        getTextPre("white-space: nowrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE\nF\nG \n H  ")
    public void getTextPreWhiteSpacePre() throws Exception {
        getTextPre("white-space: pre");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE\nF\nG \n H  ")
    public void getTextPreWhiteSpacePreWrap() throws Exception {
        getTextPre("white-space: pre-wrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D \nE\nF\nG \n H")
    public void getTextPreWhiteSpacePreLine() throws Exception {
        getTextPre("white-space: pre-line");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "  A B  C     D \nE\nF\nG \n H  ",
            CHROME = "A B C D \nE\nF\nG \n H")
    @NotYetImplemented(CHROME)
    public void getTextPreWhiteSpaceBreakSpaces() throws Exception {
        getTextPre("white-space: break-spaces");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "A B C D E\nF G H",
            IE = "  A B  C     D \nE\nF\nG \n H  ")
    @NotYetImplemented(IE)
    public void getTextPreWhiteSpaceUnset() throws Exception {
        getTextPre("white-space: unset");
    }

    private void getTextPre(final String style) throws Exception {
        final String html
                = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <pre id='tester' style='" + style + ";'>"
                            + "  A B  C\t \t  D \r\nE<br>F\nG \n H  "
                            + "</pre>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        // asText has to be in sync with WebDriver
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().asText());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE<br>F\nG \n H  ")
    public void getTextArea() throws Exception {
        getTextArea("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E<br>F G H")
    public void getTextAreaWhiteSpaceNormal() throws Exception {
        getTextArea("white-space: normal");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D E<br>F G H")
    public void getTextAreaWhiteSpaceNowrap() throws Exception {
        getTextArea("white-space: nowrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE<br>F\nG \n H  ")
    public void getTextAreaWhiteSpacePre() throws Exception {
        getTextArea("white-space: pre");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("  A B  C     D \nE<br>F\nG \n H  ")
    public void getTextAreaWhiteSpacePreWrap() throws Exception {
        getTextArea("white-space: pre-wrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("A B C D \nE<br>F\nG \n H")
    public void getTextAreaWhiteSpacePreLine() throws Exception {
        getTextArea("white-space: pre-line");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "  A B  C     D \nE<br>F\nG \n H  ",
            CHROME = "A B C D \nE<br>F\nG \n H")
    @NotYetImplemented(CHROME)
    public void getTextAreaWhiteSpaceBreakSpaces() throws Exception {
        getTextArea("white-space: break-spaces");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "A B C D E<br>F G H",
            IE = "  A B  C     D \nE<br>F\nG \n H  ")
    @NotYetImplemented(IE)
    public void getTextAreaWhiteSpaceUnset() throws Exception {
        getTextArea("white-space: unset");
    }

    private void getTextArea(final String style) throws Exception {
        final String html
                = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "  <form>"
                + "  <textarea id='tester' style='" + style + ";'>"
                            + "  A B  C\t \t  D \r\nE<br>F\nG \n H  "
                            + "</textarea>\n"
                + "  </form>"
                + "</body>\n"
                + "</html>";

        final WebDriver driver = loadPage2(html);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        // asText has to be in sync with WebDriver
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().asText());
        }
    }
}
