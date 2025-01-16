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

import static org.htmlunit.junit.annotation.TestedBrowser.CHROME;
import static org.htmlunit.junit.annotation.TestedBrowser.EDGE;
import static org.htmlunit.junit.annotation.TestedBrowser.FF;
import static org.htmlunit.junit.annotation.TestedBrowser.FF_ESR;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test as not yet working for a particular browser (default value is all).
 * This will cause a failure to be considered as success and a success as failure forcing
 * us to remove this annotation when a feature has been implemented even unintentionally.
 * @see TestedBrowser
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author cd alexndr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NotYetImplemented {

    /**
     * The browsers with which the case is not yet implemented.
     * @return the browsers
     */
    TestedBrowser[] value() default {
        EDGE, FF_ESR, FF, CHROME
    };

    /**
     * The operating systems with which the case is not yet implemented.
     * @return the operating systems
     */
    OS[] os() default {};
}
