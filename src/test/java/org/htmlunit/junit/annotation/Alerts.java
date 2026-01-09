/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.htmlunit.junit.SetExpectedAlertsBeforeTestExecutionCallback.EMPTY_DEFAULT;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebDriverTestCase;

/**
 * Allows to express the expected alerts (i.e. the messages passed to the
 * window.alert function) for the different browsers for a unit test.
 * Expected alerts can be retrieved within a unit test with {@link SimpleWebTestCase#getExpectedAlerts()}
 * (resp. {@link WebDriverTestCase#getExpectedAlerts}) to be compared with the actual alerts but most of the time
 * utility functions like {@link SimpleWebTestCase#loadPageWithAlerts(String)}
 * (resp. {@link WebDriverTestCase#loadPageWithAlerts2(String)}) are used which do it
 * after having loaded the page.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author cd alexndr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Alerts {

    /**
     * Alerts that is used for all browsers (if defined, the other values are ignored).
     * @return the alerts
     */
    String[] value() default { EMPTY_DEFAULT };

    /**
     * Alerts for latest Edge.
     * @return the alerts
     */
    String[] EDGE() default { EMPTY_DEFAULT };

    /**
     * Alerts for latest Firefox.
     * @return the alerts
     */
    String[] FF() default { EMPTY_DEFAULT };

    /**
     * Alerts for Firefox ESR.
     * @return the alerts
     */
    String[] FF_ESR() default { EMPTY_DEFAULT };

    /**
     * Alerts for latest Chrome.
     * @return the alerts
     */
    String[] CHROME() default { EMPTY_DEFAULT };

    /**
     * The default alerts, if nothing more specific is defined.
     * @return the alerts
     */
    String[] DEFAULT() default { EMPTY_DEFAULT };
}
