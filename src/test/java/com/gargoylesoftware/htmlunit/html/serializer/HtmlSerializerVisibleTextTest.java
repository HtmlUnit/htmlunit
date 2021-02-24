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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.serializer.HtmlSerializerVisibleText.HtmlSerializerTextBuilder;
import com.gargoylesoftware.htmlunit.html.serializer.HtmlSerializerVisibleText.HtmlSerializerTextBuilder.Mode;

/**
 * Tests for {@link HtmlSerializerVisibleText}.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerVisibleTextTest {

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void normalize() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("", Mode.WHITE_SPACE_NORMAL);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" \t\r\n ", Mode.WHITE_SPACE_NORMAL);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" a  ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" a  ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("  x ", Mode.WHITE_SPACE_NORMAL);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.WHITE_SPACE_NORMAL);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.WHITE_SPACE_NORMAL);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("  ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.WHITE_SPACE_NORMAL);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBreak(Mode.WHITE_SPACE_NORMAL);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBreak(Mode.WHITE_SPACE_NORMAL);
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBreak(Mode.WHITE_SPACE_NORMAL);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBreak(Mode.WHITE_SPACE_NORMAL);
        serializer.append(" ", Mode.WHITE_SPACE_NORMAL);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBreak(Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.WHITE_SPACE_NORMAL);
        assertEquals("x", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBreak(Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.WHITE_SPACE_NORMAL);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.WHITE_SPACE_NORMAL);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        serializer.append("\n", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("y", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBreak(Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        assertEquals("x\ny", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("abc", Mode.WHITE_SPACE_NORMAL);
        assertEquals("abc", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a     b \t\t\t c \r \r o \n\n\n", Mode.WHITE_SPACE_NORMAL);
        assertEquals("a b c o", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void normalizeNbsp() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("abc" + (char) 160 + "x", Mode.WHITE_SPACE_NORMAL);
        assertEquals("abc x", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append((char) 160 + "x" + (char) 160, Mode.WHITE_SPACE_NORMAL);
        assertEquals(" x ", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        serializer.append((char) 160 + "x" + (char) 160, Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        assertEquals(" x ", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void normalize2() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        serializer.append("  ", Mode.WHITE_SPACE_NORMAL);
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void pre() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  hello \t abc ", Mode.WHITE_SPACE_PRE);
        assertEquals("  hello   abc ", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void textArea() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  hello \t abc ", Mode.WHITE_SPACE_PRE);
        assertEquals("  hello   abc ", serializer.getText());
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
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(text, Mode.WHITE_SPACE_NORMAL);
        serializer.getText();

        final long runTime = System.currentTimeMillis() - time;
        assertTrue("cleanUp() took too much time", runTime < 200);
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void performanceManyReplaces() {
        final String expected = StringUtils.repeat("x\n", 100_000).trim();

        final long time = System.currentTimeMillis();

        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();

        for (int i = 0; i < 100_000; i++) {
            serializer.append(" x ", Mode.WHITE_SPACE_NORMAL);
            serializer.appendBlockSeparator();
        }

        assertEquals(expected, serializer.getText());

        final long runTime = System.currentTimeMillis() - time;
        assertTrue("cleanUp() took too much time", runTime < 200);
    }

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     */
    @Test
    public void specialSpaces() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("\u3000", Mode.WHITE_SPACE_NORMAL);
        assertEquals("\u3000", serializer.getText());

        // real IE treats this as space, will not consider this for performance reasons
        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("\uFEFF", Mode.WHITE_SPACE_NORMAL);
        assertEquals("\uFEFF", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("\u200B", Mode.WHITE_SPACE_NORMAL);
        assertEquals("\u200B", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     */
    @Test
    public void normalizePre() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  A B  C\t \t  D \r\nEF\nG \n H  ", Mode.WHITE_SPACE_PRE);
        assertEquals("  A B  C     D \nEF\nG \n H  ", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     */
    @Test
    public void normalizePreLine() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  A B  C\t \t  D \r\nEF\nG \n H  ", Mode.WHITE_SPACE_PRE_LINE);
        assertEquals("A B C D \nEF\nG \n H", serializer.getText());
    }
}
