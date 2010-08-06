/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;

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
 *    &#064;Browsers({Browser.FIREFOX_2})
 *    public void test() {
 *       //your test case that succeeds with only Firefox 2
 *    }
 * }
 * </pre>
 * @version $Revision$
 * @author Ahmed Ashour
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
            if (WebDriverTestCase.class.isAssignableFrom(klass)) {
                if (browsers.contains("hu") || browsers.contains("hu-ff3")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_3, false));
                }
                if (browsers.contains("hu") || browsers.contains("hu-ie6")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_6, false));
                }
                if (browsers.contains("hu") || browsers.contains("hu-ie7")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_7, false));
                }
                if (browsers.contains("hu") || browsers.contains("hu-ie8")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_8, false));
                }
                if (browsers.contains("ff3")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_3, true));
                }
                if (browsers.contains("ie6")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_6, true));
                }
                if (browsers.contains("ie7")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_7, true));
                }
                if (browsers.contains("ie8")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_8, true));
                }
            }
            else {
                if (browsers.contains("hu") || browsers.contains("hu-ff3")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.FIREFOX_3, false));
                }
                if (browsers.contains("hu") || browsers.contains("hu-ie6")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_6, false));
                }
                if (browsers.contains("hu") || browsers.contains("hu-ie7")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_7, false));
                }
                if (browsers.contains("hu") || browsers.contains("hu-ie8")) {
                    runners_.add(new BrowserVersionClassRunner(klass, BrowserVersion.INTERNET_EXPLORER_8, false));
                }
            }
        }
        if (BrowserNoneClassRunner.containsTestMethods(klass)) {
            runners_.add(new BrowserNoneClassRunner(klass));
        }
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
        /** All versions of Internet Explorer. */
        IE,

        /** Internet Explorer 6. */
        IE6,

        /** Internet Explorer 7. */
        IE7,

        /** Internet Explorer 8. */
        IE8,

        /** All versions of Firefox. */
        FF,

        /** Firefox 3. */
        FF3,

        /** Not Browser-specific, it will run only once. Don't use this with other Browsers. */
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
            Browser.IE, Browser.FF
        };
    }

    /**
     * Expected alerts.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Alerts {

        /** Alerts that is used for all browsers (if defined, the other values are ignored). */
        String[] value() default {EMPTY_DEFAULT };

        /** Alerts for any Internet Explorer, it can be overridden by specific IE version. */
        String[] IE() default {EMPTY_DEFAULT };

        /** Alerts for Internet Explorer 6. If not defined, {@link #IE()} is used. */
        String[] IE6() default {EMPTY_DEFAULT };

        /** Alerts for Internet Explorer 7. If not defined, {@link #IE()} is used. */
        String[] IE7() default {EMPTY_DEFAULT };

        /** Alerts for Internet Explorer 8. If not defined, {@link #IE()} is used. */
        String[] IE8() default {EMPTY_DEFAULT };

        /** Alerts for any Firefox, it can be overridden by specific FF version. */
        String[] FF() default {EMPTY_DEFAULT };

        /** Alerts for Firefox 3. If not defined, {@link #FF()} is used. */
        String[] FF3() default {EMPTY_DEFAULT };
    }

    /**
     * Browsers with which the case is not yet implemented, default value is all.
     * @see Browser
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface NotYetImplemented {

        /**
         * The browsers with which the case is not yet implemented.
         */
        Browser[] value() default {
            Browser.IE, Browser.FF
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
