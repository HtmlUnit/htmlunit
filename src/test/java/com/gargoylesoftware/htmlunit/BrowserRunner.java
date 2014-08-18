/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
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
 *    &#064;Test
 *    &#064;Browsers({ Browser.FF24 })
 *    public void test() {
 *       //your test case that succeeds with only Firefox 24
 *    }
 * }
 * </pre>
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class BrowserRunner extends Suite {

    private final ArrayList<Runner> runners_ = new ArrayList<Runner>();

    /**
     * Constructor.
     *
     * @param klass the test case class
     * @throws Throwable If an exception occurs
     */
    public BrowserRunner(final Class<WebTestCase> klass) throws Throwable {
        super(klass, Collections.<Runner>emptyList());

        if (BrowserVersionClassRunner.containsTestMethods(klass)) {
            final List<String> browsers = WebDriverTestCase.getBrowsersProperties();
            if (/*browsers.contains("hu") ||*/ browsers.contains("hu-ff24")) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_24, false));
            }
            if (browsers.contains("hu") || browsers.contains("hu-ff31")) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_31, false));
            }
            if (browsers.contains("hu") || browsers.contains("hu-ie8")) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_8, false));
            }
            if (/*browsers.contains("hu") ||*/ browsers.contains("hu-ie11")) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_11, false));
            }
            if (/*browsers.contains("hu") ||*/ browsers.contains("hu-chrome")) {
                runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.CHROME, false));
            }

            if (WebDriverTestCase.class.isAssignableFrom(klass)) {
                if (browsers.contains("ff24")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_24, true));
                }
                if (browsers.contains("ff31")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_31, true));
                }
                if (browsers.contains("ie8")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_8, true));
                }
                if (browsers.contains("ie11")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_11, true));
                }
                if (browsers.contains("chrome")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.CHROME, true));
                }
            }
        }
        if (BrowserNoneClassRunner.containsTestMethods(klass)) {
            runners_.add(new BrowserNoneClassRunner(klass));
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
            final BlockJUnit4ClassRunner junit4Runner = (BlockJUnit4ClassRunner) runner;
            try {
                junit4Runner.filter(filter);
                atLeastOne = true;
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

    static final String EMPTY_DEFAULT = "~InTerNal_To_BrowSeRRunNer#@$";

    /**
     * Browser.
     * @see Browsers
     */
    public enum Browser {
        /** Latest version of Chrome. */
        CHROME,

        /** All versions of Internet Explorer. */
        IE,

        /** Internet Explorer 8. */
        IE8,

        /** Internet Explorer 11. */
        IE11,

        /** All versions of Firefox. */
        FF,

        /** Firefox 24. */
        FF24,

        /** Firefox 31. */
        FF31,

        /**
         * Not Browser-specific, it will run only once. Don't use this with other Browsers.
         * And don't call directly or indirectly {@link WebTestCase#getBrowserVersion()}
         */
        NONE;
    }

    /**
     * The only {@link Browser}s that are expected to succeed, default value is all.
     * For example, if you use <tt>@Browsers(Browser.INTERNET_EXPLORER_6)</tt> that means only IE6 is expected
     * to succeed, but IE7, FF2 and FF3 should fail.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Browsers {

        /**
         * The browsers which the case succeeds (but fails with remaining ones).
         */
        Browser[] value() default {
            IE, FF, CHROME
        };
    }

    /**
     * Allows to express the expected alerts (i.e. the messages passed to the
     * window.alert function) for the different browsers for a unit test.
     * Expected alerts can be retrieved within a unit test with {@link SimpleWebTestCase#getExpectedAlerts()}
     * (resp. {@link WebDriverTestCase#getExpectedAlerts}) to be compared with the actual alerts but most of the time
     * utility functions like {@link SimpleWebTestCase#loadPageWithAlerts(String)}
     * (resp. {@link WebDriverTestCase#loadPageWithAlerts2(String)}) are used which do it
     * after having loaded the page.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Alerts {

        /** Alerts that is used for all browsers (if defined, the other values are ignored). */
        String[] value() default { EMPTY_DEFAULT };

        /** Alerts for any Internet Explorer, it can be overridden by specific IE version. */
        String[] IE() default { EMPTY_DEFAULT };

        /** Alerts for Internet Explorer 8. If not defined, {@link #IE()} is used. */
        String[] IE8() default { EMPTY_DEFAULT };

        /** Alerts for Internet Explorer 11. If not defined, {@link #IE()} is used. */
        String[] IE11() default { EMPTY_DEFAULT };

        /** Alerts for any Firefox, it can be overridden by specific FF version. */
        String[] FF() default { EMPTY_DEFAULT };

        /** Alerts for Firefox 24. If not defined, {@link #FF()} is used. */
        String[] FF24() default { EMPTY_DEFAULT };

        /** Alerts for Firefox 31. If not defined, {@link #FF()} is used. */
        String[] FF31() default { EMPTY_DEFAULT };

        /** Alerts for latest Chrome. */
        String[] CHROME() default { EMPTY_DEFAULT };

        /** The default alerts, if nothing more specific is defined. */
        String[] DEFAULT() default { EMPTY_DEFAULT };
    }

    /**
     * Marks a test as not yet working for a particular browser (default value is all).
     * This will cause a failure to be considered as success and a success as failure forcing
     * us to remove this annotation when a feature has been implemented even unintentionally.
     * @see Browser
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface NotYetImplemented {

        /**
         * The browsers with which the case is not yet implemented.
         */
        Browser[] value() default {
            IE, FF, CHROME
        };
    }

    /**
     * Indicates that the test runs manually in a real browser but not when using WebDriver to drive the browser.
     * @see Browser
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface BuggyWebDriver {

        /**
         * The browsers with which the case is failing.
         */
        Browser[] value() default {
            IE, FF, CHROME
        };
    }

    /**
     * The number of tries that test will be executed.
     * The test will fail if and only if all trials failed.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Tries {

        /**
         * The value.
         */
        int value() default 1;
    }

}
