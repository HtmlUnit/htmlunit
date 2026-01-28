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
package org.htmlunit.general.huge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.htmlunit.TestCaseTest;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class HostParentOf extends WebDriverTestCase {

    private static int ServerRestartCount_ = 0;

    /**
     * Returns the parameterized data.
     * @param predicate the predicate, which determines whether or not to include the parent
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    protected static Collection<Arguments> data(final Predicate<String> predicate)  throws Exception {
        final Set<String> jsClassNames = TestCaseTest.getAllConfiguredJsConstructorNames();

        final List<Arguments> list = new ArrayList<>(jsClassNames.size() * jsClassNames.size() / 10);

        int i = 0;
        for (final String parent : jsClassNames) {
            if (predicate.test(parent)) {
                for (final String child : jsClassNames) {
                    list.add(Arguments.of(parent, child));
                }
                if (++i > 100) {
                    return list;
                }
            }
        }
        return list;
    }

    /**
     * Runs the test.
     *
     * @param parent the parent host name
     * @param child the child host name
     * @throws Exception if an error occurs
     */
    @ParameterizedTest(name = "_{0}_{1}", quoteTextArguments = false)
    @MethodSource("data")
    @Alerts("false/false")
    protected void test(final String parent, final String child) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title>-</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"

            + "  function isParentOf(p, c) {\n"
            + "    detector = function() {};\n"
            + "    p.prototype.myCustomFunction = detector;\n"
            + "    return (c.prototype.myCustomFunction === detector) + '/'"
            + "      + (Object.getPrototypeOf(c) === p);\n"
            + "  }\n"

            + "  try {\n"
            + "    document.title = isParentOf(" + parent + ", " + child + ");\n"
            + "  } catch(e) { document.title = 'false/false'; }\n"
            + "</script>\n"
            + "</body></html>";

        ServerRestartCount_++;
        if (ServerRestartCount_ == 200) {
            stopWebServers();
            ServerRestartCount_ = 0;
        }
        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }
}
