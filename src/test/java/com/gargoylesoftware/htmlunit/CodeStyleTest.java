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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

import com.gargoylesoftware.htmlunit.source.SVN;

/**
 * Test of coding style for things that cannot be detected by Checkstyle.
 *
 * @author Ahmed Ashour
 */
public class CodeStyleTest {

    private static final Pattern leadingWhitespace = Pattern.compile("^\\s+");
    private List<String> failures_ = new ArrayList<>();
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
        final List<File> files = new ArrayList<>();
        addAll(new File("src/main"), files);
        addAll(new File("src/test"), files);
        final List<String> classNames = getClassNames(files);
        process(files, classNames);
        for (final String className : classNames) {
            //addFailure("Not used " + className);
        }

        licenseYear();
        versionYear();
        parentInPom();
    }

    private static List<String> getClassNames(final List<File> files) {
        final List<String> list = new ArrayList<>();
        for (final File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(".java")) {
                fileName = fileName.substring(0, fileName.length() - 5);
                fileName = fileName.substring(fileName.lastIndexOf('.') + 1);
                list.add(fileName);
            }
        }
        return list;
    }

    private void addAll(final File dir, final List<File> files) throws IOException {
        final File[] children = dir.listFiles();
        if (children != null) {
            for (final File child : children) {
                if (child.isDirectory() && !".svn".equals(child.getName())) {
                    addAll(child, files);
                }
                else {
                    files.add(child);
                }
            }
        }
    }

    private void process(final List<File> files, final List<String> classNames) throws IOException {
        for (final File file : files) {
            final String relativePath = file.getAbsolutePath().substring(new File(".").getAbsolutePath().length() - 1);
            svnProperties(file, relativePath);
            if (file.getName().endsWith(".java")) {
                final List<String> lines = FileUtils.readLines(file);
                openingCurlyBracket(lines, relativePath);
                year(lines, relativePath);
                javaDocFirstLine(lines, relativePath);
                methodFirstLine(lines, relativePath);
                methodLastLine(lines, relativePath);
                lineBetweenMethods(lines, relativePath);
                runWith(lines, relativePath);
                vs85aspx(lines, relativePath);
                deprecated(lines, relativePath);
                staticJSMethod(lines, relativePath);
                singleAlert(lines, relativePath);
                staticLoggers(lines, relativePath);
                loggingEnabled(lines, relativePath);
                browserVersion_isIE(lines, relativePath);
                alerts(lines, relativePath);
                className(lines, relativePath);
                classNameUsed(lines, classNames, relativePath);
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
        final int year = Calendar.getInstance(Locale.ROOT).get(Calendar.YEAR);
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
                && line.length() > 4 && index > 0 && lines.get(index - 1).startsWith("    ")
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
            if ("    /**".equals(nextLine) && !line.isEmpty()) {
                addFailure("Non-empty line in " + relativePath + ", line: " + (index + 2));
            }
        }
    }

    /**
     * Checks property {@code svn:eol-style}.
     */
    private void svnProperties(final File file, final String relativePath) {
        if (!isSvnPropertiesDefined(file)) {
            addFailure("'svn:eol-style' property is not defined for " + relativePath);
        }
    }

    private boolean isSvnPropertiesDefined(final File file) {
        try {
            final AtomicBoolean eol = new AtomicBoolean();
            svnWCClient_.doGetProperty(file, null, SVNRevision.WORKING, SVNRevision.WORKING, SVNDepth.EMPTY,
                    new ISVNPropertyHandler() {

                    @Override
                    public void handleProperty(final long revision, final SVNPropertyData property) {
                        // nothing to do
                    }

                    @Override
                    public void handleProperty(final SVNURL url, final SVNPropertyData property) {
                        // nothing to do
                    }

                    @Override
                    public void handleProperty(final File path, final SVNPropertyData property) {
                        final String name = property.getName();
                        final String value = property.getValue().getString();
                        if ("svn:eol-style".equals(name) && "native".equals(value)) {
                            eol.set(true);
                        }
                    }
                }, null);

            final String fileName = file.getName().toLowerCase(Locale.ROOT);

            for (final String extension : SVN.getEolExtenstions()) {
                if (fileName.endsWith(extension)) {
                    return eol.get();
                }
            }
            return true;
        }
        catch (final Exception e) {
            //nothing
        }
        final String path = file.getAbsolutePath();
        // automatically generated and is outside SVN control
        return (path.contains("jQuery") && path.contains("WEB-INF") && path.contains("cgi"))
                || (path.contains("jQuery") && path.contains("csp.log"));
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
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory() && !".svn".equals(file.getName())) {
                    if (recursive) {
                        processXML(file, true);
                    }
                }
                else {
                    if (file.getName().endsWith(".xml")) {
                        final List<String> lines = FileUtils.readLines(file);
                        final String relativePath = file.getAbsolutePath().substring(
                                new File(".").getAbsolutePath().length() - 1);
                        mixedIndentation(lines, relativePath);
                        trailingWhitespace(lines, relativePath);
                        badIndentationLevels(lines, relativePath);
                    }
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
            if (!line.isEmpty()) {
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
        final List<String> lines = FileUtils.readLines(new File("LICENSE.txt"));
        if (!lines.get(1).contains("Copyright (c) 2002-" + Calendar.getInstance(Locale.ROOT).get(Calendar.YEAR))) {
            addFailure("Incorrect year in LICENSE.txt");
        }
    }

    /**
     * Checks the year in the {@link Version}.
     */
    private void versionYear() throws IOException {
        final List<String> lines =
                FileUtils.readLines(new File("src/main/java/com/gargoylesoftware/htmlunit/Version.java"));
        for (final String line : lines) {
            if (line.contains("return \"Copyright (c) 2002-" + Calendar.getInstance(Locale.ROOT).get(Calendar.YEAR))) {
                return;
            }
        }
        addFailure("Incorrect year in Version.getCopyright()");
    }

    /**
     * Verifies no &lt;parent&gt; tag in {@code pom.xml}.
     */
    private void parentInPom() throws IOException {
        final List<String> lines = FileUtils.readLines(new File("pom.xml"));
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("<parent>")) {
                addFailure("'pom.xml' should not have <parent> tag in line: " + (i + 1));
                break;
            }
        }
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
                if (line.contains("@Test")) {
                    browserNone = false;
                }
                if (relativePath.contains("JavaScriptEngineTest") && line.contains("nonStandardBrowserVersion")) {
                    browserNone = true;
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
            line = line.trim().toLowerCase(Locale.ROOT);
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
    private static List<String> getAnnotations(final List<String> lines, int index) {
        final List<String> annotations = new ArrayList<>();
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

    private static int getIndentation(final String line) {
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
                && !relativePath.contains("BrowserVersionFeatures")
                && !relativePath.contains("HTMLDocument")
                && !relativePath.contains("HTMLParser")
            && !relativePath.contains("DateTimeFormat")) {
            int index = 1;
            for (final String line : lines) {
                if (line.contains(".isIE()") || line.contains(".isFirefox()")) {
                    addFailure(".isIE() and .isFirefox() should not be used, please use .hasFeature(): "
                            + relativePath + ", line: " + index);
                }
                if (line.contains(".getBrowserVersionNumeric()")
                        && !relativePath.contains("IEConditionalCommentExpressionEvaluator")
                        && !relativePath.contains("IEConditionalCompilationScriptPreProcessor")
                        && !relativePath.contains("BrowserConfiguration")
                        && !relativePath.contains("Window.java")) {
                    addFailure(".getBrowserVersionNumeric() should not be used, please use .hasFeature(): "
                            + relativePath + ", line: " + index);
                }
                index++;
            }
        }
    }

    /**
     * Verifies that \@Alerts is correctly defined.
     */
    private void alerts(final List<String> lines, final String relativePath) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("    @Alerts(")) {
                final List<String> alerts = alertsToList(lines, i, true);
                alertVerify(alerts, relativePath, i);
            }
        }
    }

    /**
     * Verifies that the class name is used.
     */
    private static void classNameUsed(final List<String> lines, final List<String> classNames,
            final String relativePath) {
        String simpleName = relativePath.substring(0, relativePath.length() - 5);
        simpleName = simpleName.substring(simpleName.lastIndexOf(File.separator) + 1);
        for (final String line : lines) {
            for (final Iterator<String> it = classNames.iterator(); it.hasNext();) {
                final String className = it.next();
                if (line.contains(className) && !className.equals(simpleName)) {
                    it.remove();
                }
            }
        }
    }

    /**
     * Verifies that the class name is used.
     */
    private void className(final List<String> lines, final String relativePath) {
        if (relativePath.contains("main") && relativePath.contains("host")
                && !relativePath.contains("LabelsHelper")) {
            String fileName = relativePath.substring(0, relativePath.length() - 5);
            fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
            for (final String line : lines) {
                if (line.startsWith(" * ")) {
                    int p0 = line.indexOf("{@code ");
                    if (p0 != -1) {
                        p0 = p0 + "{@code ".length();
                        final int p1 = line.indexOf('}',  p0 + 1);
                        final String name = line.substring(p0,  p1);
                        if (!name.equals(fileName)) {
                            addFailure("Incorrect host class in " + relativePath);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns array of String of the alerts which are in the specified index.
     *
     * @param lines the list of strings
     * @param alertsIndex the index in which the \@Alerts is defined
     * @return array of alert strings
     */
    public static List<String> alertsToList(final List<String> lines, final int alertsIndex) {
        return alertsToList(lines, alertsIndex, false);
    }

    private static List<String> alertsToList(final List<String> lines, final int alertsIndex,
            final boolean preserveCommas) {
        if ("    @Alerts".equals(lines.get(alertsIndex))) {
            lines.set(alertsIndex, "    @Alerts()");
        }
        if (!lines.get(alertsIndex).startsWith("    @Alerts(")) {
            throw new IllegalArgumentException("No @Alerts found in " + (alertsIndex + 1));
        }
        final StringBuilder alerts = new StringBuilder();
        for (int i = alertsIndex;; i++) {
            final String line = lines.get(i);
            if (alerts.length() != 0) {
                alerts.append('\n');
            }
            if (line.startsWith("    @Alerts(")) {
                alerts.append(line.substring("    @Alerts(".length()));
            }
            else {
                alerts.append(line);
            }
            if (line.endsWith(")")) {
                alerts.deleteCharAt(alerts.length() - 1);
                break;
            }
        }
        final List<String> list = alertsToList(alerts.toString());
        if (!preserveCommas) {
            for (int i = 0; i < list.size(); i++) {
                String value = list.get(i);
                if (value.startsWith(",")) {
                    value = value.substring(1).trim();
                }
                list.set(i, value);
            }
        }
        return list;
    }

    /**
     * Verifies a specific \@Alerts definition.
     */
    private void alertVerify(final List<String> alerts, final String relativePath, final int lineIndex) {
        if (alerts.size() == 1) {
            if (alerts.get(0).contains("DEFAULT")) {
                addFailure("No need for \"DEFAULT\" in "
                        + relativePath + ", line: " + (lineIndex + 1));
            }
        }
        else {
            final List<String> names = new ArrayList<>();
            for (String alert : alerts) {
                if (alert.charAt(0) == ',') {
                    if (alert.charAt(1) != '\n') {
                        addFailure("Expectation must be in a separate line in "
                                + relativePath + ", line: " + (lineIndex + 1));
                    }
                    alert = alert.substring(1).trim();
                }

                final int quoteIndex = alert.indexOf('"');
                final int equalsIndex = alert.indexOf('=');
                if (equalsIndex != -1 && equalsIndex < quoteIndex) {
                    final String name = alert.substring(0, equalsIndex - 1);
                    alertVerifyOrder(name, names, relativePath, lineIndex);
                    names.add(name);
                }
            }
        }
    }

    /**
     * Converts the given alerts definition to an array of expressions.
     */
    private static List<String> alertsToList(final String string) {
        final List<String> list = new ArrayList<>();
        if ("\"\"".equals(string)) {
            list.add(string);
        }
        else {
            final StringBuilder currentToken = new StringBuilder();

            boolean insideString = true;
            boolean startsWithBraces = false;
            for (final String token : string.split("(?<!\\\\)\"")) {
                insideString = !insideString;
                if (currentToken.length() != 0) {
                    currentToken.append('"');
                }
                else {
                    startsWithBraces = token.toString().contains("{");
                }

                if (!insideString && token.startsWith(",") && !startsWithBraces) {
                    list.add(currentToken.toString());
                    currentToken.setLength(0);
                    startsWithBraces = token.toString().contains("{");
                }

                if (!insideString && token.contains("}")) {
                    final int curlyIndex = token.indexOf('}') + 1;
                    currentToken.append(token.substring(0, curlyIndex));
                    list.add(currentToken.toString());
                    currentToken.setLength(0);
                    currentToken.append(token.substring(curlyIndex));
                }
                else {
                    if (!insideString && token.contains(",") && !startsWithBraces) {
                        final String[] expressions = token.split(",");
                        currentToken.append(expressions[0]);
                        if (currentToken.length() != 0) {
                            list.add(currentToken.toString());
                        }
                        for (int i = 1; i < expressions.length - 1; i++) {
                            list.add(',' + expressions[i]);
                        }
                        currentToken.setLength(0);
                        currentToken.append(',' + expressions[expressions.length - 1]);
                    }
                    else {
                        currentToken.append(token);
                    }
                }
            }
            if (currentToken.length() != 0) {
                if (!currentToken.toString().contains("\"")) {
                    currentToken.insert(0, '"');
                }
                int totalQuotes = 0;
                for (int i = 0; i < currentToken.length(); i++) {
                    if (currentToken.charAt(i) == '"' && (i == 0 || currentToken.charAt(i - 1) != '\\')) {
                        totalQuotes++;
                    }
                }
                if (totalQuotes % 2 != 0) {
                    currentToken.append('"');
                }

                list.add(currentToken.toString());
            }
        }
        return list;
    }

    /**
     * Verifies \@Alerts specific order.
     *
     * @param browserName the browser name
     * @param previousList the previously defined browser names
     */
    private void alertVerifyOrder(final String browserName, final List<String> previousList,
            final String relativePath, final int lineIndex) {
        switch (browserName) {
            case "DEFAULT":
                if (!previousList.isEmpty()) {
                    addFailure("DEFAULT must be first in "
                            + relativePath + ", line: " + (lineIndex + 1));
                }
                break;

            case "IE":
                if (previousList.contains("IE11")) {
                    addFailure("IE must be before specifc IE version in "
                            + relativePath + ", line: " + (lineIndex + 1));
                }
                break;

            case "FF":
                if (previousList.contains("FF38") || previousList.contains("FF45")) {
                    addFailure("FF must be before specifc FF version in "
                            + relativePath + ", line: " + (lineIndex + 1));
                }
                break;

            default:
        }
    }

    /**
     * Tests if all JUnit 4 candidate test methods declare <tt>@Test</tt> annotation.
     * @throws Exception if the test fails
     */
    @Test
    public void tests() throws Exception {
        testTests(new File("src/test/java"));
    }

    private void testTests(final File dir) throws Exception {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory()) {
                if (!".svn".equals(file.getName())) {
                    testTests(file);
                }
            }
            else {
                if (file.getName().endsWith(".java")) {
                    final int index = new File("src/test/java").getAbsolutePath().length();
                    String name = file.getAbsolutePath();
                    name = name.substring(index + 1, name.length() - 5);
                    name = name.replace(File.separatorChar, '.');
                    final Class<?> clazz;
                    try {
                        clazz = Class.forName(name);
                    }
                    catch (final Exception e) {
                        continue;
                    }
                    name = file.getName();
                    if (name.endsWith("Test.java") || name.endsWith("TestCase.java")) {
                        for (final Constructor<?> ctor : clazz.getConstructors()) {
                            if (ctor.getParameterTypes().length == 0) {
                                for (final Method method : clazz.getDeclaredMethods()) {
                                    if (Modifier.isPublic(method.getModifiers())
                                            && method.getAnnotation(Before.class) == null
                                            && method.getAnnotation(BeforeClass.class) == null
                                            && method.getAnnotation(After.class) == null
                                            && method.getAnnotation(AfterClass.class) == null
                                            && method.getAnnotation(Test.class) == null
                                            && method.getReturnType() == Void.TYPE
                                            && method.getParameterTypes().length == 0) {
                                        fail("Method '" + method.getName()
                                                + "' in " + name + " does not declare @Test annotation");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
