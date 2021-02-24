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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner.Default;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.annotations.ToRunWithRealBrowsers;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes.Definition;

/**
 * Tests for iterability of CSS style attributes defined in {@link StyleAttributes}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
@ToRunWithRealBrowsers
public class StyleAttributesIterableTest extends WebDriverTestCase {

    private static int ServerRestartCount_ = 0;

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();
        for (final Definition definition : StyleAttributes.Definition.values()) {
            list.add(new Object[] {definition});
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
    @Parameter
    public Definition definition_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void test() throws Exception {
        test(definition_.getPropertyName());
    }

    private void test(final String propertyName) throws Exception {
        ServerRestartCount_++;
        if (ServerRestartCount_ == 200) {
            stopWebServers();
            ServerRestartCount_ = 0;
        }

        final String html =
            "<html><head><script>\n"
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
