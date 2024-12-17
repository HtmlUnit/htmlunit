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

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.htmlunit.BrowserVersion;
import org.junit.Assert;
import org.junit.runner.RunWith;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Architecture tests for our test cases.
 *
 * @author Ronald Brill
 */
@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "org.htmlunit")
public class Architecture2Test {

    @ArchTest
    public static void allPropertyTestShouldTestTheSameObjects(final JavaClasses classes) {
        compare(classes, "ElementPropertiesTest", "ElementOwnPropertiesTest");
        compare(classes, "ElementPropertiesTest", "ElementOwnPropertySymbolsTest");
    }

    @ArchTest
    public static void allElementTestShouldTestTheSameObjects(final JavaClasses classes) {
        compare(classes, "ElementChildNodesTest", "ElementClosesItselfTest");
        compare(classes, "ElementChildNodesTest", "ElementCreationTest");
        compare(classes, "ElementChildNodesTest", "ElementDefaultStyleDisplayTest");
        compare(classes, "ElementChildNodesTest", "ElementOuterHtmlTest");
    }

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
            if (tmp.size() == 0) {
                Assert.fail("The " + oneTests.size() + " method(s) "
                    + oneTests.stream().sorted().collect(Collectors.toList())
                    + " are available in " + oneName + " but missing in " + anotherName + ".");
            }
            else if (oneTests.size() == 0) {
                anotherTests.removeAll(oneTests);
                Assert.fail("The " + tmp.size() + " method(s) "
                    + tmp.stream().sorted().collect(Collectors.toList())
                    + " are available in " + anotherName + " but missing in " + oneName + ".");
            }

            Assert.fail("The " + tmp.size() + " method(s) "
                    + tmp.stream().sorted().collect(Collectors.toList())
                    + " are available in " + anotherName + " but missing in " + oneName
                    + " and the " + oneTests.size() + " method(s) "
                    + oneTests.stream().sorted().collect(Collectors.toList())
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

        .should().callMethod(BrowserVersion.class, "isChrome", new Class[] {});

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

        .should().callMethod(BrowserVersion.class, "isEdge", new Class[] {});

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
        .should().callMethod(BrowserVersion.class, "isFirefox", new Class[] {});

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
        .should().callMethod(BrowserVersion.class, "isFirefoxESR", new Class[] {});


    /**
     * Do not use hamcrest.
     */
    @ArchTest
    public static final ArchRule hamcrest = noClasses()
        .should().dependOnClassesThat().resideInAPackage("org.hamcrest..");
}
