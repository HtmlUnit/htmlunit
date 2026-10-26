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
package org.htmlunit.attachment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Attachment#getSuggestedFilename(String)}.
 * The expectations are the file names real browsers (Chrome, Edge) save, see
 * {@code ContentDispositionDownloadTest}.
 *
 * @author Ronald Brill
 */
public class AttachmentSuggestedFilenameTest {

    /**
     * Quoted.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void quoted() throws Exception {
        assertEquals("foo.txt", Attachment.getSuggestedFilename("attachment; filename=\"foo.txt\""));
    }

    /**
     * Unquoted.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void unquoted() throws Exception {
        assertEquals("foo.txt", Attachment.getSuggestedFilename("attachment; filename=foo.txt"));
    }

    /**
     * Upper case parameter name.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void upperCaseParameterName() throws Exception {
        assertEquals("foo.txt", Attachment.getSuggestedFilename("attachment; FILENAME=\"foo.txt\""));
    }

    /**
     * Upper case disposition type.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void upperCaseDispositionType() throws Exception {
        assertEquals("foo.txt", Attachment.getSuggestedFilename("ATTACHMENT; filename=\"foo.txt\""));
    }

    /**
     * Whitespace around equals.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void whitespaceAroundEquals() throws Exception {
        assertEquals("foo.txt", Attachment.getSuggestedFilename("attachment; filename = \"foo.txt\""));
    }

    /**
     * Whitespace before semicolon.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void whitespaceBeforeSemicolon() throws Exception {
        assertEquals("foo.txt", Attachment.getSuggestedFilename("attachment; filename=foo.txt ; size=4"));
    }

    /**
     * Other parameters.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void otherParameters() throws Exception {
        assertEquals("foo.txt", Attachment.getSuggestedFilename("attachment; size=4; filename=\"foo.txt\"; creation-date=\"Wed, 12 Feb 1997 16:29:51 -0500\""));
    }

    /**
     * Semicolon in quoted value.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void semicolonInQuotedValue() throws Exception {
        assertEquals("a;b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a;b.txt\"; size=4"));
    }

    /**
     * Parameter name with prefix.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void parameterNameWithPrefix() throws Exception {
        assertEquals("y.txt", Attachment.getSuggestedFilename("attachment; xfilename=\"x.txt\"; filename=y.txt"));
    }

    /**
     * Escaped quote.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void escapedQuote() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a\\\"b.txt\""));
    }

    /**
     * Path traversal.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void pathTraversal() throws Exception {
        assertEquals("_.._evil.txt", Attachment.getSuggestedFilename("attachment; filename=\"../../evil.txt\""));
    }

    /**
     * Absolute path.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void absolutePath() throws Exception {
        assertEquals("_etc_evil.txt", Attachment.getSuggestedFilename("attachment; filename=\"/etc/evil.txt\""));
    }

    /**
     * Backslash path.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void backslashPath() throws Exception {
        assertEquals("C__dir_f.txt", Attachment.getSuggestedFilename("attachment; filename=\"C:\\\\dir\\\\f.txt\""));
    }

    /**
     * Leading and trailing dots.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void leadingAndTrailingDots() throws Exception {
        assertEquals("hidden", Attachment.getSuggestedFilename("attachment; filename=\" .hidden. \""));
    }

    /**
     * Control character.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void controlCharacter() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a\tb.txt\""));
    }

    /**
     * Extended utf8.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedUtf8() throws Exception {
        assertEquals("\u20ac rates.txt", Attachment.getSuggestedFilename("attachment; filename*=UTF-8''%e2%82%ac%20rates.txt"));
    }

    /**
     * Extended iso88591.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedIso88591() throws Exception {
        assertEquals("\u00a3 rates.txt", Attachment.getSuggestedFilename("attachment; filename*=iso-8859-1'en'%A3%20rates.txt"));
    }

    /**
     * Extended wins.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedWins() throws Exception {
        assertEquals("gr\u00f6\u00dfe.txt", Attachment.getSuggestedFilename("attachment; filename=\"fallback.txt\"; filename*=UTF-8''gr%C3%B6%C3%9Fe.txt"));
    }

    /**
     * Extended wins first.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedWinsFirst() throws Exception {
        assertEquals("gr\u00f6\u00dfe.txt", Attachment.getSuggestedFilename("attachment; filename*=UTF-8''gr%C3%B6%C3%9Fe.txt; filename=\"fallback.txt\""));
    }

    /**
     * Extended unknown charset.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedUnknownCharset() throws Exception {
        assertEquals("fallback.txt", Attachment.getSuggestedFilename("attachment; filename*=foo''bar.txt; filename=\"fallback.txt\""));
    }

    /**
     * Extended invalid encoding.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedInvalidEncoding() throws Exception {
        assertEquals("fallback.txt", Attachment.getSuggestedFilename("attachment; filename*=UTF-8''abc%zz.txt; filename=\"fallback.txt\""));
    }

    /**
     * Extended path traversal.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedPathTraversal() throws Exception {
        assertEquals("_.._evil.txt", Attachment.getSuggestedFilename("attachment; filename*=UTF-8''..%2F..%2Fevil.txt"));
    }

    /**
     * Extended illegal char.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void extendedIllegalChar() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename*=UTF-8''a%3Ab.txt"));
    }

    /**
     * Illegal char colon.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharColon() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a:b.txt\""));
    }

    /**
     * Illegal char asterisk.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharAsterisk() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a*b.txt\""));
    }

    /**
     * Illegal char question mark.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharQuestionMark() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a?b.txt\""));
    }

    /**
     * Illegal char less than.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharLessThan() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a<b.txt\""));
    }

    /**
     * Illegal char greater than.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharGreaterThan() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a>b.txt\""));
    }

    /**
     * Illegal char pipe.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void illegalCharPipe() throws Exception {
        assertEquals("a_b.txt", Attachment.getSuggestedFilename("attachment; filename=\"a|b.txt\""));
    }

    /**
     * No filename.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noFilename() throws Exception {
        assertNull(Attachment.getSuggestedFilename("attachment"));
    }

    /**
     * Empty filename.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void emptyFilename() throws Exception {
        assertNull(Attachment.getSuggestedFilename("attachment; filename=\"\""));
    }

    /**
     * Filename without value.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void filenameWithoutValue() throws Exception {
        assertNull(Attachment.getSuggestedFilename("attachment; filename="));
    }

    /**
     * Only dots.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void onlyDots() throws Exception {
        assertNull(Attachment.getSuggestedFilename("attachment; filename=\"...\""));
    }

    /**
     * No header.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noHeader() throws Exception {
        assertNull(Attachment.getSuggestedFilename(null));
    }
}
