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
import java.util.Set;

import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.TestWithParameters;

import com.gargoylesoftware.htmlunit.runners.BrowserVersionClassRunner;
import com.gargoylesoftware.htmlunit.runners.BrowserVersionClassRunnerWithParameters;

/**
 * The custom runner <code>BrowserParameterizedRunner</code> combines the behavior of both
 * {@link com.gargoylesoftware.htmlunit.BrowserRunner} and {@link org.junit.runners.Parameterized}.
 *
 * It uses {@link org.junit.runners.Parameterized.Parameter} for field injection.
 *
 * You must define a single {@link Default} method, which has global
 * {@link com.gargoylesoftware.htmlunit.BrowserRunner.Alerts}.
 * You can add other specific tests, which will not be parameterized, and they can have other
 * {@link com.gargoylesoftware.htmlunit.BrowserRunner.Alerts} or
 * {@link com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented}.
 *
 * The method name will start with underscore '_' and have the parameters joined by an underscore.
 * If the method of a data entry already exists, then it will not be considered, as the actual method will override it.
 *
 * For example, you can have:
 * <pre>
 * &#064;RunWith(BrowserParameterizedRunner.class)
 * public class SomeTest extends WebDriverTestCase {
 *
 *    &#064;Parameters
 *    public static Iterable&lt;Object[]&gt; data() {
 *        return Arrays.asList(new Object[][] { { 0, 0 }, { 1, 1 }, { 2, 1 },
 *                 /&#042 will be overridden, see below &#042/ { 3, 2 },
 *                 { 4, 3 }, { 5, 5 }, { 6, 8 } });
 *    }
 *
 *    &#064;Parameter
 *    public int param1;
 *
 *    &#064;Parameter(1)
 *    public int param2;
 *
 *    &#064;Test
 *    &#064;Alerts("some alert")
 *    &#064;Default
 *    public void test() {
 *       loadPageWithAlerts2("some HTML with " + param1 + " " + param2);
 *    }
 *
 *
 *    /&#042&#042
 *     &#042 This method will override the <tt>{ 3, 2 }</tt> entry.
 *     &#042/
 *    &#064;Test
 *    &#064;Alerts("another alert")
 *    &#064;NotYetImplemented
 *    public void _3_2() {
 *       loadPageWithAlerts2("some HTML without the parameters, since it is not the &#064;Default");
 *    }
 *
 *    &#064;Test
 *    &#064;Alerts("another alert")
 *    &#064;NotYetImplemented
 *    public void anotherTest() {
 *       loadPageWithAlerts2("some HTML without the parameters, since it is not the &#064;Default");
 *    }
 * }
 * </pre>
 *
 * @author Ahmed Ashour
 */
public class BrowserParameterizedRunner extends Suite {

    /**
     * Annotation for a methods which is the default one to be executed for all parameters.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Default {
    }

    private final ArrayList<Runner> runners_ = new ArrayList<>();

    /**
     * Only called reflectively. Do not use programmatically.
     * @param klass the class
     * @throws Throwable if an error occurs
     */
    public BrowserParameterizedRunner(final Class<WebTestCase> klass) throws Throwable {
        super(klass, Collections.<Runner>emptyList());

        verifyDefaultMEthod();

        final Parameters parameters = getParametersMethod().getAnnotation(
                Parameters.class);

        final List<TestWithParameters> tests = createTestsForParameters(
                allParameters(), parameters.name());

        if (BrowserVersionClassRunner.containsTestMethods(klass)) {
            final Set<String> browsers = WebDriverTestCase.getBrowsersProperties();
            if (WebDriverTestCase.class.isAssignableFrom(klass)) {
                if (browsers.contains("chrome")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.CHROME, true, tests));
                }
                if (browsers.contains("ff38")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.FIREFOX_38, true, tests));
                }
                if (browsers.contains("ff45")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.FIREFOX_45, true, tests));
                }
                if (browsers.contains("ie")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.INTERNET_EXPLORER, true, tests));
                }
                if (browsers.contains("edge")) {
                    runners_.add(new BrowserVersionClassRunnerWithParameters(
                            klass, BrowserVersion.EDGE, true, tests));
                }
            }

            if (browsers.contains("hu-chrome")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.CHROME, false, tests));
            }
            if (browsers.contains("hu-ff38")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.FIREFOX_38, false, tests));
            }
            if (browsers.contains("hu-ff45")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.FIREFOX_45, false, tests));
            }
            if (browsers.contains("hu-ie")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.INTERNET_EXPLORER, false, tests));
            }
            if (browsers.contains("hu-edge")) {
                runners_.add(new BrowserVersionClassRunnerWithParameters(
                        klass, BrowserVersion.EDGE, false, tests));
            }
        }
        else {
            throw new IllegalStateException("No @Test method found");
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
            final Iterable<Object> allParameters, final String namePattern) {
        int i = 0;
        final List<TestWithParameters> children = new ArrayList<>();
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

    private void verifyDefaultMEthod() throws Exception {
        final List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(Default.class);
        if (methods.size() != 1) {
            throw new Exception("A single method with @Default must exist in class "
                + getTestClass().getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void filter(final Filter filter) throws NoTestsRemainException {
        boolean atLeastOne = false;
        for (final Runner runner : getChildren()) {
            try {
                if (runner instanceof Filterable) {
                    ((Filterable) runner).filter(filter);
                    atLeastOne = true;
                }
            }
            catch (final NoTestsRemainException e) {
                // nothing
            }
        }

        if (!atLeastOne) {
            throw new NoTestsRemainException();
        }
    }
}
