/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.junit.runner.RunWith;

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
}
