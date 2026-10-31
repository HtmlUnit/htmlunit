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
package org.htmlunit.html.serializer;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.serializer.HtmlSerializerVisibleText.HtmlSerializerTextBuilder;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.xml.XmlPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlSerializerVisibleText}.
 * This contains the tests for plain controls.
 *
 * @author Ronald Brill
 * @author cd alexndr
 */
public class HtmlSerializerVisibleText2Test extends WebDriverTestCase {

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "",
            EDGE = "",
            FF = "\n    baz\n  ",
            FF_ESR = "\n    baz\n  ")
    @HtmlUnitNYI(CHROME = "baz",
            EDGE = "baz",
            FF = "baz",
            FF_ESR = "baz")
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
            final XmlPage page = (XmlPage) getEnclosedPage();
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <br id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <script id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "var x;  \n \t   \r\n var y;</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <style id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </style>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <noframes id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </noframes>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <div id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <pre id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </pre>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "       A B C D EF G H\nI\n      Second\n    ",
            FF = "A B C D EF G H\nI\nSecond",
            FF_ESR = "A B C D EF G H\nI\nSecond")
    @HtmlUnitNYI(CHROME = "A B C D EF G H\nI\nSecond",
            EDGE = "A B C D EF G H\nI\nSecond")
    public void getVisibleTextWhiteSpaceSelect() throws Exception {
        getVisibleTextWhiteSpaceSelect(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI\nSecond")
    public void getVisibleTextWhiteSpaceSelectNormal() throws Exception {
        getVisibleTextWhiteSpaceSelect("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI\nSecond")
    public void getVisibleTextWhiteSpaceSelectNowrap() throws Exception {
        getVisibleTextWhiteSpaceSelect("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "       A B C D EF G H\nI\n      Second\n    ",
            FF = "A B C D EF G H\nI\nSecond",
            FF_ESR = "A B C D EF G H\nI\nSecond")
    @HtmlUnitNYI(CHROME = "  A B  C     D \nEF\nG \n H  \n I  \nSecond",
            EDGE = "  A B  C     D \nEF\nG \n H  \n I  \nSecond",
            FF = "  A B  C     D \nEF\nG \n H  \n I  \nSecond",
            FF_ESR = "  A B  C     D \nEF\nG \n H  \n I  \nSecond")
    public void getVisibleTextWhiteSpaceSelectPre() throws Exception {
        getVisibleTextWhiteSpaceSelect("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "       A B C D EF G H\nI\n      Second\n    ",
            FF = "A B C D EF G H\nI\nSecond",
            FF_ESR = "A B C D EF G H\nI\nSecond")
    public void getVisibleTextWhiteSpaceSelectPreWrap() throws Exception {
        getVisibleTextWhiteSpaceSelect("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI\nSecond")
    @HtmlUnitNYI(CHROME = "A B C D \nEF\nG \n H\nI\nSecond",
            EDGE = "A B C D \nEF\nG \n H\nI\nSecond",
            FF = "A B C D \nEF\nG \n H\nI\nSecond",
            FF_ESR = "A B C D \nEF\nG \n H\nI\nSecond")
    public void getVisibleTextWhiteSpaceSelectPreLine() throws Exception {
        getVisibleTextWhiteSpaceSelect("pre-line");
    }

    private void getVisibleTextWhiteSpaceSelect(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextInputSubmitNoValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextInputResetNoValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextInputResetBlankValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' value='  \t'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextInputSubmitBlankValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester' value='  \t'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
    @HtmlUnitNYI(CHROME = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item",
            EDGE = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item",
            FF = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item",
            FF_ESR = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item")
    public void getVisibleTextWhiteSpaceOrderedListPreLine() throws Exception {
        getVisibleTextWhiteSpaceOrderedList("pre-line");
    }

    private void getVisibleTextWhiteSpaceOrderedList(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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
    @HtmlUnitNYI(CHROME = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item",
            EDGE = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item",
            FF = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item",
            FF_ESR = "first item\n A B C D \nEF\nG \n H\nI\nthird item\n4. item\n some text \n\nlast item")
    public void getVisibleTextWhiteSpaceUnorderedListPreLine() throws Exception {
        getVisibleTextWhiteSpaceUnorderedList("pre-line");
    }

    private void getVisibleTextWhiteSpaceUnorderedList(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
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
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sum")
    public void getVisibleTextDetails() throws Exception {
        getVisibleTextFormated("<details id='tester'>"
                + "<summary>Sum</summary>"
                + "<p>detail</p>"
                + "</details>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sum\nSum2")
    public void getVisibleTextDetailsTwoSums() throws Exception {
        getVisibleTextFormated("<details id='tester'>"
                + "<summary>Sum</summary>"
                + "<summary>Sum2</summary>"
                + "<p>detail</p>"
                + "</details>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sum\ndetail")
    public void getVisibleTextDetailsOpen() throws Exception {
        getVisibleTextFormated("<details id='tester' open=true>"
                + "<summary>Sum</summary>"
                + "<p>detail</p>"
                + "</details>");
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("beforeafter")
    public void getVisibleTextCdata() throws Exception {
        getVisibleTextFormated("<div id='tester'>"
                + "before<![CDATA[inside]]>after"
                + "</div>");
    }

    private void getVisibleTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleNumberInputValidNumber() throws Exception {
        getVisibleTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "12");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleNumberInputInvalidNumber() throws Exception {
        getVisibleTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "ab");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("demo")
    public void getVisibleEm() throws Exception {
        getVisibleTextFormated("<em id='tester'>demo</em>");
    }

    private void getVisibleTextFormatedAfterTyping(final String htmlTesterSnipped,
                        final String... typed) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("inpt"));
        for (final String string : typed) {
            input.sendKeys(string);
        }

        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title\nSubtitle")
    public void getVisibleHgroup() throws Exception {
        getVisibleTextFormated("<hgroup id='tester'>"
                + "<h1>Title</h1>"
                + "<p>Subtitle</p>"
                + "</hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title")
    public void getVisibleHgroupHeadingOnly() throws Exception {
        getVisibleTextFormated("<hgroup id='tester'>"
                + "<h1>Title</h1>"
                + "</hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title\nSubtitle\nTagline")
    public void getVisibleHgroupMultipleChildren() throws Exception {
        getVisibleTextFormated("<hgroup id='tester'>"
                + "<h1>Title</h1>"
                + "<p>Subtitle</p>"
                + "<p>Tagline</p>"
                + "</hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleHgroupEmpty() throws Exception {
        getVisibleTextFormated("<hgroup id='tester'></hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Outer\nTitle\nSubtitle")
    public void getVisibleHgroupNested() throws Exception {
        getVisibleTextFormated("<div id='tester'>"
                + "<p>Outer</p>"
                + "<hgroup>"
                + "<h2>Title</h2>"
                + "<p>Subtitle</p>"
                + "</hgroup>"
                + "</div>");
    }

    // ---------- parity with the innerText tests -----------------------------------

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "       A B C D EF G H\nI\nJ K L M\n       Second\nThird Fourth\n      Last\n    ",
            FF = "A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast",
            FF_ESR = "A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast")
    public void getVisibleTextWhiteSpaceOptgroupSelect() throws Exception {
        getVisibleTextWhiteSpaceSelectOptgroup(null);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast")
    public void getVisibleTextWhiteSpaceSelectOptgroupNormal() throws Exception {
        getVisibleTextWhiteSpaceSelectOptgroup("normal");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast")
    public void getVisibleTextWhiteSpaceSelectOptgroupNowrap() throws Exception {
        getVisibleTextWhiteSpaceSelectOptgroup("nowrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "       A B C D EF G H\nI\nJ K L M\n       Second\nThird Fourth\n      Last\n    ",
            FF = "A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast",
            FF_ESR = "A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast")
    public void getVisibleTextWhiteSpaceSelectOptgroupPre() throws Exception {
        getVisibleTextWhiteSpaceSelectOptgroup("pre");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "       A B C D EF G H\nI\nJ K L M\n       Second\nThird Fourth\n      Last\n    ",
            FF = "A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast",
            FF_ESR = "A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast")
    public void getVisibleTextWhiteSpaceSelectOptgroupPreWrap() throws Exception {
        getVisibleTextWhiteSpaceSelectOptgroup("pre-wrap");
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI\nJ K L M\nSecond\nThird Fourth\nLast")
    public void getVisibleTextWhiteSpaceSelectOptgroupPreLine() throws Exception {
        getVisibleTextWhiteSpaceSelectOptgroup("pre-line");
    }

    /**
     * A script inside of the element is not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text")
    public void getVisibleTextWithScript() throws Exception {
        getVisibleTextFormated("<div id='tester'>text<script>var x = 'invisible';</script></div>");
    }

    /**
     * A title inside of the body.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xz")
    public void getVisibleTextWithTitle() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<title>y</title>z</div>");
    }

    /**
     * The title is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextTitle() throws Exception {
        getVisibleTextFormated("<div>x<title id='tester'>y</title>z</div>");
    }

    /**
     * The head with a title is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextTitleInHead() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head id='tester'><title>y</title></head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }

    // ---------- display and visibility --------------------------------------------

    /**
     * A child with display none is not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextDisplayNoneChild() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span style='display: none'>b</span>c</div>");
    }

    /**
     * The element itself has display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextDisplayNoneSelf() throws Exception {
        getVisibleTextFormated("<div id='tester' style='display: none'>abc</div>");
    }

    /**
     * A nested element inside a display none block is not part of the text, even if it is display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextDisplayNoneNested() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<div style='display: none'><span style='display: inline'>b</span></div>"
                + "c</div>");
    }

    /**
     * A child with visibility hidden is not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextVisibilityHiddenChild() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span style='visibility: hidden'>b</span>c</div>");
    }

    /**
     * The element itself has visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextVisibilityHiddenSelf() throws Exception {
        getVisibleTextFormated("<div id='tester' style='visibility: hidden'>abc</div>");
    }

    /**
     * A visible child inside of a hidden parent is part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("acd")
    public void getVisibleTextVisibilityHiddenChildVisible() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span style='visibility: hidden'>b<span style='visibility: visible'>c</span>"
                + "</span>d</div>");
    }

    /**
     * A child with visibility collapse.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextVisibilityCollapse() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span style='visibility: collapse'>b</span>c</div>");
    }

    /**
     * A child with the hidden attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextHiddenAttribute() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span hidden>b</span>c</div>");
    }

    /**
     * The hidden attribute is overridden by an explicit display.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getVisibleTextHiddenAttributeOverridden() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span hidden style='display: inline'>b</span>c</div>");
    }

    /**
     * A child with opacity zero.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextOpacityZero() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span style='opacity: 0'>b</span>c</div>");
    }

    /**
     * An inline-block between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getVisibleTextDisplayInlineBlock() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<div style='display: inline-block'>b</div>c</div>");
    }

    /**
     * Block elements with display inline do not break the line.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getVisibleTextDisplayInlineOnBlocks() throws Exception {
        getVisibleTextFormated("<div id='tester'><div style='display: inline'>a</div><div style='display: inline'>b</div>"
                + "</div>");
    }

    /**
     * A span with display block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextDisplayBlockOnSpan() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span style='display: block'>b</span>c</div>");
    }

    /**
     * The children of a flex container.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextDisplayFlexSpans() throws Exception {
        getVisibleTextFormated("<div id='tester' style='display: flex'><span>a</span><span>b</span></div>");
    }

    /**
     * Block children of a flex container.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextDisplayFlexDivs() throws Exception {
        getVisibleTextFormated("<div id='tester' style='display: flex'><div>a</div><div>b</div></div>");
    }

    /**
     * The children of a grid container.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextDisplayGrid() throws Exception {
        getVisibleTextFormated("<div id='tester' style='display: grid'><span>a</span><span>b</span></div>");
    }

    /**
     * An element with display contents.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextDisplayContents() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<div style='display: contents'>b</div>c</div>");
    }

    /**
     * Divs that are displayed as table.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextDisplayTableCellDivs() throws Exception {
        getVisibleTextFormated("<div id='tester'><div style='display: table'><div style='display: table-row'>"
                + "<div style='display: table-cell'>a</div><div style='display: table-cell'>b</div></div></div>"
                + "</div>");
    }

    /**
     * Divs that are displayed as list items.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextDisplayListItemOnDivs() throws Exception {
        getVisibleTextFormated("<div id='tester'><div style='display: list-item'>a</div><div style='display: list-item'>"
                + "b</div></div>");
    }

    /**
     * A child that is positioned outside of the viewport.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void getVisibleTextPositionAbsoluteOffscreen() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<span style='position: absolute; left: -9999px'>b</span>c</div>");
    }

    /**
     * The content is clipped by a container without height.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleTextOverflowHiddenNoHeight() throws Exception {
        getVisibleTextFormated("<div id='tester' style='height: 0; overflow: hidden'>abc</div>");
    }

    /**
     * Floating blocks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextFloatBlocks() throws Exception {
        getVisibleTextFormated("<div id='tester'><div style='float: left'>a</div><div style='float: left'>b</div></div>");
    }

    /**
     * A child with content-visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void getVisibleTextContentVisibilityHidden() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<div style='content-visibility: hidden'>b</div>c</div>");
    }

    // ---------- block structure ---------------------------------------------------

    /**
     * Nested block elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void getVisibleTextNestedDivs() throws Exception {
        getVisibleTextFormated("<div id='tester'><div>a<div>b</div>c</div>d</div>");
    }

    /**
     * An empty block between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextEmptyDivBetweenText() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<div></div>b</div>");
    }

    /**
     * A block inside of an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextDivInsideSpan() throws Exception {
        getVisibleTextFormated("<span id='tester'>a<div>b</div>c</span>");
    }

    /**
     * A br after a block element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\nb")
    public void getVisibleTextBrAfterBlock() throws Exception {
        getVisibleTextFormated("<div id='tester'><div>a</div><br>b</div>");
    }

    /**
     * Consecutive br elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\n\nb")
    public void getVisibleTextBrConsecutive() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<br><br><br>b</div>");
    }

    /**
     * A br at the start and at the end.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void getVisibleTextBrLeadingTrailing() throws Exception {
        getVisibleTextFormated("<div id='tester'><br>a<br></div>");
    }

    /**
     * Headings are block elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void getVisibleTextHeadings() throws Exception {
        getVisibleTextFormated("<div id='tester'><h1>a</h1><h2>b</h2>c<h3>d</h3></div>");
    }

    /**
     * A blockquote between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextBlockquote() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<blockquote>b</blockquote>c</div>");
    }

    /**
     * The sectioning elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd\ne\nf\ng")
    public void getVisibleTextSectioningElements() throws Exception {
        getVisibleTextFormated("<div id='tester'><section>a</section><article>b</article><nav>c</nav><aside>d</aside><header>"
                + "e</header><footer>f</footer><main>g</main></div>");
    }

    /**
     * A figure with a caption.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("caption")
    public void getVisibleTextFigureFigcaption() throws Exception {
        getVisibleTextFormated("<div id='tester'><figure><img alt='alt'><figcaption>caption</figcaption></figure></div>");
    }

    /**
     * A horizontal rule between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextHrBetweenText() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<hr>b</div>");
    }

    /**
     * A fieldset with a legend.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("legend\na")
    public void getVisibleTextFieldsetLegend() throws Exception {
        getVisibleTextFormated("<fieldset id='tester'><legend>legend</legend>a</fieldset>");
    }

    /**
     * An address element between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextAddressElement() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<address>b</address>c</div>");
    }

    /**
     * A closed dialog is not rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextDialogClosed() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<dialog>b</dialog>c</div>");
    }

    /**
     * An open dialog is rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextDialogOpen() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<dialog open>b</dialog>c</div>");
    }

    // ---------- text and white space ----------------------------------------------

    /**
     * Leading and trailing spaces are removed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void getVisibleTextLeadingTrailingSpaces() throws Exception {
        getVisibleTextFormated("<div id='tester'>   a   </div>");
    }

    /**
     * Spaces are collapsed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextCollapsedSpaces() throws Exception {
        getVisibleTextFormated("<div id='tester'>a      b</div>");
    }

    /**
     * Spaces across inline element boundaries.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c")
    public void getVisibleTextSpacesAcrossInlineElements() throws Exception {
        getVisibleTextFormated("<div id='tester'>a <b> b </b> c</div>");
    }

    /**
     * A space between two inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextSpaceBetweenInlineElements() throws Exception {
        getVisibleTextFormated("<div id='tester'><span>a</span> <span>b</span></div>");
    }

    /**
     * An empty inline element between spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextEmptyInlineBetweenSpaces() throws Exception {
        getVisibleTextFormated("<div id='tester'>a <b></b> b</div>");
    }

    /**
     * A newline in the text is a space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextNewlineInText() throws Exception {
        getVisibleTextFormated("<div id='tester'>a\nb</div>");
    }

    /**
     * A tab in the text is a space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextTabInText() throws Exception {
        getVisibleTextFormated("<div id='tester'>a\tb</div>");
    }

    /**
     * Non-breaking spaces are not collapsed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   b")
    public void getVisibleTextNbspRun() throws Exception {
        getVisibleTextFormated("<div id='tester'>a&nbsp;&nbsp;&nbsp;b</div>");
    }

    /**
     * Leading and trailing non-breaking spaces are kept.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(" a ")
    public void getVisibleTextNbspLeadingTrailing() throws Exception {
        getVisibleTextFormated("<div id='tester'>&nbsp;a&nbsp;</div>");
    }

    /**
     * A zero width space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getVisibleTextZeroWidthSpace() throws Exception {
        getVisibleTextFormated("<div id='tester'>a&#8203;b</div>");
    }

    /**
     * Other unicode spaces are not collapsed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\u2003\u2003b")
    public void getVisibleTextEmSpaces() throws Exception {
        getVisibleTextFormated("<div id='tester'>a&#8195;&#8195;b</div>");
    }

    /**
     * A soft hyphen.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\u00ADb")
    public void getVisibleTextSoftHyphen() throws Exception {
        getVisibleTextFormated("<div id='tester'>a&shy;b</div>");
    }

    /**
     * Escaped markup characters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b> & \"")
    public void getVisibleTextEntityEscapes() throws Exception {
        getVisibleTextFormated("<div id='tester'>&lt;b&gt; &amp; &quot;</div>");
    }

    /**
     * The text-transform uppercase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC SS")
    @HtmlUnitNYI(CHROME = "abc ß",
            EDGE = "abc ß",
            FF = "abc ß",
            FF_ESR = "abc ß")
    public void getVisibleTextTextTransformUppercase() throws Exception {
        getVisibleTextFormated("<div id='tester' style='text-transform: uppercase'>abc ß</div>");
    }

    /**
     * The text-transform lowercase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    @HtmlUnitNYI(CHROME = "ABC",
            EDGE = "ABC",
            FF = "ABC",
            FF_ESR = "ABC")
    public void getVisibleTextTextTransformLowercase() throws Exception {
        getVisibleTextFormated("<div id='tester' style='text-transform: lowercase'>ABC</div>");
    }

    /**
     * The text-transform capitalize.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Abc Def-Ghi")
    @HtmlUnitNYI(CHROME = "abc def-ghi",
            EDGE = "abc def-ghi",
            FF = "abc def-ghi",
            FF_ESR = "abc def-ghi")
    public void getVisibleTextTextTransformCapitalize() throws Exception {
        getVisibleTextFormated("<div id='tester' style='text-transform: capitalize'>abc def-ghi</div>");
    }

    /**
     * The white-space break-spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b\n c")
    public void getVisibleTextWhiteSpaceBreakSpaces() throws Exception {
        getVisibleTextFormated("<div id='tester' style='white-space: break-spaces'>a   b\n  c</div>");
    }

    /**
     * The white-space-collapse preserve.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   b\n  c")
    public void getVisibleTextWhiteSpaceCollapsePreserve() throws Exception {
        getVisibleTextFormated("<div id='tester' style='white-space-collapse: preserve'>a   b\n  c</div>");
    }

    /**
     * The white-space-collapse preserve-breaks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b\n c")
    public void getVisibleTextWhiteSpaceCollapsePreserveBreaks() throws Exception {
        getVisibleTextFormated("<div id='tester' style='white-space-collapse: preserve-breaks'>a   b\n  c</div>");
    }

    /**
     * The white-space-collapse break-spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b\n c")
    public void getVisibleTextWhiteSpaceCollapseBreakSpaces() throws Exception {
        getVisibleTextFormated("<div id='tester' style='white-space-collapse: break-spaces'>a   b\n  c</div>");
    }

    /**
     * White-space pre on an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x a   b\n  c y")
    public void getVisibleTextWhiteSpacePreOnInline() throws Exception {
        getVisibleTextFormated("<div id='tester'>x <span style='white-space: pre'>a   b\n  c</span> y</div>");
    }

    /**
     * A child resets the white-space of its parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   bc d ef   g")
    public void getVisibleTextWhiteSpacePreChildNormal() throws Exception {
        getVisibleTextFormated("<div id='tester' style='white-space: pre'>a   b<span style='white-space: normal'>"
                + "c   d\n  e</span>f   g</div>");
    }

    /**
     * White-space pre-line on an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x a b\n c y")
    public void getVisibleTextWhiteSpacePreLineInline() throws Exception {
        getVisibleTextFormated("<div id='tester'>x <span style='white-space: pre-line'>a   b\n  c</span> y</div>");
    }

    /**
     * The first newline of a pre is removed by the parser.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getVisibleTextPreLeadingNewline() throws Exception {
        getVisibleTextFormated("<pre id='tester'>\nabc</pre>");
    }

    /**
     * Only the first newline of a pre is removed by the parser.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getVisibleTextPreLeadingTwoNewlines() throws Exception {
        getVisibleTextFormated("<pre id='tester'>\n\nabc</pre>");
    }

    /**
     * A pre with inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a  b\n  c")
    public void getVisibleTextPreWithInlineElements() throws Exception {
        getVisibleTextFormated("<pre id='tester'>a  <b>b</b>\n  c</pre>");
    }

    /**
     * A pre inside of a div.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb  c\nd")
    public void getVisibleTextPreInsideDiv() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<pre>b  c</pre>d</div>");
    }

    /**
     * Generated content is not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void getVisibleTextCssGeneratedContent() throws Exception {
        getVisibleTextFormated("<style>#tester::before { content: 'x'; } #tester::after { content: 'y'; }</style>"
                + "<div id='tester'>a</div>");
    }

    /**
     * Comments are not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getVisibleTextCommentBetweenText() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<!-- c -->b</div>");
    }

    /**
     * An unknown element is inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getVisibleTextUnknownElement() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<foo-bar>b</foo-bar>c</div>");
    }

    // ---------- tables ------------------------------------------------------------

    /**
     * A simple table.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b\nc d")
    public void getVisibleTextTableSimple() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td>a</td><td>b</td></tr><tr><td>c</td><td>d</td></tr></table>");
    }

    /**
     * A table with caption, head, body and foot.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cap\nh1 h2\na b\nf1 f2")
    public void getVisibleTextTableWithSections() throws Exception {
        getVisibleTextFormated("<table id='tester'><caption>cap</caption><thead><tr><th>h1</th><th>h2</th></tr></thead><tbody>"
                + "<tr><td>a</td><td>b</td></tr></tbody><tfoot><tr><td>f1</td><td>f2</td></tr></tfoot></table>");
    }

    /**
     * A tfoot defined before the tbody.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("f\na")
    public void getVisibleTextTableFootBeforeBody() throws Exception {
        getVisibleTextFormated("<table id='tester'><tfoot><tr><td>f</td></tr></tfoot><tbody><tr><td>a</td></tr></tbody>"
                + "</table>");
    }

    /**
     * A table with colspan and rowspan.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb c\nd")
    public void getVisibleTextTableColspanRowspan() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td colspan='2'>a</td></tr><tr><td rowspan='2'>b</td><td>c</td></tr>"
                + "<tr><td>d</td></tr></table>");
    }

    /**
     * A table with empty cells.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a c\ne")
    public void getVisibleTextTableEmptyCells() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td>a</td><td></td><td>c</td></tr><tr><td></td><td>e</td><td></td></tr>"
                + "</table>");
    }

    /**
     * A table inside of a table cell.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb c")
    public void getVisibleTextTableNested() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td>a</td><td><table><tr><td>b</td><td>c</td></tr></table></td></tr>"
                + "</table>");
    }

    /**
     * A table cell with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a c")
    public void getVisibleTextTableCellDisplayNone() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td>a</td><td style='display: none'>b</td><td>c</td></tr></table>");
    }

    /**
     * A table row with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void getVisibleTextTableRowDisplayNone() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td>a</td></tr><tr style='display: none'><td>b</td></tr><tr><td>c</td>"
                + "</tr></table>");
    }

    /**
     * A table cell with block content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextTableCellWithBlocks() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td><p>a</p><p>b</p></td><td>c</td></tr></table>");
    }

    /**
     * A table cell with line breaks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb c")
    public void getVisibleTextTableCellWithLineBreaks() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td>a<br>b</td><td>c</td></tr></table>");
    }

    /**
     * White space between the cells and rows.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b\nc")
    public void getVisibleTextTableWhitespaceBetweenCells() throws Exception {
        getVisibleTextFormated("<table id='tester'>\n  <tr>\n    <td>a</td>\n    <td>b</td>\n  </tr>\n  <tr>\n    <td>c</td>"
                + "\n  </tr>\n</table>");
    }

    /**
     * The row is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextTableRowAsTester() throws Exception {
        getVisibleTextFormated("<table><tr id='tester'><td>a</td><td>b</td></tr></table>");
    }

    /**
     * The cell is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextTableCellAsTester() throws Exception {
        getVisibleTextFormated("<table><tr><td id='tester'>  a  b  </td></tr></table>");
    }

    /**
     * Table cells with display block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextTableCellDisplayBlock() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td style='display: block'>a</td><td style='display: block'>b</td></tr>"
                + "</table>");
    }

    /**
     * A table between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na b\ny")
    public void getVisibleTextTableInsideDiv() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<table><tr><td>a</td><td>b</td></tr></table>y</div>");
    }

    // ---------- form controls -----------------------------------------------------

    /**
     * The text of a button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextButtonText() throws Exception {
        getVisibleTextFormated("<button id='tester'>  a   b  </button>");
    }

    /**
     * A button with inline children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa b cy")
    public void getVisibleTextButtonWithInlineChildren() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<button>a <b>b</b> c</button>y</div>");
    }

    /**
     * A button with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextButtonDisplayNone() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<button style='display: none'>a</button>y</div>");
    }

    /**
     * The input of type text between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypeText() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='text' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type password between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypePassword() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='password' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type button between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypeButton() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='button' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type image between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypeImage() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='image' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type file between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypeFile() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='file' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type range between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypeRange() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='range' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type color between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypeColor() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='color' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type date between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextInputTypeDate() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<input type='date' value='v' alt='alt'>y</div>");
    }

    /**
     * A textarea with new lines.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa\n b\n\ncy")
    public void getVisibleTextTextareaWithNewlines() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<textarea>a\n b\n\nc</textarea>y</div>");
    }

    /**
     * A textarea with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextTextareaDisplayNone() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<textarea style='display: none'>a</textarea>y</div>");
    }

    /**
     * The textarea is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  a   b  ")
    public void getVisibleTextTextareaAsTester() throws Exception {
        getVisibleTextFormated("<textarea id='tester'>  a   b  </textarea>");
    }

    /**
     * A select with the multiple attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextSelectMultiple() throws Exception {
        getVisibleTextFormated("<select id='tester' multiple><option>a</option><option selected>b</option><option>c</option>"
                + "</select>");
    }

    /**
     * A select displayed as list box.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getVisibleTextSelectSizeListbox() throws Exception {
        getVisibleTextFormated("<select id='tester' size='3'><option>a</option><option>b</option><option>c</option></select>");
    }

    /**
     * A select with a selected option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextSelectSelectedOption() throws Exception {
        getVisibleTextFormated("<select id='tester'><option>a</option><option selected>b</option></select>");
    }

    /**
     * A select with a disabled option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextSelectDisabledOption() throws Exception {
        getVisibleTextFormated("<select id='tester'><option>a</option><option disabled>b</option></select>");
    }

    /**
     * A select with an option with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextSelectOptionDisplayNone() throws Exception {
        getVisibleTextFormated("<select id='tester'><option>a</option><option style='display: none'>b</option></select>");
    }

    /**
     * A select without options.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleTextSelectWithoutOptions() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<select></select>y</div>");
    }

    /**
     * An option with a label attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("T")
    public void getVisibleTextOptionLabelAttribute() throws Exception {
        getVisibleTextFormated("<select id='tester'><option label='L'>T</option></select>");
    }

    /**
     * The option is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextOptionAsTester() throws Exception {
        getVisibleTextFormated("<select><option id='tester'>  a   b  </option></select>");
    }

    /**
     * A datalist is not rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\ny")
    public void getVisibleTextDatalistWithOptions() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<datalist><option value='v'>t</option></datalist>y</div>");
    }

    /**
     * A disabled fieldset.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("l\na")
    public void getVisibleTextFieldsetDisabled() throws Exception {
        getVisibleTextFormated("<fieldset id='tester' disabled><legend>l</legend><input value='v'>a</fieldset>");
    }

    /**
     * A label with an input.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getVisibleTextLabelWithInput() throws Exception {
        getVisibleTextFormated("<label id='tester'>a <input type='checkbox'> b</label>");
    }

    /**
     * Meter, progress and output elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xmpoy")
    public void getVisibleTextMeterProgressOutput() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<meter value='1'>m</meter><progress value='1'>p</progress><output>o</output>"
                + "y</div>");
    }

    // ---------- embedded content and special elements -----------------------------

    /**
     * An image with alt text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getVisibleTextImgWithAlt() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<img alt='alt'>b</div>");
    }

    /**
     * An image without alt text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getVisibleTextImgWithoutAlt() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<img>b</div>");
    }

    /**
     * An anchor with a link.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getVisibleTextAnchor() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<a href='#x'>b</a>c</div>");
    }

    /**
     * An iframe with srcdoc and fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("afallbackb")
    public void getVisibleTextIframeSrcdoc() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<iframe srcdoc='<b>x</b>'>fallback</iframe>b</div>");
    }

    /**
     * An object with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("afallbackb")
    public void getVisibleTextObjectFallback() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<object data='x.bin'>fallback</object>b</div>");
    }

    /**
     * A video with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("afallbackb")
    public void getVisibleTextVideoFallback() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<video>fallback</video>b</div>");
    }

    /**
     * An audio with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getVisibleTextAudioFallback() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<audio>fallback</audio>b</div>");
    }

    /**
     * A canvas with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("afallbackb")
    public void getVisibleTextCanvasFallback() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<canvas>fallback</canvas>b</div>");
    }

    /**
     * An svg with text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a\nt\nb",
            FF = "atb",
            FF_ESR = "atb")
    public void getVisibleTextSvgText() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<svg width='100' height='20'><text x='0' y='10'>t</text></svg>b</div>");
    }

    /**
     * An svg with title and desc.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("attddb")
    public void getVisibleTextSvgTitleAndDesc() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<svg width='10' height='10'><title>tt</title><desc>dd</desc></svg>b</div>");
    }

    /**
     * An svg with a foreignObject.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nf\nb")
    public void getVisibleTextSvgForeignObject() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<svg width='100' height='20'><foreignObject width='100' height='20'><div>"
                + "f</div></foreignObject></svg>b</div>");
    }

    /**
     * A MathML element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a\nx\n+\n1\nb",
            FF = "ax+1b",
            FF_ESR = "ax+1b")
    @HtmlUnitNYI(CHROME = "ax+1b",
            EDGE = "ax+1b")
    public void getVisibleTextMathMl() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<math><mi>x</mi><mo>+</mo><mn>1</mn></math>b</div>");
    }

    /**
     * The content of a template is not rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextTemplateElement() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<template>b</template>c</div>");
    }

    /**
     * A noscript element (scripting is enabled).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getVisibleTextNoscriptElement() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<noscript>b</noscript>c</div>");
    }

    /**
     * A shadow root with a slot.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("beforelightafter")
    public void getVisibleTextShadowRootWithSlot() throws Exception {
        getVisibleTextFormated("<div id='tester'><b>light</b></div><script>"
                + "document.getElementById('tester').attachShadow({mode: 'open'}).innerHTML = 'before<slot>"
                + "</slot>after';</script>");
    }

    /**
     * A shadow root without a slot hides the light DOM.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("shadow")
    public void getVisibleTextShadowRootWithoutSlot() throws Exception {
        getVisibleTextFormated("<div id='tester'><b>light</b></div><script>"
                + "document.getElementById('tester').attachShadow({mode: 'open'}).innerHTML = '<i>shadow</i>"
                + "';</script>");
    }

    /**
     * A closed details without a summary.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void getVisibleTextDetailsWithoutSummary() throws Exception {
        getVisibleTextFormated("<details id='tester'>a</details>");
    }

    /**
     * A closed details with a summary only.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("s")
    public void getVisibleTextDetailsSummaryOnly() throws Exception {
        getVisibleTextFormated("<details id='tester'><summary>s</summary></details>");
    }

    /**
     * The summary is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("s")
    public void getVisibleTextSummaryAsTester() throws Exception {
        getVisibleTextFormated("<details><summary id='tester'>s</summary>a</details>");
    }

    /**
     * A ruby with rp elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getVisibleTextRubyWithRp() throws Exception {
        getVisibleTextFormated("<div id='tester'><ruby>a<rp>(</rp><rt>b</rt><rp>)</rp></ruby></div>");
    }

    private void getVisibleTextWhiteSpaceSelectOptgroup(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <select id='tester' "
                    + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "      <optgroup label='Group \t  1 \n  one'>\n"
            + "        <option>  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </option>\n"
            + "        <option>  J  K\tL \n M  </option>\n"
            + "      </optgroup>\n"
            + "      <optgroup label='Group 2'>\n"
            + "        <option>Second</option>\n"
            + "        <option>  Third \r\n Fourth  </option>\n"
            + "      </optgroup>\n"
            + "      <option>Last</option>\n"
            + "    </select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
            assertEquals(getExpectedAlerts()[0], page.getVisibleText());
        }
    }
}
