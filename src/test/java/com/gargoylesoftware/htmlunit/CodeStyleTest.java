/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of coding style for things that cannot be detected by Checkstyle.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CodeStyleTest {

    private List<String> errors_;

    /**
     * Before.
     */
    @Before
    public void before() {
        errors_ = new ArrayList<String>();
    }

    /**
     * After.
     */
    @After
    public void after() {
        final StringBuilder sb = new StringBuilder();
        for (final String error : errors_) {
            sb.append("\n" + error);
        }

        final int errorsNumber = errors_.size();
        if (errorsNumber == 1) {
            fail("CodeStyle error: " + sb);
        }
        else if (errorsNumber > 1) {
            fail("CodeStyle " + errorsNumber + " errors: " + sb);
        }
    }

    private void addFailure(final String error) {
        errors_.add(error);
    }

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
    private void openingCurlyBracket(final List<String> lines, final String path) {
        int index = 1;
        for (final String line : lines) {
            if (line.trim().equals("{")) {
                addFailure("Opening curly bracket is alone at " + path + ", line: " + index);
            }
            index++;
        }
    }

    /**
     * Checks the year in the source.
     */
    private void year(final List<String> lines, final String path) {
        assertTrue("Incorrect year in " + path, lines.get(1).contains("Copyright (c) 2002-"
            + Calendar.getInstance().get(Calendar.YEAR)));
    }

    /**
     * Checks the JavaDoc first line, it should not be empty, and should not start with lower-case.
     */
    private void javaDocFirstLine(final List<String> lines, final String relativePath) {
        for (int index = 1; index < lines.size(); index++) {
            final String previousLine = lines.get(index - 1);
            final String currentLine = lines.get(index);
            if (previousLine.trim().equals("/**")) {
                if (currentLine.trim().equals("*") || currentLine.contains("*/")) {
                    addFailure("Empty line in " + relativePath + ", line: " + (index + 1));
                }
                if (currentLine.trim().startsWith("*")) {
                    final String text = currentLine.trim().substring(1).trim();
                    if (text.length() != 0 && Character.isLowerCase(text.charAt(0))) {
                        addFailure("Lower case start in " + relativePath + ", line: " + (index + 1));
                    }
                }
            }
        }
    }

    /**
     * Checks the method first line, it should not be empty.
     */
    private void methodFirstLine(final List<String> lines, final String relativePath) {
        for (int index = 0; index < lines.size() - 1; index++) {
            final String line = lines.get(index);
            if (lines.get(index + 1).trim().length() == 0
                && line.length() > 4
                && Character.isWhitespace(line.charAt(0)) && line.endsWith("{")
                && !line.contains(" class ") && !line.contains(" interface ") && !line.contains(" @interface ")
                && (!Character.isWhitespace(line.charAt(4))
                    || line.trim().startsWith("public") || line.trim().startsWith("protected")
                    || line.trim().startsWith("private"))) {
                addFailure("Empty line in " + relativePath + ", line: " + (index + 2));
            }
        }
    }

    /**
     * Checks the method last line, it should not be empty.
     */
    private void methodLastLine(final List<String> lines, final String relativePath) {
        for (int index = 0; index < lines.size() - 1; index++) {
            final String line = lines.get(index);
            final String nextLine = lines.get(index + 1);
            if (line.trim().length() == 0 && nextLine.equals("    }")) {
                addFailure("Empty line in " + relativePath + ", line: " + (index + 1));
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
            addFailure("'svn:eol-style' and 'svn:keywords' properties are not defined for " + relativePath);
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
     * @param file file to read
     * @return the list of lines
     * @throws IOException if an error occurs
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
                    badIndentationLevels(lines, relativePath);
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
                addFailure("Mixed indentation in " + relativePath + ", line: " + (i + 1));
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
                    addFailure("Trailing whitespace in " + relativePath + ", line: " + (i + 1));
                }
            }
        }
    }

    /**
     * Verifies that no XML files have bad indentation levels (each indentation level is 4 spaces).
     */
    private void badIndentationLevels(final List<String> lines, final String relativePath) {
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            final int length1 = line.length();
            final int length2 = line.trim().length();
            final int indentation = length1 - length2;
            if (indentation % 4 != 0) {
                addFailure("Bad indentation level (" + indentation + ") in " + relativePath + ", line: " + (i + 1));
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
        addFailure("Incorrect year in Version.getCopyright()");
    }

    /**
     * Verifies that no direct instantiation of WebClient from a test that runs with BrowserRunner.
     */
    private void runWith(final List<String> lines, final String relativePath) {
        if (relativePath.replace('\\', '/').contains("src/test/java") && !relativePath.contains("CodeStyleTest")) {
            boolean runWith = false;
            int index = 1;
            for (final String line : lines) {
                if (line.contains("@RunWith(BrowserRunner.class)")) {
                    runWith = true;
                }
                if (runWith) {
                    if (line.contains("new WebClient(")) {
                        addFailure("Test " + relativePath
                            + " should never directly instantiate WebClient, please use getWebClient() instead.");
                    }
                    if (line.contains("notYetImplemented()")) {
                        addFailure("Use @NotYetImplemented instead of notYetImplemented() in "
                            + relativePath + ", line: " + index);
                    }
                }
                index++;
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
                addFailure("Two empty contiguous lines at " + relativePath + ", line: " + (i + 1));
            }
        }
    }

}
