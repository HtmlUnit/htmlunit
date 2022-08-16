/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark a Java class as JavaScript class.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(JsxClasses.class)
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
     * The class name.
     * @return the class name
     */
    String className() default "";

    /**
     * The {@link SupportedBrowser}s supported by this constant.
     * @return the {@link SupportedBrowser}s
     */
    SupportedBrowser[] value() default {
        CHROME,
        EDGE,
        FF,
        FF_ESR,
        IE
    };
}
