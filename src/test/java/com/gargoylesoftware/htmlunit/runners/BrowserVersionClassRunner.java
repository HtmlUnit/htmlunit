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
package com.gargoylesoftware.htmlunit.runners;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.AlertsStandards;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.annotations.StandardsMode;

/**
 * The runner for test methods that run with a specific browser ({@link BrowserRunner.TestedBrowser}).
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class BrowserVersionClassRunner extends BlockJUnit4ClassRunner {

    /** If no alerts defined. */
    public static final String[] NO_ALERTS_DEFINED = {"no alerts defined"};

    private final BrowserVersion browserVersion_;
    private final boolean realBrowser_;
    static final boolean maven_ = System.getProperty("htmlunit.maven") != null;

    /**
     * Constructs a new instance.
     * @param klass the class
     * @param browserVersion the browser version
     * @param realBrowser use real browser or not
     * @throws InitializationError if an error occurs
     */
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
            expectedAlerts = NO_ALERTS_DEFINED;
            if (isDefined(alerts.value())) {
                expectedAlerts = alerts.value();
            }
            else {
                if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER) {
                    expectedAlerts = firstDefined(alerts.IE(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.EDGE) {
                    expectedAlerts = firstDefined(alerts.EDGE(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.FIREFOX_78) {
                    expectedAlerts = firstDefined(alerts.FF78(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.FIREFOX) {
                    expectedAlerts = firstDefined(alerts.FF(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.CHROME) {
                    expectedAlerts = firstDefined(alerts.CHROME(), alerts.DEFAULT());
                }
            }
        }

        if (isRealBrowser()) {
            final BuggyWebDriver buggyWebDriver = method.getAnnotation(BuggyWebDriver.class);
            if (buggyWebDriver != null) {
                if (isDefined(buggyWebDriver.value())) {
                    expectedAlerts = buggyWebDriver.value();
                }
                else {
                    if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.IE(), buggyWebDriver.DEFAULT());
                    }
                    else if (browserVersion_ == BrowserVersion.EDGE) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.EDGE(), buggyWebDriver.DEFAULT());
                    }
                    else if (browserVersion_ == BrowserVersion.FIREFOX_78) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.FF78(), buggyWebDriver.DEFAULT());
                    }
                    else if (browserVersion_ == BrowserVersion.FIREFOX) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.FF(), buggyWebDriver.DEFAULT());
                    }
                    else if (browserVersion_ == BrowserVersion.CHROME) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.CHROME(), buggyWebDriver.DEFAULT());
                    }
                }
            }
        }
        else {
            final HtmlUnitNYI htmlUnitNYI = method.getAnnotation(HtmlUnitNYI.class);
            if (htmlUnitNYI != null) {
                if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.IE());
                }
                else if (browserVersion_ == BrowserVersion.EDGE) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.EDGE());
                }
                else if (browserVersion_ == BrowserVersion.FIREFOX_78) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF78());
                }
                else if (browserVersion_ == BrowserVersion.FIREFOX) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF());
                }
                else if (browserVersion_ == BrowserVersion.CHROME) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.CHROME());
                }
            }
        }

        testCase.setExpectedAlerts(expectedAlerts);
    }

    private void setAlertsStandards(final WebTestCase testCase, final Method method) {
        final AlertsStandards alerts = method.getAnnotation(AlertsStandards.class);
        if (alerts != null) {
            String[] expectedAlerts = NO_ALERTS_DEFINED;
            if (isDefined(alerts.value())) {
                expectedAlerts = alerts.value();
            }
            else {
                if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER) {
                    expectedAlerts = firstDefined(alerts.IE(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.EDGE) {
                    expectedAlerts = firstDefined(alerts.EDGE(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.FIREFOX_78) {
                    expectedAlerts = firstDefined(alerts.FF78(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.FIREFOX) {
                    expectedAlerts = firstDefined(alerts.FF(), alerts.DEFAULT());
                }
                else if (browserVersion_ == BrowserVersion.CHROME) {
                    expectedAlerts = firstDefined(alerts.CHROME(), alerts.DEFAULT());
                }
            }
            testCase.setExpectedAlerts(expectedAlerts);
        }
        else {
            setAlerts(testCase, method);
        }
    }

    private static String[] firstDefined(final String[]... variants) {
        for (final String[] var : variants) {
            if (isDefined(var)) {
                return var;
            }
        }
        return NO_ALERTS_DEFINED;
    }

    public static String[] firstDefinedOrGiven(final String[] given, final String[]... variants) {
        for (final String[] var : variants) {
            if (isDefined(var)) {
                try {
                    assertArrayEquals(var, given);
                    fail("BuggyWebDriver duplicates expectations");
                }
                catch (final AssertionError e) {
                    // ok
                }
                return var;
            }
        }
        return given;
    }

    /**
     * {@inheritDoc}
     */
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
        String prefix = "";
        if (isNotYetImplemented(method) && !realBrowser_) {
            prefix = "(NYI) ";
        }

        String browserString = browserVersion_.getNickname();
        if (realBrowser_) {
            browserString = "Real " + browserString;
        }
        if (!maven_) {
            return String.format("%s [%s]", method.getName(), browserString);
        }
        String className = method.getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return String.format("%s%s [%s]", prefix, className + '.' + method.getName(), browserString);
    }

    /**
     * Does the test class contains test methods.
     *
     * @param klass the class
     * @return whether it contains test methods or not
     */
    public static boolean containsTestMethods(final Class<WebTestCase> klass) {
        for (final Method method : klass.getMethods()) {
            if (method.getAnnotation(Test.class) != null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDefined(final String[] alerts) {
        return alerts.length != 1 || !alerts[0].equals(BrowserRunner.EMPTY_DEFAULT);
    }

    /**
     * Returns true if current {@link #browserVersion_} is contained in the specific <tt>browsers</tt>.
     */
    private boolean isDefinedIn(final TestedBrowser[] browsers) {
        for (final TestedBrowser browser : browsers) {
            switch (browser) {
                case IE:
                    if (browserVersion_ == BrowserVersion.INTERNET_EXPLORER) {
                        return true;
                    }
                    break;

                case EDGE:
                    if (browserVersion_ == BrowserVersion.EDGE) {
                        return true;
                    }
                    break;

                case FF78:
                    if (browserVersion_ == BrowserVersion.FIREFOX_78) {
                        return true;
                    }
                    break;

                case FF:
                    if (browserVersion_ == BrowserVersion.FIREFOX) {
                        return true;
                    }
                    break;

                case CHROME:
                    if (browserVersion_ == BrowserVersion.CHROME) {
                        return true;
                    }
                    break;

                default:
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
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
        statement = withRules(method, test, statement);
        statement = withInterruptIsolation(statement);

        //  End of copy & paste from super.methodBlock()  //

        boolean notYetImplemented = false;
        final int tries;

        if (testCase instanceof WebDriverTestCase && realBrowser_) {
            tries = 1;
        }
        else {
            notYetImplemented = isNotYetImplemented(method);
            tries = getTries(method);
        }
        if (method instanceof StandardsFrameworkMethod && ((StandardsFrameworkMethod) method).isStandards()) {
            setAlertsStandards(testCase, method.getMethod());
        }
        else {
            setAlerts(testCase, method.getMethod());
        }
        statement = new BrowserStatement(statement, method, realBrowser_,
                notYetImplemented, tries, browserVersion_);
        return statement;
    }

    private Statement withRules(final FrameworkMethod method, final Object target, final Statement statement) {
        Statement result = statement;
        result = withMethodRules(method, target, result);
        result = withTestRules(method, target, result);
        return result;
    }

    private Statement withMethodRules(final FrameworkMethod method, final Object target, Statement result) {
        final List<TestRule> testRules = getTestRules(target);
        for (final org.junit.rules.MethodRule each : getMethodRules(target)) {
            if (!testRules.contains(each)) {
                result = each.apply(result, method, target);
            }
        }
        return result;
    }

    private List<org.junit.rules.MethodRule> getMethodRules(final Object target) {
        return rules(target);
    }

    /**
     * Returns a Statement.
     *
     * @param statement The base statement
     * @return a RunRules statement if any class-level Rule are found, or the base statement
     */
    private Statement withTestRules(final FrameworkMethod method, final Object target, final Statement statement) {
        final List<TestRule> testRules = getTestRules(target);
        return testRules.isEmpty() ? statement : new RunRules(statement, testRules, describeChild(method));
    }

    /**
     * Returns if not yet implemented.
     * @param method the method
     * @return if not yet implemented
     */
    protected boolean isNotYetImplemented(final FrameworkMethod method) {
        final NotYetImplemented notYetImplementedBrowsers = method.getAnnotation(NotYetImplemented.class);
        return notYetImplementedBrowsers != null && isDefinedIn(notYetImplementedBrowsers.value());
    }

    private static int getTries(final FrameworkMethod method) {
        final Tries tries = method.getAnnotation(Tries.class);
        return tries != null ? tries.value() : 1;
    }

    /**
     * Returns the browser version.
     * @return the browser version
     */
    protected BrowserVersion getBrowserVersion() {
        return browserVersion_;
    }

    /**
     * Is real browser.
     * @return whether we are using real browser or not
     */
    protected boolean isRealBrowser() {
        return realBrowser_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TestClass createTestClass(final Class<?> testClass) {
        if (testClass.getAnnotation(StandardsMode.class) != null) {
            return new StandardsTestClass(testClass);
        }
        return super.createTestClass(testClass);
    }
}
