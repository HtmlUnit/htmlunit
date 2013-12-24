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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.javascript.host.PropertiesTest;

/**
 * Generates HTML file with all <tt>NotYetImplemented</tt> methods.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class NotYetImplementedTest {

    private Set<String> entries_ = new TreeSet<String>();

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        process(new File("src/test/java"));
        save();
    }

    private void process(final File dir) throws IOException {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !".svn".equals(file.getName())) {
                process(file);
            }
            else {
                if (file.getName().endsWith(".java") && !"SimpleWebTestCase.java".equals(file.getName())
                        && !"NotYetImplementedTest.java".equals(file.getName())
                        && !"CodeStyleTest.java".equals(file.getName())) {
                    final List<String> lines = FileUtils.readLines(file);
                    final String relativePath = file.getAbsolutePath().substring(
                        new File(".").getAbsolutePath().length() - 1).replace('\\', '/');
                    process(lines, relativePath);
                }
            }
        }
    }

    private void process(final List<String> lines, final String path) {
        int index = 1;
        String revision = "-1";
        for (final String line : lines) {
            if (line.contains("@NotYetImplemented()")) {
                String methodName = null;
                for (int i = index; i >= 0; i--) {
                    final String l = lines.get(i);
                    if (l.startsWith("    public ")) {
                        methodName = l.split(" ")[6];
                        break;
                    }
                }
                final int lineNumber = getLineNumber(lines, index);
                final String description = getDescription(lines, index);
                entries_.add(path + ';' + revision + ';' + methodName + ';' + lineNumber + ';' + description);
            }
            else if (line.startsWith("    @NotYetImplemented")) {
                String browser;
                if (line.contains("(")) {
                    browser = line.replaceAll(".*\\((.*)\\).*", "$1");
                    browser = browser.replaceAll("Browser\\.", "");
                    browser = browser.replaceAll("[{}]", "");
                    browser = browser.trim();
                }
                else {
                    browser = "";
                }
                String methodName = null;
                for (int i = index; i < lines.size(); i++) {
                    final String l = lines.get(i);
                    if (l.startsWith("    public ")) {
                        methodName = l.split(" ")[6];
                        break;
                    }
                }
                final int lineNumber = getLineNumber(lines, index);
                final String description = getDescription(lines, index);
                entries_.add(path + ';' + revision + ';' + methodName + ';' + lineNumber + ";" + browser
                        + ';' + description);
            }
            else if (line.startsWith(" * @version $Revision: ")) {
                revision = line.substring(" * @version $Revision: ".length(), line.lastIndexOf(' '));
            }
            index++;
        }
    }

    private static int getLineNumber(final List<String> lines, final int index) {
        for (int i = index; i >= 0; i--) {
            final String l = lines.get(i);
            if (l.startsWith("    /**")) {
                return i;
            }
        }
        return 0;
    }

    private static String getDescription(final List<String> lines, final int index) {
        final StringBuilder builder = new StringBuilder();
        for (int i = getLineNumber(lines, index); i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            final int start = line.indexOf(' ') != -1 ? line.indexOf(' ') + 1 : -1;
            final boolean end = line.endsWith("*/");
            if (line.contains("* @throws ") || line.contains("* @exception")) {
                break;
            }
            if (start != -1) {
                if (builder.length() != 0) {
                    builder.append(' ');
                }
                builder.append(line.substring(start, line.length() - (end ? 2 : 0)));
            }
            if (end) {
                break;
            }
        }
        return builder.toString().replace(";", "__semicolon__");
    }

    private void save() throws Exception {
        final StringBuilder builder = new StringBuilder();
        builder.append("<html><head>\n");
        builder.append("<style type=\"text/css\">\n");
        builder.append("table.bottomBorder { border-collapse:collapse; }\n");
        builder.append("table.bottomBorder td, table.bottomBorder th { "
                            + "border-bottom:1px dotted black;padding:5px; }\n");
        builder.append("table.bottomBorder td.numeric { text-align:right; }\n");
        builder.append("</style>\n");
        builder.append("</head><body>\n");
        builder.append("<p>NotYetImplemented is a condition in which a test is known to fail with HtmlUnit.</p>");
        // statistics
        builder.append("<h3>Overview</h3>");
        final int overviewPos = builder.length();
        // per browser

        // details
        builder.append("<h3>Details</h3>");
        builder.append("<table class='bottomBorder'>\n");
        builder.append("  <tr><th>File</th><th>#</th><th>Method</th><th>Line</th><th>Description</th></tr>\n");
        String lastFile = null;

        int count = 0;
        int countIE8 = 0;
        int countIE9 = 0;
        int countIE11 = 0;
        int countFF17 = 0;
        int countFF24 = 0;
        for (final String entry : entries_) {
            final String[] values = entry.split(";");
            final String file = values[0];
            final String fileName = file.substring(file.lastIndexOf('/') + 1, file.length() - 5);
            final String revision = values[1];
            final String method = values[2];
            final String line = values[3];
            final String browser = values.length >= 5 ? values[4] : "All";
            final String description = entry.endsWith(";") ? "&nbsp;"
                    : values[values.length - 1].replace("__semicolon__", ";");
            builder.append("  <tr>\n");
            if (!file.equals(lastFile)) {
                int totalCount = 0;
                for (final String e : entries_) {
                    if (e.startsWith(file)) {
                        totalCount++;
                    }
                }
                if (totalCount != 1) {
                    builder.append("    <td rowspan='" + totalCount + "'>");
                }
                else {
                    builder.append("    <td>");
                }
                builder.append(fileName);
                builder.append("</td>\n");
                lastFile = file;
            }
            builder.append("    <td>").append(Integer.toString(count++)).append("</td>\n");
            builder.append("    <td><a href='https://sourceforge.net/p/htmlunit/code/" + revision
                    + "/tree/trunk/htmlunit/" + file + "#l" + line + "'>").append(method).append("</a> ")
                    .append(browser).append("</td>\n");
            builder.append("    <td class='numeric'>").append(line).append("</td>\n");
            builder.append("    <td>").append(description).append("</td>\n");
            builder.append("  </tr>\n");

            if (browser.contains("IE8")) {
                countIE8++;
            }
            if (browser.contains("IE11")) {
                countIE11++;
            }
            if (browser.contains("IE9")) {
                countIE9++;
            }
            if (!browser.contains("IE8")
                    && !browser.contains("IE8")
                    && !browser.contains("IE9")
                    && browser.contains("IE11")) {
                countIE8++;
                countIE9++;
                countIE11++;
            }

            if (browser.contains("FF17")) {
                countFF17++;
            }
            if (browser.contains("FF24")) {
                countFF24++;
            }
            if (!browser.contains("FF17")
                    && !browser.contains("FF24")
                    && browser.contains("FF")) {
                countFF17++;
                countFF24++;
            }
            if (browser.contains("All")) {
                countIE8++;
                countIE9++;
                countIE11++;
                countFF17++;
                countFF24++;
            }

        }
        builder.append("</table>\n").append("</body></html>");

        final StringBuilder overview = new StringBuilder();
        overview.append("<table class='bottomBorder'>\n");
        overview.append("  <tr>\n");
        overview.append("    <td class='numeric'>").append(Integer.toString(count)).append("</td>\n");
        overview.append("    <td>methods marked as NotYetImplemented</td>\n");
        overview.append("  </tr>\n");

        overview.append("  <tr>\n");
        overview.append("    <td class='numeric'>").append(Integer.toString(countIE8)).append("</td>\n");
        overview.append("    <td>for IE8</td>\n");
        overview.append("  </tr>\n");

        overview.append("  <tr>\n");
        overview.append("    <td class='numeric'>").append(Integer.toString(countIE9)).append("</td>\n");
        overview.append("    <td>for IE9</td>\n");
        overview.append("  </tr>\n");

        overview.append("  <tr>\n");
        overview.append("    <td class='numeric'>").append(Integer.toString(countIE11)).append("</td>\n");
        overview.append("    <td>for IE11</td>\n");
        overview.append("  </tr>\n");

        overview.append("  <tr>\n");
        overview.append("    <td class='numeric'>").append(Integer.toString(countFF17)).append("</td>\n");
        overview.append("    <td>for FF17</td>\n");
        overview.append("  </tr>\n");

        overview.append("  <tr>\n");
        overview.append("    <td class='numeric'>").append(Integer.toString(countFF24)).append("</td>\n");
        overview.append("    <td>for FF24</td>\n");
        overview.append("  </tr>\n");
        overview.append("</table>\n");

        builder.insert(overviewPos, overview);

        FileUtils.writeStringToFile(new File(PropertiesTest.getArtifactsDirectory(), "notYetImplemented.html"),
                builder.toString());
    }

}
