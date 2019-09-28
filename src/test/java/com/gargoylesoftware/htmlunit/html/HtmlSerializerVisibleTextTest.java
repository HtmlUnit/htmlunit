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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlSerializerVisibleText.HtmlSerializerTextBuilder;
import com.gargoylesoftware.htmlunit.html.HtmlSerializerVisibleText.HtmlSerializerTextBuilder.Mode;

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
        serializer.append("", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" \t\r\n ", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendBlockSeparator();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" a  ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" a  ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("  x ", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("  ", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendNewLine();
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendNewLine();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendNewLine();
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendNewLine();
        serializer.append(" ", Mode.NORMALIZE);
        assertEquals("\n", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.appendNewLine();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("x", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendNewLine();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\nx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendTab();
        serializer.append(" ", Mode.NORMALIZE);
        serializer.appendTab();
        serializer.append("x", Mode.NORMALIZE);
        assertEquals("a\t \tx", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
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

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("abc", Mode.NORMALIZE);
        assertEquals("abc", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a     b \t\t\t c \r \r o \n\n\n", Mode.NORMALIZE);
        assertEquals("a b c o", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void normalizeNbsp() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("abc" + (char) 160 + "x", Mode.NORMALIZE);
        assertEquals("abc x", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append((char) 160 + "x" + (char) 160, Mode.NORMALIZE);
        assertEquals(" x ", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
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
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("a", Mode.NORMALIZE);
        serializer.appendBlockSeparator();
        serializer.appendBlockSeparator();
        assertEquals("a", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
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
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  hello \t abc ", Mode.NORMALIZE_PRE);
        assertEquals("  hello   abc ", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder}.
     */
    @Test
    public void textArea() {
        final HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  hello \t abc ", Mode.NORMALIZE_PRE);
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
        serializer.append(text, Mode.NORMALIZE);
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
            serializer.append(" x ", Mode.NORMALIZE);
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
        serializer.append("\u3000", Mode.NORMALIZE);
        assertEquals("\u3000", serializer.getText());

        // real IE treats this as space, will not consider this for performance reasons
        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("\uFEFF", Mode.NORMALIZE);
        assertEquals("\uFEFF", serializer.getText());

        serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("\u200B", Mode.NORMALIZE);
        assertEquals("\u200B", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     */
    @Test
    public void normalizePre() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  A B  C\t \t  D \r\nEF\nG \n H  ", Mode.NORMALIZE_PRE);
        assertEquals("  A B  C     D \nEF\nG \n H  ", serializer.getText());
    }

    /**
     * Test {@link HtmlSerializerTextBuilder} special spaces.
     */
    @Test
    public void normalizePreLine() {
        HtmlSerializerTextBuilder serializer = new HtmlSerializerVisibleText.HtmlSerializerTextBuilder();
        serializer.append("  A B  C\t \t  D \r\nEF\nG \n H  ", Mode.NORMALIZE_PRE_LINE);
        assertEquals("A B C D \nEF\nG \n H", serializer.getText());
    }
}
