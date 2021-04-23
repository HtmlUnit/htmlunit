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
package com.gargoylesoftware.htmlunit.runners;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.ComparisonFailure;
import org.junit.runners.model.FrameworkMethod;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CodeStyleTest;
import com.gargoylesoftware.htmlunit.general.HostExtractor;

/**
 * This is meant to automatically correct the test case to put either the real browser expectations,
 * or the {@link com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented} annotation for HtmlUnit.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
final class TestCaseCorrector {

    private TestCaseCorrector() {
    }

    static void correct(final FrameworkMethod method, final boolean realBrowser, final BrowserVersion browserVersion,
            final boolean notYetImplemented, final Throwable t) throws IOException {
        final String testRoot = "src/test/java/";
        final String browserString = browserVersion.getNickname().toUpperCase(Locale.ROOT);
        final File file = new File(testRoot + method.getDeclaringClass().getName().replace('.', '/') + ".java");
        final List<String> lines = FileUtils.readLines(file, UTF_8);
        final String methodLine = "    public void " + method.getName() + "()";
        if (realBrowser) {
            String defaultExpectation = null;
            for (int i = 0; i < lines.size(); i++) {
                if ("    @Default".equals(lines.get(i))) {
                    defaultExpectation = getDefaultExpectation(lines, i);
                }
                if (lines.get(i).startsWith(methodLine)) {
                    i = addExpectation(lines, i, browserString, (ComparisonFailure) t);
                    break;
                }
                if (i == lines.size() - 2) {
                    addMethodWithExpectation(lines, i, browserString, method.getName(), (ComparisonFailure) t,
                            defaultExpectation);
                    break;
                }
            }
        }
        else if (!notYetImplemented) {
            String defaultExpectation = null;
            for (int i = 0; i < lines.size(); i++) {
                if ("    @Default".equals(lines.get(i))) {
                    defaultExpectation = getDefaultExpectation(lines, i);
                }
                if (lines.get(i).startsWith(methodLine)) {
                    addNotYetImplemented(lines, i, browserString);
                    break;
                }
                if (i == lines.size() - 2) {
                    addNotYetImplementedMethod(lines, i, browserString, method.getName(), defaultExpectation);
                    break;
                }
            }
        }
        else {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(methodLine)) {
                    removeNotYetImplemented(lines, i, browserString);
                    break;
                }
            }
        }
        FileUtils.writeLines(file, UTF_8.name(), lines);
    }

    private static String getDefaultExpectation(final List<String> lines, final int defaultIndex) {
        int index = defaultIndex;
        while (index >= 0 && !lines.get(index).contains("Alerts")) {
            index--;
        }
        if (index >= 0) {
            final String line = lines.get(index);
            return line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
        }
        return null;
    }

    private static int addExpectation(final List<String> lines, int i,
            final String browserString, final ComparisonFailure comparisonFailure) {
        while (!lines.get(i).startsWith("    @Alerts")) {
            i--;
        }
        final List<String> alerts = CodeStyleTest.alertsToList(lines, i);
        for (final Iterator<String> it = alerts.iterator(); it.hasNext();) {
            if (it.next().startsWith(browserString + " = ")) {
                it.remove();
            }
        }
        alerts.add(browserString + " = " + getActualString(comparisonFailure));
        lines.remove(i);
        while (lines.get(i).startsWith("        ")) {
            lines.remove(i);
        }

        Collections.sort(alerts);
        String defaultAlert = null;
        for (final String alert : alerts) {
            if (alert.startsWith("DEFAULT = ")) {
                defaultAlert = alert;
                break;
            }
        }

        if (defaultAlert != null) {
            alerts.remove(defaultAlert);
            alerts.add(0, defaultAlert);
        }

        for (int x = 0; x < alerts.size(); x++) {
            String line = alerts.get(x);
            if (x == 0) {
                if (!line.contains(" = ")) {
                    line = "DEFAULT = " + line;
                }
                line = "    @Alerts(" + line;
            }
            else {
                line = "            " + line;
            }
            if (x < alerts.size() - 1) {
                line += ",";
            }
            else {
                line += ")";
            }
            lines.add(i++, line);
        }
        return i;
    }

    private static String getActualString(final ComparisonFailure failure) {
        String actual = failure.getActual();
        actual = actual.substring(1, actual.length() - 1);
        actual = StringEscapeUtils.escapeJava(actual);
        if (actual.length() > 96) {
            final StringBuilder builder = new StringBuilder();
            while (!actual.isEmpty()) {
                int length = actual.lastIndexOf(',', 96) + 1;
                if (length == 0 && !actual.isEmpty()) {
                    length = Math.min(96, actual.length());
                }
                if (builder.length() != 0) {
                    builder.append(System.lineSeparator()).append("                + ");
                }
                builder.append('"').append(actual.substring(0, length)).append('"');
                actual = actual.substring(length);
            }
            return builder.toString();
        }
        return "\"" + actual + "\"";
    }

    private static void removeNotYetImplemented(final List<String> lines,
            final int i, final String browserString) {
        final String previous = lines.get(i - 1);
        if (previous.contains("@NotYetImplemented")) {
            if (previous.indexOf('(') != -1) {
                final int p0 = previous.indexOf('(') + 1;
                final int p1 = previous.lastIndexOf(')');
                String browsers = previous.substring(p0, p1);
                if (browsers.indexOf('{') != -1) {
                    browsers = browsers.substring(1, browsers.length() - 1).trim();
                }
                final Set<String> browserSet = new HashSet<>();
                for (final String browser : browsers.split(",")) {
                    browserSet.add(browser.trim());
                }
                browserSet.remove(browserString);
                if (browserSet.size() == 1) {
                    lines.set(i - 1, "    @NotYetImplemented(" + browserSet.iterator().next() + ")");
                }
                else if (browserSet.size() > 1) {
                    lines.set(i - 1, "    @NotYetImplemented({" + String.join(", ", browserSet) + "})");
                }
                else {
                    lines.remove(i - 1);
                }
            }
            else {
                final List<String> allBrowsers = new ArrayList<>(Arrays.asList("CHROME", "EDGE", "FF", "FF78", "IE"));
                for (final Iterator<String> it = allBrowsers.iterator(); it.hasNext();) {
                    if (it.next().equals(browserString)) {
                        it.remove();
                    }
                }
                lines.set(i - 1, "    @NotYetImplemented({" + String.join(", ", allBrowsers) + "})");
            }
        }
    }

    private static void addNotYetImplementedMethod(final List<String> lines,
            int i, final String browserString, final String methodName, final String defaultExpectations) {
        String parent = methodName;
        final String child = parent.substring(parent.lastIndexOf('_') + 1);
        parent = parent.substring(1, parent.indexOf('_', 1));

        if (!lines.get(i).isEmpty()) {
            i++;
        }
        lines.add(i++, "");
        lines.add(i++, "    /**");
        lines.add(i++, "     * @throws Exception if the test fails");
        lines.add(i++, "     */");
        lines.add(i++, "    @Test");
        lines.add(i++, "    @Alerts(\"" + defaultExpectations + "\")");
        lines.add(i++, "    @NotYetImplemented(" + browserString + ")");
        lines.add(i++, "    public void _" + parent + "_" + child + "() throws Exception {");
        lines.add(i++, "        test(\"" + parent + "\", \"" + child + "\");");
        lines.add(i++, "    }");
        lines.add(i++, "}");
        while (lines.size() > i) {
            lines.remove(i);
        }
    }

    private static void addNotYetImplemented(final List<String> lines, final int i, final String browserString) {
        final String previous = lines.get(i - 1);
        if (previous.contains("@NotYetImplemented")) {
            if (previous.indexOf('(') != -1 && !previous.contains(browserString)) {
                final int p0 = previous.indexOf('(') + 1;
                final int p1 = previous.lastIndexOf(')');
                String browsers = previous.substring(p0, p1);
                if (browsers.indexOf('{') != -1) {
                    browsers = browsers.substring(1, browsers.length() - 1).trim();
                }
                browsers += ", " + browserString;
                lines.set(i - 1, "    @NotYetImplemented({" + browsers + "})");
            }
        }
        else {
            lines.add(i, "    @NotYetImplemented(" + browserString + ")");
        }
    }

    private static void addMethodWithExpectation(final List<String> lines,
            int i, final String browserString, final String methodName, final ComparisonFailure comparisonFailure,
            final String defaultExpectations) {
        String parent = methodName;
        final String child = parent.substring(parent.lastIndexOf('_') + 1);
        final int index = parent.indexOf('_', 1);
        if (index != -1) {
            parent = parent.substring(1, index);
        }
        else {
            parent = parent.substring(1);
        }

        if (!lines.get(i).isEmpty()) {
            i++;
        }
        lines.add(i++, "");
        lines.add(i++, "    /**");
        lines.add(i++, "     * @throws Exception if the test fails");
        lines.add(i++, "     */");
        lines.add(i++, "    @Test");
        lines.add(i++, "    @Alerts(DEFAULT = \"" + defaultExpectations + "\",");
        lines.add(i++, "            " + browserString + " = " + getActualString(comparisonFailure) + ")");
        if (index != -1) {
            lines.add(i++, "    public void _" + parent + "_" + child + "() throws Exception {");
            lines.add(i++, "        test(\"" + parent + "\", \"" + child + "\");");
        }
        else {
            String method = parent;
            for (final String prefix : HostExtractor.PREFIXES_) {
                if (method.startsWith(prefix)) {
                    method = prefix.toLowerCase(Locale.ROOT) + method.substring(prefix.length());
                    break;
                }
            }
            if (Character.isUpperCase(method.charAt(0))) {
                method = Character.toLowerCase(method.charAt(0)) + method.substring(1);
            }
            lines.add(i++, "    public void " + method + "() throws Exception {");
            lines.add(i++, "        test(\"" + parent + "\");");
        }
        lines.add(i++, "    }");
        lines.add(i++, "}");
        while (lines.size() > i) {
            lines.remove(i);
        }
    }
}
