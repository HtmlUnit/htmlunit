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
package com.gargoylesoftware.htmlunit.html.serializer;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;
import static java.nio.charset.StandardCharsets.ISO_8859_1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.serializer.HtmlSerializerVisibleText.HtmlSerializerTextBuilder;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Tests for {@link HtmlSerializerVisibleText}.
 * This contains the tests for plain controls.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlSerializerVisibleText2Test extends WebDriverTestCase {

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "baz",
            CHROME = "",
            EDGE = "",
            IE = "<foo>\n<bar>baz</bar>\n</foo>")
    @NotYetImplemented({CHROME, EDGE, IE})
    public void xmlPage() throws Exception {
        final String xml = "<xml>\n"
                + "  <foo>\n"
                + "    <bar>baz</bar>\n"
                + "  </foo>\n"
                + "</xml>";
        final WebDriver driver = loadPage2(xml, URL_FIRST, "text/xml;charset=ISO-8859-1", ISO_8859_1);
        final String text = driver.findElement(By.xpath("//foo")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final XmlPage page = (XmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final DomNode node = page.getFirstByXPath("//foo");
            assertEquals(getExpectedAlerts()[0], node.getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceBreak() throws Exception {
        getVisibleTextWhiteSpaceBreak(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceBreakNormal() throws Exception {
        getVisibleTextWhiteSpaceBreak("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceBreakNowrap() throws Exception {
        getVisibleTextWhiteSpaceBreak("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceBreakPre() throws Exception {
        getVisibleTextWhiteSpaceBreak("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceBreakPreWrap() throws Exception {
        getVisibleTextWhiteSpaceBreak("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceBreakPreLine() throws Exception {
        getVisibleTextWhiteSpaceBreak("pre-line");
    }

    private void getVisibleTextWhiteSpaceBreak(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <br id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputHidden() throws Exception {
        getVisibleTextWhiteSpaceInputHidden(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputHiddenNormal() throws Exception {
        getVisibleTextWhiteSpaceInputHidden("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputHiddenNowrap() throws Exception {
        getVisibleTextWhiteSpaceInputHidden("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputHiddenPre() throws Exception {
        getVisibleTextWhiteSpaceInputHidden("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputHiddenPreWrap() throws Exception {
        getVisibleTextWhiteSpaceInputHidden("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputHiddenPreLine() throws Exception {
        getVisibleTextWhiteSpaceInputHidden("pre-line");
    }

    private void getVisibleTextWhiteSpaceInputHidden(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='hidden' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \r\nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceScript() throws Exception {
        getVisibleTextWhiteSpaceScript(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceScriptNormal() throws Exception {
        getVisibleTextWhiteSpaceScript("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceScriptNowrap() throws Exception {
        getVisibleTextWhiteSpaceScript("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceScriptPre() throws Exception {
        getVisibleTextWhiteSpaceScript("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceScriptPreWrap() throws Exception {
        getVisibleTextWhiteSpaceScript("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceScriptPreLine() throws Exception {
        getVisibleTextWhiteSpaceScript("pre-line");
    }

    private void getVisibleTextWhiteSpaceScript(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <script id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "var x;  \n \t   \r\n var y;</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceStyle() throws Exception {
        getVisibleTextWhiteSpaceStyle(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceStyleNormal() throws Exception {
        getVisibleTextWhiteSpaceStyle("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceStyleNowrap() throws Exception {
        getVisibleTextWhiteSpaceStyle("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceStylePre() throws Exception {
        getVisibleTextWhiteSpaceStyle("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceStylePreWrap() throws Exception {
        getVisibleTextWhiteSpaceStyle("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceStylePreLine() throws Exception {
        getVisibleTextWhiteSpaceStyle("pre-line");
    }

    private void getVisibleTextWhiteSpaceStyle(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <style id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </style>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceNoframes() throws Exception {
        getVisibleTextWhiteSpaceNoframes(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceNoframesNormal() throws Exception {
        getVisibleTextWhiteSpaceNoframes("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceNoframesNowrap() throws Exception {
        getVisibleTextWhiteSpaceNoframes("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceNoframesPre() throws Exception {
        getVisibleTextWhiteSpaceNoframes("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceNoframesPreWrap() throws Exception {
        getVisibleTextWhiteSpaceNoframes("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceNoframesPreLine() throws Exception {
        getVisibleTextWhiteSpaceNoframes("pre-line");
    }

    private void getVisibleTextWhiteSpaceNoframes(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <noframes id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </noframes>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getVisibleTextWhiteSpaceDiv() throws Exception {
        getVisibleTextWhiteSpaceDiv(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getVisibleTextWhiteSpaceDivNormal() throws Exception {
        getVisibleTextWhiteSpaceDiv("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getVisibleTextWhiteSpaceDivNowrap() throws Exception {
        getVisibleTextWhiteSpaceDiv("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  \n I  ")
    public void getVisibleTextWhiteSpaceDivPre() throws Exception {
        getVisibleTextWhiteSpaceDiv("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  \n I  ")
    public void getVisibleTextWhiteSpaceDivPreWrap() throws Exception {
        getVisibleTextWhiteSpaceDiv("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D \nEF\nG \n H\nI")
    public void getVisibleTextWhiteSpaceDivPreLine() throws Exception {
        getVisibleTextWhiteSpaceDiv("pre-line");
    }

    private void getVisibleTextWhiteSpaceDiv(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <div id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  \n I  ")
    public void getVisibleTextWhiteSpacePre() throws Exception {
        getVisibleTextWhiteSpacePre(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getVisibleTextWhiteSpacePreNormal() throws Exception {
        getVisibleTextWhiteSpacePre("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getVisibleTextWhiteSpacePreNowrap() throws Exception {
        getVisibleTextWhiteSpacePre("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  \n I  ")
    public void getVisibleTextWhiteSpacePrePre() throws Exception {
        getVisibleTextWhiteSpacePre("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  \n I  ")
    public void getVisibleTextWhiteSpacePrePreWrap() throws Exception {
        getVisibleTextWhiteSpacePre("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D \nEF\nG \n H\nI")
    public void getVisibleTextWhiteSpacePrePreLine() throws Exception {
        getVisibleTextWhiteSpacePre("pre-line");
    }

    private void getVisibleTextWhiteSpacePre(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <pre id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </pre>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  <br> I  ")
    public void getVisibleTextWhiteSpaceTextArea() throws Exception {
        getVisibleTextWhiteSpaceTextArea(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getVisibleTextWhiteSpaceTextAreaNormal() throws Exception {
        getVisibleTextWhiteSpaceTextArea("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getVisibleTextWhiteSpaceTextAreaNowrap() throws Exception {
        getVisibleTextWhiteSpaceTextArea("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  <br> I  ")
    public void getVisibleTextWhiteSpaceTextAreaPre() throws Exception {
        getVisibleTextWhiteSpaceTextArea("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \nEF\nG \n H  <br> I  ")
    public void getVisibleTextWhiteSpaceTextAreaPreWrap() throws Exception {
        getVisibleTextWhiteSpaceTextArea("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D \nEF\nG \n H <br> I")
    public void getVisibleTextWhiteSpaceTextAreaPreLine() throws Exception {
        getVisibleTextWhiteSpaceTextArea("pre-line");
    }

    private void getVisibleTextWhiteSpaceTextArea(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceTitle() throws Exception {
        getVisibleTextWhiteSpaceTitle(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceTitleNormal() throws Exception {
        getVisibleTextWhiteSpaceTitle("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceTitleNowrap() throws Exception {
        getVisibleTextWhiteSpaceTitle("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceTitlePre() throws Exception {
        getVisibleTextWhiteSpaceTitle("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceTitlePreWrap() throws Exception {
        getVisibleTextWhiteSpaceTitle("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceTitlePreLine() throws Exception {
        getVisibleTextWhiteSpaceTitle("pre-line");
    }

    private void getVisibleTextWhiteSpaceTitle(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head>\n"
            + "<title id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
            + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </title>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            CHROME = "        A B  C     D \nEF\nG \n H   I  \n      Second\n    ",
            EDGE = "        A B  C     D \nEF\nG \n H   I  \n      Second\n    ",
            IE = "A B C D EF G H I Second")
    @NotYetImplemented({CHROME, EDGE, IE})
    public void getVisibleTextWhiteSpaceSelect() throws Exception {
        getVisibleTextWhiteSpaceSelect(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            CHROME = "  A B  C     D \nEF\nG \n H   I  \nSecond",
            EDGE = "  A B  C     D \nEF\nG \n H   I  \nSecond",
            IE = "A B C D EF G H I Second")
    @NotYetImplemented({CHROME, EDGE, IE})
    public void getVisibleTextWhiteSpaceSelectNormal() throws Exception {
        getVisibleTextWhiteSpaceSelect("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            CHROME = "  A B  C     D \nEF\nG \n H   I  \nSecond",
            EDGE = "  A B  C     D \nEF\nG \n H   I  \nSecond",
            IE = "A B C D EF G H I Second")
    @NotYetImplemented({CHROME, EDGE, IE})
    public void getVisibleTextWhiteSpaceSelectNowrap() throws Exception {
        getVisibleTextWhiteSpaceSelect("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "        A B  C     D \nEF\nG \n H   I  \n      Second\n    ",
            FF = "A B C D EF G H I\nSecond",
            FF78 = "A B C D EF G H I\nSecond")
    @NotYetImplemented({FF, FF78})
    public void getVisibleTextWhiteSpaceSelectPre() throws Exception {
        getVisibleTextWhiteSpaceSelect("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "        A B  C     D \nEF\nG \n H   I  \n      Second\n    ",
            FF = "A B C D EF G H I\nSecond",
            FF78 = "A B C D EF G H I\nSecond")
    @NotYetImplemented({FF, FF78})
    public void getVisibleTextWhiteSpaceSelectPreWrap() throws Exception {
        getVisibleTextWhiteSpaceSelect("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            CHROME = "  A B  C     D \nEF\nG \n H   I  \nSecond",
            EDGE = "  A B  C     D \nEF\nG \n H   I  \nSecond",
            IE = "A B C D \nEF\nG \n H I \n Second")
    @NotYetImplemented
    public void getVisibleTextWhiteSpaceSelectPreLine() throws Exception {
        getVisibleTextWhiteSpaceSelect("pre-line");
    }

    private void getVisibleTextWhiteSpaceSelect(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <select id='tester' "
                    + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "      <option>  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </option>\n"
            + "      <option>Second</option>\n"
            + "    </select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputSubmit() throws Exception {
        getVisibleTextWhiteSpaceInputSubmit(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputSubmitNormal() throws Exception {
        getVisibleTextWhiteSpaceInputSubmit("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputSubmitNowrap() throws Exception {
        getVisibleTextWhiteSpaceInputSubmit("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputSubmitPre() throws Exception {
        getVisibleTextWhiteSpaceInputSubmit("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputSubmitPreWrap() throws Exception {
        getVisibleTextWhiteSpaceInputSubmit("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputSubmitPreLine() throws Exception {
        getVisibleTextWhiteSpaceInputSubmit("pre-line");
    }

    private void getVisibleTextWhiteSpaceInputSubmit(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \r\nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputReset() throws Exception {
        getVisibleTextWhiteSpaceInputReset(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputResetNormal() throws Exception {
        getVisibleTextWhiteSpaceInputReset("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputResetNowrap() throws Exception {
        getVisibleTextWhiteSpaceInputReset("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputResetPre() throws Exception {
        getVisibleTextWhiteSpaceInputReset("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputResetPreWrap() throws Exception {
        getVisibleTextWhiteSpaceInputReset("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputResetPreLine() throws Exception {
        getVisibleTextWhiteSpaceInputReset("pre-line");
    }

    private void getVisibleTextWhiteSpaceInputReset(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \r\nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputCheckbox() throws Exception {
        getVisibleTextWhiteSpaceInputCheckbox(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputCheckboxNormal() throws Exception {
        getVisibleTextWhiteSpaceInputCheckbox("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputCheckboxNowrap() throws Exception {
        getVisibleTextWhiteSpaceInputCheckbox("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputCheckboxPre() throws Exception {
        getVisibleTextWhiteSpaceInputCheckbox("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputCheckboxPreWrap() throws Exception {
        getVisibleTextWhiteSpaceInputCheckbox("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputCheckboxPreLine() throws Exception {
        getVisibleTextWhiteSpaceInputCheckbox("pre-line");
    }

    private void getVisibleTextWhiteSpaceInputCheckbox(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='checkbox' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \r\nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputRadio() throws Exception {
        getVisibleTextWhiteSpaceInputRadio(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputRadioNormal() throws Exception {
        getVisibleTextWhiteSpaceInputRadio("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputRadioNowrap() throws Exception {
        getVisibleTextWhiteSpaceInputRadio("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputRadioPre() throws Exception {
        getVisibleTextWhiteSpaceInputRadio("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputRadioPreWrap() throws Exception {
        getVisibleTextWhiteSpaceInputRadio("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextWhiteSpaceInputRadioPreLine() throws Exception {
        getVisibleTextWhiteSpaceInputRadio("pre-line");
    }

    private void getVisibleTextWhiteSpaceInputRadio(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='radio' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \r\nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getVisibleTextWhiteSpaceOrderedList() throws Exception {
        getVisibleTextWhiteSpaceOrderedList(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getVisibleTextWhiteSpaceOrderedListNormal() throws Exception {
        getVisibleTextWhiteSpaceOrderedList("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getVisibleTextWhiteSpaceOrderedListNowrap() throws Exception {
        getVisibleTextWhiteSpaceOrderedList("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("    first item\n      A B  C     D \nEF\nG \n H  \n I  \n"
                + "    third item\n4. item\n    some text \n    \nlast item\n  ")
    public void getVisibleTextWhiteSpaceOrderedListPre() throws Exception {
        getVisibleTextWhiteSpaceOrderedList("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("    first item\n      A B  C     D \nEF\nG \n H  \n I  \n"
                + "    third item\n4. item\n    some text \n    \nlast item\n  ")
    public void getVisibleTextWhiteSpaceOrderedListPreWrap() throws Exception {
        getVisibleTextWhiteSpaceOrderedList("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D \nEF\nG \n H\nI\nthird item\n4. item\nsome text\nlast item")
    @NotYetImplemented
    public void getVisibleTextWhiteSpaceOrderedListPreLine() throws Exception {
        getVisibleTextWhiteSpaceOrderedList("pre-line");
    }

    private void getVisibleTextWhiteSpaceOrderedList(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <ol id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "    <li>first item</li>\n"
            + "    <li>  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </li>\n"
            + "    <li>third item</li><li>4. item</li>\n"
            + "    some text \n"
            + "    <li>last item</li>\n"
            + "  </ol>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getVisibleTextWhiteSpaceUnorderedList() throws Exception {
        getVisibleTextWhiteSpaceUnorderedList(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getVisibleTextWhiteSpaceUnorderedListNormal() throws Exception {
        getVisibleTextWhiteSpaceUnorderedList("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getVisibleTextWhiteSpaceUnorderedListNowrap() throws Exception {
        getVisibleTextWhiteSpaceUnorderedList("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("    first item\n      A B  C     D \nEF\nG \n H  \n I  \n"
                + "    third item\n4. item\n    some text \n    \nlast item\n  ")
    public void getVisibleTextWhiteSpaceUnorderedListPre() throws Exception {
        getVisibleTextWhiteSpaceUnorderedList("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("    first item\n      A B  C     D \nEF\nG \n H  \n I  \n"
                + "    third item\n4. item\n    some text \n    \nlast item\n  ")
    public void getVisibleTextWhiteSpaceUnorderedListPreWrap() throws Exception {
        getVisibleTextWhiteSpaceUnorderedList("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D \nEF\nG \n H\nI\nthird item\n4. item\nsome text\nlast item")
    @NotYetImplemented
    public void getVisibleTextWhiteSpaceUnorderedListPreLine() throws Exception {
        getVisibleTextWhiteSpaceUnorderedList("pre-line");
    }

    private void getVisibleTextWhiteSpaceUnorderedList(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <ul id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "    <li>first item</li>\n"
            + "    <li>  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </li>\n"
            + "    <li>third item</li><li>4. item</li>\n"
            + "    some text \n"
            + "    <li>last item</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getVisibleTextLabel() throws Exception {
        getVisibleTextFormated("<label id='tester'>The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getVisibleTextLabelNormal() throws Exception {
        getVisibleTextFormated("<label id='tester' style='white-space: normal'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getVisibleTextLabelNowrap() throws Exception {
        getVisibleTextFormated("<label id='tester' style='white-space: nowrap'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getVisibleTextLabelPre() throws Exception {
        getVisibleTextFormated("<label id='tester' style='white-space: pre'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getVisibleTextLabelPreWrap() throws Exception {
        getVisibleTextFormated("<label id='tester' style='white-space: pre-wrap'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getVisibleTextLabelPreLine() throws Exception {
        getVisibleTextFormated("<label id='tester' style='white-space: pre-line'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getVisibleTextParagraphNbsp() throws Exception {
        getVisibleTextFormated("<p id='tester'>A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getVisibleTextParagraphNbspNormal() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: normal'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getVisibleTextParagraphNbspNowrap() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: nowrap'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getVisibleTextParagraphNbspPre() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getVisibleTextParagraphNbspPreWrap() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre-wrap'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getVisibleTextParagraphNbspPreLine() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre-line'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n  NBSPs  ")
    public void getVisibleTextParagraphMultilineNbsp() throws Exception {
        getVisibleTextFormated("<p id='tester'>A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n  NBSPs  ")
    public void getVisibleTextParagraphMultilineNbspNormal() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: normal'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n  NBSPs  ")
    public void getVisibleTextParagraphMultilineNbspNowrap() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: nowrap'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n  NBSPs  ")
    public void getVisibleTextParagraphMultilineNbspPre() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n  NBSPs  ")
    public void getVisibleTextParagraphMultilineNbspPreWrap() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre-wrap'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n  NBSPs  ")
    public void getVisibleTextParagraphMultilineNbspPreLine() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre-line'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getVisibleText() for issue #128
     * (https://github.com/HtmlUnit/htmlunit/issues/128).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("I have out of 2 stamps")
    public void getVisibleTextInputInsideP() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + " I have <input type='number' value='2'/> out of 2 stamps</p>");
    }

    private void getVisibleTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }
}
