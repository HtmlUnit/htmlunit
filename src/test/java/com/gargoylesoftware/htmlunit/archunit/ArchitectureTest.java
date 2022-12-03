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
package com.gargoylesoftware.htmlunit.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.lang.reflect.Executable;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * Architecture tests.
 *
 * @author Ronald Brill
 */
@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "com.gargoylesoftware.htmlunit", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    /**
     * Utility classes should be placed in 'com.gargoylesoftware.htmlunit.util'.
     */
    @ArchTest
    public static final ArchRule utilsPackageRule = classes()
        .that().haveNameMatching(".*Util.?")
        .should().resideInAPackage("com.gargoylesoftware.htmlunit.util");

    /**
     * Do not use awt if not really needed (because not available on android).
     */
    @ArchTest
    public static final ArchRule awtPackageRule = noClasses()
        .that()
            .doNotHaveFullyQualifiedName(
                    "com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.AwtRenderingBackend")
            .and().doNotHaveFullyQualifiedName(
                    "com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.AwtRenderingBackend$SaveState")
            .and().doNotHaveFullyQualifiedName(
                    "com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.AwtRenderingBackend$1")
            .and().doNotHaveFullyQualifiedName(
                    "com.gargoylesoftware.htmlunit.html.DoTypeProcessor")
            .and().doNotHaveFullyQualifiedName(
                    "com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration")
            .and().doNotHaveFullyQualifiedName("com.gargoylesoftware.htmlunit.html.applets.AppletContextImpl")
            .and().resideOutsideOfPackage("com.gargoylesoftware.htmlunit.platform..")
        .should().dependOnClassesThat().resideInAnyPackage("java.awt..");

    /**
     * JsxClasses are always in the javascript package.
     */
    @ArchTest
    public static final ArchRule jsxClassAnnotationPackages = classes()
            .that().areAnnotatedWith(JsxClass.class)
            .should().resideInAPackage("..javascript..");

    /**
     * Every JsxGetter with propertyName defined has to end in '_js'.
     *
     * AbstractJavaScriptConfiguration.process(ClassConfiguration, String, SupportedBrowser)
     * stores the value.
     */
    @ArchTest
    public static final ArchRule jsxConstant = fields()
            .that().areAnnotatedWith(JsxConstant.class)
            .should().haveModifier(JavaModifier.PUBLIC)
            .andShould().haveModifier(JavaModifier.STATIC)
            .andShould().haveModifier(JavaModifier.FINAL);

    /**
     * JsxGetter/Setter/Functions are always in the javascript package.
     */
    @ArchTest
    public static final ArchRule jsxAnnotationPackages = methods()
            .that().areAnnotatedWith(JsxGetter.class)
                    .or().areAnnotatedWith(JsxSetter.class)
                    .or().areAnnotatedWith(JsxFunction.class)
            .should().beDeclaredInClassesThat().resideInAPackage("..javascript..");

    /**
     * JsxGetter/Setter/Functions only valid in classes annotated as JsxClass.
     */
    @ArchTest
    public static final ArchRule jsxAnnotationJsxClass = methods()
            .that().areAnnotatedWith(JsxGetter.class)
                    .or().areAnnotatedWith(JsxSetter.class)
                    .or().areAnnotatedWith(JsxFunction.class)
            .should().beDeclaredInClassesThat().areAnnotatedWith(JsxClass.class)
            .orShould().beDeclaredInClassesThat().areAnnotatedWith(JsxClasses.class);

    /**
     * Every JsxGetter has to be named get... or is....
     */
    @ArchTest
    public static final ArchRule jsxGetterAnnotationStartsWithGet = methods()
            .that().areAnnotatedWith(JsxGetter.class)
            .should().haveNameStartingWith("get")
            .orShould().haveNameStartingWith("is");

    private static final DescribedPredicate<JavaMethod> haveJsxGetterWithNonDefaultPropertyName =
            new DescribedPredicate<JavaMethod>("@JsxGetter has a non default propertyName") {
                @Override
                public boolean test(final JavaMethod method) {
                    return StringUtils.isNotEmpty(method.getAnnotationOfType(JsxGetter.class).propertyName());
                }
            };

    /**
     * Every JsxGetter with propertyName defined has to end in '_js'.
     */
    @ArchTest
    public static final ArchRule jsxGetterAnnotationPostfix = methods()
            .that().areAnnotatedWith(JsxGetter.class)
            .and(haveJsxGetterWithNonDefaultPropertyName)
            .should().haveNameEndingWith("_js");

    /**
     * Every JsxGetter has to be named get... or is....
     */
    @ArchTest
    public static final ArchRule jsxSetterAnnotationStartsWithSet = methods()
            .that().areAnnotatedWith(JsxSetter.class)
            .should().haveNameStartingWith("set");

    private static final DescribedPredicate<JavaMethod> haveJsxSetterWithNonDefaultPropertyName =
            new DescribedPredicate<JavaMethod>("@JsxSetter has a non default propertyName") {
                @Override
                public boolean test(final JavaMethod method) {
                    return StringUtils.isNotEmpty(method.getAnnotationOfType(JsxSetter.class).propertyName());
                }
            };

    /**
     * Every JsxSetter with propertyName defined has to end in '_js'.
     */
    @ArchTest
    public static final ArchRule jsxSetterAnnotationPostfix = methods()
            .that().areAnnotatedWith(JsxSetter.class)
            .and(haveJsxSetterWithNonDefaultPropertyName)
            .should().haveNameEndingWith("_js");

    private static final ArchCondition<JavaMethod> hasMatchingGetter =
            new ArchCondition<JavaMethod>("every @JsxSetter needs a matching @JsxGetter") {
                @Override
                public void check(final JavaMethod method, final ConditionEvents events) {
                    String getterName = "g" + method.getName().substring(1);
                    if (method.getOwner().tryGetMethod(getterName).isPresent()) {
                        return;
                    }

                    getterName = "is" + method.getName().substring(3);
                    if (method.getOwner().tryGetMethod(getterName).isPresent()) {
                        return;
                    }

                    events.add(SimpleConditionEvent.violated(method,
                            "No matching JsxGetter found for " + method.getFullName()));
                }
            };

    /**
     * Every @JsxSetter needs a matching @JsxGetter.
     */
    @ArchTest
    public static final ArchRule setterNeedsMatchingGetter = methods()
            .that().areAnnotatedWith(JsxSetter.class)
            .should(hasMatchingGetter);

    /**
     * Do not overwrite toString for javascript, use jsToString and define the
     * functionName in the @JsxFunction annotation.
     */
    @ArchTest
    public static final ArchRule jsToString = methods()
            .that().areAnnotatedWith(JsxFunction.class)
            .should().haveNameNotMatching("toString");

    /**
     * Make sure to not use java.lang.reflect.Executable.
     */
    @ArchTest
    public static final ArchRule android7Executable = noClasses()
            .should().dependOnClassesThat().haveFullyQualifiedName(Executable.class.getName());

    /**
     * Make sure to not use {@link ThreadLocal#withInitial(java.util.function.Supplier)}.
     */
    @ArchTest
    public static final ArchRule android7ThreadLocalWithInitial = noClasses()
            .should().callMethod(ThreadLocal.class, "withInitial", Supplier.class);

    /**
     * Make sure to not use java.util.function.Supplier.
     */
    @ArchTest
    public static final ArchRule android6Supplier = noClasses()
            .should().dependOnClassesThat().haveFullyQualifiedName(Supplier.class.getName());

    /**
     * Make sure to not use Xerces.
     */
    @ArchTest
    public static final ArchRule xerces = noClasses()
        .that()
            .doNotHaveFullyQualifiedName(
                    "com.gargoylesoftware.htmlunit.util.XmlUtilsXercesHelper")
        .should().dependOnClassesThat().resideInAnyPackage("org.apache.xerces..");


    /**
     * Make sure to not use jdk - Xerces.
     */
    @ArchTest
    public static final ArchRule jdkXerces = noClasses()
        .that()
            .doNotHaveFullyQualifiedName(
                    "com.gargoylesoftware.htmlunit.util.XmlUtilsSunXercesHelper")
        .should().dependOnClassesThat().resideInAnyPackage("com.sun.org.apache.xerces..");
}
