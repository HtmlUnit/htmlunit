/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Utility for automatically transforming Moalla JavaScript tests to WebDriver test cases.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class MozillaTestGenerator {

    private MozillaTestGenerator() { }

    /**
     * Outputs java test case for the specified JavaScript source.
     * @param author the author name
     * @param htmlunitRoot HtmlUnit root path
     * @param mozillaRoot Mozilla root path
     * @param jsPath relative JavaScript source path, e.g. "/js/src/tests/js1_2/regexp/everything.js"
     * @param initialScript whether another initial script is needed or not
     * @throws IOException if a reading error occurs
     */
    @SuppressWarnings("unchecked")
    public static void printMozillaTest(final String author, final String htmlunitRoot,
            final String mozillaRoot, final String jsPath, final boolean initialScript) throws IOException {
        for (final Object o : FileUtils.readLines(new File(htmlunitRoot, "LICENSE.txt"))) {
            System.out.println(o);
        }
        final String[] jsPathTokens = jsPath.split("/");
        System.out.println("package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla."
                + jsPathTokens[4] + ";");
        System.out.println();
        System.out.println("import org.junit.Test;");
        System.out.println("import org.junit.runner.RunWith;");
        System.out.println();
        System.out.println("import com.gargoylesoftware.htmlunit.BrowserRunner;");
        System.out.println("import com.gargoylesoftware.htmlunit.WebDriverTestCase;");
        System.out.println("import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;");
        System.out.println();
        System.out.println("/**");
        System.out.println(" * Tests originally in '" + jsPath + "'.");
        System.out.println(" *");
        System.out.println(" * @version $Revision$");
        System.out.println(" * @author " + author);
        System.out.println(" */");
        System.out.println("@RunWith(BrowserRunner.class)");
        String className = jsPathTokens[jsPathTokens.length - 1];
        className = Character.toUpperCase(className.charAt(0)) + className.substring(1, className.length() - 3);
        System.out.println("public class " + className + "Test extends WebDriverTestCase {");
        final List<String> lines = FileUtils.readLines(new File(mozillaRoot, jsPath));
        int testNumber = 1;
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.startsWith("new TestCase")) {
                if (line.endsWith(";")) {
                    System.out.println("ERROR...... test case ends with ; in " + (i + 1));
                    continue;
                }
                int x = i + 1;
                String next = lines.get(x++).trim();
                while (!next.endsWith(";")) {
                    next = lines.get(x++).trim();
                }
                final String expected = getExpected(next);
                final String script;
                if (next.startsWith("String(")) {
                    final int p0 = next.indexOf("String(", 1) + "String(".length();
                    script = next.substring(p0, next.length() - 3);
                }
                else if (next.startsWith("\"")) {
                    script = next.substring(expected.length() + 3, next.length() - 2).trim();
                }
                else {
                    script = next.substring(next.indexOf(',') + 1, next.length() - 2).trim();
                }
                System.out.println();
                System.out.println("    /**");
                System.out.println("     * Tests " + script + ".");
                System.out.println("     * @throws Exception if the test fails");
                System.out.println("     */");
                System.out.println("    @Test");
                System.out.println("    @Alerts(\"" + expected + "\")");
                System.out.println("    public void test" + testNumber++ + "() throws Exception {");
                if (initialScript) {
                    System.out.print("        test(initialScript, ");
                }
                else {
                    System.out.print("        test(");
                }
                System.out.println("\"" + script.replace("\\", "\\\\").replace("\"", "\\\"") + "\");");
                System.out.println("    }");
            }
        }
        System.out.println();
        if (initialScript) {
            System.out.println("    private void test(final String script) throws Exception {");
            System.out.println("        test(null, script);");
            System.out.println("    }");
            System.out.println("");
            System.out.println(
                    "    private void test(final String initialScript, final String script) throws Exception {");
            System.out.println("        String html = \"<html><head><title>foo</title><script>\\n\";");
            System.out.println("        if (initialScript != null) {");
            System.out.println("            html += initialScript + \";\\n\";");
            System.out.println("        }");
            System.out.println("        html += \"  alert(\" + script + \");\\n\"");
            System.out.println("            + \"</script></head><body>\\n\"");
            System.out.println("            + \"</body></html>\";");
            System.out.println("        loadPageWithAlerts2(html);");
            System.out.println("    }");
        }
        else {
            System.out.println("    private void test(final String script) throws Exception {");
            System.out.println("        final String html = \"<html><head><title>foo</title><script>\\n\"");
            System.out.println("            + \"  alert(\" + script + \");\\n\"");
            System.out.println("            + \"</script></head><body>\\n\"");
            System.out.println("            + \"</body></html>\";");
            System.out.println("        loadPageWithAlerts2(html);");
            System.out.println("    }");
        }
        System.out.println("}");
    }

    private static String getExpected(final String line) {
        if (line.startsWith("\"")) {
            final int p0 = 1;
            int p1 = p0 + 1;
            for (int i = p1; i < line.length() - 1; i++) {
                if (line.charAt(i) == '"' && line.charAt(i - 1) != '\\') {
                    p1 = i;
                    break;
                }
            }
            return line.substring(p0, p1);
        }
        else if (line.startsWith("'")) {
            final int p0 = 1;
            int p1 = p0 + 1;
            for (int i = p1; i < line.length() - 1; i++) {
                if (line.charAt(i) == '\'' && line.charAt(i - 1) != '\\') {
                    p1 = i;
                    break;
                }
            }
            return line.substring(p0, p1);
        }
        else if (line.startsWith("String([")) {
            final StringBuilder buffer = new StringBuilder();
            char terminator = line.charAt("String([".length());
            int p0 = "String([\"".length();
            if (terminator != '"' && terminator != '\'') {
                p0--;
                terminator = ']';
            }
            while (true) {
                int p1 = p0 + 1;
                int i = p1;
                if (line.charAt(p0) != terminator) {
                    for (i = p1; i < line.length() - 1; i++) {
                        if (line.charAt(i) == terminator && line.charAt(i - 1) != '\\') {
                            p1 = i;
                            break;
                        }
                    }
                }
                else {
                    i = p1 - 1;
                    p1 = p0;
                }
                if (buffer.length() > 0) {
                    buffer.append(',');
                }
                buffer.append(line.substring(p0, p1));
                if (terminator != '"' && terminator != '\'') {
                    break;
                }
                if (line.charAt(i + 1) == ']') {
                    break;
                }
                terminator = line.charAt(i + 2);
                p0 = i + 3;
            }
            return buffer.toString();
        }
        else {
            return line.substring(0, line.indexOf(','));
        }
    }

}
