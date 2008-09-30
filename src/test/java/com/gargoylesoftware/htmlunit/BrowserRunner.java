/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
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

        if (!TestClassRunnerForBrowserVersion.getTestMethods(klass).isEmpty()) {
            runners_.add(new TestClassRunnerForBrowserVersion(klass, BrowserVersion.INTERNET_EXPLORER_6_0));
            runners_.add(new TestClassRunnerForBrowserVersion(klass, BrowserVersion.INTERNET_EXPLORER_7_0));
            runners_.add(new TestClassRunnerForBrowserVersion(klass, BrowserVersion.FIREFOX_2));
            runners_.add(new TestClassRunnerForBrowserVersion(klass, BrowserVersion.FIREFOX_3));
        }
        if (!TestClassRunnerForNoBrowser.getTestMethods(klass).isEmpty()) {
            runners_.add(new TestClassRunnerForNoBrowser(klass));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Runner> getChildren() {
        return runners_;
    }

    private static final String EMPTY_DEFAULT = "~InTerNal_To_BrowSeRRunNer#@$";

    /**
     * Browser.
     * @see Browsers
     */
    public enum Browser {
        /** Internet Explorer 6. */
        INTERNET_EXPLORER_6,

        /** Internet Explorer 7. */
        INTERNET_EXPLORER_7,

        /** Firefox 2. */
        FIREFOX_2,

        /** Firefox 3. */
        FIREFOX_3,

        /** Not Browser-specific, it will run only once. Don't use this with other Browsers. */
        NONE;
    }

    /**
     * The only {@link Browser}s that are expected to succeed, default value is all.
     * For example, if you use <tt>@Browsers(Browser.INTERNET_EXPLORER_6)</tt> that means only IE6 is expected
     * to succeed, but IE7 and FF2 should fail.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Browsers {

        /**
         * The browsers which the case succeeds (but fails with remaining ones).
         */
        Browser[] value() default {
            Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3
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

        /** Alerts for any Firefox, it can be overridden by specific FF version. */
        String[] FF() default {EMPTY_DEFAULT };

        /** Alerts for Firefox 2. */
        String[] FF2() default {EMPTY_DEFAULT };

        /** Alerts for Firefox 3. */
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
            Browser.INTERNET_EXPLORER_6, Browser.INTERNET_EXPLORER_7, Browser.FIREFOX_2, Browser.FIREFOX_3
        };
    }

    static class TestClassRunnerForBrowserVersion extends BlockJUnit4ClassRunner {

        private final BrowserVersion browserVersion_;

        public TestClassRunnerForBrowserVersion(final Class<WebTestCase> klass,
            final BrowserVersion browserVersion) throws InitializationError {
            super(klass);
            this.browserVersion_ = browserVersion;
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
                else if (browserVersion_ == BrowserVersion.FIREFOX_3) {
                    if (isDefined(alerts.FF3())) {
                        expectedAlerts = alerts.FF3();
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
        protected String testName(final FrameworkMethod method) {
            String className = method.getMethod().getDeclaringClass().getName();
            className = className.substring(className.lastIndexOf('.') + 1);
            return String.format("%s[%s]", className + '.' + method.getName(), getShortname(browserVersion_));
        }

        @Override
        protected List<FrameworkMethod> computeTestMethods() {
            final List<FrameworkMethod> methods = super.computeTestMethods();
            for (int i = 0; i < methods.size(); i++) {
                final Method method = methods.get(i).getMethod();
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers != null && browsers.value()[0] == Browser.NONE) {
                    methods.remove(i--);
                }
            }
            return methods;
        }

        private static List<Method> getTestMethods(final Class<WebTestCase> klass) {
            final List<Method> list = new ArrayList<Method>();
            for (final Method method : klass.getMethods()) {
                if (method.getAnnotation(Test.class) != null) {
                    final Browsers browsers = method.getAnnotation(Browsers.class);
                    if (browsers == null || browsers.value()[0] != Browser.NONE) {
                        list.add(method);
                    }
                }
            }
            return list;
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

                    case FIREFOX_3:
                        if (browserVersion_ == BrowserVersion.FIREFOX_3) {
                            return true;
                        }
                        break;

                    default:
                }
            }
            return false;
        }

        @Override
        protected Statement methodBlock(final FrameworkMethod method) {
            final Object test;
            final WebTestCase testCase;
            try {
                testCase = (WebTestCase) createTest();
                test = new ReflectiveCallable() {
                    @Override
                    protected Object runReflectiveCall() throws Throwable {
                        return testCase;
                    }
                } .run();
            }
            catch (final Throwable e) {
                return new Fail(e);
            }

            Statement statement = methodInvoker(method, test);
            statement = possiblyExpectingExceptions(method, test, statement);
            statement = withPotentialTimeout(method, test, statement);
            statement = withBefores(method, test, statement);
            statement = withAfters(method, test, statement);

            final Browsers browsers = method.getAnnotation(Browsers.class);
            final boolean shouldFail = browsers != null && !isDefinedIn(browsers.value());

            final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
            final boolean notYetImplemented = notYetImplementedBrowsers != null
                && isDefinedIn(notYetImplementedBrowsers.value());
            setAlerts(testCase, method.getMethod());
            statement = new BrowserStatement(statement, method.getMethod(), shouldFail,
                    notYetImplemented, getShortname(browserVersion_));
            return statement;
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
        else if (browserVersion == BrowserVersion.FIREFOX_3) {
            return "Firefox 3";
        }
        else {
            return browserVersion.getApplicationName() + browserVersion.getApplicationVersion();
        }
    }

    static class TestClassRunnerForNoBrowser extends BlockJUnit4ClassRunner {

        public TestClassRunnerForNoBrowser(final Class<WebTestCase> klass) throws InitializationError {
            super(klass);
        }

        @Override
        protected Statement methodBlock(final FrameworkMethod method) {
            final Object test;
            final WebTestCase testCase;
            try {
                testCase = (WebTestCase) createTest();
                test = new ReflectiveCallable() {
                    @Override
                    protected Object runReflectiveCall() throws Throwable {
                        return testCase;
                    }
                } .run();
            }
            catch (final Throwable e) {
                return new Fail(e);
            }

            Statement statement = methodInvoker(method, test);
            statement = possiblyExpectingExceptions(method, test, statement);
            statement = withPotentialTimeout(method, test, statement);
            statement = withBefores(method, test, statement);
            statement = withAfters(method, test, statement);

            final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
            final boolean notYetImplemented = notYetImplementedBrowsers != null;
            statement = new BrowserStatement(statement, method.getMethod(), false,
                    notYetImplemented, "");
            return statement;
        }

        @Override
        protected String getName() {
            return "[No Browser]";
        }

        @Override
        protected String testName(final FrameworkMethod method) {
            String className = method.getMethod().getDeclaringClass().getName();
            className = className.substring(className.lastIndexOf('.') + 1);
            return String.format("%s[No Browser]", className + '.' + method.getName());
        }

        @Override
        protected List<FrameworkMethod> computeTestMethods() {
            final List<FrameworkMethod> methods = super.computeTestMethods();
            for (int i = 0; i < methods.size(); i++) {
                final Method method = methods.get(i).getMethod();
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers == null || browsers.value()[0] != Browser.NONE) {
                    methods.remove(i--);
                }
            }
            return methods;
        }

        private static List<Method> getTestMethods(final Class<WebTestCase> klass) {
            final List<Method> list = new ArrayList<Method>();
            for (final Method method : klass.getMethods()) {
                if (method.getAnnotation(Test.class) != null) {
                    final Browsers browsers = method.getAnnotation(Browsers.class);
                    if (browsers != null && browsers.value()[0] == Browser.NONE) {
                        list.add(method);
                    }
                }
            }
            return list;
        }

        @Override
        protected void validateTestMethods(final List<Throwable> errors) {
            super.validateTestMethods(errors);
            final List<Throwable> collectederrors = new ArrayList<Throwable>();
            for (final FrameworkMethod method : computeTestMethods()) {
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers != null) {
                    for (final Browser browser : browsers.value()) {
                        System.out.println(browsers.value().length);
                        if (browser == Browser.NONE && browsers.value().length > 1) {
                            collectederrors.add(new Exception("Method " + method.getName()
                                    + "() cannot have Browser.NONE along with other Browsers."));
                        }
                    }
                }
            }
            for (final Throwable error : collectederrors) {
                errors.add(error);
            }
        }
    }

    private static final class BrowserStatement extends Statement {

        private Statement next_;
        private final boolean shouldFail_;
        private final boolean notYetImplemented_;
        private final Method method_;
        private final String browserVersionString_;

        private BrowserStatement(final Statement next, final Method method, final boolean shouldFail,
                final boolean notYetImplemented, final String browserVersionString) {
            next_ = next;
            method_ = method;
            shouldFail_ = shouldFail;
            notYetImplemented_ = notYetImplemented;
            browserVersionString_ = browserVersionString;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                next_.evaluate();
                if (shouldFail_) {
                    final String errorMessage;
                    if (browserVersionString_ == null) {
                        errorMessage = method_.getName() + " is marked to fail with "
                            + browserVersionString_ + ", but succeeds";
                    }
                    else {
                        errorMessage = method_.getName() + " is marked to fail, but succeeds";
                    }
                    throw new Exception(errorMessage);
                }
                else if (notYetImplemented_) {
                    final String errorMessage;
                    if (browserVersionString_ == null) {
                        errorMessage = method_.getName() + " is marked as not implemented but already works";
                    }
                    else {
                        errorMessage = method_.getName() + " is marked as not implemented with "
                            + browserVersionString_ + " but already works";
                    }
                    throw new Exception(errorMessage);
                }
            }
            catch (final Throwable e) {
                if (!shouldFail_ && !notYetImplemented_) {
                    throw new Exception(e.getCause());
                }
            }
        }
    }
}
