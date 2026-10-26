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
package org.htmlunit.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ContentDispositionParser}.
 *
 * @author Ronald Brill
 */
public class ContentDispositionParserTest {

    /**
     * Null header.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void nullHeader() throws Exception {
        assertNull(ContentDispositionParser.parse(null));
    }

    /**
     * Empty header.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void emptyHeader() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("");
        assertEquals("", cd.getType());
        assertFalse(cd.isAttachment());
        assertTrue(cd.getParameters().isEmpty());
        assertNull(cd.getFilename());
    }

    /**
     * Type only.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void typeOnly() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment");
        assertEquals("attachment", cd.getType());
        assertTrue(cd.isAttachment());
        assertFalse(cd.isInline());
        assertNull(cd.getFilename());
    }

    /**
     * Type is case insensitive and trimmed.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void typeIsCaseInsensitiveAndTrimmed() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("  INLINE ; filename=a.txt");
        assertEquals("inline", cd.getType());
        assertTrue(cd.isInline());
        assertEquals("a.txt", cd.getFilename());
    }

    /**
     * Quoted and unquoted.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void quotedAndUnquoted() throws Exception {
        assertEquals("a b.txt", ContentDispositionParser.parse("attachment; filename=\"a b.txt\"").getFilename());
        assertEquals("a.txt", ContentDispositionParser.parse("attachment; filename=a.txt").getFilename());
    }

    /**
     * Parameter name is case insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void parameterNameIsCaseInsensitive() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; FileName=a.txt; SIZE=4");
        assertEquals("a.txt", cd.getFilename());
        assertEquals("a.txt", cd.getParameter("FILENAME"));
        assertEquals("4", cd.getParameter("size"));
    }

    /**
     * Whitespace.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void whitespace() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment;filename = \"a.txt\" ;size = 4 ");
        assertEquals("a.txt", cd.getFilename());
        assertEquals("4", cd.getParameter("size"));
    }

    /**
     * Semicolon in quoted value.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void semicolonInQuotedValue() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; filename=\"a;b.txt\"; size=4");
        assertEquals("a;b.txt", cd.getFilename());
        assertEquals("4", cd.getParameter("size"));
    }

    /**
     * Escaped characters in quoted value.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void escapedCharactersInQuotedValue() throws Exception {
        assertEquals("a\"b.txt", ContentDispositionParser.parse("attachment; filename=\"a\\\"b.txt\"").getFilename());
        assertEquals("a\\b.txt", ContentDispositionParser.parse("attachment; filename=\"a\\\\b.txt\"").getFilename());
    }

    /**
     * Unterminated quoted value.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void unterminatedQuotedValue() throws Exception {
        assertEquals("a.txt", ContentDispositionParser.parse("attachment; filename=\"a.txt").getFilename());
    }

    /**
     * Empty and missing value.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void emptyAndMissingValue() throws Exception {
        assertEquals("", ContentDispositionParser.parse("attachment; filename=\"\"").getParameter("filename"));
        assertEquals("", ContentDispositionParser.parse("attachment; filename=").getParameter("filename"));
        assertEquals("", ContentDispositionParser.parse("attachment; filename").getParameter("filename"));
    }

    /**
     * Parameter with prefix.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void parameterWithPrefix() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; xfilename=x.txt; filename=y.txt");
        assertEquals("y.txt", cd.getFilename());
        assertEquals("x.txt", cd.getParameter("xfilename"));
    }

    /**
     * First parameter wins.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void firstParameterWins() throws Exception {
        assertEquals("a.txt", ContentDispositionParser.parse("attachment; filename=a.txt; filename=b.txt").getFilename());
    }

    /**
     * Empty parts.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void emptyParts() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment;; ; =x; filename=a.txt;");
        assertEquals("a.txt", cd.getFilename());
        assertEquals(1, cd.getParameters().size());
    }

    /**
     * Parameters keep order.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void parametersKeepOrder() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; b=1; a=2; c=3");
        assertEquals("[b, a, c]", cd.getParameters().keySet().toString());
    }

    /**
     * Extended utf8.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedUtf8() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; filename*=UTF-8''%e2%82%ac%20rates.txt");
        assertEquals("\u20ac rates.txt", cd.getExtendedParameter("filename"));
        assertEquals("\u20ac rates.txt", cd.getFilename());
        assertNull(cd.getParameter("filename"));
    }

    /**
     * Extended iso88591 with language.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedIso88591WithLanguage() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; filename*=iso-8859-1'en'%A3%20rates.txt");
        assertEquals("\u00a3 rates.txt", cd.getFilename());
    }

    /**
     * Extended wins whatever the order is.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedWinsWhateverTheOrderIs() throws Exception {
        assertEquals("\u00e4.txt", ContentDispositionParser
                .parse("attachment; filename=a.txt; filename*=UTF-8''%C3%A4.txt").getFilename());
        assertEquals("\u00e4.txt", ContentDispositionParser
                .parse("attachment; filename*=UTF-8''%C3%A4.txt; filename=a.txt").getFilename());
    }

    /**
     * Extended unknown charset is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedUnknownCharsetIsIgnored() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; filename*=foo''bar.txt; filename=a.txt");
        assertNull(cd.getExtendedParameter("filename"));
        assertEquals("a.txt", cd.getFilename());
    }

    /**
     * Extended without quotes is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedWithoutQuotesIsIgnored() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; filename*=UTF-8bar.txt; filename=a.txt");
        assertNull(cd.getExtendedParameter("filename"));
        assertEquals("a.txt", cd.getFilename());
    }

    /**
     * Extended broken percent encoding is kept.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedBrokenPercentEncodingIsKept() throws Exception {
        assertEquals("abc%zz.txt", ContentDispositionParser.parse("attachment; filename*=UTF-8''abc%zz.txt").getFilename());
        assertEquals("abc%4", ContentDispositionParser.parse("attachment; filename*=UTF-8''abc%4").getFilename());
        assertEquals("abc%", ContentDispositionParser.parse("attachment; filename*=UTF-8''abc%").getFilename());
    }

    /**
     * Other extended parameter.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void otherExtendedParameter() throws Exception {
        final ContentDisposition cd = ContentDispositionParser.parse("attachment; title*=UTF-8''%C3%A4");
        assertEquals("\u00e4", cd.getExtendedParameter("TITLE"));
        assertNull(cd.getParameter("title"));
    }

    /**
     * Value is not sanitized.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void valueIsNotSanitized() throws Exception {
        assertEquals("../../a:b.txt", ContentDispositionParser.parse("attachment; filename=\"../../a:b.txt\"").getFilename());
    }
}
