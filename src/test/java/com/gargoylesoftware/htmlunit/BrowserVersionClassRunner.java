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

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.MethodRule;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;

/**
 * The runner for test methods that run with a specific browser ({@link BrowserRunner.Browser})
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class BrowserVersionClassRunner extends BlockJUnit4ClassRunner {

    private final BrowserVersion browserVersion_;
    private final boolean realBrowser_;
    static final boolean maven_ = System.getProperty("htmlunit.maven") != null;

    public BrowserVersionClassRunner(final Class<WebTestCase> klass,
        final BrowserVersion browserVersion, final boolean realBrowser) throws InitializationError {
        super(klass);
        browserVersion_ = browserVersion;
        realBrowser_ = realBrowser;
    }

    private void setAlerts(final WebTestCase testCase, final Method method) {
        final Alerts alerts = method.getAnnotation(Alerts.class);
        String[] expectedAlerts = {};
        if (alerts != null) {
            if (isDefined(alerts.value())) {
                expectedAlerts = alerts.value();
            }
            else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_6) {
                if (isDefined(alerts.IE6())) {
                    expectedAlerts = alerts.IE6();
                }
                else if (isDefined(alerts.IE())) {
                    expectedAlerts = alerts.IE();
                }
            }
            else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_7) {
                if (isDefined(alerts.IE7())) {
                    expectedAlerts = alerts.IE7();
                }
                else if (isDefined(alerts.IE())) {
                    expectedAlerts = alerts.IE();
                }
            }
            else if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_8) {
                if (isDefined(alerts.IE8())) {
                    expectedAlerts = alerts.IE8();
                }
                else if (isDefined(alerts.IE())) {
                    expectedAlerts = alerts.IE();
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
            else if (browserVersion_ == BrowserVersion.FIREFOX_3_6) {
                if (isDefined(alerts.FF3_6())) {
                    expectedAlerts = alerts.FF3_6();
                }
                else if (isDefined(alerts.FF())) {
                    expectedAlerts = alerts.FF();
                }
            }
        }
        testCase.setExpectedAlerts(expectedAlerts);
    }

    @Override
    protected Object createTest() throws Exception {
        final Object test = super.createTest();
        assertTrue("Test class must inherit WebTestCase", test instanceof WebTestCase);
        final WebTestCase object = (WebTestCase) test;
        object.setBrowserVersion(browserVersion_);
        if (test instanceof WebDriverTestCase) {
            ((WebDriverTestCase) test).setUseRealBrowser(realBrowser_);
        }
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final Filter filter) throws NoTestsRemainException {
        computeTestMethods();

        for (final ListIterator<FrameworkMethod> iter = testMethods_.listIterator(); iter.hasNext();) {
            final FrameworkMethod method = iter.next();
            // compute 2 descriptions to verify if it is the intended test:
            // - one "normal", this is what Eclipse's filter awaits when typing Ctrl+X T
            //   when cursor is located on a test method
            // - one with browser nickname, this is what is needed when re-running a test from
            //   the JUnit view
            // as the list of methods is cached, this is what will be returned when computeTestMethods() is called
            final Description description = Description.createTestDescription(getTestClass().getJavaClass(),
                method.getName());
            final Description description2 = Description.createTestDescription(getTestClass().getJavaClass(),
                testName(method));
            if (!filter.shouldRun(description) && !filter.shouldRun(description2)) {
                iter.remove();
            }
        }
    }

    @Override
    protected String getName() {
        String browserString = browserVersion_.getNickname();
        if (realBrowser_) {
            browserString = "Real " + browserString;
        }
        return String.format("[%s]", browserString);
    }

    @Override
    protected String testName(final FrameworkMethod method) {
        String className = method.getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        String prefix = "";
        if (isNotYetImplemented(method) && !realBrowser_) {
            prefix = "(NYI) ";
        }
        else if (isExpectedToFail(method)) {
            prefix = "(failure expected) ";
        }

        String browserString = browserVersion_.getNickname();
        if (realBrowser_) {
            browserString = "Real " + browserString;
        }
        if (!maven_) {
            return String.format("%s [%s]", method.getName(), browserString);
        }
        return String.format("%s%s [%s]", prefix, className + '.' + method.getName(), browserString);
    }

    private List<FrameworkMethod> testMethods_;

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (testMethods_ != null) {
            return testMethods_;
        }
        final List<FrameworkMethod> methods = super.computeTestMethods();
        for (int i = 0; i < methods.size(); i++) {
            final Method method = methods.get(i).getMethod();
            final Browsers browsers = method.getAnnotation(Browsers.class);
            if (browsers != null && browsers.value()[0] == Browser.NONE) {
                methods.remove(i--);
            }
        }
        final Comparator<FrameworkMethod> comparator = new Comparator<FrameworkMethod>() {
            public int compare(final FrameworkMethod fm1, final FrameworkMethod fm2) {
                return fm1.getName().compareTo(fm2.getName());
            }
        };
        Collections.sort(methods, comparator);
        testMethods_ = methods;
        return testMethods_;
    }

    static boolean containsTestMethods(final Class<WebTestCase> klass) {
        for (final Method method : klass.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                final Browsers browsers = method.getAnnotation(Browsers.class);
                if (browsers == null || browsers.value()[0] != Browser.NONE) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isDefined(final String[] alerts) {
        return alerts.length != 1 || !alerts[0].equals(BrowserRunner.EMPTY_DEFAULT);
    }

    /**
     * Returns true if current {@link #browserVersion_} is contained in the specific <tt>browsers</tt>.
     */
    private boolean isDefinedIn(final Browser[] browsers) {
        for (final Browser browser : browsers) {
            switch(browser) {
                case IE:
                    if (browserVersion_.isIE()) {
                        return true;
                    }
                    break;

                case IE6:
                    if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_6) {
                        return true;
                    }
                    break;

                case IE7:
                    if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_7) {
                        return true;
                    }
                    break;

                case IE8:
                    if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER_8) {
                        return true;
                    }
                    break;

                case FF:
                    if (browserVersion_.isFirefox()) {
                        return true;
                    }
                    break;

                case FF3:
                    if (browserVersion_ == BrowserVersion.FIREFOX_3) {
                        return true;
                    }
                    break;

                case FF3_6:
                    if (browserVersion_ == BrowserVersion.FIREFOX_3_6) {
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
        statement = withRules(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);

        final boolean shouldFail = isExpectedToFail(method);
        final boolean notYetImplemented;
        final int tries;

        if (testCase instanceof WebDriverTestCase && realBrowser_) {
            notYetImplemented = false;
            tries = 1;
        }
        else {
            notYetImplemented = isNotYetImplemented(method);
            tries = getTries(method);
        }
        setAlerts(testCase, method.getMethod());
        statement = new BrowserStatement(statement, method.getMethod(), shouldFail,
                notYetImplemented, tries, browserVersion_.getNickname());
        return statement;
    }

    private Statement withRules(final FrameworkMethod method, final Object target, final Statement statement) {
        Statement result = statement;
        for (final MethodRule each : rules(target)) {
            result = each.apply(result, method, target);
        }
        return result;
    }

    private boolean isExpectedToFail(final FrameworkMethod method) {
        final Browsers browsers = method.getAnnotation(Browsers.class);
        return browsers != null && !isDefinedIn(browsers.value());
    }

    private boolean isNotYetImplemented(final FrameworkMethod method) {
        final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
        return notYetImplementedBrowsers != null && isDefinedIn(notYetImplementedBrowsers.value());
    }

    private int getTries(final FrameworkMethod method) {
        final Tries tries = method.getAnnotation(Tries.class);
        return tries != null ? tries.value() : 1;
    }
}
