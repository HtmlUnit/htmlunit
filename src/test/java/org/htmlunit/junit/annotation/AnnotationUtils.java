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

import static org.htmlunit.junit.SetExpectedAlertsBeforeTestExecutionCallback.isDefined;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.htmlunit.BrowserVersion;
import org.htmlunit.junit.SetExpectedAlertsBeforeTestExecutionCallback;
import org.junit.jupiter.api.Assertions;

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
            if (!isDefined(alerts.value())) {
                Assertions.assertFalse(
                        isDefined(alerts.DEFAULT())
                        && isDefined(alerts.CHROME())
                        && isDefined(alerts.FF())
                        && isDefined(alerts.FF_ESR())
                        && isDefined(alerts.EDGE()),
                        "Obsolete DEFAULT because all browser expectations defined individually ("
                                + method.getDeclaringClass().getName() + "." + method.getName());

                assertNotEquals("@Alerts", method, BrowserVersion.CHROME, alerts.CHROME(), alerts.DEFAULT());
                assertNotEquals("@Alerts", method, BrowserVersion.FIREFOX, alerts.FF(), alerts.DEFAULT());
                assertNotEquals("@Alerts", method, BrowserVersion.FIREFOX_ESR, alerts.FF_ESR(), alerts.DEFAULT());
                assertNotEquals("@Alerts", method, BrowserVersion.EDGE, alerts.EDGE(), alerts.DEFAULT());
            }

            final HtmlUnitNYI nyiAlerts = method.getAnnotation(HtmlUnitNYI.class);
            if (nyiAlerts != null) {
                if (isDefined(nyiAlerts.CHROME())) {
                    if (isDefined(alerts.CHROME())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.CHROME, alerts.CHROME(), nyiAlerts.CHROME());
                    }
                    else if (isDefined(alerts.DEFAULT())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.CHROME, alerts.DEFAULT(), nyiAlerts.CHROME());
                    }
                    else if (isDefined(alerts.value())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.CHROME, alerts.value(), nyiAlerts.CHROME());
                    }
                }

                if (isDefined(nyiAlerts.FF_ESR())) {
                    if (isDefined(alerts.FF_ESR())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.FIREFOX_ESR, alerts.FF_ESR(), nyiAlerts.FF_ESR());
                    }
                    else if (isDefined(alerts.DEFAULT())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.FIREFOX_ESR, alerts.DEFAULT(), nyiAlerts.FF_ESR());
                    }
                    else if (isDefined(alerts.value())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.FIREFOX_ESR, alerts.value(), nyiAlerts.FF_ESR());
                    }
                }

                if (isDefined(nyiAlerts.FF())) {
                    if (isDefined(alerts.FF())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.FIREFOX, alerts.FF(), nyiAlerts.FF());
                    }
                    else if (isDefined(alerts.DEFAULT())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.FIREFOX, alerts.DEFAULT(), nyiAlerts.FF());
                    }
                    else if (isDefined(alerts.value())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.FIREFOX, alerts.value(), nyiAlerts.FF());
                    }
                }

                if (isDefined(nyiAlerts.EDGE())) {
                    if (isDefined(alerts.EDGE())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.EDGE, alerts.EDGE(), nyiAlerts.EDGE());
                    }
                    else if (isDefined(alerts.DEFAULT())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.EDGE, alerts.DEFAULT(), nyiAlerts.EDGE());
                    }
                    else if (isDefined(alerts.value())) {
                        assertNotEquals("@HtmlUnitNYI",
                                method, BrowserVersion.EDGE, alerts.value(), nyiAlerts.EDGE());
                    }
                }
            }
        }
    }

    private static void assertNotEquals(final String annotation, final Method method, final BrowserVersion browser,
            final String[] value1, final String[] value2) {
        if (value1.length != 0 && !SetExpectedAlertsBeforeTestExecutionCallback.EMPTY_DEFAULT.equals(value1[0])
                && value1.length == value2.length
                && Arrays.asList(value1).toString().equals(Arrays.asList(value2).toString())) {
            final String nickname = browser == null ? "DEFAULT" : browser.getNickname();
            throw new AssertionError("Redundant " + annotation + " for " + nickname + " in "
                    + method.getDeclaringClass().getSimpleName() + '.' + method.getName() + "()");
        }
    }
}
