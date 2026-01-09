/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import java.net.URL;
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
import org.htmlunit.javascript.configuration.WorkerJavaScriptConfiguration;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests that constants class are correct.
 *
 * @author Ronald Brill
 */
public class DedicatedWorkerGlobalScopeConstantsTest extends WebDriverTestCase {

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

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @ParameterizedTest(name = "_{0}", quoteTextArguments = false)
    @MethodSource("data")
    void test(final String host) throws Exception {
        test(host, getExpectedString(host));
    }

    private void test(final String className, final String[] expectedAlerts) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "try {\n"
                + "  var myWorker = new Worker('worker.js');\n"
                + "  myWorker.onmessage = function(e) {\n"
                + "    window.document.title = '' + e.data;\n"
                + "  };\n"
                + "  setTimeout(function() { myWorker.postMessage('test');}, 10);\n"
                + "} catch(e) { window.document.title = 'exception'; }\n"
                + "</script></body></html>\n";

        final String workerJs = "onmessage = function(e) {\n"
                + "  var workerResult = '';\n"
                + "  try {\n"
                + "    var all = [];\n"
                + "    for (var x in " + className + ") {\n"
                + "      if (typeof " + className + "[x] == 'number') {\n"
                + "        all.push(x);\n"
                + "      }\n"
                + "    }\n"
                + "    all.sort();\n"

                + "    for (var i in all) {\n"
                + "      var x = all[i];\n"
                + "      workerResult += x + ':' + " + className + "[x] + '\\u00a7';\n"
                + "    }\n"
                + "  } catch(e) {}\n"
                + "  postMessage(workerResult);\n"
                + "}\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.TEXT_JAVASCRIPT);

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), expectedAlerts);
    }

    private String[] getExpectedString(final String className) {
        final String[] expectedAlerts = getExpectedAlerts();

        if (expectedAlerts != null && expectedAlerts.length == 1
                && "-".equals(expectedAlerts[0])) {
            return new String[0];
        }

        if (expectedAlerts != null && expectedAlerts.length > 0) {
            return expectedAlerts;
        }

        if (className.endsWith("Array") || "Image".equals(className) || "Option".equals(className)) {
            return new String[0];
        }
        if ("Error".equals(className) && getBrowserVersion().hasFeature(JS_ERROR_STACK_TRACE_LIMIT)) {
            return new String[] {"stackTraceLimit:10"};
        }

        final WorkerJavaScriptConfiguration javaScriptConfig
                = WorkerJavaScriptConfiguration.getInstance(getBrowserVersion());
        final HashMap<String, ClassConfiguration> classConfigurationIndex = new HashMap<>();
        for (final ClassConfiguration config : javaScriptConfig.getAll()) {
            classConfigurationIndex.put(config.getClassName(), config);
        }

        final List<String> constants = new ArrayList<>();
        ClassConfiguration classConfig = classConfigurationIndex.get(className);

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

    /**
     * @throws Exception if the test fails
     */
    @Alerts(CHROME = {"AT_TARGET:2", "BUBBLING_PHASE:3", "CAPTURING_PHASE:1", "NONE:0"},
            EDGE = {"AT_TARGET:2", "BUBBLING_PHASE:3", "CAPTURING_PHASE:1", "NONE:0"},
            FF = "-",
            FF_ESR = "-")
    void _SecurityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent", getExpectedString("SecurityPolicyViolationEvent"));
    }
}
