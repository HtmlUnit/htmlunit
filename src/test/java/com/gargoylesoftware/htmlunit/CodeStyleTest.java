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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.ISVNPropertyHandler;
import org.tmatesoft.svn.core.wc.SVNPropertyData;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Test of coding style for things that cannot be detected by Checkstyle.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class CodeStyleTest {

    private static final Pattern leadingWhitespace = Pattern.compile("^\\s+");
    private List<String> failures_ = new ArrayList<String>();
    private SVNWCClient svnWCClient_;

    /**
     * After.
     */
    @After
    public void after() {
        if (svnWCClient_ != null) {
            svnWCClient_.getOperationsFactory().getRepositoryPool().dispose();
        }
        final StringBuilder sb = new StringBuilder();
        for (final String error : failures_) {
            sb.append('\n').append(error);
        }

        final int errorsNumber = failures_.size();
        if (errorsNumber == 1) {
            fail("CodeStyle error: " + sb);
        }
        else if (errorsNumber > 1) {
            fail("CodeStyle " + errorsNumber + " errors: " + sb);
        }
    }

    private void addFailure(final String error) {
        failures_.add(error);
    }

    /**
     * @throws IOException if the test fails
     */
    @Test
    public void codeStyle() throws IOException {
        final ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        final ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager();
        svnWCClient_ = new SVNWCClient(authManager, options);
        process(new File("src/main/java"));
        process(new File("src/test/java"));
        licenseYear();
        versionYear();
    }

    private void process(final File dir) throws IOException {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !".svn".equals(file.getName())) {
                process(file);
            }
            else if (file.getName().endsWith(".java")) {
                final List<String> lines = getLines(file);
                final String relativePath = file.getAbsolutePath().substring(
                                new File(".").getAbsolutePath().length() - 1);
                openingCurlyBracket(lines, relativePath);
                year(lines, relativePath);
                javaDocFirstLine(lines, relativePath);
                methodFirstLine(lines, relativePath);
                methodLastLine(lines, relativePath);
                lineBetweenMethods(lines, relativePath);
                svnProperties(file, relativePath);
                runWith(lines, relativePath);
                twoEmptyLines(lines, relativePath);
                vs85aspx(lines, relativePath);
                deprecated(lines, relativePath);
                staticJSMethod(lines, relativePath);
                singleAlert(lines, relativePath);
                staticLoggers(lines, relativePath);
                loggingEnabled(lines, relativePath);
                browserVersion_isIE(lines, relativePath);
                versionBeforeAuthor(lines, relativePath);
            }
        }
    }

    /**
     * Ensures that no opening curly bracket exists by itself in a single line.
     */
    private void openingCurlyBracket(final List<String> lines, final String path) {
        int index = 1;
        for (final String line : lines) {
            if ("{".equals(line.trim())) {
                addFailure("Opening curly bracket is alone at " + path + ", line: " + index);
            }
            index++;
        }
    }

    /**
     * Checks the year in the source.
     */
    private void year(final List<String> lines, final String path) {
        final int year = Calendar.getInstance().get(Calendar.YEAR);
        if (lines.size() < 2 || !lines.get(1).contains("Copyright (c) 2002-" + year)) {
            addFailure("Incorrect year in " + path);
        }
    }

    /**
     * Checks the JavaDoc first line, it should not be empty, and should not start with lower-case.
     */
    private void javaDocFirstLine(final List<String> lines, final String relativePath) {
        for (int index = 1; index < lines.size(); index++) {
            final String previousLine = lines.get(index - 1);
            final String currentLine = lines.get(index);
            if ("/**".equals(previousLine.trim())) {
                if ("*".equals(currentLine.trim()) || currentLine.contains("*/")) {
                    addFailure("Empty line in " + relativePath + ", line: " + (index + 1));
                }
                if (currentLine.trim().startsWith("*")) {
                    final String text = currentLine.trim().substring(1).trim();
                    if (!text.isEmpty() && Character.isLowerCase(text.charAt(0))) {
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
            if (StringUtils.isBlank(lines.get(index + 1))
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
            if (StringUtils.isBlank(line) && "    }".equals(nextLine)) {
                addFailure("Empty line in " + relativePath + ", line: " + (index + 1));
            }
        }
    }

    /**
     * Checks that empty line must exist between consecutive methods.
     */
    private void lineBetweenMethods(final List<String> lines, final String relativePath) {
        for (int index = 0; index < lines.size() - 1; index++) {
            final String line = lines.get(index);
            final String nextLine = lines.get(index + 1);
            if ("    }".equals(line) && !nextLine.isEmpty() && !"}".equals(nextLine)) {
                addFailure("Non-empty line in " + relativePath + ", line: " + (index + 1));
            }
        }
    }

    /**
     * Checks properties svn:eol-style and svn:keywords.
     */
    private void svnProperties(final File file, final String relativePath) {
        if (!isSvnPropertiesDefined(file)) {
            addFailure("'svn:eol-style' and 'svn:keywords' properties are not defined for " + relativePath);
        }
    }

    private boolean isSvnPropertiesDefined(final File file) {
        try {
            final AtomicInteger i = new AtomicInteger();
            svnWCClient_.doGetProperty(file, null, SVNRevision.WORKING, SVNRevision.WORKING, SVNDepth.EMPTY,
                    new ISVNPropertyHandler() {

                    @Override
                    public void handleProperty(final long revision, final SVNPropertyData property) {
                    }

                    @Override
                    public void handleProperty(final SVNURL url, final SVNPropertyData property) {
                    }

                    @Override
                    public void handleProperty(final File path, final SVNPropertyData property) {
                        final String name = property.getName();
                        final String value = property.getValue().getString();
                        if ("svn:eol-style".equals(name) && "native".equals(value)) {
                            i.set(i.get() + 1);
                        }
                        else if ("svn:keywords".equals(name) && "Author Date Id Revision".equals(value)) {
                            i.set(i.get() + 2);
                        }
                    }
                }, null);
            if (i.get() == 3) {
                return true;
            }
        }
        catch (final Exception e) {
            //nothing
        }
        return false;
    }

    /**
     * Reads the given file as lines.
     * @param file file to read
     * @return the list of lines
     * @throws IOException if an error occurs
     */
    static List<String> getLines(final File file) throws IOException {
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
        processXML(new File("cruise"), false);
        processXML(new File("src/main/resources"), true);
        processXML(new File("src/assembly"), true);
        processXML(new File("src/changes"), true);
    }

    private void processXML(final File dir, final boolean recursive) throws Exception {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !".svn".equals(file.getName())) {
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
            final int indentation = getIndentation(lines.get(i));
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
        if (!lines.get(1).contains("Copyright (c) 2002-" + Calendar.getInstance().get(Calendar.YEAR))) {
            addFailure("Incorrect year in LICENSE.txt");
        }
    }

    /**
     * Checks the year in the {@link Version}.
     */
    private void versionYear() throws IOException {
        final List<String> lines = getLines(new File("src/main/java/com/gargoylesoftware/htmlunit/Version.java"));
        for (final String line : lines) {
            if (line.contains("return \"Copyright (c) 2002-" + Calendar.getInstance().get(Calendar.YEAR))) {
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
            boolean browserNone = true;
            int index = 1;
            for (final String line : lines) {
                if (line.contains("@RunWith(BrowserRunner.class)")) {
                    runWith = true;
                }
                if (line.contains("@Browsers(Browser.NONE)")) {
                    browserNone = true;
                }
                if (line.contains("@Test")) {
                    browserNone = false;
                }
                if (runWith) {
                    if (!browserNone && line.contains("new WebClient(") && !line.contains("getBrowserVersion()")) {
                        addFailure("Test " + relativePath + " line " + index
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
            if (StringUtils.isBlank(previousLine) && StringUtils.isBlank(line)) {
                addFailure("Two empty contiguous lines at " + relativePath + ", line: " + (i + 1));
            }
        }
    }

    /**
     * Verifies that no "(VS.85).aspx" token exists (which is sometimes used in MSDN documentation).
     */
    private void vs85aspx(final List<String> lines, final String relativePath) {
        if (!relativePath.contains("CodeStyleTest")) {
            int i = 0;
            for (final String line : lines) {
                if (line.contains("(VS.85).aspx")) {
                    addFailure("Please remove \"(VS.85)\" from the URL found in "
                        + relativePath + ", line: " + (i + 1));
                }
                i++;
            }
        }
    }

    /**
     * Verifies that deprecated tag is followed by "As of " or "since ", and '@Deprecated' annotation follows.
     */
    private void deprecated(final List<String> lines, final String relativePath) {
        int i = 0;
        for (String line : lines) {
            line = line.trim().toLowerCase();
            if (line.startsWith("* @deprecated")) {
                if (!line.startsWith("* @deprecated as of ") && !line.startsWith("* @deprecated since ")) {
                    addFailure("@deprecated must be immediately followed by \"As of \" or \"since \" in "
                        + relativePath + ", line: " + (i + 1));
                }
                if (!getAnnotations(lines, i).contains("@Deprecated")) {
                    addFailure("No \"@Deprecated\" annotation for " + relativePath + ", line: " + (i + 1));
                }
            }
            i++;
        }
    }

    /**
     * Returns all annotation lines that comes after the given 'javadoc' line.
     * @param lines source code lines
     * @param index the index to start searching from, must be a 'javadoc' line.
     */
    private List<String> getAnnotations(final List<String> lines, int index) {
        final List<String> annotations = new ArrayList<String>();
        while (!lines.get(index++).trim().endsWith("*/")) {
            //empty;
        }
        while (lines.get(index).trim().startsWith("@")) {
            annotations.add(lines.get(index++).trim());
        }
        return annotations;
    }

    /**
     * Verifies that no static JavaScript method exists.
     */
    private void staticJSMethod(final List<String> lines, final String relativePath) {
        if (relativePath.endsWith("Console.java")) {
            return;
        }
        int i = 0;
        for (final String line : lines) {
            if (line.contains(" static ")
                    && (line.contains(" jsxFunction_") || line.contains(" jsxGet_") || line.contains(" jsxSet_"))
                    && !line.contains(" jsxFunction_write") && !line.contains(" jsxFunction_insertBefore")
                    && !line.contains(" jsxFunction_drawImage")) {
                addFailure("Use of static JavaScript function in " + relativePath + ", line: " + (i + 1));
            }
            i++;
        }
    }

    /**
     * Single @Alert does not need curly brackets.
     */
    private void singleAlert(final List<String> lines, final String relativePath) {
        int i = 0;
        for (final String line : lines) {
            if (line.trim().startsWith("@Alerts") && line.contains("@Alerts({") && line.contains("})")) {
                final String alert = line.substring(line.indexOf('{'), line.indexOf('}'));
                if (!alert.contains(",") && alert.contains("\"")
                        && alert.indexOf('"', alert.indexOf('"') + 1) != -1) {
                    addFailure("No need for curly brackets in " + relativePath + ", line: " + (i + 1));
                }
            }
            i++;
        }
    }

    /**
     * Verifies that only static loggers exist.
     */
    private void staticLoggers(final List<String> lines, final String relativePath) {
        int i = 0;
        final String logClassName = Log.class.getSimpleName();
        for (String line : lines) {
            line = line.trim();
            if (line.contains(" " + logClassName + " ") && !line.contains(" LOG ") && !line.contains(" static ")
                && !line.startsWith("//") && !line.contains("httpclient.wire")) {
                addFailure("Non-static logger in " + relativePath + ", line: " + (i + 1));
            }
            i++;
        }
    }

    /**
     * Verifies that there is code to check log enablement.
     * <p> For example,
     * <code><pre>
     *    if (log.isDebugEnabled()) {
     *        ... do something expensive ...
     *        log.debug(theResult);
     *    }
     * </pre></code>
     * </p>
     */
    private void loggingEnabled(final List<String> lines, final String relativePath) {
        if (relativePath.contains("CodeStyleTest")) {
            return;
        }
        int i = 0;
        for (String line : lines) {
            if (line.contains("LOG.trace(")) {
                loggingEnabled(lines, i, "Trace", relativePath);
            }
            else if (line.contains("LOG.debug(")) {
                loggingEnabled(lines, i, "Debug", relativePath);
            }
            i++;
        }
    }

    private void loggingEnabled(final List<String> lines, final int index, final String method,
            final String relativePath) {
        final int indentation = getIndentation(lines.get(index));
        for (int i = index - 1; i >= 0; i--) {
            final String line = lines.get(i);
            if (getIndentation(line) < indentation && line.contains("LOG.is" + method + "Enabled()")) {
                return;
            }
            if (getIndentation(line) == 4) { // a method
                addFailure("You must check \"if (LOG.is" + method + "Enabled())\" around " + relativePath
                        + ", line: " + (index + 1));
                return;
            }
        }
    }

    private int getIndentation(final String line) {
        final Matcher matcher = leadingWhitespace.matcher(line);
        if (matcher.find()) {
            return matcher.end() - matcher.start();
        }
        return 0;
    }

    /**
     * Verifies that not invocation of browserVersion.isIE(), .isFirefox() or .getBrowserVersionNumeric().
     */
    private void browserVersion_isIE(final List<String> lines, final String relativePath) {
        if (relativePath.replace('\\', '/').contains("src/main/java")
                && !relativePath.contains("JavaScriptConfiguration")
                && !relativePath.contains("BrowserVersionFeatures")) {
            int index = 1;
            for (final String line : lines) {
                if ((line.contains(".isIE()") || line.contains(".isFirefox()"))) {
                    addFailure(".isIE() and .isFirefox() should not be used, please use .hasFeature(): "
                            + relativePath + ", line: " + index);
                }
                if (line.contains(".getBrowserVersionNumeric()")
                        && !relativePath.contains("IEConditionalCommentExpressionEvaluator")
                        && !relativePath.contains("IEConditionalCompilationScriptPreProcessor")
                        && !relativePath.contains("Window.java")) {
                    addFailure(".getBrowserVersionNumeric() should not be used, please use .hasFeature(): "
                            + relativePath + ", line: " + index);
                }
                index++;
            }
        }
    }

    /**
     * Verifies that \@version is always before \@author.
     */
    private void versionBeforeAuthor(final List<String> lines, final String relativePath) {
        boolean versionFound = false;
        for (final String line : lines) {
            if (line.startsWith(" * @version")) {
                versionFound = true;
            }
            else if (line.startsWith(" * @author") && !versionFound) {
                addFailure("@version should exist before @author in: " + relativePath);
            }
        }
    }
}
