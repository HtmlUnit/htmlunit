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

import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Use to generate test cases.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class TestSource {

    private TestSource() { }

    /**
     * Generate test case for each one HTML elements.
     * @param testNamePrefix the prefix of the test name
     * @param htmlGeneratorMethod the method name which is called to generate the HTML, it expects a tag name parameter
     * @param defaultAlerts default string inside the parenthesis of <tt>@Alerts()</tt>, can be null
     */
    public static void generateTestForHtmlElements(final String testNamePrefix, final String htmlGeneratorMethod,
            final String defaultAlerts) {
        for (final String tag : HtmlPageTest.HTML_TAGS_) {
            System.out.println();
            System.out.println("    /**");
            System.out.println("     * @throws Exception if the test fails");
            System.out.println("     */");
            System.out.println("    @Test");
            System.out.print("    @Alerts(");
            if (defaultAlerts != null) {
                System.out.print(defaultAlerts);
            }
            System.out.println(")");
            System.out.println("    public void " + testNamePrefix + '_' + tag + "() throws Exception {");
            System.out.println("        loadPageWithAlerts2(" + htmlGeneratorMethod + "(\"" + tag + "\"));");
            System.out.println("    }");
        }
    }
}
