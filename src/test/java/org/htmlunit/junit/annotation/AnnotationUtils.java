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
package org.htmlunit.junit.annotation;

import static org.junit.Assert.assertFalse;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.htmlunit.BrowserVersion;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserVersionClassRunner;

/**
 * Utility functions for working with our annotations.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class AnnotationUtils {

    /**
     * Disallow instantiation of this class.
     */
    private AnnotationUtils() {
        // Empty.
    }

    /**
     * Helper to validate defined annotations for various errors.
     *
     * @param method the method to validate
     */
    public static void assertAlerts(final Method method) {
        final Alerts alerts = method.getAnnotation(Alerts.class);
        if (alerts != null) {
            if (!BrowserVersionClassRunner.isDefined(alerts.value())) {
                assertFalse("Obsolete DEFAULT because all browser expectations are defined individually",
                        BrowserVersionClassRunner.isDefined(alerts.DEFAULT())
                        && BrowserVersionClassRunner.isDefined(alerts.CHROME())
                        && BrowserVersionClassRunner.isDefined(alerts.FF())
                        && BrowserVersionClassRunner.isDefined(alerts.FF_ESR())
                        && BrowserVersionClassRunner.isDefined(alerts.EDGE()));

                assertFalse("Obsolete DEFAULT because all browser expectations are defined individually",
                        BrowserVersionClassRunner.isDefined(alerts.DEFAULT())
                        && BrowserVersionClassRunner.isDefined(alerts.CHROME())
                        && BrowserVersionClassRunner.isDefined(alerts.FF())
                        && BrowserVersionClassRunner.isDefined(alerts.FF_ESR())
                        && BrowserVersionClassRunner.isDefined(alerts.EDGE()));

                assertNotEquals(method, BrowserVersion.EDGE, alerts.EDGE(), alerts.DEFAULT());
                assertNotEquals(method, BrowserVersion.CHROME, alerts.CHROME(), alerts.DEFAULT());
                assertNotEquals(method, BrowserVersion.FIREFOX, alerts.FF(), alerts.DEFAULT());
                assertNotEquals(method, BrowserVersion.FIREFOX_ESR, alerts.FF_ESR(), alerts.DEFAULT());
            }

            final HtmlUnitNYI nyiAlerts = method.getAnnotation(HtmlUnitNYI.class);
            if (nyiAlerts != null) {
                if (BrowserVersionClassRunner.isDefined(alerts.CHROME())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.CHROME())) {
                    assertNotEquals(method, BrowserVersion.CHROME, alerts.CHROME(), nyiAlerts.CHROME());
                }
                else if (BrowserVersionClassRunner.isDefined(alerts.DEFAULT())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.CHROME())) {
                    assertNotEquals(method, BrowserVersion.CHROME, alerts.DEFAULT(), nyiAlerts.CHROME());
                }

                if (BrowserVersionClassRunner.isDefined(alerts.FF_ESR())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.FF_ESR())) {
                    assertNotEquals(method, BrowserVersion.FIREFOX_ESR, alerts.FF_ESR(), nyiAlerts.FF_ESR());
                }
                else if (BrowserVersionClassRunner.isDefined(alerts.DEFAULT())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.FF_ESR())) {
                    assertNotEquals(method, BrowserVersion.FIREFOX_ESR, alerts.DEFAULT(), nyiAlerts.FF_ESR());
                }

                if (BrowserVersionClassRunner.isDefined(alerts.FF())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.FF())) {
                    assertNotEquals(method, BrowserVersion.FIREFOX, alerts.FF(), nyiAlerts.FF());
                }
                else if (BrowserVersionClassRunner.isDefined(alerts.DEFAULT())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.FF())) {
                    assertNotEquals(method, BrowserVersion.FIREFOX, alerts.DEFAULT(), nyiAlerts.FF());
                }

                if (BrowserVersionClassRunner.isDefined(alerts.EDGE())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.EDGE())) {
                    assertNotEquals(method, BrowserVersion.EDGE, alerts.EDGE(), nyiAlerts.EDGE());
                }
                else if (BrowserVersionClassRunner.isDefined(alerts.DEFAULT())
                        && BrowserVersionClassRunner.isDefined(nyiAlerts.EDGE())) {
                    assertNotEquals(method, BrowserVersion.EDGE, alerts.DEFAULT(), nyiAlerts.EDGE());
                }
            }
        }

        final AlertsStandards alerts2 = method.getAnnotation(AlertsStandards.class);
        if (alerts2 != null) {
            if (!BrowserVersionClassRunner.isDefined(alerts2.value())) {
                assertFalse("Obsolete DEFAULT because all browser expectations are defined individually",
                        BrowserVersionClassRunner.isDefined(alerts2.DEFAULT())
                        && BrowserVersionClassRunner.isDefined(alerts2.CHROME())
                        && BrowserVersionClassRunner.isDefined(alerts2.FF())
                        && BrowserVersionClassRunner.isDefined(alerts2.FF_ESR())
                        && BrowserVersionClassRunner.isDefined(alerts2.EDGE()));

                assertNotEquals(method, BrowserVersion.EDGE, alerts2.EDGE(), alerts2.DEFAULT());
                assertNotEquals(method, BrowserVersion.CHROME, alerts2.CHROME(), alerts2.DEFAULT());
                assertNotEquals(method, BrowserVersion.FIREFOX, alerts2.FF(), alerts2.DEFAULT());
                assertNotEquals(method, BrowserVersion.FIREFOX_ESR, alerts2.FF_ESR(), alerts2.DEFAULT());
            }
        }
    }

    private static void assertNotEquals(final Method method, final BrowserVersion browser,
            final String[] value1, final String[] value2) {
        if (value1.length != 0 && !BrowserRunner.EMPTY_DEFAULT.equals(value1[0])
                && value1.length == value2.length
                && Arrays.asList(value1).toString().equals(Arrays.asList(value2).toString())) {
            throw new AssertionError("Redundant alert for " + browser.getNickname() + " in "
                    + method.getDeclaringClass().getSimpleName() + '.' + method.getName() + "()");
        }
    }
}
