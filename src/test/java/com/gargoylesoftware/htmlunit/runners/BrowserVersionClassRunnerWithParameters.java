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
package com.gargoylesoftware.htmlunit.runners;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.parameterized.TestWithParameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner.Default;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * A {@link BrowserVersionClassRunner} which is also parameterized.
 *
 * @author Ahmed Ashour
 */
public class BrowserVersionClassRunnerWithParameters extends BrowserVersionClassRunner {

    private final List<TestWithParameters> tests_;

    private List<FrameworkMethod> testMethods_;

    /**
     * The constructor.
     * @param klass the class
     * @param browserVersion the browser version
     * @param realBrowser whether to use real browser or not
     * @param tests the tests
     * @throws InitializationError if an error occurs
     */
    public BrowserVersionClassRunnerWithParameters(final Class<WebTestCase> klass,
            final BrowserVersion browserVersion, final boolean realBrowser,
            final List<TestWithParameters> tests)
        throws InitializationError {
        super(klass, browserVersion, realBrowser);
        tests_ = tests;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (testMethods_ != null) {
            return testMethods_;
        }
        final List<FrameworkMethod> originalMethod = super.computeTestMethods();
        if (tests_ == null) {
            return originalMethod;
        }
        final Set<String> nativeMethodNames = new HashSet<>();
        final List<FrameworkMethod> methods = new ArrayList<>();
        FrameworkMethod defualtMethod = null;
        for (final FrameworkMethod m : originalMethod) {
            final List<Object> parameters;
            if (m.getAnnotation(Default.class) != null) {
                defualtMethod = m;
                parameters = tests_.get(0).getParameters();
            }
            else {
                parameters = Collections.emptyList();
                nativeMethodNames.add(m.getName());
            }
            final FrameworkMethodWithParameters newMethod = new FrameworkMethodWithParameters(
                    getTestClass(), m.getMethod(), parameters);
            methods.add(newMethod);
        }

        for (int i = 0; i < tests_.size() - 1; i++) {
            final FrameworkMethodWithParameters method = new FrameworkMethodWithParameters(
                    getTestClass(), defualtMethod.getMethod(), tests_.get(i + 1).getParameters());
            methods.add(method);
        }

        for (final Iterator<FrameworkMethod> it = methods.iterator(); it.hasNext();) {
            final FrameworkMethod method = it.next();
            if (method.getAnnotation(Default.class) != null && nativeMethodNames.contains(method.getName())) {
                it.remove();
            }
        }

        final Comparator<FrameworkMethod> comparator = new Comparator<FrameworkMethod>() {
            @Override
            public int compare(final FrameworkMethod fm1, final FrameworkMethod fm2) {
                return fm1.getName().compareTo(fm2.getName());
            }
        };
        Collections.sort(methods, comparator);
        testMethods_ = methods;
        return testMethods_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String testName(final FrameworkMethod method) {
        String prefix = "";
        if (isNotYetImplemented(method) && !isRealBrowser()) {
            prefix = "(NYI) ";
        }
        else if (isRealBrowser() && isBuggyWebDriver(method)) {
            prefix = "(buggy) ";
        }

        String browserString = getBrowserVersion().getNickname();
        if (isRealBrowser()) {
            browserString = "Real " + browserString;
        }

        final String methodName = method.getName();

        if (!maven_) {
            return String.format("%s [%s]", methodName, browserString);
        }
        String className = method.getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return String.format("%s%s [%s]", prefix, className + '.' + methodName, browserString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateConstructor(final List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
        if (fieldsAreAnnotated()) {
            validateZeroArgConstructor(errors);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateFields(final List<Throwable> errors) {
        super.validateFields(errors);
        if (fieldsAreAnnotated()) {
            final List<FrameworkField> annotatedFieldsByParameter = getAnnotatedFieldsByParameter();
            final int[] usedIndices = new int[annotatedFieldsByParameter.size()];
            for (final FrameworkField each : annotatedFieldsByParameter) {
                final int index = each.getField().getAnnotation(Parameter.class)
                        .value();
                if (index < 0 || index > annotatedFieldsByParameter.size() - 1) {
                    errors.add(new Exception("Invalid @Parameter value: "
                            + index + ". @Parameter fields counted: "
                            + annotatedFieldsByParameter.size()
                            + ". Please use an index between 0 and "
                            + (annotatedFieldsByParameter.size() - 1) + "."));
                }
                else {
                    usedIndices[index]++;
                }
            }
            for (int index = 0; index < usedIndices.length; index++) {
                final int numberOfUse = usedIndices[index];
                if (numberOfUse == 0) {
                    errors.add(new Exception("@Parameter(" + index
                            + ") is never used."));
                }
                else if (numberOfUse > 1) {
                    errors.add(new Exception("@Parameter(" + index
                            + ") is used more than once (" + numberOfUse + ")."));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        return childrenInvoker(notifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Annotation[] getRunnerAnnotations() {
        return new Annotation[0];
    }

    private List<FrameworkField> getAnnotatedFieldsByParameter() {
        return getTestClass().getAnnotatedFields(Parameter.class);
    }

    private boolean fieldsAreAnnotated() {
        return !getAnnotatedFieldsByParameter().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Description describeChild(final FrameworkMethod method) {
        if (method.getAnnotation(Default.class) != null) {
            return Description.createTestDescription(getTestClass().getJavaClass(),
                    testName(method), method.getAnnotations());
        }
        return super.describeChild(method);
    }
}
