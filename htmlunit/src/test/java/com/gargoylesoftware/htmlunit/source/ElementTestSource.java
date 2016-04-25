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
package com.gargoylesoftware.htmlunit.source;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfigurationTest;

/**
 * Use to generate test cases similar to the ones in the 'general' package.
 *
 * @author Ahmed Ashour
 */
public final class ElementTestSource {

    private ElementTestSource() { }

    /**
     * Generate test case for each one HTML elements.
     * @param htmlGeneratorMethod the method name which is called to generate the HTML, it expects a tag name parameter
     * @param defaultAlerts default string inside the parenthesis of <tt>@Alerts</tt>, can be null
     */
    public static void generateTestForHtmlElements(final String htmlGeneratorMethod,
            final String defaultAlerts) {
        final Map<String, String> namesMap = new HashMap<>();
        for (final String className : JavaScriptConfigurationTest.getClassesForPackage(HtmlPage.class)) {
            try {
                final Class<?> c = Class.forName(className);
                final Field field = c.getDeclaredField("TAG_NAME");
                namesMap.put(field.get(null).toString(), c.getName());
                try {
                    final Field field2 = c.getDeclaredField("TAG_NAME2");
                    namesMap.put(field2.get(null).toString(), c.getName());
                }
                catch (final Exception e) {
                    //ignore
                }
            }
            catch (final Exception e) {
                //ignore
            }
        }
        for (final String tag : HtmlPageTest.HTML_TAGS_) {
            System.out.println();
            System.out.println("    /**");
            if (namesMap.containsKey(tag)) {
                System.out.println("     * Test {@link " + namesMap.get(tag) + "}.");
                System.out.println("     *");
            }
            System.out.println("     * @throws Exception if the test fails");
            System.out.println("     */");
            System.out.println("    @Test");
            System.out.print("    @Alerts(");
            if (defaultAlerts != null) {
                System.out.print(defaultAlerts);
            }
            System.out.println(")");
            System.out.println("    public void " + tag + "() throws Exception {");
            System.out.println("        loadPageWithAlerts2(" + htmlGeneratorMethod + "(\"" + tag + "\"));");
            System.out.println("    }");
        }
    }
}
