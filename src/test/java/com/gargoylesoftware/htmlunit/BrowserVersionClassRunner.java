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

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * The runner for test methods that run with a specific browser ({@link BrowserRunner.Browser})
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class BrowserVersionClassRunner extends BlockJUnit4ClassRunner {

    private final BrowserVersion browserVersion_;

    public BrowserVersionClassRunner(final Class<WebTestCase> klass,
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
        return String.format("[%s]", BrowserRunner.getDescription(browserVersion_));
    }

    @Override
    protected String testName(final FrameworkMethod method) {
        String className = method.getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return String.format("%s[%s]", className + '.' + method.getName(),
            BrowserRunner.getDescription(browserVersion_));
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
                notYetImplemented, BrowserRunner.getDescription(browserVersion_));
        return statement;
    }
}
