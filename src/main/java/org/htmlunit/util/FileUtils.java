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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * File utility class for functions not covered by third-party libraries.
 *
 * @author Ronald Brill
 */
public final class FileUtils {

    /** Characters not allowed in file names on at least one platform (control characters aside). */
    private static final String INVALID_FILENAME_CHARS = "\\/:*?\"<>|";

    /** The posix permissions of a directory accessible for the owner only ({@code rwx------}). */
    public static final FileAttribute<Set<PosixFilePermission>> USER_ONLY_DIRECTORY =
            PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"));

    /** The posix permissions of a file readable and writable for the owner only ({@code rw-------}). */
    public static final FileAttribute<Set<PosixFilePermission>> USER_ONLY_FILE =
            PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------"));

    /**
     * Disallow instantiation of this class.
     */
    private FileUtils() {
        // Empty.
    }

    /**
     * Makes the given name usable as the name of a file, the way the browsers do it
     * for downloaded files.
     * <ul>
     * <li>Characters that are not allowed in file names on at least one platform
     * ({@code \ / : * ? " < > |}), the control characters (U+0000 - U+001F) and DEL (U+007F)
     * are replaced by an underscore. Because the path separators are replaced, the result is always
     * a plain file name; e.g. {@code ../../evil.txt} becomes {@code _.._evil.txt}, so the name
     * can be resolved against a folder without leaving it.</li>
     * <li>Leading dots and white space (no hidden files) and trailing dots and white space
     * are removed.</li>
     * </ul>
     * <p>The name is not checked any further; e.g. names reserved by Windows (like {@code CON} or
     * {@code NUL}) and the maximum length of a file name are not taken into account.</p>
     *
     * @param name the name to sanitize, can be {@code null}
     * @return the sanitized name or {@code null} if the name is {@code null} or nothing is left
     */
    public static String sanitizeFilename(final String name) {
        if (name == null) {
            return null;
        }

        final StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (c < ' ' || c == 127 || INVALID_FILENAME_CHARS.indexOf(c) >= 0) {
                sb.append('_');
            }
            else {
                sb.append(c);
            }
        }

        int start = 0;
        int end = sb.length();
        while (start < end && isTrimmed(sb.charAt(start))) {
            start++;
        }
        while (end > start && isTrimmed(sb.charAt(end - 1))) {
            end--;
        }
        return start == end ? null : sb.substring(start, end);
    }

    private static boolean isTrimmed(final char c) {
        return c == '.' || Character.isWhitespace(c);
    }

    /**
     * Creates the given directory and all the nonexistent parent directories; the directories
     * created by this method are accessible for the owner only ({@code rwx------}). This is
     * relevant for directories inside the shared temp directory, where the default permissions
     * (taken from the umask) usually allow every other user of the machine to read the content.
     * <p>The directory itself must not exist (otherwise a {@link FileAlreadyExistsException}
     * is thrown); a directory with the same name created by someone else (e.g. another user of the
     * shared temp directory) is never used. Existing parent directories are used as they are.</p>
     * <p>On file systems without posix support the permissions are not set, the directories are
     * created with the defaults of the platform.</p>
     * <p>On non-POSIX file systems (e.g. Windows) no permissions are set; the file inherits the
     * access rules of its parent folder. The default temp folder on Windows is already
     * restricted to the current user.</p>
     *
     * @param directory the directory to create
     * @throws FileAlreadyExistsException if the directory already exists
     * @throws IOException if the directory can't be created
     */
    public static void createUserOnlyDirectory(final Path directory) throws IOException {
        final Path parent = directory.toAbsolutePath().getParent();
        if (isPosix(directory)) {
            if (parent != null) {
                Files.createDirectories(parent, USER_ONLY_DIRECTORY);
            }
            Files.createDirectory(directory, USER_ONLY_DIRECTORY);
            return;
        }

        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.createDirectory(directory);
    }

    /**
     * Creates the given file (it must not exist) and opens it for writing; the file is
     * readable for the owner only ({@code rw-------}). The file is created with these permissions
     * (no window where it is readable for others) and an existing file or link with the same name
     * is never used.
     * <p>On file systems without posix support the permissions are not set, the file is
     * created with the defaults of the platform.</p>
     * <p>On non-POSIX file systems (e.g. Windows) no permissions are set; the file inherits the
     * access rules of its parent folder. The default temp folder on Windows is already
     * restricted to the current user.</p>
     *
     * @param file the file to create
     * @return the output stream to write the content
     * @throws FileAlreadyExistsException if the file already exists
     * @throws IOException if the file can't be created
     */
    public static OutputStream newUserOnlyOutputStream(final Path file) throws IOException {
        final Set<OpenOption> options = Set.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        if (isPosix(file)) {
            return Channels.newOutputStream(Files.newByteChannel(file, options, USER_ONLY_FILE));
        }
        return Channels.newOutputStream(Files.newByteChannel(file, options));
    }

    private static boolean isPosix(final Path path) {
        return path.getFileSystem().supportedFileAttributeViews().contains("posix");
    }
}
