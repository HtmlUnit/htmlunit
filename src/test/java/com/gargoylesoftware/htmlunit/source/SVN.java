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
package com.gargoylesoftware.htmlunit.source;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

/**
 * Subversion utilities.
 *
 * @author Ahmed Ashour
 */
public final class SVN {

    /**
     * List of extensions for files which should have SVN eol-style property with {@code native} value.
     */
    private static List<String> EOL_EXTENSIONS_
        = Arrays.asList(".html", ".htm", ".js", ".css", ".xml", ".txt", ".properties", "*.php",
                "*.ini", "*.sh", "*.bat", "*.log", "*.java");

    private SVN() { }

    /**
     * List of extensions for files which should have SVN eol-style property with {@code native} value.
     * @return the extensions list
     */
    public static List<String> getEolExtenstions() {
        return EOL_EXTENSIONS_;
    }

    /**
     * Recursively deletes any '.svn' folder which contains Subversion information.
     * @param dir the directory to recursively delete '.svn' from
     * @throws IOException if an exception happens
     */
    public static void deleteSVN(final File dir) throws IOException {
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (".svn".equals(f.getName())) {
                    FileUtils.deleteDirectory(f);
                }
                else {
                    deleteSVN(f);
                }
            }
        }
    }

    /**
     * Ensures that all files inside the specified directory has consistent new lines.
     * @param dir the directory to recursively ensure all contained files have consistent new lines
     * @throws IOException if an exception happens
     */
    public static void consistentNewlines(final File dir) throws IOException {
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (!".svn".equals(f.getName())) {
                    consistentNewlines(f);
                }
            }
            else {
                final String fileName = f.getName().toLowerCase(Locale.ROOT);
                for (final String extension : EOL_EXTENSIONS_) {
                    if (fileName.endsWith(extension)) {
                        FileUtils.writeLines(f, FileUtils.readLines(f));
                        break;
                    }
                }
            }
        }
    }
}
