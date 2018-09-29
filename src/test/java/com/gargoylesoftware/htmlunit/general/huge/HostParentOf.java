/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general.huge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import org.junit.After;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner.Default;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.TestCaseTest;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class HostParentOf extends WebDriverTestCase {

    /**
     * Returns the parameterized data.
     * @param predicate the predicate, which determines whether or not to include the parent
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    protected static Collection<Object[]> data(final Predicate<String> predicate)  throws Exception {
        final List<Object[]> list = new ArrayList<>();
        final List<String> strings = TestCaseTest.getAllClassNames();
        for (final String parent : strings) {
            if (predicate.test(parent)) {
                for (final String child : strings) {
                    list.add(new Object[] {parent, child});
                }
            }
        }
        return list;
    }

    /**
     * The parent element name.
     */
    @Parameter
    public String parent_;

    /**
     * The child element name.
     */
    @Parameter(1)
    public String child_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    @Default
    public void isParentOf() throws Exception {
        test(parent_, child_);
    }

    /**
     * Runs the test.
     *
     * @param parent the parent host name
     * @param child the child host name
     * @throws Exception if an error occurs
     */
    protected void test(final String parent, final String child) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head>\n"
            + "<title>-</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      document.title = isParentOf(" + parent + ", " + child + ");\n"
            + "    } catch(e) { document.title = 'false'; }\n"
            + "  }\n"

            + "  /*\n"
            + "   * Returns true if o1 prototype is parent/grandparent of o2 prototype\n"
            + "   */\n"
            + "  function isParentOf(o1, o2) {\n"
            + "    o1.prototype.myCustomFunction = function() {};\n"
            + "    return o2.prototype.myCustomFunction != undefined;\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

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

    @After
    public void after() {
        parent_ = null;
        child_ = null;
    }
}
