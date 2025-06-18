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

import java.lang.reflect.Method;
import java.util.Optional;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.ReflectionUtils;
import org.opentest4j.AssertionFailedError;


/**
 * BeforeTestExecutionCallback to set the {@code alerts} property.
 *
 * @author Ronald Brill
 */
public class SetExpectedAlertsBeforeTestExecutionCallback implements BeforeTestExecutionCallback {

    /** If no alerts defined. */
    public static final String[] NO_ALERTS_DEFINED = {"no alerts defined"};

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     */
    public static final String EMPTY_DEFAULT = "~InTerNal_To_BrowSeRRunNer#@$";

    @Override
    public void beforeTestExecution(final ExtensionContext context) throws Exception {
        final Object testInstance = context.getRequiredTestInstance();

        if (testInstance instanceof WebTestCase) {
            final WebTestCase webTestCase = (WebTestCase) testInstance;

            Method testMethod = context.getRequiredTestMethod();
            final Optional<Method> potentialOberwrittenMethod =
                    ReflectionUtils.findMethod(testInstance.getClass(), context.getDisplayName(), new Class[] {});
            if (potentialOberwrittenMethod.isPresent()) {
                testMethod = potentialOberwrittenMethod.get();
            }

            setAlerts(webTestCase, testMethod);
        }
    }

    private static void setAlerts(final WebTestCase webTestCase, final Method method) {
        final Alerts alerts = method.getAnnotation(Alerts.class);
        final BrowserVersion browserVersion = webTestCase.getBrowserVersion();

        String[] expectedAlerts = {};
        if (alerts != null) {
            expectedAlerts = NO_ALERTS_DEFINED;
            if (isDefined(alerts.value())) {
                expectedAlerts = alerts.value();
            }
            else {
                if (browserVersion == BrowserVersion.EDGE) {
                    expectedAlerts = firstDefined(alerts.EDGE(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.FIREFOX_ESR) {
                    expectedAlerts = firstDefined(alerts.FF_ESR(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.FIREFOX) {
                    expectedAlerts = firstDefined(alerts.FF(), alerts.DEFAULT());
                }
                else if (browserVersion == BrowserVersion.CHROME) {
                    expectedAlerts = firstDefined(alerts.CHROME(), alerts.DEFAULT());
                }
            }
        }

        boolean realBrowser = false;
        if (webTestCase instanceof WebDriverTestCase) {
            realBrowser = ((WebDriverTestCase) webTestCase).useRealBrowser();
        }
        if (realBrowser) {
            final BuggyWebDriver buggyWebDriver = method.getAnnotation(BuggyWebDriver.class);
            if (buggyWebDriver != null) {
                if (isDefined(buggyWebDriver.value())) {
                    expectedAlerts = buggyWebDriver.value();
                }
                else {
                    if (browserVersion == BrowserVersion.EDGE) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.EDGE(), buggyWebDriver.DEFAULT());
                    }
                    else if (browserVersion == BrowserVersion.FIREFOX_ESR) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.FF_ESR(), buggyWebDriver.DEFAULT());
                    }
                    else if (browserVersion == BrowserVersion.FIREFOX) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.FF(), buggyWebDriver.DEFAULT());
                    }
                    else if (browserVersion == BrowserVersion.CHROME) {
                        expectedAlerts = firstDefinedOrGiven(expectedAlerts,
                                            buggyWebDriver.CHROME(), buggyWebDriver.DEFAULT());
                    }
                }
            }
        }
        else {
            final HtmlUnitNYI htmlUnitNYI = method.getAnnotation(HtmlUnitNYI.class);
            if (htmlUnitNYI != null) {
                if (browserVersion == BrowserVersion.EDGE) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.EDGE());
                }
                else if (browserVersion == BrowserVersion.FIREFOX_ESR) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF_ESR());
                }
                else if (browserVersion == BrowserVersion.FIREFOX) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.FF());
                }
                else if (browserVersion == BrowserVersion.CHROME) {
                    expectedAlerts = firstDefinedOrGiven(expectedAlerts, htmlUnitNYI.CHROME());
                }
            }
        }

        webTestCase.setExpectedAlerts(expectedAlerts);
    }


    /**
     * @param alerts the alerts to check
     * @return true if there is exactly one alert defined
     */
    public static boolean isDefined(final String[] alerts) {
        return alerts.length != 1 || !alerts[0].equals(EMPTY_DEFAULT);
    }

    private static String[] firstDefined(final String[]... variants) {
        for (final String[] var : variants) {
            if (isDefined(var)) {
                return var;
            }
        }
        return NO_ALERTS_DEFINED;
    }

    /**
     * @param given the default
     * @param variants variants
     * @return a string array containing the first defined value or the provided one
     */
    public static String[] firstDefinedOrGiven(final String[] given, final String[]... variants) {
        for (final String[] var : variants) {
            if (isDefined(var)) {
                try {
                    Assertions.assertArrayEquals(var, given);
                    Assertions.fail("BuggyWebDriver duplicates expectations");
                }
                catch (final AssertionFailedError e) {
                    // ok
                }
                return var;
            }
        }
        return given;
    }

}
