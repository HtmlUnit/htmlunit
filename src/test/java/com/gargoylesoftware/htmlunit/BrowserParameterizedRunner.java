/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * Browser Parameterized Runner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class BrowserParameterizedRunner extends Suite {

    /**
     * Annotation for a method which provides parameters to be injected into the
     * test class constructor by <code>Parameterized</code>. The method has to
     * be public and static.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Parameters {
        /**
         * Optional pattern to derive the test's name from the parameters. Use
         * numbers in braces to refer to the parameters or the additional data
         * as follows:
         * <pre>
         * {index} - the current parameter index
         * {0} - the first parameter value
         * {1} - the second parameter value
         * etc...
         * </pre>
         * <p>
         * Default value is "{index}" for compatibility with previous JUnit
         * versions.
         *
         * @see MessageFormat
         */
        String name() default "{index}";
    }

    /**
     * Annotation for fields of the test class which will be initialized by the
     * method annotated by <code>Parameters</code>.
     * By using directly this annotation, the test class constructor isn't needed.
     * Index range must start at 0.
     * Default value is 0.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface Parameter {
        /**
         * Method that returns the index of the parameter in the array
         * returned by the method annotated by <code>Parameters</code>.
         * Index range must start at 0.
         * Default value is 0.
         */
        int value() default 0;
    }

    private final ArrayList<Runner> runners_ = new ArrayList<>();

    /**
     * Only called reflectively. Do not use programmatically.
     * @param klass the class
     * @throws Throwable if an error occurs
     */
    public BrowserParameterizedRunner(final Class<WebTestCase> klass) throws Throwable {
        super(klass, Collections.<Runner>emptyList());
        final Parameters parameters = getParametersMethod().getAnnotation(
                Parameters.class);

        final List<TestWithParameters> tests = createTestsForParameters(
                allParameters(), parameters.name());

        if (BrowserVersionClassRunner.containsTestMethods(klass)) {
            final List<String> browsers = WebDriverTestCase.getBrowsersProperties();
            if (/*browsers.contains("hu") ||*/ browsers.contains("hu-ff24")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.FIREFOX_24, false, tests));
            }
            if (browsers.contains("hu") || browsers.contains("hu-ff31")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.FIREFOX_31, false, tests));
            }
            if (browsers.contains("hu") || browsers.contains("hu-ie8")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.INTERNET_EXPLORER_8, false, tests));
            }
            if (/*browsers.contains("hu") ||*/ browsers.contains("hu-ie11")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.INTERNET_EXPLORER_11, false, tests));
            }
            if (browsers.contains("hu") || browsers.contains("hu-chrome")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.CHROME, false, tests));
            }

            if (WebDriverTestCase.class.isAssignableFrom(klass)) {
                if (browsers.contains("ff24")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.FIREFOX_24, true, tests));
                }
                if (browsers.contains("ff31")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.FIREFOX_31, true, tests));
                }
                if (browsers.contains("ie8")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.INTERNET_EXPLORER_8, true, tests));
                }
                if (browsers.contains("ie11")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.INTERNET_EXPLORER_11, true, tests));
                }
                if (browsers.contains("chrome")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.CHROME, true, tests));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<Runner> getChildren() {
        return runners_;
    }

    private TestWithParameters createTestWithNotNormalizedParameters(
            final String pattern, final int index, final Object parametersOrSingleParameter) {
        final Object[] parameters = (parametersOrSingleParameter instanceof Object[])
                ? (Object[]) parametersOrSingleParameter
                : new Object[] {parametersOrSingleParameter};
        return createTestWithParameters(getTestClass(), pattern, index,
                parameters);
    }

    @SuppressWarnings("unchecked")
    private Iterable<Object> allParameters() throws Throwable {
        final Object parameters = getParametersMethod().invokeExplosively(null);
        if (parameters instanceof Iterable) {
            return (Iterable<Object>) parameters;
        }
        else if (parameters instanceof Object[]) {
            return Arrays.asList((Object[]) parameters);
        }
        else {
            throw parametersMethodReturnedWrongType();
        }
    }

    private FrameworkMethod getParametersMethod() throws Exception {
        final List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(
                Parameters.class);
        for (final FrameworkMethod each : methods) {
            if (each.isStatic() && each.isPublic()) {
                return each;
            }
        }

        throw new Exception("No public static parameters method on class "
                + getTestClass().getName());
    }

    private List<TestWithParameters> createTestsForParameters(
            final Iterable<Object> allParameters, final String namePattern)
        throws Exception {
        int i = 0;
        final List<TestWithParameters> children = new ArrayList<TestWithParameters>();
        for (final Object parametersOfSingleTest : allParameters) {
            children.add(createTestWithNotNormalizedParameters(namePattern,
                    i++, parametersOfSingleTest));
        }
        return children;
    }

    private Exception parametersMethodReturnedWrongType() throws Exception {
        final String className = getTestClass().getName();
        final String methodName = getParametersMethod().getName();
        final String message = MessageFormat.format(
                "{0}.{1}() must return an Iterable of arrays.",
                className, methodName);
        return new Exception(message);
    }

    private static TestWithParameters createTestWithParameters(
            final TestClass testClass, final String pattern, final int index, final Object[] parameters) {
        final String finalPattern = pattern.replaceAll("\\{index\\}",
                Integer.toString(index));
        final String name = MessageFormat.format(finalPattern, parameters);
        return new TestWithParameters("[" + name + "]", testClass,
                Arrays.asList(parameters));
    }
}
