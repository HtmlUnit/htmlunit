/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to specify a range of browser, e.g. Firefox from version 38 to version 45.
 *
 * @author Ahmed Ashour
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface WebBrowser {

    /**
     * The browser name.
     * @return the browser name
     */
    BrowserName value();

    /**
     * The minimum version which supports this feature.
     * @return the minimum version
     */
    float minVersion() default 0;

    /**
     * The maximum version which supports this feature.
     * @return the maximum version
     */
    float maxVersion() default Float.MAX_VALUE;
}
