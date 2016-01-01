/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

/**
 * Tests for {@link HtmlSerializer}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlSerializerTest {

    /**
     * Test {@link HtmlSerializer#cleanup(String)}.
     */
    @Test
    public void cleanUp() {
        final String ls = System.getProperty("line.separator");
        final HtmlSerializer serializer = new HtmlSerializer();

        assertEquals("", serializer.cleanUp(""));
        assertEquals("", serializer.cleanUp(" \t\r\n "));

        assertEquals("", serializer.cleanUp(HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR));
        assertEquals("", serializer.cleanUp(HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + " "));
        assertEquals("", serializer.cleanUp(" " + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR));
        assertEquals("", serializer.cleanUp(" " + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + " "));
        assertEquals("a", serializer.cleanUp(" a  " + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR));
        assertEquals("a" + ls + "x", serializer.cleanUp(" a  " + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "  x "));
        assertEquals("a" + ls + "x", serializer.cleanUp("a" + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "x"));
        assertEquals("a" + ls + "x", serializer.cleanUp("a"
                                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR
                                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "x"));
        assertEquals("a" + ls + "x", serializer.cleanUp("a"
                                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "  "
                                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "x"));

        assertEquals(ls, serializer.cleanUp(HtmlSerializer.AS_TEXT_NEW_LINE));
        assertEquals(ls, serializer.cleanUp(HtmlSerializer.AS_TEXT_NEW_LINE + " "));
        assertEquals(ls, serializer.cleanUp(" " + HtmlSerializer.AS_TEXT_NEW_LINE));
        assertEquals(ls, serializer.cleanUp(" " + HtmlSerializer.AS_TEXT_NEW_LINE + " "));

        assertEquals("x", serializer.cleanUp(
                        HtmlSerializer.AS_TEXT_NEW_LINE
                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "x"));
        assertEquals("a" + ls + "x", serializer.cleanUp("a"
                        + HtmlSerializer.AS_TEXT_NEW_LINE
                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "x"));

        assertEquals("a" + ls + "x", serializer.cleanUp("a"
                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR
                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR
                        + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR + "x"));

        assertEquals("a   x", serializer.cleanUp("a" + HtmlSerializer.AS_TEXT_BLANK
                        + " " + HtmlSerializer.AS_TEXT_BLANK + "x"));

        assertEquals("a\t \tx", serializer.cleanUp("a" + HtmlSerializer.AS_TEXT_TAB
                        + " " + HtmlSerializer.AS_TEXT_TAB + "x"));

        assertEquals("abc", serializer.cleanUp("abc"));
        assertEquals("abc x", serializer.cleanUp("abc" + (char) 160 + "x"));
        assertEquals("a b c o", serializer.cleanUp("a     b \t\t\t c \r \r o \n\n\n"));
    }

    /**
     * Test {@link HtmlSerializer#cleanup(String)}.
     */
    @Test
    public void cleanUpPerformanceWhitespace() {
        final HtmlSerializer serializer = new HtmlSerializer();

        final int length = 80_000;
        final char[] charArray = new char[length];
        Arrays.fill(charArray, ' ');
        charArray[0] = 'a';
        charArray[length - 1] = 'a';
        final String text = new String(charArray);

        final long time = System.currentTimeMillis();
        serializer.cleanUp(text);

        final long runTime = System.currentTimeMillis() - time;
        assertTrue("cleanUp() took too much time", runTime < 1_000);
    }

    /**
     * Test {@link HtmlSerializer#cleanup(String)}.
     */
    @Test
    public void cleanUpPerformanceManyReplaces() {
        final HtmlSerializer serializer = new HtmlSerializer();

        final String text = StringUtils.repeat(" x " + HtmlSerializer.AS_TEXT_BLOCK_SEPARATOR, 20_000);

        final long time = System.currentTimeMillis();
        serializer.cleanUp(text);

        final long runTime = System.currentTimeMillis() - time;
        assertTrue("cleanUp() took too much time", runTime < 1_000);
    }
}
