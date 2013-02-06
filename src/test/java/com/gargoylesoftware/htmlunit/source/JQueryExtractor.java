/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;

/**
 * Extracts the needed expectation from the real browsers output, this is done by waiting the browser to finish
 * all the tests, then select all visible text and copy it to a local file.
 *
 * In IE8 raw file, test outputs should be manually separated in a new line.
 *
 * Steps to generate the tests:
 * <ol>
 *   <li>Call {@link #extractExpectations(File, File)}, where <tt>input</tt> is the raw file from the browser</li>
 *   <li>Have a quick look on the output files, and compare them to verify there is only minimal differences</li>
 *   <li>Rename all outputs to browser names e.g. "results.IE8.txt", "results.FF3.6.txt" etc</li>
 *   <li>Put all outputs in one folder and call {@link #generateTestCases(File)}</li>
 * </ol>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public final class JQueryExtractor {

    private JQueryExtractor() {
    }

    /**
     * Main method.
     * @param args program arguments
     * @throws Exception s
     */
    public static void main(final String[] args) throws Exception {
        final File expectationsDir =
                new File("src/test/resources/libraries/jQuery/1.8.2/expectations");
        generateTestCases(expectationsDir);
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
        final BufferedReader reader = new BufferedReader(new FileReader(input));
        final BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        int testNumber = 1;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("" + testNumber + '.') && line.endsWith("Rerun")) {
                line = line.substring(0, line.length() - 5);
                writer.write(line + "\n");
                testNumber++;
            }
            else if (line.endsWith("Rerun")) {
                if (line.indexOf("" + testNumber + '.', 4) != -1) {
                    System.out.println("Incorrect line for test# " + testNumber + ", please correct it manually");
                    break;
                }
                line = "" + testNumber + '.' + ' ' + line.substring(0, line.length() - 5);
                writer.write(line + "\n");
                testNumber++;
            }
        }
        System.out.println("Last output #" + (testNumber - 1));
        reader.close();
        writer.close();
    }

    /**
     * Generates the java code of the test cases.
     * @param dir the directory which holds the expectations
     * @throws IOException if an error occurs.
     */
    public static void generateTestCases(final File dir) throws IOException {
        final Browser[] browsers = Browser.values();
        // main browsers regardless of version e.g. "FF"
        final List<String> mainNames = new ArrayList<String>();
        for (final Browser b : browsers) {
            final String name = b.name();
            if (!"NONE".equals(name) && Character.isLetter(name.charAt(name.length() - 1))) {
                mainNames.add(name);
            }
        }

        final Map<String, List<String>> browserVersions = new HashMap<String, List<String>>();
        for (final Browser b : browsers) {
            final String name = b.name();
            for (final String mainName : mainNames) {
                if (!name.equals(mainName) && name.startsWith(mainName)) {
                    List<String> list = browserVersions.get(mainName);
                    if (list == null) {
                        list = new ArrayList<String>();
                        browserVersions.put(mainName, list);
                    }
                    list.add(name);
                }
            }
        }
        final Map<String, Expectations> browserExpectations = new HashMap<String, Expectations>();
        for (final File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                for (final Browser b : browsers) {
                    final String browserName = b.name();
                    if (file.getName().equalsIgnoreCase("results." + browserName.replace('_', '.') + ".txt")) {
                        browserExpectations.put(browserName, Expectations.readExpectations(file));
                    }
                }
            }
        }

        // gather all the tests (some tests don't get executed for all browsers)
        final List<Test> allTests = computeTestsList(browserExpectations);

        final Collection<String> availableBrowserNames = new TreeSet<String>(browserExpectations.keySet());
        for (final Test test : allTests) {
            final Map<String, String> testExpectation = new TreeMap<String, String>();
            final Map<Integer, List<String>> lineToBrowser = new TreeMap<Integer, List<String>>();
            for (final String browserName : availableBrowserNames) {
                final Expectation expectation = browserExpectations.get(browserName).getExpectation(test);
                if (expectation != null) {
                    List<String> browsersForLine = lineToBrowser.get(expectation.getLine());
                    if (browsersForLine == null) {
                        browsersForLine = new ArrayList<String>();
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
                    && new HashSet<String>(testExpectation.values()).size() == 1;
            if (allSame) {
                final String first = testExpectation.keySet().iterator().next();
                String expectation = testExpectation.get(first);
                if (expectation.length() > 100) {
                    expectation = expectation.substring(0, 50) + "\"\n            + \"" + expectation.substring(50);
                }
                System.out.print("\"" + expectation + '"');
            }
            else {
                boolean first = true;
                for (final String browserName : availableBrowserNames) {
                    final String expectation = testExpectation.get(browserName);
                    if (expectation == null) {
                        continue; // test didn't run for this browser
                    }
                    if (!first) {
                        System.out.println(",");
                        System.out.print("        ");
                    }
                    System.out.print(browserName + " = \"" + expectation + '"');
                    first = false;
                }

            }
            System.out.println(")");
            System.out.println("    public void " + test.getName().replaceAll("\\W",  "_") + "() throws Exception {");
            System.out.println("        runTest(\"" + test.getName().replace("\"",  "\\\"") + "\");");
            System.out.println("    }");
            System.out.println();
        }
    }

    private static List<Test> computeTestsList(final Map<String, Expectations> browserExpectations) {
        final Map<String, Test> map = new HashMap<String, Test>();
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

        final List<Test> tests = new ArrayList<Test>(map.values());
        Collections.sort(tests);

        return tests;
    }
}

class Expectations implements Iterable<Expectation> {
    static Expectations readExpectations(final File file) throws IOException {
        final Expectations expectations = new Expectations();
        final List<String> lines = FileUtils.readLines(file);
        for (int i = 0; i < lines.size(); ++i) {
            expectations.add(new Expectation(i + 1, lines.get(i)));
        }

        return expectations;
    }

    private final Map<String, Expectation> expectations_ = new HashMap<String, Expectation>();

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

class Expectation {

    private static final Pattern pattern_ = Pattern.compile("(\\d+\\. ?)?(.+)\\((\\d+, \\d+, \\d+)\\)");
    private final int line_;
    private final String testName_;
    private final String testResult_;

    public Expectation(final int line, final String string) {
        line_ = line;
        final Matcher matcher = pattern_.matcher(string);
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid line " + line + ": " + string);
        }
        final String testNumber = matcher.group(1);
        if (testNumber != null && !testNumber.trim().equals(line + ".")) {
            throw new RuntimeException("Invalid test number for line " + line + ": " + string);
        }

        testName_ = matcher.group(2).trim();
        testResult_ = matcher.group(3);
    }

    public int getLine() {
        return line_;
    }

    public String getTestName() {
        return testName_;
    }

    public String getTestResult() {
        return testResult_;
    }
}

class Test implements Comparable<Test> {
    private final List<Integer> lines_ = new ArrayList<Integer>();
    private final String name_;

    public Test(final String name) {
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
