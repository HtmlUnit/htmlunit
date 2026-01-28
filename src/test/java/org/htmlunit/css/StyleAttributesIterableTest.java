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
package org.htmlunit.css;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.css.StyleAttributes.Definition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for iterability of CSS style attributes defined in {@link StyleAttributes}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class StyleAttributesIterableTest extends WebDriverTestCase {

    private static int ServerRestartCount_ = 0;

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        final List<Arguments> list = new ArrayList<>();
        for (final Definition definition : StyleAttributes.Definition.values()) {
            list.add(Arguments.of(definition));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getExpectedAlerts() {
        if (definition_ == null) {
            return super.getExpectedAlerts();
        }
        final BrowserVersion browserVersion = getBrowserVersion();
        return new String[] {Boolean.toString(definition_.isAvailable(browserVersion, true))};
    }

    /**
     * The {@link Definition} to test.
     */
    private Definition definition_;

    /**
     * The default test.
     * @param definition the definition param
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @MethodSource("data")
    public void test(final Definition definition) throws Exception {
        definition_ = definition;
        test(definition_.getPropertyName());
    }

    private void test(final String propertyName) throws Exception {
        ServerRestartCount_++;
        if (ServerRestartCount_ == 200) {
            stopWebServers();
            ServerRestartCount_ = 0;
        }

        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    for (var i in e.style) {\n"
            + "      if (i == '" + propertyName + "') {\n"
            + "        log('true');\n"
            + "        return;\n"
            + "      }\n"
            + "    }\n"
            + "    log('false');\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
