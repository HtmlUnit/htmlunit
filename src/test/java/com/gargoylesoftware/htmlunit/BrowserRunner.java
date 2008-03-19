/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.CompositeRunner;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

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
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class BrowserRunner extends CompositeRunner {

    private static final String EMPTY_DEFAULT = "~InTerNal_To_BrowSeRRunNer#@$";

    /**
     * Browser.
     */
    public enum Browser {
        /** Internet Explorer 6.*/
        INTERNET_EXPLORER_6,
        
        /** Internet Explorer 7.*/
        INTERNET_EXPLORER_7,

        /** Firefox 2.*/
        FIREFOX_2,

        /** Not Browser-specific, it will run only once. Don't use this with other Browsers. */
        NONE;
    }
    
    
    /**
     * Browsers to test with, default value is all.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Browsers {

        /**
         * The browsers which the case succeeds (but fails with remaining ones).
         */
        Browser[] value() default {
            Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2
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

        /** Alerts for Internet Explorer 6.  If not defined, {@link #IE()} is used. */
        String[] IE6() default {EMPTY_DEFAULT };

        /** Alerts for Internet Explorer 7.  If not defined, {@link #IE()} is used. */
        String[] IE7() default {EMPTY_DEFAULT };

        /** Alerts for any Firefox, it can be overridden by specific FF version. */
        String[] FF() default {EMPTY_DEFAULT };

        /** Alerts for Firefox 2. */
        String[] FF2() default {EMPTY_DEFAULT };
    }
    
    /**
     * Browsers with which the case is not yet implemented, default value is all.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface NotYetImplemented {

        /**
         * The browsers with which the case is not yet implemented.
         */
        Browser[] value() default {
            Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2
        };
    }

    static class TestClassRunnerForBrowserVersion extends JUnit4ClassRunner {

        private final BrowserVersion browserVersion_;
        
        public TestClassRunnerForBrowserVersion(final Class< ? extends WebTestCase> klass,
            final BrowserVersion browserVersion) throws InitializationError {
            super(klass);
            this.browserVersion_ = browserVersion;
        }

        @Override
        protected void invokeTestMethod(final Method method, final RunNotifier notifier) {
            final Description description = methodDescription(method);
            Object test;
            try {
                test = createTest();
            }
            catch (final InvocationTargetException e) {
                notifier.testAborted(description, e.getCause());
                return;
            }
            catch (final Exception e) {
                notifier.testAborted(description, e);
                return;
            }
            final Browsers browsers = method.getAnnotation(Browsers.class);
            final boolean shouldFail = browsers != null && !isDefinedIn(browsers.value());
            
            final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
            final boolean notYetImplemented = notYetImplementedBrowsers != null
                && isDefinedIn(notYetImplementedBrowsers.value());
            final TestMethod testMethod = wrapMethod(method);
            setAlerts((WebTestCase) test, method);
            new BrowserRoadie(test, testMethod, notifier, description, method, shouldFail, notYetImplemented,
                getShortname(browserVersion_)).run();
        }

        private void setAlerts(final WebTestCase testCase, final Method method) {
            final Alerts alerts = method.getAnnotation(Alerts.class);
            if (alerts != null) {
                String[] expectedAlerts = {};
                if (isDefined(alerts.value())) {
                    expectedAlerts = alerts.value();
                }
                else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_6_0) {
                    if (isDefined(alerts.IE6())) {
                        expectedAlerts = alerts.IE6();
                    }
                    else if (isDefined(alerts.IE())) {
                        expectedAlerts = alerts.IE();
                    }
                }
                else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_7_0) {
                    if (isDefined(alerts.IE7())) {
                        expectedAlerts = alerts.IE7();
                    }
                    else if (isDefined(alerts.IE())) {
                        expectedAlerts = alerts.IE();
                    }
                }
                else if (browserVersion_ == BrowserVersion.FIREFOX_2) {
                    if (isDefined(alerts.FF2())) {
                        expectedAlerts = alerts.FF2();
                    }
                    else if (isDefined(alerts.FF())) {
                        expectedAlerts = alerts.FF();
                    }
                }
                testCase.setExpectedAlerts(expectedAlerts);
            }
        }

        @Override
        protected Object createTest() throws Exception {
            final WebTestCase object = (WebTestCase) super.createTest();
            object.setBrowserVersion(browserVersion_);
            return object;
        }

        @Override
        protected String getName() {
            return String.format("[%s]", getShortname(browserVersion_));
        }

        @Override
        protected String testName(final Method method) {
            return String.format("%s[%s]", method.getName(), getShortname(browserVersion_));
        }

        @Override
        protected void validate() throws InitializationError {
            super.validate();
            final List<Throwable> errors = new ArrayList<Throwable>();
            for (final Method method : getTestMethods()) {
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers != null) {
                    for (final Browser browser : browsers.value()) {
                        if (browser == Browser.NONE && browsers.value().length > 1) {
                            errors.add(new Exception("Method " + method.getName()
                                    + " can not have Browser.NONE along with other Browsers."));
                        }
                    }
                }
            }
            if (!errors.isEmpty()) {
                throw new InitializationError(errors);
            }
        }

        @Override
        protected List<Method> getTestMethods() {
            final List<Method> methods = super.getTestMethods();
            for (final Method method : methods) {
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers != null && browsers.value()[0] == Browser.NONE) {
                    methods.remove(method);
                }
            }
            return methods;
        }

        private boolean isDefined(final String[] alerts) {
            return alerts.length != 1 || !alerts[0].equals(EMPTY_DEFAULT);
        }
        
        /**
         * Returns true if current {@link #browserVersion_} is contained in the specific <tt>browsers</tt>.
         */
        private boolean isDefinedIn(final Browser[] browsers) {
            for (final Browser browser : browsers) {
                switch(browser) {
                    case INTERNET_EXPLORER_6:
                        if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_6_0) {
                            return true;
                        }
                        break;

                    case INTERNET_EXPLORER_7:
                        if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_7_0) {
                            return true;
                        }
                        break;

                    case FIREFOX_2:
                        if (browserVersion_ == BrowserVersion.FIREFOX_2) {
                            return true;
                        }
                        break;

                    default:
                }
            }
            return false;
        }
    }

    private static String getShortname(final BrowserVersion browserVersion) {
        if (browserVersion == BrowserVersion.INTERNET_EXPLORER_6_0) {
            return "Internet Explorer 6";
        }
        else if (browserVersion == BrowserVersion.INTERNET_EXPLORER_7_0) {
            return "Internet Explorer 7";
        }
        else if (browserVersion == BrowserVersion.FIREFOX_2) {
            return "Firefox 2";
        }
        else {
            return browserVersion.getApplicationName() + browserVersion.getApplicationVersion();
        }
    }

    static class TestClassRunnerForNoBrowser extends JUnit4ClassRunner {

        public TestClassRunnerForNoBrowser(final Class< ? extends WebTestCase> klass) throws InitializationError {
            super(klass);
        }

        @Override
        protected void invokeTestMethod(final Method method, final RunNotifier notifier) {
            final Description description = methodDescription(method);
            Object test;
            try {
                test = createTest();
            }
            catch (final InvocationTargetException e) {
                notifier.testAborted(description, e.getCause());
                return;
            }
            catch (final Exception e) {
                notifier.testAborted(description, e);
                return;
            }
            final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
            final boolean notYetImplemented = notYetImplementedBrowsers != null;
            final TestMethod testMethod = wrapMethod(method);
            new BrowserRoadie(test, testMethod, notifier, description, method, false, notYetImplemented,
                "").run();
        }

        @Override
        protected String getName() {
            return "[No Browser]";
        }

        @Override
        protected String testName(final Method method) {
            return String.format("%s[No Browser]", method.getName());
        }

        @Override
        protected List<Method> getTestMethods() {
            final List<Method> methods = super.getTestMethods();
            for (int i = 0; i < methods.size(); i++) {
                final Method method = methods.get(i);
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers == null || browsers.value()[0] != Browser.NONE) {
                    methods.remove(method);
                    i--;
                }
            }
            return methods;
        }

    }
    /**
     * Constructs a new instance.
     *
     * @param klass test case.
     * @throws Exception If an error occurs.
     */
    public BrowserRunner(final Class< ? extends WebTestCase> klass) throws Exception {
        super(klass.getName());
        assertTrue("Test case must extend WebTestCase", WebTestCase.class.isAssignableFrom(klass));
        add(new TestClassRunnerForBrowserVersion(klass, BrowserVersion.INTERNET_EXPLORER_6_0));
        add(new TestClassRunnerForBrowserVersion(klass, BrowserVersion.INTERNET_EXPLORER_7_0));
        add(new TestClassRunnerForBrowserVersion(klass, BrowserVersion.FIREFOX_2));
        add(new TestClassRunnerForNoBrowser(klass));
    }

}
