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
package org.htmlunit.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebTestCase;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.Statement;

/**
 * The custom runner <code>BrowserRunner</code> implements browser parameterized
 * tests. When running a test class, instances are created for the
 * cross-product of the test methods and {@link BrowserVersion}s.
 *
 * For example, write:
 * <pre>
 * &#064;RunWith(BrowserRunner.class)
 * public class SomeTest extends WebTestCase {
 *
 *     &#064;Test
 *     public void test() {
 *         getBrowserVersion();// returns the currently used BrowserVersion
 *     }
 * }
 * </pre>
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author cd alexndr
 */
public class BrowserRunner extends Suite {

    static final String REAL_CHROME = "chrome";
    static final String REAL_FIREFOX = "ff";
    static final String REAL_FIREFOX_ESR = "ff-esr";
    static final String REAL_EDGE = "edge";

    static final String HTMLUNIT_CHROME = "hu-chrome";
    static final String HTMLUNIT_FIREFOX = "hu-ff";
    static final String HTMLUNIT_FIREFOX_ESR = "hu-ff-esr";
    static final String HTMLUNIT_EDGE = "hu-edge";

    private final ArrayList<Runner> runners_ = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param klass the test case class
     * @throws Throwable If an exception occurs
     */
    public BrowserRunner(final Class<WebTestCase> klass) throws Throwable {
        super(klass, Collections.<Runner>emptyList());

        if (BrowserVersionClassRunner.containsTestMethods(klass)) {
            final Set<String> browsers = WebDriverTestCase.getBrowsersProperties();
            if (WebDriverTestCase.class.isAssignableFrom(klass)) {
                if (browsers.contains(REAL_CHROME)) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.CHROME, true));
                }
                if (browsers.contains(REAL_FIREFOX_ESR)) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_ESR, true));
                }
                if (browsers.contains(REAL_FIREFOX)) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX, true));
                }
                if (browsers.contains(REAL_EDGE)) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.EDGE, true));
                }
            }

            if (browsers.contains(HTMLUNIT_CHROME)) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.CHROME, false));
            }
            if (browsers.contains(HTMLUNIT_FIREFOX_ESR)) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_ESR, false));
            }
            if (browsers.contains(HTMLUNIT_FIREFOX)) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX, false));
            }
            if (browsers.contains(HTMLUNIT_EDGE)) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.EDGE, false));
            }
        }
        else {
            throw new IllegalStateException("No @Test method found");
        }
    }

    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        return childrenInvoker(notifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final Filter filter) throws NoTestsRemainException {
        boolean atLeastOne = false;
        for (final Runner runner : getChildren()) {
            try {
                if (runner instanceof Filterable) {
                    ((Filterable) runner).filter(filter);
                    atLeastOne = true;
                }
            }
            catch (final NoTestsRemainException e) {
                // nothing
            }
        }

        if (!atLeastOne) {
            throw new NoTestsRemainException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Runner> getChildren() {
        return runners_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     */
    public static final String EMPTY_DEFAULT = "~InTerNal_To_BrowSeRRunNer#@$";

}
