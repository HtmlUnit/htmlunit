/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
            if (file.isDirectory() && !file.getName().equals(".svn")) {
                process(file);
            }
            else {
                if (file.getName().endsWith(".java") && !file.getName().equals("WebTestCase.java")
                        && !file.getName().equals("NotYetImplementedTest.java")
                        && !file.getName().equals("CodeStyleTest.java")) {
                    final List<String> lines = CodeStyleTest.getLines(file);
                    final String relativePath = file.getAbsolutePath().substring(
                        new File(".").getAbsolutePath().length() - 1).replace('\\', '/');
                    process(lines, relativePath);
                }
            }
        }
    }

    private void process(final List<String> lines, final String path) {
        int index = 1;
        for (final String line : lines) {
            if (line.contains("notYetImplemented()")) {
                String methodName = null;
                int lineNumber = -1;
                for (int i = index; i >= 0; i--) {
                    final String l = lines.get(i);
                    if (l.startsWith("    public ")) {
                        methodName = l.split(" ")[6];
                        break;
                    }
                }
                for (int i = index; i >= 0; i--) {
                    final String l = lines.get(i);
                    if (l.startsWith("    /**")) {
                        lineNumber = i;
                        break;
                    }
                }
                entries_.add(path + ',' + methodName + ',' + lineNumber);
            }
            else if (line.contains("@NotYetImplemented")) {
                String methodName = null;
                int lineNumber = -1;
                for (int i = index; i < lines.size(); i++) {
                    final String l = lines.get(i);
                    if (l.startsWith("    public ")) {
                        methodName = l.split(" ")[6];
                        break;
                    }
                }
                for (int i = index; i >= 0; i--) {
                    final String l = lines.get(i);
                    if (l.startsWith("    /**")) {
                        lineNumber = i;
                        break;
                    }
                }
                entries_.add(path + ',' + methodName + ',' + lineNumber);
            }
            index++;
        }
    }

    private void save() throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head><body>\n");
        sb.append("NotYetImplemented is a condition in which a test is known to fail with HtmlUnit.");
        sb.append("<table border='1'>\n");
        sb.append("  <tr><th>File</th><th>Method</th><th>Line</th></tr>\n");
        String lastFile = null;
        for (final String entry : entries_) {
            final String[] values = entry.split(",");
            final String file = values[0];
            final String fileName = file.substring(file.lastIndexOf('/') + 1, file.length() - 5);
            final String method = values[1];
            final String line = values[2];
            sb.append("  <tr>\n");
            if (!file.equals(lastFile)) {
                int totalCount = 0;
                for (final String e : entries_) {
                    if (e.startsWith(file)) {
                        totalCount++;
                    }
                }
                if (totalCount != 1) {
                    sb.append("    <td rowspan='" + totalCount + "'>");
                }
                else {
                    sb.append("    <td>");
                }
                sb.append(fileName);
                sb.append("</td>\n");
                lastFile = file;
            }
            sb.append("    <td><a href='http://htmlunit.svn.sourceforge.net/viewvc/htmlunit/trunk/htmlunit/" + file + "?view=markup#l_" + line + "'>").append(method).append("</a></td>\n");
            sb.append("    <td>").append(line).append("</td>\n");
            sb.append("  </tr>\n");
        }
        sb.append("</table>\n").append("</body></html>");
        FileUtils.writeStringToFile(new File(PropertiesTest.getArtifactsDirectory(), "notYetImplemented.html"),
                sb.toString());
    }

}
