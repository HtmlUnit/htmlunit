/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.lang.reflect.Executable;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxClasses;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.junit.runner.RunWith;

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
@AnalyzeClasses(packages = "org.htmlunit", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    /**
     * Utility classes should be placed in 'org.htmlunit.util'.
     */
    @ArchTest
    public static final ArchRule utilsPackageRule = classes()
        .that().haveNameMatching(".*Util.?")
        .and().doNotHaveFullyQualifiedName("org.htmlunit.cssparser.util.ParserUtils")
        .and().doNotHaveFullyQualifiedName("org.htmlunit.http.HttpUtils")

        .and().doNotHaveFullyQualifiedName("org.htmlunit.platform.font.AwtFontUtil")
        .and().doNotHaveFullyQualifiedName("org.htmlunit.platform.font.FontUtil")
        .and().doNotHaveFullyQualifiedName("org.htmlunit.platform.font.NoOpFontUtil")

        .and().doNotHaveFullyQualifiedName("org.htmlunit.csp.Utils")

        .and().resideOutsideOfPackage("org.htmlunit.jetty.util..")
        .and().doNotHaveFullyQualifiedName("org.htmlunit.jetty.websocket.api.util.QuoteUtil")
        .and().doNotHaveFullyQualifiedName("org.htmlunit.jetty.websocket.common.util.ReflectUtils")
        .and().doNotHaveFullyQualifiedName("org.htmlunit.jetty.websocket.common.util.TextUtil")

        .should().resideInAPackage("org.htmlunit.util");

    /**
     * Do not use awt if not really needed (because not available on android).
     */
    @ArchTest
    public static final ArchRule awtPackageRule = noClasses()
        .that()
            .resideOutsideOfPackage("org.htmlunit.platform..")
            .and().resideOutsideOfPackage("org.htmlunit.corejs.javascript.tools..")
            .and().resideOutsideOfPackage("org.htmlunit.jetty..")
        .should().dependOnClassesThat().resideInAnyPackage("java.awt..");

    /**
     * JsxClasses are always in the javascript package.
     */
    @ArchTest
    public static final ArchRule jsxClassAnnotationPackages = classes()
            .that().areAnnotatedWith(JsxClass.class)
            .should().resideInAPackage("..javascript..");

    /**
     * Every JsxConstant should be public static final.
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
                    .or().areAnnotatedWith(JsxConstructor.class)
                    .or().areAnnotatedWith(JsxConstant.class)
            .should().beDeclaredInClassesThat().resideInAPackage("..javascript..");

    /**
     * JsxGetter/Setter/Functions only valid in classes annotated as JsxClass.
     */
    @ArchTest
    public static final ArchRule jsxAnnotationJsxClass = methods()
            .that().areAnnotatedWith(JsxGetter.class)
                    .or().areAnnotatedWith(JsxSetter.class)
                    .or().areAnnotatedWith(JsxFunction.class)
                    .or().areAnnotatedWith(JsxConstructor.class)
                    .or().areAnnotatedWith(JsxConstant.class)
            .should().beDeclaredInClassesThat().areAnnotatedWith(JsxClass.class)
            .orShould().beDeclaredInClassesThat().areAnnotatedWith(JsxClasses.class);

    /**
     * JsxConstants should not defined as short.
     */
    @ArchTest
    public static final ArchRule jsxConstantReturnType = fields()
            .that().areAnnotatedWith(JsxConstant.class)
            .should().notHaveRawType("short")
            .andShould().notHaveRawType("float");

    /**
     * JsxGetter/Setter/Functions should not return a short.
     */
    @ArchTest
    public static final ArchRule jsxAnnotationReturnType = methods()
            .that().areAnnotatedWith(JsxGetter.class)
                    .or().areAnnotatedWith(JsxSetter.class)
                    .or().areAnnotatedWith(JsxFunction.class)
            .should().notHaveRawReturnType("short")
            .andShould().notHaveRawReturnType("float");

    /**
     * JsxConstructor should not used for constructors.
     */
    @ArchTest
    public static final ArchRule jsxAnnotationJsxConstructor = constructors()
            .should().notBeAnnotatedWith(JsxConstructor.class);

    /**
     * JsxConstructor should have name jsConstructor.
     */
    @ArchTest
    public static final ArchRule jsxAnnotationJsxConstructorNaming = methods()
            .that().areAnnotatedWith(JsxConstructor.class)
            .should().haveName("jsConstructor");

    /**
     * JsxGetter/Setter/Functions should not use a short as parameter.
     */
    @ArchTest
    public static final ArchRule jsxAnnotationParameterType = methods()
            .that().areAnnotatedWith(JsxGetter.class)
                    .or().areAnnotatedWith(JsxSetter.class)
                    .or().areAnnotatedWith(JsxFunction.class)
            .should().notHaveRawParameterTypes("short")
            .andShould().notHaveRawParameterTypes("float");

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
     * Make sure to not use org.w3c.dom.traversal.TreeWalker.
     */
    @ArchTest
    public static final ArchRule androidTreeWalker = noClasses()
            .that()
                .doNotHaveFullyQualifiedName("org.htmlunit.html.HtmlDomTreeWalker")
                .and().doNotHaveFullyQualifiedName("org.htmlunit.platform.dom.traversal.DomTreeWalker")
                .and().doNotHaveFullyQualifiedName("org.htmlunit.SgmlPage")
            .should().dependOnClassesThat().haveFullyQualifiedName("org.w3c.dom.traversal.TreeWalker");

    /**
     * Make sure to not use org.w3c.dom.traversal.DocumentTraversal.
     */
    @ArchTest
    public static final ArchRule androidDocumentTraversal = noClasses()
            .should().dependOnClassesThat().haveFullyQualifiedName("org.w3c.dom.traversal.DocumentTraversal");

    /**
     * Make sure to not use javax.imageio.
     */
    @ArchTest
    public static final ArchRule androidRanges = noClasses()
        .should().dependOnClassesThat().resideInAnyPackage("org.w3c.dom.ranges..");


    /**
     * Make sure to not use javax.imageio.
     */
    @ArchTest
    public static final ArchRule androidImageio = noClasses()
         .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.platform.image.ImageIOImageData")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.platform.canvas.rendering.AwtRenderingBackend")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.platform.canvas.rendering.AwtRenderingBackend")
            .and().resideOutsideOfPackage("org.htmlunit.jetty..")
        .should().dependOnClassesThat().resideInAnyPackage("javax.imageio..");

    /**
     * Make sure to not use Xerces.
     */
    @ArchTest
    public static final ArchRule xerces = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.platform.util.XmlUtilsXercesHelper")
        .should().dependOnClassesThat().resideInAnyPackage("org.apache.xerces..");

    /**
     * Make sure to not use jdk - Xerces.
     */
    @ArchTest
    public static final ArchRule jdkXerces = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.platform.util.XmlUtilsSunXercesHelper")
        .should().dependOnClassesThat().resideInAnyPackage("com.sun.org.apache.xerces..");

    /**
     * Make sure the httpclient is only accessed from the adapter classes.
     */
    @ArchTest
    public static final ArchRule httpClient = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.HttpWebConnection")
            .and().areNotInnerClasses()
            .and().areNotMemberClasses()

            .and().doNotHaveFullyQualifiedName("org.htmlunit.WebClient")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.WebRequest")

            .and().resideOutsideOfPackage("org.htmlunit.httpclient..")
        .should().dependOnClassesThat().resideInAnyPackage("org.apache.http..");

    /**
     * Make sure the HttpWebConnection is the only entry into the HttpClient adapter.
     */
    @ArchTest
    public static final ArchRule httpWebConnection = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.HttpWebConnection")
            .and().areNotInnerClasses()
            .and().areNotMemberClasses()

            .and().doNotHaveFullyQualifiedName("org.htmlunit.WebClient")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.WebRequest")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.xml.XMLHttpRequest")

            .and().resideOutsideOfPackage("org.htmlunit.httpclient..")
        .should().dependOnClassesThat().resideInAnyPackage("org.htmlunit.httpclient..");

    /**
     * Do not use core-js dependencies outside of the adapter.
     */
    @ArchTest
    public static final ArchRule corejsPackageRule = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.WebConsole")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.WebConsole$1")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.ScriptException")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.html.DomElement")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.html.HtmlDialog")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.html.HtmlDialog$1")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.html.HtmlPage")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.util.WebClientUtils")

            .and().resideOutsideOfPackage("org.htmlunit.javascript..")

            .and().resideOutsideOfPackage("org.htmlunit.corejs..")
        .should().dependOnClassesThat().resideInAnyPackage("org.htmlunit.corejs..");

    /**
     * Do not use core-js ScriptRuntime outside of the JavaScriptEngine.
     */
    @ArchTest
    public static final ArchRule corejsScriptRuntimeRule = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.URLSearchParams")

            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.HtmlUnitContextFactory")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.JavaScriptEngine")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.JavaScriptEngine$3")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.regexp.HtmlUnitRegExpProxy")
            .and().resideOutsideOfPackage("org.htmlunit.corejs..")

        .should().dependOnClassesThat().haveFullyQualifiedName("org.htmlunit.corejs.javascript.ScriptRuntime");

    /**
     * Do not use core-js org.htmlunit.corejs.javascript.Undefined.instance directly.
     */
    @ArchTest
    public static final ArchRule corejsUndefinedRule = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.javascript.JavaScriptEngine")
            .and().resideOutsideOfPackage("org.htmlunit.corejs..")

        .should().dependOnClassesThat().haveFullyQualifiedName("org.htmlunit.corejs.javascript.Undefined");

    /**
     * Do not use jetty.
     */
    @ArchTest
    public static final ArchRule jettyPackageRule = noClasses()
        .should().dependOnClassesThat().resideInAnyPackage("org.eclipse.jetty..");
}
