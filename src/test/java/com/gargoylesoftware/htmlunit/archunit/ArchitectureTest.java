/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Architecture tests.
 *
 * @author Ronald Brill
 */
@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "com.gargoylesoftware.htmlunit")
public class ArchitectureTest {

    /**
     * Utility classes should be placed in 'com.gargoylesoftware.htmlunit.util'.
     */
    @ArchTest
    public static final ArchRule utilsPackageRule = classes()
        .that().haveNameMatching(".*Util.?")
        .should().resideInAPackage("com.gargoylesoftware.htmlunit.util");

    /**
     * JsxClasses are always in the javascript package.
     */
    @ArchTest
    public static final ArchRule jsxClassAnnotationPackages = classes()
            .that().areAnnotatedWith(JsxClass.class)
            .should().resideInAPackage("..javascript..");

    /**
     * JsxGetter/Setter/Functions are always in the javascript package.
     */
    @ArchTest
    public static final ArchRule jsxGetterAnnotationPackages = methods()
            .that().areAnnotatedWith(JsxGetter.class)
                    .or().areAnnotatedWith(JsxSetter.class)
                    .or().areAnnotatedWith(JsxFunction.class)
            .should().beDeclaredInClassesThat().resideInAPackage("..javascript..");

    /**
     * JsxGetter/Setter/Functions only valid in classes annotated as JsxClass.
     */
    @ArchTest
    public static final ArchRule jsxGetterAnnotationJsxClass = methods()
            .that().areAnnotatedWith(JsxGetter.class)
                    .or().areAnnotatedWith(JsxSetter.class)
                    .or().areAnnotatedWith(JsxFunction.class)
            .should().beDeclaredInClassesThat().areAnnotatedWith(JsxClass.class)
            .orShould().beDeclaredInClassesThat().areAnnotatedWith(JsxClasses.class);
}
