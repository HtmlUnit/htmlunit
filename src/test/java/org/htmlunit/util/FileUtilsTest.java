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
package org.htmlunit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link FileUtils}.
 *
 * @author Ronald Brill
 */
public class FileUtilsTest {

    /**
     * Valid names are returned unchanged.
     */
    @Test
    public void sanitizeFilenameValid() {
        assertEquals("test.txt", FileUtils.sanitizeFilename("test.txt"));
        assertEquals("a", FileUtils.sanitizeFilename("a"));
        assertEquals("noext", FileUtils.sanitizeFilename("noext"));
        assertEquals("archive.tar.gz", FileUtils.sanitizeFilename("archive.tar.gz"));
        assertEquals("my file (1).txt", FileUtils.sanitizeFilename("my file (1).txt"));
        assertEquals("a-b_c+d=e,f;g'h&i!j#k$l%m@n~o[p]q{r}s.txt",
                FileUtils.sanitizeFilename("a-b_c+d=e,f;g'h&i!j#k$l%m@n~o[p]q{r}s.txt"));
        assertEquals("a..b.txt", FileUtils.sanitizeFilename("a..b.txt"));
    }

    /**
     * Non ASCII characters are valid.
     */
    @Test
    public void sanitizeFilenameUnicode() {
        assertEquals("gr\u00f6\u00dfe.txt", FileUtils.sanitizeFilename("gr\u00f6\u00dfe.txt"));
        assertEquals("\u65e5\u672c\u8a9e.pdf", FileUtils.sanitizeFilename("\u65e5\u672c\u8a9e.pdf"));
        assertEquals("\u20ac.txt", FileUtils.sanitizeFilename("\u20ac.txt"));
        // surrogate pair
        assertEquals("\ud83d\ude00.png", FileUtils.sanitizeFilename("\ud83d\ude00.png"));
        // C1 control characters are not replaced
        assertEquals("a\u0085b", FileUtils.sanitizeFilename("a\u0085b"));
    }

    /**
     * Null and names where nothing is left.
     */
    @Test
    public void sanitizeFilenameNothingLeft() {
        assertNull(FileUtils.sanitizeFilename(null));
        assertNull(FileUtils.sanitizeFilename(""));
        assertNull(FileUtils.sanitizeFilename(" "));
        assertNull(FileUtils.sanitizeFilename("   "));
        assertNull(FileUtils.sanitizeFilename("."));
        assertNull(FileUtils.sanitizeFilename(".."));
        assertNull(FileUtils.sanitizeFilename("..."));
        assertNull(FileUtils.sanitizeFilename(" . . "));
        assertNull(FileUtils.sanitizeFilename("\u3000"));
    }

    /**
     * Every invalid character is replaced by an underscore.
     */
    @Test
    public void sanitizeFilenameInvalidChars() {
        assertEquals("a_b", FileUtils.sanitizeFilename("a\\b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a/b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a:b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a*b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a?b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a\"b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a<b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a>b"));
        assertEquals("a_b", FileUtils.sanitizeFilename("a|b"));

        assertEquals("_________", FileUtils.sanitizeFilename("\\/:*?\"<>|"));
        assertEquals("a__b", FileUtils.sanitizeFilename("a//b"));
        assertEquals("_", FileUtils.sanitizeFilename("/"));
    }

    /**
     * Control characters and DEL are replaced by an underscore.
     */
    @Test
    public void sanitizeFilenameControlChars() {
        for (char c = 0; c < ' '; c++) {
            assertEquals("a_b", FileUtils.sanitizeFilename("a" + c + "b"), "char " + (int) c);
        }
        assertEquals("a_b", FileUtils.sanitizeFilename("a\u007fb"));

        // the replacement happens first; white space control chars are not trimmed
        assertEquals("_test.txt_", FileUtils.sanitizeFilename("\ttest.txt\n"));
        assertEquals("test.txt__", FileUtils.sanitizeFilename("test.txt\r\n"));
        assertEquals("_", FileUtils.sanitizeFilename("\u0000"));
    }

    /**
     * Leading dots and white space are removed (no hidden files).
     */
    @Test
    public void sanitizeFilenameLeading() {
        assertEquals("htaccess", FileUtils.sanitizeFilename(".htaccess"));
        assertEquals("hidden", FileUtils.sanitizeFilename("...hidden"));
        assertEquals("test.txt", FileUtils.sanitizeFilename("  test.txt"));
        assertEquals("test.txt", FileUtils.sanitizeFilename(" . .test.txt"));
        assertEquals("test.txt", FileUtils.sanitizeFilename("\u2003test.txt"));
        assertEquals("test.txt", FileUtils.sanitizeFilename("\u3000test.txt"));

        // nbsp is not white space for Java
        assertEquals("\u00a0test.txt", FileUtils.sanitizeFilename("\u00a0test.txt"));
    }

    /**
     * Trailing dots and white space are removed.
     */
    @Test
    public void sanitizeFilenameTrailing() {
        assertEquals("test", FileUtils.sanitizeFilename("test."));
        assertEquals("test", FileUtils.sanitizeFilename("test..."));
        assertEquals("test.txt", FileUtils.sanitizeFilename("test.txt  "));
        assertEquals("test.txt", FileUtils.sanitizeFilename("test.txt . . "));
        assertEquals("test.txt", FileUtils.sanitizeFilename("test.txt\u2003"));
    }

    /**
     * Leading and trailing at the same time; inner dots and white space are kept.
     */
    @Test
    public void sanitizeFilenameBoth() {
        assertEquals("test.txt", FileUtils.sanitizeFilename(" .test.txt. "));
        assertEquals("a . b", FileUtils.sanitizeFilename(". a . b ."));
        assertEquals("my  file.txt", FileUtils.sanitizeFilename("  my  file.txt  "));
    }

    /**
     * Paths are flattened to a plain file name.
     */
    @Test
    public void sanitizeFilenamePaths() {
        assertEquals("_.._evil.txt", FileUtils.sanitizeFilename("../../evil.txt"));
        assertEquals("_.._evil.txt", FileUtils.sanitizeFilename("..\\..\\evil.txt"));
        assertEquals("_evil.txt", FileUtils.sanitizeFilename("./evil.txt"));
        assertEquals("_etc_passwd", FileUtils.sanitizeFilename("/etc/passwd"));
        assertEquals("C__Windows_win.ini", FileUtils.sanitizeFilename("C:\\Windows\\win.ini"));
        assertEquals("__server_share_x.txt", FileUtils.sanitizeFilename("\\\\server\\share\\x.txt"));
        assertEquals("dir_", FileUtils.sanitizeFilename("dir/"));
        assertEquals("_", FileUtils.sanitizeFilename("../"));
        assertEquals("_", FileUtils.sanitizeFilename("..\\"));
        assertEquals("file.txt_stream", FileUtils.sanitizeFilename("file.txt:stream"));
        assertEquals("_", FileUtils.sanitizeFilename("../.."));
    }

    /**
     * The result can always be resolved against a folder without leaving it.
     * @param folder the folder
     */
    @Test
    public void sanitizeFilenameStaysInsideFolder(@TempDir final Path folder) {
        final String[] names = {"../../evil.txt", "..\\..\\evil.txt", "/etc/passwd", "C:\\x.txt",
            "\\\\server\\share\\x.txt", "a/../../b", "..", "../..", ". ./..", "\u0000/x", "~/x"};

        for (final String name : names) {
            final String sanitized = FileUtils.sanitizeFilename(name);
            if (sanitized != null) {
                final Path resolved = folder.resolve(sanitized).normalize();
                assertEquals(folder, resolved.getParent(), name);
            }
        }
    }

    /**
     * Names reserved by Windows are not handled.
     */
    @Test
    public void sanitizeFilenameReservedNames() {
        assertEquals("CON", FileUtils.sanitizeFilename("CON"));
        assertEquals("nul.txt", FileUtils.sanitizeFilename("nul.txt"));
    }

    /**
     * Long names are not shortened.
     */
    @Test
    public void sanitizeFilenameLong() {
        final String name = "a".repeat(1000) + ".txt";
        assertEquals(name, FileUtils.sanitizeFilename(name));
    }
}
