/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

/**
 * Test of coding style for things that can not be detected by Checkstyle.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CodeStyleTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void codeStyle() throws Exception {
        process(new File("src/main/java"));
        process(new File("src/test/java"));
        licenseYear();
        versionYear();
    }

    private void process(final File dir) throws IOException {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !file.getName().equals(".svn")) {
                process(file);
            }
            else {
                if (file.getName().endsWith(".java")) {
                    final List<String> lines = getLines(file);
                    final String relativePath = file.getAbsolutePath().substring(
                        new File(".").getAbsolutePath().length() - 1);
                    openingCurlyBracket(lines, relativePath);
                    year(lines, relativePath);
                    javaDocFirstLine(lines, relativePath);
                    methodFirstLine(lines, relativePath);
                    methodLastLine(lines, relativePath);
                    svnProperties(file, relativePath);
                    runWith(lines, relativePath);
                    twoEmptyLines(lines, relativePath);
                }
            }
        }
    }

    /**
     * Ensures that no opening curly bracket exists by itself in a single line.
     */
    private void openingCurlyBracket(final List<String> lines, final String path) throws IOException {
        int index = 1;
        for (final String line : lines) {
            if (line.trim().equals("{")) {
                fail("Opening curly bracket is alone at " + path + ", line: " + index);
            }
            index++;
        }
    }

    /**
     * Checks the year in the source.
     */
    private void year(final List<String> lines, final String path) throws IOException {
        assertTrue("Incorrect year in " + path, lines.get(1).contains("Copyright (c) 2002-"
            + Calendar.getInstance().get(Calendar.YEAR)));
    }

    /**
     * Checks the JavaDoc first line, it should not be empty.
     */
    private void javaDocFirstLine(final List<String> lines, final String relativePath) throws IOException {
        for (int index = 1; index < lines.size(); index++) {
            final String previousLine = lines.get(index - 1);
            final String currentLine = lines.get(index);
            if (previousLine.trim().equals("/**") && currentLine.trim().equals("*")) {
                fail("Empty line in " + relativePath + ", line: " + (index + 1));
            }
        }
    }

    /**
     * Checks the method first line, it should not be empty.
     */
    private void methodFirstLine(final List<String> lines, final String relativePath) throws IOException {
        for (int index = 0; index < lines.size() - 1; index++) {
            final String line = lines.get(index);
            if (lines.get(index + 1).trim().length() == 0
                && line.length() > 4
                && Character.isWhitespace(line.charAt(0)) && line.endsWith("{")
                && !line.contains(" class ") && !line.contains(" interface ") && !line.contains(" @interface ")
                && (!Character.isWhitespace(line.charAt(4))
                    || line.trim().startsWith("public") || line.trim().startsWith("protected")
                    || line.trim().startsWith("private"))) {
                fail("Empty line in " + relativePath + ", line: " + (index + 2));
            }
        }
    }

    /**
     * Checks the method last line, it should not be empty.
     */
    private void methodLastLine(final List<String> lines, final String relativePath) throws IOException {
        for (int index = 0; index < lines.size() - 1; index++) {
            final String line = lines.get(index);
            final String nextLine = lines.get(index + 1);
            if (line.trim().length() == 0 && nextLine.equals("    }")) {
                fail("Empty line in " + relativePath + ", line: " + (index + 1));
            }
        }
    }

    /**
     * Checks properties svn:eol-style and svn:keywords.
     */
    private void svnProperties(final File file, final String relativePath) throws IOException {
        final File svnBase = new File(file.getParentFile(), ".svn/prop-base/" + file.getName() + ".svn-base");
        final File svnWork = new File(file.getParentFile(), ".svn/props/" + file.getName() + ".svn-work");
        if (!isSvnPropertiesDefined(svnBase) && !isSvnPropertiesDefined(svnWork)) {
            fail("'svn:eol-style' and 'svn:keywords' properties are not defined for " + relativePath);
        }
    }

    private boolean isSvnPropertiesDefined(final File file) throws IOException {
        boolean eolStyleDefined = false;
        boolean keywordsDefined = false;
        if (file.exists()) {
            final List<String> lines = getLines(file);
            for (int i = 0; i + 2 < lines.size(); i++) {
                final String line = lines.get(i);
                final String nextLine = lines.get(i + 2);
                if (line.equals("svn:eol-style") && nextLine.equals("native")) {
                    eolStyleDefined = true;
                }
                else if (line.equals("svn:keywords") && nextLine.equals("Author Date Id Revision")) {
                    keywordsDefined = true;
                }
            }
        }
        return eolStyleDefined && keywordsDefined;
    }

    /**
     * Reads the given file as lines.
     * @param file file to read.
     * @return the list of lines.
     * @throws IOException if an error occurs.
     */
    private static List<String> getLines(final File file) throws IOException {
        final List<String> rv = new ArrayList<String>();
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            rv.add(line);
        }
        reader.close();
        return rv;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void xmlStyle() throws Exception {
        processXML(new File("."), false);
        processXML(new File("src/main/resources"), true);
        processXML(new File("src/assembly"), true);
        processXML(new File("src/changes"), true);
    }

    private void processXML(final File dir, final boolean recursive) throws Exception {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !file.getName().equals(".svn")) {
                if (recursive) {
                    processXML(file, true);
                }
            }
            else {
                if (file.getName().endsWith(".xml")) {
                    final List<String> lines = getLines(file);
                    final String relativePath = file.getAbsolutePath().substring(
                        new File(".").getAbsolutePath().length() - 1);
                    mixedIndentation(lines, relativePath);
                    trailingWhitespace(lines, relativePath);
                }
            }
        }
    }

    /**
     * Verifies that no XML files have mixed indentation (tabs and spaces, mixed).
     */
    private void mixedIndentation(final List<String> lines, final String relativePath) {
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.indexOf('\t') != -1) {
                fail("Mixed indentation in " + relativePath + ", line: " + (i + 1));
            }
        }
    }

    /**
     * Verifies that no XML files have trailing whitespace.
     */
    private void trailingWhitespace(final List<String> lines, final String relativePath) {
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.length() > 0) {
                final char last = line.charAt(line.length() - 1);
                if (Character.isWhitespace(last)) {
                    fail("Trailing whitespace in " + relativePath + ", line: " + (i + 1));
                }
            }
        }
    }

    /**
     * Checks the year in LICENSE.txt.
     */
    private void licenseYear() throws IOException {
        final List<String> lines = getLines(new File("LICENSE.txt"));
        assertTrue("Incorrect year in LICENSE.txt", lines.get(1).contains("Copyright (c) 2002-"
                + Calendar.getInstance().get(Calendar.YEAR)));
    }

    /**
     * Checks the year in the {@link Version}.
     */
    private void versionYear() throws IOException {
        final List<String> lines = getLines(new File("src/main/java/com/gargoylesoftware/htmlunit/Version.java"));
        for (final String line : lines) {
            if (line.contains("return \"Copyright (C) 2002-" + Calendar.getInstance().get(Calendar.YEAR))) {
                return;
            }
        }
        fail("Incorrect year in Version.getCopyright()");
    }
    
    /**
     * Verifies that no direct instantiation of WebClient from a test that runs with BrowserRunner.
     */
    private void runWith(final List<String> lines, final String relativePath) {
        if (relativePath.replace('\\', '/').contains("src/test/java") && !relativePath.contains("CodeStyleTest")) {
            boolean runWith = false;
            for (final String line : lines) {
                if (line.contains("@RunWith(BrowserRunner.class)")) {
                    runWith = true;
                }
                if (runWith && line.contains("new WebClient(")) {
                    fail("Test " + relativePath
                        + " should never directly instantiate WebClient, please use getWebClient() instead.");
                }
            }
        }
    }

    /**
     * Verifies that no two empty contiguous lines.
     */
    private void twoEmptyLines(final List<String> lines, final String relativePath) {
        for (int i = 1; i < lines.size(); i++) {
            final String previousLine = lines.get(i - 1);
            final String line = lines.get(i);
            if (previousLine.trim().length() == 0 && line.trim().length() == 0) {
                fail("Two empty contiguous lines at " + relativePath + ", line: " + (i + 1));
            }
        }
    }

}
