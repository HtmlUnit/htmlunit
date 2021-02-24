/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.source;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.libraries.JQuery1x8x2Test;

/**
 * Extracts the needed expectation from the real browsers output, this is done by waiting the browser to finish
 * all the tests, then select all visible text and copy it to a local file.
 *
 * Steps to generate the tests:
 * <ol>
 *   <li>Call {@link #extractExpectations(File, File)}, where <tt>input</tt> is the raw file from the browser</li>
 *   <li>Have a quick look on the output files, and compare them to verify there are only minimal differences</li>
 *   <li>Rename all outputs to browser names e.g. {@code results.IE.txt}, {@code results.FF60.txt}, etc</li>
 *   <li>Put all outputs in one folder and call {@link #generateTestCases(Class, File)}</li>
 * </ol>
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public final class JQueryExtractor {
    private static final String RERUN_ID = "Rerun [";

    private JQueryExtractor() {
    }

    /**
     * Main method.
     * @param args program arguments
     * @throws Exception s
     */
    public static void main(final String[] args) throws Exception {
        final Class<? extends WebDriverTestCase> testClass = JQuery1x8x2Test.class;
        // final Class<? extends WebDriverTestCase> testClass = JQuery1x11x3Test.class;
        // final Class<? extends WebDriverTestCase> testClass = JQuery3x3x1Test.class;

        final String version = (String) MethodUtils.invokeExactMethod(testClass.newInstance(), "getVersion");
        final File baseDir = new File("src/test/resources/libraries/jQuery/" + version + "/expectations");

        for (final String browser : new String[] {"CHROME", "EDGE", "FF", "FF78", "IE"}) {
            final File out = new File(baseDir, browser + ".out");
            final File results = new File(baseDir, "results." + browser + ".txt");
            extractExpectations(out, results);
        }

        generateTestCases(testClass, baseDir);
    }

    /**
     * Transforms the raw expectation, to the needed one by HtmlUnit.
     * This methods puts only the main line of the test output, without the details.
     *
     * @param input the raw file of real browser, with header and footer removed
     * @param output the expectation
     * @throws IOException if an error occurs
     */
    public static void extractExpectations(final File input, final File output) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                int testNumber = 1;
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    // for jQuery 3.3.1 we have patched the test output
                    // to make the hash visible
                    if (line.contains(RERUN_ID)) {
                        // cleanup ie output
                        line = line.replace(".skipped", ".");

                        final int start = line.indexOf(RERUN_ID) + RERUN_ID.length();
                        final int end = line.indexOf(']', start);
                        final String testId = line.substring(start, end);

                        line = line.substring(0, line.indexOf(RERUN_ID)).trim();
                        final String prefix = "" + testNumber + ".";
                        if (line.startsWith(prefix)) {
                            line = line.substring(prefix.length());
                        }
                        line = "" + testNumber + '.' + ' ' + line + " [" + testId + ']';
                        writer.write(line + System.lineSeparator());
                        testNumber++;
                    }

                    // the test number is at least for 1.11.3 no longer part of the output
                    // instead a ordered list is used by qunit
                    // if (line.startsWith("" + testNumber + '.') && endPos > -1) {
                    else if (line.indexOf("Rerun") > -1) {
                        line = line.substring(0, line.indexOf("Rerun"))
                                + " [" + testNumber + ']';
                        writer.write(line + System.lineSeparator());
                        testNumber++;
                    }
                    else if (line.endsWith("Rerun")) {
                        if (line.indexOf("" + testNumber + '.', 4) != -1) {
                            System.out.println("Incorrect line for test# " + testNumber
                                    + ", please correct it manually");
                            break;
                        }
                        line = "" + testNumber + '.'
                                + ' ' + line.substring(0, line.length() - 5)
                                + " [" + testNumber + ']';
                        writer.write(line + System.lineSeparator());
                        testNumber++;
                    }
                }
                System.out.println("Last output #" + (testNumber - 1));
            }
        }
    }

    /**
     * Generates the java code of the test cases.
     * @param testClass the class containing the tests
     * @param dir the directory which holds the expectations
     * @throws Exception if an error occurs.
     */
    public static void generateTestCases(final Class<? extends WebDriverTestCase> testClass,
            final File dir) throws Exception {
        final TestedBrowser[] browsers = TestedBrowser.values();
        // main browsers regardless of version e.g. "FF"
        final List<String> mainNames = new ArrayList<>();
        for (final TestedBrowser b : browsers) {
            final String name = b.name();
            if (!"NONE".equals(name) && Character.isLetter(name.charAt(name.length() - 1))) {
                mainNames.add(name);
            }
        }

        final Map<String, List<String>> browserVersions = new HashMap<>();
        for (final TestedBrowser b : browsers) {
            final String name = b.name();
            for (final String mainName : mainNames) {
                if (!name.equals(mainName) && name.startsWith(mainName)) {
                    List<String> list = browserVersions.get(mainName);
                    if (list == null) {
                        list = new ArrayList<>();
                        browserVersions.put(mainName, list);
                    }
                    list.add(name);
                }
            }
        }
        final Map<String, Expectations> browserExpectations = new HashMap<>();
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    for (final TestedBrowser b : browsers) {
                        final String browserName = b.name();
                        if (file.getName().equalsIgnoreCase("results." + browserName.replace('_', '.') + ".txt")) {
                            browserExpectations.put(browserName, Expectations.readExpectations(file));
                        }
                    }
                }
            }
        }

        // gather all the tests (some tests don't get executed for all browsers)
        final List<Test> allTests = computeTestsList(browserExpectations);

        final Collection<String> availableBrowserNames = new TreeSet<>(browserExpectations.keySet());
        for (final Test test : allTests) {
            final Map<String, String> testExpectation = new TreeMap<>();
            final Map<Integer, List<String>> lineToBrowser = new TreeMap<>();
            for (final String browserName : availableBrowserNames) {
                final Expectation expectation = browserExpectations.get(browserName).getExpectation(test);
                if (expectation != null) {
                    List<String> browsersForLine = lineToBrowser.get(expectation.getLine());
                    if (browsersForLine == null) {
                        browsersForLine = new ArrayList<>();
                        lineToBrowser.put(expectation.getLine(), browsersForLine);
                    }
                    browsersForLine.add(browserName);
                    final String str = expectation.getTestResult();
                    testExpectation.put(browserName,
                            str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\""));
                }
            }
            System.out.println("    /**");
            System.out.println("     * Test " + lineToBrowser + ".");
            System.out.println("     * @throws Exception if an error occurs");
            System.out.println("     */");
            System.out.println("    @Test");
            System.out.print("    @Alerts(");

            final boolean allSame = testExpectation.size() == availableBrowserNames.size()
                    && new HashSet<>(testExpectation.values()).size() == 1;
            if (allSame) {
                final String first = testExpectation.keySet().iterator().next();
                String expectation = testExpectation.get(first);
                if (expectation.length() > 100) {
                    expectation = expectation.substring(0, 50) + "\"\n            + \"" + expectation.substring(50);
                }
                System.out.print("\"" + expectation + '"');
            }
            else {
                final List<String> cleanedBrowserNames = new ArrayList<>(testExpectation.keySet());
                Collections.sort(cleanedBrowserNames);

                if (testExpectation.size() == availableBrowserNames.size()) {
                    // Hack a bit to avoid redundant alerts
                    // find the best default
                    int matches = 0;
                    ArrayList<String> defaultBrowsers = null;

                    for (final String browser : cleanedBrowserNames) {
                        final String expectation = testExpectation.get(browser);

                        final ArrayList<String> matchBrowsers = new ArrayList<>();
                        int matchCount = 0;
                        for (final String otherBrowser : cleanedBrowserNames) {
                            if (!browser.equals(otherBrowser)
                                    && expectation.equals(testExpectation.get(otherBrowser))) {
                                matchCount++;
                                matchBrowsers.add(otherBrowser);
                            }
                        }
                        if (matches < matchCount) {
                            matches = matchCount;
                            matchBrowsers.add(browser);
                            defaultBrowsers = matchBrowsers;
                        }
                    }

                    if (matches > 1) {
                        testExpectation.put("DEFAULT", testExpectation.get(defaultBrowsers.get(0)));
                        cleanedBrowserNames.add(0, "DEFAULT");
                        for (final String browser : defaultBrowsers) {
                            testExpectation.remove(browser);
                            cleanedBrowserNames.remove(browser);
                        }
                    }
                }

                boolean first = true;
                if (cleanedBrowserNames.size() == 1 && "DEFAULT".equals(cleanedBrowserNames.get(0))) {
                    System.out.print("\"" + testExpectation.get("DEFAULT") + '"');
                }
                else {
                    for (final String browserName : cleanedBrowserNames) {
                        final String expectation = testExpectation.get(browserName);
                        if (expectation == null) {
                            continue; // test didn't run for this browser
                        }
                        if (!first) {
                            System.out.println(",");
                            System.out.print("            ");
                        }
                        System.out.print(browserName + " = \"" + expectation + '"');
                        first = false;
                    }
                }
            }
            System.out.println(")");

            final String methodName = test.getName().replaceAll("\\W", "_");
            try {
                final Method method = testClass.getMethod(methodName);
                final NotYetImplemented notYetImplemented = method.getAnnotation(NotYetImplemented.class);
                if (null != notYetImplemented) {
                    final TestedBrowser[] notYetImplementedBrowsers = notYetImplemented.value();
                    if (notYetImplementedBrowsers.length > 0) {
                        final List<String> browserNames = new ArrayList<>(notYetImplementedBrowsers.length);
                        for (final TestedBrowser browser : notYetImplementedBrowsers) {
                            browserNames.add(browser.name());
                        }
                        Collections.sort(browserNames);

                        // TODO dirty hack
                        if (browserNames.size() == 5
                                && browserNames.contains("CHROME")
                                && browserNames.contains("EDGE")
                                && browserNames.contains("FF")
                                && browserNames.contains("FF78")
                                && browserNames.contains("IE")) {
                            System.out.println("    @NotYetImplemented");
                        }
                        else {
                            System.out.print("    @NotYetImplemented(");
                            if (browserNames.size() > 1) {
                                System.out.print("{ ");
                            }
                            System.out.print(String.join(", ", browserNames));
                            if (browserNames.size() > 1) {
                                System.out.print(" }");
                            }
                            System.out.println(")");
                        }
                    }
                }
            }
            catch (final NoSuchMethodException e) {
                // ignore
            }

            System.out.println("    public void " + methodName + "() throws Exception {");
            System.out.println("        runTest(\"" + test.getName().replace("\"", "\\\"") + "\");");
            System.out.println("    }");
            System.out.println();
        }
    }

    private static List<Test> computeTestsList(final Map<String, Expectations> browserExpectations) {
        final Map<String, Test> map = new HashMap<>();
        for (final Expectations expectations : browserExpectations.values()) {
            for (final Expectation expectation : expectations) {
                final String testName = expectation.getTestName();
                Test test = map.get(testName);
                if (test == null) {
                    test = new Test(testName);
                    map.put(testName, test);
                }
                test.addLine(expectation.getLine());
            }
        }

        final List<Test> tests = new ArrayList<>(map.values());
        Collections.sort(tests);

        return tests;
    }

    static class Expectations implements Iterable<Expectation> {
        static Expectations readExpectations(final File file) throws IOException {
            final Expectations expectations = new Expectations();
            final List<String> lines = FileUtils.readLines(file, ISO_8859_1);
            for (int i = 0; i < lines.size(); i++) {
                expectations.add(new Expectation(file, i + 1, lines.get(i)));
            }

            return expectations;
        }

        private final Map<String, Expectation> expectations_ = new HashMap<>();

        public Expectation getExpectation(final Test test) {
            return expectations_.get(test.getName());
        }

        private void add(final Expectation expectation) {
            expectations_.put(expectation.getTestName(), expectation);
        }

        @Override
        public Iterator<Expectation> iterator() {
            return expectations_.values().iterator();
        }
    }

    static class Expectation {

        private static final Pattern pattern_ =
                Pattern.compile("(\\d+\\. ?)?(.+)\\((\\d+(, \\d+, \\d+)?)\\) \\[([0-9a-f]{1,8})\\]");

        private final int line_;
        private final String testId_;
        private final String testName_;
        private final String testResult_;

        Expectation(final File file, final int line, final String string) {
            line_ = line;
            final Matcher matcher = pattern_.matcher(string);
            if (!matcher.matches()) {
                throw new RuntimeException("Invalid line " + line + ": '" + string
                        + "' in file: " + file.getAbsolutePath());
            }
            final String testNumber = matcher.group(1);
            if (testNumber != null && !testNumber.trim().equals(line + ".")) {
                throw new RuntimeException("Invalid test number for line " + line + ": " + string
                        + " in file: " + file.getAbsolutePath());
            }

            testName_ = matcher.group(2).trim();
            testResult_ = matcher.group(3);
            testId_ = matcher.group(4);
        }

        public int getLine() {
            return line_;
        }

        public String getTestId() {
            return testId_;
        }

        public String getTestName() {
            return testName_;
        }

        public String getTestResult() {
            return testResult_;
        }
    }

    static class Test implements Comparable<Test> {
        private final List<Integer> lines_ = new ArrayList<>();
        private final String name_;

        Test(final String name) {
            name_ = name;
        }

        public String getName() {
            return name_;
        }

        void addLine(final int line) {
            if (!lines_.contains(line)) {
                lines_.add(line);
                Collections.sort(lines_);
            }
        }

        @Override
        public int compareTo(final Test o) {
            int diff = lines_.get(0) - o.lines_.get(0);
            if (diff == 0) {
                diff = lines_.size() - o.lines_.size();
                if (diff == 0) {
                    diff = name_.compareTo(o.name_);
                }
            }
            return diff;
        }

        @Override
        public int hashCode() {
            return name_.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return (obj instanceof Test) && name_.equals(((Test) obj).name_);
        }
    }
}
