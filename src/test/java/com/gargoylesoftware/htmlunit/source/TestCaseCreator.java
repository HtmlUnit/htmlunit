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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Prints out a tiny test case, from a provided HTML file.
 *
 * @author Ahmed Ashour
 */
public final class TestCaseCreator {

    /**
     * The entry point.
     *
     * @param args the arguments
     * @throws IOException if an error occurs
     */
    public static void main(final String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("HTML file location is not provided");
            return;
        }

        final File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("File does not exist " + file.getAbsolutePath());
        }

        System.out.println("        /**");
        System.out.println("         * @throws Exception if an error occurs");
        System.out.println("         */");
        System.out.println("        @Test");
        System.out.println("        @Alerts()");
        System.out.println("        public void test() throws Exception {");

        final List<String> lines = FileUtils.readLines(file, ISO_8859_1);
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (i == 0) {
                System.out.println("            final String html = \"" + line.replace("\"", "\\\"") + "\\n\"");
            }
            else {
                System.out.print("                + \"" + line.replace("\"", "\\\"") + "\\n\"");
                if (i == lines.size() - 1) {
                    System.out.print(";");
                }
                System.out.println();
            }
        }
        System.out.println("            loadPageWithAlerts2(html);");
        System.out.println("        }");
    }

    private TestCaseCreator() {
    }
}
