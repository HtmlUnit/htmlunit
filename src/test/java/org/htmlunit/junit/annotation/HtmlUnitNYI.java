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

import static org.htmlunit.junit.SetExpectedAlertsBeforeTestExecutionCallback.EMPTY_DEFAULT;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the test produces different result when running with HtmlUnit.
 * @see TestedBrowser
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author cd alexndr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HtmlUnitNYI {

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
}
