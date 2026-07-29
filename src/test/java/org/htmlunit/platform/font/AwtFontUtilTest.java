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
package org.htmlunit.platform.font;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link AwtFontUtil}.
 *
 * @author Ronald Brill
 */
public class AwtFontUtilTest {

    private final FontUtil fontUtil_ = new AwtFontUtil();

    /**
     * A single short line, wide enough box, should never wrap: exactly one line.
     */
    @Test
    public void countLines_singleShortLine() {
        assertEquals(1, fontUtil_.countLines("Hello", 1000, "12px"));
    }

    /**
     * Empty content should report zero lines.
     */
    @Test
    public void countLines_emptyContent() {
        assertEquals(0, fontUtil_.countLines("", 1000, "12px"));
    }

    /**
     * {@code null} content should not throw and should report zero lines,
     * relying on the null-safety of {@code StringUtils.split(null, ...)}.
     */
    @Test
    public void countLines_nullContent() {
        assertEquals(0, fontUtil_.countLines(null, 1000, "12px"));
    }

    /**
     * Explicit newlines with real text on each line: two lines in, two lines out
     * (given a pixel width wide enough that neither line wraps).
     */
    @Test
    public void countLines_multipleExplicitLines() {
        assertEquals(2, fontUtil_.countLines("Hello\nWorld", 1000, "12px"));
    }

    /**
     * Blank lines (whitespace-only or empty, between explicit newlines) should
     * each count as exactly one line rather than being skipped or merged.
     * This directly probes the interaction between
     * {@code StringUtils.split(content, '\n')} and the blank-line branch.
     */
    @Test
    public void countLines_blankLineBetweenTextLines() {
        assertEquals(3, fontUtil_.countLines("Hello\n\nWorld", 1000, "12px"));
    }

    /**
     * A line consisting only of whitespace should still count as one line.
     */
    @Test
    public void countLines_whitespaceOnlyLine() {
        assertEquals(1, fontUtil_.countLines("   ", 1000, "12px"));
    }

    /**
     * Multiple consecutive blank lines should each be counted individually,
     * not collapsed into fewer lines by the underlying split.
     */
    @Test
    public void countLines_multipleConsecutiveBlankLines() {
        assertEquals(5, fontUtil_.countLines("A\n\n\n\nB", 1000, "12px"));
    }

    /**
     * A trailing newline at the end of the content: verifies whether a
     * trailing empty segment is (or is not) counted as an extra line.
     * Documents current behavior rather than asserting a "correct" answer,
     * since the semantics of a trailing newline are easy to get wrong silently.
     */
    @Test
    public void countLines_trailingNewline() {
        final int lines = fontUtil_.countLines("Hello\n", 1000, "12px");
        assertTrue(lines == 1 || lines == 2,
                "Unexpected line count for trailing newline: " + lines);
    }

    /**
     * A long line with a narrow pixel width should wrap into more than one
     * layout line, exercising the {@code LineBreakMeasurer} branch.
     */
    @Test
    public void countLines_longLineWrapsWithNarrowWidth() {
        final String longLine =
                "The quick brown fox jumps over the lazy dog. ".repeat(10);
        final int wideLines = fontUtil_.countLines(longLine, 5000, "12px");
        final int narrowLines = fontUtil_.countLines(longLine, 50, "12px");

        assertTrue(narrowLines > wideLines,
                "Narrower pixel width should force more line breaks: "
                        + "wide=" + wideLines + " narrow=" + narrowLines);
    }

    /**
     * A single line with no whitespace at all (so {@code LineBreakMeasurer}
     * cannot find a break point) combined with a very narrow width should not
     * loop forever; it must terminate at or before the internal safety cap.
     * Currently that cap is a hardcoded {@code 1000} inside
     * {@link AwtFontUtil#countLines}.
     */
    @Test
    public void countLines_unbreakableLineRespectsSafetyCap() {
        final String unbreakable = "a".repeat(5000);
        final int lines = fontUtil_.countLines(unbreakable, 1, "12px");

        assertTrue(lines <= 1000,
                "countLines should never exceed the internal safety cap, got: " + lines);
    }

    /**
     * Sanity check that a larger font size, at a fixed pixel width, tends to
     * produce at least as many wrapped lines as a smaller font size (larger
     * glyphs need more line breaks to fit the same width). This exercises the
     * {@code fontSizeInt / 1.1} scaling factor indirectly.
     */
    @Test
    public void countLines_largerFontSizeWrapsAtLeastAsMuch() {
        final String longLine =
                "The quick brown fox jumps over the lazy dog. ".repeat(10);
        final int smallFontLines = fontUtil_.countLines(longLine, 300, "10px");
        final int largeFontLines = fontUtil_.countLines(longLine, 300, "30px");

        assertTrue(largeFontLines >= smallFontLines,
                "Larger font size should not produce fewer wrapped lines: "
                        + "small=" + smallFontLines + " large=" + largeFontLines);
    }

    /**
     * Multiple paragraphs, each independently subject to wrapping, should sum
     * their individual line counts.
     */
    @Test
    public void countLines_multipleWrappingParagraphs() {
        final String paragraph = "The quick brown fox jumps over the lazy dog. ".repeat(5);
        final String content = paragraph + "\n" + paragraph;

        final int singleParagraphLines = fontUtil_.countLines(paragraph, 200, "12px");
        final int combinedLines = fontUtil_.countLines(content, 200, "12px");

        assertEquals(singleParagraphLines * 2, combinedLines);
    }
}
