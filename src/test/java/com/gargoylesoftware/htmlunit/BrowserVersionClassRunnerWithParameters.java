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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * A runner.
 *
 * @version $Revision$
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
        final List<FrameworkMethod> methods = new ArrayList<>();
        for (final FrameworkMethod m : originalMethod) {
            final BrowserFrameworkMethod newMethod = new BrowserFrameworkMethod(m.getMethod());
            methods.add(newMethod);
        }

        FrameworkMethod defualtMethod = null;
        for (final FrameworkMethod m : methods) {
            if ("defaultTest".equals(m.getName())) {
                defualtMethod = m;
                break;
            }
        }
        for (int i = 0; i < tests_.size() - 1; i++) {
            methods.add(defualtMethod);
        }
        final Comparator<FrameworkMethod> comparator = new Comparator<FrameworkMethod>() {
            public int compare(final FrameworkMethod fm1, final FrameworkMethod fm2) {
                return fm1.getName().compareTo(fm2.getName());
            }
        };
        Collections.sort(methods, comparator);
        testMethods_ = methods;
        return testMethods_;
    }

    private int testIndex_ = 0;

    /**
     * {@inheritDoc}
     */
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        if ("defaultTest".equals(method.getName())) {
            final BrowserFrameworkMethod newMethod = (BrowserFrameworkMethod) method;
            newMethod.setParameters(tests_.get(testIndex_++).getParameters());
        }
        super.runChild(method, notifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String testName(final FrameworkMethod method) {
        System.out.println("Test name " + method);
        String className = method.getMethod().getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
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
        String methodName = method.getName();
        if (method instanceof BrowserFrameworkMethod) {
            methodName = ((BrowserFrameworkMethod) method).getParametersAsString();
        }
        if (!maven_) {
            return String.format("%s [%s]", methodName, browserString);
        }
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
}
