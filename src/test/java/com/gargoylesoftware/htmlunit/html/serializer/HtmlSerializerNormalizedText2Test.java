/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlNumberInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlSerializerNormalizedText}.
 * This contains the tests for plain controls.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlSerializerNormalizedText2Test extends SimpleWebTestCase {

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n")
    public void getNormalizedTextWhiteSpaceBreak() throws Exception {
        getNormalizedTextWhiteSpaceBreak(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n")
    public void getNormalizedTextWhiteSpaceBreakNormal() throws Exception {
        getNormalizedTextWhiteSpaceBreak("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n")
    public void getNormalizedTextWhiteSpaceBreakNowrap() throws Exception {
        getNormalizedTextWhiteSpaceBreak("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n")
    public void getNormalizedTextWhiteSpaceBreakPre() throws Exception {
        getNormalizedTextWhiteSpaceBreak("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n")
    public void getNormalizedTextWhiteSpaceBreakPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceBreak("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\n")
    public void getNormalizedTextWhiteSpaceBreakPreLine() throws Exception {
        getNormalizedTextWhiteSpaceBreak("pre-line");
    }

    private void getNormalizedTextWhiteSpaceBreak(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <br id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceInputHidden() throws Exception {
        getNormalizedTextWhiteSpaceInputHidden(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceInputHiddenNormal() throws Exception {
        getNormalizedTextWhiteSpaceInputHidden("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceInputHiddenNowrap() throws Exception {
        getNormalizedTextWhiteSpaceInputHidden("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceInputHiddenPre() throws Exception {
        getNormalizedTextWhiteSpaceInputHidden("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceInputHiddenPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceInputHidden("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceInputHiddenPreLine() throws Exception {
        getNormalizedTextWhiteSpaceInputHidden("pre-line");
    }

    private void getNormalizedTextWhiteSpaceInputHidden(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='hidden' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceScript() throws Exception {
        getNormalizedTextWhiteSpaceScript(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceScriptNormal() throws Exception {
        getNormalizedTextWhiteSpaceScript("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceScriptNowrap() throws Exception {
        getNormalizedTextWhiteSpaceScript("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceScriptPre() throws Exception {
        getNormalizedTextWhiteSpaceScript("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceScriptPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceScript("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceScriptPreLine() throws Exception {
        getNormalizedTextWhiteSpaceScript("pre-line");
    }

    private void getNormalizedTextWhiteSpaceScript(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <script id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "var x;  \n \t   \n var y;</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceStyle() throws Exception {
        getNormalizedTextWhiteSpaceStyle(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceStyleNormal() throws Exception {
        getNormalizedTextWhiteSpaceStyle("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceStyleNowrap() throws Exception {
        getNormalizedTextWhiteSpaceStyle("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceStylePre() throws Exception {
        getNormalizedTextWhiteSpaceStyle("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceStylePreWrap() throws Exception {
        getNormalizedTextWhiteSpaceStyle("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceStylePreLine() throws Exception {
        getNormalizedTextWhiteSpaceStyle("pre-line");
    }

    private void getNormalizedTextWhiteSpaceStyle(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <style id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \nEF\nG \n H  <br> I  </style>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceNoframes() throws Exception {
        getNormalizedTextWhiteSpaceNoframes(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceNoframesNormal() throws Exception {
        getNormalizedTextWhiteSpaceNoframes("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceNoframesNowrap() throws Exception {
        getNormalizedTextWhiteSpaceNoframes("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceNoframesPre() throws Exception {
        getNormalizedTextWhiteSpaceNoframes("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceNoframesPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceNoframes("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextWhiteSpaceNoframesPreLine() throws Exception {
        getNormalizedTextWhiteSpaceNoframes("pre-line");
    }

    private void getNormalizedTextWhiteSpaceNoframes(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <noframes id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \nEF\nG \n H  <br> I  </noframes>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceDiv() throws Exception {
        getNormalizedTextWhiteSpaceDiv(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceDivNormal() throws Exception {
        getNormalizedTextWhiteSpaceDiv("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceDivNowrap() throws Exception {
        getNormalizedTextWhiteSpaceDiv("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceDivPre() throws Exception {
        getNormalizedTextWhiteSpaceDiv("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceDivPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceDiv("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceDivPreLine() throws Exception {
        getNormalizedTextWhiteSpaceDiv("pre-line");
    }

    private void getNormalizedTextWhiteSpaceDiv(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <div id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                + "  A B  C\t \t  D \nEF\nG \n H  <br> I  </div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \n"
            + "EF\n"
            + "G \n"
            + " H   I  ")
    public void getNormalizedTextWhiteSpacePre() throws Exception {
        getNormalizedTextWhiteSpacePre(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \n"
            + "EF\n"
            + "G \n"
            + " H   I  ")
    public void getNormalizedTextWhiteSpacePreNormal() throws Exception {
        getNormalizedTextWhiteSpacePre("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \n"
            + "EF\n"
            + "G \n"
            + " H   I  ")
    public void getNormalizedTextWhiteSpacePreNowrap() throws Exception {
        getNormalizedTextWhiteSpacePre("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \n"
            + "EF\n"
            + "G \n"
            + " H   I  ")
    public void getNormalizedTextWhiteSpacePrePre() throws Exception {
        getNormalizedTextWhiteSpacePre("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \n"
            + "EF\n"
            + "G \n"
            + " H   I  ")
    public void getNormalizedTextWhiteSpacePrePreWrap() throws Exception {
        getNormalizedTextWhiteSpacePre("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C\t \t  D \n"
            + "EF\n"
            + "G \n"
            + " H   I  ")
    public void getNormalizedTextWhiteSpacePrePreLine() throws Exception {
        getNormalizedTextWhiteSpacePre("pre-line");
    }

    private void getNormalizedTextWhiteSpacePre(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <pre id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \nEF\nG \n H  <br> I  </pre>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \n"
            + "EF\n"
            + "G \n"
            + " H  <br> I")
    public void getNormalizedTextWhiteSpaceTextArea() throws Exception {
        getNormalizedTextWhiteSpaceTextArea(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \n"
            + "EF\n"
            + "G \n"
            + " H  <br> I")
    public void getNormalizedTextWhiteSpaceTextAreaNormal() throws Exception {
        getNormalizedTextWhiteSpaceTextArea("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \n"
            + "EF\n"
            + "G \n"
            + " H  <br> I")
    public void getNormalizedTextWhiteSpaceTextAreaNowrap() throws Exception {
        getNormalizedTextWhiteSpaceTextArea("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \n"
            + "EF\n"
            + "G \n"
            + " H  <br> I")
    public void getNormalizedTextWhiteSpaceTextAreaPre() throws Exception {
        getNormalizedTextWhiteSpaceTextArea("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \n"
            + "EF\n"
            + "G \n"
            + " H  <br> I")
    public void getNormalizedTextWhiteSpaceTextAreaPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceTextArea("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("  A B  C     D \n"
            + "EF\n"
            + "G \n"
            + " H  <br> I")
    public void getNormalizedTextWhiteSpaceTextAreaPreLine() throws Exception {
        getNormalizedTextWhiteSpaceTextArea("pre-line");
    }

    private void getNormalizedTextWhiteSpaceTextArea(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <textarea id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
                    + "  A B  C\t \t  D \nEF\nG \n H  <br> I  </textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceTitle() throws Exception {
        getNormalizedTextWhiteSpaceTitle(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceTitleNormal() throws Exception {
        getNormalizedTextWhiteSpaceTitle("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceTitleNowrap() throws Exception {
        getNormalizedTextWhiteSpaceTitle("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceTitlePre() throws Exception {
        getNormalizedTextWhiteSpaceTitle("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceTitlePreWrap() throws Exception {
        getNormalizedTextWhiteSpaceTitle("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceTitlePreLine() throws Exception {
        getNormalizedTextWhiteSpaceTitle("pre-line");
    }

    private void getNormalizedTextWhiteSpaceTitle(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head>\n"
            + "<title id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">"
            + "  A B  C\t \t  D \nEF\nG \n H  <br> I  </title>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceSelect() throws Exception {
        getNormalizedTextWhiteSpaceSelect(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceSelectNormal() throws Exception {
        getNormalizedTextWhiteSpaceSelect("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceSelectNowrap() throws Exception {
        getNormalizedTextWhiteSpaceSelect("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceSelectPre() throws Exception {
        getNormalizedTextWhiteSpaceSelect("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceSelectPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceSelect("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H \n"
            + " I")
    public void getNormalizedTextWhiteSpaceSelectPreLine() throws Exception {
        getNormalizedTextWhiteSpaceSelect("pre-line");
    }

    private void getNormalizedTextWhiteSpaceSelect(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <select id='tester' "
                    + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "      <option>  A B  C\t \t  D \nEF\nG \n H  <br> I  </option>\n"
            + "      <option>Second</option>\n"
            + "    </select>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputSubmit() throws Exception {
        getNormalizedTextWhiteSpaceInputSubmit(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputSubmitNormal() throws Exception {
        getNormalizedTextWhiteSpaceInputSubmit("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputSubmitNowrap() throws Exception {
        getNormalizedTextWhiteSpaceInputSubmit("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputSubmitPre() throws Exception {
        getNormalizedTextWhiteSpaceInputSubmit("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputSubmitPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceInputSubmit("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputSubmitPreLine() throws Exception {
        getNormalizedTextWhiteSpaceInputSubmit("pre-line");
    }

    private void getNormalizedTextWhiteSpaceInputSubmit(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Submit Query")
    public void getNormalizedTextInputSubmitNoValue() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextInputSubmitBlankValue() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='submit' name='tester' id='tester' value='  \t' >\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputReset() throws Exception {
        getNormalizedTextWhiteSpaceInputReset(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputResetNormal() throws Exception {
        getNormalizedTextWhiteSpaceInputReset("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputResetNowrap() throws Exception {
        getNormalizedTextWhiteSpaceInputReset("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputResetPre() throws Exception {
        getNormalizedTextWhiteSpaceInputReset("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputResetPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceInputReset("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C D EF G H <br> I")
    public void getNormalizedTextWhiteSpaceInputResetPreLine() throws Exception {
        getNormalizedTextWhiteSpaceInputReset("pre-line");
    }

    private void getNormalizedTextWhiteSpaceInputReset(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Reset")
    public void getNormalizedTextInputResetNoValue() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' >\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }


    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getNormalizedTextInputResetBlankValue() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='reset' name='tester' id='tester' value='  \t'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputCheckbox() throws Exception {
        getNormalizedTextWhiteSpaceInputCheckbox(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputCheckboxNormal() throws Exception {
        getNormalizedTextWhiteSpaceInputCheckbox("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputCheckboxNowrap() throws Exception {
        getNormalizedTextWhiteSpaceInputCheckbox("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputCheckboxPre() throws Exception {
        getNormalizedTextWhiteSpaceInputCheckbox("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputCheckboxPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceInputCheckbox("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputCheckboxPreLine() throws Exception {
        getNormalizedTextWhiteSpaceInputCheckbox("pre-line");
    }

    private void getNormalizedTextWhiteSpaceInputCheckbox(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='checkbox' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputRadio() throws Exception {
        getNormalizedTextWhiteSpaceInputRadio(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputRadioNormal() throws Exception {
        getNormalizedTextWhiteSpaceInputRadio("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputRadioNowrap() throws Exception {
        getNormalizedTextWhiteSpaceInputRadio("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputRadioPre() throws Exception {
        getNormalizedTextWhiteSpaceInputRadio("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputRadioPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceInputRadio("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("unchecked")
    public void getNormalizedTextWhiteSpaceInputRadioPreLine() throws Exception {
        getNormalizedTextWhiteSpaceInputRadio("pre-line");
    }

    private void getNormalizedTextWhiteSpaceInputRadio(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='radio' name='tester' id='tester' "
                        + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'"))
                        + " value='  A B  C\t \t  D \nEF\nG \n H  <br> I  '>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. first item\n"
            + "2. A B C D EF G H \n"
            + " I\n"
            + "3. third item\n"
            + "4. 4. item\n"
            + "some text\n"
            + "5. last item")
    public void getNormalizedTextWhiteSpaceOrderedList() throws Exception {
        getNormalizedTextWhiteSpaceOrderedList(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. first item\n"
            + "2. A B C D EF G H \n"
            + " I\n"
            + "3. third item\n"
            + "4. 4. item\n"
            + "some text\n"
            + "5. last item")
    public void getNormalizedTextWhiteSpaceOrderedListNormal() throws Exception {
        getNormalizedTextWhiteSpaceOrderedList("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. first item\n"
            + "2. A B C D EF G H \n"
            + " I\n"
            + "3. third item\n"
            + "4. 4. item\n"
            + "some text\n"
            + "5. last item")
    public void getNormalizedTextWhiteSpaceOrderedListNowrap() throws Exception {
        getNormalizedTextWhiteSpaceOrderedList("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. first item\n"
            + "2. A B C D EF G H \n"
            + " I\n"
            + "3. third item\n"
            + "4. 4. item\n"
            + "some text\n"
            + "5. last item")
    public void getNormalizedTextWhiteSpaceOrderedListPre() throws Exception {
        getNormalizedTextWhiteSpaceOrderedList("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. first item\n"
            + "2. A B C D EF G H \n"
            + " I\n"
            + "3. third item\n"
            + "4. 4. item\n"
            + "some text\n"
            + "5. last item")
    public void getNormalizedTextWhiteSpaceOrderedListPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceOrderedList("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. first item\n"
            + "2. A B C D EF G H \n"
            + " I\n"
            + "3. third item\n"
            + "4. 4. item\n"
            + "some text\n"
            + "5. last item")
    public void getNormalizedTextWhiteSpaceOrderedListPreLine() throws Exception {
        getNormalizedTextWhiteSpaceOrderedList("pre-line");
    }

    private void getNormalizedTextWhiteSpaceOrderedList(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <ol id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "    <li>first item</li>\n"
            + "    <li>  A B  C\t \t  D \nEF\nG \n H  <br> I  </li>\n"
            + "    <li>third item</li><li>4. item</li>\n"
            + "    some text \n"
            + "    <li>last item</li>\n"
            + "  </ol>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\n"
            + "A B C D EF G H \n"
            + " I\n"
            + "third item\n"
            + "4. item\n"
            + "some text\n"
            + "last item")
    public void getNormalizedTextWhiteSpaceUnorderedList() throws Exception {
        getNormalizedTextWhiteSpaceUnorderedList(null);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\n"
            + "A B C D EF G H \n"
            + " I\n"
            + "third item\n"
            + "4. item\n"
            + "some text\n"
            + "last item")
    public void getNormalizedTextWhiteSpaceUnorderedListNormal() throws Exception {
        getNormalizedTextWhiteSpaceUnorderedList("normal");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\n"
            + "A B C D EF G H \n"
            + " I\n"
            + "third item\n"
            + "4. item\n"
            + "some text\n"
            + "last item")
    public void getNormalizedTextWhiteSpaceUnorderedListNowrap() throws Exception {
        getNormalizedTextWhiteSpaceUnorderedList("nowrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\n"
            + "A B C D EF G H \n"
            + " I\n"
            + "third item\n"
            + "4. item\n"
            + "some text\n"
            + "last item")
    public void getNormalizedTextWhiteSpaceUnorderedListPre() throws Exception {
        getNormalizedTextWhiteSpaceUnorderedList("pre");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\n"
            + "A B C D EF G H \n"
            + " I\n"
            + "third item\n"
            + "4. item\n"
            + "some text\n"
            + "last item")
    public void getNormalizedTextWhiteSpaceUnorderedListPreWrap() throws Exception {
        getNormalizedTextWhiteSpaceUnorderedList("pre-wrap");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("first item\n"
            + "A B C D EF G H \n"
            + " I\n"
            + "third item\n"
            + "4. item\n"
            + "some text\n"
            + "last item")
    public void getNormalizedTextWhiteSpaceUnorderedListPreLine() throws Exception {
        getNormalizedTextWhiteSpaceUnorderedList("pre-line");
    }

    private void getNormalizedTextWhiteSpaceUnorderedList(final String whiteSpace) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  <ul id='tester' " + (whiteSpace == null ? "" : ("style='white-space: " + whiteSpace + "'")) + ">\n"
            + "    <li>first item</li>\n"
            + "    <li>  A B  C\t \t  D \nEF\nG \n H  <br> I  </li>\n"
            + "    <li>third item</li><li>4. item</li>\n"
            + "    some text \n"
            + "    <li>last item</li>\n"
            + "  </ul>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getNormalizedTextLabel() throws Exception {
        getNormalizedTextFormated("<label id='tester'>The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getNormalizedTextLabelNormal() throws Exception {
        getNormalizedTextFormated("<label id='tester' style='white-space: normal'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getNormalizedTextLabelNowrap() throws Exception {
        getNormalizedTextFormated("<label id='tester' style='white-space: nowrap'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getNormalizedTextLabelPre() throws Exception {
        getNormalizedTextFormated("<label id='tester' style='white-space: pre'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getNormalizedTextLabelPreWrap() throws Exception {
        getNormalizedTextFormated("<label id='tester' style='white-space: pre-wrap'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The text to be tested")
    public void getNormalizedTextLabelPreLine() throws Exception {
        getNormalizedTextFormated("<label id='tester' style='white-space: pre-line'>"
                + "The text to be <span>tested</span></label>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getNormalizedTextParagraphNbsp() throws Exception {
        getNormalizedTextFormated("<p id='tester'>A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getNormalizedTextParagraphNbspNormal() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: normal'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getNormalizedTextParagraphNbspNowrap() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: nowrap'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getNormalizedTextParagraphNbspPre() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: pre'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getNormalizedTextParagraphNbspPreWrap() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: pre-wrap'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A   nbsp and spaces")
    public void getNormalizedTextParagraphNbspPreLine() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: pre-line'>"
                + "A &nbsp; nbsp and spaces</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n"
            + "  NBSPs  ")
    public void getNormalizedTextParagraphMultilineNbsp() throws Exception {
        getNormalizedTextFormated("<p id='tester'>A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n"
            + "  NBSPs  ")
    public void getNormalizedTextParagraphMultilineNbspNormal() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: normal'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n"
            + "  NBSPs  ")
    public void getNormalizedTextParagraphMultilineNbspNowrap() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: nowrap'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n"
            + "  NBSPs  ")
    public void getNormalizedTextParagraphMultilineNbspPre() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: pre'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n"
            + "  NBSPs  ")
    public void getNormalizedTextParagraphMultilineNbspPreWrap() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: pre-wrap'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getNormalizedText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A  \n"
            + "  NBSPs  ")
    public void getNormalizedTextParagraphMultilineNbspPreLine() throws Exception {
        getNormalizedTextFormated("<p id='tester' style='white-space: pre-line'>"
                + "A &nbsp<br />&nbsp NBSPs&nbsp;&nbsp;</p>");
    }

    /**
     * Verifies getNormalizedText() for issue #128
     * (https://github.com/HtmlUnit/htmlunit/issues/128).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("I have 2 out of 2 stamps")
    public void getNormalizedTextInputInsideP() throws Exception {
        getNormalizedTextFormated("<p id='tester'>"
                + " I have <input type='number' value='2'/> out of 2 stamps</p>");
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Sum",
            IE = "Sum\ndetail")
    public void getNormalizedTextDetails() throws Exception {
        getNormalizedTextFormated("<details id='tester'>"
                + "<summary>Sum</summary>"
                + "<p>detail</p>"
                + "</details>");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Sum\nSum2",
            IE = "SumSum2\ndetail")
    public void getNormalizedTextDetailsTwoSums() throws Exception {
        getNormalizedTextFormated("<details id='tester'>"
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
    public void getNormalizedTextDetailsOpen() throws Exception {
        getNormalizedTextFormated("<details id='tester' open=true>"
                + "<summary>Sum</summary>"
                + "<p>detail</p>"
                + "</details>");
    }

    private void getNormalizedTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final String text = page.getBody().asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("01 Jan 2021")
    public void asTextInsideSpan() throws Exception {
        final String html = "<html><body><form>\n"
            + "<span id='test'>\n"
            + "  <select name='day' size='1'>"
                    + "<option value='1' selected>01</option>"
                    + "<option value='2'>02</option></select>\n"
            + "  <select name='month' size='1'>"
                    + "<option value='1' selected>Jan</option>"
                    + "<option value='2'>Feb</option></select>\n"
            + "  <select name='year' size='1'>"
                    + "<option value='1'>2022</option>"
                    + "<option value='2' selected>2021</option></select>\n"
            + "</span>"
            + "</form></body></html>";
        final HtmlPage page = loadPage(html);
        final DomElement elem = page.getElementById("test");
        assertEquals(getExpectedAlerts()[0], elem.asNormalizedText());
    }

    /**
     * Tests getTableCell(int,int).
     * @exception Exception If the test fails
     */
    @Test
    public void getCellAt() throws Exception {
        final String htmlContent
            = "<html>"
            + "<body>\n"
            + "<table id='table1' summary='Test table'>\n"
            + "<tr>"
                + "<td><a>cell1a</a><div><span>cell1b</span></div></td>"
                + "<td>cell2</td><td rowspan='2'>cell4</td>"
            + "</tr>\n"
            + "<tr>"
                + "<td colspan='2'>cell3</td>"
            + "</tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final HtmlTableCell cell1 = table.getCellAt(0, 0);
        // assertEquals("cell1 contents", "cell1acell1b", cell1.asNormalizedText());
        assertEquals("cell1 contents", "cell1a\ncell1b", cell1.asNormalizedText());

        final HtmlTableCell cell2 = table.getCellAt(0, 1);
        assertEquals("cell2 contents", "cell2", cell2.asNormalizedText());

        final HtmlTableCell cell3 = table.getCellAt(1, 0);
        assertEquals("cell3 contents", "cell3", cell3.asNormalizedText());
        assertSame("cells (1,0) and (1,1)", cell3, table.getCellAt(1, 1));

        final HtmlTableCell cell4 = table.getCellAt(0, 2);
        assertEquals("cell4 contents", "cell4", cell4.asNormalizedText());
        assertSame("cells (0,2) and (1,2)", cell4, table.getCellAt(1, 2));
    }

    /**
     * Tests getTableCell(int,int).
     * @exception Exception If the test fails
     */
    @Test
    public void getCellAtWithBreaks() throws Exception {
        final String htmlContent
            = "<html>"
            + "<body>\n"
            + "<table id='table1' summary='Test table'>\n"
            + "<tr>"
                + "<td><a>cell1a</a><div><br><span>cell1b</span></div></td>"
                + "<td>cell2</td><td rowspan='2'>cell4</td>"
            + "</tr>\n"
            + "<tr>"
                + "<td colspan='2'>cell3</td>"
            + "</tr>\n"
            + "</table>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlTable table = page.getHtmlElementById("table1");

        final HtmlTableCell cell1 = table.getCellAt(0, 0);
        // assertEquals("cell1 contents", "cell1a\ncell1b", cell1.asNormalizedText());
        assertEquals("cell1 contents", "cell1a\n\ncell1b", cell1.asNormalizedText());

        final HtmlTableCell cell2 = table.getCellAt(0, 1);
        assertEquals("cell2 contents", "cell2", cell2.asNormalizedText());

        final HtmlTableCell cell3 = table.getCellAt(1, 0);
        assertEquals("cell3 contents", "cell3", cell3.asNormalizedText());
        assertSame("cells (1,0) and (1,1)", cell3, table.getCellAt(1, 1));

        final HtmlTableCell cell4 = table.getCellAt(0, 2);
        assertEquals("cell4 contents", "cell4", cell4.asNormalizedText());
        assertSame("cells (0,2) and (1,2)", cell4, table.getCellAt(1, 2));
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x 12 y")
    public void getNormalizedNumberInputValidNumber() throws Exception {
        getNormalizedTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "12");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "x y",
            FF = "x ab y",
            FF_ESR = "x ab y",
            IE = "x ab y")
    public void getNormalizedNumberInputInvalidNumber() throws Exception {
        getNormalizedTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "ab");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x - y")
    public void getNormalizedNumberInputMinusOnly() throws Exception {
        getNormalizedTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "-");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x + y")
    public void getNormalizedNumberInputPlusOnly() throws Exception {
        getNormalizedTextFormatedAfterTyping("<p id='tester'>x<input id='inpt' type='number' value=''/>y</p>", "+");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("demo")
    public void getVisibleEm() throws Exception {
        getNormalizedTextFormated("<em id='tester'>demo</em>");
    }

    private void getNormalizedTextFormatedAfterTyping(final String htmlTesterSnipped,
                        final String... typed) throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlNumberInput input = page.getHtmlElementById("inpt");
        for (final String string : typed) {
            input.type(string);
        }

        final HtmlElement tester = page.getHtmlElementById("tester");
        assertEquals(getExpectedAlerts()[0], tester.asNormalizedText());
    }
}
