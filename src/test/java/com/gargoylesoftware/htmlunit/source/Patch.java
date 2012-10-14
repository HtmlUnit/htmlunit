/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Patches utilities.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class Patch {

    private Patch() { }

    /**
     * Checks the @author tag in the files touched by the specified patch.
     *
     * @param baseDir the root folder of HtmlUnit, this can be '.' if you are calling this methods from HtmlUnit code
     * base. If you are calling this method from another project, this specifies HtmlUnit home folder.
     * @param patchPath the path to the patch
     * @param authorName the author name, e.g. "John Smith"
     * @throws IOException if an exception occurs
     */
    public static void checkAuthor(final String baseDir, final String patchPath, final String authorName)
        throws IOException {
        final List<String> errors = new ArrayList<String>();
        final List<String> lines = FileUtils.readLines(new File(patchPath));
        for (final String line : lines) {
            if (line.startsWith("+++")) {
                final String fileName = line.substring(4, line.indexOf('\t', 4));
                if (fileName.endsWith(".java")) {
                    final File file = new File(baseDir, fileName);
                    if (file.exists()) {
                        final List<String> fileLines = FileUtils.readLines(file);
                        boolean authorFound = false;
                        for (final String fileLine : fileLines) {
                            if (fileLine.contains("@author " + authorName)) {
                                authorFound = true;
                                break;
                            }
                        }
                        if (!authorFound) {
                            System.out.println("No \"@author " + authorName + "\" in " + file.getAbsolutePath());
                            errors.add(file.getAbsolutePath());
                        }
                    }
                    else {
                        System.out.println("File does not exist: " + file);
                    }
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new RuntimeException("Total missing files: " + errors.size());
        }
    }

    /**
     * Prints to <tt>System.out</tt> the "String html = ..." string from the actual HTML file.
     * @param htmlFile the path of the HTML file
     * @throws IOException if an exception occurs
     */
    public static void generateHtmlString(final File htmlFile) throws IOException {
        final List<String> lines = FileUtils.readLines(htmlFile);
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i).replace("\t", "    ").replace("\\", "\\\\").replace("\"", "\\\"");
            if (i == 0) {
                System.out.println("        final String html = \"" + line + "\\n\"");
            }
            else {
                System.out.print("            + \"" + line);
                if (i == lines.size() - 1) {
                    System.out.print("\";");
                }
                else {
                    System.out.print("\\n\"");
                }
                System.out.println();
            }
        }
    }
}
