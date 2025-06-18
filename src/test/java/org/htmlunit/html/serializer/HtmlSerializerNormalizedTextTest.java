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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.serializer.HtmlSerializerNormalizedText.HtmlSerializerTextBuilder;
import org.htmlunit.html.serializer.HtmlSerializerNormalizedText.HtmlSerializerTextBuilder.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlSerializerNormalizedText}.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerNormalizedTextTest {

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void normalize() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(" \t\r\n ", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(" a  ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(" a  ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("  x ", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("  ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendNewLine();
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendNewLine();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendNewLine();
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendNewLine();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendNewLine();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("x", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendNewLine();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendTab();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendTab();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\t \tx", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        serializer.append("\n", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("y", Mode.NORMALIZE);
        serializer.appendNewLine();
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        assertEquals("x\ny", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("abc", Mode.NORMALIZE);
        assertEquals("abc", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a     b \t\t\t c \r \r o \n\n\n", Mode.NORMALIZE);
        assertEquals("a b c o", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void normalizeNbsp() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("abc" + (char) 160 + "x", Mode.NORMALIZE);
        assertEquals("abc x", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append((char) 160 + "x" + (char) 160, Mode.NORMALIZE);
        assertEquals(" x ", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        serializer.append((char) 160 + "x" + (char) 160, Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        assertEquals(" x ", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void normalize2() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("  ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void pre() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("  hello \t abc ", Mode.PRESERVE_BLANK_TAB_NEWLINE);
        assertEquals("  hello \t abc ", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void textArea() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("  hello \t abc ", Mode.PRESERVE_BLANK_NEWLINE);
        assertEquals("  hello   abc", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void performanceWhitespace() {
        final int length = 100_000;
        final char[] charArray = new char[length];
        Arrays.fill(charArray, ' ');
        charArray[0] = 'a';
        charArray[length - 1] = 'a';
        final String text = new String(charArray);

        final long time = System.currentTimeMillis();
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append(text, Mode.NORMALIZE);
        serializer.getText();

        final long runTime = System.currentTimeMillis() - time;
        Assertions.assertTrue(runTime < 200, "cleanUp() took too much time");
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void performanceManyReplaces() {
        final String expected = StringUtils.repeat("x\n", 100_000).trim();

        final long time = System.currentTimeMillis();

        final HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();

        for (int i = 0; i < 100_000; i++) {
            serializer.append(" x ", Mode.NORMALIZE);
            serializer.appendBlockSeparator();
        }

        assertEquals(expected, serializer.getText());

        final long runTime = System.currentTimeMillis() - time;
        Assertions.assertTrue(runTime < 200, "cleanUp() took too much time");
    }

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     */
    @Test
    public void specialSpaces() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("\u3000", Mode.NORMALIZE);
        assertEquals("\u3000", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("\uFEFF", Mode.NORMALIZE);
        assertEquals("\uFEFF", serializer.getText());

        serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.append("\u200B", Mode.NORMALIZE);
        assertEquals("\u200B", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     */
    @Test
    public void variousNewLines() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerNormalizedText.HtmlSerializerTextBuilder();
        serializer.appendNewLine();
        serializer.append("\n", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        assertEquals("", serializer.getText());
    }

    /**
     * @throws IOException in case of errors
     */
    @Test
    public void cssEnableDisable1() throws IOException {
        final String html =
                "<div>\r\n"
                  + "<p>p\r\n"
                    + "<br>br\r\n"
                  + "</p>\r\n"
                + "</div>";
        final String expected = "p \nbr";

        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            assertEquals(expected, page.asNormalizedText());

            webClient.getOptions().setCssEnabled(false);
            assertEquals(expected, page.asNormalizedText());
        }
    }

    /**
     * @throws IOException in case of errors
     */
    @Test
    public void cssEnableDisable2() throws IOException {
        final String html =
                "<div>\r\n"
                  + "<p>p\r\n"
                    + "<br>br\r\n"
                  + "</p>\r\n"
                  + "<p>p</p>\r\n"
                + "</div>";
        final String expected = "p \nbr\np";

        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            assertEquals(expected, page.asNormalizedText());

            webClient.getOptions().setCssEnabled(false);
            assertEquals(expected, page.asNormalizedText());
        }
    }

    /**
     * @throws IOException in case of errors
     */
    @Test
    public void cssEnableDisable3() throws IOException {
        final String html =
                "<div>\r\n"
                  + "<p>p\r\n"
                    + "<br>br\r\n"
                  + "</p>\r\n"
                  + "<p>p</p>\r\n"
                  + "<p>p\r\n"
                    + "<br>br\r\n"
                    + "<br>br\r\n"
                    + "<br>br\r\n"
                  + "</p>\r\n"
                  + "<p>p</p>\r\n"
                + "</div>";

        final String expected =
                "p \n"
                + "br\n"
                + "p\n"
                + "p \n"
                + "br \n"
                + "br \n"
                + "br\n"
                + "p";

        try (WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.loadHtmlCodeIntoCurrentWindow(html);

            assertEquals(expected, page.asNormalizedText());

            webClient.getOptions().setCssEnabled(false);
            assertEquals(expected, page.asNormalizedText());
        }
    }
}
