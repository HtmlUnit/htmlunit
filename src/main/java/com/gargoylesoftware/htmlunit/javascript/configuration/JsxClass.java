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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark a Java class as JavaScript class.
 *
 * @author Ahmed Ashour
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JsxClass {

    /**
     * The DOM class (if any).
     * @return the DOM class
     */
    Class<?> domClass() default Object.class;

    /**
     * Is JavaScript Object.
     * @return is JavaScript Object
     */
    boolean isJSObject() default true;

    /**
     * Should be defined in only Standards Mode.
     * @return whether is defined in Standards Mode only
     */
    boolean isDefinedInStandardsMode() default true;

    /**
     * The class name.
     * @return the class name
     */
    String className() default "";

    /**
     * The {@link WebBrowser}s supported by this constant.
     * @return the {@link WebBrowser}s
     */
    WebBrowser[] browsers() default {
        @WebBrowser(CHROME),
        @WebBrowser(FF),
        @WebBrowser(IE),
        @WebBrowser(EDGE)
    };
}
