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
package com.gargoylesoftware.htmlunit.general;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ERROR_STACK_TRACE_LIMIT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner.Default;
import com.gargoylesoftware.htmlunit.annotations.ToRunWithRealBrowsers;
import com.gargoylesoftware.htmlunit.TestCaseTest;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

/**
 * Test all {@code constant}s defined in host classes.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserParameterizedRunner.class)
@ToRunWithRealBrowsers
public class HostConstantsTest extends WebDriverTestCase {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();
        final List<String> strings = TestCaseTest.getAllClassNames();
        for (final String host : strings) {
            list.add(new Object[] {host});
        }
        return list;
    }

    /**
     * The parent element name.
     */
    @Parameter
    public String host_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void test() throws Exception {
        setExpectedAlerts(getExpectedString());

        loadPageWithAlerts2("<html><head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  try {\n"
                + "    var all = [];\n"
                + "    for (var x in " + host_ + ") {\n"
                + "      if (typeof " + host_ + "[x] == 'number') {\n"
                + "        all.push(x);\n"
                + "      }\n"
                + "    }\n"
                + "    all.sort();\n"
                + "    var string = '';\n"
                + "    for (var i in all) {\n"
                + "      var x = all[i];\n"
                + "      string += x + ':' + " + host_ + "[x] + ' ';\n"
                + "    }\n"
                + "    alert(string);\n"
                + "  } catch (e) {\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "</body></html>");
    }

    private String getExpectedString() throws Exception {
        if (host_.endsWith("Array") || "Image".equals(host_) || "Option".equals(host_)) {
            return "";
        }
        if ("Error".equals(host_) && getBrowserVersion().hasFeature(JS_ERROR_STACK_TRACE_LIMIT)) {
            return "stackTraceLimit:10 ";
        }
        final JavaScriptConfiguration javaScriptConfig = JavaScriptConfiguration.getInstance(getBrowserVersion());
        final List<String> constants = new ArrayList<>();
        ClassConfiguration classConfig = javaScriptConfig.getClassConfiguration(host_);

        boolean first = true;
        while (classConfig != null) {
            if (first && !classConfig.isJsObject()) {
                break;
            }
            if (first || classConfig.getJsConstructor() != null) {
                for (final ConstantInfo constantInfo : classConfig.getConstants()) {
                    constants.add(constantInfo.getName() + ":" + constantInfo.getValue());
                }
            }
            classConfig = javaScriptConfig.getClassConfiguration(classConfig.getExtendedClassName());
            first = false;
        }

        Collections.sort(constants, new Comparator<String>() {

            @Override
            public int compare(final String o1, final String o2) {
                return o1.substring(0, o1.indexOf(':')).compareTo(o2.substring(0, o2.indexOf(':')));
            }
        });
        final StringBuilder builder = new StringBuilder();
        for (final String key : constants) {
            builder.append(key).append(' ');
        }
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }
}
