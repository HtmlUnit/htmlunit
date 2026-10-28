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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for {@link HtmlSerializerInnerOuterText}.
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
    @Alerts(DEFAULT = "A B C D EF G H I\nJ K L M\nSecond\nThird Fourth\nLast",
            FF = "",
            FF_ESR = "")
    public void getInnerTextWhiteSpaceOptgroupSelect() throws Exception {
        getInnerTextWhiteSpaceSelectOptgroup(null);
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nJ K L M\nSecond\nThird Fourth\nLast",
            FF = "",
            FF_ESR = "")
    public void getInnerTextWhiteSpaceSelectOptgroupNormal() throws Exception {
        getInnerTextWhiteSpaceSelectOptgroup("normal");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nJ K L M\nSecond\nThird Fourth\nLast",
            FF = "",
            FF_ESR = "")
    public void getInnerTextWhiteSpaceSelectOptgroupNowrap() throws Exception {
        getInnerTextWhiteSpaceSelectOptgroup("nowrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nJ K L M\nSecond\nThird Fourth\nLast",
            FF = "",
            FF_ESR = "")
    public void getInnerTextWhiteSpaceSelectOptgroupPre() throws Exception {
        getInnerTextWhiteSpaceSelectOptgroup("pre");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nJ K L M\nSecond\nThird Fourth\nLast",
            FF = "",
            FF_ESR = "")
    public void getInnerTextWhiteSpaceSelectOptgroupPreWrap() throws Exception {
        getInnerTextWhiteSpaceSelectOptgroup("pre-wrap");
    }

    /**
     * Verifies getInnerText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "A B C D EF G H I\nJ K L M\nSecond\nThird Fourth\nLast",
            FF = "",
            FF_ESR = "")
    public void getInnerTextWhiteSpaceSelectOptgroupPreLine() throws Exception {
        getInnerTextWhiteSpaceSelectOptgroup("pre-line");
    }

    private void getInnerTextWhiteSpaceSelectOptgroup(final String whiteSpace) throws Exception {
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
        assertEquals(getExpectedAlerts()[0], text);
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title\n\nSubtitle")
    public void getInnerTextHgroup() throws Exception {
        getInnerTextFormated("<hgroup id='tester'>"
                + "<h1>Title</h1>"
                + "<p>Subtitle</p>"
                + "</hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title")
    public void getInnerTextHgroupHeadingOnly() throws Exception {
        getInnerTextFormated("<hgroup id='tester'>"
                + "<h1>Title</h1>"
                + "</hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title\n\nSubtitle\n\nTagline")
    public void getInnerTextHgroupMultipleChildren() throws Exception {
        getInnerTextFormated("<hgroup id='tester'>"
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
    public void getInnerTextHgroupEmpty() throws Exception {
        getInnerTextFormated("<hgroup id='tester'></hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title\n\nSubtitle")
    public void getOuterTextHgroup() throws Exception {
        getOuterTextFormated("<hgroup id='tester'>"
                + "<h1>Title</h1>"
                + "<p>Subtitle</p>"
                + "</hgroup>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Title")
    public void getOuterTextHgroupHeadingOnly() throws Exception {
        getOuterTextFormated("<hgroup id='tester'>"
                + "<h1>Title</h1>"
                + "</hgroup>");
    }

    // ---------- parity with the visible text tests --------------------------------

    /**
     * A CDATA section in a HTML document is a bogus comment.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("beforeafter")
    public void getInnerTextCdata() throws Exception {
        getInnerTextFormated("<div id='tester'>before<![CDATA[inside]]>after</div>");
    }

    /**
     * A single inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("demo")
    public void getInnerTextEm() throws Exception {
        getInnerTextFormated("<em id='tester'>demo</em>");
    }

    /**
     * A nested hgroup.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Outer\n\nTitle\n\nSubtitle")
    public void getInnerTextHgroupNested() throws Exception {
        getInnerTextFormated("<div id='tester'><p>Outer</p><hgroup><h2>Title</h2><p>Subtitle</p></hgroup></div>");
    }

    /**
     * A nested hgroup (outerText).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Outer\n\nTitle\n\nSubtitle")
    public void getOuterTextHgroupNested() throws Exception {
        getOuterTextFormated("<div id='tester'><p>Outer</p><hgroup><h2>Title</h2><p>Subtitle</p></hgroup></div>");
    }

    // ---------- display and visibility --------------------------------------------

    /**
     * A child with display none is not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextDisplayNoneChild() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span style='display: none'>b</span>c</div>");
    }

    /**
     * The element itself has display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextDisplayNoneSelf() throws Exception {
        getInnerTextFormated("<div id='tester' style='display: none'>abc</div>");
    }

    /**
     * A nested element inside a display none block is not part of the text, even if it is display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextDisplayNoneNested() throws Exception {
        getInnerTextFormated("<div id='tester'>a<div style='display: none'><span style='display: inline'>b</span></div>"
                + "c</div>");
    }

    /**
     * A child with visibility hidden is not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextVisibilityHiddenChild() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span style='visibility: hidden'>b</span>c</div>");
    }

    /**
     * The element itself has visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextVisibilityHiddenSelf() throws Exception {
        getInnerTextFormated("<div id='tester' style='visibility: hidden'>abc</div>");
    }

    /**
     * A visible child inside of a hidden parent is part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("acd")
    public void getInnerTextVisibilityHiddenChildVisible() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span style='visibility: hidden'>b<span style='visibility: visible'>c</span>"
                + "</span>d</div>");
    }

    /**
     * A child with visibility collapse.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextVisibilityCollapse() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span style='visibility: collapse'>b</span>c</div>");
    }

    /**
     * A child with the hidden attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextHiddenAttribute() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span hidden>b</span>c</div>");
    }

    /**
     * The hidden attribute is overridden by an explicit display.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextHiddenAttributeOverridden() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span hidden style='display: inline'>b</span>c</div>");
    }

    /**
     * A child with opacity zero.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextOpacityZero() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span style='opacity: 0'>b</span>c</div>");
    }

    /**
     * An inline-block between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextDisplayInlineBlock() throws Exception {
        getInnerTextFormated("<div id='tester'>a<div style='display: inline-block'>b</div>c</div>");
    }

    /**
     * Block elements with display inline do not break the line.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextDisplayInlineOnBlocks() throws Exception {
        getInnerTextFormated("<div id='tester'><div style='display: inline'>a</div><div style='display: inline'>b</div>"
                + "</div>");
    }

    /**
     * A span with display block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextDisplayBlockOnSpan() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span style='display: block'>b</span>c</div>");
    }

    /**
     * The children of a flex container.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextDisplayFlexSpans() throws Exception {
        getInnerTextFormated("<div id='tester' style='display: flex'><span>a</span><span>b</span></div>");
    }

    /**
     * Block children of a flex container.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextDisplayFlexDivs() throws Exception {
        getInnerTextFormated("<div id='tester' style='display: flex'><div>a</div><div>b</div></div>");
    }

    /**
     * The children of a grid container.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextDisplayGrid() throws Exception {
        getInnerTextFormated("<div id='tester' style='display: grid'><span>a</span><span>b</span></div>");
    }

    /**
     * An element with display contents.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextDisplayContents() throws Exception {
        getInnerTextFormated("<div id='tester'>a<div style='display: contents'>b</div>c</div>");
    }

    /**
     * Divs that are displayed as table.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\tb")
    public void getInnerTextDisplayTableCellDivs() throws Exception {
        getInnerTextFormated("<div id='tester'><div style='display: table'><div style='display: table-row'>"
                + "<div style='display: table-cell'>a</div><div style='display: table-cell'>b</div></div></div>"
                + "</div>");
    }

    /**
     * Divs that are displayed as list items.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextDisplayListItemOnDivs() throws Exception {
        getInnerTextFormated("<div id='tester'><div style='display: list-item'>a</div><div style='display: list-item'>"
                + "b</div></div>");
    }

    /**
     * A child that is positioned outside of the viewport.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextPositionAbsoluteOffscreen() throws Exception {
        getInnerTextFormated("<div id='tester'>a<span style='position: absolute; left: -9999px'>b</span>c</div>");
    }

    /**
     * The content is clipped by a container without height.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextOverflowHiddenNoHeight() throws Exception {
        getInnerTextFormated("<div id='tester' style='height: 0; overflow: hidden'>abc</div>");
    }

    /**
     * Floating blocks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextFloatBlocks() throws Exception {
        getInnerTextFormated("<div id='tester'><div style='float: left'>a</div><div style='float: left'>b</div></div>");
    }

    /**
     * A child with content-visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextContentVisibilityHidden() throws Exception {
        getInnerTextFormated("<div id='tester'>a<div style='content-visibility: hidden'>b</div>c</div>");
    }

    // ---------- block structure ---------------------------------------------------

    /**
     * Nested block elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void getInnerTextNestedDivs() throws Exception {
        getInnerTextFormated("<div id='tester'><div>a<div>b</div>c</div>d</div>");
    }

    /**
     * An empty block between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextEmptyDivBetweenText() throws Exception {
        getInnerTextFormated("<div id='tester'>a<div></div>b</div>");
    }

    /**
     * A block inside of an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextDivInsideSpan() throws Exception {
        getInnerTextFormated("<span id='tester'>a<div>b</div>c</span>");
    }

    /**
     * A br after a block element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\nb")
    public void getInnerTextBrAfterBlock() throws Exception {
        getInnerTextFormated("<div id='tester'><div>a</div><br>b</div>");
    }

    /**
     * Consecutive br elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\n\nb")
    public void getInnerTextBrConsecutive() throws Exception {
        getInnerTextFormated("<div id='tester'>a<br><br><br>b</div>");
    }

    /**
     * A br at the start and at the end.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\na\n")
    public void getInnerTextBrLeadingTrailing() throws Exception {
        getInnerTextFormated("<div id='tester'><br>a<br></div>");
    }

    /**
     * Headings are block elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void getInnerTextHeadings() throws Exception {
        getInnerTextFormated("<div id='tester'><h1>a</h1><h2>b</h2>c<h3>d</h3></div>");
    }

    /**
     * A blockquote between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextBlockquote() throws Exception {
        getInnerTextFormated("<div id='tester'>a<blockquote>b</blockquote>c</div>");
    }

    /**
     * The sectioning elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd\ne\nf\ng")
    public void getInnerTextSectioningElements() throws Exception {
        getInnerTextFormated("<div id='tester'><section>a</section><article>b</article><nav>c</nav><aside>d</aside><header>"
                + "e</header><footer>f</footer><main>g</main></div>");
    }

    /**
     * A figure with a caption.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("caption")
    public void getInnerTextFigureFigcaption() throws Exception {
        getInnerTextFormated("<div id='tester'><figure><img alt='alt'><figcaption>caption</figcaption></figure></div>");
    }

    /**
     * A horizontal rule between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextHrBetweenText() throws Exception {
        getInnerTextFormated("<div id='tester'>a<hr>b</div>");
    }

    /**
     * A fieldset with a legend.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("legend\na")
    public void getInnerTextFieldsetLegend() throws Exception {
        getInnerTextFormated("<fieldset id='tester'><legend>legend</legend>a</fieldset>");
    }

    /**
     * An address element between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextAddressElement() throws Exception {
        getInnerTextFormated("<div id='tester'>a<address>b</address>c</div>");
    }

    /**
     * A closed dialog is not rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextDialogClosed() throws Exception {
        getInnerTextFormated("<div id='tester'>a<dialog>b</dialog>c</div>");
    }

    /**
     * An open dialog is rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextDialogOpen() throws Exception {
        getInnerTextFormated("<div id='tester'>a<dialog open>b</dialog>c</div>");
    }

    // ---------- text and white space ----------------------------------------------

    /**
     * Leading and trailing spaces are removed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void getInnerTextLeadingTrailingSpaces() throws Exception {
        getInnerTextFormated("<div id='tester'>   a   </div>");
    }

    /**
     * Spaces are collapsed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getInnerTextCollapsedSpaces() throws Exception {
        getInnerTextFormated("<div id='tester'>a      b</div>");
    }

    /**
     * Spaces across inline element boundaries.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c")
    public void getInnerTextSpacesAcrossInlineElements() throws Exception {
        getInnerTextFormated("<div id='tester'>a <b> b </b> c</div>");
    }

    /**
     * A space between two inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getInnerTextSpaceBetweenInlineElements() throws Exception {
        getInnerTextFormated("<div id='tester'><span>a</span> <span>b</span></div>");
    }

    /**
     * An empty inline element between spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getInnerTextEmptyInlineBetweenSpaces() throws Exception {
        getInnerTextFormated("<div id='tester'>a <b></b> b</div>");
    }

    /**
     * A newline in the text is a space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getInnerTextNewlineInText() throws Exception {
        getInnerTextFormated("<div id='tester'>a\nb</div>");
    }

    /**
     * A tab in the text is a space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getInnerTextTabInText() throws Exception {
        getInnerTextFormated("<div id='tester'>a\tb</div>");
    }

    /**
     * Non-breaking spaces are not collapsed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\u00A0\u00A0\u00A0b")
    public void getInnerTextNbspRun() throws Exception {
        getInnerTextFormated("<div id='tester'>a&nbsp;&nbsp;&nbsp;b</div>");
    }

    /**
     * Leading and trailing non-breaking spaces are kept.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\u00A0a\u00A0")
    public void getInnerTextNbspLeadingTrailing() throws Exception {
        getInnerTextFormated("<div id='tester'>&nbsp;a&nbsp;</div>");
    }

    /**
     * A zero width space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\u200Bb")
    public void getInnerTextZeroWidthSpace() throws Exception {
        getInnerTextFormated("<div id='tester'>a&#8203;b</div>");
    }

    /**
     * Other unicode spaces are not collapsed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\u2003\u2003b")
    public void getInnerTextEmSpaces() throws Exception {
        getInnerTextFormated("<div id='tester'>a&#8195;&#8195;b</div>");
    }

    /**
     * A soft hyphen.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\u00ADb")
    public void getInnerTextSoftHyphen() throws Exception {
        getInnerTextFormated("<div id='tester'>a&shy;b</div>");
    }

    /**
     * Escaped markup characters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b> & \"")
    public void getInnerTextEntityEscapes() throws Exception {
        getInnerTextFormated("<div id='tester'>&lt;b&gt; &amp; &quot;</div>");
    }

    /**
     * The text-transform uppercase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC SS")
    public void getInnerTextTextTransformUppercase() throws Exception {
        getInnerTextFormated("<div id='tester' style='text-transform: uppercase'>abc ß</div>");
    }

    /**
     * The text-transform lowercase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextTextTransformLowercase() throws Exception {
        getInnerTextFormated("<div id='tester' style='text-transform: lowercase'>ABC</div>");
    }

    /**
     * The text-transform capitalize.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Abc Def-Ghi")
    public void getInnerTextTextTransformCapitalize() throws Exception {
        getInnerTextFormated("<div id='tester' style='text-transform: capitalize'>abc def-ghi</div>");
    }

    /**
     * The white-space break-spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   b\n  c")
    public void getInnerTextWhiteSpaceBreakSpaces() throws Exception {
        getInnerTextFormated("<div id='tester' style='white-space: break-spaces'>a   b\n  c</div>");
    }

    /**
     * The white-space-collapse preserve.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   b\n  c")
    public void getInnerTextWhiteSpaceCollapsePreserve() throws Exception {
        getInnerTextFormated("<div id='tester' style='white-space-collapse: preserve'>a   b\n  c</div>");
    }

    /**
     * The white-space-collapse preserve-breaks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b\nc")
    public void getInnerTextWhiteSpaceCollapsePreserveBreaks() throws Exception {
        getInnerTextFormated("<div id='tester' style='white-space-collapse: preserve-breaks'>a   b\n  c</div>");
    }

    /**
     * The white-space-collapse break-spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   b\n  c")
    public void getInnerTextWhiteSpaceCollapseBreakSpaces() throws Exception {
        getInnerTextFormated("<div id='tester' style='white-space-collapse: break-spaces'>a   b\n  c</div>");
    }

    /**
     * White-space pre on an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x a   b\n  c y")
    public void getInnerTextWhiteSpacePreOnInline() throws Exception {
        getInnerTextFormated("<div id='tester'>x <span style='white-space: pre'>a   b\n  c</span> y</div>");
    }

    /**
     * A child resets the white-space of its parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   bc d ef   g")
    public void getInnerTextWhiteSpacePreChildNormal() throws Exception {
        getInnerTextFormated("<div id='tester' style='white-space: pre'>a   b<span style='white-space: normal'>"
                + "c   d\n  e</span>f   g</div>");
    }

    /**
     * White-space pre-line on an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x a b\nc y")
    public void getInnerTextWhiteSpacePreLineInline() throws Exception {
        getInnerTextFormated("<div id='tester'>x <span style='white-space: pre-line'>a   b\n  c</span> y</div>");
    }

    /**
     * The first newline of a pre is removed by the parser.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextPreLeadingNewline() throws Exception {
        getInnerTextFormated("<pre id='tester'>\nabc</pre>");
    }

    /**
     * Only the first newline of a pre is removed by the parser.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\nabc")
    public void getInnerTextPreLeadingTwoNewlines() throws Exception {
        getInnerTextFormated("<pre id='tester'>\n\nabc</pre>");
    }

    /**
     * A pre with inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a  b\n  c")
    public void getInnerTextPreWithInlineElements() throws Exception {
        getInnerTextFormated("<pre id='tester'>a  <b>b</b>\n  c</pre>");
    }

    /**
     * A pre inside of a div.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb  c\nd")
    public void getInnerTextPreInsideDiv() throws Exception {
        getInnerTextFormated("<div id='tester'>a<pre>b  c</pre>d</div>");
    }

    /**
     * Generated content is not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void getInnerTextCssGeneratedContent() throws Exception {
        getInnerTextFormated("<style>#tester::before { content: 'x'; } #tester::after { content: 'y'; }</style>"
                + "<div id='tester'>a</div>");
    }

    /**
     * Comments are not part of the text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextCommentBetweenText() throws Exception {
        getInnerTextFormated("<div id='tester'>a<!-- c -->b</div>");
    }

    /**
     * An unknown element is inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextUnknownElement() throws Exception {
        getInnerTextFormated("<div id='tester'>a<foo-bar>b</foo-bar>c</div>");
    }

    // ---------- tables ------------------------------------------------------------

    /**
     * A simple table.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\tb\nc\td")
    public void getInnerTextTableSimple() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td>a</td><td>b</td></tr><tr><td>c</td><td>d</td></tr></table>");
    }

    /**
     * A table with caption, head, body and foot.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cap\nh1\th2\na\tb\nf1\tf2")
    public void getInnerTextTableWithSections() throws Exception {
        getInnerTextFormated("<table id='tester'><caption>cap</caption><thead><tr><th>h1</th><th>h2</th></tr></thead><tbody>"
                + "<tr><td>a</td><td>b</td></tr></tbody><tfoot><tr><td>f1</td><td>f2</td></tr></tfoot></table>");
    }

    /**
     * A tfoot defined before the tbody.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("f\na")
    public void getInnerTextTableFootBeforeBody() throws Exception {
        getInnerTextFormated("<table id='tester'><tfoot><tr><td>f</td></tr></tfoot><tbody><tr><td>a</td></tr></tbody>"
                + "</table>");
    }

    /**
     * A table with colspan and rowspan.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\tc\nd")
    public void getInnerTextTableColspanRowspan() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td colspan='2'>a</td></tr><tr><td rowspan='2'>b</td><td>c</td></tr>"
                + "<tr><td>d</td></tr></table>");
    }

    /**
     * A table with empty cells.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\t\tc\n\te\t")
    public void getInnerTextTableEmptyCells() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td>a</td><td></td><td>c</td></tr><tr><td></td><td>e</td><td></td></tr>"
                + "</table>");
    }

    /**
     * A table inside of a table cell.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\t\nb\tc")
    public void getInnerTextTableNested() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td>a</td><td><table><tr><td>b</td><td>c</td></tr></table></td></tr>"
                + "</table>");
    }

    /**
     * A table cell with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\tc")
    public void getInnerTextTableCellDisplayNone() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td>a</td><td style='display: none'>b</td><td>c</td></tr></table>");
    }

    /**
     * A table row with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void getInnerTextTableRowDisplayNone() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td>a</td></tr><tr style='display: none'><td>b</td></tr><tr><td>c</td>"
                + "</tr></table>");
    }

    /**
     * A table cell with block content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\nb\n\n\tc")
    public void getInnerTextTableCellWithBlocks() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td><p>a</p><p>b</p></td><td>c</td></tr></table>");
    }

    /**
     * A table cell with line breaks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\tc")
    public void getInnerTextTableCellWithLineBreaks() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td>a<br>b</td><td>c</td></tr></table>");
    }

    /**
     * White space between the cells and rows.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\tb\nc")
    public void getInnerTextTableWhitespaceBetweenCells() throws Exception {
        getInnerTextFormated("<table id='tester'>\n  <tr>\n    <td>a</td>\n    <td>b</td>\n  </tr>\n  <tr>\n    <td>c</td>"
                + "\n  </tr>\n</table>");
    }

    /**
     * The row is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\tb")
    public void getInnerTextTableRowAsTester() throws Exception {
        getInnerTextFormated("<table><tr id='tester'><td>a</td><td>b</td></tr></table>");
    }

    /**
     * The cell is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getInnerTextTableCellAsTester() throws Exception {
        getInnerTextFormated("<table><tr><td id='tester'>  a  b  </td></tr></table>");
    }

    /**
     * Table cells with display block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getInnerTextTableCellDisplayBlock() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td style='display: block'>a</td><td style='display: block'>b</td></tr>"
                + "</table>");
    }

    /**
     * A table between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\tb\ny")
    public void getInnerTextTableInsideDiv() throws Exception {
        getInnerTextFormated("<div id='tester'>x<table><tr><td>a</td><td>b</td></tr></table>y</div>");
    }

    // ---------- form controls -----------------------------------------------------

    /**
     * The text of a button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getInnerTextButtonText() throws Exception {
        getInnerTextFormated("<button id='tester'>  a   b  </button>");
    }

    /**
     * A button with inline children.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xa b cy")
    public void getInnerTextButtonWithInlineChildren() throws Exception {
        getInnerTextFormated("<div id='tester'>x<button>a <b>b</b> c</button>y</div>");
    }

    /**
     * A button with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextButtonDisplayNone() throws Exception {
        getInnerTextFormated("<div id='tester'>x<button style='display: none'>a</button>y</div>");
    }

    /**
     * The input of type text between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypeText() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='text' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type password between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypePassword() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='password' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type button between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypeButton() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='button' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type image between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypeImage() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='image' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type file between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypeFile() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='file' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type range between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypeRange() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='range' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type color between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypeColor() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='color' value='v' alt='alt'>y</div>");
    }

    /**
     * The input of type date between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextInputTypeDate() throws Exception {
        getInnerTextFormated("<div id='tester'>x<input type='date' value='v' alt='alt'>y</div>");
    }

    /**
     * A textarea with new lines.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextTextareaWithNewlines() throws Exception {
        getInnerTextFormated("<div id='tester'>x<textarea>a\n b\n\nc</textarea>y</div>");
    }

    /**
     * A textarea with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextTextareaDisplayNone() throws Exception {
        getInnerTextFormated("<div id='tester'>x<textarea style='display: none'>a</textarea>y</div>");
    }

    /**
     * The textarea is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextTextareaAsTester() throws Exception {
        getInnerTextFormated("<textarea id='tester'>  a   b  </textarea>");
    }

    /**
     * A select with the multiple attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextSelectMultiple() throws Exception {
        getInnerTextFormated("<select id='tester' multiple><option>a</option><option selected>b</option><option>c</option>"
                + "</select>");
    }

    /**
     * A select displayed as list box.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void getInnerTextSelectSizeListbox() throws Exception {
        getInnerTextFormated("<select id='tester' size='3'><option>a</option><option>b</option><option>c</option></select>");
    }

    /**
     * A select with a selected option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "a\nb",
            EDGE = "a\nb")
    public void getInnerTextSelectSelectedOption() throws Exception {
        getInnerTextFormated("<select id='tester'><option>a</option><option selected>b</option></select>");
    }

    /**
     * A select with a disabled option.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "a\nb",
            EDGE = "a\nb")
    public void getInnerTextSelectDisabledOption() throws Exception {
        getInnerTextFormated("<select id='tester'><option>a</option><option disabled>b</option></select>");
    }

    /**
     * A select with an option with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "a\nb",
            EDGE = "a\nb")
    public void getInnerTextSelectOptionDisplayNone() throws Exception {
        getInnerTextFormated("<select id='tester'><option>a</option><option style='display: none'>b</option></select>");
    }

    /**
     * A select without options.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextSelectWithoutOptions() throws Exception {
        getInnerTextFormated("<div id='tester'>x<select></select>y</div>");
    }

    /**
     * An option with a label attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "T",
            EDGE = "T")
    public void getInnerTextOptionLabelAttribute() throws Exception {
        getInnerTextFormated("<select id='tester'><option label='L'>T</option></select>");
    }

    /**
     * The option is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  a   b  ")
    public void getInnerTextOptionAsTester() throws Exception {
        getInnerTextFormated("<select><option id='tester'>  a   b  </option></select>");
    }

    /**
     * A datalist is not rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void getInnerTextDatalistWithOptions() throws Exception {
        getInnerTextFormated("<div id='tester'>x<datalist><option value='v'>t</option></datalist>y</div>");
    }

    /**
     * A disabled fieldset.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("l\na")
    public void getInnerTextFieldsetDisabled() throws Exception {
        getInnerTextFormated("<fieldset id='tester' disabled><legend>l</legend><input value='v'>a</fieldset>");
    }

    /**
     * A label with an input.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a  b")
    public void getInnerTextLabelWithInput() throws Exception {
        getInnerTextFormated("<label id='tester'>a <input type='checkbox'> b</label>");
    }

    /**
     * Meter, progress and output elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xoy")
    public void getInnerTextMeterProgressOutput() throws Exception {
        getInnerTextFormated("<div id='tester'>x<meter value='1'>m</meter><progress value='1'>p</progress><output>o</output>"
                + "y</div>");
    }

    // ---------- embedded content and special elements -----------------------------

    /**
     * An image with alt text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextImgWithAlt() throws Exception {
        getInnerTextFormated("<div id='tester'>a<img alt='alt'>b</div>");
    }

    /**
     * An image without alt text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextImgWithoutAlt() throws Exception {
        getInnerTextFormated("<div id='tester'>a<img>b</div>");
    }

    /**
     * An anchor with a link.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getInnerTextAnchor() throws Exception {
        getInnerTextFormated("<div id='tester'>a<a href='#x'>b</a>c</div>");
    }

    /**
     * An iframe with srcdoc and fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextIframeSrcdoc() throws Exception {
        getInnerTextFormated("<div id='tester'>a<iframe srcdoc='<b>x</b>'>fallback</iframe>b</div>");
    }

    /**
     * An object with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("afallbackb")
    public void getInnerTextObjectFallback() throws Exception {
        getInnerTextFormated("<div id='tester'>a<object data='x.bin'>fallback</object>b</div>");
    }

    /**
     * A video with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextVideoFallback() throws Exception {
        getInnerTextFormated("<div id='tester'>a<video>fallback</video>b</div>");
    }

    /**
     * An audio with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextAudioFallback() throws Exception {
        getInnerTextFormated("<div id='tester'>a<audio>fallback</audio>b</div>");
    }

    /**
     * A canvas with fallback content.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextCanvasFallback() throws Exception {
        getInnerTextFormated("<div id='tester'>a<canvas>fallback</canvas>b</div>");
    }

    /**
     * An svg with text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a\nt\nb",
            FF = "atb",
            FF_ESR = "atb")
    public void getInnerTextSvgText() throws Exception {
        getInnerTextFormated("<div id='tester'>a<svg width='100' height='20'><text x='0' y='10'>t</text></svg>b</div>");
    }

    /**
     * An svg with title and desc.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextSvgTitleAndDesc() throws Exception {
        getInnerTextFormated("<div id='tester'>a<svg width='10' height='10'><title>tt</title><desc>dd</desc></svg>b</div>");
    }

    /**
     * An svg with a foreignObject.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nf\nb")
    public void getInnerTextSvgForeignObject() throws Exception {
        getInnerTextFormated("<div id='tester'>a<svg width='100' height='20'><foreignObject width='100' height='20'><div>"
                + "f</div></foreignObject></svg>b</div>");
    }

    /**
     * A MathML element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a\n𝑥\n+\n1\nb",
            FF = "ax+1b",
            FF_ESR = "ax+1b")
    public void getInnerTextMathMl() throws Exception {
        getInnerTextFormated("<div id='tester'>a<math><mi>x</mi><mo>+</mo><mn>1</mn></math>b</div>");
    }

    /**
     * The content of a template is not rendered.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextTemplateElement() throws Exception {
        getInnerTextFormated("<div id='tester'>a<template>b</template>c</div>");
    }

    /**
     * A noscript element (scripting is enabled).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void getInnerTextNoscriptElement() throws Exception {
        getInnerTextFormated("<div id='tester'>a<noscript>b</noscript>c</div>");
    }

    /**
     * A shadow root with a slot.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("light")
    public void getInnerTextShadowRootWithSlot() throws Exception {
        getInnerTextFormated("<div id='tester'><b>light</b></div><script>"
                + "document.getElementById('tester').attachShadow({mode: 'open'}).innerHTML = 'before<slot>"
                + "</slot>after';</script>");
    }

    /**
     * A shadow root without a slot hides the light DOM.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("")
    public void getInnerTextShadowRootWithoutSlot() throws Exception {
        getInnerTextFormated("<div id='tester'><b>light</b></div><script>"
                + "document.getElementById('tester').attachShadow({mode: 'open'}).innerHTML = '<i>shadow</i>"
                + "';</script>");
    }

    /**
     * A closed details without a summary.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getInnerTextDetailsWithoutSummary() throws Exception {
        getInnerTextFormated("<details id='tester'>a</details>");
    }

    /**
     * A closed details with a summary only.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("s")
    public void getInnerTextDetailsSummaryOnly() throws Exception {
        getInnerTextFormated("<details id='tester'><summary>s</summary></details>");
    }

    /**
     * The summary is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("s")
    public void getInnerTextSummaryAsTester() throws Exception {
        getInnerTextFormated("<details><summary id='tester'>s</summary>a</details>");
    }

    /**
     * A ruby with rp elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void getInnerTextRubyWithRp() throws Exception {
        getInnerTextFormated("<div id='tester'><ruby>a<rp>(</rp><rt>b</rt><rp>)</rp></ruby></div>");
    }

    // ---------- outerText ---------------------------------------------------------

    /**
     * The element itself has display none. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void getOuterTextDisplayNoneSelf() throws Exception {
        getOuterTextFormated("<div id='tester' style='display: none'>abc</div>");
    }

    /**
     * Nested block elements. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void getOuterTextNestedDivs() throws Exception {
        getOuterTextFormated("<div id='tester'><div>a<div>b</div>c</div>d</div>");
    }

    /**
     * The text-transform uppercase. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC SS")
    public void getOuterTextTextTransformUppercase() throws Exception {
        getOuterTextFormated("<div id='tester' style='text-transform: uppercase'>abc ß</div>");
    }

    /**
     * A pre with inline elements. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a  b\n  c")
    public void getOuterTextPreWithInlineElements() throws Exception {
        getOuterTextFormated("<pre id='tester'>a  <b>b</b>\n  c</pre>");
    }

    /**
     * A simple table. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\tb\nc\td")
    public void getOuterTextTableSimple() throws Exception {
        getOuterTextFormated("<table id='tester'><tr><td>a</td><td>b</td></tr><tr><td>c</td><td>d</td></tr></table>");
    }

    /**
     * The cell is the element under test. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getOuterTextTableCellAsTester() throws Exception {
        getOuterTextFormated("<table><tr><td id='tester'>  a  b  </td></tr></table>");
    }

    /**
     * The text of a button. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void getOuterTextButtonText() throws Exception {
        getOuterTextFormated("<button id='tester'>  a   b  </button>");
    }

    /**
     * A select with a selected option. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            CHROME = "a\nb",
            EDGE = "a\nb")
    public void getOuterTextSelectSelectedOption() throws Exception {
        getOuterTextFormated("<select id='tester'><option>a</option><option selected>b</option></select>");
    }

    /**
     * A shadow root with a slot. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("light")
    public void getOuterTextShadowRootWithSlot() throws Exception {
        getOuterTextFormated("<div id='tester'><b>light</b></div><script>"
                + "document.getElementById('tester').attachShadow({mode: 'open'}).innerHTML = 'before<slot>"
                + "</slot>after';</script>");
    }

    /**
     * A horizontal rule between text. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void getOuterTextHrBetweenText() throws Exception {
        getOuterTextFormated("<div id='tester'>a<hr>b</div>");
    }
}
