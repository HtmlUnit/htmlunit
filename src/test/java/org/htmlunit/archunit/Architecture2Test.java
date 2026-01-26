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
package org.htmlunit.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.htmlunit.BrowserVersion;
import org.htmlunit.junit.annotation.AnnotationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * Architecture tests for our test cases.
 *
 * @author Ronald Brill
 */
@AnalyzeClasses(packages = "org.htmlunit")
public class Architecture2Test {

    /**
     * All property test should test the same objects.
     * @param classes all classes
     */
    @ArchTest
    public static void allPropertyTestShouldTestTheSameObjects(final JavaClasses classes) {
        compare(classes, "ElementPropertiesTest", "ElementOwnPropertiesTest");
        compare(classes, "ElementPropertiesTest", "ElementOwnPropertySymbolsTest");
    }

    /**
     * All element test should test the same objects.
     * @param classes all classes
     */
    @ArchTest
    public static void allElementTestShouldTestTheSameObjects(final JavaClasses classes) {
        compare(classes, "ElementChildNodesTest", "ElementClosesItselfTest");
        compare(classes, "ElementChildNodesTest", "ElementCreationTest");
        compare(classes, "ElementChildNodesTest", "ElementDefaultStyleDisplayTest");
        compare(classes, "ElementChildNodesTest", "ElementOuterHtmlTest");
    }

    /**
     * All host test should test the same objects.
     * @param classes all classes
     */
    @ArchTest
    public static void allHostTestShouldTestTheSameObjects(final JavaClasses classes) {
        compare(classes, "HostClassNameTest", "HostTypeOfTest");
        compare(classes, "HostClassNameTest", "DedicatedWorkerGlobalScopeClassNameTest");
        compare(classes, "HostClassNameTest", "DedicatedWorkerGlobalScopeTypeOfTest");
    }

    private static void compare(final JavaClasses classes, final String oneName, final String anotherName) {
        final Set<String> oneTests =
                classes.get("org.htmlunit.general." + oneName).getAllMethods().stream()
                    .filter(m -> m.tryGetAnnotationOfType("org.junit.Test").isPresent())
                    .map(m -> m.getName())
                    .collect(Collectors.toSet());

        final Set<String> anotherTests =
                classes.get("org.htmlunit.general." + anotherName).getAllMethods().stream()
                    .filter(m -> m.tryGetAnnotationOfType("org.junit.Test").isPresent())
                    .map(m -> m.getName())
                    .collect(Collectors.toSet());

        final Set<String> tmp = new HashSet<>(anotherTests);
        tmp.removeAll(oneTests);
        oneTests.removeAll(anotherTests);

        if (tmp.size() + oneTests.size() > 0) {
            if (tmp.isEmpty()) {
                Assertions.fail("The " + oneTests.size() + " method(s) "
                    + oneTests.stream().sorted().toList()
                    + " are available in " + oneName + " but missing in " + anotherName + ".");
            }
            else if (oneTests.isEmpty()) {
                anotherTests.removeAll(oneTests);
                Assertions.fail("The " + tmp.size() + " method(s) "
                    + tmp.stream().sorted().toList()
                    + " are available in " + anotherName + " but missing in " + oneName + ".");
            }

            Assertions.fail("The " + tmp.size() + " method(s) "
                    + tmp.stream().sorted().toList()
                    + " are available in " + anotherName + " but missing in " + oneName
                    + " and the " + oneTests.size() + " method(s) "
                    + oneTests.stream().sorted().toList()
                    + " are available in " + oneName + " but missing in " + anotherName + ".");
        }
    }

    /**
     * Do not use BrowserVersion.isChrome().
     */
    @ArchTest
    public static final ArchRule isChrome = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.BrowserVersion")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$Chrome")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$ChromeAndEdge")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$ChromeAndEdgeAndFirefox")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$ChromeAndEdgeNotIterable")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.dom.Document")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.intl.DateTimeFormat")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.intl.NumberFormat")

        .should().callMethod(BrowserVersion.class, "isChrome");

    /**
     * Do not use BrowserVersion.isEdge().
     */
    @ArchTest
    public static final ArchRule isEdge = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.BrowserVersion")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$Edge")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$Chrome")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$ChromeAndEdge")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$ChromeAndEdgeAndFirefox")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$ChromeAndEdgeNotIterable")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.dom.Document")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.intl.DateTimeFormat")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.intl.NumberFormat")

        .should().callMethod(BrowserVersion.class, "isEdge");

    /**
     * Do not use BrowserVersion.isFirefox().
     */
    @ArchTest
    public static final ArchRule isFirefox = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.BrowserVersion")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.worker.DedicatedWorkerGlobalScope")

            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$FF")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$FFNotIterable")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$FFESR")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$FFLatest")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.css.BrowserConfiguration$ChromeAndEdgeAndFirefox")

            .and().doNotHaveFullyQualifiedName("org.htmlunit.general.huge.ElementClosesElementTest")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.general.huge.ElementClosesElement2Test")
        .should().callMethod(BrowserVersion.class, "isFirefox");

    /**
     * Do not use BrowserVersion.isFirefoxESR().
     */
    @ArchTest
    public static final ArchRule isFirefoxESR = noClasses()
        .that()
            .doNotHaveFullyQualifiedName("org.htmlunit.BrowserVersion")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.worker.DedicatedWorkerGlobalScope")

            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.intl.DateTimeFormat")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.intl.NumberFormat")
        .should().callMethod(BrowserVersion.class, "isFirefoxESR");

    /**
     * Do not use hamcrest.
     */
    @ArchTest
    public static final ArchRule hamcrest = noClasses()
        .should().dependOnClassesThat().resideInAPackage("org.hamcrest..");

    private static final ArchCondition<JavaMethod> haveConsistentTestAnnotations =
            new ArchCondition<>("have consistent HtmlUnit test annotations") {
                @Override
                public void check(final JavaMethod method, final ConditionEvents events) {
                    try {
                        AnnotationUtils.assertAlerts(method.reflect());
                    }
                    catch (final AssertionError e) {
                        events.add(SimpleConditionEvent.violated(method, e.getMessage()));
                    }
                }
            };

    /**
     * Validate test annotations.
     */
    @ArchTest
    public static final ArchRule consistentTestAnnotations = methods()
            .that().areAnnotatedWith(Test.class)
            .and().areNotDeclaredIn("org.htmlunit.junit.annotation.AnnotationUtilsTest")
            .should(haveConsistentTestAnnotations);

    /**
     * Do not use archunit outside of this.
     */
    @ArchTest
    public static final ArchRule archunitPackageRule = noClasses()
            .that()
                .resideOutsideOfPackage("org.htmlunit.archunit..")

            .should().dependOnClassesThat().resideInAnyPackage("com.tngtech.archunit..");

    /**
     * Do not use google commons.
     */
    @ArchTest
    public static final ArchRule googleCommonPackageRule = noClasses()
            .should().dependOnClassesThat().resideInAnyPackage("com.google.common..");

    /**
     * Do not use jetty.
     */
    @ArchTest
    public static final ArchRule jettyPackageRule = noClasses()
        .that()
            .resideOutsideOfPackage("org.htmlunit.archunit..")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.util.JettyServerUtils")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.util.JettyServerUtils$ConsoleErrorHandler")

            .and().doNotHaveFullyQualifiedName("org.htmlunit.WebServerTestCase")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.WebDriverTestCase")

            .and().doNotHaveFullyQualifiedName("org.htmlunit.HttpWebConnectionProxyTest")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.WebSocketTest$ChatWebSocketListener")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.WebSocketTest$CookiesWebSocketListener")
            .and().doNotHaveFullyQualifiedName("org.htmlunit.javascript.host.WebSocketTest$EventsWebSocketListener")
        .should()
            .dependOnClassesThat().resideInAnyPackage("org.eclipse.jetty..");
    }
