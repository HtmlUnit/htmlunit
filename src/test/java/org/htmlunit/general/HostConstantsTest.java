/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.general;

import static org.htmlunit.BrowserVersionFeatures.JS_ERROR_STACK_TRACE_LIMIT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.htmlunit.TestCaseTest;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.javascript.configuration.ClassConfiguration;
import org.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import org.htmlunit.javascript.configuration.JavaScriptConfiguration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test all {@code constant}s defined in host classes.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostConstantsTest extends WebDriverTestCase {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        final List<Arguments> list = new ArrayList<>();
        final Set<String> classNames = TestCaseTest.getAllConfiguredJsClassNames();
        final ArrayList<String> classNamesSorted = new ArrayList<>(classNames);
        Collections.sort(classNamesSorted);

        for (final String host : classNamesSorted) {
            if (!"Audio".equals(host)) {
                list.add(Arguments.of(host));
            }
        }
        return list;
    }

    @ParameterizedTest
    @MethodSource("data")
    void test(final String host) throws Exception {
        setExpectedAlerts(getExpectedString(host));

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "function test() {\n"
                + "  try {\n"
                + "    var all = [];\n"
                + "    for (var x in " + host + ") {\n"
                + "      if (typeof " + host + "[x] == 'number') {\n"
                + "        all.push(x);\n"
                + "      }\n"
                + "    }\n"
                + "    all.sort();\n"

                // uncomment to generate java code
                // + "    var helper = '';\n"
                // + "    for (var i in all) {\n"
                // + "      var x = all[i];\n"
                // + "      string += '\\r\\n    /** The constant {@code ' + x + '}. */\\r\\n';\n"
                //    + "      string += '    @JsxConstant\\r\\n';\n"
                //    + "      string += '    public static final long ' + x + ' = ' + " + host_ + "[x] + 'L;\\r\\n';\n"
                //    + "    }\n"
                //    + "    alert(string);\n"

                + "    for (var i in all) {\n"
                + "      var x = all[i];\n"
                + "      log(x + ':' + " + host + "[x]);\n"
                + "    }\n"
                + "  } catch(e) {}\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    private String[] getExpectedString(final String host) throws Exception {
        if (host.endsWith("Array") || "Image".equals(host) || "Option".equals(host)) {
            return new String[0];
        }
        if ("Error".equals(host) && getBrowserVersion().hasFeature(JS_ERROR_STACK_TRACE_LIMIT)) {
            return new String[] {"stackTraceLimit:10"};
        }

        final JavaScriptConfiguration javaScriptConfig = JavaScriptConfiguration.getInstance(getBrowserVersion());
        final HashMap<String, ClassConfiguration> classConfigurationIndex = new HashMap<>();
        for (final ClassConfiguration config : javaScriptConfig.getAll()) {
            classConfigurationIndex.put(config.getClassName(), config);
        }

        final List<String> constants = new ArrayList<>();
        ClassConfiguration classConfig = classConfigurationIndex.get(host);

        boolean first = true;
        while (classConfig != null) {
            if (first && !classConfig.isJsObject()) {
                break;
            }
            if (first || classConfig.getJsConstructor() != null) {
                final List<ConstantInfo> constantInfos = classConfig.getConstants();
                if (constantInfos != null) {
                    for (final ConstantInfo constantInfo : constantInfos) {
                        constants.add(constantInfo.getName() + ":" + constantInfo.getValue());
                    }
                }
            }
            classConfig = classConfigurationIndex.get(classConfig.getExtendedClassName());
            first = false;
        }

        constants.sort((o1, o2) -> o1.substring(0, o1.indexOf(':')).compareTo(o2.substring(0, o2.indexOf(':'))));
        return constants.toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }
}
