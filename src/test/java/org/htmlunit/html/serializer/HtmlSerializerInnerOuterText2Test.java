/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HtmlSerializerVisibleText}.
 * This contains the tests for plain controls.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerInnerOuterText2Test extends WebDriverTestCase {

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceBreak() throws Exception {
        getInnerTextWhiteSpaceBreak(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceBreakNormal() throws Exception {
        getInnerTextWhiteSpaceBreak("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceBreakNowrap() throws Exception {
        getInnerTextWhiteSpaceBreak("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceBreakPre() throws Exception {
        getInnerTextWhiteSpaceBreak("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceBreakPreWrap() throws Exception {
        getInnerTextWhiteSpaceBreak("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceBreakPreLine() throws Exception {
        getInnerTextWhiteSpaceBreak("pre-line");
    }

    private void getInnerTextWhiteSpaceBreak(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <br id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputHidden() throws Exception {
        getInnerTextWhiteSpaceInputHidden(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputHiddenNormal() throws Exception {
        getInnerTextWhiteSpaceInputHidden("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputHiddenNowrap() throws Exception {
        getInnerTextWhiteSpaceInputHidden("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputHiddenPre() throws Exception {
        getInnerTextWhiteSpaceInputHidden("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputHiddenPreWrap() throws Exception {
        getInnerTextWhiteSpaceInputHidden("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputHiddenPreLine() throws Exception {
        getInnerTextWhiteSpaceInputHidden("pre-line");
    }

    private void getInnerTextWhiteSpaceInputHidden(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("var x;  \n \t   \n var y;")
    public void getInnerTextWhiteSpaceScript() throws Exception {
        getInnerTextWhiteSpaceScript(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("var x;  \n \t   \n var y;")
    public void getInnerTextWhiteSpaceScriptNormal() throws Exception {
        getInnerTextWhiteSpaceScript("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("var x;  \n \t   \n var y;")
    public void getInnerTextWhiteSpaceScriptNowrap() throws Exception {
        getInnerTextWhiteSpaceScript("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("var x;  \n \t   \n var y;")
    public void getInnerTextWhiteSpaceScriptPre() throws Exception {
        getInnerTextWhiteSpaceScript("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("var x;  \n \t   \n var y;")
    public void getInnerTextWhiteSpaceScriptPreWrap() throws Exception {
        getInnerTextWhiteSpaceScript("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("var x;  \n \t   \n var y;")
    public void getInnerTextWhiteSpaceScriptPreLine() throws Exception {
        getInnerTextWhiteSpaceScript("pre-line");
    }

    private void getInnerTextWhiteSpaceScript(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <script id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "var x;  \n \t   \r\n var y;</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceStyle() throws Exception {
        getInnerTextWhiteSpaceStyle(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceStyleNormal() throws Exception {
        getInnerTextWhiteSpaceStyle("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceStyleNowrap() throws Exception {
        getInnerTextWhiteSpaceStyle("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceStylePre() throws Exception {
        getInnerTextWhiteSpaceStyle("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceStylePreWrap() throws Exception {
        getInnerTextWhiteSpaceStyle("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceStylePreLine() throws Exception {
        getInnerTextWhiteSpaceStyle("pre-line");
    }

    private void getInnerTextWhiteSpaceStyle(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <style id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </style>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceNoframes() throws Exception {
        getInnerTextWhiteSpaceNoframes(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceNoframesNormal() throws Exception {
        getInnerTextWhiteSpaceNoframes("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceNoframesNowrap() throws Exception {
        getInnerTextWhiteSpaceNoframes("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceNoframesPre() throws Exception {
        getInnerTextWhiteSpaceNoframes("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceNoframesPreWrap() throws Exception {
        getInnerTextWhiteSpaceNoframes("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceNoframesPreLine() throws Exception {
        getInnerTextWhiteSpaceNoframes("pre-line");
    }

    private void getInnerTextWhiteSpaceNoframes(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <noframes id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </noframes>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getInnerTextWhiteSpaceDiv() throws Exception {
        getInnerTextWhiteSpaceDiv(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getInnerTextWhiteSpaceDivNormal() throws Exception {
        getInnerTextWhiteSpaceDiv("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getInnerTextWhiteSpaceDivNowrap() throws Exception {
        getInnerTextWhiteSpaceDiv("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  \n I  ")
    public void getInnerTextWhiteSpaceDivPre() throws Exception {
        getInnerTextWhiteSpaceDiv("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  \n I  ")
    public void getInnerTextWhiteSpaceDivPreWrap() throws Exception {
        getInnerTextWhiteSpaceDiv("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D\nEF\nG\nH\nI")
    @HtmlUnitNYI(CHROME = "A B C D\nEF\nG\nH\n I",
            EDGE = "A B C D\nEF\nG\nH\n I",
            FF = "A B C D\nEF\nG\nH\n I",
            FF_ESR = "A B C D\nEF\nG\nH\n I")
    public void getInnerTextWhiteSpaceDivPreLine() throws Exception {
        getInnerTextWhiteSpaceDiv("pre-line");
    }

    private void getInnerTextWhiteSpaceDiv(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <div id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  \n I  ")
    @HtmlUnitNYI(CHROME = "A B C D EF G H\nI",
            EDGE = "A B C D EF G H\nI",
            FF = "A B C D EF G H\nI",
            FF_ESR = "A B C D EF G H\nI")
    public void getInnerTextWhiteSpacePre() throws Exception {
        getInnerTextWhiteSpacePre(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getInnerTextWhiteSpacePreNormal() throws Exception {
        getInnerTextWhiteSpacePre("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H\nI")
    public void getInnerTextWhiteSpacePreNowrap() throws Exception {
        getInnerTextWhiteSpacePre("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  \n I  ")
    public void getInnerTextWhiteSpacePrePre() throws Exception {
        getInnerTextWhiteSpacePre("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  \n I  ")
    public void getInnerTextWhiteSpacePrePreWrap() throws Exception {
        getInnerTextWhiteSpacePre("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D\nEF\nG\nH\nI")
    @HtmlUnitNYI(CHROME = "A B C D\nEF\nG\nH\n I",
            EDGE = "A B C D\nEF\nG\nH\n I",
            FF = "A B C D\nEF\nG\nH\n I",
            FF_ESR = "A B C D\nEF\nG\nH\n I")
    public void getInnerTextWhiteSpacePrePreLine() throws Exception {
        getInnerTextWhiteSpacePre("pre-line");
    }

    private void getInnerTextWhiteSpacePre(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <pre id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </pre>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceTextArea() throws Exception {
        getInnerTextWhiteSpaceTextArea(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceTextAreaNormal() throws Exception {
        getInnerTextWhiteSpaceTextArea("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceTextAreaNowrap() throws Exception {
        getInnerTextWhiteSpaceTextArea("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceTextAreaPre() throws Exception {
        getInnerTextWhiteSpaceTextArea("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceTextAreaPreWrap() throws Exception {
        getInnerTextWhiteSpaceTextArea("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceTextAreaPreLine() throws Exception {
        getInnerTextWhiteSpaceTextArea("pre-line");
    }

    private void getInnerTextWhiteSpaceTextArea(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceTitle() throws Exception {
        getInnerTextWhiteSpaceTitle(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceTitleNormal() throws Exception {
        getInnerTextWhiteSpaceTitle("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceTitleNowrap() throws Exception {
        getInnerTextWhiteSpaceTitle("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceTitlePre() throws Exception {
        getInnerTextWhiteSpaceTitle("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceTitlePreWrap() throws Exception {
        getInnerTextWhiteSpaceTitle("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \nEF\nG \n H  <br> I  ")
    public void getInnerTextWhiteSpaceTitlePreLine() throws Exception {
        getInnerTextWhiteSpaceTitle("pre-line");
    }

    private void getInnerTextWhiteSpaceTitle(final String whiteSpace) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
            + "  A B  C\t \t  D \r\nEF\nG \n H  <br> I  </title>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            FF = "",
            FF_ESR = "")
    @HtmlUnitNYI(CHROME = "A B C D EF G H I Second",
            EDGE = "A B C D EF G H I Second",
            FF = "A B C D EF G H I Second",
            FF_ESR = "A B C D EF G H I Second")
    public void getInnerTextWhiteSpaceSelect() throws Exception {
        getInnerTextWhiteSpaceSelect(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            FF = "",
            FF_ESR = "")
    @HtmlUnitNYI(CHROME = "A B C D EF G H I Second",
            EDGE = "A B C D EF G H I Second",
            FF = "A B C D EF G H I Second",
            FF_ESR = "A B C D EF G H I Second")
    public void getInnerTextWhiteSpaceSelectNormal() throws Exception {
        getInnerTextWhiteSpaceSelect("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            FF = "",
            FF_ESR = "")
    @HtmlUnitNYI(CHROME = "A B C D EF G H I Second",
            EDGE = "A B C D EF G H I Second",
            FF = "A B C D EF G H I Second",
            FF_ESR = "A B C D EF G H I Second")
    public void getInnerTextWhiteSpaceSelectNowrap() throws Exception {
        getInnerTextWhiteSpaceSelect("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            FF = "",
            FF_ESR = "")
    @HtmlUnitNYI(CHROME = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ",
            EDGE = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ",
            FF = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ",
            FF_ESR = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ")
    public void getInnerTextWhiteSpaceSelectPre() throws Exception {
        getInnerTextWhiteSpaceSelect("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            FF = "",
            FF_ESR = "")
    @HtmlUnitNYI(CHROME = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ",
            EDGE = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ",
            FF = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ",
            FF_ESR = "        A B  C\t \t  D \nEF\nG \n H   I  \n      Second\n    ")
    public void getInnerTextWhiteSpaceSelectPreWrap() throws Exception {
        getInnerTextWhiteSpaceSelect("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nSecond",
            FF = "",
            FF_ESR = "")
    @HtmlUnitNYI(CHROME = "A B C D\nEF\nG\nH I\nSecond\n",
            EDGE = "A B C D\nEF\nG\nH I\nSecond\n",
            FF = "A B C D\nEF\nG\nH I\nSecond\n",
            FF_ESR = "A B C D\nEF\nG\nH I\nSecond\n")
    public void getInnerTextWhiteSpaceSelectPreLine() throws Exception {
        getInnerTextWhiteSpaceSelect("pre-line");
    }

    private void getInnerTextWhiteSpaceSelect(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputSubmit() throws Exception {
        getInnerTextWhiteSpaceInputSubmit(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputSubmitNormal() throws Exception {
        getInnerTextWhiteSpaceInputSubmit("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputSubmitNowrap() throws Exception {
        getInnerTextWhiteSpaceInputSubmit("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputSubmitPre() throws Exception {
        getInnerTextWhiteSpaceInputSubmit("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputSubmitPreWrap() throws Exception {
        getInnerTextWhiteSpaceInputSubmit("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputSubmitPreLine() throws Exception {
        getInnerTextWhiteSpaceInputSubmit("pre-line");
    }

    private void getInnerTextWhiteSpaceInputSubmit(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextInputSubmitNoValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextInputResetNoValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' >\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextInputResetBlankValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' value='  \t'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextInputSubmitBlankValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester' value='  \t'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputReset() throws Exception {
        getInnerTextWhiteSpaceInputReset(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputResetNormal() throws Exception {
        getInnerTextWhiteSpaceInputReset("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputResetNowrap() throws Exception {
        getInnerTextWhiteSpaceInputReset("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputResetPre() throws Exception {
        getInnerTextWhiteSpaceInputReset("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputResetPreWrap() throws Exception {
        getInnerTextWhiteSpaceInputReset("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputResetPreLine() throws Exception {
        getInnerTextWhiteSpaceInputReset("pre-line");
    }

    private void getInnerTextWhiteSpaceInputReset(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputCheckbox() throws Exception {
        getInnerTextWhiteSpaceInputCheckbox(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputCheckboxNormal() throws Exception {
        getInnerTextWhiteSpaceInputCheckbox("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputCheckboxNowrap() throws Exception {
        getInnerTextWhiteSpaceInputCheckbox("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputCheckboxPre() throws Exception {
        getInnerTextWhiteSpaceInputCheckbox("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputCheckboxPreWrap() throws Exception {
        getInnerTextWhiteSpaceInputCheckbox("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputCheckboxPreLine() throws Exception {
        getInnerTextWhiteSpaceInputCheckbox("pre-line");
    }

    private void getInnerTextWhiteSpaceInputCheckbox(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputRadio() throws Exception {
        getInnerTextWhiteSpaceInputRadio(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputRadioNormal() throws Exception {
        getInnerTextWhiteSpaceInputRadio("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputRadioNowrap() throws Exception {
        getInnerTextWhiteSpaceInputRadio("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputRadioPre() throws Exception {
        getInnerTextWhiteSpaceInputRadio("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputRadioPreWrap() throws Exception {
        getInnerTextWhiteSpaceInputRadio("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextWhiteSpaceInputRadioPreLine() throws Exception {
        getInnerTextWhiteSpaceInputRadio("pre-line");
    }

    private void getInnerTextWhiteSpaceInputRadio(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getInnerTextWhiteSpaceOrderedList() throws Exception {
        getInnerTextWhiteSpaceOrderedList(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getInnerTextWhiteSpaceOrderedListNormal() throws Exception {
        getInnerTextWhiteSpaceOrderedList("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getInnerTextWhiteSpaceOrderedListNowrap() throws Exception {
        getInnerTextWhiteSpaceOrderedList("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    @HtmlUnitNYI(CHROME = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            EDGE = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF_ESR = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    public void getInnerTextWhiteSpaceOrderedListPre() throws Exception {
        getInnerTextWhiteSpaceOrderedList("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    @HtmlUnitNYI(CHROME = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            EDGE = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF_ESR = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    public void getInnerTextWhiteSpaceOrderedListPreWrap() throws Exception {
        getInnerTextWhiteSpaceOrderedList("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n\nfirst item\n\n\nA B C D\nEF\nG\nH\nI"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n")
    @HtmlUnitNYI(CHROME = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n",
            EDGE = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n",
            FF = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n",
            FF_ESR = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n")
    public void getInnerTextWhiteSpaceOrderedListPreLine() throws Exception {
        getInnerTextWhiteSpaceOrderedList("pre-line");
    }

    private void getInnerTextWhiteSpaceOrderedList(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getInnerTextWhiteSpaceUnorderedList() throws Exception {
        getInnerTextWhiteSpaceUnorderedList(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getInnerTextWhiteSpaceUnorderedListNormal() throws Exception {
        getInnerTextWhiteSpaceUnorderedList("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\nA B C D EF G H\nI\nthird item\n4. item\nsome text\nlast item")
    public void getInnerTextWhiteSpaceUnorderedListNowrap() throws Exception {
        getInnerTextWhiteSpaceUnorderedList("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    @HtmlUnitNYI(CHROME = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            EDGE = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF_ESR = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    public void getInnerTextWhiteSpaceUnorderedListPre() throws Exception {
        getInnerTextWhiteSpaceUnorderedList("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    @HtmlUnitNYI(CHROME = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            EDGE = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ",
            FF_ESR = "    \nfirst item\n\n    \n  A B  C\t \t  D \nEF\nG \n H  \n I  \n"
                + "\n    \nthird item\n4. item\n\n    some text \n    \nlast item\n\n  ")
    public void getInnerTextWhiteSpaceUnorderedListPreWrap() throws Exception {
        getInnerTextWhiteSpaceUnorderedList("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n\nfirst item\n\n\nA B C D\nEF\nG\nH\nI\n"
                + "\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n")
    @HtmlUnitNYI(CHROME = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n",
            EDGE = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n",
            FF = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n",
            FF_ESR = "first item\n\n\n A B C D\nEF\nG\nH\n I"
                + "\n\n\nthird item\n4. item\n\nsome text\n\nlast item\n\n")
    public void getInnerTextWhiteSpaceUnorderedListPreLine() throws Exception {
        getInnerTextWhiteSpaceUnorderedList("pre-line");
    }

    private void getInnerTextWhiteSpaceUnorderedList(final String whiteSpace) throws Exception {
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
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getInnerTextLabel() throws Exception {
        getInnerTextFormated("<label id='tester'>The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getInnerTextLabelNormal() throws Exception {
        getInnerTextFormated("<label id='tester' style='white-space: normal'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getInnerTextLabelNowrap() throws Exception {
        getInnerTextFormated("<label id='tester' style='white-space: nowrap'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getInnerTextLabelPre() throws Exception {
        getInnerTextFormated("<label id='tester' style='white-space: pre'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getInnerTextLabelPreWrap() throws Exception {
        getInnerTextFormated("<label id='tester' style='white-space: pre-wrap'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getInnerTextLabelPreLine() throws Exception {
        getInnerTextFormated("<label id='tester' style='white-space: pre-line'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0 nbsp and spaces")
    public void getInnerTextParagraphNbsp() throws Exception {
        getInnerTextFormated("<p id='tester'>A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0 nbsp and spaces")
    public void getInnerTextParagraphNbspNormal() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: normal'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0 nbsp and spaces")
    public void getInnerTextParagraphNbspNowrap() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: nowrap'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0 nbsp and spaces")
    public void getInnerTextParagraphNbspPre() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: pre'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0 nbsp and spaces")
    public void getInnerTextParagraphNbspPreWrap() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: pre-wrap'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0 nbsp and spaces")
    public void getInnerTextParagraphNbspPreLine() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: pre-line'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0\n\u00A0 NBSPs\u00A0\u00A0")
    public void getInnerTextParagraphMultilineNbsp() throws Exception {
        getInnerTextFormated("<p id='tester'>A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0\n\u00A0 NBSPs\u00A0\u00A0")
    public void getInnerTextParagraphMultilineNbspNormal() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: normal'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0\n\u00A0 NBSPs\u00A0\u00A0")
    public void getInnerTextParagraphMultilineNbspNowrap() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: nowrap'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0\n\u00A0 NBSPs\u00A0\u00A0")
    public void getInnerTextParagraphMultilineNbspPre() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: pre'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0\n\u00A0 NBSPs\u00A0\u00A0")
    public void getInnerTextParagraphMultilineNbspPreWrap() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: pre-wrap'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A \u00A0\n\u00A0 NBSPs\u00A0\u00A0")
    public void getInnerTextParagraphMultilineNbspPreLine() throws Exception {
        getInnerTextFormated("<p id='tester' style='white-space: pre-line'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getInnerText() for issue #128
     * (https://github.com/HtmlUnit/htmlunit/issues/128).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("I have  out of 2 stamps")
    @HtmlUnitNYI(CHROME = "I have out of 2 stamps",
            EDGE = "I have out of 2 stamps",
            FF = "I have out of 2 stamps",
            FF_ESR = "I have out of 2 stamps")
    public void getInnerTextInputInsideP() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + " I have <input type='number' value='2'/> out of 2 stamps</p>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sum")
    public void getInnerTextDetails() throws Exception {
        getInnerTextFormated("<details id='tester'>"
                + "<summary>Sum</summary>"
                + "<p>detail</p>"
                + "</details>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sum")
    @HtmlUnitNYI(CHROME = "SumSum2",
            EDGE = "SumSum2",
            FF = "SumSum2",
            FF_ESR = "SumSum2")
    public void getInnerTextDetailsTwoSums() throws Exception {
        getInnerTextFormated("<details id='tester'>"
                + "<summary>Sum</summary>"
                + "<summary>Sum2</summary>"
                + "<p>detail</p>"
                + "</details>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Sum\n\ndetail")
    @HtmlUnitNYI(CHROME = "Sum\ndetail",
            EDGE = "Sum\ndetail",
            FF = "Sum\ndetail",
            FF_ESR = "Sum\ndetail")
    public void getInnerTextDetailsOpen() throws Exception {
        getInnerTextFormated("<details id='tester' open=true>"
                + "<summary>Sum</summary>"
                + "<p>detail</p>"
                + "</details>");
    }

    private void getInnerTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    private void getOuterTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').outerText");
        assertEquals(getExpectedAlerts()[0], "" + text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleNumberInputValidNumber() throws Exception {
        getInnerTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "12");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getVisibleNumberInputInvalidNumber() throws Exception {
        getInnerTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "ab");
    }

    private void getInnerTextFormatedAfterTyping(final String htmlTesterSnipped,
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

        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text")
    public void getInnerTextWithScript() throws Exception {
        getInnerTextFormated("<div id='tester'>"
                + "text"
                + "<script>var x = 'invisible';</script>"
                + "</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xz")
    public void getInnerTextWithTitle() throws Exception {
        getInnerTextFormated("<div id='tester'>"
                + "x<title>y</title>z"
                + "</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("y")
    public void getInnerTextTitle() throws Exception {
        getInnerTextFormated("<div>"
                + "x<title id='tester'>y</title>z"
                + "</div>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("y")
    public void getInnerTextTitleInHead() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head id='tester'><title>y</title></head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("text")
    public void getOuterTextWithScript() throws Exception {
        getOuterTextFormated("<div id='tester'>"
                + "text"
                + "<script>var x = 'invisible';</script>"
                + "</div>");
    }
}
